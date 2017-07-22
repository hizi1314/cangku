package com.guantang.cangkuonline.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.adapter.MyPicAdapter.Holder;
import com.guantang.cangkuonline.helper.TemporarilyImageBean;

public class ImageDeleteAdapter extends BaseAdapter {
	private List<TemporarilyImageBean> ImageList = new ArrayList<TemporarilyImageBean>();
	private LayoutInflater inflater;
	private Context context;
	private int width;
	
	public ImageDeleteAdapter(Context context, int width) {
		super();
		this.context = context;
		this.width = width;
		inflater = LayoutInflater.from(context);
	}
	
	public void setData(List<TemporarilyImageBean> ImageList){
		this.ImageList = ImageList;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		return ImageList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO 自动生成的方法存根
		return ImageList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO 自动生成的方法存根
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO 自动生成的方法存根

		Holder holder = null;
		if(convertView==null){
			holder = new Holder();
			convertView = inflater.inflate(R.layout.image_delete, null);
			holder.imgView = (ImageView) convertView.findViewById(R.id.imgView);
			holder.deleteView = (ImageView) convertView.findViewById(R.id.delete_icon);
//			imgView.measure(0, 0);
//			imgView.getLayoutParams().height=imgView.getMeasuredWidth();
			holder.imgView.setLayoutParams(new FrameLayout.LayoutParams(width*3/8,width*3/8));
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		
		if(ImageList.get(position)!=null){
			if(ImageList.get(position).getDeletethis()){
				holder.deleteView.setVisibility(View.VISIBLE);
				holder.imgView.setColorFilter(Color.parseColor("#77000000"));
			}else if(!ImageList.get(position).getDeletethis()){
				holder.deleteView.setVisibility(View.GONE);
				holder.imgView.setColorFilter(null);
			}
			holder.imgView.setImageBitmap(ImageList.get(position).getBm());
		}
		return convertView;
	}
	
	class Holder{
		ImageView imgView,deleteView;
	}
	
	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				context.getResources().getDisplayMetrics());
	}
}
