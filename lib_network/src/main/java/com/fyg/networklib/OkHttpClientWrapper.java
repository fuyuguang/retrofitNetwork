package com.fyg.networklib;

import androidx.annotation.NonNull;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by fuyuguang on 2022/6/29 10:20 上午.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
 */
public class OkHttpClientWrapper implements Call.Factory{

    private final OkHttpClient mOkHttpClient;
    private final IHostReplace mIHostReplace;

    public OkHttpClientWrapper(@NonNull OkHttpClient okHttpClient, IHostReplace IHostReplace){
        mOkHttpClient = okHttpClient;
        mIHostReplace = IHostReplace;
    }


    @Override
    public Call newCall(Request request) {
        if (mIHostReplace != null){
            request = mIHostReplace.hostReplace(request);
        }
        return mOkHttpClient.newCall(request);
    }
}
