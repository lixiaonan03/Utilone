package com.lxn.utilone.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lxn.utilone.mvvm.bean.ApiResponse
import com.lxn.utilone.util.Log
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


    protected fun <T> launchWithLoading(
        requestBlock: suspend () -> ApiResponse<T>, resultCallback: (ApiResponse<T>) -> Unit
    ) {
        viewModelScope.launch {
            flow {
                Log.i("net", "flow=="+Thread.currentThread().name)
                emit(requestBlock.invoke())
            }.onStart { //开始的

            }.onCompletion { // 完成的
                Log.i("net", "flow完成的=="+Thread.currentThread().name)
            }.collect {
                Log.i("net", "flow收到的=="+Thread.currentThread().name)
                resultCallback(it)
            }
        }
    }

    /**
     * 可以做一些清除工作的
     */
    override fun onCleared() {
        super.onCleared()
    }
}