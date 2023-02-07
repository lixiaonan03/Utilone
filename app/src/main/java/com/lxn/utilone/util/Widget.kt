package com.lxn.utilone.util

import android.util.DisplayMetrics
import com.dianping.logan.Util
import com.lxn.utilone.util.operationutil.Utils

val dm: DisplayMetrics = Utils.getApp().resources.displayMetrics

inline val Int.dp get() = (dm.density * this + 0.5).toInt()

inline val Int.dpf get() = dm.density * this + 0F

inline val Double.dpf get() = (dm.density * this).toFloat()