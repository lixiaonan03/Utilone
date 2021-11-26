package com.lxn.utilone.util.operationutil;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

/**
 * @author lixiaonan
 * 功能描述: 剪切板工具类的
 * 时 间： 2019-11-06 12:42
 */
public final class ClipboardUtils {

    private ClipboardUtils() {

    }

    /**
     * 复制文本到剪贴板
     *
     * @param text 文本
     */
    public static void copyText(final CharSequence text) {
        try {
            ClipboardManager cm = (ClipboardManager) Utils.getApp().getSystemService(Context.CLIPBOARD_SERVICE);
            //noinspection ConstantConditions
            cm.setPrimaryClip(ClipData.newPlainText("text", text));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取剪贴板的文本
     *
     * @return 剪贴板的文本 如果异常和没有则返回空字符串
     */
    public static CharSequence getText() {
        try {
            ClipboardManager cm = (ClipboardManager) Utils.getApp().getSystemService(Context.CLIPBOARD_SERVICE);
            //noinspection ConstantConditions
            ClipData clip = cm.getPrimaryClip();
            if (clip != null && clip.getItemCount() > 0) {
                return clip.getItemAt(0).coerceToText(Utils.getApp());
            }
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 清空剪贴板
     */
    public void clearClip() {
        ClipboardManager mClipboardManager = (ClipboardManager) Utils.getApp().getSystemService(Context.CLIPBOARD_SERVICE);
        mClipboardManager.setPrimaryClip(ClipData.newPlainText(null, ""));
    }
}
