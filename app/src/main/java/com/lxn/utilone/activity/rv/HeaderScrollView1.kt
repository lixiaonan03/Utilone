package com.lxn.utilone.activity.rv

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.widget.NestedScrollView

/**
  *  @author 李晓楠
  *  功能描述: 最简单的方式处理 NestedScrollView 嵌套  RecyclerView 导致rv复用失效的
  *  时 间： 2023/2/7 14:36
  */
class HeaderScrollView1 @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : NestedScrollView(context, attrs, defStyleAttr) {
    override fun measureChildWithMargins(
        child: View, parentWidthMeasureSpec: Int, widthUsed: Int, parentHeightMeasureSpec: Int, heightUsed: Int
    ) {
        val lp = child.layoutParams as MarginLayoutParams
        val childWidthMeasureSpec = getChildMeasureSpec(
            parentWidthMeasureSpec, paddingLeft + paddingRight + lp.leftMargin + lp.rightMargin + widthUsed, lp.width
        )
        val childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
            lp.topMargin + lp.bottomMargin, MeasureSpec.UNSPECIFIED
        )
        //--> NestedScrollView测量子View时使用的测量模式为MeasureSpec.UNSPECIFIED
        //--> 子view大小不受限制，即Recycler View的高度就是加载了全部item的大小 (导致一开始就创建了全部的item)
        //--> 当前的数据都创建展示了，触发onLoadMore事件-->加载完后又全部item创建并开始促发下次onLoadMore事件，直到没有更多数据为止。
        // 只需要NestedScrollView测量RecyclerView不使用MeasureSpec.UNSPECIFIED模式即可。因此可以重写measureChildWithMargins。
        child.measure(childWidthMeasureSpec, parentHeightMeasureSpec)
    }
}