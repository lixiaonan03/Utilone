package com.lxn.utilone.tabfragmentdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.ViewGroup;

import com.lxn.utilone.R;
import com.lxn.utilone.view.MyViewPager;
import com.lxn.utilone.view.TitleIndicator;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

/**
 * @copyright:北京爱钱帮财富科技有限公司 功能描述:  tab页切换的fragment 使用的简单demo
 * 作 者:  李晓楠
 * 时 间： 2016/8/12 14:52
 */
public class IndicatorFragmentActivity extends FragmentActivity{


    protected int mCurrentTab = 0;
    protected int mLastTab = -1;

    //存放选项卡信息的列表
    protected ArrayList<TabInfo> mTabs = new ArrayList<>();

    //viewpager adapter
    protected MyAdapter myAdapter = null;

    //viewpager
    protected MyViewPager mPager;

    //选项卡控件
    protected TitleIndicator mIndicator;

    public TitleIndicator getIndicator() {
        return mIndicator;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tabfragment);
        initViews();

        //设置viewpager内部页面之间的间距
        mPager.setPageMargin(getResources().getDimensionPixelSize(R.dimen.page_margin_width));
        //设置viewpager内部页面间距的drawable
        mPager.setPageMarginDrawable(R.color.page_viewer_margin_color);
    }

    @Override
    protected void onDestroy() {
        mTabs.clear();
        mTabs = null;
        myAdapter.notifyDataSetChanged();
        myAdapter = null;
        mPager.setAdapter(null);
        mPager = null;
        mIndicator = null;

        super.onDestroy();
    }


    public static final int FRAGMENT_ONE = 0;
    public static final int FRAGMENT_TWO = 1;
    public static final int FRAGMENT_THREE = 2;

    private void initViews() {
        // 这里初始化界面
        mTabs.add(new TabInfo(FRAGMENT_ONE, "第一个", FragmentOne.class));
        mTabs.add(new TabInfo(FRAGMENT_TWO, "第2个", FragmentTwo.class));
        mTabs.add(new TabInfo(FRAGMENT_THREE, "第三个", FragmentThree.class));
        mCurrentTab = 1;


        myAdapter = new MyAdapter(this, getSupportFragmentManager(), mTabs);

        mPager = (MyViewPager) findViewById(R.id.pager);
        mPager.setAdapter(myAdapter);
        //滑动监听
        mPager.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mIndicator.onScrolled((mPager.getWidth() + mPager.getPageMargin()) * position + positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                mIndicator.onSwitched(position);
                mCurrentTab = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    mLastTab = mCurrentTab;
                }
            }
        });
        mPager.setOffscreenPageLimit(mTabs.size());

        mIndicator = (TitleIndicator) findViewById(R.id.pagerindicator);
        mIndicator.init(mCurrentTab, mTabs, mPager);

        mPager.setCurrentItem(mCurrentTab);
        mLastTab = mCurrentTab;
    }




    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }

    /**
     * 单个选项卡类，每个选项卡包含名字，图标以及提示（可选，默认不显示）
     */
    public static class TabInfo implements Parcelable {

        private int id;
        private int icon;
        private String name = null;
        public boolean hasTips = false;
        public Fragment fragment = null;
        public boolean notifyChange = false;
        @SuppressWarnings("rawtypes")
        public Class fragmentClass = null;

        @SuppressWarnings("rawtypes")
        public TabInfo(int id, String name, Class clazz) {
            this(id, name, 0, clazz);
        }

        @SuppressWarnings("rawtypes")
        public TabInfo(int id, String name, boolean hasTips, Class clazz) {
            this(id, name, 0, clazz);
            this.hasTips = hasTips;
        }

        @SuppressWarnings("rawtypes")
        public TabInfo(int id, String name, int iconid, Class clazz) {
            super();

            this.name = name;
            this.id = id;
            icon = iconid;
            fragmentClass = clazz;
        }

        public TabInfo(Parcel p) {
            this.id = p.readInt();
            this.name = p.readString();
            this.icon = p.readInt();
            this.notifyChange = p.readInt() == 1;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setIcon(int iconid) {
            icon = iconid;
        }

        public int getIcon() {
            return icon;
        }

        @SuppressWarnings({"rawtypes", "unchecked"})
        public Fragment createFragment() {
            if (fragment == null) {
                Constructor constructor;
                try {
                    constructor = fragmentClass.getConstructor(new Class[0]);
                    fragment = (Fragment) constructor.newInstance(new Object[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return fragment;
        }

        public static final Creator<TabInfo> CREATOR = new Creator<TabInfo>() {
            public TabInfo createFromParcel(Parcel p) {
                return new TabInfo(p);
            }

            public TabInfo[] newArray(int size) {
                return new TabInfo[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel p, int flags) {
            p.writeInt(id);
            p.writeString(name);
            p.writeInt(icon);
            p.writeInt(notifyChange ? 1 : 0);
        }

    }

    /**
     * fragment的页面适配器
     */
    public class MyAdapter extends FragmentPagerAdapter {
        ArrayList<TabInfo> tabs = null;
        Context context = null;

        public MyAdapter(Context context, FragmentManager fm, ArrayList<TabInfo> tabs) {
            super(fm);
            this.tabs = tabs;
            this.context = context;
        }

        @Override
        public Fragment getItem(int pos) {
            Fragment fragment = null;
            if (tabs != null && pos < tabs.size()) {
                TabInfo tab = tabs.get(pos);
                if (tab == null) return null;
                fragment = tab.createFragment();
            }
            return fragment;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            if (tabs != null && tabs.size() > 0) return tabs.size();
            return 0;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabInfo tab = tabs.get(position);
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            tab.fragment = fragment;
            return fragment;
        }
    }
}
