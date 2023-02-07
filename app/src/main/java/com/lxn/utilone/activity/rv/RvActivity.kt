package com.lxn.utilone.activity.rv

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.lxn.utilone.activity.BaseActivity
import com.lxn.utilone.databinding.ActivityRvBinding

/**
  *  @author 李晓楠
  *  功能描述: rv 学习测试
  *  时 间： 2023/1/30 15:24
  */
class RvActivity : BaseActivity(){

     companion object {
        fun startActivity(context: Context?) {
            val intent = Intent(context, RvActivity::class.java)
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            context?.startActivity(intent)
        }
    }

    private lateinit var vb: ActivityRvBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivityRvBinding.inflate(layoutInflater)
        setContentView(vb.root)
        vb.linTop.topText.text = "Recyclerview 学习测试"

        vb.tvTest1.setOnClickListener {
            RvTest1Activity.startActivity(this@RvActivity)
        }
        vb.tvTest2.setOnClickListener {
            RvTest2Activity.startActivity(this@RvActivity)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
    }
}