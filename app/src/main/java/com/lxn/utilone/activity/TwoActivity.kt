package com.lxn.utilone.activity

import android.app.Activity
import android.content.Intent
import android.os.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.lxn.utilone.databinding.ActivityFinishBinding

/**
 * 测试屏幕旋转切换时 onDestroy() 中调用了 finish() 中的造成的影响
 * ps:这个项目跑起来屏幕旋转切换的  https://jun2sn6jvo.feishu.cn/docx/Gve4dVowVoTc6exGBVccLecWnTf
 * @author：李晓楠
 * 时间：2023/2/8 10:08
 */
class TwoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFinishBinding

    /**
     * 通过反射拿到的 mToken
     * mToken 是 Android 的 Activity 类的一个成员变量。它表示 Activity 对象的令牌，是用于标识该 Activity 对象的唯一标识符。
     */
    private var activityToken:IBinder? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        Log.i("lxn12345","onCreate====${this.isFinishing}==${this@TwoActivity}===${savedInstanceState}===")
        super.onCreate(savedInstanceState)
        binding = ActivityFinishBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.text1.setOnClickListener {
            startActivity(Intent(this@TwoActivity,TwoActivity::class.java))
        }
        binding.text2.setOnClickListener {
           finish()
        }
         activityToken = Activity::class.java.getDeclaredField("mToken").run {
            isAccessible = true
            get(this@TwoActivity) as IBinder
        }

    }

    override fun onStart() {
        Log.i("lxn12345","onStart====${this.isFinishing}=====${activityToken}===${this@TwoActivity}")
        super.onStart()
    }

    override fun onStop() {
        Log.i("lxn12345","onStop===${this.isFinishing}===${this@TwoActivity}")
        super.onStop()
    }

    override fun onResume() {
        Log.i("lxn12345","onResume===${this.isFinishing}===${this@TwoActivity}")
        super.onResume()
    }

    override fun onRestart() {
        Log.i("lxn12345","onRestart===${this.isFinishing}===${this@TwoActivity}")
        super.onRestart()
    }

    override fun onPause() {
        Log.i("lxn12345","onPause===${this.isFinishing}===${this@TwoActivity}")
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.i("lxn12345","onSaveInstanceState====${this.isFinishing}==${this@TwoActivity}====${outState.toString()}")
        super.onSaveInstanceState(outState)
    }
    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        Log.i("lxn12345","onSaveInstanceState=111===${this.isFinishing}==${this@TwoActivity}====${outState.toString()}")
        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        Log.i("lxn12345","onRestoreInstanceState===${this.isFinishing}===${this@TwoActivity}====${savedInstanceState.toString()}")
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onDestroy() {
        Log.i("lxn12345","onDestroy===${this.isFinishing}===${this@TwoActivity}")

//        this@TwoActivity.finish()
        super.onDestroy()
        Handler().postDelayed({
              //在这里延迟7s 方便看出问题所在 可以看日志
              this@TwoActivity.finish()
        },7000L)
//        finish()

//        onCreate====false==com.lxn.finishdemo.TwoActivity@4bbbe43===null===
//        onStart====false=====android.os.BinderProxy@2ee00cb===com.lxn.finishdemo.TwoActivity@4bbbe43
//                onResume===false===com.lxn.finishdemo.TwoActivity@4bbbe43
//        onPause===false===com.lxn.finishdemo.TwoActivity@4bbbe43
//        onStop===false===com.lxn.finishdemo.TwoActivity@4bbbe43
//        onSaveInstanceState====false==com.lxn.finishdemo.TwoActivity@4bbbe43====Bundle[{}]
//        onDestroy===false===com.lxn.finishdemo.TwoActivity@4bbbe43
//        onCreate====false==com.lxn.finishdemo.TwoActivity@8c9fbfe===Bundle[]===
//        onStart====false=====android.os.BinderProxy@2ee00cb===com.lxn.finishdemo.TwoActivity@8c9fbfe
//                onRestoreInstanceState===false===com.lxn.finishdemo.TwoActivity@8c9fbfe====
//        onResume===false===com.lxn.finishdemo.TwoActivity@8c9fbfe
//        onPause===true===com.lxn.finishdemo.TwoActivity@8c9fbfe
//        onStop===true===com.lxn.finishdemo.TwoActivity@8c9fbfe
//        onDestroy===true===com.lxn.finishdemo.TwoActivity@8c9fbfe
    }
}