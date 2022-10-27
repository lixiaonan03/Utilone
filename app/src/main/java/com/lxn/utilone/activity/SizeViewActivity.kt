package com.lxn.utilone.activity

import android.content.ComponentCallbacks
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.compose.ui.window.Dialog
import com.alibaba.android.arouter.facade.annotation.Route
import com.lxn.utilone.databinding.ActivitySizeViewBinding
import com.lxn.utilone.util.Log
import com.lxn.utilone.util.operationutil.AdaptScreenUtils
import com.lxn.utilone.util.operationutil.ScreenUtils

/**
  *  @author 李晓楠
  *  功能描述: 屏幕适配版本学习的  https://jun2sn6jvo.feishu.cn/docx/X2zXdQ9cSoC9EAxdBd4cIExCnX4
  *  时 间： 2022/10/19 14:59 
  */
@Route(path = ActivityConstans.View_PATH, name = "适配学习的")
class SizeViewActivity : BaseActivity() {

    private lateinit var binding: ActivitySizeViewBinding


    private var sNoncompatDensity = 0f
    private var sNoncompatScaledDensity = 0f
    private var sNoncompatDensityDpi = 0

    //基准屏幕宽度的
    private val sWidthDp = 360f //屏幕宽度dp = px / (density / 160)

    private val sHeightDp = 640f //屏幕高度dp

    override fun onCreate(savedInstanceState: Bundle?) {
        //根据头条的方案设置的屏幕适配方案的



        super.onCreate(savedInstanceState)

        binding = ActivitySizeViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.linTop.topText.text = "适配学习的"


        //获取导航栏高度
        val navheight = ScreenUtils.getNavigationBarHeight()
        Log.i("lxnview","导航栏高度===$navheight")


        binding.tvToutiao.setOnClickListener {
            val msg = "是否启用头条修改方案"
            AlertDialog.Builder(this)
                .setMessage(msg)
                .setPositiveButton("确定") { _, i ->
                    setDensity(this@SizeViewActivity, true)
                }.setNeutralButton("取消", null)
                .create()
                .show()


        }

        binding.goIntent.setOnClickListener {
             startActivity(Intent(this,Test1Activity::class.java))
        }
    }


    fun setHeightDensity(context: Context) {
        setDensity(context, false)
    }

    /**
     * 重新设置density 值进行适配的
     */
    fun setDensity(context: Context, isWidth: Boolean) {
        val application = context.applicationContext
        val displayMetrics = context.resources.displayMetrics
        if (sNoncompatDensity == 0f) {
            sNoncompatDensity = displayMetrics.density
            sNoncompatScaledDensity = displayMetrics.scaledDensity
            sNoncompatDensityDpi = displayMetrics.densityDpi
            application.registerComponentCallbacks(object : ComponentCallbacks {
                override fun onConfigurationChanged(newConfig: Configuration) {
                    if (newConfig != null && newConfig.fontScale > 0) {
                        sNoncompatScaledDensity = application.resources.displayMetrics.scaledDensity
                    }
                }

                override fun onLowMemory() {}
            })
        }
        val targetDensity: Float
        if (isWidth) {
            targetDensity = displayMetrics.widthPixels / sWidthDp
        } else {
            targetDensity = displayMetrics.heightPixels / sHeightDp
        }
        val targetScaledDensity: Float = targetDensity * (sNoncompatScaledDensity / sNoncompatDensity)
        val targetDensityDpi = (160 * targetDensity).toInt()
        displayMetrics.density = targetDensity
        displayMetrics.scaledDensity = targetScaledDensity
        displayMetrics.densityDpi = targetDensityDpi
    }


//    /**
//     * 适配pt 方案的
//     */
//    override fun getResources(): Resources {
//        return AdaptScreenUtils.adaptWidth(super.getResources(),320);
//    }
}