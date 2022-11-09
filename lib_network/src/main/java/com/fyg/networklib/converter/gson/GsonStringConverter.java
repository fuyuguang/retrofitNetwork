package com.fyg.networklib.converter.gson;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.IOException;

import retrofit2.Converter;

/**
 * Created by fuyuguang on 2022/9/15 7:35 下午.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
 */
public class GsonStringConverter<T> implements Converter<T, String> {

    private final Gson gson;
    private final TypeAdapter<?> adapter;

    GsonStringConverter(Gson gson, TypeAdapter<?> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public String convert(T value) throws IOException {
        if (value == null){
            return null;
        }
        if (value instanceof String){
            return TextUtils.isEmpty((CharSequence) value) ? null :  value.toString();
        }else{
            return value.toString();
        }
    }
}
