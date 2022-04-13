package com.lxn.utilone.click;

import android.view.View;
import android.view.ViewGroup;

import com.lxn.utilone.util.Log;

/**
  *  @author lixiaonan    通过代理来实现的
  *  功能描述: hook OnClickListenr对象
 *    1、创建监听代理管理类，用于统一管理OnClickListenr对象的调用即实现：
 *    2、创建反射管理类，用于保存hook到的OnClickListener对象：
 *    3、递归深度遍历所有的控件，为其替换OnClickListenr对象
  *  时 间： 2022/4/6 11:42 AM
  */
public class ProxyManager {
    /**
     * 发送日志
     * @param view
     */
    public static void sendLog(View view){
        //模拟发送一些日志
        Log.i("lxnclick","设置代理对象的的view==="+view.toString());
    }


    public static class ProxyListener implements View.OnClickListener{
        View.OnClickListener mOriginalListener;

        public ProxyListener(View.OnClickListener mOriginalListener) {
            this.mOriginalListener = mOriginalListener;
        }

        @Override
        public void onClick(View v) {
            sendLog(v);
            if(mOriginalListener!=null){
                mOriginalListener.onClick(v);
            }
        }
    }

    public static void hookViews(View view) {
        try {
            if(view.getVisibility() == View.VISIBLE) {
                if(view instanceof ViewGroup) {
                    ViewGroup group = (ViewGroup) view;
                    int count = group.getChildCount();
                    for(int i=0; i<count; i++) {
                        View child = group.getChildAt(i);
                        hookViews(child);
                    }
                } else {
                    if(view.isClickable()) {
                        HookView hookView = new HookView(view);
                        Object listenerInfo = hookView.mHookMethod.invoke(view);
                        Object originalListener = hookView.mHookField.get(listenerInfo);
                        // 反射字段 set方法设置的参数  第一个为 设置的类对象是那个  第2个是设置的字段对象是按个
                        hookView.mHookField.set(listenerInfo, new ProxyManager.ProxyListener((View.OnClickListener)originalListener));
                    }
                }
            }
        } catch (Exception e) {

        }
    }
}
