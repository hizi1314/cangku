package com.guantang.cangkuonline.activity;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.XListView.XListView.IXListViewListener;
import com.guantang.cangkuonline.commonadapter.CommonAdapter;
import com.guantang.cangkuonline.commonadapter.ViewHolder;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.eventbusBean.ObjectNine;
import com.guantang.cangkuonline.swipemenuXlistview.SwipeMenuXListView;
import com.guantang.cangkuonline.webservice.WebserviceImport;
import com.guantang.cangkuonline.webservice.WebserviceImport_more;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SomeOneTypeOrderActivity extends Activity implements IXListViewListener,OnClickListener{
	
	private ImageButton backImgBtn;
	private TextView titleTextView;
	private SwipeMenuXListView myListView;
	private List<Map<String, Object>> mList = new ArrayList<Map<String, Object>>();
	private String[] str = { DataBaseHelper.ID, DataBaseHelper.Status,
			DataBaseHelper.DWName, DataBaseHelper.LXR,
			DataBaseHelper.OrderIndex, DataBaseHelper.Trades,
			DataBaseHelper.dirc, DataBaseHelper.Sqdt, DataBaseHelper.TEL,
			DataBaseHelper.zje, DataBaseHelper.yfje, DataBaseHelper.syje,
			DataBaseHelper.BZ, DataBaseHelper.sqr, DataBaseHelper.ReqDate,
			DataBaseHelper.DepName, DataBaseHelper.DWID, DataBaseHelper.sqrID };
	private String[] str2 = { "id", "status", "dwName", "lxr", "orderIndex",
			"trades", "dirc", "sqdt", "tel", "zje", "yfje", "syje", "bz",
			"sqr", "ReqDate", "depName", "dwid", "sqrID" };
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	private DDAdapter mDDAdapter;
	private String sqfromtime = "";
	private String sqtotime = "";
	private int orderNum = 0; //0 今日订单，1未审核订单，2被驳回订单，3已完成订单

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_someonetypeorder);
		backImgBtn = (ImageButton) findViewById(R.id.back);
		titleTextView = (TextView) findViewById(R.id.title);
		myListView = (SwipeMenuXListView) findViewById(R.id.mylist);
		backImgBtn.setOnClickListener(this);
		myListView.setXListViewListener(this);
		myListView.setPullLoadEnable(true);// 设置可以加载更多
		myListView.setPullRefreshEnable(true);// 设置可以刷新
		
		mDDAdapter = new DDAdapter(this, mList, R.layout.dditem_layout);
		myListView.setAdapter(mDDAdapter);
		sqfromtime = formatter.format(System.currentTimeMillis());
		sqtotime = formatter.format(System.currentTimeMillis());
		
		Intent intent = getIntent();
		orderNum = intent.getIntExtra("myorderstart", 0);
		
		if(WebserviceImport.isOpenNetwork(this)){
			if(orderNum==0){
				titleTextView.setText("今日订单");
				new GetDDInfo_1_0AsyncTask().execute("All","0",sqfromtime,sqtotime,"");
			}else if(orderNum==1){
				titleTextView.setText("未审核订单");
				new GetDDInfo_1_0AsyncTask().execute("NotAuditedInfo","0",sqfromtime,sqtotime,"");
			}else if(orderNum==2){
				titleTextView.setText("被驳回订单");
				new GetDDInfo_1_0AsyncTask().execute("RejectInfo","0",sqfromtime,sqtotime,"");
			}else if(orderNum==3){
				titleTextView.setText("已完成订单");
				new GetDDInfo_1_0AsyncTask().execute("FinishedInfo","0",sqfromtime,sqtotime,"");
			}
		}else{
			Toast.makeText(this, "网络没连接", Toast.LENGTH_SHORT).show();
		}
	}

	class GetDDInfo_1_0AsyncTask extends AsyncTask<String,Void,String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO 自动生成的方法存根
			String jsonString = WebserviceImport_more.GetDDInfo_1_0(params[0], params[1], "10", params[2], params[3], params[4], "-1");
			return jsonString;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO 自动生成的方法存根
			super.onPostExecute(result);
			stopLoadface();
			try {
				JSONObject jsonObject = new JSONObject(result);
				switch(jsonObject.getInt("Status")){
				case 1:
					parseData(jsonObject);
					break;
				case 2://提示数据加载完成
					parseData(jsonObject);
					if(!mList.isEmpty()){
						Toast.makeText(SomeOneTypeOrderActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					}
					break;
				default:
					Toast.makeText(SomeOneTypeOrderActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				}
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}
	
	public void parseData(JSONObject jsonObject) throws JSONException{
		JSONObject dataJSONObject = jsonObject.getJSONObject("Data");
		JSONObject allJsonObject = null;
		if(orderNum==0){
			allJsonObject = dataJSONObject.getJSONObject("All");
		}else if(orderNum==1){
			allJsonObject = dataJSONObject.getJSONObject("NotAuditedInfo");
		}else if(orderNum==2){
			allJsonObject = dataJSONObject.getJSONObject("RejectInfo");
		}else if(orderNum==3){
			allJsonObject = dataJSONObject.getJSONObject("FinishedInfo");
		}
		JSONArray dsJSONArray = allJsonObject.getJSONArray("ds");
		int Length = dsJSONArray.length();
		for(int i = 0; i<Length; i++){
			JSONObject myJSONObject = dsJSONArray.getJSONObject(i);
			Map<String,Object> map = new HashMap<String,Object>();
			for(int j =0;j<18;j++){
				map.put(str[j], myJSONObject.getString(str2[j]));
			}
			mList.add(map);
		}
		mDDAdapter.setData(mList);
	}

	@Override
	public void onRefresh() {
		// TODO 自动生成的方法存根
		onLoadTime();
		if(WebserviceImport.isOpenNetwork(this)){
			mList.clear();
			if(orderNum==0){
				new GetDDInfo_1_0AsyncTask().execute("All","0",sqfromtime,sqtotime,"");
			}else if(orderNum==1){
				new GetDDInfo_1_0AsyncTask().execute("NotAuditedInfo","0",sqfromtime,sqtotime,"");
			}else if(orderNum==2){
				new GetDDInfo_1_0AsyncTask().execute("Pending","0",sqfromtime,sqtotime,"");
			}else if(orderNum==3){
				new GetDDInfo_1_0AsyncTask().execute("FinishedInfo","0",sqfromtime,sqtotime,"");
			}
		}else{
			stopLoadface();
			Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
		}
	}
	@Override
	public void onLoadMore() {
		// TODO 自动生成的方法存根
		onLoadTime();
		if(WebserviceImport.isOpenNetwork(this)){
			if(mList.isEmpty()){
				if(orderNum==0){
					new GetDDInfo_1_0AsyncTask().execute("All","0",sqfromtime,sqtotime,"");
				}else if(orderNum==1){
					new GetDDInfo_1_0AsyncTask().execute("NotAuditedInfo","0",sqfromtime,sqtotime,"");
				}else if(orderNum==2){
					new GetDDInfo_1_0AsyncTask().execute("Pending","0",sqfromtime,sqtotime,"");
				}else if(orderNum==3){
					new GetDDInfo_1_0AsyncTask().execute("FinishedInfo","0",sqfromtime,sqtotime,"");
				}
			}else{
				if(orderNum==0){
					new GetDDInfo_1_0AsyncTask().execute("All",mList.get(mList.size()-1).get(DataBaseHelper.ID).toString(),sqfromtime,sqtotime,"");
				}else if(orderNum==1){
					new GetDDInfo_1_0AsyncTask().execute("NotAuditedInfo",mList.get(mList.size()-1).get(DataBaseHelper.ID).toString(),sqfromtime,sqtotime,"");
				}else if(orderNum==2){
					new GetDDInfo_1_0AsyncTask().execute("Pending",mList.get(mList.size()-1).get(DataBaseHelper.ID).toString(),sqfromtime,sqtotime,"");
				}else if(orderNum==3){
					new GetDDInfo_1_0AsyncTask().execute("FinishedInfo",mList.get(mList.size()-1).get(DataBaseHelper.ID).toString(),sqfromtime,sqtotime,"");
				}
			}
		}else{
			stopLoadface();
			Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void onLoadTime() {
		Calendar c = Calendar.getInstance();
		String year = String.valueOf(c.get(Calendar.YEAR));
		String month = String.valueOf(new DecimalFormat("00").format(c
				.get(Calendar.MONTH) + 1));
		String day = String.valueOf(new DecimalFormat("00").format(c
				.get(Calendar.DAY_OF_MONTH)));
		String hour = String.valueOf(new DecimalFormat("00").format(c
				.get(Calendar.HOUR_OF_DAY)));
		String minute = String.valueOf(new DecimalFormat("00").format(c
				.get(Calendar.MINUTE)));
		String refreshDate = year + "-" + month + "-" + day + " " + hour + ":"
				+ minute;
		myListView.setRefreshTime(refreshDate);
	}

	/**
	 * 停止界面加载动画
	 * */
	public void stopLoadface() {
		myListView.stopRefresh();
		myListView.stopLoadMore();
	}
	
	class DDAdapter extends CommonAdapter<Map<String,Object>>{

		public DDAdapter(Context mContext, List<Map<String, Object>> mList,
				int LayoutId) {
			super(mContext, mList, LayoutId);
			// TODO 自动生成的构造函数存根
		}

		@Override
		public void convert(ViewHolder holder, final Map<String, Object> item) {
			// TODO 自动生成的方法存根
			TextView ddhTxtView = holder.getView(R.id.ddhTxtView);
			TextView ddtypeTxtView = holder.getView(R.id.ddtypeTxtView);
			TextView dwTitleTxtView = holder.getView(R.id.dwTitleTxtView);
			TextView dwnameTxtView = holder.getView(R.id.dwnameTxtView);
			TextView yqdhsjTxtView = holder.getView(R.id.yqdhsjTxtView);
			TextView Icon_TxtView = holder.getView(R.id.Icon_TxtView);
			
			Object OrderIndexObject = item.get(DataBaseHelper.OrderIndex);
			ddhTxtView.setText(OrderIndexObject == null || OrderIndexObject.equals("null") ?"":OrderIndexObject.toString());
			
			String dricString = item.get(DataBaseHelper.dirc).toString();
			if(dricString != null){
				if(dricString.equals("0")){
					ddtypeTxtView.setText("采购订单");
					dwTitleTxtView.setText("供应商：");
				}else if(dricString.equals("1")){
					ddtypeTxtView.setText("销售订单");
					dwTitleTxtView.setText("客户名称：");
				}
			}
			
			Object dwnameObject = item.get(DataBaseHelper.DWName);
			dwnameTxtView.setText(dwnameObject == null || dwnameObject.equals("null") ?"":dwnameObject.toString());
			
			Object yqdhsjObject = item.get(DataBaseHelper.ReqDate);
			yqdhsjTxtView.setText(yqdhsjObject == null|| yqdhsjObject.equals("null") ?"":yqdhsjObject.toString().subSequence(0, 10));
			
			String statusString = item.get(DataBaseHelper.Status).toString();
			if(statusString != null ){
				if(statusString.equals("0")){
					Icon_TxtView.setText("待审核");
				}else if(statusString.equals("1")){
					Icon_TxtView.setText("被驳回");
				}else if(statusString.equals("2")||statusString.equals("3")){
					Icon_TxtView.setText("待执行");
				}else if(statusString.equals("5")||statusString.equals("7")){
					Icon_TxtView.setText("已完成");
				}
			}
			holder.getConvertView().setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO 自动生成的方法存根
					Intent intent = new Intent(context,DDdetailActivity.class);
					intent.putExtra("map", (Serializable) item);
					startActivity(intent);
				}
			});
		}
		
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch(v.getId()){
		case R.id.back:
			finish();
			break;
		}
	}

}
