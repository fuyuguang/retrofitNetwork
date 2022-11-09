package com.fyg.networklib.retrofit

import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.fyg.networklib.BaseNetworkApi
import com.fyg.networklib.Ktx
import com.fyg.networklib.NetWorkManager
import com.fyg.networklib.callbacklib.FinalCallAdapterFactory
import com.fyg.networklib.callbacklib.LifeCycleCallAdapterFactory
import com.fyg.networklib.converter.DataConverterFactory
import com.fyg.networklib.interceptor.HttpHeadInterceptor
import com.fyg.networklib.interceptor.OfflineCacheInterceptor
import com.fyg.networklib.interceptor.OnlineCacheInterceptor
import com.fyg.networklib.interceptor.RequestParameterInterceptor
import com.fyg.networklib.util.ContextUtil
import com.fyg.networklib.util.NetWorkKey
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.io.IOException
import java.net.Proxy
import java.net.ProxySelector
import java.net.SocketAddress
import java.net.URI
import java.util.*
import java.util.concurrent.TimeUnit.MILLISECONDS


////双重校验锁式-单例 封装NetApiService 方便直接快速调用简单的接口
//val apiService: ApiService by lazy {
//    NetworkApi.mRetrofit.create(ApiService::class.java)
//}
//
//val apiUserService: ApiService by lazy{
//    NetworkApi.mRetrofit.create(ApiService::class.java)
//}
//
//val apiHomeService: ApiHomeService  by {
//    NetworkApi.mRetrofit.create(ApiHomeService::class.java)
//}



class NetworkApiImp : BaseNetworkApi() {

    companion object {


        val INSTANCE: NetworkApiImp by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            NetworkApiImp()
        }

        val mRetrofit = NetworkApiImp.INSTANCE.getRetrofit(NetWorkManager.getInstance().mNetConfig.mBaseUrl)

    }

    /**
     * 实现重写父类的setHttpClientBuilder方法，
     * 在这里可以添加拦截器，可以对 OkHttpClient.Builder 做任意操作
     */
    override fun setHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        val loggingInterceptor = HttpLoggingInterceptor()
        //设置日志打印级别
        //设置日志打印级别
        loggingInterceptor.level = if (Ktx.DEBUG)HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE


        builder.apply {
            //设置缓存配置 缓存最大10M
            cache(Cache(java.io.File(ContextUtil.getInstance().cacheDir, "okhttp_cache"),
                (10 * 1024 * 1024).toLong()
            ))

//            internalCache()
            //添加Cookies自动持久化
            cookieJar(cookieJar)
            //示例：添加公共heads 注意要设置在日志拦截器之前，不然Log中会不显示head信息
            addInterceptor(HttpHeadInterceptor())
            //添加缓存拦截器 可传入缓存天数，不传默认7天
//            addInterceptor(CacheInterceptor())
            addInterceptor(RequestParameterInterceptor(NetWorkManager.getInstance().mNetConfig.mIbuildMap))
            // 日志拦截器
            addInterceptor(loggingInterceptor)
            addNetworkInterceptor(OnlineCacheInterceptor())
            addInterceptor(OfflineCacheInterceptor())
            //超时时间 连接、读、写
            var timeout : Long =  if (Ktx.DEBUG) (5 * 1000).toLong() else NetWorkKey.TIMEOUT.toLong()
            connectTimeout(timeout, MILLISECONDS)
            readTimeout(timeout, MILLISECONDS)
            writeTimeout(timeout, MILLISECONDS)
            if (!Ktx.DEBUG){
                proxySelector(object : ProxySelector(){
                    override fun select(uri: URI?): MutableList<Proxy> {
                        return Collections.singletonList(Proxy.NO_PROXY)
                    }

                    override fun connectFailed(uri: URI?, sa: SocketAddress?, ioe: IOException?) {

                    }
                })
                proxy(Proxy.NO_PROXY)
            }
        }
        return builder
    }



    /**
     * 实现重写父类的setRetrofitBuilder方法，
     * 在这里可以对Retrofit.Builder做任意操作，比如添加GSON解析器，protobuf等
     */
    override fun setRetrofitBuilder(builder: Retrofit.Builder): Retrofit.Builder {
        return builder.apply {
            addConverterFactory(DataConverterFactory())
            addCallAdapterFactory(LifeCycleCallAdapterFactory.INSTANCE)
            addCallAdapterFactory(FinalCallAdapterFactory.INSTANCE)
        }
    }

    val cookieJar: PersistentCookieJar by lazy {
        PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(ContextUtil.getInstance()))
    }

}


