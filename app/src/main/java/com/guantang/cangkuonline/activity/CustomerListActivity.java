package com.guantang.cangkuonline.activity;

import java.io.Serializable;
import java.text.DecimalFormat;
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
import com.guantang.cangkuonline.XListView.XListView;
import com.guantang.cangkuonline.XListView.XListView.IXListViewListener;
import com.guantang.cangkuonline.application.MyApplication;
import com.guantang.cangkuonline.commonadapter.CommonAdapter;
import com.guantang.cangkuonline.commonadapter.ViewHolder;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.helper.RightsHelper;
import com.guantang.cangkuonline.sortlistview.SideBar;
import com.guantang.cangkuonline.webservice.WebserviceImport;
import com.guantang.cangkuonline.webservice.WebserviceImport_more;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CustomerListActivity extends Activity implements OnClickListener,
		IXListViewListener,TextWatcher {

	private ImageButton backImgBtn, addImgBtn;
	private ImageView del_chaImgView;
	private TextView titleTextView;
	private EditText searchEdit;
	private XListView mListView;
	private List<Map<String, Object>> mList = new ArrayList<Map<String, Object>>();
	private String[] str = { "id", "dwName", "py", "tel", "fax", "lxr", "addr",
			"yb", "email", "www", "swdjh", "yh", "zh", "dwType", "ord", "note",
			"country", "state", "city", "area", "areaIndex", "zkl" };
	private MyAdapter mMyAdapter;
	private String dwType = "1";//1:客户，2:供应商，3:即是客户也是供应商
	
	private Semaphore semaphore = new Semaphore(1);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customerlist);
		Intent intent = getIntent();
		dwType = intent.getStringExtra("dwType");
		initView();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		init();
	}

	public void initView() {
		backImgBtn = (ImageButton) findViewById(R.id.back);
		addImgBtn = (ImageButton) findViewById(R.id.add);
		del_chaImgView = (ImageView) findViewById(R.id.del_cha);
		searchEdit = (EditText) findViewById(R.id.searchEdit);
		mListView = (XListView) findViewById(R.id.myListView);
		titleTextView = (TextView) findViewById(R.id.title);

		backImgBtn.setOnClickListener(this);
		addImgBtn.setOnClickListener(this);
		del_chaImgView.setOnClickListener(this);
		searchEdit.setOnClickListener(this);
		searchEdit.addTextChangedListener(this);

		mListView.setXListViewListener(this);
		mListView.setPullLoadEnable(true);// 设置可以加载更多
		mListView.setPullRefreshEnable(true);// 设置可以刷新
		
		if(dwType.equals("1")){
			titleTextView.setText("客户列表");
		}else if(dwType.equals("2")){
			titleTextView.setText("供应商列表");
		}
		mMyAdapter = new MyAdapter(this, mList, R.layout.item_customerlist);
		mListView.setAdapter(mMyAdapter);
	}

	public void init() {
		if (WebserviceImport.isOpenNetwork(this)) {
			mList.clear();
			new GetCustomerAsyncTask().execute("0", "10", searchEdit.getText().toString().trim());
		} else {
			Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.add:
			if(dwType.equals("1")){
				if(RightsHelper.isHaveRight(RightsHelper.dw_addedit_KH, MyApplication.getInstance().getSharedPreferences())){
					intent.setClass(this, AddCustomerAndSupplierActivity.class);
					startActivity(intent);
				}else{
					Toast.makeText(this, "你没有添加客户的权限", Toast.LENGTH_SHORT).show();
				}
				
			}else if(dwType.equals("2")){
				if(RightsHelper.isHaveRight(RightsHelper.dw_addedit_GYS, MyApplication.getInstance().getSharedPreferences())){
					intent.setClass(this, AddCustomerAndSupplierActivity.class);
					startActivity(intent);
				}else{
					Toast.makeText(this, "你没有添加供应商的权限", Toast.LENGTH_SHORT).show();
				}
			}
			break;
		case R.id.del_cha:
			searchEdit.setText("");
			break;
		case R.id.searchEdit:
			intent.setClass(this, SearchActivity.class);
			intent.putExtra("hint", 2);
			startActivityForResult(intent, 1);
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) { // onActivityResult 是在 onResume() 之前调用
		// TODO 自动生成的方法存根
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			if (resultCode == 1) {
				searchEdit.setText(data.getStringExtra("searchString"));
//				init();
			}
		}
	}

	class GetCustomerAsyncTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO 自动生成的方法存根
			String jsonString = WebserviceImport_more.GetCompanyInfo_1_0(
					params[0], params[1], -1,dwType, params[2]);
			return jsonString;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO 自动生成的方法存根
			super.onPostExecute(result);
			stopLoadface();
			try {
				JSONObject jsonObject = new JSONObject(result);
				semaphore.release();
				switch (jsonObject.getInt("Status")) {
				case 1:
					parseData(jsonObject);
					break;
				case 2:// 数据加载完会提示
					parseData(jsonObject);
					Toast.makeText(CustomerListActivity.this,
							jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				default:
					Toast.makeText(CustomerListActivity.this,
							jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				}
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}

	public void parseData(JSONObject jsonObject) throws JSONException {
		JSONObject dataJsonObject = jsonObject.getJSONObject("Data");
		JSONArray dsJsonArray = dataJsonObject.getJSONArray("ds");
		int length = dsJsonArray.length();
		for (int i = 0; i < length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			JSONObject myJsonObject = dsJsonArray.getJSONObject(i);
			for (int j = 0; j < 22; j++) {
				map.put(str[j], myJsonObject.get(str[j]));
			}
			mList.add(map);
		}
		mMyAdapter.setData(mList);
	}

	class MyAdapter extends CommonAdapter<Map<String, Object>> {

		public MyAdapter(Context mContext, List<Map<String, Object>> mList,
				int LayoutId) {
			super(mContext, mList, LayoutId);
			// TODO 自动生成的构造函数存根
		}

		@Override
		public void convert(ViewHolder holder, final Map<String, Object> item) {
			// TODO 自动生成的方法存根
			TextView companyTxtView = holder.getView(R.id.companyTxtView);
			TextView addressTxtView = holder.getView(R.id.addressTxtView);
			TextView lxrTxtView = holder.getView(R.id.lxrTxtView);
			TextView telTxtView = holder.getView(R.id.telTxtView);

			Object dwNameObject = item.get("dwName");
			companyTxtView.setText(dwNameObject == null
					|| dwNameObject.toString().equals("null") ? ""
					: dwNameObject.toString());

			Object addressObject = item.get("addr");
			addressTxtView.setText(addressObject == null
					|| addressObject.toString().equals("null") ? ""
					: addressObject.toString());

			Object lxrObject = item.get("lxr");
			lxrTxtView.setText(lxrObject == null
					|| lxrObject.toString().equals("null") ? "" : lxrObject
					.toString());

			String telObject = item.get("tel").toString();
			if (telObject == null || telObject.equals("null")) {
				telTxtView.setText("");
			} else {
				SpannableString num1 = new SpannableString(telObject);
				num1.setSpan(new URLSpan("tel:" + telObject.toString()), 0,
						telObject.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				telTxtView.setText(num1);
				telTxtView.setMovementMethod(LinkMovementMethod.getInstance());
			}
			
			holder.getConvertView().setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					
					for(int i=0;i<22;i++){
						Object value = item.get(str[i]);
						if(value==null || value.toString().equals("null")){
							item.put(str[i], "");
						}else{
							item.put(str[i], value);
						}
					}
					
					Intent intent = new Intent(CustomerListActivity.this,CustomerAndSupplierDetailActivity.class);
					intent.putExtra("map", (Serializable)item);
					startActivity(intent);
				}
			});

		}

	}

	@Override
	public void onRefresh() {
		// TODO 自动生成的方法存根
		onLoadTime();
		if (WebserviceImport.isOpenNetwork(this)) {
			mList.clear();
			try {
				semaphore.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			new GetCustomerAsyncTask().execute("0", "10", searchEdit.getText()
					.toString().trim());
		} else {
			stopLoadface();
			Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onLoadMore() {
		// TODO 自动生成的方法存根
		onLoadTime();
		if (WebserviceImport.isOpenNetwork(this)) {
			try {
				semaphore.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (mList.isEmpty()) {
				new GetCustomerAsyncTask().execute("0", "10", searchEdit
						.getText().toString().trim());
			} else {
				new GetCustomerAsyncTask().execute(mList.get(mList.size() - 1)
						.get(DataBaseHelper.ID).toString(), "10", searchEdit
						.getText().toString().trim());
			}
		} else {
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
		mListView.setRefreshTime(refreshDate);
	}

	/**
	 * 停止界面加载动画
	 * */
	public void stopLoadface() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO 自动生成的方法存根
		s.toString().replace("'", "");
		mList.clear();
		mMyAdapter.notifyDataSetChanged();
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO 自动生成的方法存根
		if (s.length() > 0) {
			del_chaImgView.setVisibility(View.VISIBLE);
		} else {
			del_chaImgView.setVisibility(View.GONE);
		}
	}

//	@Override
//	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//		// TODO Auto-generated method stub
//		@SuppressWarnings("unchecked")
//		Map<String,Object> map = (Map<String, Object>) arg0.getAdapter().getItem(arg2);
//		
//		for(int i=0;i<22;i++){
//			Object value = map.get(str[i]);
//			if(value==null || value.toString().equals("null")){
//				map.put(str[i], "");
//			}else{
//				map.put(str[i], value);
//			}
//		}
//		
//		Intent intent = new Intent(this,CustomerAndSupplierDetailActivity.class);
//		intent.putExtra("map", (Serializable)map);
//		startActivity(intent);
//	}

}
