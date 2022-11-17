package com.mvvm.provider.comm;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.core.LogisticsCenter;
import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavigationCallback;
import com.alibaba.android.arouter.facade.template.IProvider;
import com.alibaba.android.arouter.launcher.ARouter;
import com.mvvm.provider.IApplicationProvider;

/**
 * * desc : ${desc}
 * Created by fyg on 2019-06-17.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 */
public class ArouterManager {

    private ArouterManager() {
    }

    public static void init(Application application,boolean isDebug) {

        if (isDebug) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
            ARouter.printStackTrace();
        }
        ARouter.init(application); // 尽可能早，推荐在Application中初始化

    }

    public static void inject(Object target) {
        ARouter.getInstance().inject(target);
    }

    public static String getOrignalUri(Intent intent) {
        if (intent != null) {
            return intent.getStringExtra(ARouter.RAW_URI);
        }
        return null;
    }

    private static Postcard getPostcard(@Nullable String path) {
        return ARouter.getInstance().build(path);
    }

    private static Postcard getPostcard(@NonNull Uri url) {
        return ARouter.getInstance().build(url);
    }


    /**
     * @param service
     * @param <T>
     * @return {@link IProvider}
     */
    public static <T> T navigation(Class<? extends T> service) {
        return ARouter.getInstance().navigation(service);
    }


    /**
     * 1. 应用内简单的跳转(通过URL跳转在'进阶用法'中)
     *
     * @param path
     */
    public static Object navigation(String path) {
        return getPostcard(path).navigation();
    }


    public static Object navigation(Uri uri) {
        return getPostcard(uri).navigation();
    }


    public static void navigationWithInt(@Nullable String path,
                                         @Nullable String keyInt, int intValue) {
        navigationWithStringIntLongObjectNavCallbackForResult(null, path,
                null, null,
                keyInt, intValue,
                null, -1,
                null, null,
                -1, null);
    }

    public static void navigationWithLong(@Nullable String path,
                                          @Nullable String keyLong, int longValue) {

        navigationWithStringIntLongObjectNavCallbackForResult(null, path,
                null, null,
                null, -1,
                keyLong, longValue,
                null, null,
                -1, null);
    }

    public static void navigationWithString(@Nullable String path,
                                            @Nullable String keyStr, @Nullable String strValue) {
        navigationWithStringIntLongObjectNavCallbackForResult(null, path,
                keyStr, strValue,
                null, -1,
                null, -1,
                null, null,
                -1, null);
    }

    public static void navigationWithObject(@Nullable String path,
                                            @Nullable String keyObj, @Nullable Object objValue) {

        navigationWithStringIntLongObjectNavCallbackForResult(null, path,
                null, null,
                null, -1,
                null, -1,
                keyObj, objValue,
                -1, null);
    }


    public static void navigationWithStringInt(@Nullable String path,
                                               @Nullable String keyStr, @Nullable String strValue,
                                               @Nullable String keyInt, int intValue) {
        navigationWithStringIntLongObjectNavCallbackForResult(null, path,
                keyStr, strValue,
                keyInt, intValue,
                null, -1,
                null, null,
                -1, null);

    }


    public static void navigationWithStringIntNavCallback(@Nullable String path,
                                                          @Nullable String keyStr, @Nullable String strValue,
                                                          @Nullable String keyInt, int intValue,
                                                          NavigationCallback callback) {
        navigationWithStringIntLongObjectNavCallbackForResult(null, path,
                keyStr, strValue,
                keyInt, intValue,
                null, -1,
                null, null,
                -1, callback);

    }


    public static void navigationWithIntForResult(Context context, @Nullable String path,
                                                  @Nullable String keyInt, int intValue, int requestCode) {

        navigationWithStringIntLongObjectNavCallbackForResult(context, path,
                null, null,
                keyInt, intValue,
                null, -1,
                null, null,
                requestCode, null);
    }


    public static void navigationWithStringIntLongObjectNavCallbackForResult(Context context, @Nullable String path,
                                                                             @Nullable String keyStr, @Nullable String strValue,
                                                                             @Nullable String keyInt, int intValue,
                                                                             @Nullable String keyLong, int longValue,
                                                                             @Nullable String keyObj, @Nullable Object objValue,
                                                                             int requestCode, NavigationCallback callback) {
        navigationWithStringIntLongObjectNavCallbackForResult(context, path,
                keyStr, strValue,
                keyInt, intValue,
                keyLong, longValue,
                keyObj, objValue,
                requestCode, callback, 0, 0);
    }


    public static void navigationWithStringIntLongObjectNavCallbackForResult(Context context, @Nullable String path,
                                                                             @Nullable String keyStr, @Nullable String strValue,
                                                                             @Nullable String keyInt, int intValue,
                                                                             @Nullable String keyLong, int longValue,
                                                                             @Nullable String keyObj, @Nullable Object objValue,
                                                                             int requestCode, NavigationCallback callback, int enterAnim, int exitAnim) {
        navigationWithStringIntLongObjectNavCallbackForResult(context, path,
                keyStr, strValue,
                keyInt, intValue,
                keyLong, longValue,
                keyObj, objValue,
                requestCode, callback,
                0, 0, false);

    }


    public static void navigationWithStringIntLongObjectNavCallbackForResult(Context context, @Nullable String path,
                                                                             @Nullable String keyStr, @Nullable String strValue,
                                                                             @Nullable String keyInt, int intValue,
                                                                             @Nullable String keyLong, int longValue,
                                                                             @Nullable String keyObj, @Nullable Object objValue,
                                                                             int requestCode, NavigationCallback callback, int enterAnim, int exitAnim, boolean isGreenChannel) {
        Postcard postcard = getPostcard(path);
        if (!TextUtils.isEmpty(keyStr)) {
            postcard.withString(keyStr, strValue);
        }
        if (!TextUtils.isEmpty(keyInt)) {
            postcard.withInt(keyInt, intValue);
        }
        if (!TextUtils.isEmpty(keyLong)) {
            postcard.withLong(keyLong, longValue);
        }
        if (!TextUtils.isEmpty(keyObj)) {
            postcard.withObject(keyObj, objValue);
        }
        if (enterAnim > 0 && exitAnim > 0) {
            postcard.withTransition(enterAnim, exitAnim);
        }
        if (isGreenChannel) {
            postcard.greenChannel();
        }
        navigation(context, postcard, requestCode, callback);

    }


    public static class SimpleNavigationCallback implements NavigationCallback {
        @Override
        public void onFound(Postcard postcard) {
        }

        @Override
        public void onLost(Postcard postcard) {
        }

        @Override
        public void onArrival(Postcard postcard) {
        }

        @Override
        public void onInterrupt(Postcard postcard) {
        }
    }

    private static Object navigation(Context mContext, Postcard postcard, int requestCode, NavigationCallback callback) {
        return ARouter.getInstance().navigation(mContext, postcard, requestCode, callback);
    }


    public static void destroy() {
        ARouter.getInstance().destroy();
    }

    public static Class<?> getClass(String path) {
        Postcard postcard = ARouter.getInstance().build(path);
        LogisticsCenter.completion(postcard);
        return postcard.getDestination();

    }


    public static void initModule(Application application, Boolean isDebug,String... modulePaths) {
        if (modulePaths != null && modulePaths.length > 0) {
            IApplicationProvider provider;
            for (String path : modulePaths) {
                if (path != null) {
                    provider = (IApplicationProvider) navigation(path);
                    if (provider != null) {
                        provider.onCreate(application,isDebug);
                    }
                }
            }
        }
    }
}
