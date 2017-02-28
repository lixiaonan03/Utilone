package com.lxn.utilone.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.volley.listener.HttpBackListListener;
import android.volley.util.VolleyUtil;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lxn.utilone.R;
import com.lxn.utilone.adapter.NearsearchItemAdapter;
import com.lxn.utilone.adapter.SearchItemAdapter;
import com.lxn.utilone.model.GoodItemVO;
import com.lxn.utilone.model.PageParameter;
import com.lxn.utilone.model.QueryReqBean;
import com.lxn.utilone.util.CommonVariable;
import com.lxn.utilone.util.PreferencesUtil;
import com.lxn.utilone.view.CustomProgressDialog;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 搜索商品界面还是商品列表界面
 * 
 * @author lxn
 *
 */
public class SearchActivity extends BaseActivity {

	private InputMethodManager inputMethodManager;
	private ImageView top_back;
	private EditText search_edit;// 搜索输入框
	private ImageView search_go;// 去搜索
	private TextView hot_one, hot_two, hot_three;

	private PullToRefreshListView listview;
	private SearchItemAdapter adapter;
	private TextView sort_default;
	private RelativeLayout sort_price;
	private TextView sort_price_text;
	private ImageView sort_price_img;
	private TextView sort_salesvolume;
	private TextView sort_goodevaluate;

	private int PRICE_UP = 1;// 价格升序排列
	private int PRICE_DOWN = 2;// 价格降序排列

	private View linedefault;
	private View lineprice;
	private View linesales;
	private View lineevaluate;
    
	private int sort_flag=0;//排序的规则  0默认1 价格升 2价格降 3销量 4 好评
	private int sort_price_current = 0;// 当前价格的排序规则
	public List<GoodItemVO> list;// 要加载的数据

	private RelativeLayout nodata;
	private LinearLayout baselinear;
	private Button clear_allhistory;
	private ListView nearsearchlist;
	private LinearLayout searchanswerLin;
	private List<String> nearList=new ArrayList<String>();// 搜索历史列表
	private NearsearchItemAdapter nearsearchadapter;

	private Integer catid;
	private CustomProgressDialog customProgressDialog;
    
	int pageNum = 0;
	int pagesize = 2;
	private String searchkey="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		catid = getIntent().getIntExtra("id", -1);
		customProgressDialog = new CustomProgressDialog(SearchActivity.this,
				"正在加载......");

		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		initView();
	}

	/**
	 * 启动当前activity
	 * @param context  上下文环境
	 * @param id     商品ID 为-1是表示查询的是全部
     */
	public static void actionStart(Context context,int id){
		Intent intent=new Intent(context,SearchActivity.class);
		intent.putExtra("id",id);
		context.startActivity(intent);
	}
	/**
	 * 初始化控件
	 */
	private void initView() {
		top_back = (ImageView) findViewById(R.id.top_back);

		search_edit = (EditText) findViewById(R.id.search_edit);
		search_go = (ImageView) findViewById(R.id.searchimg);
		// 热门搜索的
		hot_one = (TextView) findViewById(R.id.hot_one);
		hot_two = (TextView) findViewById(R.id.hot_two);
		hot_three = (TextView) findViewById(R.id.hot_three);
		hot_one.setOnClickListener(hotviewclick);
		hot_two.setOnClickListener(hotviewclick);
		hot_three.setOnClickListener(hotviewclick);
		// 最近搜索的
		baselinear = (LinearLayout) findViewById(R.id.baselinear);

		clear_allhistory = (Button) findViewById(R.id.clear_allhistory);
		nearsearchlist = (ListView) findViewById(R.id.nearsearchlist);
		// 搜索结果的界面
		searchanswerLin = (LinearLayout) findViewById(R.id.searchanswerLin);
		// 排序的
		sort_default = (TextView) findViewById(R.id.sort_default);
		sort_price = (RelativeLayout) findViewById(R.id.sort_price);
		sort_price_text = (TextView) findViewById(R.id.sort_price_text);
		sort_price_img = (ImageView) findViewById(R.id.sort_price_img);
		sort_salesvolume = (TextView) findViewById(R.id.sort_salesvolume);
		sort_goodevaluate = (TextView) findViewById(R.id.sort_goodevaluate);

		linedefault = findViewById(R.id.linedefault);
		lineprice = findViewById(R.id.lineprice);
		linesales = findViewById(R.id.linesales);
		lineevaluate = findViewById(R.id.lineevaluate);

		listview = (PullToRefreshListView) findViewById(R.id.listview);
		// 没有数据的
		nodata = (RelativeLayout) findViewById(R.id.nodata);

		top_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		search_edit.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					// 先隐藏键盘
					((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
							.hideSoftInputFromWindow(getCurrentFocus()
									.getWindowToken(),
									InputMethodManager.HIDE_NOT_ALWAYS);
					String searchtext = search_edit.getText().toString().trim();
					if (null == searchtext || "".equals(searchtext)) {
						Toast.makeText(SearchActivity.this, "请输入搜索内容！",
								Toast.LENGTH_SHORT).show();
					} else {
						// 修改 Pref 文件中最近搜索的内容
						savehistory(searchtext);
						// 执行搜索方法
						pageNum=0;
						loadData(true,searchtext,"ONSHELF_TIME desc");
					}

					return true;
				}
				return false;
			}

		});
		search_go.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String searchtext = search_edit.getText().toString().trim();
				if (null == searchtext || "".equals(searchtext)) {
					Toast.makeText(SearchActivity.this, "请输入搜索内容！",
							Toast.LENGTH_SHORT).show();
				} else {
					// 修改 Pref 文件中最近搜索的内容
					savehistory(searchtext);
					// 执行搜索方法
					loadData(true,searchtext,"ONSHELF_TIME desc");
				}
			}
		});
		clear_allhistory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO 清空历史搜索的内容
				if (null != nearList && nearList.size() > 0) {
					PreferencesUtil.setStr(PreferencesUtil.SEARCH_HISTORY, "");
					nearList.clear();
					nearsearchadapter.notifyDataSetChanged();
				}
			}
		});
		sort_default.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 默认的被点击 选中默认的排序
				sort_flag=0;
				sort_price_current = 0;
				sort_price_img.setImageResource(R.drawable.sort_price);

				sort_default.setTextColor(getResources().getColor(
						R.color.order_textcolor_checked));
				sort_default
						.setBackgroundResource(R.color.order_background_checked);
				linedefault
						.setBackgroundResource(R.color.order_linecolor_checked);

				sort_price_text.setTextColor(getResources().getColor(
						R.color.order_textcolor_nochecked));
				sort_price
						.setBackgroundResource(R.color.order_background_nochecked);
				lineprice
						.setBackgroundResource(R.color.order_linecolor_nochecked);

				sort_salesvolume.setTextColor(getResources().getColor(
						R.color.order_textcolor_nochecked));
				sort_salesvolume
						.setBackgroundResource(R.color.order_background_nochecked);
				linesales
						.setBackgroundResource(R.color.order_linecolor_nochecked);

				sort_goodevaluate.setTextColor(getResources().getColor(
						R.color.order_textcolor_nochecked));
				sort_goodevaluate
						.setBackgroundResource(R.color.order_background_nochecked);
				lineevaluate
						.setBackgroundResource(R.color.order_linecolor_nochecked);
				pageNum=0;
				if (catid == -1) {
					// 获取搜索历史内容
					loadData(true,searchkey,"ONSHELF_TIME desc");
				} else {
					getGoodsBycatid(true,catid,"ONSHELF_TIME desc");
				}
			}
		});
		sort_price.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 价格排序
				sort_price_text.setTextColor(getResources().getColor(
						R.color.order_textcolor_checked));
				sort_price
						.setBackgroundResource(R.color.order_background_checked);
				lineprice
						.setBackgroundResource(R.color.order_linecolor_checked);

				sort_default.setTextColor(getResources().getColor(
						R.color.order_textcolor_nochecked));
				sort_default
						.setBackgroundResource(R.color.order_background_nochecked);
				linedefault
						.setBackgroundResource(R.color.order_linecolor_nochecked);

				sort_salesvolume.setTextColor(getResources().getColor(
						R.color.order_textcolor_nochecked));
				sort_salesvolume
						.setBackgroundResource(R.color.order_background_nochecked);
				linesales
						.setBackgroundResource(R.color.order_linecolor_nochecked);

				sort_goodevaluate.setTextColor(getResources().getColor(
						R.color.order_textcolor_nochecked));
				sort_goodevaluate
						.setBackgroundResource(R.color.order_background_nochecked);
				lineevaluate
						.setBackgroundResource(R.color.order_linecolor_nochecked);
				if (sort_price_current == PRICE_UP) {
					// 当前是升序的 图片箭头和数据变成降序的
					sort_flag=2;
					sort_price_img.setImageResource(R.drawable.sort_price_down);
					sort_price_current = PRICE_DOWN;
					pageNum=0;
					if (catid == -1) {
						// 获取搜索历史内容
						loadData(true,searchkey,"MALL_PRICE desc");
					} else {
						getGoodsBycatid(true,catid,"MALL_PRICE desc");
					}
				} else {
					sort_price_current = PRICE_UP;
					sort_price_img.setImageResource(R.drawable.sort_price_up);
					sort_flag=1;
					pageNum=0;
					if (catid == -1) {
						// 获取搜索历史内容
						loadData(true,searchkey,"MALL_PRICE asc");
					} else {
						getGoodsBycatid(true,catid,"MALL_PRICE asc");
					}
				}
			}
		});
		sort_salesvolume.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				// TODO 销量排序
				sort_price_current = 0;
				sort_flag=3;
				sort_price_img.setImageResource(R.drawable.sort_price);

				sort_salesvolume.setTextColor(getResources().getColor(
						R.color.order_textcolor_checked));
				sort_salesvolume
						.setBackgroundResource(R.color.order_background_checked);
				linesales
						.setBackgroundResource(R.color.order_linecolor_checked);

				sort_default.setTextColor(getResources().getColor(
						R.color.order_textcolor_nochecked));
				sort_default
						.setBackgroundResource(R.color.order_background_nochecked);
				linedefault
						.setBackgroundResource(R.color.order_linecolor_nochecked);

				sort_price_text.setTextColor(getResources().getColor(
						R.color.order_textcolor_nochecked));
				sort_price
						.setBackgroundResource(R.color.order_background_nochecked);
				lineprice
						.setBackgroundResource(R.color.order_linecolor_nochecked);

				sort_goodevaluate.setTextColor(getResources().getColor(
						R.color.order_textcolor_nochecked));
				sort_goodevaluate
						.setBackgroundResource(R.color.order_background_nochecked);
				lineevaluate
						.setBackgroundResource(R.color.order_linecolor_nochecked);
                pageNum=0;
				if (catid == -1) {
					// 获取搜索历史内容
					loadData(true,searchkey,"GOODS_SALES desc");
				} else {
					getGoodsBycatid(true,catid,"GOODS_SALES desc");
				}
			}
		});
		sort_goodevaluate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				// TODO 好评排序
				sort_price_current = 0;
				sort_flag=4;
				sort_price_img.setImageResource(R.drawable.sort_price);

				sort_goodevaluate.setTextColor(getResources().getColor(
						R.color.order_textcolor_checked));
				sort_goodevaluate
						.setBackgroundResource(R.color.order_background_checked);
				lineevaluate
						.setBackgroundResource(R.color.order_linecolor_checked);

				sort_default.setTextColor(getResources().getColor(
						R.color.order_textcolor_nochecked));
				sort_default
						.setBackgroundResource(R.color.order_background_nochecked);
				linedefault
						.setBackgroundResource(R.color.order_linecolor_nochecked);

				sort_price_text.setTextColor(getResources().getColor(
						R.color.order_textcolor_nochecked));
				sort_price
						.setBackgroundResource(R.color.order_background_nochecked);
				lineprice
						.setBackgroundResource(R.color.order_linecolor_nochecked);

				sort_salesvolume.setTextColor(getResources().getColor(
						R.color.order_textcolor_nochecked));
				sort_salesvolume
						.setBackgroundResource(R.color.order_background_nochecked);
				linesales
						.setBackgroundResource(R.color.order_linecolor_nochecked);
                pageNum=0;
				if (catid == -1) {
					// 获取搜索历史内容
					loadData(true,searchkey,"GOODS_EVALS desc");
				} else {
					getGoodsBycatid(true,catid,"GOODS_EVALS desc");
				}
			}
		});
		String nearsearch = PreferencesUtil
				.getStr(PreferencesUtil.SEARCH_HISTORY);
		String[] near = nearsearch.split("#");
		nearList = new ArrayList<String>();
		Collections.addAll(nearList, near);
		if (nearsearch.equals("")) {
			// 没有保存过
			nearList.removeAll(nearList);
		}
		initListView();
	}

	/**
	 * 获取最近搜索的内容
	 */
	private void getnearsearch() {
		nearsearchadapter = new NearsearchItemAdapter(SearchActivity.this,
				nearList);
		nearsearchlist.setAdapter(nearsearchadapter);
		nearsearchlist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 修改 Pref 文件中最近搜索的内容
				String key = nearList.get(position);
				savehistory(key);
				pageNum=0;
				loadData(true,key,"ONSHELF_TIME desc");
			}
		});
	}

	/**
	 * 初始化listview
	 */
	private void initListView() {

		list = new ArrayList<GoodItemVO>();
		 /*
         * Mode.BOTH：同时支持上拉下拉
         * Mode.PULL_FROM_START：只支持下拉Pulling Down
         * Mode.PULL_FROM_END：只支持上拉Pulling Up
         */
        /*
         * 如果Mode设置成Mode.BOTH，需要设置刷新Listener为OnRefreshListener2，并实现onPullDownToRefresh()、onPullUpToRefresh()两个方法。
         * 如果Mode设置成Mode.PULL_FROM_START或Mode.PULL_FROM_END，需要设置刷新Listener为OnRefreshListener，同时实现onRefresh()方法。
         * 当然也可以设置为OnRefreshListener2，但是Mode.PULL_FROM_START的时候只调用onPullDownToRefresh()方法，
         * Mode.PULL_FROM的时候只调用onPullUpToRefresh()方法.
         */
        /*
         * setOnRefreshListener(OnRefreshListener listener):设置刷新监听器；
         * setOnLastItemVisibleListener(OnLastItemVisibleListener listener):设置是否到底部监听器；
         * setOnPullEventListener(OnPullEventListener listener);设置事件监听器；
         * onRefreshComplete()：设置刷新完成
         */
        /*
         * pulltorefresh.setOnScrollListener()
         */
		// SCROLL_STATE_TOUCH_SCROLL(1) 正在滚动
		// SCROLL_STATE_FLING(2) 手指做了抛的动作（手指离开屏幕前，用力滑了一下）
		// SCROLL_STATE_IDLE(0) 停止滚动
        /*
         * setOnLastItemVisibleListener
         * 当用户拉到底时调用
         */
        /*
         * setOnTouchListener是监控从点下鼠标 （可能拖动鼠标）到放开鼠标（鼠标可以换成手指）的整个过程 ，他的回调函数是onTouchEvent（MotionEvent event）,
         * 然后通过判断event.getAction()是MotionEvent.ACTION_UP还是ACTION_DOWN还是ACTION_MOVE分别作不同行为。
         * setOnClickListener的监控时间只监控到手指ACTION_DOWN时发生的行为
         */
		listview.setMode(Mode.BOTH);
		ILoadingLayout startLabels = listview    
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时    
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示    
        ILoadingLayout endLabels = listview.getLoadingLayoutProxy(    
                false, true);    
        endLabels.setPullLabel("上拉加载...");// 刚下拉时，显示的提示    
        endLabels.setRefreshingLabel("正在加载...");// 刷新时    
        endLabels.setReleaseLabel("放开加载...");// 下来达到一定距离时，显示的提示   
		
		
        adapter = new SearchItemAdapter(SearchActivity.this, list);
		listview.setAdapter(adapter);
		
		listview.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				// 下拉刷新
				pageNum = 0;
				switch (sort_flag) {
				case 0:
					if (catid == -1) {
						// 获取搜索历史内容
						loadData(true,searchkey,"ONSHELF_TIME desc");
					} else {
						getGoodsBycatid(true,catid,"ONSHELF_TIME desc");
					}
				
					break;
				case 1:
					if (catid == -1) {
						// 获取搜索历史内容
						loadData(true,searchkey,"MALL_PRICE asc");
					} else {
						getGoodsBycatid(true,catid,"MALL_PRICE asc");
					}
					break;
				case 2:
					if (catid == -1) {
						// 获取搜索历史内容
						loadData(true,searchkey,"MALL_PRICE desc");
					} else {
						getGoodsBycatid(true,catid,"MALL_PRICE desc");
					}
					break;
				case 3:
					if (catid == -1) {
						// 获取搜索历史内容
						loadData(true,searchkey,"GOODS_SALES desc");
					} else {
						getGoodsBycatid(true,catid,"GOODS_SALES desc");
					}
					break;
				case 4:
					if (catid == -1) {
						// 获取搜索历史内容
						loadData(true,searchkey,"GOODS_EVALS desc");
					} else {
						getGoodsBycatid(true,catid,"GOODS_EVALS desc");
					}
					break;
				default:
					break;
				}
				
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				// 上拉加载更多
				switch (sort_flag) {
				case 0:
					if (catid == -1) {
						// 获取搜索历史内容
						loadData(false,searchkey,"ONSHELF_TIME desc");
					} else {
						getGoodsBycatid(false,catid,"ONSHELF_TIME desc");
					}
					break;
				case 1:
					if (catid == -1) {
						// 获取搜索历史内容
						loadData(false,searchkey,"MALL_PRICE asc");
					} else {
						getGoodsBycatid(false,catid,"MALL_PRICE asc");
					}
					break;
				case 2:
					if (catid == -1) {
						// 获取搜索历史内容
						loadData(false,searchkey,"MALL_PRICE desc");
					} else {
						getGoodsBycatid(false,catid,"MALL_PRICE desc");
					}
					break;
				case 3:
					if (catid == -1) {
						// 获取搜索历史内容
						loadData(false,searchkey,"GOODS_SALES desc");
					} else {
						getGoodsBycatid(false,catid,"GOODS_SALES desc");
					}
					break;
				case 4:
					if (catid == -1) {
						// 获取搜索历史内容
						loadData(false,searchkey,"GOODS_EVALS desc");
					} else {
						getGoodsBycatid(false,catid,"GOODS_EVALS desc");
					}
					break;
				default:
					break;
				}
			}
		});
		pageNum = 0;
		// 在这做判断 如果是商品分类进来的就
				if (catid == -1) {
					// 获取搜索历史内容
					getnearsearch();
					baselinear.setVisibility(View.VISIBLE);
					searchanswerLin.setVisibility(View.GONE);
				} else {
					getGoodsBycatid(true,catid,"ONSHELF_TIME desc");
					baselinear.setVisibility(View.GONE);
					searchanswerLin.setVisibility(View.VISIBLE);
				}
		

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				//TODO 使用下拉刷新的这个组件时  记得在这里arg2-1 再对list进行get 要不让会发生数组越界
				arg2=arg2-1;
				long id = list.get(arg2).getEnnGoods().getGoodsId();
				String name = list.get(arg2).getEnnGoods().getGoodsName();
				String goodstate = list.get(arg2).getEnnGoods().getGoodsState();
				String imgurl = list.get(arg2).getImgUrl();
				BigDecimal goodprice = list.get(arg2).getEnnGoods()
						.getMallPrice();
				// int iseletron = list.get(arg2).getEnnGoods().getIsdianzi();
				Intent intent = new Intent();
				intent.setClass(SearchActivity.this,
						ProductdeatilActivity.class);
				intent.putExtra("id", id);
				intent.putExtra("name", name);
				intent.putExtra("imgurl", imgurl);
				intent.putExtra("goodprice", goodprice);
				intent.putExtra("goodstate", goodstate);
				startActivity(intent);
			}
		});
		listview.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// 隐藏软键盘
				if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
					if (getCurrentFocus() != null) {
						inputMethodManager.hideSoftInputFromWindow(
								getCurrentFocus().getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
					}

				}
				return false;
			}
		});
	}

	/**
	 * 搜索加载数据
	 * 
	 * @param searchtext
	 *            搜索的内容
	 */
	private void loadData(final boolean clear,String searchtext,String sort) {
		pageNum++;
		catid=-1;
		searchkey=searchtext;
		// 向服务端去加载数据
		customProgressDialog.show();
		QueryReqBean req=new QueryReqBean();
		PageParameter pageparameter=new PageParameter();
		pageparameter.setCurrentPage(pageNum);
		pageparameter.setPageSize(pagesize);
		req.setPageParameter(pageparameter);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("goodsName", searchtext);
		map.put("goodsTname", searchtext);
		req.setSearchParams(map);
		req.setSortExp(sort);
		VolleyUtil.sendObjectByPostToListBypage(CommonVariable.GetGoodURL, null, req,
				GoodItemVO.class, new HttpBackListListener<GoodItemVO>() {

					@Override
					public void onSuccess(List<GoodItemVO> t) {
						// TODO 把收到的内容装换成list
						customProgressDialog.dismiss();
						List<GoodItemVO> newlist = t;
						if (clear)
							list.clear();
						if (newlist != null)
							list.addAll(newlist);
						adapter.set_list(list);
						adapter.notifyDataSetChanged();
						listview.onRefreshComplete();
						if (newlist != null && newlist.size() > 0) {
							if (newlist.size() < pagesize) {
								listview.setMode(Mode.PULL_FROM_START);
							} else {
								listview.setMode(Mode.BOTH);
							}
							baselinear.setVisibility(View.GONE);
							searchanswerLin.setVisibility(View.VISIBLE);
						} else {
							if (pageNum == 1) {
								baselinear.setVisibility(View.GONE);
								searchanswerLin.setVisibility(View.GONE);
								nodata.setVisibility(View.VISIBLE);
							} else {
								baselinear.setVisibility(View.GONE);
								searchanswerLin.setVisibility(View.VISIBLE);
								listview.setMode(Mode.PULL_FROM_START);
							}

						}
					}

					@Override
					public void onFail(String failstring) {
						// TODO Auto-generated method stub
						customProgressDialog.dismiss();
						baselinear.setVisibility(View.GONE);
						searchanswerLin.setVisibility(View.GONE);
						nodata.setVisibility(View.VISIBLE);
						Toast.makeText(SearchActivity.this, "加载数据失败！", Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onError(VolleyError error) {
						customProgressDialog.dismiss();
						baselinear.setVisibility(View.GONE);
						searchanswerLin.setVisibility(View.GONE);
						nodata.setVisibility(View.VISIBLE);
						Toast.makeText(SearchActivity.this, "加载数据失败！", Toast.LENGTH_SHORT).show();
					}

				}, false, null);
	}

	/**
	 * 分页向服务加载商品列表页
	 * @param clear  
	 * @param id
	 * @param sort
	 */
	private void getGoodsBycatid(final boolean clear,Integer id,String sort) {
		pageNum++;
		// 向服务端去加载数据
		customProgressDialog.show();
		QueryReqBean req=new QueryReqBean();
		PageParameter pageparameter=new PageParameter();
		pageparameter.setCurrentPage(pageNum);
		pageparameter.setPageSize(pagesize);
		req.setPageParameter(pageparameter);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("catId", id);
		req.setSearchParams(map);
		req.setSortExp(sort);
		VolleyUtil.sendObjectByPostToListBypage(CommonVariable.GetGoodURL, null, req,
				GoodItemVO.class, new HttpBackListListener<GoodItemVO>() {

					@Override
					public void onSuccess(List<GoodItemVO> t) {
						// TODO 把收到的内容装换成list
						customProgressDialog.dismiss();
						List<GoodItemVO> newlist = t;
						if (clear)
							list.clear();
						if (newlist != null)
							list.addAll(newlist);
						
						adapter.set_list(list);
						adapter.notifyDataSetChanged();
						listview.onRefreshComplete();
						if (newlist != null && newlist.size() > 0) {
							if (newlist.size() < pagesize) {
								listview.setMode(Mode.PULL_FROM_START);
							} else {
								listview.setMode(Mode.BOTH);
							}
						}else{
							if(pageNum==1){
								baselinear.setVisibility(View.GONE);
								searchanswerLin.setVisibility(View.GONE);
								nodata.setVisibility(View.VISIBLE);
							}else{
								listview.setMode(Mode.PULL_FROM_START);
							}
						}
					}

					@Override
					public void onFail(String failstring) {
						// TODO Auto-generated method stub
						customProgressDialog.dismiss();
						baselinear.setVisibility(View.GONE);
						searchanswerLin.setVisibility(View.GONE);
						nodata.setVisibility(View.VISIBLE);
						Toast.makeText(SearchActivity.this,"加载数据失败！",Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onError(VolleyError error) {
						customProgressDialog.dismiss();
						baselinear.setVisibility(View.GONE);
						searchanswerLin.setVisibility(View.GONE);
						nodata.setVisibility(View.VISIBLE);
						Toast.makeText(SearchActivity.this,"加载数据失败！",Toast.LENGTH_SHORT).show();
					}

				}, false, null);
	}

	OnClickListener hotviewclick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
			case R.id.hot_one:
				// 修改 Pref 文件中最近搜索的内容
				savehistory(hot_one.getText().toString().trim());
				// 执行搜索方法
				pageNum=0;
				loadData(true,hot_one.getText().toString().trim(),"ONSHELF_TIME desc");
				break;
			case R.id.hot_two:
				savehistory(hot_two.getText().toString().trim());
				pageNum=0;
				loadData(true,hot_two.getText().toString().trim(),"ONSHELF_TIME desc");
				break;
			case R.id.hot_three:
				savehistory(hot_three.getText().toString().trim());
				pageNum=0;
				loadData(true,hot_three.getText().toString().trim(),"ONSHELF_TIME desc");
				break;

			default:
				break;
			}
		}
	};

	/**
	 * 保存搜索历史
	 * @param searchkey
	 */
	private void savehistory(String searchkey) {
		// 判断是否已经存在历史
		if (nearList.contains(searchkey)) {
			nearList.remove(searchkey);
		}
		nearList.add(0, searchkey);
		String sb = "";
		int size = nearList.size();
		for (int i = 0; i < (size > 6 ? 6 : size); i++) {
			String element = nearList.get(i);
			if (sb.equals(""))
				sb = element;
			else
				sb += "#" + element;
		}
		PreferencesUtil.setStr(PreferencesUtil.SEARCH_HISTORY, sb);
	}
}
