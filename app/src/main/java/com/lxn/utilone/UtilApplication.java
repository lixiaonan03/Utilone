package com.lxn.utilone;

import android.app.Activity;
import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Debug;

import com.alibaba.android.arouter.launcher.ARouter;
import com.dianping.logan.Logan;
import com.dianping.logan.LoganConfig;
import com.lxn.utilone.util.LogUtils;
import com.lxn.utilone.util.operationutil.ProcessUtils;
import com.tencent.mmkv.MMKV;

import java.io.File;

import androidx.annotation.NonNull;

/**
 * @author lixiaonan
 * 功能描述: application的基础类
 * 时 间： 2020/3/15 上午11:39
 */
public class UtilApplication extends Application {

    public static UtilApplication application = null;


    @Override
    public void onCreate() {
        super.onCreate();

        //在主进程添加第三方的一些数据初始化的
        if (ProcessUtils.isMainProcess()) {
            application = this;
            //注册activity 的生命周期
            initActivityLife();
            initARouter();
            initLogan();

            MMKV.initialize(this);
        }
    }

    private void initARouter(){
//        if (Debug.()) {
//            ARouter.openLog();
//            ARouter.openDebug();
//        }
        // 尽可能早，推荐在Application中初始化
        ARouter.init(application);
    }

    public static UtilApplication getInstance() {
        return application;
    }


    /**
     * 美团的logan实现
     */
    private void initLogan(){
        LoganConfig config = new LoganConfig.Builder()
                .setCachePath(getApplicationContext().getFilesDir().getAbsolutePath())
                .setPath(getApplicationContext().getExternalFilesDir(null).getAbsolutePath()
                        + File.separator + "logan_v1")
                .setEncryptKey16("".getBytes())
                .setEncryptIV16("".getBytes())
                .build();
        Logan.init(config);
    }
    /**
     * 注册activity的生命周期监听
     */
    private int count = 0;

    private void initActivityLife() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {
                if (0 == count) {
                    LogUtils.iLi(">>>>>>>>>>>>>>>>>>>切到前台  lifecycle");
                }
                count++;
            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {
                //判断下离开时常 如果大于需要验证的  判断是否需要密码验证

            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {
                count--;
                if (0 == count) {
                    // 程序切换到了后台之后 会调用 可以做一些处理工作
                    LogUtils.iLi(">>>>>>>>>>>>>>>>>>>切到后台  lifecycle");
                }
            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {

            }
        });
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1) {
            //非默认值
            getResources();
        }
        super.onConfigurationChanged(newConfig);
    }

    /**
     * 处理修改系统字体大小对应用内部的布局有影响  强制把他设置为默认的
     */
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {
            //非默认值
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();//设置默认
            res.updateConfiguration(newConfig, res.getDisplayMetrics());
        }
        return res;
    }

}
