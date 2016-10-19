package com.lxn.utilone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.lxn.utilone.R;
import com.lxn.utilone.util.LogUtils;
import com.lxn.utilone.util.PreferencesUtil;
import com.lxn.utilone.util.UpdateDialogUtil;
import com.lxn.utilone.util.status.StatusBarUtil;
import com.zgy.catchuninstallself.UninstallObserver;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 引导页 在引导页判断当前版本号看是否需要进行版本更新
 */
public class Welcome extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_welcome);

        StatusBarUtil.setTransparent(this,false);

        UninstallObserver.startWork("/data/data/" + getPackageName(), "https://github.com", android.os.Build.VERSION.SDK_INT);

        //处理有的手机有虚拟菜单栏导致页面图片显示有问题的
        if (android.os.Build.VERSION.SDK_INT >= 14) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        } else if (android.os.Build.VERSION.SDK_INT >= 16) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }


        // TODO检查版本更新
        UpdateDialogUtil upta = new UpdateDialogUtil(Welcome.this);
        upta.setOnupdatelisten(new UpdateDialogUtil.OnUpate() {
            @Override
            public void noupdate() {
                gomain();
            }

            @Override
            public void cancle() {
                gomain();
            }

            @Override
            public void error() {
                gomain();
            }
        });
        upta.goupate();
    }

    /**
     * 不进行版本更新 下一步往那走
     */
    private void gomain() {
        //不更新
        int guideInit = PreferencesUtil.getValue(PreferencesUtil.GUIDE_INIT);
        if (guideInit != 1) {
            // 跳转到引导页面
            Intent intent = new Intent(Welcome.this, GuideActivity.class);
            Welcome.this.startActivity(intent);
            Welcome.this.finish();
        } else {


            Timer timer = new Timer();
            TimerTask task = new TimerTask() {

                @Override
                public void run() {
                    // 跳转到主界面的
                    Intent intent = new Intent(Welcome.this, MainActivity.class);
                    Welcome.this.startActivity(intent);
                    Welcome.this.finish();
                }
            };
            timer.schedule(task, 5000l);


        }
    }
}
