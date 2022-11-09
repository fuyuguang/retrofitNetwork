package com.fyg.networklib.util;

import android.text.TextUtils;

/**
 * @author xingdong
 * @createdate 2014年12月19日
 * @description 字符串相关的工具类
 */
public class StringUtils {

    /**
     * 字符串转整数
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            if (!TextUtils.isEmpty(str)){
                return Integer.parseInt(str);
            }
        } catch (Exception e) {
        }
        return defValue;
    }

}
