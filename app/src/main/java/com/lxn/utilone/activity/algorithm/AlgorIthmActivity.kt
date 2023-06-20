package com.lxn.utilone.activity.algorithm

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.lxn.utilone.activity.BaseActivity
import com.lxn.utilone.databinding.ActivityRvBinding
import com.lxn.utilone.databinding.ActivitySuanfaBinding

/**
  *  @author 李晓楠
  *  功能描述: 算法学习测试
  *  时 间： 2023/6/7 14:30
  */
class AlgorIthmActivity : BaseActivity(){

     companion object {
        fun startActivity(context: Context?) {
            val intent = Intent(context, AlgorIthmActivity::class.java)
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            context?.startActivity(intent)
        }
    }

    private lateinit var vb: ActivitySuanfaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivitySuanfaBinding.inflate(layoutInflater)
        setContentView(vb.root)
        vb.linTop.topText.text = "算法学习计算的"

        //算法学习计算
        vb.tvSku.setOnClickListener {
            SkuActivity.startActivity(this@AlgorIthmActivity)
        }

    }
}