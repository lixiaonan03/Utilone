package com.lxn.utilone.mvvm.observer

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.aisier.network.observer.IStateObserver
import com.lxn.utilone.mvvm.bean.ApiResponse
import com.lxn.utilone.util.ToastUtils

/**
  *  @author 李晓楠
  *  功能描述: 带状态的livedata
  *  时 间： 2022/10/27 16:44
  */
class StateLiveData<T> : MutableLiveData<ApiResponse<T>>() {

    fun observeState(owner: LifecycleOwner, listenerBuilder: ListenerBuilder.() -> Unit) {
        val listener = ListenerBuilder().also(listenerBuilder)
        val value = object : IStateObserver<T>() {

            override fun onSuccess(data: T) {
                listener.mSuccessListenerAction?.invoke(data)
            }

            override fun onError(e: Throwable) {
                listener.mErrorListenerAction?.invoke(e)
            }

            override fun onDataEmpty() {
                listener.mEmptyListenerAction?.invoke()
            }

            override fun onComplete() {
                listener.mCompleteListenerAction?.invoke()
            }

            override fun onFailed(errorCode: Int?, errorMsg: String?) {
                listener.mFailedListenerAction?.invoke(errorCode, errorMsg)
            }

        }
        super.observe(owner, value)
    }

    inner class ListenerBuilder {
        internal var mSuccessListenerAction: ((T) -> Unit)? = null
        internal var mErrorListenerAction: ((Throwable) -> Unit)? = null
        internal var mEmptyListenerAction: (() -> Unit)? = null
        internal var mCompleteListenerAction: (() -> Unit)? = null
        internal var mFailedListenerAction: ((Int?, String?) -> Unit)? = null

        fun onSuccess(action: (T) -> Unit) {
            mSuccessListenerAction = action
        }

        fun onFailed(action: (Int?, String?) -> Unit) {
            mFailedListenerAction = action
        }

        fun onException(action: (Throwable) -> Unit) {
            mErrorListenerAction = action
        }

        fun onEmpty(action: () -> Unit) {
            mEmptyListenerAction = action
        }

        fun onComplete(action: () -> Unit) {
            mCompleteListenerAction = action
        }
    }

}