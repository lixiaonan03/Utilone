package com.lxn.utilone.util.operationutil;


import android.content.res.Resources;
import android.graphics.Color;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

/**
  *  @author lixiaonan
  *  功能描述: 资源管理类的
  *  时 间： 2019-11-02 19:04
  */
public final class ResourceUtils {


    private ResourceUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 根据String资源id 获取数据资源
     * @param id  R.String.   字符串资源
     * @return
     */
    public static String getString(@StringRes int id) {
        try {
            return Utils.getApp().getResources().getString(id);
        } catch (Resources.NotFoundException ignore) {
            return "";
        }
    }

    /**
     * 获取color 颜色资源id
     * @param id R.color. 颜色资源
     * @return 颜色值，异常的情况下返回 0
     */
    public static int getColor(@ColorRes int id) {
        try {
            return ContextCompat.getColor(Utils.getApp(), id);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Color-string to color-int.
     * <p>Supported formats are:</p>
     *
     * <ul>
     * <li><code>#RRGGBB</code></li>
     * <li><code>#AARRGGBB</code></li>
     * </ul>
     *
     * <p>The following names are also accepted: <code>red</code>, <code>blue</code>,
     * <code>green</code>, <code>black</code>, <code>white</code>, <code>gray</code>,
     * <code>cyan</code>, <code>magenta</code>, <code>yellow</code>, <code>lightgray</code>,
     * <code>darkgray</code>, <code>grey</code>, <code>lightgrey</code>, <code>darkgrey</code>,
     * <code>aqua</code>, <code>fuchsia</code>, <code>lime</code>, <code>maroon</code>,
     * <code>navy</code>, <code>olive</code>, <code>purple</code>, <code>silver</code>,
     * and <code>teal</code>.</p>
     *
     * @param colorString The color-string.
     * @return color-int
     * @throws IllegalArgumentException The string cannot be parsed.
     */
    public static int string2Int(@NonNull String colorString) {
        return Color.parseColor(colorString);
    }
}
