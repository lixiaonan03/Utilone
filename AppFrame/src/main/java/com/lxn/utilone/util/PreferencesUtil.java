package com.lxn.utilone.util;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * SharedPreferences的使用工具类 (暂时先也在一个文件SHOP_CONFIG中  后面如果有需要在进行修改)
 */
public class PreferencesUtil {

	public static final String SHOP_CONFIG = "shop_config";//文件名称
	public static final String APP_INIT = "appInit";
	public static final String USER_NAME="user_name";
	public static final String USER_PWD="user_pwd";
	public static final String REMEMBER_PWD="remember_pwd";
	public static final String SEARCH_HISTORY="search_history";
	public static final String GUIDE_INIT="guide_init";//是否展示过引导页 0未展示 1已展示
	
	
	public static final String IS_LOGIN="is_login";//判断是否登录 0 退出状态 1 登录状态
	
	private static SharedPreferences spf;
	/**
	 * 在application里进行初始化
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
	 * @param app   上下文环境
	 * @param sharepreferencesname  文件名称
	 * @return
	 */
	public static SharedPreferences initother(Context app,String sharepreferencesname){
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
	public static void setInt(String tag, int value) {
		Editor editor = spf.edit();
		editor.putInt(tag, value);
		editor.commit();
	}
	public static int getValue(String tag) {
		return spf.getInt(tag, -1);
	}

	public static long getLong(String tag) {
		return spf.getLong(tag, -1l);
	}

	public static String getStr(String tag) {
		return spf.getString(tag, "");
	}

	public static void setStr(String tag, String content) {
		Editor editor = spf.edit();
		editor.putString(tag, content);
		editor.commit();
	}

	public static void setLong(String tag, long content) {
		Editor editor = spf.edit();
		editor.putLong(tag, content);
		editor.commit();
	}

	public static Boolean getBoolean(String tag) {
		return spf.getBoolean(tag, false);
	}

	public static void setBoolean(String tag, Boolean b) {
		Editor editor = spf.edit();
		editor.putBoolean(tag, b);
		editor.commit();
	}

}
