package com.guantang.cangkuonline.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.application.MyApplication;
import com.guantang.cangkuonline.commonadapter.CommonAdapter;
import com.guantang.cangkuonline.commonadapter.ViewHolder;
import com.guantang.cangkuonline.helper.DecimalsHelper;
import com.guantang.cangkuonline.webservice.WebserviceImport;
import com.guantang.cangkuonline.webservice.WebserviceImport_more;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TransdDetailActivity extends Activity {
	private ImageButton backImgBtn;
	private TextView dhTextView,dateTextView,diaochuTextView,diaoruTextView,jbrTextView,hpnamesTextView,bzTextView;
	private String djid;
	private List<JSONObject> mlist = new ArrayList<JSONObject>();
	private ListView myListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transddetail);
		initView();
		init();
	}
	
	public void initView(){
		backImgBtn = (ImageButton) findViewById(R.id.back);
		dhTextView = (TextView) findViewById(R.id.dhTextView);
		dateTextView = (TextView) findViewById(R.id.dateTextView);
		diaochuTextView = (TextView) findViewById(R.id.diaochuTextView);
		diaoruTextView = (TextView) findViewById(R.id.diaoruTextView);
		jbrTextView = (TextView) findViewById(R.id.jbrTextView);
		hpnamesTextView = (TextView) findViewById(R.id.hpnamesTextView);
		bzTextView = (TextView) findViewById(R.id.bzTextView);
		myListView = (ListView) findViewById(R.id.myListView);
		
		backImgBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	
	public void init(){
		Intent intent = getIntent();
		String jsonString = intent.getStringExtra("jsonObject");
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			djid = jsonObject.getString("id");
			dhTextView.setText(jsonObject.getString("mvdh"));
			dateTextView.setText(jsonObject.getString("mvdt"));
			diaochuTextView.setText(jsonObject.getString("sckName"));
			diaoruTextView.setText(jsonObject.getString("dckName"));
			jbrTextView.setText(jsonObject.getString("jbr"));
			hpnamesTextView.setText(jsonObject.getString("hpnames"));
			bzTextView.setText(jsonObject.getString("bz"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(WebserviceImport.isOpenNetwork(this)){
			new GettransdAsyncTask().execute(djid);
		}else{
			Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
		}
	}
	
	class GettransdAsyncTask extends AsyncTask<String,Void,String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String jsonString = WebserviceImport_more.Gettransd_1_0(params[0]);
			return jsonString;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				JSONObject jsonObject = new JSONObject(result);
				switch(jsonObject.getInt("Status")){
				case 1:
					JSONObject dataJSONObject = jsonObject.getJSONObject("Data");
					JSONArray dsJSONArray = dataJSONObject.getJSONArray("ds");
					int length = dsJSONArray.length();
					for(int i = 0; i<length;i++){
						JSONObject myJsonObject = dsJSONArray.getJSONObject(i);
						mlist.add(myJsonObject);
					}
					MyAdapter mMyAdapter = new MyAdapter(TransdDetailActivity.this, mlist, R.layout.djdetail_item);
					myListView.setAdapter(mMyAdapter);
				break;
				default:
					Toast.makeText(TransdDetailActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
				break;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	class MyAdapter extends CommonAdapter<JSONObject>{

		public MyAdapter(Context mContext, List<JSONObject> mList, int LayoutId) {
			super(mContext, mList, LayoutId);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void convert(ViewHolder holder, JSONObject item) {
			// TODO Auto-generated method stub
			TextView bmTextView = holder.getView(R.id.bm);
			TextView mcTextView = holder.getView(R.id.mc);
			TextView ggTextView = holder.getView(R.id.gg);
			TextView jldwTextView = holder.getView(R.id.jldw);
			TextView numTextView = holder.getView(R.id.num);
			TextView zjTextView = holder.getView(R.id.zj);
			TextView djTextView = holder.getView(R.id.dj);
			
			try {
				String HPBMString = item.getString("HPBM");
				bmTextView.setText((HPBMString==null || HPBMString.equals("null"))?"":HPBMString);
				String HPMCString = item.getString("HPMC");
				mcTextView.setText((HPMCString==null || HPMCString.equals("null"))?"":HPMCString);
				String GGXHString = item.getString("GGXH");
				ggTextView.setText((GGXHString==null || GGXHString.equals("null"))?"":GGXHString);
				String JLDWString = item.getString("JLDW");
				jldwTextView.setText((JLDWString==null || JLDWString.equals("null"))?"":JLDWString);
				String slString = item.getString("sl");
				numTextView.setText((slString==null || slString.equals("null"))?"":DecimalsHelper.Transfloat(Double.parseDouble(slString.toString()),MyApplication.getInstance().getNumPoint()));
				String zjString = item.getString("zj");
				zjTextView.setText((zjString==null || zjString.equals("null"))?"":DecimalsHelper.Transfloat(Double.parseDouble(zjString.toString()),MyApplication.getInstance().getJePoint()));
				String djString = item.getString("dj");
				djTextView.setText((djString==null || djString.equals("null"))?"":DecimalsHelper.Transfloat(Double.parseDouble(djString.toString()),MyApplication.getInstance().getJePoint()));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
}
