package com.fyg.networklib.result
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.fyg.networklib.BaseResponse
import com.fyg.networklib.Ktx
import com.fyg.networklib.NetWorkManager
import com.fyg.networklib.callbacklib.LifeCycleCallback.ILoadingView
import com.fyg.networklib.callbacklib.exception.AppException
import com.fyg.networklib.callbacklib.exception.ParseNetThrowable
import com.fyg.networklib.result.Status.*

/**
 * Created by fuyuguang on 2022/4/20 11:16 上午.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
    []()
    []()
    包装 RootResultForRetrofit 中的具体业务数据 T

    Jx 返回的通用网络数据格式
 */
 class Resource<T> constructor(
     status: Status,
    val data: BaseResponse<T>?,
     message: String?,
     throwable: Throwable?
) :BaseResource(status,message,throwable){

    init {

    }

    fun parseResponse(
        onSuccess: ((T?) -> Unit),
        onError: ((AppException) -> Unit)? = {
            NetWorkManager.getInstance().mNetConfig?.mShowToast?.showShort(data?.getResponseMsg())
        }
    ){
        parseResponseWithLoadingView(null,onSuccess,onError)
    }

    /*inline*/ fun parseResponseWithLoadingView(
        iLoadingView: ILoadingView? = null,
        onSuccess: ((T?) -> Unit),
        onError: ((AppException) -> Unit)? = {
            /** 提供默认值，如果开发需要自定义，需复写  */
            NetWorkManager.getInstance().mNetConfig?.mShowToast?.showShort(data?.getResponseMsg())
        }
    ) {
        when (status) {
            SUCCESS -> {
                if (data != null){
                    if (data.isSucces()){
                        onSuccess?.invoke(data.getResult())
                    }else{
                        onError?.invoke(AppException(data.getResponseCode(), data.getResponseMsg()))
                    }
                }else{
                    NetWorkManager.getInstance().mNetConfig?.mShowToast?.showShort("Resource data  is null")
                }
            }

            ERROR -> {
                //JXToast.showShort(ParseNetThrowable.parseThrowable(throwable).toString())
                //onError?.invoke(AppException(ParseNetThrowable.parseThrowable(throwable),throwable))
                val errorLog = ParseNetThrowable.parseThrowable(throwable).toString();
                NetWorkManager.getInstance().mNetConfig?.mShowToast?.showShort(errorLog)

                if (Ktx.DEBUG){
                    Log.d("HTTP",errorLog)
                }
            }
            VERIFY_ERROR -> {
                NetWorkManager.getInstance().mNetConfig?.mShowToast?.apply {
                    showShort(message)
                }
            }
            LOADING_START -> {
                iLoadingView?.showLoadingDialog()
            }
            LOADING_END -> {
                iLoadingView?.dismissLoadingDialog()
            }
        }
    }


/*inline*/ fun  parseResponseWithUiState(
    uiState: MutableLiveData<BaseResource>?, onSuccess: ((T?) -> Unit),
    onError: ((AppException) -> Unit)? = {
            /** 提供默认值，如果开发需要自定义，需复写  */
            NetWorkManager.getInstance().mNetConfig?.mShowToast?.showShort(data?.getResponseMsg())
        }
    ) {
        when (status) {
            SUCCESS -> {
                if (data != null){
                    if (data.isSucces()){
                        onSuccess?.invoke(data.getResult())
                    }else{
                        onError?.invoke(AppException(data.getResponseCode(), data.getResponseMsg()))
                    }
                }else{
                    NetWorkManager.getInstance().mNetConfig?.mShowToast?.showShort("Resource data  is null")
                }
            }
            ERROR -> {
                val errorLog = ParseNetThrowable.parseThrowable(throwable).toString();
                NetWorkManager.getInstance().mNetConfig?.mShowToast?.showShort(errorLog)
                if (Ktx.DEBUG){
                    Log.d("HTTP",errorLog)
                }
            }
            VERIFY_ERROR -> {
                NetWorkManager.getInstance().mNetConfig?.mShowToast?.apply {
                    showShort(message)
                }
            }
            LOADING_START ,LOADING_END -> {
                uiState?.value = this
            }
        }
    }



    companion object {
        fun <T> success(data: BaseResponse<T>?): Resource<T> =
            Resource(status = SUCCESS, data = data, message = null, throwable = null)
        fun <T> error(data: BaseResponse<T>?, message: String, throwable: Throwable): Resource<T> =
            Resource(status = ERROR, data = data, message = message, throwable = throwable)
        fun verifyError(message: String): Resource<*> =
            Resource<String>(status = VERIFY_ERROR, data = null, message = message, throwable = null)
        fun <T> loadingStart(data: BaseResponse<T>?): Resource<T> =
            Resource(status = LOADING_START, data = data, message = null, throwable = null)
        fun <T> loadingEnd(data: BaseResponse<T>?): Resource<T> =
            Resource(status = LOADING_END, data = data, message = null, throwable = null)

    }
}


















