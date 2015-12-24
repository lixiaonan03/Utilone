package com.lxn.utilone.util;

import android.util.DisplayMetrics;

/**
 * 作者：lxn on 2015/12/21 11:00
 * 描述：Utilone ——————单位换算的util
 * 	px  ：像素
     in  ：英寸
     mm  ：毫米
     pt  ：磅，1/72 英寸
     dp  ：一个基于density的抽象单位，如果一个160dpi的屏幕，1dp=1px
     dip ：等同于dp
     sp  ：同dp相似，但还会根据用户的字体大小偏好来缩放。
          建议使用sp作为文本的单位，其它用dip 布局时尽量使用单位dp，少使用px

     +0.5f  是为了四舍五入
 *
 */
public class UnitUtil {
    /**设备显示材质**/
    private static DisplayMetrics mDisplayMetrics = DeviceUtil.getDisplayMetrics();

    /**
     * sp转换px
     * @param spValue sp数值
     * @return px数值
     */
    public static int spTopx(float spValue) {
        return (int) (spValue * mDisplayMetrics.scaledDensity + 0.5f);
    }

    /**
     * px转换sp
     * @param pxValue px数值
     * @return sp数值
     */
    public static int pxTosp(float pxValue) {
        return (int) (pxValue / mDisplayMetrics.scaledDensity + 0.5f);
    }

    /**
     * dp转换px
     * @param dpValue dp数值
     * @return px数值
     */
    public static int dpTopx(int dpValue) {
        return (int) (dpValue * mDisplayMetrics.density + 0.5f);
    }

    /**
     * px转换dp
     * @param pxValue px数值
     * @return dp数值
     */
    public static int pxTodp(float pxValue) {
        return (int) (pxValue / mDisplayMetrics.density + 0.5f);
    }
}
