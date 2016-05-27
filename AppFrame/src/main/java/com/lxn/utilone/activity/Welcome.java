package com.lxn.utilone.activity;

import android.content.Intent;
import android.os.Bundle;

import com.lxn.utilone.R;
import com.lxn.utilone.util.PreferencesUtil;
import com.lxn.utilone.util.UpdateDialogUtil;

/**
 * 引导页 在引导页判断当前版本号看是否需要进行版本更新
 */
public class Welcome extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);
        // TODO检查版本更新
        UpdateDialogUtil upta = new UpdateDialogUtil(Welcome.this);
        upta.setNoUpdateListener(new UpdateDialogUtil.NoUpdateListener() {
            @Override
            public void noupdate() {
                // 不更新
                int guideInit = PreferencesUtil
                        .getValue(PreferencesUtil.GUIDE_INIT);
                if (guideInit != 1) {
                    // 跳转到引导页面
                    Intent intent = new Intent(Welcome.this,
                            GuideActivity.class);
                    Welcome.this.startActivity(intent);
                    Welcome.this.finish();
                } else {
                    // 跳转到主界面的
                    Intent intent = new Intent(Welcome.this, MainActivity.class);
                    Welcome.this.startActivity(intent);
                    Welcome.this.finish();
                    /*
                     * * Timer timer = new Timer(); TimerTask task = new
					 * TimerTask() {
					 *
					 * @Override public void run() {
					 *
					 * } }; timer.schedule(task, 1000 * 1);
					 */

                }
            }
        });
        upta.goupdate();
    }


}
