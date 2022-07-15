package com.lxn.utilone.activity;

import android.os.Bundle;

import com.lxn.utilone.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * @author：李晓楠 时间：2022/7/15 18:17
 */
public class XposedDemo implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        Log.i("lxn12","包名"+lpparam.packageName+"=="+lpparam.processName);
        XposedHelpers.findAndHookMethod("android.app.Activity",lpparam.classLoader,"onResume",new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                Log.i("lxn12","方法执行前"+param.thisObject);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.i("lxn12","方法执行后"+param.thisObject);
            }
        });

    }
}
