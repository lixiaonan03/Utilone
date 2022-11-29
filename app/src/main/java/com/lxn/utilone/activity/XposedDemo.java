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
//        XposedHelpers.findAndHookMethod("android.app.Activity", lpparam.classLoader, "onResume", new XC_MethodHook() {
//            @Override
//            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                super.beforeHookedMethod(param);
//                Log.i("lxn12", "方法执行前" + param.thisObject);
//            }
//
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                super.afterHookedMethod(param);
//                Log.i("lxn12", "方法执行后" + param.thisObject);
//            }
//        });

        //FIXME： 1、hook 手机中的app的方法调用(一定要在 LSPosed APP中把需要hook的app加入到utilone的模块中)   2、启动之后在下面的方法中判断要hook的方法 注意是否有参数

        XposedHelpers.findAndHookMethod("android.widget.TextView", lpparam.classLoader, "setText", CharSequence.class,new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                Log.i("lxn12", "方法执行前" + param.thisObject);
                Log.i("lxn121", "获取参数的" + param.args[0].toString());
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.i("lxn12", "方法执行后" + param.thisObject);
            }
        });



    }
}
