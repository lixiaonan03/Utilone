package com.lxn.utilone.mvvm.repository

import com.lxn.utilone.mvvm.bean.ApiResponse
import com.lxn.utilone.mvvm.bean.ApiSuccessResponse
import com.lxn.utilone.mvvm.bean.User
import com.lxn.utilone.mvvm.bean.WxArticleBean
import com.lxn.utilone.mvvm.net.RetrofitClient
import com.lxn.utilone.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
  *  @author 李晓楠
  *  功能描述: 真是业务数据的数仓处理
  *  时 间： 2022/10/27 14:28 
  */
class WxArticleRepository : BaseRepository() {

    private val mService by lazy {
        RetrofitClient.service
    }

    suspend fun fetchWxArticleFromNet(): ApiResponse<List<WxArticleBean>> {
        return executeHttp {
            mService.getWxArticle()
        }
    }

    suspend fun fetchWxArticleFromDb(): ApiResponse<List<WxArticleBean>> {
        return getWxArticleFromDatabase()
    }

    suspend fun fetchWxArticleError(): ApiResponse<List<WxArticleBean>> {
        return executeHttp {
            mService.getWxArticleError()
        }
    }

    /**
     * 登录操作的
     */
    suspend fun login(username: String, password: String): ApiResponse<User?> {
        Log.i("net", "数仓里面线程的=="+Thread.currentThread().name)
        return executeHttp {
            Log.i("net", "数仓延迟之前的=="+Thread.currentThread().name)
            mService.login(username, password)
        }
    }

    private suspend fun getWxArticleFromDatabase(): ApiResponse<List<WxArticleBean>> = withContext(Dispatchers.IO) {
        val bean = WxArticleBean()
        bean.id = 999
        bean.name = "零先生"
        bean.visible = 1
        ApiSuccessResponse(arrayListOf(bean))
    }


}