package com.guantang.cangkuonline.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.adapter.CkListViewAdapter;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.database.DataBaseMethod;
import com.guantang.cangkuonline.helper.CheckEditWatcher;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;
import com.guantang.cangkuonline.webservice.WebserviceImport;

public class HP_filterActivity extends Activity implements OnClickListener,
		OnCheckedChangeListener {
	private EditText   hpmc_EditText, ggxh_EditText,
			hpbm_EditText, hptm_EditText;
	private TextView ckEditText,lbEditText,sccsEditText;
	private ImageButton backImgBtn;
	private LinearLayout cklayout,lblayout,sccslayout;
	private Button resetBtn, confirmBtn;
	private RadioGroup existenceRadioGroup;
	private RadioButton existenceRadioButton, nonexistenceRadioButton;
	private TextView szck_textview;
	private List<Map<String, Object>> cklist;
	private String[] str15 = { "ID", "CKMC", "FZR", "TEL", "ADDR", "inact",
			"outact", "BZ" };
	private String[] str_ck = { DataBaseHelper.ID, DataBaseHelper.CKMC,
			DataBaseHelper.FZR, DataBaseHelper.TEL, DataBaseHelper.ADDR,
			DataBaseHelper.INACT, DataBaseHelper.OUTACT, DataBaseHelper.BZ };

	private List<Map<String, Object>> ckNameAndID = new ArrayList<Map<String, Object>>();
	/**
	 * 引入一个值为1的信号量，防止ckNameAndID装数据的时候就刷新界面
	 */
	private volatile Semaphore mSemaphore = new Semaphore(1);

	private ExecutorService cacheThreadPool = Executors.newCachedThreadPool();
	private PopupWindow mPopupWindow;
	private ListView mListView;
	private DataBaseMethod dm = new DataBaseMethod(this);
	private AlertDialog.Builder builder;
	private AlertDialog dialog;
	private int ScreenWidth, ScreenHeigth, y;
	private int ckid = -1;
	private String ckname = "";
	private String lbid = "", dwid = "", LBIndex = "";
	private SharedPreferences mSharedPreferences;

	private int hasckkc = 0; //是否有库存 有库存1，没有库存-1，0有无库存都显示

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 不去重新绘制界面
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		setContentView(R.layout.activity_hpfilter);
		mSharedPreferences = getSharedPreferences(
				ShareprefenceBean.SHAREPREFENCE_NAME, Context.MODE_PRIVATE);
		initcontent();
		init();
		this.setFinishOnTouchOutside(false);
	}

	public void initcontent() {
		szck_textview = (TextView) findViewById(R.id.szck_text);
		lbEditText = (TextView) findViewById(R.id.lbedit);
		ckEditText = (TextView) findViewById(R.id.ckedit);
		sccsEditText = (TextView) findViewById(R.id.sccs_edit);
		hpmc_EditText = (EditText) findViewById(R.id.hpmc_edit);
		ggxh_EditText = (EditText) findViewById(R.id.ggxh_edit);
		hpbm_EditText = (EditText) findViewById(R.id.hpbm_edit);
		hptm_EditText = (EditText) findViewById(R.id.hptm_edit);
		backImgBtn = (ImageButton) findViewById(R.id.back);
		resetBtn = (Button) findViewById(R.id.reset);
		confirmBtn = (Button) findViewById(R.id.confirm);
		existenceRadioGroup = (RadioGroup) findViewById(R.id.existenceRadioGroup);
		existenceRadioButton = (RadioButton) findViewById(R.id.existenceRadioButton);
		nonexistenceRadioButton = (RadioButton) findViewById(R.id.nonexistenceRadioButton);
		cklayout=(LinearLayout) findViewById(R.id.cklayout);
		lblayout=(LinearLayout) findViewById(R.id.lblayout);
		sccslayout = (LinearLayout) findViewById(R.id.sccslayout);

		CheckEditWatcher cked = new CheckEditWatcher();
		lbEditText.addTextChangedListener(cked);
		sccsEditText.addTextChangedListener(cked);
		hpmc_EditText.addTextChangedListener(cked);
		ggxh_EditText.addTextChangedListener(cked);
		hpbm_EditText.addTextChangedListener(cked);
		hptm_EditText.addTextChangedListener(cked);

		existenceRadioGroup.setOnCheckedChangeListener(this);
		backImgBtn.setOnClickListener(this);
		cklayout.setOnClickListener(this);
		lblayout.setOnClickListener(this);
		sccslayout.setOnClickListener(this);
		resetBtn.setOnClickListener(this);
		confirmBtn.setOnClickListener(this);
		cklist = new ArrayList<Map<String, Object>>();
	}

	public void init() {
		DisplayMetrics dms = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dms);
		ScreenWidth = dms.widthPixels;
		ScreenHeigth = dms.heightPixels;
		if (mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1) == 1) {
			if (WebserviceImport.isOpenNetwork(this)) {
				cacheThreadPool.execute(run);
			} else {
				List<Map<String, Object>> list1 = dm.getAllCK();
				if (list1.size() > 0) {
					Iterator<Map<String, Object>> it = list1.iterator();
					while (it.hasNext()) {
						Map<String, Object> map2 = new HashMap<String, Object>();
						Map<String, Object> map1 = it.next();
						map2.put("ckmc", map1.get(DataBaseHelper.CKMC)
								.toString());
						map2.put("ckid", map1.get(DataBaseHelper.ID).toString());
						ckNameAndID.add(map2);
					}
				}
			}
		} else if (mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1) == -1) {
			List<Map<String, Object>> list1 = dm.getAllCK();
			if (list1.size() > 0) {
				Iterator<Map<String, Object>> it = list1.iterator();
				while (it.hasNext()) {
					Map<String, Object> map2 = new HashMap<String, Object>();
					Map<String, Object> map1 = it.next();
					map2.put("ckmc", map1.get(DataBaseHelper.CKMC).toString());
					map2.put("ckid", map1.get(DataBaseHelper.ID).toString());
					ckNameAndID.add(map2);
				}
			}
		}
	}

	private Runnable run = new Runnable() {

		@Override
		public void run() {
			// TODO 自动生成的方法存根
			Message msg = Message.obtain();
			List<Map<String, Object>> ck_list;
			ck_list = WebserviceImport.GetCK(str_ck, str15, mSharedPreferences);
			msg.obj = ck_list;
			handler.sendMessage(msg);
		}
	};
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ckNameAndID.clear();
			try {
				mSemaphore.acquire();
				cklist = (List<Map<String, Object>>) msg.obj;
				if (cklist.size() > 0) {
					Iterator<Map<String, Object>> it = cklist.iterator();
					while (it.hasNext()) {
						Map<String, Object> map2 = new HashMap<String, Object>();
						Map<String, Object> map1 = it.next();
						map2.put("ckmc", map1.get(DataBaseHelper.CKMC)
								.toString());
						map2.put("ckid", map1.get(DataBaseHelper.ID).toString());
						ckNameAndID.add(map2);
					}
				}
				mSemaphore.release();
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}

			// if (ckmc.length < 7) {
			// y = (60 * ckmc.length);
			// } else {
			// y = 500;
			// }
			//
			// LayoutInflater inflater = LayoutInflater
			// .from(HP_filterActivity.this);
			// View view = inflater.inflate(R.layout.popupwindow_list, null);
			// mListView = (ListView) view.findViewById(R.id.popuplist);
			// ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(
			// HP_filterActivity.this, R.layout.popupwindow_list_textview,
			// ckmc);
			// mListView.setAdapter(adapter1);
			// mPopupWindow = new PopupWindow(view, ScreenWidth / 2, y);
			// // 这个是为了点击“返回Back”也能使其消失.
			// mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
			// // 使其聚焦
			// mPopupWindow.setFocusable(true);
			// // 设置允许在外点击消失
			// mPopupWindow.setOutsideTouchable(true);
			// // 刷新状态
			// mPopupWindow.update();
		};
	};

	public void setEmpty() {
		lbEditText.setText("");
		lbEditText.setHint(lbEditText.getHint());
		ckEditText.setText("");
		ckEditText.setHint(ckEditText.getHint());
		sccsEditText.setText("");
		sccsEditText.setHint(sccsEditText.getHint());
		hpmc_EditText.setText("");
		ggxh_EditText.setText("");
		hpbm_EditText.setText("");
		hptm_EditText.setText("");
		existenceRadioButton.setChecked(false);
		nonexistenceRadioButton.setChecked(false);
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.lblayout:
			intent.setClass(this, LB_ChoseActivity.class);
			startActivityForResult(intent, 1);
			break;
		case R.id.cklayout:
			// if (mPopupWindow.isShowing()) {
			// mPopupWindow.dismiss();
			// } else {
			// mPopupWindow.showAsDropDown(ckEditText, 0, 0);
			// }
			// mListView.setOnItemClickListener(new OnItemClickListener() {
			//
			// @Override
			// public void onItemClick(AdapterView<?> arg0, View arg1,
			// int arg2, long arg3) {
			// // TODO 自动生成的方法存根
			// String str = arg0.getAdapter().getItem(arg2).toString();
			// ckEditText.setText(str);
			// mPopupWindow.dismiss();
			// }
			// });
			if (ckNameAndID.isEmpty()) {
				Toast.makeText(this, "仓库信息还在获取，稍后再试！", Toast.LENGTH_SHORT)
						.show();
			} else {
				builder = new AlertDialog.Builder(this);
				LayoutInflater inflater = LayoutInflater.from(this);
				View view = inflater.inflate(R.layout.popupwindow_list, null);
				mListView = (ListView) view.findViewById(R.id.popuplist);
				CkListViewAdapter ckListViewAdapter = new CkListViewAdapter(this);
				try {
					mSemaphore.acquire();
					ckListViewAdapter.setData(ckNameAndID);
					mSemaphore.release();
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				mListView.setAdapter(ckListViewAdapter);
				mListView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO 自动生成的方法存根
						Map<String, Object> getMap = (Map<String, Object>) arg0
								.getAdapter().getItem(arg2);
						ckname = getMap.get("ckmc").toString();
						ckEditText.setText(ckname);
						ckid = Integer.parseInt(getMap.get("ckid").toString());
						dialog.dismiss();
					}
				});
				builder.setView(view);
				dialog = builder.create();
				dialog.show();
			}
			break;
		case R.id.sccslayout:
			intent.setClass(this, DwListActivity.class);
			startActivityForResult(intent, 2);
			break;
		case R.id.reset:
			setEmpty();
			break;
		case R.id.confirm:
			String sqlString = "";
			if (!lbid.equals("")) {
				sqlString = sqlString + "and LBIndex like '" + LBIndex + "%' ";
			}
			if (!sccsEditText.getText().toString().equals("")) {
				sqlString = sqlString + " and SCCS like '%"
						+ sccsEditText.getText().toString() + "%' ";
			}
			if (!hpmc_EditText.getText().toString().equals("")) {
				sqlString = sqlString + " and HPMC like '%"
						+ hpmc_EditText.getText().toString() + "%' ";
			}
			if (!ggxh_EditText.getText().toString().equals("")) {
				sqlString = sqlString + " and GGXH like '%"
						+ ggxh_EditText.getText().toString() + "%' ";
			}
			if (!hpbm_EditText.getText().toString().equals("")) {
				sqlString = sqlString + " and HPBM like '%"
						+ hpbm_EditText.getText().toString() + "%' ";
			}
			if (!hptm_EditText.getText().toString().equals("")) {
				if (mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1) == 1) {
					sqlString = sqlString + " and HPTBM like '%"
							+ hptm_EditText.getText().toString() + "%'";
				} else if (mSharedPreferences.getInt(
						ShareprefenceBean.IS_LOGIN, -1) == -1) {
					sqlString = sqlString + " and HPBM like '%"
							+ hptm_EditText.getText().toString() + "%'";
				}
			}
			intent.putExtra("sql", sqlString);
			intent.putExtra("ckid", ckid);
			intent.putExtra("ckname", ckname);
			intent.putExtra("hasckkc", hasckkc);
			setResult(1, intent);
			finish();
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO 自动生成的方法存根
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			if (resultCode == 1) {
				lbEditText.setText(data.getStringExtra("lb"));
				lbid = data.getStringExtra("lbid");
				LBIndex = data.getStringExtra("index");
			}
		} else if (requestCode == 2) {
			if (resultCode == 1) {
				sccsEditText.setText(data.getStringExtra("dw"));
				dwid = data.getStringExtra("dwid");
			}
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO 自动生成的方法存根
		switch (checkedId) {
		case R.id.existenceRadioButton:
			hasckkc = 1;
			break;
		case R.id.nonexistenceRadioButton:
			hasckkc = -1;
			break;
		default:
			hasckkc = 0;
			break;
		}
	}

}
