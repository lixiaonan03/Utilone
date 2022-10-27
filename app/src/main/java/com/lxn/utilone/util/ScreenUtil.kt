package com.lxn.utilone.util

import android.app.Activity
import android.content.res.Resources

/**
 * 今日头条的适配解决方案
 * @author：李晓楠
 * 时间：2022/10/19 17:55
 */
object ScreenUtil {

    fun adapterScreen(activity: Activity, targetDP: Int, isVertical: Boolean) {
        //系统的屏幕尺寸
        val systemDM = Resources.getSystem().displayMetrics
        //app整体的屏幕尺寸
        val appDM = activity.application.resources.displayMetrics
        //activity的屏幕尺寸
        val activityDM = activity.resources.displayMetrics

        if (isVertical) {
            // 适配屏幕的高度
            activityDM.density = activityDM.heightPixels / targetDP.toFloat()
        } else {
            // 适配屏幕的宽度
            activityDM.density = activityDM.widthPixels / targetDP.toFloat()
        }
        // 适配相应比例的字体大小
        activityDM.scaledDensity = activityDM.density * (systemDM.scaledDensity / systemDM.density)
        // 适配dpi
        activityDM.densityDpi = (160 * activityDM.density).toInt()
    }

    fun resetScreen(activity: Activity) {
        //系统的屏幕尺寸
        val systemDM = Resources.getSystem().displayMetrics
        //app整体的屏幕尺寸
        val appDM = activity.application.resources.displayMetrics
        //activity的屏幕尺寸
        val activityDM = activity.resources.displayMetrics

        activityDM.density = systemDM.density
        activityDM.scaledDensity = systemDM.scaledDensity
        activityDM.densityDpi = systemDM.densityDpi

        appDM.density = systemDM.density
        appDM.scaledDensity = systemDM.scaledDensity
        appDM.densityDpi = systemDM.densityDpi
    }
}