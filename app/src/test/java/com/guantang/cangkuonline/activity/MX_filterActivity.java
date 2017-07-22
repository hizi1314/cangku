package com.guantang.cangkuonline.activity;


import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.adapter.CkListViewAdapter;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.database.DataBaseMethod;
import com.guantang.cangkuonline.helper.CheckEditWatcher;
import com.guantang.cangkuonline.helper.EditHelper;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;
import com.guantang.cangkuonline.webservice.WebserviceImport;

public class MX_filterActivity extends Activity implements OnClickListener,OnCheckedChangeListener{
	private Button f_Btn,t_Btn,reset_Btn,confirm_Btn;
	private CheckBox pandian_Rb,ru_Rb,chu_Rb;
	private EditText dh_EditText;
	private TextView ck_EditText,dw_EditText;
	private ImageButton backImageButton;
	private LinearLayout cklayout,dwlayout;
	private SimpleDateFormat formatter;
	private PopupWindow mPopupWindow;
	private List<Map<String, Object>> cklist;
	private String[] str15 = { "ID", "CKMC", "FZR", "TEL", "ADDR", "inact",
			"outact", "BZ" };
	private String[] str_ck = { DataBaseHelper.ID, DataBaseHelper.CKMC,
			DataBaseHelper.FZR, DataBaseHelper.TEL, DataBaseHelper.ADDR,
			DataBaseHelper.INACT, DataBaseHelper.OUTACT, DataBaseHelper.BZ };
	private List<Map<String, Object>> ckNameAndID = new ArrayList<Map<String,Object>>();
	/**
	 * 引入一个值为1的信号量，防止ckNameAndID装数据的时候就刷新界面
	 */
	private volatile Semaphore mSemaphore = new Semaphore(1);
	private ExecutorService cacheThreadPool = Executors.newCachedThreadPool();
	private int ScreenWidth, ScreenHeigth, y;
	private DataBaseMethod dm = new DataBaseMethod(this);
	private ListView mListView;
	private Calendar calendar;
	private AlertDialog.Builder builder;
	private AlertDialog dialog;
	private String SQL="",djnameString="";
	private int ckid=-1;
	private Boolean ruku_flag=false;
	private Boolean chuku_flag=false;
	private Boolean pandian_flag=false;
	private SharedPreferences mSharedPreferences;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		// 不去重新绘制界面
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		setContentView(R.layout.activity_mx_filter);
		mSharedPreferences = getSharedPreferences(ShareprefenceBean.SHAREPREFENCE_NAME, Context.MODE_PRIVATE);
		initContorl();
		init();
		this.setFinishOnTouchOutside(false);
	}
	public void initContorl(){
		f_Btn=(Button) findViewById(R.id.dt1);
		t_Btn=(Button) findViewById(R.id.dt2);
		reset_Btn=(Button) findViewById(R.id.reset);
		confirm_Btn=(Button) findViewById(R.id.confirm);
		ck_EditText=(TextView) findViewById(R.id.ckedit);
		dw_EditText=(TextView) findViewById(R.id.dw_edit);
		dh_EditText=(EditText) findViewById(R.id.dh_edit);
		cklayout=(LinearLayout) findViewById(R.id.cklayout);
		dwlayout=(LinearLayout) findViewById(R.id.dwlayout);
		pandian_Rb=(CheckBox) findViewById(R.id.ck1);
		ru_Rb=(CheckBox) findViewById(R.id.ck2);
		chu_Rb=(CheckBox) findViewById(R.id.ck3);
		backImageButton = (ImageButton) findViewById(R.id.back);
		
		CheckEditWatcher cked = new CheckEditWatcher();
		dh_EditText.addTextChangedListener(cked);
		
		backImageButton.setOnClickListener(this);
		cklayout.setOnClickListener(this);
		dwlayout.setOnClickListener(this);
		f_Btn.setOnClickListener(this);
		t_Btn.setOnClickListener(this);
		reset_Btn.setOnClickListener(this);
		confirm_Btn.setOnClickListener(this);
		pandian_Rb.setOnCheckedChangeListener(this);
		ru_Rb.setOnCheckedChangeListener(this);
		chu_Rb.setOnCheckedChangeListener(this);
		
		
		cklist = new ArrayList<Map<String, Object>>();
	}
	public void init(){
		String datetime;
		formatter=new SimpleDateFormat("yyyy-MM-dd");
		datetime=formatter.format(new Date(System.currentTimeMillis()));
		f_Btn.setText(datetime);
		t_Btn.setText(datetime);
		DisplayMetrics dms = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dms);
		ScreenWidth = dms.widthPixels;
		ScreenHeigth = dms.heightPixels;
		if(mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1)==1){
			if(WebserviceImport.isOpenNetwork(this)){
				cacheThreadPool.execute(run);
			}else{
				List<Map<String,Object>> list1 =dm.getAllCK();
				if(!list1.isEmpty()){
					ckNameAndID.clear();
					Iterator<Map<String, Object>> it=list1.iterator();
					while(it.hasNext()){
						Map<String, Object> map2 = new HashMap<String, Object>();
						Map<String, Object> map1 = it.next();
						map2.put("ckmc", map1.get(DataBaseHelper.CKMC).toString());
						map2.put("ckid", map1.get(DataBaseHelper.ID).toString());
						ckNameAndID.add(map2);
					}
				}
			}
		}else if(mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1)==-1){
			List<Map<String,Object>> list1 =dm.getAllCK();
			if(!list1.isEmpty()){
				ckNameAndID.clear();
				Iterator<Map<String, Object>> it=list1.iterator();
				while(it.hasNext()){
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
			ck_list = WebserviceImport.GetCK(str_ck, str15,mSharedPreferences);
			msg.obj = ck_list;
			handler.sendMessage(msg);
		}
	};
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			try {
				mSemaphore.acquire();
				ckNameAndID.clear();
				cklist = (List<Map<String, Object>>) msg.obj;
				if(cklist.size()>0){
					Iterator<Map<String, Object>> it=cklist.iterator();
					while(it.hasNext()){
						Map<String, Object> map2 = new HashMap<String, Object>();
						Map<String, Object> map1 = it.next();
						map2.put("ckmc", map1.get(DataBaseHelper.CKMC).toString());
						map2.put("ckid", map1.get(DataBaseHelper.ID).toString());
						ckNameAndID.add(map2);
					}
				}
				mSemaphore.release();
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			
//			if (ckmc.length < 7) {
//				y = (60 * ckmc.length);
//			} else {
//				y = 500;
//			}
//			
//			LayoutInflater inflater = LayoutInflater
//					.from(MX_filterActivity.this);
//			View view = inflater.inflate(R.layout.popupwindow_list, null);
//			mListView = (ListView) view.findViewById(R.id.popuplist);
//			ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(
//					MX_filterActivity.this, R.layout.popupwindow_list_textview,
//					ckmc);
//			mListView.setAdapter(adapter1);
//			mPopupWindow = new PopupWindow(view, ScreenWidth / 2, y);
////			mPopupWindow = new PopupWindow(view, ViewGroup.wrapcontent, ViewGroup.wrapcontent); 
//
//			// 这个是为了点击“返回Back”也能使其消失.
//			mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
//			// 使其聚焦
//			mPopupWindow.setFocusable(true);
//			// 设置允许在外点击消失
//			mPopupWindow.setOutsideTouchable(true);
//			// 刷新状态
//			mPopupWindow.update();
		};
	};
	public void setEmpty(){
		String datetime=formatter.format(new Date(System.currentTimeMillis()));
		f_Btn.setText(datetime);
		t_Btn.setText(datetime);
		ru_Rb.setChecked(false);
		pandian_Rb.setChecked(false);
		chu_Rb.setChecked(false);
		ck_EditText.setText("");
		ck_EditText.setHint(ck_EditText.getHint());
		dw_EditText.setText("");
		dw_EditText.setHint(dw_EditText.getHint());
		dh_EditText.setText("");
		djnameString = "";
	}
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		Intent intent = new Intent();
		switch(v.getId()){
		case R.id.back:
			finish();
			break;
		case R.id.dt1:
			calendar = Calendar.getInstance();
			DatePickerDialog dateDialog=new DatePickerDialog(this, new OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear,
						int dayOfMonth) {
					// TODO 自动生成的方法存根
					f_Btn.setText(year+"-"+new DecimalFormat("00").format(monthOfYear+1)+"-"+new DecimalFormat("00").format(dayOfMonth));
				}
			}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
			dateDialog.show();
			break;
		case R.id.dt2:
			calendar = Calendar.getInstance();
			DatePickerDialog dateDialog2=new DatePickerDialog(this, new OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear,
						int dayOfMonth) {
					// TODO 自动生成的方法存根
					t_Btn.setText(year+"-"+new DecimalFormat("00").format(monthOfYear+1)+"-"+new DecimalFormat("00").format(dayOfMonth));
				}
			}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
			dateDialog2.show();
			break;
		case R.id.cklayout:
//			if (mPopupWindow.isShowing()) {
//				mPopupWindow.dismiss();
//			} else {
//				mPopupWindow.showAsDropDown(ck_EditText, 0, 0);
//			}
//			mListView.setOnItemClickListener(new OnItemClickListener() {
//
//				@Override
//				public void onItemClick(AdapterView<?> arg0, View arg1,
//						int arg2, long arg3) {
//					// TODO 自动生成的方法存根
//					String str = arg0.getAdapter().getItem(arg2).toString();
//					ck_EditText.setText(str);
//					ckid=Integer.parseInt(ck_id[arg2]);
//					mPopupWindow.dismiss();
//				}
//			});
			if(ckNameAndID.isEmpty()){
				Toast.makeText(this, "仓库信息还在获取，稍后再试！", Toast.LENGTH_SHORT).show();
			}else{
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				LayoutInflater inflater = LayoutInflater.from(this);
				View view = inflater.inflate(R.layout.popupwindow_list, null);
				ListView myListView = (ListView) view.findViewById(R.id.popuplist);
				CkListViewAdapter ckListViewAdapter = new CkListViewAdapter(this);
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
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO 自动生成的方法存根
						Map<String, Object> getMap = (Map<String, Object>) arg0.getAdapter().getItem(arg2);
						ck_EditText.setText(getMap.get("ckmc").toString());
						ckid = Integer.parseInt(getMap.get("ckid").toString());
						dialog.dismiss();
					}
				});
				builder.setView(view);
				dialog = builder.create();
				dialog.show();
			}
			
			break;
		case R.id.dwlayout:
			intent.setClass(this, DwListActivity.class);
			startActivityForResult(intent, 1);
			break;
		case R.id.reset:
			setEmpty();
			break;
		case R.id.confirm:
			SQL="";
			String ck=EditHelper.cutString(ck_EditText.getText().toString());
			String dh=EditHelper.cutString(dh_EditText.getText().toString());
			String dw=EditHelper.cutString(dw_EditText.getText().toString());
			
			// c movem,b moved,a th_hp
			if(!dh.equals("")){
				SQL=SQL+"  and b.mvddh='"+dh+"'";
			}else{
				dh="不限";
			}
			if(!dw.equals("")){
				SQL=SQL+"  and c.dwName='"+dw+"'";
			}else{
				dw="不限";
			}
			
			String wherestr = "";
			if (ruku_flag) {
				wherestr = " and (b.mdType = '1' ";
			}
			if (chuku_flag) {
				if (wherestr.equals("")) {
					wherestr = " and (b.mdType = '2' ";
				} else {
					wherestr = wherestr + " or b.mdType = '2' ";
				}
			}
			if (pandian_flag) {
				if (wherestr.equals("")) {
					wherestr = " and b.mdType = '0' ";
				} else {
					wherestr = wherestr + " or b.mdType = '0' )";
				}
			} else {
				if (!wherestr.equals("")) {
					wherestr = wherestr + " )";
				}
			}
			SQL=SQL+wherestr;
			
			if(ruku_flag){
				djnameString ="入库单据";
			}
			
			if(chuku_flag){
				if(TextUtils.isEmpty(djnameString)){
					djnameString = "出库单据";
				}else{
					djnameString = djnameString+"\t出库单据";
				}
			}
			
			if(pandian_flag){
				if(TextUtils.isEmpty(djnameString)){
					djnameString = "盘点单据";
				}else{
					djnameString = djnameString+"\t盘点单据";
				}
			}
			
			intent.putExtra("fromtime", f_Btn.getText().toString());
			intent.putExtra("totime", t_Btn.getText().toString());
			intent.putExtra("djnameString", djnameString);
			intent.putExtra("ckmc", ck);
			intent.putExtra("ckid", ckid);
			intent.putExtra("dwmc", dw);
			intent.putExtra("dh", dh);
			intent.putExtra("SQL", SQL);
			intent.setClass(this, Moved_DJ.class);
			setResult(1, intent);
			finish();
			break;
		}
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO 自动生成的方法存根
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==1){
			if(resultCode==1){
				Map <String,Object> map = (Map<String, Object>) data.getSerializableExtra("dwmap");
				dw_EditText.setText(map.get(DataBaseHelper.DWName).toString());
			}
		}
	}
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO 自动生成的方法存根
		if(isChecked){
			switch(buttonView.getId()){
			case R.id.ck1:
				pandian_flag=true;
				break;
			case R.id.ck2:
				ruku_flag=true;
				break;
			case R.id.ck3:
				chuku_flag=true;
				break;
			}
		}else{
			switch(buttonView.getId()){
			case R.id.ck1:
				pandian_flag=false;
				break;
			case R.id.ck2:
				ruku_flag=false;
				break;
			case R.id.ck3:
				chuku_flag=false;
				break;
			}
		}
		
	}
}
