package com.lxn.utilone.mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lxn.utilone.mvvm.bean.ApiResponse
import com.lxn.utilone.mvvm.observer.SingleLiveEvent
import com.lxn.utilone.util.Log
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

/**
 *  @author 李晓楠
 *  功能描述: 基础的viewmodel 类的
 *  时 间： 2022/10/27 14:09
 */
abstract class BaseViewModel : ViewModel() {

    var showLoading = MutableLiveData<Boolean>()

    var showEmpty = MutableLiveData<Boolean>()




    protected fun <T> launchWithLoading(
        requestBlock: suspend () -> ApiResponse<T>, resultCallback: (ApiResponse<T>) -> Unit
    ) {
        viewModelScope.launch {
            flow {
                Log.i("net", "flow=="+Thread.currentThread().name)
                emit(requestBlock.invoke())
            }.onStart { //开始的
                showLoading.value = true
            }.onCompletion { // 完成的
                showLoading.value = false
                Log.i("net", "flow完成的=="+Thread.currentThread().name)
            }.catch {
                //可以收到报错信息
            }.collect {
                Log.i("net", "flow收到的=="+Thread.currentThread().name)
                resultCallback(it)
            }
        }
        //执行顺序为： 先执行 onStart -》 collect  -》  onCompletion -》 catch(如果有的话)
    }

    /**
     * 直接请求 自己控制进度条的展示
     */
    protected inline fun launch(
        crossinline queryBlock: suspend () -> Unit,
    ) {
        viewModelScope.launch {
            try {
                queryBlock()
            } catch (e: Exception) {
            }
        }
    }



    //是否是第一次加载网络数据，主要是跟PageStates结合使用，解决问题：只有第一次请求网络失败时才展示error_state_page
    var isFirstLoad = true
    val pageStates: BaseViewModel.PageStates by lazy { PageStates() }

    inner class PageStates {
        //正在加载中
        val loadingState by lazy { SingleLiveEvent<Boolean>() }

        //加载成功
        val successState by lazy { SingleLiveEvent<Boolean>() }

        //加载失败
        val emptyState by lazy { SingleLiveEvent<Boolean>() }

        //加载错误
        val errorState by lazy { SingleLiveEvent<Boolean>() }

        //以后扩展
        //customState.....
    }



    /**
     * 可以做一些清除工作的
     */
    override fun onCleared() {
        super.onCleared()
    }
}