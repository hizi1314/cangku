package com.guantang.cangkuonline.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.customview.ControlScrollViewPager;
import com.guantang.cangkuonline.customview.PagerSlidingTabStrip;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.fragment.AllOrderFragment;
import com.guantang.cangkuonline.fragment.DDMXFragment;
import com.guantang.cangkuonline.fragment.DDprogressFragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class DDdetailActivity extends FragmentActivity implements OnClickListener{
	private ImageButton backImgBtn;
	private View tablineView;
	private TextView ddhTxtView,dwtitleTxtView,dwnameTxtView,sqdateTxtView,yqdhsjTxtView,
			statusTxtView,depTxtView,sqrTxtView,lxrTxtView,lxphoneTxtView,zjeTxtView,yfjeTxtView,
			syjeTxtView,bzTxtView,leftTxtView,rightTxtView;
	private LinearLayout moreLayout,showLayout;
	private ImageView showImgView;
//	private PagerSlidingTabStrip mPagerSlidingTabStrip;
	private ControlScrollViewPager mControlScrollViewPager;
	private List<Fragment> fragmentlist=new ArrayList<Fragment>();
	private MyAdapter mMyAdapter;
	private int width;
	private int selectTabNum = 0; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dd_detail);
		initView();
		init();
	}
	
	public void initView(){
		backImgBtn = (ImageButton) findViewById(R.id.back);
		ddhTxtView = (TextView) findViewById(R.id.ddhTxtView);
		dwtitleTxtView = (TextView) findViewById(R.id.dwtitleTxtView);
		dwnameTxtView = (TextView) findViewById(R.id.dwnameTxtView);
		sqdateTxtView = (TextView) findViewById(R.id.sqdateTxtView);
		yqdhsjTxtView = (TextView) findViewById(R.id.yqdhsjTxtView);
		statusTxtView = (TextView) findViewById(R.id.statusTxtView);
		depTxtView = (TextView) findViewById(R.id.depTxtView);
		sqrTxtView = (TextView) findViewById(R.id.sqrTxtView);
		lxrTxtView = (TextView) findViewById(R.id.lxrTxtView);
		lxphoneTxtView = (TextView) findViewById(R.id.lxphoneTxtView);
		zjeTxtView = (TextView) findViewById(R.id.zjeTxtView);
		yfjeTxtView = (TextView) findViewById(R.id.yfjeTxtView);
		syjeTxtView = (TextView) findViewById(R.id.syjeTxtView);
		bzTxtView = (TextView) findViewById(R.id.bzTxtView);
		moreLayout = (LinearLayout) findViewById(R.id.moreLayout);
		showLayout = (LinearLayout) findViewById(R.id.showLayout);
		showImgView = (ImageView) findViewById(R.id.showImgView);
		leftTxtView = (TextView) findViewById(R.id.leftTxtView);
		rightTxtView = (TextView) findViewById(R.id.rightTxtView);
		tablineView = findViewById(R.id.tablineView);
//		mPagerSlidingTabStrip =(PagerSlidingTabStrip) findViewById(R.id.pagerTabStrip1);
		mControlScrollViewPager = (ControlScrollViewPager) findViewById(R.id.viewpagercontent1);
		
		backImgBtn.setOnClickListener(this);
		showLayout.setOnClickListener(this);
		leftTxtView.setOnClickListener(this);
		rightTxtView.setOnClickListener(this);
		mControlScrollViewPager.setScrollable(false);
		
		DisplayMetrics mDisplayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
		width = mDisplayMetrics.widthPixels;
		int hight = mDisplayMetrics.heightPixels;
		
		LinearLayout.LayoutParams lps = new LinearLayout.LayoutParams(width/2,dp2px(2));
		tablineView.setLayoutParams(lps);
		
	}
	
	public void init(){
		Intent intent = getIntent();
		Map<String,Object> map =(Map<String, Object>) intent.getSerializableExtra("map");
		
		Object ddhObject = map.get(DataBaseHelper.OrderIndex);
		ddhTxtView.setText(ddhObject == null || ddhObject.equals("null") ? "":ddhObject.toString());
		
		String dirc = map.get(DataBaseHelper.dirc).toString();
		if(dirc.equals("0")){
			dwtitleTxtView.setText("供应商：");
		}else if(dirc.equals("1")){
			dwtitleTxtView.setText("客户名称：");
		}
		Object dwnameObject = map.get(DataBaseHelper.DWName);
		dwnameTxtView.setText(dwnameObject == null || dwnameObject.equals("null") ?"":dwnameObject.toString());
		
		String sqdate = map.get(DataBaseHelper.Sqdt).toString().replace("T", " ").substring(0, 19);
		sqdateTxtView.setText(sqdate);
		String reqdate = map.get(DataBaseHelper.ReqDate).toString().replace("T", " ");
		yqdhsjTxtView.setText(reqdate);
		
		String statusString = map.get(DataBaseHelper.Status).toString();
		if(statusString != null ){
			if(statusString.equals("0")){
				statusTxtView.setText("待审核");
			}else if(statusString.equals("1")){
				statusTxtView.setText("被驳回");
			}else if(statusString.equals("2")||statusString.equals("3")){
				statusTxtView.setText("待执行");
			}else if(statusString.equals("5")||statusString.equals("7")){
				statusTxtView.setText("已完成");
			}
		}
		
		Object depNameObject = map.get(DataBaseHelper.DepName);
		depTxtView.setText(depNameObject == null || depNameObject.equals("null") ? "":depNameObject.toString());
		
		Object sqrObject = map.get(DataBaseHelper.sqr);
		sqrTxtView.setText(sqrObject == null || sqrObject.equals("null") ? "":sqrObject.toString());
		
		Object lxrObject = map.get(DataBaseHelper.LXR);
		lxrTxtView.setText(lxrObject == null || lxrObject.equals("null") ? "":lxrObject.toString());
		
		Object lxphoneObject = map.get(DataBaseHelper.TEL);
		lxphoneTxtView.setText(lxphoneObject == null || lxphoneObject.equals("null") ? "":lxphoneObject.toString());
		
		Object zjeObject = map.get(DataBaseHelper.zje);
		zjeTxtView.setText(zjeObject == null || zjeObject.equals("null") ?"":zjeObject.toString());
		
		Object yfjeObject = map.get(DataBaseHelper.yfje);
		yfjeTxtView.setText(yfjeObject == null || yfjeObject.equals("null") ?"":yfjeObject.toString());
		
		Object syjeObject = map.get(DataBaseHelper.syje);
		syjeTxtView.setText(syjeObject == null || syjeObject.equals("null")?"":syjeObject.toString());
		
		Object bzObject = map.get(DataBaseHelper.BZ);
		bzTxtView.setText(bzObject == null || bzObject.equals("null")?"":bzObject.toString());
		
		
		Object orderid = map.get(DataBaseHelper.ID);
		fragmentlist.add(DDprogressFragment.getInstance(orderid == null || orderid.equals("null") ?"":orderid.toString()));
		fragmentlist.add(DDMXFragment.getInstance(orderid == null || orderid.equals("null") ?"":orderid.toString(),dirc));
		
		mMyAdapter = new MyAdapter(getSupportFragmentManager());
		mControlScrollViewPager.setAdapter(mMyAdapter);
		mControlScrollViewPager.setOffscreenPageLimit(2);
		
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch(v.getId()){
		case R.id.back:
			finish();
			break;
		case R.id.showLayout:
			if(!moreLayout.isShown()){
				moreLayout.setVisibility(View.VISIBLE);
				showImgView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.upbtn));
			}else{
				moreLayout.setVisibility(View.GONE);
				showImgView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.downbtn));
			}
			break;
		case R.id.leftTxtView:
			// 参数1～2：x轴的开始位置
			// 参数3～4：x轴的结束位置
			// 参数5～6：y轴的开始位置
			// 参数7～8：y轴的结束位置
			if(selectTabNum == 1){
				TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF,0f,
						Animation.RELATIVE_TO_SELF, 0f,Animation.RELATIVE_TO_SELF, 0f);
				translateAnimation.setDuration(200);
				translateAnimation.setFillAfter(true);
				selectTabNum = 0;
				tablineView.startAnimation(translateAnimation);
				leftTxtView.setTextColor(Color.parseColor("#f5b53a"));
				rightTxtView.setTextColor(Color.parseColor("#333333"));
				mControlScrollViewPager.setCurrentItem(0);
			}
			break;
		case R.id.rightTxtView:
			if(selectTabNum == 0){
				TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,1f,
						Animation.RELATIVE_TO_SELF, 0f,Animation.RELATIVE_TO_SELF, 0f);
				translateAnimation.setDuration(200);
				translateAnimation.setFillAfter(true);
				selectTabNum = 1;
				tablineView.startAnimation(translateAnimation);
				leftTxtView.setTextColor(Color.parseColor("#333333"));
				rightTxtView.setTextColor(Color.parseColor("#f5b53a"));
				mControlScrollViewPager.setCurrentItem(1);
			}
			break;
		}
	}
	
	class MyAdapter extends FragmentPagerAdapter{

		public MyAdapter(FragmentManager fm){
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO 自动生成的方法存根
			return fragmentlist.get(arg0);
		}

		@Override
		public int getCount() {
			// TODO 自动生成的方法存根
			return fragmentlist.size();
		}
		
		
	}
	
	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}
	
}
