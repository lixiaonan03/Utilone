package com.lxn.utilone.activity.rv

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lxn.utilone.activity.BaseActivity
import com.lxn.utilone.databinding.ActivityRvBinding
import com.lxn.utilone.databinding.ActivityRvTest1Binding
import com.lxn.utilone.databinding.ActivityRvTest2Binding
import com.lxn.utilone.util.LogUtils
import com.lxn.utilone.util.dp
import com.lxn.utilone.util.dpf

/**
 *  @author 李晓楠
 *  功能描述: 使用 学习练习的自定义layoutmanager的
 *  时 间： 2023/2/6 11:34
 */
class RvTest2Activity : BaseActivity() {

    companion object {
        fun startActivity(context: Context?) {
            val intent = Intent(context, RvTest2Activity::class.java)
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            context?.startActivity(intent)
        }
    }

    private lateinit var testAdapter: Test1Adapter
    private lateinit var vb: ActivityRvTest2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivityRvTest2Binding.inflate(layoutInflater)
        setContentView(vb.root)
        vb.linTop.topText.text = "学习练习的自定义layoutmanager"

        vb.tvGet.setOnClickListener {
           LogUtils.iWithTag("lxnRv","测试=${vb.rv.height}=")
        }
        vb.tvScroll.setOnClickListener {
            vb.rv.smoothScrollToPosition(15)
        }
        vb.rv.apply {
            this.adapter = this@RvTest2Activity.adapter
            layoutManager = WaterfallLayoutManager(2)

            addItemDecoration(object: RecyclerView.ItemDecoration() {
                private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    style = Paint.Style.STROKE
                    strokeWidth = 0.5.dpf
                    color = 0xFF777777.toInt()
                }

                override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                    for (i in 0 until parent.childCount) {
                        val child = parent.getChildAt(i)
                        c.drawRect(
                            child.left + 0F,
                            child.top + 0F,
                            child.right + 0F,
                            child.bottom + 0f,
                            paint
                        )
                    }
                }
            })
        }

    }

    private class TextHolder(tv: TextView) : RecyclerView.ViewHolder(tv) {
        @JvmField
        val textView = itemView as TextView
    }

    private data class Bean(
        val text: String,
        val height: Int,
    )


    private val adapter = Adapter()

    private class Adapter : RecyclerView.Adapter<TextHolder>() {

        val dataSet = ArrayList<Bean>(32).apply {
            val heightEnum = arrayOf(
                151.dp,
                197.dp,
                209.dp,
                85.dp,
                134.dp,
            )

            for (i in 0 until 32) {
                val bean = Bean(
                    text = i.toString(),
                    height = heightEnum[i % heightEnum.size],
                )
                add(bean)
            }
        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextHolder {
            LogUtils.iWithTag("lxnRv","onCreateViewHolder=${parent}==")

            val tv = TextView(parent.context).apply {
                layoutParams = ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                )
                gravity = Gravity.CENTER
                textSize = 22F
            }
            return TextHolder(tv)
        }

        override fun onBindViewHolder(holder: TextHolder, position: Int) {
            LogUtils.iWithTag("lxnRv","onBindViewHolder=${position}==")

            val bean = dataSet[position]
            holder.textView.text = bean.text
            holder.itemView.layoutParams.height = bean.height
        }

        override fun getItemCount(): Int {
           return dataSet.size
        }

        override fun onViewAttachedToWindow(holder: TextHolder) {
            LogUtils.iWithTag("lxnRv","onViewAttachedToWindow=${holder.adapterPosition}=layoutPosition=${holder.layoutPosition}===oldPosition=${holder.oldPosition}==")
            super.onViewAttachedToWindow(holder)
        }

        override fun onViewDetachedFromWindow(holder: TextHolder) {
            LogUtils.iWithTag("lxnRv","=onViewDetachedFromWindow=${holder.adapterPosition}=layoutPosition=${holder.layoutPosition}===oldPosition=${holder.oldPosition}==")

            super.onViewDetachedFromWindow(holder)
        }
    }
}