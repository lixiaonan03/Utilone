package com.lxn.utilone.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.lxn.utilone.R;
import com.lxn.utilone.util.CommonVariable;
import com.lxn.utilone.util.NetManager;
import com.lxn.utilone.view.MySwipeRefreshLayout;
import com.zxing.activity.MipcaActivityCapture;

import java.math.BigDecimal;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


/**
 * 首页模块
 *
 * @author lxn
 */
public class HomeFragment extends BaseFragment {

    private WebView webview;
    private ProgressBar progressbar;
    private String url;
    private RelativeLayout gosearch;
    private MySwipeRefreshLayout swipeLayout;
    private ImageView erweima;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    @Override
    public void onFirstVisible(Bundle savedInstanceState) {
        super.onFirstVisible(savedInstanceState);
        url = CommonVariable.HomeURL;

        gosearch = (RelativeLayout) getView().findViewById(R.id.search);
        //扫描二维码
        erweima = (ImageView) getView().findViewById(R.id.erweima);
        erweima.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO 跳转到二维码扫描界面
                Intent intent = new Intent();
                //zbar 二维码扫描的
                //intent.setClass(getActivity(), CaptureActivity.class);
                //zxing二维码扫描的
                intent.setClass(getActivity(), MipcaActivityCapture.class);
                startActivity(intent);
            }
        });


        swipeLayout = (MySwipeRefreshLayout) getView().findViewById(R.id.swipe_container);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                // 重新刷新页面
                if (webview.getProgress() == 100) {
                    webview.loadUrl(url);
                }

            }
        });
        swipeLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);

        webview = (WebView) getView().findViewById(R.id.webview);
        webview.setHorizontalScrollBarEnabled(false);// 水平不显示
        webview.setVerticalScrollBarEnabled(false); // 垂直不显示


        progressbar = (ProgressBar) getView().findViewById(R.id.progressbar);
        initwebview();
    }

    @Override
    public void onFragmentPause() {
        super.onFragmentPause();
    }

    @Override
    public void onFragmentResume() {
        super.onFragmentResume();
    }

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    private void initwebview() {
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);//开启javascript
        webview.addJavascriptInterface(new JavaScriptInterface(), "shop");
        WebSettings settings = webview.getSettings();
        //加入UA标示 让其可以加载微信专享的页面
        String useragent = settings.getUserAgentString() + "micromessenger";
        settings.setUserAgentString(useragent);

        //同时有https和http 算mixed content   webview 5.0默认不支持mixed content
        if (Build.VERSION.SDK_INT >= 21) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        webSettings.setDomStorageEnabled(true);  //开启DOM
        webSettings.setDefaultTextEncodingName("utf-8"); //设置编码
        // // web页面处理
        webSettings.setAllowFileAccess(true);// 支持文件流
        // webSettings.setSupportZoom(true);// 支持缩放
        // webSettings.setBuiltInZoomControls(true);// 支持缩放
        webSettings.setUseWideViewPort(true);// 调整到适合webview大小
        webSettings.setLoadWithOverviewMode(true);// 调整到适合webview大小
        webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);// 屏幕自适应网页,如果没有这个，在低分辨率的手机上显示可能会异常
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        //提高网页加载速度，暂时阻塞图片加载，然后网页加载好了，在进行加载图片
        webSettings.setBlockNetworkImage(true);
        //开启缓存机制
        webSettings.setAppCacheEnabled(true);
        //根据当前网页连接状态 如果是wifi设置无缓存
        if (NetManager.getInstance().getCurrentNetType().equals("wifi")) {
            //设置无缓存
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            //设置缓存
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description,
                                        String failingUrl) {
                progressbar.setVisibility(View.GONE);
                webview.setVisibility(View.GONE);
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressbar.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressbar.setVisibility(View.GONE);
            }
        });
        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressbar.setProgress(newProgress);
                if (newProgress == 100) {
                    // 隐藏进度条
                    swipeLayout.setRefreshing(false);
                } else {
                    if (!swipeLayout.isRefreshing()) swipeLayout.setRefreshing(true);
                }
                super.onProgressChanged(view, newProgress);
            }

            // 一个回调接口使用的主机应用程序通知当前页面的自定义视图已被撤职
            CustomViewCallback customViewCallback;

            // 进入全屏的时候的回调方法
            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                // 赋值给callback
                customViewCallback = callback;
                // 设置webView隐藏
                webview.setVisibility(View.GONE);
              /*  // 声明video，把之后的视频放到这里面去
                FrameLayout video = (FrameLayout) findViewById(R.id.video);
                // 将video放到当前视图中
                video.addView(view);
                // 横屏显示
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                // 设置全屏
                setFullScreen();*/
            }

            // 退出全屏的时候的回调方法
            @Override
            public void onHideCustomView() {
                if (customViewCallback != null) {
                    // 隐藏掉
                    customViewCallback.onCustomViewHidden();
                }
           /*     // 用户当前的首选方向
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
                // 退出全屏
                quitFullScreen();*/
                // 设置WebView可见
                webview.setVisibility(View.VISIBLE);
            }


            /**
             * 网页加载标题回调
             * @param view
             * @param title 网页标题
             */
            @Override
            public void onReceivedTitle(WebView view, String title) {
            }
        });

        webview.loadUrl(url);
    }


    /**
     * 设置全屏
     */
    private void setFullScreen() {
        // 设置全屏的相关属性，获取当前的屏幕状态，然后设置全屏
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 全屏下的状态码：1098974464
        // 窗口下的状态吗：1098973440
    }

    /**
     * 退出全屏
     */
    private void quitFullScreen() {
        // 声明当前屏幕状态的参数并获取
        final WindowManager.LayoutParams attrs = getActivity().getWindow().getAttributes();
        attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getActivity().getWindow().setAttributes(attrs);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    /**
     * 与webview 交互使用的
     *
     * @author lxn
     */
    final class JavaScriptInterface {
        int goodid = 0;
        String name;
        BigDecimal goodprice;
        Integer iselectron;

        JavaScriptInterface() {
        }

        /**
         * This is not called on the UI thread. Post a runnable to invoke
         * loadUrl on the UI thread.
         */
        /*@JavascriptInterface
		public void clickOnAndroid() {
			mHandler.post(new Runnable() {
				public void run() {
					webview.loadUrl("javascript:wave()");
				}
			});
		}*/
        @JavascriptInterface
        public void gooddetail(String goodid) {

        }

        @JavascriptInterface
        public void getShortcutNav(String gowhere) {

        }

        @JavascriptInterface
        public void goodlist(String id) {

        }

        @JavascriptInterface
        public void gobuy(String id) {

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 01 && resultCode == 01) {

        }
    }

}
