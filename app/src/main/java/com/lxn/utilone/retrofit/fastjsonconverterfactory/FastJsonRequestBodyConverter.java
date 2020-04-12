package com.lxn.utilone.retrofit.fastjsonconverterfactory;

import com.alibaba.fastjson.JSON;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

/**
  *   北京爱钱帮财富科技有限公司
  *   功能描述: 自定义的fastjson 的请求体的json 转换
  *    作 者:  李晓楠
  *    时 间： 2017/3/13 上午11:34
  */
public class FastJsonRequestBodyConverter<T> implements Converter<T, RequestBody> {
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");

    @Override
    public RequestBody convert(T value) throws IOException {
        return RequestBody.create(MEDIA_TYPE, JSON.toJSONBytes(value));
    }
}
