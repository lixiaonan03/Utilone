package com.lxn.utilone.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.lxn.utilone.R;

/**
  *  @author lixiaonan
  *  功能描述: 首页的
  *  时 间： 2021/11/26 4:33 PM
  */
@Route(path = ActivityConstans.MAIN_PATH , name = "首页")
public class MainActivity extends BaseActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        TextView tvOkHttp= findViewById(R.id.tvOkhttp);
        tvOkHttp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(ActivityConstans.OKHTTP_PATH).navigation();
            }
        });

    }
}
