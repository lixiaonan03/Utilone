package com.lxn.utilone.util;

import android.widget.Toast;

import com.xyyy.farm.FarmApplication;

public class ToastUtils {

    public static void toastshort(String str){
    	Toast.makeText(FarmApplication.application,str, Toast.LENGTH_SHORT).show();
    }
    public static void toastlong(String str){
    	Toast.makeText(FarmApplication.application,str, Toast.LENGTH_LONG).show();
    }
}
