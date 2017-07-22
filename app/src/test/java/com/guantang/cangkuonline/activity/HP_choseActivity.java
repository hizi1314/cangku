package com.guantang.cangkuonline.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.XListView.XListView;
import com.guantang.cangkuonline.XListView.XListView.IXListViewListener;
import com.guantang.cangkuonline.activity.DiaoboHP_choseActivity.SearchHPAsyncTask;
import com.guantang.cangkuonline.activity.OrderHP_choseActivity.HpLoadAnyctask;
import com.guantang.cangkuonline.adapter.HpListBaseAdapter2;
import com.guantang.cangkuonline.adapter.HpListBaseAdapter3;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.database.DataBaseMethod;
import com.guantang.cangkuonline.database.DataBaseOperateMethod;
import com.guantang.cangkuonline.dialog.AddNumberDialog;
import com.guantang.cangkuonline.dialog.DiaoboAddNumDialog;
import com.guantang.cangkuonline.eventbusBean.ObjectFive;
import com.guantang.cangkuonline.eventbusBean.ObjectSeven;
import com.guantang.cangkuonline.eventbusBean.ObjectSix;
import com.guantang.cangkuonline.helper.DecimalsHelper;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;
import com.guantang.cangkuonline.sortlistview.SideBar;
import com.guantang.cangkuonline.swipemenulistview.SwipeMenuListView;
import com.guantang.cangkuonline.webservice.WebserviceImport;
import com.guantang.cangkuonline.webservice.WebserviceImport_more;

import de.greenrobot.event.EventBus;

public class HP_choseActivity extends Activity implements OnClickListener,
		TextWatcher, IXListViewListener {
	private ImageButton backImageButton, scanImageButton;
	private TextView titleTextView, numbershowTxtView, comfirmBtn;
	private Button deleteBtn, searchBtn2;
	private FrameLayout jianhuokuangLayout;
	private ImageView searchDelBtn;
	private EditText mEditText1;
	private XListView mXListView1;
	private SwipeMenuListView mListView2;
	private LinearLayout mLinearLayout;
	private LayoutInflater inflater;
	private ArrayList<View> views = new ArrayList<View>();
	private int ScreenWidth;
	private int ckid = -1, op_type = 1;
	private String djid = "", dh = "", dacttype = "";
	private ProgressDialog proDialog;
	private HpListBaseAdapter2 HplistAdapter;
	// private HpListBaseAdapter3 showlistAdapter;
	private List<Map<String, Object>> searchlist = new ArrayList<Map<String,Object>>();
	private List<Map<String, Object>> stacklist = new ArrayList<Map<String,Object>>();
	private List<Map<String, Object>> getList = new ArrayList<Map<String,Object>>();
	private List<Map<String, Object>> hpList = new ArrayList<Map<String,Object>>();
	private DataBaseOperateMethod dm_op = new DataBaseOperateMethod(this);
	private DataBaseMethod dm = new DataBaseMethod(this);
	private String[] str2 = { DataBaseHelper.ID, DataBaseHelper.HPMC,
			DataBaseHelper.HPBM, DataBaseHelper.HPTM, DataBaseHelper.GGXH,
			DataBaseHelper.CurrKC };
	private String[] str4 = { DataBaseHelper.ID, DataBaseHelper.HPMC,
			DataBaseHelper.HPBM, DataBaseHelper.HPTM, DataBaseHelper.GGXH,
			DataBaseHelper.CurrKC, DataBaseHelper.KCSL };
	private String[] strs = { "id", "HPMC", "HPBM", "HPTBM", "GGXH", "CurrKc","kcsl" };
	private String[] str1 = { DataBaseHelper.HPID, DataBaseHelper.BTKC,
			DataBaseHelper.MSL };
	private String[] str3 = { DataBaseHelper.HPID, DataBaseHelper.MID,
			DataBaseHelper.MVDDATE, DataBaseHelper.MSL, DataBaseHelper.MVDType,
			DataBaseHelper.DH, DataBaseHelper.BTKC, DataBaseHelper.ATKC,
			DataBaseHelper.MVDirect, DataBaseHelper.DJ, DataBaseHelper.ZJ,
			DataBaseHelper.MVType, DataBaseHelper.CKID };
	private ExecutorService cacheThreadPool = Executors.newCachedThreadPool();
	private SideBar sideBar;
	private TextView dialog;
	private SharedPreferences mSharedPreferences;
	private Boolean flagend = false;// 标记是否加载完，true为加载完
	
	/**
	 * 条码识别，第0元素 为1是部分匹配，为0是完全匹配，为-1是部分匹配设置有误，如果是部分匹配后续还有4个元素，分别是货品开始、货品截至、数量开始、数量截至。
	 * */
	private Map<String, Integer> MatchMap = new HashMap<String, Integer>();
	/**
	 * 部分匹配的数量部分的数字。如果不是部分匹配且不是数字，numString的值是""。
	 * */
	private String numString = "";
	
	
	/**
	 * 添加一个信号量，防止列表界面还没刷新数据就变更了
	 * */
	private volatile Semaphore mSemaphore = new Semaphore(1);

	// private ClearEditText mClearEditText;
	// /**
	// * 汉字转换成拼音的类
	// */
	// private CharacterParser characterParser;
	// private List<SortModel> SourceDateList;
	//
	// /**
	// * 根据拼音来排列ListView里面的数据类
	// */
	// private PinyinComparator pinyinComparator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 不去重新绘制界面
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		setContentView(R.layout.activity_hp_add);
		mSharedPreferences = getSharedPreferences(
				ShareprefenceBean.SHAREPREFENCE_NAME, Context.MODE_PRIVATE);
		Intent intent = getIntent();
		ckid = intent.getIntExtra("ckid", -1);
		djid = intent.getStringExtra("djid");
		dh = intent.getStringExtra("dh");
		op_type = intent.getIntExtra("op_type", 1);
		dacttype = intent.getStringExtra("dacttype");
		
		EventBus.getDefault().register(this);
		initControl();
		init();
	}

	@Override
	protected void onDestroy() {
		// TODO 自动生成的方法存根
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	public void initControl() {
		backImageButton = (ImageButton) findViewById(R.id.back);
		titleTextView = (TextView) findViewById(R.id.title);
		scanImageButton = (ImageButton) findViewById(R.id.scanImgBtn);
		mEditText1 = (EditText) findViewById(R.id.listtext1);
		mXListView1 = (XListView) findViewById(R.id.hplist1);
		jianhuokuangLayout = (FrameLayout) findViewById(R.id.add_hp);
		comfirmBtn = (TextView) findViewById(R.id.show_hp);
		mLinearLayout = (LinearLayout) findViewById(R.id.tabpager);
		numbershowTxtView = (TextView) findViewById(R.id.numbershow);
		searchDelBtn = (ImageView) findViewById(R.id.del_cha);

		searchDelBtn.setOnClickListener(this);
		backImageButton.setOnClickListener(this);
		scanImageButton.setOnClickListener(this);
		mXListView1.setPullLoadEnable(true);// 设置可以加载更多
		mXListView1.setPullRefreshEnable(true);// 设置可以刷新

		mXListView1.setXListViewListener(this);
		jianhuokuangLayout.setOnClickListener(this);
		comfirmBtn.setOnClickListener(this);
		mEditText1.addTextChangedListener(this);
		mEditText1.setOnClickListener(this);
		mEditText1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId==EditorInfo.IME_ACTION_SEARCH || (event!=null&&event.getKeyCode()==KeyEvent.KEYCODE_ENTER)){
					if (mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1) == 1) {
						if (WebserviceImport.isOpenNetwork(HP_choseActivity.this)) {
							// proDialog = ProgressDialog.show(this, null, "正在加载数据……");
//							try {
//								mSemaphore.acquire();
//							} catch (InterruptedException e) {
//								// TODO 自动生成的 catch 块
//								e.printStackTrace();
//							}
							new SearchHPAsyncTask().execute("10", "0");
						} else {
							Toast.makeText(HP_choseActivity.this, "网络未连接", Toast.LENGTH_SHORT).show();
						}
					} else if (mSharedPreferences
							.getInt(ShareprefenceBean.IS_LOGIN, -1) == -1) {

						if (op_type == 1) {
							searchlist = dm.queryList(str2, mEditText1.getText()
									.toString(), "0");
						} else {
							searchlist = dm.queryList_ckid(mEditText1.getText()
									.toString(), "0", ckid);
						}

						if (searchlist.size() > 0) {
							for (int i = 0; i < searchlist.size(); i++) {
								List<Map<String, Object>> list2 = dm_op.Gt_Moved(djid,
										searchlist.get(i).get(DataBaseHelper.ID)
												.toString(), str3);
								if (op_type == 0) {
									if (!list2.isEmpty()) {
										searchlist.get(i).put(
												"caozuoshu",
												list2.get(0).get(DataBaseHelper.ATKC)
														.toString());
										searchlist.get(i).put(
												"shuying",
												list2.get(0).get(DataBaseHelper.MVType)
														.toString());// 是否盘盈
									} else {
										searchlist.get(i).put("caozuoshu", "无");// 无
																				// 表示该货品没有被盘点
									}
								} else {
									if (!list2.isEmpty()) {
										searchlist.get(i).put(
												"caozuoshu",
												list2.get(0).get(DataBaseHelper.MSL)
														.toString());
									} else {
										searchlist.get(i).put("caozuoshu", "0");
									}
								}
							}
							setAdapter1();
						} else {
							if (op_type == 1) {
								Toast.makeText(HP_choseActivity.this, "搜索货品不存在",
										Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(HP_choseActivity.this, "搜索货品不存在本仓库中",
										Toast.LENGTH_SHORT).show();
							}
						}
					}
					return true;
				}
				return false;
			}
		});

		// 加载本单据已填写的货品种数
		Map<String, Object> map = dm_op.Djhp_Sum(djid);
		String munstr = map.get("num").toString();
		if (!munstr.equals("0")) {
			numbershowTxtView.setVisibility(View.VISIBLE);
			if (Integer.parseInt(munstr) > 9) {
				numbershowTxtView.setBackgroundResource(R.drawable.numtip2);
			} else if (Integer.parseInt(munstr) > 99) {
				numbershowTxtView.setBackgroundResource(R.drawable.numtip3);
			} else {
				numbershowTxtView.setBackgroundResource(R.drawable.numtip1);
			}
			if (Integer.parseInt(munstr) > 999) {
				numbershowTxtView.setText("999");
			} else {
				numbershowTxtView.setText(munstr);
			}
			comfirmBtn.setTextColor(Color.WHITE);
			comfirmBtn.setBackground(getResources().getDrawable(
					R.drawable.addcomplete));
			comfirmBtn.setClickable(true);// 没有添加成功货品就不能点击
		} else {
			numbershowTxtView.setVisibility(View.GONE);
			comfirmBtn.setTextColor(R.color.ziti1);
			comfirmBtn.setBackgroundColor(getResources().getColor(R.color.transparent));
			comfirmBtn.setClickable(false);// 没有添加成功货品就不能点击
		}
	}

	public void init() {
		// MyHpAddPageAdapter mPagerAdapter = new MyHpAddPageAdapter(views);
		// mViewPager.setAdapter(mPagerAdapter);
		searchlist = new ArrayList<Map<String, Object>>();
		stacklist = new ArrayList<Map<String, Object>>();
		getList = new ArrayList<Map<String, Object>>();
		hpList = new ArrayList<Map<String, Object>>();
		HplistAdapter = new HpListBaseAdapter2(this, op_type, djid, dh, ckid,
				dacttype);
		mXListView1.setAdapter(HplistAdapter);

		if (mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1) == 1) {
			if (WebserviceImport.isOpenNetwork(this)) {

//				cacheThreadPool.execute(searchRunnable);
				searchlist.clear();
				new SearchHPAsyncTask().execute("10", "0");
			} else {
				Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
			}
		} else if (mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1) == -1) {
			localLoadHP();
		}

	}
	
	public void onEventMainThread(ObjectFive obj) {
		if (op_type == 0) {
			for (int i = 0; i < searchlist.size(); i++) {
				if (searchlist.get(i).get(DataBaseHelper.ID).toString()
						.equals(obj.getHpid())) {
					searchlist.get(i).put("caozuoshu", obj.getNum());
					searchlist.get(i).put("shuying", obj.getShuying());
					break;
				}
			}
		} else {
			for (int i = 0; i < searchlist.size(); i++) {
				if (searchlist.get(i).get(DataBaseHelper.ID).toString()
						.equals(obj.getHpid())) {
					searchlist.get(i).put("caozuoshu", obj.getNum());
					break;
				}
			}
		}

		HplistAdapter.notifyDataSetChanged();
	}

	public void onEventMainThread(ObjectSix obj) {

		Map<String, Object> map = dm_op.Djhp_Sum(djid);
		String munstr = map.get("num").toString();
		if (!munstr.equals("0")) {
			numbershowTxtView.setVisibility(View.VISIBLE);
			if (Integer.parseInt(munstr) > 9) {
				numbershowTxtView.setBackgroundResource(R.drawable.numtip2);
			} else if (Integer.parseInt(munstr) > 99) {
				numbershowTxtView.setBackgroundResource(R.drawable.numtip3);
			} else {
				numbershowTxtView.setBackgroundResource(R.drawable.numtip1);
			}
			if (Integer.parseInt(munstr) > 999) {
				numbershowTxtView.setText("999");
			} else {
				numbershowTxtView.setText(munstr);
			}
			comfirmBtn.setTextColor(Color.WHITE);
			comfirmBtn.setBackground(getResources().getDrawable(
					R.drawable.addcomplete));
			comfirmBtn.setClickable(true);// 没有添加成功货品就不能点击
		} else {
			numbershowTxtView.setVisibility(View.GONE);
			comfirmBtn.setTextColor(R.color.ziti1);
			comfirmBtn.setBackgroundResource(R.color.transparent);
			comfirmBtn.setClickable(false);// 没有添加成功货品就不能点击
		}

	}

	public void onEventMainThread(ObjectSeven obj) {
		// map是数据条数
		Map<String, Object> map = dm_op.Djhp_Sum(djid);
		String munstr = map.get("num").toString();
		if (!munstr.equals("0")) {
			numbershowTxtView.setVisibility(View.VISIBLE);
			if (Integer.parseInt(munstr) > 9) {
				numbershowTxtView.setBackgroundResource(R.drawable.numtip2);
			} else if (Integer.parseInt(munstr) > 99) {
				numbershowTxtView.setBackgroundResource(R.drawable.numtip3);
			} else {
				numbershowTxtView.setBackgroundResource(R.drawable.numtip1);
			}
			if (Integer.parseInt(munstr) > 999) {
				numbershowTxtView.setText("999");
			} else {
				numbershowTxtView.setText(munstr);
			}
			comfirmBtn.setTextColor(Color.WHITE);
			comfirmBtn.setBackground(getResources().getDrawable(
					R.drawable.addcomplete));
			comfirmBtn.setClickable(true);// 没有添加成功货品就不能点击
		} else {
			numbershowTxtView.setVisibility(View.GONE);
			comfirmBtn.setTextColor(R.color.ziti1);
			comfirmBtn.setBackgroundResource(R.color.transparent);
			comfirmBtn.setClickable(false);// 没有添加成功货品就不能点击
		}
		// 删除的map2
		Map<String, Object> map2 = obj.getMap();
		if (op_type == 0) {
			for (int i = 0; i < searchlist.size(); i++) {
				if (searchlist.get(i).get(DataBaseHelper.ID)
						.equals(map2.get(DataBaseHelper.HPID).toString())) {
					searchlist.get(i).put("caozuoshu", "无");
				}
			}
		} else {
			for (int i = 0; i < searchlist.size(); i++) {
				if (searchlist.get(i).get(DataBaseHelper.ID)
						.equals(map2.get(DataBaseHelper.HPID).toString())) {
					searchlist.get(i).put("caozuoshu", "0");
				}
			}
		}
		HplistAdapter.notifyDataSetChanged();
	}

	public void setAdapter1() {
		HplistAdapter.setDataList(searchlist);
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		Intent intent = new Intent();
		Animation animation = null;
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.add_hp:
			intent.setClass(this, DJ_detailActivity.class);
			intent.putExtra("dh", dh);
			intent.putExtra("djid", djid);
			intent.putExtra("ckid", ckid);
			intent.putExtra("op_type", op_type);
			intent.putExtra("dacttype", dacttype);
			startActivity(intent);
			break;
		case R.id.show_hp:
			setResult(1, intent);
			finish();
			break;
		case R.id.scanImgBtn:
			intent.setClass(this, CaptureActivity.class);
			startActivityForResult(intent, 1);
			break;
		case R.id.del_cha:
			mEditText1.setText("");
			break;
		case R.id.listtext1:
			intent.setClass(this, SearchActivity.class);
//			intent.putExtra("HP_choseActivityStart", (Boolean)true);
			intent.putExtra("hint", 1);
			startActivityForResult(intent, 2);
			break;
		}
	}

	/**
	 * 本地加加载货品的方法
	 * */
	public void localLoadHP() {
		stopLoadface();
		if (op_type == 1) {
			if (searchlist.isEmpty()) {
				searchlist = dm.queryList(str2,
						mEditText1.getText().toString(), "0");
				if (searchlist.size() > 0) {
					for (int i = 0; i < searchlist.size(); i++) {
						List<Map<String, Object>> list2 = dm_op.Gt_Moved(djid,
								searchlist.get(i).get(DataBaseHelper.ID)
										.toString(), str3);
						if (op_type == 0) {
							if (!list2.isEmpty()) {
								searchlist.get(i).put(
										"caozuoshu",
										list2.get(0).get(DataBaseHelper.ATKC)
												.toString());
								searchlist.get(i).put(
										"shuying",
										list2.get(0).get(DataBaseHelper.MVType)
												.toString());// 是否盘盈
							} else {
								searchlist.get(i).put("caozuoshu", "无");// 无
																		// 表示该货品没有被盘点
							}
						} else {
							if (!list2.isEmpty()) {
								searchlist.get(i).put(
										"caozuoshu",
										list2.get(0).get(DataBaseHelper.MSL)
												.toString());
							} else {
								searchlist.get(i).put("caozuoshu", "0");
							}
						}
					}
					setAdapter1();
				}
			} else {
				List<Map<String, Object>> littlelist = new ArrayList<Map<String, Object>>();
				littlelist = dm.queryList(
						str2,
						mEditText1.getText().toString(),
						searchlist.get(searchlist.size() - 1)
								.get(DataBaseHelper.ID).toString());
				if (!littlelist.isEmpty()) {
					for (int i = 0; i < littlelist.size(); i++) {
						List<Map<String, Object>> list2 = dm_op.Gt_Moved(djid,
								littlelist.get(i).get(DataBaseHelper.ID)
										.toString(), str3);
						if (op_type == 0) {
							if (!list2.isEmpty()) {
								littlelist.get(i).put(
										"caozuoshu",
										list2.get(0).get(DataBaseHelper.ATKC)
												.toString());
								littlelist.get(i).put(
										"shuying",
										list2.get(0).get(DataBaseHelper.MVType)
												.toString());// 是否盘盈
							} else {
								littlelist.get(i).put("caozuoshu", "无");// 无
																		// 表示该货品没有被盘点
							}
						} else {
							if (!list2.isEmpty()) {
								littlelist.get(i).put(
										"caozuoshu",
										list2.get(0).get(DataBaseHelper.MSL)
												.toString());
							} else {
								littlelist.get(i).put("caozuoshu", "0");
							}
						}
					}
					searchlist.addAll(littlelist);
					setAdapter1();
				} else {
					Toast.makeText(HP_choseActivity.this, "所有货品已经加载完成",
							Toast.LENGTH_SHORT).show();
				}
			}
		} else {
			if (searchlist.isEmpty()) {
				searchlist = dm.queryList_ckid(mEditText1.getText().toString(),
						"0", ckid);
				if (searchlist.size() > 0) {
					for (int i = 0; i < searchlist.size(); i++) {
						List<Map<String, Object>> list2 = dm_op.Gt_Moved(djid,
								searchlist.get(i).get(DataBaseHelper.ID)
										.toString(), str3);
						if (op_type == 0) {
							if (!list2.isEmpty()) {
								searchlist.get(i).put(
										"caozuoshu",
										list2.get(0).get(DataBaseHelper.ATKC)
												.toString());
								searchlist.get(i).put(
										"shuying",
										list2.get(0).get(DataBaseHelper.MVType)
												.toString());// 是否盘盈
							} else {
								searchlist.get(i).put("caozuoshu", "无");// 无
																		// 表示该货品没有被盘点
							}
						} else {
							if (!list2.isEmpty()) {
								searchlist.get(i).put(
										"caozuoshu",
										list2.get(0).get(DataBaseHelper.MSL)
												.toString());
							} else {
								searchlist.get(i).put("caozuoshu", "0");
							}
						}
					}
					setAdapter1();
				}
			} else {
				List<Map<String, Object>> littlelist = new ArrayList<Map<String, Object>>();
				littlelist = dm.queryList_ckid(
						mEditText1.getText().toString(),
						searchlist.get(searchlist.size() - 1)
								.get(DataBaseHelper.ID).toString(), ckid);
				if (!littlelist.isEmpty()) {
					for (int i = 0; i < littlelist.size(); i++) {
						List<Map<String, Object>> list2 = dm_op.Gt_Moved(djid,
								littlelist.get(i).get(DataBaseHelper.ID)
										.toString(), str3);
						if (op_type == 0) {
							if (!list2.isEmpty()) {
								littlelist.get(i).put(
										"caozuoshu",
										list2.get(0).get(DataBaseHelper.ATKC)
												.toString());
								littlelist.get(i).put(
										"shuying",
										list2.get(0).get(DataBaseHelper.MVType)
												.toString());// 是否盘盈
							} else {
								littlelist.get(i).put("caozuoshu", "无");// 无
																		// 表示该货品没有被盘点
							}
						} else {
							if (!list2.isEmpty()) {
								littlelist.get(i).put(
										"caozuoshu",
										list2.get(0).get(DataBaseHelper.MSL)
												.toString());
							} else {
								littlelist.get(i).put("caozuoshu", "0");
							}
						}
					}
					searchlist.addAll(littlelist);
					setAdapter1();
				} else {
					Toast.makeText(HP_choseActivity.this, "所有货品已经加载完成",
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO 自动生成的方法存根
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			if (resultCode == 1) {
				//----------------------------条码部分匹配原则---------------------------------------
				String scanString = data.getStringExtra("scan_tm");
				String scanTMString = "";
				MatchMap=dm_op.getTMMacth();
				switch(MatchMap.get("partMatch")){
				case 0:
					scanTMString = scanString;
					numString="";
					break;
				case 1:
					if(scanString.length()>=MatchMap.get("sljz")){
						scanTMString=scanString.substring(MatchMap.get("hpks")-1, MatchMap.get("hpjz"));
						numString=scanString.substring(MatchMap.get("slks")-1, MatchMap.get("sljz"));
						if(!numString.matches("^(-?\\d+)(\\.\\d+)?$")){//匹配为非数字
							numString="";
							Toast.makeText(this, "条码部分匹配数量部分为非数字", Toast.LENGTH_LONG).show();
							return;
						}else{
							numString=DecimalsHelper.Transfloat(Double.parseDouble(numString), 8);
						}
					}else if(scanString.length()>=MatchMap.get("hpjz")){
						scanTMString=scanString.substring(MatchMap.get("hpks")-1, MatchMap.get("hpjz"));
						numString="";
						Toast.makeText(this, "条码数量部分长度不够,数量自动归零", Toast.LENGTH_LONG).show();
					}else if(scanString.length()<MatchMap.get("hpjz")){
						scanTMString = scanString;
						Toast.makeText(this, "条码长度不够,搜索结果可能有误", Toast.LENGTH_LONG).show();
					}
					break;
				case -1:
					Toast.makeText(this, "条码部分匹配设置有误，匹配设置请设置正整数", Toast.LENGTH_LONG).show();
					return;
				}
				//--------------------------------------------------------------------------------
				mEditText1.setText(scanTMString);
				if (mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1) == 1) {
					if (WebserviceImport.isOpenNetwork(this)) {
						 proDialog = ProgressDialog.show(this, null,
						 "正在加载数据……");
//						cacheThreadPool.execute(search_tmRunnable);
						new SearchHPbyTMAsyncTask().execute(mEditText1.getText().toString());
					} else {
						Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT)
								.show();
					}
				} else if (mSharedPreferences.getInt(
						ShareprefenceBean.IS_LOGIN, -1) == -1) {
					if (op_type == 1) {
						searchlist = dm.Gethp_tm(str2, mEditText1.getText()
								.toString());
						if (!searchlist.isEmpty()) {
							List<Map<String, Object>> list2 = dm_op.Gt_Moved(
									djid,
									searchlist.get(0).get(DataBaseHelper.ID)
											.toString(), str3);
							if (!list2.isEmpty()) {
								searchlist.get(0).put(
										"caozuoshu",
										list2.get(0)
												.get(DataBaseHelper.MSL)
												.toString());
							} else {
								searchlist.get(0).put("caozuoshu", "0");
							}
							setAdapter1();
							AddNumberDialog mAddNumberDialog = new AddNumberDialog(HP_choseActivity.this, op_type, searchlist.get(0), djid, dh, ckid, dacttype,numString,R.style.ButtonDialogStyle);
							mAddNumberDialog.setCanceledOnTouchOutside(false);
							Window win = mAddNumberDialog.getWindow();
							win.getDecorView().setPadding(0, 0, 0, 0);
							win.setGravity(Gravity.BOTTOM);
							WindowManager.LayoutParams lp = win.getAttributes();
							        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
							        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
							        win.setAttributes(lp);
							mAddNumberDialog.show();
						} else {
							Toast.makeText(HP_choseActivity.this, "扫描的货品不存在",
									Toast.LENGTH_SHORT).show();
						}
					} else {
						searchlist = dm.GetHP_byTM_CKID(mEditText1.getText()
								.toString(), ckid);
						if (!searchlist.isEmpty()) {
							List<Map<String, Object>> list2 = dm_op.Gt_Moved(
									djid,
									searchlist.get(0).get(DataBaseHelper.ID)
											.toString(), str3);
							if (op_type == 0) {
								if (!list2.isEmpty()) {
									searchlist.get(0).put(
											"caozuoshu",
											list2.get(0)
													.get(DataBaseHelper.ATKC)
													.toString());
									searchlist.get(0).put(
											"shuying",
											list2.get(0)
													.get(DataBaseHelper.MVType)
													.toString());// 是否盘盈
								} else {
									searchlist.get(0).put("caozuoshu", "无");// 无
																			// 表示该货品没有被盘点
								}
							} else {
								if (!list2.isEmpty()) {
									searchlist.get(0).put(
											"caozuoshu",
											list2.get(0)
													.get(DataBaseHelper.MSL)
													.toString());
								} else {
									searchlist.get(0).put("caozuoshu", "0");
								}
							}
							setAdapter1();
							AddNumberDialog mAddNumberDialog = new AddNumberDialog(HP_choseActivity.this, op_type, searchlist.get(0), djid, dh, ckid, dacttype,numString,R.style.ButtonDialogStyle);
							mAddNumberDialog.setCanceledOnTouchOutside(false);
							Window win = mAddNumberDialog.getWindow();
							win.getDecorView().setPadding(0, 0, 0, 0);
							win.setGravity(Gravity.BOTTOM);
							WindowManager.LayoutParams lp = win.getAttributes();
							        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
							        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
							        win.setAttributes(lp);
							mAddNumberDialog.show();
						} else {
							Toast.makeText(HP_choseActivity.this,
									"本仓库不存在扫描的货品", Toast.LENGTH_SHORT).show();
						}
					}
				}
			}
		}else if(requestCode == 2){
			if(resultCode == 1){
				searchlist.clear();
				if(data.getIntExtra("scanOrsearch", -1)==1){//判断是否从搜索页面跳转过来的，1是从搜索页面的输入框条转过来的，2是从搜索页面的扫描跳转过来的，-1是直接打开这个界面
					mEditText1.setText(data.getStringExtra("searchString"));
					if (mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1) == 1) {
						if (WebserviceImport.isOpenNetwork(HP_choseActivity.this)) {
							// proDialog = ProgressDialog.show(this, null, "正在加载数据……");
//							try {
//								mSemaphore.acquire();
//							} catch (InterruptedException e) {
//								// TODO 自动生成的 catch 块
//								e.printStackTrace();
//							}
							new SearchHPAsyncTask().execute("10", "0");
						} else {
							Toast.makeText(HP_choseActivity.this, "网络未连接", Toast.LENGTH_SHORT).show();
						}
					} else if (mSharedPreferences
							.getInt(ShareprefenceBean.IS_LOGIN, -1) == -1) {

						if (op_type == 1) {
							searchlist = dm.queryList(str2, mEditText1.getText()
									.toString(), "0");
						} else {
							searchlist = dm.queryList_ckid(mEditText1.getText()
									.toString(), "0", ckid);
						}

						if (searchlist.size() > 0) {
							for (int i = 0; i < searchlist.size(); i++) {
								List<Map<String, Object>> list2 = dm_op.Gt_Moved(djid,
										searchlist.get(i).get(DataBaseHelper.ID)
												.toString(), str3);
								if (op_type == 0) {
									if (!list2.isEmpty()) {
										searchlist.get(i).put(
												"caozuoshu",
												list2.get(0).get(DataBaseHelper.ATKC)
														.toString());
										searchlist.get(i).put(
												"shuying",
												list2.get(0).get(DataBaseHelper.MVType)
														.toString());// 是否盘盈
									} else {
										searchlist.get(i).put("caozuoshu", "无");// 无
																				// 表示该货品没有被盘点
									}
								} else {
									if (!list2.isEmpty()) {
										searchlist.get(i).put(
												"caozuoshu",
												list2.get(0).get(DataBaseHelper.MSL)
														.toString());
									} else {
										searchlist.get(i).put("caozuoshu", "0");
									}
								}
							}
							setAdapter1();
						} else {
							if (op_type == 1) {
								Toast.makeText(HP_choseActivity.this, "搜索货品不存在",
										Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(HP_choseActivity.this, "搜索货品不存在本仓库中",
										Toast.LENGTH_SHORT).show();
							}
						}
					}
				}else if(data.getIntExtra("scanOrsearch", -1)==2){//判断是否从搜索页面跳转过来的，1是从搜索页面的输入框条转过来的，2是从搜索页面的扫描条转过来的，-1是直接打开这个界面
					//----------------------------条码部分匹配原则---------------------------------------
					String scanString = data.getStringExtra("searchString");
					String scanTMString = "";
					MatchMap=dm_op.getTMMacth();
					switch(MatchMap.get("partMatch")){
					case 0:
						scanTMString = scanString;
						numString="";
						break;
					case 1:
						if(scanString.length()>=MatchMap.get("sljz")){
							scanTMString=scanString.substring(MatchMap.get("hpks")-1, MatchMap.get("hpjz"));
							numString=scanString.substring(MatchMap.get("slks")-1, MatchMap.get("sljz"));
							if(!numString.matches("^(-?\\d+)(\\.\\d+)?$")){//匹配为非数字
								numString="";
								Toast.makeText(this, "条码部分匹配数量部分为非数字", Toast.LENGTH_LONG).show();
								return;
							}else{
								numString=DecimalsHelper.Transfloat(Double.parseDouble(numString), 8);
							}
						}else if(scanString.length()>=MatchMap.get("hpjz")){
							scanTMString=scanString.substring(MatchMap.get("hpks")-1, MatchMap.get("hpjz"));
							numString="";
							Toast.makeText(this, "条码数量部分长度不够,数量自动归零", Toast.LENGTH_LONG).show();
						}else if(scanString.length()<MatchMap.get("hpjz")){
							scanTMString = scanString;
							Toast.makeText(this, "条码长度不够,搜索结果可能有误", Toast.LENGTH_LONG).show();
						}
						break;
					case -1:
						Toast.makeText(this, "条码部分匹配设置有误，匹配设置请设置正整数", Toast.LENGTH_LONG).show();
						return;
					}
					//--------------------------------------------------------------------------------
					mEditText1.setText(scanTMString);
					if (mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1) == 1) {
						if (WebserviceImport.isOpenNetwork(this)) {
							 proDialog = ProgressDialog.show(this, null,
							 "正在加载数据……");
//							cacheThreadPool.execute(search_tmRunnable);
							new SearchHPbyTMAsyncTask().execute(mEditText1.getText().toString());
						} else {
							Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT)
									.show();
						}
					} else if (mSharedPreferences.getInt(
							ShareprefenceBean.IS_LOGIN, -1) == -1) {
						if (op_type == 1) {
							searchlist = dm.Gethp_tm(str2, mEditText1.getText()
									.toString());
							if (!searchlist.isEmpty()) {
								List<Map<String, Object>> list2 = dm_op.Gt_Moved(
										djid,
										searchlist.get(0).get(DataBaseHelper.ID)
												.toString(), str3);
								if (!list2.isEmpty()) {
									searchlist.get(0).put(
											"caozuoshu",
											list2.get(0)
													.get(DataBaseHelper.MSL)
													.toString());
								} else {
									searchlist.get(0).put("caozuoshu", "0");
								}
								setAdapter1();
								AddNumberDialog mAddNumberDialog = new AddNumberDialog(HP_choseActivity.this, op_type, searchlist.get(0), djid, dh, ckid, dacttype,numString,R.style.ButtonDialogStyle);
								mAddNumberDialog.setCanceledOnTouchOutside(false);
								Window win = mAddNumberDialog.getWindow();
								win.getDecorView().setPadding(0, 0, 0, 0);
								win.setGravity(Gravity.BOTTOM);
								WindowManager.LayoutParams lp = win.getAttributes();
								        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
								        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
								        win.setAttributes(lp);
								mAddNumberDialog.show();
							} else {
								Toast.makeText(HP_choseActivity.this, "扫描的货品不存在",
										Toast.LENGTH_SHORT).show();
							}
						} else {
							searchlist = dm.GetHP_byTM_CKID(mEditText1.getText()
									.toString(), ckid);
							if (!searchlist.isEmpty()) {
								List<Map<String, Object>> list2 = dm_op.Gt_Moved(
										djid,
										searchlist.get(0).get(DataBaseHelper.ID)
												.toString(), str3);
								if (op_type == 0) {
									if (!list2.isEmpty()) {
										searchlist.get(0).put(
												"caozuoshu",
												list2.get(0)
														.get(DataBaseHelper.ATKC)
														.toString());
										searchlist.get(0).put(
												"shuying",
												list2.get(0)
														.get(DataBaseHelper.MVType)
														.toString());// 是否盘盈
									} else {
										searchlist.get(0).put("caozuoshu", "无");// 无
																				// 表示该货品没有被盘点
									}
								} else {
									if (!list2.isEmpty()) {
										searchlist.get(0).put(
												"caozuoshu",
												list2.get(0)
														.get(DataBaseHelper.MSL)
														.toString());
									} else {
										searchlist.get(0).put("caozuoshu", "0");
									}
								}
								setAdapter1();
								AddNumberDialog mAddNumberDialog = new AddNumberDialog(HP_choseActivity.this, op_type, searchlist.get(0), djid, dh, ckid, dacttype,numString,R.style.ButtonDialogStyle);
								mAddNumberDialog.setCanceledOnTouchOutside(false);
								Window win = mAddNumberDialog.getWindow();
								win.getDecorView().setPadding(0, 0, 0, 0);
								win.setGravity(Gravity.BOTTOM);
								WindowManager.LayoutParams lp = win.getAttributes();
								        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
								        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
								        win.setAttributes(lp);
								mAddNumberDialog.show();
							} else {
								Toast.makeText(HP_choseActivity.this,
										"本仓库不存在扫描的货品", Toast.LENGTH_SHORT).show();
							}
						}

					}
				}
			}
		}
	}
	
	class SearchHPAsyncTask extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			if(op_type == 1){
				String jsonString = WebserviceImport_more.GetHpInfo_search_1_0(
						"search", params[0], params[1], "1", "-1", mEditText1.getText()
								.toString().trim());
				return jsonString;
			}else{
				String jsonString = WebserviceImport_more.GetHpInfo_search_1_0(
						"search", params[0], params[1], "1", String.valueOf(ckid), mEditText1.getText()
								.toString().trim());
				return jsonString;
			}
			
			
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(result);
				mSemaphore.release();
				switch (jsonObject.getInt("Status")) {
				case 1:
					parseJSON(jsonObject);
					break;
				case 2:
					parseJSON(jsonObject);
					Toast.makeText(HP_choseActivity.this,
							jsonObject.getString("Message"), Toast.LENGTH_SHORT)
							.show();
					break;
				case -2:
					Toast.makeText(HP_choseActivity.this,
							jsonObject.getString("Message"), Toast.LENGTH_SHORT)
							.show();
					break;
				case -3:
					Toast.makeText(HP_choseActivity.this,
							jsonObject.getString("Message"), Toast.LENGTH_SHORT)
							.show();
					break;
				case -4:
					Toast.makeText(HP_choseActivity.this,
							jsonObject.getString("Message"), Toast.LENGTH_SHORT)
							.show();
					break;
				default:
					Toast.makeText(HP_choseActivity.this,
							jsonObject.getString("Message"), Toast.LENGTH_SHORT)
							.show();
					break;
				}
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			stopLoadface();
		}
		
	}
	
	public void parseJSON(JSONObject jsonObject) throws JSONException{
		JSONObject dataJSONObject = jsonObject.getJSONObject("Data");
		JSONArray dsJSONArray = dataJSONObject.getJSONArray("ds");
		int length = dsJSONArray.length();
		if (op_type == 0) {
			for (int i = 0; i < length; i++) {
				JSONObject myJSONObject = dsJSONArray.getJSONObject(i);
				Map<String, Object> map = new HashMap<String, Object>();
				for (int j = 0; j < 7; j++) {
					map.put(str4[j], myJSONObject.getString(strs[j]));
				}
				List<Map<String, Object>> list2 = dm_op.Gt_Moved(
						djid,map.get(DataBaseHelper.ID).toString(), str3);
				if (!list2.isEmpty()) {
					map.put("caozuoshu",list2.get(0).get(DataBaseHelper.ATKC).toString());
					map.put("shuying",list2.get(0).get(DataBaseHelper.MVType).toString());// 是否盘盈
				} else {
					map.put("caozuoshu", "无");// 表示该货品没有被盘点
				}
				searchlist.add(map);
			}
		}else{
			for (int i = 0; i < length; i++) {
				JSONObject myJSONObject = dsJSONArray.getJSONObject(i);
				Map<String, Object> map = new HashMap<String, Object>();
				for (int j = 0; j < 7; j++) {
					map.put(str4[j], myJSONObject.getString(strs[j]));
				}
				List<Map<String, Object>> list2 = dm_op.Gt_Moved(
						djid,map.get(DataBaseHelper.ID).toString(), str3);
				if (!list2.isEmpty()) {
					map.put("caozuoshu",list2.get(0).get(DataBaseHelper.MSL).toString());
				} else {
					map.put("caozuoshu", "0");
				}
				searchlist.add(map);
			}
		}
		HplistAdapter.setDataList(searchlist);
	}
	
	class SearchHPbyTMAsyncTask extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			if(op_type==1){
				String jsonString = WebserviceImport_more.GetHP_ByTM_2_0(params[0],-1,
						false);
				return jsonString;
			}else{
				String jsonString = WebserviceImport_more.GetHP_ByTM_2_0(params[0],ckid,
						false);
				return jsonString;
			}
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			proDialog.dismiss();
			try {
				JSONObject jsonObject = new JSONObject(result);
				switch (jsonObject.getInt("Status")) {
				case 1:
					JSONObject dataJSONObject = jsonObject
							.getJSONObject("Data");
					JSONArray dsJSONArray = dataJSONObject.getJSONArray("ds");
					int length = dsJSONArray.length();
					if (op_type == 0) {
						for (int i = 0; i < length; i++) {
							JSONObject myJSONObject = dsJSONArray.getJSONObject(i);
							Map<String, Object> map = new HashMap<String, Object>();
							for (int j = 0; j < 6; j++) {
								map.put(str4[j], myJSONObject.getString(strs[j]));
							}
							List<Map<String, Object>> list2 = dm_op.Gt_Moved(
									djid,map.get(DataBaseHelper.ID).toString(), str3);
							if (!list2.isEmpty()) {
								map.put("caozuoshu",list2.get(0).get(DataBaseHelper.ATKC).toString());
								map.put("shuying",list2.get(0).get(DataBaseHelper.MVType).toString());// 是否盘盈
							} else {
								map.put("caozuoshu", "无");// 表示该货品没有被盘点
							}
							searchlist.add(map);
						}
					}else{
						for (int i = 0; i < length; i++) {
							JSONObject myJSONObject = dsJSONArray.getJSONObject(i);
							Map<String, Object> map = new HashMap<String, Object>();
							for (int j = 0; j < 7; j++) {
								map.put(str4[j], myJSONObject.getString(strs[j]));
							}
							List<Map<String, Object>> list2 = dm_op.Gt_Moved(
									djid,map.get(DataBaseHelper.ID).toString(), str3);
							if (!list2.isEmpty()) {
								map.put("caozuoshu",list2.get(0).get(DataBaseHelper.MSL).toString());
							} else {
								map.put("caozuoshu", "0");
							}
							searchlist.add(map);
						}
					}
					HplistAdapter.setDataList(searchlist);
					AddNumberDialog mAddNumberDialog = new AddNumberDialog(HP_choseActivity.this, op_type, searchlist.get(0), djid, dh, ckid, dacttype,numString,R.style.ButtonDialogStyle);
					mAddNumberDialog.setCanceledOnTouchOutside(false);
					Window win = mAddNumberDialog.getWindow();
					win.getDecorView().setPadding(0, 0, 0, 0);
					win.setGravity(Gravity.BOTTOM);
					WindowManager.LayoutParams lp = win.getAttributes();
					lp.width = WindowManager.LayoutParams.MATCH_PARENT;
					lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
					win.setAttributes(lp);
					mAddNumberDialog.show();
					break;
				case -1:
					Toast.makeText(HP_choseActivity.this,
							jsonObject.getString("Message"), Toast.LENGTH_SHORT)
							.show();
					break;
				case -2:
					Toast.makeText(HP_choseActivity.this,
							jsonObject.getString("Message"), Toast.LENGTH_SHORT)
							.show();
					break;
				case -3:
					Toast.makeText(HP_choseActivity.this,
							jsonObject.getString("Message"), Toast.LENGTH_SHORT)
							.show();
					break;
				default:
					Toast.makeText(HP_choseActivity.this,
							jsonObject.getString("Message"), Toast.LENGTH_SHORT)
							.show();
					break;
				}
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}

	@Override
	public void onRefresh() {
		// TODO 自动生成的方法存根
		searchlist.clear();
		onLoad();
		if (mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1) == 1) {
			if (WebserviceImport.isOpenNetwork(this)) {
				searchlist.clear();
				try {
					mSemaphore.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				new SearchHPAsyncTask().execute("10", "0");
			} else {
				Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
			}
		} else if (mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1) == -1) {
			localLoadHP();
		}
	}

	@Override
	public void onLoadMore() {
		// TODO 自动生成的方法存根
		onLoad();
		if (mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1) == 1) {
			if (WebserviceImport.isOpenNetwork(this)) {

			try {
				mSemaphore.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				if (searchlist.isEmpty()) {
					new SearchHPAsyncTask().execute("10", "0");
				} else {
					new SearchHPAsyncTask().execute("10", searchlist
							.get(searchlist.size() - 1).get(DataBaseHelper.ID)
							.toString());
				}
			} else {
				Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
			}
		} else if (mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1) == -1) {
			localLoadHP();
		}

	}

	public void onLoad() {
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
		mXListView1.setRefreshTime(refreshDate);
	}
	
	/**
	 * 停止界面加载动画
	 * */
	public void stopLoadface() {
		mXListView1.stopRefresh();
		mXListView1.stopLoadMore();
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO 自动生成的方法存根
		s.toString().replace("'", "");
		searchlist.clear();
		HplistAdapter.notifyDataSetChanged();
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
