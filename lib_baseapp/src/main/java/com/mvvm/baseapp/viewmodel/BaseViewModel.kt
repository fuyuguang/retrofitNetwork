package com.mvvm.baseapp.viewmodel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fyg.networklib.BaseResponse
import com.fyg.networklib.model.bean.ApiResponse
import com.fyg.networklib.result.BaseResource
import com.fyg.networklib.result.CustomResource
import com.fyg.networklib.result.Resource
import com.fyg.networklib.result.Status.*
import com.mvvm.baseapp.IDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.reflect.ParameterizedType

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/12
 * 描述　: ViewModel的基类 使用ViewModel类，放弃AndroidViewModel，原因：用处不大 完全有其他方式获取Application上下文
 */
//open abstract class BaseViewModel(var dataSource : IDataSource) : ObservableViewModel() {
open abstract class BaseViewModel<T : IDataSource> : ObservableViewModel() {

     val mDataSource : T

    init {
        mDataSource = createDataSource()
    }

    private fun createDataSource(): T {

        val clazz  = (this.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<T>
        return clazz.newInstance();
//        return ViewModelProvider(this).get(getVmClazz(this))

    }

//    abstract  fun createDataSource(): IDataSource?
//    abstract dataSource : IDataSource;

//    lateinit var mDataSource
//    public constructor(var dataSource : IDataSource){
//        mDataSource =  dataSource;
//    }

//    val mRepo = BaseRepoImp();
//    abstract val mRepo

    val mUiStatus = MutableLiveData<BaseResource>()

    val actionResource: suspend (Resource<*>, MutableLiveData<*>, MutableLiveData<BaseResource>?) -> Unit = { resource, liveData, loadingState ->
        when (resource.status) {
            SUCCESS, ERROR -> {
                liveData.value = resource
            }
            LOADING_START, LOADING_END -> {
                loadingState?.value = resource
            }
        }
    }


    val actionCustomResource2: suspend (CustomResource<*>, MutableLiveData<*>, MutableLiveData<BaseResource>?) -> Unit = { resource, liveData, loadingState ->
        when (resource.status) {
            SUCCESS, ERROR -> {
                liveData.value = resource
            }
            LOADING_START, LOADING_END,VERIFY_ERROR -> {
                loadingState?.value = resource
            }
        }
    }

    /**
     * BaseViewModel.loadDataWithData 的方式， mViewModel.loadDataWithData()不能找到该方法
     * 用非酒仙后台的数据格式请求
     * */
    @kotlin.Deprecated("使用loadDataWithCustomResource 代替")
    inline fun <T> loadDataWithCustomResourceOld(liveData :MutableLiveData<*>, crossinline invokeMethod :suspend BaseViewModel<*>.()-> T, loadingState : MutableLiveData<BaseResource>? = this.mUiStatus){

        viewModelScope.launch {
            flow {
                emit(CustomResource.success(invokeMethod()))
            }.onStart {
                emit(CustomResource.loadingStart(data = null))
            }.onCompletion {
                this.emit(CustomResource.loadingEnd(data = null))
            }.flowOn(Dispatchers.Default).catch { cause ->
                this.emit(CustomResource.error(data = null, message = cause?.message ?: "Error Occurred",throwable = cause))
            }.collect {
                actionCustomResource2(it,liveData,loadingState)
            }
        }
    }

    /**
     * 该方式 已废弃， 使用loadDataWithResource 方法更好,
     * 解析数据更方便
     *  */
    @kotlin.Deprecated("使用loadDataWithCustomResource 代替")
    inline fun <T> loadDataWithCustomResourceAndApiResponse(liveData :MutableLiveData<*>, crossinline invokeMethod :suspend BaseViewModel<*>.()-> ApiResponse<T>, loadingState : MutableLiveData<BaseResource>? = this.mUiStatus){
        viewModelScope.launch {
            flow {
                emit(CustomResource.success(invokeMethod()))
            }.onStart {
                emit(CustomResource.loadingStart(data = null))
            }.onCompletion {
                this.emit(CustomResource.loadingEnd(data = null))
            }.flowOn(Dispatchers.Default).catch { cause ->
                this.emit(CustomResource.error(data = null, message = cause?.message ?: "Error Occurred",throwable = cause))
            }.collect {
                actionCustomResource2(it,liveData,loadingState)
            }
        }
    }



    @kotlin.Deprecated("使用loadDataWithResource 代替")
    inline fun <T> loadDataWithResourceOld(liveData :MutableLiveData<*>, crossinline invokeMethod :suspend BaseViewModel<*>.()-> ApiResponse<T>, loadingState : MutableLiveData<BaseResource>? = this.mUiStatus){

        viewModelScope.launch {
            flow {
                emit(Resource.success(invokeMethod()))
            }.onStart {
                emit(Resource.loadingStart(data = null))
            }.onCompletion {
                this.emit(Resource.loadingEnd(data = null))
            }.flowOn(Dispatchers.Default).catch { cause ->
                this.emit(Resource.error(data = null, message = cause?.message ?: "Error Occurred",throwable = cause))
            }.collect {
                actionResource(it,liveData,loadingState)
            }
        }
    }





    /** 适合单个酒仙的api接口  */
    @Deprecated("已过期，请使用 loadDataWithResourceFlow 代替")
    fun <T> loadDataWithResource(
        liveData: MutableLiveData<*>,
        invokeMethod: suspend () -> BaseResponse<T>,
        loadingState: MutableLiveData<BaseResource>? = this.mUiStatus
    ) {
        viewModelScope.launch {
            createFlow(invokeMethod).asResourceFlow().collect {
                actionResource(it,liveData,loadingState)
            }
        }
    }


    private fun <T> createResourceFlow(t: suspend () -> BaseResponse<T>): Flow<BaseResponse<T>> {
        return flow {
            emit(t())
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


      fun <T> Flow<T>.asResourceFlow(): Flow<Resource<T>> where T : BaseResponse<*> {
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


    @Deprecated("已经过期，请使用 loadDataWithCustomResourceFlow 代替")
    /** 适合单个非酒仙的api接口  */
    fun <T> loadDataWithCustomResource(
        liveData: MutableLiveData<*>,
        invokeMethod: suspend () -> T,
        loadingState: MutableLiveData<BaseResource>? = this.mUiStatus
    ) {
        viewModelScope.launch {
            createFlow(invokeMethod).asCustomResourceFlow().collect {
                actionCustomResource2(it,liveData,loadingState)
            }
        }
    }


    /** 流式请求酒仙api，  */
     inline fun <T> loadDataWithResourceFlow(
        liveData: MutableLiveData<*>,
        flow : Flow<T>,
        loadingState: MutableLiveData<BaseResource>? = this.mUiStatus,
    ) where T : BaseResponse<*> {
        viewModelScope.launch {
            flow.asResourceFlow().collect {
                actionResource(it,liveData,loadingState)
            }
        }
    }

    /** 流式请求酒仙api，  */
    inline fun <T> loadDataWithResourceFlow(
        liveData: MutableLiveData<*>,
        flow : () -> Flow<T>,
    ) where T : BaseResponse<*> {
        loadDataWithResourceFlow(liveData,flow.invoke(),this.mUiStatus)
    }


    /** 流式请求非酒仙api  */
    @Deprecated("已经过期，请使用 loadDataWithCustomResourceFlow2 代替")
    inline fun <T> loadDataWithCustomResourceFlow(
        flow : Flow<T>,
        liveData: MutableLiveData<*>,
        loadingState: MutableLiveData<BaseResource>? = this.mUiStatus,
    ) /*where T : BaseResponse<*>,E : Resource<T>*/ {
        viewModelScope.launch {
            flow.asCustomResourceFlow().collect {

                actionCustomResource2(it,liveData,loadingState)
            }
        }
    }


    /** 流式请求非酒仙api  */
    inline fun <T> loadDataWithCustomResourceFlow2(
        flow : Flow<T>,
        liveData: MutableLiveData<*>,
        loadingState: MutableLiveData<BaseResource>? = this.mUiStatus,
    ) /*where T : BaseResponse<*>,E : Resource<T>*/ {
        viewModelScope.launch {
            flow.asCustomResourceFlow().collect {

                actionCustomResource2(it,liveData,loadingState)
            }
        }
    }


    /** 流式请求非酒仙api  */
    @Deprecated("已经过期，请使用 loadDataWithCustomResourceFlow2 代替")
    inline fun <T> loadDataWithCustomResourceFlow(
        liveData: MutableLiveData<*>,
        flow : () -> Flow<T>,
    ) {
        loadDataWithCustomResourceFlow(flow.invoke(),liveData,mUiStatus)
    }

    inline fun <T> loadDataWithCustomResourceFlow2(
        liveData: MutableLiveData<*>,
        flow : () -> Flow<T>,
    ) {
        loadDataWithCustomResourceFlow2(flow.invoke(),liveData,mUiStatus)
    }



    override fun onCleared() {
        super.onCleared()
        mDataSource.onCleared()
    }
}


