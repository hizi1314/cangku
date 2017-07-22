package com.guantang.cangkuonline.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.application.MyApplication;
import com.guantang.cangkuonline.database.DataBaseCheckMethod;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.database.DataBaseOperateMethod;
import com.guantang.cangkuonline.helper.CheckEditWatcher;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;
import com.guantang.cangkuonline.webservice.WebserviceImport;
import com.guantang.cangkuonline.webservice.WebserviceImport_more;

public class Parameter_PrefActivity extends Activity implements OnClickListener {
	private ImageButton back, save;
	private EditText res1, res2, res3, res4, res5, res6, datename1, datename2;
	private Button commitBtn;
	private DataBaseCheckMethod dm_ck = new DataBaseCheckMethod(this);
	private List<Map<String, Object>> ls;
	private CheckEditWatcher cked = new CheckEditWatcher();
	private ProgressDialog pro_dialog;
	private String[] str = { DataBaseHelper.GID, DataBaseHelper.ItemID,
			DataBaseHelper.ItemV, DataBaseHelper.Ord };
	private String[] str1 = { "GID", "ItemID", "ItemV", "ord" };
	private DataBaseOperateMethod dm_op;
	private SharedPreferences mSharedPreferences;
	private boolean modifystatusFlag = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.parameter_pref);
		mSharedPreferences = getSharedPreferences(
				ShareprefenceBean.SHAREPREFENCE_NAME, Context.MODE_PRIVATE);
		initControl();
		init();
	}

	public void initControl() {
		back = (ImageButton) findViewById(R.id.back);
		save = (ImageButton) findViewById(R.id.ok);
		commitBtn = (Button) findViewById(R.id.commitBtn);
		res1 = (EditText) findViewById(R.id.res1);
		res2 = (EditText) findViewById(R.id.res2);
		res3 = (EditText) findViewById(R.id.res3);
		res4 = (EditText) findViewById(R.id.res4);
		res5 = (EditText) findViewById(R.id.res5);
		res6 = (EditText) findViewById(R.id.res6);
		datename1 = (EditText) findViewById(R.id.datename1);
		datename2 = (EditText) findViewById(R.id.datename2);
		res1.addTextChangedListener(cked);
		res2.addTextChangedListener(cked);
		res3.addTextChangedListener(cked);
		res4.addTextChangedListener(cked);
		res5.addTextChangedListener(cked);
		res6.addTextChangedListener(cked);
		datename1.addTextChangedListener(cked);
		datename2.addTextChangedListener(cked);

		setEditTextEnabled(modifystatusFlag);

		back.setOnClickListener(this);
		save.setOnClickListener(this);
		commitBtn.setOnClickListener(this);
	}

	public void init() {
		dm_op = new DataBaseOperateMethod(this);
		ls = new ArrayList<Map<String, Object>>();
		if (mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1) == 1) {
			commitBtn.setVisibility(View.VISIBLE);
			if (WebserviceImport.isOpenNetwork(this)) {
				pro_dialog = ProgressDialog.show(this, null, "正在刷新数据");
				new Thread(downloadRun).start();
			} else {
				Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
			}
		} else {
			commitBtn.setVisibility(View.GONE);
			ls = dm_ck.Gt_Res();
			if (ls.size() != 0) {
				setView(ls);
			}
		}
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				pro_dialog.dismiss();
				ls = dm_ck.Gt_Res();
				if (ls.size() != 0) {
					setView(ls);
				}
				setView(ls);
				break;
			case -1:
				pro_dialog.dismiss();
				ls = dm_ck.Gt_Res();
				if (ls.size() != 0) {
					setView(ls);
				}
				Toast.makeText(Parameter_PrefActivity.this, "获取数据异常，启用本地数据库",
						Toast.LENGTH_SHORT).show();
				break;
			case -2:
				pro_dialog.dismiss();
				Toast.makeText(Parameter_PrefActivity.this, "没有获得新数据",
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
	};
	Runnable downloadRun = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message msg = new Message();
			try {
				ls = WebserviceImport.GetConf("自定义字段", "", str, str1,
						mSharedPreferences);
				String[] values = new String[str.length];
				if (ls != null && ls.size() > 0) {
					dm_op.Del_Conf(new String[] { "自定义字段" });
					for (int i = 0; i < ls.size(); i++) {
						for (int j = 0; j < str.length; j++) {
							values[j] = (String) ls.get(i).get(str[j]);
						}
						dm_op.insert_into(DataBaseHelper.TB_Conf, str, values);
					}
					msg.what = 1;
				} else {
					msg.what = -2;
				}

			} catch (Exception e) {
				msg.what = -1;
			}
			msg.setTarget(mHandler);
			mHandler.sendMessage(msg);
		}

	};

	private void setView(List<Map<String, Object>> list) {
		if (list.size() != 0) {
			String str;
			str = (String) list.get(0).get("文本型1");
			if (str == null) {
				res1.setText("");
			} else {
				res1.setText(str);
			}
			str = (String) list.get(1).get("文本型2");
			if (str == null) {
				res2.setText("");
			} else {
				res2.setText(str);
			}
			str = (String) list.get(2).get("文本型3");
			if (str == null) {
				res3.setText("");
			} else {
				res3.setText(str);
			}
			str = (String) list.get(3).get("文本型4");
			if (str == null) {
				res4.setText("");
			} else {
				res4.setText(str);
			}
			str = (String) list.get(4).get("文本型5");
			if (str == null) {
				res5.setText("");
			} else {
				res5.setText(str);
			}
			str = (String) list.get(5).get("文本型6");
			if (str == null) {
				res6.setText("");
			} else {
				res6.setText(str);
			}
			str = (String) list.get(6).get("日期型1");
			if (str == null) {
				datename1.setText("");
			} else {
				datename1.setText(str);
			}
			str = (String) list.get(7).get("日期型2");
			if (str == null) {
				datename2.setText("");
			} else {
				datename2.setText(str);
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent i = new Intent();
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.ok:
			if (mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1) == 1) {
				if (WebserviceImport.isOpenNetwork(this)) {
					pro_dialog = ProgressDialog.show(this, null, "正在刷新数据");
					new Thread(downloadRun).start();
				} else {
					ls = dm_ck.Gt_Res();
					if (ls.size() != 0) {
						setView(ls);
					}
					Toast.makeText(this, "网络未连接，启用本地数据库", Toast.LENGTH_SHORT)
							.show();
				}
			} else {
				Toast.makeText(this, "离线模式不能修改扩展字段", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.commitBtn:
			if(!MyApplication.getInstance().getSharedPreferences().getString(ShareprefenceBean.DWNAME_LOGIN, "").equals("测试")&& mSharedPreferences.getInt(ShareprefenceBean.TIYANZHANGHAO, 0)!=1){
				if(modifystatusFlag){
					modifystatusFlag=false;
					commitBtn.setText("修  改");
					setEditTextEnabled(modifystatusFlag);
					if(WebserviceImport.isOpenNetwork(this)){
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("文本型1", res1.getText().toString().trim());
						map.put("文本型2", res2.getText().toString().trim());
						map.put("文本型3", res3.getText().toString().trim());
						map.put("文本型4", res4.getText().toString().trim());
						map.put("文本型5", res5.getText().toString().trim());
						map.put("文本型6", res6.getText().toString().trim());
						map.put("日期型1", datename1.getText().toString().trim());
						map.put("日期型2", datename2.getText().toString().trim());
						JSONObject jsonObject = new JSONObject(map);
						pro_dialog = ProgressDialog.show(Parameter_PrefActivity.this, null, "正在修改数据");
						new ModfiyCustomFieldAsyncTask().execute(jsonObject.toString());
					}else{
						Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
					}
				}else{
					modifystatusFlag=true;
					commitBtn.setText("确  认");
					setEditTextEnabled(modifystatusFlag);
				}
			}else{
				Toast.makeText(this, "测试账户不能修改扩展字段", Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
	}
	
	public void setEditTextEnabled(boolean status){
		res1.setEnabled(status);
		res2.setEnabled(status);
		res3.setEnabled(status);
		res4.setEnabled(status);
		res5.setEnabled(status);
		res6.setEnabled(status);
		datename1.setEnabled(status);
		datename2.setEnabled(status);
	}
	
	class ModfiyCustomFieldAsyncTask extends AsyncTask<String , Void, String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO 自动生成的方法存根
			String jsonString = WebserviceImport_more.AddCustomField_1_0(params[0], mSharedPreferences);
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
					if (WebserviceImport.isOpenNetwork(Parameter_PrefActivity.this)) {
						new Thread(downloadRun).start();
						Toast.makeText(Parameter_PrefActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					}else{
						pro_dialog.dismiss();
						Toast.makeText(Parameter_PrefActivity.this, "网络未连接,没有刷新成功", Toast.LENGTH_SHORT).show();
					}
					break;
				case -1:
					pro_dialog.dismiss();
					Toast.makeText(Parameter_PrefActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -2:
					pro_dialog.dismiss();
					Toast.makeText(Parameter_PrefActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -3:
					pro_dialog.dismiss();
					Toast.makeText(Parameter_PrefActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -4:
					pro_dialog.dismiss();
					Toast.makeText(Parameter_PrefActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				default:
					pro_dialog.dismiss();
					Toast.makeText(Parameter_PrefActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				}
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				pro_dialog.dismiss();
				e.printStackTrace();
			}
		}
	}
	
}
