package com.lxn.utilone.util;

import android.util.Log;

public class LogUtils {
	public static String TAG="lixiaonan";


    public static void i(String tag, String msg) {
            Log.i(tag, msg);
    }
    public static void i(String msg) {
    	Log.i(TAG,msg);
    }
}
