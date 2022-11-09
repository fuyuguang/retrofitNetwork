package com.fyg.networklib.interceptor
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Created by fuyuguang on 2022/4/2 6:02 下午.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
    []()
    []()

    { @link okhttp3.internal.http.BridgeInterceptor}
    开发者没有添加Accept-Encoding时，自动添加Accept-Encoding: gzip
    自动添加的request，response支持自动解压
    手动添加不负责解压缩
    自动解压时移除Content-Length，所以上层Java代码想要contentLength时为-1
    自动解压时移除 Content-Encoding
    自动解压时，如果是分块传输编码，Transfer-Encoding: chunked不受影响。
 */
class HttpHeadInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
//        builder.addHeader(BaseHttpEngine.ACCEPT_ENCODING, BaseHttpEngine.GZIP).build()
        return chain.proceed(builder.build())
    }
}