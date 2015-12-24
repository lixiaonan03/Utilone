package com.lxn.utilone.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.volley.listener.HttpBackListListener;
import android.volley.util.VolleyUtil;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.lxn.utilone.R;
import com.lxn.utilone.activity.SearchActivity;
import com.lxn.utilone.adapter.ClassifyMenuContentItemAdapter;
import com.lxn.utilone.adapter.ClassifyMenuItemAdapter;
import com.lxn.utilone.model.EnnGoodsCat;
import com.lxn.utilone.util.CommonVariable;
import com.lxn.utilone.util.ToastUtils;
import com.lxn.utilone.view.CustomProgressDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品分类的模块
 * 
 * @author lxn
 */
public class ClassifyFragment extends BaseFragment {

	private RelativeLayout search;// 搜索框的按钮
	private ListView menu_listview;// 左边的分类列表
	private GridView menu_gridview_content;// 右边的详细分类列表
	private ClassifyMenuItemAdapter menuadapter;
	private ClassifyMenuContentItemAdapter menucontentadapter;
	private List<EnnGoodsCat> menulist = new ArrayList<EnnGoodsCat>();// 左边一级分类的数据
	private List<EnnGoodsCat> menucontentlist = new ArrayList<EnnGoodsCat>();// 右边二级分类的数据
	private CustomProgressDialog customProgressDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_classify, container,
				false);
		return view;
	}
	@Override
	public void onFirstVisible(Bundle savedInstanceState) {
		super.onFirstVisible(savedInstanceState);
		initView();
		customProgressDialog=new CustomProgressDialog(getActivity(), "正在加载......");
	}

	@Override
	public void onFragmentResume() {
		super.onFragmentResume();
		if(null!=menulist&&menulist.size()>0){

		}else{
			initdata();
		}
	}

	@Override
	public void onFragmentPause() {
		super.onFragmentPause();
	}
    /**
     * 初始化控件
     */
	private void initView() {
		search = (RelativeLayout) getView().findViewById(R.id.search);
		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), SearchActivity.class);
				startActivity(intent);
			}
		});
		menu_listview = (ListView) getView().findViewById(R.id.menu_list);
		menu_gridview_content = (GridView) getView().findViewById(
				R.id.menu_list_content);
		//左边 一级分类的
		menuadapter = new ClassifyMenuItemAdapter(getActivity(), menulist);
		menu_listview.setAdapter(menuadapter);
		menu_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				smoothScroollListView(arg2);
				menuadapter.setViewBackGround(arg2);
				menuadapter.notifyDataSetChanged();
			}
		});
		// 右边九宫格布局的
		menucontentadapter = new ClassifyMenuContentItemAdapter(getActivity(),
				menucontentlist);
		menu_gridview_content.setAdapter(menucontentadapter);
		menu_gridview_content.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				Integer id = menucontentlist.get(arg2).getCatId();
				String catname = menucontentlist.get(arg2).getCatName();
				Intent intent = new Intent();
				intent.setClass(getActivity(), SearchActivity.class);
				intent.putExtra("id", id);
				intent.putExtra("catname", catname);
				startActivity(intent);
			}
		});
	}
    /**
     * 加载一级分类的数据
     */
	private void initdata() {
		customProgressDialog.show();
		VolleyUtil.sendStringRequestByGetToList(CommonVariable.GetCatURL
				+ "-100", null, null, EnnGoodsCat.class, new HttpBackListListener<EnnGoodsCat>() {

			@Override
			public void onSuccess(List<EnnGoodsCat> t) {
				// TODO 请求数据成功
				menulist = t;
				menuadapter.set_list(menulist);
				menuadapter.setViewBackGround(0);
				menuadapter.notifyDataSetChanged();
				smoothScroollListView(0);
			}

			@Override
			public void onFail(String failstring) {
				// TODO 业务访问失败
				customProgressDialog.dismiss();
				ToastUtils.toastshort("加载数据失败！");
			}

			@Override
			public void onError(VolleyError error) {
				// TODO 接口访问失败
				customProgressDialog.dismiss();
				ToastUtils.toastshort("加载数据失败！");
			}

		}, false, null);
	}

	/**
	 * 加载右边条目中分类加载的数据
	 * 
	 * @param id
	 */
	private void loadContentData(Integer id) {
		String url = CommonVariable.GetCatURL + id;
		VolleyUtil.sendStringRequestByGetToList(url, null, null,
				EnnGoodsCat.class, new HttpBackListListener<EnnGoodsCat>() {

					@Override
					public void onSuccess(List<EnnGoodsCat> t) {
						// TODO 请求数据成功
						customProgressDialog.dismiss();
						if (t != null) {
							menucontentlist = t;
						} else {
							menucontentlist = new ArrayList<EnnGoodsCat>();
						}
						menucontentadapter.set_list(menucontentlist);
						menucontentadapter.notifyDataSetChanged();
					}

					@Override
					public void onFail(String failstring) {
						// TODO 业务访问失败
						customProgressDialog.dismiss();
						ToastUtils.toastshort("加载数据失败！");
						menucontentlist = new ArrayList<EnnGoodsCat>();
						menucontentadapter.set_list(menucontentlist);
						menucontentadapter.notifyDataSetChanged();
					}

					@Override
					public void onError(VolleyError error) {
						// TODO 接口访问失败
						customProgressDialog.dismiss();
						ToastUtils.toastshort("加载数据失败！");
						menucontentlist = new ArrayList<EnnGoodsCat>();
						menucontentadapter.set_list(menucontentlist);
						menucontentadapter.notifyDataSetChanged();
					}

				}, false, null);
	}

	

	/**
	 * listView scroll
	 * 
	 * @param position
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	public void smoothScroollListView(int position) {
		if (Build.VERSION.SDK_INT >= 21) {
			menu_listview.setSelectionFromTop(position, 0);
		} else if (Build.VERSION.SDK_INT >= 11) {
			menu_listview.smoothScrollToPositionFromTop(position, 0, 500);
		} else if (Build.VERSION.SDK_INT >= 8) {
			int firstVisible = menu_listview.getFirstVisiblePosition();
			int lastVisible = menu_listview.getLastVisiblePosition();
			if (position < firstVisible) {
				menu_listview.smoothScrollToPosition(position);
			} else {
				if (firstVisible == 0) {
					menu_listview.smoothScrollToPosition(position + lastVisible
							- firstVisible);
				} else {
					menu_listview.smoothScrollToPosition(position + lastVisible
							- firstVisible - 1);
				}
			}
		} else {
			menu_listview.setSelection(position);
		}
		// TODO 根据左边点击条目的数据 获取右边表格布局中的数据
		if(!customProgressDialog.isShowing()){
			customProgressDialog.show();
		}
		if(menulist.size()>0){
			loadContentData(menulist.get(position).getCatId());
		}
	}
}
