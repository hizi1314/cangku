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

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.TagFlowLayout.FlowLayout;
import com.guantang.cangkuonline.TagFlowLayout.TagAdapter;
import com.guantang.cangkuonline.TagFlowLayout.TagFlowLayout;
import com.guantang.cangkuonline.XListView.XListView;
import com.guantang.cangkuonline.XListView.XListView.IXListViewListener;
import com.guantang.cangkuonline.adapter.JSONDataAdapter;
import com.guantang.cangkuonline.application.MyApplication;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.webservice.WebserviceImport;
import com.guantang.cangkuonline.webservice.WebserviceImport_more;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MySubmitDJActivity extends Activity implements OnClickListener,OnItemClickListener,IXListViewListener{
	
	private TextView startTxtView,endTxtView,danjuTxtView,searchTxtView;
	private ImageButton backBtn;
	private TextView titleTextView;
	private XListView mListView;
	
	private List<JSONObject> djlist = new ArrayList<JSONObject>();
	private String sql = "";
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
	private int ckid = -1;
	private Boolean firstflag = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todaynetdj);
		initView();
		init();
	}
	
	public void initView(){
		backBtn = (ImageButton) findViewById(R.id.back);
		mListView = (XListView) findViewById(R.id.djlist);
		startTxtView = (TextView) findViewById(R.id.startTxtView);
		endTxtView = (TextView) findViewById(R.id.endTxtView);
		danjuTxtView = (TextView) findViewById(R.id.danjuTxtView);
		searchTxtView = (TextView) findViewById(R.id.search);
		titleTextView = (TextView) findViewById(R.id.title);
		searchTxtView.setOnClickListener(this);
		backBtn.setOnClickListener(this);

		mListView.setOnItemClickListener(this);
		mListView.setPullLoadEnable(true);// 设置可以加载更多
		mListView.setPullRefreshEnable(true);// 设置可以刷新
		mListView.setXListViewListener(this);
		
		titleTextView.setText("我提交的单据");
		
	}

	public void init(){
		Intent intent = getIntent();
		typeString = intent.getStringExtra(DataBaseHelper.mType);
		ckid = intent.getIntExtra("ckid", -1);
		formatter1 = new SimpleDateFormat("yyyy-MM-dd");
		todaytime=formatter1.format(new Date(System.currentTimeMillis()));
		mJSONDataAdapter = new JSONDataAdapter(this);
		mListView.setAdapter(mJSONDataAdapter);
		
		if(typeString!=null){
			sql = DataBaseHelper.MVDT+" >= '"+todaytime+"' and "+DataBaseHelper.MVDT+" <= '"+todaytime+"' and "+
					DataBaseHelper.mType+" = '"+typeString+"'";
			if(typeString.equals("0")){
//				conditionList.clear();
//				conditionList.add(todaytime + "至" + todaytime);
//				conditionList.add("盘点单据");

				startTxtView.setText(todaytime);
				endTxtView.setText(todaytime);
				danjuTxtView.setText("盘点单据");
			}else if(typeString.equals("1")){
//				conditionList.clear();
//				conditionList.add(todaytime + "至" + todaytime);
//				conditionList.add("入库单据");

				startTxtView.setText(todaytime);
				endTxtView.setText(todaytime);
				danjuTxtView.setText("入库单据");
			}else if(typeString.equals("2")){
//				conditionList.clear();
//				conditionList.add(todaytime + "至" + todaytime);
//				conditionList.add("出库单据");

				startTxtView.setText(todaytime);
				endTxtView.setText(todaytime);
				danjuTxtView.setText("出库单据");
			}
		}
		
		if (WebserviceImport.isOpenNetwork(this)) {
			new MyDJAsynctask().executeOnExecutor(cacheThreadPool, sql,"mvdt desc","1","10");
		} else {
			Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
		}
	}
	
	class MyDJAsynctask extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO 自动生成的方法存根
			String JsonString = WebserviceImport_more.Gt_MovemByMy_2_0(params[0], params[1], Integer.parseInt(params[2]), Integer.parseInt(params[3]),ckid,MyApplication.getInstance().getSharedPreferences());
			return JsonString;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO 自动生成的方法存根
			super.onPostExecute(result);
			
			try {
				JSONObject jsonObject = new JSONObject(result);
				switch(jsonObject.getInt("Status")){
				case 1:
					JSONArray dataJsonArray = jsonObject.getJSONArray("Data");
					List<JSONObject> littleList = new ArrayList<JSONObject>();
					int dataJsonArrayLength = dataJsonArray.length();
					for(int i=0;i<dataJsonArrayLength;i++){
						littleList.add(dataJsonArray.getJSONObject(i));
					}
					djlist.addAll(littleList);
					mJSONDataAdapter.setData(djlist);
					break;
				case -1:
					Toast.makeText(MySubmitDJActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				}
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
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
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO 自动生成的方法存根
		
		JSONObject JsonObject = (JSONObject) arg0.getAdapter().getItem(arg2);
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			for(int i=0;i<str1.length;i++){
				String valueString = JsonObject.getString((str2[i]));
				if(valueString==null){
					map.put(str1[i], "");
				}else{
					map.put(str1[i], valueString);
				}
			}
		} catch (JSONException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		Bundle bundle = new Bundle();
		ArrayList<Map<String, Object>> List = new ArrayList<Map<String,Object>>();
		List.add(map);
		Intent intent = new Intent();
		bundle.putSerializable("DJDetails", (Serializable) List);
		intent.putExtras(bundle);
		intent.putExtra("from", 0);
		intent.putExtra("HPM_ID", map.get(DataBaseHelper.HPM_ID).toString());
		intent.setClass(this, HistoryDJ_DetailsActivity.class);
		startActivity(intent);
	}
	
	@Override
	public void onRefresh() {
		// TODO 自动生成的方法存根
		if (WebserviceImport.isOpenNetwork(this)) {
			djlist.clear();
			new MyDJAsynctask().executeOnExecutor(cacheThreadPool, sql,"mvdt desc","1","10");
		} else {
			Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
		}
		onLoad();
	}

	@Override
	public void onLoadMore() {
		// TODO 自动生成的方法存根
		if (WebserviceImport.isOpenNetwork(this)) {
			new MyDJAsynctask().executeOnExecutor(cacheThreadPool, sql,"mvdt desc",String.valueOf(djlist.size()+1),String.valueOf(djlist.size()+10));
		} else {
			Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
		}
		onLoad();
	}
	
	private void onLoad(){
		Calendar calendar = Calendar.getInstance();
		String year = String.valueOf(calendar.get(Calendar.YEAR));
		String month = String.valueOf(new DecimalFormat("00").format(calendar.get(Calendar.MONTH)+1));
		String day = String.valueOf(new DecimalFormat("00").format(calendar.get(Calendar.DAY_OF_MONTH)));
		String hour = String.valueOf(new DecimalFormat("00").format(calendar.get(Calendar.HOUR_OF_DAY)));
		String minute = String.valueOf(new DecimalFormat("00").format(calendar.get(Calendar.MINUTE)));
		
		String refreshDate = year+"-"+month+"-"+day+" "+hour+":"+minute;
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime(refreshDate);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO 自动生成的方法存根
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			if (resultCode == 1) {
//				conditionList.clear();
				String fromtime = data.getStringExtra("fromtime");
				String totime = data.getStringExtra("totime");
				sql = "mvdt >='" + fromtime
						+ " 00:00:00'  and " + "mvdt <='"
						+ totime + " 23:59:59' ";
//				conditionList.add(fromtime + "至" + totime);
				
				startTxtView.setText(fromtime);
				endTxtView.setText(totime);
				
				int ckid = data.getIntExtra("ckid", -1);
				String ckmc = data.getStringExtra("ckmc");
				if (ckid != -1) {
					sql = sql + " and " + DataBaseHelper.CKID + " = " + ckid
							+ " ";
//					conditionList.add("仓库:" + ckmc);
				}

				String wherestr = "";
				StringBuilder sb = new StringBuilder();
				if (data.getBooleanExtra("ruku_flag", false)) {
					wherestr = " and (" + DataBaseHelper.mType + " = 1 ";
//					conditionList.add("入库单据");
					sb.append("入库单据");
				}
				if (data.getBooleanExtra("chuku_flag", false)) {
					if (wherestr.equals("")) {
						wherestr = " and (" + DataBaseHelper.mType + " = 2 ";
//						conditionList.add("出库单据");
						sb.append("出库单据");
					} else {
						wherestr = wherestr + " or " + DataBaseHelper.mType
								+ " = 2 ";
//						conditionList.add("出库单据");
						sb.append("、出库单据");
					}
				}
				if (data.getBooleanExtra("pandian_flag", false)) {
					if (wherestr.equals("")) {
						wherestr = " and " + DataBaseHelper.mType + " = 0 ";
//						conditionList.add("盘库单据");
						sb.append("盘点单据");
					} else {
						wherestr = wherestr + " or " + DataBaseHelper.mType
								+ " = 0 )";
//						conditionList.add("盘库单据");
						sb.append("、盘点单据");
					}
				} else {
					if (!wherestr.equals("")) {
						wherestr = wherestr + " )";
					}
				}

				String dh = data.getStringExtra("dh");
				if (!dh.equals("不限")) {
					sql = sql + " and " + DataBaseHelper.MVDH + " = '" + dh
							+ "' ";
//					conditionList.add("单号:" + dh);
				}
				sql = sql + wherestr;
				if(sb.toString().isEmpty()){
					danjuTxtView.setText("不限");
				}else{
					danjuTxtView.setText(sb.toString());
				}
				
				if(WebserviceImport.isOpenNetwork(this)){
					djlist.clear();
					new MyDJAsynctask().executeOnExecutor(cacheThreadPool, sql,"mvdt desc","1","10");
				}else{
					Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
}
