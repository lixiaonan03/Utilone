package com.lxn.utilone.activity;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.lxn.utilone.R;
import com.lxn.utilone.hook.InstrumentationProxy;

import java.lang.reflect.Field;

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

        replaceActivityInstrumentation(this);

        ImageView ivGoMain= findViewById(R.id.ivWelcome);
        ivGoMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
//                startActivity(intent);
                ARouter.getInstance().build(ActivityConstans.MAIN_PATH).navigation();
            }
        });

    }

    /**
     * 替换跳转的变量的
     * @param activity
     */
    public void replaceActivityInstrumentation(Activity activity){
        try {
            Field field = Activity.class.getDeclaredField("mInstrumentation");
            //取消权限检查的
            field.setAccessible(true);
            Instrumentation instrumentation = (Instrumentation)field.get(activity);
            InstrumentationProxy instrumentationProxy =new InstrumentationProxy(instrumentation);
            field.set(activity,instrumentationProxy);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
