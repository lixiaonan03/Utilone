package com.lxn.utilone.activity

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.ColorFilter
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.os.Bundle
import android.os.Process
import android.view.View
import com.lxn.utilone.databinding.ActivityDevBinding
import com.lxn.utilone.util.ToastUtils
import kotlin.system.exitProcess


/**
 *  @author 李晓楠
 *  功能描述: 开发者页面
 *  时 间： 2023/1/5 15:49
 */
class DevActivity : BaseActivity() {

    private lateinit var binding: ActivityDevBinding


    override fun onCreate(savedInstanceState: Bundle?) { //根据头条的方案设置的屏幕适配方案的


        super.onCreate(savedInstanceState)

        binding = ActivityDevBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //模拟重启应用的
        binding.tvReStartApp.setOnClickListener {
            ToastUtils.toastshort("应用重启中")

            // 一种重启方式
//            val intent = packageManager.getLaunchIntentForPackage(application.packageName)
//            val restartIntent: PendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)
//            val mgr: AlarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
//            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 0, restartIntent) // 1秒钟后重启应用
//            exitProcess(0)

            //另一种
            val i = packageManager.getLaunchIntentForPackage(packageName)
            if (i != null) {
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(i)
                Process.killProcess(Process.myPid())
            }
        }

        //整个页面置灰的效果
        binding.tvGrey.setOnClickListener {
            val paint = Paint()
            val colorMatrix = ColorMatrix()
            //设置灰度 0最灰  1最亮
            colorMatrix.setSaturation(0.1f)
            paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
            window.decorView.setLayerType(View.LAYER_TYPE_HARDWARE,paint)
        }
    }
}