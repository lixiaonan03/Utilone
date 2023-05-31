package com.lxn.utilone.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.LinearLayout

/**
 * @author：李晓楠
 * 时间：2023/5/6 18:20
 */
class MyActivitylin(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs)  {
    init {
        // 在这里对TextView进行自定义的初始化操作
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        Log.i("lxn436","自定义线性 MyActivitylin=的宽度===${MeasureSpec.getSize(widthMeasureSpec)}=高度===${MeasureSpec.getSize(heightMeasureSpec)}}")
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}