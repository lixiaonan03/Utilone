package com.lxn.utilone.util.operationutil;

import android.app.Activity;
import android.app.Application;

import java.util.List;

/**
 * <pre>
 *     author: blankj
 *     blog  : http://blankj.com
 *     time  : 2020/03/19
 *     desc  :
 * </pre>
 */
class UtilsBridge {

    static void init(Application app) {
        UtilsActivityLifecycleImpl.INSTANCE.init(app);
    }

    static void unInit(Application app) {
        UtilsActivityLifecycleImpl.INSTANCE.unInit(app);
    }



    static List<Activity> getActivityList() {
        return UtilsActivityLifecycleImpl.INSTANCE.getActivityList();
    }

    static Application getApplicationByReflect() {
        return UtilsActivityLifecycleImpl.INSTANCE.getApplicationByReflect();
    }



    static void runOnUiThread(final Runnable runnable) {
        ThreadUtils.runOnUiThread(runnable);
    }

    static void runOnUiThreadDelayed(final Runnable runnable, long delayMillis) {
        ThreadUtils.runOnUiThreadDelayed(runnable, delayMillis);
    }



}
