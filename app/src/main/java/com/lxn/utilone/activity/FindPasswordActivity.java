package com.lxn.utilone.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.volley.listener.HttpBackBaseListener;
import android.volley.util.VolleyUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.lxn.utilone.R;
import com.lxn.utilone.util.CommonVariable;
import com.lxn.utilone.util.RegularExpression;
import com.lxn.utilone.util.StringUtils;
import com.lxn.utilone.util.ToastUtils;
import com.lxn.utilone.view.CustomProgressDialog;

/**
 * 短信验证码获取倒计时和自动获取短信验证码的实例
 *
 */
public class FindPasswordActivity extends BaseActivity {
	
	private ImageView top_back;
	private TextView top_text;
	private EditText edittextphone;
	private EditText edittextcode;
	private Button getcodebutton;
	private Button submit;
	private EditText edittextpassword;
	private SmsObserver smsObserver;
	private int yzm = 1;//1 验证码超时或无效  2 有效 
	private CustomProgressDialog customProgressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userfindpassword);
		top_back = (ImageView) findViewById(R.id.top_back);
		top_text = (TextView) findViewById(R.id.top_text);
		top_text.setText("找回密码");
		top_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});


		smsObserver = new SmsObserver(this, smsHandler);
		getContentResolver().registerContentObserver(SMS_INBOX, true,
				smsObserver);

		customProgressDialog=new CustomProgressDialog(FindPasswordActivity.this, "正在重置......");
		edittextphone = (EditText) findViewById(R.id.edittextphone);
		edittextcode = (EditText) findViewById(R.id.edittextcode);
		edittextpassword = (EditText) findViewById(R.id.edittextpassword);

		getcodebutton = (Button) findViewById(R.id.getcodebutton);
		getcodebutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				String phone = edittextphone.getText().toString().trim();
				//TODO 正则表达式工具类的使用
				String check = RegularExpression.checkRegularExpression(phone,
						RegularExpression.MOBILE_PHONE, "请输入正确的手机号码！");

				if (StringUtils.isNotBlank(check)) {
					ToastUtils.toastshort(check);
					return;
				}
				getcodebutton.setBackgroundResource(R.drawable.getcode_onbutton);
				getcodebutton.setClickable(false);
				VolleyUtil.sendStringRequestByGetToString(CommonVariable.GetCodeURL + phone + "/" + 02, null, null, new HttpBackBaseListener(){


					@Override
					public void onSuccess(String string) {
						edittextphone.setEnabled(false);
						yzm = 2;
						new MyCount(120000, 1000).start();
					}

					@Override
					public void onFail(String failstring) {
						ToastUtils.toastshort("获取验证码失败！");
						getcodebutton.setBackgroundResource(R.drawable.login_button);
						getcodebutton.setClickable(true);
					}

					@Override
					public void onError(VolleyError error) {
						ToastUtils.toastshort("获取验证码失败！");
						getcodebutton.setBackgroundResource(R.drawable.login_button);
						getcodebutton.setClickable(true);
					}
				}, false, null);
			}
		});
		submit = (Button) findViewById(R.id.submit);
		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				//提交按钮
				String phone = edittextphone.getText().toString().trim();
				String code = edittextcode.getText().toString().trim();
				String password = edittextpassword.getText().toString().trim();
				if(StringUtils.isBlank(phone)||StringUtils.isBlank(code)||StringUtils.isBlank(password)){
					ToastUtils.toastshort("请输入相关信息！");
					return;
				}
				if(yzm ==1){
					ToastUtils.toastshort("验证码已失效！");
					return;
				}
				String check = RegularExpression.checkRegularExpression(password,
						RegularExpression.userpass, "请输入6-12位字母或数字！");
				if (StringUtils.isNotBlank(check)) {
					ToastUtils.toastshort(check);
					return;
				}
				customProgressDialog.show();
				VolleyUtil.sendStringRequestByGetToString(CommonVariable.FindMemberPasswordURL+phone+"/"+code+"/"+password, null, null, new HttpBackBaseListener() {
					
					@Override
					public void onSuccess(String string) {
						// TODO 接口成功的回调
						customProgressDialog.dismiss();
						edittextphone.setText("");
						edittextcode.setText("");
						edittextpassword.setText("");
						getcodebutton.setBackgroundResource(R.drawable.login_button);
			        	getcodebutton.setText("获取验证码");
			        	yzm = 1;
						ToastUtils.toastshort("重置成功！");
			        	finish();
					}
					
					@Override
					public void onFail(String failstring) {
						customProgressDialog.dismiss();
						ToastUtils.toastshort("重置失败！" + failstring);
					}
					
					@Override
					public void onError(VolleyError error) {
						customProgressDialog.dismiss();
						ToastUtils.toastshort("重置失败！");
					}
				}, false, null);
			}
		});
	}
	class MyCount extends CountDownTimer {  
		  
        public MyCount(long millisInFuture, long countDownInterval) {  
            super(millisInFuture, countDownInterval);  
        }  
  
        @Override  
        public void onFinish() {  
        	//倒计时完要做的事情
        	getcodebutton.setClickable(true);
        	edittextphone.setEnabled(true);
        	
        	
        	getcodebutton.setBackgroundResource(R.drawable.login_button);
        	getcodebutton.setText("获取验证码");
        	yzm = 1;
        }  
  
        @Override  
        public void onTick(long millisUntilFinished) {  
        	getcodebutton.setText(millisUntilFinished / 1000+"秒");  
        }  
  
    }  
	
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		getContentResolver().unregisterContentObserver(smsObserver);
	}

	class SmsObserver extends ContentObserver {

		public SmsObserver(Context context, Handler handler) {
			super(handler);
		}

		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			// 每当有新短信到来时，使用我们获取短消息的方法
			getSmsFromPhone();
		}
	}
	/**
	 * 读取手机短信验证码
	 */
	private Uri SMS_INBOX = Uri.parse("content://sms/");

	public void getSmsFromPhone() {
		ContentResolver cr = getContentResolver();
		String[] projection = new String[] { "body" };// "_id", "address",
		// "person",, "date",
		// "type
		String where = " address = '1069009576575' AND date >  "
				+ (System.currentTimeMillis() - 10 * 60 * 1000);
		Cursor cur = cr.query(SMS_INBOX, projection, where, null, "date desc");
		if (null == cur)
			return;
		if (cur.moveToNext()) {
			String body = cur.getString(cur.getColumnIndex("body"));
			int i = body.indexOf("：", 0);
			String code = body.substring(i + 1, i + 1 + 6);
			edittextcode.setText(code);
            //设定光标位置
			edittextcode.setSelection(code.length());
			//获取焦点
			edittextcode.requestFocus();
		}
		//关闭游标
		if(cur!=null){
			cur.close();
		}
	}

	public Handler smsHandler = new Handler() {
		// 这里可以进行回调的操作

	};

}
