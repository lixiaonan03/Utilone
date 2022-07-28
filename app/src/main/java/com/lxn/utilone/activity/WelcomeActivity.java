package com.lxn.utilone.activity;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.lxn.utilone.R;
import com.lxn.utilone.hook.InstrumentationProxy;
import com.lxn.utilone.util.Log;

import java.lang.reflect.Field;

/**
 * @author lixiaonan
 * 功能描述: 引导页处理的
 * 时 间： 2021/11/26 12:26 PM
 */
public class WelcomeActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome);

        replaceActivityInstrumentation(this);

        ImageView ivGoMain= findViewById(R.id.ivWelcome);
        ivGoMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
//                startActivity(intent);
                ARouter.getInstance().build(ActivityConstans.MAIN_PATH).navigation();
            }
        });
        //
        startWatchDog();
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 替换跳转的变量的
     * @param activity
     */
    public void replaceActivityInstrumentation(Activity activity){
        try {
            Field field = Activity.class.getDeclaredField("mInstrumentation");
            //取消权限检查的
            field.setAccessible(true);
            Instrumentation instrumentation = (Instrumentation)field.get(activity);
            InstrumentationProxy instrumentationProxy =new InstrumentationProxy(instrumentation);
            field.set(activity,instrumentationProxy);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 最简单的 ANR 实现方案
     * 启动一个线程去 去查看 anr 变量在5s 之内是否会变成false
     * 如果变成了false 就没发生anr
     */
    private void startWatchDog() {
        Thread anrThread = new Thread(){
            @Override
            public void run() {
                super.run();
                while (true){
                    looper();
                }
            }
            private volatile boolean anrFlag;
            //当前这个handle 还是在主线程 这个handle还是主的
            private Handler handle = new Handler(Looper.myLooper());
            private void looper(){
                anrFlag =true;
                handle.post(() -> anrFlag = false);
                SystemClock.sleep(5000);
                //5秒之后 雷没有拆掉就是有问题
                if (anrFlag) {
                    Log.w("ANR-Watchdog-Demo1", "loop: ANR!!");
                }
            }
        };
        anrThread.start();
    }
}
