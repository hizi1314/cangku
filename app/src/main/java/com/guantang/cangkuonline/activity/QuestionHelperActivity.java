package com.guantang.cangkuonline.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.adapter.MyHpAddPageAdapter;

public class QuestionHelperActivity extends Activity implements OnPageChangeListener{
	private ViewPager mViewPager;
	private ArrayList<View> mList;
	private LinearLayout pagerpointlayout;
	private ImageView pointImageView1,pointImageView2,pointImageView3,pointImageView4,pointImageView5;
	private int startfrom=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_helpviewpager);
		Intent intent = getIntent();
		startfrom = intent.getIntExtra("startfrom", 0);
		initView();
	}
	public void initView(){
		mViewPager = (ViewPager) this.findViewById(R.id.myviewpager);
		pagerpointlayout = (LinearLayout) this.findViewById(R.id.pagerpointlayout);
		pointImageView1 = (ImageView) this.findViewById(R.id.pager1);
		pointImageView2 = (ImageView) this.findViewById(R.id.pager2);
		pointImageView3 = (ImageView) this.findViewById(R.id.pager3);
		pointImageView4 = (ImageView) this.findViewById(R.id.pager4);
		pointImageView5 = (ImageView) this.findViewById(R.id.pager5);
		
		LayoutInflater inflater = LayoutInflater.from(this);
		mList = new ArrayList<View>();
		MyHpAddPageAdapter mHpAddPageAdapter = null;
		switch (startfrom) {
		case 1:
			pagerpointlayout.setVisibility(View.VISIBLE);
			View view1_1 = inflater.inflate(R.layout.helper_1_1, null);
			View view1_2 = inflater.inflate(R.layout.helper_1_2, null);
			View view1_3 = inflater.inflate(R.layout.helper_1_3, null);
			View view1_4 = inflater.inflate(R.layout.helper_1_4, null);
			View view1_5 = inflater.inflate(R.layout.helper_1_5, null);
			
			mList.add(view1_1);
			mList.add(view1_2);
			mList.add(view1_3);
			mList.add(view1_4);
			mList.add(view1_5);
			mHpAddPageAdapter = new MyHpAddPageAdapter(mList);
			break;
		case 2:
			pagerpointlayout.setVisibility(View.GONE);
			View view2_1 = inflater.inflate(R.layout.helper_2_1, null);
			mList.add(view2_1);
			mHpAddPageAdapter = new MyHpAddPageAdapter(mList);
			break;
		case 3:
			pagerpointlayout.setVisibility(View.VISIBLE);
			pointImageView3.setVisibility(View.GONE);
			pointImageView4.setVisibility(View.GONE);
			pointImageView5.setVisibility(View.GONE);
			View view3_1 = inflater.inflate(R.layout.helper_3_1, null);
			View view3_2 = inflater.inflate(R.layout.helper_3_2, null);
			mList.add(view3_1);
			mList.add(view3_2);
			mHpAddPageAdapter = new MyHpAddPageAdapter(mList);
			break;
		case 4:
			pagerpointlayout.setVisibility(View.GONE);
			View view4_1 = inflater.inflate(R.layout.helper_4_1, null);
			mList.add(view4_1);
			mHpAddPageAdapter = new MyHpAddPageAdapter(mList);
			break;
		case 5:
			pagerpointlayout.setVisibility(View.VISIBLE);
			View view5_1 = inflater.inflate(R.layout.helper_5_1, null);
			View view5_2 = inflater.inflate(R.layout.helper_5_2, null);
			View view5_3 = inflater.inflate(R.layout.helper_5_3, null);
			View view5_4 = inflater.inflate(R.layout.helper_5_4, null);
			View view5_5 = inflater.inflate(R.layout.helper_5_5, null);
			mList.add(view5_1);
			mList.add(view5_2);
			mList.add(view5_3);
			mList.add(view5_4);
			mList.add(view5_5);
			mHpAddPageAdapter = new MyHpAddPageAdapter(mList);
			break;
		case 6:
			pagerpointlayout.setVisibility(View.GONE);
			View view6_1 = inflater.inflate(R.layout.helper_6_1, null);
			mList.add(view6_1);
			mHpAddPageAdapter = new MyHpAddPageAdapter(mList);
			break;
		case 7:
			pagerpointlayout.setVisibility(View.GONE);
			View view7_1 = inflater.inflate(R.layout.helper_7_1, null);
			mList.add(view7_1);
			mHpAddPageAdapter = new MyHpAddPageAdapter(mList);
			break;
		case 8:
			pagerpointlayout.setVisibility(View.VISIBLE);
			pointImageView3.setVisibility(View.GONE);
			pointImageView4.setVisibility(View.GONE);
			pointImageView5.setVisibility(View.GONE);
			View view8_1 = inflater.inflate(R.layout.helper_8_1, null);
			View view8_2 = inflater.inflate(R.layout.helper_8_2, null);
			mList.add(view8_1);
			mList.add(view8_2);
			mHpAddPageAdapter = new MyHpAddPageAdapter(mList);
			break;
		default:
			break;
		}
		mViewPager.setAdapter(mHpAddPageAdapter);
		mViewPager.setOnPageChangeListener(this);
		
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
		switch (startfrom) {
		case 1:
			switch (arg0) {
			case 0:
				pointImageView1.setImageResource(R.drawable.page_now);
				pointImageView2.setImageResource(R.drawable.page);
				pointImageView3.setImageResource(R.drawable.page);
				pointImageView4.setImageResource(R.drawable.page);
				pointImageView5.setImageResource(R.drawable.page);
				break;
			case 1:
				pointImageView1.setImageResource(R.drawable.page);
				pointImageView2.setImageResource(R.drawable.page_now);
				pointImageView3.setImageResource(R.drawable.page);
				pointImageView4.setImageResource(R.drawable.page);
				pointImageView5.setImageResource(R.drawable.page);
				break;
			case 2:
				pointImageView1.setImageResource(R.drawable.page);
				pointImageView2.setImageResource(R.drawable.page);
				pointImageView3.setImageResource(R.drawable.page_now);
				pointImageView4.setImageResource(R.drawable.page);
				pointImageView5.setImageResource(R.drawable.page);
				break;
			case 3:
				pointImageView1.setImageResource(R.drawable.page);
				pointImageView2.setImageResource(R.drawable.page);
				pointImageView3.setImageResource(R.drawable.page);
				pointImageView4.setImageResource(R.drawable.page_now);
				pointImageView5.setImageResource(R.drawable.page);
				break;
			case 4:
				pointImageView1.setImageResource(R.drawable.page);
				pointImageView2.setImageResource(R.drawable.page);
				pointImageView3.setImageResource(R.drawable.page);
				pointImageView4.setImageResource(R.drawable.page);
				pointImageView5.setImageResource(R.drawable.page_now);
				break;
			default:
				break;
			}
			break;
		case 3:
			switch (arg0) {
			case 0:
				pointImageView1.setImageResource(R.drawable.page_now);
				pointImageView2.setImageResource(R.drawable.page);
				break;
			case 1:
				pointImageView1.setImageResource(R.drawable.page);
				pointImageView2.setImageResource(R.drawable.page_now);
				break;
			default:
				break;
			}
			break;
		case 5:
			switch (arg0) {
			case 0:
				pointImageView1.setImageResource(R.drawable.page_now);
				pointImageView2.setImageResource(R.drawable.page);
				pointImageView3.setImageResource(R.drawable.page);
				pointImageView4.setImageResource(R.drawable.page);
				pointImageView5.setImageResource(R.drawable.page);
				break;
			case 1:
				pointImageView1.setImageResource(R.drawable.page);
				pointImageView2.setImageResource(R.drawable.page_now);
				pointImageView3.setImageResource(R.drawable.page);
				pointImageView4.setImageResource(R.drawable.page);
				pointImageView5.setImageResource(R.drawable.page);
				break;
			case 2:
				pointImageView1.setImageResource(R.drawable.page);
				pointImageView2.setImageResource(R.drawable.page);
				pointImageView3.setImageResource(R.drawable.page_now);
				pointImageView4.setImageResource(R.drawable.page);
				pointImageView5.setImageResource(R.drawable.page);
				break;
			case 3:
				pointImageView1.setImageResource(R.drawable.page);
				pointImageView2.setImageResource(R.drawable.page);
				pointImageView3.setImageResource(R.drawable.page);
				pointImageView4.setImageResource(R.drawable.page_now);
				pointImageView5.setImageResource(R.drawable.page);
				break;
			case 4:
				pointImageView1.setImageResource(R.drawable.page);
				pointImageView2.setImageResource(R.drawable.page);
				pointImageView3.setImageResource(R.drawable.page);
				pointImageView4.setImageResource(R.drawable.page);
				pointImageView5.setImageResource(R.drawable.page_now);
				break;
			default:
				break;
			}
			break;
		case 8:
			switch (arg0) {
			case 0:
				pointImageView1.setImageResource(R.drawable.page_now);
				pointImageView2.setImageResource(R.drawable.page);
				break;
			case 1:
				pointImageView1.setImageResource(R.drawable.page);
				pointImageView2.setImageResource(R.drawable.page_now);
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
		
	}
	
}
