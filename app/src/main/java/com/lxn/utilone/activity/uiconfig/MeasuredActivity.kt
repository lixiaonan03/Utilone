package com.lxn.utilone.activity.uiconfig

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.alibaba.android.arouter.facade.annotation.Route
import com.lxn.utilone.R
import com.lxn.utilone.UtilApplication
import com.lxn.utilone.activity.ActivityConstans
import com.lxn.utilone.databinding.ActivityLanguageBinding
import com.lxn.utilone.databinding.ActivityMesuredBinding
import com.lxn.utilone.databinding.TopBackBinding
import com.lxn.utilone.mvvm.base.BaseKtActivity
import com.lxn.utilone.util.Log
import com.lxn.utilone.util.LogUtils
import com.lxn.utilone.util.ToastUtils
import java.util.*

/**
  *  @author 李晓楠
  *  功能描述: View测量宽高学习的
  *  时 间： 2023/5/25 10:59 
  */
@Route(path = ActivityConstans.MEASURE_PATH, name = "view测试宽高学习的")
class MeasuredActivity : BaseKtActivity() {

    private lateinit var binding: ActivityMesuredBinding



    //    protected IBpPageController mBpPageController;
    override fun attachBaseContext(newBase: Context?) { //头条的适配方案解决的
        DensityCompatUtils.setWidthDensity(newBase)
        super.attachBaseContext(newBase)
    }

    override fun getUiMode(): UIType? {
        return UIType.DEFAULT
    }

    override fun getView(): View {
        val displayMetrics = resources.displayMetrics
        val dpi = displayMetrics.densityDpi
        LogUtils.iWithTag("lxn43","之后的===${dpi}=density==${displayMetrics.density}==scaledDensity==${displayMetrics.scaledDensity}")

        binding = ActivityMesuredBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun updateTopBarView(topVb: TopBackBinding, backClick: ((view: View?) -> Unit)?) {
        super.updateTopBarView(topVb, backClick)
        topVb.topText.text = "关于测量布局的学习"
    }

    override fun initView(savedInstanceState: Bundle?) {

        //获取设备dpi

        binding.addView.setOnClickListener{
            showView()
        }
        binding.addView3.setOnClickListener{
            val dialog = MyDialogFragment()

            dialog.show(supportFragmentManager, "dialog")
        }


    }

    /**
     *  展示这个需要权限
     */
    private fun showView(){ //获取WindowManager实例
        //获取WindowManager实例
        val wm = UtilApplication.getInstance().getSystemService(WINDOW_SERVICE) as WindowManager //设置LayoutParams属性
        //设置LayoutParams属性
        val layoutParams = WindowManager.LayoutParams() //宽高尺寸
        //宽高尺寸
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        layoutParams.width = 1000
        layoutParams.format = PixelFormat.TRANSPARENT //设置背景阴暗
        //设置背景阴暗
        layoutParams.flags = layoutParams.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
        layoutParams.dimAmount = 0.6f
        //Window类型
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE
        }
        //构造TextView
        val myView = TextView(this)
        myView.text = "hello window" //设置背景为红色
        //设置背景为红色
        myView.setBackgroundResource(R.color.color_ff3b28) //添加到WindowManager
        val myParam = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 600)
        myParam.gravity = Gravity.CENTER
        myView.layoutParams = myParam

        //myFrameLayout 作为rootView
        val myFrameLayout = FrameLayout(this) //设置背景为绿色
        //设置背景为绿色
        myFrameLayout.setBackgroundColor(Color.GREEN)
        myFrameLayout.addView(myView)

        //添加到window
        wm.addView(myFrameLayout, layoutParams)

    }
}