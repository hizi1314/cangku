package com.guantang.cangkuonline.adapter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.security.auth.PrivateCredentialPermission;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.activity.ShowImagePagerActivity;
import com.guantang.cangkuonline.helper.TemporarilyImageBean;
import com.guantang.cangkuonline.webservice.WebserviceImport;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class DownLoadImageAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private Context context;
	private Holder holder;
	private List<TemporarilyImageBean> ImageList = new ArrayList<TemporarilyImageBean>();
	private SharedPreferences mSharedPreferences;
	private int width;
	public DownLoadImageAdapter(Context mcontext,SharedPreferences mSharedPreferences,int width){
		this.context = mcontext;
		inflater = LayoutInflater.from(context);
		this.mSharedPreferences = mSharedPreferences;
		this.width = width;
	}
	
	public DownLoadImageAdapter(Context mcontext,List<TemporarilyImageBean> list,SharedPreferences mSharedPreferences,int width){
		this.context = mcontext;
		inflater = LayoutInflater.from(context);
		ImageList = list;
		this.mSharedPreferences = mSharedPreferences;
		this.width = width;
	}
	
	public void setData(List<TemporarilyImageBean> list){
		ImageList = list;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		return ImageList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO 自动生成的方法存根
		return ImageList.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO 自动生成的方法存根
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO 自动生成的方法存根
		if(convertView==null){
			holder = new Holder();
			convertView = inflater.inflate(R.layout.image_delete, null);
			holder.deleteView = (ImageView) convertView.findViewById(R.id.delete_icon);
			holder.imgView = (ImageView) convertView.findViewById(R.id.imgView);
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
}
