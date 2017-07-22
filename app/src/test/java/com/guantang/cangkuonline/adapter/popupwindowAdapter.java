package com.guantang.cangkuonline.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.guantang.cangkuonline.R;

public class popupwindowAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<Map<String,Object>> list;
	
	public popupwindowAdapter(Context context,List<Map<String,Object>> list) {
		inflater=LayoutInflater.from(context);
		this.list=list;
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView==null){
			holder=new ViewHolder();
			convertView=inflater.inflate(R.layout.item_textview, null);	
			holder.mTextView=(TextView) convertView.findViewById(R.id.text1);
			holder.mTextView.setSingleLine();
			holder.mImageView=(ImageView) convertView.findViewById(R.id.imageview1);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		
		if(list.get(position).get("ckmc").equals("—按仓库类型查看——")||list.get(position).get("ckmc").equals("—按货品信息查看——")){
			//设置这一项点击不起作用
			holder.mImageView.setVisibility(View.VISIBLE);
			holder.mTextView.setTextColor(Color.parseColor("#90030303"));
			holder.mTextView.setTextSize(15);
			
		}else{
			holder.mImageView.setVisibility(View.GONE);
			holder.mTextView.setPadding(10, 0, 0, 0);
			holder.mTextView.setTextColor(Color.parseColor("#030303"));
			holder.mTextView.setTextSize(19);
			
		}
		if(list.get(position)!=null){
			holder.mTextView.setText(list.get(position).get("ckmc").toString());
		}
		return convertView;
	}
	/*
	 * 设置监听是否可用
	 * */
	@Override
	public boolean isEnabled(int position) {
		// TODO 自动生成的方法存根
		if(list.get(position).get("ckmc").equals("—按仓库类型查看——")||list.get(position).get("ckmc").equals("—按货品信息查看——")){
			return false;
		}else{
			return true;
		}
	}
	
}
class ViewHolder{
	TextView mTextView;
	ImageView mImageView;
}