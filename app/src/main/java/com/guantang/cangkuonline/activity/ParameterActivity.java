package com.guantang.cangkuonline.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.guantang.cangkuonline.R;

public class ParameterActivity extends Activity implements OnClickListener {
	private ImageButton back;
	private LinearLayout layout1, layout2, layout3, layout4, layout5,layout6; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.parameter);
		initControl();
	}

	public void initControl() {
		back = (ImageButton) findViewById(R.id.back);
		layout1 = (LinearLayout) findViewById(R.id.res_layout);
		layout2 = (LinearLayout) findViewById(R.id.ruku_layout);
		layout3 = (LinearLayout) findViewById(R.id.chuku_layout);
		layout4 = (LinearLayout) findViewById(R.id.dep_layout);
		layout5 = (LinearLayout) findViewById(R.id.hplb_layout);
		layout6 = (LinearLayout) findViewById(R.id.ck_layout);
		back.setOnClickListener(this);
		layout1.setOnClickListener(this);
		layout2.setOnClickListener(this);
		layout3.setOnClickListener(this);
		layout4.setOnClickListener(this);
		layout5.setOnClickListener(this);
		layout6.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent i = new Intent();
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.res_layout:
			i.setClass(this, Parameter_PrefActivity.class);
			startActivity(i);
			break;
		case R.id.ruku_layout:
			i.setClass(this, SettingChuRuTypeActivity.class);
			i.putExtra("type", 1);
			startActivity(i);
			break;
		case R.id.chuku_layout:
			i.setClass(this, SettingChuRuTypeActivity.class);
			i.putExtra("type", 2);
			startActivity(i);
			break;
		case R.id.dep_layout:
			i.setClass(this, AddDepActivity.class);
			startActivity(i);
			break;
		case R.id.hplb_layout:
			i.setClass(this, AddLBActivity.class);
			startActivity(i);
			break;
		case R.id.ck_layout:
			i.setClass(this, CKListActivity.class);
			startActivity(i);
			break;
		default:
			break;
		}
	}
}
