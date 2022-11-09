package com.fyg.networklib.interceptor

import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response
import java.io.IOException

/**
 * Created by fuyuguang on 2022/5/23 9:24 上午.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
 */
open class CacheInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Chain): Response {
        return chain.proceed(chain.request())
    }

    companion object {
        const val OFFLINE_CACHE_TIME = "offlineCacheTime"
        const val OFFLINE_CACHE_TIME_HEADER = "offlineCacheTime:"

        const val COMMON_PARAMETER_KEY = "commonParameter"
        const val COMMON_PARAMETER_HEADER = "commonParameter:"
        const val COMMON_PARAMETER_IGNORE = "commonParameter:ignore"

        const val ONLINE_CACHE_TIME = "onlineCacheTime"
        const val ONLINE_CACHE_TIME_HEADER = "onlineCacheTime:"

        const val CACHE_TIME = "onlineCacheTime:%d,offlineCacheTime:%d"

        const val FORCE_REFRESH = "force_refresh"

    }
}