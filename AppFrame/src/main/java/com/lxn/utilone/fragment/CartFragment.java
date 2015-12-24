package com.lxn.utilone.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lxn.utilone.R;
import com.lxn.utilone.adapter.MycartItemAdapter;
import com.lxn.utilone.db.Cart;
import com.lxn.utilone.db.CartDao;
import com.lxn.utilone.model.GoodCarVO;
import com.lxn.utilone.util.MathUtil;
import com.lxn.utilone.util.StrToNumber;
import com.lxn.utilone.util.ToastUtils;
import com.lxn.utilone.view.CustomProgressDialog;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车模块
 *
 * @author lxn
 */
public class CartFragment extends BaseFragment {

    private TextView cart_edit;// 编辑按钮
    private CheckBox checkBoxall;
    private TextView allmoneytextview;
    private Button pay;
    private ListView listview;
    private MycartItemAdapter adapter;
    private List<Cart> list = new ArrayList<Cart>();
    private BigDecimal allmoney;
    private int flag = 1;// 1 代表支付状态 2 代表 编辑状态
    private CustomProgressDialog customProgressDialog;
    private RelativeLayout yeslogin;
    private LinearLayout nodata;
    private TextView nodatatext;
    private ImageView nodataimg;
    private int cartgoodflag = 0;//购物车中的商品标记   0是无电子类商品 1有电子类商品
    private ChangeReceiver receiver;

    private boolean isclick = false;//底部全选按钮 是否是通过广播接收处理的 false不是  true 是

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onFirstVisible(Bundle savedInstanceState) {
        super.onFirstVisible(savedInstanceState);
        initview();
        customProgressDialog = new CustomProgressDialog(getActivity(),
                "正在加载......");

        // 初始化广播
        receiver = new ChangeReceiver();
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("changeallmoney");
        intentFilter.addAction("changeallmoneyand");
        broadcastManager.registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onFragmentPause() {
        super.onFragmentPause();
    }

    @Override
    public void onFragmentResume() {
        super.onFragmentResume();
        changeviewbylogin();
    }


    private void initview() {
        yeslogin = (RelativeLayout) getView().findViewById(R.id.yeslogin);
        nodata = (LinearLayout) getView().findViewById(R.id.nodata);
        nodata.setOnClickListener(viewclick);
        nodatatext = (TextView) getView().findViewById(R.id.nodatatext);
        nodataimg = (ImageView) getView().findViewById(R.id.nodataimg);

        cart_edit = (TextView) getView().findViewById(R.id.cart_edit);
        checkBoxall = (CheckBox) getView().findViewById(R.id.checkBoxall);
        checkBoxall.setChecked(true);
        allmoneytextview = (TextView) getView().findViewById(
                R.id.allmoneytextview);
        pay = (Button) getView().findViewById(R.id.pay);
        listview = (ListView) getView().findViewById(R.id.listview);
        cart_edit.setOnClickListener(viewclick);
        pay.setOnClickListener(viewclick);
        checkBoxall.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (isclick) {
                    isclick = false;
                    return;
                } else {
                    isclick = false;
                    for (Cart mycart : adapter.get_list()) {
                        if (arg1) {
                            mycart.setFlag(0);
                        } else {
                            mycart.setFlag(1);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }

            }
        });
        adapter = new MycartItemAdapter(getActivity(), list);
        listview.setAdapter(adapter);
    }

    /**
     * 根据登录状态改变view
     */
    void changeviewbylogin() {
        //去数据库查询 未登录的购物车的数据
        list = CartDao.getInstance().queryAllGood();
        if (null != list && list.size() > 0) {
            cart_edit.setVisibility(View.VISIBLE);
            yeslogin.setVisibility(View.VISIBLE);
            nodata.setVisibility(View.GONE);
            adapter.set_list(list);
            adapter.notifyDataSetChanged();
            changeallmoney();
        } else {
            cart_edit.setVisibility(View.GONE);
            nodatatext.setText("您的购物车当前没有任何商品。");
            yeslogin.setVisibility(View.GONE);
            nodata.setVisibility(View.VISIBLE);
        }
    }

    OnClickListener viewclick = new OnClickListener() {

        @Override
        public void onClick(View arg0) {
            switch (arg0.getId()) {
                case R.id.cart_edit:
                    if (null != list && list.size() > 0) {
                        //编辑按钮
                        if (flag == 1) {
                            // 当前是支付状态 点击后变成编辑状态
                            cart_edit.setText("完成");
                            flag = 2;
                            adapter.setFlag(flag);
                            allmoneytextview.setVisibility(View.GONE);
                            pay.setText("删除");

                            checkBoxall.setChecked(false);
                            for (Cart mycart : adapter.get_list()) {
                                mycart.setFlag(1);
                            }
                            adapter.notifyDataSetChanged();
                            changeallmoney();
                        } else {
                            cart_edit.setText("编辑");
                            flag = 1;
                            adapter.setFlag(flag);
                            allmoneytextview.setVisibility(View.VISIBLE);
                            pay.setText("去结算");
                            checkBoxall.setChecked(true);
                            for (Cart mycart : adapter.get_list()) {
                                mycart.setFlag(0);
                            }
                            adapter.notifyDataSetChanged();
                            changeallmoney();
                        }
                    }

                    break;
                case R.id.pay:

                    break;
                case R.id.nodata:

                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 改变总结金额
     */
    private void changeallmoney() {
        allmoney =new BigDecimal(0.00);
        for (Cart mycart : adapter.get_list()) {
            if (mycart.getFlag() == 0) {
                allmoney=allmoney.add(MathUtil.getMultiply(mycart.getGoodprice(), mycart.getNum()));
            }
        }
        BigDecimal f1 = MathUtil.subDigit(allmoney,2);
        allmoneytextview.setText("合计：￥" + f1);
    }

    /**
     * 把服务端接口使用的GoodCarVO 转化成 自己使用的Cart 实体
     *
     * @param t
     * @return
     */
    private List<Cart> changeGoodCarVOToCart(List<GoodCarVO> t) {
        List<Cart> cartlist = new ArrayList<Cart>();
        for (GoodCarVO goodcarvo : t) {
            Cart cart = new Cart();
            cart.setNum(StrToNumber.strToint(goodcarvo.getNum()));
            if (null != goodcarvo && null != goodcarvo.getGood().getGoodsId()) {
                cart.setGoodid(goodcarvo.getGood().getGoodsId());
            } else {
                cart.setGoodid(0);
            }
            if (null != goodcarvo && null != goodcarvo.getGood().getGoodsName()) {
                cart.setGoodname(goodcarvo.getGood().getGoodsName());
            } else {
                cart.setGoodname("");
            }
            if (null != goodcarvo && null != goodcarvo.getGood().getMallPrice()) {
                cart.setGoodprice(goodcarvo.getGood().getMallPrice());
            } else {
                cart.setGoodprice(new BigDecimal(0.00));
            }
            if (null != goodcarvo && null != goodcarvo.getGoodImgs() && goodcarvo.getGoodImgs().size() > 0) {
                if (null != goodcarvo.getGoodImgs().get(0).getImgPath())
                    cart.setGoodimgurl(goodcarvo.getGoodImgs().get(0).getImgPath());
            }
            //TODO  是否是电子卡
            if (null != goodcarvo && null != goodcarvo.getIsElect()) {
                if (goodcarvo.getIsElect().equals("0")) {
                    cart.setIselectron(0);
                }
                if (goodcarvo.getIsElect().equals("1")) {
                    cart.setIselectron(1);
                }
            } else {
                cart.setIselectron(0);
            }
            cartlist.add(cart);
        }
        return cartlist;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 02 && resultCode == 02) {
            changeviewbylogin();
        }
    }

    private class ChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("changeallmoney".equals(action)) {
                changeallmoney();
            }
            if ("changeallmoneyand".equals(action)) {
                changeallmoney();
                if (checkBoxall.isChecked()) {
                    isclick = true;
                }
                checkBoxall.setChecked(false);
            }
        }
    }
}
