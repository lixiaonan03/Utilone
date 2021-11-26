package com.lxn.utilone.activity;

import android.os.Bundle;

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
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }


}
