package com.guantang.cangkuonline.adapter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.guantang.cangkuonline.JellyViewPager.NetPictureFragment;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;
import com.guantang.cangkuonline.webservice.WebserviceImport;

import android.R.integer;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.LruCache;
import android.util.Base64;
import android.view.View;

public class NetPicFragPagerAdapter extends FragmentPagerAdapter {

	private List<String> ImageNameList = new ArrayList<String>();
	private int width=0,height=0;
	private Context context;
	private String hpid = "";
	
	
	public NetPicFragPagerAdapter(Context context,String hpid,FragmentManager fm,List<String> ImageNameList,int width,int height) {
		super(fm);
		// TODO 自动生成的构造函数存根
		this.context = context;
		this.hpid = hpid;
		this.ImageNameList = ImageNameList;
		this.width=width;
		this.height=height;
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO 自动生成的方法存根
		NetPictureFragment netPictureFragment = new NetPictureFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("width", width);
		bundle.putInt("height", height);
		bundle.putString("hpid", "hpid");
		bundle.putString("ImageName", ImageNameList.get(arg0));
		netPictureFragment.setArguments(bundle);
		return netPictureFragment;
	}

	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		return ImageNameList.size();
	}

}
