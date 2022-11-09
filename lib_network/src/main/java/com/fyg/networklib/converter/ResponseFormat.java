package com.fyg.networklib.converter;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Created by fuyuguang on 2022/4/24 5:33 下午.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
    []()
    []()

 */
@Target(METHOD)
@Retention(RUNTIME)
public @interface ResponseFormat {

    String FASTJSON = "fastJson";

    String GSON = "gson";

    String value() default FASTJSON;


    boolean filterEmpty() default true;

}