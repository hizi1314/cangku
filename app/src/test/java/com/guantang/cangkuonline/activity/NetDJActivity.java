package com.guantang.cangkuonline.activity;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.TagFlowLayout.FlowLayout;
import com.guantang.cangkuonline.TagFlowLayout.TagAdapter;
import com.guantang.cangkuonline.TagFlowLayout.TagFlowLayout;
import com.guantang.cangkuonline.XListView.XListView;
import com.guantang.cangkuonline.XListView.XListView.IXListViewListener;
import com.guantang.cangkuonline.activity.MySubmitDJActivity.MyDJAsynctask;
import com.guantang.cangkuonline.adapter.JSONDataAdapter;
import com.guantang.cangkuonline.customview.ControlScrollViewPager;
import com.guantang.cangkuonline.customview.PagerSlidingTabStrip;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.eventbusBean.ObjectTen;
import com.guantang.cangkuonline.fragment.DJFragment;
import com.guantang.cangkuonline.fragment.DiaoboDJFragment;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;
import com.guantang.cangkuonline.webservice.WebserviceImport;
import com.guantang.cangkuonline.webservice.WebserviceImport_more;

public class NetDJActivity extends FragmentActivity implements OnClickListener{
	
	private TextView searchTxtView;
	private ImageButton backBtn;
	private SharedPreferences mSharedPreferences;
	private List<JSONObject> djlist = new ArrayList<JSONObject>();
	private String sql = "",sql1 = "";
	private String[] str1 = { DataBaseHelper.HPM_ID, DataBaseHelper.MVDH,
			DataBaseHelper.MVDT, DataBaseHelper.CKMC, DataBaseHelper.CKID,
			DataBaseHelper.DepName, DataBaseHelper.DepID,
			DataBaseHelper.DWName, DataBaseHelper.JBR,
			DataBaseHelper.BZ, DataBaseHelper.hpDetails,
			DataBaseHelper.actType, DataBaseHelper.mType, DataBaseHelper.Lrsj,
			DataBaseHelper.Lrr };
	private String[] str2 = { "hpm_id", "mvdh", "mvdt", "ckName", "ckid",
			"depName", "depId", "dwName", "jbr", "bz", "hpDetails",
			"actType", "mType", "lrsj", "lrr" };
	private SimpleDateFormat formatter1;
	private JSONDataAdapter mJSONDataAdapter;
	private ExecutorService cacheThreadPool = Executors.newCachedThreadPool();
	private String typeString,todaytime;
	private Boolean firstflag = true;
	private PagerSlidingTabStrip mPagerSlidingTabStrip;
	private ControlScrollViewPager mControlScrollViewPager;
	private List<String> titleList = new ArrayList<String>();
	private List<Fragment> fragmentList = new ArrayList<Fragment>();
	private MyPagerAdapter mMyPagerAdapter;
	
	public interface DJfilterInterface{
		void execute(ObjectTen obj);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_netdj);
		mSharedPreferences = getSharedPreferences(ShareprefenceBean.SHAREPREFENCE_NAME, Context.MODE_PRIVATE);
		initView();
		init();
	}
	
	
	
	public void initView() {
		backBtn = (ImageButton) findViewById(R.id.back);
		searchTxtView = (TextView) findViewById(R.id.search);
		mPagerSlidingTabStrip =(PagerSlidingTabStrip) findViewById(R.id.pagerTabStrip1);
		mControlScrollViewPager = (ControlScrollViewPager) findViewById(R.id.viewpagercontent1);
		searchTxtView.setOnClickListener(this);
		backBtn.setOnClickListener(this);
		mControlScrollViewPager.setScrollable(true);

	}

	public void init() {
		
		titleList.add("入库单据");
		titleList.add("出库单据");
		titleList.add("盘点单据");
		titleList.add("调拨单据");
		
		DJFragment rukuDJFragment = DJFragment.getInstance(1);
		DJFragment chukuDJFragment = DJFragment.getInstance(2);
		DJFragment pandianDJFragment = DJFragment.getInstance(0);
		DiaoboDJFragment diaoboDJFragment = new DiaoboDJFragment();
		
		fragmentList.add(rukuDJFragment);
		fragmentList.add(chukuDJFragment);
		fragmentList.add(pandianDJFragment);
		fragmentList.add(diaoboDJFragment);
		
		mMyPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
		mControlScrollViewPager.setAdapter(mMyPagerAdapter);
		mPagerSlidingTabStrip.setViewPager(mControlScrollViewPager);
		mMyPagerAdapter.setData(fragmentList);
		mControlScrollViewPager.setOffscreenPageLimit(4);
		setPageTitlesValue();
			
		if (WebserviceImport.isOpenNetwork(this)) {
			new MyCheckPermissions().executeOnExecutor(cacheThreadPool);
		} else {
			Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
		}
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
		// TODO 自动生成的方法存根
		Intent intent = new Intent();
		Calendar calender = Calendar.getInstance();
		switch(v.getId()){
		case R.id.back:
			finish();
		break;
		case R.id.search:
			intent.setClass(this, NetDJfilterActivity.class);
			startActivityForResult(intent, 1);
			break;
		}
	}
	
	class MyCheckPermissions extends AsyncTask<Void, Void, String>{

		@Override
		protected String doInBackground(Void... params) {
			// TODO 自动生成的方法存根
			String jsonString = WebserviceImport_more.Gt_MovemLevel_1_0(mSharedPreferences);
			return jsonString;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO 自动生成的方法存根
			super.onPostExecute(result);
			try {
				JSONObject jsonObject = new JSONObject(result);
				switch(jsonObject.getInt("Status")){
				case 1:
					String permissionString = jsonObject.getString("Data");
					String[] permission = permissionString.split(",");
					StringBuilder sayBuilder = new StringBuilder();
					if(permission[0].equals("1")){
						sayBuilder.append(" 入库 ");
					}
					if(permission[1].equals("1")){
						sayBuilder.append("出库 ");
					}
					if(permission[2].equals("1")){
						sayBuilder.append("盘点 ");
					}
					if(!sayBuilder.toString().isEmpty()){
						Toast.makeText(NetDJActivity.this, "你拥有查看所有"+sayBuilder.toString()+"的单据权限", Toast.LENGTH_LONG).show();
					}else{
						Toast.makeText(NetDJActivity.this, "你没有相应的单据查看权限", Toast.LENGTH_SHORT).show();
					}
					break;
				}
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO 自动生成的方法存根
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			if (resultCode == 1) {
				String fromtime = data.getStringExtra("fromtime");
				String totime = data.getStringExtra("totime");
				sql = "mvdt >='" + fromtime
						+ " 00:00:00'  and " + "mvdt <='"
						+ totime + " 23:59:59' ";
				sql1 = " mvdt >='" + fromtime
						+ " 00:00:00'  and " + "mvdt <='"
						+ totime + " 23:59:59' ";
				
				int ckid = data.getIntExtra("ckid", -1);
				if (ckid != -1) {
					sql = sql + " and " + DataBaseHelper.CKID + " = " + ckid
							+ " ";
					sql1 = sql1 + " and " + DataBaseHelper.sckid +" = "+ckid+" or "+DataBaseHelper.dckid + " = "+ckid+" ";
				}

				String dh = data.getStringExtra("dh");
				if (!dh.equals("不限")) {
					sql = sql + " and " + DataBaseHelper.MVDH + " = '" + dh
							+ "' ";
					sql1 = sql1 + " and " + DataBaseHelper.MVDH + " = '" + dh
							+ "' ";
				}
				
				for(int i = 0 ; i<fragmentList.size();i++){
					((DJfilterInterface) fragmentList.get(i)).execute(new ObjectTen(sql, sql1));
				}
				
			}
		}
	}
	
	class MyPagerAdapter extends FragmentPagerAdapter{
		
		private List<Fragment> mList = new ArrayList<Fragment>();
		
		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}
		
		public void setData(List<Fragment> list){
			this.mList = list;
			notifyDataSetChanged();
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			return mList.get(arg0);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mList.size();
		}
		
		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			return titleList.get(position);
		}
		
	}
	
}
