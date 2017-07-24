package com.LeiHolmes.retrofit2demo.support;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * description: Response converter
 * author： dongyeforever@gmail.com
 * date: 2016-04-26 14:31
 */
public class FastjsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private Type type;
    private Charset charset;

    public FastjsonResponseBodyConverter() {
    }

    public FastjsonResponseBodyConverter(Type type, Charset charset) {
        this.type = type;
        this.charset = charset;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        try {
            String s = value.string();
            return JSON.parseObject(s, type);
        } finally {
            value.close();
        }
    }
}