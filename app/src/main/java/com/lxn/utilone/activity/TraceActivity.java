package com.lxn.utilone.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lxn.utilone.R;


/**
 * webview的嵌套页面
 * @author lxn
 */
public class TraceActivity extends BaseActivity {

	private WebView webview;
	private ProgressBar progressbar;
	private ImageView top_back;
	String url;
	private TextView top_text;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_otherwebview);
		url = getIntent().getStringExtra("url").trim();
		top_text = (TextView) findViewById(R.id.top_text);
		top_text.setText("嵌套页面");
		top_back = (ImageView) findViewById(R.id.top_back);
		top_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (webview.canGoBack()) {
					webview.goBack();
				} else {
					finish();
				}
			}
		});
		webview = (WebView) findViewById(R.id.webview);
		progressbar = (ProgressBar) findViewById(R.id.progressbar);
		initwebview();
		
	}
	@SuppressLint("SetJavaScriptEnabled")
	private void initwebview() {
		WebSettings webSettings = webview.getSettings();
		webSettings.setJavaScriptEnabled(true);
		/*
		WebSettings settings = webview.getSettings(); */ 
		String useragent = webSettings.getUserAgentString()+"micromessenger";  
		webSettings.setUserAgentString(useragent);
		webview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				webview.loadUrl(url);
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
				super.onProgressChanged(view, newProgress);
			}
		});
		webview.loadUrl(url);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { // 按下的如果是BACK，同时没有重复
			if (webview.canGoBack()) {
				webview.goBack();
			} else {
				finish();
			}
			// true 不传递 false 传递
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
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
