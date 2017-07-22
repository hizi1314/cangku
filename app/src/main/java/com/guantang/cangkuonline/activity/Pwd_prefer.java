package com.guantang.cangkuonline.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.application.MyApplication;
import com.guantang.cangkuonline.helper.PwdHelper;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;
import com.guantang.cangkuonline.webservice.WebserviceImport;
import com.guantang.cangkuonline.webservice.WebserviceImport_more;

public class Pwd_prefer extends Activity implements OnClickListener{
	private TextView usernameTextView;
	private EditText password1EditText,password2EditText,password3EditText;
	private Button comfirmBtn;
	private ImageButton backImgView;
	private SharedPreferences mSharedPreferences;
	private ProgressDialog pro_dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.pwd_prefer);
		mSharedPreferences=this.getSharedPreferences(ShareprefenceBean.SHAREPREFENCE_NAME, MODE_PRIVATE);
		initView();
	}
	public void initView(){
		usernameTextView = (TextView) findViewById(R.id.defaultusername);
		password1EditText = (EditText) findViewById(R.id.passwordEdit1);
		password2EditText = (EditText) findViewById(R.id.passwordEdit2);
		password3EditText = (EditText) findViewById(R.id.passwordEdit3);
		comfirmBtn = (Button) findViewById(R.id.confirmBtn1);
		backImgView = (ImageButton) findViewById(R.id.back);
		
		backImgView.setOnClickListener(this);
		usernameTextView.setOnClickListener(this);
		comfirmBtn.setOnClickListener(this);
		
		usernameTextView.setText(mSharedPreferences.getString(ShareprefenceBean.USERNAME, ""));
	}
	
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch(v.getId()){
		case R.id.back:
			finish();
			break;
		case R.id.confirmBtn1:
			if(password1EditText.getText().toString().trim().equals("")||password2EditText.getText().toString().trim().equals("")||password3EditText.getText().toString().trim().equals("")){
				Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT);
			}else{
				if(password2EditText.getText().toString().trim().equals(password3EditText.getText().toString().trim())){
					if(WebserviceImport.isOpenNetwork(this)){
						pro_dialog = ProgressDialog.show(this, null, null);
						new modifyAsynctask().execute();	
					}else{
						Toast.makeText(this, "网络未连接", Toast.LENGTH_LONG).show();
					}
				}else{
					Toast.makeText(this, "确认密码与新密码不一致", Toast.LENGTH_SHORT).show();
				}
			}
			break;
		}
	}
	
	class modifyAsynctask extends AsyncTask<Void, Void, String>{

		@Override
		protected String doInBackground(Void... params) {
			// TODO 自动生成的方法存根
			PwdHelper pwdhelper = new PwdHelper();
			String jsonString =WebserviceImport_more.Change_Password_1_0(usernameTextView.getText().toString(),
					pwdhelper.createMD5(password1EditText.getText().toString().trim() + "#cd@guantang").toUpperCase(),
					pwdhelper.createMD5(password2EditText.getText().toString().trim() + "#cd@guantang").toUpperCase(),
					MyApplication.getInstance().getSharedPreferences());
			return jsonString;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO 自动生成的方法存根
			pro_dialog.dismiss();
			super.onPostExecute(result);
			try {
				JSONObject changPasswordJsonObject = new JSONObject(result);
				switch(changPasswordJsonObject.getInt("Status")){
				case 1:
					Toast.makeText(Pwd_prefer.this, changPasswordJsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					mSharedPreferences.edit().putInt(ShareprefenceBean.IS_LOGIN, -1).commit();
					Intent intent=new Intent(Pwd_prefer.this,NewLoginActivity.class);
	    			startActivity(intent);
					break;
				case -1:
					Toast.makeText(Pwd_prefer.this, changPasswordJsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -2:
					Toast.makeText(Pwd_prefer.this, changPasswordJsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -3:
					Toast.makeText(Pwd_prefer.this, changPasswordJsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				default:
					Toast.makeText(Pwd_prefer.this, changPasswordJsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				}
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}
}
