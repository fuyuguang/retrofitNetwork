package com.fyg.networklib.converter.fastjson;

import android.text.TextUtils;

import java.io.IOException;
import java.lang.reflect.Type;

import retrofit2.Converter;


public class FastJsonStringConverter<T> implements Converter<T, String> {
    private final Type type;

    public FastJsonStringConverter(Type type) {
        this.type = type;
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