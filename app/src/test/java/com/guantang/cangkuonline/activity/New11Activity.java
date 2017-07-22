package com.guantang.cangkuonline.activity;

import java.net.URL;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.application.MyApplication;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;
import com.guantang.cangkuonline.startmarket.MarketUtils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

public class New11Activity extends Activity{
	private ImageButton backBtn,mImgeButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new11);
		backBtn = (ImageButton) findViewById(R.id.back);
		mImgeButton = (ImageButton) findViewById(R.id.gotopayImgBtn);
		backBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if(MyApplication.getInstance().getSharedPreferences().getInt(ShareprefenceBean.IS_LOGIN, -1)==-1){
					Intent intent = new Intent(New11Activity.this,NewLoginActivity.class);
					startActivity(intent);
					finish();
				}else{
					finish();
				}
			}
		});
		mImgeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Uri uri = Uri.parse("https://item.taobao.com/item.htm?spm=a230r.7195193.1997079397.8.7PjwI0&id=38913545614&abbucket=19");
				Intent intent = new Intent(Intent.ACTION_VIEW,uri);
				startActivity(intent);
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(MyApplication.getInstance().getSharedPreferences().getInt(ShareprefenceBean.IS_LOGIN, -1)==-1){
				Intent intent = new Intent(this,NewLoginActivity.class);
				startActivity(intent);
				finish();
			}else{
				finish();
			}
			
		}
		return super.onKeyDown(keyCode, event);
	}
}
