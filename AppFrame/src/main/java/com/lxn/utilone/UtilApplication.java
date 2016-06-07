package com.lxn.utilone;

import android.app.Application;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.volley.util.VolleyUtil;

import com.alipay.euler.andfix.patch.PatchManager;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
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
import java.io.IOException;

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

    public static UtilApplication getInstance() {
        return application;
    }

    public PatchManager getPatchManager() {
        return patchManager;
    }

    public void setPatchManager(PatchManager patchManager) {
        this.patchManager = patchManager;
    }

    /**
     * 初始化Imageloader
     *
     * @param context
     */
    public static void initImageLoader(Context context) {
        // 这个是你希望的缓存文件的目录：imageloader/Cache
        File cacheDir = StorageUtils.getOwnCacheDirectory(context,
                "/xyyy/lxn/imageloader/Cache");

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
                .writeDebugLogs() // Remove for release app
                .build();
        ImageLoader.getInstance().init(config);
    }
}
