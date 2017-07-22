package com.guantang.cangkuonline.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.customview.ControlScrollViewPager;
import com.guantang.cangkuonline.customview.PagerSlidingTabStrip;
import com.guantang.cangkuonline.fragment.CompanyMovedFragment;
import com.guantang.cangkuonline.fragment.CompanyOrderFragment;
import com.guantang.cangkuonline.fragment.VisitRecordFragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomerAndSupplierDetailActivity extends FragmentActivity implements OnClickListener {
	
	private ImageButton backImgBtn;
	private TextView dwTxtView,lxrTxtView,telTxtView,addressTxtView,dwbmTxtView,
					dwTypeTxtView,areaTxtView,swdjhTxtView,emailTxtView,ybTxtView,
					yhTxtView,zhTxtView;
	private ImageView new_modfiyEdit,showImgView;
	private LinearLayout showLayout,moreLayout;
	private PagerSlidingTabStrip mPagerSlidingTabStrip;
	private ControlScrollViewPager mViewpager;
	private MyPagerAdapter mMyPagerAdapter;
	private List<String> titleList = new ArrayList<String>();
	private ArrayList<Fragment> fragmentlist=new ArrayList<Fragment>();
	private Map<String,Object> dwmap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customerandsupplierdetail);
		initView();
		init();
	}
	
	public void initView(){
		backImgBtn = (ImageButton) findViewById(R.id.back);
		dwTxtView = (TextView) findViewById(R.id.dwTxtView);
		lxrTxtView = (TextView) findViewById(R.id.lxrTxtView);
		telTxtView = (TextView) findViewById(R.id.telTxtView);
		addressTxtView = (TextView) findViewById(R.id.addressTxtView);
		dwbmTxtView = (TextView) findViewById(R.id.dwbmTxtView);
		dwTypeTxtView = (TextView) findViewById(R.id.dwTypeTxtView);
		areaTxtView = (TextView) findViewById(R.id.areaTxtView);
		swdjhTxtView = (TextView) findViewById(R.id.swdjhTxtView);
		emailTxtView = (TextView) findViewById(R.id.emailTxtView);
		ybTxtView = (TextView) findViewById(R.id.ybTxtView);
		yhTxtView = (TextView) findViewById(R.id.yhTxtView);
		zhTxtView = (TextView) findViewById(R.id.zhTxtView);
		new_modfiyEdit = (ImageView) findViewById(R.id.new_modfiyEdit);
		showImgView = (ImageView) findViewById(R.id.showImgView);
		mPagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.pagerTabStrip1);
		mViewpager = (ControlScrollViewPager) findViewById(R.id.viewpagercontent1);
		showLayout = (LinearLayout) findViewById(R.id.showLayout);
		moreLayout = (LinearLayout) findViewById(R.id.moreLayout);
		
		backImgBtn.setOnClickListener(this);
		new_modfiyEdit.setOnClickListener(this);
		showLayout.setOnClickListener(this);
		
		mViewpager.setScrollable(false);
	}
	
	public void init(){
		Intent intent = getIntent();
		dwmap = (Map<String, Object>) intent.getSerializableExtra("map");
		
		Object dwNameObject = dwmap.get("dwName");
		dwTxtView.setText(dwNameObject == null || dwNameObject.toString().equals("null") ?"":dwNameObject.toString());
		
		Object lxrObject = dwmap.get("lxr");
		lxrTxtView.setText(lxrObject == null || lxrObject.toString().equals("null")?"":lxrObject.toString());
		
		Object telObject = dwmap.get("tel");
		telTxtView.setText(telObject == null || telObject.toString().equals("null")?"":telObject.toString());
		
		Object addrObject = dwmap.get("addr");
		addressTxtView.setText(addrObject == null || addrObject.toString().equals("null")?"":addrObject.toString());
		
		Object dwbmObject = dwmap.get("py");
		dwbmTxtView.setText(dwbmObject == null || dwbmObject.toString().equals("null")?"":dwbmObject.toString());
		
		Object dwTypeObject = dwmap.get("dwType");
		if(dwTypeObject == null || dwTypeObject.toString().equals("null")){
			dwTypeTxtView.setText("");
		}else{
			int dwTypeNum = Integer.parseInt(dwTypeObject.toString());
			if(dwTypeNum == 1){
				dwTypeTxtView.setText("客户");
			}else if(dwTypeNum == 2){
				dwTypeTxtView.setText("供应商");
			}else if(dwTypeNum == 3){
				dwTypeTxtView.setText("客户、供应商");
			}else{
				dwTypeTxtView.setText("");
			}
		}
		
		Object areaObject = dwmap.get("area");
		areaTxtView.setText(areaObject == null || areaObject.toString().equals("null")?"":areaObject.toString());
		
		Object swdjhObject = dwmap.get("swdjh");
		swdjhTxtView.setText(swdjhObject == null || swdjhObject.toString().equals("null")?"":swdjhObject.toString());
		
		Object emailObject = dwmap.get("email");
		emailTxtView.setText(emailObject == null || emailObject.toString().equals("null")?"":emailObject.toString());
		
		Object ybObject = dwmap.get("yb");
		ybTxtView.setText(ybObject == null || ybObject.toString().equals("null")?"":ybObject.toString());
		
		Object yhObject = dwmap.get("yh");
		yhTxtView.setText(yhObject == null || yhObject.toString().equals("null")?"":yhObject.toString());
		
		Object zhObject = dwmap.get("zh");
		zhTxtView.setText(ybObject == null || zhObject.toString().equals("null")?"":zhObject.toString());
		
		titleList.add("拜访记录");
		titleList.add("历史订单");
		titleList.add("出入库明细");
		
		fragmentlist.add(VisitRecordFragment.getInstance(dwmap.get("id").toString(),dwTxtView.getText().toString()));
		fragmentlist.add(CompanyOrderFragment.getInstance(dwmap.get("id").toString()));
		fragmentlist.add(CompanyMovedFragment.getInstance(dwmap.get("id").toString()));
		
		mViewpager.setOffscreenPageLimit(3);
		mMyPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
		mViewpager.setAdapter(mMyPagerAdapter);
		mPagerSlidingTabStrip.setViewPager(mViewpager);
		setPageTitlesValue();
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
		// TODO Auto-generated method stub
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
		case R.id.new_modfiyEdit:
			Intent intent = new Intent(this,EditCustomerAndSupplierActivity.class);
			intent.putExtra("dwmap", (Serializable)dwmap);
			startActivity(intent);
			finish();
			break;
		}
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
	
}
