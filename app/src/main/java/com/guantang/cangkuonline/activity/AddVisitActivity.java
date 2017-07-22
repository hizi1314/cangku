package com.guantang.cangkuonline.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.webservice.WebserviceImport;
import com.guantang.cangkuonline.webservice.WebserviceImport_more;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AddVisitActivity extends Activity implements OnClickListener {

	private ImageButton backImgBtn;
	private TextView titleTxtView, commitTxtView;
	private LinearLayout dwnameLayout, dwDetailLayout;
	private TextView dwNameTxtView, lxrTxtView, telTxtView, adressTxtView;
	private EditText lxrEdit, telEdit, contentEdit, pointEdit;
	private String dwid = "";
	private ProgressDialog pro_dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addvisit);
		initView();
		init();
	}

	public void initView() {
		backImgBtn = (ImageButton) findViewById(R.id.back);
		titleTxtView = (TextView) findViewById(R.id.title);
		commitTxtView = (TextView) findViewById(R.id.commitTxtView);
		dwnameLayout = (LinearLayout) findViewById(R.id.dwnameLayout);
		dwDetailLayout = (LinearLayout) findViewById(R.id.dwDetailLayout);
		dwNameTxtView = (TextView) findViewById(R.id.dwNameTxtView);
		lxrTxtView = (TextView) findViewById(R.id.lxrTxtView);
		telTxtView = (TextView) findViewById(R.id.telTxtView);
		adressTxtView = (TextView) findViewById(R.id.adressTxtView);
		pointEdit = (EditText) findViewById(R.id.pointEdit);
		lxrEdit = (EditText) findViewById(R.id.lxrEdit);
		telEdit = (EditText) findViewById(R.id.telEdit);
		contentEdit = (EditText) findViewById(R.id.contentEdit);

		backImgBtn.setOnClickListener(this);
		commitTxtView.setOnClickListener(this);
		dwnameLayout.setOnClickListener(this);
		dwDetailLayout.setVisibility(View.GONE);
	}

	public void init() {

	}
	
	public void setEmpty(){
		dwDetailLayout.setVisibility(View.GONE);
		dwNameTxtView.setText("");
		lxrTxtView.setText("");
		telTxtView.setText("");
		adressTxtView.setText("");
		pointEdit.setText("");
		lxrEdit.setText("");
		telEdit.setText("");
		contentEdit.setText("");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.commitTxtView:
			if(dwid.equals("") && dwNameTxtView.getText().toString().equals("")){
				Toast.makeText(this, "请选择单位", Toast.LENGTH_SHORT).show();
				return;
			}
			if(contentEdit.getText().toString().trim().equals("")){
				Toast.makeText(this, "请填写拜访记录", Toast.LENGTH_SHORT).show();
				return;
			}
			if(WebserviceImport.isOpenNetwork(this)){
				pro_dialog = ProgressDialog.show(this, null, "正在提交");
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("dwid", dwid);
				map.put("content", contentEdit.getText().toString().trim());
				map.put("OppMan", lxrEdit.getText().toString().trim());
				map.put("Tel", telEdit.getText().toString().trim());
				map.put("addr", pointEdit.getText().toString().trim());
				map.put("GPS", "");
				JSONObject jsonObject = new JSONObject(map);
				new AddVisitlogAsyncTask().execute(jsonObject.toString());
			}else{
				Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.dwnameLayout:
			Intent intent = new Intent();
			intent.setClass(this, DwListActivity.class);
			startActivityForResult(intent, 1);
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==1){
			if(resultCode==1){
				Map<String,Object> map = (Map<String, Object>) data.getSerializableExtra("dwmap");
				dwid = data.getStringExtra("dwid");
				
				Object dwName = map.get(DataBaseHelper.DWName);
				dwNameTxtView.setText(dwName==null ?"": dwName.toString());
				Object lxr = map.get(DataBaseHelper.LXR);
				lxrTxtView.setText(lxr==null ?"":lxr.toString());
				Object tel = map.get(DataBaseHelper.TEL);
				telTxtView.setText(tel == null ?"":tel.toString());
				Object addr = map.get(DataBaseHelper.ADDR);
				adressTxtView.setText(addr == null ?"":addr.toString());
				dwDetailLayout.setVisibility(View.VISIBLE);
			}
		}
	}

	class AddVisitlogAsyncTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String jsonString = WebserviceImport_more.AddCrmlog_1_0(params[0]);
			return jsonString;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pro_dialog.dismiss();
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(result);
				switch (jsonObject.getInt("Status")) {
				case 1:
					setEmpty();
					Toast.makeText(AddVisitActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				default:
					Toast.makeText(AddVisitActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
