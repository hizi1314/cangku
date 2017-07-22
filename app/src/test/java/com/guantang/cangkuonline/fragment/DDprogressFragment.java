package com.guantang.cangkuonline.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.commonadapter.CommonAdapter;
import com.guantang.cangkuonline.commonadapter.ViewHolder;
import com.guantang.cangkuonline.webservice.WebserviceImport;
import com.guantang.cangkuonline.webservice.WebserviceImport_more;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

public class DDprogressFragment extends Fragment {
	
	private ListView mListView;
	private String orderid;
	private List<Map<String,Object>> mList = new ArrayList<Map<String,Object>>();
	private Context context;
	
	public static DDprogressFragment getInstance(String orderid){
		DDprogressFragment ddProgressFragment = new DDprogressFragment();
		Bundle bundle = new Bundle();
		bundle.putString("orderid", orderid);
		ddProgressFragment.setArguments(bundle);
		return ddProgressFragment;
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
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		orderid = getArguments().getString("orderid");
		View view = inflater.inflate(R.layout.ddprogress_listviewfragment, null);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onActivityCreated(savedInstanceState);
		mListView = (ListView) getView().findViewById(R.id.mylist);
		
		if(WebserviceImport.isOpenNetwork(context)){
			new DDProgressAsyncTask().execute(orderid);
		}else{
			Toast.makeText(context, "网络未连接", Toast.LENGTH_SHORT).show();
		}
	}
	
	class DDProgressAsyncTask extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO 自动生成的方法存根
			String jsonString = WebserviceImport_more.GetDDLog_1_0(params[0]);
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
					JSONArray dataJsonArray = jsonObject.getJSONArray("Data");
					int length = dataJsonArray.length();
					for(int i = 0; i<length; i++){
						Map<String,Object> map = new HashMap<String,Object>();
						JSONObject itemJSONObject = dataJsonArray.getJSONObject(i);
						map.put("id", itemJSONObject.getString("id"));
						map.put("OrderID", itemJSONObject.getString("OrderID"));
						map.put("LogTime", itemJSONObject.getString("LogTime"));
						map.put("LogDesc", itemJSONObject.get("LogDesc"));
						map.put("CreateUser", itemJSONObject.get("CreateUser"));
						mList.add(map);
					}
					MyAdapter myAdapter = new MyAdapter(context);
					mListView.setAdapter(myAdapter);
					myAdapter.setData(mList);
					break;
				default:
					Toast.makeText(context, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				}
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}
	
//	class MyAdapter extends CommonAdapter<Map<String,Object>>{
//		
//		private String dateString = "";
//		
//		public MyAdapter(Context mContext, List<Map<String, Object>> mList,
//				int LayoutId) {
//			super(mContext, mList, LayoutId);
//			// TODO 自动生成的构造函数存根
//		}
//
//		@Override
//		public void convert(ViewHolder holder, Map<String, Object> item) {
//			// TODO 自动生成的方法存根
//			LinearLayout dateLayout = holder.getView(R.id.datelayout);
//			TextView dateTxtView = holder.getView(R.id.dateTxtView);
//			TextView timeTxtView = holder.getView(R.id.timeTxtView);
//			TextView doTxtView = holder.getView(R.id.doTxtView);
//			
//			String[] time =item.get("LogTime").toString().split(" ");
//			dateTxtView.setText(time[0]);
//			dateLayout.setVisibility(View.VISIBLE);
//			
//			if(holder.getPosition()>0){
//				String[] lasttime =mList.get(holder.getPosition()-1).get("LogTime").toString().split(" ");
//				if(time[0].equals(lasttime[0])){
//					dateLayout.setVisibility(View.GONE);
//				}
//			}
//			timeTxtView.setText(time[1]);
//			
//			if(holder.getPosition()==0){
//				doTxtView.setTextColor(Color.parseColor("#ff5757"));
//			}else{
//				doTxtView.setTextColor(Color.parseColor("#666666"));
//			}
//			doTxtView.setText(item.get("LogDesc").toString());
//		}
//		
//	}
	
	class MyAdapter extends BaseAdapter{
		
		private Context mContext;
		private LayoutInflater inflater;
		private List<Map<String,Object>> mList = new ArrayList<Map<String,Object>>();
		
		public MyAdapter(Context context){
			this.mContext = context;
			inflater = LayoutInflater.from(context);
		}
		
		public void setData(List<Map<String,Object>> mList){
			this.mList = mList;
			notifyDataSetChanged();
		}
		
		@Override
		public int getCount() {
			// TODO 自动生成的方法存根
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO 自动生成的方法存根
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO 自动生成的方法存根
			return position;
		}
		
		

		@Override
		public int getItemViewType(int position) {
			// TODO 自动生成的方法存根
			if(position==0){
				return 0;
			}else if(position!=0 && position==(mList.size()-1)){
				return 2;
			}else{
				return 1;
			}
		}

		@Override
		public int getViewTypeCount() {
			// TODO 自动生成的方法存根
			return 3;
		}
		
		public View getfirstView(int position, View convertView, ViewGroup parent){
			
			ViewHolder holder = null;
			if(convertView == null){
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.item_progress_first_layout, null);
				holder.timeTxtView = (TextView) convertView.findViewById(R.id.timeTxtView);
				holder.doTxtView = (TextView) convertView.findViewById(R.id.doTxtView);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			Map<String,Object> map = mList.get(position);
			
			holder.timeTxtView.setText(map.get("LogTime").toString());
			
			holder.doTxtView.setText(map.get("LogDesc").toString());
			
			return convertView;
		}
		
		public View getlaterView(int position, View convertView, ViewGroup parent){
			ViewHolder holder = null;
			if(convertView == null){
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.item_progress_later_layout, null);
				holder.timeTxtView = (TextView) convertView.findViewById(R.id.timeTxtView);
				holder.doTxtView = (TextView) convertView.findViewById(R.id.doTxtView);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			Map<String,Object> map = mList.get(position);
			
			holder.timeTxtView.setText(map.get("LogTime").toString());
			
			holder.doTxtView.setText(map.get("LogDesc").toString());
			
			return convertView;
		}
		
		public View getlastView(int position, View convertView, ViewGroup parent){
			ViewHolder holder = null;
			if(convertView == null){
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.item_progress_last_layout, null);
				holder.timeTxtView = (TextView) convertView.findViewById(R.id.timeTxtView);
				holder.doTxtView = (TextView) convertView.findViewById(R.id.doTxtView);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			Map<String,Object> map = mList.get(position);
			
			holder.timeTxtView.setText(map.get("LogTime").toString());
			
			holder.doTxtView.setText(map.get("LogDesc").toString());
			
			return convertView;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO 自动生成的方法存根
			if(getItemViewType(position)==0){
				return getfirstView(position, convertView, parent);
			}else if(getItemViewType(position)==1){
				return getlaterView(position, convertView, parent);
			}else if(getItemViewType(position)==2){
				return getlastView(position, convertView, parent);
			}
			
			return convertView;
		}
		
		class ViewHolder{
			TextView timeTxtView,doTxtView;
		}
		
	}
	
}
