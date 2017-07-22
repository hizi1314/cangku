package com.guantang.cangkuonline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.guantang.cangkuonline.R;

public class AllFunctionAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private Context context;
	private String[] str_array;
	
	public AllFunctionAdapter(Context context,String[] array){
		this.context=context;
		inflater=LayoutInflater.from(context);
		str_array=array;
	}
	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		return str_array==null?0:str_array.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO 自动生成的方法存根
		return str_array==null?null:str_array[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO 自动生成的方法存根
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView textView = null;
		if(convertView==null){
			convertView=inflater.inflate(R.layout.subscribe_category_item, null);
			textView = (TextView) convertView.findViewById(R.id.text_item);
			convertView.setTag(textView);
		}else{
			textView=(TextView) convertView.getTag();
		}
		if(str_array[position]!=null){
			textView.setText(str_array[position]);
		}
		textView.setTextSize(16);
		textView.setTextColor(context.getResources().getColor(R.color.ziti1));
		
		return convertView;
	}

}
