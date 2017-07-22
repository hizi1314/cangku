package com.guantang.cangkuonline.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.application.MyApplication;
import com.guantang.cangkuonline.customview.PagerSlidingTabStrip;
import com.guantang.cangkuonline.fragment.HpCkkcFragment;
import com.guantang.cangkuonline.fragment.HpInformationFragment;
import com.guantang.cangkuonline.fragment.HpMovedFragment;
import com.guantang.cangkuonline.fragment.PiCiFragment;
import com.guantang.cangkuonline.helper.RightsHelper;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;

public class HpInformationActivity extends FragmentActivity implements OnClickListener,OnPageChangeListener{
	
	private ImageButton backImgBtn;
	private TextView modfiyTextView;
	private PagerSlidingTabStrip mPagerSlidingTabStrip;
	private ViewPager mViewPager;
	private SharedPreferences mSharedPreferences;
	private String hpid = "",hpmc = "",hpbm = "",ggxh = "",jldw = "",lb = "";
	private List<String> titleList = new ArrayList<String>();
	private ArrayList<Fragment> fragmentlist=new ArrayList<Fragment>();
	private MyPagerAdapter mMyPagerAdapter;
	
	/**
	 * 当获取数据后，把把计量单位和货品类别传递给货品明细fragment
	 * */
	public interface TransmitJldwAndLb{
		public abstract void execute(String jldw,String lb);
	};
	private TransmitJldwAndLb mTransmitJldwAndLb;
	public void setTransmitJldwAndLb(TransmitJldwAndLb transmitJldwAndLb){
		mTransmitJldwAndLb = transmitJldwAndLb;
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hpinformation);
		mSharedPreferences = MyApplication.getInstance().getSharedPreferences();
		Intent intent = getIntent();
		hpid = intent.getStringExtra("id");
		hpmc = intent.getStringExtra("hpmc");
		hpbm = intent.getStringExtra("hpbm");
		ggxh = intent.getStringExtra("ggxh");
		initView();
	}
	
	@Override
	protected void onStart() {
		// TODO 自动生成的方法存根
		super.onStart();
		titleList.clear();
		fragmentlist.clear();
		init();
		setPageTitlesValue();
	}
	
	public void initView(){
		backImgBtn = (ImageButton) findViewById(R.id.back);
		modfiyTextView = (TextView) findViewById(R.id.mod);
		mPagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.pagerTabStrip1);
		mViewPager = (ViewPager) findViewById(R.id.viewpagercontent1);
		
		backImgBtn.setOnClickListener(this);
		modfiyTextView.setOnClickListener(this);
		mViewPager.setOnPageChangeListener(this);
	}
	
	public void init(){
		titleList.add("货品信息");
		titleList.add("库存信息");
		titleList.add("出入库明细");
		titleList.add("相关批次");
		
		fragmentlist.add(HpInformationFragment.getInstance(hpid));
		fragmentlist.add(HpCkkcFragment.getInstance(hpid));
		fragmentlist.add(HpMovedFragment.getInstance(hpid,hpmc,hpbm,ggxh));
		fragmentlist.add(new PiCiFragment());
		
		mViewPager.setOffscreenPageLimit(4);
		mMyPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mMyPagerAdapter);
		mPagerSlidingTabStrip.setViewPager(mViewPager);
	}
	
	/**
	 * 设置Titile相关属性
	 */
	private void setPageTitlesValue() {
		DisplayMetrics dm = getResources().getDisplayMetrics();
		// 设置为true 均匀分配title位置
		mPagerSlidingTabStrip.setShouldExpand(true);
		
		mPagerSlidingTabStrip.setDividerColor(Color.TRANSPARENT);

		//(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, dm)  将数值1转成dp为单位
		//设置下划分割线高度
		mPagerSlidingTabStrip.setUnderlineHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, dm)); 

		//设置指示条高条
		mPagerSlidingTabStrip.setIndicatorHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, dm));

		//设置文本字大小
		mPagerSlidingTabStrip.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, dm));

		//设置指示条颜色
		mPagerSlidingTabStrip.setIndicatorColor(Color.parseColor("#f5b53a"));

		//设置选中文本颜色
		mPagerSlidingTabStrip.setSelectedTextColor(Color.parseColor("#f5b53a"));

		//设置Title背景颜色
		mPagerSlidingTabStrip.setTabBackground(0);//android.R.color.darker_gray
	}
	
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		Intent intent = new Intent();
		switch(v.getId()){
		case R.id.back:
			finish();
			break;
		case R.id.mod:
			if(mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1)==1){
				if (RightsHelper.isHaveRight(RightsHelper.hp_addedit,mSharedPreferences) == true){
					intent.setClass(HpInformationActivity.this, ModifyHPActivity.class);
					intent.putExtra("hpid", hpid);
					startActivity(intent);
				}else{
					Toast.makeText(HpInformationActivity.this, "对不起，你没有的添加、修改货品的权限",
							Toast.LENGTH_SHORT).show();
				}
			}else{
				Toast.makeText(this, "离线模式下不能修改货品", Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}
	
	public void setJldw_Lb(String mJldw,String mLb){
		if(mJldw!=null){
			this.jldw = jldw;
		}
		if(mLb!=null){
			this.lb = mLb;
		}
	}
	
	public String getJldw(){
		return jldw;
	}
	
	public String getLb(){
		return lb;
	}
	
	class MyPagerAdapter extends FragmentPagerAdapter{

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO 自动生成的构造函数存根
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO 自动生成的方法存根
			return fragmentlist.get(arg0);
		}
		
		@Override
		public CharSequence getPageTitle(int position) {
			// TODO 自动生成的方法存根
			return titleList.get(position);
		}

		@Override
		public int getCount() {
			// TODO 自动生成的方法存根
			return fragmentlist.size();
		}
		
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
		if(arg0==2){
			mTransmitJldwAndLb.execute(jldw, lb);
		}
	}
	
}
