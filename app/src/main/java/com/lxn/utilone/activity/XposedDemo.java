package com.lxn.utilone.activity;

import com.lxn.utilone.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
  *  @author lixiaonan
  *  功能描述: Xposed 抓包别人的项目的
  *  时 间： 2022/7/20 15:34
  */
public class XposedDemo implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        Log.i("lxn12", "包名" + lpparam.packageName + "==" + lpparam.processName);
        XposedHelpers.findAndHookMethod("android.app.Activity", lpparam.classLoader, "onResume", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                Log.i("lxn12", "方法执行前" + param.thisObject);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.i("lxn12", "方法执行后" + param.thisObject);
            }
        });

    }
}
