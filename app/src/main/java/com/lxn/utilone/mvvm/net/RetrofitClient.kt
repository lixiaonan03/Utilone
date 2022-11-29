package com.lxn.utilone.mvvm.net

import com.lxn.utilone.mvvm.ApiService
import com.lxn.utilone.network.OkHttpClientsUtils
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * retrofitClient 工具类
 * @author：李晓楠
 * 时间：2022/10/27 15:02
 */
object RetrofitClient {

    /**
     * 接口api
     */
    val service by lazy {
        getService(ApiService::class.java, ApiService.BASE_URL)
    }


    //获取OkHttpClient 客户端示例
    private val client: OkHttpClient by lazy {
        OkHttpClientsUtils.getClient()
    }


    fun handleBuilder(builder: OkHttpClient.Builder) {}

    /**
     * app 不同的service
     */
    fun <Service> getService(serviceClass: Class<Service>, baseUrl: String): Service {
        return Retrofit.Builder().client(client).addConverterFactory(GsonConverterFactory.create()).baseUrl(baseUrl).build().create(serviceClass)
    }
}