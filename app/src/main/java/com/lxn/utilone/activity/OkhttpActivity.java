package com.lxn.utilone.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lxn.utilone.R;
import com.lxn.utilone.databinding.ActivityOkhttpBinding;
import com.lxn.utilone.network.DnsDemo;
import com.lxn.utilone.network.OkHttpClientsUtils;
import com.lxn.utilone.network.PrintingEventListener;
import com.lxn.utilone.util.Log;
import com.lxn.utilone.util.operationutil.ThreadUtils;

import java.io.IOException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import androidx.annotation.NonNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @author lixiaonan
 * 功能描述: okhttp测试页的
 * 时 间： 2021/11/26 4:33 PM
 */
@Route(path = ActivityConstans.OKHTTP_PATH, name = "okhttp测试页")
public class OkhttpActivity extends BaseActivity {

    private ActivityOkhttpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOkhttpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    /**
     * 初始化view
     */
    private void initView() {
        binding.linTop.topText.setText("okhttp学习");
        binding.linTop.topBack.setOnClickListener(v -> finish());

        binding.tvGetSync.setOnClickListener(v -> {
            ThreadUtils.executeByCached(new ThreadUtils.Task<String>() {
                @Override
                public String doInBackground() throws Throwable {
                    return getRequest();
                }

                @Override
                public void onSuccess(String result) {
                    binding.tvGetSyncResponse.setText(result);
                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onFail(Throwable t) {

                }
            });
        });
        binding.tvGetSyncResponse.setOnClickListener(v -> {
            String url = "https://api.github.com/users/lxn03/repos";
            //这种默认请求的是get
            OkHttpClient client = OkHttpClientsUtils.getClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.i("net", Thread.currentThread().getName() + "失败的====" + call.toString());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    Log.i("lxn123", "当期的线程的===" + Thread.currentThread().getName());
                    Log.i("lxn123", Thread.currentThread().getName() + "==成功的====" + response.toString());
                }
            });

        });
        //FIXME  通过公钥锁定的方式限制抓包等行为
        binding.tvLockKey.setOnClickListener(v -> {
            String url = "https://pic.wanwustore.cn/";
            //这种默认请求的是get
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.eventListener(new PrintingEventListener());
            // 日志拦截器，用来记录所有的网络请求和响应
            // 这个拦截器应该放到所有拦截器的最后，用来监听真正的请求/响应
            // FIXME 在传入一个错误的公钥的时候 访问日志能打印出来 异常信息并携带出来争取的sha256 值 类似这样的
//            Peer certificate chain:   //域名证书、中间证书、根证书的值
//            sha256/K29eS30xtD6XoGqBrCbYYlgnC4vA8iLN6Ozwi0cuycI=: CN=*.wanwustore.cn,O=安鼎睿科技（北京）有限公司,ST=北京市,C=CN
//            sha256/B2o6KjBMvs+b5LG04FOX1d+Dw82KgV4aPdu+Q6Wq764=: CN=WoTrus OV Server CA  [Run by the Issuer],O=WoTrus CA Limited,C=CN
//            sha256/x4QzPSC810K5/cMjb05Qm4k3Bw5zBn4lTdO/nEW/Td4=: CN=USERTrust RSA Certification Authority,O=The USERTRUST Network,L=Jersey City,ST=New Jersey,C=US
//            Pinned certificates for pic.wanwustore.cn:
//            sha256/K19eS30xtD6XoGqBrCbYYlgnC4vA8iLN6Ozwi0cuycI=     //传入错误的
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor(message -> {
                Log.i("lxnNet", message);
            });
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
            builder.certificatePinner(new CertificatePinner.Builder()
                    //注意这个地方只能 加入 sha256 或者 sha1 算法的摘要代码里面有判断    并且如果 network_security_config 这个里面配置对了,这个地方配置错误也会访问错误
                    .add("pic.wanwustore.cn", "sha256/K19eS30xtD6XoGqBrCbYYlgnC4vA8iLN6Ozwi0cuycI=").build());
            builder.dns(new DnsDemo());
            OkHttpClient client = builder.build();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.i("net", Thread.currentThread().getName() + "失败的====" + call.toString());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    Log.i("lxn123", "当期的线程的===" + Thread.currentThread().getName());
                    Log.i("lxn123", Thread.currentThread().getName() + "==成功的====" + response.toString());
                }
            });

        });
    }


    /**
     * get请求的
     *
     * @return
     */
    private static String getRequest() {
        try {
            //            String url="http://172.16.3.228:8081/test1/hello2";
            //            String url="https://mapi.wanwustore.cn/rewss";
            String url = "https://kyfw.12306.cn/otn/resources/login.html";
            //这种默认请求的是get
            OkHttpClient client = OkHttpClientsUtils.getClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Call call = client.newCall(request);
            //
            Response response = call.execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
