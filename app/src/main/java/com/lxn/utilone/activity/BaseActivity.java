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
        //hookview的
        ProxyManager.hookViews(getWindow().getDecorView());

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
