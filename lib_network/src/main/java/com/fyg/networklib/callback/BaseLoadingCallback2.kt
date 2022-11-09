package com.fyg.networklib.callback
import com.fyg.networklib.IBusinessHandler
import com.fyg.networklib.callbacklib.LifeCycleCallback.Callback
import com.fyg.networklib.callbacklib.LifeCycleCallback.ILoadingView

/**
 * Created by fuyuguang on 2022/4/7 3:37 下午.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
 * []()
 * []()
 *
 */
abstract class BaseLoadingCallback2<T>(private val mLoadingView: ILoadingView?) : Callback<T?, ILoadingView?> {
    override fun onStart() {
        mLoadingView?.showLoadingDialog()
    }

    override fun onCompleted() {
        mLoadingView?.dismissLoadingDialog()
    }

    override fun getBusinessHandler(): IBusinessHandler {
        return BusinessHandler.getInstance()
    }

    override fun onFailure(throwable: Throwable): Boolean {
        return false
    }

    override fun onError(data: T?): Boolean {
        return false
    }
}