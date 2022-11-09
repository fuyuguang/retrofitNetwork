package com.fyg.networklib.callbacklib;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.alibaba.fastjson.JSON;
import com.fyg.networklib.Ktx;
import com.fyg.networklib.cache.ApiETagCacheManager;
import com.fyg.networklib.cache.ApiResponseCacheManager;
import com.fyg.networklib.callbacklib.exception.HttpError;
import com.fyg.networklib.model.bean.ApiResponse;
import com.fyg.networklib.model.bean.RequestTag;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.Executor;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.SkipCallbackExecutor;

/**
 * Created by fuyuguang on 2022/3/28 6:02 下午.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
 * []()
 * []()
 */
public final class LifeCycleCallAdapterFactory extends CallAdapter.Factory {
    public static final CallAdapter.Factory INSTANCE = new LifeCycleCallAdapterFactory();


    static boolean isAnnotationPresent(Annotation[] annotations, Class<? extends Annotation> cls) {
        for (Annotation annotation : annotations) {
            if (cls.isInstance(annotation)) {
                return true;
            }
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public @Nullable
    CallAdapter<?, ?> get(
            Type returnType, Annotation[] annotations, Retrofit retrofit) {
        if (getRawType(returnType) != LifeCycleCallback.class) {
            return null;
        }
        if (!(returnType instanceof ParameterizedType)) {
            throw new IllegalStateException(
                    "LifeCycleCallback return type must be parameterized"
                            + " as LifeCycleCallback<Foo> or LifeCycleCallback<? extends Foo>");
        }
        Type innerType = getParameterUpperBound(0, (ParameterizedType) returnType);
        final Executor executor = isAnnotationPresent(annotations, SkipCallbackExecutor.class) ? null : retrofit.callbackExecutor();
        if (getRawType(innerType) != Response.class) {

            return new CallAdapter<Object, LifeCycleCallback<?, ?>>() {

                @Override
                public Type responseType() {
                    return innerType;
                }

                @Override
                public LifeCycleCallback<?, ?> adapt(Call<Object> call) {
                    return new RealLifecycleCallbackImp(executor, call, false,getRawType(innerType),innerType);
                }
            };
        }

        // Generic type is Response<T>. Extract T and create the Response version of the adapter.
        if (!(innerType instanceof ParameterizedType)) {
            throw new IllegalStateException(
                    "Response must be parameterized" + " as Response<Foo> or Response<? extends Foo>");
        }
        Type responseType = getParameterUpperBound(0, (ParameterizedType) innerType);

        return new CallAdapter<Object, LifeCycleCallback<?, ?>>() {

            @Override
            public Type responseType() {
                return responseType;
            }

            @Override
            public LifeCycleCallback<?, ?> adapt(Call<Object> call) {
                return new RealLifecycleCallbackImp(executor, call, true,getRawType(responseType),responseType);
            }
        };
    }

}



class RealLifecycleCallbackImp extends RealLifecycleCallback {
    private final Class mRawType;
    private final Type mInnerType;
    private boolean mRawTypeIsResponse;
    private Executor mExecutor;

    RealLifecycleCallbackImp(Executor executor, Call call, boolean rawTypeIsResponse,Class<?> returnType,Type innerType) {
        super(call);
        mRawTypeIsResponse = rawTypeIsResponse;
        mRawType = returnType;
        mInnerType = innerType;
        mExecutor = executor;
        if (mExecutor == null) {
            mExecutor = command -> {
                if (command != null) {
                    command.run();
                }
            };
        }
    }


    @Override
    public void enqueue(LifecycleOwner source, final Callback realCallback) {
        CallbackWrapper callbackWrapper = new CallbackWrapper(realCallback,realCallback);
        if (source != null) {
            source.getLifecycle().addObserver(this);
        }
        mExecutor.execute(() -> {
            if (callbackWrapper != null) {
                callbackWrapper.onStart();
            }
        });
        call.enqueue(new retrofit2.Callback() {
            @Override
            public void onResponse(Call call, Response response) {

                ApiResponse cacheData = saveCache(response);
                mExecutor.execute(() -> {

                    try{
                        if (response.isSuccessful()) {
                            if (callbackWrapper != null) {
                                if (mRawTypeIsResponse) {
                                    callbackWrapper.onResponse(response);
                                } else {
                                    if (response.body() != null) {
                                        callbackWrapper.onResponse(response.body());
                                    } else  {
                                        callbackWrapper.onFailure(new HttpError("response.body() == null"));
                                    }
                                }
                            }
                        } else if (response.code() == 304){
                            if (callbackWrapper != null) {
                                if (mRawTypeIsResponse) {
                                } else {
                                    callbackWrapper.onResponse(cacheData);
                                }
                            }
                        }else{
                            if (callbackWrapper != null) {
                                callbackWrapper.onFailure(new HttpException(response));
                            }
                        }
                    }catch (Exception e){
                        if (Ktx.Companion.getDEBUG()){
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Ktx.app, HttpError.getExceptionInfo(e),Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{
                            if (callbackWrapper != null){
                                callbackWrapper.onFailure(e);
                            }
                        }
                    }finally {
                        if (callbackWrapper != null) {
                            callbackWrapper.onCompleted();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                mExecutor.execute(() -> {
                    if (callbackWrapper != null) {
                        callbackWrapper.onFailure(t);
                        callbackWrapper.onCompleted();
                    }
                });
            }
        });


    }

    private ApiResponse saveCache(Response response)  {
        Request request = response.raw().request();
        RequestTag requestTag = request.tag(RequestTag.class);
        if (response.code() == 200){
            if (mRawTypeIsResponse) {

            } else {
                ApiResponse apiResponse = (ApiResponse) response.body();
                if (apiResponse != null && requestTag != null && !TextUtils.isEmpty(apiResponse.getEtag())){
                    ApiETagCacheManager.getInstance().save(Ktx.app, requestTag.url,apiResponse.getEtag());
                    ApiResponseCacheManager.getInstance().save(Ktx.app, requestTag.url,JSON.toJSONString(response.body()));
                    return apiResponse;
                }
            }
        }else if (response.code() == 304){

            if (mRawTypeIsResponse) {

            } else {
                if (requestTag != null && !TextUtils.isEmpty(requestTag.url) &&  ApiResponse.class == mRawType){
                    String cacheData = ApiResponseCacheManager.getInstance().get(Ktx.app, requestTag.url);
                    if (!TextUtils.isEmpty(cacheData)){
                        if (mInnerType instanceof ParameterizedType) {
                            ApiResponse realResponse = JSON.parseObject(cacheData, mInnerType);
                            return realResponse;
                        }
                    }
                }
            }
        }
        return null;

    }

}

abstract class RealLifecycleCallback<T, R> implements LifeCycleCallback<T, LifeCycleCallback.ILoadingView> {


    protected final Call<?> call;

    RealLifecycleCallback(Call<?> call) {
        this.call = call;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
    }

    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {

        if (source.getLifecycle().getCurrentState().compareTo(Lifecycle.State.DESTROYED) == 0) {
            if (!call.isCanceled()) {
                call.cancel();
            }
            source.getLifecycle().removeObserver(this);
        }
    }
}
