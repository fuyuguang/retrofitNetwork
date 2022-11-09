package com.fyg.networklib

import me.jessyan.retrofiturlmanager.RetrofitUrlManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit

abstract class BaseNetworkApi {

    fun <T> getApi(serviceClass: Class<T>, baseUrl: String): T {
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
        return setRetrofitBuilder(retrofitBuilder).build()
            .create(serviceClass)
    }

    fun  getRetrofit(baseUrl: String): Retrofit {
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(baseUrl)
            if (isDebug){
                retrofitBuilder.callFactory(OkHttpClientWrapper(okHttpClient,HostReplaceImp()))
            }else{
                retrofitBuilder.client(okHttpClient)
            }

        return setRetrofitBuilder(retrofitBuilder).build();
    }


    /**
     * 实现重写父类的setHttpClientBuilder方法，
     * 在这里可以添加拦截器，可以对 OkHttpClient.Builder 做任意操作
     */
    abstract fun setHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder

    /**
     * 实现重写父类的setRetrofitBuilder方法，
     * 在这里可以对Retrofit.Builder做任意操作，比如添加GSON解析器，Protocol
     */
    abstract fun setRetrofitBuilder(builder: Retrofit.Builder): Retrofit.Builder

    /**
     * 配置http
     */
    private val okHttpClient: OkHttpClient
        get() {
            var builder : OkHttpClient.Builder;
            return if (isDebug){
                builder = setHttpClientBuilder(OkHttpClient.Builder())
                builder.build()
            }else{
                builder = RetrofitUrlManager.getInstance().with(OkHttpClient.Builder())
                builder = setHttpClientBuilder(builder)
                builder.build()
            }
        }

    var isDebug = true;
}



