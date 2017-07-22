package com.guantang.cangkuonline.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.XListView.XListView;
import com.guantang.cangkuonline.commonadapter.CommonAdapter;
import com.guantang.cangkuonline.commonadapter.ViewHolder;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.webservice.WebserviceImport;
import com.guantang.cangkuonline.webservice.WebserviceImport_more;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DDMXFragment extends Fragment {

	private TextView dircNumNameTxtView;
	private ListView mListView;
	private String orderid;
	private String dirc = "0";
	private String[] str1 = {DataBaseHelper.ID,DataBaseHelper.orderID,DataBaseHelper.HPID,DataBaseHelper.HPBM,DataBaseHelper.HPMC
			,DataBaseHelper.GGXH,DataBaseHelper.SL,DataBaseHelper.DJ,DataBaseHelper.ZJ,DataBaseHelper.zxsl,DataBaseHelper.Notes};
	private String[] str2 = {"id","orderId","hpid","hpbm","hpmc","ggxh","sl","dj","zj","zxsl","notes"};
	private Context context;

	public static DDMXFragment getInstance(String orderid, String dirc) {
		DDMXFragment mDDdmxFragment = new DDMXFragment();
		Bundle bundle = new Bundle();
		bundle.putString("orderid", orderid);
		bundle.putString("dirc", dirc);
		mDDdmxFragment.setArguments(bundle);
		return mDDdmxFragment;
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
		dirc = getArguments().getString("dirc");
		View view = inflater.inflate(R.layout.ddmx_fragment, null);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onActivityCreated(savedInstanceState);

		dircNumNameTxtView = (TextView) getView().findViewById(
				R.id.dircNumNameTxtView);
		mListView = (ListView) getView().findViewById(R.id.list);

		if (dirc.equals("0")) {
			dircNumNameTxtView.setText("已入数量");
		} else if (dirc.equals("1")) {
			dircNumNameTxtView.setText("已出数量");
		}

		if (WebserviceImport.isOpenNetwork(context)) {
			new GetDDDetailAsyncTask().execute(orderid);
		} else {
			Toast.makeText(context, "网络未连接", Toast.LENGTH_SHORT).show();
		}

	}

	class GetDDDetailAsyncTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO 自动生成的方法存根
			String jsonOString = WebserviceImport_more
					.GetDDDetail_1_0(params[0]);
			return jsonOString;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO 自动生成的方法存根
			super.onPostExecute(result);
			try {
				JSONObject jsonObject = new JSONObject(result);
				switch (jsonObject.getInt("Status")) {
				case 1:
					List<Map<String,Object>> mList = new ArrayList<Map<String,Object>>();
					JSONArray dataJSONArray = jsonObject.getJSONArray("Data");
					int length = dataJSONArray.length();
					for(int i = 0; i < length ; i++ ){
						Map<String,Object> map = new HashMap<String,Object>();
						JSONObject dataJSONObject = dataJSONArray.getJSONObject(i);
						for(int j = 0; j<11; j++){
							map.put(str1[j], dataJSONObject.get(str2[j]));
						}
						mList.add(map);
					}
					MyAdapter myAdapter = new MyAdapter(context, mList, R.layout.item_ddmx_layout);
					mListView.setAdapter(myAdapter);
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
	
	class MyAdapter extends CommonAdapter<Map<String,Object>>{

		public MyAdapter(Context mContext, List<Map<String, Object>> mList,
				int LayoutId) {
			super(mContext, mList, LayoutId);
			// TODO 自动生成的构造函数存根
		}

		@Override
		public void convert(ViewHolder holder, Map<String, Object> item) {
			// TODO 自动生成的方法存根
			TextView hpbmTxtView = holder.getView(R.id.hpbmTxtView);
			TextView hpmcTxtView = holder.getView(R.id.hpmcTxtView);
			TextView ggxhTxtView = holder.getView(R.id.ggxhTxtView);
			TextView ddslTxtView = holder.getView(R.id.ddslTxtView);
			TextView zxslTxtView = holder.getView(R.id.zxslTxtView);
			TextView djTxtView = holder.getView(R.id.djTxtView);
			TextView jeTxtView = holder.getView(R.id.jeTxtView);
			TextView bzTxtView = holder.getView(R.id.bzTxtView);
			
			Object hpbmObject = item.get(DataBaseHelper.HPBM);
			hpbmTxtView.setText(hpbmObject.toString().equals("null")?"":hpbmObject.toString());
			
			Object hpmcObject = item.get(DataBaseHelper.HPMC);
			hpmcTxtView.setText(hpmcObject.toString().equals("null")?"":hpmcObject.toString());
			
			Object ggxhObject = item.get(DataBaseHelper.GGXH);
			ggxhTxtView.setText(ggxhObject.toString().equals("null")?"":hpbmObject.toString());
			
			Object ddslObject = item.get(DataBaseHelper.SL);
			ddslTxtView.setText(ddslObject.toString().equals("null")?"0":ddslObject.toString());
			
			Object zxslObject = item.get(DataBaseHelper.zxsl);
			zxslTxtView.setText(zxslObject.toString().equals("null")?"0":zxslObject.toString());
			
			Object djObject = item.get(DataBaseHelper.DJ);
			djTxtView.setText(djObject.toString().equals("null")?"0":djObject.toString());
			
			Object jeObject = item.get(DataBaseHelper.ZJ);
			jeTxtView.setText(jeObject.toString().equals("null")?"0":jeObject.toString());
			
			Object bzObject = item.get(DataBaseHelper.Notes);
			bzTxtView.setText(bzObject.toString().equals("null")?"":bzObject.toString());
		}
		
	}
	
}
