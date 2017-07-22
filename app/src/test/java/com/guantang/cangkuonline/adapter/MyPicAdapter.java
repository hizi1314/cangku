package com.guantang.cangkuonline.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.helper.TemporarilyImageBean;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;

public class MyPicAdapter extends BaseAdapter{
	private List<TemporarilyImageBean> ImageList = new ArrayList<TemporarilyImageBean>();
	private Context context;
	private LayoutInflater inflater;
	private int width;
	//定义构造方法加入Context参数
	public MyPicAdapter(Context context,List<TemporarilyImageBean> ImageList,int width){
		this.context=context;
		this.ImageList=ImageList;
		this.width=width;
	}
	public MyPicAdapter(Context context,int width){
		this.context=context;
		this.width=width;
		inflater = LayoutInflater.from(context);
	}
	
	public void setData(List<TemporarilyImageBean> ImageList){
		this.ImageList = ImageList;
		notifyDataSetChanged();
	}
	
	@Override
	//返回值为所有图片的个数
	public int getCount() {
		return ImageList.size();
	}

	public Object getItem(int position) {
		return ImageList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Holder holder = null;
		if(convertView==null){
			holder = new Holder();
			convertView = inflater.inflate(R.layout.image_delete, null);
			holder.imgView = (ImageView) convertView.findViewById(R.id.imgView);
			holder.deleteView = (ImageView) convertView.findViewById(R.id.delete_icon);
//			holder.deleteView.measure(0, 0);
//			holder.deleteView.getLayoutParams().height=holder.deleteView.getMeasuredWidth();
			holder.imgView.setLayoutParams(new FrameLayout.LayoutParams(width*3/8,width*3/8));
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		
		holder.deleteView.setVisibility(View.GONE);
		if(ImageList.get(position)!=null){
			holder.imgView.setImageBitmap(ImageList.get(position).getBm());
		}else{
			holder.imgView.setVisibility(View.GONE);
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
