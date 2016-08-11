package com.lxn.utilone.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import com.lxn.utilone.view.MySwipeRefreshLayout;
import com.zbar.lib.CaptureActivity;
import com.zxing.activity.MipcaActivityCapture;

import java.math.BigDecimal;

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


        swipeLayout = (MySwipeRefreshLayout) getView().findViewById(
                R.id.swipe_container);

        swipeLayout
                .setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

                    @Override
                    public void onRefresh() {
                        // 重新刷新页面
                        if (webview.getProgress() == 100) {
                            webview.loadUrl(url);
                        }

                    }
                });
        swipeLayout.setColorSchemeResources(R.color.holo_blue_bright,
                R.color.holo_green_light, R.color.holo_orange_light,
                R.color.holo_red_light);

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
        webSettings.setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new JavaScriptInterface(), "shop");
        WebSettings settings = webview.getSettings();
        String useragent = settings.getUserAgentString() + "micromessenger";
        settings.setUserAgentString(useragent);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
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
                    if (!swipeLayout.isRefreshing())
                        swipeLayout.setRefreshing(true);
                }
                super.onProgressChanged(view, newProgress);
            }
        });

        webview.loadUrl(url);
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
