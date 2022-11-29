package com.lxn.utilone.mvvm.bean

/**
  *  @author 李晓楠
  *  功能描述: 模拟网络接口实体类
  *  时 间： 2022/10/27 15:20
  */
data class User(
        val admin: Boolean?,
        val chapterTops: List<Any>?,
        val email: String?,
        val icon: String?,
        val id: Int?,
        val nickname: String?,
        val publicName: String?,
        val username: String?
)