package com.lxn.utilone;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.volley.util.VolleyUtil;

import com.alipay.euler.andfix.patch.PatchManager;
import com.android.volley.RequestQueue;
import com.bbt.commonlib.LibApplication;
import com.bbt.commonlib.operationutil.ProcessUtils;
import com.didichuxing.doraemonkit.DoraemonKit;
import com.idlefish.flutterboost.BoostPluginRegistry;
import com.idlefish.flutterboost.FlutterBoost;
import com.idlefish.flutterboost.Platform;
import com.idlefish.flutterboost.Utils;
import com.idlefish.flutterboost.interfaces.INativeRouter;
import com.lxn.utilone.router.PageRouter;
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
import com.zgy.catchuninstallself.UninstallObserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.lifecycle.ProcessLifecycleOwner;
import io.flutter.embedding.android.FlutterView;
import io.flutter.view.FlutterMain;

/**
  *  @author lixiaonan
  *  功能描述: application的基础类
  *  时 间： 2020/3/15 上午11:39
  */
public class UtilApplication extends LibApplication {

    public static UtilApplication application = null;
    public static final Logger logger = LoggerFactory.getLogger(UtilApplication.class);
    public static RequestQueue requestQueue;

    @Override
    public void onCreate() {
        super.onCreate();

        initFlutterBoost();
        //在主进程添加第三方的一些数据初始化的
        if(ProcessUtils.isMainProcess()){
            closeAndroidPWarningDialog();
            application = this;
            //注册activity 的生命周期
            initActivityLife();
            //绑定全局生命的
            ProcessLifecycleOwner.get().getLifecycle().addObserver(new ApplicationObserver());
            initLibs();
        }
    }

    public static UtilApplication getInstance() {
        return application;
    }

    /**
     * 初始化flutterBoost 相关的
     */
    private void initFlutterBoost(){
        //flutter 加入的
        FlutterMain.startInitialization(this);
        INativeRouter router = new INativeRouter() {
            @Override
            public void openContainer(Context context, String url, Map<String, Object> urlParams, int requestCode, Map<String, Object> exts) {
                String assembleUrl = Utils.assembleUrl(url, urlParams);
                PageRouter.openPageByUrl(context, assembleUrl, urlParams);
            }
        };
        FlutterBoost.BoostLifecycleListener boostLifecycleListener = new FlutterBoost.BoostLifecycleListener() {
            @Override
            public void beforeCreateEngine() {
            }

            @Override
            public void onEngineCreated() {
                BoostPluginRegistry boostPluginRegistry = new BoostPluginRegistry(FlutterBoost.instance().engineProvider());
            }

            @Override
            public void onPluginsRegistered() {
            }

            @Override
            public void onEngineDestroy() {
            }
        };

        //
        // AndroidManifest.xml 中必须要添加 flutterEmbedding 版本设置
        //   <meta-data android:name="flutterEmbedding" android:value="2"> </meta-data>
        // GeneratedPluginRegistrant 会自动生成 新的插件方式　
        //
        Platform platform = new FlutterBoost
                .ConfigBuilder(this, router)
                .isDebug(true)
                .whenEngineStart(FlutterBoost.ConfigBuilder.ANY_ACTIVITY_CREATED)
                .renderMode(FlutterView.RenderMode.texture)
                .lifecycleListener(boostLifecycleListener)
                .build();

        FlutterBoost.instance().init(platform);
    }
    /**
     * 注册activity的生命周期监听
     */
    private int count = 0;
    private void initActivityLife(){
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
            public void onActivitySaveInstanceState(@NonNull Activity activity,@NonNull Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {

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
    /**
     * 初始化Imageloader
     * @param context 上下文
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
    //hotfix使用
    private PatchManager patchManager;
    private static final String APATCH_PATH = "/lixiaonan/out.apatch";
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


    /**
     * 初始化一些第三方组件的
     */
    private void initLibs(){
        //初始化滴滴调试组件的
        DoraemonKit.install(application,null,"f4e527b850334b837afea1b97f844cc1");

        //Sp 使用的基础文件 推荐使用腾讯的mmkv
        PreferencesUtil.init(application);
        //全局异常捕获的
        BadHandler.getInstance().init(application);
        //FIXME 卸载弹出网页的 高版本已经不好使了
        UninstallObserver.startWork(getApplicationContext().getFilesDir().getParent(), "http://blog.csdn" +
                ".net/ksksjipeng/article/details/51992435",android.os.Build.VERSION.SDK_INT);

        //FIXME 已经过时 初始化ImageLoad使用的
        initImageLoader(application);
        //FIXME 已经过时 volley网路请求的请求队列
        requestQueue= VolleyUtil.getInstance();
        //inithotfix();
    }


    /**
     * 去掉android P上用反射调用源码的提示弹窗
     */
    private void closeAndroidPWarningDialog() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            try {
                Class aClass = Class.forName("android.content.pm.PackageParser$Package");
                Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
                declaredConstructor.setAccessible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Class cls = Class.forName("android.app.ActivityThread");
                Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
                declaredMethod.setAccessible(true);
                Object activityThread = declaredMethod.invoke(null);
                Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
                mHiddenApiWarningShown.setAccessible(true);
                mHiddenApiWarningShown.setBoolean(activityThread, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
