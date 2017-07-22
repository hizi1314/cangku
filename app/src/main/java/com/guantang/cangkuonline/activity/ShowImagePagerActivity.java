package com.guantang.cangkuonline.activity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.JellyViewPager.PictureFragment;
import com.guantang.cangkuonline.JellyViewPager.TestFragPagerAdapter;
import com.guantang.cangkuonline.adapter.NetPicFragPagerAdapter;
import com.guantang.cangkuonline.application.MyApplication;
import com.guantang.cangkuonline.eventbusBean.NumberBitmap;
import com.guantang.cangkuonline.helper.TemporarilyImageBean;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;
import com.guantang.cangkuonline.static_constant.PathConstant;
import com.guantang.cangkuonline.webservice.WebserviceImport;

import de.greenrobot.event.EventBus;
import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.LruCache;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ShowImagePagerActivity extends FragmentActivity implements OnClickListener,OnPageChangeListener{
	private ViewPager mJellyViewPager;
//	private Button preBtn,nextBtn;
	private ImageButton backImgBtn;
	private TextView titleTextView;
	private List<String> ImageNameList = new ArrayList<String>();
	private TestFragPagerAdapter testFragPagerAdapter;
	private NetPicFragPagerAdapter netPicFragPagerAdapter;
	private String nowShowImage = "";
	private int ShowNumber=0;
	private int height=0,width=0;
	private String hpid = "";
	private ExecutorService cacheThreadpool = Executors.newCachedThreadPool();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_showimagepager);
		overridePendingTransition(R.anim.center_in, R.anim.center_out);
		
		initView();
		init();
	}
	
	@Override
	protected void onDestroy() {
		// TODO 自动生成的方法存根
		super.onDestroy();
	}
	
	private void initView(){
		mJellyViewPager = (ViewPager) this.findViewById(R.id.myViewPager1);
		backImgBtn = (ImageButton) this.findViewById(R.id.back);
		titleTextView =(TextView) this.findViewById(R.id.title);
//		preBtn = (Button) this.findViewById(R.id.preBtn);
//		nextBtn = (Button) this.findViewById(R.id.nextBtn);
		
		backImgBtn.setOnClickListener(this);
		titleTextView.setOnClickListener(this);
//		preBtn.setOnClickListener(this);
//		nextBtn.setOnClickListener(this);
		mJellyViewPager.setOnPageChangeListener(this);
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels;
		height = dm.heightPixels;
	}
	
	@SuppressWarnings("deprecation")
	private void init(){
		Intent intent = getIntent();
		hpid = intent.getStringExtra("hpid");
		ImageNameList = (List<String>) intent.getSerializableExtra("ImageNameList");
		nowShowImage = intent.getStringExtra("nowShowImage");
		
		if(!nowShowImage.isEmpty() && nowShowImage!=null){
			for(int i=0;i<ImageNameList.size();i++){
				if(ImageNameList.get(i).equals(nowShowImage)){
					ShowNumber=i;
					break;
				}
			}
		}
		if(intent.getIntExtra("LocalOrNet", -1)==1){
			netPicFragPagerAdapter = new NetPicFragPagerAdapter(this,hpid, getSupportFragmentManager(), ImageNameList, width, height);
			mJellyViewPager.setAdapter(netPicFragPagerAdapter);
			mJellyViewPager.setCurrentItem(ShowNumber);
			mJellyViewPager.setOffscreenPageLimit(6);
		}else if(intent.getIntExtra("LocalOrNet", -1)==-1){
			testFragPagerAdapter = new TestFragPagerAdapter(this,getSupportFragmentManager(),ImageNameList,width,height);
			mJellyViewPager.setAdapter(testFragPagerAdapter);
			mJellyViewPager.setCurrentItem(ShowNumber);
			mJellyViewPager.setOffscreenPageLimit(6);
		}
		titleTextView.setText((ShowNumber+1)+"/"+ImageNameList.size());
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		int currentItem=mJellyViewPager.getCurrentItem();
		switch(v.getId()){
		case R.id.back:
			finish();
			overridePendingTransition(R.anim.center_in, R.anim.center_out);
			break;
//		case R.id.preBtn:
//			if(currentItem>0){
//				mJellyViewPager.setCurrentItem(currentItem-1);
//			}else{
//				Toast.makeText(this, "没有上一页了", Toast.LENGTH_SHORT).show();
//			}
//			
//			break;
//		case R.id.nextBtn:
//			if(currentItem<ImageNameList.size()-1){
//				mJellyViewPager.setCurrentItem(currentItem+1);
//			}else{
//				Toast.makeText(this, "没有下一页了", Toast.LENGTH_SHORT).show();
//			}
//			break;
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
		titleTextView.setText((position+1)+"/"+ImageNameList.size());
	}
	@Override
	public void onPageScrollStateChanged(int state) {
		// TODO 自动生成的方法存根
		switch(state){
		case 1: //正在滑动
			break;
		case 2: //滑动结束
			break;
		}
	}
	@Override
	public void onBackPressed() {
		// TODO 自动生成的方法存根
		super.onBackPressed();
		overridePendingTransition(R.anim.center_in, R.anim.center_out);
	}
	
	
	class NumberBitmapBase64{
		private int picNumber;
		private String imageNameString;
		private String base64;
		public int getPicNumber() {
			return picNumber;
		}
		public void setPicNumber(int picNumber) {
			this.picNumber = picNumber;
		}
		public String getImageNameString() {
			return imageNameString;
		}
		public void setImageNameString(String imageNameString) {
			this.imageNameString = imageNameString;
		}
		public String getBase64() {
			return base64;
		}
		public void setBase64(String base64) {
			this.base64 = base64;
		}
		
	}
}
