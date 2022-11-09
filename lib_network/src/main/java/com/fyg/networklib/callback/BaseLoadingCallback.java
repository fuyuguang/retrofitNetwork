package com.fyg.networklib.callback;

import com.fyg.networklib.IBusinessHandler;
import com.fyg.networklib.callbacklib.LifeCycleCallback;

/**
 * Created by fuyuguang on 2022/4/7 3:37 下午.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
    []()
    []()
 */

public abstract class BaseLoadingCallback<T> implements LifeCycleCallback.Callback<T, LifeCycleCallback.ILoadingView> {

    private LifeCycleCallback.ILoadingView mLoadingView;

    public BaseLoadingCallback(LifeCycleCallback.ILoadingView r) {
        mLoadingView = r;
    }

    public void onStart() {
        if (mLoadingView != null) {
            mLoadingView.showLoadingDialog();
        }
    }

    public void onCompleted() {
        if (mLoadingView != null) {
            mLoadingView.dismissLoadingDialog();
        }
    }

    @Override
    public IBusinessHandler getBusinessHandler(){
        return BusinessHandler.getInstance();
    }


    @Override
    public boolean onFailure(Throwable throwable) {
        return false;
    }

    @Override
    public boolean onError(T data) {
        return false;
    }

    @Override
    public boolean onNullResult(T data) {
        return false;
    }
}