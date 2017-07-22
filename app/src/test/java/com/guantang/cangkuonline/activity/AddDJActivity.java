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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.listener.OnDrawLineChartTouchListener;
import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.application.MyApplication;
import com.guantang.cangkuonline.commonadapter.CommonAdapter;
import com.guantang.cangkuonline.database.DataBaseCheckMethod;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.database.DataBaseMethod;
import com.guantang.cangkuonline.database.DataBaseOperateMethod;
import com.guantang.cangkuonline.dialog.CommonDialog;
import com.guantang.cangkuonline.dialog.CommonDialog.DialogContentListener;
import com.guantang.cangkuonline.helper.DecimalsHelper;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;
import com.guantang.cangkuonline.webservice.WebserviceHelper;
import com.guantang.cangkuonline.webservice.WebserviceImport;

public class AddDJActivity extends Activity implements OnClickListener{
	private ImageButton backImageButton, okImageButton;
	private TextView titleTextView, dateTextView, depTextView, typeTextView,
			ckTextView, dwTextView, hp_totalTextView, all_totalTextView;
	private LinearLayout deplayout, typelayout, cklayout, dwlayout,
			addhplayout, tongjilayout;
	private LinearLayout customLayout1,customLayout2,customLayout3;
	private TextView customText1,customText2,customText3;
	private RelativeLayout addedlayout;
	private EditText dhEditText, jbrEditText, bzEditText;
	private EditText customEdit1,customEdit2,customEdit3;
	private Button commitBtn;
	private ImageView addhp1ImageView;
	private int op_type = 1;// 1是入库单据，2出库单据
	private SimpleDateFormat formatter, formatter2;
//	private CheckEditWatcher cked = new CheckEditWatcher();
	private DataBaseMethod dm = new DataBaseMethod(this);
	private DataBaseCheckMethod dm_cm = new DataBaseCheckMethod(this);
	private DataBaseOperateMethod dm_op = new DataBaseOperateMethod(this);
	private DataBaseCheckMethod dm_ck=new DataBaseCheckMethod(this);
	private List<Map<String, Object>> ck_ls = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> getList = new ArrayList<Map<String, Object>>();
	private String[] str = { DataBaseHelper.ID, DataBaseHelper.CKMC,
			DataBaseHelper.FZR, DataBaseHelper.TEL, DataBaseHelper.ADDR,
			DataBaseHelper.INACT, DataBaseHelper.OUTACT, DataBaseHelper.BZ };
	private String[] str3 = { DataBaseHelper.HPID, DataBaseHelper.MID,
			DataBaseHelper.MVDDATE, DataBaseHelper.MSL, DataBaseHelper.MVDType,
			DataBaseHelper.DH, DataBaseHelper.BTKC, DataBaseHelper.ATKC,
			DataBaseHelper.MVDirect, DataBaseHelper.DJ, DataBaseHelper.ZJ,
			DataBaseHelper.MVType, DataBaseHelper.CKID };
	private String[] strs1 = { DataBaseHelper.MVDH, DataBaseHelper.MVDT,
			DataBaseHelper.DWName, DataBaseHelper.JBR, DataBaseHelper.BZ,
			DataBaseHelper.mType, DataBaseHelper.actType,
			DataBaseHelper.DepName, DataBaseHelper.DepID, DataBaseHelper.CKMC,
			DataBaseHelper.CKID, DataBaseHelper.Lrr, DataBaseHelper.Lrsj,
			DataBaseHelper.DWID, DataBaseHelper.Details, DataBaseHelper.HPzj,DataBaseHelper.RES1,DataBaseHelper.RES2,DataBaseHelper.RES3};
	private String[] strs2 = { "mvdh", "mvdt", "dwName", "jbr", "bz", "mType",
			"actType", "depName", "depId", "ckName", "ckid", "lrr", "lrsj",
			"dwid", "hpDetails", "hpzjz","Res1","Res2","Res3" };
	private String[] strs3 = { DataBaseHelper.HPID, DataBaseHelper.MVDDATE,
			DataBaseHelper.MSL, DataBaseHelper.MVDType, DataBaseHelper.DH,
			DataBaseHelper.MVDirect, DataBaseHelper.DJ, DataBaseHelper.ZJ,
			DataBaseHelper.MVType, DataBaseHelper.CKID, DataBaseHelper.BTKC,
			DataBaseHelper.ATKC };
	private String[] strs4 = { "hpid", "mvddt", "msl", "mdType", "mvddh",
			"mddirect", "dj", "zj", "dactType", "ckid", "Btkc", "Atkc" };
	private SharedPreferences mSharePreferences;
	private ExecutorService cacheThreadpool = Executors.newCachedThreadPool();
	private ProgressDialog pro_Dialog;
	private AlertDialog dialog;
	private String ckmc="", djid="", dwid="",depid="";
	private int ckid = -1;
	private List<Map<String, Object>> lss = new ArrayList<Map<String, Object>>();//出入库类型的集合
	/**
	 * 是否对单据进行了操作的标志，以便确定是否保存该单据，true表示操作了该单据，false表示没有操作该单据。
	 * */
	private Boolean operationFlag=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.rukudj);
		mSharePreferences = getSharedPreferences(
				ShareprefenceBean.SHAREPREFENCE_NAME, Context.MODE_PRIVATE);
		initControl();
		init();
	}

	public void initControl() {
		titleTextView = (TextView) findViewById(R.id.title);
		backImageButton = (ImageButton) findViewById(R.id.back);
		okImageButton = (ImageButton) findViewById(R.id.ok);
		dateTextView = (TextView) findViewById(R.id.date);
		depTextView = (TextView) findViewById(R.id.dep);
		typeTextView = (TextView) findViewById(R.id.type);
		ckTextView = (TextView) findViewById(R.id.ck);
		dwTextView = (TextView) findViewById(R.id.dw);

		deplayout = (LinearLayout) findViewById(R.id.deplayout);
		typelayout = (LinearLayout) findViewById(R.id.typelayout);
		cklayout = (LinearLayout) findViewById(R.id.cklayout);
		dwlayout = (LinearLayout) findViewById(R.id.dwlayout);
		addhplayout = (LinearLayout) findViewById(R.id.addhp);
		addedlayout = (RelativeLayout) findViewById(R.id.added);
		dhEditText = (EditText) findViewById(R.id.dh);
		jbrEditText = (EditText) findViewById(R.id.jbr);
		bzEditText = (EditText) findViewById(R.id.bz);
		hp_totalTextView = (TextView) findViewById(R.id.hp_total);
		all_totalTextView = (TextView) findViewById(R.id.total);
		commitBtn = (Button) findViewById(R.id.commitBtn);
		addhp1ImageView = (ImageView) findViewById(R.id.addhp1);
		tongjilayout = (LinearLayout) findViewById(R.id.tongjilayout);
		
		customLayout1 = (LinearLayout) findViewById(R.id.customLayout1);
		customLayout2 = (LinearLayout) findViewById(R.id.customLayout2);
		customLayout3 = (LinearLayout) findViewById(R.id.customLayout3);
		customText1 = (TextView) findViewById(R.id.customText1);
		customText2 =(TextView) findViewById(R.id.customText2);
		customText3 = (TextView) findViewById(R.id.customText3);
		customEdit1 = (EditText) findViewById(R.id.customEdit1);
		customEdit2 = (EditText) findViewById(R.id.customEdit2);
		customEdit3 = (EditText) findViewById(R.id.customEdit3);
		
		Intent intent = getIntent();
		op_type = intent.getIntExtra("op_type", 1);
		switch (op_type) {
		case 1:
			titleTextView.setText("入库单据");
			break;
		case 2:
			titleTextView.setText("出库单据");
			break;
		}
		
		dhEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO 自动生成的方法存根
				dm_ck.update_movem(DataBaseHelper.MVDH, s.toString().trim().replace("'", ""), djid);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO 自动生成的方法存根
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO 自动生成的方法存根
//				if(s.toString().indexOf("'")>-1){
//					int pos=s.toString().indexOf("'");
//					s.delete(pos,pos+1);
//					
//				}
				if(!s.toString().equals("")){
					operationFlag=true;
				}
				s.toString().replace("'", "");
			}
		});
		
		jbrEditText.setText(MyApplication.getInstance().getSharedPreferences().getString(ShareprefenceBean.USERNAME, ""));
		jbrEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO 自动生成的方法存根
				dm_ck.update_movem(DataBaseHelper.JBR, s.toString().trim().replace("'", ""), djid);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO 自动生成的方法存根
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO 自动生成的方法存根
//				if(s.toString().indexOf("'")>-1){
//					int pos=s.toString().indexOf("'");
//					s.delete(pos,pos+1);
//					
//				}
				if(!s.toString().equals("")){
					operationFlag=true;
				}
				s.toString().replace("'", "");
			}
		});
		bzEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO 自动生成的方法存根
				dm_ck.update_movem(DataBaseHelper.BZ, s.toString().trim().replace("'", ""), djid);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO 自动生成的方法存根
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO 自动生成的方法存根
//				if(s.toString().indexOf("'")>-1){
//					int pos=s.toString().indexOf("'");
//					s.delete(pos,pos+1);
//				}
				if(!s.toString().equals("")){
					operationFlag=true;
				}
				s.toString().replace("'", "");
			}
		});
		
		customEdit1.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO 自动生成的方法存根
				dm_ck.update_movem(DataBaseHelper.RES1, s.toString().trim().replace("'", ""), djid);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO 自动生成的方法存根
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO 自动生成的方法存根
				if(!s.toString().equals("")){
					operationFlag=true;
				}
				s.toString().replace("'", "");
			}
		});
		
		customEdit2.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO 自动生成的方法存根
				dm_ck.update_movem(DataBaseHelper.RES2, s.toString().trim().replace("'", ""), djid);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO 自动生成的方法存根
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO 自动生成的方法存根
				if(!s.toString().equals("")){
					operationFlag=true;
				}
				s.toString().replace("'", "");
			}
		});
		
		customEdit3.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO 自动生成的方法存根
				dm_ck.update_movem(DataBaseHelper.RES3, s.toString().trim().replace("'", ""), djid);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO 自动生成的方法存根
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO 自动生成的方法存根
				if(!s.toString().equals("")){
					operationFlag=true;
				}
				s.toString().replace("'", "");
			}
		});
		
		
		backImageButton.setOnClickListener(this);
		okImageButton.setOnClickListener(this);
		dateTextView.setOnClickListener(this);
		deplayout.setOnClickListener(this);
		typelayout.setOnClickListener(this);
		cklayout.setOnClickListener(this);
		dwlayout.setOnClickListener(this);
		addhplayout.setOnClickListener(this);
		addedlayout.setOnClickListener(this);
		commitBtn.setOnClickListener(this);
		addhp1ImageView.setOnClickListener(this);
		tongjilayout.setOnClickListener(this);
		
		switch (op_type) {
		case 1:
			lss = dm_cm.Gt_Type("入库类型", DataBaseHelper.DWName);
			if(lss.size()==1){
				typeTextView.setText(lss.get(0).get(DataBaseHelper.DWName).toString());
			}
			break;
		case 2:
			lss = dm_cm.Gt_Type("出库类型", DataBaseHelper.DWName);
			if(lss.size()==1){
				typeTextView.setText(lss.get(0).get(DataBaseHelper.DWName).toString());
			}
			break;
		}
		
		List<Map<String,Object>> littleList = dm_op.getDJRes();
		setRes(littleList);
	}
	
	public void setRes(List<Map<String,Object>> list){
		if(!list.isEmpty()){
			String str;
			str = (String) list.get(0).get("文本型1");
			if (str == null || str.equals("")) {
				customLayout1.setVisibility(View.GONE);
			} else {
				customText1.setText(str);
			}
			str = (String) list.get(0).get("文本型2");
			if (str == null || str.equals("")) {
				customLayout2.setVisibility(View.GONE);
			} else {
				customText2.setText(str);
			}
			str = (String) list.get(0).get("文本型3");
			if (str == null || str.equals("")) {
				customLayout3.setVisibility(View.GONE);
			} else {
				customText3.setText(str);
			}
		}
	}
	
	public void init() {
		formatter = new SimpleDateFormat("yyyy-MM-dd");
		formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ck_ls = dm_op.Gt_ck(str);
		if (dm_op.searchUncompleteDJ(op_type).equals("")
				|| dm_op.searchUncompleteDJ(op_type) == null) {
			dateTextView.setText(formatter.format(new Date(System
					.currentTimeMillis())));
			dm_op.insert_into(DataBaseHelper.TB_MoveM,
					new String[] { DataBaseHelper.MVDT,DataBaseHelper.mType},
					new String[] { formatter.format(new Date(System
							.currentTimeMillis())), String.valueOf(op_type)});
			djid = dm_op.GtMax_DJID();
			dm_op.Update_DJtype(djid, 0);// type 0代表没有完成，1代表上传和保存本地，-1代表保存本地没上传
			
			//在没有填数据之前，先给数据库相应字段赋值，避免该字段为空
			dm_ck.update_movem(DataBaseHelper.MVDH, "", djid);
			dm_ck.update_movem(DataBaseHelper.JBR, "", djid);
			dm_ck.update_movem(DataBaseHelper.BZ, "", djid);
			
			//判断如果只有一个仓库，就默认显示仓库。
			if(ck_ls.size()==1){
				Map<String, Object> map = ck_ls.get(0);
				ckmc = map.get(DataBaseHelper.CKMC).toString();
				ckTextView.setText(ckmc);
				ckid = Integer.parseInt((String) map.get(DataBaseHelper.ID));
				dm_ck.update_movem(DataBaseHelper.CKMC, ckmc, djid);
				dm_ck.update_movem(DataBaseHelper.CKID, map.get(DataBaseHelper.ID).toString(), djid);
				if(op_type==1){
					if(map.get(DataBaseHelper.INACT)==null||map.get(DataBaseHelper.INACT).toString().equals("")){
						if(lss.size()==1){
							typeTextView.setText(lss.get(0).get(DataBaseHelper.DWName).toString());
						}
					}else{
						typeTextView.setText(map.get(DataBaseHelper.INACT).toString());
					}
				}else if(op_type==2){
					typeTextView.setText(map.get(DataBaseHelper.OUTACT).toString());
					if(map.get(DataBaseHelper.OUTACT)==null||map.get(DataBaseHelper.OUTACT).toString().equals("")){
						if(lss.size()==1){
							typeTextView.setText(lss.get(0).get(DataBaseHelper.DWName).toString());
						}
					}else{
						typeTextView.setText(map.get(DataBaseHelper.OUTACT).toString());
					}
				}
			}
		} else {
			djid=dm_op.searchUncompleteDJ(op_type);
			AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
			builder1.setCancelable(false);
			builder1.setMessage("有未完成单据，是否继续上次单据？");
			builder1.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							dm_op.Del_Moved(djid);
							dm_op.Del_Movem(djid);
							dateTextView.setText(formatter.format(new Date(System
									.currentTimeMillis())));
							dm_op.insert_into(DataBaseHelper.TB_MoveM,
									new String[] { DataBaseHelper.MVDT,DataBaseHelper.mType},
									new String[] { formatter.format(new Date(System
											.currentTimeMillis())),  String.valueOf(op_type)});
							djid = dm_op.GtMax_DJID();
							dm_op.Update_DJtype(djid, 0);// type 0代表没有完成，1代表上传和保存本地，-1代表保存本地没上传
							addedlayout.setVisibility(View.GONE);
							addhplayout.setVisibility(View.VISIBLE);
							dialog.dismiss();
						}
					});
			builder1.setPositiveButton("继续",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							// TODO 自动生成的方法存根
//							commitBtn.setVisibility(View.VISIBLE);
							List<Map<String, Object>> list1=dm_op.Gt_Movem(djid, strs1);
							Map<String, Object> map1=list1.get(0);
							if(map1.get(DataBaseHelper.MVDH)==null||map1.get(DataBaseHelper.MVDH).toString().equals("")){
								dhEditText.setText("");
							}else{
								operationFlag=true;
								dhEditText.setText(map1.get(DataBaseHelper.MVDH).toString());
							}
							if(map1.get(DataBaseHelper.DWName)==null||map1.get(DataBaseHelper.DWName).toString().equals("")){
								dwTextView.setText("");
							}else{
								operationFlag=true;
								dwTextView.setText(map1.get(DataBaseHelper.DWName).toString());
								dwid=map1.get(DataBaseHelper.DWID).toString();
							}
							if(map1.get(DataBaseHelper.actType)==null||map1.get(DataBaseHelper.actType).toString().equals("")){
								typeTextView.setText("");
							}else{
								operationFlag=true;
								typeTextView.setText(map1.get(DataBaseHelper.actType).toString());
							}
							if(map1.get(DataBaseHelper.JBR)==null||map1.get(DataBaseHelper.JBR).toString().equals("")){
								jbrEditText.setText(MyApplication.getInstance().getSharedPreferences().getString(ShareprefenceBean.USERNAME, ""));
							}else{
								operationFlag=true;
								jbrEditText.setText(map1.get(DataBaseHelper.JBR).toString());
							}
							if(map1.get(DataBaseHelper.BZ)==null||map1.get(DataBaseHelper.BZ).toString().equals("")){
								bzEditText.setText("");
							}else{
								operationFlag=true;
								bzEditText.setText(map1.get(DataBaseHelper.BZ).toString());
							}
							if(map1.get(DataBaseHelper.DepName)==null||map1.get(DataBaseHelper.DepName).toString().equals("")){
								depTextView.setText("");
							}else{
								operationFlag=true;
								depTextView.setText(map1.get(DataBaseHelper.DepName).toString());
								depid=map1.get(DataBaseHelper.DepID).toString();
							}
							if(map1.get(DataBaseHelper.CKMC)==null||map1.get(DataBaseHelper.CKMC).toString().equals("")){
								ckTextView.setText("");
							}else{
								operationFlag=true;
								ckTextView.setText(map1.get(DataBaseHelper.CKMC).toString());
								ckid=Integer.parseInt(map1.get(DataBaseHelper.CKID).toString());
							}
							if(map1.get(DataBaseHelper.RES1)==null||map1.get(DataBaseHelper.RES1).toString().equals("")){
								customEdit1.setText("");
							}else{
								operationFlag=true;
								customEdit1.setText(map1.get(DataBaseHelper.RES1).toString());
							}
							if(map1.get(DataBaseHelper.RES2)==null||map1.get(DataBaseHelper.RES2).toString().equals("")){
								customEdit2.setText("");
							}else{
								operationFlag=true;
								customEdit2.setText(map1.get(DataBaseHelper.RES2).toString());
							}
							if(map1.get(DataBaseHelper.RES3)==null||map1.get(DataBaseHelper.RES3).toString().equals("")){
								customEdit3.setText("");
							}else{
								operationFlag=true;
								customEdit3.setText(map1.get(DataBaseHelper.RES3).toString());
							}
							
							dateTextView.setText(formatter.format(new Date(System
									.currentTimeMillis())));
							dialog.dismiss();
						}
					});
			builder1.create().show();
		}

	}

	@Override
	protected void onStart() {
		// TODO 自动生成的方法存根
		super.onResume();
		double count = 0;
		getList = dm_op.Gt_Moved(djid, str3);
		if(getList.size()>0){
			addedlayout.setVisibility(View.VISIBLE);
			addhplayout.setVisibility(View.GONE);
		}else{
			addedlayout.setVisibility(View.GONE);
			addhplayout.setVisibility(View.VISIBLE);
		}
		hp_totalTextView.setText(String.valueOf(getList.size()));
		for (int i = 0; i < getList.size(); i++) {
			if ((!getList.get(i).get(DataBaseHelper.MSL).toString().equals(""))
					|| (getList.get(i).get(DataBaseHelper.MSL) != null)) {
				count += Float.parseFloat(getList.get(i)
						.get(DataBaseHelper.MSL).toString());
			}
		}
		all_totalTextView.setText(DecimalsHelper.Transfloat(count,MyApplication.getInstance().getNumPoint()));
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.back:
			if(operationFlag || Integer.parseInt(hp_totalTextView.getText().toString())!=0){
				exitDJDialog();
			}else{
				dm_op.Del_Moved(djid);
				dm_op.Del_Movem(djid);
				djid = "";
				finish();
			}
			break;
		case R.id.commitBtn:
			if(mSharePreferences.getInt(ShareprefenceBean.IS_LOGIN, -1)==1){
				if (hp_totalTextView.getText().toString().equals("0")) {
					Toast.makeText(AddDJActivity.this, "请先添加货品", Toast.LENGTH_SHORT)
							.show();
				} else if (typeTextView.getText().toString().equals("")) {
					Toast.makeText(AddDJActivity.this, "请选择类型", Toast.LENGTH_SHORT)
							.show();
				} else if (ckTextView.getText().toString().equals("")) {
					Toast.makeText(AddDJActivity.this, "请选择仓库", Toast.LENGTH_SHORT)
							.show();
				} else {
					if(WebserviceImport.isOpenNetwork(this)){
						pro_Dialog = ProgressDialog.show(this, null, "正在上传数据……");
						saveDJ();
						cacheThreadpool.execute(adddjRun);
					}else{
						Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
					}
				}
			}else if(mSharePreferences.getInt(ShareprefenceBean.IS_LOGIN, -1)==-1){
				saveDJ();
				dm_op.Update_DJtype(djid, -1);
				Toast.makeText(this, "单据已保存，请在本地单据中上传", 5000).show();
				finish();
			}
			break;
		case R.id.date:
			Calendar calendar = Calendar.getInstance();
			DatePickerDialog dateDialog = new DatePickerDialog(this,
					new OnDateSetListener() {
						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							// TODO 自动生成的方法存根
							dateTextView.setText(year+ "-"+ new DecimalFormat("00").format(monthOfYear + 1)+ "-"+ new DecimalFormat("00").format(dayOfMonth));
							dm_ck.update_movem(DataBaseHelper.MVDT,year+ "-"+ new DecimalFormat("00").format(monthOfYear + 1)+ "-"+ new DecimalFormat("00").format(dayOfMonth), djid);
							operationFlag=true;
						}
					}, calendar.get(Calendar.YEAR),
					calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH));
			dateDialog.show();
			break;
		case R.id.deplayout:
			intent.setClass(this, DepListActivity.class);
			startActivityForResult(intent, 2);
			break;
		case R.id.typelayout:
			final String[] ss = new String[lss.size()];
			for (int n = 0; n < lss.size(); n++) {
				ss[n] = (String) lss.get(n).get(DataBaseHelper.DWName);
			}
			
			CommonDialog mCommonDialog = new CommonDialog(this, R.layout.select_dialog, R.style.yuanjiao_activity);
			mCommonDialog.setCancelable(false);
			mCommonDialog.setDialogContentListener(new DialogContentListener() {
				
				@Override
				public void contentExecute(View parent, final Dialog dialog, Object[] objs) {
					// TODO Auto-generated method stub
					TextView mTextView = (TextView) parent.findViewById(R.id.titletextview);
					ListView myListView = (ListView) parent.findViewById(R.id.mylist);
					Button confirmBtn = (Button) parent.findViewById(R.id.confirmBtn);
					
					mTextView.setText("类型选择");
					confirmBtn.setText("取消");
					confirmBtn.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO 自动生成的方法存根
							dialog.dismiss();
						}
					});
					ArrayAdapter<String> fileList = new ArrayAdapter<String>(AddDJActivity.this,
			                R.layout.list_item, ss);
					myListView.setAdapter(fileList);
					myListView.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
							// TODO Auto-generated method stub
							typeTextView.setText(ss[arg2]);
							dm_ck.update_movem(DataBaseHelper.actType, ss[arg2], djid);
							operationFlag=true;
							dialog.dismiss();
						}
					});
				}
			});
			mCommonDialog.show();
			
//			final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//			builder.setTitle("请选择类型");
//			builder.setSingleChoiceItems(ss, -1,
//					new DialogInterface.OnClickListener() {
//						public void onClick(DialogInterface dialog, int which) {
//							typeTextView.setText(ss[which]);
//							dm_ck.update_movem(DataBaseHelper.actType, ss[which], djid);
//							operationFlag=true;
//							dialog.dismiss();
//						}
//					});
//			builder.setNegativeButton("取消",
//					new DialogInterface.OnClickListener() {
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							// TODO Auto-generated method stub
//							dialog.dismiss();
//						}
//					});
//			builder.create().show();
			break;
		case R.id.cklayout:
			final String[] ss2;
			if (!ck_ls.isEmpty()) {
				ss2 = new String[ck_ls.size()];
				for (int j = 0; j < ck_ls.size(); j++) {
					ss2[j] = (String) ck_ls.get(j).get(DataBaseHelper.CKMC);
				}
				
				CommonDialog myCommonDialog = new CommonDialog(this, R.layout.select_dialog, R.style.yuanjiao_activity);
				myCommonDialog.setCancelable(false);
				myCommonDialog.setDialogContentListener(new DialogContentListener() {
					
					@Override
					public void contentExecute(View parent, final Dialog dialog, Object[] objs) {
						// TODO Auto-generated method stub
						
						TextView mTextView = (TextView) parent.findViewById(R.id.titletextview);
						ListView myListView = (ListView) parent.findViewById(R.id.mylist);
						Button confirmBtn = (Button) parent.findViewById(R.id.confirmBtn);
						
						mTextView.setText("仓库选择");
						confirmBtn.setText("取消");
						confirmBtn.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO 自动生成的方法存根
								dialog.dismiss();
							}
						});
						
						ArrayAdapter<String> fileList = new ArrayAdapter<String>(AddDJActivity.this,
				                R.layout.list_item, ss2);
						myListView.setAdapter(fileList);
						myListView.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
								// TODO Auto-generated method stub
								if (ck_ls.get(arg2).get(DataBaseHelper.ID) != null) {
									ckTextView.setText(ss2[arg2]);
									ckid = Integer.parseInt((String) ck_ls.get(
											arg2).get(DataBaseHelper.ID));
									ckmc = ss2[arg2];
									dm_ck.update_movem(DataBaseHelper.CKMC, ckmc, djid);
									dm_ck.update_movem(DataBaseHelper.CKID, ck_ls.get(
											arg2).get(DataBaseHelper.ID).toString(), djid);
									operationFlag=true;
									if(typeTextView.getText().toString().equals("")){
										if(op_type==1){
											String rutype=ck_ls.get(arg2).get(DataBaseHelper.INACT).toString();
											if(!rutype.equals("")&&rutype!=null){
												typeTextView.setText(rutype);
											}else{
												typeTextView.setText("");
											}
										}else if(op_type==2){
											String chutype = ck_ls.get(arg2).get(DataBaseHelper.OUTACT).toString();
											if(!chutype.equals("")&&chutype!=null){
												typeTextView.setText(chutype);
											}else{
												typeTextView.setText("");
											}
										}
									}
								}else{
									Toast.makeText(AddDJActivity.this, "仓库id获取出错", Toast.LENGTH_SHORT).show();
								}
								dialog.dismiss();
							}
						});
					}
				});
				myCommonDialog.show();
				
//				AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
//				builder2.setTitle("请选择仓库");
//				builder2.setSingleChoiceItems(ss2, -1,
//						new DialogInterface.OnClickListener() {
//							public void onClick(DialogInterface dialog,
//									int which) {
//								if (ck_ls.get(which).get(DataBaseHelper.ID) != null) {
//									ckTextView.setText(ss2[which]);
//									ckid = Integer.parseInt((String) ck_ls.get(
//											which).get(DataBaseHelper.ID));
//									ckmc = ss2[which];
//									dm_ck.update_movem(DataBaseHelper.CKMC, ckmc, djid);
//									dm_ck.update_movem(DataBaseHelper.CKID, ck_ls.get(
//											which).get(DataBaseHelper.ID).toString(), djid);
//									operationFlag=true;
//									if(typeTextView.getText().toString().equals("")){
//										if(op_type==1){
//											String rutype=ck_ls.get(which).get(DataBaseHelper.INACT).toString();
//											if(!rutype.equals("")&&rutype!=null){
//												typeTextView.setText(rutype);
//											}else{
//												typeTextView.setText("");
//											}
//										}else if(op_type==2){
//											String chutype = ck_ls.get(which).get(DataBaseHelper.OUTACT).toString();
//											if(!chutype.equals("")&&chutype!=null){
//												typeTextView.setText(chutype);
//											}else{
//												typeTextView.setText("");
//											}
//										}
//									}
//								}else{
//									Toast.makeText(AddDJActivity.this, "仓库id获取出错", Toast.LENGTH_SHORT).show();
//								}
//								dialog.dismiss();
//							}
//						});
//				builder2.setNegativeButton("取消",
//						new DialogInterface.OnClickListener() {
//							@Override
//							public void onClick(DialogInterface dialog,
//									int which) {
//								// TODO Auto-generated method stub
//								dialog.dismiss();
//							}
//						});
//				builder2.create().show();
			}else{
				Toast.makeText(this, "无仓库信息或没有更新仓库信息", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.dwlayout:
			intent.setClass(this, DwListActivity.class);
			startActivityForResult(intent, 1);
			break;
		case R.id.addhp:
			if (ckTextView.getText().toString().equals("")) {
				Toast.makeText(this, "请先选择仓库", Toast.LENGTH_SHORT)
						.show();
//			}if(typeTextView.getText().toString().equals("")){
//				Toast.makeText(this, "请先选择类型", Toast.LENGTH_SHORT)
//				.show();
			} else {
				intent.setClass(this, HP_choseActivity.class);
				intent.putExtra("dh", dhEditText.getText().toString());
				intent.putExtra("djid", djid);
				intent.putExtra("ckid", ckid);
				intent.putExtra("op_type", op_type);
				intent.putExtra("dacttype", typeTextView.getText().toString());
				startActivityForResult(intent, 3);
			}
			break;
		case R.id.addhp1:
			if (ckTextView.getText().toString().equals("")) {
				Toast.makeText(this, "请先选择仓库", Toast.LENGTH_SHORT)
						.show();
//			}if(typeTextView.getText().toString().equals("")){
//				Toast.makeText(this, "请先选择类型", Toast.LENGTH_SHORT)
//				.show();
			} else {
				intent.setClass(this, HP_choseActivity.class);
				intent.putExtra("dh", dhEditText.getText().toString());
				intent.putExtra("djid", djid);
				intent.putExtra("ckid", ckid);
				intent.putExtra("op_type", op_type);
				intent.putExtra("dacttype", typeTextView.getText().toString());
				startActivityForResult(intent, 3);
			}
			break;
		case R.id.tongjilayout:
			intent.setClass(this, DJ_detailActivity.class);
			intent.putExtra("dh", dhEditText.getText().toString());
			intent.putExtra("djid", djid);
			intent.putExtra("ckid", ckid);
			intent.putExtra("op_type", op_type);
			intent.putExtra("dacttype", typeTextView.getText().toString());
			startActivity(intent);
			break;	
		}
	}

	public void saveDJ() {
		String time = formatter2.format(new Date(System.currentTimeMillis()));
		String details2 ="",hpstr = "";
		if(!getList.isEmpty()){
			hpstr = dm.Gethp(new String[]{DataBaseHelper.HPMC}, getList.get(0).get(DataBaseHelper.HPID).toString()).get(0).get(DataBaseHelper.HPMC).toString();
		}
		if(hp_totalTextView.getText().toString().equals("1")){
			details2 = hpstr;
		}else if (hp_totalTextView.getText().toString().equals("0")){
			details2 = "无货品明细";
		}else{
			details2 = hpstr + " 等"
					+ hp_totalTextView.getText().toString() + "种货品";
		}
		String hpzjz = dm_op.GetHpzjz(djid);
		if(dhEditText.getText().toString()==null){
			dhEditText.setText("");
		}
		if(dwTextView.getText().toString()==null){
			dwTextView.setText("");
			dwid="";
		}
		if(typeTextView.getText().toString()==null){
			typeTextView.setText("");
		}
		if(jbrEditText.getText().toString()==null){
			jbrEditText.setText("");
		}
		if(bzEditText.getText().toString()==null){
			bzEditText.setText("");
		}
		if(depTextView.getText().toString()==null){
			depTextView.setText("");
		}
		if(ckTextView.getText().toString()==null){
			ckTextView.setText("");
		}
		if(customEdit1.getText().toString()==null){
			customEdit1.setText("");
		}
		if(customEdit2.getText().toString()==null){
			customEdit2.setText("");
		}
		if(customEdit3.getText().toString()==null){
			customEdit3.setText("");
		}
		dm_op.Update_MoveM(djid,dhEditText.getText().toString(),dateTextView.getText().toString(), dwTextView
				.getText().toString(), jbrEditText.getText().toString(),
				bzEditText.getText().toString(), typeTextView.getText()
						.toString(), ckTextView.getText().toString(), ckid,
				details2, mSharePreferences.getString(ShareprefenceBean.USERNAME, ""), time, op_type, dwid,
				depTextView.getText().toString(), depid, hpzjz,customEdit1.getText().toString(),
				customEdit2.getText().toString(),customEdit3.getText().toString());
		dm_op.Update_MoveD(djid, dhEditText.getText().toString(), typeTextView
				.getText().toString(), ckid);
		List<Map<String, Object>> mxList = new ArrayList<Map<String,Object>>();
		mxList = dm_op.Gt_Moved(djid, str3);
		if(mxList!=null){
			for(int i=0;i<mxList.size();i++){
				String hpidsString = mxList.get(i).get(DataBaseHelper.HPID).toString();
				float atkcString = Float.valueOf(mxList.get(i).get(DataBaseHelper.ATKC).toString());
				String mvdirectString = mxList.get(i).get(DataBaseHelper.MVDirect).toString();
				float mslString = Float.valueOf(mxList.get(i).get(DataBaseHelper.MSL).toString());
//				dm_op.Del_CKKC(hpidsString, ckid);
				if(dm_op.Gt_ckkc(hpidsString, ckid)!=null){
					dm_op.Update_CKKC(hpidsString, ckid, atkcString);
				}else{
					dm_op.insert_ckkc(hpidsString, ckid, atkcString);
				}
				if(mvdirectString.equals("1")){
					dm_op.UpdateCurrKc_byhpid(hpidsString, mslString, 1);
				}else if(mvdirectString.equals("2")){
					dm_op.UpdateCurrKc_byhpid(hpidsString, mslString, 2);
				}
			}
		}
	}

	private Runnable adddjRun = new Runnable() {

		@Override
		public void run() {
			Message msg = Message.obtain();
			String[] values_movem = new String[strs1.length];
			List<Map<String, Object>> ls2 = dm_op.Gt_Movem(djid, strs1);
			if (ls2 != null) {
				for (int j = 0; j < strs1.length; j++) {
					values_movem[j] = (String) ls2.get(0).get(strs1[j]);
				}
			}
			List<Map<String, Object>> lss = dm_op.Gt_Moved(djid, strs3);
			String[] values_moved = new String[lss.size() * strs3.length];
			for (int j = 0; j < lss.size(); j++) {
				for (int n = 0; n < strs3.length; n++) {
					values_moved[j * strs3.length + n] = (String) lss.get(j)
							.get(strs3[n]);
				}
			}
			int flag = -1;
			try {
				String type = (String) ls2.get(0).get(DataBaseHelper.mType);
				switch (Integer.parseInt(type)) {
				case 0:
					flag = WebserviceImport.AddDJ(values_movem, strs2,
							values_moved, strs4, WebserviceHelper.AddPD,mSharePreferences);
					break;
				case 1:
					flag = WebserviceImport.AddDJ(values_movem, strs2,
							values_moved, strs4, WebserviceHelper. AddRuKu_1_0,mSharePreferences);
					break;
				case 2:
					flag = WebserviceImport.AddChuKu(values_movem, strs2,
							values_moved, strs4, false,mSharePreferences);
					break;
				}
			} catch (Exception e) {
				flag = -1;
			}
			if (flag > 0) {
				dm_op.Update_DJtype(djid, 1);// type
												// 0代表没有保存，1代表上传和保存本地，-1代表保存本地没上传
			} else {
				dm_op.Update_DJtype(djid, -1);
			}
			msg.what = flag;
			msg.setTarget(mHandler);
			mHandler.sendMessage(msg);
		}
	};

	Runnable addRun2 = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message msg = new Message();
			String[] values_movem = new String[strs1.length];
			List<Map<String, Object>> ls2 = dm_op.Gt_Movem(djid, strs1);
			if (ls2 != null) {
				for (int j = 0; j < strs1.length; j++) {
					values_movem[j] = (String) ls2.get(0).get(strs1[j]);
				}
			}
			List<Map<String, Object>> lss = dm_op.Gt_Moved(djid, strs3);
			String[] values_moved = new String[lss.size() * strs3.length];
			for (int j = 0; j < lss.size(); j++) {
				for (int n = 0; n < strs3.length; n++) {
					values_moved[j * strs3.length + n] = (String) lss.get(j)
							.get(strs3[n]);
				}
			}
			int flag = -1;
			try {
				String type = (String) ls2.get(0).get(DataBaseHelper.mType);
				switch (Integer.parseInt(type)) {
				case 0:
					flag = WebserviceImport.AddDJ(values_movem, strs2,
							values_moved, strs4, WebserviceHelper.AddPD,mSharePreferences);
					break;
				case 1:
					flag = WebserviceImport.AddDJ(values_movem, strs2,
							values_moved, strs4, WebserviceHelper.AddRuKu_1_0,mSharePreferences);
					break;
				case 2:
					flag = WebserviceImport.AddChuKu(values_movem, strs2,
							values_moved, strs4, true,mSharePreferences);
					break;
				default:
					flag = -1;
					break;
				}
			} catch (Exception e) {
				flag = -1;
			}
			if (flag > 0) {
				dm_op.Update_DJtype(djid, 1);// type
												// 0代表没有保存，1代表上传和保存本地，-1代表保存本地没上传
			} else {
				dm_op.Update_DJtype(djid, -1);
			}
			msg.what = flag;
			msg.setTarget(mHandler);
			mHandler.sendMessage(msg);
		}
	};
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			pro_Dialog.dismiss();
			if (msg.what > 0) {
//				if (mSharePreferences.getBoolean(
//						ShareprefenceBean.FIRST_PROMPT_DJ, true)) {
//					AlertDialog.Builder builder = new AlertDialog.Builder(
//							AddDJActivity.this);
//					builder.setMessage("已上传的单据需在网页端审核单据。");
//					builder.setNegativeButton("确定",
//							new DialogInterface.OnClickListener() {
//						
//								@Override
//								public void onClick(DialogInterface dialog,
//										int which) {
//									// TODO Auto-generated method stub
//									djid = "";
//									finish();
//									dialog.dismiss();
//								}
//							});
//					builder.setPositiveButton("不再提示",
//							new DialogInterface.OnClickListener() {
//
//								@Override
//								public void onClick(DialogInterface dialog,
//										int which) {
//									// TODO Auto-generated method stub
//									mSharePreferences
//											.edit()
//											.putBoolean(
//													ShareprefenceBean.FIRST_PROMPT_DJ,
//													false).commit();
//									djid = "";
//									finish();
//									dialog.dismiss();
//								}
//							});
//					builder.create().show();
//				}else{
//					Toast.makeText(AddDJActivity.this , "单据上传成功", Toast.LENGTH_SHORT).show();
//					djid = "";
//					finish();
//				}
				Toast.makeText(AddDJActivity.this , "单据上传成功", Toast.LENGTH_SHORT).show();
				djid = "";
				finish();
			} else if (msg.what == -501) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						AddDJActivity.this);
				builder.setMessage("有货品不足，是否继续出库？");
				builder.setNegativeButton("允许出库",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
								if(WebserviceImport.isOpenNetwork(AddDJActivity.this)){
									pro_Dialog = ProgressDialog.show(
											AddDJActivity.this, null, "正在上传数据");
									cacheThreadpool.execute(addRun2);
								}else{
									Toast.makeText(AddDJActivity.this, "网络未连接", Toast.LENGTH_SHORT).show();
								}
							}
						});
				builder.setPositiveButton("禁止出库",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								Intent intent = new Intent(AddDJActivity.this, DJMXCheckActivity.class);
								intent.putExtra("op_type", op_type);
								intent.putExtra("djid", djid);
								intent.putExtra("ckid", ckid);
								intent.putExtra("dh", dhEditText.getText().toString());
								intent.putExtra("dacttype", typeTextView.getText().toString());
								startActivity(intent);
								dialog.dismiss();
							}
						});
				builder.create().show();
			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(AddDJActivity.this);
				switch (msg.what) {
				case -101:
					builder.setMessage("仓库信息有误,请更新仓库信息后再上传。");
					break;
				case -102:
					builder.setMessage("出入库类型有误,请更新参数信息后再上传。");
					break;
				case -103:
					builder.setMessage("单号已存在,请修改单号后再上传。");
					break;
				case -104:
					builder.setMessage("单据日期错误,禁止插入单据,请修改单据后再上传。");
					break;
				case -500:
					builder.setMessage("库存不足,禁止出库,请修改单据后再上传。");
					break;
				case -3:
					builder.setMessage("帐号信息验证错误。");
					break;
				case -2:
					builder.setMessage("权限不足,不能上传单据。");
					break;
				case -502:
					builder.setMessage("账面数有误,请修改盘点单后再上传。");
					break;
				case -10:
					builder.setMessage("未知异常。");
					break;
				case -11:
					builder.setMessage("服务端异常。");
					break;
				case -12:
					builder.setMessage("数据解析异常。");
					break;
				case -503:
					builder.setMessage("该单据的某货品在指定仓库无库存，如果强制提交库存为负数");
					break;
				default:
					builder.setMessage("提交失败");
					break;
				}
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});
				builder.create().show();
			}
		};
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO 自动生成的方法存根
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			if (resultCode == 1) {
				Map<String,Object> map = (Map<String, Object>) data.getSerializableExtra("dwmap");
				dwTextView.setText(map.get(DataBaseHelper.DWName).toString());
				dwid = data.getStringExtra("dwid");
				dm_ck.update_movem(DataBaseHelper.DWName, map.get(DataBaseHelper.DWName).toString(), djid);
				dm_ck.update_movem(DataBaseHelper.DWID, data.getStringExtra("dwid"), djid);
				if(!dwTextView.getText().toString().equals("")){
					operationFlag=true;
				}
			}
		} else if (requestCode == 2) {
			if (resultCode == 1) {
				depTextView.setText(data.getStringExtra("depname"));
				depid = data.getStringExtra("depid");
				dm_ck.update_movem(DataBaseHelper.DepName, data.getStringExtra("depname"), djid);
				dm_ck.update_movem(DataBaseHelper.DepID, data.getStringExtra("depid"), djid);
				if(!depTextView.getText().toString().equals("")){
					operationFlag=true;
				}
			}
		} else if (requestCode == 3) {
			if(resultCode == 1){
				addedlayout.setVisibility(View.VISIBLE);
				addhplayout.setVisibility(View.GONE);
			}
		}

	}
	public void exitDJDialog(){
		AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
		View view = LayoutInflater.from(this).inflate(R.layout.dialogbutton, null);
		Button exit_unsaveBtn = (Button) view.findViewById(R.id.exit_unsaveBtn);
		Button exit_saveBtn = (Button) view.findViewById(R.id.exit_saveBtn);
		Button stayBtn = (Button) view.findViewById(R.id.stayBtn);
		exit_unsaveBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				dm_op.Del_Moved(djid);
				dm_op.Del_Movem(djid);
				djid = "";
				dialog.dismiss();
				finish();
			}
		});
		exit_saveBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				dialog.dismiss();
				finish();
			}
		});
		stayBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				dialog.dismiss();
			}
		});
		builder1.setView(view);
		builder1.setTitle("是否要退出？");
		dialog = builder1.create();
		dialog.show();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO 自动生成的方法存根

		if (keyCode == KeyEvent.KEYCODE_BACK ) {
			if(operationFlag || Integer.parseInt(hp_totalTextView.getText().toString())!=0){
				exitDJDialog();
			}else{
				dm_op.Del_Moved(djid);
				dm_op.Del_Movem(djid);
				djid = "";
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
