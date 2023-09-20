package com.lxn.utilone.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jaeger.library.StatusBarUtil;
import com.lxn.utilone.R;
import com.lxn.utilone.hook.InstrumentationProxy;
import com.lxn.utilone.util.Log;

import java.lang.reflect.Field;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.splashscreen.SplashScreen;

/**
 * @author lixiaonan
 * 功能描述: 引导页处理的
 * 时 间： 2021/11/26 12:26 PM
 */
public class WelcomeActivity extends BaseActivity {


    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        int taskId = getTaskId();
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.AppTask> tasks = am.getAppTasks(); // 获取所有应用任务列表
        int numActivitiesInTask = 0;
        for (ActivityManager.AppTask taskInfo : tasks) {
            Log.i("lxnPush", "taskInfo=="+taskInfo.getTaskInfo().taskId+" === "+taskInfo.getTaskInfo().toString());
            if (taskInfo.getTaskInfo().taskId == taskId) {
                numActivitiesInTask = taskInfo.getTaskInfo().numActivities;
                break;
            }
        }

        Log.i("lxnPush", "欢迎页的=taskid="+taskId+"="+numActivitiesInTask+"=="+getIntent().toString());

        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        splashScreen.setKeepOnScreenCondition(() -> true);
        setContentView(R.layout.activity_welcome);

        //设置状态栏沉浸的   https://blog.csdn.net/guolin_blog/article/details/51763825
        StatusBarUtil.setTranslucent(this,0);
        splashScreen.setKeepOnScreenCondition(() -> false);
        replaceActivityInstrumentation(this);

        ImageView ivGoMain= findViewById(R.id.ivWelcome);
        ivGoMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
//                startActivity(intent);
                ARouter.getInstance().build(ActivityConstans.MAIN_PATH).navigation();
                finish();
            }
        });
        //测试ANR 代码的
//        startWatchDog();
//        try {
//            Thread.sleep(6000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        Handler handler1 = new Handler(Looper.getMainLooper());
        Log.i("lxnHandle","hanlder1===="+ handler1.getClass().toString());

        Message message = Message.obtain(handler);
        message.what = 2;
        handler.sendMessageDelayed(message,2000000);
        Log.i("lxnHandle","handler===="+ handler.getClass().toString());
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


    private Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            try {
                Log.i("lxnHandle","执行的===");
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

}
