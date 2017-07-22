package com.guantang.cangkuonline.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.activity.SeeVisitDetailActivity;
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
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class VisitRecordFragment extends Fragment {

	public String dwid = "",dwName = "";
	public ListView mListView;
	public List<Map<String, Object>> mList = new ArrayList<Map<String, Object>>();
	public String[] str = { "id", "dwid", "logTime", "content", "OurMan", "OppMan", "Tel", "addr", "GPS" };
	public Context context;
	
	public static VisitRecordFragment getInstance(String dwid,String dwName) {
		VisitRecordFragment visitRecordFragment = new VisitRecordFragment();
		Bundle bundle = new Bundle();
		bundle.putString("dwid", dwid);
		bundle.putString("dwName", dwName);
		visitRecordFragment.setArguments(bundle);
		return visitRecordFragment;
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
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		dwid = getArguments().getString("dwid");
		dwName = getArguments().getString("dwName");
		View view = inflater.inflate(R.layout.ddprogress_listviewfragment, null);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mListView = (ListView) getView().findViewById(R.id.mylist);
		if (WebserviceImport.isOpenNetwork(context)) {
			new GetCrmlogAsyncTask().execute();
		} else {
			Toast.makeText(context, "网络未连接", Toast.LENGTH_SHORT).show();
		}
	}

	class GetCrmlogAsyncTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String jsonString = WebserviceImport_more.GetCrmlog_1_0(dwid);
			return jsonString;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				JSONObject jsonObject = new JSONObject(result);
				switch (jsonObject.getInt("Status")) {
				case 1:
					JSONArray dataJsonObject = jsonObject.getJSONArray("Data");
					int length = dataJsonObject.length();
					for (int i = 0; i < length; i++) {
						JSONObject myJsonObject = dataJsonObject.getJSONObject(i);
						Map<String, Object> map = new HashMap<String, Object>();
						for (int j = 0; j < 9; j++) {
							map.put(str[j], myJsonObject.get(str[j]));
						}
						mList.add(map);
					}
					MyAdapter mMyAdapter = new MyAdapter(context);
					mListView.setAdapter(mMyAdapter);
					mMyAdapter.setData(mList);
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

	class MyAdapter extends BaseAdapter {

		private Context mContext;
		private LayoutInflater inflater;
		private List<Map<String, Object>> mList = new ArrayList<Map<String, Object>>();

		public MyAdapter(Context context) {
			this.mContext = context;
			inflater = LayoutInflater.from(context);
		}

		public void setData(List<Map<String, Object>> mList) {
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
			if (position == 0) {
				return 0;
			} else if (position != 0 && position == (mList.size() - 1)) {
				return 2;
			} else {
				return 1;
			}
		}

		@Override
		public int getViewTypeCount() {
			// TODO 自动生成的方法存根
			return 3;
		}

		public View getfirstView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.item_visitrecord_first_layout, null);
				holder.timeTxtView = (TextView) convertView.findViewById(R.id.timeTxtView);
				holder.whoDoTxtView = (TextView) convertView.findViewById(R.id.whoDoTxtView);
				holder.contentTxtView = (TextView) convertView.findViewById(R.id.contentTxtView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			Map<String, Object> map = mList.get(position);

			Object logTimeObject = map.get("logTime");
			holder.timeTxtView.setText(logTimeObject==null || logTimeObject.toString().equals("null") ? "" : logTimeObject.toString());

			Object OurManObject = map.get("OurMan");
			holder.whoDoTxtView.setText(OurManObject==null || OurManObject.toString().equals("null") ? "" : OurManObject.toString() + "添加拜访记录");

			Object contentObject = map.get("content");
			holder.contentTxtView.setText(contentObject==null || contentObject.toString().equals("null") ? "" : contentObject.toString());
			
			convertView.setOnClickListener(new MyOnClickListener(map));
			
			return convertView;
		}

		public View getlaterView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.item_visitrecord_later_layout, null);
				holder.timeTxtView = (TextView) convertView.findViewById(R.id.timeTxtView);
				holder.whoDoTxtView = (TextView) convertView.findViewById(R.id.whoDoTxtView);
				holder.contentTxtView = (TextView) convertView.findViewById(R.id.contentTxtView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			Map<String, Object> map = mList.get(position);

			Object logTimeObject = map.get("logTime");
			holder.timeTxtView.setText(logTimeObject==null || logTimeObject.toString().equals("null") ? "" : logTimeObject.toString());

			Object OurManObject = map.get("OurMan");
			holder.whoDoTxtView.setText(OurManObject==null || OurManObject.toString().equals("null") ? "" : OurManObject.toString() + "添加拜访记录");

			Object contentObject = map.get("content");
			holder.contentTxtView.setText(contentObject==null || contentObject.toString().equals("null") ? "" : contentObject.toString());
			
			convertView.setOnClickListener(new MyOnClickListener(map));
			
			return convertView;
		}

		public View getlastView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.item_visitrecord_last_layout, null);
				holder.timeTxtView = (TextView) convertView.findViewById(R.id.timeTxtView);
				holder.whoDoTxtView = (TextView) convertView.findViewById(R.id.whoDoTxtView);
				holder.contentTxtView = (TextView) convertView.findViewById(R.id.contentTxtView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			Map<String,Object> map = mList.get(position);
			
			Object logTimeObject = map.get("logTime");
			holder.timeTxtView.setText(logTimeObject==null || logTimeObject.toString().equals("null") ? "" : logTimeObject.toString());

			Object OurManObject = map.get("OurMan");
			holder.whoDoTxtView.setText(OurManObject==null || OurManObject.toString().equals("null") ? "" : OurManObject.toString() + "添加拜访记录");

			Object contentObject = map.get("content");
			holder.contentTxtView.setText(contentObject==null || contentObject.toString().equals("null") ? "" : contentObject.toString());
			
			convertView.setOnClickListener(new MyOnClickListener(map));
			
			return convertView;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			if (getItemViewType(position) == 0) {
				return getfirstView(position, convertView, parent);
			} else if (getItemViewType(position) == 1) {
				return getlaterView(position, convertView, parent);
			} else if (getItemViewType(position) == 2) {
				return getlastView(position, convertView, parent);
			}

			return convertView;
		}

		class ViewHolder {
			TextView timeTxtView, whoDoTxtView, contentTxtView;
		}
		
		class MyOnClickListener implements OnClickListener{
			
			private Map<String,Object> map = new HashMap<String,Object>();
			
			public MyOnClickListener(Map<String,Object> map) {
				// TODO Auto-generated constructor stub
				this.map = map;
			}
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				JSONObject mapJSONObject = new JSONObject(map);
				try {
					for(int i =0 ; i<9 ; i++){
						String valueString = mapJSONObject.getString((str[i]));
						if(valueString==null){
							map.put(str[i], "");
						}else{
							map.put(str[i], valueString);
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Intent intent = new Intent(mContext,SeeVisitDetailActivity.class);
				intent.putExtra("visitdetail", (Serializable) map);
				intent.putExtra("dwName", dwName);
				startActivity(intent);
			}
			
		}
	}

}
