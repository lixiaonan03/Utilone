package com.lxn.utilone.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.lxn.utilone.R;
import com.lxn.utilone.adapter.GuideViewPagerAdapter;
import com.lxn.utilone.util.PreferencesUtil;

import androidx.viewpager.widget.ViewPager;


/*
 * 引导页
 */
public class GuideActivity extends  BaseActivity{
	private Context context;
	private ViewPager mPager;// 页卡内容
	private List<View> listViews; // 引导页页面列表
	LayoutInflater mInflater;
	private ImageView button;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = GuideActivity.this;
		setContentView(R.layout.activity_guide);
		initViewPager();
		button=(ImageView) findViewById(R.id.go_login);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				PreferencesUtil.setInt(PreferencesUtil.GUIDE_INIT, 1);
				Intent intent = new Intent(GuideActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
	
	private void initViewPager() {
		mInflater = getLayoutInflater();
		mPager = (ViewPager) findViewById(R.id.vPager);
		listViews = new ArrayList<View>();
		listViews.add(mInflater.inflate(R.layout.guide_one, null));
		listViews.add(mInflater.inflate(R.layout.guide_two, null));
		listViews.add(mInflater.inflate(R.layout.guide_three, null));
		listViews.add(mInflater.inflate(R.layout.guide_four, null));
		listViews.add(mInflater.inflate(R.layout.guide_five, null));
		mPager.setAdapter(new GuideViewPagerAdapter(context, listViews));
		mPager.setCurrentItem(0);
		mPager.setOnPageChangeListener(new PageChangeListener());
	}
	/**
	 * ViewPager页面变化监听
	 */

	public class PageChangeListener implements ViewPager.OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			switch (arg0) {

			case ViewPager.SCROLL_STATE_DRAGGING://正在滑行中
				break;
			case ViewPager.SCROLL_STATE_SETTLING://目标加载完毕
				break;
			case ViewPager.SCROLL_STATE_IDLE://什么也没做
			    /*	if (viewPage.getCurrentItem() == viewPage.getAdapter().getCount() - 1 && !flag) {
					goMain();
			     }*/
				break;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int arg0) {
			if (arg0 == 0) {
				button.setVisibility(View.GONE);
			}
			if (arg0 == 1) {
				button.setVisibility(View.GONE);
			}
			if (arg0 == 2) {
				button.setVisibility(View.GONE);
			}
			if (arg0 == 3) {
				button.setVisibility(View.GONE);
			}
			if (arg0 == 4) {
				button.setVisibility(View.VISIBLE);
			}
		}

	}
}
