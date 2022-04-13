package com.lxn.utilone.click;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author：李晓楠 时间：2022/4/6 12:56
 */
public class ProxyLayout extends FrameLayout {

    public ProxyLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_UP){

        }
        return super.dispatchTouchEvent(ev);
    }
}
