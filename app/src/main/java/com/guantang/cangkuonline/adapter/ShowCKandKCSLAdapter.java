package com.guantang.cangkuonline.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.guantang.cangkuonline.R;

public class ShowCKandKCSLAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<String> list = new ArrayList<String>();
	
	public ShowCKandKCSLAdapter(Context context){
		this.context = context;
		inflater = LayoutInflater.from(context);
	}
	public void setData(List<String> list){
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
		if(convertView == null){
			convertView = inflater.inflate(R.layout.onlytextview, null);
			textView = (TextView) convertView.findViewById(R.id.mytext1);
			convertView.setTag(textView);
		}else{
			textView = (TextView) convertView.getTag();
		}
		if(list.get(position)!=null){
			textView.setText(list.get(position));
		}
		return convertView;
	}
	
}
