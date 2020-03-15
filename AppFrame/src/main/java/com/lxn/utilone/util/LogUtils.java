package com.lxn.utilone.util;

import android.util.Log;

import com.lxn.utilone.BuildConfig;

import org.json.JSONException;
import org.json.JSONObject;
/**
  *  @author lixiaonan
  *  功能描述: 打印日志的一些操作
  *  时 间： 2020/3/15 下午2:32
  */
public class LogUtils {
	public static String TAG="lxnTest";


    /**
     * @param tag 自定义tag
     * @param msg
     */
    public static void iWithTag(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            if (StringUtils.isNotBlank(msg)) {
                Log.i(tag, msg);
            }
        }
    }

    /**
     * 自己默认的打印日志标签
     *
     * @param msg
     */
    public static void iLi(String msg) {
        if (BuildConfig.DEBUG) {
            if (StringUtils.isNotBlank(msg)) {
                Log.i(TAG, msg);
            }
        }
    }

    public static void d(String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, msg);
        }
    }

    public static void e(String msg) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, msg);
        }
    }

    public static void json(String json) {
        if (StringUtils.isBlank(json) || !json.trim().startsWith("{\"")){
            iLi(json);
        } else {
            try {
                JSONObject object = new JSONObject(json);
                iLi(object.toString(2));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
