package com.guantang.cangkuonline.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.guantang.cangkuonline.helper.DecimalsHelper;
import com.guantang.cangkuonline.webservice.WebserviceImport;
import com.guantang.cangkuonline.webservice.WebserviceImport_more;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class AddDiaoboActivity extends Activity implements OnClickListener {

	private ImageButton backImgBtn;
	private TextView dateTxtView,diaochuTxtView,diaoruTxtView,hp_totalTxtView,totalTxtView;
	private EditText dhEdit,jbrEdit,bzEdit;
	private ImageView addhp1ImgView;
	private LinearLayout diaochulayout,diaorulayout,addhplayout,tongjilayout;
	private RelativeLayout addedlayout;
	private Button commitBtn;
	private List<Map<String,Object>> ckList = new ArrayList<Map<String,Object>>();
	private String[] ss2;
	private String[] str = { DataBaseHelper.ID, DataBaseHelper.CKMC,
			DataBaseHelper.FZR, DataBaseHelper.TEL, DataBaseHelper.ADDR,
			DataBaseHelper.INACT, DataBaseHelper.OUTACT, DataBaseHelper.BZ };
	private DataBaseOperateMethod dm_op = new DataBaseOperateMethod(this);
	private DataBaseCheckMethod dm_ck=new DataBaseCheckMethod(this);
	private DataBaseMethod dm = new DataBaseMethod(this);
	private int sckid = -1,dckid = -1;
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	private String djid = "";
	private List<Map<String,Object>> getList = new ArrayList<Map<String,Object>>();
	
	private String str1[] = {"sckName","sckid","dckName","dckid","mvdh","mvdt","jbr","hpnames","bz","hpzjz"};
	// 调拨明细字段
	private String str2[] = { "hpid", "sl", "dj", "zj", "mid", "note" };
	private ProgressDialog pro_Dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_adddiaobo_layout);
		initView();
		init();
	}

	public void initView() {
		backImgBtn = (ImageButton) findViewById(R.id.back);
		dateTxtView = (TextView) findViewById(R.id.dateTxtView);
		diaochuTxtView = (TextView) findViewById(R.id.diaochuTxtView);
		diaoruTxtView = (TextView) findViewById(R.id.diaoruTxtView);
		dhEdit = (EditText) findViewById(R.id.dhEdit);
		jbrEdit = (EditText) findViewById(R.id.jbrEdit);
		bzEdit = (EditText) findViewById(R.id.bzEdit);
		diaochulayout = (LinearLayout) findViewById(R.id.diaochulayout);
		diaorulayout = (LinearLayout) findViewById(R.id.diaorulayout);
		addedlayout = (RelativeLayout) findViewById(R.id.added);
		addhplayout = (LinearLayout) findViewById(R.id.addhp);
		addhp1ImgView = (ImageView) findViewById(R.id.addhp1);
		totalTxtView = (TextView) findViewById(R.id.total);
		hp_totalTxtView = (TextView) findViewById(R.id.hp_total);
		commitBtn = (Button) findViewById(R.id.commitBtn);
		tongjilayout = (LinearLayout) findViewById(R.id.tongjilayout);
		
		addhp1ImgView.setOnClickListener(this);
		backImgBtn.setOnClickListener(this);
		diaochulayout.setOnClickListener(this);
		diaorulayout.setOnClickListener(this);
		addhplayout.setOnClickListener(this);
		commitBtn.setOnClickListener(this);
		tongjilayout.setOnClickListener(this);
		
		ckList = dm_op.Gt_ck(str);
		if(!ckList.isEmpty()){
			ss2 = new String[ckList.size()];
			for (int j = 0; j < ckList.size(); j++) {
				ss2[j] = (String) ckList.get(j).get(DataBaseHelper.CKMC);
			}
		}
	}
	
	public void init(){
		
		dateTxtView.setText(formatter.format(System.currentTimeMillis()));
		dm_op.insert_into(DataBaseHelper.TB_TRANSM,new String[] { DataBaseHelper.MVDT},
				new String[] { formatter.format(new Date(System.currentTimeMillis()))});
		djid = dm_op.GtMax_DBDJID();
		
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		double count = 0;
		getList = dm_op.Gt_Transd(djid, str2);
		if(getList.size()>0){
			addedlayout.setVisibility(View.VISIBLE);
			addhplayout.setVisibility(View.GONE);
		}else{
			addedlayout.setVisibility(View.GONE);
			addhplayout.setVisibility(View.VISIBLE);
		}
		hp_totalTxtView.setText(String.valueOf(getList.size()));
		for (int i = 0; i < getList.size(); i++) {
			if ((!getList.get(i).get(DataBaseHelper.SL).toString().equals(""))
					|| (getList.get(i).get(DataBaseHelper.SL) != null)) {
				count += Float.parseFloat(getList.get(i)
						.get(DataBaseHelper.SL).toString());
			}
		}
		totalTxtView.setText(DecimalsHelper.Transfloat(count,MyApplication.getInstance().getNumPoint()));
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		switch(v.getId()){
		case R.id.back:
			if(Integer.parseInt(hp_totalTxtView.getText().toString())!=0){
				exitDJDialog();
			}else{
				finish();
			}
			break;
		case R.id.diaochulayout:
			if (!ckList.isEmpty()) {
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
						
						ArrayAdapter<String> fileList = new ArrayAdapter<String>(AddDiaoboActivity.this,
				                R.layout.list_item, ss2);
						myListView.setAdapter(fileList);
						myListView.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
								// TODO Auto-generated method stub
								if (ckList.get(arg2).get(DataBaseHelper.ID) != null) {
									diaochuTxtView.setText(ss2[arg2]);
									sckid = Integer.parseInt((String) ckList.get(
											arg2).get(DataBaseHelper.ID));
									dm_ck.update_transm(DataBaseHelper.sckName, ss2[arg2], djid);
									dm_ck.update_transm(DataBaseHelper.sckid, String.valueOf(sckid), djid);
								}else{
									Toast.makeText(AddDiaoboActivity.this, "仓库id获取出错", Toast.LENGTH_SHORT).show();
								}
								dialog.dismiss();
							}
						});
					}
				});
				myCommonDialog.show();
			}else{
				Toast.makeText(this, "无仓库信息或没有更新仓库信息", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.diaorulayout:
			if (!ckList.isEmpty()) {
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
						
						ArrayAdapter<String> fileList = new ArrayAdapter<String>(AddDiaoboActivity.this,
				                R.layout.list_item, ss2);
						myListView.setAdapter(fileList);
						myListView.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
								// TODO Auto-generated method stub
								if (ckList.get(arg2).get(DataBaseHelper.ID) != null) {
									diaoruTxtView.setText(ss2[arg2]);
									dckid = Integer.parseInt((String) ckList.get(
											arg2).get(DataBaseHelper.ID));
									dm_ck.update_transm(DataBaseHelper.dckName, ss2[arg2], djid);
									dm_ck.update_transm(DataBaseHelper.dckid, String.valueOf(sckid), djid);
									
								}else{
									Toast.makeText(AddDiaoboActivity.this, "仓库id获取出错", Toast.LENGTH_SHORT).show();
								}
								dialog.dismiss();
							}
						});
					}
				});
				myCommonDialog.show();
			}else{
				Toast.makeText(this, "无仓库信息或没有更新仓库信息", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.commitBtn:
			if(!TextUtils.isEmpty(diaochuTxtView.getText().toString()) && !TextUtils.isEmpty(diaoruTxtView.getText().toString())){
				if(WebserviceImport.isOpenNetwork(this)){
					pro_Dialog = ProgressDialog.show(this, null, "正在上传数据……");
					saveDJ();
					List<Map<String,Object>> list2 = dm_op.Gt_Transm(djid, str1);
					if(!list2.isEmpty()){
						JSONObject djJsonObject = new JSONObject(list2.get(0));
						JSONArray mxjsonArray = new JSONArray();
						int length = getList.size();
						for(int i=0;i<length;i++){
							JSONObject mxJsonObject = new JSONObject(getList.get(i));
							mxjsonArray.put(mxJsonObject);
						}
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("transm", djJsonObject);
						map.put("transd", mxjsonArray);
						JSONObject ddJsonObject = new JSONObject(map);
						new AddDBAsyncTask().execute(ddJsonObject.toString());
					}
				}else{
					Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
				}
			}else{
				Toast.makeText(this, "请选择仓库", Toast.LENGTH_SHORT).show();
			}
			
			
			break;
		case R.id.addhp:
			if(!TextUtils.isEmpty(diaochuTxtView.getText().toString()) && !TextUtils.isEmpty(diaoruTxtView.getText().toString())){
				intent.setClass(this, DiaoboHP_choseActivity.class);
				intent.putExtra("sckid", sckid);
				intent.putExtra("djid",djid);
				startActivity(intent);
			}else{
				Toast.makeText(this, "请选择仓库", Toast.LENGTH_SHORT).show();
			}
			
			break;
		case R.id.addhp1:
			intent.setClass(this, DiaoboHP_choseActivity.class);
			intent.putExtra("sckid", sckid);
			intent.putExtra("djid",djid);
			startActivity(intent);
			break;
		case R.id.tongjilayout:
			intent.setClass(this, DiaoboDetailActivity.class);
			intent.putExtra("djid", djid);
			startActivity(intent);
			break;	
		}
	}
	
	public void saveDJ(){
		String details2 ="",hpstr = "";
		if(!getList.isEmpty()){
			hpstr = dm.Gethp(new String[]{DataBaseHelper.HPMC}, getList.get(0).get(DataBaseHelper.HPID).toString()).get(0).get(DataBaseHelper.HPMC).toString();
		}
		if(hp_totalTxtView.getText().toString().equals("1")){
			details2 = hpstr;
		}else if (hp_totalTxtView.getText().toString().equals("0")){
			details2 = "无货品明细";
		}else{
			details2 = hpstr + " 等"
					+ hp_totalTxtView.getText().toString() + "种货品";
		}
		dm_op.Update_transm(diaochuTxtView.getText().toString(), sckid, diaoruTxtView.getText().toString(), dckid, dhEdit.getText().toString(),
				dateTxtView.getText().toString(), jbrEdit.getText().toString(), details2, bzEdit.getText().toString(), "0");
		
	}
	
	class AddDBAsyncTask extends AsyncTask<String,Void,String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String json = WebserviceImport_more.AddDB_1_0(params[0]);
			return json;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pro_Dialog.dismiss();
			try {
				JSONObject jsonObject = new JSONObject(result);
				switch(jsonObject.getInt("Status")){
				case 1:
					finish();
					Toast.makeText(AddDiaoboActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				default:
					Toast.makeText(AddDiaoboActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	public void exitDJDialog(){
		CommonDialog mCommonDialog = new CommonDialog(this, R.layout.prompt_dialog_layout, R.style.yuanjiao_dialog);
		mCommonDialog.setDialogContentListener(new DialogContentListener() {
			
			@Override
			public void contentExecute(View parent, final Dialog dialog, Object[] objs) {
				// TODO Auto-generated method stub
				TextView titleTextView = (TextView) parent.findViewById(R.id.title);
				TextView contentTextview = (TextView) parent.findViewById(R.id.content_txtview);
				TextView cancelTextview = (TextView) parent.findViewById(R.id.cancel);
				TextView confirmTextView = (TextView) parent.findViewById(R.id.confirm);
				
				titleTextView.setVisibility(View.GONE);
				contentTextview.setText("是否退出调拨订单？");
				cancelTextview.setText("否");
				confirmTextView.setText("是");
				cancelTextview.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO 自动生成的方法存根
						dialog.dismiss();
					}
				});
				confirmTextView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dm_op.Del_transd(djid);
						dm_op.Del_transm(djid);
						finish();
						dialog.dismiss();
					}
				});
			}
		});
		mCommonDialog.show();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0){
			if(Integer.parseInt(hp_totalTxtView.getText().toString())!=0){
				exitDJDialog();
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
