package com.fyg.networklib.result
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.fyg.networklib.Ktx
import com.fyg.networklib.NetWorkManager
import com.fyg.networklib.callbacklib.LifeCycleCallback.ILoadingView
import com.fyg.networklib.callbacklib.exception.AppException
import com.fyg.networklib.callbacklib.exception.ParseNetThrowable
import com.fyg.networklib.model.bean.ApiResponse
import com.fyg.networklib.result.Status.*

/**
 * Created by fuyuguang on 2022/4/19
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
    []()
    []()
    T 为 RootResultForRetrofit，脱 RootResultForRetrofit类型 用Resource

    State 适用于自定义数据类型，用State的参数化类型包裹服务端返回的业务数据
        例如：
        a1, retrofit 请求网络的接口如下：

            @GET("/home/recommend.htm")
            suspend fun getHomeRecommend(
            @Query("proIds") proIds: String = "1,2,3",
            @Query("pageIndex") pageIndex: Int = 1,
            @Query("pageSize") pageSize: Int = 20
            ): ApiResponse<ApiHomePagerResponse<ArrayList<HomeWineList>>>

        a2, ViewModel中定义的LiveData 参数化类型如下：
            val mHomeRecommendLiveDataWithState = MutableLiveData<State<ApiResponse<ApiHomePagerResponse<ArrayList<HomeWineList>>>>>()

        a3, activity 请求接口的方式

            例：NetCustomDataTypeActivity 中
            mViewModel.getHomeRecommendWithState(0,mViewModel.mHomeRecommendLiveDataWithState);


        a4, ViewModel中定义的LiveData，观察数据变更的方式有如下方式

            aa1,



        val uploadHeadPortraitLiveData = MutableLiveData<State<FileDescResult>>()
 */

 class CustomResource<T>(
     status: Status,
    val data: T?,
     message: String?,
     throwable: Throwable?
) :BaseResource(status,message,throwable) {

    companion object {
        fun <T> success(data: T): CustomResource<T> =
            CustomResource(status = SUCCESS, data = data, message = null, throwable = null)

        fun <T> error(data: T?, message: String, throwable: Throwable?): CustomResource<T> =
            CustomResource(status = ERROR, data = data, message = message, throwable = throwable)

        fun verifyError(message: String): CustomResource<*> =
            CustomResource(status = VERIFY_ERROR, data = null, message = message, throwable = null)

        fun <T> loadingStart(data: T?): CustomResource<T> =
            CustomResource(status = LOADING_START, data = data, message = null, throwable = null)

        fun <T> loadingEnd(data: T?): CustomResource<T> =
            CustomResource(status = LOADING_END, data = data, message = null, throwable = null)

    }


    fun parseResponseWithLoadingView(
        iLoadingView: ILoadingView?,
        onSuccess: T?.() -> Unit,
        @Suppress("unused") onError: ((AppException) -> Unit)? = null
    ) {
        when (status) {
            SUCCESS -> {
                /**
                由于RootResultForRetrofit.isCommunicationOk 需要接收 RootResultForRetrofit 类型，
                data 实际类型是 RootResultForRetrofit<CollectListResultInfo>,但编译时不识别，我们需要和他套上真实的类型，这样就可以调用isCommunicationOk 方法判断了
                把data 类型改为 （RootResultForRetrofit<T>）类型，
                data class Resource<T>(val status: Status,
                val data: RootResultForRetrofit<T>?,val message: String?,val throwable: Throwable?)
                这里没法判断 业务接口是否调用成功，需要脱一层外衣*/
//                if (RootResultForRetrofit.isCommunicationOk(data)) {
//                    onSuccess?.invoke(data!!.mData)
//                } else {
//                    onError?.invoke(AppException(data.mErrorCode, data.mErrorMsg))
//                }

                onSuccess?.invoke(data)
            }
            ERROR -> {
                val errorLog = ParseNetThrowable.parseThrowable(throwable).toString()
                NetWorkManager.getInstance().mNetConfig?.mShowToast?.apply {
                    showShort(errorLog)
                }
                if (Ktx.DEBUG) {
                    Log.d("HTTP", errorLog)
                }
                //onError?.invoke(AppException(ParseNetThrowable.parseThrowable(throwable),throwable))
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


    /** 泛型方法需要优化，去除 第二个多余的参数，  */
    fun <T> parseResponseWithLoadingView(
        iLoadingView: ILoadingView?,
        resource: CustomResource<ApiResponse<T>>,
        onSuccess: ((T?) -> Unit),
        onError: ((AppException) -> Unit)? = null
    ) {
        resource.let { resource ->
            when (resource.status) {
                SUCCESS -> {
                    resource.data?.apply {
                        if (this.isSucces()) {
                            onSuccess?.invoke(this.getResult())
                        } else {
                            onError?.invoke(
                                AppException(
                                    this.getResponseCode(),
                                    this.getResponseMsg()
                                )
                            )
                        }
                    }
                }
                ERROR -> {
                    NetWorkManager.getInstance().mNetConfig?.mShowToast?.apply {
                        showShort(ParseNetThrowable.parseThrowable(resource.throwable).toString())
                    }
                }
                VERIFY_ERROR -> {
                    NetWorkManager.getInstance().mNetConfig?.mShowToast?.apply {
                        showShort(resource.message)
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
    }


    /** 泛型方法需要优化，去除 第二个多余的参数，  */
    fun <T> parseResponse(
        resource: CustomResource<ApiResponse<T>>,
        uiState: MutableLiveData<BaseResource>?,
        onSuccess: ((T?) -> Unit),
        onError: ((AppException) -> Unit)? = null
    ) {
        resource.let { resource ->
            when (resource.status) {
                SUCCESS -> {
                    resource.data?.apply {
                        if (this.isSucces()) {
                            onSuccess?.invoke(this.getResult())
                        } else {
                            onError?.invoke(
                                AppException(
                                    this.getResponseCode(),
                                    this.getResponseMsg()
                                )
                            )
                        }
                    }
                }
                ERROR -> {
                    NetWorkManager.getInstance().mNetConfig?.mShowToast?.apply {
                        showShort(ParseNetThrowable.parseThrowable(resource.throwable).toString())
                    }
                }
                VERIFY_ERROR -> {
                    NetWorkManager.getInstance().mNetConfig?.mShowToast?.apply {
                        showShort(resource.message)
                    }
                }
                LOADING_START -> {
                    uiState?.value = resource
                }
                LOADING_END -> {
                    uiState?.value = resource
                }

            }
        }
    }



//    resource: CustomResource<ApiResponse<T>>,
//    uiState: MutableLiveData<BaseResource>?,
//    fun parseResponseWithUiState(uiState: MutableLiveData<CustomResource<*>>?, onSuccess: T?.() -> Unit,@Suppress("unused") onError: ((AppException) -> Unit)? = null) {
    fun parseResponseWithUiState(uiState: MutableLiveData<BaseResource>?, onSuccess: T?.() -> Unit,@Suppress("unused") onComplate: ((AppException) -> Unit)? = null) {
        when (status) {
            SUCCESS -> {
                onSuccess?.invoke(data)
            }
            ERROR -> {
                val errorLog = ParseNetThrowable.parseThrowable(throwable).toString()
                NetWorkManager.getInstance().mNetConfig?.mShowToast?.apply {
                    showShort(errorLog)
                }
                if (Ktx.DEBUG){
                    Log.d("HTTP",errorLog)
                }
                onComplate?.invoke(AppException(ParseNetThrowable.parseThrowable(throwable),throwable))
            }
            VERIFY_ERROR -> {
                NetWorkManager.getInstance().mNetConfig?.mShowToast?.apply {
                    showShort(message)
                }
                onComplate?.invoke(AppException(ParseNetThrowable.parseThrowable(throwable),throwable))
            }
            LOADING_START ,LOADING_END-> {
                uiState?.value = this
            }
        }
    }



    fun parseResponseWithUiState2(resource: CustomResource<T>,uiState: MutableLiveData<BaseResource>?, onSuccess: CustomResource<T>?.() -> Unit,@Suppress("unused") onComplate: ((AppException) -> Unit)? = null) {
        when (status) {
            SUCCESS -> {
                onSuccess?.invoke(this@CustomResource)
            }
            ERROR -> {
                val errorLog = ParseNetThrowable.parseThrowable(throwable).toString()
                NetWorkManager.getInstance().mNetConfig?.mShowToast?.apply {
                    showShort(errorLog)
                }
                if (Ktx.DEBUG){
                    Log.d("HTTP",errorLog)
                }
                onComplate?.invoke(AppException(ParseNetThrowable.parseThrowable(throwable),throwable))
            }
            VERIFY_ERROR -> {
                NetWorkManager.getInstance().mNetConfig?.mShowToast?.apply {
                    showShort(message)
                }
                onComplate?.invoke(AppException(ParseNetThrowable.parseThrowable(throwable),throwable))
            }
            LOADING_START ,LOADING_END-> {
                uiState?.value = this
            }
        }
    }
}
