package com.guantang.cangkuonline.activity;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.application.MyApplication;
import com.guantang.cangkuonline.database.DataBaseCheckMethod;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.database.DataBaseImport;
import com.guantang.cangkuonline.database.DataBaseOperateMethod;
import com.guantang.cangkuonline.dialog.AddtypeDialog;
import com.guantang.cangkuonline.dialog.AddtypeDialog.OnMyClickListener;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;
import com.guantang.cangkuonline.webservice.WebserviceImport;
import com.guantang.cangkuonline.webservice.WebserviceImport_more;

public class SettingChuRuTypeActivity extends Activity implements OnClickListener{
	private ImageButton backImgBtn,addtypeImgBtn;
	private TextView titleTextView;
	private ListView typeListView;
	private String[] str={DataBaseHelper.GID,DataBaseHelper.ItemV};
	private String[] str1={"dirc","name"};
	private int type=1;//1入库类型，2出库类型。
	private DataBaseOperateMethod dm_op = new DataBaseOperateMethod(this);
	private DataBaseCheckMethod dm_ck=new DataBaseCheckMethod(this);
	private MyAdapter myAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settingchurutype);
		Intent intent = getIntent();
		type = intent.getIntExtra("type", 1);
		initView();
		init();
	}
	@Override
	protected void onStart() {
		// TODO 自动生成的方法存根
		super.onStart();
		init();
	}
	public void initView(){
		backImgBtn = (ImageButton) findViewById(R.id.back);
		addtypeImgBtn = (ImageButton) findViewById(R.id.addtype);
		titleTextView = (TextView) findViewById(R.id.title);
		typeListView = (ListView) findViewById(R.id.typeListView);
		
		backImgBtn.setOnClickListener(this);
		addtypeImgBtn.setOnClickListener(this);
		
		if(type == 1){
			titleTextView.setText("入库类型");
		}else if(type == 2){
			titleTextView.setText("出库类型");
		}
		myAdapter = new MyAdapter();
		typeListView.setAdapter(myAdapter);
	}
	
	public void init(){
		if(MyApplication.getInstance().getSharedPreferences().getInt(ShareprefenceBean.IS_LOGIN, -1)==1){
			if(WebserviceImport.isOpenNetwork(this)){
				new MyAsyncTask().execute();
			}else{
				Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
			}
		}else if(MyApplication.getInstance().getSharedPreferences().getInt(ShareprefenceBean.IS_LOGIN, -1)==-1){
			List<Map<String, Object>> mList = new ArrayList<Map<String,Object>>();
			switch(type){
			case 1:
				mList=dm_ck.Gt_Type("入库类型",str[1]);
				break;
			case 2:
				mList=dm_ck.Gt_Type("出库类型",str[1]);
				break;
			}
			if(mList.size()>0){
				myAdapter.setData(mList);
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch(v.getId()){
		case R.id.back:
			finish();
			break;
		case R.id.addtype:
			if(MyApplication.getInstance().getSharedPreferences().getInt(ShareprefenceBean.IS_LOGIN, -1)==1){
				if(!MyApplication.getInstance().getSharedPreferences().getString(ShareprefenceBean.DWNAME_LOGIN, "").equals("测试") && MyApplication.getInstance().getSharedPreferences().getInt(ShareprefenceBean.TIYANZHANGHAO, 0)!=1){
					addtypeDialog();
				}else{
					Toast.makeText(this, "测试账户不能添加出入库类型", Toast.LENGTH_SHORT).show();
				}
			}else if(MyApplication.getInstance().getSharedPreferences().getInt(ShareprefenceBean.IS_LOGIN, -1)==-1){
				Toast.makeText(SettingChuRuTypeActivity.this, "离线模式不能添加出入库类型", Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}
	
	public void addtypeDialog(){
//		AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.yuanjiao_activity);
//		LayoutInflater inflater = LayoutInflater.from(this);
//		View view = inflater.inflate(R.layout.dialog_addtype, null);
//		TextView dialogTitle = (TextView) view.findViewById(R.id.dialogTitle);
//		final EditText typeEditText = (EditText) view.findViewById(R.id.typeEditText);
//		final EditText prevEditText = (EditText) view.findViewById(R.id.prevEditText);
//		final TextView cancelTextView = (TextView) view.findViewById(R.id.cancel);
//		final TextView confirmTextView = (TextView) view.findViewById(R.id.confirm);
//		if(type==1){
//			dialogTitle.setText("添加入库类型");
//			prevEditText.setText("RK-");
//		}else if(type==2){
//			dialogTitle.setText("添加出库类型");
//			prevEditText.setText("CK-");
//		}
//		cancelTextView.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO 自动生成的方法存根
//				dialog.dismiss();
//			}
//		});
//		confirmTextView.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO 自动生成的方法存根
//				if(WebserviceImport.isOpenNetwork(SettingChuRuTypeActivity.this)){
//					new addtypeAsyncTask().execute(typeEditText.getText().toString().trim(),prevEditText.getText().toString().trim());
//					dialog.dismiss();
//				}else{
//					Toast.makeText(SettingChuRuTypeActivity.this, "网络未连接", Toast.LENGTH_SHORT).show();
//				}
//			}
//		});
//		builder.setView(view);
//		dialog=builder.create();
//		dialog.show();
		
		final AddtypeDialog addtypeDialog = new AddtypeDialog(this, type,R.style.yuanjiao_activity);
		addtypeDialog.setOnMyClicklistener(new OnMyClickListener() {
			
			@Override
			public void execute(String typeString, String prevString) {
				// TODO 自动生成的方法存根
				if(WebserviceImport.isOpenNetwork(SettingChuRuTypeActivity.this)){
					new addtypeAsyncTask().execute(addtypeDialog.getTypeString(),addtypeDialog.getPrevString());
					addtypeDialog.dismiss();
				}else{
					Toast.makeText(SettingChuRuTypeActivity.this, "网络未连接", Toast.LENGTH_SHORT).show();
				}
			}
		});
		addtypeDialog.setCancelable(false);
		addtypeDialog.show();
	}
	
	class MyAsyncTask extends AsyncTask<Void, Void, String>{

		@Override
		protected String doInBackground(Void... params) {
			// TODO 自动生成的方法存根
			String jsonString=WebserviceImport_more.GetSingeIOType_1_0(type, MyApplication.getInstance().getSharedPreferences());
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
					List<Map<String, Object>> mList = new ArrayList<Map<String,Object>>();
					JSONArray dataArray = jsonObject.getJSONArray("Data");
					if(dataArray.length()>0){
						if(type == 1){
							dm_op.Del_Conf(new String[] { "入库类型" }, DataBaseImport.getInstance(SettingChuRuTypeActivity.this).getReadableDatabase());
						}else if(type == 2){
							dm_op.Del_Conf(new String[] { "出库类型" }, DataBaseImport.getInstance(SettingChuRuTypeActivity.this).getReadableDatabase());
						}
						for(int i=0;i<dataArray.length();i++){
							JSONObject object = dataArray.getJSONObject(i);
							Map<String, Object> map = new HashMap<String, Object>();
							if(object.getInt(str1[0])==1){
								map.put(str[0], "入库类型");
								map.put(str[1], object.get(str1[1]));
							}else if(object.getInt(str1[0])==2){
								map.put(str[0], "出库类型");
								map.put(str[1], object.get(str1[1]));
							}
							dm_op.insert_into(DataBaseHelper.TB_Conf, str, new String[]{map.get(str[0]).toString(),map.get(str[1]).toString()});
							mList.add(map);
						}
					}
					
					myAdapter.setData(mList);

					Toast.makeText(SettingChuRuTypeActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -1:
					Toast.makeText(SettingChuRuTypeActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -2:
					Toast.makeText(SettingChuRuTypeActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				}
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}
	
	class addtypeAsyncTask extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO 自动生成的方法存根
			String jsonString = WebserviceImport_more.AddIotype_1_0(params[0], type, params[1], MyApplication.getInstance().getSharedPreferences());
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
					if(WebserviceImport.isOpenNetwork(SettingChuRuTypeActivity.this)){
						new MyAsyncTask().execute();
						Toast.makeText(SettingChuRuTypeActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(SettingChuRuTypeActivity.this, "网络未连接,没有刷新成功", Toast.LENGTH_SHORT).show();
					}
					break;
				case -1:
					Toast.makeText(SettingChuRuTypeActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -2:
					Toast.makeText(SettingChuRuTypeActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -3:
					Toast.makeText(SettingChuRuTypeActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -4:
					Toast.makeText(SettingChuRuTypeActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				default:
					Toast.makeText(SettingChuRuTypeActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				}
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			
		}
	}
	
	class MyAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private List<Map<String, Object>> mlist = new ArrayList<Map<String,Object>>();
		public MyAdapter(){
			inflater = LayoutInflater.from(SettingChuRuTypeActivity.this);
		}
		
		public void setData(List<Map<String, Object>> mlist){
			this.mlist=mlist;
			notifyDataSetChanged();
		}
		
		@Override
		public int getCount() {
			// TODO 自动生成的方法存根
			return mlist.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO 自动生成的方法存根
			return mlist.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO 自动生成的方法存根
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO 自动生成的方法存根
			TextView mTextView = null;
			if(convertView==null){
				convertView = inflater.inflate(R.layout.lbchoseitem, null);
				mTextView = (TextView) convertView.findViewById(R.id.lbitem);
				convertView.setTag(mTextView);
			}else{
				mTextView = (TextView) convertView.getTag();
			}
			
			Map<String, Object> map = mlist.get(position);
			
			Object nameObject = map.get(str[1]);
			mTextView.setText(nameObject==null?"":nameObject.toString());
			return convertView;
		}
		
	}
}
