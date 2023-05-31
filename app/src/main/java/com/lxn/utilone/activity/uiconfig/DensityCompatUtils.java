package com.lxn.utilone.activity.uiconfig;

import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * 头条适配封
 */
public class DensityCompatUtils {
    private static float sNoncompatDensity;
    private static float sNoncompatScaledDensity;
    private static int sNoncompatDensityDpi;

    private static float sWidthDp = 376f;   //屏幕宽度dp = px / (density / 160)
    private static float sHeightDp = 640f;//屏幕高度dp

    public static void setWidthDensity(Context context) {
        setDensity(context,true);
    }

    public static void setHeightDensity(Context context) {
        setDensity(context,false);
    }

    public static void setDensity(Context context, boolean isWidth) {
        final Context application = context.getApplicationContext();
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        //获取屏幕dpi
        Log.i("lxn43","之前的===${dpi}="+displayMetrics.densityDpi+"==density}=="+displayMetrics.density+"scaledDensity="+displayMetrics.scaledDensity);

        if (sNoncompatDensity == 0) {
            sNoncompatDensity = displayMetrics.density;
            sNoncompatScaledDensity = displayMetrics.scaledDensity;
            sNoncompatDensityDpi = displayMetrics.densityDpi;
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration newConfig) {
                    if (newConfig != null && newConfig.fontScale > 0) {
                        sNoncompatScaledDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {

                }
            });
        }

        final float targetDensity;
        if (isWidth) {
            targetDensity = displayMetrics.widthPixels / sWidthDp;
        } else {
            targetDensity = displayMetrics.heightPixels / sHeightDp;
        }
        final float targetScaledDensity = targetDensity * (sNoncompatScaledDensity / sNoncompatDensity);
        final int targetDensityDpi = (int) (160 * targetDensity);

        displayMetrics.density = targetDensity;
        displayMetrics.scaledDensity = targetScaledDensity;
        displayMetrics.densityDpi = targetDensityDpi;
    }

    public static void setDefaultDensity(Context context) {
        //  App.getInstance().getResources().getDisplayMetrics().setToDefaults(); //displayMetrics.hashCode() = 0
        final Context application = context.getApplicationContext();
        DisplayMetrics displayMetrics = application.getResources().getDisplayMetrics();
        displayMetrics.density = sNoncompatDensity;
        displayMetrics.scaledDensity = sNoncompatScaledDensity;
        displayMetrics.densityDpi = sNoncompatDensityDpi;
    }
}
