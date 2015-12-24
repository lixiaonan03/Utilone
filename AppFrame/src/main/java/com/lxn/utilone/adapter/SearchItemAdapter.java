package com.lxn.utilone.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxn.utilone.R;
import com.lxn.utilone.model.GoodItemVO;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.math.BigDecimal;
import java.util.List;

public class SearchItemAdapter extends BaseAdapter {
	private Context _context;
	private List<GoodItemVO> _list;

	public SearchItemAdapter(Context context, List<GoodItemVO> list) {
		super();
		this._context = context;
		this._list = list;
	}

	public List<GoodItemVO> get_list() {
		return _list;
	}

	public void set_list(List<GoodItemVO> _list) {
		this._list = _list;
	}

	@Override
	public int getCount() {
		return _list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return _list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		GoodItemVO oneitem = (GoodItemVO) getItem(position);
		Holder holder;
		if (null == convertView) {
			convertView = View.inflate(_context, R.layout.adapter_search_item,
					null);
			holder = new Holder();
			holder.item_img = (ImageView) convertView
					.findViewById(R.id.item_img);
			holder.item_describe = (TextView) convertView
					.findViewById(R.id.item_describe);
			holder.item_price = (TextView) convertView
					.findViewById(R.id.item_price);
			holder.item_pricenouse = (TextView) convertView
					.findViewById(R.id.item_pricenouse);
			holder.item_goodevaluate = (TextView) convertView
					.findViewById(R.id.item_goodevaluate);
			holder.item_peoplenum = (TextView) convertView
					.findViewById(R.id.item_peoplenum);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		 // 使用DisplayImageOptions.Builder()创建DisplayImageOptions  60*60
        DisplayImageOptions options = new DisplayImageOptions.Builder()   
            .showImageOnLoading(R.drawable.loadingimg60)         // 设置图片下载期间显示的图片  
            .showImageForEmptyUri(R.drawable.loadingimg60)  // 设置图片Uri为空或是错误的时候显示的图片  
            .showImageOnFail(R.drawable.loadingimg60)       // 设置图片加载或解码过程中发生错误显示的图片      
            .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中  
            .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中  
            .build();                                   // 创建配置过得DisplayImageOption对象  
        ImageLoader.getInstance().displayImage(oneitem.getImgUrl(), holder.item_img, options);
        //ImageLoader.getInstance().displayImage(oneitem.getImgUrl(),  holder.item_img);
        //VolleyUtil.disPlayImage(holder.item_img, oneitem.getImgUrl(), R.drawable.ic_launcher, R.drawable.ic_launcher);
		if(null!=oneitem.getEnnGoods()&&null!=oneitem.getEnnGoods().getGoodsName()){
			holder.item_describe.setText(oneitem.getEnnGoods().getGoodsName());
		}else{
			holder.item_describe.setText("");
		}
		if(null!=oneitem.getEnnGoods()&&null!=oneitem.getEnnGoods().getMallPrice()){
			holder.item_price.setText("￥"+oneitem.getEnnGoods().getMallPrice());
		}else{
			holder.item_price.setText("￥ 0.00");
		}
		if(null!=oneitem.getEnnGoods()&&null!=oneitem.getEnnGoods().getGoodsPrice()){
			holder.item_pricenouse.setText("￥"+new BigDecimal(oneitem.getEnnGoods().getGoodsPrice()).setScale(2, BigDecimal.ROUND_HALF_UP));
		}else{
			holder.item_pricenouse.setText("￥0.00");
		}
		holder.item_pricenouse.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG); //中划线
		if(null!=oneitem.getEnnGoods()&&null!=oneitem.getEnnGoods().getGoodsEvals()){
			holder.item_goodevaluate.setText("好评 "+oneitem.getEnnGoods().getGoodsEvals().multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP)+"%");
		}else{
			holder.item_goodevaluate.setText("好评 100%");
		}
		if(null!=oneitem.getEnnGoods()&&null!=oneitem.getEnnGoods().getGoodsSales()){
			holder.item_peoplenum.setText(oneitem.getEnnGoods().getGoodsSales().setScale(0, BigDecimal.ROUND_HALF_UP)+"人付款");
		}else{
			holder.item_peoplenum.setText("0人付款");
		}
		return convertView;
	}

	private static class Holder {
		ImageView item_img;
		TextView item_describe;
		TextView item_price;
		TextView item_pricenouse;
		TextView item_goodevaluate;
		TextView item_peoplenum;
	}

}
