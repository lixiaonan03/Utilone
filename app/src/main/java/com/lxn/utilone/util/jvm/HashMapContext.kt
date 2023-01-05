package com.lxn.utilone.util.jvm

/**
 * 对 hashMapOf()  方法改进
 * @author：李晓楠
 * 时间：2023/1/4 15:23
 */
@JvmInline

value class HashMapContext<K,V>(
    val map:HashMap<K,V>
){
   inline fun K.to(value: V) {
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
 *  val maptest = hashMapOf2(
        "a" to 1,
        "b" to 2,
        "c" to 3,
        )
 */
inline fun <K,V> hashMapOf2(block:HashMapContext<K,V>.() -> Unit) = HashMap<K,V>().apply {
    HashMapContext(this).block()


}