package com.mvvm.baseapp

import androidx.lifecycle.MutableLiveData
import com.fyg.networklib.BaseResponse
import com.fyg.networklib.result.CustomResource
import com.fyg.networklib.result.Resource
import com.fyg.networklib.result.Status.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.Closeable
import kotlin.coroutines.CoroutineContext

/**
 * Created by fuyuguang on 2022/11/10 9:15 PM.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
 */
abstract class IDataSource {

     var mCoroutineScope =  CloseableCoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
     fun onCleared() {
         mCoroutineScope.close()
    }

     class CloseableCoroutineScope(context: CoroutineContext) : Closeable, CoroutineScope {
        override val coroutineContext: CoroutineContext = context
        override fun close() {
            coroutineContext.cancel()
        }
    }


    val customResourceAction: suspend (CustomResource<*>?, MutableLiveData<*>?, MutableLiveData<CustomResource<*>>?) -> Unit = { resource, liveData, loadingState ->
        when (resource?.status) {
            SUCCESS, ERROR -> {
                liveData?.value = resource
            }
            LOADING_START, LOADING_END, VERIFY_ERROR -> {
                loadingState?.value = resource
            }
        }
    }


    fun <T> Flow<BaseResponse<T>>.asResourceFlow(): Flow<Resource<T>>  {
        return this
            .map {
                Resource.success(it)
            }
            .onStart {
                emit(Resource.loadingStart(data = null))
            }
            .onCompletion {
                this.emit(Resource.loadingEnd(data = null))
            }
            .flowOn(Dispatchers.Default)
            .catch { cause ->
                /** 当无网时，onCompletion  会执行，但livedate 收不到 Resource.loadingEnd(data = null) 事件，原因待排查，先在此处重发  */
                this.emit(Resource.loadingEnd(data = null))
                emit(
                    Resource.error(
                        data = null,
                        message = cause?.message ?: "Error Occurred",
                        throwable = cause
                    )
                )
            }
    }

    fun <T> Flow<T>.asResourceFlow2(): Flow<Resource<T>> where T : BaseResponse<*> {
        return this
            .map {
                Resource.success(it as BaseResponse<T>)
            }
            .onStart {
                emit(Resource.loadingStart(data = null))
            }
            .onCompletion {
                this.emit(Resource.loadingEnd(data = null))
            }
            .flowOn(Dispatchers.Default)
            .catch { cause ->
                /** 当无网时，onCompletion  会执行，但livedate 收不到 Resource.loadingEnd(data = null) 事件，原因待排查，先在此处重发  */
                this.emit(Resource.loadingEnd(data = null))
                emit(
                    Resource.error(
                        data = null,
                        message = cause?.message ?: "Error Occurred",
                        throwable = cause
                    )
                )
            }
    }



    fun <T> Flow<T>.asCustomResourceFlow(): Flow<CustomResource<T>>  {
        return this
            .map {
                CustomResource.success(it)
            }
            .onStart {
                emit(CustomResource.loadingStart(data = null))
            }
            .onCompletion {
                this.emit(CustomResource.loadingEnd(data = null))
            }
            .flowOn(Dispatchers.Default)
            .catch { cause ->
                this.emit(CustomResource.loadingEnd(data = null))
                emit(
                    CustomResource.error(
                        data = null,
                        message = cause?.message ?: "Error Occurred",
                        throwable = cause
                    )
                )
            }
    }


    /** 流式请求非酒仙api  */
     fun <T> loadDataWithCustomResourceFlow2(
        flow : Flow<T>,
        liveData: MutableLiveData<*>,
        loadingState: MutableLiveData<CustomResource<*>>?,
    ) /*where T : BaseResponse<*>,E : Resource<T>*/ {
        mCoroutineScope.launch {
            flow.asCustomResourceFlow().collect {
                customResourceAction(it,liveData,loadingState)
            }
        }
    }

    companion object DataSourceFactory{
        @JvmStatic
        inline fun <reified T : IDataSource>  createDataSource(t:T) : IDataSource{
            return T::class.java.newInstance()
        }
    }



    /**
     *  T 可指代 BaseResponse<T>
     *  <T> createResourceFlow(t: suspend () -> BaseResponse<T>): Flow<BaseResponse<T>>
     *  */
    fun <T> createFlow(t: suspend () -> T): Flow<T> {
        return flow {
            emit(t())
        }.cancellable()
    }


}
