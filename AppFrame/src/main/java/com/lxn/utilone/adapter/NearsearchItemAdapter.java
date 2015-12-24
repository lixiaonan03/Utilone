package com.lxn.utilone.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.lxn.utilone.R;
import com.lxn.utilone.util.PreferencesUtil;

import java.util.List;


/**
 * 最近搜索列表的适配器
 *
 * @author lxn
 */
public class NearsearchItemAdapter extends BaseAdapter {
    private Context _context;
    private List<String> list;


    public NearsearchItemAdapter(Context context, List<String> list) {
        super();
        this._context = context;
        this.list = list;
    }

    public List<String> getlist() {
        return list;
    }

    public void setlist(List<String> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list.size() > 6) {
            return 6;
        }
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        return list.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final String onesearch = (String) getItem(position);
        Holder holder;
        if (null == convertView) {
            convertView = View.inflate(_context, R.layout.adapter_nearsearch_item, null);
            holder = new Holder();
            holder.searchname = (TextView) convertView.findViewById(R.id.searchname);
            holder.delimg = (ImageView) convertView.findViewById(R.id.delimg);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.searchname.setText(onesearch);
        holder.delimg.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                list.remove(onesearch);
                StringBuilder sb = new StringBuilder();
                int size = list.size();
                for (int i = 0; i < (size > 6 ? 6 : size); i++) {
                    String element = list.get(i);
                    if (sb.toString().equals("")) {
                        sb.append(element);
                    } else {
                        sb.append("#").append(element);
                    }
                }
                notifyDataSetChanged();
                PreferencesUtil.setStr(PreferencesUtil.SEARCH_HISTORY, sb.toString());
            }
        });
        return convertView;
    }

    private static class Holder {
        public TextView searchname;
        public ImageView delimg;
    }

}
