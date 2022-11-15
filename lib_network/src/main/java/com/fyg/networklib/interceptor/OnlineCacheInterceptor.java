package com.fyg.networklib.interceptor;

import android.text.TextUtils;

import com.fyg.networklib.util.StringUtils;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by fuyuguang on 2022/5/20 7:01 下午.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
 */
public class OnlineCacheInterceptor extends CacheInterceptor {



    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        String onlineCacheTime = request.header(ONLINE_CACHE_TIME);

        int mOnlineCacheTime = 0;

        if (!TextUtils.isEmpty(onlineCacheTime)) {
            mOnlineCacheTime = StringUtils.toInt(onlineCacheTime, 0);
        }

        Response response = chain.proceed(request);

        if(mOnlineCacheTime > 0 && response.code() != 304){
            return  response.newBuilder()
                    .header("Cache-Control", "public, max-age=" + mOnlineCacheTime)
                    .removeHeader("Pragma").build();
        }else{
            return response;
        }

    }
}
