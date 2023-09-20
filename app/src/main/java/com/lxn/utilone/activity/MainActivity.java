package com.lxn.utilone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Choreographer;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.lxn.utilone.R;
import com.lxn.utilone.activity.algorithm.AlgorIthmActivity;
import com.lxn.utilone.activity.architecture.ArchitectureActivity;
import com.lxn.utilone.activity.rv.RvActivity;
import com.lxn.utilone.databinding.ActivityMainBinding;
import com.lxn.utilone.modle.PersonLxn;
import com.lxn.utilone.util.DeviceUtil;
import com.lxn.utilone.util.Log;
import com.lxn.utilone.util.LogUtils;
import com.lxn.utilone.util.operationutil.ThreadUtils;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.List;

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
    private ActivityMainBinding vb;

    private Handler handler =ThreadUtils.getMainHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vb = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(vb.getRoot());

        Log.i("lxnPush", "首页的=taskid="+getTaskId());

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

        TextView sizeView= findViewById(R.id.sizeView);
        sizeView.setOnClickListener(v -> ARouter.getInstance().build(ActivityConstans.View_PATH).navigation());

        TextView mvvm= findViewById(R.id.mvvm);
        mvvm.setOnClickListener(v -> ARouter.getInstance().build(ActivityConstans.MVVM1_PATH).navigation());

        findViewById(R.id.tvDev).setOnClickListener(v -> startActivity(new Intent(MainActivity.this,DevActivity.class)));

        //Glide 测试的
        findViewById(R.id.tvGlide).setOnClickListener(v ->  ARouter.getInstance().build(ActivityConstans.GLIDE_PATH).navigation());
        //tvRV 测试的
        findViewById(R.id.tvRV).setOnClickListener(v ->  RvActivity.Companion.startActivity(MainActivity.this));

        //firebase 测试的
        findViewById(R.id.tvFirebase).setOnClickListener(v ->  {

            List<String> list =new ArrayList<>();
            list.add("1");
            list.get(2);

        } );
        //语言设置的
        findViewById(R.id.tvLanguage).setOnClickListener(v ->  ARouter.getInstance().build(ActivityConstans.LANGUAGE_PATH).withInt("lxn",100).navigation());

        findViewById(R.id.tvMeasure).setOnClickListener(v ->  ARouter.getInstance().build(ActivityConstans.MEASURE_PATH).navigation());
        findViewById(R.id.tvSuanfa).setOnClickListener(v -> AlgorIthmActivity.Companion.startActivity(MainActivity.this));
        //架构学习的
        findViewById(R.id.tvArchitecture).setOnClickListener(v -> ArchitectureActivity.Companion.startActivity(MainActivity.this));



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
                //注册监听下一次 vsync信号  不需要一直监听
//                Choreographer.getInstance().postFrameCallback(this);
            }
        });

//        Transformation<Bitmap> circleCrop = new CircleCrop();
//        Glide.with(this)
//                .load("https://upload-images.jianshu.io/upload_images/6038844-a7cc326f385542f5.gif?imageMogr2/auto-orient/strip|imageView2/2/format/webp")
//                .optionalTransform(WebpDrawable.class, new WebpDrawableTransformation(circleCrop))
//                .into(vb.ivImage);

        Glide.with(this).load("https://upload-images.jianshu.io/upload_images/6038844-a7cc326f385542f5.gif?imageMogr2/auto-orient/strip|imageView2/2/format/webp").into(vb.ivImage);

//        String picPath = "https://pic.wanwustore.cn/Fjt_IddpcNV6aNAyxCfRRBFHU_-c?imageView2/2/w/300";
        String picPath = "https://rcns_trustworthy.s3.rapidcompute.com/appbucket111/identityCardFront/5a723f010d1e4d4987cc2b7902cf2263.jpg";

        Glide.with(this).load(picPath).into(vb.ivImage1);

//        testAnr();
        //给looper设置自定义的打印器的
        Looper.myLooper().setMessageLogging(x -> {
//              Log.i("lxn123",x);
        });


        //测试屏幕分辨率的
        Log.i("lxnDpi","=densityDpi===="+ DeviceUtil.densityDpi);
        Log.i("lxnDpi","=scaledDensity===="+ DeviceUtil.scaledDensity);
        Log.i("lxnDpi","=density===="+ DeviceUtil.density);
        Log.i("lxnDpi","=SCREEN_HEIGHT===="+ DeviceUtil.SCREEN_HEIGHT);
        Log.i("lxnDpi","=xdpi===="+ DeviceUtil.getDisplayMetrics().xdpi);


        vb.tvTest3.setText("设置进去的---");
        Log.i("lxnTextView","=vb.tvTest3===="+ vb.tvTest3.getText().toString());
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
