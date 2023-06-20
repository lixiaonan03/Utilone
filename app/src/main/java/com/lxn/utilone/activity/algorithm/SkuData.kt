package com.lxn.utilone.activity.algorithm

/**
 * @author：李晓楠
 * 时间：2023/6/7 14:59
 */


data class Sku(
        /**
         * 颜色组的标识
         */
        val color: Int,
        // 发货方式
        val session: Int,
        // 尺寸大小的标识
        val size: Int,
        // sku的ID
        val id: Int,
        // 库存占比
        val stock: Int
)


data class Data(
        // 颜色组
        val colorList: List<Int> = arrayListOf(),
        // 发货组
        val sessions: List<Int> = arrayListOf(),
        // 尺寸组
        val sizeList: List<Int> = arrayListOf(),
        // sku的集合
        var skuList: List<Sku> = arrayListOf(),
        // 处理完的集合
        var state: Result = Result()
)

/**
 * 处理完的数据
 */
data class Result(
        var size: HashMap<Int, HashSet<Int>> = hashMapOf(),
        var color: HashMap<Int, HashSet<Int>> = hashMapOf(),
        var session: HashMap<Int, HashSet<Int>> = hashMapOf(),

        //售罄的sku ID的集合
        var soldout: HashSet<Int> = hashSetOf(),
        var selectColor: Int = 0,
        var selectSize: Int = 0,
        var selectSession: Int = 0,
)
