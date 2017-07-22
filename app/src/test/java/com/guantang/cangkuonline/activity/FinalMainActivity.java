package com.guantang.cangkuonline.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.application.MyApplication;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.database.DataBaseImport;
import com.guantang.cangkuonline.database.DataBaseMethod;
import com.guantang.cangkuonline.database.DataBaseOperateMethod;
import com.guantang.cangkuonline.dialog.CommonDialog;
import com.guantang.cangkuonline.dialog.CommonDialog.DialogContentListener;
import com.guantang.cangkuonline.eventbusBean.EventObject8;
import com.guantang.cangkuonline.fragment.Analytic_StatisticsFragment;
import com.guantang.cangkuonline.fragment.CangKuFirstFragment;
import com.guantang.cangkuonline.fragment.ConsumerFragment;
import com.guantang.cangkuonline.fragment.NewSettingFragment;
import com.guantang.cangkuonline.fragment.OrderFragment;
import com.guantang.cangkuonline.helper.RightsHelper;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;
import com.guantang.cangkuonline.webservice.WebserviceHelper;
import com.guantang.cangkuonline.webservice.WebserviceImport;

import de.greenrobot.event.EventBus;

public class FinalMainActivity extends FragmentActivity implements
		OnClickListener {
	private LinearLayout homepagerLayout,orderPageLayout,consumerPageLayout,analytic_statisticsLayout,
			settingLayout;
	private ImageView imgView1, imgView2, imgView3,imgView4,imgView5;
	private TextView textView1, textView2, textView3,textView4,textView5;
	private FrameLayout contentpagerlayout;
	private SharedPreferences mSharedPreferences;
	private LayoutInflater inflater;
	private int ScreenWidth = 0;
	private ProgressDialog pro_dialog;
	private CommonDialog mpDialog;
	private boolean hpxinxiflag = false,ckkcxinxiflag=false, hpleixinflag = true,
			dwxinxiflag = true, canshuxinxiflag = true,cangkuxinxiflag = true;
	/**
	 * tabNumber代表当前处于tab状态
	 * */
	private int tabNumber = 1;
	private List<Map<String, Object>> ls;
	private Thread thread;
	private SQLiteDatabase db;
	private DataBaseOperateMethod dm_op = new DataBaseOperateMethod(this);
	private DataBaseMethod dm = new DataBaseMethod(this);
	private SimpleDateFormat formatter;
	private int hp_num = 0, lb_num = 0, dw_num = 0, conf_num = 0, ck_num = 0,
			ckkc_num = 0;
	private String[] str = { DataBaseHelper.ID, DataBaseHelper.HPMC,
			DataBaseHelper.HPBM, DataBaseHelper.HPTM, DataBaseHelper.GGXH,
			DataBaseHelper.CurrKC, DataBaseHelper.JLDW, DataBaseHelper.HPSX,
			DataBaseHelper.HPXX, DataBaseHelper.SCCS, DataBaseHelper.BZ,
			DataBaseHelper.RKCKJ, DataBaseHelper.CKCKJ, DataBaseHelper.CKCKJ2,
			DataBaseHelper.JLDW2, DataBaseHelper.BigNum, DataBaseHelper.RES1,
			DataBaseHelper.RES2, DataBaseHelper.RES3, DataBaseHelper.RES4,
			DataBaseHelper.RES5, DataBaseHelper.RES6, DataBaseHelper.LBS,
			DataBaseHelper.KCJE, DataBaseHelper.LBID, DataBaseHelper.LBIndex,
			DataBaseHelper.ImagePath };
	private String[] str1 = { "id", "HPMC", "HPBM", "HPTBM", "GGXH", "CurrKc",
			"JLDW", "HPSX", "HPXX", "SCCS", "BZ", "RKCKJ", "CKCKJ", "CKCKJ2",
			"JLDW2", "BigNum", "res1", "res2", "res3", "res4", "res5", "res6",
			"LBS", "kcje", "LBID", "LBIndex", "ImageUrl" };
	private String[] str2 = { DataBaseHelper.ID, DataBaseHelper.Name,
			DataBaseHelper.Lev, DataBaseHelper.PID, DataBaseHelper.Ord,
			DataBaseHelper.Sindex };
	private String[] str3 = { "ID", "name", "lev", "PID", "ord", "sindex" };
	private String[] str4 = { DataBaseHelper.ID, DataBaseHelper.DWName,
			DataBaseHelper.ADDR, DataBaseHelper.FAX, DataBaseHelper.YB,
			DataBaseHelper.Net, DataBaseHelper.LXR, DataBaseHelper.TEL,
			DataBaseHelper.Email, DataBaseHelper.BZ, DataBaseHelper.DWtype };
	private String[] str5 = { "id", "dwName", "addr", "fax", "yb", "www",
			"lxr", "tel", "email", "bz", "dwType" };
	private String[] str6 = { DataBaseHelper.GID, DataBaseHelper.ItemID,
			DataBaseHelper.ItemV, DataBaseHelper.Ord };
	private String[] str7 = { "GID", "ItemID", "ItemV", "ord" };
	private String[] str10 = { DataBaseHelper.GID, DataBaseHelper.ItemV };
	private String[] str11 = { "dirc", "name" };
	private String[] str12 = { DataBaseHelper.ID, DataBaseHelper.HPID,
			DataBaseHelper.CKID, DataBaseHelper.KCSL };
	private String[] str13 = { "id", "hpid", "ckid", "kcsl" };
	private String[] str14 = { DataBaseHelper.ID, DataBaseHelper.CKMC,
			DataBaseHelper.FZR, DataBaseHelper.TEL, DataBaseHelper.ADDR,
			DataBaseHelper.INACT, DataBaseHelper.OUTACT, DataBaseHelper.BZ };
	private String[] str15 = { "ID", "CKMC", "FZR", "TEL", "ADDR", "inact",
			"outact", "BZ" };
	private String[] str16 = { "ID", "name", "dwlevel", "PID", "dwOrder",
			"dwIndex" };
	private String[] str17 = { DataBaseHelper.ID, DataBaseHelper.Name,
			DataBaseHelper.depLevel, DataBaseHelper.PID,
			DataBaseHelper.depOrder, DataBaseHelper.depindex };
	
	private boolean stopflag = false; // true表示停止下载，false表示继续下载
	private LinkedList<Runnable> loadRunnableList = new LinkedList<Runnable>();
	private Handler runnableHandler;
	private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(6);
	/**
	 * 控制runnableHandler初始化完成
	 * */
	private volatile Semaphore runnableHandlerSemaphore = new Semaphore(1);
	/**
	 * 控制线程一个一个执行
	 * */
	private volatile Semaphore mSemaphore = new Semaphore(1);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 不去重新绘制界面
		// getWindow().setSoftInputMode(
		// WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		setContentView(R.layout.activity_finalmain);
		mSharedPreferences = this.getSharedPreferences(
				ShareprefenceBean.SHAREPREFENCE_NAME, Context.MODE_PRIVATE);
		EventBus.getDefault().register(this);
		inflater = LayoutInflater.from(this);

		initView();
		init();
		Intent intent = getIntent();
		String serConfLastModifyTime = intent
				.getStringExtra("serConfLastModifyTime");

		if (intent.getBooleanExtra("isSYNC", false)) {// 是否同步
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO 自动生成的方法存根
					onMyThread(false);
				}
			}, 400);
			mSharedPreferences.edit().putString(ShareprefenceBean.SerConfLastModifyTime,serConfLastModifyTime).commit();
		} else {
			if (WebserviceImport.isOpenNetwork(this)) {
				if (!serConfLastModifyTime.equals("")&& !serConfLastModifyTime.equals(mSharedPreferences.getString(ShareprefenceBean.SerConfLastModifyTime,""))) {
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							// TODO 自动生成的方法存根
							try {
								runnableHandlerSemaphore.acquire();
							} catch (InterruptedException e) {
								// TODO 自动生成的 catch 块
								e.printStackTrace();
							}
							new Thread(handleRunnable).start();
							mayLoadData();
							Toast.makeText(FinalMainActivity.this, "正在为你同步已修改的参数", Toast.LENGTH_LONG).show();
						}
					}, 400);
					mSharedPreferences.edit().putString(ShareprefenceBean.SerConfLastModifyTime,serConfLastModifyTime).commit();
				}
			} else {
				if (!serConfLastModifyTime.equals("")&& !serConfLastModifyTime.equals(mSharedPreferences.getString(ShareprefenceBean.SerConfLastModifyTime,""))) {
					Toast.makeText(this, "系统参数已修改，注意及时同步数据", Toast.LENGTH_LONG)
							.show();
				}
			}
		}
	}

	@Override
	protected void onDestroy() {
		// TODO 自动生成的方法存根
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	public void initView() {
		imgView1 = (ImageView) findViewById(R.id.img1);
		imgView2 = (ImageView) findViewById(R.id.img2);
		imgView3 = (ImageView) findViewById(R.id.img3);
		imgView4 = (ImageView) findViewById(R.id.img4);
		imgView5 = (ImageView) findViewById(R.id.img5);
		textView1 = (TextView) findViewById(R.id.text1);
		textView2 = (TextView) findViewById(R.id.text2);
		textView3 = (TextView) findViewById(R.id.text3);
		textView4 = (TextView) findViewById(R.id.text4);
		textView5 = (TextView) findViewById(R.id.text5);
		homepagerLayout = (LinearLayout) findViewById(R.id.HomePage);
		orderPageLayout = (LinearLayout) findViewById(R.id.orderPage);
		consumerPageLayout = (LinearLayout) findViewById(R.id.consumerPage);
		analytic_statisticsLayout = (LinearLayout) findViewById(R.id.analytic_statistics);
		settingLayout = (LinearLayout) findViewById(R.id.setting);
		contentpagerlayout = (FrameLayout) findViewById(R.id.contentpager);
		homepagerLayout.setOnClickListener(this);
		orderPageLayout.setOnClickListener(this);
		consumerPageLayout.setOnClickListener(this);
		analytic_statisticsLayout.setOnClickListener(this);
		settingLayout.setOnClickListener(this);
		formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}

	public void init() {
		imgView1.setImageDrawable(getResources().getDrawable(
				R.drawable.storage_orange_btn));
		textView1.setTextColor(getResources().getColor(R.color.theme_orange));
		imgView2.setImageDrawable(getResources().getDrawable(R.drawable.order_grey_btn));
		textView2.setTextColor(getResources().getColor(R.color.ziti666666));
		imgView3.setImageDrawable(getResources().getDrawable(R.drawable.customer_grey_btn));
		textView3.setTextColor(getResources().getColor(R.color.ziti666666));
		imgView4.setImageDrawable(getResources().getDrawable(R.drawable.pie_chart_grey_btn));
		textView4.setTextColor(getResources().getColor(R.color.ziti666666));
		imgView5.setImageDrawable(getResources().getDrawable(R.drawable.setting_grey_btn));
		textView5.setTextColor(getResources().getColor(R.color.ziti666666));
		dongtaiFirst();
		tabNumber = 1;
	}
	
	public void onEventMainThread(EventObject8 eo8) {
		onMyThread(true);
	}
	
	/**
	 * 同步更新对话框。
	 * @param option 可选同步项是否同步，true 要同步，false 不同步。
	 * 
	 * */
	public void onMyThread(final boolean option) {
		
		CommonDialog myDialog = new CommonDialog(this, R.layout.update_info, R.style.yuanjiao_dialog);
		myDialog.setCancelable(true);
		myDialog.setDialogContentListener(new DialogContentListener() {

			@Override
			public void contentExecute(View parent, final Dialog dialog,Object... obj) {
				// TODO 自动生成的方法存根
				final CheckBox hpxinxiCheckBox = (CheckBox) parent
						.findViewById(R.id.ck_hp);
				final CheckBox ckkcxinxiCheckBox = (CheckBox) parent.findViewById(R.id.ck_ckkc);
				final CheckBox hpleixinCheckBox = (CheckBox) parent
						.findViewById(R.id.ck_lb);
				final CheckBox dwxinxiCheckBox = (CheckBox) parent
						.findViewById(R.id.ck_dw);
				final CheckBox canshuxinxiCheckBox = (CheckBox) parent
						.findViewById(R.id.ck_type);
				final CheckBox cangkuxinxiCheckBox = (CheckBox) parent
						.findViewById(R.id.ck_ck);
				
				TextView confirmTextView = (TextView) parent.findViewById(R.id.confirm);
				confirmTextView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO 自动生成的方法存根
						if (mSharedPreferences.getInt(
								ShareprefenceBean.IS_LOGIN, -1) == 1) {
							if (WebserviceImport
									.isOpenNetwork(FinalMainActivity.this)) {
								hpxinxiflag = hpxinxiCheckBox.isChecked();
								ckkcxinxiflag = ckkcxinxiCheckBox.isChecked();
								hpleixinflag = hpleixinCheckBox.isChecked();
								dwxinxiflag = dwxinxiCheckBox.isChecked();
								canshuxinxiflag = canshuxinxiCheckBox
										.isChecked();
								cangkuxinxiflag = cangkuxinxiCheckBox
										.isChecked();
								try {
									runnableHandlerSemaphore.acquire();
								} catch (InterruptedException e) {
									// TODO 自动生成的 catch 块
									e.printStackTrace();
								}
								new Thread(handleRunnable).start();
								mayLoadData();
							} else {
								Toast.makeText(FinalMainActivity.this,
										"网络未连接，请检查网络状况", Toast.LENGTH_SHORT)
										.show();
							}
						} else {
							Toast.makeText(FinalMainActivity.this, "请登录账号",
									Toast.LENGTH_SHORT).show();
						}
						dialog.dismiss();
					}
				});
				
				hpxinxiCheckBox.setChecked(option);
				ckkcxinxiCheckBox.setChecked(option);
			}
		},new Object[]{});
		myDialog.show();
		
//		View view = inflater.inflate(R.layout.update_info, null);
//		AlertDialog.Builder builder = new AlertDialog.Builder(this);
//		// builder.setTitle("同步数据");
//		// builder.setMessage("数据库为空,为保证数据准确,请从服务端同步数据！");
//		builder.setView(view);
//		builder.setPositiveButton("同步数据",
//				new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						// TODO 自动生成的方法存根
//						dialog.dismiss();
//						if (mSharedPreferences.getInt(
//								ShareprefenceBean.IS_LOGIN, -1) == 1) {
//							if (WebserviceImport
//									.isOpenNetwork(FinalMainActivity.this)) {
//								hpxinxiflag = hpxinxiCheckBox.isChecked();
//								ckkcxinxiflag = ckkcxinxiCheckBox.isChecked();
//								hpleixinflag = hpleixinCheckBox.isChecked();
//								dwxinxiflag = dwxinxiCheckBox.isChecked();
//								canshuxinxiflag = canshuxinxiCheckBox
//										.isChecked();
//								cangkuxinxiflag = cangkuxinxiCheckBox
//										.isChecked();
//								try {
//									runnableHandlerSemaphore.acquire();
//								} catch (InterruptedException e) {
//									// TODO 自动生成的 catch 块
//									e.printStackTrace();
//								}
//								new Thread(handleRunnable).start();
//								mayLoadData();
//							} else {
//								Toast.makeText(FinalMainActivity.this,
//										"网络未连接，请检查网络状况", Toast.LENGTH_SHORT)
//										.show();
//							}
//						} else {
//							Toast.makeText(FinalMainActivity.this, "请登录账号",
//									Toast.LENGTH_SHORT).show();
//						}
//
//					}
//				});
//		// builder.setNegativeButton("以后同步", new
//		// DialogInterface.OnClickListener() {
//		//
//		// @Override
//		// public void onClick(DialogInterface dialog, int which) {
//		// // TODO 自动生成的方法存根
//		// dialog.dismiss();
//		//
//		// }
//		// });
//		AlertDialog syncDialog = builder.create();
//		syncDialog.setCancelable(true);
//		syncDialog.show();
	}

	/**
	 * 仓库的动态布局
	 **/
	public void dongtaiFirst() {
		FragmentTransaction fragmentTransaction = getSupportFragmentManager()
				.beginTransaction();
//		HomePagerFragment homePagerFragment = new HomePagerFragment();
		CangKuFirstFragment cangkuFirstFragment = new CangKuFirstFragment();
		fragmentTransaction.replace(R.id.contentpager, cangkuFirstFragment);
		fragmentTransaction.commit();
	}

	public void dongtaiTwo(){
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		OrderFragment orderFragment = new OrderFragment();
		fragmentTransaction.replace(R.id.contentpager, orderFragment);
		fragmentTransaction.commit();
	}
	
	/**
	 * 仓库的动态布局
	 **/
	public void dongtaiThree(){
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		ConsumerFragment consumerFragment = new ConsumerFragment();
		fragmentTransaction.replace(R.id.contentpager, consumerFragment);
		fragmentTransaction.commit();
	}
	
	/**
	 * 分析统计的动态布局
	 */
	public void dongtaiFour() {
		// LinearLayout contentTwo = (LinearLayout) inflater.inflate(
		// R.layout.contenttwo, null);
		// contentpagerlayout.removeAllViews();
		// contentpagerlayout.addView(contentTwo);
		FragmentTransaction fragmentTransaction = getSupportFragmentManager()
				.beginTransaction();
		Analytic_StatisticsFragment analytic_StatisticsFragment = new Analytic_StatisticsFragment();
		fragmentTransaction.replace(R.id.contentpager,
				analytic_StatisticsFragment);
		fragmentTransaction.commit();
	}

	/**
	 * 设置的动态布局
	 */
	public void dongtaiFive() {
//		String Imei = ((TelephonyManager) this
//				.getSystemService(TELEPHONY_SERVICE)).getDeviceId();
		FragmentTransaction fragmentTransaction = getSupportFragmentManager()
				.beginTransaction();
		NewSettingFragment settingFragment = new NewSettingFragment();
		fragmentTransaction.replace(R.id.contentpager, settingFragment);
		fragmentTransaction.commit();

	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.HomePage:
			if (tabNumber != 1) {
				imgView1.setImageDrawable(getResources().getDrawable(
						R.drawable.storage_orange_btn));
				textView1.setTextColor(getResources().getColor(R.color.theme_orange));
				imgView2.setImageDrawable(getResources().getDrawable(R.drawable.order_grey_btn));
				textView2.setTextColor(getResources().getColor(R.color.ziti666666));
				imgView3.setImageDrawable(getResources().getDrawable(R.drawable.customer_grey_btn));
				textView3.setTextColor(getResources().getColor(R.color.ziti666666));
				imgView4.setImageDrawable(getResources().getDrawable(R.drawable.pie_chart_grey_btn));
				textView4.setTextColor(getResources().getColor(R.color.ziti666666));
				imgView5.setImageDrawable(getResources().getDrawable(R.drawable.setting_grey_btn));
				textView5.setTextColor(getResources().getColor(R.color.ziti666666));
				dongtaiFirst();
				tabNumber = 1;
			}
			break;
		case R.id.orderPage:
			if(tabNumber !=2){
				imgView1.setImageDrawable(getResources().getDrawable(
						R.drawable.storage_grey_btn));
				textView1.setTextColor(getResources().getColor(R.color.ziti666666));
				imgView2.setImageDrawable(getResources().getDrawable(R.drawable.order_orange_btn));
				textView2.setTextColor(getResources().getColor(R.color.theme_orange));
				imgView3.setImageDrawable(getResources().getDrawable(R.drawable.customer_grey_btn));
				textView3.setTextColor(getResources().getColor(R.color.ziti666666));
				imgView4.setImageDrawable(getResources().getDrawable(R.drawable.pie_chart_grey_btn));
				textView4.setTextColor(getResources().getColor(R.color.ziti666666));
				imgView5.setImageDrawable(getResources().getDrawable(R.drawable.setting_grey_btn));
				textView5.setTextColor(getResources().getColor(R.color.ziti666666));
				dongtaiTwo();
				tabNumber = 2;
			}
			break;
		case R.id.consumerPage:
			if(tabNumber !=3){
				imgView1.setImageDrawable(getResources().getDrawable(R.drawable.storage_grey_btn));
				textView1.setTextColor(getResources().getColor(R.color.ziti666666));
				imgView2.setImageDrawable(getResources().getDrawable(R.drawable.order_grey_btn));
				textView2.setTextColor(getResources().getColor(R.color.ziti666666));
				imgView3.setImageDrawable(getResources().getDrawable(R.drawable.customer_orange_btn));
				textView3.setTextColor(getResources().getColor(R.color.theme_orange));
				imgView4.setImageDrawable(getResources().getDrawable(R.drawable.pie_chart_grey_btn));
				textView4.setTextColor(getResources().getColor(R.color.ziti666666));
				imgView5.setImageDrawable(getResources().getDrawable(R.drawable.setting_grey_btn));
				textView5.setTextColor(getResources().getColor(R.color.ziti666666));
				dongtaiThree();
				tabNumber = 3;
			}
			break;
		case R.id.analytic_statistics:
			if (tabNumber != 4) {
				if (RightsHelper.isHaveRight(RightsHelper.tj,
						mSharedPreferences) == true) {
					imgView1.setImageDrawable(getResources().getDrawable(
							R.drawable.storage_grey_btn));
					textView1.setTextColor(getResources().getColor(
							R.color.ziti666666));
					imgView2.setImageDrawable(getResources().getDrawable(R.drawable.order_grey_btn));
					textView2.setTextColor(getResources().getColor(R.color.ziti666666));
					imgView3.setImageDrawable(getResources().getDrawable(R.drawable.customer_grey_btn));
					textView3.setTextColor(getResources().getColor(R.color.ziti666666));
					imgView4.setImageDrawable(getResources().getDrawable(
							R.drawable.pie_chart_orange_btn));
					textView4.setTextColor(getResources().getColor(R.color.theme_orange));
					imgView5.setImageDrawable(getResources().getDrawable(
							R.drawable.setting_grey_btn));
					textView5.setTextColor(getResources().getColor(
							R.color.ziti666666));
					dongtaiFour();
				} else {
					Toast.makeText(this, "对不起，你没有分析统计权限。", Toast.LENGTH_SHORT).show();
				}
				tabNumber = 4;
			}
			break;
		case R.id.setting:
			if (tabNumber != 5) {
				imgView1.setImageDrawable(getResources().getDrawable(
						R.drawable.storage_grey_btn));
				textView1.setTextColor(getResources().getColor(R.color.ziti666666));
				imgView2.setImageDrawable(getResources().getDrawable(R.drawable.order_grey_btn));
				textView2.setTextColor(getResources().getColor(R.color.ziti666666));
				imgView3.setImageDrawable(getResources().getDrawable(R.drawable.customer_grey_btn));
				textView3.setTextColor(getResources().getColor(R.color.ziti666666));
				imgView4.setImageDrawable(getResources().getDrawable(
						R.drawable.pie_chart_grey_btn));
				textView4.setTextColor(getResources().getColor(R.color.ziti666666));
				imgView5.setImageDrawable(getResources().getDrawable(
						R.drawable.setting_orange_btn));
				textView5.setTextColor(getResources().getColor(R.color.theme_orange));
				dongtaiFive();
				tabNumber = 5;
			}
			break;
		}
	}

	Runnable ExitloadRun = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message msg = new Message();
			msg.what = WebserviceImport.delMEI(MyApplication.getInstance()
					.getIEMI(), mSharedPreferences);
			msg.setTarget(mHandler3);
			mHandler3.sendMessage(msg);
		}

	};
	@SuppressLint("HandlerLeak")
	Handler mHandler3 = new Handler() {
		public void handleMessage(Message msg) {
			pro_dialog.dismiss();
			finish();
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(0);
		}
	};
	
	private Runnable handleRunnable=new Runnable() {
		
		@Override
		public void run() {
			// TODO 自动生成的方法存根
			Looper.prepare();
			runnableHandler = new Handler(){
				public void handleMessage(Message msg) {
					stopflag = false;
					switch(msg.what){
					case 0:
						mpDialog = new CommonDialog(FinalMainActivity.this, R.layout.custom_progressbar_layout, R.style.yuanjiao_dialog);
						mpDialog.setCancelable(false);
						mpDialog.setDialogContentListener(new DialogContentListener() {
							
							@Override
							public void contentExecute(View parent, Dialog dialog,Object[] objs) {
								// TODO 自动生成的方法存根
								TextView titleTextView = mpDialog.getView(R.id.title);
								TextView cancelTextView = mpDialog.getView(R.id.cancel);
								TextView percentTextView = mpDialog.getView(R.id.percentTextView);
								ProgressBar pregressbar = mpDialog.getView(R.id.mybar);
								
								titleTextView.setText("正在更新货品信息");
								pregressbar.setMax(100);
								pregressbar.setProgress(0);
								cancelTextView.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										// TODO 自动生成的方法存根
										stopflag = true;
										mpDialog.dismiss();
									}
								});
							}
						},null);
						mpDialog.setCancelable(false);
						mpDialog.show();

//						mpDialog = new ProgressDialog(FinalMainActivity.this,R.style.yuanjiao_dialog);
//						mpDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//						mpDialog.setTitle("正在更新货品信息");
//						mpDialog.setMax(100);
//						mpDialog.setProgress(0);
//						mpDialog.setIndeterminate(false);
//						mpDialog.setCancelable(false);
//						mpDialog.setButton(-2, "取消",
//								new DialogInterface.OnClickListener() {
//
//									@Override
//									public void onClick(DialogInterface dialog,
//											int which) {
//										stopflag = true;
//										dialog.cancel();
//									}
//								});
//						mpDialog.show();
						fixedThreadPool.execute(loadRunnableList.removeFirst());
						break;
					case 1:
						mpDialog = new CommonDialog(FinalMainActivity.this, R.layout.custom_progressbar_layout, R.style.yuanjiao_dialog);
						mpDialog.setCancelable(false);
						mpDialog.setDialogContentListener(new DialogContentListener() {
							
							@Override
							public void contentExecute(View parent, Dialog dialog,Object[] objs) {
								// TODO 自动生成的方法存根
								TextView titleTextView = mpDialog.getView(R.id.title);
								TextView cancelTextView = mpDialog.getView(R.id.cancel);
								TextView percentTextView = mpDialog.getView(R.id.percentTextView);
								ProgressBar pregressbar = mpDialog.getView(R.id.mybar);
								
								titleTextView.setText("正在更新仓库库存信息");
								pregressbar.setMax(100);
								pregressbar.setProgress(0);
								cancelTextView.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										// TODO 自动生成的方法存根
										stopflag = true;
										mpDialog.dismiss();
									}
								});
							}
						},null);
						mpDialog.setCancelable(false);
						mpDialog.show();
						
//						mpDialog = new ProgressDialog(FinalMainActivity.this,R.style.yuanjiao_dialog);
//						mpDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//						mpDialog.setTitle("正在更新仓库库存信息");
//						mpDialog.setMax(100);
//						mpDialog.setProgress(0);
//						mpDialog.setIndeterminate(false);
//						mpDialog.setCancelable(false);
//						mpDialog.setButton(-2, "取消",
//								new DialogInterface.OnClickListener() {
//
//									@Override
//									public void onClick(DialogInterface dialog,
//											int which) {
//										stopflag = true;
//										dialog.cancel();
//
//									}
//
//								});
//						mpDialog.show();
						fixedThreadPool.execute(loadRunnableList.removeFirst());
						break;
					case 2:
						
						mpDialog = new CommonDialog(FinalMainActivity.this, R.layout.prompt_dialog_layout2, R.style.yuanjiao_dialog);
						mpDialog.setCancelable(false);
						mpDialog.setDialogContentListener(new DialogContentListener() {
							
							@Override
							public void contentExecute(View parent, Dialog dialog,Object[] objs) {
								// TODO 自动生成的方法存根
								TextView contentTextView = mpDialog.getView(R.id.content_txtview);
								TextView cancelTextView = mpDialog.getView(R.id.cancel);
								contentTextView.setText("正在更新货品类型");
								cancelTextView.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										// TODO 自动生成的方法存根
										stopflag = true;
										mpDialog.dismiss();
									}
								});
							}
						},null);
						mpDialog.setCancelable(false);
						mpDialog.show();
						
//						mpDialog = new ProgressDialog(FinalMainActivity.this,R.style.yuanjiao_dialog);
//						mpDialog.setMessage("正在更新货品类型");
//						mpDialog.setCancelable(false);
//						mpDialog.setButton(-2, "取消",
//								new DialogInterface.OnClickListener() {
//
//									@Override
//									public void onClick(DialogInterface dialog,
//											int which) {
//										stopflag = true;
//										dialog.cancel();
//									}
//
//								});
//						mpDialog.show();
						fixedThreadPool.execute(loadRunnableList.removeFirst());
						break;
					case 3:
						
						mpDialog = new CommonDialog(FinalMainActivity.this, R.layout.prompt_dialog_layout2, R.style.yuanjiao_dialog);
						mpDialog.setCancelable(false);
						mpDialog.setDialogContentListener(new DialogContentListener() {
							
							@Override
							public void contentExecute(View parent, Dialog dialog,Object[] objs) {
								// TODO 自动生成的方法存根
								TextView contentTextView = mpDialog.getView(R.id.content_txtview);
								TextView cancelTextView = mpDialog.getView(R.id.cancel);
								contentTextView.setText("正在更新往来单位");
								cancelTextView.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										// TODO 自动生成的方法存根
										stopflag = true;
										mpDialog.dismiss();
									}
								});
							}
						},null);
						mpDialog.setCancelable(false);
						mpDialog.show();
						
//						mpDialog = new ProgressDialog(FinalMainActivity.this,R.style.yuanjiao_dialog);
//						mpDialog.setMessage("正在更新往来单位");
//						mpDialog.setCancelable(false);
//						mpDialog.setButton(-2, "取消",
//								new DialogInterface.OnClickListener() {
//
//									@Override
//									public void onClick(DialogInterface dialog,
//											int which) {
//										stopflag = true;
//										dialog.cancel();
//									}
//
//								});
//						mpDialog.show();
						fixedThreadPool.execute(loadRunnableList.removeFirst());
						break;
					case 4:
						
						mpDialog = new CommonDialog(FinalMainActivity.this, R.layout.prompt_dialog_layout2, R.style.yuanjiao_dialog);
						mpDialog.setCancelable(false);
						mpDialog.setDialogContentListener(new DialogContentListener() {
							
							@Override
							public void contentExecute(View parent, Dialog dialog,Object[] objs) {
								// TODO 自动生成的方法存根
								TextView contentTextView = mpDialog.getView(R.id.content_txtview);
								TextView cancelTextView = mpDialog.getView(R.id.cancel);
								contentTextView.setText("正在更新往来单位");
								cancelTextView.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										// TODO 自动生成的方法存根
										
										mpDialog.dismiss();
									}
								});
							}
						},null);
						mpDialog.setCancelable(false);
						mpDialog.show();
						
//						mpDialog = new ProgressDialog(FinalMainActivity.this,R.style.yuanjiao_dialog);
//						mpDialog.setMessage("正在更新参数设置");
//						mpDialog.setCancelable(true);
//						mpDialog.show();
						fixedThreadPool.execute(loadRunnableList.removeFirst());
						break;
					case 5:
						
						mpDialog = new CommonDialog(FinalMainActivity.this, R.layout.prompt_dialog_layout2, R.style.yuanjiao_dialog);
						mpDialog.setCancelable(false);
						mpDialog.setDialogContentListener(new DialogContentListener() {
							
							@Override
							public void contentExecute(View parent, Dialog dialog,Object[] objs) {
								// TODO 自动生成的方法存根
								TextView contentTextView = mpDialog.getView(R.id.content_txtview);
								TextView cancelTextView = mpDialog.getView(R.id.cancel);
								contentTextView.setText("正在更新仓库信息");
								cancelTextView.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										// TODO 自动生成的方法存根
										
										mpDialog.dismiss();
									}
								});
							}
						},null);
						mpDialog.setCancelable(false);
						mpDialog.show();
						
//						mpDialog = new ProgressDialog(FinalMainActivity.this,R.style.yuanjiao_dialog);
//						mpDialog.setMessage("正在更新仓库信息");
//						mpDialog.setCancelable(true);
//						mpDialog.show();
						fixedThreadPool.execute(loadRunnableList.removeFirst());
						break;
					case 6:
						fixedThreadPool.execute(loadRunnableList.removeFirst());
						break;
					case 7:
						fixedThreadPool.execute(loadRunnableList.removeFirst());
						break;
					}
				};
			};
			runnableHandlerSemaphore.release();
			Looper.loop();
		}
	};
	
	private void mayLoadData() {
		try {
			if(runnableHandler==null){
			runnableHandlerSemaphore.acquire();
			}
		} catch (InterruptedException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		if(hpxinxiflag){
			Message msg = Message.obtain();
			try {
				mSemaphore.acquire();
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			loadRunnableList.add(hpxinxiRunnable);
			msg.what=0;
			runnableHandler.sendMessage(msg);
		}
		if(ckkcxinxiflag){
			Message msg = Message.obtain();
			try {
				mSemaphore.acquire();
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			loadRunnableList.add(ckkcRunnable);
			msg.what=1;
			runnableHandler.sendMessage(msg);
		}
		if(hpleixinflag){
			Message msg = Message.obtain();
			try {
				mSemaphore.acquire();
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			loadRunnableList.add(hpleixiRunnable);
			msg.what=2;
			runnableHandler.sendMessage(msg);
		}
		if(dwxinxiflag){
			Message msg = Message.obtain();
			try {
				mSemaphore.acquire();
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			loadRunnableList.add(dwxinxiRunnable);
			msg.what=3;
			runnableHandler.sendMessage(msg);
		}
		if(canshuxinxiflag){
			Message msg = Message.obtain();
			try {
				mSemaphore.acquire();
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			loadRunnableList.add(canshuxinxiRunnable);
			msg.what=4;
			runnableHandler.sendMessage(msg);
		}
		if(cangkuxinxiflag){
			Message msg = Message.obtain();
			try {
				mSemaphore.acquire();
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			loadRunnableList.add(cangkuxinxiRunnable);
			msg.what=5;
			runnableHandler.sendMessage(msg);
		}
		
		Message msg = Message.obtain();
		try {
			mSemaphore.acquire();
		} catch (InterruptedException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		loadRunnableList.add(tongjiRunnable);
		msg.what=6;
		runnableHandler.sendMessage(msg);
		runnableHandlerSemaphore.release();
	}
	
	private Handler myHandler = new Handler(){
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0:
				mSharedPreferences.edit().putString(ShareprefenceBean.UPDATE_TIME_HP,formatter.format(new Date(System.currentTimeMillis()))).commit();
//				Toast.makeText(FinalMainActivity.this, "货品信息更新完毕", Toast.LENGTH_SHORT).show();
				break;
			case -2:
				Toast.makeText(FinalMainActivity.this, "获取货品数量异常", Toast.LENGTH_SHORT)
						.show();
				break;
			case -3:
				Toast.makeText(FinalMainActivity.this, "货品信息数据异常", Toast.LENGTH_SHORT)
						.show();
				break;
			case 1:
				mSharedPreferences.edit().putString(ShareprefenceBean.UPDATE_TIME_CKKC,formatter.format(new Date(System.currentTimeMillis()))).commit();
//				Toast.makeText(FinalMainActivity.this, "仓库库存信息更新完毕", Toast.LENGTH_SHORT).show();
				break;
			case -9:
				Toast.makeText(FinalMainActivity.this, "获取仓库库存信息数量异常", Toast.LENGTH_SHORT).show();
				break;
			case -7:
				mSharedPreferences.edit().putString(ShareprefenceBean.UPDATE_TIME_CKKC,formatter.format(new Date(System.currentTimeMillis()))).commit();
				Toast.makeText(FinalMainActivity.this, "仓库库存信息数据异常", Toast.LENGTH_SHORT).show();
				break;
			case 2:
				mSharedPreferences.edit().putString(ShareprefenceBean.UPDATE_TIME_LB,formatter.format(new Date(System.currentTimeMillis()))).commit();
//				Toast.makeText(FinalMainActivity.this, "货品类型更新完毕", Toast.LENGTH_SHORT).show();
				break;
			case -4:
				Toast.makeText(FinalMainActivity.this, "货品类型数据异常", Toast.LENGTH_SHORT).show();
				break;
			case 3:
				mSharedPreferences.edit().putString(ShareprefenceBean.UPDATE_TIME_DW,formatter.format(new Date(System.currentTimeMillis()))).commit();
//				Toast.makeText(FinalMainActivity.this, "往来单位更新完毕", Toast.LENGTH_SHORT).show();
				break;
			case -5:
				Toast.makeText(FinalMainActivity.this, "往来单位数据异常", Toast.LENGTH_SHORT).show();
				break;
			case 4:
				mSharedPreferences.edit().putString(ShareprefenceBean.UPDATE_TIME_TYPE,formatter.format(new Date(System.currentTimeMillis()))).commit();
//				Toast.makeText(FinalMainActivity.this, "参数更新完毕", Toast.LENGTH_SHORT).show();
				break;
			case -6:
				Toast.makeText(FinalMainActivity.this, "参数数据异常", Toast.LENGTH_SHORT).show();
				break;
			case 5:
				mSharedPreferences.edit().putString(ShareprefenceBean.UPDATE_TIME_CK,formatter.format(new Date(System.currentTimeMillis()))).commit();
//				Toast.makeText(FinalMainActivity.this, "仓库信息更新完毕", Toast.LENGTH_SHORT).show();
				break;
			case -8:
				Toast.makeText(FinalMainActivity.this, "仓库信息数据异常", Toast.LENGTH_SHORT).show();
				break;
			}
		};
	};
	
	private Runnable hpxinxiRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO 自动生成的方法存根
			Message msg = Message.obtain();
			try {
				db = DataBaseImport.getInstance(FinalMainActivity.this).getReadableDatabase();
				db.beginTransaction();
				dm_op.del_tableContent(DataBaseHelper.TB_HP, db);
				int total = WebserviceImport.Get_Total(
						WebserviceHelper.GetHp_Total, -1, mSharedPreferences);
				int leave = WebserviceImport.GetHp_Total_Leave("-1",
						mSharedPreferences);
				String id = "0", date = "";
				hp_num = 0;
				String[] values = new String[str.length];
				if (total >= 0 && (leave > 0 || leave == 0)) {
						while (leave != 0) {
							if (stopflag == true) {
								break;
							}
							if (total == leave) {
								ls = WebserviceImport.GetHpInfo_top(200, "0", 1,
										-1, str, str1, mSharedPreferences);
								if (!ls.isEmpty()) {
									id = (String) ls.get(ls.size() - 1).get(
											DataBaseHelper.ID);
									leave = WebserviceImport.GetHp_Total_Leave(id,
											mSharedPreferences);
//									mpDialog.setProgress(GtProgress(leave, total));
									((ProgressBar) mpDialog.getView(R.id.mybar)).setProgress(GtProgress(leave, total));
//									((TextView) mpDialog.getView(R.id.percentTextView)).setText(GtProgress(leave, total)+"%");
									date = formatter.format(new Date(System
											.currentTimeMillis()));
									for (int i = 0; i < ls.size(); i++) {
										for (int j = 0; j < str.length; j++) {

											if (RightsHelper.isHaveRight(
													RightsHelper.system_cw,
													mSharedPreferences) == true) {
												values[j] = (String) ls.get(i).get(
														str[j]);
											} else {
												if (str[j]
														.equals(DataBaseHelper.RKCKJ)
														|| str[j]
																.equals(DataBaseHelper.CKCKJ)
														|| str[j]
																.equals(DataBaseHelper.CKCKJ2)
														|| str[j]
																.equals(DataBaseHelper.KCJE)) {
													values[j] = "";
												} else {
													values[j] = (String) ls.get(i)
															.get(str[j]);
												}
											}
										}
										dm_op.insert_into_fromfile(
												DataBaseHelper.TB_HP, str, values,
												db, date);
										hp_num++;
									}
								}
							} else {
								ls = WebserviceImport.GetHpInfo_top(200, id, 1, -1,
										str, str1, mSharedPreferences);
								if (!ls.isEmpty()) {
									id = (String) ls.get(ls.size() - 1).get(
											DataBaseHelper.ID);
									leave = WebserviceImport.GetHp_Total_Leave(id,
											mSharedPreferences);
//									mpDialog.setProgress(GtProgress(leave, total));
									((ProgressBar) mpDialog.getView(R.id.mybar)).setProgress(GtProgress(leave, total));
//									((TextView) mpDialog.getView(R.id.percentTextView)).setText(GtProgress(leave, total)+"%");
									date = formatter.format(new Date(System
											.currentTimeMillis()));
									for (int i = 0; i < ls.size(); i++) {
										for (int j = 0; j < str.length; j++) {
											if (RightsHelper.isHaveRight(
													RightsHelper.system_cw,
													mSharedPreferences) == true) {
												values[j] = (String) ls.get(i).get(
														str[j]);
											} else {
												if (str[j]
														.equals(DataBaseHelper.RKCKJ)
														|| str[j]
																.equals(DataBaseHelper.CKCKJ)
														|| str[j]
																.equals(DataBaseHelper.CKCKJ2)
														|| str[j]
																.equals(DataBaseHelper.KCJE)) {
													values[j] = "";
												} else {
													values[j] = (String) ls.get(i)
															.get(str[j]);
												}
											}
										}
										dm_op.insert_into_fromfile(
												DataBaseHelper.TB_HP, str, values,
												db, date);
										hp_num++;
									}
								}
							}
						}
					msg.what = 0;
				} else if (total <= -1 || leave < 0) {
					msg.what = -2;
				} else {
					msg.what = 0;
				}
			} catch (Exception e) {
				msg.what = -3;
			} finally {
				db.setTransactionSuccessful();
				db.endTransaction();
				db.close();
				mpDialog.dismiss();
				mSemaphore.release();
				myHandler.sendMessage(msg);
			}
			
		}
	};

	private Runnable ckkcRunnable = new Runnable() {
		
		@Override
		public void run() {
			// TODO 自动生成的方法存根
			Message msg = Message.obtain();
			try{
				db = DataBaseImport.getInstance(FinalMainActivity.this).getReadableDatabase();
				db.beginTransaction();
				dm_op.del_tableContent(DataBaseHelper.TB_CKKC, db);
				int total = WebserviceImport
						.GetCKKC_Total(mSharedPreferences);
				int leave = WebserviceImport.GetCKKC_Total_Leave("-1",
						mSharedPreferences);
				String id = "0";
				String[] values = new String[str12.length];
				if (total >= 0 && (leave > 0 || leave == 0)) {
						while (leave != 0) {
							if (stopflag == true) {
								break;
							}
							if (total == leave) {
								ls = WebserviceImport.GetCKKC_top(200,
										"0", str12, str13,
										mSharedPreferences);
								if (!ls.isEmpty()) {
									id = (String) ls.get(ls.size() - 1)
											.get(DataBaseHelper.ID);
									leave = WebserviceImport
											.GetCKKC_Total_Leave(id,
													mSharedPreferences);
//									mpDialog.setProgress(GtProgress(
//											leave, total));
									((ProgressBar) mpDialog.getView(R.id.mybar)).setProgress(GtProgress(leave, total));
//									((TextView) mpDialog.getView(R.id.percentTextView)).setText(GtProgress(leave, total)+"%");
									for (int i = 0; i < ls.size(); i++) {
										for (int j = 0; j < str12.length; j++) {
											values[j] = (String) ls
													.get(i).get(
															str12[j]);
										}
										dm_op.insert_into_fromfile(
												DataBaseHelper.TB_CKKC,
												str12, values, db);
										ckkc_num++;
									}
								}
							} else {
								ls = WebserviceImport.GetCKKC_top(200,
										id, str12, str13,
										mSharedPreferences);
								if (!ls.isEmpty()) {
									id = (String) ls.get(ls.size() - 1)
											.get(DataBaseHelper.ID);
									leave = WebserviceImport
											.GetCKKC_Total_Leave(id,
													mSharedPreferences);
//									mpDialog.setProgress(GtProgress(
//											leave, total));
									((ProgressBar) mpDialog.getView(R.id.mybar)).setProgress(GtProgress(leave, total));
//									((TextView) mpDialog.getView(R.id.percentTextView)).setText(GtProgress(leave, total)+"%");
									for (int i = 0; i < ls.size(); i++) {
										for (int j = 0; j < str12.length; j++) {
											values[j] = (String) ls
													.get(i).get(
															str12[j]);
										}
//										dm_op.Del_CKKC(
//												(String) ls
//														.get(i)
//														.get(DataBaseHelper.HPID),
//												(String) ls
//														.get(i)
//														.get(DataBaseHelper.CKID),
//												db);
										dm_op.insert_into_fromfile(
												DataBaseHelper.TB_CKKC,
												str12, values, db);
										ckkc_num++;
									}

								}
							}
						}
						msg.what=1;
				} else {
					msg.what = -9;
				}
			} catch (Exception e) {
				msg.what = -7;
			} finally {
				db.setTransactionSuccessful();
				db.endTransaction();
				db.close();
				mpDialog.dismiss();
				mSemaphore.release();
				myHandler.sendMessage(msg);
			}
		}
	};
	
	private Runnable hpleixiRunnable= new Runnable() {
		public void run() {
			Message msg = Message.obtain();
			try {
				int maxid = WebserviceImport.GetMaxID_LB(mSharedPreferences);
				String id = "0";
				lb_num = 0;
				String[] values = new String[str2.length];
				db = DataBaseImport.getInstance(FinalMainActivity.this).getReadableDatabase();
				db.beginTransaction();
				dm_op.del_tableContent(DataBaseHelper.TB_hplbTree, db);
				while (!id.equals(String.valueOf(maxid))) {
					if (stopflag == true) {
						break;
					}
					ls = WebserviceImport.GetHPLB(200, id, str2,
							str3, mSharedPreferences);
					if (!ls.isEmpty()) {
						for (int i = 0; i < ls.size(); i++) {
							for (int j = 0; j < str2.length; j++) {
								values[j] = (String) ls.get(i).get(
										str2[j]);
							}
							dm_op.insert_into_fromfile(
									DataBaseHelper.TB_hplbTree,
									str2, values, db);
							lb_num++;
						}
						id = (String) ls.get(ls.size() - 1).get(
								DataBaseHelper.ID);
					} else {
						break;
					}
				}
				msg.what=2;
			} catch (Exception e) {
				msg.what = -4;
			} finally {
				db.setTransactionSuccessful();
				db.endTransaction();
				db.close();
				mpDialog.dismiss();
				mSemaphore.release();
				myHandler.sendMessage(msg);
			}
		}
	};
	
	private Runnable dwxinxiRunnable = new Runnable() {
		
		@Override
		public void run() {
			// TODO 自动生成的方法存根
			Message msg = Message.obtain();
			try {
			int maxid = WebserviceImport
					.GetMaxID_DW(mSharedPreferences);
			String id = "0";
			dw_num = 0;
			String[] values = new String[str4.length];
			db = DataBaseImport.getInstance(FinalMainActivity.this).getReadableDatabase();
			db.beginTransaction();
			dm_op.del_tableContent(DataBaseHelper.TB_Company, db);
				while (!id.equals(String.valueOf(maxid))) {
					if (stopflag == true) {
						break;
					}
					ls = WebserviceImport.GetDW(200, id, str4,
							str5, mSharedPreferences);
					if (!ls.isEmpty()) {
						for (int i = 0; i < ls.size(); i++) {
							for (int j = 0; j < str4.length; j++) {
								values[j] = (String) ls.get(i).get(
										str4[j]);
							}
							dm_op.insert_into_fromfile(
									DataBaseHelper.TB_Company,
									str4, values, db);
							dw_num++;
						}
						id = (String) ls.get(ls.size() - 1).get(
								DataBaseHelper.ID);
					} else {
						break;
					}
				}
				msg.what=3;
			} catch (Exception e) {
				msg.what = -5;
			} finally {
				db.setTransactionSuccessful();
				db.endTransaction();
				db.close();
				mpDialog.dismiss();
				mSemaphore.release();
				myHandler.sendMessage(msg);
			}
		}
	};
	
	private Runnable canshuxinxiRunnable = new Runnable() {
		
		@Override
		public void run() {
			// TODO 自动生成的方法存根
			Message msg = Message.obtain();
			try {
			conf_num = 0;
			String[] values = new String[str6.length];
			db = DataBaseImport.getInstance(FinalMainActivity.this).getReadableDatabase();
			db.beginTransaction();
			dm_op.del_tableContent(DataBaseHelper.TB_Conf, db);
				ls = WebserviceImport.GetConf("自定义字段", "", str6,
						str7, mSharedPreferences);
				if (!ls.isEmpty()) {
//					dm_op.Del_Conf(new String[] { "自定义字段" }, db);
					for (int i = 0; i < ls.size(); i++) {
						for (int j = 0; j < str6.length; j++) {
							values[j] = (String) ls.get(i).get(
									str6[j]);
						}
						dm_op.insert_into_fromfile(
								DataBaseHelper.TB_Conf, str6,
								values, db);
						conf_num++;
					}
				}
				
				ls = WebserviceImport.GetConf("单据自定义字段", "", str6, str7, mSharedPreferences);
				if(!ls.isEmpty()){
					for (int i = 0; i < ls.size(); i++) {
						for (int j = 0; j < str6.length; j++) {
							values[j] = (String) ls.get(i).get(
									str6[j]);
						}

						dm_op.insert_into_fromfile(
								DataBaseHelper.TB_Conf, str6,
								values, db);
						conf_num++;
					}
				}
				
				ls = WebserviceImport.GetConf("财务信息", "", str6,
						str7, mSharedPreferences);
				if (!ls.isEmpty()) {
//					dm_op.Del_Conf(new String[] { "财务信息" }, db);
					for (int i = 0; i < ls.size(); i++) {
						for (int j = 0; j < str6.length; j++) {
							values[j] = (String) ls.get(i).get(
									str6[j]);
						}
						dm_op.insert_into_fromfile(
								DataBaseHelper.TB_Conf, str6,
								values, db);
						conf_num++;
					}
				}
				
				ls = WebserviceImport.GetConf("条码识别", "", str6, str7, mSharedPreferences);
				if(!ls.isEmpty()){
//					dm_op.Del_Conf(new String[] { "条码识别" }, db);
					for(int i=0;i<ls.size();i++){
						for(int j = 0;j<str6.length;j++){
							values[j] = ls.get(i).get(str6[j]).toString();
						}
						dm_op.insert_into_fromfile(DataBaseHelper.TB_Conf, str6, values, db);
						conf_num++;
					}
				}
				
				ls = WebserviceImport.GetIOType(str10, str11,
						mSharedPreferences);
				values = new String[str10.length];
				if (!ls.isEmpty()) {
//					dm_op.Del_Conf(new String[] { "入库类型" }, db);
//					dm_op.Del_Conf(new String[] { "出库类型" }, db);
					for (int i = 0; i < ls.size(); i++) {
						for (int j = 0; j < str10.length; j++) {
							if (str10[j].equals(DataBaseHelper.GID)) {
								if (ls.get(i).get(str10[j])
										.equals("1")) {
									values[j] = "入库类型";
								} else if (ls.get(i).get(str10[j])
										.equals("2")) {
									values[j] = "出库类型";
								}
							} else {
								values[j] = (String) ls.get(i).get(
										str10[j]);
							}

						}
						dm_op.insert_into_fromfile(
								DataBaseHelper.TB_Conf, str10,
								values, db);
						conf_num++;
					}
				}
				ls = WebserviceImport.GetDep(str17, str16,
						mSharedPreferences);
				values = new String[str16.length];
				dm_op.del_tableContent(DataBaseHelper.TB_depTree, db);
				if (!ls.isEmpty()) {
					for (int i = 0; i < ls.size(); i++) {
						for (int j = 0; j < str16.length; j++) {
							values[j] = (String) ls.get(i).get(
									str17[j]);
						}
//						dm.del_Dep((String) ls.get(i).get("id"), db);
						dm_op.insert_into_fromfile(
								DataBaseHelper.TB_depTree, str17,
								values, db);
						conf_num++;
					}
				}
				msg.what = 4;
			} catch (Exception e) {
				msg.what = -6;
			} finally {
				db.setTransactionSuccessful();
				db.endTransaction();
				db.close();
				MyApplication.getInstance().setALLPoint();
				mpDialog.dismiss();
				mSemaphore.release();
				myHandler.sendMessage(msg);
			}
		}
	};
	
	private Runnable cangkuxinxiRunnable = new Runnable() {
		
		@Override
		public void run() {
			// TODO 自动生成的方法存根
			Message msg = Message.obtain();
			try {
				db = DataBaseImport.getInstance(FinalMainActivity.this).getReadableDatabase();
				db.beginTransaction();
				dm_op.del_tableContent(DataBaseHelper.TB_CK, db);
				ck_num = 0;
				ls = WebserviceImport.GetCK(str14, str15,
						mSharedPreferences);
				if (!ls.isEmpty()) {
					String[] values = new String[str14.length];
					for (int i = 0; i < ls.size(); i++) {
						String id = (String) ls.get(i).get(
								DataBaseHelper.ID);
						if (id != null && !id.equals("")) {
							for (int j = 0; j < str14.length; j++) {
								values[j] = (String) ls.get(i).get(
										str14[j]);
							}
							// dm_op.Del_CK(id);
							dm_op.insert_into1(DataBaseHelper.TB_CK,
									str14, values, db);
							ck_num++;
						}
					}
				}
				msg.what=5;
			} catch (Exception e) {
				// TODO: handle exception
				msg.what=-8;
			} finally{
				db.setTransactionSuccessful();
				db.endTransaction();
				db.close();
				mpDialog.dismiss();
				mSemaphore.release();
				myHandler.sendMessage(msg);
			}
		}
	};
	

	private Runnable tongjiRunnable = new Runnable() {
		
		@Override
		public void run() {
			// TODO 自动生成的方法存根
			Looper.prepare();
			StringBuilder strBuilder = new StringBuilder();
			if (hpxinxiflag) {
				strBuilder.append("货品信息更新：" + String.valueOf(hp_num)
						+ "条\n");
			}
			if(ckkcxinxiflag){
				strBuilder.append("仓库库存信息更新：" + String.valueOf(ckkc_num)
						+ "条\n");
			}
			if (hpleixinflag) {
				strBuilder.append("货品类型更新：" + String.valueOf(lb_num)
						+ "条\n");
			}
			if (dwxinxiflag) {
				strBuilder.append("往来单位信息更新：" + String.valueOf(dw_num)
						+ "条\n");
			}
			if (canshuxinxiflag) {
				strBuilder.append("参数更新：" + String.valueOf(conf_num)
						+ "条\n");
			}
			if (cangkuxinxiflag) {
				strBuilder.append("仓库信息更新：" + String.valueOf(ck_num) + "条");
			}
			
			mpDialog = new CommonDialog(FinalMainActivity.this, R.layout.prompt_dialog_layout2, R.style.yuanjiao_dialog);
			mpDialog.setDialogContentListener(new DialogContentListener() {
				
				@Override
				public void contentExecute(View parent, Dialog dialog, Object[] objs) {
					// TODO 自动生成的方法存根
					TextView contentTextView = mpDialog.getView(R.id.content_txtview);
					TextView cancelTextView = mpDialog.getView(R.id.cancel);
					contentTextView.setText(objs[0].toString());
					cancelTextView.setText("确定");
					cancelTextView.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO 自动生成的方法存根
							
							mpDialog.dismiss();
						}
					});
				}
			},new Object[]{strBuilder});
			mpDialog.show();
			
//			AlertDialog.Builder builder = new AlertDialog.Builder(
//					FinalMainActivity.this);
//			builder.setMessage(strBuilder.toString());
//			builder.setCancelable(false);
//			builder.setPositiveButton("确认",
//					new DialogInterface.OnClickListener() {
//
//						@Override
//						public void onClick(DialogInterface dialog,
//								int which) {
//							// TODO Auto-generated method stub
//							hp_num = 0;
//							lb_num = 0;
//							dw_num = 0;
//							conf_num = 0;
//							ck_num = 0;
//							ckkc_num = 0;
//							dialog.dismiss();
//
//						}
//					});
//			builder.create().show();
			mSemaphore.release();
			Looper.loop();
		}
	};
	
	
	private int GtProgress(int leave, int total) {
		if (leave == total) {
			return 100;
		} else {
			return (total - leave) * 100 / total;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stu
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			
			CommonDialog myDialog = new CommonDialog(this, R.layout.prompt_dialog_layout, R.style.yuanjiao_dialog);
			myDialog.setDialogContentListener(new DialogContentListener() {
				
				@Override
				public void contentExecute(View parent, final Dialog dialog, Object[] objs) {
					// TODO 自动生成的方法存根
					TextView titleTextView = (TextView) parent.findViewById(R.id.title);
					TextView contentTextview = (TextView) parent.findViewById(R.id.content_txtview);
					TextView cancelTextview = (TextView) parent.findViewById(R.id.cancel);
					TextView confirmTextView = (TextView) parent.findViewById(R.id.confirm);
					
					titleTextView.setVisibility(View.GONE);
					contentTextview.setText("是否退出程序？");
					cancelTextview.setText("否");
					confirmTextView.setText("是");
					cancelTextview.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO 自动生成的方法存根
							dialog.dismiss();
						}
					});
					
					confirmTextView.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO 自动生成的方法存根
							if (mSharedPreferences.getInt(
									ShareprefenceBean.IS_LOGIN, -1) == 1) {
								pro_dialog = ProgressDialog.show(
										FinalMainActivity.this, null, "正在退出");
								new Thread(ExitloadRun).start();
								mSharedPreferences.edit()
										.putInt(ShareprefenceBean.IS_LOGIN, -1)
										.commit();
								dialog.dismiss();
							} else if (mSharedPreferences.getInt(
									ShareprefenceBean.IS_LOGIN, -1) == -1) {
								mSharedPreferences.edit()
										.putInt(ShareprefenceBean.IS_LOGIN, -1)
										.commit();
								dialog.dismiss();
								// 关闭app进程
								finish();
								android.os.Process
										.killProcess(android.os.Process.myPid());
								System.exit(0);
							}
						}
					});
					
				}
			}, null);
			myDialog.show();
			
//			final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//			builder.setMessage("是否退出程序？");
//			builder.setPositiveButton("是",
//					new DialogInterface.OnClickListener() {
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							// TODO Auto-generated method stub
//
//							if (mSharedPreferences.getInt(
//									ShareprefenceBean.IS_LOGIN, -1) == 1) {
//								pro_dialog = ProgressDialog.show(
//										FinalMainActivity.this, null, "正在退出");
//								new Thread(ExitloadRun).start();
//								mSharedPreferences.edit()
//										.putInt(ShareprefenceBean.IS_LOGIN, -1)
//										.commit();
//								dialog.dismiss();
//							} else if (mSharedPreferences.getInt(
//									ShareprefenceBean.IS_LOGIN, -1) == -1) {
//								mSharedPreferences.edit()
//										.putInt(ShareprefenceBean.IS_LOGIN, -1)
//										.commit();
//								dialog.dismiss();
//								// 关闭app进程
//								finish();
//								android.os.Process
//										.killProcess(android.os.Process.myPid());
//								System.exit(0);
//							}
//						}
//					});
//			builder.setNegativeButton("否",
//					new DialogInterface.OnClickListener() {
//
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							// TODO Auto-generated method stub
//							builder.create().dismiss();
//						}
//					});
//			builder.create().show();
		}
		return super.onKeyDown(keyCode, event);
	}
}
