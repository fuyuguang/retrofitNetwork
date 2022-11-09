package com.fyg.networklib.interceptor;

import android.text.TextUtils;

import com.fyg.networklib.util.ContextUtil;
import com.fyg.networklib.util.NetworkHelper;
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
public class OfflineCacheInterceptor extends CacheInterceptor {


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!NetworkHelper.isNetworkAvailable(ContextUtil.getInstance())) {
            //JXToast.showShort(R.string.offline_cache_interceptor_tip);
            String offlineCacheTime = request.header(OFFLINE_CACHE_TIME);
            if (!TextUtils.isEmpty(offlineCacheTime)) {
                int mOfflineCacheTime = StringUtils.toInt(offlineCacheTime, 0);
                if (mOfflineCacheTime > 0){
                    request = request.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + mOfflineCacheTime)
                            .build();
                }
            }
        }else{
            String forceRefresh = request.url().queryParameter(FORCE_REFRESH);
            if ("true".equals(forceRefresh)) {
                request = request.newBuilder()
                        .header("Cache-Control", "public, max-age=" + 0)
                        .build();
            }
        }
        return chain.proceed(request);
    }
}
