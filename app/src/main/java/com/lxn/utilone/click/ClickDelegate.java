package com.lxn.utilone.click;

import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;

import com.lxn.utilone.util.Log;

/**
  *  @author lixiaonan
  *  功能描述: 辅助代理的
  *  时 间： 2022/4/6
  */
public class ClickDelegate extends View.AccessibilityDelegate {
    @Override
    public void sendAccessibilityEvent(View host, int eventType) {
        super.sendAccessibilityEvent(host, eventType);
        if (eventType == AccessibilityEvent.TYPE_VIEW_CLICKED) {
            //发送埋点日志的
            Log.i("lxnAccessibilityDelegate", "辅助发送日志host=" + host.toString());
        }
    }

    public ClickDelegate(final View rootView) {
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            setDelegate(rootView);
        });
    }


    public void setDelegate(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            if (view instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) view;
                int count = group.getChildCount();
                for (int i = 0; i < count; i++) {
                    View child = group.getChildAt(i);
                    setDelegate(child);
                }
            } else {
                if (view.isClickable()) {
                    view.setAccessibilityDelegate(this);
                }
            }
        }
    }
}
