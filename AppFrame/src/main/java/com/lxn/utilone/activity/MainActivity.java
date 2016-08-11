package com.lxn.utilone.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lxn.utilone.AppManager;
import com.lxn.utilone.R;
import com.lxn.utilone.db.CartDao;
import com.lxn.utilone.fragment.CartFragment;
import com.lxn.utilone.fragment.ClassifyFragment;
import com.lxn.utilone.fragment.HomeFragment;
import com.lxn.utilone.fragment.MycenterFragment;

import java.lang.ref.WeakReference;

/**
 * 主Activity
 *
 * @author lxn
 */
public class MainActivity extends FragmentActivity {

    public int FLAG_HOME = 0;
    public int FLAG_CLASSIFY = 1;
    public int FLAG_CART = 2;
    public int FLAG_MYCENTER = 3;

    private HomeFragment homeFragment;// 首页
    private ClassifyFragment classifyFragment;// 分类
    private CartFragment cartFragment;// 购物车的
    private MycenterFragment mycenterFragment;// 我的新侬
    private Fragment[] fragments;
    private int index;// 将要展示那个fragment
    private int currentTabIndex;// 页面当前展示的是那个fragment
    private int flag;// 跳转标记 0 首页 1 分类 2购物车 3我的新侬
    private String username;// 从登录跳转回来的 所带的用户名
    private String imgurl;
    private RelativeLayout rel_cart;
    private RelativeLayout rel_home;
    private RelativeLayout rel_classify;
    private RelativeLayout rel_mycenter;
    private RelativeLayout num_rel;
    private TextView item_count;
    private ChangeReceiver receiver;


    private MyHandler mHandler = new MyHandler(this);

    /**
     * handle 消息处理对象 使用弱引用持有外部activity的引用 防止内存泄露的情况产生
     */
    private static class MyHandler extends Handler {
        private WeakReference<Context> reference;

        public MyHandler(Context context) {
            reference = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = (MainActivity) reference.get();
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    activity.rel_home.performClick();
                    break;
                case 1:
                    activity.rel_classify.performClick();
                    break;
                case 2:
                    activity.rel_cart.performClick();
                    break;
                case 3:
                    activity.rel_mycenter.performClick();
                    break;
                default:
                    break;
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        setContentView(R.layout.activity_main);
        flag = getIntent().getIntExtra("flag", 0);
        username = getIntent().getStringExtra("username");
        imgurl = getIntent().getStringExtra("imgurl");

        initView();
        homeFragment = new HomeFragment();
        classifyFragment = new ClassifyFragment();
        cartFragment = new CartFragment();
        mycenterFragment = new MycenterFragment();
        fragments = new Fragment[]{homeFragment, classifyFragment,
                cartFragment, mycenterFragment};






        /*//TODO    原来的添加fragment
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, homeFragment)
                .show(homeFragment).commit();*/




        // 根据不同的进入的标志添加fragment
        if(0==flag){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, homeFragment)
                    .addToBackStack(null)
                    .show(homeFragment).commit();
        }else if(1==flag){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, classifyFragment)
                    .addToBackStack(null)
                    .show(homeFragment).commit();
        }else if(3==flag){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, mycenterFragment)
                    .addToBackStack(null)
                    .show(mycenterFragment).commit();
        }


        // 初始化广播
        receiver = new ChangeReceiver();
        LocalBroadcastManager broadcastManager = LocalBroadcastManager
                .getInstance(MainActivity.this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("changeallmoney");
        broadcastManager.registerReceiver(receiver, intentFilter);

    }

    /**
     * 初始化底部按钮
     */
    private void initView() {
        rel_home = (RelativeLayout) findViewById(R.id.rel_home);
        rel_classify = (RelativeLayout) findViewById(R.id.rel_classify);
        rel_cart = (RelativeLayout) findViewById(R.id.rel_cart);
        num_rel = (RelativeLayout) findViewById(R.id.num_rel);
        item_count = (TextView) findViewById(R.id.item_count);
        rel_mycenter = (RelativeLayout) findViewById(R.id.rel_mycenter);

        rel_home.setOnClickListener(viewclick);
        rel_classify.setOnClickListener(viewclick);
        rel_cart.setOnClickListener(viewclick);

        rel_mycenter.setOnClickListener(viewclick);
        switch (flag) {

            case 3:
                rel_mycenter.setSelected(true);
                Message message = mHandler.obtainMessage();
                message.what = 3;
                mHandler.sendMessage(message);
                break;
            default:
                rel_home.setSelected(true);
                break;
        }

    }


    OnClickListener viewclick = new OnClickListener() {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.rel_home:
                    index = FLAG_HOME;
                    break;
                case R.id.rel_classify:
                    index = FLAG_CLASSIFY;
                    break;
                case R.id.rel_cart:
                    index = FLAG_CART;
                    break;

                case R.id.rel_mycenter:
                    index = FLAG_MYCENTER;
                    break;
            }
            if (currentTabIndex != index) {
                FragmentTransaction trx = getSupportFragmentManager()
                        .beginTransaction();
                trx.hide(fragments[currentTabIndex]);
                if (!fragments[index].isAdded()) {
                    trx.add(R.id.fragment_container, fragments[index]);
                    trx.addToBackStack(null);
                    if (index == FLAG_MYCENTER) {
						/* 创建一个Bundle用来存储数据，传递到Fragment中 */
                        Bundle bundle = new Bundle();
						/* 往bundle中添加数据 */
                        bundle.putString("imgurl", imgurl);
                        bundle.putString("name", username);
						/* 把数据设置到Fragment中 */
                        fragments[index].setArguments(bundle);
                    }
                }
                trx.show(fragments[index]).commit();
            }
            switch (currentTabIndex) {
                case 0:
                    rel_home.setSelected(false);
                    break;
                case 1:
                    rel_classify.setSelected(false);
                    break;
                case 2:
                    rel_cart.setSelected(false);
                    break;
                case 3:
                    rel_mycenter.setSelected(false);
                    break;

                default:
                    break;
            }
            // 当前的菜单按钮设为选中状态
            switch (index) {
                case 0:
                    rel_home.setSelected(true);
                    break;
                case 1:
                    rel_classify.setSelected(true);
                    break;
                case 2:
                    rel_cart.setSelected(true);
                    break;
                case 3:

                    rel_mycenter.setSelected(true);
                    break;

                default:
                    break;
            }
            currentTabIndex = index;
        }
    };

    @Override
    public void onBackPressed() {
        if (currentTabIndex != 0) {
            Message message = mHandler.obtainMessage();
            message.what = 0;
            mHandler.sendMessage(message);
        } else {
            isExit();
        }
    }

    public void isExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("您确定要退出么？");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AppManager.getAppManager().AppExit();
                dialog.dismiss();// 取消dialog，或dismiss掉
            }
        });
        if (!isFinishing()) {
            builder.create().show();
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
            // 未登录 查询本地数据库 查询出所有商品数量的 总数
            int goodnum = CartDao.getInstance().queryNumAll();
            if (goodnum > 0) {
                num_rel.setVisibility(View.VISIBLE);
                item_count.setText("" + goodnum);
            } else {
                num_rel.setVisibility(View.GONE);
            }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 01 && resultCode == 01) {
            if (homeFragment != null) {
                homeFragment.onActivityResult(requestCode, resultCode, data);
            }
        }
        if (requestCode == 02 && resultCode == 02) {
            if (cartFragment != null) {
                cartFragment.onActivityResult(requestCode, resultCode, data);
            }
        }
        if (currentTabIndex==3) {
            if (mycenterFragment != null) {
                mycenterFragment
                        .onActivityResult(requestCode, resultCode, data);
            }
        }
    }
    private class ChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("changeallmoney".equals(action)) {
                    // 未登录 查询本地数据库 查询出所有商品数量的 总数
                    int goodnum = CartDao.getInstance().queryNumAll();
                    if (goodnum > 0) {
                        num_rel.setVisibility(View.VISIBLE);
                        item_count.setText("" + goodnum);
                    } else {
                        num_rel.setVisibility(View.GONE);
                    }
            }
        }
    }
}
