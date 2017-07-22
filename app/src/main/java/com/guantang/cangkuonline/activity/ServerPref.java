package com.guantang.cangkuonline.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;

public class ServerPref extends Activity implements OnClickListener{
	private ImageView back;
	private Button save,recover;
	private EditText sername1,sername2,sername3,sername4,url1,url2,url3,url4;
	private SharedPreferences mSharedPreferences;
	private String[] ss=new String[]{"www3.gtcangku.com","www.gtcangku.com","www2.gtcangku.com","test3.gtcangku.com"};
	private String[] ss2=new String[]{"上海服务器","广东服务器","北京服务器","其他服务器"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.server_pref);
		mSharedPreferences = this.getSharedPreferences(
				ShareprefenceBean.SHAREPREFENCE_NAME, Context.MODE_PRIVATE);
		initControl();
		init();
	}
	private void initControl(){
		back=(ImageView) this.findViewById(R.id.back);
		save=(Button) this.findViewById(R.id.save);
		recover=(Button) this.findViewById(R.id.recover);
		sername1= (EditText) this.findViewById(R.id.sername1);
		sername2= (EditText) this.findViewById(R.id.sername2);
		sername3= (EditText) this.findViewById(R.id.sername3);
		sername4= (EditText) this.findViewById(R.id.sername4);
		url1= (EditText) this.findViewById(R.id.url1);
		url2= (EditText) this.findViewById(R.id.url2);
		url3= (EditText) this.findViewById(R.id.url3);
		url4= (EditText) this.findViewById(R.id.url4);
		back.setOnClickListener(this);
		save.setOnClickListener(this);
		recover.setOnClickListener(this);
	}
	private void init(){
		url1.setText(mSharedPreferences.getString(ShareprefenceBean.SERVICE_IDN_URL1, ss[0]));
		url2.setText(mSharedPreferences.getString(ShareprefenceBean.SERVICE_IDN_URL2, ss[1]));
		url3.setText(mSharedPreferences.getString(ShareprefenceBean.SERVICE_IDN_URL3, ss[2]));
		url4.setText(mSharedPreferences.getString(ShareprefenceBean.IDN_ALONE_URL, ss[3]));
		
		sername1.setText(ss2[0]);
		sername2.setText(ss2[1]);
		sername3.setText(ss2[2]);
		sername4.setText(ss2[3]);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent i=new Intent();
		switch(v.getId()){
		case R.id.back:
			finish();
			break;
		case R.id.save:
			mSharedPreferences.edit().putString(ShareprefenceBean.SERVICE_IDN_URL1, url1.getText().toString().trim()).commit();
			mSharedPreferences.edit().putString(ShareprefenceBean.SERVICE_IDN_URL2, url2.getText().toString().trim()).commit();
			mSharedPreferences.edit().putString(ShareprefenceBean.SERVICE_IDN_URL3, url3.getText().toString().trim()).commit();
			mSharedPreferences.edit().putString(ShareprefenceBean.IDN_ALONE_URL, url4.getText().toString()).commit();
			
			mSharedPreferences.edit().putString(ShareprefenceBean.SERVICE_NAME1, sername1.getText().toString().trim()).commit();
			mSharedPreferences.edit().putString(ShareprefenceBean.SERVICE_NAME2, sername2.getText().toString().trim()).commit();
			mSharedPreferences.edit().putString(ShareprefenceBean.SERVICE_NAME3, sername3.getText().toString().trim()).commit();
			mSharedPreferences.edit().putString(ShareprefenceBean.ALONE_SERVICE_NAME, sername4.getText().toString()).commit();
			Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
			break;
		case R.id.recover:
			mSharedPreferences.edit().putString(ShareprefenceBean.SERVICE_IDN_URL1, ss[0]).commit();
			mSharedPreferences.edit().putString(ShareprefenceBean.SERVICE_IDN_URL2, ss[1]).commit();
			mSharedPreferences.edit().putString(ShareprefenceBean.SERVICE_IDN_URL3, ss[2]).commit();
			mSharedPreferences.edit().putString(ShareprefenceBean.IDN_ALONE_URL, ss[3]).commit();
			
			mSharedPreferences.edit().putString(ShareprefenceBean.SERVICE_NAME1, ss2[0]).commit();
			mSharedPreferences.edit().putString(ShareprefenceBean.SERVICE_NAME2, ss2[1]).commit();
			mSharedPreferences.edit().putString(ShareprefenceBean.SERVICE_NAME3, ss2[2]).commit();
			mSharedPreferences.edit().putString(ShareprefenceBean.ALONE_SERVICE_NAME, ss2[3]).commit();
			url1.setText(ss[0]);
			url2.setText(ss[1]);
			url3.setText(ss[2]);
			url4.setText(ss[3]);
			sername1.setText(ss2[0]);
			sername2.setText(ss2[1]);
			sername3.setText(ss2[2]);
			sername4.setText(ss2[3]);
			Toast.makeText(this, "恢复成功", Toast.LENGTH_SHORT).show();
			break;
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){
		    finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
