package com.guantang.cangkuonline.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.guantang.cangkuonline.R;

public class CkListViewAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private Context context;
	private List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
	
	public CkListViewAdapter(Context context) {
		// TODO 自动生成的构造函数存根
		this.context = context;
		inflater = LayoutInflater.from(context);
	}
	
	public void setData (List<Map<String, Object>> list){
		this.list = list;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO 自动生成的方法存根
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO 自动生成的方法存根
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO 自动生成的方法存根
		TextView textView = null;
		if(convertView == null ){
			convertView = inflater.inflate(R.layout.popupwindow_list_textview, null);
			textView = (TextView) convertView.findViewById(R.id.textview_popup);
			convertView.setTag(textView);
		}else{
			textView = (TextView) convertView.getTag();
		}
		
		Map<String, Object> map = list.get(position);
		if(map!=null){
			textView.setText(map.get("ckmc").toString());
		}
		
		return convertView;
	}

}
