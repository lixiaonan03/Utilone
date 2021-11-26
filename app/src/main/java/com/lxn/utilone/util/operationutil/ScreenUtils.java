package com.lxn.utilone.util.operationutil;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;

/**
 * @author lixiaonan
 * 功能描述: 屏幕计算相关的util类
 * 时 间： 2019-11-06 14:57
 */
public final class ScreenUtils {

    private ScreenUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 获取屏幕宽度 px值
     *
     * @return 屏幕的px值 异常返回 -1
     */
    public static int getScreenWidth() {
        try {
            WindowManager wm = (WindowManager) Utils.getApp().getSystemService(Context.WINDOW_SERVICE);
            if (wm == null) {
                return -1;
            }
            Point point = new Point();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                wm.getDefaultDisplay().getRealSize(point);
            } else {
                wm.getDefaultDisplay().getSize(point);
            }
            return point.x;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 获取屏幕高度 px值
     *
     * @return 屏幕的高度px值 异常返回 -1
     */
    public static int getScreenHeight() {
        try {
            WindowManager wm = (WindowManager) Utils.getApp().getSystemService(Context.WINDOW_SERVICE);
            if (wm == null) {
                return -1;
            }
            Point point = new Point();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                wm.getDefaultDisplay().getRealSize(point);
            } else {
                wm.getDefaultDisplay().getSize(point);
            }
            return point.y;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 获取导航栏的真实高度
     * @return 导航栏高度
     */
    private int getNavigationBarHeight() {
        int result = 0;
        Resources resources = Utils.getApp().getResources();
        int resourceId =
                resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取状态栏高度
     * @return int 高度
     */
    private int getStatusBarHeight() {
        int result = 0;
        Resources resources = Utils.getApp().getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId);
        }
        return result;
    }


    /**
     * 获取屏幕密度的
     *
     * @return the density of screen
     */
    public static float getScreenDensity() {
        try {
            return Utils.getApp().getResources().getDisplayMetrics().density;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Return the screen density expressed as dots-per-inch.
     *
     * @return the screen density expressed as dots-per-inch
     */
    public static int getScreenDensityDpi() {
        return Utils.getApp().getResources().getDisplayMetrics().densityDpi;
    }

    /**
     * 获取activity的截屏数据
     * @param activity
     * @return 返回的bitmap数据 失败为null
     */
    public static Bitmap screenShot(@NonNull final Activity activity) {
        return screenShot(activity, false);
    }

    /**
     * 获取传入的activity的截屏
     * @param activity          The activity.
     * @param isDeleteStatusBar true代表删除状态栏的 false代表不删除状态栏的额
     * @return 返回的bitmap数据 失败为null
     */
    public static Bitmap screenShot(@NonNull final Activity activity, boolean isDeleteStatusBar) {
        try {
            View decorView = activity.getWindow().getDecorView();
            decorView.setDrawingCacheEnabled(true);
            decorView.setWillNotCacheDrawing(false);
            Bitmap bmp = decorView.getDrawingCache();
            if (bmp == null)
                return null;
            DisplayMetrics dm = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            Bitmap ret;
            if (isDeleteStatusBar) {
                Resources resources = activity.getResources();
                int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
                int statusBarHeight = resources.getDimensionPixelSize(resourceId);
                ret = Bitmap.createBitmap(
                        bmp,
                        0,
                        statusBarHeight,
                        dm.widthPixels,
                        dm.heightPixels - statusBarHeight
                );
            } else {
                ret = Bitmap.createBitmap(bmp, 0, 0, dm.widthPixels, dm.heightPixels);
            }
            decorView.destroyDrawingCache();
            return ret;
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 对一个view对象进行生成bitmap对象
     * @param view view对象
     * @return 生成的bitmap对象 异常返回null
     */
    public static Bitmap viewToBitmap(final View view) {
        try {
            if (view == null){
                return null;
            }
            Bitmap ret =
                    Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(ret);
            Drawable bgDrawable = view.getBackground();
            if (bgDrawable != null) {
                bgDrawable.draw(canvas);
            } else {
                canvas.drawColor(Color.WHITE);
            }
            view.draw(canvas);
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * dp转px
     * @param dpValue The value of dp.
     * @return value of px
     */
    public static int dp2px(final float dpValue) {
        final float scale = Utils.getApp().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转dp
     * @param pxValue The value of px.
     * @return value of dp
     */
    public static int px2dp(final float pxValue) {
        final float scale = Utils.getApp().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp转px
     * @param spValue The value of sp.
     * @return value of px
     */
    public static int sp2px(final float spValue) {
        final float fontScale = Utils.getApp().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * px转sp
     * @param pxValue The value of px.
     * @return value of sp
     */
    public static int px2sp(final float pxValue) {
        final float fontScale = Utils.getApp().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }
}
