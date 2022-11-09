package com.fyg.networklib.callbacklib;

import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import com.fyg.networklib.IBusinessHandler;

import retrofit2.Callback;

/**
 * Created by fuyuguang on 2022/4/2 6:03 下午.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
    []()
    []()

 */

public interface LifeCycleCallback<T,R extends LifeCycleCallback.ILoadingView> extends Callback<T>, LifecycleEventObserver {

    void enqueue(LifecycleOwner source,Callback<T,R> callback);


    interface ILoadingView {
         void showLoadingDialog();
         void dismissLoadingDialog();
    }

    public static interface Callback<T,R> extends IBusinessHandler.Factory {
        void onResponse(T data);

        boolean onNullResult(T data);

        boolean onFailure(Throwable throwable);

        boolean onError(T data);

        void onStart();

        void onCompleted();

        IBusinessHandler getBusinessHandler();

    }
}