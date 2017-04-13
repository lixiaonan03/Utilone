package com.lxn.utilone.retrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.internal.connection.ConnectInterceptor;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * 作者：李晓楠  okhttpcilent的封装
 * 时间：2017/3/10 14:31
 */
public class OkHttpClientsUtils {
    private static OkHttpClient client;

    public static OkHttpClient getClient() {
        if (client == null) {
            synchronized (OkHttpClientsUtils.class) {
                if (client == null) {
                    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                    client = new OkHttpClient.Builder()
                            //错误重连
                            .retryOnConnectionFailure(false)
                             //连接超时时间
                            .connectTimeout(60, TimeUnit.SECONDS)
                            .readTimeout(60, TimeUnit.SECONDS)
                            .writeTimeout(60, TimeUnit.SECONDS)
                            .addInterceptor(new CommonInterceptor())
                            .addNetworkInterceptor(logging)
                            .build();
                }
            }
        }
        return client;
    }
}
