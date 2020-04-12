package com.lxn.utilone.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxn.utilone.R;


/**
 * 通过帧动画 实现 类似58同城 类似的加载网络的进度条
 */
public class CustomProgressDialog extends ProgressDialog {

	private AnimationDrawable mAnimation;
	private Context mContext;
	private ImageView loadingimage;
	private String mLoadingTip;
	private TextView loadingtext;
	private int mResid;//帧动画资源的id
	 public CustomProgressDialog(Context context, String content) {
	    this(context,content, R.drawable.loadanim);
	 }
    /**
     * @param context  上下文环境
     * @param content  要显示的文字内容
     * @param id       帧动画资源的ID
     */
	public CustomProgressDialog(Context context, String content, int id) {
		super(context);
		this.mContext = context;
		this.mLoadingTip = content;
		this.mResid = id;
	}
   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initView();
		initData();
	}

	private void initData() {

		loadingimage.setBackgroundResource(mResid);
		// 通过ImageView对象拿到背景显示的AnimationDrawable
		mAnimation = (AnimationDrawable) loadingimage.getBackground();
		// 为了防止在onCreate方法中只显示第一帧的解决方案之一
		loadingimage.post(new Runnable() {
			@Override
			public void run() {
				mAnimation.start();
			}
		});
		loadingtext.setText(mLoadingTip);

	}

	public void setContent(String str) {
		loadingtext.setText(str);
	}

	private void initView() {
		setContentView(R.layout.progress_dialog);
		/** 设置透明度 */
		Window window = getWindow();
		window.setBackgroundDrawableResource(R.drawable.dailog_bg);
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.alpha = 1.0f;// 透明度
		lp.dimAmount = 0.1f;// 黑暗度
		/*lp.width=WindowManager.LayoutParams.WRAP_CONTENT;
		lp.height=WindowManager.LayoutParams.MATCH_PARENT;*/
		window.setAttributes(lp);
		//window.setContentView(R.layout.progress_dialog);//背景
		//getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND); 
		loadingtext = (TextView) findViewById(R.id.loadingtext);
		loadingimage = (ImageView) findViewById(R.id.loadingimage);
	}

	/*
	 * @Override public void onWindowFocusChanged(boolean hasFocus) { // TODO
	 * Auto-generated method stub mAnimation.start();
	 * super.onWindowFocusChanged(hasFocus); }
	 */
}
