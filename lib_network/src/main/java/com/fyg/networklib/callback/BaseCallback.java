package com.fyg.networklib.callback;


import com.fyg.networklib.IBusinessHandler;
import com.fyg.networklib.callbacklib.LifeCycleCallback;
import com.fyg.networklib.callbacklib.LifeCycleCallback.Callback;
import com.fyg.networklib.callbacklib.LifeCycleCallback.ILoadingView;
import com.fyg.networklib.model.bean.ApiResponse;

/**
 * Created by fuyuguang on 2022/4/7 3:31 下午.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
 */
public  class BaseCallback<T> implements LifeCycleCallback.Callback<T, LifeCycleCallback.ILoadingView> {

    private final Callback<T, ILoadingView> mRealCallback;

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



    @Override
    public IBusinessHandler getBusinessHandler(){
        return BusinessHandler.getInstance();
    }

    public BaseCallback(LifeCycleCallback.Callback<T, LifeCycleCallback.ILoadingView> callback) {
        mRealCallback = callback;
    }

    @Override
    public void onResponse(T data) {
        if (data instanceof ApiResponse) {
            if (ApiResponse.Companion.isTokenDated((ApiResponse<?>) data)) {
//                UserInfoManager.userTokenDatedHandle((BaseResponse<?>) data);
                return;
            }
        }

        if (mRealCallback != null) {
            mRealCallback.onResponse(data);
        }
    }

    @Override
    public boolean onNullResult(T data) {
        return false;
    }


    @Override
    public boolean onFailure(Throwable throwable) {
        if (mRealCallback != null) {
            return mRealCallback.onFailure(throwable);
        }
        return false;
    }

    @Override
    public boolean onError(T data) {
        if (mRealCallback != null) {
            return mRealCallback.onError(data);
        }
        return false;
    }
}
