package com.guantang.cangkuonline.activity;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.application.MyApplication;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.database.DataBaseOperateMethod;
import com.guantang.cangkuonline.helper.RightsHelper;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;
import com.guantang.cangkuonline.webservice.WebserviceImport;


public class DwListActivity extends Activity implements OnClickListener,OnItemClickListener,TextWatcher{
	private ListView mListView;
	private TextView mTextView;
	private ImageButton backImgBtn,addImgBtn;
	private ImageView del_chaImgView;
	private EditText listEdit;
	private List<Map<String,Object>> ls=new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> searchList = new ArrayList<Map<String,Object>>();
	private String dw_id="";
	private String[] str1={DataBaseHelper.ID,DataBaseHelper.DWName,DataBaseHelper.ADDR,DataBaseHelper.FAX,
			DataBaseHelper.YB,DataBaseHelper.Net,DataBaseHelper.LXR,DataBaseHelper.TEL,
			DataBaseHelper.Email,DataBaseHelper.BZ,DataBaseHelper.DWtype};
	private String[] str2={"id","dwName","addr","fax","yb","www","lxr","tel","email","bz","dwType"};
	private ProgressDialog pro_Dialog;
	private DataBaseOperateMethod  dm_op=new DataBaseOperateMethod(this);
	private SharedPreferences mSharedPreferences;
	private MyAdapter mMyAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_dw_list);
		mSharedPreferences = getSharedPreferences(ShareprefenceBean.SHAREPREFENCE_NAME,Context.MODE_PRIVATE);
		initcontrol();
		init();
	}
	public void initcontrol(){
		backImgBtn=(ImageButton) findViewById(R.id.back);
		mListView=(ListView) findViewById(R.id.dw_list);
		mTextView=(TextView) findViewById(R.id.showtext);
		listEdit=(EditText) findViewById(R.id.listtext);
		del_chaImgView = (ImageView) findViewById(R.id.del_cha);
		addImgBtn = (ImageButton) findViewById(R.id.add);
		listEdit.addTextChangedListener(this);
		mListView.setOnItemClickListener(this);
		backImgBtn.setOnClickListener(this);
		del_chaImgView.setOnClickListener(this);
		addImgBtn.setOnClickListener(this);
		
		mMyAdapter = new MyAdapter(this);
	}
	public void init(){
		if(mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1)==1){
			if(WebserviceImport.isOpenNetwork(this)){
				pro_Dialog=ProgressDialog.show(this, null, "正在获取数据……");
				new Thread(run).start();
			}else{
				Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
			}
		}else if(mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1)==-1){
			ls=dm_op.Gt_cp(str1);
			if(!ls.isEmpty()){
				mMyAdapter.setData(ls);
				mListView.setAdapter(mMyAdapter);
			}
		}
	}
	Runnable run = new Runnable(){
		@Override
		public void run() {
			// TODO 自动生成的方法存根
			Message msg=Message.obtain();
			List<Map<String,Object>> list;
			list=WebserviceImport.GetDW(-1, "0", str1, str2,mSharedPreferences);
			msg.obj=list;
			mhandler.sendMessage(msg);
		}
		
	};
	Handler mhandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			ls=(List<Map<String, Object>>) msg.obj;
			mMyAdapter.setData(ls);
			mListView.setAdapter(mMyAdapter);
			pro_Dialog.dismiss();
		};
	};
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch(v.getId()){
		case R.id.back:
			finish();
			break;
		case R.id.del_cha:
			listEdit.setText("");
			break;
		case R.id.add:
			if(RightsHelper.isHaveRight(RightsHelper.dw_addedit_GYS, MyApplication.getInstance().getSharedPreferences())){
				Intent intent = new Intent();
				intent.setClass(this, AddCustomerAndSupplierActivity.class);
				startActivity(intent);
				finish();
			}else{
				Toast.makeText(this, "你没有添加供应商的权限", Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO 自动生成的方法存根
		
		Map<String, Object> map = (Map<String, Object>) arg0.getAdapter().getItem(arg2);
		dw_id=map.get(DataBaseHelper.ID).toString();
		if(dw_id==null){
			dw_id="";
		}
		Intent intent=new Intent();
		intent.putExtra("dwmap", (Serializable) map);
		intent.putExtra("dwid", dw_id);
		setResult(1, intent);
		finish();
	}
	
	@Override
	public void afterTextChanged(Editable s) {
		// TODO 自动生成的方法存根
		if (s.length() > 0) {
			del_chaImgView.setVisibility(View.VISIBLE);
		} else {
			del_chaImgView.setVisibility(View.GONE);
		}
	}
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO 自动生成的方法存根
		
	}
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO 自动生成的方法存根
		searchList.clear();
		String textString = s.toString();
		Iterator<Map<String, Object>> it = ls.iterator();
		while(it.hasNext()){
			Map<String,Object> map = it.next();
			if(map.get(DataBaseHelper.DWName).toString().contains(textString)){
				searchList.add(map);
			}
		}
		mMyAdapter.setData(searchList);
	}
	
	class MyAdapter extends BaseAdapter{
		private List<Map<String, Object>> mList = new ArrayList<Map<String,Object>>();
		private LayoutInflater inflater;
		private Context context;
		public MyAdapter(Context context){
			this.context = context;
			inflater = LayoutInflater.from(context);
		}
		public void setData(List<Map<String, Object>> mList){
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
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO 自动生成的方法存根
			TextView mTextView = (TextView) inflater.inflate(R.layout.list_item, null);
			Map<String, Object> map = mList.get(position);
			mTextView.setText(map.get(DataBaseHelper.DWName).toString());
			return mTextView;
		}
		
	}
}
