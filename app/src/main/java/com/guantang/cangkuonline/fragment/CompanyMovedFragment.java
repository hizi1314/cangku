package com.guantang.cangkuonline.fragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.XListView.XListView;
import com.guantang.cangkuonline.XListView.XListView.IXListViewListener;
import com.guantang.cangkuonline.activity.MX_InfoActivity;
import com.guantang.cangkuonline.application.MyApplication;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.fragment.CompanyOrderFragment.GetCompanyDDAsyncTask;
import com.guantang.cangkuonline.helper.RightsHelper;
import com.guantang.cangkuonline.webservice.WebserviceImport;
import com.guantang.cangkuonline.webservice.WebserviceImport_more;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CompanyMovedFragment extends Fragment implements IXListViewListener {

	public String dwid = "";
	public XListView mXListView;
	public LinearLayout cw_layout;
	public List<Map<String, Object>> mList = new ArrayList<Map<String, Object>>();
	private String[] str3 = { "hpd_id", "mvddh", "mvddt", "mddirect", "dactType", "msl", "dj", "zj", "dwName", "jbr",
			"ckName", "depName" };
	private MyAdapter mMyAdapter;
	private Context context;

	public static CompanyMovedFragment getInstance(String dwid) {
		CompanyMovedFragment mCompanyOrderFragment = new CompanyMovedFragment();
		Bundle bundle = new Bundle();
		bundle.putString("dwid", dwid);
		mCompanyOrderFragment.setArguments(bundle);
		return mCompanyOrderFragment;
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
		View view = inflater.inflate(R.layout.companymoved_layout, null);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		cw_layout = (LinearLayout) getView().findViewById(R.id.cw);
		mXListView = (XListView) getView().findViewById(R.id.list);
		mXListView.setXListViewListener(this);
		mXListView.setPullLoadEnable(true);// 设置可以加载更多
		mXListView.setPullRefreshEnable(true);// 设置可以刷新
		
		mMyAdapter = new MyAdapter(context);
		mXListView.setAdapter(mMyAdapter);
		
		if (RightsHelper.isHaveRight(RightsHelper.system_cw, MyApplication.getInstance().getSharedPreferences())) {
			cw_layout.setVisibility(View.VISIBLE);
		} else {
			cw_layout.setVisibility(View.GONE);
		}

		if (WebserviceImport.isOpenNetwork(context)) {
			new GetCompanyMovedAsyncTask().execute("0");
		} else {
			Toast.makeText(context, "网络未连接", Toast.LENGTH_SHORT).show();
		}
	}
	
	class GetCompanyMovedAsyncTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String jsonString = WebserviceImport_more.GetCompanyMoved_1_0(20, params[0], -1, dwid);
			return jsonString;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			stopLoadface();
			try {
				JSONObject jsonObject = new JSONObject(result);
				switch (jsonObject.getInt("Status")) {
				case 1:
					JSONObject dataJsonObject = jsonObject.getJSONObject("Data");
					JSONArray dsJsonObject = dataJsonObject.getJSONArray("ds");
					int length = dsJsonObject.length();
					for (int i = 0; i < length; i++) {
						Map<String, Object> map = new HashMap<String, Object>();
						JSONObject myJsonObject = dsJsonObject.getJSONObject(i);
						for (int j = 0; j < 12; j++) {
							map.put(str3[j], myJsonObject.get(str3[j]));
						}
						mList.add(map);
					}
					mMyAdapter.setDate(mList);
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
	public void onRefresh() {
		// TODO 自动生成的方法存根
		onLoadTime();
		if (WebserviceImport.isOpenNetwork(context)) {
			mList.clear();
			new GetCompanyMovedAsyncTask().execute("0");
		} else {
			stopLoadface();
			Toast.makeText(context, "网络未连接", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onLoadMore() {
		// TODO 自动生成的方法存根
		onLoadTime();
		if (WebserviceImport.isOpenNetwork(context)) {
			if (mList.isEmpty()) {
				new GetCompanyMovedAsyncTask().execute("0");
			} else {
				new GetCompanyMovedAsyncTask().execute(mList.get(mList.size() - 1).get("hpd_id").toString());
			}
		} else {
			stopLoadface();
			Toast.makeText(context, "网络未连接", Toast.LENGTH_SHORT).show();
		}
	}

	public void onLoadTime() {
		Calendar c = Calendar.getInstance();
		String year = String.valueOf(c.get(Calendar.YEAR));
		String month = String.valueOf(new DecimalFormat("00").format(c.get(Calendar.MONTH) + 1));
		String day = String.valueOf(new DecimalFormat("00").format(c.get(Calendar.DAY_OF_MONTH)));
		String hour = String.valueOf(new DecimalFormat("00").format(c.get(Calendar.HOUR_OF_DAY)));
		String minute = String.valueOf(new DecimalFormat("00").format(c.get(Calendar.MINUTE)));
		String refreshDate = year + "-" + month + "-" + day + " " + hour + ":" + minute;
		mXListView.setRefreshTime(refreshDate);
	}

	/**
	 * 停止界面加载动画
	 */
	public void stopLoadface() {
		mXListView.stopRefresh();
		mXListView.stopLoadMore();
	}

	class MyAdapter extends BaseAdapter {

		public Context mContext;
		public LayoutInflater inflater;
		public List<Map<String, Object>> mList = new ArrayList<Map<String, Object>>();

		public MyAdapter(Context context) {
			this.mContext = context;
			inflater = LayoutInflater.from(context);
		}

		public void setDate(List<Map<String, Object>> list) {
			mList = list;
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public int getItemViewType(int position) {
			// TODO Auto-generated method stub
			if (RightsHelper.isHaveRight(RightsHelper.system_cw, MyApplication.getInstance().getSharedPreferences())) {
				return 1;// 有财务计算
			} else {
				return 0;// 没有财务计算
			}

		}

		@Override
		public int getViewTypeCount() {
			// TODO Auto-generated method stub
			return 2;
		}

		public View getcwView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.moved_hp_item, null);
				holder.depNameTextView = (TextView) convertView.findViewById(R.id.depName);
				holder.dhTextView = (TextView) convertView.findViewById(R.id.dh);
				holder.dateTextView = (TextView) convertView.findViewById(R.id.date);
				holder.opTextView = (TextView) convertView.findViewById(R.id.op);
				holder.typeTextView = (TextView) convertView.findViewById(R.id.type);
				holder.numTextView = (TextView) convertView.findViewById(R.id.num);
				holder.djTextView = (TextView) convertView.findViewById(R.id.dj);
				holder.zjTextView = (TextView) convertView.findViewById(R.id.zj);
				holder.dwTextView = (TextView) convertView.findViewById(R.id.dw);
				holder.jbrTextView = (TextView) convertView.findViewById(R.id.jbr);
				holder.ckTextView = (TextView) convertView.findViewById(R.id.ck);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			final Map<String, Object> map = mList.get(position);

			Object mvdhObject = map.get("mvddh");
			holder.dhTextView.setText(mvdhObject == null || mvdhObject.toString().equals("null")? "" : mvdhObject.toString());

			Object mvdtObject = map.get("mvddt");
			holder.dateTextView.setText(mvdtObject == null || mvdtObject.toString().equals("null")? "" : mvdtObject.toString());

			Object mvtypeObject = map.get("dactType");
			holder.typeTextView.setText(mvtypeObject == null || mvtypeObject.toString().equals("null")? "" : mvtypeObject.toString());

			Object mslObject = map.get("msl");
			holder.numTextView.setText(mslObject == null || mslObject.toString().equals("null")? "" : mslObject.toString());

			Object djObject = map.get("dj");
			holder.djTextView.setText(djObject == null || djObject.toString().equals("null")? "" : djObject.toString());

			Object zjObject = map.get("zj");
			holder.zjTextView.setText(zjObject == null || zjObject.toString().equals("null")? "" : zjObject.toString());

			Object dwnameObject = map.get("dwName");
			holder.dwTextView.setText(dwnameObject == null || dwnameObject.toString().equals("null")? "" : dwnameObject.toString());

			Object jbrObject = map.get("jbr");
			holder.jbrTextView.setText(jbrObject == null || jbrObject.toString().equals("null")? "" : jbrObject.toString());

			Object ckmcObject = map.get("ckName");
			holder.ckTextView.setText(ckmcObject == null || ckmcObject.toString().equals("null")? "" : ckmcObject.toString());

			Object depnameObject = map.get("depName");
			holder.depNameTextView.setText(depnameObject == null || depnameObject.toString().equals("null")? "" : depnameObject.toString());

			String mvdirectString = map.get("mddirect").toString();
			if (mvdirectString.equals("1")) {
				holder.opTextView.setText("入库");
			} else if (mvdirectString.equals("2")){
				holder.opTextView.setText("出库");
			} else {
				holder.opTextView.setText(mvdirectString);
			}

			if ((position % 2) == 0) {
				convertView.setBackgroundColor(Color.parseColor("#FFFFFF"));
			} else if ((position % 2) == 1) {
				convertView.setBackgroundColor(Color.parseColor("#E5E5E5"));
			}

			return convertView;
		}

		public View getnocwView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.moved_hp_item2, null);
				holder.depNameTextView = (TextView) convertView.findViewById(R.id.depName);
				holder.dhTextView = (TextView) convertView.findViewById(R.id.dh);
				holder.dateTextView = (TextView) convertView.findViewById(R.id.date);
				holder.opTextView = (TextView) convertView.findViewById(R.id.op);
				holder.typeTextView = (TextView) convertView.findViewById(R.id.type);
				holder.numTextView = (TextView) convertView.findViewById(R.id.num);
				holder.dwTextView = (TextView) convertView.findViewById(R.id.dw);
				holder.jbrTextView = (TextView) convertView.findViewById(R.id.jbr);
				holder.ckTextView = (TextView) convertView.findViewById(R.id.ck);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			final Map<String, Object> map = mList.get(position);
			
			
			Object mvdhObject = map.get("hpd_id");
			holder.dhTextView.setText(mvdhObject == null || mvdhObject.toString().equals("null")? "" : mvdhObject.toString());

			Object mvdtObject = map.get("mvddh");
			holder.dateTextView.setText(mvdtObject == null || mvdtObject.toString().equals("null")? "" : mvdtObject.toString());

			Object mvtypeObject = map.get("dactType");
			holder.typeTextView.setText(mvtypeObject == null || mvtypeObject.toString().equals("null")? "" : mvtypeObject.toString());

			Object mslObject = map.get("msl");
			holder.numTextView.setText(mslObject == null || mslObject.toString().equals("null")? "" : mslObject.toString());

			Object dwnameObject = map.get("dwName");
			holder.dwTextView.setText(dwnameObject == null || dwnameObject.toString().equals("null")? "" : dwnameObject.toString());

			Object jbrObject = map.get("jbr");
			holder.jbrTextView.setText(jbrObject == null || jbrObject.toString().equals("null")? "" : jbrObject.toString());

			Object ckmcObject = map.get("ckName");
			holder.ckTextView.setText(ckmcObject == null || ckmcObject.toString().equals("null")? "" : ckmcObject.toString());

			Object depnameObject = map.get("depName");
			holder.depNameTextView.setText(depnameObject == null || depnameObject.toString().equals("null")? "" : depnameObject.toString());

			String mvdirectString = map.get("mddirect").toString();
			if (mvdirectString.equals("1")) {
				holder.opTextView.setText("入库");
			} else if (mvdirectString.equals("2")) {
				holder.opTextView.setText("出库");
			} else {
				holder.opTextView.setText(mvdirectString);
			}

			if ((position % 2) == 0) {
				convertView.setBackgroundColor(Color.parseColor("#FFFFFF"));
			} else if ((position % 2) == 1) {
				convertView.setBackgroundColor(Color.parseColor("#E5E5E5"));
			}
			
			return convertView;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if(getItemViewType(position)==1){
				return getcwView(position, convertView, parent);
			}else if(getItemViewType(position)==0){
				return getnocwView(position, convertView, parent);
			}
			return convertView;
		}

		class ViewHolder {
			TextView dhTextView, dateTextView, opTextView, typeTextView, numTextView, djTextView, zjTextView,
					dwTextView, jbrTextView, ckTextView, depNameTextView;
		}
	}

}
