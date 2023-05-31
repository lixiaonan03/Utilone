package com.lxn.utilone.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout

/**
 * @author：李晓楠
 * 时间：2023/5/6 18:20
 */
class MyFrame(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {
    init {
        // 在这里对TextView进行自定义的初始化操作
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMyFrameLog(widthMeasureSpec, heightMeasureSpec)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    fun setMyFrameLog(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        Log.i("lxn437", "自定义帧布局 MyFrame=的宽度=${MeasureSpec.getMode(widthMeasureSpec)}==${MeasureSpec.getSize(widthMeasureSpec)}=高度==${MeasureSpec.getMode(heightMeasureSpec)}==${MeasureSpec.getSize(heightMeasureSpec)}}")

    }
}