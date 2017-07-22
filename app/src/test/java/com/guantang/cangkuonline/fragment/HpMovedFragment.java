package com.guantang.cangkuonline.fragment;

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

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.TagFlowLayout.FlowLayout;
import com.guantang.cangkuonline.TagFlowLayout.TagAdapter;
import com.guantang.cangkuonline.TagFlowLayout.TagFlowLayout;
import com.guantang.cangkuonline.XListView.XListView;
import com.guantang.cangkuonline.XListView.XListView.IXListViewListener;
import com.guantang.cangkuonline.activity.HpInformationActivity.TransmitJldwAndLb;
import com.guantang.cangkuonline.activity.Moved_hpActivity;
import com.guantang.cangkuonline.adapter.Moved_hpAdapter;
import com.guantang.cangkuonline.application.MyApplication;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.database.DataBaseOperateMethod;
import com.guantang.cangkuonline.helper.DecimalsHelper;
import com.guantang.cangkuonline.helper.RightsHelper;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;
import com.guantang.cangkuonline.webservice.WebserviceImport;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HpMovedFragment extends Fragment implements OnClickListener,OnScrollListener,IXListViewListener,TransmitJldwAndLb{
	
	private ImageView searchImgBtn;
	private TextView fromtimeBtn, totimeBtn;
	private XListView mXListView;
	private LinearLayout cw_layout;
	private TextView znumTxtView, zjeTxtView;
	private String hpid="",hpmc="",hpbm="",ggxh="",jldw="",lb="";
	private SharedPreferences mSharedPreferences;
	private SimpleDateFormat formatter;
	private Moved_hpAdapter mMoved_hpAdapter;
	private List<Map<String, Object>> mlist = new ArrayList<Map<String, Object>>();
	private DataBaseOperateMethod dm_op;
	private String[] str = { "hpd_id", DataBaseHelper.MVDH,
			DataBaseHelper.MVDT, DataBaseHelper.MVDirect,
			DataBaseHelper.MVType, DataBaseHelper.MSL, DataBaseHelper.DJ,
			DataBaseHelper.ZJ, DataBaseHelper.DWName, DataBaseHelper.JBR,
			DataBaseHelper.CKMC, DataBaseHelper.DepName };
	private String[] str3 = { "hpd_id", "mvddh", "mvddt", "mddirect",
			"dactType", "msl", "dj", "zj", "dwName", "jbr", "ckName", "depName" };
	private String[] str4 = { DataBaseHelper.MVDH, DataBaseHelper.MVDT,
			DataBaseHelper.MVDirect, DataBaseHelper.MVType, DataBaseHelper.MSL,
			DataBaseHelper.DWName, DataBaseHelper.JBR, DataBaseHelper.CKMC };
	private String[] str5 = { DataBaseHelper.MVDH, DataBaseHelper.MVDT,
			DataBaseHelper.MVDirect, DataBaseHelper.MVType, DataBaseHelper.MSL,
			DataBaseHelper.DJ, DataBaseHelper.ZJ, DataBaseHelper.DWName,
			DataBaseHelper.JBR, DataBaseHelper.CKMC, DataBaseHelper.DepName };
	/**
	 * 添加一个信号量，防止列表界面还没刷新数据就变更了
	 * */
	private volatile Semaphore mSemaphore = new Semaphore(1);
	private double ruku_num = 0, chuku_num = 0;
	private int where = 0;
	private ExecutorService cacheThreadPool = Executors.newCachedThreadPool();
	private Calendar calendar = Calendar.getInstance();
	private Context context;
	
	public static HpMovedFragment getInstance(String hpid,String hpmc,String hpbm,String ggxh){
		HpMovedFragment hmf = new HpMovedFragment();
		Bundle bundle = new Bundle();
		bundle.putString("hpid", hpid);
		bundle.putString("hpmc", hpmc);
		bundle.putString("hpbm", hpbm);
		bundle.putString("ggxh", ggxh);
		hmf.setArguments(bundle);
		return hmf;
	}
	
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
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		View v = inflater.inflate(R.layout.hpmovedfragment, null);
		return v;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onActivityCreated(savedInstanceState);
		hpid = getArguments().getString("hpid");
		hpmc = getArguments().getString("hpmc");
		hpbm = getArguments().getString("hpbm");
		ggxh = getArguments().getString("ggxh");
		mSharedPreferences = MyApplication.getInstance().getSharedPreferences();
		dm_op = new DataBaseOperateMethod(context);
		initView();
		init();
	}
	
	public void initView(){
		searchImgBtn = (ImageView) getView().findViewById(R.id.search);
		fromtimeBtn = (TextView) getView().findViewById(R.id.dt1);
		totimeBtn = (TextView) getView().findViewById(R.id.dt2);
		mXListView = (XListView) getView().findViewById(R.id.list);
		cw_layout = (LinearLayout) getView().findViewById(R.id.cw);
		znumTxtView = (TextView) getView().findViewById(R.id.znum);
		zjeTxtView = (TextView) getView().findViewById(R.id.zje);
		
		searchImgBtn.setOnClickListener(this);
		fromtimeBtn.setOnClickListener(this);
		totimeBtn.setOnClickListener(this);
		mXListView.setOnScrollListener(this);
		if (mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1) == 1) {
			mXListView.setPullLoadEnable(true);// 设置可以加载更多
			mXListView.setPullRefreshEnable(true);// 设置可以刷新
		} else if (mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1) == -1) {
			mXListView.setPullLoadEnable(false);
			mXListView.setPullRefreshEnable(false);
		}
		mXListView.setXListViewListener(this);

		if (RightsHelper
				.isHaveRight(RightsHelper.system_cw, mSharedPreferences)) {
			cw_layout.setVisibility(View.VISIBLE);
		} else {
			cw_layout.setVisibility(View.GONE);
		}
		
	}
	
	public void init(){
		formatter = new SimpleDateFormat("yyyy-MM-dd");
		String time = formatter.format(new Date(System.currentTimeMillis()));
		fromtimeBtn.setText(time);
		totimeBtn.setText(time);
		mMoved_hpAdapter = new Moved_hpAdapter(context, mSharedPreferences, hpmc,
				hpbm, ggxh, jldw, lb);
		mXListView.setAdapter(mMoved_hpAdapter);
		if (mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1) == 1) {
			if (WebserviceImport.isOpenNetwork(context)) {
				cacheThreadPool.execute(downloadRun);
			} else {
				Toast.makeText(context, "网络未连接，请检查网络连接", Toast.LENGTH_SHORT)
						.show();
			}
		} else {
			mlist = dm_op.GetckrkMoved_byIdTime(hpid, str5, fromtimeBtn
					.getText().toString(), totimeBtn.getText().toString());
			for (int i = 0; i < mlist.size(); i++) {
				mlist.get(i).put("click_Color", (Boolean) false); // false
				// 没被点击的时候不设置颜色，true
				// 设置颜色
			}
			setAdapter();
		}
	}

	public void setAdapter() {
		mMoved_hpAdapter.setData(mlist);
	}
	
	@Override
	public void execute(String jldw, String lb) {
		// TODO 自动生成的方法存根
		if(mMoved_hpAdapter!=null){
			this.jldw = jldw;
			this.lb = lb;
			mMoved_hpAdapter.setJldwAndLb(jldw, lb);
		}else{
			this.jldw = jldw;
			this.lb = lb;
		}
	}
	
	private Runnable downloadRun = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				mSemaphore.acquire();
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			Message msg = new Message();
			String time1 = fromtimeBtn.getText().toString();
			String time2 = totimeBtn.getText().toString();
			if (mlist == null || mlist.size() == 0) {
				mlist = WebserviceImport.GetMoved_DJ_ByHpid(20, "0", hpid,
						time1, time2, 0, -1, str, str3, mSharedPreferences);
				ruku_num = WebserviceImport.Get_Moved_hp_znum(hpid, time1,
						time2, 1, -1, mSharedPreferences);
				chuku_num = WebserviceImport.Get_Moved_hp_znum(hpid, time1,
						time2, 2, -1, mSharedPreferences);
				msg.what = 1;
				for (int i = 0; i < mlist.size(); i++) {
					mlist.get(i).put("click_Color", (Boolean) false); // false
					// 没被点击的时候不设置颜色，true
					// 设置颜色
				}
			} else {
				int id_web = WebserviceImport.GetMaxID_Moved_DJ_ByHpid(hpid,
						time1, time2, 0, -1, mSharedPreferences);
				if (id_web > 0) {
					if (String.valueOf(id_web).equals(
							(String) mlist.get(mlist.size() - 1).get("hpd_id"))) {
						msg.what = -3;
					} else {
						List<Map<String,Object>> ls2 = WebserviceImport.GetMoved_DJ_ByHpid(20,
								(String) mlist.get(mlist.size() - 1).get("hpd_id"), hpid, time1, time2, 0, -1,
								str, str3, mSharedPreferences);
						if (ls2 == null) {
							msg.what = -1;
						} else {
							mlist.addAll(ls2);
							msg.what = 2;
						}
					}
					for (int i = 0; i < mlist.size(); i++) {
						mlist.get(i).put("click_Color", (Boolean) false); // false
						// 没被点击的时候不设置颜色，true
						// 设置颜色
					}
				} else {
					msg.what = -2;
				}
			}
			mHandler.sendMessage(msg);
		}

	};
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 2:
				if (mlist != null) {
					if (mlist.size() < 11) {
						mXListView.setPullLoadEnable(false);
					} else {
						mXListView.setPullLoadEnable(true);
					}
					setAdapter();
					mXListView.setSelection(where);
					// setTextView();
				}
				break;
			case 1:
				if (mlist != null) {
					if (mlist.size() < 11) {
						mXListView.setPullLoadEnable(false);
					} else {
						mXListView.setPullLoadEnable(true);
					}
					setAdapter();
					mXListView.setSelection(0);
					// setTextView();
					if (ruku_num < -1 || chuku_num < -1) {
						znumTxtView.setText("获取数据有误");
						zjeTxtView.setText("获取数据有误");
					} else {
						if (ruku_num == -1) {
							ruku_num = 0;
						}
						if (chuku_num == -1) {
							chuku_num = 0;
						}
						znumTxtView.setText(DecimalsHelper.Transfloat(ruku_num,
								MyApplication.getInstance().getNumPoint()));
						zjeTxtView.setText(DecimalsHelper.Transfloat(chuku_num,
								MyApplication.getInstance().getJePoint()));
					}
				}
				break;
			case -1:
				Toast.makeText(context, "加载失败",
						Toast.LENGTH_SHORT).show();
				break;
			case -2:
				Toast.makeText(context, "获取数据有误",
						Toast.LENGTH_SHORT).show();
				break;
			case -3:
				Toast.makeText(context, "已到最后一项",
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
			mSemaphore.release();
		}
	};
	
	@Override
	public void onRefresh() {
		// TODO 自动生成的方法存根
		if (WebserviceImport.isOpenNetwork(context)) {
			mlist.clear();

			cacheThreadPool.execute(downloadRun);
		} else {
			Toast.makeText(context, "网络未连接，请检查网络连接", Toast.LENGTH_SHORT).show();
		}
		onLoad();
	}

	@Override
	public void onLoadMore() {
		// TODO 自动生成的方法存根
		if (WebserviceImport.isOpenNetwork(context)) {

			cacheThreadPool.execute(downloadRun);
		} else {
			Toast.makeText(context, "网络未连接，请检查网络连接", Toast.LENGTH_SHORT).show();
		}
		onLoad();
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO 自动生成的方法存根
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
			where = mXListView.getFirstVisiblePosition();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.search:
			mlist.clear();
			if (mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1) == 1) {
				if (WebserviceImport.isOpenNetwork(context)) {
					new Thread(downloadRun).start();
				} else {
					Toast.makeText(context, "网络未连接，请检查网络连接", Toast.LENGTH_SHORT)
							.show();
				}
			} else {
				mlist = dm_op.GetckrkMoved_byIdTime(hpid, str5, fromtimeBtn
						.getText().toString(), totimeBtn.getText().toString());
				for (int i = 0; i < mlist.size(); i++) {
					mlist.get(i).put("click_Color", (Boolean) false); // false
					// 没被点击的时候不设置颜色，true
					// 设置颜色
				}
				setAdapter();
			}
			break;
		case R.id.dt1:
			DatePickerDialog dateDialog = new DatePickerDialog(context,
					new OnDateSetListener() {
						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							// TODO 自动生成的方法存根
							fromtimeBtn.setText(year
									+ "-"
									+ new DecimalFormat("00")
											.format(monthOfYear + 1)
									+ "-"
									+ new DecimalFormat("00")
											.format(dayOfMonth));
						}
					}, calendar.get(Calendar.YEAR),
					calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH));
			dateDialog.show();
			break;
		case R.id.dt2:
			DatePickerDialog dateDialog2 = new DatePickerDialog(context,
					new OnDateSetListener() {
						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							// TODO 自动生成的方法存根
							totimeBtn.setText(year
									+ "-"
									+ new DecimalFormat("00")
											.format(monthOfYear + 1)
									+ "-"
									+ new DecimalFormat("00")
											.format(dayOfMonth));
						}
					}, calendar.get(Calendar.YEAR),
					calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH));
			dateDialog2.show();
			break;
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
		mXListView.stopRefresh();
		mXListView.stopLoadMore();
		mXListView.setRefreshTime(refreshDate);
	}
	
}
