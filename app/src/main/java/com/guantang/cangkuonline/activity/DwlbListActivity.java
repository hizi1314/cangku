package com.guantang.cangkuonline.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.commonadapter.CommonAdapter;
import com.guantang.cangkuonline.commonadapter.ViewHolder;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.mulu.ElementBean;
import com.guantang.cangkuonline.mulu.MuLuFlowLayout;
import com.guantang.cangkuonline.mulu.MuluAdapter;
import com.guantang.cangkuonline.webservice.WebserviceImport;
import com.guantang.cangkuonline.webservice.WebserviceImport_more;

import de.greenrobot.event.EventBus;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class DwlbListActivity extends Activity implements OnClickListener,OnItemClickListener {

	private ImageButton backImgBtn,addImgBtn;
	private TextView dingcengTxtView;
	private TextView resetTextView, confirmTextView;
	private ListView mListView;
	private HorizontalScrollView hScrollView;
	private MuluAdapter mMuluAdapter;
	private MuLuFlowLayout mMuLuFlowLayout;
	private ListAdapter mListAdapter;
	private List<Map<String, Object>> mList = new ArrayList<Map<String, Object>>();
	private List<Map<String,Object>> littleList = new ArrayList<Map<String,Object>>();
	private String[] str = { "id", "PID", "dworder", "dwindex", "dwlevel",
			"name" };
	private String pid = "0";
	private String dwname = "";
	private String dwindex = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dep_chose);
		EventBus.getDefault().register(this);
		initView();
		init();
	}
	
	@Override
	protected void onDestroy() {
		// TODO 自动生成的方法存根
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	public void initView() {
		backImgBtn = (ImageButton) findViewById(R.id.back);
		dingcengTxtView = (TextView) findViewById(R.id.dingcengTxtView);
		resetTextView = (TextView) findViewById(R.id.resetView);
		confirmTextView = (TextView) findViewById(R.id.confirmView);
		mListView = (ListView) findViewById(R.id.list);
		hScrollView = (HorizontalScrollView) findViewById(R.id.hScrollView);
		mMuLuFlowLayout = (MuLuFlowLayout) findViewById(R.id.firstLagFlowLayout);
		addImgBtn = (ImageButton) findViewById(R.id.add);
		
		addImgBtn.setVisibility(View.INVISIBLE);

		backImgBtn.setOnClickListener(this);
		dingcengTxtView.setOnClickListener(this);
		resetTextView.setOnClickListener(this);
		confirmTextView.setOnClickListener(this);
		mListView.setOnItemClickListener(this);
		
		mMuluAdapter = new MuluAdapter(this);
		mMuLuFlowLayout.setAdapter(mMuluAdapter);
		
	}

	public void init() {
		
		mListAdapter = new ListAdapter(this, littleList, R.layout.lbchoseitem);
		mListView.setAdapter(mListAdapter);
		
		if (WebserviceImport.isOpenNetwork(this)) {
			new DwlbAsyncTask().execute();
		} else {
			Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void onEventMainThread(ElementBean bean) {
		littleList.clear();
		pid = bean.getId();
		Iterator<Map<String,Object>> it = mList.iterator();
		while(it.hasNext()){
			Map<String,Object> map1 = it.next();
			if(map1.get("PID").toString().equals(pid)){
				littleList.add(map1);
			}
		}
		mListAdapter.setData(littleList);
		GetDwname(pid);
	}
	
	class DwlbAsyncTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			// TODO 自动生成的方法存根
			String jsonString = WebserviceImport_more.GetRdwTree_1_0();
			return jsonString;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO 自动生成的方法存根
			super.onPostExecute(result);
			try {
				JSONObject jsonObject = new JSONObject(result);
				switch (jsonObject.getInt("Status")) {
				case 1:
					JSONArray dataJsonArray = jsonObject.getJSONArray("Data");
					int length = dataJsonArray.length();
					for(int i=0;i<length;i++){
						Map<String,Object> map = new HashMap<String, Object>();
						JSONObject myJSONObject = dataJsonArray.getJSONObject(i);
						for(int j = 0; j<6; j++){
							map.put(str[j], myJSONObject.get(str[j]));
						}
						mList.add(map);
					}
					littleList.clear();
					Iterator<Map<String,Object>> it = mList.iterator();
					while(it.hasNext()){
						Map<String,Object> map = it.next();
						if(map.get("PID").toString().equals("0")){
							littleList.add(map);
						}
					}
					mListAdapter.setData(littleList);
					break;
				default:
					Toast.makeText(DwlbListActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				}
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO 自动生成的方法存根
		Map<String, Object> map = (Map<String, Object>) arg0.getAdapter().getItem(arg2);
		pid = map.get(DataBaseHelper.ID).toString();
		
		ElementBean elementBean = new ElementBean();
		elementBean.setPid(map.get(DataBaseHelper.PID).toString());
		elementBean.setId(map.get(DataBaseHelper.ID).toString());
		elementBean.setName(map.get(DataBaseHelper.Name).toString());
		elementBean.setLev(Integer.parseInt(map.get("dwlevel").toString()));
		mMuluAdapter.addData(elementBean);
		hScrollView.setFillViewport(true);
		
		hScrollView.post(new Runnable() {//开启一个线程滑动到最末端
			
			@Override
			public void run() {
				// TODO 自动生成的方法存根
				hScrollView.fullScroll(ScrollView.FOCUS_RIGHT);
//				hScrollView.fullScroll(ScrollView.FOCUS_DOWN);
			}
		});
		littleList.clear();
		Iterator<Map<String,Object>> it = mList.iterator();
		while(it.hasNext()){
			Map<String,Object> map1 = it.next();
			if(map1.get("PID").toString().equals(pid)){
				littleList.add(map1);
			}
		}
		mListAdapter.setData(littleList);
		GetDwname(pid);
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.dingcengTxtView:
			littleList.clear();
			mMuluAdapter.addData(new ArrayList<ElementBean>());
			Iterator<Map<String,Object>> it = mList.iterator();
			while(it.hasNext()){
				Map<String,Object> map = it.next();
				if(map.get("PID").toString().equals("0")){
					littleList.add(map);
				}
			}
			mListAdapter.setData(littleList);
			pid = "0";
			dwname = "";
			dwindex = "";
			break;
		case R.id.resetView:
			intent.putExtra("dwName", "");
			intent.putExtra("dwindex", "");
			setResult(1, intent);
			finish();
			break;
		case R.id.confirmView:
			intent.putExtra("dwName", dwname);
			intent.putExtra("dwindex", dwindex);
			setResult(1, intent);
			finish();
			break;
		}
	}
	
	public void GetDwname(String id){
		Iterator<Map<String,Object>> it = mList.iterator();
		while(it.hasNext()){
			Map<String,Object> map = it.next();
			if(map.get("id").toString().equals(id)){
				dwindex = map.get("dwindex").toString();
//				dwname=map.get("name").toString();
				dwname = mMuluAdapter.getListString();
				break;
			}else{
				dwindex = "";
				dwname = "";
			}
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if(!pid.equals("0")){
				pid = mMuluAdapter.getLastElementBean().getPid();
				Iterator<Map<String,Object>> it = mList.iterator();
				while(it.hasNext()){
					Map<String,Object> map = it.next();
					if(map.get("PID").toString().equals(pid)){
						littleList.add(map);
					}
				}
				mListAdapter.setData(littleList);
				mMuluAdapter.removeLastElementBean();
				if(pid.equals("0")){
					dwname = "";
					dwindex = "";
				}else{
					GetDwname(pid);
				}
				return false;
			}else{
				finish();
				return true;
			}
		}else{
			return false;
		}
//		return super.onKeyDown(keyCode, event);
	}
	
	class ListAdapter extends CommonAdapter<Map<String,Object>>{

		public ListAdapter(Context mContext, List<Map<String, Object>> mList,
				int LayoutId) {
			super(mContext, mList, LayoutId);
			// TODO 自动生成的构造函数存根
		}

		@Override
		public void convert(ViewHolder holder, Map<String, Object> item) {
			// TODO 自动生成的方法存根
			TextView nameTexView = holder.getView(R.id.lbitem);
			String name = item.get("name").toString();
			nameTexView.setText(name==null||name.equals("null")?"":name);
		}
		
	}
	
}
