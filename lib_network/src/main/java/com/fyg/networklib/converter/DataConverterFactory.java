package com.fyg.networklib.converter;

import androidx.annotation.Nullable;

import com.google.gson.GsonBuilder;
import com.fyg.networklib.converter.fastjson.FastJsonConverterFactory;
import com.fyg.networklib.converter.gson.GsonConverterFactory;
import com.fyg.networklib.converter.gson.StringTypeAdapter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
/**
 * Created by fuyuguang on 2022/4/24 5:39 下午.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
    []()
    []()
    
 */
public class DataConverterFactory extends Converter.Factory {

    private final Converter.Factory fastJsonFactory = FastJsonConverterFactory.create();
    private final Converter.Factory gsonFactory = GsonConverterFactory.create(new GsonBuilder().registerTypeAdapter(String.class, new StringTypeAdapter()).create());

    public static DataConverterFactory create() {
        return new DataConverterFactory();
    }


    public @Nullable Converter<?, RequestBody> requestBodyConverter(
            Type type,
            Annotation[] parameterAnnotations,
            Annotation[] methodAnnotations,
            Retrofit retrofit) {

        for (Annotation annotation : methodAnnotations) {
            if (!(annotation instanceof ResponseFormat)) {
                continue;
            }
            String value = ((ResponseFormat) annotation).value();
            if (ResponseFormat.GSON.equals(value)) {
                return gsonFactory.requestBodyConverter(type, parameterAnnotations, methodAnnotations,retrofit);
            } else  {

            }
        }
        return fastJsonFactory.requestBodyConverter(type, parameterAnnotations, methodAnnotations,retrofit);
    }


    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        for (Annotation annotation : annotations) {
            if (!(annotation instanceof ResponseFormat)) {
                continue;
            }
            String value = ((ResponseFormat) annotation).value();
            if (ResponseFormat.GSON.equals(value)) {
                return gsonFactory.responseBodyConverter(type, annotations, retrofit);
            } else  {

            }
        }
        return fastJsonFactory.responseBodyConverter(type, annotations, retrofit);
    }


    public @Nullable Converter<?, String> stringConverter(
            Type type, Annotation[] annotations, Retrofit retrofit) {

        for (Annotation annotation : annotations) {
            if (!(annotation instanceof ResponseFormat)) {
                continue;
            }
            String value = ((ResponseFormat) annotation).value();
            boolean filterEmpty = ((ResponseFormat) annotation).filterEmpty();
            if (ResponseFormat.GSON.equals(value)) {
                return filterEmpty ? gsonFactory.stringConverter(type,annotations,retrofit) : null;
            } else if (ResponseFormat.FASTJSON.equals(value)){
                return filterEmpty ? fastJsonFactory.stringConverter(type,annotations,retrofit) : null;
            }else{

            }
        }
        return fastJsonFactory.stringConverter(type,annotations,retrofit) ;
    }

}