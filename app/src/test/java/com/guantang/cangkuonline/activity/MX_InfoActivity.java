package com.guantang.cangkuonline.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.database.DataBaseHelper;

public class MX_InfoActivity extends Activity {
	private TextView mTextView1, mTextView2, mTextView3, mTextView4,
			mTextView5, mTextView6, mTextView7, mTextView8, mTextView9,
			mTextView10, mTextView11, mTextView12, mTextView13, mTextView14,
			mTextView15, mTextView16,mTextView17;
	private TextView dhtext, typetext, datetext, cktext, bmtext, mctext,
			ggtext, jldwtext, lbtext, numtext, djtext, zjtext, kctext, deptext,
			dwtext, jbrtext,bztext;
	private int width, hight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mxinfo);
		initControl();
		init();
		this.setFinishOnTouchOutside(true);
	}

	public void initControl() {
		dhtext = (TextView) findViewById(R.id.dhtext);
		typetext = (TextView) findViewById(R.id.typetext);
		datetext = (TextView) findViewById(R.id.datetext);
		cktext = (TextView) findViewById(R.id.cktext);
		bmtext = (TextView) findViewById(R.id.bmtext);
		mctext = (TextView) findViewById(R.id.mctext);
		ggtext = (TextView) findViewById(R.id.ggtext);
		jldwtext = (TextView) findViewById(R.id.jldwtext);
		lbtext = (TextView) findViewById(R.id.lbtext);
		numtext = (TextView) findViewById(R.id.numtext);
		djtext = (TextView) findViewById(R.id.djtext);
		zjtext = (TextView) findViewById(R.id.zjtext);
		kctext = (TextView) findViewById(R.id.kctext);
		deptext = (TextView) findViewById(R.id.deptext);
		dwtext = (TextView) findViewById(R.id.dwtext);
		jbrtext = (TextView) findViewById(R.id.jbrtext);
		bztext = (TextView) findViewById(R.id.bztext);

		mTextView1 = (TextView) findViewById(R.id.text1);
		mTextView2 = (TextView) findViewById(R.id.text2);
		mTextView3 = (TextView) findViewById(R.id.text3);
		mTextView4 = (TextView) findViewById(R.id.text4);
		mTextView5 = (TextView) findViewById(R.id.text5);
		mTextView6 = (TextView) findViewById(R.id.text6);
		mTextView7 = (TextView) findViewById(R.id.text7);
		mTextView8 = (TextView) findViewById(R.id.text8);
		mTextView9 = (TextView) findViewById(R.id.text9);
		mTextView10 = (TextView) findViewById(R.id.text10);
		mTextView11 = (TextView) findViewById(R.id.text11);
		mTextView12 = (TextView) findViewById(R.id.text12);
		mTextView13 = (TextView) findViewById(R.id.text13);
		mTextView14 = (TextView) findViewById(R.id.text14);
		mTextView15 = (TextView) findViewById(R.id.text15);
		mTextView16 = (TextView) findViewById(R.id.text16);
		mTextView17 = (TextView) findViewById(R.id.text17);

		DisplayMetrics dms = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dms);
		width = dms.widthPixels;
		hight = dms.heightPixels;
		LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
				width / 6, LayoutParams.WRAP_CONTENT);
		LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(
				width / 3, LayoutParams.WRAP_CONTENT);
		mTextView1.setLayoutParams(lp1);
		mTextView2.setLayoutParams(lp1);
		mTextView3.setLayoutParams(lp1);
		mTextView4.setLayoutParams(lp1);
		mTextView5.setLayoutParams(lp1);
		mTextView6.setLayoutParams(lp1);
		mTextView7.setLayoutParams(lp1);
		mTextView8.setLayoutParams(lp1);
		mTextView9.setLayoutParams(lp1);
		mTextView10.setLayoutParams(lp1);
		mTextView11.setLayoutParams(lp1);
		mTextView12.setLayoutParams(lp1);
		mTextView13.setLayoutParams(lp1);
		mTextView14.setLayoutParams(lp1);
		mTextView15.setLayoutParams(lp1);
		mTextView16.setLayoutParams(lp1);
		mTextView17.setLayoutParams(lp1);
		
		dhtext.setLayoutParams(lp2);
		typetext.setLayoutParams(lp2);
		datetext.setLayoutParams(lp2);
		cktext.setLayoutParams(lp2);
		bmtext.setLayoutParams(lp2);
		mctext.setLayoutParams(lp2);
		ggtext.setLayoutParams(lp2);
		jldwtext.setLayoutParams(lp2);
		lbtext.setLayoutParams(lp2);
		numtext.setLayoutParams(lp2);
		djtext.setLayoutParams(lp2);
		zjtext.setLayoutParams(lp2);
		kctext.setLayoutParams(lp2);
		deptext.setLayoutParams(lp2);
		dwtext.setLayoutParams(lp2);
		jbrtext.setLayoutParams(lp2);
		bztext.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	}
	public void init(){
		Intent intent=getIntent();
		Object mvdhObject = intent.getStringExtra(DataBaseHelper.MVDH);
		dhtext.setText(mvdhObject==null?"":mvdhObject.toString());
		
		Object mvtypeObject = intent.getStringExtra(DataBaseHelper.MVType);
		typetext.setText(mvtypeObject==null?"":mvtypeObject.toString());
		
		Object mvddateObject = intent.getStringExtra(DataBaseHelper.MVDDATE);
		datetext.setText(mvddateObject==null?"":mvddateObject.toString());
		
		Object ckmcObject = intent.getStringExtra(DataBaseHelper.CKMC);
		cktext.setText(ckmcObject==null?"":ckmcObject.toString());
		
		Object hpbmObject = intent.getStringExtra(DataBaseHelper.HPBM);
		bmtext.setText(hpbmObject==null?"":hpbmObject.toString());
		
		Object hpmcObject = intent.getStringExtra(DataBaseHelper.HPMC);
		mctext.setText(hpmcObject==null?"":hpmcObject.toString());
		
		Object ggxhObject = intent.getStringExtra(DataBaseHelper.GGXH);
		ggtext.setText(ggxhObject==null?"":ggxhObject.toString());
		
		Object jldwObject = intent.getStringExtra(DataBaseHelper.JLDW);
		jldwtext.setText(jldwObject==null?"":jldwObject.toString());
		
		Object lbsObject = intent.getStringExtra(DataBaseHelper.LBS);
		lbtext.setText(lbsObject==null?"":lbsObject.toString());
		
		Object mslObject = intent.getStringExtra(DataBaseHelper.MSL);
		numtext.setText(mslObject==null?"":mslObject.toString());
		
		Object djObject = intent.getStringExtra(DataBaseHelper.DJ);
		djtext.setText(djObject==null?"":djObject.toString());
		
		Object zjObject = intent.getStringExtra(DataBaseHelper.ZJ);
		zjtext.setText(zjObject==null?"":zjObject.toString());
		
		Object azkcObject = intent.getStringExtra(DataBaseHelper.AZKC);
		kctext.setText(azkcObject==null?"":azkcObject.toString());
		
		Object depnameObject = intent.getStringExtra(DataBaseHelper.DepName);
		deptext.setText(depnameObject==null?"":depnameObject.toString());
		
		Object dwnameObject = intent.getStringExtra(DataBaseHelper.DWName);
		dwtext.setText(dwnameObject==null?"":dwnameObject.toString());
		
		Object jbrObject = intent.getStringExtra(DataBaseHelper.JBR);
		jbrtext.setText(jbrObject==null?"":jbrObject.toString());
		
		Object bzObject = intent.getStringExtra(DataBaseHelper.BZ);
		bztext.setText(bzObject==null?"":bzObject.toString());
	}

	
}
