package com.lxn.utilone.mvvm

import com.lxn.utilone.mvvm.bean.ApiResponse
import com.lxn.utilone.mvvm.bean.User
import com.lxn.utilone.mvvm.bean.WxArticleBean
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

/**
  *  @author 李晓楠
  *  功能描述: 接口相关的 api
  *  时 间： 2022/10/27 15:03
  */
interface ApiService {

    @GET("wxarticle/chapters/json")
    suspend fun getWxArticle(): ApiResponse<List<WxArticleBean>>

    @GET("abc/chapters/json")
    suspend fun getWxArticleError(): ApiResponse<List<WxArticleBean>>

    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(@Field("username") userName: String, @Field("password") passWord: String): ApiResponse<User?>

    companion object {
        const val BASE_URL = "https://wanandroid.com/"
    }
}