package com.guantang.cangkuonline.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.guantang.cangkuonline.R;

public class Helper_user extends Activity implements OnClickListener{
	private ImageButton back;
	private TextView pro1,pro2,pro3,pro4,pro5,pro6,pro7,pro8;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.helper_user2);
		initControl();
	}
	public void initControl(){
		back=(ImageButton) findViewById(R.id.back);
		pro1=(TextView) this.findViewById(R.id.pro1);
		pro2=(TextView) this.findViewById(R.id.pro2);
		pro3=(TextView) this.findViewById(R.id.pro3);
		pro4=(TextView) this.findViewById(R.id.pro4);
		pro5=(TextView) this.findViewById(R.id.pro5);
		pro6=(TextView) this.findViewById(R.id.pro6);
		pro7=(TextView) this.findViewById(R.id.pro7);
		pro8=(TextView) this.findViewById(R.id.pro8);
		pro1.setOnClickListener(this);
		pro2.setOnClickListener(this);
		pro3.setOnClickListener(this);
		pro4.setOnClickListener(this);
		pro5.setOnClickListener(this);
		pro6.setOnClickListener(this);
		pro7.setOnClickListener(this);
		pro8.setOnClickListener(this);
		back.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent=new Intent();
		switch(v.getId()){
		case R.id.back:
			finish();
			break;
		case R.id.pro1:
			intent.setClass(this,QuestionHelperActivity.class);
			intent.putExtra("startfrom", 1);
			startActivity(intent);
			break;
		case R.id.pro2:
			intent.setClass(this,QuestionHelperActivity.class);
			intent.putExtra("startfrom", 2);
			startActivity(intent);
			break;
		case R.id.pro3:
			intent.setClass(this,QuestionHelperActivity.class);
			intent.putExtra("startfrom", 3);
			startActivity(intent);
			break;
		case R.id.pro4:
			intent.setClass(this,QuestionHelperActivity.class);
			intent.putExtra("startfrom", 4);
			startActivity(intent);
			break;
		case R.id.pro5:
			intent.setClass(this,QuestionHelperActivity.class);
			intent.putExtra("startfrom", 5);
			startActivity(intent);
			break;
		case R.id.pro6:
			intent.setClass(this,QuestionHelperActivity.class);
			intent.putExtra("startfrom", 6);
			startActivity(intent);
			break;
		case R.id.pro7:
			intent.setClass(this,QuestionHelperActivity.class);
			intent.putExtra("startfrom", 7);
			startActivity(intent);
			break;
		case R.id.pro8:
			intent.setClass(this,QuestionHelperActivity.class);
			intent.putExtra("startfrom", 8);
			startActivity(intent);
			break;
			default:
				break;
		}
	}
	
}
