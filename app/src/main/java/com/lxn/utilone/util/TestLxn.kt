package com.lxn.utilone.util

import com.lxn.utilone.util.jvm.hashMapOf2

/**
 * @author：李晓楠
 * 时间：2023/1/18 15:52
 */
object TestLxn {
    @JvmStatic
    fun main(vararg args: String) {
        val maptest1 = hashMapOf(
            "a" to 1,
            "b" to 2,
            "c" to 3,
        )

        val maptest = hashMapOf2<String,String>{
            "a" toValue "1"
            "b" toValue "2"
            "c" toValue "3"
        }
        println(maptest.toString())
        for ((key,value) in maptest){
            println("$key=====${value}")
        }

    }
}