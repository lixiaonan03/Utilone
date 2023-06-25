package com.lxn.utilone.activity.architecture

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.reflect.KProperty1

/**
 * mvi 处理状态的扩展函数
 * @author：李晓楠
 * 时间：2023/6/25 14:15
 */


//这段代码是 Kotlin 语言编写的。它定义了一个扩展函数  observeState ，用于在Android中观察StateFlow的状态变化。该函数接收三个参数： lifecycleOwner  表示生命周期拥有者， prop1  表示  StateFlow  的属性， action  表示状态变化后要执行的操作。
// 函数内部通过  lifecycleOwner.lifecycleScope.launch  来启动一个协程，并通过
// repeatOnLifecycle  来确保协程在  STARTED  状态下执行。然后通过  map  函数将  StateFlow  对象转换成一个  StateTuple1  对象，再通过
// distinctUntilChanged  函数过滤掉重复的状态变化，最后通过  collect  函数来订阅数据流并获取数据，执行  action  中定义的操作
fun <T, A> StateFlow<T>.observeState(
        lifecycleOwner: LifecycleOwner,
        prop1: KProperty1<T, A>,
        action: (A) -> Unit
) {
    lifecycleOwner.lifecycleScope.launch {
        //关联的 Lifecycle 至少处于 STARTED 状态时运行，并且会在 Lifecycle 处于 STOPPED 状态时取消运行：
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            this@observeState.map {
                StateTuple1(prop1.get(it))
            }.distinctUntilChanged().collect { (a) ->
                action.invoke(a)
            }
        }
    }
}


fun <T> SharedFlow<List<T>>.observeEvent(lifecycleOwner: LifecycleOwner, action: (T) -> Unit) {
    lifecycleOwner.lifecycleScope.launch {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            this@observeEvent.collect {
                it.forEach { event ->
                    action.invoke(event)
                }
            }
        }
    }
}


internal data class StateTuple1<A>(val a: A)



//设置值
fun <T> MutableStateFlow<T>.setState(reducer: T.() -> T) {
    this.value = this.value.reducer()
}

//发送各个事件
suspend fun <T> SharedFlowEvents<T>.setEvent(vararg values: T) {
    val eventList = values.toList()
    this.emit(eventList)
}



//什么是typealias. 在Kotlin 源码中遇到长签名的表达式多多少少都会使用 typealias ，它的作用就是给类取一个别名。 通过 typealias 关键字，给 String 类型取了一个别名 Password ，接下来就可以像使用 String 来使用 Password
typealias SharedFlowEvents<T> = MutableSharedFlow<List<T>>

@Suppress("FunctionName")
fun <T> SharedFlowEvents(): SharedFlowEvents<T> {
    //SharedFlow 和 StateFlow 相比，他有缓冲区区，并可以定义缓冲区的溢出规则，已经可以定义给一个新的接收器发送多少数据的缓存值。
    return MutableSharedFlow()
}