package com.aladdin.retrofit2demo.support;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

/**
 * description: Request converter
 * author： dongyeforever@gmail.com
 * date: 2016-04-26 14:24
 */
public class FastjsonRequestBodyConverter<T> implements Converter<T, RequestBody> {
    private Type type;
    private Charset charset;
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");

    public FastjsonRequestBodyConverter(Type type, Charset charset) {
        this.type = type;
        this.charset = charset;
    }

    @Override
    public RequestBody convert(T value) throws IOException {
        return RequestBody.create(MEDIA_TYPE, JSON.toJSONString(value).getBytes(charset));
    }
}
