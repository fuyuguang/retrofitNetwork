package com.fyg.networklib.cache;

import android.content.Context;

import com.fyg.networklib.Ktx;
import com.fyg.networklib.log.DebugLog;

import java.io.File;

public class ApiResponseCacheManager {

    private  volatile DiskCacheHelper mCache;


    private ApiResponseCacheManager(Context context) {
        File cacheDir = new File(context.getCacheDir(), "retrofit"+File.separator+"api_response"+File.separator);
        if (cacheDir != null) {
            mCache = DiskCacheHelper.get(cacheDir);
        }
    }


    private static class InnerSingleton {
        private static final ApiResponseCacheManager INSTANCE = new ApiResponseCacheManager(Ktx.app);
    }

    public static ApiResponseCacheManager getInstance() {
        return InnerSingleton.INSTANCE;
    }

    @DebugLog
    public  synchronized void save(Context context, String key, String value) {

        InnerSingleton.INSTANCE.mCache.put(key, value);
    }

    @DebugLog
    public  synchronized String get(Context context, String key) {
        return InnerSingleton.INSTANCE.mCache.getAsString(key);
    }

    public  synchronized void clear(Context context) {
        InnerSingleton.INSTANCE.mCache.clear();
    }
}
