package com.fyg.networklib.callbacklib.exception;

import android.util.MalformedJsonException;

import com.alibaba.fastjson.JSONException;
import com.google.gson.JsonParseException;

import org.apache.http.conn.ConnectTimeoutException;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;

import javax.net.ssl.SSLException;

import retrofit2.HttpException;

/**
 * Created by fuyuguang on 2022/4/19 6:03 下午.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
 */
public class ParseNetThrowable {



    public static  HttpError parseThrowable(Throwable t) {
        if (t instanceof HttpError) {
            return (HttpError) t;
        } else if (t instanceof HttpException) {
            HttpException httpException = (HttpException) t;
            final String msg;
            switch (httpException.code()) {
                case 400:
                    msg = "参数错误";
                    break;
                case 401:
                    msg = "身份未授权";
                    break;
                case 403:
                    msg = "禁止访问";
                    break;
                case 404:
                    msg = "地址未找到";
                    break;
                case 504:
                    msg = "请求超出，请检查网络是否正常";
                    break;

//                case 304:
//                    httpException.response().body();
//                    break;

                default:
                    msg = "服务异常";
            }
            return new HttpError(msg, httpException);
        } else if (t instanceof UnknownHostException) {
            return new HttpError("网络异常", t);
        } else if (t instanceof ConnectException) {
            return new HttpError("网络连接错误，请稍后重试", t);
        } else if (t instanceof SocketException) {
            return new HttpError("服务异常", t);
        } else if (t instanceof SocketTimeoutException) {
            return new HttpError("响应超时", t);
        } else if(t instanceof JsonParseException || t instanceof ParseException || t instanceof MalformedJsonException  ||
                t instanceof JSONException){
            return new HttpError("解析错误，请稍后再试", t);
        }else if(t instanceof SSLException){
            return new HttpError("证书出错，请稍后再试", t);
        }else if(t instanceof ConnectTimeoutException || t instanceof SocketTimeoutException || t instanceof UnknownHostException){
            return new HttpError("网络连接超时，请稍后重试", t);
        } else{
            return new HttpError("请求失败", t);
        }
    }
}
