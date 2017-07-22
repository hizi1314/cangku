package com.guantang.cangkuonline.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.guantang.cangkuonline.R;

public class GridViewAdapter extends BaseAdapter {
	private LayoutInflater infater;
	private Context context;
	private int ScreenWidth;
	private List<Map<String,Object>> mlist=new ArrayList<Map<String,Object>>();
	public GridViewAdapter(Context context,int width){
		this.context=context;
		infater=LayoutInflater.from(context);
		ScreenWidth=width;
	}
	public void setData(List<Map<String,Object>> list){
		mlist=list;
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		return mlist.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO 自动生成的方法存根
		return mlist.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO 自动生成的方法存根
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO 自动生成的方法存根
		ViewHolder Holder;
		if(convertView==null){
			Holder = new ViewHolder();
			convertView=infater.inflate(R.layout.item_gridviewdemo_layout, null);
			Holder.imageview=(ImageView) convertView.findViewById(R.id.gridtupian);
			Holder.textview=(TextView) convertView.findViewById(R.id.gridtxt);
			convertView.setTag(Holder);
		}else{
			Holder=(ViewHolder) convertView.getTag();
		}
		Map<String,Object> map=mlist.get(position);
		Object imageObject = map.get("image");
		if(imageObject!=null){
			Holder.imageview.setImageResource(Integer.parseInt(imageObject.toString()));
		}
		
		Object nameObject = map.get("name");
		Holder.textview.setText(nameObject==null?"":nameObject.toString());
		return convertView;
	}
	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				context.getResources().getDisplayMetrics());
	}
	class ViewHolder{
		ImageView imageview = null;
		TextView textview = null;
	}
}
