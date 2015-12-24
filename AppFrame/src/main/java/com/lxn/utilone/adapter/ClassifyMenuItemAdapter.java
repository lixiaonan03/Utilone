package com.lxn.utilone.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lxn.utilone.R;
import com.lxn.utilone.model.EnnGoodsCat;

/**
 * 商品分类左边条目的adapter
 * @author lxn
 *
 */
public class ClassifyMenuItemAdapter extends BaseAdapter {
	private Context _context;
	private List<EnnGoodsCat> _list;
	private int mIndex = -1;

	public ClassifyMenuItemAdapter(Context context, List<EnnGoodsCat> list) {
		super();
		this._context = context;
		this._list = list;
	}
	public void setViewBackGround(int index){
        this.mIndex = index;
    }
	public List<EnnGoodsCat> get_list() {
		return _list;
	}

	public void set_list(List<EnnGoodsCat> _list) {
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
		EnnGoodsCat oneitem = (EnnGoodsCat) getItem(position);
		Holder holder;
		if (null == convertView) {
			convertView = View.inflate(_context, R.layout.adapter_classifymenu_item,
					null);
			holder = new Holder();
			holder.item_text = (TextView) convertView
					.findViewById(R.id.item_text);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		if (oneitem != null ) {
			if(null!=oneitem.getCatName()){
				holder.item_text.setText(oneitem.getCatName());
			}else{
				holder.item_text.setText("");
			}
		}
		if(position == mIndex){
			holder.item_text.setTextColor(_context.getResources().getColor(R.color.classify_menu_textcolorcheck));
			convertView.setBackgroundResource(R.color.classify_menu_bgwhite);
        }else{
        	holder.item_text.setTextColor(_context.getResources().getColor(R.color.classify_menu_textcolornocheck));
        	convertView.setBackgroundResource(R.color.classify_menu_bg);
        }

		return convertView;
	}

	private static class Holder {
		TextView item_text;
	}

}
