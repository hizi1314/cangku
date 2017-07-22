package com.guantang.cangkuonline.activity;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.XListView.XListView;
import com.guantang.cangkuonline.XListView.XListView.IXListViewListener;
import com.guantang.cangkuonline.adapter.DemoAdapter;
import com.guantang.cangkuonline.adapter.HpListBaseAdapter;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.database.DataBaseMethod;
import com.guantang.cangkuonline.database.DataBaseOperateMethod;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;
import com.guantang.cangkuonline.webservice.WebserviceImport;
import com.guantang.cangkuonline.webservice.WebserviceImport_more;

public class HpInfo_ListActivity extends Activity implements OnClickListener,
		TextWatcher, OnItemClickListener, IXListViewListener {
	private static int HP_REQUESTCODE = 0;
	private ImageButton backImgBtn, scanImgBtn;
	private ImageView searchDelBtn;
	private ImageView up_downImgView;
	private TextView titleTextView, titleTextView1, filterImgBtn;
	private EditText mEditText;
	private ExpandableListView title_listView1;
	private XListView mXListView;

	private RelativeLayout title_layout, title_changeLayout;
	private HpListBaseAdapter mySimpleAdapter;
	private SimpleDateFormat formatter;
	private String scan_tm = null;
	private DataBaseMethod dm = new DataBaseMethod(this);
	private DataBaseOperateMethod dm_op = new DataBaseOperateMethod(this);
	private List<Map<String, Object>> ls2, ls3, list;
	private String[] str = { DataBaseHelper.HPMC, DataBaseHelper.HPBM,
			DataBaseHelper.GGXH, DataBaseHelper.CurrKC, DataBaseHelper.KCSL };
	private String[] str2 = { DataBaseHelper.ID, DataBaseHelper.HPMC,
			DataBaseHelper.HPBM, DataBaseHelper.HPTM, DataBaseHelper.GGXH,
			DataBaseHelper.CurrKC };
	private String[] str15 = { "ID", "CKMC", "FZR", "TEL", "ADDR", "inact",
			"outact", "BZ" };
	private String[] str_ck = { DataBaseHelper.ID, DataBaseHelper.CKMC,
			DataBaseHelper.FZR, DataBaseHelper.TEL, DataBaseHelper.ADDR,
			DataBaseHelper.INACT, DataBaseHelper.OUTACT, DataBaseHelper.BZ };
	private List<Map<String, Object>> ckmc_array = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> cklist = new ArrayList<Map<String, Object>>();
	private List<List<Map<String, Object>>> childList = new ArrayList<List<Map<String, Object>>>();
	private Boolean net_mode = true;// true 联网模式下进行的数据操作，false 本地数据操作；
	private Thread mThread;
	private int position;
	private int scrolledX, scrolledY;
	private PopupWindow mPopupWindow;
	private ExecutorService cacheThreadpool = Executors.newCachedThreadPool();
	private SharedPreferences mSharedPreferences;
	private Boolean flagstart = true;// 第一次加载
	private Boolean flagend = false;// 标记是否加载玩，true为加载玩
	private int ScreenWidth, ScreenHeigth, ckid = -1;
	private int loadtag = -2;// -3表示搜索状态下加载，-2表示title选择下的加载，1表示高级搜索下的加载,-4表示条码状态下搜索
	private String hptag_str, ckmc_str;
	private String sqlString = "";
	private DemoAdapter mDemoAdapter;
	
	private int hasckkc =0; //是否有库存 有库存1，没有库存-1，0有无库存都显示
	private volatile Semaphore mSemaphore = new Semaphore(1);

	private Handler refreshHandler = new Handler() {
		public void handleMessage(Message msg) {
			mXListView.setOnScrollListener(new OnScrollListener() {

				@Override
				public void onScrollStateChanged(AbsListView view,
						int scrollState) {
					// TODO 自动生成的方法存根
					if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
						position = mXListView.getFirstVisiblePosition();

					}
				}

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					// TODO 自动生成的方法存根

				}
			});
			switch (msg.what) {
			case 0:
				String[] stt = { "货品信息", "今日变化", "库存不足", "库存过多" };
				for (int i = 0; i < stt.length; i++) {
					HashMap<String, Object> map1 = new HashMap<String, Object>();
					map1.put("ckmc", stt[i]);
					ckmc_array.add(map1);
				}

				List<Map<String, Object>> littleList = new ArrayList<Map<String, Object>>();
				for (int i = 0; i <= cklist.size(); i++) {
					HashMap<String, Object> map2 = new HashMap<String, Object>();
					if (i == 0) {
						map2.put("ckmc", "所有仓库");
						map2.put("ckid", "-1");
					} else {
						map2.put("ckmc",
								cklist.get(i - 1).get(DataBaseHelper.CKMC)
										.toString());
						map2.put("ckid",
								cklist.get(i - 1).get(DataBaseHelper.ID)
										.toString());
					}
					littleList.add(map2);
				}
				for (int i = 0; i <= ckmc_array.size(); i++) {
					childList.add(littleList);
				}
				break;
			case 1:// 获取网络货品信息
				setAdapter2();
				onLoad();
				break;
			case 2:
				try {
					JSONObject jsonObject = new JSONObject(msg.obj.toString());
					switch(jsonObject.getInt("Status")){
					case 1:
						JSONArray jsonArray = new JSONArray(jsonObject.getString("Data"));
						int length=jsonArray.length();
						List<Map<String, Object>> littleList2 = new ArrayList<Map<String,Object>>();
						for(int i=0;i<length;i++){
							JSONObject dataJsonObject = jsonArray.getJSONObject(i);
							Map<String, Object> map = new HashMap<String, Object>();
							map.put(DataBaseHelper.ID, dataJsonObject.getString("id"));
							map.put(DataBaseHelper.HPMC, dataJsonObject.getString("HPMC"));
							map.put(DataBaseHelper.HPBM, dataJsonObject.getString("HPBM"));
							map.put(DataBaseHelper.HPTM, dataJsonObject.getString("HPTM"));
							map.put(DataBaseHelper.GGXH, dataJsonObject.getString("GGXH"));
							map.put(DataBaseHelper.CurrKC, dataJsonObject.getString("CurrKc"));
							littleList2.add(map);
						}
						ls2 = littleList2;
						if (ls2.size() == 0){
							flagend = true;
						} else {
							flagend = false;
						}
						setAdapter2();
						mSemaphore.release();
						onLoad();
						break;
					case -1:
						Toast.makeText(HpInfo_ListActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
						break;
					case -2:
						Toast.makeText(HpInfo_ListActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
						break;
					}
				} catch (JSONException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				
				break;
			case 3:// 接受扫描货品
				if (!ls2.isEmpty()) {
					mySimpleAdapter.setListData(ls2,ckid);
					mXListView.setAdapter(mySimpleAdapter);
				} else {
					Toast.makeText(HpInfo_ListActivity.this, "扫描的条码没有对应的货品不存在",
							Toast.LENGTH_SHORT).show();
				}
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.hpinfo_list_activity);
		Intent intent = getIntent();
		scan_tm = intent.getStringExtra("scan_tm");
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		ScreenWidth = dm.widthPixels;
		ScreenHeigth = dm.heightPixels;
		mSharedPreferences = getSharedPreferences(
				ShareprefenceBean.SHAREPREFENCE_NAME, Context.MODE_PRIVATE);
		intiControl();
		init();
	}

	public void intiControl() {
		title_changeLayout = (RelativeLayout) this
				.findViewById(R.id.title_change);
		title_layout = (RelativeLayout) this.findViewById(R.id.titlelayout);
		up_downImgView = (ImageView) this.findViewById(R.id.up_down);
		titleTextView = (TextView) this.findViewById(R.id.title);
		titleTextView1 = (TextView) this.findViewById(R.id.title1);
		backImgBtn = (ImageButton) this.findViewById(R.id.back);
		filterImgBtn = (TextView) this.findViewById(R.id.filter1);
		scanImgBtn = (ImageButton) this.findViewById(R.id.scan);
		searchDelBtn = (ImageView) this.findViewById(R.id.del_cha);
		mEditText = (EditText) this.findViewById(R.id.listtext);
		mXListView = (XListView) this.findViewById(R.id.hplist_load);
		filterImgBtn.setOnClickListener(this);
		title_changeLayout.setOnClickListener(this);
		backImgBtn.setOnClickListener(this);
		scanImgBtn.setOnClickListener(this);
		searchDelBtn.setOnClickListener(this);
		mXListView.setXListViewListener(this);
		mXListView.setOnItemClickListener(this);
		mEditText.addTextChangedListener(this);
		mEditText.setOnClickListener(this);
		
//		mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//			
//			@Override
//			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//				// TODO 自动生成的方法存根
//				if(actionId==EditorInfo.IME_ACTION_SEARCH || (event!=null&&event.getKeyCode()==KeyEvent.KEYCODE_ENTER)){
//					if (mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1) == 1) {
//						if (WebserviceImport.isOpenNetwork(HpInfo_ListActivity.this)) {
//							titleTextView.setText("货品信息");
//							titleTextView1.setVisibility(View.GONE);
//							flagstart = true;
//							loadtag = -3;
//							cacheThreadpool.execute(searchRunnable);
//						} else {
//							Toast.makeText(HpInfo_ListActivity.this, "网络未连接", Toast.LENGTH_SHORT).show();
//						}
//					} else if (mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1) == -1) {
//						titleTextView.setText("货品信息");
//						ls2 = dm.queryList(str2, mEditText.getText().toString());
//						if(!ls2.isEmpty()){
//							mySimpleAdapter.setListData(ls2);
//						}else{
//							Toast.makeText(HpInfo_ListActivity.this, "搜索货品不存在", Toast.LENGTH_LONG).show();
//						}
//					}
//					return true;
//				}
//				return false;
//			}
//		});
		
		
	}
	
	public void init() {
		formatter = new SimpleDateFormat("yyyy-MM-dd");
		ls2 = new ArrayList<Map<String, Object>>();// 装网络数据
		ls3 = new ArrayList<Map<String, Object>>();// 装网络搜索数据
		list = new ArrayList<Map<String, Object>>();
		ckmc_array = new ArrayList<Map<String, Object>>();
		mySimpleAdapter = new HpListBaseAdapter(this);
		mXListView.setAdapter(mySimpleAdapter);

		Intent intent = getIntent();
		if (intent.getBooleanExtra("itmeClick", false)) {// 首页点击数字启动本activity
			ckmc_str = intent.getStringExtra("ckmc");
			hptag_str = intent.getStringExtra("hptag");
			ckid = intent.getIntExtra("ckid", -1);
			if (mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1) == 1) {
				mXListView.setPullLoadEnable(true);// 设置可以加载更多
				mXListView.setPullRefreshEnable(true);// 设置可以刷新
				if (WebserviceImport.isOpenNetwork(this)) {
					cacheThreadpool.execute(LoadCKmessage);
				} else {
					Toast.makeText(this, "网络未连接", Toast.LENGTH_LONG).show();
				}
			} else if (mSharedPreferences
					.getInt(ShareprefenceBean.IS_LOGIN, -1) == -1) {
				mXListView.setPullLoadEnable(false);// 设置不可以加载更多
				mXListView.setPullRefreshEnable(false);// 设置不可以刷新
				String[] stt = { "货品信息", "今日变化", "库存不足", "库存过多" };
				for (int i = 0; i < stt.length; i++) {
					HashMap<String, Object> map1 = new HashMap<String, Object>();
					map1.put("ckmc", stt[i]);
					ckmc_array.add(map1);
				}
				List<Map<String, Object>> littleList = new ArrayList<Map<String, Object>>();
				for (int i = 0; i <= cklist.size(); i++) {
					HashMap<String, Object> map2 = new HashMap<String, Object>();
					if (i == 0) {
						map2.put("ckmc", "所有仓库");
						map2.put("ckid", "-1");
					} else {
						map2.put("ckmc",
								cklist.get(i - 1).get(DataBaseHelper.CKMC)
										.toString());
						map2.put("ckid",
								cklist.get(i - 1).get(DataBaseHelper.ID)
										.toString());
					}
					littleList.add(map2);
				}
				for (int i = 0; i <= ckmc_array.size(); i++) {
					childList.add(littleList);
				}
			}
			titleHpInfoLoad();
		} else {// 直接启动
			if (mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1) == 1) {
				mXListView.setPullLoadEnable(true);// 设置可以加载更多
				mXListView.setPullRefreshEnable(true);// 设置可以刷新
				if (WebserviceImport.isOpenNetwork(this)) {// 有网络
					hptag_str = "货品信息";
					cacheThreadpool.execute(LoadCKmessage);
					if(intent.getIntExtra("scanOrsearch", -1)==1){//判断是否从搜索页面跳转过来的，1是从搜索页面的输入框条转过来的，2是从搜索页面的扫描条转过来的，-1是直接打开这个界面
						loadtag = -3;
						mEditText.setText(intent.getStringExtra("searchString"));
						cacheThreadpool.execute(searchRunnable);
					}else if(intent.getIntExtra("scanOrsearch", -1)==2){
						loadtag = -4;
						mEditText.setText(intent.getStringExtra("searchString"));
						cacheThreadpool.execute(search_tmRunnable);
					}else{
						loadtag = -2;
						mEditText.setText("");
						cacheThreadpool.execute(loadmessage);
					}
				} else {// 没网络
					Toast.makeText(this, "网络未连接", Toast.LENGTH_LONG).show();
				}
			} else if (mSharedPreferences
					.getInt(ShareprefenceBean.IS_LOGIN, -1) == -1) {
				mXListView.setPullLoadEnable(false);// 设置不可以加载更多
				mXListView.setPullRefreshEnable(false);// 设置不可以刷新
				cklist = dm.getAllCK();
				String[] stt = { "货品信息", "今日变化", "库存不足", "库存过多" };
				for (int i = 0; i < stt.length; i++) {
					HashMap<String, Object> map1 = new HashMap<String, Object>();
					map1.put("ckmc", stt[i]);
					ckmc_array.add(map1);
				}
				List<Map<String, Object>> littleList = new ArrayList<Map<String, Object>>();
				for (int i = 0; i <= cklist.size(); i++) {
					HashMap<String, Object> map2 = new HashMap<String, Object>();
					if (i == 0) {
						map2.put("ckmc", "所有仓库");
						map2.put("ckid", "-1");
					} else {
						map2.put("ckmc",
								cklist.get(i - 1).get(DataBaseHelper.CKMC)
										.toString());
						map2.put("ckid",
								cklist.get(i - 1).get(DataBaseHelper.ID)
										.toString());
					}
					littleList.add(map2);
				}
				for (int i = 0; i <= ckmc_array.size(); i++) {
					childList.add(littleList);
				}
				if(intent.getIntExtra("scanOrsearch", -1)==1){//判断是否从搜索页面跳转过来的，1是从搜索页面的输入框条转过来的，2是从搜索页面的扫描条转过来的，-1是直接打开这个界面
					mEditText.setText(intent.getStringExtra("searchString"));
					ls2 = dm.queryList(str2, mEditText.getText().toString(),ckid);
					if(ls2.isEmpty()){
						Toast.makeText(this, "搜索货品不存在", Toast.LENGTH_LONG).show();
					}
					cacheThreadpool.execute(searchRunnable);
				}else if(intent.getIntExtra("scanOrsearch", -1)==2){
					mEditText.setText(intent.getStringExtra("searchString"));
					ls2 = dm.Gethp_tm(str2, mEditText.getText().toString());
					if (ls2.isEmpty()) {
						Toast.makeText(this, "不存在扫描的货品信息", Toast.LENGTH_LONG)
								.show();
					}
				}else{
					mEditText.setText("");
					ls2 = dm.Gethp(str2);
				}
				mySimpleAdapter.setListData(ls2,ckid);
			}
		}

		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.popupwindow_titlelist, null);
		// popupwindowAdapter pup_adapter = new popupwindowAdapter(this,
		// ckmc_array);
		mDemoAdapter = new DemoAdapter(this);
		title_listView1 = (ExpandableListView) view
				.findViewById(R.id.popuplist1);
		title_listView1.setAdapter(mDemoAdapter);

		mPopupWindow = new PopupWindow(view, ScreenWidth * 3 / 5,
				LayoutParams.WRAP_CONTENT);
		// 这个是为了点击“返回Back”也能使其消失.
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		// 使其聚焦
		mPopupWindow.setFocusable(true);
		// 设置允许在外点击消失
		mPopupWindow.setOutsideTouchable(true);
		// 刷新状态
		mPopupWindow.update();
	}

	Runnable LoadCKmessage = new Runnable() {

		@Override
		public void run() {
			// TODO 自动生成的方法存根
			Message msg = Message.obtain();
			cklist = WebserviceImport.GetCK(str_ck, str15, mSharedPreferences);
			msg.what = 0;// 1刷新数据
			refreshHandler.sendMessage(msg);
		}
	};
	Runnable loadmessage = new Runnable() {
		@Override
		public void run() {
			// TODO 自动生成的方法存根
			Message msg = Message.obtain();
			synchronized (this) {
				if (flagstart) {
					ls2 = WebserviceImport.GetHpInfo_top(15, "0", 1, ckid,
							str2, new String[] { "id", "HPMC", "HPBM", "hptm",
									"GGXH", "CurrKc" }, mSharedPreferences);
				} else {
					if (ls2.isEmpty()) {
						ls2 = WebserviceImport.GetHpInfo_top(15, "0", 1, ckid,
								str2, new String[] { "id", "HPMC", "HPBM",
										"hptm", "GGXH", "CurrKc" },
								mSharedPreferences);
					} else {
						ls2 = WebserviceImport.GetHpInfo_top(15, (String) ls2
								.get(ls2.size() - 1).get(DataBaseHelper.ID), 1,
								ckid, str2, new String[] { "id", "HPMC",
										"HPBM", "hptm", "GGXH", "CurrKc" },
								mSharedPreferences);
					}
					if (ls2.size() == 0) {
						flagend = true;
					} else {
						flagend = false;
					}

				}
			}
			msg.what = 1;// 1刷新数据
			refreshHandler.sendMessage(msg);
		}
	};
	Runnable todayChangeRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO 自动生成的方法存根
			Message msg = Message.obtain();
			synchronized (this) {
				if (flagstart) {
					ls2 = WebserviceImport_more.GetHpInfoToday_top(15, "0", 1,
							ckid, str2, new String[] { "id", "HPMC", "HPBM",
									"hptm", "GGXH", "CurrKc" },
							mSharedPreferences);
				} else {
					if (ls2.isEmpty()) {
						ls2 = WebserviceImport_more.GetHpInfoToday_top(15, "0",
								1, ckid, str2, new String[] { "id", "HPMC",
										"HPBM", "hptm", "GGXH", "CurrKc" },
								mSharedPreferences);
					} else {
						ls2 = WebserviceImport_more.GetHpInfoToday_top(
								15,
								(String) ls2.get(ls2.size() - 1).get(
										DataBaseHelper.ID), 1, ckid, str2,
								new String[] { "id", "HPMC", "HPBM", "hptm",
										"GGXH", "CurrKc" }, mSharedPreferences);
					}
					if (ls2.size() == 0) {
						flagend = true;
					} else {
						flagend = false;
					}
				}
			}
			msg.what = 1;// 1刷新数据
			refreshHandler.sendMessage(msg);
		}
	};
	Runnable xxnumRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO 自动生成的方法存根
			Message msg = Message.obtain();
			synchronized (this) {
				if (flagstart) {
					ls2 = WebserviceImport.GetHpInfoXX_top(15, "0", 1, ckid,
							str2, new String[] { "id", "HPMC", "HPBM", "hptm",
									"GGXH", "CurrKc" }, mSharedPreferences);
				} else {
					if (ls2.isEmpty()) {
						ls2 = WebserviceImport.GetHpInfoXX_top(15, "0", 1,
								ckid, str2, new String[] { "id", "HPMC",
										"HPBM", "hptm", "GGXH", "CurrKc" },
								mSharedPreferences);
					} else {
						ls2 = WebserviceImport.GetHpInfoXX_top(15, (String) ls2
								.get(ls2.size() - 1).get(DataBaseHelper.ID), 1,
								ckid, str2, new String[] { "id", "HPMC",
										"HPBM", "hptm", "GGXH", "CurrKc" },
								mSharedPreferences);
					}
					if (ls2.size() == 0) {
						flagend = true;
					} else {
						flagend = false;
					}
				}
			}
			msg.what = 1;// 1刷新数据
			refreshHandler.sendMessage(msg);
		}

	};
	Runnable sxnumRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO 自动生成的方法存根
			Message msg = Message.obtain();
			synchronized (this) {
				if (flagstart) {
					ls2 = WebserviceImport.GetHpInfoSX_top(15, "0", 1, ckid,
							str2, new String[] { "id", "HPMC", "HPBM", "hptm",
									"GGXH", "CurrKc" }, mSharedPreferences);
				} else {
					if (ls2.isEmpty()) {
						ls2 = WebserviceImport.GetHpInfoSX_top(15, "0", 1,
								ckid, str2, new String[] { "id", "HPMC",
										"HPBM", "hptm", "GGXH", "CurrKc" },
								mSharedPreferences);
					} else {
						ls2 = WebserviceImport.GetHpInfoSX_top(15, (String) ls2
								.get(ls2.size() - 1).get(DataBaseHelper.ID), 1,
								ckid, str2, new String[] { "id", "HPMC",
										"HPBM", "hptm", "GGXH", "CurrKc" },
								mSharedPreferences);
					}
					if (ls2.size() == 0) {
						flagend = true;
					} else {
						flagend = false;
					}
				}
			}
			msg.what = 1;// 1刷新数据
			refreshHandler.sendMessage(msg);
		}

	};
	Runnable searchRunnable = new Runnable() {
		@Override
		public void run() {
			Message msg = Message.obtain();
			synchronized (this) {
				if (flagstart) {
					ls2 = WebserviceImport.GetHpInfo_top_search(15, "0", 1, -1,
							mEditText.getText().toString(), str2,
							new String[] { "id", "HPMC", "HPBM", "hptm", "GGXH",
									"CurrKc" }, mSharedPreferences);
				} else {
					if (ls2.isEmpty()) {
						ls2 = WebserviceImport.GetHpInfo_top_search(15, "0", 1, -1,
								mEditText.getText().toString(), str2, new String[] {
										"id", "HPMC", "HPBM", "hptm", "GGXH",
										"CurrKc" }, mSharedPreferences);
					} else {
						ls2 = WebserviceImport.GetHpInfo_top_search(15,
								ls2.get(ls2.size() - 1).get(DataBaseHelper.ID)
										.toString(), 1, -1, mEditText.getText()
										.toString(), str2, new String[] { "id",
										"HPMC", "HPBM", "hptm", "GGXH", "CurrKc" },
								mSharedPreferences);
					}
					if (ls2.size() == 0) {
						flagend = true;
					} else {
						flagend = false;
					}
				}
			}
			msg.what = 1;
			refreshHandler.sendMessage(msg);
		}
	};
	Runnable search_tmRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO 自动生成的方法存根
			Message msg = Message.obtain();
			String id = WebserviceImport.GetHP_ByTM(mEditText.getText()
					.toString(), false, mSharedPreferences);
			ls2 = WebserviceImport.GetHpInfo_byid(id, -1, str2, new String[] {
					"id", "HPMC", "HPBM", "hptm", "GGXH", "CurrKc" },
					mSharedPreferences);
			msg.what = 3;
			refreshHandler.sendMessage(msg);
		}

	};
	Runnable searchComplexRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO 自动生成的方法存根
			Message msg = Message.obtain();
			try {
				mSemaphore.acquire();
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			String jsonString = "";
			synchronized (this) {
				if (flagstart) {
//					ls2 = WebserviceImport_more.GetHpInfo_top_search_complex(15,
//							"0", 1, ckid, str2, new String[] { "id", "HPMC",
//									"HPBM", "hptm", "GGXH", "CurrKc" }, sqlString,
//							mSharedPreferences);
					jsonString = WebserviceImport_more.GetHpInfo_top_search_complex_1_0(15, "0", 1, ckid, sqlString, hasckkc, mSharedPreferences);
				} else {
					if (ls2.isEmpty()) {
//						ls2 = WebserviceImport_more.GetHpInfo_top_search_complex(
//								15, "0", 1, ckid, str2, new String[] { "id",
//										"HPMC", "HPBM", "hptm", "GGXH", "CurrKc" },
//								sqlString, mSharedPreferences);
						jsonString = WebserviceImport_more.GetHpInfo_top_search_complex_1_0(15, "0", 1, ckid, sqlString, hasckkc, mSharedPreferences);
					} else {
//						ls2 = WebserviceImport_more.GetHpInfo_top_search_complex(
//								15,
//								(String) ls2.get(ls2.size() - 1).get(
//										DataBaseHelper.ID), 1, ckid, str2,
//								new String[] { "id", "HPMC", "HPBM", "hptm",
//										"GGXH", "CurrKc" }, sqlString,
//								mSharedPreferences);
						jsonString = WebserviceImport_more.GetHpInfo_top_search_complex_1_0(15, (String) ls2.get(ls2.size() - 1).get(DataBaseHelper.ID), 1, ckid, sqlString, hasckkc, mSharedPreferences);
					}
				}
			}
			msg.what = 2;// 1刷新数据
			msg.obj = jsonString;
			refreshHandler.sendMessage(msg);
		}
	};

	/*
	 * 网络获取数据的适配器
	 */
	public void setAdapter2() {
		if (flagstart) {// 第一次加载数据，重新绘制Listview
			if(!ls2.isEmpty()){
				mySimpleAdapter.setListData(ls2,ckid);
			}else{
				Toast.makeText(this, "货品不存在", Toast.LENGTH_SHORT).show();
			}
			flagstart = false;
		} else {
			if (ls2.size() > 0) {
				mySimpleAdapter.addListData(ls2);
			}else{
				Toast.makeText(this, "货品信息已经加载完成", Toast.LENGTH_SHORT).show();
			}
		}
		mXListView.setSelection(position);
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		int num = 0;
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.title_change:
			if (mPopupWindow.isShowing()) {
				mPopupWindow.dismiss();
				// up_downImgView.setImageResource(R.drawable.title_up);
			} else {
				mDemoAdapter.setData(ckmc_array, childList);
				mPopupWindow.showAsDropDown(title_layout, ScreenWidth / 4, -20);
				// up_downImgView.setImageResource(R.drawable.title_down);
			}
			title_listView1.setOnChildClickListener(new OnChildClickListener() {
				
				@Override
				public boolean onChildClick(ExpandableListView parent, View v,
						int groupPosition, int childPosition, long id) {
					// TODO 自动生成的方法存根
					Map<String, Object> map = (Map<String, Object>) mDemoAdapter
							.getGroup(groupPosition);
					Map<String, Object> map2 = (Map<String, Object>) mDemoAdapter
							.getChild(groupPosition, childPosition);
					hptag_str = map.get("ckmc").toString();
					ckmc_str = map2.get("ckmc").toString();
					ckid = Integer.parseInt(map2.get("ckid").toString());
					titleHpInfoLoad();
					mPopupWindow.dismiss();
					return true;
				}
			});
			break;
		case R.id.scan:
			intent.setClass(this, CaptureActivity.class);
			startActivityForResult(intent, HP_REQUESTCODE);
			break;
		case R.id.listtext:
			intent.setClass(this, SearchActivity.class);
			intent.putExtra("hint", 1);
			startActivityForResult(intent, 6);
//			finish();
			break;
		case R.id.del_cha:
			mEditText.setText("");
			break;
		case R.id.filter1:
			flagstart = true;
			intent.setClass(this, HP_filterActivity.class);
			startActivityForResult(intent, 5);
			break;
		}
	}

	/**
	 * title 选择加载货品
	 * */
	public void titleHpInfoLoad() {
		if (mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1) == 1) {
			if (WebserviceImport.isOpenNetwork(HpInfo_ListActivity.this)) {
				loadtag = -2;
				if (hptag_str.equals("库存不足")) {
					flagstart = true;
					mEditText.setText("");
					cacheThreadpool.execute(xxnumRunnable);
				} else if (hptag_str.equals("库存过多")) {
					flagstart = true;
					mEditText.setText("");
					cacheThreadpool.execute(sxnumRunnable);
				} else if (hptag_str.equals("今日变化")) {
					flagstart = true;
					mEditText.setText("");
					cacheThreadpool.execute(todayChangeRunnable);
				} else if (hptag_str.equals("货品信息")) {
					flagstart = true;
					mEditText.setText("");
					cacheThreadpool.execute(loadmessage);
				}
				titleTextView1.setVisibility(View.VISIBLE);
				titleTextView.setText(hptag_str);
				titleTextView1.setText(ckmc_str);
			} else {
				Toast.makeText(HpInfo_ListActivity.this, "网络未连接",
						Toast.LENGTH_SHORT).show();
			}
		} else if (mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1) == -1) {
			mEditText.setText("");
			if (hptag_str.equals("库存不足")) {
				if (ckid == -1) {
					ls2 = dm.Gethp_xx(str2);
				} else {
					ls2 = dm.Gethp_xxByckid(ckid);
				}
				mySimpleAdapter.setListData(ls2,ckid);
			} else if (hptag_str.equals("库存过多")) {
				if (ckid == -1) {
					ls2 = dm.Gethp_sx(str2);
				} else {
					ls2 = dm.Gethp_sxByckid(ckid);
				}
				mySimpleAdapter.setListData(ls2,ckid);
			} else if (hptag_str.equals("今日变化")) {
				String date = formatter.format(new Date(System
						.currentTimeMillis()));
				if (ckid == -1) {
					ls2 = dm.Gethp_todaychange(date);
				} else {
					ls2 = dm.Gethp_todaychangeByckid(ckid, date);
				}
				mySimpleAdapter.setListData(ls2,ckid);
			} else if (hptag_str.equals("货品信息")) {
				if (ckid == -1) {
					ls2 = dm.Gethp(str2);
				} else {
					ls2 = dm.GethpByckid(ckid);
				}
				mySimpleAdapter.setListData(ls2,ckid);
			}
			titleTextView1.setVisibility(View.VISIBLE);
			titleTextView.setText(hptag_str);
			titleTextView1.setText(ckmc_str);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO 自动生成的方法存根
		Intent intent = new Intent(this, HpInformationActivity.class);
		Map<String, Object> ms = (Map<String, Object>) arg0.getAdapter()
				.getItem(arg2);
		intent.putExtra("id", ms.get(DataBaseHelper.ID).toString());
		intent.putExtra("hpmc", ms.get(DataBaseHelper.HPMC).toString());
		intent.putExtra("hpbm", ms.get(DataBaseHelper.HPBM).toString());
		intent.putExtra("ggxh", ms.get(DataBaseHelper.GGXH).toString());
		startActivity(intent);
	}

	// ------------------------------刷新加载区域----------------------------
	@Override
	public void onRefresh() {
		// TODO 自动生成的方法存根
		flagstart = true;

		// mXListView.setStackFromBottom(false);
		if (WebserviceImport.isOpenNetwork(this)) {
			if (loadtag == -2) {
				if (hptag_str.equals("库存不足")) {
					cacheThreadpool.execute(xxnumRunnable);
				} else if (hptag_str.equals("库存过多")) {
					cacheThreadpool.execute(sxnumRunnable);
				} else if (hptag_str.equals("今日变化")) {
					cacheThreadpool.execute(todayChangeRunnable);
				} else if (hptag_str.equals("货品信息")) {
					cacheThreadpool.execute(loadmessage);
				}
			} else if (loadtag == -3) {
				cacheThreadpool.execute(searchRunnable);
			} else if (loadtag == 1) {
				cacheThreadpool.execute(searchComplexRunnable);
			} else if(loadtag == -4){
				//搜索刷新，不做任何处理
				onLoad();
			}
		} else {
			Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onLoadMore() {
		// TODO 自动生成的方法存根
		// mXListView.setStackFromBottom(true);
		if (flagend) {
			onLoad();
			Toast.makeText(this, "货品信息已经加载完成", Toast.LENGTH_SHORT).show();
		} else {
			if (WebserviceImport.isOpenNetwork(this)) {
				if (loadtag == -2) {
					if (hptag_str.equals("库存不足")) {
						cacheThreadpool.execute(xxnumRunnable);
					} else if (hptag_str.equals("库存过多")) {
						cacheThreadpool.execute(sxnumRunnable);
					} else if (hptag_str.equals("今日变化")) {
						cacheThreadpool.execute(todayChangeRunnable);
					} else if (hptag_str.equals("货品信息")) {
						cacheThreadpool.execute(loadmessage);
					}
				} else if (loadtag == -3) {
					cacheThreadpool.execute(searchRunnable);
				} else if (loadtag == 1) {
					cacheThreadpool.execute(searchComplexRunnable);
				} else if(loadtag == -4){
					//搜索刷新，不做任何处理
					onLoad();
				}
				
			} else {
				Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
			}
		}

	}

	private void onLoad() {
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
		mXListView.stopRefresh();
		mXListView.stopLoadMore();
		mXListView.setRefreshTime(refreshDate);
	}

	// --------------------------------------------------------------------
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO 自动生成的方法存根
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == HP_REQUESTCODE) {
			if (resultCode == 1) {
				scan_tm = data.getStringExtra("scan_tm");
				mEditText.setText(scan_tm);
				titleTextView.setText("货品信息");
				if (mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1) == 1) {
					if (WebserviceImport.isOpenNetwork(this)) {
						loadtag = -4;
						flagstart = true;
						cacheThreadpool.execute(search_tmRunnable);
					} else {
						Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT)
								.show();
					}
				} else if (mSharedPreferences.getInt(
						ShareprefenceBean.IS_LOGIN, -1) == -1) {
					ls2 = dm.Gethp_tm(str2, mEditText.getText().toString());
					mySimpleAdapter.setListData(ls2,ckid);
					if (ls2.isEmpty()) {
						Toast.makeText(this, "不存在扫描的货品信息", Toast.LENGTH_LONG)
								.show();
					}
				}
			}
		} else if (requestCode == 5) {
			if (resultCode == 1) {
				sqlString = data.getStringExtra("sql");
				// 高级搜索的ckid
				hasckkc = data.getIntExtra("hasckkc", 0);
				ckid = data.getIntExtra("ckid", -1);
				if (mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1) == 1) {
					if (WebserviceImport.isOpenNetwork(this)) {
						loadtag = 1;
						flagstart = true;
						titleTextView.setText("货品信息");
						titleTextView1.setVisibility(View.GONE);
						mEditText.setText("");
						cacheThreadpool.execute(searchComplexRunnable);
					} else {
						Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT)
								.show();
					}
				} else if (mSharedPreferences.getInt(
						ShareprefenceBean.IS_LOGIN, -1) == -1) {
					mEditText.setText("");
					titleTextView.setText("货品信息");
					ls2 = dm.GetHp_complex(sqlString, ckid, hasckkc);
					mySimpleAdapter.setListData(ls2,ckid);
				}
			}
		}else if (requestCode == 6){
			if (resultCode == 1) {
				if(mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1)==1){
					flagstart = true;
					if(data.getIntExtra("scanOrsearch", -1)==1){//判断是否从搜索页面跳转过来的，1是从搜索页面的输入框条转过来的，2是从搜索页面的扫描条转过来的，-1是直接打开这个界面
						loadtag = -3;
						mEditText.setText(data.getStringExtra("searchString"));
						cacheThreadpool.execute(searchRunnable);
					}else if(data.getIntExtra("scanOrsearch", -1)==2){
						loadtag = -4;
						mEditText.setText(data.getStringExtra("searchString"));
						cacheThreadpool.execute(search_tmRunnable);
					}
				}else{
					if(data.getIntExtra("scanOrsearch", -1)==1){//判断是否从搜索页面跳转过来的，1是从搜索页面的输入框条转过来的，2是从搜索页面的扫描条转过来的，-1是直接打开这个界面
						mEditText.setText(data.getStringExtra("searchString"));
						ls2 = dm.queryList(str2, data.getStringExtra("searchString"),ckid);
						if(ls2.isEmpty()){
							Toast.makeText(this, "搜索货品不存在", Toast.LENGTH_LONG).show();
						}
						mySimpleAdapter.setListData(ls2,ckid);
					}else if(data.getIntExtra("scanOrsearch", -1)==2){
						mEditText.setText(data.getStringExtra("searchString"));
						ls2 = dm.Gethp_tm(str2, data.getStringExtra("searchString"));
						if (ls2.isEmpty()) {
							Toast.makeText(this, "不存在扫描的货品信息", Toast.LENGTH_LONG)
									.show();
						}
						mySimpleAdapter.setListData(ls2,ckid);
					}
				}
			}
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO 自动生成的方法存根
		s.toString().replace("'", "");
		ls2.clear();
		mySimpleAdapter.notifyDataSetChanged();
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
			searchDelBtn.setVisibility(View.VISIBLE);
		} else {
			searchDelBtn.setVisibility(View.GONE);
		}
	}
}
