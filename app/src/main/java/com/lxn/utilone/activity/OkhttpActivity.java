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
import com.lxn.utilone.util.LogUtils;
import com.lxn.utilone.util.operationutil.ThreadUtils;
import com.lxn.utilone.util.sslutil.HttpSslUtil;
import com.lxn.utilone.view.NoDoubleClickListener;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

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
            //可以在这个地方配置证书校验
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


        binding.tvSSL.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                try {
                    startSSl();
                } catch (CertificateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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

    /**
     * 学习ssl 配置
     */
    private void startSSl() throws CertificateException, IOException {
        String url = "https://www.baidu.com/";
        //这种默认请求的是get
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.eventListener(new PrintingEventListener());
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(message -> {
            Log.i("lxnNet", message);
        });
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(logging);
        //TODO 一种写法 使用工具类的  加入证书认证
        InputStream[] inputStreams = new InputStream[]{getResources().openRawResource(R.raw.lixioanancal)};
        HttpSslUtil.SSLParams sslParams = HttpSslUtil.getSslSocketFactory(inputStreams);
        // builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);

        //另外一种写法证书认证的
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        InputStream caInput = new BufferedInputStream(getResources().openRawResource(R.raw.lixioanancal));
        try {
            ca = cf.generateCertificate(caInput);
        } catch (CertificateException e) {
            e.printStackTrace();
        } finally {
            caInput.close();
        }
        builder.sslSocketFactory(createSSLSocketFactory(), mMyTrustManager);


        //hostnameVerifier则是对服务端返回的一些信息进行相关校验的地方， 用于客户端判断所连接的服务端是否可信，通常默认return true,或者简单校验hostname是否正确，
        // 默认不使用的话会调用okhttp的OkHostnameVerifier:
        //FIXME 域名校验认证的 和上面的证书认证2个配置可以单独存在并不一定是组合存在的
        builder.hostnameVerifier(new TrustAllHostnameVerifier());
        OkHttpClient client = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                //证书过期会报 CertificateExpiredException  这个类型的异常
                Log.i("net", Thread.currentThread().getName() + "失败的====" + call.toString());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.i("lxn123", "当期的线程的===" + Thread.currentThread().getName());
                Log.i("lxn123", Thread.currentThread().getName() + "==成功的====" + response.toString());
            }
        });
    }


    private MyTrustManager mMyTrustManager;
    private SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;
        try {
            mMyTrustManager = new MyTrustManager();
            SSLContext context = SSLContext.getInstance("TLSv1", "AndroidOpenSSL");
            context.init(null, new TrustManager[]{mMyTrustManager}, new SecureRandom());
            ssfFactory = context.getSocketFactory();
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        return ssfFactory;
    }


    /**
     * X509TrustManager 实现 对证书进行校验
     */
    private  Certificate ca;
    public class MyTrustManager implements X509TrustManager {

        //该方法检查客户端的证书，若不信任该证书则抛出异常。由于我们不需要对客户端进行认证，
        //因此我们只需要执行默认的信任管理器的这个方法。JSSE中，默认的信任管理器类为TrustManager。
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        //该方法检查服务器的证书，若不信任该证书同样抛出异常。通过自己实现该方法，可以使之信任我们指定的任何证书。
        //在实现该方法时，也可以简单的不做任何处理，即一个空的函数体，由于不会抛出异常，它就会信任任何证书。
        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            //            //在这个地方可以针对服务端的 公钥证书进行验证如果过期可以抛出异常  接口访问域名就会报ssl握手异常
            //            throw new CertificateException();
            //比如百度域名在这个地方就会返回 证书链中包含3个证书的情况
            for (X509Certificate cert : chain) {
                //检查证书当前是否有效。如果当前日期和时间在证书中给出的有效期内。 如果有就会抛出异常
                cert.checkValidity();
                LogUtils.iWithTag("lxnSSL","cert==="+cert.toString()+"===");
                //省略try catch
                try {
                    // 验证此证书是否使用与指定公钥相对应
                    LogUtils.iWithTag("lxnSSL","getPublicKey==="+ca.getPublicKey().toString()+"===");
                    cert.verify(((X509Certificate) ca).getPublicKey());
                } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException | SignatureException e) {
                    e.printStackTrace();
                    //如果上面的验签过程 发生错误不对应会抛出SignatureException 异常,如果把这个异常catch住，就相当于忽略证书认证了，能访问通。
                    //但如果把这个异常抛出了 就会ssl链接失败访问不通
                    throw  new CertificateException(e);
                }

            }
        }

        //返回受信任的X509证书数组。
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    /**
     * 实现HostnameVerifier接口   可以对域名进行处理
     */
    private class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            //如果在这个地方进行了域名的判断认证 ,在返回false的情况下，访问该域名的网站会报如下错误
            //            javax.net.ssl.SSLPeerUnverifiedException: Hostname pic.wanwustore.cn not verified:
            //            certificate: sha256/VVymzKVBWS6oZPwmCEheREn0UO45M8A4E1AIWLfBpd8=
            //                    DN: CN=*.wanwustore.cn,O=安鼎睿科技（北京）有限公司,ST=北京市,C=CN
            //            subjectAltNames: [*.wanwustore.cn, wanwustore.cn]
            if (hostname.equals("pic.wanwustore.cn")) {
                return false;
            }
            return true;
        }
    }
}