package com.lxn.utilone.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lxn.utilone.R;
import com.lxn.utilone.databinding.ActivityOkhttpBinding;
import com.lxn.utilone.network.OkHttpClientsUtils;
import com.lxn.utilone.util.Log;
import com.lxn.utilone.util.operationutil.ThreadUtils;

import java.io.IOException;

import androidx.annotation.NonNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
  *  @author lixiaonan
  *  功能描述: okhttp测试页的
  *  时 间： 2021/11/26 4:33 PM
  */
@Route(path = ActivityConstans.OKHTTP_PATH , name = "okhttp测试页")
public class OkhttpActivity extends BaseActivity{

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
    private void initView(){
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
        binding.tvGetSyncResponse.setOnClickListener(v ->{
                String url="https://api.github.com/users/lxn03/repos";
                //这种默认请求的是get
                OkHttpClient client = OkHttpClientsUtils.getClient();
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.i("net",Thread.currentThread().getName()+"失败的===="+call.toString());
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        Log.i("lxn123","当期的线程的==="+Thread.currentThread().getName());
                        Log.i("lxn123",Thread.currentThread().getName()+"==成功的===="+response.toString());
                    }
                });

        });
    }


    /**
     * get请求的
     * @return
     */
    private static String getRequest() {
        try {
//            String url="http://172.16.3.228:8081/test1/hello2";
            String url="https://mapi.wanwustore.cn/rewss";
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
