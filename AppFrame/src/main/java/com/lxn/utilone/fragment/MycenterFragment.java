package com.lxn.utilone.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lxn.utilone.R;
import com.lxn.utilone.UtilApplication;
import com.lxn.utilone.activity.CircularProgressBarDemoActivity;
import com.lxn.utilone.activity.LoginActivity;
import com.lxn.utilone.activity.PhotoPickActivity;
import com.lxn.utilone.util.BitmapUtil;
import com.lxn.utilone.util.CommonVariable;
import com.lxn.utilone.util.LogUtils;
import com.lxn.utilone.util.StringUtils;
import com.lxn.utilone.util.ToastUtils;
import com.lxn.utilone.util.UploadUtil;
import com.lxn.utilone.view.RoundImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * 我的新侬模块
 *
 * @author lxn
 */
public class MycenterFragment extends BaseFragment {
    private RelativeLayout myorder;// 我的订单

    private RelativeLayout user_information;
    private RoundImageView user_img;
    private String username;
    private String imgurl;
    private TextView user_name;
    private Button logout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mycenter, container, false);
    }

    @SuppressLint("NewApi")
    @Override
    public void onFirstVisible(Bundle savedInstanceState) {
        super.onFirstVisible(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            username = bundle.getString("name", "");
            imgurl = bundle.getString("imgurl", "");
        }
        initView();
    }

    @Override
    public void onFragmentResume() {
        super.onFragmentResume();
        changeuserinfo();
    }

    @Override
    public void onFragmentPause() {
        super.onFragmentPause();
    }

    private void initView() {
        user_information = (RelativeLayout) getView().findViewById(
                R.id.user_information);
        user_img = (RoundImageView) getView().findViewById(R.id.user_image);
        user_name = (TextView) getView().findViewById(R.id.user_name);
        logout = (Button) getView().findViewById(R.id.logout);
        user_information.setOnClickListener(viewclick);

        myorder = (RelativeLayout) getView().findViewById(R.id.myorder);


        myorder.setOnClickListener(viewclick);
        logout.setOnClickListener(viewclick);

    }

    OnClickListener viewclick = new OnClickListener() {

        @Override
        public void onClick(View arg0) {
            int id = arg0.getId();
            Intent intent = new Intent();

            switch (id) {
                case R.id.user_information:
                    // 个人资料
                    if (UtilApplication.isLogin) {
                        onClick_Img();
                    } else {
                        intent.setClass(getActivity(), LoginActivity.class);
                        intent.putExtra("flag", 04);
                        getActivity().startActivityForResult(intent, 04);
                    }
                    break;
                case R.id.myorder:
                    // 我的订单
                    intent.setClass(getActivity(), CircularProgressBarDemoActivity.class);
                    startActivity(intent);

                    break;
                case R.id.logout:
                    //退出
                    if (UtilApplication.isLogin) {
                        UtilApplication.isLogin = false;
                        logout.setVisibility(View.GONE);
                        user_img.setImageResource(R.drawable.user_img);
                        user_name.setText("请登录");
                        UtilApplication.userid=0;
                        UtilApplication.usernamelogin="";
                        UtilApplication.userimgurlrlogin="";
                        intent.setClass(getActivity(), LoginActivity.class);
                        intent.putExtra("flag", 04);
                        getActivity().startActivityForResult(intent, 04);
                    } else {
                        intent.setClass(getActivity(), LoginActivity.class);
                        intent.putExtra("flag", 04);
                        getActivity().startActivityForResult(intent, 04);
                    }

                    break;

                default:
                    break;
            }
        }

    };

    /**
     * 更新用户信息的展示
     */
    private void changeuserinfo() {
        if (UtilApplication.isLogin) {
            logout.setVisibility(View.VISIBLE);
            // 设置登录完成的信息
                // 使用DisplayImageOptions.Builder()创建DisplayImageOptions
                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .showImageOnLoading(R.drawable.user_img)         // 设置图片下载期间显示的图片
                        .showImageForEmptyUri(R.drawable.user_img)  // 设置图片Uri为空或是错误的时候显示的图片
                        .showImageOnFail(R.drawable.user_img)       // 设置图片加载或解码过程中发生错误显示的图片
                        .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                        .build();                                   // 创建配置过得DisplayImageOption对象
                ImageLoader.getInstance().displayImage(UtilApplication.userimgurlrlogin, user_img,options);
                if(StringUtils.isNotBlank(UtilApplication.usernamelogin)){
                    user_name.setText(UtilApplication.usernamelogin);
                }else{
                    user_name.setText("请设置昵称");
                }

        }else{
            logout.setVisibility(View.GONE);
        }
    }


    Bitmap bitmap = null;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PhotoPickActivity.PHOTO_REQ_ALBUM
                || requestCode == PhotoPickActivity.PHOTO_REQ_CAPTURE) {

            if (resultCode == PhotoPickActivity.PHOTO_BACK_URI) {
                Uri uri = (Uri) data.getExtras().get("data");
                bitmap = BitmapUtil.getRotateBitmap(getActivity(),
                        uri);
            }
            if (resultCode == PhotoPickActivity.PHOTO_BACK_BMP) {
                bitmap = (Bitmap) data.getExtras().get("data");
            }
            if(null!=bitmap){

                UploadUtil uploadUtil = UploadUtil.getInstance();
                uploadUtil
                        .setOnUploadProcessListener(new UploadUtil.OnUploadProcessListener() {

                            @Override
                            public void onUploadProcess(int uploadSize) {
                            }

                            @Override
                            public void onUploadDone(int responseCode,
                                                     String message) {
                                Message msg = Message.obtain();
                                msg.what = 10;
                                msg.arg1 = responseCode;
                                msg.obj = message;
                                handler.sendMessage(msg);
                            }

                            @Override
                            public void initUpload(int fileSize) {
                            }
                        }); // 设置监听器监听上传状态
                Map<String, String> params = new HashMap<String, String>();
                int userid = 0;
                if (UtilApplication.isLogin) {
                        userid = UtilApplication.userid;
                }
                params.put("membId", userid+"");
                uploadUtil.uploadFile(bitmap, "file",
                        CommonVariable.UpdateMemberinfoImgURL, params);

            }
        }
        changeuserinfo();
    }
    private MyHandler handler = new MyHandler(getActivity());

    /**
     * handle 消息处理对象 使用弱引用持有外部activity的引用 防止内存泄露的情况产生
     */
    private class MyHandler extends Handler {
        private WeakReference<Context> reference;

        public MyHandler(Context context) {
            reference = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10:

                    if (1 == msg.arg1) {
                        user_img.setImageBitmap(bitmap);
                        JSONObject object = JSON.parseObject((String) msg.obj);
                        String code = (String) object.get("retCode");
                        if ("200".equals(code)) {
                            if (object.containsKey("responseBody")) {
                                JSONObject response = (JSONObject) object
                                        .get("responseBody");
                                if (response.containsKey("membImg")) {
                                    String userimgurl = (String) response
                                            .get("membImg");
                                    if (null != userimgurl) {
                                        UtilApplication.userimgurlrlogin = userimgurl;
                                    }
                                }
                            }
                        }else {
                            ToastUtils.toastshort("头像上传失败！");
                        }
                    } else {
                        ToastUtils.toastshort("头像上传失败！");
                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }

    };
    /**
     * 弹出用户选择头像提示框
     */
    void onClick_Img() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.view_dialog_capture);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);

        LinearLayout menu_capture_r1 = (LinearLayout) dialog
                .findViewById(R.id.menu_capture_r1);
        LinearLayout menu_capture_r2 = (LinearLayout) dialog
                .findViewById(R.id.menu_capture_r2);
        Button dialog_contorl_b1 = (Button) dialog
                .findViewById(R.id.dialog_contorl_b1);

        menu_capture_r1.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
                onClick_Capture();
            }
        });
        menu_capture_r2.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
                onClick_Pick();
            }
        });
        dialog_contorl_b1.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * 点击选择本地文件
     */
    void onClick_Pick() {
        PhotoPickActivity.PhotoCrop crop = new PhotoPickActivity.PhotoCrop(1, 1, 150, 150);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(PhotoPickActivity.PHOTO_CROPKEY, crop);

        Intent intent = new Intent(getActivity(),
                PhotoPickActivity.class);
        intent.putExtra(PhotoPickActivity.PHOTO_TYPEKEY,
                PhotoPickActivity.PHOTO_REQ_ALBUM);
        intent.putExtra(PhotoPickActivity.PHOTO_STORAGEKEY, Environment
                .getExternalStorageDirectory().toString());
        intent.putExtras(mBundle);
        startActivityForResult(intent, PhotoPickActivity.PHOTO_REQ_ALBUM);
    }

    /**
     * 点击拍照
     */
    void onClick_Capture() {
        PhotoPickActivity.PhotoCrop crop = new PhotoPickActivity.PhotoCrop(1, 1, 150, 150);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(PhotoPickActivity.PHOTO_CROPKEY, crop);

        Intent intent = new Intent(getActivity(),
                PhotoPickActivity.class);
        intent.putExtra(PhotoPickActivity.PHOTO_TYPEKEY,
                PhotoPickActivity.PHOTO_REQ_CAPTURE);
        intent.putExtra(PhotoPickActivity.PHOTO_STORAGEKEY, Environment
                .getExternalStorageDirectory().toString());
        intent.putExtras(mBundle);
        startActivityForResult(intent, PhotoPickActivity.PHOTO_REQ_CAPTURE);
    }
}
