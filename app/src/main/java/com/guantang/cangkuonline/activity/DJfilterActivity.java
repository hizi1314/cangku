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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
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
import android.widget.TextView;
import android.widget.Toast;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.adapter.CkListViewAdapter;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.database.DataBaseMethod;
import com.guantang.cangkuonline.helper.EditHelper;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;
import com.guantang.cangkuonline.webservice.WebserviceImport;

public class DJfilterActivity extends Activity implements android.view.View.OnClickListener,OnCheckedChangeListener{
	private Button dt1Btn,dt2Btn,reset_Btn,confirm_Btn;
	private CheckBox rkBox,ckBox,pdBox;
	private EditText dhEditText;
	private TextView ckEditText;
	private ImageButton backImageButton;
	private ListView mListView;
	private SimpleDateFormat formattor;
	private LinearLayout ckLayout;
	private List<Map<String, Object>> cklist;
	private List<Map<String, Object>> ckNameAndID = new ArrayList<Map<String,Object>>();
	/**
	 * 引入一个值为1的信号量，防止ckNameAndID装数据的时候就刷新界面
	 */
	private volatile Semaphore mSemaphore = new Semaphore(1);
	
	private int ckid=-1;
	private ExecutorService cacheThreadPool = Executors.newCachedThreadPool();
	private String[] str15 = { "ID", "CKMC", "FZR", "TEL", "ADDR", "inact",
			"outact", "BZ" };
	private String[] str_ck = { DataBaseHelper.ID, DataBaseHelper.CKMC,
			DataBaseHelper.FZR, DataBaseHelper.TEL, DataBaseHelper.ADDR,
			DataBaseHelper.INACT, DataBaseHelper.OUTACT, DataBaseHelper.BZ };
	private AlertDialog.Builder builder;
	private AlertDialog dialog;
	private Boolean ruku_flag=false;
	private Boolean chuku_flag=false;
	private Boolean pandian_flag=false;
	private DataBaseMethod dm = new DataBaseMethod(this);
	private SharedPreferences sharedPreferences;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		// 不去重新绘制界面
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		setContentView(R.layout.activity_dj_filter);
		this.setFinishOnTouchOutside(false);
		sharedPreferences = getSharedPreferences(ShareprefenceBean.SHAREPREFENCE_NAME, Context.MODE_PRIVATE);
		initView();
		init();
	}
	public void initView(){
		dt1Btn = (Button) findViewById(R.id.dt1);
		dt2Btn = (Button) findViewById(R.id.dt2);
		rkBox = (CheckBox) findViewById(R.id.ck1);
		ckBox = (CheckBox) findViewById(R.id.ck2);
		pdBox = (CheckBox) findViewById(R.id.ck3);
		ckLayout = (LinearLayout) findViewById(R.id.ckLayout);
		ckEditText = (TextView) findViewById(R.id.ckedit);
		dhEditText = (EditText) findViewById(R.id.dh_edit);
		reset_Btn = (Button) findViewById(R.id.reset);
		confirm_Btn = (Button) findViewById(R.id.confirm);
		backImageButton = (ImageButton) findViewById(R.id.back);
		
		backImageButton.setOnClickListener(this);
		dt1Btn.setOnClickListener(this);
		dt2Btn.setOnClickListener(this);
		ckLayout.setOnClickListener(this);
		reset_Btn.setOnClickListener(this);
		confirm_Btn.setOnClickListener(this);
		rkBox.setOnCheckedChangeListener(this);
		ckBox.setOnCheckedChangeListener(this);
		pdBox.setOnCheckedChangeListener(this);
		dhEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO 自动生成的方法存根
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO 自动生成的方法存根
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO 自动生成的方法存根
				s.toString().replace("'", "");
			}
		});
	}
	public void init(){
		formattor = new SimpleDateFormat("yyyy-MM-dd");
		String date = formattor.format(new Date(System.currentTimeMillis()));
		dt1Btn.setText(date);
		dt2Btn.setText(date);
		if(sharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1)==1){
			if(WebserviceImport.isOpenNetwork(this)){
				cacheThreadPool.execute(run);
			}else{
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
			}
		}else if(sharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1)==-1){
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
		}
	}
	
	public void setEmpty(){
		String datetime=formattor.format(new Date(System.currentTimeMillis()));
		dt1Btn.setText(datetime);
		dt2Btn.setText(datetime);
		rkBox.setChecked(false);
		ckBox.setChecked(false);
		pdBox.setChecked(false);
		ckEditText.setText("");
		ckEditText.setHint(ckEditText.getHint());
		dhEditText.setText("");
	}
	
	private Runnable run = new Runnable() {
		@Override
		public void run() {
			// TODO 自动生成的方法存根
			Message msg = Message.obtain();
			List<Map<String, Object>> ck_list;
			ck_list = WebserviceImport.GetCK(str_ck, str15,sharedPreferences);
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
			
		};
	};
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		Intent intent = new Intent();
		Calendar calender = Calendar.getInstance();
		switch(v.getId()){
		case R.id.back:
			finish();
			break;
		case R.id.dt1:
			DatePickerDialog dateDialog = new DatePickerDialog(this, new OnDateSetListener() {
				
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear,
						int dayOfMonth) {
					// TODO 自动生成的方法存根
					dt1Btn.setText(year+"-"+new DecimalFormat("00").format(monthOfYear+1)+"-"+ new DecimalFormat("00").format(dayOfMonth));
				}
			}, calender.get(Calendar.YEAR), calender.get(Calendar.MONTH), calender.get(Calendar.DAY_OF_MONTH));
			dateDialog.show();
			break;
		case R.id.dt2:
			DatePickerDialog dateDialog1 = new DatePickerDialog(this, new OnDateSetListener() {
				
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear,
						int dayOfMonth) {
					// TODO 自动生成的方法存根
					dt2Btn.setText(year+"-"+new DecimalFormat("00").format(monthOfYear+1)+"-"+ new DecimalFormat("00").format(dayOfMonth));
				}
			}, calender.get(Calendar.YEAR), calender.get(Calendar.MONTH), calender.get(Calendar.DAY_OF_MONTH));
			dateDialog1.show();
			break;
		case R.id.ckLayout:
			if(ckNameAndID.isEmpty()){
				Toast.makeText(this, "仓库信息还在获取，稍后再试！", Toast.LENGTH_SHORT).show();
			}else{
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
						Map<String, Object> getMap = (Map<String, Object>) arg0.getAdapter().getItem(arg2);
						ckEditText.setText(getMap.get("ckmc").toString());
						ckid = Integer.parseInt(getMap.get("ckid").toString());
						dialog.dismiss();
					}
				});
				builder.setView(view);
				dialog = builder.create();
				dialog.show();
			}
			break;
		case R.id.reset:
			setEmpty();
			break;
		case R.id.confirm:
			String ck=EditHelper.cutString(ckEditText.getText().toString()) ;
			String dh=EditHelper.cutString(dhEditText.getText().toString());
			if(ck.equals("")||ck==null){
				ck="不限";
			}
			if(dh.equals("")||dh==null){
				dh="不限";
			}
			intent.putExtra("fromtime", dt1Btn.getText().toString());
			intent.putExtra("totime", dt2Btn.getText().toString());
			intent.putExtra("ruku_flag", ruku_flag);
			intent.putExtra("chuku_flag", chuku_flag);
			intent.putExtra("pandian_flag", pandian_flag);
			intent.putExtra("ckmc", ck);
			intent.putExtra("ckid", ckid);
			intent.putExtra("dh", dh);
			setResult(1, intent);
			finish();
			break;
		case R.id.cancel:
			finish();
			break;
		}
	}
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO 自动生成的方法存根
		if(isChecked){
			if(buttonView.getText().equals("入库单据")){
				ruku_flag=true;
			}else if(buttonView.getText().equals("出库单据")){
				chuku_flag=true;
			}else if(buttonView.getText().equals("盘点单据")){
				pandian_flag=true;
			}
		}else{
			if(buttonView.getText().equals("入库单据")){
				ruku_flag=false;
			}else if(buttonView.getText().equals("出库单据")){
				chuku_flag=false;
			}else if(buttonView.getText().equals("盘点单据")){
				pandian_flag=false;
			}
		}
	}
	
}
