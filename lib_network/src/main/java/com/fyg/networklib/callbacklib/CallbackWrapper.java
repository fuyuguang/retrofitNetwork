package com.fyg.networklib.callbacklib;

import com.fyg.networklib.BaseResponse;
import com.fyg.networklib.IBusinessHandler;
import com.fyg.networklib.callbacklib.LifeCycleCallback.Callback;

/**
 * Created by fuyuguang on 2022/4/7 3:31 下午.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
 */
public  class CallbackWrapper<T>  {

    private final Callback mRealCallback;
    private final IBusinessHandler mIBusinessHandler;

    public void onStart() {
        if (mRealCallback != null) {
            mRealCallback.onStart();
        }
    }

    public void onCompleted() {
        if (mRealCallback != null) {
            mRealCallback.onCompleted();
        }
    }



    public CallbackWrapper(IBusinessHandler.Factory factory, LifeCycleCallback.Callback callback) {
        mRealCallback = callback;
        mIBusinessHandler = factory.getBusinessHandler();
    }


    public void onResponse(T data) {
        if (data instanceof BaseResponse) {
            if (mIBusinessHandler != null && mIBusinessHandler.isExpired((BaseResponse<?>) data)){
                mIBusinessHandler.tokenExpiredHandler((BaseResponse<?>) data);
                return;
            }

            if (mRealCallback != null) {
                BaseResponse apiResponse = (BaseResponse) data;
                if (data != null && apiResponse.isSucces()){
                    if (apiResponse.getResult() != null){
                        mRealCallback.onResponse(data);
                    }else{
                        if (!mRealCallback.onNullResult(data)){
                            if (mIBusinessHandler != null){
                                mIBusinessHandler.onNullResult((BaseResponse<?>) data);
                            }
                        }
                    }
                }else{
                    if (!mRealCallback.onError(data)){
                        if (mIBusinessHandler != null){
                            mIBusinessHandler.onError((BaseResponse<?>) data);
                        }
                    }
                }
            }
            return;
        }

        if (mRealCallback != null) {
            mRealCallback.onResponse(data);
        }
    }


    public void onFailure(Throwable throwable) {
        if (mRealCallback != null && !mRealCallback.onFailure(throwable)) {
            if (mIBusinessHandler != null){
                mIBusinessHandler.onFailure(throwable);
            }
        }
    }
}
