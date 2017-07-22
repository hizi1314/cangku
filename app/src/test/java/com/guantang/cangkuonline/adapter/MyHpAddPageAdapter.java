package com.guantang.cangkuonline.adapter;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class MyHpAddPageAdapter extends PagerAdapter {
	private ArrayList<View> views=new ArrayList<View>();
	public MyHpAddPageAdapter(ArrayList<View> views){
		this.views=views;
	}
	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		return views.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO 自动生成的方法存根
		return arg0==arg1;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO 自动生成的方法存根
		View v=views.get(position);
		container.addView(v);
		return v;
	}
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO 自动生成的方法存根
		View v=views.get(position);
		container.removeView(v);
	}
}
