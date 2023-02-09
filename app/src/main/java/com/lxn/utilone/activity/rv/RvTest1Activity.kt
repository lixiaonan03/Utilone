package com.lxn.utilone.activity.rv

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.lxn.utilone.R
import com.lxn.utilone.activity.BaseActivity
import com.lxn.utilone.databinding.ActivityRvTest1Binding
import com.lxn.utilone.util.operationutil.Utils

/**
  *  @author 李晓楠
  *  功能描述: NestedScrollView嵌套RV的
  *  https://jun2sn6jvo.feishu.cn/docx/doxcnH1JgqmT9i2fx79fU0tQRzq
  *  时 间： 2023/1/31 17:20
  */
class RvTest1Activity : BaseActivity(){

     companion object {
        fun startActivity(context: Context?) {
            val intent = Intent(context, RvTest1Activity::class.java)
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            context?.startActivity(intent)
        }
    }



    private lateinit var testAdapter: Test1Adapter
    private lateinit var vb: ActivityRvTest1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivityRvTest1Binding.inflate(layoutInflater)
        setContentView(vb.root)
        vb.linTop.topText.text = "NestedScrollView嵌套RV的"

        vb.tvGet.setOnClickListener {
            showView()
        }

        testAdapter = Test1Adapter()
        // NestedScrollView嵌套RV的情况下  rvItem的复用会失效 会在第一次加载时把所有的item 都加载出来
        // 如果使用了 HeaderScrollView 和 HeaderScrollView1 都能处理不复用的问题
        vb.rv.apply {
            this.layoutManager  = LinearLayoutManager(this@RvTest1Activity)
//            this.isNestedScrollingEnabled = false
            this.adapter  =testAdapter
        }
        val list: ArrayList<Int> = ArrayList()
        for (i in 0 .. 100){
            list.add(i)
        }
        testAdapter.setList(list)
    }

    override fun onDestroy() {
        super.onDestroy()
    }


    private lateinit var wm: WindowManager
    fun showView(){ //获取WindowManager实例
        //获取WindowManager实例
        wm = getSystemService(WINDOW_SERVICE) as WindowManager

        //设置LayoutParams属性
        val layoutParams = WindowManager.LayoutParams()
        //宽高尺寸
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
        layoutParams.format = PixelFormat.TRANSPARENT

        //设置背景阴暗
        layoutParams.flags = layoutParams.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
        layoutParams.dimAmount = 0.6f

        //Window类型
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }

        //构造TextView
        //构造TextView
        val myView = TextView(this)
        myView.text = "hello window" //设置背景为红色
        //设置背景为红色
        myView.setBackgroundResource(R.color.common_red)
        val myParam: FrameLayout.LayoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 400)
        myParam.gravity = Gravity.CENTER
        myView.layoutParams = myParam

        //myFrameLayout 作为rootView

        //myFrameLayout 作为rootView
        val myFrameLayout = FrameLayout(this) //设置背景为绿色
        //设置背景为绿色
        myFrameLayout.setBackgroundColor(Color.GREEN)
        myFrameLayout.addView(myView)

        //添加到window
        (window.decorView as ViewGroup).addView(myFrameLayout, layoutParams)
    }
}