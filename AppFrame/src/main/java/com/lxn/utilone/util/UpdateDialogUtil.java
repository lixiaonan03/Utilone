package com.lxn.utilone.util;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.lxn.utilone.R;
import com.lxn.utilone.UtilApplication;

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
 * Created by li on 2016/4/29 17:12.
 * 版本更新所需要的工具类
 */
public class UpdateDialogUtil {
    private Context context;

    private int newVerCode;
    private String newVerName;
    private String newApkUrl;
    private String desc;
    private int intcurrent;
    private ProgressBar progressBar_update;
    private TextView version_baifenbi;
    private TextView version_newcode;
    private TextView version_newstr;
    private Button update;
    private DownSoft downSoft;
    private String newApkName;

    public UpdateDialogUtil(Context context) {
        this.context = context;
        intcurrent=DeviceUtil.getVersionCode();
    }
    private onupate sss;

    public void setSss(onupate sss) {
        this.sss = sss;
    }

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
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    // 更新
                    showLoadDialog();
                    break;
                case 2:
                    // 不更新
                    sss.noupdate();
                    break;

                default:
                    break;
            }
        }
    }
    public void  goupate(){
        new Thread(new Runnable() { // 开启线程上传文件

            @Override
            public void run() {
                getServerVerCode();
            }
        }).start();
    }
    private MyHandler handle = new MyHandler(UtilApplication.getInstance());
    /**
     * 获取服务器上apk版本号(其他信息)
     */
    public void getServerVerCode() {

        try {
            String verjson = readContent(CommonVariable.IP
                    + "statics/app/update/update.json");
            if (StringUtils.isNotBlank(verjson)) {
                JSONObject obj = new JSONObject(verjson);
                newVerCode = StrToNumber.strToint(obj.getString("verCode"));
                newVerName = obj.getString("verName");
                newApkName = obj.getString("apkname");
                newApkUrl = obj.getString("apkUrl");
                desc = obj.getString("desc");
                if (newVerCode > intcurrent) {
                    // 需要版本更新
                    handle.sendMessage(handle.obtainMessage(1));
                } else {
                    handle.sendMessage(handle.obtainMessage(2));
                }			} else {
                // 获取版本更新数据失败
                handle.sendMessage(handle.obtainMessage(2));
            }
        } catch (Exception e) {
            e.printStackTrace();
            handle.sendMessage(handle.obtainMessage(2));
        }
    }

    public String readContent(String url) throws IOException {
        StringBuilder sb = new StringBuilder();
        URL getUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) getUrl
                .openConnection();
        // 进行连接，但是实际上get request要在下一句的connection.getInputStream()函数中才会真正发到
        try {
            connection.connect();
        } catch (IOException e) {
            e.printStackTrace();
            handle.sendMessage(handle.obtainMessage(2));
        }
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
        final Dialog dialog = new Dialog(context);
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

        update.setOnClickListener(new View.OnClickListener() {
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
                //解决有的服务器获取的文件流的长度为 -1的  关闭gzip 压缩
                connection.setRequestProperty("Accept-Encoding", "identity");
                connection.connect();
                long length = connection.getContentLength();
                InputStream is = connection.getInputStream();
                FileOutputStream fileOutputStream = null;
                if (is != null) {
                    File file = new File(
                            Environment.getExternalStorageDirectory(),
                            "iqb.apk");
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
                e.printStackTrace();
            } catch (IOException e) {
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
                            .getExternalStorageDirectory(), "iqb.apk")),
                    "application/vnd.android.package-archive");
            context.startActivity(intent);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar_update.setProgress(values[0]);
            version_baifenbi.setText(values[0] + "%");
        }
    }

   public interface  onupate{
        void noupdate();
    }
}
