package com.guantang.cangkuonline.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.activity.LoginActivity.LoginAsyncTask;
import com.guantang.cangkuonline.adapter.ItemCanDeleteAdapter;
import com.guantang.cangkuonline.application.MyApplication;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.database.DataBaseOperateMethod;
import com.guantang.cangkuonline.dialog.CommonDialog;
import com.guantang.cangkuonline.dialog.CommonDialog.DialogContentListener;
import com.guantang.cangkuonline.eventbusBean.ObjectOne;
import com.guantang.cangkuonline.helper.EditHelper;
import com.guantang.cangkuonline.helper.PwdHelper;
import com.guantang.cangkuonline.helper.UpdateVersion;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;
import com.guantang.cangkuonline.static_constant.PathConstant;
import com.guantang.cangkuonline.webservice.WebserviceHelper;
import com.guantang.cangkuonline.webservice.WebserviceImport;
import com.guantang.cangkuonline.webservice.WebserviceImport_more;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengDialogButtonListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import de.greenrobot.event.EventBus;

public class NewLoginActivity extends Activity implements OnClickListener,OnCheckedChangeListener{
	
	private TextView serverTxt;
	private EditText companyEdit,usernameAutoCompleteTextView,passwordAutoCompleteTextView;
	private Button loginBtn,lixianloginBtn;
	private CheckBox eyeCheckBox;
	private LinearLayout serverlayout,tiyanLinearLayout,registerlayout;
	private ImageView downImgView;
	private SharedPreferences mSharedPreferences;
	private String[] url_array;
	private String[] ser_array;
	private ProgressDialog pro_dialog;
	private PopupWindow mserverPopupWindow,downPopupWindow;
	private Boolean isdateup = true;// 是否更新
	private String username="",password="",dwname="";
	private PwdHelper pwdhelper = new PwdHelper();
	private ExecutorService cacheThreadPool = Executors.newCachedThreadPool();
	private DataBaseOperateMethod dm_op = new DataBaseOperateMethod(this);
	private boolean tiyanflag = false;//如果上次登录是tiyan账户 true,上次登录不是体验账户 false;
	/**
	 * 点击输入框的下拉三角符弹出的popupwindow里面数据的适配器
	 * */
	private ItemCanDeleteAdapter mItemCanDeleteAdapter;
	/**
	 * 是否是注册页面返回到本登陆界面
	 * */
	private Boolean registerstartFlagBoolean = false;
	/**
	 * 权限字符串
	 * */
	private String rightString = "";
	/**
	 * 参数设置最后一次修改时间
	 * */
	private String serConfLastModifyTime = "";
	
	private int selectServerNum=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_newlogin);
		overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
		
		EventBus.getDefault().register(this);
		mSharedPreferences = MyApplication.getInstance().getSharedPreferences();
		initView();
	}
	
	@Override
	public void onBackPressed() {
		// TODO 自动生成的方法存根
		super.onBackPressed();
		overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
	}
	
	public void initView(){
		serverTxt=(TextView) findViewById(R.id.serverTxt);
		companyEdit = (EditText) findViewById(R.id.companyEdit);
		usernameAutoCompleteTextView = (EditText) findViewById(R.id.usernameEdit);
		passwordAutoCompleteTextView = (EditText) findViewById(R.id.passwordEdit);
		loginBtn = (Button) findViewById(R.id.loginBtn);
		lixianloginBtn = (Button) findViewById(R.id.lixianloginBtn);
		eyeCheckBox = (CheckBox) findViewById(R.id.eye);
		tiyanLinearLayout = (LinearLayout) findViewById(R.id.tiyanlayout);
		registerlayout = (LinearLayout) findViewById(R.id.registerlayout);
		serverlayout = (LinearLayout) findViewById(R.id.serverlayout);
		downImgView = (ImageView) findViewById(R.id.downImgView);
		
		
		serverlayout.setOnClickListener(this);
		loginBtn.setOnClickListener(this);
		lixianloginBtn.setOnClickListener(this);
		tiyanLinearLayout.setOnClickListener(this);
		registerlayout.setOnClickListener(this);
		downImgView.setOnClickListener(this);
		eyeCheckBox.setOnCheckedChangeListener(this);
		
		if(mSharedPreferences.getBoolean(ShareprefenceBean.SHOWPASS, true)){
			eyeCheckBox.setChecked(true);
		}else{
			eyeCheckBox.setChecked(false);
		}
	}
	
	public void init(){
//		serverTxt.setText(mSharedPreferences.getString(
//				ShareprefenceBean.SER_NAME, ""));
//		String serTextString = serverTxt.getText().toString();
		int num = mSharedPreferences.getInt(ShareprefenceBean.SERVER_NUM, -1);
		selectServerNum = num;
		if(num>=0 && num<=3){
			serverTxt.setText(ser_array[num]);
			WebserviceHelper.URL = EditHelper.CheckHttp(url_array[num])+ url_array[num] + "/";
			if(num==3){
				companyEdit.setText(mSharedPreferences.getString(ShareprefenceBean.IDN_ALONE_URL, ""));
				WebserviceHelper.loginflag = 0;
			}else{
				companyEdit.setText(mSharedPreferences.getString(ShareprefenceBean.DWNAME_LOGIN, ""));
				WebserviceHelper.loginflag = 1;
			}
		}
//		if (serTextString.equals(mSharedPreferences.getString(ShareprefenceBean.ALONE_SERVICE_NAME, "独立域名用户"))) {// 独立域名用户就设置网址，不是就设置公司
//			companyEdit.setText(mSharedPreferences.getString(ShareprefenceBean.IDN_ALONE_URL, ""));
//			WebserviceHelper.URL = EditHelper.CheckHttp(url_array[3])
//					+ url_array[3] + "/";
//		} else {
//			for (int i = 0; i < 4; i++) {
//				if (serTextString.equals(ser_array[i])) {
//					WebserviceHelper.URL = EditHelper.CheckHttp(url_array[i])
//							+ url_array[i] + "/";
//					break;
//				}
//			}
//			companyEdit.setText(mSharedPreferences.getString(ShareprefenceBean.DWNAME_LOGIN, ""));
//		}
		usernameAutoCompleteTextView.setText(mSharedPreferences.getString(ShareprefenceBean.USERNAME, ""));
		passwordAutoCompleteTextView.setText(mSharedPreferences.getString(ShareprefenceBean.PASSWORD, ""));

//		WebserviceHelper.loginflag = mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1);
		
	}

	@Override
	protected void onStart() {
		// TODO 自动生成的方法存根
		super.onStart();
		
//		SQLiteDatabase db = MyApplication.getInstance().getDataBaseImport().getReadableDatabase();
//		Cursor c = db.rawQuery("select count(id) num from tb_login where company='测试' and username = 'admin' and password = 'admin' and "+DataBaseHelper.miwenpassword+" = '"+pwdhelper.createMD5("admin" + "#cd@guantang").toUpperCase()+"'",null);
//		if(c.moveToFirst()){
//			int num = c.getInt(c.getColumnIndex("num"));
//			if(num<1){
//				db.execSQL("insert into tb_login (company,username,password,miwenpassword)values (测试','admin','admin','"+pwdhelper.createMD5("admin" + "#cd@guantang").toUpperCase()+"')");
//			}
//		}
//		c.close();
//		db.close();
		
		url_array = new String[] {
				mSharedPreferences.getString(ShareprefenceBean.SERVICE_IDN_URL1,
						"www3.gtcangku.com"),
				mSharedPreferences.getString(ShareprefenceBean.SERVICE_IDN_URL2,
						"www.gtcangku.com"),
				mSharedPreferences.getString(ShareprefenceBean.SERVICE_IDN_URL3,
						"www2.gtcangku.com"),
				mSharedPreferences.getString(ShareprefenceBean.IDN_ALONE_URL,
						"test3.gtcangku.com") };

		ser_array = new String[] {
				mSharedPreferences.getString(ShareprefenceBean.SERVICE_NAME1,
						"上海服务器"),
				mSharedPreferences.getString(ShareprefenceBean.SERVICE_NAME2,
						"广东服务器"),
				mSharedPreferences.getString(ShareprefenceBean.SERVICE_NAME3,
						"北京服务器"),
				mSharedPreferences.getString(
						ShareprefenceBean.ALONE_SERVICE_NAME, "其他服务器") };
		if (registerstartFlagBoolean == false) {// 如果不是注册页面返回
			init();
		} else {
			registerstartFlagBoolean = false;
		}
	}
	
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		if (System.currentTimeMillis() - mSharedPreferences.getLong(ShareprefenceBean.NOT_UPDATA, 0) > 3
				* 24 * 60 * 60 * 1000) {
//			if (isdateup) {
//				UmengUpdateAgent.setUpdateOnlyWifi(false);
//				UmengUpdateAgent.update(this);
//			}
//			UmengUpdateAgent.setDialogListener(new UmengDialogButtonListener() {
//
//				@Override
//				public void onClick(int status) {
//					switch (status) {
//					case UpdateStatus.Update:
//						mSharedPreferences.edit().putLong(
//								ShareprefenceBean.NOT_UPDATA, 0);
//						isdateup = true;
//						break;
//					case UpdateStatus.Ignore:
//						isdateup = false;
//						break;
//					case UpdateStatus.NotNow:
//						isdateup = false;
//						mSharedPreferences
//								.edit()
//								.putLong(ShareprefenceBean.NOT_UPDATA,
//										System.currentTimeMillis()).commit();
//						break;
//					}
//				}
//			});
			if(WebserviceImport.isOpenNetwork(this)){
				new ApkUpdateAsyncTesk().execute();
			}
		}
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	@Override
	protected void onDestroy() {
		// TODO 自动生成的方法存根
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
	
	public void onEventMainThread(ObjectOne objectOne) {
		// TODO 自动生成的方法存根
		Log.v("tag", 1 + "");
		String[] strArray = new String[3];
		strArray = objectOne.getMsg().split("\t");
		companyEdit.setText(strArray[0]);
		serverTxt.setText(strArray[1]);
		WebserviceHelper.URL = strArray[2];
		WebserviceHelper.loginflag = 1;
		usernameAutoCompleteTextView.setText("admin");
		passwordAutoCompleteTextView.setText("admin");
		registerstartFlagBoolean = true;
	}
	
	public void initServerPopupWindow(){
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.popupwindow_list, null);
		ListView myListView = (ListView) view.findViewById(R.id.popuplist);
		String[] littileArray = {"上海   "+url_array[0],"广东   "+url_array[1],"北京   "+url_array[2],"其他   "+url_array[3]};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.popupwindow_list_textview,littileArray);
		myListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				// TODO 自动生成的方法存根
				serverTxt.setText(ser_array[arg2]);
				selectServerNum = arg2;
				if(arg2 > -1 && arg2 < 3){
					WebserviceHelper.loginflag = 1;
					WebserviceHelper.URL = EditHelper.CheckHttp(url_array[arg2])+ url_array[arg2] + "/";
					//如果公司这一栏是独立域名服务器地址，则清空。
					if(companyEdit.getText().toString().equals(url_array[3])){
						companyEdit.setText("");
						companyEdit.setHint("请输入单位名称");
					}
				}else if(arg2==3){//独立域名
					companyEdit.setHint("请输入独立域名地址");
					companyEdit.setText(url_array[arg2]);
					WebserviceHelper.loginflag = 0;
				}
				mserverPopupWindow.dismiss();
			}
		});
		myListView.setAdapter(adapter);
//		serverTxt.measure(0, 0);
//		int width = serverTxt.getMeasuredWidth();
		int width = serverTxt.getWidth();
		mserverPopupWindow = new PopupWindow(view,width,LayoutParams.WRAP_CONTENT);
		// 这个是为了点击“返回Back”也能使其消失.
		mserverPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		// 使其聚焦
		mserverPopupWindow.setFocusable(true);
		// 设置允许在外点击消失
		mserverPopupWindow.setOutsideTouchable(true);
		// 刷新状态
		mserverPopupWindow.update();
		mserverPopupWindow.showAsDropDown(serverTxt);
	}
	
	public void initDownPopupWindow(){
		List<Map<String,Object>> mlist = dm_op.getLoginInfo_byCompany(companyEdit.getText().toString().trim());
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.popupwindow_list, null);
		ListView myListView = (ListView) view.findViewById(R.id.popuplist);
		mItemCanDeleteAdapter = new ItemCanDeleteAdapter(this,mlist);
		myListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				// TODO 自动生成的方法存根
				Map<String,Object> map =  (Map<String, Object>) arg0.getAdapter().getItem(arg2);
				usernameAutoCompleteTextView.setText(map.get(DataBaseHelper.username).toString());
				passwordAutoCompleteTextView.setText(map.get(DataBaseHelper.password).toString());
				downPopupWindow.dismiss();
			}
		});
		myListView.setAdapter(mItemCanDeleteAdapter);
//		usernameAutoCompleteTextView.measure(0, 0);
		int width = usernameAutoCompleteTextView.getMeasuredWidth();
		downPopupWindow = new PopupWindow(view,width,LayoutParams.WRAP_CONTENT);
		// 这个是为了点击“返回Back”也能使其消失.
		downPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		// 使其聚焦
		downPopupWindow.setFocusable(true);
		// 设置允许在外点击消失
		downPopupWindow.setOutsideTouchable(true);
		// 刷新状态
		downPopupWindow.update();
		downPopupWindow.showAsDropDown(usernameAutoCompleteTextView);
	}
	
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch(v.getId()){
		case R.id.serverlayout:
			initServerPopupWindow();
			break;
		case R.id.loginBtn:
			if (serverTxt.getText().toString().equals("")) {
				Toast.makeText(this, "请选择服务器", Toast.LENGTH_SHORT).show();
			} else {
				if (WebserviceImport.isOpenNetwork(this)) {
					mSharedPreferences.edit().putInt(ShareprefenceBean.TIYANZHANGHAO, 0).commit();
					if(selectServerNum==3){
						WebserviceHelper.URL =EditHelper.CheckHttp(companyEdit.getText().toString().trim())+ companyEdit.getText().toString().trim() + "/";
					}
					pro_dialog = ProgressDialog.show(this, null, "正在登录……");
					username = usernameAutoCompleteTextView.getText().toString().trim();
					password = passwordAutoCompleteTextView.getText().toString().trim();
					dwname = companyEdit.getText().toString().trim();
					Map<String,Object> map = new HashMap<String, Object>();
					map.put("dwname", dwname);
					map.put("UserName", username);
					map.put("password", pwdhelper.createMD5(password + "#cd@guantang").toUpperCase());
					map.put("IMEI", MyApplication.getInstance().getIEMI());
					map.put("Versions", MyApplication.getInstance().getVisionCode());
					map.put("PhoneSystem","Android");
					JSONObject jsonObject = new JSONObject(map);
					new LoginAsyncTask().execute(jsonObject.toString());
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setMessage("网络未连接，是否进行离线登录？");
					builder.setNegativeButton("否",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO 自动生成的方法存根
									dialog.dismiss();
								}
							});
					builder.setPositiveButton("是",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO 自动生成的方法存根
									lixianlogin();
									dialog.dismiss();
								}
							});
					builder.create().show();
				}
			}
			break;
		case R.id.downImgView:
			initDownPopupWindow();
			break;
		case R.id.lixianloginBtn:
			mSharedPreferences.edit().putInt(ShareprefenceBean.TIYANZHANGHAO, 0).commit();
			lixianlogin();
			break;
		case R.id.tiyanlayout:
			if (WebserviceImport.isOpenNetwork(this)) {
				if(mSharedPreferences.getInt(ShareprefenceBean.TIYANZHANGHAO, 0)==1){
					tiyanflag = true;
				}else{
					tiyanflag = false;
				}
				pro_dialog = ProgressDialog.show(this, null, "正在登录……");
				mSharedPreferences.edit().putInt(ShareprefenceBean.TIYANZHANGHAO, 1).commit();
				WebserviceHelper.URL = "http://sh.gtcangku.com/";
				WebserviceHelper.loginflag=1;
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("dwname", "测试");
				map.put("UserName", "admin");
				map.put("password", pwdhelper.createMD5("admin" + "#cd@guantang").toUpperCase());
				map.put("IMEI", MyApplication.getInstance().getIEMI());
				map.put("Versions", MyApplication.getInstance().getVisionCode());
				map.put("PhoneSystem","Android");
				JSONObject jsonObject = new JSONObject(map);
				new LoginAsyncTask().execute(jsonObject.toString());
			} else {
				Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.registerlayout:
			Intent intent = new Intent(this, AddRegActivity.class);
			startActivity(intent);
			break;
		}
	}
	
	class ApkUpdateAsyncTesk extends AsyncTask<Void, Void, String>{

		@Override
		protected String doInBackground(Void... params) {
			// TODO 自动生成的方法存根
			String json = WebserviceImport_more.AndroidApkUpdate(MyApplication.getInstance().getVisionCode());
			return json;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO 自动生成的方法存根
			super.onPostExecute(result);
			try {
				JSONObject jsonObject = new JSONObject(result);
				switch(jsonObject.getInt("Status")){
				case 1:
					JSONObject DatajsonObject = new JSONObject(jsonObject.getString("Data"));
					String apkUrlString=DatajsonObject.getString("ApkUrl");
					String UpdateText = DatajsonObject.getString("UpdateTxt");
					String apkName = (String) apkUrlString.subSequence(apkUrlString.lastIndexOf("/"), apkUrlString.length());
					
					CommonDialog myDialog = new CommonDialog(NewLoginActivity.this, R.layout.prompt_dialog_layout, R.style.yuanjiao_dialog);
					myDialog.setDialogContentListener(new DialogContentListener() {
						
						@Override
						public void contentExecute(View parent, final Dialog dialog, final Object[] objs) {
							// TODO 自动生成的方法存根
							TextView titleTextView = (TextView) parent.findViewById(R.id.title);
							TextView contentTextView = (TextView) parent.findViewById(R.id.content_txtview);
							TextView cancelTextView = (TextView) parent.findViewById(R.id.cancel);
							TextView confirmTextView = (TextView) parent.findViewById(R.id.confirm);
							
							titleTextView.setVisibility(View.VISIBLE);
							titleTextView.setText("更新提示");
							contentTextView.setText(objs[0].toString());
							cancelTextView.setText("取消");
							confirmTextView.setText("更新");
							
							cancelTextView.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO 自动生成的方法存根
									mSharedPreferences.edit().putLong(ShareprefenceBean.NOT_UPDATA,System.currentTimeMillis()).commit();
									dialog.dismiss();
								}
							});
							confirmTextView.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO 自动生成的方法存根
									new UpdateVersion(NewLoginActivity.this, objs[1].toString(), objs[2].toString());
									dialog.dismiss();
								}
							});
						}
					}, new Object[]{UpdateText,apkName,apkUrlString});
					myDialog.show();
					break;
				case -1:
//					Toast.makeText(NewLoginActivity.this, "已是最新版本，不需要更新", Toast.LENGTH_SHORT).show();
					break;
				default:
					Toast.makeText(NewLoginActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				}
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
		
	}
	
	class LoginAsyncTask extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO 自动生成的方法存根
			String jsonString = WebserviceImport_more.Login_Validate_1_0(params[0]);
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
					JSONObject dataObJsonObject = jsonObject.getJSONObject("Data");
					serConfLastModifyTime=dataObJsonObject.getString("GtLastupdate");
					WebserviceHelper.serid = dataObJsonObject.getString("ServerId");
					rightString = dataObJsonObject.getString("Gt_Rights");
					if(mSharedPreferences.getInt(ShareprefenceBean.TIYANZHANGHAO, 0)==0){
						dm_op.saveLoginInfo(dwname, username, password,pwdhelper.createMD5(password + "#cd@guantang").toUpperCase());	
					}else{
						dm_op.saveLoginInfo("测试", "admin", "admin",pwdhelper.createMD5("admin" + "#cd@guantang").toUpperCase());
					}
					afterLogin();
					break;
				case -1:
					Toast.makeText(NewLoginActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -2:
					Toast.makeText(NewLoginActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -3:
					Toast.makeText(NewLoginActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -4:
					Toast.makeText(NewLoginActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -5:
					Toast.makeText(NewLoginActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -6:
					Toast.makeText(NewLoginActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -7:
					Toast.makeText(NewLoginActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -8:
					Toast.makeText(NewLoginActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -9:
					Toast.makeText(NewLoginActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -10:
					Toast.makeText(NewLoginActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -11:
					AlertDialog.Builder builder = new AlertDialog.Builder(
							NewLoginActivity.this);
					builder.setCancelable(false);
					builder.setMessage(jsonObject.getString("Message"));
					builder.setNegativeButton("退出软件",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO 自动生成的方法存根
									// 关闭app进程
									finish();
									android.os.Process
											.killProcess(android.os.Process
													.myPid());
									System.exit(0);
								}
							});
					builder.setPositiveButton("更新软件",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO 自动生成的方法存根
									afterLogin();
									UmengUpdateAgent.setUpdateAutoPopup(false);
									UmengUpdateAgent
											.forceUpdate(NewLoginActivity.this);
									UmengUpdateAgent.setUpdateOnlyWifi(false);
									UmengUpdateAgent
											.setUpdateListener(new UmengUpdateListener() {
												@Override
												public void onUpdateReturned(
														int updateStatus,
														UpdateResponse updateInfo) {
													switch (updateStatus) {
													case 0: // has update
														UmengUpdateAgent
																.showUpdateDialog(
																		NewLoginActivity.this,
																		updateInfo);
														break;
													case 1: // has no update
														Toast.makeText(
																NewLoginActivity.this,
																"没有更新",
																Toast.LENGTH_SHORT)
																.show();
														afterLogin();
														break;
													case 2: // none wifi
														Toast.makeText(
																NewLoginActivity.this,
																"没有wifi连接， 只在wifi下更新",
																Toast.LENGTH_SHORT)
																.show();
														break;
													case 3: // time out
														Toast.makeText(
																NewLoginActivity.this,
																"超时",
																Toast.LENGTH_SHORT)
																.show();
														break;
													}
												}
											});
								}
							});
					builder.create().show();
					break;
				case -12:
					Toast.makeText(NewLoginActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -13:
					Toast.makeText(NewLoginActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -14:
					Toast.makeText(NewLoginActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -15:
					Toast.makeText(NewLoginActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				}
				pro_dialog.dismiss();
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
				pro_dialog.dismiss();
			}
		}
	}
	
	public void afterLogin() {
		if (!mSharedPreferences
				.getBoolean(ShareprefenceBean.ISFIRST_LOGIN, true)) {
			if (mSharedPreferences.getString(ShareprefenceBean.NET_URL, "").equals(WebserviceHelper.URL)
					&& mSharedPreferences.getString(ShareprefenceBean.DWNAME_LOGIN, "").equals(dwname) 
					&& mSharedPreferences.getString(ShareprefenceBean.SERID, "").equals(WebserviceHelper.serid)) {
				saveLoginMessage();
				Intent intent = new Intent(NewLoginActivity.this,
						FinalMainActivity.class);
				intent.putExtra("isSYNC", false);
				intent.putExtra("serConfLastModifyTime", serConfLastModifyTime);
				startActivity(intent);
				finish();
			} else {
				
				CommonDialog myDialog = new CommonDialog(this, R.layout.prompt_dialog_layout, R.style.yuanjiao_dialog);
				myDialog.setCancelable(false);
				myDialog.setDialogContentListener(new DialogContentListener() {
					
					@Override
					public void contentExecute(View parent, final Dialog dialog, Object[] objs) {
						// TODO 自动生成的方法存根
						TextView titleTextView = (TextView) parent.findViewById(R.id.title);
						TextView contentTextView = (TextView) parent.findViewById(R.id.content_txtview);
						TextView cancelTextView = (TextView) parent.findViewById(R.id.cancel);
						TextView confirmTextView = (TextView) parent.findViewById(R.id.confirm);
						
						titleTextView.setText("帐套已切换");
						contentTextView.setText("帐套改变,坚持登录本地数据库会被清空。");
						confirmTextView.setText("确认登录");
						cancelTextView.setText("返回重试");
						cancelTextView.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO 自动生成的方法存根
								cacheThreadPool.execute(ExitloadRun);
								dialog.dismiss();
							}
						});
						confirmTextView.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO 自动生成的方法存根
								deletePic();
								deleteDatabase(DataBaseHelper.DB);
								saveLoginMessage();
								clearSomeShareprefence();
								Intent intent = new Intent(NewLoginActivity.this,
										FinalMainActivity.class);
								if(tiyanflag){
									intent.putExtra("isSYNC", false);
								}else{
									intent.putExtra("isSYNC", true);
								}
								intent.putExtra("serConfLastModifyTime", serConfLastModifyTime);
								startActivity(intent);
								finish();
								dialog.dismiss();
							}
						});
					}
				});
				myDialog.show();
				
//				AlertDialog.Builder builder3 = new AlertDialog.Builder(NewLoginActivity.this);
//				builder3.setTitle("帐套已切换");
//				builder3.setMessage("为保证本地数据信息正确，请清空本地数据库再使用，或者登录以前帐号进行备份数据库，然后登陆新账号。");
//				builder3.setPositiveButton("清空数据并登录",
//						new DialogInterface.OnClickListener() {
//							@Override
//							public void onClick(DialogInterface dialog,
//									int which) {
//								// TODO 自动生成的方法存根
//								deletePic();
//								deleteDatabase(DataBaseHelper.DB);
//								saveLoginMessage();
//								mSharedPreferences
//										.edit()
//										.putString(ShareprefenceBean.CUSTOM_DW,
//												"").commit();
//								Intent intent = new Intent(NewLoginActivity.this,
//										FinalMainActivity.class);
//								intent.putExtra("isSYNC", true);
//								intent.putExtra("serConfLastModifyTime", serConfLastModifyTime);
//								startActivity(intent);
//								finish();
//								dialog.dismiss();
//							}
//						});
//				builder3.setNegativeButton("重新登录",
//						new DialogInterface.OnClickListener() {
//
//							@Override
//							public void onClick(DialogInterface dialog,
//									int which) {
//								cacheThreadPool.execute(ExitloadRun);
//								dialog.dismiss();
//							}
//						});
//				builder3.create().show();
			}
		} else {
			saveLoginMessage();
			Intent intent = new Intent(NewLoginActivity.this,
					FinalMainActivity.class);
			intent.putExtra("isSYNC", true);
			intent.putExtra("serConfLastModifyTime", serConfLastModifyTime);
			startActivity(intent);
			finish();
		}
	}
	
	public void deletePic() {
		File file = new File(PathConstant.PATH_photo);
		File[] fileList = file.listFiles();
		if (fileList!=null && fileList.length > 0) {
			for (File f : fileList){
				if (f.exists()) {
					f.delete();
				}
			}
		}
	}
	
	/**
	 * 当切换用户时，要清空一些Shareprefences,调用此方法
	 * */
	public void clearSomeShareprefence(){
		mSharedPreferences.edit().putString(ShareprefenceBean.UPDATE_TIME_HP, null).commit();
		mSharedPreferences.edit().putString(ShareprefenceBean.UPDATE_TIME_CKKC, null).commit();
		mSharedPreferences.edit().putString(ShareprefenceBean.UPDATE_TIME_LB, null).commit();
		mSharedPreferences.edit().putString(ShareprefenceBean.UPDATE_TIME_DW, null).commit();
		mSharedPreferences.edit().putString(ShareprefenceBean.UPDATE_TIME_TYPE, null).commit();
		mSharedPreferences.edit().putString(ShareprefenceBean.UPDATE_TIME_CK, null).commit();
		mSharedPreferences.edit().putString(ShareprefenceBean.SHOUYE_CKMC, null).commit();
		mSharedPreferences.edit().putInt(ShareprefenceBean.SHOUYE_CKID, -1).commit();
//		mSharedPreferences.edit().putString(ShareprefenceBean.CUSTOM_DW, "").commit();
	}
	
	public void saveLoginMessage() {
		if(mSharedPreferences.getInt(ShareprefenceBean.TIYANZHANGHAO, 0)==0){//非体验账户保存类容
			mSharedPreferences.edit().putString(ShareprefenceBean.USERNAME, username).commit();
			mSharedPreferences.edit().putString(ShareprefenceBean.MIWENPASSWORD,pwdhelper.createMD5(password + "#cd@guantang")
					.toUpperCase()).commit();
			mSharedPreferences.edit().putString(ShareprefenceBean.PASSWORD, password).commit();
			if (serverTxt.getText().toString().equals(mSharedPreferences.getString(
					ShareprefenceBean.ALONE_SERVICE_NAME, "其他服务器"))) { // 保存用户输入的独立域名网址
				mSharedPreferences.edit()
				.putString(ShareprefenceBean.IDN_ALONE_URL, dwname)
				.commit();
				mSharedPreferences.edit()
				.putString(ShareprefenceBean.DWNAME_LOGIN, dwname).commit();
			} else {
				mSharedPreferences.edit()
				.putString(ShareprefenceBean.DWNAME_LOGIN, dwname).commit();
			}
			mSharedPreferences
			.edit()
			.putInt(ShareprefenceBean.SERVER_NUM,
					selectServerNum).commit();
			
		}
		
		mSharedPreferences.edit()
				.putString(ShareprefenceBean.NET_URL, WebserviceHelper.URL)
				.commit();
		mSharedPreferences
				.edit()
				.putInt(ShareprefenceBean.LOGIN_FLAG,
						WebserviceHelper.loginflag).commit();
		mSharedPreferences.edit()
				.putString(ShareprefenceBean.SERID, WebserviceHelper.serid)
				.commit();
		mSharedPreferences.edit()
				.putBoolean(ShareprefenceBean.ISFIRST_LOGIN, false).commit();

		mSharedPreferences.edit().putInt(ShareprefenceBean.IS_LOGIN, 1).commit();
		mSharedPreferences
				.edit()
				.putString(ShareprefenceBean.RIGHTS,rightString)
				.commit();
		if (eyeCheckBox.isChecked()) {
			mSharedPreferences.edit()
					.putBoolean(ShareprefenceBean.SHOWPASS, true).commit();
		} else {
			mSharedPreferences.edit()
					.putBoolean(ShareprefenceBean.SHOWPASS, false).commit();
		}
	}
	
	Runnable ExitloadRun = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message msg = new Message();
			WebserviceImport.delMEI(MyApplication.getInstance().getIEMI(),
					mSharedPreferences);
		}
	};
	
	/**
	 * 离线登录
	 * */
	public void lixianlogin() {
		if (!mSharedPreferences.getBoolean(ShareprefenceBean.ISFIRST_LOGIN, true)) {// 没有在线登陆，就无法离线登录
			username = usernameAutoCompleteTextView.getText().toString().trim();
			password = passwordAutoCompleteTextView.getText().toString().trim();
			dwname = companyEdit.getText().toString().trim();
			if (mSharedPreferences.getString(ShareprefenceBean.NET_URL, "").equals(WebserviceHelper.URL)
					&& mSharedPreferences.getString(ShareprefenceBean.DWNAME_LOGIN, "").equals(dwname)){
				int userflag=0; //1代表匹配到用户名，0代表没找到用户名
				int passwordflag=0;//1代表匹配到密码，0代表没找到密码
				List<Map<String,Object>> mlist = dm_op.getLoginInfo_byCompany(dwname);
				Iterator<Map<String,Object>> iterator = mlist.iterator();
				String miwenString = pwdhelper.createMD5(password + "#cd@guantang").toUpperCase();
				while(iterator.hasNext()){
					Map<String,Object> map = iterator.next();
					if(map.get(DataBaseHelper.username).toString().equals(username)){
						if(miwenString.equals(map.get(DataBaseHelper.miwenpassword))){
							mSharedPreferences.edit().putString(ShareprefenceBean.PASSWORD, password).commit();
							mSharedPreferences.edit().putInt(ShareprefenceBean.IS_LOGIN, -1).commit();
							if (eyeCheckBox.isChecked()) {
								mSharedPreferences.edit()
										.putBoolean(ShareprefenceBean.SHOWPASS, true).commit();
							} else {
								mSharedPreferences.edit()
										.putBoolean(ShareprefenceBean.SHOWPASS, false).commit();
							}
							passwordflag=1;
							userflag=1;
							Intent intent = new Intent(NewLoginActivity.this,
									FinalMainActivity.class);
							intent.putExtra("isSYNC", false);
							intent.putExtra("serConfLastModifyTime", serConfLastModifyTime);
							startActivity(intent);
							finish();
							break;
						}
						userflag=1;
					}
				}
				if(userflag==0){
					Toast.makeText(this, "未找到当前公司的用户在本手机登陆过", Toast.LENGTH_LONG).show();
				}
				if(userflag==1&&passwordflag==0){
					Toast.makeText(this, "密码不正确", Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(this, "与上次在线登录公司信息不一致，无法离线登录", Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(this, "确保在线登录之后，再离线登录", Toast.LENGTH_SHORT).show();
		}

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO 自动生成的方法存根
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// 关闭app进程
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(0);
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO 自动生成的方法存根
		switch(buttonView.getId()){
		case R.id.eye:
			if (isChecked) {
				passwordAutoCompleteTextView.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				eyeCheckBox.setBackground(getResources().getDrawable(R.drawable.eye_org));
			} else {
				passwordAutoCompleteTextView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				eyeCheckBox.setBackground(getResources().getDrawable(R.drawable.eye_gray));
			}
			break;
		}
	}
	
}
