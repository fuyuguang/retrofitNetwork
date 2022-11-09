package com.fyg.networklib.cache;

import android.text.TextUtils;

import com.fyg.networklib.cache.disc.impl.ext.LruDiskCache;
import com.fyg.networklib.cache.disc.naming.Md5Sha1FileNameGenerator;
import com.fyg.networklib.cache.disc.utils.IoUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

class DiskCacheHelper {

    /**
     * 200M
     */
    private static final long CACHE_MAX_SIZE = 50 * 1024 * 1024;
    private static final int CACHE_MAX_COUNT = 1000;

    private LruDiskCache mDiskLruCache;

    private DiskCacheHelper(File cacheDir) {
        try {
            long startTime = System.currentTimeMillis();
            mDiskLruCache = new LruDiskCache(cacheDir, null, new Md5Sha1FileNameGenerator(), CACHE_MAX_SIZE, CACHE_MAX_COUNT);
            //LoggerHelper.i("DiskCache time", "init use time " + (System.currentTimeMillis() - startTime));
        } catch (Exception e) {
            //LoggerHelper.e(e);
        }
    }

    public synchronized static DiskCacheHelper get(File cacheDir) {
        return new DiskCacheHelper(cacheDir);
    }

    public synchronized void put(final String key, final String value) {
        long startTime = System.currentTimeMillis();
        putString(key, value);
        //LoggerHelper.i("DiskCache time", "put use time " + (System.currentTimeMillis() - startTime));
    }


    private void putString(final String key, final String value) {
        if (TextUtils.isEmpty(value)) {
            return;
        }
        if (mDiskLruCache != null) {
            ByteArrayInputStream inputStream = null;
            try {
                inputStream = new ByteArrayInputStream(value.getBytes("UTF-8"));
                mDiskLruCache.save(key, inputStream);
            } catch (Exception e) {
                //LoggerHelper.e(e);
            } finally {
                IoUtils.closeSilently(inputStream);
            }
        }
    }

    public synchronized String getAsString(String key) {
        long startTime = System.currentTimeMillis();
        String value = get(key);
        //LoggerHelper.i("DiskCache time", "get use time " + (System.currentTimeMillis() - startTime));
        return value;
    }

    private String get(String key) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }
        if (mDiskLruCache != null) {
            File file = mDiskLruCache.get(key);
            if (file != null) {
                FileInputStream fileInputStream = null;
                ByteArrayOutputStream outputStream = null;
                try {
                    fileInputStream = new FileInputStream(file);
                    outputStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[8 * 1024];
                    int read;
                    while ((read = fileInputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, read);
                    }
                    return outputStream.toString("UTF-8");
                } catch (Exception e) {
                    //LoggerHelper.e(e);
                } finally {
                    IoUtils.closeSilently(fileInputStream);
                    IoUtils.closeSilently(outputStream);
                }
            }
        }
        return null;
    }

    public synchronized void clear() {
        if (mDiskLruCache != null) {
            try {
                mDiskLruCache.clear();
            } catch (Exception e) {
                //LoggerHelper.e(e);
            }
        }
    }
}
