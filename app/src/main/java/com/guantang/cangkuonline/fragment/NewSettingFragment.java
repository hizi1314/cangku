package com.guantang.cangkuonline.fragment;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.activity.AboutActivity;
import com.guantang.cangkuonline.activity.FreeUseActivity;
import com.guantang.cangkuonline.activity.Helper;
import com.guantang.cangkuonline.activity.Helper_user;
import com.guantang.cangkuonline.activity.New11Activity;
import com.guantang.cangkuonline.activity.NewLoginActivity;
import com.guantang.cangkuonline.activity.ParameterActivity;
import com.guantang.cangkuonline.activity.Pwd_prefer;
import com.guantang.cangkuonline.activity.RegistrationInformationActivity;
import com.guantang.cangkuonline.activity.ToolManagerActivity;
import com.guantang.cangkuonline.activity.UserManagerActivity;
import com.guantang.cangkuonline.activity.WebHelper;
import com.guantang.cangkuonline.application.MyApplication;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;
import com.guantang.cangkuonline.startmarket.MarketUtils;
import com.guantang.cangkuonline.webservice.WebserviceImport;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class NewSettingFragment extends Fragment implements OnClickListener{
	
	private TextView dwnameTextView,usernameTextView,urlTextView;
	private Button logoutBtn;
	private LinearLayout yonghuguanliLayout,mimamodifyLayout,canshuLayout,loaddateLayout,guanligongjiLayout,helpLayout,aboutLayout,appMarketLayout,huodongLayout;
	private LinearLayout PersonalCenterLayout;
	private SharedPreferences mSharedPreferences;
	private ProgressDialog pro_dialog;
	private Context context;
	
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
		mSharedPreferences = MyApplication.getInstance().getSharedPreferences();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		View v = inflater.inflate(R.layout.settinglastfragment_layout, null);
		return v;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onActivityCreated(savedInstanceState);
		init();
	}
	
	public void init(){
		dwnameTextView = (TextView) getView().findViewById(R.id.dwname);
		usernameTextView = (TextView) getView().findViewById(R.id.username);
		urlTextView = (TextView) getView().findViewById(R.id.url);
		logoutBtn = (Button) getView().findViewById(R.id.logout);
		yonghuguanliLayout = (LinearLayout) getView().findViewById(R.id.yonghuguanliLayout);
		mimamodifyLayout = (LinearLayout) getView().findViewById(R.id.mimamodifyLayout);
		canshuLayout = (LinearLayout) getView().findViewById(R.id.canshuLayout);
		loaddateLayout = (LinearLayout) getView().findViewById(R.id.loaddateLayout);
		guanligongjiLayout = (LinearLayout) getView().findViewById(R.id.guanligongjiLayout);
		helpLayout = (LinearLayout) getView().findViewById(R.id.helpLayout);
		aboutLayout = (LinearLayout) getView().findViewById(R.id.aboutLayout);
		appMarketLayout = (LinearLayout) getView().findViewById(R.id.appMarketLayout);
		PersonalCenterLayout = (LinearLayout) getView().findViewById(R.id.PersonalCenterLayout);
		huodongLayout = (LinearLayout) getView().findViewById(R.id.huodongLayout);
		
		PersonalCenterLayout.setOnClickListener(this);
		logoutBtn.setOnClickListener(this);
		yonghuguanliLayout.setOnClickListener(this);
		mimamodifyLayout.setOnClickListener(this);
		canshuLayout.setOnClickListener(this);
		loaddateLayout.setOnClickListener(this);
		guanligongjiLayout.setOnClickListener(this);
		helpLayout.setOnClickListener(this);
		aboutLayout.setOnClickListener(this);
		appMarketLayout.setOnClickListener(this);
		huodongLayout.setOnClickListener(this);
		
		getDWname();
		if(mSharedPreferences.getInt(ShareprefenceBean.TIYANZHANGHAO, 0)==1){
			usernameTextView.setText("admin");
			urlTextView.setText("sh.gtcangku.com");
		}else{
			if(mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, -1)==1){
	        	if(mSharedPreferences.getString(ShareprefenceBean.USERNAME, "").equals("")){
	        		usernameTextView.setText("登录名异常");
	        	}else{ 
	        		usernameTextView.setText(mSharedPreferences.getString(ShareprefenceBean.USERNAME, ""));
	        	}
	        	String myUrl = mSharedPreferences.getString(ShareprefenceBean.NET_URL, "192.168.1.188:8080");
	        	urlTextView.setText(myUrl.subSequence(7, myUrl.lastIndexOf("/")));
	        }else{
	        	if(mSharedPreferences.getString(ShareprefenceBean.USERNAME, "").equals("")){
	        		usernameTextView.setText("登录名异常");
	        	}else{ 
	        		usernameTextView.setText(mSharedPreferences.getString(ShareprefenceBean.USERNAME, ""));
	        	}
	        	urlTextView.setText(mSharedPreferences.getString(ShareprefenceBean.IDN_ALONE_URL, ""));
	        }
		}
	}

	/**
	 * 获取单位名称用于在title显示
	 * */
	public void getDWname() {
		if(mSharedPreferences.getInt(ShareprefenceBean.TIYANZHANGHAO, 0)==1){
			dwnameTextView.setText("测试");
		}else{
//			if (mSharedPreferences.getString(ShareprefenceBean.CUSTOM_DW, "").equals("")) {
				if (mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1) == 1) {
					if (!mSharedPreferences.getString(
							ShareprefenceBean.DWNAME_LOGIN, "").equals("")) {
						dwnameTextView.setText(mSharedPreferences.getString(
								ShareprefenceBean.DWNAME_LOGIN, "").toString());
					} else {
						dwnameTextView.setText("冠唐仓库管理系统");
					}
				} else if (mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG,
						1) == 0) {
					if (!mSharedPreferences.getString(
							ShareprefenceBean.IDN_ALONE_URL, "").equals("")) {
						dwnameTextView.setText(mSharedPreferences.getString(
								ShareprefenceBean.IDN_ALONE_URL, "").toString());
					} else {
						dwnameTextView.setText("冠唐仓库管理系统");
					}
				}
//			} else {
//				dwnameTextView.setText(mSharedPreferences.getString(
//						ShareprefenceBean.CUSTOM_DW, ""));
//			}
		}
		
	}
	
//	public void modifyDWName(){
//		AlertDialog.Builder builder = new AlertDialog.Builder(context);
//		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//		LinearLayout layout = new LinearLayout(context);
//		layout.setOrientation(LinearLayout.HORIZONTAL);
//		layout.setGravity(Gravity.CENTER);
//		final EditText myEditText = new EditText(context);
//		lp.setMargins(10, 10, 10, 10);
//		lp.gravity = Gravity.CENTER;
//		myEditText.setLayoutParams(lp);
//		myEditText.setBackgroundResource(R.drawable.dare_edittext);
//		myEditText.setPadding(10, 10, 10, 10);
//		layout.addView(myEditText);
//		builder.setTitle("自定义公司名称");
//		builder.setView(layout);
//		builder.setNegativeButton("恢复默认",
//				new DialogInterface.OnClickListener() {
//					
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						// TODO 自动生成的方法存根
//						mSharedPreferences.edit()
//								.putString(ShareprefenceBean.CUSTOM_DW, "")
//								.commit();
//						getDWname();
//						dialog.dismiss();
//					}
//				});
//		builder.setPositiveButton("确认",
//				new DialogInterface.OnClickListener() {
//					
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						// TODO 自动生成的方法存根
//						if (myEditText.getText().equals("")) {
//							Toast.makeText(context, "单位名称不能为空",
//									Toast.LENGTH_SHORT).show();
//						} else {
//							mSharedPreferences
//									.edit()
//									.putString(ShareprefenceBean.CUSTOM_DW,
//											myEditText.getText().toString())
//									.commit();
//							getDWname();
//							dialog.dismiss();
//						}
//					}
//				});
//		builder.create().show();
//	}
	
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		Intent intent = new Intent();
		switch(v.getId()){
		case R.id.PersonalCenterLayout:
			intent.setClass(context, RegistrationInformationActivity.class);
			startActivity(intent);
			break;
		case R.id.username:
//			modifyDWName();
			break;
		case R.id.logout:
			if(mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1)==1){
				pro_dialog = ProgressDialog.show(context, null, "正在退出");
				new Thread(ExitloadRun).start();
			}else if(mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1)==-1){
				intent.setClass(context,NewLoginActivity.class);
			    startActivity(intent);
			    ((Activity)context).finish();
			}
			break;
		case R.id.yonghuguanliLayout:
			intent.setClass(context, UserManagerActivity.class);
			startActivity(intent);
			break;
		case R.id.mimamodifyLayout:
			if(mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1)==1){

				if(!mSharedPreferences.getString(ShareprefenceBean.DWNAME_LOGIN, "").equals("测试") && mSharedPreferences.getInt(ShareprefenceBean.TIYANZHANGHAO, 0)!=1){
					intent.setClass(context, Pwd_prefer.class);
					startActivity(intent);
				}else{
					Toast.makeText(context, "测试账户不能修改密码", Toast.LENGTH_SHORT).show();
				}
			}else{
				Toast.makeText(context, "请在线登录修改密码", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.canshuLayout:
			intent.setClass(context, ParameterActivity.class);
			startActivity(intent);
			break;
		case R.id.loaddateLayout:
			intent.setClass(context, WebHelper.class);
			startActivity(intent);
			break;
		case R.id.guanligongjiLayout:
			intent.setClass(context, ToolManagerActivity.class);
			startActivity(intent);
			break;
		case R.id.helpLayout:
			intent.setClass(context, Helper_user.class);
			startActivity(intent);
			break;
		case R.id.aboutLayout:
			intent.setClass(context, AboutActivity.class);
			startActivity(intent);
			break;
		case R.id.appMarketLayout:
			intent.setClass(context, FreeUseActivity.class);
			startActivity(intent);
			break;
		case R.id.huodongLayout:
			intent.setClass(context, New11Activity.class);
			startActivity(intent);
			break;
		}
	}
	
	Runnable ExitloadRun = new Runnable(){
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message msg = Message.obtain();
			synchronized (msg) {
				msg.what=WebserviceImport.delMEI(MyApplication.getInstance().getIEMI(),mSharedPreferences);
				msg.setTarget(mHandler3);
				mHandler3.sendMessage(msg);
			}
		} 
	 };
	 
	 @SuppressLint("HandlerLeak")
		Handler mHandler3= new Handler(){
			 public void handleMessage(Message msg){
				 pro_dialog.dismiss();
				 switch(msg.what){
				 case 1:
					mSharedPreferences.edit().putInt(ShareprefenceBean.IS_LOGIN, -1).commit();
					Toast.makeText(context, "退出成功", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent();
					intent.setClass(context,NewLoginActivity.class);
				    startActivity(intent);
				    ((Activity) context).finish();
					 break;
				 case -1:
					 Toast.makeText(context, "退出失败", Toast.LENGTH_SHORT).show();
					 break;
				 case -2:
					 Toast.makeText(context, "头文件验证失败", Toast.LENGTH_SHORT).show();
					 break;
				 case -3:
					 Toast.makeText(context, "程序处理异常", Toast.LENGTH_SHORT).show();
					 break;
				 case -4:
					mSharedPreferences.edit().putInt(ShareprefenceBean.IS_LOGIN, -1).commit();
					Toast.makeText(context, "未知信息", Toast.LENGTH_SHORT).show();
					 break;
				 case -5:
					 Toast.makeText(context, "网络连接异常", Toast.LENGTH_SHORT).show();
					 break;
				 case -6:
					 Toast.makeText(context, "数据解析错误", Toast.LENGTH_SHORT).show();
					 break;
				 }
			 }
	 };
	
}
