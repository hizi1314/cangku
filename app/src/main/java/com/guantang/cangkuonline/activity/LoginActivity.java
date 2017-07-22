package com.guantang.cangkuonline.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.application.MyApplication;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.eventbusBean.ObjectOne;
import com.guantang.cangkuonline.helper.CheckEditWatcher;
import com.guantang.cangkuonline.helper.EditHelper;
import com.guantang.cangkuonline.helper.PwdHelper;
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

public class LoginActivity extends Activity implements OnClickListener,
		OnCheckedChangeListener {
	private LinearLayout lin_layout, serverlayout, bottomMoreLayout;
	private EditText neturlEditText, passwordEditText;
	private EditText nameAutoCompleteTextView;
	private Button loginBtn, lixianBtn;
	private CheckBox jizhumimaCheckBox, showpasswordCheckBox;
	private TextView serTextView, companyTextView, zhuceTextView, ourTextView,
			helpTextView, imeiTextView, server_prefTextView;
	private ImageButton moreImgBtn;
	private PopupWindow mPopupWindow;
	private String[] url_array;
	private String[] ser_array;
	private int url_subid = 0;// 登录时，ser_array数组每一项对应的下表
	public static int wrong = 0;// 登录错误次数
	private ProgressDialog pro_dialog;
	private Boolean isdateup = true;// 是否更新
	private Boolean showPassWordFlag = false; // 是否显示密码
	private List<Map<String, Object>> ls;
	private PwdHelper pwdhelper = new PwdHelper();
	private int ScreenWidth, ScreenHeigth;
	private ListView mListView;
	private SharedPreferences mSharePreferences;
	private String username, password, dwname;
	private AlertDialog.Builder builder;
	private AlertDialog Dialog;
	private AlertDialog.Builder LiXianBuilder;
	private AlertDialog LiXianDialog;
	private ExecutorService cacheThreadPool = Executors.newCachedThreadPool();
	private String[] str = { "name", "rights" };
	private String rightString = "";
	
	private String serConfLastModifyTime = "";
	
	/**
	 * 是否是注册页面返回到本登陆界面
	 * */
	private Boolean registerstartFlagBoolean = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.newlogin_activity);
		mSharePreferences = this.getSharedPreferences(
				ShareprefenceBean.SHAREPREFENCE_NAME, Context.MODE_PRIVATE);

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		ScreenWidth = dm.widthPixels;
		ScreenHeigth = dm.heightPixels;

		initControl();

		EventBus.getDefault().register(this);
	}

	@Override
	protected void onStart() {
		// TODO 自动生成的方法存根
		super.onStart();
		
		url_array = new String[] {
				mSharePreferences.getString(ShareprefenceBean.SERVICE_IDN_URL1,
						"sh.gtcangku.com"),
				mSharePreferences.getString(ShareprefenceBean.SERVICE_IDN_URL2,
						"www.gtcangku.com"),
				mSharePreferences.getString(ShareprefenceBean.SERVICE_IDN_URL3,
						"www2.gtcangku.com"),
				mSharePreferences.getString(ShareprefenceBean.IDN_ALONE_URL,
						"test3.gtcangku.com") };

		ser_array = new String[] {
				mSharePreferences.getString(ShareprefenceBean.SERVICE_NAME1,
						"官方服务器(上海)"),
				mSharePreferences.getString(ShareprefenceBean.SERVICE_NAME2,
						"官方服务器(广东)"),
				mSharePreferences.getString(ShareprefenceBean.SERVICE_NAME3,
						"官方服务器(北京)"),
				mSharePreferences.getString(
						ShareprefenceBean.ALONE_SERVICE_NAME, "独立域名用户") };

		if (registerstartFlagBoolean == false) {// 如果不是注册页面返回
			init();
		} else {
			registerstartFlagBoolean = false;
		}
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		if (System.currentTimeMillis()
				- mSharePreferences.getLong(ShareprefenceBean.NOT_UPDATA, 0) > 3
				* 24 * 60 * 60 * 1000) {
			if (isdateup) {
				UmengUpdateAgent.setUpdateOnlyWifi(false);
				UmengUpdateAgent.update(this);
			}
			UmengUpdateAgent.setDialogListener(new UmengDialogButtonListener() {

				@Override
				public void onClick(int status) {
					switch (status) {
					case UpdateStatus.Update:
						mSharePreferences.edit().putLong(
								ShareprefenceBean.NOT_UPDATA, 0);
						isdateup = true;
						break;
					case UpdateStatus.Ignore:
						isdateup = false;
						break;
					case UpdateStatus.NotNow:
						isdateup = false;
						mSharePreferences
								.edit()
								.putLong(ShareprefenceBean.NOT_UPDATA,
										System.currentTimeMillis()).commit();
						break;
					}
				}
			});
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

	public void initControl() {
		lin_layout = (LinearLayout) this.findViewById(R.id.lin_layout1);
		serverlayout = (LinearLayout) this.findViewById(R.id.serverlayout);
		neturlEditText = (EditText) this.findViewById(R.id.company_txt);
		nameAutoCompleteTextView = (EditText) this.findViewById(R.id.user_edit);
		companyTextView = (TextView) this.findViewById(R.id.company);
		passwordEditText = (EditText) this.findViewById(R.id.pass_edit);
		loginBtn = (Button) this.findViewById(R.id.loginBtn);
		lixianBtn = (Button) this.findViewById(R.id.lixianBtn);
		jizhumimaCheckBox = (CheckBox) this.findViewById(R.id.rememberBox);
		zhuceTextView = (TextView) this.findViewById(R.id.register_text);
		serTextView = (TextView) this.findViewById(R.id.server_txtleft);
		moreImgBtn = (ImageButton) this.findViewById(R.id.more);
		bottomMoreLayout = (LinearLayout) findViewById(R.id.bottomMoreLayout);
		ourTextView = (TextView) findViewById(R.id.our);
		helpTextView = (TextView) findViewById(R.id.help);
		imeiTextView = (TextView) findViewById(R.id.imei);
		server_prefTextView = (TextView) findViewById(R.id.server_pref);
		showpasswordCheckBox = (CheckBox) findViewById(R.id.isShowPassWord_Box);

		CheckEditWatcher cked = new CheckEditWatcher();
		neturlEditText.addTextChangedListener(cked);
		nameAutoCompleteTextView.addTextChangedListener(cked);
		passwordEditText.addTextChangedListener(cked);

		ourTextView.setOnClickListener(this);
		helpTextView.setOnClickListener(this);
		imeiTextView.setOnClickListener(this);
		server_prefTextView.setOnClickListener(this);
		serverlayout.setOnClickListener(this);
		loginBtn.setOnClickListener(this);
		zhuceTextView.setOnClickListener(this);
		moreImgBtn.setOnClickListener(this);
		lixianBtn.setOnClickListener(this);
		jizhumimaCheckBox.setOnCheckedChangeListener(this);
		showpasswordCheckBox.setOnCheckedChangeListener(this);

		if (mSharePreferences.getBoolean(ShareprefenceBean.ISFIRST_LOGIN, true)) {
			jizhumimaCheckBox.setChecked(true);
			showpasswordCheckBox.setChecked(false);
		} else {
			jizhumimaCheckBox.setChecked(mSharePreferences.getBoolean(
					ShareprefenceBean.JIZHUMIMA, true));
			showpasswordCheckBox.setChecked(mSharePreferences.getBoolean(
					ShareprefenceBean.SHOWPASS, false));
		}
	}

	public void init() {
//		serTextView.setText(mSharePreferences.getString(
//				ShareprefenceBean.SER_NAME, "官方服务器(上海)"));
		String serTextString = serTextView.getText().toString();
		if (serTextString.equals(mSharePreferences.getString(
				ShareprefenceBean.ALONE_SERVICE_NAME, "独立域名用户"))) {// 独立域名用户就设置网址，不是就设置公司
			neturlEditText.setText(mSharePreferences.getString(
					ShareprefenceBean.IDN_ALONE_URL, "gd.gtcangku.com"));
			WebserviceHelper.URL = EditHelper.CheckHttp(url_array[3])
					+ url_array[3] + "/";
		} else {
			for (int i = 0; i < 4; i++) {
				if (serTextString.equals(ser_array[i])) {
					WebserviceHelper.URL = EditHelper.CheckHttp(url_array[i])
							+ url_array[i] + "/";
					break;
				}
			}
			neturlEditText.setText(mSharePreferences.getString(
					ShareprefenceBean.DWNAME_LOGIN, "测试"));
		}
		nameAutoCompleteTextView.setText(mSharePreferences.getString(
				ShareprefenceBean.USERNAME, "admin"));
		if (jizhumimaCheckBox.isChecked()) {
			passwordEditText.setText(mSharePreferences.getString(
					ShareprefenceBean.PASSWORD, "admin"));
		} else {
			passwordEditText.setText("");
		}

		WebserviceHelper.loginflag = mSharePreferences.getInt(
				ShareprefenceBean.LOGIN_FLAG, 1);
		// WebserviceHelper.URL = mSharePreferences.getString(
		// ShareprefenceBean.NET_URL,
		// "http://192.168.1.188:8080/");
	}

	public void onEventMainThread(ObjectOne objectOne) {
		// TODO 自动生成的方法存根
		Log.v("tag", 1 + "");
		// 避免原来url_subid=3，导致登录的时候修改WebserviceHelper.URL；
		url_subid = 0;
		String[] strArray = new String[3];
		strArray = objectOne.getMsg().split("\t");
		neturlEditText.setText(strArray[0]);
		serTextView.setText(strArray[1]);
		WebserviceHelper.URL = strArray[2];
		nameAutoCompleteTextView.setText("admin");
		passwordEditText.setText("admin");
		registerstartFlagBoolean = true;
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.serverlayout:
			builder = new AlertDialog.Builder(this);
			LayoutInflater inflater = LayoutInflater.from(this);
			View view = inflater.inflate(R.layout.popupwindow_list, null);
			ListView myListView = (ListView) view.findViewById(R.id.popuplist);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					LoginActivity.this, R.layout.popupwindow_list_textview,
					ser_array);
			myListView.setAdapter(adapter);
			myListView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO 自动生成的方法存根
					String mystr = (String) arg0.getAdapter().getItem(arg2)
							.toString();
					serTextView.setText(mystr);
					url_subid = arg2;
					if (arg2 > -1 && arg2 < 3) {
						if (mSharePreferences.getBoolean(
								ShareprefenceBean.ISFIRST_LOGIN, true)) {// 是否是第一次登录
							companyTextView.setText("公司名：");
							neturlEditText.setText("测试");
							neturlEditText.setHint("");
							nameAutoCompleteTextView.setText("admin");
							passwordEditText.setText("admin");

						} else {
							companyTextView.setText("公司名：");
							neturlEditText.setHint("");
//							if (mSharePreferences.getString(
//									ShareprefenceBean.SER_NAME, "").equals(
//									mystr)) {
//								neturlEditText.setText(mSharePreferences
//										.getString(
//												ShareprefenceBean.DWNAME_LOGIN,
//												"测试"));
//								nameAutoCompleteTextView
//										.setText(mSharePreferences.getString(
//												ShareprefenceBean.USERNAME,
//												"admin"));
//								if (jizhumimaCheckBox.isChecked()) {
//									passwordEditText.setText(mSharePreferences
//											.getString(
//													ShareprefenceBean.PASSWORD,
//													"admin"));
//								} else {
//									passwordEditText.setText("");
//								}
//							} else {
//								neturlEditText.setText("测试");
//								nameAutoCompleteTextView.setText("admin");
//								passwordEditText.setText("admin");
//							}

						}
						WebserviceHelper.URL = EditHelper
								.CheckHttp(url_array[url_subid])
								+ url_array[arg2] + "/";
						WebserviceHelper.loginflag = 1;
					} else if (arg2 == 3) {// 独立域名
						neturlEditText.setHint("可设置默认地址");
						companyTextView.setText("地    址：");
						if (mSharePreferences.getBoolean(
								ShareprefenceBean.ISFIRST_LOGIN, true)) {// 是否是第一次登录
							neturlEditText.setText(mSharePreferences.getString(
									ShareprefenceBean.IDN_ALONE_URL,
									"gd.gtcangku.com"));
							nameAutoCompleteTextView.setText("admin");
							passwordEditText.setText("admin");
						} else {
//							if (mSharePreferences.getString(
//									ShareprefenceBean.SER_NAME, "独立域名用户")
//									.equals(mystr)) {
//								neturlEditText.setText(mSharePreferences
//										.getString(
//												ShareprefenceBean.IDN_ALONE_URL,
//												"gd.gtcangku.com"));
//								nameAutoCompleteTextView.setText(mSharePreferences
//										.getString(ShareprefenceBean.USERNAME,
//												""));
//								if (jizhumimaCheckBox.isChecked()) {
//									passwordEditText.setText(mSharePreferences
//											.getString(
//													ShareprefenceBean.PASSWORD,
//													"admin"));
//								} else {
//									passwordEditText.setText("");
//								}
//							} else {
//								neturlEditText.setText(mSharePreferences
//										.getString(
//												ShareprefenceBean.IDN_ALONE_URL,
//												"gd.gtcangku.com"));
//								nameAutoCompleteTextView.setText("admin");
//								passwordEditText.setText("admin");
//							}
						}

						WebserviceHelper.loginflag = 0;
					}
					Dialog.dismiss();
				}
			});
			builder.setView(view);
			Dialog = builder.show();

			break;
		case R.id.loginBtn:
			if (url_subid == 3) {
				WebserviceHelper.URL = EditHelper
						.CheckHttp(url_array[url_subid])
						+ neturlEditText.getText() + "/";
			}
			if (serTextView.getText().toString().equals("请选择服务器")) {
				Toast.makeText(LoginActivity.this, "请选择服务器", Toast.LENGTH_SHORT)
						.show();
			} else {
				if (WebserviceImport.isOpenNetwork(LoginActivity.this)) {
					pro_dialog = ProgressDialog.show(this, null, "正在登录……");
					username = nameAutoCompleteTextView.getText().toString();
					password = passwordEditText.getText().toString();
					dwname = neturlEditText.getText().toString();
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
		case R.id.more:
			AnimationSet animationSet = new AnimationSet(true);
			if (bottomMoreLayout.isShown()) {
				ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 0f, 1f,
						1f, Animation.RELATIVE_TO_SELF, 1f,
						Animation.RELATIVE_TO_SELF, 1f);
				scaleAnimation.setDuration(700);
				scaleAnimation.setFillAfter(true);
				animationSet.addAnimation(scaleAnimation);

				AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
				// 设置动画执行的时间
				alphaAnimation.setDuration(700);
				animationSet.addAnimation(alphaAnimation);
				bottomMoreLayout.startAnimation(animationSet);
				bottomMoreLayout.setVisibility(View.GONE);
				moreImgBtn.setImageDrawable(getResources().getDrawable(
						R.drawable.arrow));
			} else {
				bottomMoreLayout.setVisibility(View.VISIBLE);
				ScaleAnimation scaleAnimation = new ScaleAnimation(0f, 1f, 1f,
						1f, Animation.RELATIVE_TO_SELF, 1f,
						Animation.RELATIVE_TO_SELF, 1f);
				scaleAnimation.setDuration(700);
				scaleAnimation.setFillAfter(true);
				animationSet.addAnimation(scaleAnimation);

				AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
				// 设置动画执行的时间
				alphaAnimation.setDuration(700);
				alphaAnimation.setStartOffset(0);// 这个动画应该什么时候开始相对于开始时间。这是最有用的时候
				animationSet.addAnimation(alphaAnimation);
				bottomMoreLayout.startAnimation(animationSet);
				moreImgBtn.setImageDrawable(getResources().getDrawable(
						R.drawable.arrow_fan));
			}
			break;
		case R.id.our:
			Intent i = new Intent(LoginActivity.this, Helper.class);
			startActivity(i);
			break;
		case R.id.help:
			Intent in2 = new Intent(LoginActivity.this, Helper_user.class);
			in2.putExtra("qidong_help", "longin_activity");
			startActivity(in2);
			break;
		case R.id.imei:
			AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
			builder2.setTitle("本机串号");
			builder2.setMessage(MyApplication.getInstance().getIEMI());
			builder2.setPositiveButton("复制",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
							copy(MyApplication.getInstance().getIEMI(),
									LoginActivity.this);
						}
					});
			builder2.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					});
			builder2.create().show();
			break;
		case R.id.server_pref:
			Intent in = new Intent(LoginActivity.this, ServerPref.class);
			startActivity(in);
			break;
		case R.id.register_text:
			Intent intent = new Intent(this, AddRegActivity.class);
			startActivity(intent);
			break;
		case R.id.lixianBtn:
			lixianlogin();
			break;
		}
	}

	public void saveLoginMessage() {
		mSharePreferences.edit()
				.putString(ShareprefenceBean.USERNAME, username).commit();
		mSharePreferences
				.edit()
				.putString(
						ShareprefenceBean.MIWENPASSWORD,
						pwdhelper.createMD5(password + "#cd@guantang")
								.toUpperCase()).commit();
		mSharePreferences.edit()
		.putString(ShareprefenceBean.PASSWORD, password).commit();
		if (jizhumimaCheckBox.isChecked()) {
			mSharePreferences.edit()
					.putBoolean(ShareprefenceBean.JIZHUMIMA, true).commit();
		} else {
			mSharePreferences.edit()
					.putBoolean(ShareprefenceBean.JIZHUMIMA, false).commit();
		}
		if (showpasswordCheckBox.isChecked()) {
			mSharePreferences.edit()
					.putBoolean(ShareprefenceBean.SHOWPASS, true).commit();
		} else {
			mSharePreferences.edit()
					.putBoolean(ShareprefenceBean.SHOWPASS, false).commit();
		}
		if (serTextView
				.getText()
				.toString()
				.equals(mSharePreferences.getString(
						ShareprefenceBean.ALONE_SERVICE_NAME, "独立域名用户"))) { // 保存用户输入的独立域名网址
			mSharePreferences.edit()
					.putString(ShareprefenceBean.IDN_ALONE_URL, dwname)
					.commit();
			mSharePreferences.edit()
					.putString(ShareprefenceBean.DWNAME_LOGIN, dwname).commit();
		} else {
			mSharePreferences.edit()
					.putString(ShareprefenceBean.DWNAME_LOGIN, dwname).commit();
		}
//		mSharePreferences
//				.edit()
//				.putString(ShareprefenceBean.SER_NAME,
//						serTextView.getText().toString()).commit();
		mSharePreferences.edit()
				.putString(ShareprefenceBean.NET_URL, WebserviceHelper.URL)
				.commit();
		mSharePreferences
				.edit()
				.putInt(ShareprefenceBean.LOGIN_FLAG,
						WebserviceHelper.loginflag).commit();
		mSharePreferences.edit()
				.putString(ShareprefenceBean.SERID, WebserviceHelper.serid)
				.commit();
		mSharePreferences.edit()
				.putBoolean(ShareprefenceBean.ISFIRST_LOGIN, false).commit();

		mSharePreferences.edit().putInt(ShareprefenceBean.IS_LOGIN, 1).commit();
		mSharePreferences
				.edit()
				.putString(ShareprefenceBean.RIGHTS,rightString)
				.commit();
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
					afterLogin();
					break;
				case -1:
					Toast.makeText(LoginActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -2:
					Toast.makeText(LoginActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -3:
					Toast.makeText(LoginActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -4:
					Toast.makeText(LoginActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -5:
					Toast.makeText(LoginActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -6:
					Toast.makeText(LoginActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -7:
					Toast.makeText(LoginActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -8:
					Toast.makeText(LoginActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -9:
					Toast.makeText(LoginActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -10:
					Toast.makeText(LoginActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -11:
					AlertDialog.Builder builder = new AlertDialog.Builder(
							LoginActivity.this);
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
											.forceUpdate(LoginActivity.this);
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
																		LoginActivity.this,
																		updateInfo);
														break;
													case 1: // has no update
														Toast.makeText(
																LoginActivity.this,
																"没有更新",
																Toast.LENGTH_SHORT)
																.show();
														afterLogin();
														break;
													case 2: // none wifi
														Toast.makeText(
																LoginActivity.this,
																"没有wifi连接， 只在wifi下更新",
																Toast.LENGTH_SHORT)
																.show();
														break;
													case 3: // time out
														Toast.makeText(
																LoginActivity.this,
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
					Toast.makeText(LoginActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -13:
					Toast.makeText(LoginActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -14:
					Toast.makeText(LoginActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -15:
					Toast.makeText(LoginActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
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
	
	Runnable ExitloadRun = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message msg = new Message();
			WebserviceImport.delMEI(MyApplication.getInstance().getIEMI(),
					mSharePreferences);
		}
	};

	public void afterLogin() {
		if (!mSharePreferences
				.getBoolean(ShareprefenceBean.ISFIRST_LOGIN, true)) {
			if (mSharePreferences.getString(ShareprefenceBean.NET_URL, "").equals(WebserviceHelper.URL)
					&& mSharePreferences.getString(ShareprefenceBean.DWNAME_LOGIN, "").equals(dwname) 
					&& mSharePreferences.getString(ShareprefenceBean.SERID, "").equals(WebserviceHelper.serid)) {
				saveLoginMessage();
				Intent intent = new Intent(LoginActivity.this,
						FinalMainActivity.class);
				intent.putExtra("isSYNC", false);
				intent.putExtra("serConfLastModifyTime", serConfLastModifyTime);
				startActivity(intent);
				finish();
			} else {
				AlertDialog.Builder builder3 = new AlertDialog.Builder(
						LoginActivity.this);
				builder3.setTitle("帐套已切换");
				builder3.setMessage("为保证本地数据信息正确，请清空本地数据库再使用，或者登录以前帐号进行备份数据库，然后登陆新账号。");
				builder3.setPositiveButton("清空数据并登录",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO 自动生成的方法存根
								deletePic();
								deleteDatabase(DataBaseHelper.DB);
								saveLoginMessage();
//								mSharePreferences
//										.edit()
//										.putString(ShareprefenceBean.CUSTOM_DW,
//												"").commit();
								Intent intent = new Intent(LoginActivity.this,
										FinalMainActivity.class);
								intent.putExtra("isSYNC", true);
								intent.putExtra("serConfLastModifyTime", serConfLastModifyTime);
								startActivity(intent);
								finish();
								dialog.dismiss();
							}
						});
				builder3.setNegativeButton("重新登录",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								cacheThreadPool.execute(ExitloadRun);
								dialog.dismiss();
							}
						});
				builder3.create().show();
			}
		} else {
			saveLoginMessage();
			Intent intent = new Intent(LoginActivity.this,
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
		if (fileList.length > 0) {
			for (File f : fileList) {
				if (f.exists()) {
					f.delete();
				}
			}
		}
	}

	/**
	 * 复制功能
	 * */
	@SuppressWarnings("deprecation")
	public void copy(String str, Context context) {
		int version_code = android.os.Build.VERSION.SDK_INT;
		ClipboardManager cmb = (ClipboardManager) context
				.getSystemService(Context.CLIPBOARD_SERVICE);
		if (version_code < 11) {
			ClipData textCd = ClipData.newPlainText("kkk", str.trim());
			cmb.setPrimaryClip(textCd);
		} else {
			cmb.setText(str.trim());
		}
	}

	/**
	 * 离线登录
	 * */
	public void lixianlogin() {
		if (!mSharePreferences.getString(ShareprefenceBean.MIWENPASSWORD, "")
				.equals("")) {// 密文密码，目前只有离线登录才用到了
			username = nameAutoCompleteTextView.getText().toString();
			password = passwordEditText.getText().toString();
			dwname = neturlEditText.getText().toString();
			if (mSharePreferences.getString(ShareprefenceBean.NET_URL, "")
					.equals(WebserviceHelper.URL)
					&& mSharePreferences.getString(
							ShareprefenceBean.DWNAME_LOGIN, "").equals(dwname)
					&& mSharePreferences.getString(ShareprefenceBean.USERNAME,
							"").equals(username)) {
				if (pwdhelper
						.createMD5(password + "#cd@guantang")
						.toUpperCase()
						.equals(mSharePreferences.getString(
								ShareprefenceBean.MIWENPASSWORD, ""))) {
					if (jizhumimaCheckBox.isChecked()) {
						mSharePreferences
								.edit()
								.putString(ShareprefenceBean.PASSWORD, password)
								.commit();
					} else {
						mSharePreferences.edit()
								.putString(ShareprefenceBean.PASSWORD, "")
								.commit();
					}
					mSharePreferences.edit()
							.putInt(ShareprefenceBean.IS_LOGIN, -1).commit();
					Intent intent = new Intent(LoginActivity.this,
							FinalMainActivity.class);
					intent.putExtra("isSYNC", false);
					intent.putExtra("serConfLastModifyTime", serConfLastModifyTime);
					startActivity(intent);
					finish();
				} else {
					Toast.makeText(this, "密码不正确，离线密码与在线登录密码一致", Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(this, "与上次在线登录信息不一致，无法离线登录", Toast.LENGTH_SHORT).show();
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
		switch (buttonView.getId()) {
		case R.id.rememberBox:
			if (isChecked) {
				jizhumimaCheckBox.setChecked(true);
			} else {
				jizhumimaCheckBox.setChecked(false);
			}
			break;
		case R.id.isShowPassWord_Box:
			if (isChecked) {
				passwordEditText
						.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
			} else {
				passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT
						| InputType.TYPE_TEXT_VARIATION_PASSWORD);
			}
			break;
		}
	}
}
