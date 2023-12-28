package com.lxn.utilone.activity.ui

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
import com.lxn.utilone.databinding.ActivityUisdkBinding
import com.lxn.utilone.util.operationutil.Utils


class UISDkActivity : BaseActivity() {

    companion object {
        fun startActivity(context: Context?) {
            val intent = Intent(context, UISDkActivity::class.java)
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            context?.startActivity(intent)
        }
    }

    private lateinit var vb: ActivityUisdkBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivityUisdkBinding.inflate(layoutInflater)
        setContentView(vb.root)
        vb.linTop.topText.text = "三方sdkUI的学习"
        vb.linTop.topBack.setOnClickListener {
            finish()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
    }


}