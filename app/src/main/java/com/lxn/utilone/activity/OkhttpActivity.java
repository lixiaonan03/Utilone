package com.lxn.utilone.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lxn.utilone.R;
import com.lxn.utilone.databinding.ActivityOkhttpBinding;

/**
  *  @author lixiaonan
  *  功能描述: okhttp测试页的
  *  时 间： 2021/11/26 4:33 PM
  */
@Route(path = ActivityConstans.OKHTTP_PATH , name = "okhttp测试页")
public class OkhttpActivity extends BaseActivity{

    private ActivityOkhttpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOkhttpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    /**
     * 初始化view
     */
    private void initView(){
        binding.linTop.topText.setText("okhttp学习");
        binding.linTop.topBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
