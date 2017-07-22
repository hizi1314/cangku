package com.guantang.cangkuonline.commonadapter;

/**
 * 万能的ListView和GridView适配器，简化代码量，只需要写不同的地方
 * */

import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class CommonAdapter<T> extends BaseAdapter {

	protected Context context;
	protected List<T> mList;
	protected final int LayoutId;
	
//	public CommonAdapter(Context mContext,int LayoutId){
//		context = mContext;
//		this.LayoutId = LayoutId;
//	}
	
	public CommonAdapter(Context mContext,List<T> mList,int LayoutId){
		context = mContext;
		this.mList = mList;
		this.LayoutId = LayoutId;
	}
	
	public void setData(List<T> mList){
		this.mList = mList;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		return mList.size();
	}

	@Override
	public T getItem(int position) {
		// TODO 自动生成的方法存根
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO 自动生成的方法存根
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO 自动生成的方法存根
		ViewHolder holder = ViewHolder.getInstance(context, convertView, parent, LayoutId, position);
		convert(holder, getItem(position));
		return holder.getConvertView();
	}
	
	public List<T> getList(){
		return mList;
	}
	
	public abstract void convert(ViewHolder holder, T item);
	
}
