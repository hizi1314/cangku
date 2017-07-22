package com.guantang.cangkuonline.activity;

//import com.umeng.fb.FeedbackAgent;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.application.MyApplication;
import com.umeng.fb.FeedbackAgent;

public class Helper extends Activity {
	TextView phone, ver;
	FeedbackAgent agent;
	Button fb;
	ImageButton back, share;
	ImageButton fb2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.helper);
		phone = (TextView) findViewById(R.id.phone);
		ver = (TextView) findViewById(R.id.ver);
		back = (ImageButton) findViewById(R.id.back);

		share = (ImageButton) findViewById(R.id.share);
		SpannableString num1 = new SpannableString("400-028-0130");
		// num1.setSpan(new StyleSpan(Typeface.BOLD), 0, 5,
		// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		num1.setSpan(new URLSpan("tel:400-028-0130"), 0, 12,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		// num1.setSpan(new UnderlineSpan(), 0, num1.length(),
		// Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		// num2.setSpan(new UnderlineSpan(), 0, num2.length(),
		// Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		phone.setText(num1);
		phone.setMovementMethod(LinkMovementMethod.getInstance());
		ver.setText(MyApplication.getInstance().getVisionName());
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				finish();
			}
		});
	}
}
