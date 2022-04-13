package com.lxn.utilone.view;

import android.view.View;

import java.util.Calendar;

/**
 * @author lixiaonan
 * 功能描述: 防止重复点击的
 * 时 间： 2022/4/1 aop
 */
public abstract class NoDoubleClickListener implements View.OnClickListener {

    public static final int MIN_CLICK_DELAY_TIME = 1000;

    private long lastClickTime = 0;

    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onNoDoubleClick(v);
            //可以做一些埋点监控的事情

        }
    }

    public abstract void onNoDoubleClick(View v);
}