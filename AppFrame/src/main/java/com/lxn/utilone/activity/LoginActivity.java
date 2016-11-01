package com.lxn.utilone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.volley.listener.HttpBackBeanListener;
import android.volley.util.VolleyUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.lxn.utilone.R;
import com.lxn.utilone.UtilApplication;
import com.lxn.utilone.model.EnnMember;
import com.lxn.utilone.util.CommonVariable;
import com.lxn.utilone.util.StringUtils;
import com.lxn.utilone.util.ToastUtils;
import com.lxn.utilone.util.cache.ACache;
import com.lxn.utilone.view.CustomProgressDialog;

/**
 * 登录界面
 */
public class LoginActivity extends BaseActivity {

	private EditText editTextLoginCode;
	private EditText editTextPassword;
	private TextView loginSubmit;
	private TextView goregister;
	private TextView forgetpassword;
	private ImageView top_back;
	private CustomProgressDialog customProgressDialog;
	private ImageView login_wx;
	private TextView top_text;
	private Button save,get;

	private ACache mAcache;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

        //初始化缓存管理器
		mAcache= ACache.get(this);

		customProgressDialog = new CustomProgressDialog(LoginActivity.this,
				"正在登录......");
		initView();
	}

	private void initView() {
		top_back = (ImageView) findViewById(R.id.top_back);
		top_text = (TextView) findViewById(R.id.top_text);
		top_text.setText("登录");

		editTextLoginCode = (EditText) findViewById(R.id.editTextLoginCode);
		editTextPassword = (EditText) findViewById(R.id.editTextPassword);
		loginSubmit = (TextView) findViewById(R.id.login_submit);
		goregister = (TextView) findViewById(R.id.goregister);
		forgetpassword = (TextView) findViewById(R.id.forgetpassword);

		login_wx = (ImageView) findViewById(R.id.login_wx);
		// 设置监听
		top_back.setOnClickListener(new ViewOnClickListener());
		loginSubmit.setOnClickListener(new ViewOnClickListener());
		goregister.setOnClickListener(new ViewOnClickListener());
		forgetpassword.setOnClickListener(new ViewOnClickListener());
		login_wx.setOnClickListener(new ViewOnClickListener());


		//保存和获取缓存的按钮
		save=(Button)findViewById(R.id.save);
		get=(Button)findViewById(R.id.get);
		save.setOnClickListener(new ViewOnClickListener());
		get.setOnClickListener(new ViewOnClickListener());
	}

	private class ViewOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			int viewid = v.getId();
			switch (viewid) {
			case R.id.top_back:
				// 回退
				finish();
				break;
			case R.id.login_submit:
				// 登录
				goLogin();

				break;
			case R.id.goregister:

				break;
			case R.id.forgetpassword:
				// 忘记密码
				Intent intentfind = new Intent(LoginActivity.this,
						FindPasswordActivity.class);
				startActivity(intentfind);
				break;
			case R.id.login_wx:

				break;
				case R.id.save:
                //保存缓存
					 String username = editTextLoginCode.getText().toString().trim();
					if(StringUtils.isBlank(username)){
						ToastUtils.toastshort("请输入用户名");
					}
					mAcache.put("username",username);
				break;
				case R.id.get:
                //获取缓存
					editTextPassword.setText(mAcache.getAsString("username"));
				break;

			default:
				break;
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

	}

	/**
	 * 去登录的方法
	 */
	private void goLogin() {
		final String username = editTextLoginCode.getText().toString().trim();
		final String password = editTextPassword.getText().toString().trim();
		// 判断是否输入内容
		if (null == username || "" == username || "".equals(password)
				|| null == password) {
			ToastUtils.toastshort("请输入用户名和密码！");
			return;
		}
			customProgressDialog.show();
		/*	String encryptStr = MD5Util.encryptStr("name=" + username
					+ "&crypted_password=" + password);
			// String url=CommonVariable.LoginURL+encryptStr;*/
		/*	String url = CommonVariable.LoginURL + username + "/" + password;
			VolleyUtil.sendStringRequestByGetToBean(url, null, null,
					EnnMember.class, new HttpBackBeanListener<EnnMember>() {

						@Override
						public void onSuccess(EnnMember user) {
							customProgressDialog.dismiss();
							UtilApplication.isLogin=true;
							UtilApplication.userid=user.getMembId();
							UtilApplication.usernamelogin=user.getMembName();
							UtilApplication.userimgurlrlogin=user.getMembImg();
							logingofor();
						}

						@Override
						public void onFail(String failstring) {
							// TODO Auto-generated method stub
							customProgressDialog.dismiss();
							ToastUtils.toastlong(failstring);
						}

						@Override
						public void onError(VolleyError error) {
							// TODO Auto-generated method stub
							customProgressDialog.dismiss();
							ToastUtils.toastshort("登录失败！");
						}

					}, false, null);*/

	}
	/**
	 * 根据传入的标记 决定登录完成之后 需要跳转的界面
	 */
	void logingofor() {
		int flag = getIntent().getIntExtra("flag", 0);
		switch (flag) {
		case 01:
			// 要回到主界面的
			setResult(01);
			finish();
			break;
		case 02:
			// 要回到购物车的
			setResult(02);
			finish();
			break;
		case 03:
			// 要回到会员卡的
			setResult(03);
			finish();
			break;
		case 04:
			// 要回到个人中心
			setResult(04);
			finish();
			break;


		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (null!=customProgressDialog&&customProgressDialog.isShowing()) {
			customProgressDialog.dismiss();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (null!=customProgressDialog&&customProgressDialog.isShowing()) {
			customProgressDialog.dismiss();
		}
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
}
