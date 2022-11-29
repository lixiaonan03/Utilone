package com.lxn.utilone.mvvm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.lxn.utilone.mvvm.bean.ApiSuccessResponse
import com.lxn.utilone.mvvm.bean.WxArticleBean
import com.lxn.utilone.util.Log
import com.lxn.utilone.util.ToastUtils
import kotlinx.coroutines.*

/**
 * App生命周期几倍的
 * @author：李晓楠
 * 时间：2022/11/3 09:10
 */
class AppViewModel (val context: Application): AndroidViewModel(context) {

    /**
     * 弹toast展示的
     */
    fun show(string: String){
       ToastUtils.toastshort(string)
    }


    /**
     * 测试的
     */
    fun requestFromDb() {
        //这个地方的context 是
        Log.i("net","context==="+context)

        /**
         * 使用全局的这个在页面管理之后依然会继续执行协程里面的任务
         */
        try {
            GlobalScope.launch {
                var job = withContext(Dispatchers.IO) {
                    Log.i("net","开始执行的=job==="+"开始线程的=="+Thread.currentThread().name)
                    delay(3000)
                    val bean = WxArticleBean()
                    ApiSuccessResponse(arrayListOf(bean))
                    Log.i("net","继续执行的==job=="+"开始线程的=="+Thread.currentThread().name)

                }
                Log.i("net","=job==$job"+"开始线程的=="+Thread.currentThread().name)
            }

        } catch (e: Exception) {
            Log.i("net","报异常了==$e=="+"开始线程的=="+Thread.currentThread().name)
            e.stackTrace
        }

        /**
         * 在作用域中执行的任务 在界面关闭之后会取消掉任务也就是 继续执行的=== 这个日志不会继续执行
         */
        val job = viewModelScope.launch {
            //协程关了之后会关
            Log.i("net","job之前的===="+Thread.currentThread().name)
            var job = withContext(Dispatchers.IO) {
                Log.i("net","开始执行的===="+Thread.currentThread().name)
                delay(3000)
                val bean = WxArticleBean()
                bean.id = 999
                bean.name = "零先生"
                bean.visible = 1
                ApiSuccessResponse(arrayListOf(bean))
                Log.i("net","继续执行的===="+Thread.currentThread().name)
            }
            Log.i("net","job之后的===="+Thread.currentThread().name + job.toString())
        }
    }
}