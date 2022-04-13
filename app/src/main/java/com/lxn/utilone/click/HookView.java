package com.lxn.utilone.click;

import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author lixiaonan
 * 功能描述: 创建方式
 * 时 间： 2022/4/6 11:49 AM
 */
public class HookView {
    public Method mHookMethod;
    public Field mHookField;

    public HookView(View view) {
        try {
            Class viewClass = Class.forName("android.view.View");
            mHookMethod = viewClass.getDeclaredMethod("getListenerInfo");
            mHookMethod.setAccessible(true);
            Class listenerInfoClass = Class.forName("android.view.View$ListenerInfo");
            mHookField = listenerInfoClass.getDeclaredField("mOnClickListener");
            mHookField.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
