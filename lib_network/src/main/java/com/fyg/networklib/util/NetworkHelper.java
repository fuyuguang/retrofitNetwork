package com.fyg.networklib.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络管理类，获取、监控网络状态，
 */
public final class NetworkHelper {

    private static NetworkInfo getActiveNetwork(Context context) {
        if (null == context) {
            return null;
        }
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null == connMgr) {
            return null;
        }

        return connMgr.getActiveNetworkInfo();
    }


    /**
     * 判断网络是否可用
     */
    public static boolean isNetworkAvailable(Context context) {

        NetworkInfo networkInfo = getActiveNetwork(context);
        if (null != networkInfo && networkInfo.isAvailable() && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
//                LoggerHelper.d(TAG, "WIFI 网络可用");
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
//                LoggerHelper.d(TAG, "3G网络可用");
            }
            return true;
        } else {
//            LoggerHelper.d(TAG, "网络不可用");
            return false;
        }
    }

}
