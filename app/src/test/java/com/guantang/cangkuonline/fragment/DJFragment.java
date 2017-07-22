package com.guantang.cangkuonline.fragment;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.XListView.XListView.IXListViewListener;
import com.guantang.cangkuonline.activity.HistoryDJ_DetailsActivity;
import com.guantang.cangkuonline.activity.NetDJActivity;
import com.guantang.cangkuonline.activity.NetDJActivity.DJfilterInterface;
import com.guantang.cangkuonline.application.MyApplication;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.eventbusBean.ObjectTen;
import com.guantang.cangkuonline.activity.MyOrderActivity.DDfilterInterface;
import com.guantang.cangkuonline.adapter.JSONDataAdapter;
import com.guantang.cangkuonline.swipemenuXlistview.SwipeMenuXListView;
import com.guantang.cangkuonline.webservice.WebserviceImport;
import com.guantang.cangkuonline.webservice.WebserviceImport_more;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class DJFragment extends Fragment implements IXListViewListener,OnItemClickListener,DJfilterInterface{
	
	private SwipeMenuXListView mSwipeMenuXListView;
	private int type =1; //1是入库单据，2是出库单据，0是盘点单据；
	private List<JSONObject> djlist = new ArrayList<JSONObject>();
	private Context context;
	private JSONDataAdapter mJSONDataAdapter;
	private String sql;
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
	
	private Semaphore semaphore = new Semaphore(1);
	
	
	public static DJFragment getInstance (int mType){
		DJFragment mDJFragment = new DJFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("type", mType);
		mDJFragment.setArguments(bundle);
		return mDJFragment;
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		context = activity;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		type = getArguments().getInt("type");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.orderstatus_fragment_layout, null);
		mSwipeMenuXListView = (SwipeMenuXListView) view.findViewById(R.id.djlist);
		mSwipeMenuXListView.setXListViewListener(this);
		mSwipeMenuXListView.setOnItemClickListener(this);
		mSwipeMenuXListView.setPullLoadEnable(true);// 设置可以加载更多
		mSwipeMenuXListView.setPullRefreshEnable(true);// 设置可以刷新
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		init();
	}
	
	public void init(){
		mJSONDataAdapter = new JSONDataAdapter(context);
		mSwipeMenuXListView.setAdapter(mJSONDataAdapter);
		
		if (WebserviceImport.isOpenNetwork(context)) {
//			sql = DataBaseHelper.MVDT+" >= '"+todaytime+"' and "+DataBaseHelper.MVDT+" <= '"+todaytime+"' and "+
//					DataBaseHelper.mType+" = '"+type+"'";
			sql = DataBaseHelper.mType+" = '"+type+"'";
			new MyDJAsyncTask().execute(sql,"mvdt desc","1","10");
		} else {
			Toast.makeText(context, "网络未连接", Toast.LENGTH_SHORT).show();
		}
	}
	
	class MyDJAsyncTask extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO 自动生成的方法存根
			String jsonString = WebserviceImport_more.Gt_MovemByPermissions_1_0(params[0], params[1], Integer.parseInt(params[2]), Integer.parseInt(params[3]), MyApplication.getInstance().getSharedPreferences());
			return jsonString;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO 自动生成的方法存根
			super.onPostExecute(result);
			stopLoad();
			try {
				JSONObject jsonObject = new JSONObject(result);
				semaphore.release();
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
					Toast.makeText(context, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				}
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}
	
	
	@Override
	public void onRefresh() {
		// TODO 自动生成的方法存根
		if(WebserviceImport.isOpenNetwork(context)){
			djlist.clear();
			onLoad();
			try {
				semaphore.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			new MyDJAsyncTask().execute(sql,"mvdt desc","1","10");
		}else{
			Toast.makeText(context, "网络未连接", Toast.LENGTH_SHORT).show();
		}
		
	}

	@Override
	public void onLoadMore() {
		// TODO 自动生成的方法存根
		if(WebserviceImport.isOpenNetwork(context)){
			onLoad();
			try {
				semaphore.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(djlist.isEmpty()){
				new MyDJAsyncTask().execute(sql,"mvdt desc","1","10");
			}else{
				new MyDJAsyncTask().execute(sql,"mvdt desc",String.valueOf(djlist.size()+1),String.valueOf(djlist.size()+10));
			}
		}else{
			Toast.makeText(context, "网络未连接", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void onLoad() {
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
		mSwipeMenuXListView.setRefreshTime(refreshDate);
	}
	
	private void stopLoad(){
		mSwipeMenuXListView.stopRefresh();
		mSwipeMenuXListView.stopLoadMore();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
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
		intent.setClass(context, HistoryDJ_DetailsActivity.class);
		startActivity(intent);
	}

	@Override
	public void execute(ObjectTen obj) {
		// TODO Auto-generated method stub
		sql = obj.getSql();
		if(WebserviceImport.isOpenNetwork(context)){
			djlist.clear();
			new MyDJAsyncTask().execute(sql,"mvdt desc","1","10");
		}else{
			Toast.makeText(context, "网络未连接", Toast.LENGTH_SHORT).show();
		}
	}
	
}
