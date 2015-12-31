package com.lxn.utilone.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lxn.utilone.R;
import com.lxn.utilone.util.CommonVariable;
import com.lxn.utilone.util.LogUtils;
import com.lxn.utilone.util.NetManager;
import com.lxn.utilone.util.PreferencesUtil;
import com.lxn.utilone.util.StringUtils;
import com.lxn.utilone.util.ToastUtils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 引导页 在引导页判断当前版本号看是否需要进行版本更新
 */
public class Welcome extends BaseActivity {
    private int newVerCode;
    private String newVerName;
    private String newApkUrl;
    private String newApkName;
    private String desc;
    private int intcurrent;
    private ProgressBar progressBar_update;
    private TextView version_newstr;
    private TextView version_baifenbi;
    private TextView version_newcode;
    private Button update;
    private Button cancle;
    private DownSoft downSoft;
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
            Welcome activity = (Welcome) reference.get();
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    // 更新
                    activity.showLoadDialog();
                    break;
                case 2:
                    // 不更新
                    int guideInit = PreferencesUtil
                            .getValue(PreferencesUtil.GUIDE_INIT);
                    if (guideInit != 1) {
                        // 跳转到引导页面
                        Intent intent = new Intent(activity,
                                GuideActivity.class);
                        activity.startActivity(intent);
                        activity.finish();
                    } else {
                        // 跳转到主界面的
                        Intent intent = new Intent(activity, MainActivity.class);
                        activity.startActivity(intent);
                        activity.finish();
                    /*
					 * * Timer timer = new Timer(); TimerTask task = new
					 * TimerTask() {
					 *
					 * @Override public void run() {
					 *
					 * } }; timer.schedule(task, 1000 * 1);
					 */

                    }
                    break;

                default:
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);
        // TODO  获取当前版本号
        intcurrent = getVersion();
        //查看服务器的版本  在这里延迟半秒已让欢迎页显示
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getServerVerCode();
            }
        }).start();
    }

    /**
     * 获取服务器上apk版本号(其他信息)
     */
    public void getServerVerCode() {

        try {
            String verjson = readContent(CommonVariable.IP
                    + "/mallService/soft/update.json");
            if (StringUtils.isNotBlank(verjson)) {
                JSONObject obj = new JSONObject(verjson);
                newVerCode = Integer.parseInt(obj.getString("verCode"));
                newVerName = obj.getString("verName");
                newApkName = obj.getString("apkname");
                newApkUrl = obj.getString("apkUrl");
                desc = obj.getString("desc");
                if (newVerCode > intcurrent) {
                    // 需要版本更新
                    mHandler.sendMessage(mHandler.obtainMessage(1));
                } else {
                    mHandler.sendMessage(mHandler.obtainMessage(2));
                }
            } else {
                // 获取版本更新数据失败
                mHandler.sendMessage(mHandler.obtainMessage(2));
            }
        } catch (Exception e) {
            e.printStackTrace();
            mHandler.sendMessage(mHandler.obtainMessage(2));
        }
    }

    /**
     * 读取json字符串的信息
     * @param url json文件地址
     * @return
     * @throws IOException
     */
    public String readContent(String url) throws IOException {
        StringBuilder sb = new StringBuilder();
        URL getUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) getUrl
                .openConnection();
        // 进行连接，但是实际上get request要在下一句的connection.getInputStream()函数中才会真正发到
        connection.setConnectTimeout(3000);
        connection.connect();
        InputStream is = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is,
                "UTF-8"), 8192);
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        reader.close();
        // 断开连接
        connection.disconnect();
        return sb.toString();
    }

    private void showLoadDialog() {
        final Dialog dialog = new Dialog(Welcome.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_update);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        // 进度条
        progressBar_update = (ProgressBar) dialog
                .findViewById(R.id.progressBar_update);
        // 进度条显示
        version_baifenbi = (TextView) dialog
                .findViewById(R.id.version_baifenbi);
        // 当前版本号
        version_newcode = (TextView) dialog.findViewById(R.id.version_newcode);
        version_newcode.setText("最新版本：" + newVerName);
        version_newstr = (TextView) dialog.findViewById(R.id.version_newstr);
        version_newstr.setText("更新内容：" + desc);
        update = (Button) dialog.findViewById(R.id.update);
        cancle = (Button) dialog.findViewById(R.id.cancle);

        update.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                // 更新
                int i = progressBar_update.getProgress();
                if (i == 0) {
                    if (null == downSoft) {
                        downSoft = new DownSoft();
                        downSoft.execute();
                    }
                } else {
                    if (i == 100) {

                    } else {
                        ToastUtils.toastshort("正在下载,请稍后...");
                    }
                }
            }
        });
        cancle.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (downSoft != null) {
                    downSoft.cancel(true);
                }
                dialog.dismiss();
                int guideInit = PreferencesUtil
                        .getValue(PreferencesUtil.GUIDE_INIT);
                if (guideInit != 1) {
                    // 跳转到引导页面
					Intent intent = new Intent(Welcome.this,
							GuideActivity.class);
					startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(Welcome.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        dialog.show();

    }

    public class DownSoft extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            if ("".equals(newApkUrl))
                return null;

            URL getUrl;
            try {
                getUrl = new URL(newApkUrl);
                HttpURLConnection connection = (HttpURLConnection) getUrl
                        .openConnection();
                connection.connect();
                long length = connection.getContentLength();
                InputStream is = connection.getInputStream();
                FileOutputStream fileOutputStream = null;
                if (is != null) {
                    File file = new File(
                            Environment.getExternalStorageDirectory(),
                            "shop.apk");
                    if (file.exists()) {
                        file.deleteOnExit();
                    }
                    fileOutputStream = new FileOutputStream(file);

                    byte[] buf = new byte[1024];
                    int ch = -1;
                    int count = 0;
                    while ((ch = is.read(buf)) != -1) {
                        fileOutputStream.write(buf, 0, ch);
                        count += ch;
                        if (length > 0) {
                            int d = (int) (count * 100 / length);
                            publishProgress(d);
                        }
                    }
                }
                fileOutputStream.flush();
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Void result) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(new File(Environment
                            .getExternalStorageDirectory(), "shop.apk")),
                    "application/vnd.android.package-archive");
            startActivity(intent);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar_update.setProgress(values[0]);
            version_baifenbi.setText(values[0] + "%");
        }
    }

    /**
     * 获取当前应用的版本号
     *
     * @return 当前应用的版本号
     */
    public int getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            int version = info.versionCode;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity被销毁时移除handle的消息处理
        mHandler.removeCallbacksAndMessages(null);
    }
}
