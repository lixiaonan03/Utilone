package com.lxn.utilone.util;

import android.widget.Toast;

import com.lxn.utilone.UtilApplication;

public class ToastUtils {

    public static void toastshort(String str){
    	Toast.makeText(UtilApplication.application,str, Toast.LENGTH_SHORT).show();
    }
    public static void toastlong(String str){
    	Toast.makeText(UtilApplication.application,str, Toast.LENGTH_LONG).show();
    }
}
