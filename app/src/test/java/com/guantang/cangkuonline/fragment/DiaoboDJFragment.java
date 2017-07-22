package com.guantang.cangkuonline.fragment;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.XListView.XListView.IXListViewListener;
import com.guantang.cangkuonline.activity.NetDJActivity.DJfilterInterface;
import com.guantang.cangkuonline.activity.TransdDetailActivity;
import com.guantang.cangkuonline.commonadapter.CommonAdapter;
import com.guantang.cangkuonline.commonadapter.ViewHolder;
import com.guantang.cangkuonline.eventbusBean.ObjectTen;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class DiaoboDJFragment extends Fragment implements IXListViewListener,OnItemClickListener,DJfilterInterface{
	
	private Context context;
	private SwipeMenuXListView mSwipeMenuXListView;
	private List<JSONObject> djlist = new ArrayList<JSONObject>();
	private String sql;
	private String str1[] = {"sckName","sckid","dckName","dckid","mvdh","mvdt","jbr","hpnames","bz","hpzjz"};
	// 调拨明细字段
	private String str2[] = { "hpid", "sl", "dj", "zj", "mid", "note" };
	private MyAdapter mMyAdapter;
	
	private Semaphore semaphore = new Semaphore(1);
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.context = activity;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
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
		mMyAdapter = new MyAdapter(context, djlist, R.layout.item_diaobodj);
		mSwipeMenuXListView.setAdapter(mMyAdapter);
		
		if(WebserviceImport.isOpenNetwork(context)){
			new GetTransmAsyncTask().execute(sql," mvdt desc ","1","10");
		}else{
			Toast.makeText(context, "网络未连接", Toast.LENGTH_SHORT).show();
		}
	}
	
	class GetTransmAsyncTask extends AsyncTask<String,Void,String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String jsonString = WebserviceImport_more.Gettransm_1_0(params[0], params[1], params[2], params[3]);
			return jsonString;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			stopLoad();
			try {
				JSONObject jsonObject = new JSONObject(result);
				semaphore.release();
				switch(jsonObject.getInt("Status")){
				case 1:
					JSONObject dataJSONObject = jsonObject.getJSONObject("Data");
					JSONArray dsJSONArray = dataJSONObject.getJSONArray("ds");
					int length = dsJSONArray.length();
					for(int i=0;i<length;i++){
						JSONObject myJsonObject = dsJSONArray.getJSONObject(i);
						djlist.add(myJsonObject);
					}
					mMyAdapter.setData(djlist);
					break;
				default:
					Toast.makeText(context, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		JSONObject jsonObject = (JSONObject) arg0.getAdapter().getItem(arg2);
		Intent intent = new Intent();
		intent.setClass(context, TransdDetailActivity.class);
		intent.putExtra("jsonObject", jsonObject.toString());
		startActivity(intent);
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		if(WebserviceImport.isOpenNetwork(context)){
			onLoad();
			try {
				semaphore.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			new GetTransmAsyncTask().execute(sql,"mvdt desc","1","10");
		}else{
			Toast.makeText(context, "网络未连接", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		if(WebserviceImport.isOpenNetwork(context)){
			if(djlist.isEmpty()){
				onLoad();
				try {
					semaphore.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				new GetTransmAsyncTask().execute(sql,"mvdt desc","1","10");
			}else{
				new GetTransmAsyncTask().execute(sql,"mvdt desc",String.valueOf(djlist.size()+1),String.valueOf(djlist.size()+10));
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
	
	class MyAdapter extends CommonAdapter<JSONObject>{

		public MyAdapter(Context mContext, List<JSONObject> mList, int LayoutId) {
			super(mContext, mList, LayoutId);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void convert(ViewHolder holder, JSONObject item) {
			// TODO Auto-generated method stub
			TextView dhTextView = holder.getView(R.id.dh);
			TextView diaochuTextView = holder.getView(R.id.diaochuTextView);
			TextView diaoruTextView = holder.getView(R.id.diaoruTextView);
			TextView dateTextView = holder.getView(R.id.dateTextView);
			TextView hpDetailsTextView = holder.getView(R.id.hpDetails);
			
			try {
				String mvdhString = item.getString("mvdh");
				dhTextView.setText((mvdhString==null || mvdhString.equals("null"))?"":mvdhString);
				String sckNameString = item.getString("sckName");
				diaochuTextView.setText((sckNameString==null || sckNameString.equals("null"))?"":sckNameString);
				String dckNameString = item.getString("dckName");
				diaoruTextView.setText((dckNameString==null || dckNameString.equals("null"))?"":dckNameString);
				String mvdtString = item.getString("mvdt");
				dateTextView.setText((mvdtString==null || mvdtString.equals("null"))?"":mvdtString.subSequence(0, 10));
				String hpnamesString = item.getString("hpnames");
				hpDetailsTextView.setText((hpnamesString==null || hpnamesString.equals("null"))?"":hpnamesString);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public void execute(ObjectTen obj) {
		// TODO Auto-generated method stub
		sql = obj.getSql1();
		if(WebserviceImport.isOpenNetwork(context)){
			djlist.clear();
			new GetTransmAsyncTask().execute(sql,"mvdt desc","1","10");
		}else{
			Toast.makeText(context, "网络未连接", Toast.LENGTH_SHORT).show();
		}
	}
	
}
