package com.guantang.cangkuonline.activity;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.dialog.CommonDialog;
import com.guantang.cangkuonline.dialog.CommonDialog.DialogContentListener;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class OrderfilterActivity extends Activity implements OnClickListener{
	
	private ImageButton backImgBtn;
	private LinearLayout ddlelayout,dwlayout,deplayout;
	private TextView ddlbTextView,dwTextView,depTxtView,sqDatefromTextView,sqDatetoTextView,dhDatefromTextView,dhDatetoTextView;
	private EditText dddh_edit;
	private Button resetBtn,confirmBtn;
	private Calendar calendar;
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	private String dirc = "";
	private String dwid = "";
	private String sql = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_orderfilter);
		initView();
		init();
	}
	
	public void initView(){
		backImgBtn = (ImageButton) findViewById(R.id.back);
		ddlelayout = (LinearLayout) findViewById(R.id.ddlelayout);
		dwlayout = (LinearLayout) findViewById(R.id.dwlayout);
		deplayout = (LinearLayout) findViewById(R.id.deplayout);
		ddlbTextView = (TextView) findViewById(R.id.ddlbTextView);
		dwTextView = (TextView) findViewById(R.id.dwTextView);
		sqDatefromTextView = (TextView) findViewById(R.id.sqDatefrom);
		sqDatetoTextView = (TextView) findViewById(R.id.sqDateto);
		dhDatefromTextView = (TextView) findViewById(R.id.dhDatefrom);
		dhDatetoTextView = (TextView) findViewById(R.id.dhDateto);
		depTxtView =  (TextView) findViewById(R.id.depTextView);
		dddh_edit = (EditText) findViewById(R.id.dddh_edit);
		resetBtn = (Button) findViewById(R.id.reset);
		confirmBtn = (Button) findViewById(R.id.confirm);
		
		backImgBtn.setOnClickListener(this);
		deplayout.setOnClickListener(this);
		ddlelayout.setOnClickListener(this);
		dwlayout.setOnClickListener(this);
		sqDatefromTextView.setOnClickListener(this);
		sqDatetoTextView.setOnClickListener(this);
		dhDatefromTextView.setOnClickListener(this);
		dhDatetoTextView.setOnClickListener(this);
		resetBtn.setOnClickListener(this);
		confirmBtn.setOnClickListener(this);
	}
	
	public void init(){
		sqDatefromTextView.setText(formatter.format(System.currentTimeMillis()));
		sqDatetoTextView.setText(formatter.format(System.currentTimeMillis()));
//		dhDatefromTextView.setText(formatter.format(System.currentTimeMillis()));
//		dhDatetoTextView.setText(formatter.format(System.currentTimeMillis()));
		
	}
	
	public void setEmpty(){
		ddlbTextView.setText("");
		dwTextView.setText("");
		depTxtView.setText("");
		dddh_edit.setText("");
		sqDatefromTextView.setText(formatter.format(System.currentTimeMillis()));
		sqDatetoTextView.setText(formatter.format(System.currentTimeMillis()));
		dhDatefromTextView.setText("");
		dhDatetoTextView.setText("");
		dirc = "0";
		dwid = "";
		sql = "";
	}
	
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		Intent intent = new Intent();
		switch(v.getId()){
		case R.id.back:
			finish();
			break;
		case R.id.ddlelayout:
			CommonDialog sqrDialog = new CommonDialog(this, R.layout.select_dialog, R.style.yuanjiao_activity);
			sqrDialog.setDialogContentListener(new DialogContentListener() {
				
				@Override
				public void contentExecute(View parent, final Dialog dialog, Object[] objs) {
					// TODO 自动生成的方法存根
					TextView mTextView = (TextView) parent.findViewById(R.id.titletextview);
					ListView myListView = (ListView) parent.findViewById(R.id.mylist);
					Button confirmBtn = (Button) parent.findViewById(R.id.confirmBtn);
					mTextView.setText("选择订单类型");
					confirmBtn.setVisibility(View.GONE);
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(OrderfilterActivity.this,R.layout.popupwindow_list_textview, new String[]{"采购订单","销售订单"});
					myListView.setAdapter(adapter);
					myListView.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							// TODO 自动生成的方法存根
							String str = arg0.getAdapter().getItem(arg2).toString();
							ddlbTextView.setText(str);
							if(arg2 ==0){
								dirc = "0";
							}else if(arg2 ==1){
								dirc = "1";
							}
							dialog.dismiss();
						}
					});	
				}
			});
			sqrDialog.show();
			break;
		case R.id.dwlayout:
			intent.setClass(this, DwListActivity.class);
			startActivityForResult(intent, 1);
			break;
		case R.id.deplayout:
			intent.setClass(this, DepListActivity.class);
			startActivityForResult(intent, 2);
			break;
		case R.id.sqDatefrom:
			calendar = Calendar.getInstance();
			DatePickerDialog dateDialog1 = new DatePickerDialog(this, new OnDateSetListener() {
				
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear,
						int dayOfMonth) {
					// TODO 自动生成的方法存根
					sqDatefromTextView.setText(year +"-"+ new DecimalFormat("00").format(monthOfYear+1)+"-"+ new DecimalFormat("00").format(dayOfMonth));
				}
			}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
			dateDialog1.show();
			break;
		case R.id.sqDateto:
			calendar = Calendar.getInstance();
			DatePickerDialog dateDialog2 = new DatePickerDialog(this, new OnDateSetListener() {
				
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear,
						int dayOfMonth) {
					// TODO 自动生成的方法存根
					sqDatetoTextView.setText(year +"-"+ new DecimalFormat("00").format(monthOfYear+1)+"-"+new DecimalFormat("00").format(dayOfMonth));
				}
			}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
			dateDialog2.show();
			break;
		case R.id.dhDatefrom:
			calendar = Calendar.getInstance();
			DatePickerDialog dateDialog3 = new DatePickerDialog(this, new OnDateSetListener() {
				
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear,
						int dayOfMonth) {
					// TODO 自动生成的方法存根
					dhDatefromTextView.setText(year + "-" + new DecimalFormat("00").format(monthOfYear+1)+"-"+new DecimalFormat("00").format(dayOfMonth));
				}
			}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
			dateDialog3.show();
			break;
		case R.id.dhDateto:
			calendar = Calendar.getInstance();
			DatePickerDialog dateDialog4 = new DatePickerDialog(this, new OnDateSetListener() {
				
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear,
						int dayOfMonth) {
					// TODO 自动生成的方法存根
					dhDatetoTextView.setText(year + "-" + new DecimalFormat("00").format(monthOfYear+1)+"-"+new DecimalFormat("00").format(dayOfMonth));
				}
			}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
			dateDialog4.show();
			break;
		case R.id.reset:
			setEmpty();
			break;
		case R.id.confirm:
			String[] str2 = { "id","status", "dwName", "lxr", "orderIndex",
					"trades", "dirc", "sqdt", "tel", "zje", "yfje", "syje", "bz",
					"sqr", "ReqDate", "depName", "dwid","sqrID" };
			if(!dirc.equals("")){
				sql = sql+" and dirc = "+dirc+" ";
			}
			
			if(!dwid.equals("")){
				sql = sql+" and dwid = "+dwid+" ";
			}
			
			if(!depTxtView.getText().toString().equals("")){
				sql = sql+" and depName = '"+depTxtView.getText().toString()+"'";
			}
			
			if(!dddh_edit.getText().toString().trim().equals("")){
				sql = sql+" and orderIndex = '"+dddh_edit.getText().toString().trim()+"' ";
			}
			
			if(!dhDatefromTextView.getText().toString().equals("")){
				sql = sql + " and ReqDate >= '"+dhDatefromTextView.getText().toString()+"'";
			}
			
			if(!dhDatetoTextView.getText().toString().equals("")){
				sql = sql + " and ReqDate >= '"+dhDatetoTextView.getText().toString()+"'";
			}
			
			intent.putExtra("sqfromtime", sqDatefromTextView.getText().toString());
			intent.putExtra("sqtotime", sqDatetoTextView.getText().toString());
			intent.putExtra("sql", sql);
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
				Map<String,Object> map = (Map<String, Object>) data.getSerializableExtra("dwmap");
				dwTextView.setText(map.get(DataBaseHelper.DWName).toString());
				dwid = data.getStringExtra("dwid");
			}
		}else if (requestCode == 2) {
			if (resultCode == 1) {
				depTxtView.setText(data.getStringExtra("depname"));
			}
		}
	}
	
}
