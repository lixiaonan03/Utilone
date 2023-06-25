package com.lxn.utilone.activity.architecture

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.lxn.utilone.activity.BaseActivity
import com.lxn.utilone.databinding.ActivityArchitectureBinding
import com.lxn.utilone.databinding.ActivityRvBinding
import com.lxn.utilone.databinding.ActivitySuanfaBinding

/**
  *  @author 李晓楠
  *  功能描述: 架构学习的
  *  时 间： 2023/6/20 15:55
  */
class ArchitectureActivity : BaseActivity(){

     companion object {
        fun startActivity(context: Context?) {
            val intent = Intent(context, ArchitectureActivity::class.java)
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            context?.startActivity(intent)
        }
    }

    private lateinit var vb: ActivityArchitectureBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivityArchitectureBinding.inflate(layoutInflater)
        setContentView(vb.root)
        vb.linTop.topText.text = "架构学习的"

        //mvi 架构学习的
        vb.tvMVi.setOnClickListener {
            FlowLoginActivity.startActivity(this@ArchitectureActivity)
        }

    }
}