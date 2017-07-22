package com.guantang.cangkuonline.activity;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.TagFlowLayout.FlowLayout;
import com.guantang.cangkuonline.TagFlowLayout.TagAdapter;
import com.guantang.cangkuonline.TagFlowLayout.TagFlowLayout;
import com.guantang.cangkuonline.XListView.XListView;
import com.guantang.cangkuonline.XListView.XListView.IXListViewListener;
import com.guantang.cangkuonline.adapter.TablelistAdapter1;
import com.guantang.cangkuonline.adapter.TablelistAdapter2;
import com.guantang.cangkuonline.application.MyApplication;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.helper.DecimalsHelper;
import com.guantang.cangkuonline.helper.RightsHelper;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;
import com.guantang.cangkuonline.webservice.WebserviceImport;

public class Moved_DJ extends Activity implements OnClickListener,
		OnScrollListener, IXListViewListener {
	private XListView mListView;
	private TextView znum_textview, zje_textview, title_textview, text2, filterTxtView;
	private ImageButton backImgBtn;
	private LinearLayout cwLayout;
	private TextView starttimeTxtView,endtimeTxtView,cangkuTxtView,danweiTxtView,danhaoTxtView,danjuTxtView;
	private SimpleDateFormat formatter;
	private String time1, time2;
	private List<Map<String, Object>> ls = new ArrayList<Map<String, Object>>();
	// private ProgressDialog pro_Dialog;
	private String[] str = { DataBaseHelper.MVDH, DataBaseHelper.MVType,
			DataBaseHelper.MVDDATE, DataBaseHelper.MSL, DataBaseHelper.DJ,
			DataBaseHelper.ZJ, DataBaseHelper.DepName, DataBaseHelper.DWName,
			DataBaseHelper.JBR, DataBaseHelper.HPMC, DataBaseHelper.HPBM,
			DataBaseHelper.GGXH, DataBaseHelper.JLDW, DataBaseHelper.LBS,
			DataBaseHelper.AZKC, DataBaseHelper.CKMC };
	private String[] str4 = { "hpd_id", "mvddh", "dactType", "mvddt", "sl",
			"dj", "zj", "depName", "dwName", "jbr", "HPMC", "HPBM", "GGXH",
			"JLDW", "HPLX", "Azkc", "ckname", "mbz" };
	private String[] str5 = { "hpd_id", DataBaseHelper.MVDH,
			DataBaseHelper.MVType, DataBaseHelper.MVDDATE, DataBaseHelper.MSL,
			DataBaseHelper.DJ, DataBaseHelper.ZJ, DataBaseHelper.DepName,
			DataBaseHelper.DWName, DataBaseHelper.JBR, DataBaseHelper.HPMC,
			DataBaseHelper.HPBM, DataBaseHelper.GGXH, DataBaseHelper.JLDW,
			DataBaseHelper.LBS, DataBaseHelper.AZKC, DataBaseHelper.CKMC,
			DataBaseHelper.BZ };
	private String[] str6 = { DataBaseHelper.MVDH, DataBaseHelper.MVType,
			DataBaseHelper.MVDDATE, DataBaseHelper.MSL, DataBaseHelper.DepName,
			DataBaseHelper.DWName, DataBaseHelper.JBR, DataBaseHelper.HPMC,
			DataBaseHelper.HPBM, DataBaseHelper.GGXH, DataBaseHelper.JLDW,
			DataBaseHelper.LBS, DataBaseHelper.AZKC, DataBaseHelper.CKMC };
	private String SQL = "";
	private String ckmc = "不限", dwmc = "不限", dh = "不限", djnameString = "";
	private int ckid = -1;
	private double ruku_num, chuku_num;
	private SimpleAdapter listItemAdapter;
	private TablelistAdapter1 tableAdapter1;
	private TablelistAdapter2 tableAdapter2;
	private int where = 0, visibleItemCount = -1;
	private ExecutorService cacheThreadPool = Executors.newCachedThreadPool();
	private int ListItem = -1;// 获取ListView 里面某一项的位置
	private Boolean color_flag = true;// true 设置ListView 里面某一项为亮色
	private SharedPreferences mSharedPreferences;

	/**
	 * 添加一个信号量，防止列表界面还没刷新数据就变更了
	 * */
	private volatile Semaphore mSemaphore = new Semaphore(1);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.moved_dj);
		mSharedPreferences = getSharedPreferences(
				ShareprefenceBean.SHAREPREFENCE_NAME, Context.MODE_PRIVATE);
		initControl();
		init();
	}

	public void initControl() {
		mListView = (XListView) findViewById(R.id.list);
		filterTxtView = (TextView) findViewById(R.id.search);
		backImgBtn = (ImageButton) findViewById(R.id.back);
		text2 = (TextView) findViewById(R.id.text2);
		znum_textview = (TextView) findViewById(R.id.znum);
		zje_textview = (TextView) findViewById(R.id.zje);
		title_textview = (TextView) findViewById(R.id.title);
		cwLayout = (LinearLayout) findViewById(R.id.cw);
		starttimeTxtView = (TextView) findViewById(R.id.starttimeTxtView);
		endtimeTxtView = (TextView) findViewById(R.id.endtimeTxtView);
		cangkuTxtView = (TextView) findViewById(R.id.cangkuTxtView);
		danweiTxtView = (TextView) findViewById(R.id.danweiTxtView);
		danhaoTxtView = (TextView) findViewById(R.id.danhaoTxtView);
		danjuTxtView = (TextView) findViewById(R.id.danjuTxtView);
		backImgBtn.setOnClickListener(this);
		filterTxtView.setOnClickListener(this);
		mListView.setOnScrollListener(this);
		mListView.setPullLoadEnable(true);// 设置可以加载更多
		mListView.setPullRefreshEnable(true);// 设置可以刷新
		mListView.setXListViewListener(this);

		if (RightsHelper.isHaveRight(RightsHelper.system_cw, mSharedPreferences)) {
			tableAdapter1 = new TablelistAdapter1(this);
			mListView.setAdapter(tableAdapter1);
		} else {
			tableAdapter2 = new TablelistAdapter2(this);
			mListView.setAdapter(tableAdapter2);
		}

		if (RightsHelper
				.isHaveRight(RightsHelper.system_cw, mSharedPreferences)) {
			cwLayout.setVisibility(View.VISIBLE);
			zje_textview.setVisibility(View.VISIBLE);
			text2.setVisibility(View.VISIBLE);
		} else {
			cwLayout.setVisibility(View.GONE);
			zje_textview.setVisibility(View.GONE);
			text2.setVisibility(View.GONE);
		}
	}

	public void init() {
		String datatime;
		formatter = new SimpleDateFormat("yyyy-MM-dd");
		datatime = formatter.format(new Date(System.currentTimeMillis()));
		time1 = datatime;
		time2 = datatime;
		
		starttimeTxtView.setText(time1);
		endtimeTxtView.setText(time2);
		cangkuTxtView.setText("不限");
		danweiTxtView.setText("不限");
		danhaoTxtView.setText("不限");
		danjuTxtView.setText("不限");
		
		if (mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1) == 1) {
			if (WebserviceImport.isOpenNetwork(this)) {
				// pro_Dialog = ProgressDialog.show(this, null, "正在加载数据……");
//				try {
//					mSemaphore.acquire();
//				} catch (InterruptedException e) {
//					// TODO 自动生成的 catch 块
//					e.printStackTrace();
//				}
				cacheThreadPool.execute(downloadRun);
			} else {
				Toast.makeText(this, "网络未连接，请检查网络连接", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	public void setAdapter() {
		if (RightsHelper
				.isHaveRight(RightsHelper.system_cw, mSharedPreferences)) {
			tableAdapter1.setData(ls);
		} else {
			tableAdapter2.setData(ls);
		}
	}

	private Runnable downloadRun = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message msg = Message.obtain();
			synchronized (this) {
				if (ls.isEmpty()) {
					List<Map<String, Object>> lss = WebserviceImport.GetMoved_DJ(
							20, "0", time1, time2, 0, ckid, str5, str4, SQL,
							mSharedPreferences);
					ruku_num = WebserviceImport.Get_Moved_sl_znum(time1, time2, 1,
							ckid, SQL, mSharedPreferences);
					chuku_num = WebserviceImport.Get_Moved_zj_znum(time1, time2, 1,
							ckid, SQL, mSharedPreferences);
					for (int i = 0; i < lss.size(); i++) {
						lss.get(i).put("click_Color", (Boolean) false); // false
																		// 没被点击的时候不设置颜色，true
																		// 设置颜色
					}
					msg.obj = lss;
					msg.what = 1;
				} else {
					int id_web = WebserviceImport.GetMaxID_Moved_DJ(time1, time2,
							1, ckid, SQL, mSharedPreferences);
					if (id_web > 0) {
						if (String.valueOf(id_web).equals(
								(String) ls.get(ls.size() - 1).get("hpd_id"))) {
							msg.what = -3;
						} else {
							List<Map<String, Object>> ls2 = new ArrayList<Map<String, Object>>();
							ls2 = WebserviceImport.GetMoved_DJ(20,
									(String) ls.get(ls.size() - 1).get("hpd_id"),
									time1, time2, 0, ckid, str5, str4, SQL,
									mSharedPreferences);
							if (ls2 == null) {
								msg.what = -1;
							} else {
								for (int i = 0; i < ls2.size(); i++) {
									ls2.get(i).put("click_Color", (Boolean) false);// false
																					// 没被点击的时候不设置颜色，true
																					// 设置颜色
								}
								msg.obj = ls2;
								msg.what = 2;
							}
						}
					} else {
						msg.what = -2;
					}
				}
			}
			msg.setTarget(mHandler);
			mHandler.sendMessage(msg);
		}
	};

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			// pro_Dialog.dismiss();
			switch (msg.what) {
			case 1:
				ls.addAll((List<Map<String, Object>>) msg.obj);
				if (ls != null) {
					if (ls.size() < 11) {
						mListView.setPullLoadEnable(false);
					} else {
						mListView.setPullLoadEnable(true);
					}
					setAdapter();
					mListView.setSelection(0);
					if (ruku_num < -1 || chuku_num < -1) {
						znum_textview.setText("获取数据有误");
						zje_textview.setText("获取数据有误");
					} else {
						if (ruku_num == -1) {
							ruku_num = 0;
						}
						if (chuku_num == -1) {
							chuku_num = 0;
						}
						znum_textview.setText(DecimalsHelper.Transfloat(
								ruku_num, MyApplication.getInstance()
										.getNumPoint()));
						zje_textview.setText(DecimalsHelper.Transfloat(
								chuku_num, MyApplication.getInstance()
										.getJePoint()));
					}
				}
				break;
			case 2:
				ls.addAll((List<Map<String, Object>>) msg.obj);
				if (ls != null) {
					if (ls.size() < 11) {
						mListView.setPullLoadEnable(false);
					} else {
						mListView.setPullLoadEnable(true);
					}
					setAdapter();
					mListView.setSelection(where);
					// setTextView();
				}
				break;
			case -1:
				Toast.makeText(Moved_DJ.this, "加载失败", Toast.LENGTH_SHORT)
						.show();
				break;
			case -2:
				Toast.makeText(Moved_DJ.this, "获取数据有误", Toast.LENGTH_SHORT)
						.show();
				break;
			case -3:
				Toast.makeText(Moved_DJ.this, "已到最后一项", Toast.LENGTH_SHORT)
						.show();
				break;
			}
			mSemaphore.release();
		};
	};
	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO 自动生成的方法存根

		this.visibleItemCount = visibleItemCount;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO 自动生成的方法存根
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
			where = mListView.getFirstVisiblePosition();

		}
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.search:
			intent.setClass(this, MX_filterActivity.class);
			startActivityForResult(intent, 1);
			break;
		}
	}

	@Override
	public void onRefresh() {
		// TODO 自动生成的方法存根
		if (WebserviceImport.isOpenNetwork(this)) {
			ls.clear();
			// pro_Dialog = ProgressDialog.show(this, null, "正在加载数据……");
			try {
				mSemaphore.acquire();
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			cacheThreadPool.execute(downloadRun);
		} else {
			Toast.makeText(this, "网络未连接，请检查网络连接", Toast.LENGTH_SHORT).show();
		}
		onLoad();
	}

	@Override
	public void onLoadMore() {
		// TODO 自动生成的方法存根
		if (WebserviceImport.isOpenNetwork(this)) {
			// pro_Dialog = ProgressDialog.show(this, null, "正在加载数据……");
			try {
				mSemaphore.acquire();
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			cacheThreadPool.execute(downloadRun);
		} else {
			Toast.makeText(this, "网络未连接，请检查网络连接", Toast.LENGTH_SHORT).show();
		}
		mListView.stopLoadMore();
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
		mListView.stopRefresh();
		mListView.setRefreshTime(refreshDate);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO 自动生成的方法存根
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			if (resultCode == 1) {
				time1 = data.getStringExtra("fromtime");
				time2 = data.getStringExtra("totime");
				ckid = data.getIntExtra("ckid", -1);
				ckmc = data.getStringExtra("ckmc");
				djnameString = data.getStringExtra("djnameString");
				dwmc = data.getStringExtra("dwmc");
				dh = data.getStringExtra("dh");
				SQL = data.getStringExtra("SQL");
				if (mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1) == 1) {
					if (WebserviceImport.isOpenNetwork(this)) {
						// pro_Dialog = ProgressDialog.show(this, null,
						// "正在加载数据……");
						ls.clear();
//						try {
//							mSemaphore.acquire();
//						} catch (InterruptedException e) {
//							// TODO 自动生成的 catch 块
//							e.printStackTrace();
//						}
						cacheThreadPool.execute(downloadRun);
					} else {
						Toast.makeText(this, "网络未连接，请检查网络连接",
								Toast.LENGTH_SHORT).show();
					}
				} else if (mSharedPreferences.getInt(
						ShareprefenceBean.IS_LOGIN, -1) == -1) {
					Toast.makeText(Moved_DJ.this, "请先登录账号", Toast.LENGTH_SHORT)
							.show();
				}
				String danjutype = djnameString.replace("\t", "、");
				
				starttimeTxtView.setText(time1);
				endtimeTxtView.setText(time2);
				cangkuTxtView.setText(ckmc);
				danweiTxtView.setText(dwmc);
				danhaoTxtView.setText(dh);
				if(!danjutype.isEmpty()){
					danjuTxtView.setText(danjutype);
				}else{
					danjuTxtView.setText("不限");
				}
			}
		}
	}

}
