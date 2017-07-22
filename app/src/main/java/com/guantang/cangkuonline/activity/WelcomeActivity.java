package com.guantang.cangkuonline.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guantang.cangkuonline.R;

public class WelcomeActivity extends Activity implements OnPageChangeListener{
	private ViewPager mViewPager;
	private ImageView pointview1,pointview2,pointview3;
	private LinearLayout pagerpointlayout;
	private ArrayList<View> viewArrayList = new ArrayList<View>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		initView();
	}
	
	public void initView(){
		pagerpointlayout = (LinearLayout) findViewById(R.id.pagerpointlayout);
		mViewPager=(ViewPager) findViewById(R.id.pager);
		pointview1= (ImageView) findViewById(R.id.point1);
		pointview2= (ImageView) findViewById(R.id.point2);
		pointview3= (ImageView) findViewById(R.id.point3);
		mViewPager.setOnPageChangeListener(this);
		
		LayoutInflater inflater = LayoutInflater.from(this);
		ViewGroup viewGroup1 = (ViewGroup) inflater.inflate(R.layout.imageviewlayout, null);
		viewGroup1.setBackgroundResource(R.drawable.welcome1);
		viewArrayList.add(viewGroup1);
		
		ViewGroup viewGroup2 = (ViewGroup) inflater.inflate(R.layout.imageviewlayout, null);
		viewGroup2.setBackgroundResource(R.drawable.welcome2);
		viewArrayList.add(viewGroup2);
		
		ViewGroup viewGroup3 = (ViewGroup) inflater.inflate(R.layout.imageviewlayout, null);
		viewGroup3.setBackgroundResource(R.drawable.welcome3);
		TextView imgButton = (TextView) viewGroup3.findViewById(R.id.imgbtn);
		imgButton.setBackgroundResource(R.drawable.enterbtn);
		imgButton.setVisibility(View.VISIBLE);
		imgButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent intent=new Intent(WelcomeActivity.this,NewLoginActivity.class);
    			startActivity(intent);
    			finish();
			}
		});
		viewArrayList.add(viewGroup3);
		
		ViewpagerAdapter mViewpagerAdapter = new ViewpagerAdapter();
		mViewPager.setAdapter(mViewpagerAdapter);
	}
	
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO 自动生成的方法存根
		
	}
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO 自动生成的方法存根
		
	}
	@Override
	public void onPageSelected(int arg0) {
		// TODO 自动生成的方法存根
		switch(arg0){
		case 0:
//			pagerpointlayout.setVisibility(View.VISIBLE);
			pointview1.setImageResource(R.drawable.point_new);
			pointview2.setImageResource(R.drawable.point);
			pointview3.setImageResource(R.drawable.point);
			break;
		case 1:
//			pagerpointlayout.setVisibility(View.VISIBLE);
			pointview1.setImageResource(R.drawable.point);
			pointview2.setImageResource(R.drawable.point_new);
			pointview3.setImageResource(R.drawable.point);
			break;
		case 2:
//			pagerpointlayout.setVisibility(View.GONE);
			pointview1.setImageResource(R.drawable.point);
			pointview2.setImageResource(R.drawable.point);
			pointview3.setImageResource(R.drawable.point_new);
			break;
		}
	}
	
	class ViewpagerAdapter extends PagerAdapter{
		
		@Override
		public int getCount() {
			// TODO 自动生成的方法存根
			return viewArrayList==null?0:viewArrayList.size();
		}
		
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO 自动生成的方法存根
			return arg0==arg1;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			container.addView(viewArrayList.get(position));
			return viewArrayList.get(position);

		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
		
	}
}
