package com.lxn.utilone.mvvm

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import com.lxn.utilone.mvvm.observer.StateLiveData
import com.lxn.utilone.mvvm.bean.ApiResponse
import com.lxn.utilone.mvvm.bean.User
import com.lxn.utilone.mvvm.bean.WxArticleBean
import com.lxn.utilone.mvvm.repository.WxArticleRepository
import com.lxn.utilone.util.Log
import kotlinx.coroutines.launch

/**
  *  @author 李晓楠
  *  功能描述: 真正业务使用的viewmodel 的
  *  时 间： 2022/10/27 14:15 
  */
class ApiViewModel : BaseViewModel() {

    private val repository by lazy { WxArticleRepository() }

    val wxArticleLiveData = StateLiveData<List<WxArticleBean>>()
    val userLiveData = StateLiveData<User?>()
    private val dbLiveData = StateLiveData<List<WxArticleBean>>()
    private val apiLiveData = StateLiveData<List<WxArticleBean>>()

    //同时监听2个不同的livedata
    val mediatorLiveDataLiveData = MediatorLiveData<ApiResponse<List<WxArticleBean>>>().apply {
        this.addSource(apiLiveData) {
            this.value = it
        }
        this.addSource(dbLiveData) {
            this.value = it
        }
    }

    fun requestNet() {
        viewModelScope.launch {
            showLoading.value = true
            wxArticleLiveData.value = repository.fetchWxArticleFromNet()
            showLoading.value = false
        }
    }

    fun requestNetError() {
        viewModelScope.launch {
            wxArticleLiveData.value = repository.fetchWxArticleError()
        }
    }

    fun requestFromNet() {
        viewModelScope.launch {
            apiLiveData.value = repository.fetchWxArticleFromNet()
        }
    }

    fun requestFromDb() {
        viewModelScope.launch {
            dbLiveData.value = repository.fetchWxArticleFromDb()
        }
    }

    /**
     * 该请求使用Flow优化，自带loading。
     * 同时这个还自带生命周期的管理
     */
    fun login(username: String, password: String) {
        Log.i("net", "开始线程的=="+Thread.currentThread().name)
        launchWithLoading(requestBlock = {
            repository.login(username, password)
        }, resultCallback = {
            userLiveData.value = it
            Log.i("net", "结束的=$it="+Thread.currentThread().name)
        })
    }
}