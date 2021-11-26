package com.lxn.utilone.util.operationutil;

import android.app.Activity;
import android.os.Build;

import java.util.List;

import androidx.annotation.NonNull;

/**
  *  @author lixiaonan
  *  功能描述: activity相关的工具类
  *  时 间： 2020/7/13 4:31 PM
  */
public final class ActivityUtils {
    /**
     * 新增 Activity 生命周期监听
     * @param activity  The activity.
     * @param callbacks The callbacks.
     */
    public static void addActivityLifecycleCallbacks(final Activity activity,
                                                     final Utils.ActivityLifecycleCallbacks callbacks) {
        UtilsActivityLifecycleImpl.INSTANCE.addActivityLifecycleCallbacks(activity, callbacks);
    }

    /**
     * 移除 Activity 生命周期监听
     * @param activity The activity.
     */
    public static void removeActivityLifecycleCallbacks(final Activity activity) {
        UtilsActivityLifecycleImpl.INSTANCE.removeActivityLifecycleCallbacks(activity);
    }

    /**
     * 移除 Activity 生命周期监听
     * @param activity  The activity.
     * @param callbacks The callbacks.
     */
    public static void removeActivityLifecycleCallbacks(final Activity activity,
                                                        final Utils.ActivityLifecycleCallbacks callbacks) {
        UtilsActivityLifecycleImpl.INSTANCE.removeActivityLifecycleCallbacks(activity, callbacks);
    }

    /**
     * 获取栈顶activity
     * @return
     */
    public static Activity getTopActivity() {
        return UtilsActivityLifecycleImpl.INSTANCE.getTopActivity();
    }

    /**
     * 判断 Activity 是否存活
     * @param activity The activity.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isActivityAlive(final Activity activity) {
        return activity != null && !activity.isFinishing()
                && (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1 || !activity.isDestroyed());
    }


    /**
     * 退出所有activity
     */
    public static void finishAllActivities() {
        finishAllActivities(false);
    }

    /**
     * 退出所有activity
     * @param isLoadAnim True to use animation for the outgoing activity, false otherwise.
     */
    public static void finishAllActivities(final boolean isLoadAnim) {
        List<Activity> activityList = UtilsBridge.getActivityList();
        for (Activity act : activityList) {
            // sActivityList remove the index activity at onActivityDestroyed
            act.finish();
            if (!isLoadAnim) {
                act.overridePendingTransition(0, 0);
            }
        }
    }
}
