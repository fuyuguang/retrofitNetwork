package com.fyg.networklib.converter.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Created by fuyuguang on 2022/9/15 6:20 下午.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
 */
public class StringTypeAdapter extends TypeAdapter<String> {

    @Override
    public String read(JsonReader in) throws IOException {
        JsonToken peek = in.peek();
        if (peek == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        /* coerce booleans to strings for backwards compatibility */
        if (peek == JsonToken.BOOLEAN) {
            return Boolean.toString(in.nextBoolean());
        }
        return in.nextString();
    }
    @Override
    public void write(JsonWriter out, String value) throws IOException {
        // 下面这个if是关键代码，指定序列化时，遇到空串则直接输出null值。
        // 由于Gson默认是不序列化null的，所以这里就相当于在序列化时排除了空串的字段
        if ("".equals(value)) {
            out.nullValue();
            return;
        }
        out.value(value);
    }

}
