package com.lxn.utilone.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.Choreographer;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.lxn.utilone.R;
import com.lxn.utilone.aspect.BehaviorAnno;
import com.lxn.utilone.modle.PersonLxn;
import com.lxn.utilone.util.Log;
import com.lxn.utilone.util.LogUtils;
import com.lxn.utilone.util.operationutil.ThreadUtils;
import com.tencent.mmkv.MMKV;

/**
  *  @author lixiaonan
  *  功能描述: 首页的
  *  时 间： 2021/11/26 4:33 PM
  */
@Route(path = ActivityConstans.MAIN_PATH , name = "首页")
public class MainActivity extends BaseActivity{


    //记录上次的帧时间
    private long mLastFrameTime;

    private int mFrameCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        testMMKV();
        TextView tvOkHttp= findViewById(R.id.tvOkhttp);
        tvOkHttp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MMKV kv = MMKV.mmkvWithID("lxn");
                Log.i("lxnmmkv","准备点击的的====="+kv.count()+"");
                kv.removeValuesForKeys(new String[]{"1","2"});
                Log.i("lxnmmkv","准备点击删除之后的====="+kv.count()+kv.allKeys());
                v.invalidate();
                  //测试发送万物的
//                Uri uri = Uri.parse("wwopenc2fe743aaeec03f9://openvc%3A%2F%2F%3Ftab%3Dmallhomevc");
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(intent);
//                ThreadUtils.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Uri uri = Uri.parse("wwopenc2fe743aaeec03f9://openvc%3A%2F%2F%3Ftab%3Dorganizehomevc");
//                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                        startActivity(intent);
//                    }
//                });
                ARouter.getInstance().build(ActivityConstans.OKHTTP_PATH).navigation();
            }
        });


        TextView tvCompress= findViewById(R.id.tvCompress);
        tvCompress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(ActivityConstans.COMPOSE_PATH).navigation();
            }
        });
        TextView jsBridge= findViewById(R.id.jsBridge);
        jsBridge.setOnClickListener(v -> ARouter.getInstance().build(ActivityConstans.JS_WebView_PATH).navigation());


        //监听掉帧的情况的
        Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
            @Override
            public void doFrame(long frameTimeNanos) {
                LogUtils.iWithTag("lxnFrame","回调的=="+frameTimeNanos);
                //每500毫秒重新赋值一次最新的帧时间
                if (mLastFrameTime == 0) {
                    mLastFrameTime = frameTimeNanos;
                }
                //本次帧开始时间减去上次的时间除以100万，得到毫秒的差值
                float diff = (frameTimeNanos - mLastFrameTime) / 1000000.0f;
                //这里是500毫秒输出一次帧率
                if (diff > 500) {
                    double fps = (((double) (mFrameCount * 1000L)) / diff);
                    mFrameCount = 0;
                    mLastFrameTime = 0;
                    Log.d("doFrame", "doFrame: " + fps);
                } else {
                    ++mFrameCount;
                }
                //注册监听下一次 vsync信号
//                Choreographer.getInstance().postFrameCallback(this);
            }
        });
    }

    /**
     * 测试mmkv的
     */
//    @BehaviorAnno(value = "lxn测试AOP")
    private void testMMKV(){
        MMKV kv = MMKV.mmkvWithID("lxn");
        kv.encode("1",new PersonLxn("lxn1",21));
        kv.encode("2",new PersonLxn("lxn2",22));
        kv.encode("3",new PersonLxn("lxn3",23));
        kv.encode("4",new PersonLxn("lxn4",24));
        kv.encode("5",new PersonLxn("lxn5",25));
        Log.i("lxnmmkv",kv.count()+"");
    }


}
