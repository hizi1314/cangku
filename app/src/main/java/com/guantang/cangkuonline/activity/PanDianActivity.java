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

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.application.MyApplication;
import com.guantang.cangkuonline.database.DataBaseCheckMethod;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.database.DataBaseMethod;
import com.guantang.cangkuonline.database.DataBaseOperateMethod;
import com.guantang.cangkuonline.helper.DecimalsHelper;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;
import com.guantang.cangkuonline.webservice.WebserviceHelper;
import com.guantang.cangkuonline.webservice.WebserviceImport;

public class PanDianActivity extends Activity implements OnClickListener {
	private ImageButton backImgBtn, saveImgBtn;
	private TextView dateTextView, ckTextView, pandian_allTextView,
			pandian_winTextView, pandian_loseTextView;
	private EditText dhEditText, jbrEditText, bzEditText;
	private LinearLayout cklayout, addhpLayout, tongjilayout;
	private RelativeLayout pandian_edLayout;
	private Button commitBtn;
	private ImageView addhp1ImageView;
	private List<Map<String, Object>> ck_ls = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> getList = new ArrayList<Map<String, Object>>();
	private DataBaseOperateMethod dm_op = new DataBaseOperateMethod(this);
	private DataBaseCheckMethod dm_cm = new DataBaseCheckMethod(this);
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
			DataBaseHelper.DWID, DataBaseHelper.Details, DataBaseHelper.HPzj };
	private String[] strs2 = { "mvdh", "mvdt", "dwName", "jbr", "bz", "mType",
			"actType", "depName", "depId", "ckName", "ckid", "lrr", "lrsj",
			"dwid", "hpDetails", "hpzjz" };
	private String[] strs3 = { DataBaseHelper.HPID, DataBaseHelper.MVDDATE,
			DataBaseHelper.MSL, DataBaseHelper.MVDType, DataBaseHelper.DH,
			DataBaseHelper.MVDirect, DataBaseHelper.DJ, DataBaseHelper.ZJ,
			DataBaseHelper.MVType, DataBaseHelper.CKID, DataBaseHelper.BTKC,
			DataBaseHelper.ATKC };
	private String[] strs4 = { "hpid", "mvddt", "msl", "mdType", "mvddh",
			"mddirect", "dj", "zj", "dactType", "ckid", "Btkc", "Atkc" };
	
	private ExecutorService cacheThreadpool = Executors.newCachedThreadPool();
	private int ckid = -1, op_type = 0;
	private String ckmc, djid;
	private SimpleDateFormat formatter1, formatter2;
	private ProgressDialog pro_Dialog;
	private AlertDialog dialog;
	private SharedPreferences mSharePreferences;
	private DataBaseMethod dm = new DataBaseMethod(this);
	private DataBaseCheckMethod dm_ck=new DataBaseCheckMethod(this);
	/**
	 * 是否对单据进行了操作的标志，以便确定是否保存该单据，true表示操作了该单据，false表示没有操作该单据。
	 * */
	private Boolean operationFlag=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_pandian);
		
		mSharePreferences = getSharedPreferences(
				ShareprefenceBean.SHAREPREFENCE_NAME, Context.MODE_PRIVATE);
		
		initView();
		init();
	}

	public void initView() {
		backImgBtn = (ImageButton) findViewById(R.id.back);
		saveImgBtn = (ImageButton) findViewById(R.id.save);
		dateTextView = (TextView) findViewById(R.id.date);
		ckTextView = (TextView) findViewById(R.id.ck);
		pandian_allTextView = (TextView) findViewById(R.id.pandian_all);
		pandian_winTextView = (TextView) findViewById(R.id.pandian_win);
		pandian_loseTextView = (TextView) findViewById(R.id.pandian_lose);
		dhEditText = (EditText) findViewById(R.id.dh);
		jbrEditText = (EditText) findViewById(R.id.jbr);
		bzEditText = (EditText) findViewById(R.id.bz);
		cklayout = (LinearLayout) findViewById(R.id.cklayout);
		addhpLayout = (LinearLayout) findViewById(R.id.addhp);
		pandian_edLayout = (RelativeLayout) findViewById(R.id.pandian_ed);
		commitBtn = (Button) findViewById(R.id.commitBtn);
		addhp1ImageView = (ImageView) findViewById(R.id.addhp1);
		tongjilayout = (LinearLayout) findViewById(R.id.tongjilayout);
		
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
		backImgBtn.setOnClickListener(this);
		saveImgBtn.setOnClickListener(this);
		dateTextView.setOnClickListener(this);
		cklayout.setOnClickListener(this);
		addhpLayout.setOnClickListener(this);
		pandian_edLayout.setOnClickListener(this);
		commitBtn.setOnClickListener(this);
		addhp1ImageView.setOnClickListener(this);
		tongjilayout.setOnClickListener(this);
	}

	public void init() {
		formatter1 = new SimpleDateFormat("yyyy-MM-dd");
		formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (dm_op.searchUncompleteDJ(0).equals("")
				|| dm_op.searchUncompleteDJ(0) == null) {
			dateTextView.setText(formatter1.format(new Date(System
					.currentTimeMillis())));
			dm_op.insert_into(DataBaseHelper.TB_MoveM, new String[]{DataBaseHelper.MVDT,DataBaseHelper.mType},
					new String[]{formatter1.format(new Date(System.currentTimeMillis())),"0"});
			djid = dm_op.GtMax_DJID();
			dm_op.Update_DJtype(djid, 0);// type 0代表没有保存，1代表上传和保存本地，-1代表保存本地没上传
			
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
			}
		} else {
			djid=dm_op.searchUncompleteDJ(0);
			AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
			builder1.setMessage("有未完成盘点单据，是否继续上次单据？");
			builder1.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							dm_op.Del_Moved(djid);
							dm_op.Del_Movem(djid);
							dateTextView.setText(formatter1.format(new Date(System
									.currentTimeMillis())));
							dm_op.insert_into(DataBaseHelper.TB_MoveM,
									new String[] { DataBaseHelper.MVDT,DataBaseHelper.mType },
									new String[] { formatter1.format(new Date(System
											.currentTimeMillis())), "0"});
							djid = dm_op.GtMax_DJID();
							dm_op.Update_DJtype(djid, 0);// type 0代表没有保存，1代表上传和保存本地，-1代表保存本地没上传
							
							addhpLayout.setVisibility(View.VISIBLE);
							pandian_edLayout.setVisibility(View.GONE);
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
							if(map1.get(DataBaseHelper.CKMC)==null||map1.get(DataBaseHelper.CKMC).toString().equals("")){
								ckTextView.setText("");
							}else{
								operationFlag=true;
								ckTextView.setText(map1.get(DataBaseHelper.CKMC).toString());
								ckid=Integer.parseInt(map1.get(DataBaseHelper.CKID).toString());
							}
							dateTextView.setText(formatter1.format(new Date(System
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
		double countWin = 0,countKui=0;
		getList = dm_op.Gt_Moved(djid, str3);
		if(getList.size()>0){
			addhpLayout.setVisibility(View.GONE);
			pandian_edLayout.setVisibility(View.VISIBLE);
		}else{
			addhpLayout.setVisibility(View.VISIBLE);
			pandian_edLayout.setVisibility(View.GONE);
		}
		pandian_allTextView.setText(String.valueOf(getList.size()));
		Map<String,Object> map = new HashMap<String,Object>();
		for (int i = 0; i < getList.size(); i++) {
			map=getList.get(i);
			if(map.get(DataBaseHelper.MVType).toString().equals("盘赢")){
				if ((!map.get(DataBaseHelper.MSL).toString().equals(""))
						|| (map.get(DataBaseHelper.MSL) != null)) {
					countWin += Float.parseFloat(map.get(DataBaseHelper.MSL).toString());
				}
			}else if(map.get(DataBaseHelper.MVType).toString().equals("盘亏")){
				if ((!map.get(DataBaseHelper.MSL).toString().equals(""))
						|| (map.get(DataBaseHelper.MSL) != null)) {
					countKui +=Float.parseFloat(map
							.get(DataBaseHelper.MSL).toString());
				}
			}
			
		}
		pandian_winTextView.setText(DecimalsHelper.Transfloat(countWin,MyApplication.getInstance().getNumPoint()));
		pandian_loseTextView.setText(DecimalsHelper.Transfloat(countKui,MyApplication.getInstance().getNumPoint()));
		super.onStart();
	}
	
	public void savepdDJ() {
		String details2 ="",hpstr = "";
		if(!getList.isEmpty()){
			hpstr = dm.Gethp(new String[]{DataBaseHelper.HPMC}, getList.get(0).get(DataBaseHelper.HPID).toString()).get(0).get(DataBaseHelper.HPMC).toString();
			details2 = hpstr + " 等"+ pandian_allTextView.getText().toString() + "种货品";
		}
		if(pandian_allTextView.getText().toString().equals("1")){
			details2 = hpstr;
		}else if (pandian_allTextView.getText().toString().equals("0")){
			details2 = "无货品明细";
		}else{
			details2 = hpstr + " 等"
					+ pandian_allTextView.getText().toString() + "种货品";
		}
		String time = formatter2.format(new Date(System.currentTimeMillis()));
		if(dhEditText.getText().toString()==null){
			dhEditText.setText("");
		}
		if(jbrEditText.getText().toString()==null){
			jbrEditText.setText("");
		}
		if(bzEditText.getText().toString()==null){
			bzEditText.setText("");
		}
		if(ckTextView.getText().toString()==null){
			ckTextView.setText("");
		}
		dm_ck.Check_save_movem(djid,dateTextView.getText().toString(),details2,dhEditText.getText().toString(),
				jbrEditText.getText().toString(),bzEditText.getText().toString(),ckTextView.getText().toString(),
				ckid,details2,mSharePreferences.getString(ShareprefenceBean.USERNAME, ""),time,op_type);
		dm_ck.Check_save_moved(djid, dhEditText.getText().toString());
		
		//修改tb_ckkc的货品数量，修改tb_hp的货品总数量
		List<Map<String, Object>> mxList = new ArrayList<Map<String,Object>>();
		mxList = dm_op.Gt_Moved(djid, str3);
		if(mxList!=null){
			for(int i=0;i<mxList.size();i++){
				String hpidsString = mxList.get(i).get(DataBaseHelper.HPID).toString();
				float atkcString = Float.valueOf(mxList.get(i).get(DataBaseHelper.ATKC).toString());
				String mvdirectString = mxList.get(i).get(DataBaseHelper.MVDirect).toString();
				float mslString = Float.valueOf(mxList.get(i).get(DataBaseHelper.MSL).toString());
//				dm_op.Del_CKKC(hpidsString, ckid);
//				dm_op.insert_ckkc(hpidsString, ckid, atkcString);
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
	
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.back:
			if(operationFlag || Integer.parseInt(pandian_allTextView.getText().toString())!=0){
				exitDJDialog();
			}else{
				dm_op.Del_Moved(djid);
				dm_op.Del_Movem(djid);
				djid = "";
				finish();
			}
			break;
		case R.id.commitBtn:
			if (ckid < 0) {
				Toast.makeText(this, "请选择仓库", Toast.LENGTH_SHORT).show();
			} else {
				if(pandian_allTextView.getText().toString().equals("0")){
					Toast.makeText(PanDianActivity.this, "请先添加货品", Toast.LENGTH_SHORT).show();
				}else{
					if(mSharePreferences.getInt(ShareprefenceBean.IS_LOGIN, -1)==1){
						if(WebserviceImport.isOpenNetwork(this)){
							savepdDJ();
							pro_Dialog = ProgressDialog.show(this, null, "正在上传数据……");
							cacheThreadpool.execute(addRun);
						}else{
							Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
						}
					}else if(mSharePreferences.getInt(ShareprefenceBean.IS_LOGIN, -1)==-1){
						savepdDJ();
						dm_op.Update_DJtype(djid, -1);
						Toast.makeText(this, "单据已保存，请在在线模式下上传", Toast.LENGTH_SHORT).show();
						finish();
					}
				}
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
		case R.id.cklayout:
			final String[] ss2;
			ck_ls = dm_op.Gt_ck(str);
			if (ck_ls != null) {
				ss2 = new String[ck_ls.size()];
				for (int j = 0; j < ck_ls.size(); j++) {
					ss2[j] = (String) ck_ls.get(j).get(DataBaseHelper.CKMC);
				}
				AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
				builder2.setTitle("请选择仓库");
				builder2.setSingleChoiceItems(ss2, -1,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								if (ck_ls.get(which).get(DataBaseHelper.ID) != null) {
									ckTextView.setText(ss2[which]);
									ckid = Integer.parseInt((String) ck_ls.get(
											which).get(DataBaseHelper.ID));
									ckmc = (String) ck_ls.get(which).get(
											DataBaseHelper.CKMC);
									dm_ck.update_movem(DataBaseHelper.CKMC, ckmc, djid);
									dm_ck.update_movem(DataBaseHelper.CKID, ck_ls.get(
											which).get(DataBaseHelper.ID).toString(), djid);
									operationFlag=true;
								}else{
									Toast.makeText(PanDianActivity.this, "仓库id获取出错", Toast.LENGTH_SHORT).show();
								}
								dialog.dismiss();
							}
						});
				builder2.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});
				builder2.create().show();
			}else{
				Toast.makeText(this, "无仓库信息或没有更新仓库信息", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.addhp:
			if (ckTextView.getText().toString().equals("")) {
				Toast.makeText(this, "请先选择仓库", Toast.LENGTH_SHORT)
						.show();
			} else {
				
//				if(dhEditText.getText().toString()==null){
//					dhEditText.setText("");
//				}
//				if(jbrEditText.getText().toString()==null){
//					jbrEditText.setText("");
//				}
//				if(bzEditText.getText().toString()==null){
//					bzEditText.setText("");
//				}
//				if(ckTextView.getText().toString()==null){
//					ckTextView.setText("");
//				}
//				dm_ck.Check_savePDCK_movem(djid, dhEditText.getText().toString(), jbrEditText.getText().toString(),
//						bzEditText.getText().toString(), ckTextView.getText().toString(), ckid);
				intent.setClass(this, HP_choseActivity.class);
				intent.putExtra("dh", dhEditText.getText().toString());
				intent.putExtra("djid", djid);
				intent.putExtra("ckid", ckid);
				intent.putExtra("op_type", 0);
				intent.putExtra("dacttype", "");
				startActivityForResult(intent, 1);
			}
			break;
		case R.id.addhp1:
			if (ckTextView.getText().toString().equals("")) {
				Toast.makeText(this, "请先选择仓库", Toast.LENGTH_SHORT)
						.show();
			} else {
				
//				if(dhEditText.getText().toString()==null){
//					dhEditText.setText("");
//				}
//				if(jbrEditText.getText().toString()==null){
//					jbrEditText.setText("");
//				}
//				if(bzEditText.getText().toString()==null){
//					bzEditText.setText("");
//				}
//				if(ckTextView.getText().toString()==null){
//					ckTextView.setText("");
//				}
//				dm_ck.Check_savePDCK_movem(djid, dhEditText.getText().toString(), jbrEditText.getText().toString(),
//						bzEditText.getText().toString(), ckTextView.getText().toString(), ckid);
				intent.setClass(this, HP_choseActivity.class);
				intent.putExtra("dh", dhEditText.getText().toString());
				intent.putExtra("djid", djid);
				intent.putExtra("ckid", ckid);
				intent.putExtra("op_type", 0);
				intent.putExtra("dacttype", "");
				startActivityForResult(intent, 1);
			}
			break;
		case R.id.tongjilayout:
			intent.setClass(this, DJ_detailActivity.class);
			intent.putExtra("dh", dhEditText.getText().toString());
			intent.putExtra("ckid", ckid);
			intent.putExtra("djid", djid);
			intent.putExtra("op_type", 0);
			intent.putExtra("dacttype", "");
			startActivity(intent);
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO 自动生成的方法存根
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==1){
			if(resultCode==1){
				addhpLayout.setVisibility(View.GONE);
				pandian_edLayout.setVisibility(View.VISIBLE);
			}
		}
	}


	private Runnable addRun = new Runnable(){
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message msg = new Message();
			String[] values_movem=new String[strs1.length];
			List<Map<String, Object>> ls2=dm_op.Gt_Movem(djid, strs1);
			if(ls2!=null){
				for(int j=0;j<strs1.length;j++){
					values_movem[j]=(String) ls2.get(0).get(strs1[j]);
				}
			}
			List<Map<String, Object>> lss=dm_op.Gt_Moved(djid, strs3);
			String[] values_moved=new String[lss.size()*strs3.length];
			for(int j=0;j<lss.size();j++){
				for(int n=0;n<strs3.length;n++){
					values_moved[j*strs3.length+n]=(String) lss.get(j).get(strs3[n]);
				}
			}
			int flag;
			try{
				String type=(String) ls2.get(0).get(DataBaseHelper.mType);
				switch(Integer.parseInt(type)){
				case 0:
					flag=WebserviceImport.AddDJ(values_movem, strs2, values_moved, strs4,WebserviceHelper.AddPD,mSharePreferences);
					break;
					default:
						flag=-1;
						break;
				}
			}catch(Exception e){
				flag=-1;
			}
			if (flag > 0) {
				dm_op.Update_DJtype(djid, 1);// type
												// 0代表没有保存，1代表上传和保存本地，-1代表保存本地没上传
			} else {
				dm_op.Update_DJtype(djid, -1);
			}
			msg.what=flag;
			msg.setTarget(mHandler2);
            mHandler2.sendMessage(msg);
		}
	};
	private Handler mHandler2 = new Handler(){
		 public void handleMessage(Message msg){
			 pro_Dialog.dismiss();
				if(msg.what>0){
//					if (mSharePreferences.getBoolean(
//							ShareprefenceBean.FIRST_PROMPT_DJ, true)) {
//						AlertDialog.Builder builder = new AlertDialog.Builder(
//								PanDianActivity.this);
//						builder.setMessage("已上传的单据需在网页端审核单据。");
//						builder.setNegativeButton("确定",
//								new DialogInterface.OnClickListener() {
//
//									@Override
//									public void onClick(DialogInterface dialog,
//											int which) {
//										// TODO Auto-generated method stub
//										djid = "";
//										finish();
//										dialog.dismiss();
//									}
//								});
//						builder.setPositiveButton("不再提示",
//								new DialogInterface.OnClickListener() {
//
//									@Override
//									public void onClick(DialogInterface dialog,
//											int which) {
//										// TODO Auto-generated method stub
//										mSharePreferences
//												.edit()
//												.putBoolean(
//														ShareprefenceBean.FIRST_PROMPT_DJ,
//														false).commit();
//										djid = "";
//										finish();
//										dialog.dismiss();
//									}
//								});
//						builder.create().show();
//					}else{
//						Toast.makeText(PanDianActivity.this , "单据上传成功", Toast.LENGTH_SHORT).show();
//						djid = "";
//						finish();
//					}
					Toast.makeText(PanDianActivity.this , "单据上传成功", Toast.LENGTH_SHORT).show();
					djid = "";
					finish();
				}else if(msg.what==-502){
					AlertDialog.Builder builder=new AlertDialog.Builder(PanDianActivity.this);
					builder.setMessage("账面数有误,请修改盘点单后再上传。");
					builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO 自动生成的方法存根
							dialog.dismiss();
						}
					});
					builder.setPositiveButton("修改账面数", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO 自动生成的方法存根
							Intent intent = new Intent(PanDianActivity.this, DJMXCheckActivity.class);
							intent.putExtra("op_type", op_type);
							intent.putExtra("djid", djid);
							intent.putExtra("ckid", ckid);
							intent.putExtra("dh", dhEditText.getText().toString());
							intent.putExtra("dacttype", "");
							startActivity(intent);
						}
					});
					builder.create().show();
				}else{
						AlertDialog.Builder builder=new AlertDialog.Builder(PanDianActivity.this);
						int ls=msg.what;
						switch(msg.what){
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
						case -11:
							builder.setMessage("服务端异常。");
							break;
						case -12:
							builder.setMessage("数据解析异常。");
							break;
						case -2:
							builder.setMessage("权限不足,不能上传单据。");
							break;
						case -501:
							builder.setMessage("货品出库不足，请修改单据在上传");
							break;
							default:
								break;
						}
						builder.setPositiveButton("确定", new DialogInterface.OnClickListener(){

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});
						builder.create().show();
				}
		        }
	 };
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
		 if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			 if(operationFlag || Integer.parseInt(pandian_allTextView.getText().toString())!=0){
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
