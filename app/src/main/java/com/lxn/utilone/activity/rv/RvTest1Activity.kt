package com.lxn.utilone.activity.rv

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.lxn.utilone.activity.BaseActivity
import com.lxn.utilone.databinding.ActivityRvBinding
import com.lxn.utilone.databinding.ActivityRvTest1Binding

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
}