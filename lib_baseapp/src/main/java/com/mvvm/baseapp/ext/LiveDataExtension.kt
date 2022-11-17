package com.mvvm.baseapp.ext
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.fyg.networklib.callbacklib.LifeCycleCallback.ILoadingView
import com.fyg.networklib.callbacklib.exception.AppException
import com.fyg.networklib.result.CustomResource
import com.fyg.networklib.result.Resource

/**
 * Created by fuyuguang on 2022/9/9 10:16 上午.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
 */

//inline fun <reified T > MutableLiveData<T>.observer1(owner : LifecycleOwner, observer: Observer<in T>,noinline success:  ((T?) -> Unit),noinline error : (AppException) -> Unit){
//inline fun <reified T > MutableLiveData<Resource<T>>.observer1(owner : LifecycleOwner, observer: Observer<in Resource<T>>, noinline success:  ((T?) -> Unit),noinline error : (AppException) -> Unit){


/** 在activity中观察数据，传统方式  */
fun <T> LifecycleOwner.observe(liveData: LiveData<T>, action: (t: T) -> Unit) {
    liveData.observe(this, Observer {
        it?.let {
                t ->
            action(t) }
    })
}

/** 在activity中观察酒仙后台数据 */
inline fun <reified T> LifecycleOwner.observerResource(
    liveData: LiveData<Resource<T>>,
    noinline success: ((T?) -> Unit),
    noinline error: ((AppException) -> Unit)? = null
) {
    liveData.observe(this) {
        it.parseResponse(success, error)
    }
}

/** 在activity中观察自定义数据 */
inline fun <reified T> LifecycleOwner.observerCustomResource(
    liveData: LiveData<CustomResource<T>>,
    noinline success: ((T?) -> Unit),
    noinline error: ((AppException) -> Unit)? = null,
) {
    liveData.observe(this) {
        it.parseResponseWithLoadingView(null, success, error)
    }
}


/** LiveData观察酒仙数据，传统方式  */
inline fun <reified T> MutableLiveData<Resource<T>>.observerResource(
    owner: LifecycleOwner,
    noinline success: ((T?) -> Unit),
    noinline error: ((AppException) -> Unit)? = null
) {
    observe(owner) {
        it.parseResponse(success, error)
    }
}



@Deprecated("该方式已经存在，请使用有3个参数的重载",
    ReplaceWith("observe(owner) { it.parseResponse(iLoadingView, success, error) }"),
)
inline fun <reified T> MutableLiveData<Resource<T>>.observerResource(
    owner: LifecycleOwner,
    iLoadingView: ILoadingView? = null,
    noinline success: ((T?) -> Unit),
    noinline error: ((AppException) -> Unit)? = null
) {
    observe(owner) {
        it.parseResponseWithLoadingView(iLoadingView, success, error)
    }
}




/** LiveData观察自定义数据，对传统方式的包装 */
inline fun <reified T> MutableLiveData<CustomResource<T>>.observerCustomResource(
    owner: LifecycleOwner,
    noinline success: ((T?) -> Unit),
    noinline error: ((AppException) -> Unit)? = null,
) {
    observe(owner) {
        it.parseResponseWithLoadingView(null, success, error)
    }
}



/** LiveData观察自定义数据，对传统方式的包装 参数with this  */
inline fun <reified T> MutableLiveData<CustomResource<T>>.observerCustomResourceWithThis(
    owner: LifecycleOwner,
    noinline success: T?.() -> Unit,
    noinline error: ((AppException) -> Unit)? = null,
) {
    observe(owner) {
        it.parseResponseWithLoadingView(null, success, error)
    }
}





