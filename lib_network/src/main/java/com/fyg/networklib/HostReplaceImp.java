package com.fyg.networklib;

import android.text.TextUtils;

import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import okhttp3.HttpUrl;
import okhttp3.Request;

/**
 * Created by fuyuguang on 2022/6/29 10:33 上午.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
 */
public class HostReplaceImp implements IHostReplace {


    private static final String DOMAIN_NAME = "Domain-Name";
    public static final String DOMAIN_NAME_HEADER = DOMAIN_NAME + ": ";

    @Override
    public Request hostReplace(Request request) {
        String domainName = request.header(DOMAIN_NAME);
        if (!TextUtils.isEmpty(domainName)){
            HttpUrl httpUrl = RetrofitUrlManager.getInstance().fetchDomain(domainName);
            if (httpUrl != null){
                String host = httpUrl.host();
                HttpUrl url = request.url();
                request = request.newBuilder().url(url.newBuilder().host(host).build()).build();
            }
        }
        return request;
    }
}
