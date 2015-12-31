package com.lxn.utilone.util;

import android.content.Intent;
import android.net.Uri;

import java.io.File;

/**
 * 作者：lxn on 2015/12/28 16:29
 * 描述：Utilone ——————各种intent的生成
 */
public class IntentUtil {
    /**
     * 用于打开已知文件
     *
     * @param filePath
     * @return
     */
    public static Intent openFile(String filePath, String fileName) {

        File file = new File(filePath);

        if ((file == null) || !file.exists() || file.isDirectory())
            return null;

		/* 取得扩展名 */
        String end = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		/* 依扩展名的类型决定MimeType */
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
                || end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            return getAudioFileIntent(filePath);
        } else if (end.equals("3gp") || end.equals("mp4")) {
            return getAudioFileIntent(filePath);
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
                || end.equals("jpeg") || end.equals("bmp")) {
            return getImageFileIntent(filePath);
        } else if (end.equals("apk")) {
            return getApkFileIntent(filePath);
        } else if (end.equals("ppt")) {
            return getPptFileIntent(filePath);
        } else if (end.equals("xls")) {
            return getExcelFileIntent(filePath);
        } else if (end.equals("doc")) {
            return getWordFileIntent(filePath);
        } else if (end.equals("pdf")) {
            return getPdfFileIntent(filePath);
        } else if (end.equals("chm")) {
            return getChmFileIntent(filePath);
        } else if (end.equals("txt")) {
            return getTextFileIntent(filePath, false);
        } else {
            return getFileIntent(filePath);
        }
    }

    /** Android获取一个用于打开APK文件的intent
     * @param param  文件路径
     * @return
     */
    public static Intent getFileIntent(String param) {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "*/*");
        return intent;
    }

    /** Android获取一个用于打开APK文件的intent
     * @param param 文件路径
     * @return
     */
    public static Intent getApkFileIntent(String param) {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        return intent;
    }

    /**Android获取一个用于打开VIDEO文件的intent
     * @param param 文件路径
     * @return
     */
    public static Intent getVideoFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "video/*");
        return intent;
    }

    /**Android获取一个用于打开AUDIO文件的intent
     * @param param 文件路径
     * @return
     */
    public static Intent getAudioFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "audio/*");
        return intent;
    }

    /**Android获取一个用于打开Html文件的intent
     * @param param 文件路径
     * @return
     */
    public static Intent getHtmlFileIntent(String param) {

        Uri uri = Uri.parse(param).buildUpon()
                .encodedAuthority("com.android.htmlfileprovider")
                .scheme("content").encodedPath(param).build();
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uri, "text/html");
        return intent;
    }
    /**
     *  Android获取一个用于打开图片文件的intent
     * @param param 文件路径
     * @return
     */
    public static Intent getImageFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "image/*");
        return intent;
    }

    /**Android获取一个用于打开PPT文件的intent
     * @param param 文件路径
     * @return
     */
    public static Intent getPptFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        return intent;
    }

    // Android获取一个用于打开Excel文件的intent
    public static Intent getExcelFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;
    }

    // Android获取一个用于打开Word文件的intent
    public static Intent getWordFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/msword");
        return intent;
    }
    /**
     *  Android获取一个用于打开CHM文件的intent
     * @param param 文件路径
     * @return
     */
    public static Intent getChmFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/x-chm");
        return intent;
    }

    /**Android获取一个用于打开文本文件的intent
     * @param param
     * @param paramBoolean
     * @return
     */
    public static Intent getTextFileIntent(String param, boolean paramBoolean) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (paramBoolean) {
            Uri uri1 = Uri.parse(param);
            intent.setDataAndType(uri1, "text/plain");
        } else {
            Uri uri2 = Uri.fromFile(new File(param));
            intent.setDataAndType(uri2, "text/plain");
        }
        return intent;
    }

    /**Android获取一个用于打开PDF文件的intent
     * @param param 文件路径
     * @return
     */
    public static Intent getPdfFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }

    /**
     * 取得一个带文字选择信息的图片选择的Intent
     * @param choose  选择提示信息
     * @return
     */
    public static Intent getImgChooseIntent(String choose){
        Intent innerIntent = new Intent(Intent.ACTION_PICK); //从列表中选择某项并返回所选数据
        innerIntent.setType("image/*");
        Intent wrapperIntent = Intent.createChooser(innerIntent, choose);
        return wrapperIntent;
    }

    /**
     * 调出拨打电话的界面
     * @param callnumber  要拨打的电话号码
     * @return
     */
    public static  Intent getCallInterface(String callnumber){
        String struri="tel:"+callnumber;
        Uri uri = Uri.parse(struri);
        Intent callintent = new Intent(Intent.ACTION_DIAL, uri);
        return  callintent;
    }

    /**
     * 直接调出拨打电话的界面   <uses-permission id="android.permission.CALL_PHONE" /> 拨打电话的权限
     * @param callnumber
     * @return
     */
    public static  Intent  getcall(String callnumber){
        String struri="tel:"+callnumber;
        Uri uri = Uri.parse(struri);
        Intent callintent = new Intent(Intent.ACTION_CALL, uri);
        return  callintent;
    }
}
