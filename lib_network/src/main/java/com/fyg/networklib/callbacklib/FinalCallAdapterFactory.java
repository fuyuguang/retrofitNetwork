package com.fyg.networklib.callbacklib;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.fyg.networklib.BaseResponse;
import com.fyg.networklib.Ktx;
import com.fyg.networklib.cache.ApiETagCacheManager;
import com.fyg.networklib.cache.ApiResponseCacheManager;
import com.fyg.networklib.model.bean.RequestTag;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;
import java.util.concurrent.Executor;

import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response.Builder;
import okhttp3.internal.Util;
import okio.Timeout;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.SkipCallbackExecutor;

public final class FinalCallAdapterFactory extends CallAdapter.Factory {

    public static final CallAdapter.Factory INSTANCE = new FinalCallAdapterFactory(null);

    private final @Nullable
    Executor callbackExecutor;

    FinalCallAdapterFactory(@Nullable Executor callbackExecutor) {
        this.callbackExecutor = callbackExecutor;
    }

    @Override
    public @Nullable
    CallAdapter<?, ?> get(
            Type returnType, Annotation[] annotations, Retrofit retrofit) {
        if (getRawType(returnType) != Call.class) {
            return null;
        }
        if (!(returnType instanceof ParameterizedType)) {
            throw new IllegalArgumentException(
                    "Call return type must be parameterized as Call<Foo> or Call<? extends Foo>");
        }
        final Type responseType = getParameterUpperBound(0, (ParameterizedType) returnType);

        final Executor executor =
                isAnnotationPresent(annotations, SkipCallbackExecutor.class)
                        ? null
                        : callbackExecutor;

        return new CallAdapter<Object, Call<?>>() {
            @Override
            public Type responseType() {
                return responseType;
            }

            @Override
            public Call<Object> adapt(Call<Object> call) {
                return new ExecutorCallbackCall<>(executor, call, getRawType(responseType), responseType);
            }
        };
    }

    static final class ExecutorCallbackCall<T> implements Call<T> {
        final Executor callbackExecutor;
        final Call<T> delegate;
        private final Class mRawType;
        private final Type mInnerType;

        ExecutorCallbackCall(Executor callbackExecutor, Call<T> delegate, Class<?> returnType, Type innerType) {
            this.callbackExecutor = callbackExecutor;
            this.delegate = delegate;
            mRawType = returnType;
            mInnerType = innerType;
        }

        @Override
        public void enqueue(final Callback<T> callback) {
            Objects.requireNonNull(callback, "callback == null");

            delegate.enqueue(
                    new Callback<T>() {
                        @Override
                        public void onResponse(Call<T> call, final Response<T> response) {
                            if (callbackExecutor != null) {
                                callbackExecutor.execute(
                                        () -> {
                                            if (delegate.isCanceled()) {
                                                // Emulate OkHttp's behavior of throwing/delivering an IOException on
                                                // cancellation.
                                                callback.onFailure(ExecutorCallbackCall.this, new IOException("Canceled"));
                                            } else {
                                                callback.onResponse(ExecutorCallbackCall.this, response);
                                            }
                                        });
                            } else {
                                if (delegate.isCanceled()) {
                                    // Emulate OkHttp's behavior of throwing/delivering an IOException on
                                    // cancellation.
                                    callback.onFailure(ExecutorCallbackCall.this, new IOException("Canceled"));
                                } else {
                                    BaseResponse cacheData = saveCache(response);
                                    if (response.code() == 304 && cacheData != null) {
                                        callback.onResponse(ExecutorCallbackCall.this, (Response<T>) Response.success(cacheData, new Builder()
                                                .request(response.raw().request())
                                                .protocol(Protocol.HTTP_1_1)
                                                .code(200)
                                                .headers(response.raw().networkResponse().headers())
                                                .message("OK")
                                                .body(Util.EMPTY_RESPONSE)
                                                .build()));
                                    } else {
                                        callback.onResponse(ExecutorCallbackCall.this, response);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<T> call, final Throwable t) {
                            if (callbackExecutor != null) {
                                callbackExecutor.execute(() -> callback.onFailure(ExecutorCallbackCall.this, t));
                            } else {
                                callback.onFailure(ExecutorCallbackCall.this, t);
                            }
                        }
                    });
        }

        @Override
        public boolean isExecuted() {
            return delegate.isExecuted();
        }

        @Override
        public Response<T> execute() throws IOException {
            return delegate.execute();
        }

        @Override
        public void cancel() {
            delegate.cancel();
        }

        @Override
        public boolean isCanceled() {
            return delegate.isCanceled();
        }

        @SuppressWarnings("CloneDoesntCallSuperClone") // Performing deep clone.
        @Override
        public Call<T> clone() {
            return new ExecutorCallbackCall<>(callbackExecutor, delegate.clone(), mRawType, mInnerType);
        }

        @Override
        public Request request() {
            return delegate.request();
        }

        @Override
        public Timeout timeout() {
            return delegate.timeout();
        }


        private BaseResponse saveCache(Response response) {
            Request request = response.raw().request();
            RequestTag requestTag = request.tag(RequestTag.class);
            if (response.code() == 200) {
                if (response.body() instanceof BaseResponse){
                    BaseResponse apiResponse = (BaseResponse) response.body();
                    if (apiResponse != null && requestTag != null && !TextUtils.isEmpty(apiResponse.getETag())) {
                        ApiETagCacheManager.getInstance().save(Ktx.app, requestTag.url, apiResponse.getETag());
                        ApiResponseCacheManager.getInstance().save(Ktx.app, requestTag.url, JSON.toJSONString(response.body()));
                        return apiResponse;
                    }
                }
            } else if (response.code() == 304) {
                if (requestTag != null && !TextUtils.isEmpty(requestTag.url) && BaseResponse.class.isAssignableFrom(mRawType)) {
                    String cacheData = ApiResponseCacheManager.getInstance().get(Ktx.app, requestTag.url);
                    if (!TextUtils.isEmpty(cacheData)) {
                        if (mInnerType instanceof ParameterizedType) {
                            BaseResponse realResponse = JSON.parseObject(cacheData, mInnerType);
                            return realResponse;
                        }
                    }
                }
            }
            return null;

        }

    }


    public static boolean isAnnotationPresent(Annotation[] annotations, Class<? extends Annotation> cls) {
        for (Annotation annotation : annotations) {
            if (cls.isInstance(annotation)) {
                return true;
            }
        }
        return false;
    }
}
