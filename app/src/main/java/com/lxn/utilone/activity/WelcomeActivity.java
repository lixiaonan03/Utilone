package com.lxn.utilone.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.lxn.utilone.R;

/**
 * @author lixiaonan
 * 功能描述: 引导页处理的
 * 时 间： 2021/11/26 12:26 PM
 */
public class WelcomeActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome);


        ImageView ivGoMain= findViewById(R.id.ivWelcome);
        ivGoMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(ActivityConstans.MAIN_PATH).navigation();
            }
        });

    }
}
