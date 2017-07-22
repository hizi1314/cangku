package com.guantang.cangkuonline.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.application.MyApplication;
import com.guantang.cangkuonline.database.DataBaseCheckMethod;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.database.DataBaseOperateMethod;
import com.guantang.cangkuonline.static_constant.PathConstant;
import com.guantang.cangkuonline.webservice.WebserviceImport;
import com.guantang.cangkuonline.webservice.WebserviceImport_more;

public class SettingckActivity extends Activity implements OnClickListener{
	private ImageView backImgView;
	private EditText cknameEdit,fzrEdit,telEdit,bzEdit;
	private TextView defaultRuTypeTxtView,defaultChuTypeTxtView;
	private Button confirmBtn;
	private ProgressDialog pro_Dialog;
	private String[] str14 = { DataBaseHelper.ID, DataBaseHelper.CKMC,
			DataBaseHelper.FZR, DataBaseHelper.TEL,DataBaseHelper.INACT,
			DataBaseHelper.OUTACT, DataBaseHelper.BZ };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addck);
		initView();
	}
	
	public void initView(){
		backImgView = (ImageView) findViewById(R.id.back);
		cknameEdit = (EditText) findViewById(R.id.cknameEdit);
		fzrEdit = (EditText) findViewById(R.id.fzrEdit);
		telEdit = (EditText) findViewById(R.id.telEdit);
		bzEdit = (EditText) findViewById(R.id.bzEdit);
		defaultRuTypeTxtView = (TextView) findViewById(R.id.defaultRuTypeTxtView);
		defaultChuTypeTxtView = (TextView) findViewById(R.id.defaultChuTypeTxtView);
		confirmBtn = (Button) findViewById(R.id.confirmBtn);
		
		backImgView.setOnClickListener(this);
		confirmBtn.setOnClickListener(this);
		defaultRuTypeTxtView.setOnClickListener(this);
		defaultChuTypeTxtView.setOnClickListener(this);
	}
	
	public void setEmpty(){
		cknameEdit.setText("");
		fzrEdit.setText("");
		telEdit.setText("");
		bzEdit.setText("");
		defaultRuTypeTxtView.setText("");
		defaultChuTypeTxtView.setText("");
		
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		DataBaseCheckMethod dm_cm = new DataBaseCheckMethod(this);
		List<Map<String, Object>> lss = new ArrayList<Map<String, Object>>();//出入库类型的集合
		switch(v.getId()){
		case R.id.back:
			if(!cknameEdit.getText().toString().equals("")||!fzrEdit.getText().toString().equals("")||!telEdit.getText().toString().equals("")||!bzEdit.getText().toString().equals("")||!defaultRuTypeTxtView.getText().toString().equals("")||!defaultChuTypeTxtView.getText().toString().equals("")){
				AlertDialog.Builder builder=new AlertDialog.Builder(this);
				builder.setMessage("数据未完成添加，确定退出？");
				builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO 自动生成的方法存根
						finish();
					}
				});
				builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO 自动生成的方法存根
						dialog.dismiss();
					}
				});
				builder.create().show();
			}else{
				finish();
			}
			break;
		case R.id.defaultRuTypeTxtView:
			lss = dm_cm.Gt_Type("入库类型", DataBaseHelper.DWName);
			final String[] ss1 = new String[lss.size()];
			for (int n = 0; n < lss.size(); n++) {
				ss1[n] = (String) lss.get(n).get(DataBaseHelper.DWName);
			}
			final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
			builder1.setTitle("请选择类型");
			builder1.setSingleChoiceItems(ss1, -1,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							defaultRuTypeTxtView.setText(ss1[which]);
							dialog.dismiss();
						}
					});
			builder1.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

							dialog.dismiss();
						}
					});
			builder1.create().show();
			break;
		case R.id.defaultChuTypeTxtView:
			lss = dm_cm.Gt_Type("出库类型", DataBaseHelper.DWName);
			final String[] ss2 = new String[lss.size()];
			for (int n = 0; n < lss.size(); n++) {
				ss2[n] = (String) lss.get(n).get(DataBaseHelper.DWName);
			}
			final AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
			builder2.setTitle("请选择类型");
			builder2.setSingleChoiceItems(ss2, -1,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							defaultChuTypeTxtView.setText(ss2[which]);
							dialog.dismiss();
						}
					});
			builder2.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

							dialog.dismiss();
						}
					});
			builder2.create().show();
			break;
		case R.id.confirmBtn:
			if(cknameEdit.getText().toString().trim().equals("")){
				Toast.makeText(this, "请填写仓库名称", Toast.LENGTH_SHORT).show();
			}else{
				Map<String, Object> map = new HashMap<String, Object>();
				map.put(DataBaseHelper.CKMC, cknameEdit.getText().toString().trim());
				map.put(DataBaseHelper.FZR, fzrEdit.getText().toString().trim());
				map.put(DataBaseHelper.TEL, telEdit.getText().toString().trim());
				map.put(DataBaseHelper.INACT, defaultRuTypeTxtView.getText().toString().trim());
				map.put(DataBaseHelper.OUTACT, defaultChuTypeTxtView.getText().toString().trim());
				map.put(DataBaseHelper.BZ, bzEdit.getText().toString().trim());
				JSONObject jsonObject = new JSONObject(map);
				if(WebserviceImport.isOpenNetwork(this)){
					pro_Dialog = ProgressDialog.show(this, null, "正在添加");
					new MyAsyncTask().execute(jsonObject.toString());
				}else{
					Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
				}
			}
			break;
		}
	}
	class MyAsyncTask extends AsyncTask<String, Void, String>{
		
		@Override
		protected String doInBackground(String... params) {
			// TODO 自动生成的方法存根
			String json = WebserviceImport_more.AddCK_1_0(params[0], MyApplication.getInstance().getSharedPreferences());
			return json;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO 自动生成的方法存根
			pro_Dialog.dismiss();
			super.onPostExecute(result);
			try {
				JSONObject resultJsonObject = new JSONObject(result);
				switch(resultJsonObject.getInt("Status")){
				case 1:
					Toast.makeText(SettingckActivity.this, resultJsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					DataBaseOperateMethod dm_op = new DataBaseOperateMethod(SettingckActivity.this);
//					dm_op.insert_into1(DataBaseHelper.TB_CK,str14, new String[] {resultJsonObject.getString("Data"),cknameEdit.getText().toString().trim(),
//							fzrEdit.getText().toString().trim(),telEdit.getText().toString().trim(),defaultRuTypeTxtView.getText().toString().trim(),
//							defaultChuTypeTxtView.getText().toString().trim(),bzEdit.getText().toString().trim()}, MyApplication.getInstance().getDataBaseImport().getReadableDatabase());
					setEmpty();
					break;
				case -1:
					Toast.makeText(SettingckActivity.this, resultJsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -2:
					Toast.makeText(SettingckActivity.this, resultJsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -3:
					Toast.makeText(SettingckActivity.this, resultJsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -4:
					Toast.makeText(SettingckActivity.this, resultJsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
				break;
				case -5:
					Toast.makeText(SettingckActivity.this, resultJsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
				break;
				default:
					Toast.makeText(SettingckActivity.this, resultJsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				}
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO 自动生成的方法存根
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if(!cknameEdit.getText().toString().equals("")||!fzrEdit.getText().toString().equals("")||!telEdit.getText().toString().equals("")||!bzEdit.getText().toString().equals("")||!defaultRuTypeTxtView.getText().toString().equals("")||!defaultChuTypeTxtView.getText().toString().equals("")){
				AlertDialog.Builder builder=new AlertDialog.Builder(this);
				builder.setMessage("数据未完成添加，确定退出？");
				builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO 自动生成的方法存根
						finish();
					}
				});
				builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO 自动生成的方法存根
						dialog.dismiss();
					}
				});
				builder.create().show();
			}else{
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
