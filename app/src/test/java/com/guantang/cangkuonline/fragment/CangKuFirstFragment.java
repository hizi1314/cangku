package com.guantang.cangkuonline.fragment;

import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.activity.AddActivity;
import com.guantang.cangkuonline.activity.AddDJActivity;
import com.guantang.cangkuonline.activity.AddDiaoboActivity;
import com.guantang.cangkuonline.activity.CaptureActivity;
import com.guantang.cangkuonline.activity.HpInfo_ListActivity;
import com.guantang.cangkuonline.activity.HpManagerListActivity;
import com.guantang.cangkuonline.activity.LocalDJActivity;
import com.guantang.cangkuonline.activity.Moved_DJ;
import com.guantang.cangkuonline.activity.MySubmitDJActivity;
import com.guantang.cangkuonline.activity.NetDJActivity;
import com.guantang.cangkuonline.activity.PanDianActivity;
import com.guantang.cangkuonline.activity.SearchActivity;
import com.guantang.cangkuonline.activity.WebHelper;
import com.guantang.cangkuonline.adapter.CkListViewAdapter;
import com.guantang.cangkuonline.adapter.GridViewAdapter;
import com.guantang.cangkuonline.application.MyApplication;
import com.guantang.cangkuonline.customview.TagsGridView;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.database.DataBaseMethod;
import com.guantang.cangkuonline.database.DataBaseOperateMethod;
import com.guantang.cangkuonline.eventbusBean.EventObject8;
import com.guantang.cangkuonline.helper.PwdHelper;
import com.guantang.cangkuonline.helper.RightsHelper;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;
import com.guantang.cangkuonline.webservice.WebserviceHelper;
import com.guantang.cangkuonline.webservice.WebserviceImport;
import com.guantang.cangkuonline.webservice.WebserviceImport_more;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;

import de.greenrobot.event.EventBus;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PatternMatcher;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CangKuFirstFragment extends Fragment implements OnClickListener,OnItemClickListener{
	
	private TextView titleTxtView,today_runumTxtView,today_chunumTxtView,xxnumTxtView,sxnumTxtView,cangkuTxtView;
	private ImageView searchImgView,scanImgView;
	private TagsGridView gridview;
	private SimpleDateFormat formatter1;
	private int ckid=-1;
	private SharedPreferences mSharedPreferences;
	private ExecutorService cacheThreadPool = Executors.newCachedThreadPool();
	private List<Map<String, Object>> ckNameAndID = new ArrayList<Map<String,Object>>();
	private String[] str15 = { "ID", "CKMC", "FZR", "TEL", "ADDR", "inact",
			"outact", "BZ" };
	private String[] str_ck = { DataBaseHelper.ID, DataBaseHelper.CKMC,
			DataBaseHelper.FZR, DataBaseHelper.TEL, DataBaseHelper.ADDR,
			DataBaseHelper.INACT, DataBaseHelper.OUTACT, DataBaseHelper.BZ };
	private AlertDialog.Builder builder;
	private AlertDialog Dialog,threechoiceDialog;
	private int all_xx=0,all_sx=0;
	private GridViewAdapter mGridViewAdapter;
	/**
	 * 引入一个值为1的信号量，防止ckNameAndID装数据的时候就刷新界面
	 */
	private volatile Semaphore mSemaphore = new Semaphore(1);
	
	private List<Map<String, Object>> cklist = new ArrayList<Map<String,Object>>();
	private DataBaseMethod dm;
	private DataBaseOperateMethod dm_op;
	private ProgressDialog pro_dialog;
	private PwdHelper pwdhelper = new PwdHelper();
	private Context context;
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		context = activity;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		View view = inflater.inflate(R.layout.cangkufirst_layout, null);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onActivityCreated(savedInstanceState);
		titleTxtView = (TextView) getView().findViewById(R.id.titletxtView);
		today_runumTxtView = (TextView) getView().findViewById(R.id.today_runum);
		today_chunumTxtView = (TextView) getView().findViewById(R.id.today_chunum);
		xxnumTxtView = (TextView) getView().findViewById(R.id.xxnum);
		sxnumTxtView = (TextView) getView().findViewById(R.id.sxnum);
		cangkuTxtView = (TextView) getView().findViewById(R.id.cangkuTxtView);
		searchImgView = (ImageView) getView().findViewById(R.id.searchImgView);
		scanImgView = (ImageView) getView().findViewById(R.id.scanImgView);
		gridview = (TagsGridView) getView().findViewById(R.id.gridview);
		
		titleTxtView.setOnClickListener(this);
		cangkuTxtView.setOnClickListener(this);
		today_runumTxtView.setOnClickListener(this);
		today_chunumTxtView.setOnClickListener(this);
		xxnumTxtView.setOnClickListener(this);
		sxnumTxtView.setOnClickListener(this);
		searchImgView.setOnClickListener(this);
		scanImgView.setOnClickListener(this);
		gridview.setOnItemClickListener(this);
		
		mSharedPreferences = MyApplication.getInstance().getSharedPreferences();
		
		dm= new DataBaseMethod(context);
		dm_op = new DataBaseOperateMethod(context);
		
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		mGridViewAdapter = new GridViewAdapter(context, dm.widthPixels);
		gridview.setAdapter(mGridViewAdapter);
		new functionAsyncTask().execute();
	}
	
	@Override
	public void onStart() {
		// TODO 自动生成的方法存根
		super.onStart();
		formatter1 = new SimpleDateFormat("yyyy-MM-dd");
		ckid = mSharedPreferences.getInt(ShareprefenceBean.SHOUYE_CKID, -1);
		cangkuTxtView.setText(mSharedPreferences.getString(ShareprefenceBean.SHOUYE_CKMC, "所有仓库"));
		if (mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1) == 1) {
			getDWname();
			if (WebserviceImport.isOpenNetwork(context)) {
				cacheThreadPool.execute(ck_run);
				cacheThreadPool.execute(hpinfo_run);
				cacheThreadPool.execute(movem_Amount);
			} else {
				Toast.makeText(context, "网络未连接", Toast.LENGTH_SHORT).show();
				ckNameAndID.clear();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("ckmc", "所有仓库");
				map.put("ckid", "-1");
				ckNameAndID.add(map);
				
				today_runumTxtView.setText("获取失败");
				today_chunumTxtView.setText("获取失败");
				xxnumTxtView.setText("获取失败");
				sxnumTxtView.setText("获取失败");
			}
		}else{
			titleTxtView.setText("离线");
			ckNameAndID.clear();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("ckmc", "所有仓库");
			map.put("ckid", "-1");
			ckNameAndID.add(map);
			List<Map<String, Object>> list1 = dm.getAllCK();
			if(list1.size()>0){
				Iterator<Map<String, Object>> it=list1.iterator();
				while(it.hasNext()){
					Map<String, Object> map2 = new HashMap<String, Object>();
					Map<String, Object> map1 = it.next();
					map2.put("ckmc", map1.get(DataBaseHelper.CKMC).toString());
					map2.put("ckid", map1.get(DataBaseHelper.ID).toString());
					ckNameAndID.add(map2);
				}
			}
			String timeString = formatter1.format(new Date(System.currentTimeMillis()));
			xxnumTxtView.setText(String.valueOf(dm.GethpAmount_XX(ckid)));
			sxnumTxtView.setText(String.valueOf(dm.GethpAmount_SX(ckid)));
			today_runumTxtView.setText(String.valueOf(dm_op.search_todayDJnum(1,timeString,ckid)));
			today_chunumTxtView.setText(String.valueOf(dm_op.search_todayDJnum(2,timeString,ckid)));
		}
	}
	
	/**
	 * 开个线程来组建功能选项
	 * */
	class functionAsyncTask extends AsyncTask<Void, Void, List<Map<String,Object>>>{

		@Override
		protected List<Map<String, Object>> doInBackground(Void... params) {
			// TODO 自动生成的方法存根
			List<Map<String,Object>> mList = new ArrayList<Map<String,Object>>();
			String[] function_name = { "新增货品", "货品管理","新增入库", "新增出库", "新增盘点","新增调拨",
					"单据明细", "未上传单据", "历史单据"};
			int[] function_img = {R.drawable.new_goods_btn,R.drawable.goods_management_btn,R.drawable.new_rk_btn,R.drawable.new_ck_btn,
					R.drawable.new_pd_btn,R.drawable.new_db_btn,R.drawable.djmx_btn,R.drawable.bddj_btn,R.drawable.server_dj_btn};
			for(int i=0;i<9;i++){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("name", function_name[i]);
				map.put("image", function_img[i]);
				mList.add(map);
			}
			return mList;
		}
		
		@Override
		protected void onPostExecute(List<Map<String, Object>> result) {
			// TODO 自动生成的方法存根
			super.onPostExecute(result);
			mGridViewAdapter.setData(result);
		}
	}
	
	/**
	 * 获取单位名称用于在title显示
	 * */
	public void getDWname() {
		if(mSharedPreferences.getInt(ShareprefenceBean.TIYANZHANGHAO, 0)==1){
			titleTxtView.setText("测试");
		}else{
//			if (mSharedPreferences.getString(ShareprefenceBean.CUSTOM_DW, "")
//					.equals("")) {
				if (mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1) == 1) {
					if (!mSharedPreferences.getString(
							ShareprefenceBean.DWNAME_LOGIN, "").equals("")) {
						titleTxtView.setText(mSharedPreferences.getString(
								ShareprefenceBean.DWNAME_LOGIN, "").toString());
					} else {
						titleTxtView.setText("冠唐仓库管理系统");
					}
				} else if (mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG,
						1) == 0) {
					if (!mSharedPreferences.getString(
							ShareprefenceBean.IDN_ALONE_URL, "").equals("")) {
						titleTxtView.setText(mSharedPreferences.getString(
								ShareprefenceBean.IDN_ALONE_URL, "").toString());
					} else {
						titleTxtView.setText("冠唐仓库管理系统");
					}
				}
//			} else {
//				titleTxtView.setText(mSharedPreferences.getString(ShareprefenceBean.CUSTOM_DW, ""));
//			}
		}
	}
	
	private Runnable ck_run = new Runnable() {

		@Override
		public void run() {
			// TODO 自动生成的方法存根
			synchronized (this) {
				Message msg = new Message();
				List<Map<String, Object>> ck_list;
				ck_list = WebserviceImport.GetCK(str_ck, str15, mSharedPreferences);
				msg.obj = ck_list;
				msg.what = 2;
				handler.sendMessage(msg);
			}
		}
	};
	
	private Runnable hpinfo_run = new Runnable() {
		@Override
		public void run() {
			// TODO 自动生成的方法存根
			synchronized (this) {
			Message msg = new Message();
//			all_hp = WebserviceImport.Get_Total(
//						WebserviceHelper.GetHp_Total, ckid, mSharedPreferences);
			all_xx = WebserviceImport.Get_Total(
						WebserviceHelper.GetHpXX_Total, ckid,
						mSharedPreferences);
			all_sx = WebserviceImport.Get_Total(
						WebserviceHelper.GetHpSX_Total, ckid,
						mSharedPreferences);
//			today_change = WebserviceImport_more.GetHp_Today_Total(
//						WebserviceHelper.GetHp_Today_Total, ckid,
//						mSharedPreferences);
			msg.what = 1;
			handler.sendMessage(msg);
			}
		}
	};
	
	private Runnable movem_Amount = new Runnable() {

		@Override
		public void run() {
			// TODO 自动生成的方法存根
				synchronized (this) {
					Message msg = new Message();
					String jsonString = WebserviceImport_more.Gt_MovemByMyNum_2_0(formatter1.format(new Date(System.currentTimeMillis())), formatter1.format(new Date(System.currentTimeMillis())),ckid, mSharedPreferences);
					msg.obj = jsonString;
					msg.what = 3;
					handler.sendMessage(msg);
				}
		}
	};
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				if(all_xx>=0){
					xxnumTxtView.setText(String.valueOf(all_xx));
				}else{
					xxnumTxtView.setText("获取失败");
				}
				
				if(all_sx>=0){
					sxnumTxtView.setText(String.valueOf(all_sx));
				}else{
					sxnumTxtView.setText("获取失败");
				}
				
				break;
			case 2:
				ckNameAndID.clear();
				try {
					mSemaphore.acquire();
					cklist = (List<Map<String, Object>>) msg.obj;
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("ckmc", "所有仓库");
					map.put("ckid", "-1");
					ckNameAndID.add(map);
					if(cklist.size()>0){
						Iterator<Map<String, Object>> it=cklist.iterator();
						String[] values=new String[str_ck.length];
						while(it.hasNext()){
							Map<String, Object> map2 = new HashMap<String, Object>();
							Map<String, Object> map1 = it.next();
							map2.put("ckmc", map1.get(DataBaseHelper.CKMC).toString());
							map2.put("ckid", map1.get(DataBaseHelper.ID).toString());
							ckNameAndID.add(map2);
							
//							String id=(String) map1.get(DataBaseHelper.ID);
//							if(id!=null&& !id.equals("")){
//								dm_op.Del_CK(id);
//								for(int j=0;j<str_ck.length;j++){
//									values[j]=(String) map1.get(str_ck[j]);
//								}
//								dm_op.insert_into(DataBaseHelper.TB_CK, str_ck, values);
//							}
						}
					}
					mSemaphore.release();
				} catch (InterruptedException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}
				break;
			case 3:
				try {
					JSONObject jsonObject = new JSONObject(msg.obj.toString());
					switch(jsonObject.getInt("Status")){
					case 1:
						JSONObject DataObject = jsonObject.getJSONObject("Data");
						today_runumTxtView.setText(DataObject.getString("rkMovemNum"));
						today_chunumTxtView.setText(DataObject.getString("ckMovemNum"));
						break;
					case -1:
						Toast.makeText(context, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
						break;
					}
				} catch (JSONException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				break;
			}
		};
	};
	
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		Intent intent = new Intent();
		switch(v.getId()){
		case R.id.titletxtView:
			switchModeDialog(intent);
			break;
		case R.id.searchImgView:
			intent.setClass(context, SearchActivity.class);
			startActivityForResult(intent, 2);
			break;
		case R.id.scanImgView:
			intent.setClass(context, CaptureActivity.class);
			startActivityForResult(intent, 1);
			break;
		case R.id.cangkuTxtView:
			if(ckNameAndID.isEmpty()){
				Toast.makeText(context, "仓库信息还在获取，稍后再试！", Toast.LENGTH_SHORT).show();
			}else{
				if (mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1) == 1) {
					builder = new AlertDialog.Builder(context);
					LayoutInflater inflater = LayoutInflater.from(context);
					View view = inflater.inflate(R.layout.popupwindow_list, null);
					ListView myListView = (ListView) view.findViewById(R.id.popuplist);
					CkListViewAdapter ckListViewAdapter = new CkListViewAdapter(context);
					try {
						mSemaphore.acquire();
						ckListViewAdapter.setData(ckNameAndID);
						mSemaphore.release();
					} catch (InterruptedException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
					myListView.setAdapter(ckListViewAdapter);
					myListView.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
							// TODO 自动生成的方法存根
							if (WebserviceImport.isOpenNetwork(context)) {
								Map<String, Object> getMap = (Map<String, Object>) arg0.getAdapter().getItem(arg2);
								cangkuTxtView.setText(getMap.get("ckmc").toString());
								ckid = Integer.parseInt(getMap.get("ckid").toString());
								mSharedPreferences.edit()
										.putInt(ShareprefenceBean.SHOUYE_CKID, ckid)
										.commit();
								mSharedPreferences
										.edit()
										.putString(ShareprefenceBean.SHOUYE_CKMC,
												cangkuTxtView.getText().toString()).commit();
								cacheThreadPool.execute(hpinfo_run);
								cacheThreadPool.execute(movem_Amount);
								Dialog.dismiss();
							} else {
								Toast.makeText(context, "网络未连接",
										Toast.LENGTH_SHORT).show();
								Dialog.dismiss();
							}
						}
					});
					builder.setView(view);
					Dialog = builder.show();
				} else {
					builder = new AlertDialog.Builder(context);
					LayoutInflater inflater = LayoutInflater.from(context);
					View view = inflater.inflate(R.layout.popupwindow_list, null);
					ListView myListView = (ListView) view
							.findViewById(R.id.popuplist);
					CkListViewAdapter ckListViewAdapter = new CkListViewAdapter(context);
					ckListViewAdapter.setData(ckNameAndID);
					myListView.setAdapter(ckListViewAdapter);
					myListView.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							// TODO 自动生成的方法存根
							Map<String, Object> getMap = (Map<String, Object>) arg0.getAdapter().getItem(arg2);
							cangkuTxtView.setText(getMap.get("ckmc").toString());
							ckid = Integer.parseInt(getMap.get("ckid").toString());
							String timeString = formatter1.format(new Date(System.currentTimeMillis()));
							xxnumTxtView.setText(String.valueOf(dm
									.GethpAmount_XX(ckid)));
							sxnumTxtView.setText(String.valueOf(dm
									.GethpAmount_SX(ckid)));
							today_runumTxtView.setText(String.valueOf(dm_op.search_todayDJnum(1,timeString,ckid)));
							today_runumTxtView.setText(String.valueOf(dm_op.search_todayDJnum(2,timeString,ckid)));
							mSharedPreferences.edit()
									.putInt(ShareprefenceBean.SHOUYE_CKID, ckid)
									.commit();
							mSharedPreferences
									.edit()
									.putString(ShareprefenceBean.SHOUYE_CKMC,
											cangkuTxtView.getText().toString()).commit();
							Dialog.dismiss();
						}
					});
					builder.setView(view);
					Dialog = builder.show();
				}
			}
		break;
		case R.id.today_runum:
			if (mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1) == 1) {
				intent.setClass(context, MySubmitDJActivity.class);
				intent.putExtra("ckid", ckid);
				intent.putExtra(DataBaseHelper.mType, "1");
			} else if (mSharedPreferences
					.getInt(ShareprefenceBean.IS_LOGIN, -1) == -1) {
				intent.setClass(context, LocalDJActivity.class);
				intent.putExtra("todayDJ", true);
				intent.putExtra("ckid", ckid);
				intent.putExtra("todayop_type", 1);
			}
			startActivity(intent);
			break;
		case R.id.today_chunum:
			if (mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1) == 1) {
				intent.setClass(context, MySubmitDJActivity.class);
				intent.putExtra("ckid", ckid);
				intent.putExtra(DataBaseHelper.mType, "2");
			} else if (mSharedPreferences
					.getInt(ShareprefenceBean.IS_LOGIN, -1) == -1) {
				intent.setClass(context, LocalDJActivity.class);
				intent.putExtra("todayDJ", true);
				intent.putExtra("ckid", ckid);
				intent.putExtra("todayop_type", 2);
			}
			startActivity(intent);
			break;
		case R.id.xxnum:
			intent.setClass(context, HpManagerListActivity.class);
			intent.putExtra("itmeClick", true);
			intent.putExtra("ckmc", cangkuTxtView.getText().toString());
			intent.putExtra("ckid", ckid);
			intent.putExtra("hptag", "库存不足");
			startActivity(intent);
			break;
		case R.id.sxnum:
			intent.setClass(context, HpManagerListActivity.class);
			intent.putExtra("itmeClick", true);
			intent.putExtra("ckmc", cangkuTxtView.getText().toString());
			intent.putExtra("ckid", ckid);
			intent.putExtra("hptag", "库存过多");
			startActivity(intent);
			break;
		}
	}
	
	public void AreYouLoad(){
		AlertDialog.Builder mbuilder = new AlertDialog.Builder(context);
		ViewGroup vGroup = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.threechoicedialog, null);
		TextView showtext = (TextView) vGroup.findViewById(R.id.showtext);
		Button leftBtn = (Button) vGroup.findViewById(R.id.leftBtn);
		Button midBtn = (Button) vGroup.findViewById(R.id.midBtn);
		Button rightBtn = (Button) vGroup.findViewById(R.id.rightBtn);
		showtext.setText("\t\t为确保数据准确性，切换离线模式前，请先进行数据更新！");
		leftBtn.setText("不再提示");
		midBtn.setText("立即更新");
		rightBtn.setText("进入离线");
		leftBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				mSharedPreferences.edit().putBoolean(ShareprefenceBean.AREYOULOAD, false).commit();
				pro_dialog = ProgressDialog.show(context, "", "正在退出在线模式");
				cacheThreadPool.execute(ExitloadRun);
				threechoiceDialog.dismiss();
			}
		});
		midBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				EventBus.getDefault().post(new EventObject8());
				MyApplication.getInstance().setshowupDataFlag(false);
				threechoiceDialog.dismiss();
			}
		});
		rightBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				pro_dialog = ProgressDialog.show(context, "", "正在退出在线模式");
				cacheThreadPool.execute(ExitloadRun);
				threechoiceDialog.dismiss();
			}
		});
		mbuilder.setView(vGroup);
		threechoiceDialog = mbuilder.create();
		threechoiceDialog.show();
	}
	
	public void switchModeDialog(Intent intent) {
		AlertDialog.Builder mybuilder = new AlertDialog.Builder(context);
		mybuilder.setMessage("请切换模式");
		mybuilder.setNegativeButton("离线模式",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO 自动生成的方法存根
						if (mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1) == 1) {
							if(WebserviceImport.isOpenNetwork(context)){
								if(mSharedPreferences.getBoolean(ShareprefenceBean.AREYOULOAD, true)){
									if(MyApplication.getInstance().getshowupDataFlag()){
										AreYouLoad();
									}else{
										pro_dialog = ProgressDialog.show(context, "", "正在退出在线模式");
										cacheThreadPool.execute(ExitloadRun);
									}
								}else{
									pro_dialog = ProgressDialog.show(context, "", "正在退出在线模式");
									cacheThreadPool.execute(ExitloadRun);
								}
							}else{
								//如果是没有记住密码，在离线时就删除保存在线登陆密码
								if (!mSharedPreferences.getBoolean(ShareprefenceBean.JIZHUMIMA, true)) {
									mSharedPreferences.edit().putString(ShareprefenceBean.PASSWORD, "").commit();
								}
								mSharedPreferences.edit().putInt(ShareprefenceBean.IS_LOGIN, -1).commit();
								onStart();
							}
							
						}
						dialog.dismiss();
					}
				});
		mybuilder.setPositiveButton("在线模式",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO 自动生成的方法存根
						if(mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1) == -1){
							if(WebserviceImport.isOpenNetwork(context)){
								final String username = mSharedPreferences.getString(ShareprefenceBean.USERNAME, "");
								final String password = mSharedPreferences.getString(ShareprefenceBean.PASSWORD, "");
								final String dwname = mSharedPreferences.getString(ShareprefenceBean.DWNAME_LOGIN, "");
								WebserviceHelper.URL = mSharedPreferences.getString(ShareprefenceBean.NET_URL, "");
								WebserviceHelper.loginflag = mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1);
								if(!mSharedPreferences.getBoolean(ShareprefenceBean.JIZHUMIMA, true)){//没有记住密码
									mSharedPreferences.edit().putString(ShareprefenceBean.PASSWORD, "").commit();//跟登陆界面不一样
									AlertDialog.Builder builder5 = new AlertDialog.Builder(context);
									LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
									LinearLayout layout = new LinearLayout(context);
									layout.setOrientation(LinearLayout.VERTICAL);
									layout.setGravity(Gravity.CENTER);
									TextView textview = new TextView(context);
									final EditText myEditText = new EditText(context);
									lp.setMargins(10, 10, 10, 10);
									lp.gravity = Gravity.CENTER;
									textview.setLayoutParams(lp);
									textview.setTextSize(18);
									textview.setPadding(10, 10, 10, 10);
									textview.setText("公司名称："+mSharedPreferences.getString(ShareprefenceBean.DWNAME_LOGIN,"")+"\n"
											+"用户名："+mSharedPreferences.getString(ShareprefenceBean.USERNAME,""));
									myEditText.setLayoutParams(lp);
									myEditText.setBackgroundResource(R.drawable.dare_edittext);
									myEditText.setPadding(10, 10, 10, 10);
									myEditText.setInputType(InputType.TYPE_CLASS_TEXT| InputType.TYPE_TEXT_VARIATION_PASSWORD);
									layout.addView(textview);
									layout.addView(myEditText);
									builder5.setTitle("请输入在线登录密码");
									builder5.setView(layout);
									builder5.setNegativeButton("确认",
											new DialogInterface.OnClickListener() {
												@Override
												public void onClick(DialogInterface dialog, int which) {
													// TODO 自动生成的方法存根
													if(WebserviceImport.isOpenNetwork(context)){
														pro_dialog = ProgressDialog.show(context, "", "正在登录");
														Map<String,Object> map = new HashMap<String, Object>();
														map.put("dwname", dwname);
														map.put("UserName", username);
														map.put("password", pwdhelper.createMD5(myEditText.getText().toString().trim() + "#cd@guantang").toUpperCase());
														map.put("IMEI", MyApplication.getInstance().getIEMI());
														map.put("Versions", MyApplication.getInstance().getVisionCode());
														map.put("PhoneSystem","Android");
														JSONObject jsonObject = new JSONObject(map);
														new LoginAsyncTask().execute(jsonObject.toString(),myEditText.getText().toString().trim());
													}else{
														Toast.makeText(context, "网络未连接", Toast.LENGTH_SHORT).show();
													}
													dialog.dismiss();
												}
											});
									builder5.create().show();
								}else{
									pro_dialog = ProgressDialog.show(context, "", "正在登录");
									Map<String,Object> map = new HashMap<String, Object>();
									map.put("dwname", dwname);
									map.put("UserName", username);
									map.put("password", pwdhelper.createMD5(password + "#cd@guantang").toUpperCase());
									map.put("IMEI", MyApplication.getInstance().getIEMI());
									map.put("Versions", MyApplication.getInstance().getVisionCode());
									map.put("PhoneSystem","Android");
									JSONObject jsonObject = new JSONObject(map);
									new LoginAsyncTask().execute(jsonObject.toString(),password);
								}
							}else{
								Toast.makeText(context, "网络未连接", Toast.LENGTH_SHORT).show();
							}
						}
						dialog.dismiss();
					}
				});
		mybuilder.create().show();

	}
	
	class LoginAsyncTask extends AsyncTask<String, Void, String>{
		String newPassWord = "";
		@Override
		protected String doInBackground(String... params) {
			// TODO 自动生成的方法存根
			String jsonString = WebserviceImport_more.Login_Validate_1_0(params[0]);
			newPassWord = params[1];
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
					mSharedPreferences.edit().putInt(ShareprefenceBean.IS_LOGIN, 1).commit();
					mSharedPreferences.edit().putString(ShareprefenceBean.PASSWORD, newPassWord).commit();
					mSharedPreferences.edit().putString(ShareprefenceBean.MIWENPASSWORD, pwdhelper.createMD5(newPassWord + "#cd@guantang").toUpperCase()).commit();
					onStart();
					break;
				case -1:
					Toast.makeText(context, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -2:
					Toast.makeText(context, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -3:
					Toast.makeText(context, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -4:
					Toast.makeText(context, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -5:
					Toast.makeText(context, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -6:
					Toast.makeText(context, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -7:
					Toast.makeText(context, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -8:
					Toast.makeText(context, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -9:
					Toast.makeText(context, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -10:
					Toast.makeText(context, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -11:
					AlertDialog.Builder builder = new AlertDialog.Builder(
							context);
					builder.setMessage(jsonObject.getString("Message"));
					builder.setCancelable(false);
					builder.setNegativeButton("退出软件",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO 自动生成的方法存根
									// 关闭app进程
									((Activity) context).finish();
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
									UmengUpdateAgent.setUpdateAutoPopup(false);
									UmengUpdateAgent
											.forceUpdate(context);
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
																		context,
																		updateInfo);
														break;
													case 1: // has no update
														Toast.makeText(
																context,
																"没有更新",
																Toast.LENGTH_SHORT)
																.show();
														break;
													case 2: // none wifi
														Toast.makeText(
																context,
																"没有wifi连接， 只在wifi下更新",
																Toast.LENGTH_SHORT)
																.show();
														break;
													case 3: // time out
														Toast.makeText(
																context,
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
					Toast.makeText(context, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -13:
					Toast.makeText(context, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -14:
					Toast.makeText(context, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -15:
					Toast.makeText(context, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
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
	


	public Runnable ExitloadRun = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			synchronized (this) {
				Message msg = Message.obtain();
				msg.what = WebserviceImport.delMEI(MyApplication.getInstance().getIEMI(), mSharedPreferences);
				msg.setTarget(mHandler3);
				mHandler3.sendMessage(msg);
			}
		}
	};
	Handler mHandler3 = new Handler() {
		public void handleMessage(Message msg) {
			pro_dialog.dismiss();
			//如果是没有记住密码，在离线时就删除保存在线登陆密码
			if (!mSharedPreferences.getBoolean(ShareprefenceBean.JIZHUMIMA, true)) {
				mSharedPreferences.edit().putString(ShareprefenceBean.PASSWORD, "").commit();
			}
			if (msg.what < 0) {
				Toast.makeText(context, "服务器端退出异常，请重新登录退出",Toast.LENGTH_SHORT).show();
			} else {
				mSharedPreferences.edit().putInt(ShareprefenceBean.IS_LOGIN, -1).commit();
				onStart();
			}
		}
	};

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO 自动生成的方法存根
		Map<String, Object> map = (Map<String, Object>) arg0.getAdapter()
				.getItem(arg2);
		String name = map.get("name").toString();
		switchFunctionItem(name);
	}
	
	/**
	 * 根据选择功能项来启动相应activity
	 * */
	public void switchFunctionItem(String name) {
		Intent intent = new Intent();
		if (name.equals("新增货品")) {
			if (mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1) == 1) {
				if (RightsHelper.isHaveRight(RightsHelper.hp_addedit,
						mSharedPreferences) == true) {
					intent.setClass(context, AddActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(context, "对不起，你没有添加货品的权限",
							Toast.LENGTH_SHORT).show();
				}
			} else if (mSharedPreferences.getInt(
					ShareprefenceBean.IS_LOGIN, -1) == -1) {
				Toast.makeText(context, "离线模式下不能进行新增货品操作",
						Toast.LENGTH_SHORT).show();
			}
		} else if (name.equals("新增入库")) {
			if (RightsHelper.isHaveRight(RightsHelper.dj_rk_add,
					mSharedPreferences) == true) {
				intent.setClass(context, AddDJActivity.class);
				intent.putExtra("op_type", 1);// 入库单据
				startActivity(intent);
			} else {
				Toast.makeText(context, "对不起，你没有的新增入库的权限",
						Toast.LENGTH_SHORT).show();
			}
		} else if (name.equals("新增出库")) {
			if (RightsHelper.isHaveRight(RightsHelper.dj_ck_add,
					mSharedPreferences) == true) {
				intent.setClass(context, AddDJActivity.class);
				intent.putExtra("op_type", 2);// 出库单局
				startActivity(intent);
			} else {
				Toast.makeText(context, "对不起，你没有的新增出库的权限",
						Toast.LENGTH_SHORT).show();
			}
		} else if (name.equals("新增盘点")) {
			if (RightsHelper.isHaveRight(RightsHelper.dj_pd_add,
					mSharedPreferences) == true) {
				intent.setClass(context, PanDianActivity.class);
				startActivity(intent);
			} else {
				Toast.makeText(context, "对不起，你没有的新增盘点的权限",
						Toast.LENGTH_SHORT).show();
			}
		}else if(name.equals("新增调拨")){
			if (mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1) == 1) {
				if (WebserviceImport.isOpenNetwork(context)) {
					intent.setClass(context, AddDiaoboActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(context, "网络未连接",
							Toast.LENGTH_SHORT).show();
				}
			} else if (mSharedPreferences.getInt(
					ShareprefenceBean.IS_LOGIN, -1) == -1) {
				Toast.makeText(context, "离线模式下不能新增调拨单",
						Toast.LENGTH_SHORT).show();
			}
		} else if (name.equals("货品管理")) {
			intent.setClass(context, HpManagerListActivity.class);
			startActivity(intent);
		} else if (name.equals("未上传单据")) {
			intent.setClass(context, LocalDJActivity.class);
			startActivity(intent);
		} else if (name.equals("单据明细")) {
			if (mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1) == 1) {
				intent.setClass(context, Moved_DJ.class);
				intent.putExtra("todayDJ", false);
				startActivity(intent);
			} else if (mSharedPreferences.getInt(
					ShareprefenceBean.IS_LOGIN, -1) == -1) {
				Toast.makeText(context, "离线模式下不能查看服务端历史明细",
						Toast.LENGTH_SHORT).show();
			}
		} else if (name.equals("历史单据")) {
			if (mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1) == 1) {
				if (WebserviceImport.isOpenNetwork(context)) {
					intent.setClass(context, NetDJActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(context, "网络未连接",
							Toast.LENGTH_SHORT).show();
				}
			} else if (mSharedPreferences.getInt(
					ShareprefenceBean.IS_LOGIN, -1) == -1) {
				Toast.makeText(context, "离线模式下不能查看服务端单据",
						Toast.LENGTH_SHORT).show();
			}

		}
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO 自动生成的方法存根
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==1){
			if(resultCode==1){
				String scan_tm = data.getStringExtra("scan_tm");
//				if(scan_tm.matches("^(http|https|ftp)://(.)*$")){
//					Uri uri = Uri.parse(scan_tm);
//					Intent intent = new Intent();
//					intent.setAction(Intent.ACTION_VIEW);
//					intent.setData(uri);
//					startActivity(intent);
//				}else{
					Intent intent = new Intent(context,HpInfo_ListActivity.class);
					intent.putExtra("scanOrsearch", 2);//1代表搜索，2代表扫描。
					intent.putExtra("searchString", scan_tm);
					startActivity(intent);
//				}
			}
			
		}else if(requestCode==2){
			if(resultCode==1){
				String searchString = data.getStringExtra("searchString");
				Intent intent = new Intent(context,HpInfo_ListActivity.class);
				intent.putExtra("scanOrsearch", 1);//1代表搜索，2代表扫描。
				intent.putExtra("searchString", searchString);
				startActivity(intent);
				
			}
		}
	}
}
