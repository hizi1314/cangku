package com.guantang.cangkuonline.activity;

import org.json.JSONException;
import org.json.JSONObject;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.application.MyApplication;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;
import com.guantang.cangkuonline.webservice.WebserviceImport;
import com.guantang.cangkuonline.webservice.WebserviceImport_more;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class RegistrationInformationActivity extends Activity implements OnClickListener {

	private TextView dwTxtView, userCountTxtView, effectivedateTxtView, lxrTxtView, telTxtView, urlTxtView,
			starturl1TxtView, starturl2TxtView;
	private ImageButton backImgBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registrationinformation_layout);
		initView();
		init();
	}

	private void initView() {
		dwTxtView = (TextView) findViewById(R.id.dwTxtView);
		userCountTxtView = (TextView) findViewById(R.id.userCountTxtView);
		effectivedateTxtView = (TextView) findViewById(R.id.effectivedateTxtView);
		lxrTxtView = (TextView) findViewById(R.id.lxrTxtView);
		telTxtView = (TextView) findViewById(R.id.telTxtView);
		urlTxtView = (TextView) findViewById(R.id.urlTxtView);
		starturl1TxtView = (TextView) findViewById(R.id.starturl1TxtView);
		starturl2TxtView = (TextView) findViewById(R.id.starturl2TxtView);
		backImgBtn = (ImageButton) findViewById(R.id.back);

		starturl1TxtView.setOnClickListener(this);
		starturl2TxtView.setOnClickListener(this);
		backImgBtn.setOnClickListener(this);
	}

	private void init(){
		if(WebserviceImport.isOpenNetwork(this)){
			new RegInfoAsyncTask().execute();
		}else{
			Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.starturl1TxtView:
			starturl();
			break;
		case R.id.starturl2TxtView:
			starturl();
			break;
		}
	}
	
	public void starturl(){
		Uri uri = Uri.parse("https://item.taobao.com/item.htm?spm=a1z10.5-c.w4002-1908853061.29.zSLrSF&id=38913545614");
		Intent intent  = new Intent(Intent.ACTION_VIEW,uri);
		startActivity(intent);
	}
	
	class RegInfoAsyncTask extends AsyncTask<Void,Void,String>{

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String json = WebserviceImport_more.GetRegInfo();
			return json;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				JSONObject jsonObject = new JSONObject(result);
				switch(jsonObject.getInt("Status")){
				case 1:
					JSONObject dateJSONObject = jsonObject.getJSONObject("Data");
					dwTxtView.setText(dateJSONObject.getString("RegName"));
					userCountTxtView.setText(dateJSONObject.getString("UserNumber"));
					effectivedateTxtView.setText(dateJSONObject.getString("ExpiredDate").subSequence(0, 10));
					lxrTxtView.setText(dateJSONObject.getString("Contact"));
					telTxtView.setText(dateJSONObject.getString("Phone"));
					urlTxtView.setText(MyApplication.getInstance().getSharedPreferences().getString(ShareprefenceBean.NET_URL, ""));
					break;
				default:
					Toast.makeText(RegistrationInformationActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
}
