package com.guantang.cangkuonline.VerticalViewpager;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.VerticalViewpager.ViewPager.OnPageChangeListener;
import com.guantang.cangkuonline.activity.LoginActivity;

public class GuideActivity extends Activity implements OnPageChangeListener{
	private ViewPager mViewPager;
	private Button startBtn;
	private ImageView guide1_1,guide1_2,guide1_3,guide2_1,guide2_2,guide2_3,guide3_1,
						guide3_2,guide3_3,guide4_1,guide4_2,guide4_3;
	private ImageView t1_next,t2_next,t3_next,t4_next;
	private List<View> pagers = new ArrayList<View>();
	private VerticalFragementPagerAdapter mAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guidelayout);
		init();
		animal(0);
	}
	public void init(){
		mViewPager = (ViewPager) this.findViewById(R.id.pager);
		
		View view1 = LayoutInflater.from(this).inflate(
				R.layout.guidepager1, null);
		guide1_1 = (ImageView) view1.findViewById(R.id.guide1_1);
		guide1_2 = (ImageView) view1.findViewById(R.id.guide1_2);
		guide1_3 = (ImageView) view1.findViewById(R.id.guide1_3);
		t1_next = (ImageView) view1.findViewById(R.id.t1_next);
		pagers.add(view1);
		View view2 = LayoutInflater.from(this).inflate(
				R.layout.guidepager2, null);
		guide2_1 = (ImageView) view2.findViewById(R.id.guide2_1);
		guide2_2 = (ImageView) view2.findViewById(R.id.guide2_2);
		guide2_3 = (ImageView) view2.findViewById(R.id.guide2_3);
		t2_next = (ImageView) view2.findViewById(R.id.t2_next);
		pagers.add(view2);
		
		View view3 = LayoutInflater.from(this).inflate(
				R.layout.guidepager3, null);
		guide3_1 = (ImageView) view3.findViewById(R.id.guide3_1);
		guide3_2 = (ImageView) view3.findViewById(R.id.guide3_2);
		guide3_3 = (ImageView) view3.findViewById(R.id.guide3_3);
		t3_next = (ImageView) view3.findViewById(R.id.t3_next);
		pagers.add(view3);
		
		View view4 = LayoutInflater.from(this).inflate(
				R.layout.guidepager4, null);
		guide4_1 = (ImageView) view4.findViewById(R.id.guide4_1);
		guide4_2 = (ImageView) view4.findViewById(R.id.guide4_2);
		guide4_3 = (ImageView) view4.findViewById(R.id.guide4_3);
		t4_next = (ImageView) view4.findViewById(R.id.t4_next);
		pagers.add(view4);
		
		View view5 = LayoutInflater.from(this).inflate(
				R.layout.guidepager5, null);
		startBtn = (Button) view5.findViewById(R.id.t5_start);
		startBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent intent=new Intent(GuideActivity.this,LoginActivity.class);
    			startActivity(intent);
    			finish();
			}
		});
		pagers.add(view5);
		
		mAdapter = new VerticalFragementPagerAdapter();
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(this);
		
	}
	
	private void animal(int position){
		Animation animationTop = AnimationUtils.loadAnimation(this,
				R.anim.tutorail_scalate_top);
		final Animation animationBottom = AnimationUtils.loadAnimation(this,
				R.anim.tutorail_bottom);
		
		switch(position){
		case 0:
			Animation alpha_scale_animation2 = AnimationUtils.loadAnimation(this, R.anim.alpha_scale2);
			Animation alpha_scale_animation3 = AnimationUtils.loadAnimation(this, R.anim.alpha_scale2);
			guide1_1.startAnimation(animationTop);
			t1_next.startAnimation(animationBottom);
			alpha_scale_animation2.setStartOffset(500);
			guide1_2.startAnimation(alpha_scale_animation2);
			alpha_scale_animation3.setStartOffset(1000);
			guide1_3.startAnimation(alpha_scale_animation3);
			break;
		case 1:
//			Animation rotate_scale_animation = AnimationUtils.loadAnimation(this, R.anim.rotate_scale);
			Animation alpha_scale_animation = AnimationUtils.loadAnimation(this, R.anim.alpha_scale);
			guide2_1.startAnimation(animationTop);
			alpha_scale_animation.setStartOffset(1000);
			guide2_2.startAnimation(alpha_scale_animation);
			guide2_3.startAnimation(alpha_scale_animation);
			t2_next.startAnimation(animationBottom);
			break;
		case 2:
			Animation translate_animation1 = AnimationUtils.loadAnimation(this, R.anim.translate_in);
			Animation translate_animation2 = AnimationUtils.loadAnimation(this, R.anim.translate_in);
			Animation translate_animation3 = AnimationUtils.loadAnimation(this, R.anim.translate_in);
			translate_animation2.setStartOffset(600);
			translate_animation3.setStartOffset(1200);
			
			guide3_1.startAnimation(translate_animation1);
			guide3_2.startAnimation(translate_animation2);
			guide3_3.startAnimation(translate_animation3);
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO 自动生成的方法存根
					t3_next.setVisibility(View.VISIBLE);
					t3_next.startAnimation(animationBottom);
				}
			}, 3600);
			break;
		case 3:
			guide4_1.startAnimation(animationTop);
			final Animation alpha_animation1 = AnimationUtils.loadAnimation(this, R.anim.alpha_anim);
			final Animation alpha_animation2 = AnimationUtils.loadAnimation(this, R.anim.alpha_anim);
			
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO 自动生成的方法存根
					guide4_2.setVisibility(View.VISIBLE);
					guide4_2.startAnimation(alpha_animation1);
				}
			}, 1000);
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO 自动生成的方法存根
					guide4_3.setVisibility(View.VISIBLE);
					guide4_3.startAnimation(alpha_animation2);
				}
			}, 2000);
			guide4_3.startAnimation(alpha_animation2);
			t4_next.startAnimation(animationBottom);
			break;
		case 4:
			Animation translate_animation5 = AnimationUtils.loadAnimation(this, R.anim.translate_in);
			startBtn.startAnimation(translate_animation5);
			break;
			
		}
	}
	
	private class VerticalFragementPagerAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			// TODO 自动生成的方法存根
			return pagers.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO 自动生成的方法存根
			return arg0==arg1;
		}
		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			container.addView(pagers.get(position));
			return pagers.get(position);

		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
		
		
		
	}
	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		// TODO 自动生成的方法存根
		
	}
	@Override
	public void onPageSelected(int position) {
		// TODO 自动生成的方法存根
		animal(position);
	}
	@Override
	public void onPageScrollStateChanged(int state) {
		// TODO 自动生成的方法存根
		
	}
	
}
