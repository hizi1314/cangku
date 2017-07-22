package com.guantang.cangkuonline.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.TagFlowLayout.FlowLayout;
import com.guantang.cangkuonline.TagFlowLayout.TagAdapter;
import com.guantang.cangkuonline.TagFlowLayout.TagFlowLayout;
import com.guantang.cangkuonline.adapter.MyDJAdapter;
import com.guantang.cangkuonline.customview.ControlScrollViewPager;
import com.guantang.cangkuonline.customview.PagerSlidingTabStrip;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.database.DataBaseOperateMethod;
import com.guantang.cangkuonline.fragment.CompletedUpdateDJFragment;
import com.guantang.cangkuonline.fragment.NoUpdateDJFragment;
import com.guantang.cangkuonline.fragment.NoUpdateDJFragment.RefreshInterface;
import com.guantang.cangkuonline.fragment.UnfinishedDJFragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LocalDJActivity extends FragmentActivity implements OnClickListener,RefreshInterface{
	
	private TextView titleTxtView,startTxtView,endTxtView,danjuTxtView,searchTxtView;
	private ImageButton backImgBtn;
	private LinearLayout promptLayout;
	private List<Fragment> fragmentlist=new ArrayList<Fragment>();
	private List<Map<String,Object>> DJlist = new ArrayList<Map<String,Object>>();
	private List<Map<String,Object>> noupdateList = new ArrayList<Map<String,Object>>();
	private List<Map<String,Object>> completedUpdateList = new ArrayList<Map<String,Object>>();
	private List<Map<String,Object>> unfinishedList = new ArrayList<Map<String,Object>>();
	private SimpleDateFormat formatter;
	private String date1,date2;
	private DataBaseOperateMethod dm_op = new DataBaseOperateMethod(this);
	private String[] str1 = { DataBaseHelper.HPM_ID, DataBaseHelper.MVDH,
			DataBaseHelper.MVDT, DataBaseHelper.Details, DataBaseHelper.mType,
			DataBaseHelper.JBR, DataBaseHelper.DWName,
			DataBaseHelper.actType, DataBaseHelper.CKMC,DataBaseHelper.CKID,
			DataBaseHelper.DepName, DataBaseHelper.Lrr, DataBaseHelper.Lrsj,
			DataBaseHelper.DJType };
	private String sqlString;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_localdj);
		initView();
		init();
	}
	
	public void initView(){
		backImgBtn = (ImageButton) findViewById(R.id.back);
		searchTxtView = (TextView) findViewById(R.id.search);
		startTxtView = (TextView) findViewById(R.id.startTxtView);
		endTxtView = (TextView) findViewById(R.id.endTxtView);
		danjuTxtView = (TextView) findViewById(R.id.danjuTxtView);
		titleTxtView = (TextView) findViewById(R.id.title);
		promptLayout = (LinearLayout) findViewById(R.id.promptLayout);
		
		backImgBtn.setOnClickListener(this);
		searchTxtView.setOnClickListener(this);

	}
	
	public void init(){
		formatter = new SimpleDateFormat("yyyy-MM-dd");
		String date = formatter.format(new Date(System.currentTimeMillis()));
		date1 = date;
		date2 = date;
		
		Intent intent = getIntent();
		if(intent.getBooleanExtra("todayDJ", false)){
			titleTxtView.setText("本地已上传单据");
			if(intent.getIntExtra("todayop_type", 1)==1){
				String sql = " where  " + DataBaseHelper.MVDT + "='"
						+ date  + "' and "+DataBaseHelper.mType+"='1' and "+DataBaseHelper.DJType+" =1 ";
				DJlist = dm_op.search_DJ(sql, str1,intent.getIntExtra("ckid", -1));
				
				startTxtView.setText(date1);
				endTxtView.setText(date2);
				danjuTxtView.setText("入库单据");
				
			}else if(intent.getIntExtra("todayop_type", 1)==2){
				String sql = " where  " + DataBaseHelper.MVDT + "='"
						+ date + "' and "+DataBaseHelper.mType+"='2' and "+DataBaseHelper.DJType+" =1 ";
				DJlist = dm_op.search_DJ(sql, str1,intent.getIntExtra("ckid", -1));

				startTxtView.setText(date1);
				endTxtView.setText(date2);
				danjuTxtView.setText("出库单据");
			}
			FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			CompletedUpdateDJFragment completedUpdateDJFragment = new CompletedUpdateDJFragment().getInstance(DJlist);
			fragmentTransaction.replace(R.id.contentpager, completedUpdateDJFragment).commit();
		}else{
			titleTxtView.setText("本地未上传单据");
			String sql = " where  "+DataBaseHelper.DJType+" = -1 "+ " order by lrsj desc ";
			DJlist = dm_op.search_DJ(sql, str1,intent.getIntExtra("ckid", -1));
			promptLayout.setVisibility(View.GONE);
			danjuTxtView.setText("不限");
			FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			NoUpdateDJFragment NoUpdateDJFragment = new NoUpdateDJFragment().getInstance(DJlist);
			fragmentTransaction.replace(R.id.contentpager, NoUpdateDJFragment).commit();
		}
		
	}
	
	public void setFragmentAdapter(){
		
		Intent intent = getIntent();
		if(intent.getBooleanExtra("todayDJ", false)){
			String sql = sqlString+" and "+DataBaseHelper.DJType + " = 1 "+ " order by lrsj desc ";
			DJlist = dm_op.search_DJ(sql, str1,intent.getIntExtra("ckid", -1));
			FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			CompletedUpdateDJFragment completedUpdateDJFragment = new CompletedUpdateDJFragment().getInstance(DJlist);
			fragmentTransaction.replace(R.id.contentpager, completedUpdateDJFragment).commit();
		}else{
			String sql = sqlString+" and "+DataBaseHelper.DJType + " = -1 "+ " order by lrsj desc ";
			DJlist = dm_op.search_DJ(sql, str1,intent.getIntExtra("ckid", -1));
			FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			NoUpdateDJFragment NoUpdateDJFragment = new NoUpdateDJFragment().getInstance(DJlist);
			fragmentTransaction.replace(R.id.contentpager, NoUpdateDJFragment).commit();
		}
		
		
	}

	
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		Intent intent = new Intent();
		switch(v.getId()){
		case R.id.back:
			finish();
			break;
		case R.id.search:
			intent.setClass(this, DJfilterActivity.class);
			startActivityForResult(intent, 1);
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO 自动生成的方法存根
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			if (resultCode == 1) {
				promptLayout.setVisibility(View.VISIBLE);
				String fromtime = data.getStringExtra("fromtime");
				String totime = data.getStringExtra("totime");
				sqlString = " where  " + DataBaseHelper.MVDT + ">='" + fromtime
						+ "'  and " + DataBaseHelper.MVDT + "<='"
						+ totime + "' ";
				startTxtView.setText(fromtime);
				endTxtView.setText(totime);

				int ckid = data.getIntExtra("ckid", -1);
				String ckmc = data.getStringExtra("ckmc");
				if (ckid != -1) {
					sqlString = sqlString + " and " + DataBaseHelper.CKID + " = " + ckid
							+ " ";
				}

				String wherestr = "";
				StringBuilder sb = new StringBuilder();
				if (data.getBooleanExtra("ruku_flag", false)) {
					wherestr = " and (" + DataBaseHelper.mType + " = 1 ";
					
					sb.append("入库单据");
				}
				if (data.getBooleanExtra("chuku_flag", false)) {
					if (wherestr.equals("")) {
						wherestr = " and (" + DataBaseHelper.mType + " = 2 ";
						
						sb.append("出库单据");
					} else {
						wherestr = wherestr + " or " + DataBaseHelper.mType
								+ " = 2 ";
						
						sb.append("、出库单据");
					}
				}
				if (data.getBooleanExtra("pandian_flag", false)) {
					if (wherestr.equals("")) {
						wherestr = " and " + DataBaseHelper.mType + " = 0 ";
						
						sb.append("盘点单据");
					} else {
						wherestr = wherestr + " or " + DataBaseHelper.mType
								+ " = 0 )";
						
						sb.append("、盘点单据");
					}
				} else {
					if (!wherestr.equals("")) {
						wherestr = wherestr + " )";
					}
				}

				String dh = data.getStringExtra("dh");
				if (!dh.equals("不限")) {
					sqlString = sqlString + " and " + DataBaseHelper.MVDH + " = '" + dh
							+ "' ";
					
				}

				sqlString = sqlString + wherestr;
				if(sb.toString().isEmpty()){
					danjuTxtView.setText("不限");
				}else{
					danjuTxtView.setText(sb.toString());
				}
				setFragmentAdapter();
			}
		}
	}

	@Override
	public void execute() {
		// TODO 自动生成的方法存根
		setFragmentAdapter();
	}
	
}
