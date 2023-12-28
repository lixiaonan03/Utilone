package com.lxn.utilone.network;

import com.lxn.utilone.util.Log;

import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
  *  @author lixiaonan
  *  功能描述: okhttp的封装的
  *  时 间： 2021/12/3
  */
public class OkHttpClientsUtils {

    private static final String TAG = "OkHttpClientsUtils";
    private static volatile OkHttpClient client;

    private static final int CONNECT_TIMEOUT = 10;
    private static final int READ_TIMEOUT = 20;
    private static final int WRITE_TIMEOUT = 20;


    private static OkHttpClient newOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //通过这个设置okhttp 不能用代理链接来访问 防止抓包
//        builder.proxy(Proxy.NO_PROXY);
        // 连接超时时间阈值
        builder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        // 数据获取时间阈值
        builder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
        // 写数据超时时间阈值
        builder.writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);

        Map<String, String> map = new HashMap<>();
        //2 是安卓
        map.put("clientType", "2");
        //                    map.put("Connection", "close");
        BasicParamsInterceptor basicParamsInterceptor = new BasicParamsInterceptor
                .Builder()
                //                            .addHeaderParamsMap(map)
                //                            .addHeaderParam("token",PreferencesSetGetUtil.getToken())
                //                            .addParam("token",PreferencesSetGetUtil.getToken())
                .addParamsMap(map)
                .addQueryParamsMap(map)
                .build();

        // 日志拦截器，用来记录所有的网络请求和响应
        // 这个拦截器应该放到所有拦截器的最后，用来监听真正的请求/响应
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(message -> {
            Log.i("lxnNet", message);
        });
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        //        builder.addInterceptor(basicParamsInterceptor);
        builder.addInterceptor(logging);
        builder.eventListener(new PrintingEventListener())
                //                            .eventListenerFactory(HttpEventListener.FACTORY)
                //                            .eventListenerFactory(new LoggingEventListener.Factory())
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        //认证证书的
                        return true;
                    }
                });
        builder.dns(new DnsDemo());
        return builder.build();
    }

    public static OkHttpClient getClient() {
        if (client == null) {
            synchronized (OkHttpClientsUtils.class) {
                if (client == null) {
                    client = newOkHttpClient();
                }
            }
        }
        return client;
    }

    /**
     * 制空client对象重新创立
     */
    public static void removeClient() {
        client = null;
    }
}
