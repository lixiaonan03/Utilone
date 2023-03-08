package com.lxn.exportplugin

import com.google.gson.annotations.SerializedName

/**
 * {
    "actionRules": [
       "android.intent.action.MAIN"
    ],
    "whiteNames": [],
    "blackPackages": [],
    "blackNames": [],
    "blackIgnores": []
   }
 */
data class RulesListBean(
    /**
     * actionRules
    默认判断规则，用于当前没有配置 exported 的修改逻辑,如果当前存在exported,则跳过。

    具体判断逻辑:

    如果 intent-filter - action 对应的 android:name 与 actionRules 中任意一条匹配,则将 exported 修改为 true ,否则为false 。
     */
    @SerializedName("actionRules")
    val actionRules: List<String> = listOf(),
    /**
     * 黑名单 下要忽视的类,在黑名单判断时,如果遇到此类，则使用默认规则 actionRules 判断。
     */
    @SerializedName("blackIgnores")
    val blackIgnores: List<String> = listOf(),

    /**
     * 黑名单 类名合集，如果遇到此合集中的类,并且使用了 intent-filter ,则会将 exported 直接修改为 true 。
     * 判断逻辑会与上面 blackPackages 一起判断,两者满足其一即可。
     */
    @SerializedName("blackNames")
    val blackNames: List<String> = listOf(),
    /**
     * 黑名单 包名合集,对于此包名下的类,如果使用了 intent-filter ,则会将 exported 直接修改为 false
     */
    @SerializedName("blackPackages")
    val blackPackages: List<String> = listOf(),
    /**
     * 白名单类名,如果遇到此类,并且使用了 intent-filter ,则会将 exported 修改为 true
     */
    @SerializedName("whiteNames")
    val whiteNames: List<String> = listOf()
)
