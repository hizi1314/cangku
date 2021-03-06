package com.guantang.cangkuonline.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.webservice.WebserviceImport;
import com.guantang.cangkuonline.webservice.WebserviceImport_more;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class EditCustomerAndSupplierActivity extends Activity implements
		OnClickListener {

	private ImageButton backImgBtn;
	private TextView titleTxtView, commitTxtView, dwlbTxtView;
	private EditText dwbmEdit, dwmcEdit, lxrEdit, telEdit,
			addressEdit, shEdit, emailEdit, ybEdit, bankNameEdit,
			bankCardNumEdit;
	private LinearLayout dwlbLayout;
	private CheckBox customerCheckBox, supplierCheckBox;
	private String dwindex = "",dwid = "";
	private int selectdw = 0; // 0 既不是客户也不是供应商，1是客户，2是供应商，3既是客户又是供应商

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addcustomerandsupplier);
		initView();
		init();
	}

	public void initView() {
		backImgBtn = (ImageButton) findViewById(R.id.back);
		titleTxtView = (TextView) findViewById(R.id.title);
		commitTxtView = (TextView) findViewById(R.id.commitTxtView);
		dwlbTxtView = (TextView) findViewById(R.id.dwlbTxtView);
		dwbmEdit = (EditText) findViewById(R.id.dwbmEdit);
		dwmcEdit = (EditText) findViewById(R.id.dwmcEdit);
		lxrEdit = (EditText) findViewById(R.id.lxrEdit);
		telEdit = (EditText) findViewById(R.id.telEdit);
		addressEdit = (EditText) findViewById(R.id.addressEdit);
		shEdit = (EditText) findViewById(R.id.shEdit);
		emailEdit = (EditText) findViewById(R.id.emailEdit);
		ybEdit = (EditText) findViewById(R.id.ybEdit);
		bankNameEdit = (EditText) findViewById(R.id.bankNameEdit);
		bankCardNumEdit = (EditText) findViewById(R.id.bankCardNumEdit);
		dwlbLayout = (LinearLayout) findViewById(R.id.dwlbLayout);
		customerCheckBox = (CheckBox) findViewById(R.id.customerCheckBox);
		supplierCheckBox = (CheckBox) findViewById(R.id.supplierCheckBox);

		backImgBtn.setOnClickListener(this);
		commitTxtView.setOnClickListener(this);
		dwlbLayout.setOnClickListener(this);
		
		titleTxtView.setText("单位修改");
	}

	public void init() {
		Intent intent = getIntent();
		Map<String,Object> dwmap = (Map<String, Object>) intent.getSerializableExtra("dwmap");
		
		Object dwNameObject = dwmap.get("dwName");
		dwmcEdit.setText(dwNameObject == null || dwNameObject.toString().equals("null") ?"":dwNameObject.toString());
		
		Object lxrObject = dwmap.get("lxr");
		lxrEdit.setText(lxrObject == null || lxrObject.toString().equals("null")?"":lxrObject.toString());
		
		Object telObject = dwmap.get("tel");
		telEdit.setText(telObject == null || telObject.toString().equals("null")?"":telObject.toString());
		
		Object addrObject = dwmap.get("addr");
		addressEdit.setText(addrObject == null || addrObject.toString().equals("null")?"":addrObject.toString());
		
		Object dwbmObject = dwmap.get("py");
		dwbmEdit.setText(dwbmObject == null || dwbmObject.toString().equals("null")?"":dwbmObject.toString());
		
		Object dwTypeObject = dwmap.get("dwType");
		if(dwTypeObject == null || dwTypeObject.toString().equals("null")){
			customerCheckBox.setChecked(false);
			supplierCheckBox.setChecked(false);
		}else{
			int dwTypeNum = Integer.parseInt(dwTypeObject.toString());
			if(dwTypeNum == 1){
				customerCheckBox.setChecked(true);
				supplierCheckBox.setChecked(false);
			}else if(dwTypeNum == 2){
				customerCheckBox.setChecked(false);
				supplierCheckBox.setChecked(true);
			}else if(dwTypeNum == 3){
				customerCheckBox.setChecked(true);
				supplierCheckBox.setChecked(true);
			}else{
				customerCheckBox.setChecked(false);
				supplierCheckBox.setChecked(false);
			}
		}
		
		Object areaObject = dwmap.get("area");
		dwlbTxtView.setText(areaObject == null || areaObject.toString().equals("null")?"":areaObject.toString());
		
		Object dwindexObject = dwmap.get("areaIndex");
		dwindex = (dwindexObject == null || dwindexObject.toString().equals("null"))?"":dwindexObject.toString();
		
		Object dwidObject = dwmap.get("id");
		dwid = (dwidObject == null || dwidObject.toString().equals("null"))?"":dwidObject.toString();
		
		Object swdjhObject = dwmap.get("swdjh");
		shEdit.setText(swdjhObject == null || swdjhObject.toString().equals("null")?"":swdjhObject.toString());
		
		Object emailObject = dwmap.get("email");
		emailEdit.setText(emailObject == null || emailObject.toString().equals("null")?"":emailObject.toString());
		
		Object ybObject = dwmap.get("yb");
		ybEdit.setText(ybObject == null || ybObject.toString().equals("null")?"":ybObject.toString());
		
		Object yhObject = dwmap.get("yh");
		bankNameEdit.setText(yhObject == null || yhObject.toString().equals("null")?"":yhObject.toString());
		
		Object zhObject = dwmap.get("zh");
		bankCardNumEdit.setText(ybObject == null || zhObject.toString().equals("null")?"":zhObject.toString());
		
	}
	
	public void setEmpty(){
		dwindex = "";
		selectdw = 0;
		dwlbTxtView.setText("");
		dwbmEdit.setText("");
		dwmcEdit.setText("");
		lxrEdit.setText("");
		telEdit.setText("");
		addressEdit.setText("");
		shEdit.setText("");
		emailEdit.setText("");
		ybEdit.setText("");
		bankNameEdit.setText("");
		bankCardNumEdit.setText("");
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.dwlbLayout:
			intent.setClass(this, DwlbListActivity.class);
			startActivityForResult(intent, 1);
			break;
		case R.id.commitTxtView:
			if(!customerCheckBox.isChecked()&&!supplierCheckBox.isChecked()){
				selectdw = 0;
			}else if(customerCheckBox.isChecked()&&!supplierCheckBox.isChecked()){
				selectdw = 1;
			}else if(!customerCheckBox.isChecked()&&supplierCheckBox.isChecked()){
				selectdw = 2;
			}else if(customerCheckBox.isChecked()&&supplierCheckBox.isChecked()){
				selectdw = 3;
			}
			if(selectdw == 0){
				Toast.makeText(this, "请选择单位大类", Toast.LENGTH_SHORT).show();
			}else{
				if(WebserviceImport.isOpenNetwork(this)){
					Map<String,Object> map = new HashMap<String, Object>();
					map.put("py", dwbmEdit.getText().toString().trim());
					map.put("dwType", selectdw);
					map.put("area", dwlbTxtView.getText().toString().trim());
					map.put("areaIndex", dwindex);
					map.put("dwName", dwmcEdit.getText().toString().trim());
					map.put("lxr", lxrEdit.getText().toString().trim());
					map.put("tel", telEdit.getText().toString().trim());
					map.put("addr", addressEdit.getText().toString().trim());
					map.put("swdjh", shEdit.getText().toString().trim());//税号
					map.put("email", emailEdit.getText().toString().trim());
					map.put("yb", ybEdit.getText().toString().trim());
					map.put("yh", bankNameEdit.getText().toString().trim());
					map.put("zh", bankCardNumEdit.getText().toString().trim());
					map.put("id", dwid);
					
					JSONObject jsonObject = new JSONObject(map);
					new EditCompanyAsyncTask().execute(jsonObject.toString());
				}else{
					Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
				}
			}
			break;
		}
	}

	class EditCompanyAsyncTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO 自动生成的方法存根
			String jsonString = WebserviceImport_more.EditCompany_1_0(params[0]);
			return jsonString;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO 自动生成的方法存根
			super.onPostExecute(result);
			try {
				JSONObject jsonObject = new JSONObject(result);
				switch (jsonObject.getInt("Status")) {
				case 1:
					setEmpty();
					Toast.makeText(EditCustomerAndSupplierActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					finish();
					break;
				default:
					Toast.makeText(EditCustomerAndSupplierActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				}
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO 自动生成的方法存根
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			if (resultCode == 1) {
				dwlbTxtView.setText(data.getStringExtra("dwName"));
				dwindex = data.getStringExtra("dwindex");
			}
		}
	}

}
