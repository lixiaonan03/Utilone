package com.lxn.utilone.retrofit;

import com.lxn.utilone.retrofit.fastjsonconverterfactory.FastJsonConverterFactory;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;


/**
 * 北京爱钱帮财富科技有限公司
 * 功能描述: Retrofit基本包装类
 * 作 者:  李晓楠
 * 时 间： 2017/3/10 下午1:51
 */
public class RetrofitHelper {

    private Retrofit mRetrofit = null;
    private static RetrofitHelper instance = null;

    /**
     * 单例模式获得该对象
     *
     * @return
     */
    public static RetrofitHelper getInstance() {
        synchronized (RetrofitHelper.class) {
            if (instance == null) {
                instance = new RetrofitHelper();
            }
        }
        return instance;
    }


    private RetrofitHelper() {
        mRetrofit = new Retrofit.Builder().baseUrl(Constant.BaseUrl)
                .client(OkHttpClientsUtils.getClient())
                .addConverterFactory(FastJsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }


    /**
     * 获取 Retrofit 对象
     *
     * @return Retrofit
     */
    public Retrofit getRetrofit() {

        return mRetrofit;
    }

}
