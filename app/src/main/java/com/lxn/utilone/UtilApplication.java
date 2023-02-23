package com.lxn.utilone;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;

import com.alibaba.android.arouter.launcher.ARouter;
import com.dianping.logan.Logan;
import com.dianping.logan.LoganConfig;
import com.lxn.utilone.anr.ANRError;
import com.lxn.utilone.anr.ANRWatchDog;
import com.lxn.utilone.util.Log;
import com.lxn.utilone.util.LogUtils;
import com.lxn.utilone.util.operationutil.ProcessUtils;
import com.pgyer.pgyersdk.PgyerSDKManager;
import com.tencent.mmkv.MMKV;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;

import androidx.annotation.NonNull;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

/**
 * @author lixiaonan
 * 功能描述: application的基础类
 * 时 间： 2020/3/15 上午11:39
 */
public class UtilApplication extends Application {

    public static UtilApplication application = null;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

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
            //等一些第三放应用初始化完了 再绑定到生命周期的
            ProcessLifecycleOwner.get().getLifecycle().addObserver(new ApplicationObserver());
        }

        anrWatchDog
                //anr 异常的
                .setANRListener(new ANRWatchDog.ANRListener() {
                    @Override
                    public void onAppNotResponding(@NonNull ANRError error) {
                        Log.e("ANR-Watchdog-Demo", "Detected Application Not Responding!");

                        // Some tools like ACRA are serializing the exception, so we must make sure the exception serializes correctly
                        try {
                            new ObjectOutputStream(new ByteArrayOutputStream()).writeObject(error);
                        }
                        catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }

                        Log.i("ANR-Watchdog-Demo", "Error was successfully serialized");

                        throw error;
                    }
                })
                //ANR 间隔时间的回调
                .setANRInterceptor(new ANRWatchDog.ANRInterceptor() {
                    @Override
                    public long intercept(long duration) {
                        //剩余多少时间的
                        long ret = UtilApplication.this.duration * 1000 - duration;
                        if (ret > 0) {
                            Log.w("ANR-Watchdog-Demo", "Intercepted ANR that is too short (" + duration + " ms), postponing for " + ret + " ms.");
                        }
                        return ret;
                    }
                })
        ;
        anrWatchDog.start();
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


    /**
     *onConfigurationChanged() 是 Android 中的一个生命周期回调方法，当设备配置发生更改时（例如屏幕旋转，语言环境更改等），系统会调用该方法来通知应用程序。该方法会接收一个 Configuration 对象，表示设备的新配置信息。
     * 通常情况下，当配置发生更改时，Android 会销毁并重新创建当前活动（Activity）以适应新的配置。不过，你可以通过在 AndroidManifest.xml 文件中为活动设置 android:configChanges 属性来指定某些配置发生更改时不销毁活动。
     * 在这种情况下，当配置发生更改时，系统会调用 onConfigurationChanged() 方法，应用程序可以在该方法中进行必要的处理，例如重新加载资源、重新计算布局等。
     *
     * 注意：自己使用代码设置配置改变，暂时不会回调到这里。回调到这里之后，所有的activity回到前台的时候都会带着原来的信息重新创建,用的是新的资源配置
     * @param newConfig
     */
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




    ANRWatchDog anrWatchDog = new ANRWatchDog(2000);

    int duration = 4;

    final ANRWatchDog.ANRListener silentListener = new ANRWatchDog.ANRListener() {
        @Override
        public void onAppNotResponding(@NonNull ANRError error) {
            Log.e("lxnAnr",error.toString());
        }
    };

}
