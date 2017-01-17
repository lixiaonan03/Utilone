package com.lxn.utilone.finger;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.lxn.utilone.R;
import com.lxn.utilone.activity.LoginActivity;


/**
  *  @copyright:北京爱钱帮财富科技有限公司
  *  功能描述: android 6.0之后的指纹识别的API 使用的demo
  *   作 者:  李晓楠
  *   时 间： 2016/8/19 10:43
 */
@TargetApi(Build.VERSION_CODES.M)
public class FingerDemoActivity extends Activity {
    //FingerprintManager manager;
    KeyguardManager mKeyManager;
    private final static int REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS = 0;
    private final static String TAG = "finger_log";
    private TextView textView;
    private FingerprintManagerCompat manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerdemo);

        textView=(TextView)findViewById(R.id.textView);

        // android 6.0 23 获取指纹管理的类的获取方式
        // manager = (FingerprintManager) this.getSystemService(Context.FINGERPRINT_SERVICE);
        //这个是v4兼容包的 获取方式 名字改成了 FingerprintManagerCompat
        manager = FingerprintManagerCompat.from(this);
        //这个是锁屏的管理Manager
        mKeyManager = (KeyguardManager) this.getSystemService(Context.KEYGUARD_SERVICE);



    }

    public boolean isFinger() {
        //判断硬件是否支持指纹识别
        if (!manager.isHardwareDetected()) {
            Toast.makeText(this, "没有指纹识别模块", Toast.LENGTH_SHORT).show();
            return false;
        }
        Log(TAG, "有指纹模块");

        //检查是否有指纹识别权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "没有指纹识别权限", Toast.LENGTH_SHORT).show();
            return false;
        }

        Log(TAG, "有指纹权限");

        //判断 是否开启锁屏密码  没有开启锁屏密码不能用指纹识别
        if (!mKeyManager.isKeyguardSecure()) {
            Toast.makeText(this, "没有开启锁屏密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        Log(TAG, "已开启锁屏密码");

        //判断是否有指纹录入
        if (!manager.hasEnrolledFingerprints()) {
            Toast.makeText(this, "没有录入指纹", Toast.LENGTH_SHORT).show();
            return false;
        }
        Log(TAG, "已录入指纹");
        return true;
    }

    CancellationSignal mCancellationSignal = new CancellationSignal();
    //回调方法
   // FingerprintManager.AuthenticationCallback mSelfCancelled = new FingerprintManager.AuthenticationCallback() {
    FingerprintManagerCompat.AuthenticationCallback mSelfCancelled = new FingerprintManagerCompat.AuthenticationCallback() {
        @Override
        public void onAuthenticationError(int errorCode, CharSequence errString) {
            //指纹识别失败的回调  可根据errorCode 做判断处理不同的业务逻辑
            // errorCode值 5 被需要 1 硬件不可用 7 API被锁定由于太多尝试(Failed 6次之后就会调用)  3 超时一般为30s的量级
            Toast.makeText(FingerDemoActivity.this, errString, Toast.LENGTH_SHORT).show();
            textView.setText(textView.getText() + "===" + errString + "=errorCode===" + errorCode+"===\n");
            //TODO 指纹出错次数过多后(好像是6次)的显示数字解锁密码
            if(7==errorCode){
                //次数过多之后就取消识别 并调用数字或手势解锁界面
                mCancellationSignal.cancel();
                showAuthenticationScreen();
            }
        }

        @Override
        public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
            //提示等待手指按下 手指按下  的回调
            textView.setText(textView.getText() + "===" + helpString + "onAuthenticationHelp=========\n");
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
            //指纹识别成功的回调
            textView.setText(textView.getText()+"==onAuthenticationSucceeded指纹识别成功=====\n");
            Toast.makeText(FingerDemoActivity.this, "指纹识别成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAuthenticationFailed() {
            //指纹识别失败的回调
            textView.setText(textView.getText()+"==onAuthenticationFailed指纹识别失败====\n");
            Toast.makeText(FingerDemoActivity.this, "指纹识别失败", Toast.LENGTH_SHORT).show();
        }
    };


    /**
     * 开始进行指纹识别监听
     * @param cryptoObject
     */
    public void startListening(FingerprintManagerCompat.CryptoObject cryptoObject) {

       //v4包的和 23API的参数顺序不同
       // manager.authenticate(cryptoObject, mCancellationSignal, 0, mSelfCancelled, null);
        //参数顺序 1、一个加密类的对象  2、一个标识位 暂时写死0 3、CancellationSignal类的一个对象
        // 可以取消当前扫描 4、扫描回调监听 5、是Handler类的对象，传null的话默认使用主线程的looper来处理
        manager.authenticate(cryptoObject,0,mCancellationSignal,mSelfCancelled,null);

    }


    private  void showAuthenticationScreen() {
        //调用器锁屏界面
        Intent intent = mKeyManager.createConfirmDeviceCredentialIntent("finger", "测试指纹识别");
        if (intent != null) {
            startActivityForResult(intent, REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS) {
            // Challenge completed, proceed with using cipher
            if (resultCode == RESULT_OK) {
                //锁屏界面 密码输入成功回调成功
                textView.setText(textView.getText()+"onActivityResult回调成功的=========\n");
                Intent intent =new Intent(FingerDemoActivity.this,LoginActivity.class);
                startActivity(intent);
            } else {
                //锁屏界面 密码输入错误回调
                textView.setText(textView.getText()+"onActivityResult回调失败的=========\n");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //先判断是否支持指纹识别
        if (isFinger()) {
            Log(TAG, "keyi");
            startListening(null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCancellationSignal.cancel();
    }

    @Override
    public boolean isDestroyed() {
        return super.isDestroyed();
    }

    private void Log(String tag, String msg) {
        Log.d(tag, msg);
    }
}
