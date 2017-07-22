package com.guantang.cangkuonline.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.application.MyApplication;
import com.guantang.cangkuonline.database.DataBaseCheckMethod;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.database.DataBaseMethod;
import com.guantang.cangkuonline.helper.CheckEditWatcher;
import com.guantang.cangkuonline.helper.DJWatcher;
import com.guantang.cangkuonline.helper.DecimalsHelper;
import com.guantang.cangkuonline.helper.NumberWatcher;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;
import com.guantang.cangkuonline.webservice.WebserviceImport;
import com.guantang.cangkuonline.webservice.WebserviceImport_more;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class ModifyHPActivity extends Activity implements OnClickListener{
	private Button leibiexuanzeBtn;
	private ImageButton backImg_Btn, saveImg_Btn;
	private ImageView scanImg_Btn;
	private LinearLayout res2_layout, res3_layout, morelayout, img;
	private TextView titleTextView,sccsTextView,lbTextView,imgTextView, imgtext, title, res1_text, res2_text,
			res3_text, res4_text, res5_text, res6_text,sameBtn;
	private EditText tmEditText, bmEditText, nameEditText, ggEditText,
			dwEditText, sxEditText, xxEditText, rkckjEditText,
			ckckjEditText, ckckjEditText2, dwEditText2, bignumEditText,
			res1EditText, res2EditText, res3EditText, res4EditText,
			res5EditText, res6EditText, bzEditText;
	private String[] str={DataBaseHelper.ID,DataBaseHelper.HPMC,DataBaseHelper.HPBM,DataBaseHelper.HPTM,DataBaseHelper.GGXH,
			DataBaseHelper.CurrKC,DataBaseHelper.JLDW,DataBaseHelper.HPSX,DataBaseHelper.HPXX,
			DataBaseHelper.SCCS,DataBaseHelper.BZ,DataBaseHelper.RKCKJ,DataBaseHelper.CKCKJ,
			DataBaseHelper.CKCKJ2,DataBaseHelper.JLDW2,DataBaseHelper.BigNum,DataBaseHelper.RES1,
			DataBaseHelper.RES2,DataBaseHelper.RES3,DataBaseHelper.RES4,DataBaseHelper.RES5,DataBaseHelper.RES6,
			DataBaseHelper.LBS,DataBaseHelper.LBID,DataBaseHelper.LBIndex,DataBaseHelper.KCJE,DataBaseHelper.ImagePath};
	private String[] str1={"id","HPMC","HPBM","HPTBM","GGXH","CurrKc","JLDW","HPSX","HPXX","SCCS","BZ","RKCKJ","CKCKJ",
			"CKCKJ2","JLDW2","BigNum","res1","res2","res3","res4","res5","res6","LBS","LBID","LBIndex","kcje","ImageUrl"};
	private ScrollView scrollView;
	private NumberWatcher numberWatcher = new NumberWatcher();
	private DJWatcher djWatcher = new DJWatcher();
	private CheckEditWatcher cked = new CheckEditWatcher();
	private DataBaseMethod dm = new DataBaseMethod(this);
	private DataBaseCheckMethod dm_ck = new DataBaseCheckMethod(this);
	private List<Map<String, Object>> customList = new ArrayList<Map<String, Object>>();
	private String hpid="",LBIndex="",LBId="";
	private ProgressDialog mProgressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_activity);
		Intent intent = getIntent();
		hpid = intent.getStringExtra("hpid");
		initControl();
		init();
	}
	
	public void initControl() {
		res2_layout = (LinearLayout) findViewById(R.id.res2_layout);
		res3_layout = (LinearLayout) findViewById(R.id.res3_layout);
		morelayout = (LinearLayout) findViewById(R.id.morelayout);
		img = (LinearLayout) findViewById(R.id.img);
		scrollView = (ScrollView) findViewById(R.id.addscoll);
		scanImg_Btn = (ImageView) findViewById(R.id.scan);
		sameBtn = (TextView) findViewById(R.id.same);
		imgTextView= (TextView) findViewById(R.id.imgBtn);
		backImg_Btn = (ImageButton) findViewById(R.id.back);
		saveImg_Btn = (ImageButton) findViewById(R.id.ok);
		titleTextView = (TextView) findViewById(R.id.title);
		lbTextView = (TextView) findViewById(R.id.lb);
		res1_text = (TextView) findViewById(R.id.res1_text);
		res2_text = (TextView) findViewById(R.id.res2_text);
		res3_text = (TextView) findViewById(R.id.res3_text);
		res4_text = (TextView) findViewById(R.id.res4_text);
		res5_text = (TextView) findViewById(R.id.res5_text);
		res6_text = (TextView) findViewById(R.id.res6_text);
		title = (TextView) findViewById(R.id.title);
		tmEditText = (EditText) findViewById(R.id.tm);
		bmEditText = (EditText) findViewById(R.id.bm);
		nameEditText = (EditText) findViewById(R.id.name);
		ggEditText = (EditText) findViewById(R.id.gg);
		dwEditText = (EditText) findViewById(R.id.dw);
		sxEditText = (EditText) findViewById(R.id.sx);
		xxEditText = (EditText) findViewById(R.id.xx);
		sccsTextView = (TextView) findViewById(R.id.sccs);
		rkckjEditText = (EditText) findViewById(R.id.rkckj);
		ckckjEditText = (EditText) findViewById(R.id.ckckj);
		ckckjEditText2 = (EditText) findViewById(R.id.ckckj2);
		dwEditText2 = (EditText) findViewById(R.id.dw2);
		bignumEditText = (EditText) findViewById(R.id.bignum);
		bzEditText = (EditText) findViewById(R.id.bz);
		res1EditText = (EditText) findViewById(R.id.res1);
		res2EditText = (EditText) findViewById(R.id.res2);
		res3EditText = (EditText) findViewById(R.id.res3);
		res4EditText = (EditText) findViewById(R.id.res4);
		res5EditText = (EditText) findViewById(R.id.res5);
		res6EditText = (EditText) findViewById(R.id.res6);
		
		lbTextView.setOnClickListener(this);
		img.setOnClickListener(this);
		scanImg_Btn.setOnClickListener(this);
		sameBtn.setOnClickListener(this);
		backImg_Btn.setOnClickListener(this);
		saveImg_Btn.setOnClickListener(this);
		sccsTextView.setOnClickListener(this);
		img.setOnClickListener(this);
		tmEditText.addTextChangedListener(cked);
		bmEditText.addTextChangedListener(cked);
		nameEditText.addTextChangedListener(cked);
		ggEditText.addTextChangedListener(cked);
		dwEditText.addTextChangedListener(cked);
		dwEditText2.addTextChangedListener(cked);
		res1EditText.addTextChangedListener(cked);
		res2EditText.addTextChangedListener(cked);
		res3EditText.addTextChangedListener(cked);
		res4EditText.addTextChangedListener(cked);
		res5EditText.addTextChangedListener(cked);
		res6EditText.addTextChangedListener(cked);
		bzEditText.addTextChangedListener(cked);
		sxEditText.addTextChangedListener(numberWatcher);
		xxEditText.addTextChangedListener(numberWatcher);
		rkckjEditText.addTextChangedListener(djWatcher);
		ckckjEditText.addTextChangedListener(djWatcher);
		ckckjEditText2.addTextChangedListener(djWatcher);
		bignumEditText.addTextChangedListener(numberWatcher);
		
		titleTextView.setText("货品修改");
		imgTextView.setText("点击查看");
		
		customList = dm_ck.Gt_Res();
		if (customList.size() != 0){
			setRes(customList);
		}
	}
	
	public void init(){
		if(WebserviceImport.isOpenNetwork(this)){
			mProgressDialog = ProgressDialog.show(this, null, "正在获取数据");
			new HPAsyncTask().execute(hpid);
		}else{
			Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void setRes(List<Map<String, Object>> list) {
		if (list.size() != 0) {
			String str;
			str = (String) list.get(0).get("文本型1");
			if (str == null || str.equals("")) {
				res1_text.setText("扩展字段1");
			} else {
				res1_text.setText(str);
			}
			str = (String) list.get(1).get("文本型2");
			if (str == null || str.equals("")) {
				res2_text.setText("扩展字段2");
			} else {
				res2_text.setText(str);
			}
			str = (String) list.get(2).get("文本型3");
			if (str == null || str.equals("")) {
				res3_text.setText("扩展字段3");
			} else {
				res3_text.setText(str);
			}
			str = (String) list.get(3).get("文本型4");
			if (str == null || str.equals("")) {
				res4_text.setText("扩展字段4");
			} else {
				res4_text.setText(str);
			}
			str = (String) list.get(4).get("文本型5");
			if (str == null || str.equals("")) {
				res5_text.setText("扩展字段5");
			} else {
				res5_text.setText(str);
			}
			str = (String) list.get(5).get("文本型6");
			if (str == null || str.equals("")) {
				res6_text.setText("扩展字段6");
			} else {
				res6_text.setText(str);
			}
		}
	}
	
	public void setText(List<Map<String, Object>> list){
		Map<String, Object> map = list.get(0);
		
		Object hptmObject = map.get(DataBaseHelper.HPTM);
		tmEditText.setText(hptmObject==null?"":hptmObject.toString());
		
		Object hpbmObject = map.get(DataBaseHelper.HPBM);
		bmEditText.setText(hpbmObject==null?"":hpbmObject.toString());
		
		Object hpmcObject = map.get(DataBaseHelper.HPMC);
		nameEditText.setText(hpmcObject==null?"":hpmcObject.toString());
		
		Object ggxhObject = map.get(DataBaseHelper.GGXH);
		ggEditText.setText(ggxhObject==null?"":ggxhObject.toString());
		
		
		Object sccsObject = map.get(DataBaseHelper.SCCS);
		sccsTextView.setText(sccsObject==null?"":sccsObject.toString());
		
		Object lbsObject = map.get(DataBaseHelper.LBS);
		lbTextView.setText(lbsObject==null?"":lbsObject.toString());
		
		Object lbidObject = map.get(DataBaseHelper.LBID);
		LBId = lbidObject==null?"":lbidObject.toString();
		
		Object lbindexObject = map.get(DataBaseHelper.LBIndex);
		LBIndex = lbindexObject==null?"":lbindexObject.toString();
		
		Object jldwObject = map.get(DataBaseHelper.JLDW);
		dwEditText.setText(jldwObject==null?"":jldwObject.toString());
		
		Object jldwObject2 = map.get(DataBaseHelper.JLDW2);
		dwEditText2.setText(jldwObject2==null?"":jldwObject2.toString());
		
		Object bignumObject = map.get(DataBaseHelper.BigNum);
		bignumEditText.setText((bignumObject==null || bignumObject.equals("")) ? "":DecimalsHelper.Transfloat(Double.parseDouble(bignumObject.toString()),MyApplication.getInstance().getNumPoint()));
		
		Object rkckjObject = map.get(DataBaseHelper.RKCKJ);
		rkckjEditText.setText((rkckjObject==null || rkckjObject.equals("")) ? "":DecimalsHelper.Transfloat(Double.parseDouble(rkckjObject.toString()),MyApplication.getInstance().getDjPoint()));
		
		Object ckckjObject = map.get(DataBaseHelper.CKCKJ);
		ckckjEditText.setText((ckckjObject==null || ckckjObject.equals("")) ? "":DecimalsHelper.Transfloat(Double.parseDouble(ckckjObject.toString()),MyApplication.getInstance().getDjPoint()));
		
		Object hpsxObject = map.get(DataBaseHelper.HPSX);
		sxEditText.setText((hpsxObject==null || hpsxObject.equals("")) ? "":DecimalsHelper.Transfloat(Double.parseDouble(hpsxObject.toString()),MyApplication.getInstance().getNumPoint()));
		
		Object hpxxObject = map.get(DataBaseHelper.HPXX);
		xxEditText.setText((hpxxObject==null || hpxxObject.equals("")) ? "":DecimalsHelper.Transfloat(Double.parseDouble(hpxxObject.toString()),MyApplication.getInstance().getNumPoint()));
		
		Object res1Object = map.get(DataBaseHelper.RES1);
		res1EditText.setText(res1Object==null?"":res1Object.toString());
		
		Object res2Object = map.get(DataBaseHelper.RES1);
		res1EditText.setText(res2Object==null?"":res2Object.toString());
		
		Object res3Object = map.get(DataBaseHelper.RES1);
		res1EditText.setText(res3Object==null?"":res3Object.toString());
		
		Object res4Object = map.get(DataBaseHelper.RES1);
		res1EditText.setText(res4Object==null?"":res4Object.toString());
		
		Object res5Object = map.get(DataBaseHelper.RES1);
		res1EditText.setText(res5Object==null?"":res5Object.toString());
		
		Object res6Object = map.get(DataBaseHelper.RES1);
		res1EditText.setText(res6Object==null?"":res6Object.toString());
		
		Object bzObject = map.get(DataBaseHelper.BZ);
		bzEditText.setText(bzObject==null?"":bzObject.toString());
	}
	
	class HPAsyncTask extends AsyncTask<String, Void, List<Map<String, Object>>>{

		@Override
		protected List<Map<String, Object>> doInBackground(String... params) {
			// TODO 自动生成的方法存根
			List<Map<String, Object>> list = WebserviceImport.GetHpInfo_byid(params[0], -1, str, str1, MyApplication.getInstance().getSharedPreferences());
			return list;
		}
		
		@Override
		protected void onPostExecute(List<Map<String, Object>> result) {
			// TODO 自动生成的方法存根
			super.onPostExecute(result);
			mProgressDialog.dismiss();
			if(!result.isEmpty()){
				setText(result);
			}else{
				Toast.makeText(ModifyHPActivity.this, "数据中断，无法显示", Toast.LENGTH_LONG).show();
			}
		}
	}
	
	class ModifyHPAsyncTask extends AsyncTask<String, Void,  String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO 自动生成的方法存根
			String jsonString = WebserviceImport_more.UpdateHP_1_0(hpid, params[0],tmEditText.getText().toString().trim(), MyApplication.getInstance().getSharedPreferences());
			return jsonString;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO 自动生成的方法存根
			super.onPostExecute(result);
			mProgressDialog.dismiss();
			try {
				JSONObject jsonObject = new JSONObject(result);
				switch(jsonObject.getInt("Status")){
				case 1:
					Toast.makeText(ModifyHPActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					dm.del_hp(hpid);
					dm.Addhp(Integer.parseInt(hpid),nameEditText.getText().toString().trim(),
							bmEditText.getText().toString().trim(),tmEditText.getText().toString().trim(), 
							ggEditText.getText().toString().trim(),dwEditText.getText().toString().trim(),
							dwEditText2.getText().toString().trim(),bignumEditText.getText().toString().trim(), "0", "0",
							sccsTextView.getText().toString().trim(),sxEditText.getText().toString().trim(), 
							xxEditText.getText().toString().trim(),rkckjEditText.getText().toString().trim(),
							ckckjEditText.getText().toString().trim(), 
							ckckjEditText2.getText().toString().trim(),lbTextView.getText().toString().trim(),LBId,LBIndex,
							bzEditText.getText().toString().trim(),"", 0, 
							res1EditText.getText().toString().trim(),
							res2EditText.getText().toString().trim(),res3EditText.getText().toString().trim(),
							res4EditText.getText().toString().trim(),res5EditText.getText().toString().trim(),
							res6EditText.getText().toString().trim(), "", "", 0, "","");
					finish();
					break;
				case -1:
					Toast.makeText(ModifyHPActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -2:
					Toast.makeText(ModifyHPActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -3:
					Toast.makeText(ModifyHPActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -4:
					Toast.makeText(ModifyHPActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -5:
					Toast.makeText(ModifyHPActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				}
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}
	
	public String createJson(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("HPTBM", tmEditText.getText().toString().trim());
		map.put("HPBM", bmEditText.getText().toString().trim());
		map.put("HPMC", nameEditText.getText().toString().trim());
		map.put("GGXH", ggEditText.getText().toString().trim());
		map.put("SCCS", sccsTextView.getText().toString().trim());
		map.put("LBS", lbTextView.getText().toString().trim());
		map.put("LBID", LBId);
		map.put("LBIndex", LBIndex);
		map.put("JLDW", dwEditText.getText().toString().trim());
		map.put("JLDW2", dwEditText2.getText().toString().trim());
		map.put("BigNum", bignumEditText.getText().toString().trim());
		map.put("RKCKJ", rkckjEditText.getText().toString().trim());
		map.put("CKCKJ", ckckjEditText.getText().toString().trim());
		map.put("HPSX", sxEditText.getText().toString().trim());
		map.put("HPXX", xxEditText.getText().toString().trim());
		map.put("res1", res1EditText.getText().toString().trim());
		map.put("res2", res2EditText.getText().toString().trim());
		map.put("res3", res3EditText.getText().toString().trim());
		map.put("res4", res4EditText.getText().toString().trim());
		map.put("res5", res5EditText.getText().toString().trim());
		map.put("res6", res6EditText.getText().toString().trim());
		map.put("BZ", bzEditText.getText().toString().trim());
		JSONObject jsonObject = new JSONObject(map);
		return jsonObject.toString();
	}
	
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		Intent intent = new Intent();
		switch(v.getId()){
		case R.id.ok:
			if(WebserviceImport.isOpenNetwork(this)){
				mProgressDialog = ProgressDialog.show(this, null, "正在修改货品");
				new ModifyHPAsyncTask().execute(createJson());
			}else{
				Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.same:
			bmEditText.setText(tmEditText.getText().toString());
			break;
		case R.id.scan:
			intent.setClass(this, CaptureActivity.class);
			startActivityForResult(intent, 1);
			break;
		case R.id.back:
			askSureDialog();
			break;
		case R.id.sccs:
			intent.setClass(this, DwListActivity.class);
			startActivityForResult(intent, 2);
			break;
		case R.id.lb:
			intent.setClass(this, LB_ChoseActivity.class);
			startActivityForResult(intent, 3);
			break;
		case R.id.img:
			if(MyApplication.getInstance().getSharedPreferences().getInt(ShareprefenceBean.IS_LOGIN, -1)==1){
				intent.setClass(this, ModfiyPhotoActivity.class);
				intent.putExtra("hpid", hpid);
				startActivity(intent);
			}else if(MyApplication.getInstance().getSharedPreferences().getInt(ShareprefenceBean.IS_LOGIN, -1)==-1){
				Toast.makeText(this, "离线模式不能修改图片", Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}
	
	public void askSureDialog(){
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		builder.setMessage("确定退出？");
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
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO 自动生成的方法存根
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==1){
			if(resultCode==1){
				String scan_tm=data.getStringExtra("scan_tm");
				tmEditText.setText(scan_tm);
			}
		}else if(requestCode==2){
			if(resultCode==1){
				Map<String,Object> map = (Map<String, Object>) data.getSerializableExtra("dwmap");
				sccsTextView.setText(map.get(DataBaseHelper.DWName).toString());
			}
		}else if(requestCode==3){
			if(resultCode==1){
				lbTextView.setText(data.getStringExtra("lb"));
				LBIndex = data.getStringExtra("index");
				LBId = data.getStringExtra("lbid");
			}
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO 自动生成的方法存根
		if(keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0){
			askSureDialog();
		}
		return super.onKeyDown(keyCode, event);
	}
}
