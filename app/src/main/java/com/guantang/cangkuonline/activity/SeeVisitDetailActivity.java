package com.guantang.cangkuonline.activity;

import java.util.Map;

import com.guantang.cangkuonline.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SeeVisitDetailActivity extends Activity implements OnClickListener{
	
	private ImageButton backImgBtn;
	private TextView titleTxtView, commitTxtView;
	private LinearLayout dwnameLayout, dwDetailLayout;
	private TextView dwNameTxtView, lxrTxtView, telTxtView, adressTxtView;
	private EditText lxrEdit, telEdit, contentEdit, pointEdit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addvisit);
		initView();
		init();
	}
	
	public void initView(){
		backImgBtn = (ImageButton) findViewById(R.id.back);
		titleTxtView = (TextView) findViewById(R.id.title);
		commitTxtView = (TextView) findViewById(R.id.commitTxtView);
		dwnameLayout = (LinearLayout) findViewById(R.id.dwnameLayout);
		dwDetailLayout = (LinearLayout) findViewById(R.id.dwDetailLayout);
		dwNameTxtView = (TextView) findViewById(R.id.dwNameTxtView);
		lxrTxtView = (TextView) findViewById(R.id.lxrTxtView);
		telTxtView = (TextView) findViewById(R.id.telTxtView);
		adressTxtView = (TextView) findViewById(R.id.adressTxtView);
		pointEdit = (EditText) findViewById(R.id.pointEdit);
		lxrEdit = (EditText) findViewById(R.id.lxrEdit);
		telEdit = (EditText) findViewById(R.id.telEdit);
		contentEdit = (EditText) findViewById(R.id.contentEdit);

		backImgBtn.setOnClickListener(this);
		commitTxtView.setOnClickListener(this);
		dwDetailLayout.setVisibility(View.GONE);
		
	}
	
	public void init(){
		
		Intent intent = getIntent();
		@SuppressWarnings("unchecked")
		Map<String,Object> map = (Map<String, Object>) intent.getSerializableExtra("visitdetail");
		
		titleTxtView.setText("拜访记录详情");
		commitTxtView.setVisibility(View.INVISIBLE);
		
		
		dwNameTxtView.setText(intent.getStringExtra("dwName"));
		
		Object OppManObject = map.get("OppMan");
		lxrEdit.setText(OppManObject == null || OppManObject.toString().equals("null") ? "" : OppManObject.toString());
		
		Object TelObject = map.get("Tel");
		telEdit.setText(TelObject == null || TelObject.toString().equals("null") ? "" : TelObject.toString());
		
		Object contentObject = map.get("content");
		contentEdit.setText(contentObject == null || contentObject.toString().equals("null") ? "" : contentObject.toString());
		
		Object addrObject = map.get("addr");
		pointEdit.setText(addrObject == null || addrObject.toString().equals("null")?"":addrObject.toString());
		
		lxrEdit.setEnabled(false);
		telEdit.setEnabled(false);
		contentEdit.setEnabled(false);
		pointEdit.setEnabled(false);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.back:
			finish();
			break;
		case R.id.commitTxtView:
			break;
		}
	}
	
}
