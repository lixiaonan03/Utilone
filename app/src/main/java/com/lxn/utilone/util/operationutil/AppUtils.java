package com.lxn.utilone.util.operationutil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Build;
import android.util.Log;


import com.lxn.utilone.util.toolutil.ConvertUtils;
import com.lxn.utilone.util.toolutil.EncryptUtils;
import com.lxn.utilone.util.toolutil.FileUtils;
import com.lxn.utilone.util.toolutil.StringUtil;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

/**
 * @author lixiaonan
 * 功能描述: app相关的工具类
 * 时 间： 2019-11-06 14:31
 */
public final class AppUtils {

    private AppUtils() {
    }


    /**
     * 注册 App 前后台切换监听器
     * @param listener The status of application changed listener
     */
    public static void registerAppStatusChangedListener(@NonNull final Utils.OnAppStatusChangedListener listener) {
        UtilsActivityLifecycleImpl.INSTANCE.addOnAppStatusChangedListener(listener);
    }

    /**
     * 注销 App 前后台切换监听器
     * @param listener The status of application changed listener
     */
    public static void unregisterAppStatusChangedListener(@NonNull final Utils.OnAppStatusChangedListener listener) {
        UtilsActivityLifecycleImpl.INSTANCE.removeOnAppStatusChangedListener(listener);
    }

    /**
     * 返回当前应用是否在前台
     *
     * @return 在前台为true
     */
    public static boolean isAppForeground() {
        ActivityManager am = (ActivityManager) Utils.getApp().getSystemService(Context.ACTIVITY_SERVICE);
        if (am == null) {
            return false;
        }
        List<ActivityManager.RunningAppProcessInfo> info = am.getRunningAppProcesses();
        if (info == null || info.size() == 0) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo aInfo : info) {
            if (aInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                if (aInfo.processName.equals(Utils.getApp().getPackageName())) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 退出应用的 待实验效果看对界面的onDestroy有没影响
     */
    public static void exitApp() {
        ActivityUtils.finishAllActivities();
        System.exit(0);
    }

    /**
     * 获取当前应用的版本name
     *
     * @return
     */
    public static String getAppVersionName() {
        return getAppVersionName(Utils.getApp().getPackageName());
    }

    /**
     * 获取传入应用的版本name
     *
     * @param packageName
     * @return
     */
    public static String getAppVersionName(final String packageName) {
        try {
            PackageManager pm = Utils.getApp().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取当期应用的版本号
     *
     * @return
     */
    public static long getAppVersionCode() {
        return getAppVersionCode(Utils.getApp());
    }

    /**
     * 获取出入的app的版本号
     *
     * @param context
     * @return long
     */
    public static long getAppVersionCode(Context context) {
        long appVersionCode = 0;
        try {
            PackageInfo packageInfo = context.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                appVersionCode = packageInfo.getLongVersionCode();
            } else {
                appVersionCode = packageInfo.versionCode;
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("", e.getMessage());
        }
        return appVersionCode;
    }



    /**
     * 安装 App（支持 8.0）
     * @param filePath
     */
    public static void installApp(final String filePath) {
        installApp(FileUtils.getFileByPath(filePath));
    }
    /**
     * 安装 App（支持 8.0）
     * @param file
     */
    public static void installApp(final File file) {
        if (!FileUtils.isFileExists(file)) {
            return;
        }
        Utils.getApp().startActivity(getInstallAppIntent(file, true));
    }

    private static Intent getInstallAppIntent(final File file) {
        return getInstallAppIntent(file, false);
    }

    /**
     * 获取安装app的意图
     * @param file  文件
     * @param isNewTask
     * @return
     */
    private static Intent getInstallAppIntent(final File file, final boolean isNewTask) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data;
        String type = "application/vnd.android.package-archive";
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            data = Uri.fromFile(file);
        } else {
            String authority = Utils.getApp().getPackageName() + ".utilcode.provider";
            data = FileProvider.getUriForFile(Utils.getApp(), authority, file);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        Utils.getApp().grantUriPermission(Utils.getApp().getPackageName(), data, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(data, type);
        return isNewTask ? intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) : intent;
    }


    /**
     * Return the application's signature.
     *
     * @param packageName The name of the package.
     * @return the application's signature
     */
    public static Signature[] getAppSignature(final String packageName) {
        if (StringUtil.isSpace(packageName)) {
            return null;
        }
        try {
            PackageManager pm = Utils.getApp().getPackageManager();
            @SuppressLint("PackageManagerGetSignatures")
            PackageInfo pi = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            return pi == null ? null : pi.signatures;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取应用签名的的 SHA1 值
     *
     * @return the application's signature for SHA1 value
     */
    public static String getAppSignatureSHA1() {
        return getAppSignatureSHA1(Utils.getApp().getPackageName());
    }

    /**
     * 获取应用签名的的 SHA1 值
     *
     * @param packageName The name of the package.
     * @return the application's signature for SHA1 value
     */
    public static String getAppSignatureSHA1(final String packageName) {
        return getAppSignatureHash(packageName, "SHA1");
    }

    /**
     * 获取应用签名的的 SHA256 值
     * @return the application's signature for SHA256 value
     */
    public static String getAppSignatureSHA256() {
        return getAppSignatureSHA256(Utils.getApp().getPackageName());
    }

    /**
     * 获取应用签名的的 SHA256 值
     * @param packageName The name of the package.
     * @return the application's signature for SHA256 value
     */
    public static String getAppSignatureSHA256(final String packageName) {
        return getAppSignatureHash(packageName, "SHA256");
    }

    /**
     * 获取应用签名的的 MD5 值
     * @return the application's signature for MD5 value
     */
    public static String getAppSignatureMD5() {
        return getAppSignatureMD5(Utils.getApp().getPackageName());
    }

    /**
     * Return the application's signature for MD5 value.
     *
     * @param packageName The name of the package.
     * @return the application's signature for MD5 value
     */
    public static String getAppSignatureMD5(final String packageName) {
        return getAppSignatureHash(packageName, "MD5");
    }

    private static String getAppSignatureHash(final String packageName, final String algorithm) {
        if (StringUtil.isSpace(packageName)) {
            return "";
        }
        Signature[] signature = getAppSignature(packageName);
        if (signature == null || signature.length <= 0){
            return "";
        }
        return ConvertUtils.bytes2HexString(EncryptUtils.hashTemplate(signature[0].toByteArray(), algorithm))
                .replaceAll("(?<=[0-9A-F]{2})[0-9A-F]{2}", ":$0");
    }
}
