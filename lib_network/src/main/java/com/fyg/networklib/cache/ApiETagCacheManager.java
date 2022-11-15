package com.fyg.networklib.cache;

import android.content.Context;

import com.fyg.networklib.Ktx;
import com.fyg.networklib.log.DebugLog;

import java.io.File;

public class ApiETagCacheManager {

    private volatile DiskCacheHelper mCache;

    private ApiETagCacheManager(Context context) {
        File cacheDir = new File(context.getCacheDir(), "retrofit" + File.separator + "api_eTag" + File.separator);
        if (cacheDir != null) {
            mCache = DiskCacheHelper.get(cacheDir);
        }
    }

    private static class InnerSingleton {
        private static final ApiETagCacheManager INSTANCE = new ApiETagCacheManager(Ktx.app);
    }

    public static ApiETagCacheManager getInstance() {
        return InnerSingleton.INSTANCE;
    }

    public synchronized void save(Context context, String key, String value) {
        ApiETagCacheManager.InnerSingleton.INSTANCE.mCache.put(key, value);
    }
    @DebugLog
    public synchronized String get(Context context, String key) {
        return ApiETagCacheManager.InnerSingleton.INSTANCE.mCache.getAsString(key);
    }
    @DebugLog
    public synchronized void clear(Context context) {
        ApiETagCacheManager.InnerSingleton.INSTANCE.mCache.clear();
    }
}
