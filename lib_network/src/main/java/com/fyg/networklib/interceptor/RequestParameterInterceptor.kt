package com.fyg.networklib.interceptor

import android.text.TextUtils
import com.fyg.networklib.IbuildMap
import com.fyg.networklib.cache.ApiETagCacheManager
import com.fyg.networklib.model.bean.RequestTag
import com.fyg.networklib.retrofit.GetGenerateParam
import com.fyg.networklib.util.NetWorkKey
import com.fyg.networklib.util.ContextUtil
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * @author fyg
 * @date : 2021/7/29
 * @E-Mail ：2355245065@qq.com
 * @Wechat :fyg13522647431
 * @Tel : 13522647431
 * @desc : 公参 拦截器
 */
class RequestParameterInterceptor(var buildMap: IbuildMap) : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {

        var request: Request = chain.request()

        val commonParameter = request.header(CacheInterceptor.COMMON_PARAMETER_KEY)

        if (TextUtils.isEmpty(commonParameter)) {
            request = GetGenerateParam(buildMap)
                .generateParam(request)
        }


        var requestBean = RequestTag()
        requestBean.url = request.url().toString()
        requestBean.eTag = ApiETagCacheManager.getInstance().get(ContextUtil.getInstance(), requestBean.url)

        if (!TextUtils.isEmpty(requestBean.eTag)) {
            request = request.newBuilder().url(
                request.url().newBuilder().addQueryParameter(NetWorkKey.KEY_ETAG, requestBean.eTag)
                    .build()
            ).build()
        } else {
            //request.newBuilder().cacheControl(CacheControl.Builder().noStore().build()).build()
        }

        request = request.newBuilder().tag(RequestTag::class.java, requestBean).build()

        return chain.proceed(request)
    }
}