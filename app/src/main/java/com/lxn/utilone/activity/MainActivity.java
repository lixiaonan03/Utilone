package com.lxn.utilone.activity;

import android.os.Binder;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.lxn.utilone.R;
import com.lxn.utilone.modle.PersonLxn;
import com.lxn.utilone.util.Log;
import com.tencent.mmkv.MMKV;

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

        testMMKV();
        TextView tvOkHttp= findViewById(R.id.tvOkhttp);
        tvOkHttp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MMKV kv = MMKV.mmkvWithID("lxn");
                Log.i("lxnmmkv","准备点击的的====="+kv.count()+"");
                kv.removeValuesForKeys(new String[]{"1","2"});
                Log.i("lxnmmkv","准备点击删除之后的====="+kv.count()+kv.allKeys());
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

    }

    /**
     * 测试mmkv的
     */
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
