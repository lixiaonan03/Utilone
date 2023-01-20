package com.lxn.utilone.util.jvm

import com.lxn.utilone.util.LogUtils

/**
 * 对 hashMapOf()  方法改进      @JvmInline 结合 value 可以指定一个类为内联类，需结合value一起使用
 * 内联类构造参数中有且只能有一个成员变量，最终被内联到字节码中的value。 简单说就是这个 JVM中不会有 HashMapContext 这个的类信息
 * @author：李晓楠
 * 时间：2023/1/4 15:23
 */
@JvmInline
value class HashMapContext<K,V>(val map:HashMap<K,V>){

//    infix函数需要几个条件:  https://www.imgeek.org/article/825358025
//    - 只有一个参数
//    - 在方法前必须加infix关键字
//    - 必须是成员方法或者扩展方法
   infix fun K.toValue(value: V) {
       map[this] = value
   }
}

/**
 * 众所周知，写埋点的时候经常会用这种方式，但这种方式实际上会创建好几个对象，在性能要求苛刻时会造成卡顿
 *    val maptest = hashMapOf(
        "a" to 1,
        "b" to 2,
        "c" to 3,
      )
 * 每调一次 to，实际上就会创建一个 Pair，可变参数实际上是数组，上面那种写法实际上创建了 4 个不必要的对象 多创建了 3 个 Pair 和一个 Pair[]
 *
 *  val maptest = hashMapOf2<String,Int>{
        "a" toValue 1
        "b" toValue 2
        "c" toValue 3
        }
 */
inline fun <K,V> hashMapOf2(block:HashMapContext<K,V>.() -> Unit) = HashMap<K,V>().apply {
    HashMapContext(this).block()
}