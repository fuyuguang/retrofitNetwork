package com.mvvm.provider;

import android.app.Application;

import androidx.annotation.NonNull;

/**
 * * desc : ${desc}
 * Created by fyg on 2019-07-05.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 */
public interface IApplication {

    void attachBaseContext(@NonNull Application context);

    void onCreate(@NonNull Application application,boolean isDebug);

    void onLowMemory();

    void onTerminate(@NonNull Application application);
}
