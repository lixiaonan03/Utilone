package com.lxn.utilone.activity

import android.content.ComponentCallbacks
import android.content.Context
import android.content.DialogInterface
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.compose.ui.window.Dialog
import com.alibaba.android.arouter.facade.annotation.Route
import com.lxn.utilone.databinding.ActivitySizeViewBinding
import com.lxn.utilone.databinding.ActivityTest1Binding
import com.lxn.utilone.util.Log
import com.lxn.utilone.util.operationutil.AdaptScreenUtils
import com.lxn.utilone.util.operationutil.ScreenUtils

/**
  *  @author 李晓楠
  *  功能描述: 屏幕适配版本学习的  https://jun2sn6jvo.feishu.cn/docx/X2zXdQ9cSoC9EAxdBd4cIExCnX4
  *  时 间： 2022/10/19 14:59 
  */
class Test1Activity : BaseActivity() {

    private lateinit var binding: ActivityTest1Binding


    private var sNoncompatDensity = 0f
    private var sNoncompatScaledDensity = 0f
    private var sNoncompatDensityDpi = 0

    //基准屏幕宽度的
    private val sWidthDp = 360f //屏幕宽度dp = px / (density / 160)

    private val sHeightDp = 640f //屏幕高度dp

    override fun onCreate(savedInstanceState: Bundle?) {
        //根据头条的方案设置的屏幕适配方案的



        super.onCreate(savedInstanceState)

        binding = ActivityTest1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.linTop.topText.text = "新的"

        resources.displayMetrics

    }
}