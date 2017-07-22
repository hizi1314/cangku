package com.guantang.cangkuonline.fragment;

import java.io.Serializable;
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
import com.guantang.cangkuonline.XListView.XListView.IXListViewListener;
import com.guantang.cangkuonline.activity.DDdetailActivity;
import com.guantang.cangkuonline.commonadapter.CommonAdapter;
import com.guantang.cangkuonline.commonadapter.ViewHolder;
import com.guantang.cangkuonline.database.DataBaseHelper;
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
import android.widget.TextView;
import android.widget.Toast;

public class CompanyOrderFragment extends Fragment implements IXListViewListener{

	public String dwid = "";
	public SwipeMenuXListView mListView;
	public List<Map<String,Object>> mList = new ArrayList<Map<String,Object>>();
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
	private DDAdapter mDDAdapter;
	private Context context;
	

	public static CompanyOrderFragment getInstance(String dwid) {
		CompanyOrderFragment mCompanyOrderFragment = new CompanyOrderFragment();
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
		View view = inflater.inflate(R.layout.orderstatus_fragment_layout, null);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mListView = (SwipeMenuXListView) getView().findViewById(R.id.djlist);
		mListView.setXListViewListener(this);
		mListView.setPullLoadEnable(true);// 设置可以加载更多
		mListView.setPullRefreshEnable(true);// 设置可以刷新
		
		mDDAdapter = new DDAdapter(context, mList, R.layout.dditem_layout);
		mListView.setAdapter(mDDAdapter);
		
		if (WebserviceImport.isOpenNetwork(context)) {
			new GetCompanyDDAsyncTask().execute("0");
		} else {
			Toast.makeText(context, "网络未连接", Toast.LENGTH_SHORT).show();
		}
	}

	class GetCompanyDDAsyncTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String jsonString = WebserviceImport_more.GetCompanyDD_1_0(6, params[0], -1, dwid);
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
					parseData(jsonObject);
					break;
				case 2://提示数据加载完成
					parseData(jsonObject);
					if(!mList.isEmpty()){
						Toast.makeText(context, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					}
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
	
	public void parseData(JSONObject jsonObject) throws JSONException{
		JSONObject dataJSONObject = jsonObject.getJSONObject("Data");
		JSONArray dsJSONArray = dataJSONObject.getJSONArray("ds");
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
		if(WebserviceImport.isOpenNetwork(context)){
			mList.clear();
			new GetCompanyDDAsyncTask().execute("0");
		}else{
			stopLoadface();
			Toast.makeText(context, "网络未连接", Toast.LENGTH_SHORT).show();
		}
	}
	@Override
	public void onLoadMore() {
		// TODO 自动生成的方法存根
		onLoadTime();
		if(WebserviceImport.isOpenNetwork(context)){
			if(mList.isEmpty()){
				new GetCompanyDDAsyncTask().execute("0");
			}else{
				new GetCompanyDDAsyncTask().execute(mList.get(mList.size()-1).get(DataBaseHelper.ID).toString());
			}
		}else{
			stopLoadface();
			Toast.makeText(context, "网络未连接", Toast.LENGTH_SHORT).show();
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
		mListView.setRefreshTime(refreshDate);
	}

	/**
	 * 停止界面加载动画
	 * */
	public void stopLoadface() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
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


}
