package com.guantang.cangkuonline.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.adapter.HpCkkcAdapter;
import com.guantang.cangkuonline.application.MyApplication;
import com.guantang.cangkuonline.database.DataBaseOperateMethod;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;
import com.guantang.cangkuonline.webservice.WebserviceImport;
import com.guantang.cangkuonline.webservice.WebserviceImport_more;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

public class HpCkkcFragment extends Fragment {
	
	private ListView ckkcListView;
	private String hpid = "";
	private List<Map<String,Object>> hpckkcList = new ArrayList<Map<String,Object>>();
	private HpCkkcAdapter mHpCkkcAdapter;
	private SharedPreferences mSharedPreferences;
	private DataBaseOperateMethod dm_op;
	private Context context;
	
	private HpCkkcFragment(){
		super();
	}
	
	public static HpCkkcFragment getInstance(String hpid){
		HpCkkcFragment HpCkkc = new HpCkkcFragment();
		Bundle bundle = new Bundle();
		bundle.putString("hpid", hpid);
		HpCkkc.setArguments(bundle);
		return HpCkkc;
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO 自动生成的方法存根
		super.onAttach(activity);
		context = activity;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		hpid = getArguments().getString("hpid");
		mSharedPreferences = MyApplication.getInstance().getSharedPreferences();
		dm_op = new DataBaseOperateMethod(context);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		View view = inflater.inflate(R.layout.hpckkcfragment, null);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onActivityCreated(savedInstanceState);
		ckkcListView=(ListView) getView().findViewById(R.id.ckkcListView);
		init();
	}
	
	public void init(){
		if(mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1)==1){
			if(WebserviceImport.isOpenNetwork(context)){
				new hpckkcAsyncTask().execute();
			}else{
				Toast.makeText(context, "网络未连接", Toast.LENGTH_SHORT).show();
			}
		}else if(mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1)==-1){
			hpckkcList=dm_op.get_ckkcAndckmc(hpid);
			setAdapter();
		}
	}
	
	class hpckkcAsyncTask extends AsyncTask<Void, Void, String>{

		@Override
		protected String doInBackground(Void... params) {
			// TODO 自动生成的方法存根
			String json = WebserviceImport_more.GetCKKC_1_0(hpid);
			return json;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO 自动生成的方法存根
			super.onPostExecute(result);
			try {
				JSONObject jsonObject = new JSONObject(result);
				switch(jsonObject.getInt("Status")){
				case 1:
					hpckkcList.clear();
					JSONObject dataJsonObject = jsonObject.getJSONObject("Data");
					JSONArray dsJSONArray = dataJsonObject.getJSONArray("ds");
					int length=dsJSONArray.length();
					for(int i=0;i<length;i++){
						JSONObject json = dsJSONArray.getJSONObject(i);
						String ckname = json.getString("CKMC");
						String kcsl = json.getString("kcsl");
						Map<String,Object> map = new HashMap<String,Object>();
						map.put("ckmc", ckname);
						map.put("kcsl", kcsl);
						hpckkcList.add(map);
					}
					setAdapter();
					break;
				default:
					Toast.makeText(context, "库存信息获取出错", Toast.LENGTH_SHORT).show();
					break;
				}
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			
		}
		
	}
	
	public void setAdapter(){
		mHpCkkcAdapter = new HpCkkcAdapter(context, hpckkcList, R.layout.hpckkc_item_layout);
		ckkcListView.setAdapter(mHpCkkcAdapter);
		//默认情况下Android是禁止在ScrollView中放入另外的ScrollView的，它的高度是无法计算的，上面的ckkcListView是嵌套在ScrollView中的。
		int totalHight = 0;
		int size = mHpCkkcAdapter.getCount();
		for(int i=0;i<size;i++){
			View ItemView=mHpCkkcAdapter.getView(i, null, ckkcListView);
			ItemView.measure(0, 0);
			totalHight += ItemView.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = ckkcListView.getLayoutParams();
		params.height = totalHight+ckkcListView.getPaddingBottom()+ckkcListView.getPaddingTop()+(ckkcListView.getDividerHeight()*(ckkcListView.getCount()-1));
		ckkcListView.setLayoutParams(params);
	}
	
}
