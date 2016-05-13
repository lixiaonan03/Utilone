package com.lxn.utilone.util;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.lxn.utilone.UtilApplication;

/**
 * SharedPreferences的使用工具类 (暂时先也在一个文件SHOP_CONFIG中  后面如果有需要在进行修改)
 */
public class PreferencesUtil {

    public static final String SHOP_CONFIG = "shop_config";//文件名称
    public static final String APP_INIT = "appInit";
    public static final String USER_NAME = "user_name";
    public static final String USER_PWD = "user_pwd";
    public static final String REMEMBER_PWD = "remember_pwd";
    public static final String SEARCH_HISTORY = "search_history";
    public static final String GUIDE_INIT = "guide_init";//是否展示过引导页 0未展示 1已展示


    public static final String IS_LOGIN = "is_login";//判断是否登录 0 退出状态 1 登录状态

    private static SharedPreferences spf;

    /**
     * 在application里进行初始化
     *
     * @param app
     * @return
     */
    public static void init(Context app) {
        spf = app.getSharedPreferences(SHOP_CONFIG, 0);// 0私有模式
        if (spf.getInt(APP_INIT, -1) == -1) {
            init();
        }
    }

    /**
     * 生成一个其他文件名的sharepreferences
     *
     * @param app                  上下文环境
     * @param sharepreferencesname 文件名称
     * @return
     */
    public static SharedPreferences initother(Context app, String sharepreferencesname) {
        SharedPreferences spfother = app.getSharedPreferences(sharepreferencesname, 0);// 0私有模式
        return spfother;
    }

    /**
     * 初始化文件里的字段内容
     */
    private static void init() {
        setInt(APP_INIT, 1);
        setInt(GUIDE_INIT, 0);
        setInt(REMEMBER_PWD, 0);
        setStr(USER_NAME, "");
        setStr(USER_PWD, "");
        setInt(IS_LOGIN, 0);
        setStr(SEARCH_HISTORY, "");
    }

    /**
     * 默认share文件设置int值
     * @param key
     * @param value
     */
    public static void setInt(String key, int value) {
        Editor editor = spf.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * 默认share文件获取int值  没有为-1
     * @param key
     * @return
     */
    public static int getValue(String key) {
        return spf.getInt(key, -1);
    }

    /**
     * 默认文件 获取long  值 没有为-1
     * @param tag
     * @return
     */
    public static long getLong(String tag) {
        return spf.getLong(tag, -1l);
    }

    /**
     * 默认文件 获取Str值 默认“”
     * @param key
     * @return
     */
    public static String getStr(String key) {
        return spf.getString(key, "");
    }

    public static void setStr(String key, String content) {
        Editor editor = spf.edit();
        editor.putString(key, content);
        editor.commit();
    }

    public static void setLong(String key, long content) {
        Editor editor = spf.edit();
        editor.putLong(key, content);
        editor.commit();
    }

    public static Boolean getBoolean(String key) {
        return spf.getBoolean(key, false);
    }

    public static Boolean getBooleanOther(String filename, String key) {
        SharedPreferences spfother = UtilApplication.getInstance().getSharedPreferences(filename, 0);// 0私有模式
        return spfother.getBoolean(key, false);
    }

    /**
     * 默认文件share文件设置boolean 值
     *
     * @param key
     * @param b
     */
    public static void setBoolean(String key, Boolean b) {
        Editor editor = spf.edit();
        editor.putBoolean(key, b);
        editor.commit();
    }

    /**
     * 其他share文件设置boolean 值
     * @param filename
     * @param key
     * @param b
     */
    public static void setBooleanOther(String filename, String key, Boolean b) {
        SharedPreferences spfother = UtilApplication.getInstance().getSharedPreferences(filename, 0);// 0私有模式
        Editor editor = spfother.edit();
        editor.putBoolean(key, b);
        editor.commit();
    }

}
