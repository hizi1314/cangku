package com.guantang.cangkuonline.JellyViewPager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Base64;
import android.util.LruCache;
import android.widget.Toast;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;
import com.guantang.cangkuonline.static_constant.PathConstant;
import com.guantang.cangkuonline.webservice.WebserviceImport;

public class TestFragPagerAdapter extends FragmentPagerAdapter {
	private List<String> ImageNameList = new ArrayList<String>();
	private ExecutorService cacheThreadpool = Executors.newCachedThreadPool();
	private int width=0,height=0;
	private Context context;
	private SharedPreferences mSharedPreferences;

	public TestFragPagerAdapter(Context context,FragmentManager fm,List<String> ImageNameList,int width,int height) {
		super(fm);
		this.ImageNameList = ImageNameList;
		this.width=width;
		this.height=height;
		this.context=context;
		mSharedPreferences = context.getSharedPreferences(ShareprefenceBean.SHAREPREFENCE_NAME, Context.MODE_PRIVATE);
	}

	@Override
	public Fragment getItem(int arg0) {
		PictureFragment pictureFragment = new PictureFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("width", width);
		bundle.putInt("height", height);
		bundle.putString("ImageName", ImageNameList.get(arg0));
		pictureFragment.setArguments(bundle);
		return pictureFragment;
	}

	@Override
	public int getCount() {
		return ImageNameList == null?0:ImageNameList.size();

	}
	
}
