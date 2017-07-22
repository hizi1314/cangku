package com.guantang.cangkuonline.activity;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.application.MyApplication;
import com.guantang.cangkuonline.database.DataBaseCheckMethod;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.database.DataBaseMethod;
import com.guantang.cangkuonline.database.DataBaseOperateMethod;
import com.guantang.cangkuonline.dialog.CommonDialog;
import com.guantang.cangkuonline.dialog.CommonDialog.DialogContentListener;
import com.guantang.cangkuonline.helper.EditHelper;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;
import com.guantang.cangkuonline.webservice.WebserviceImport;
import com.guantang.cangkuonline.webservice.WebserviceImport_more;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class AddOrderActivity extends Activity implements OnClickListener {

	private ImageButton backImgBtn;
	private TextView yqdhsjTxtView, ask_for_depTxtView,
			supplierTxtView, yonghunameTxtView,sqrTxtView, titleTxtView,
			hptotalTxtView, totalTxtView,totalmoneyTxtView;
	private EditText ddhEdit, lxphoneEdit, bzEdit, yfkEdit,lxrEdit;
	private LinearLayout addhpLinearLayout, tongjilayout,depLayout,supplierLayout,sqrLayout;
	private RelativeLayout addedLinearLayout;
	private ImageView addhp1ImgView;
	private Button commitBtn;
	private Dialog dialog;
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	private DataBaseMethod dm = new DataBaseMethod(this);
	private DataBaseOperateMethod dm_op = new DataBaseOperateMethod(this);
	private DataBaseCheckMethod dm_ck = new DataBaseCheckMethod(this);
	private SimpleDateFormat formatter2 = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private List<Map<String, Object>> getList = new ArrayList<Map<String, Object>>();
	private List<Map<String,Object>> EmployeeList = new ArrayList<Map<String,Object>>();
	
	private int dirc = 0; // 0 表示采购订单，1表示销售订单
	private String ddid = "";
	private String sqrid = "";
	private String dwid = "";
	private String depid = "";
	private ProgressDialog pro_Dialog;

	private String[] str = { DataBaseHelper.Status, DataBaseHelper.DWName,
			DataBaseHelper.LXR, DataBaseHelper.OrderIndex,
			DataBaseHelper.Trades, DataBaseHelper.dirc, DataBaseHelper.Sqdt,
			DataBaseHelper.TEL, DataBaseHelper.zje, DataBaseHelper.yfje,
			DataBaseHelper.syje, DataBaseHelper.BZ, DataBaseHelper.sqr,
			DataBaseHelper.ReqDate, DataBaseHelper.DepName, DataBaseHelper.DWID,DataBaseHelper.sqrID};
	// 订单明细表字段
	private String[] str1 = { DataBaseHelper.HPID, 
			DataBaseHelper.HPMC, DataBaseHelper.HPBM, DataBaseHelper.GGXH,
			DataBaseHelper.SL, DataBaseHelper.DJ, DataBaseHelper.ZJ,
			DataBaseHelper.ddirc };
	private String[] str2 = { "status", "dwName", "lxr", "orderIndex",
			"trades", "dirc", "sqdt", "tel", "zje", "yfje", "syje", "bz",
			"sqr", "ReqDate", "depName", "dwid","sqrID" };
	private String[] str3 = { "hpid", "hpmc", "hpbm", "ggxh", "sl",
			"dj", "zj", "ddirc" };

	/**
	 * 是否对单据进行了操作的标志，以便确定是否保存该单据，true表示操作了该单据，false表示没有操作该单据。
	 * */
	private Boolean operationFlag = false;
	
	private volatile Semaphore mSemaphore = new Semaphore(1);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cg_order_layout);
		initView();
		init();
	}

	@Override
	protected void onStart() {
		// TODO 自动生成的方法存根
		super.onStart();
		float count = 0;
		getList = dm_op.Gt_OrderDetails(ddid, str1);
		hptotalTxtView.setText(String.valueOf(getList.size()));
		Iterator<Map<String, Object>> it = getList.iterator();

		if (getList.size() > 0) {
			addedLinearLayout.setVisibility(View.VISIBLE);
			addhpLinearLayout.setVisibility(View.GONE);
		} else {
			addedLinearLayout.setVisibility(View.GONE);
			addhpLinearLayout.setVisibility(View.VISIBLE);
		}
		
		totalmoneyTxtView.setText(dm_ck.GtAmount_DDzje(ddid));
		
		while (it.hasNext()) {
			Map<String, Object> map = it.next();
			if (!map.get(DataBaseHelper.SL).toString().isEmpty()) {
				count += Float.parseFloat(map.get(DataBaseHelper.SL).toString());
			}
		}

		totalTxtView.setText(String.valueOf(count));
		if (!getList.isEmpty()) {
			operationFlag = true;
		}
	}

	public void initView() {
		backImgBtn = (ImageButton) findViewById(R.id.back);
		titleTxtView = (TextView) findViewById(R.id.title);
		yqdhsjTxtView = (TextView) findViewById(R.id.commitdate);
		ask_for_depTxtView = (TextView) findViewById(R.id.ask_for_dep);
		sqrTxtView = (TextView) findViewById(R.id.sqr);
		supplierTxtView = (TextView) findViewById(R.id.supplier);
		lxrEdit = (EditText) findViewById(R.id.lxr);
		yonghunameTxtView = (TextView) findViewById(R.id.yonghuname);
		ddhEdit = (EditText) findViewById(R.id.ddhEdit);
		lxphoneEdit = (EditText) findViewById(R.id.lxphoneEdit);
		bzEdit = (EditText) findViewById(R.id.bzEdit);
		yfkEdit = (EditText) findViewById(R.id.yfkEdit);
		addhpLinearLayout = (LinearLayout) findViewById(R.id.addhp);
		addedLinearLayout = (RelativeLayout) findViewById(R.id.added);
		tongjilayout = (LinearLayout) findViewById(R.id.tongjilayout);
		addhp1ImgView = (ImageView) findViewById(R.id.addhp1);
		totalTxtView = (TextView) findViewById(R.id.total);
		hptotalTxtView = (TextView) findViewById(R.id.hptotal);
		totalmoneyTxtView = (TextView) findViewById(R.id.totalmoney);
		commitBtn = (Button) findViewById(R.id.commitBtn);
		depLayout = (LinearLayout) findViewById(R.id.depLayout);
		supplierLayout = (LinearLayout) findViewById(R.id.supplierLayout);
		sqrLayout = (LinearLayout) findViewById(R.id.sqrLayout);
		
		depLayout.setOnClickListener(this);
		supplierLayout.setOnClickListener(this);
		sqrLayout.setOnClickListener(this);
		backImgBtn.setOnClickListener(this);
		yqdhsjTxtView.setOnClickListener(this);
		addhpLinearLayout.setOnClickListener(this);
		tongjilayout.setOnClickListener(this);
		addhp1ImgView.setOnClickListener(this);
		commitBtn.setOnClickListener(this);
		
		ddhEdit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO 自动生成的方法存根
				dm_ck.update_order(DataBaseHelper.OrderIndex, s.toString().trim()
						.replace("'", ""), ddid);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO 自动生成的方法存根

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO 自动生成的方法存根
				if (s.toString().indexOf("'") > -1) {
					int pos = s.toString().indexOf("'");
					s.delete(pos, pos + 1);
				}
				if (!s.toString().equals("")) {
					operationFlag = true;
				}
			}
		});
		
		lxrEdit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO 自动生成的方法存根
				dm_ck.update_order(DataBaseHelper.LXR, s.toString().trim()
						.replace("'", ""), ddid);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO 自动生成的方法存根

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO 自动生成的方法存根
				if (s.toString().indexOf("'") > -1) {
					int pos = s.toString().indexOf("'");
					s.delete(pos, pos + 1);
				}
				if (!s.toString().equals("")) {
					operationFlag = true;
				}
			}
		});
		
		bzEdit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO 自动生成的方法存根
				dm_ck.update_order(DataBaseHelper.BZ, s.toString().trim()
						.replace("'", ""), ddid);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO 自动生成的方法存根

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO 自动生成的方法存根
				if (s.toString().indexOf("'") > -1) {
					int pos = s.toString().indexOf("'");
					s.delete(pos, pos + 1);
				}
				if (!s.toString().equals("")) {
					operationFlag = true;
				}
			}
		});

		lxphoneEdit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO 自动生成的方法存根
				dm_ck.update_order(DataBaseHelper.TEL, s.toString().trim()
						.replace("'", ""), ddid);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO 自动生成的方法存根

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO 自动生成的方法存根
				if (s.toString().indexOf("'") > -1) {
					int pos = s.toString().indexOf("'");
					s.delete(pos, pos + 1);
				}
				if (!s.toString().equals("")) {
					operationFlag = true;
				}
			}
		});
		
		yfkEdit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO 自动生成的方法存根
				dm_ck.update_order(DataBaseHelper.yfje, s.toString().trim()
						.replace("'", ""), ddid);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO 自动生成的方法存根

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO 自动生成的方法存根
				if (s.toString().indexOf("'") > -1) {
					int pos = s.toString().indexOf("'");
					s.delete(pos, pos + 1);
				}
				if (!s.toString().equals("")) {
					operationFlag = true;
				}
			}
		});
		
		if (WebserviceImport.isOpenNetwork(this)) {
			new GetEmployeeAsyncTask().execute();
		} else {
			Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
		}
	}

	public void init() {
		Intent intent = getIntent();
		dirc = intent.getIntExtra("dirc", 0);
		if (dirc == 0) {
			titleTxtView.setText("采购订单");
		} else if (dirc == 1) {
			titleTxtView.setText("销售订单");
			yonghunameTxtView.setText("客户名称");
		}

		yqdhsjTxtView.setText(formatter.format(System.currentTimeMillis()));
		if (TextUtils.isEmpty(dm_op.searchUncompleteDD(dirc))) {
			dm_op.insert_into(
					DataBaseHelper.TB_Order,
					new String[] { DataBaseHelper.Sqdt, DataBaseHelper.dirc },
					new String[] {
							formatter2.format(System.currentTimeMillis()),
							String.valueOf(dirc)});
			ddid = dm_op.GtMax_DDDJID();
			dm_op.Update_DDDJtype(ddid, -5);// type -5 订单未完成,-4 未上传,-3 已上传
		} else {
			ddid = dm_op.searchUncompleteDD(dirc);
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("有未完成出单据，是否继续上次单据？");
			builder.setCancelable(false);
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO 自动生成的方法存根
							dm_op.Del_Order(ddid);
							dm_op.Del_OrderDetails(ddid);
							dm_op.insert_into(
									DataBaseHelper.TB_Order,
									new String[] { DataBaseHelper.Sqdt,
											DataBaseHelper.dirc },
									new String[] {
											formatter2.format(System
													.currentTimeMillis()),
											String.valueOf(dirc) });
							ddid = dm_op.GtMax_DDDJID();
							dm_op.Update_DDDJtype(ddid, -5);// type -5 订单未完成,-4 未上传,-3 已上传
							dialog.dismiss();
						}
					});
			builder.setPositiveButton("继续",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO 自动生成的方法存根

							List<Map<String, Object>> list1 = dm_op.Gt_Order(
									ddid, str);
							Map<String, Object> map1 = list1.get(0);
							
							if(map1.get(DataBaseHelper.ReqDate) == null || map1.get(DataBaseHelper.ReqDate).toString().equals("")){
								yqdhsjTxtView.setText(formatter.format(System.currentTimeMillis()));
							}else{
								operationFlag = true;
								yqdhsjTxtView.setText(map1.get(DataBaseHelper.ReqDate).toString());
							}
							if (map1.get(DataBaseHelper.OrderIndex) == null
									|| map1.get(DataBaseHelper.OrderIndex)
											.toString().equals("")) {
								ddhEdit.setText("");
							} else {
								operationFlag = true;
								ddhEdit.setText(map1.get(
										DataBaseHelper.OrderIndex).toString());
							}
							
							if(map1.get(DataBaseHelper.DepName) == null || map1.get(DataBaseHelper.DepName).toString().equals("")){
								ask_for_depTxtView.setText("");
							}else{
								operationFlag = true;
								ask_for_depTxtView.setText(map1.get(DataBaseHelper.DepName).toString());
							}
							if (map1.get(DataBaseHelper.DWName) == null
									|| map1.get(DataBaseHelper.DWName).toString().equals("")) {
								supplierTxtView.setText("");
							} else {
								operationFlag = true;
								supplierTxtView.setText(map1.get(
										DataBaseHelper.DWName).toString());
							}
							if(map1.get(DataBaseHelper.sqr)==null || map1.get(DataBaseHelper.sqr).toString().equals("")){
								sqrTxtView.setText("");
							}else{
								operationFlag = true;
								sqrTxtView.setText(map1.get(DataBaseHelper.sqr).toString());
							}
							if (map1.get(DataBaseHelper.LXR) == null
									|| map1.get(DataBaseHelper.LXR).toString()
											.equals("")) {
								lxrEdit.setText("");
							} else {
								operationFlag = true;
								lxrEdit.setText(map1.get(DataBaseHelper.LXR)
										.toString());
							}
							if (map1.get(DataBaseHelper.TEL) == null
									|| map1.get(DataBaseHelper.TEL).toString()
											.equals("")) {
								lxphoneEdit.setText("");
							} else {
								operationFlag = true;
								lxphoneEdit.setText(map1
										.get(DataBaseHelper.TEL).toString());
							}
							if (map1.get(DataBaseHelper.BZ) == null
									|| map1.get(DataBaseHelper.BZ).toString()
											.equals("")) {
								bzEdit.setText("");
							} else {
								operationFlag = true;
								bzEdit.setText(map1.get(DataBaseHelper.BZ)
										.toString());
							}
							if (map1.get(DataBaseHelper.yfje) == null
									|| map1.get(DataBaseHelper.yfje).toString()
											.equals("")) {
								yfkEdit.setText("");
							} else {
								operationFlag = true;
								yfkEdit.setText(map1.get(DataBaseHelper.yfje)
										.toString());
							}
							if (map1.get(DataBaseHelper.ReqDate) == null
									|| map1.get(DataBaseHelper.ReqDate)
											.toString().equals("")) {
//								yqdhsjTxtView.setText("");
							} else {
								operationFlag = true;
								yqdhsjTxtView.setText(map1.get(DataBaseHelper.ReqDate).toString());
							}
							dialog.dismiss();
						}
					});
			builder.create().show();
		}
	}

	public void exitDJDialog() {
		AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
		View view = LayoutInflater.from(this).inflate(R.layout.dialogbutton,
				null);
		Button exit_unsaveBtn = (Button) view.findViewById(R.id.exit_unsaveBtn);
		Button exit_saveBtn = (Button) view.findViewById(R.id.exit_saveBtn);
		Button stayBtn = (Button) view.findViewById(R.id.stayBtn);
		exit_unsaveBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				dm_op.Del_OrderDetails(ddid);
				dm_op.Del_Order(ddid);
				ddid = "";
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
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.back:
			if (operationFlag) {
				exitDJDialog();
			} else {
				dm_op.Del_OrderDetails(ddid);
				dm_op.Del_Order(ddid);
				ddid = "";
				finish();
			}
			break;
		case R.id.commitdate:
			Calendar calendar = Calendar.getInstance();
			DatePickerDialog dateDialog = new DatePickerDialog(this,
					new OnDateSetListener() {
						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							// TODO 自动生成的方法存根
							yqdhsjTxtView.setText(year+ "-"+ new DecimalFormat("00").format(monthOfYear + 1)
									+ "-"+ new DecimalFormat("00").format(dayOfMonth));
							dm_ck.update_order(DataBaseHelper.ReqDate,year+ "-"+ new DecimalFormat("00")
									.format(monthOfYear + 1)+ "-"+ new DecimalFormat("00").format(dayOfMonth), ddid);
							 operationFlag=true;
						}
					}, calendar.get(Calendar.YEAR),
					calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH));
			dateDialog.show();
			break;
		case R.id.depLayout:
			intent.setClass(this, DepListActivity.class);
			startActivityForResult(intent, 2);
			break;
		case R.id.supplierLayout:
			intent.setClass(this, DwListActivity.class);
			startActivityForResult(intent, 1);
			break;
		case R.id.sqrLayout:
			CommonDialog sqrDialog = new CommonDialog(this, R.layout.select_dialog, R.style.yuanjiao_activity);
			sqrDialog.setDialogContentListener(new DialogContentListener() {
				
				@Override
				public void contentExecute(View parent, final Dialog dialog, Object[] objs) {
					// TODO 自动生成的方法存根
					TextView mTextView = (TextView) parent.findViewById(R.id.titletextview);
					ListView myListView = (ListView) parent.findViewById(R.id.mylist);
					Button confirmBtn = (Button) parent.findViewById(R.id.confirmBtn);
					mTextView.setText("选择申请人");
					confirmBtn.setVisibility(View.GONE);
					
					SimpleAdapter adapter = new SimpleAdapter(AddOrderActivity.this, (List<Map<String,Object>>) objs[0], R.layout.popupwindow_list_textview,
							new String[] {"name"}, new int[] {R.id.textview_popup});
					myListView.setAdapter(adapter);
					myListView.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							// TODO 自动生成的方法存根
							Map<String,Object> map = (Map<String, Object>) arg0.getAdapter().getItem(arg2);
							sqrTxtView.setText(map.get("name").toString());
							sqrid = map.get("id").toString();
							operationFlag = true;
							dm_ck.update_order(DataBaseHelper.sqr,map.get("name").toString(), ddid);
							dialog.dismiss();
						}
					});	
				}
			}, EmployeeList);
			sqrDialog.show();
			break;
		case R.id.addhp:
			intent.setClass(this, OrderHP_choseActivity.class);
			intent.putExtra("ddh", ddhEdit.getText().toString());
			intent.putExtra("dirc", dirc);
			intent.putExtra("ddid", ddid);
			startActivity(intent);
			break;
		case R.id.tongjilayout:
			intent.setClass(this, OrderDetailActivity.class);
			intent.putExtra("ddh", ddhEdit.getText().toString());
			intent.putExtra("ddid", ddid);
			intent.putExtra("dirc", dirc);
			startActivity(intent);
			break;
		case R.id.addhp1:
			intent.setClass(this, OrderHP_choseActivity.class);
			intent.putExtra("ddh", ddhEdit.getText().toString());
			intent.putExtra("ddid", ddid);
			intent.putExtra("dirc", dirc);
			startActivity(intent);
			break;
		case R.id.commitBtn:
			if (totalTxtView.getText().toString().equals("0")) {
				Toast.makeText(AddOrderActivity.this, "请先添加货品",
						Toast.LENGTH_SHORT).show();
			} else {
				if (WebserviceImport.isOpenNetwork(this)) {
					pro_Dialog = ProgressDialog.show(this, null, "正在上传数据……");
					saveDD();
					new AddDJAsynctask().execute();
				} else {
					Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
				}
			}

			break;
		}
	}
	
	class GetEmployeeAsyncTask extends AsyncTask<Void, Void, String>{
		
		@Override
		protected String doInBackground(Void... params) {
			// TODO 自动生成的方法存根
			try {
				mSemaphore.acquire();
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			String jsonString = WebserviceImport_more.GetEmployee_1_0();
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
					JSONObject dataJSONObject = jsonObject.getJSONObject("Data");
					JSONArray dsJSONArray = dataJSONObject.getJSONArray("ds");
					int length = dsJSONArray.length();
					for(int i = 0; i<length; i++){
						Map<String,Object> map = new HashMap<String, Object>();
						JSONObject littleObject = dsJSONArray.getJSONObject(i);
						map.put("id", littleObject.get("id").toString());
						map.put("name", littleObject.get("name").toString());
						EmployeeList.add(map);
					}
					break;
				default:
					Toast.makeText(AddOrderActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				}
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}finally{
				mSemaphore.release();
			}
		}
		
	}
	
	class AddDJAsynctask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			// TODO 自动生成的方法存根
			String jsonString = "";
			List<Map<String, Object>> ls2 = dm_op.Gt_Order(ddid, str);
			if (!ls2.isEmpty()) {
				JSONObject dddjJsonObject = new JSONObject(ls2.get(0));
				List<Map<String, Object>> ls3 = dm_op.Gt_OrderDetails(ddid,
						str1);
				JSONArray ddmxJsonOArray = new JSONArray();
				if (!ls3.isEmpty()) {
					int length = ls3.size();
					for (int i = 0; i < length; i++) {
						JSONObject ddmxJsonObject = new JSONObject(ls3.get(i));
						ddmxJsonOArray.put(ddmxJsonObject);
					}
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("order", dddjJsonObject);
				map.put("details", ddmxJsonOArray);
				JSONObject ddJsonObject = new JSONObject(map);
				jsonString = WebserviceImport_more.AddDDDJ_1_0(
						ddJsonObject.toString(), ddhEdit.getText().toString(),
						MyApplication.getInstance().getSharedPreferences());
			}

			return jsonString;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO 自动生成的方法存根
			super.onPostExecute(result);
			pro_Dialog.dismiss();
			try {
				JSONObject jsonObject = new JSONObject(result);
				switch (jsonObject.getInt("Status")) {
				case 1:
					dm_op.Update_DDDJtype(ddid, -3);// type -5 订单未完成,-4 未上传,-3
													// 已上传.
					Toast.makeText(AddOrderActivity.this,
							jsonObject.getString("Message"), Toast.LENGTH_SHORT)
							.show();
					finish();
					break;
				case -1:
					dm_op.Update_DDDJtype(ddid, -4);// type -5 订单未完成,-4
													// 未上传,-3已上传.
					Toast.makeText(AddOrderActivity.this,
							jsonObject.getString("Message"), Toast.LENGTH_SHORT)
							.show();
					break;
				case -2:
					dm_op.Update_DDDJtype(ddid, -4);// type -5 订单未完成,-4
													// 未上传,-3已上传.
					Toast.makeText(AddOrderActivity.this,
							jsonObject.getString("Message"), Toast.LENGTH_SHORT)
							.show();
					break;
				case -3:
					dm_op.Update_DDDJtype(ddid, -4);// type -5 订单未完成,-4
													// 未上传,-3已上传.
					Toast.makeText(AddOrderActivity.this,
							jsonObject.getString("Message"), Toast.LENGTH_SHORT)
							.show();
					break;
				case -4:
					dm_op.Update_DDDJtype(ddid, -4);// type -5 订单未完成,-4
													// 未上传,-3已上传.
					Toast.makeText(AddOrderActivity.this,
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

	public void saveDD() {
		String date, zje, syje, yfje;
		String details2 = "", hpstr = "";
		hpstr = dm.Gethp(new String[] { DataBaseHelper.HPMC },
						getList.get(0).get(DataBaseHelper.HPID).toString())
				.get(0).get(DataBaseHelper.HPMC).toString();
		if (hptotalTxtView.getText().toString().equals("1")) {
			details2 = hpstr;
		} else if (hptotalTxtView.getText().toString().equals("0")) {
			details2 = "无货品明细";
		} else {
			details2 = hpstr + " 等" + hptotalTxtView.getText().toString()
					+ "种货品";
		}
		zje = dm_ck.GtAmount_DDzje(ddid);
		yfje = yfkEdit.getText().toString();
		if (yfje.equals("")) {
			yfje = "0";
		}
		syje = String.valueOf(Float.parseFloat(zje) - Float.parseFloat(yfje));
		if (ddhEdit.getText().toString() == null) {
			ddhEdit.setText("");
		}
		if (supplierTxtView.getText().toString() == null) {
			supplierTxtView.setText("");
		}
		if (lxrEdit.getText().toString() == null) {
			lxrEdit.setText("");
		}
		if (lxphoneEdit.getText().toString() == null) {
			lxphoneEdit.setText("");
		}
		if (bzEdit.getText().toString() == null) {
			bzEdit.setText("");
		}
		dm_op.Update_Order(
				ddid,
				ddhEdit.getText().toString(),
				supplierTxtView.getText().toString(),
				"-3",
				bzEdit.getText().toString(),
				lxrEdit.getText().toString(),
				String.valueOf(dirc),
				yqdhsjTxtView.getText().toString(),
				dwid,
				ask_for_depTxtView.getText().toString(),
				details2,
				formatter2.format(System.currentTimeMillis()),
				lxphoneEdit.getText().toString(),
				zje,
				yfje,
				syje,
				sqrTxtView.getText().toString(),sqrid);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO 自动生成的方法存根
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			if (resultCode == 1) {
				Map<String,Object> map = (Map<String, Object>) data.getSerializableExtra("dwmap");
				supplierTxtView.setText(map.get(DataBaseHelper.DWName).toString());
				dwid = data.getStringExtra("dwid");
				dm_ck.update_order(DataBaseHelper.DWName, map.get(DataBaseHelper.DWName).toString(), ddid);
				dm_ck.update_order(DataBaseHelper.DWID, data.getStringExtra("dwid"), ddid);
				if(!supplierTxtView.getText().toString().equals("")){
					operationFlag=true;
				}
			}
		} else if (requestCode == 2) {
			if (resultCode == 1) {
				ask_for_depTxtView.setText(data.getStringExtra("depname"));
				depid = data.getStringExtra("depid");
				dm_ck.update_order(DataBaseHelper.DepName, data.getStringExtra("depname"), ddid);
				if(!ask_for_depTxtView.getText().toString().equals("")){
					operationFlag=true;
				}
			}
		}
	}

}
