package com.lxn.utilone.activity;

import android.os.Bundle;
import android.view.View;

import com.lxn.utilone.click.ClickDelegate;
import com.lxn.utilone.click.ProxyManager;

import androidx.appcompat.app.AppCompatActivity;


/**
 * activity的基类
 *
 * @author lxn
 */
public class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onStart() {
        super.onStart();
        /**
         * Hook机制基于java反射原理，从rootview开始，递归遍历所有的控件View对象，并hook其对应的OnClickListenr对象，
         * 将其替换成用于上报日志的监听代理类ProxyListener，从而实现动态hook。这里模拟的是点击事件
         */
        ProxyManager.hookViews(getWindow().getDecorView());

        /**
         * AccessibilityDelegate同样也是辅助功能，其辅助主体主要是APP上的具体控件View，可检测控件点击，选中，滑动，
         * 文本变化等，当该view的相关属性出现变化时，将回调AccessibilityDelegate中的sendAccessibilityEvent，
         * 具体事件类型通过AccessibilityEvent来区分。
         */
        new ClickDelegate(getWindow().getDecorView());
    }

    @Override
    public void openContextMenu(View view) {
        super.openContextMenu(view);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub

        super.onResume();
        //overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
        //overridePendingTransition(R.anim.out_to_left, R.anim.out_to_right);
        //overridePendingTransition(R.anim.out_to_right, R.anim.out_to_left);

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }


}
