package com.lxn.utilone.util;


import org.json.JSONException;
import org.json.JSONObject;


/**
 * @author lixiaonan
 * 功能描述: 日志包装类的
 * 时 间： 2020/9/7 3:13 PM
 * 目前这个类已经基本可以满足日常业务打印log的需求
 * 如果没有特殊场景，请不要改动
 * 如果不满足需求，请在tapd上提交bug
 */
public class Log {

    private static final String DEFAULT_TAG = "WWLog";

    private static final int LOG_TAG_LEVEL_I = 1;
    private static final int LOG_TAG_LEVEL_D = 2;
    private static final int LOG_TAG_LEVEL_W = 3;
    private static final int LOG_TAG_LEVEL_E = 4;

    /**
     * 调用i()方法即可
     */
    @Deprecated
    public static void iWithTag(String tag, String msg) {
        printLogOrJson(tag, msg, LOG_TAG_LEVEL_I);
    }

    /**
     * 不再需要指定打印json类型文本
     * 只需要按照默认习惯
     * 指定打印内容和log级别，即可兼容json内容
     */
    @Deprecated
    public static void json(String json) {
        //历史兼容性问题，暂时定msg的level为i
        printLogOrJson(DEFAULT_TAG, json, LOG_TAG_LEVEL_I);
    }

    /**
     * 不再需要指定打印json类型文本
     * 只需要按照默认习惯
     * 指定打印内容和log级别，即可兼容json内容
     */
    @Deprecated
    public static void json(String tag, String json) {
        //历史兼容性问题，暂时定msg的level为i
        printLogOrJson(tag, json, LOG_TAG_LEVEL_I);
    }


    public static void i(String msg) {
        printLogOrJson(DEFAULT_TAG, msg, LOG_TAG_LEVEL_D);
    }

    public static void i(String tag, String msg) {
        printLogOrJson(tag, msg, LOG_TAG_LEVEL_I);
    }

    public static void d(String msg) {
        printLogOrJson(DEFAULT_TAG, msg, LOG_TAG_LEVEL_D);
    }

    public static void d(String tag, String msg) {
        printLogOrJson(tag, msg, LOG_TAG_LEVEL_D);
    }

    public static void w(String tag, String msg) {
        printLogOrJson(tag, msg, LOG_TAG_LEVEL_W);
    }


    public static void e(String msg) {
        printLogOrJson(DEFAULT_TAG, msg, LOG_TAG_LEVEL_E);
    }

    public static void e(String tag, String msg) {
        printLogOrJson(tag, msg, LOG_TAG_LEVEL_E);
    }

    /**
     * 其他顶层Log打印方法调用的第一层基础打印方法
     * 本方法判断打印的是Msg是Json还是普通文本
     */
    private static void printLogOrJson(String tag, String msg, int level) {
        if (msg == null || msg.length() == 0) {
            return;
        }

        if (msg.trim().startsWith("{\"")) {
            //默认为打印的是Json
            try {
                JSONObject object = new JSONObject(msg);
                iWithTag(tag, object.toString(2));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            //默认为打印的是普通文本
            printLog(tag, msg, level);
        }
    }

    /**
     * 其他顶层Log打印方法调用的第二层基础打印方法
     * 本方法对打印的文本进行分段打印处理
     */
    private static void printLog(String tag, String msg, int level) {
        boolean logEnable = true;
        if (logEnable) {
            if (tag == null || tag.length() == 0
                    || msg == null || msg.length() == 0) {
                return;
            }

            int segmentSize = 3 * 1024;
            long length = msg.length();
            if (length <= segmentSize) {
                // 长度小于等于限制直接打印
                printLogLevel(tag, msg, level);
            } else {
                while (msg.length() > segmentSize) {
                    // 循环分段打印日志
                    String logContent = msg.substring(0, segmentSize);
                    msg = msg.replace(logContent, "");
                    printLogLevel(tag, logContent, level);
                }
                // 打印剩余日志
                printLogLevel(tag, msg, level);
            }
        }
    }

    /**
     * 其他顶层Log打印方法调用的第三层基础打印方法
     * 本方法只负责根据Tag级别进行单行打印
     */
    private static void printLogLevel(String tag, String msg, int level) {
        switch (level) {
            case LOG_TAG_LEVEL_I:
                android.util.Log.i(tag, msg);
                break;
            case LOG_TAG_LEVEL_D:
                android.util.Log.d(tag, msg);
                break;
            case LOG_TAG_LEVEL_W:
                android.util.Log.w(tag, msg);
                break;
            case LOG_TAG_LEVEL_E:
                android.util.Log.e(tag, msg);
                break;
            default:
                break;
        }
    }

}
