package com.lxn.utilone;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.volley.util.VolleyUtil;

import com.alipay.euler.andfix.patch.PatchManager;
import com.android.volley.RequestQueue;
import com.lxn.utilone.util.BadHandler;
import com.lxn.utilone.util.DeviceUtil;
import com.lxn.utilone.util.LogUtils;
import com.lxn.utilone.util.PreferencesUtil;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created by lxn on 2015/12/7.
 */
public class UtilApplication extends Application {
    public static UtilApplication application = null;
    public static final Logger logger = LoggerFactory.getLogger(UtilApplication.class);
    public static RequestQueue requestQueue;
    public static boolean isLogin =false;
    public static String usernamelogin;// 原生用户名
    public static String userimgurlrlogin;//原生登录 用户头像地址
    public static int userid;//人员id

    private PatchManager patchManager;
    private static final String APATCH_PATH = "/lixiaonan/out.apatch";



    public int count = 0;//判断程序在前后台的 activity个数的标记
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        PreferencesUtil.init(application);
        BadHandler.getInstance().init(application);
        //初始化数据库
        initImageLoader(application);
        //网路请求的请求队列
        requestQueue= VolleyUtil.getInstance();

       //inithotfix();

        //注册activity 的生命周期
        initactivitylife();
    }

    public static UtilApplication getInstance() {
        return application;
    }


    /**
     * 注册activity的生命周期监听
     */
    private void initactivitylife(){
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                if (count == 0) {
                    LogUtils.i(">>>>>>>>>>>>>>>>>>>切到前台  lifecycle");

                }
                count++;
            }

            @Override
            public void onActivityResumed(Activity activity) {
                  //判断下离开时常 如果大于需要验证的  判断是否需要密码验证

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                count--;
                if (count == 0) {
                   //TODO 程序切换到了后台之后 会调用 可以做一些处理工作
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }



    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1){
            //非默认值
            getResources();
        }
        super.onConfigurationChanged(newConfig);
    }
    /**
     * 处理修改系统字体大小对应用内部的布局有影响  强制把他设置为默认的
     * @return
     */
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {//非默认值
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();//设置默认
            res.updateConfiguration(newConfig, res.getDisplayMetrics());
        }
        return res;
    }
    /**
     * 初始化Imageloader
     * @param context
     */
    public static void initImageLoader(Context context) {
        // 这个是你希望的缓存文件的目录：
       // File cacheDir = StorageUtils.getOwnCacheDirectory(context, "/xyyy/lxn/imageloader/Cache");
        //context.getExternalCacheDir()  /storage/emulated/0/Android/data/com.lxn.utilone/cache  这个目录在SD下 会随着系统卸载被删除
        File cacheDir =null;
        cacheDir = context.getExternalCacheDir();
        if(null==cacheDir){
            cacheDir = StorageUtils.getOwnCacheDirectory(context, "/xyyy/lxn/imageloader/Cache");
        }
        //这个目录在  data/data/com.lxn.utilone/cache 需要root才能查看数据
        File cacheDir1 = context.getCacheDir();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context)
                // 设置线程优先级
                .threadPriority(Thread.NORM_PRIORITY + 2)
                // 设置图片缓存路劲
                .diskCache(new UnlimitedDiskCache(cacheDir))
                /*
				 * //调用该方法会禁止在内存中缓存同一张图片的多个尺寸。当把本地图片加载到内存中时，
				 * 首先会把图片缩减至要显示的ImageView的大小，
				 * 因此可能会出现一种状况，就是会首先显示一张图的小图，然后再显示这张图的大图。这种情况下，
				 * 同一张图片的两种尺寸的Bitmap会被存储在内存中，这是默认的操作
				 * 该方法会确保删除已加载图片缓存在内存中的其他尺寸的缓存。
				 */
                .denyCacheImageMultipleSizesInMemory()
				/*
				 * ImageLoaderConfiguration
				 * 配置中的.discCacheFileNameGenerator()方法是将缓存下来的文件以什么方式命名
				 * 里面可以调用的方法有 1.new Md5FileNameGenerator() //使用MD5对UIL进行加密命名
				 * 2.new HashCodeFileNameGenerator()//使用HASHCODE对UIL进行加密命名
				 */
                .discCacheFileNameGenerator(new Md5FileNameGenerator())

                .tasksProcessingOrder(QueueProcessingType.LIFO)
                //.writeDebugLogs() // Remove for release app
                .build();
        ImageLoader.getInstance().init(config);
    }


    public PatchManager getPatchManager() {
        return patchManager;
    }

    public void setPatchManager(PatchManager patchManager) {
        this.patchManager = patchManager;
    }

    /**
     * 初始化淘宝热更新的东西
     */
    private void inithotfix(){
        patchManager = new PatchManager(this);
        patchManager.init(DeviceUtil.getVersionname());
        try {
            // load patch
            patchManager.loadPatch();
            //从本地加载补丁包（ps：一般都是从服务器去下载） .apatch file path
            String patchFileString = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + APATCH_PATH;
            patchManager.addPatch(patchFileString);
            //加载补丁成功后，删除下载的补丁
            File file = new File(patchFileString);
            if (file.exists()) {
                file.delete();
            }
            //从服务器去下载最新的补丁包 get patch under new thread
            Intent patchDownloadIntent = new Intent(this, PatchDownloadIntentService.class);
            patchDownloadIntent.putExtra("url", "http://xxx/patch/app-release-fix-shine.apatch");
            startService(patchDownloadIntent);
        } catch (Exception e) {
        }

    }
}
