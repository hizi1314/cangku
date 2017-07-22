package com.guantang.cangkuonline.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.R.string;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.application.MyApplication;
import com.guantang.cangkuonline.database.DataBaseCheckMethod;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.database.DataBaseOperateMethod;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;
import com.guantang.cangkuonline.webservice.WebserviceImport;
import com.guantang.cangkuonline.webservice.WebserviceImport_more;

public class CKListActivity extends Activity implements OnClickListener{
	private ImageButton backImgBtn,addckImgBtn;
	private ImageView go1ImageView,go2ImageView;
	private EditText cknameEdit,fzrEdit,telEdit,bzEdit;
	private ListView mListView;
	private TextView editconfirm,telTextView,showBtn,defaultRuTypeTxtView,defaultChuTypeTxtView;
	private LinearLayout ckinfoLayout;
	private String[] str={DataBaseHelper.ID,DataBaseHelper.CKMC,DataBaseHelper.FZR,DataBaseHelper.TEL
			,DataBaseHelper.ADDR,DataBaseHelper.INACT,DataBaseHelper.OUTACT,DataBaseHelper.BZ};
	private String[] str1={"ID","CKMC","FZR","TEL","ADDR","inact","outact","BZ"};
	private DataBaseOperateMethod dm_op;
	private List<Map<String, Object>> ls;
	private ProgressDialog pro_Dialog;
	private SharedPreferences mSharedPreferences;
	private List<Map<String, Object>> rutypeList = new ArrayList<Map<String,Object>>();
	private List<Map<String, Object>> chutypeList = new ArrayList<Map<String,Object>>();
	private DataBaseCheckMethod dm_cm = new DataBaseCheckMethod(this);
	private boolean modifystatusFlag=false;//true表示进入编辑状态
	private int checkid=-1;//获取点击仓库的id,如果没有点击对应的仓库则为-1
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.cklist);
		mSharedPreferences = getSharedPreferences(ShareprefenceBean.SHAREPREFENCE_NAME, Context.MODE_PRIVATE);
		init_control();
	}
	private void init_control(){
		backImgBtn=(ImageButton) findViewById(R.id.back);
		addckImgBtn = (ImageButton) findViewById(R.id.addck);
		mListView= (ListView) findViewById(R.id.cklist);
		cknameEdit=(EditText) findViewById(R.id.ckname);
		fzrEdit=(EditText) findViewById(R.id.fzr);
		telEdit=(EditText) findViewById(R.id.tel);
		bzEdit=(EditText) findViewById(R.id.bz);
		defaultRuTypeTxtView = (TextView) findViewById(R.id.defaultRuType);
		defaultChuTypeTxtView = (TextView) findViewById(R.id.defaultChuType);
		editconfirm = (TextView) findViewById(R.id.editconfirm);
		telTextView = (TextView) findViewById(R.id.telTextView);
		showBtn = (TextView) findViewById(R.id.showBtn);
		ckinfoLayout = (LinearLayout) findViewById(R.id.ckinfoLayout);
		go1ImageView = (ImageView) findViewById(R.id.go1);
		go2ImageView = (ImageView) findViewById(R.id.go2);
		
		showBtn.setOnClickListener(this);
		backImgBtn.setOnClickListener(this);
		addckImgBtn.setOnClickListener(this);
		editconfirm.setOnClickListener(this);
		defaultRuTypeTxtView.setOnClickListener(this);
		defaultChuTypeTxtView.setOnClickListener(this);
		EditTextEnabled(false);
		ckinfoLayout.setVisibility(View.GONE);
		go1ImageView.setVisibility(View.GONE);
		go2ImageView.setVisibility(View.GONE);
	}
	
	private void init(){
		dm_op=new DataBaseOperateMethod(this);
		if(mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1)==1){
			editconfirm.setVisibility(View.VISIBLE);
			if(WebserviceImport.isOpenNetwork(this)){
				pro_Dialog=ProgressDialog.show(this, null, "正在加载数据……");
				new Thread(downloadRun).start();
			}else{
				Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
			}
		}else{
			editconfirm.setVisibility(View.GONE);
			ls=new ArrayList<Map<String, Object>>();
			ls=dm_op.Gt_ck(str);
			if(!ls.isEmpty()){
				ListIterator<Map<String, Object>> it=ls.listIterator();
				while(it.hasNext()){
					Map<String, Object> map=it.next();
					map.put("check", false);
					it.set(map);
				}
				MyListAdapter myListAdapter = new MyListAdapter(this, ls);
				mListView.setAdapter(myListAdapter);
			}else{
				Toast.makeText(this, "没有仓库信息，请刷新！", Toast.LENGTH_SHORT).show();
			}
		}
		
		rutypeList = dm_cm.Gt_Type("入库类型", DataBaseHelper.DWName);
		chutypeList = dm_cm.Gt_Type("出库类型", DataBaseHelper.DWName);
	}
	
	@Override
	protected void onStart() {
		// TODO 自动生成的方法存根
		super.onStart();
		init();
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.back:
		    finish();
			break;
		case R.id.addck:
			if(mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1)==1){
				if(!mSharedPreferences.getString(ShareprefenceBean.DWNAME_LOGIN, "").equals("测试") && mSharedPreferences.getInt(ShareprefenceBean.TIYANZHANGHAO, 0)!=1){
					Intent intent = new Intent(this,SettingckActivity.class);
					startActivity(intent);
				}else{
					Toast.makeText(CKListActivity.this, "测试用户不能新增仓库", Toast.LENGTH_SHORT).show();
				}
			}else{
				Toast.makeText(this, "离线模式不能添加仓库", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.showBtn:
			Animation animation = AnimationUtils.loadAnimation(this, R.anim.cangkuinfo_translate_out);
			animation.setFillAfter(true);
			animation.setDuration(200);
			animation.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
					// TODO 自动生成的方法存根
					
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO 自动生成的方法存根
					
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					// TODO 自动生成的方法存根
					ckinfoLayout.setVisibility(View.GONE);
					ckinfoLayout.clearAnimation();
				}
			});
			ckinfoLayout.startAnimation(animation);
			
			break;
		case R.id.defaultRuType:
			int rulength=rutypeList.size();
			final String[] ss = new String[rulength];
			for(int n = 0; n < rulength; n++) {
				ss[n] = (String) rutypeList.get(n).get(DataBaseHelper.DWName);
			}
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("请选择类型");
			builder.setSingleChoiceItems(ss, -1,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							defaultRuTypeTxtView.setText(ss[which]);
							dialog.dismiss();
						}
					});
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					});
			builder.create().show();
			break;
		case R.id.defaultChuType:
			int chulength=chutypeList.size();
			final String[] ss1 = new String[chulength];
			for(int n = 0; n < chulength; n++) {
				ss1[n] = (String) chutypeList.get(n).get(DataBaseHelper.DWName);
			}
			AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
			builder1.setTitle("请选择类型");
			builder1.setSingleChoiceItems(ss1, -1,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							defaultChuTypeTxtView.setText(ss1[which]);
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
		case R.id.editconfirm:
			if(mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1)==1){
				if(!mSharedPreferences.getString(ShareprefenceBean.DWNAME_LOGIN, "").equals("测试")){
					if(modifystatusFlag){
						modifystatusFlag=false;
						editconfirm.setText("修改");
						EditTextEnabled(modifystatusFlag);
						String[] str1={DataBaseHelper.ID,DataBaseHelper.CKMC,DataBaseHelper.FZR,DataBaseHelper.TEL
								,DataBaseHelper.ADDR,DataBaseHelper.INACT,DataBaseHelper.OUTACT,DataBaseHelper.BZ};
						Map<String, Object> map = new HashMap<String, Object>();
						map.put(DataBaseHelper.CKMC, cknameEdit.getText().toString().trim());
						map.put(DataBaseHelper.FZR, fzrEdit.getText().toString().trim());
						map.put(DataBaseHelper.TEL, telEdit.getText().toString().trim());
						map.put(DataBaseHelper.INACT, defaultRuTypeTxtView.getText().toString().trim());
						map.put(DataBaseHelper.OUTACT, defaultChuTypeTxtView.getText().toString().trim());
						map.put(DataBaseHelper.BZ, bzEdit.getText().toString().trim());
						JSONObject jsonObject = new JSONObject(map);
						if(WebserviceImport.isOpenNetwork(this)){
							if(checkid==-1){
								Toast.makeText(this, "请选择仓库", Toast.LENGTH_SHORT).show();
							}else{
								pro_Dialog=ProgressDialog.show(this, null, "正在修改数据");
								new modfiyckAsyncTask().execute(jsonObject.toString());
							}
						}else{
							Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
						}
					}else{
						if(checkid==-1){
							Toast.makeText(this, "请选择仓库", Toast.LENGTH_SHORT).show();
						}else{
							modifystatusFlag=true;
							editconfirm.setText("确认");
							EditTextEnabled(modifystatusFlag);
						}
					}
				}else{
					Toast.makeText(CKListActivity.this, "测试用户不能修改仓库", Toast.LENGTH_SHORT).show();
				}
			}else{
				Toast.makeText(this, "离线模式不能修改仓库", Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}
	
	public void EditTextEnabled(boolean status){
		cknameEdit.setEnabled(status);
		fzrEdit.setEnabled(status);
		bzEdit.setEnabled(status);
		defaultRuTypeTxtView.setEnabled(status);
		defaultChuTypeTxtView.setEnabled(status);
		if(status){
			telTextView.setVisibility(View.GONE);
			telEdit.setVisibility(View.VISIBLE);
			cknameEdit.setBackground(getResources().getDrawable(R.drawable.dare_edittext));
			fzrEdit.setBackground(getResources().getDrawable(R.drawable.dare_edittext));
			telEdit.setBackground(getResources().getDrawable(R.drawable.dare_edittext));
			bzEdit.setBackground(getResources().getDrawable(R.drawable.dare_edittext));
			go1ImageView.setVisibility(View.VISIBLE);
			go2ImageView.setVisibility(View.VISIBLE);
//			defaultRuTypeEdit.setBackground(getResources().getDrawable(R.drawable.dare_edittext));
//			defaultChuTypeEdit.setBackground(getResources().getDrawable(R.drawable.dare_edittext));
		}else{
			telTextView.setVisibility(View.VISIBLE);
			telEdit.setVisibility(View.GONE);
			cknameEdit.setBackgroundResource(R.color.transparent);
			fzrEdit.setBackgroundResource(R.color.transparent);
			telEdit.setBackgroundResource(R.color.transparent);
			bzEdit.setBackgroundResource(R.color.transparent);
			go1ImageView.setVisibility(View.GONE);
			go2ImageView.setVisibility(View.GONE);
//			defaultRuTypeEdit.setBackgroundResource(R.color.transparent);
//			defaultChuTypeEdit.setBackgroundResource(R.color.transparent);
		}
	}
	
	class modfiyckAsyncTask extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO 自动生成的方法存根
			String jsonString = WebserviceImport_more.UpdateCK_1_0(checkid, params[0], mSharedPreferences);
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
					if(WebserviceImport.isOpenNetwork(CKListActivity.this)){
						telTextView.setText(telEdit.getText().toString().trim());
						new Thread(downloadRun).start();
						Toast.makeText(CKListActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					}else{
						pro_Dialog.dismiss();
						Toast.makeText(CKListActivity.this, "网络未连接,没有刷新成功", Toast.LENGTH_SHORT).show();
					}
					break;
				case -1:
					pro_Dialog.dismiss();
					Toast.makeText(CKListActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -2:
					pro_Dialog.dismiss();
					Toast.makeText(CKListActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -3:
					pro_Dialog.dismiss();
					Toast.makeText(CKListActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -4:
					pro_Dialog.dismiss();
					Toast.makeText(CKListActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				default:
					Toast.makeText(CKListActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				}
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				pro_Dialog.dismiss();
				e.printStackTrace();
			}
		}
	}
	
	Runnable downloadRun = new Runnable(){
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message msg = new Message();
			ls=WebserviceImport.GetCK(str, str1,mSharedPreferences);
			if(ls!=null&&ls.size()!=0){
				dm_op.Del_CK();
				String[] values=new String[str.length];
				for(int i=0;i<ls.size();i++){
					String id=(String) ls.get(i).get(DataBaseHelper.ID);
					if(id!=null&& !id.equals("")){
						for(int j=0;j<str.length;j++){
							values[j]=(String) ls.get(i).get(str[j]);
						}
						dm_op.insert_into(DataBaseHelper.TB_CK, str, values);
					}
				}
			}
			msg.what=1;
			msg.setTarget(mHandler);
            mHandler.sendMessage(msg);
		}
	 };
	 @SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler(){
		 public void handleMessage(Message msg){
			 pro_Dialog.dismiss();
			 if(ls==null||ls.size()==0){
				 Toast.makeText(CKListActivity.this, "未获取到数据", Toast.LENGTH_SHORT).show();
			 }else{
				 ls=dm_op.Gt_ck(str);
				 ListIterator<Map<String, Object>> it=ls.listIterator();
					while(it.hasNext()){
						Map<String, Object> map=it.next();
						map.put("check", false);
						it.set(map);
					}
					MyListAdapter myListAdapter = new MyListAdapter(CKListActivity.this, ls);
					mListView.setAdapter(myListAdapter);
			 }
		 }
	 };
	 
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount() ==0 ){
		    finish(); 
		}
		return super.onKeyDown(keyCode, event);
	}
	
	class MyListAdapter extends BaseAdapter{
		List<Map<String, Object>> mList =new ArrayList<Map<String,Object>>();
		LayoutInflater inflater ;
		public MyListAdapter(Context context,List<Map<String, Object>> list){
			inflater = LayoutInflater.from(context);
			mList = list;
		}
		@Override
		public int getCount() {
			// TODO 自动生成的方法存根
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO 自动生成的方法存根
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO 自动生成的方法存根
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO 自动生成的方法存根
			TextView mTextView = null;
			if(convertView==null){
				convertView = inflater.inflate(R.layout.lbchoseitem, null);
				mTextView = (TextView) convertView.findViewById(R.id.lbitem);
				convertView.setTag(mTextView);
			}else{
				mTextView=(TextView) convertView.getTag();
			}
			String ckmcString = mList.get(position).get(DataBaseHelper.CKMC).toString();
			mTextView.setText(ckmcString==null?"":ckmcString);
			
			if((Boolean) ((Map<String, Object>)getItem(position)).get("check")){
				convertView.setBackgroundColor(getResources().getColor(R.color.blue3));
			}else{
				convertView.setBackgroundColor(getResources().getColor(R.color.transparent));
			}
			
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO 自动生成的方法存根
					ListIterator<Map<String, Object>> it=mList.listIterator();
					while(it.hasNext()){
						Map<String, Object> map=it.next();
						map.put("check", false);
						it.set(map);
					}
					mList.get(position).put("check", true);
					
					Map<String, Object> map = ls.get(position);
					
					checkid = Integer.parseInt(map.get(DataBaseHelper.ID).toString());
					
					Object ckmcObject = map.get(DataBaseHelper.CKMC);
					cknameEdit.setText(ckmcObject==null?"":ckmcObject.toString());

					Object fzrObject = map.get(DataBaseHelper.FZR);
					fzrEdit.setText(fzrObject==null?"":fzrObject.toString());
					
					Object defaultRuTypeObject = map.get(DataBaseHelper.INACT);
					defaultRuTypeTxtView.setText(defaultRuTypeObject==null?"":defaultRuTypeObject.toString());
					
					Object defaultChuTypeObject = map.get(DataBaseHelper.OUTACT);
					defaultChuTypeTxtView.setText(defaultChuTypeObject==null?"":defaultChuTypeObject.toString());
					
					Object telObject = map.get(DataBaseHelper.TEL);
					if(telObject!=null){
						String tel1=telObject.toString();
						String[] ss=Gt_tel(tel1);
						String tel2="";
						for(int i=0;i<ss.length;i++){
							tel2=tel2+ss[i]+"\t";
						}
						SpannableString num1=new SpannableString(tel2);
						int position=0;
						for(int i=0;i<ss.length;i++){
							if(i==0){
								num1.setSpan(new URLSpan("tel:"+ss[i]), 0, ss[i].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
								position=ss[i].length()+1;
							}else{
								num1.setSpan(new URLSpan("tel:"+ss[i]), position, position+ss[i].length(), 
										Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
								position=position+ss[i].length()+1;
							}
							
						}
						telEdit.setText(tel1);
						telTextView.setText(num1);
						telTextView.setMovementMethod(LinkMovementMethod.getInstance());
					}else{
						telTextView.setText("");
						telEdit.setText("");
					}
					Object bzObject = map.get(DataBaseHelper.BZ);
					bzEdit.setText(bzObject==null?"":bzObject.toString());
					notifyDataSetChanged();
					
					if(!ckinfoLayout.isShown()){
						Animation animation = AnimationUtils.loadAnimation(CKListActivity.this, R.anim.cangkuinfo_translate_in);
						animation.setFillAfter(true);
						animation.setDuration(200);
						animation.setAnimationListener(new AnimationListener() {
							
							@Override
							public void onAnimationStart(Animation animation) {
								// TODO 自动生成的方法存根
								ckinfoLayout.setVisibility(View.VISIBLE);
							}
							
							@Override
							public void onAnimationRepeat(Animation animation) {
								// TODO 自动生成的方法存根
								
							}
							
							@Override
							public void onAnimationEnd(Animation animation) {
								// TODO 自动生成的方法存根
								ckinfoLayout.clearAnimation();
								
							}
						});
						ckinfoLayout.startAnimation(animation);
					}
				}
			});
			return convertView;
		}
		
	}
	
	private String[] Gt_tel(String tels){
		 List<String> list=new ArrayList<String>();
		 String str="";
		 String[] ss;
		 char[] chs=tels.toCharArray();
		 char[] chs2 = {' '};
		 for(int i=0;i<chs.length;i++){
			 if(Character.isDigit(chs[i])||chs[i]=='-'){
				 chs2[0]=chs[i];
				 str=str+String.valueOf(chs2);
			 }else{
				 if(str!=null&&!str.equals("")){
					 list.add(str);
					 str="";
				 }
			 }
		 }
		 if(str!=null&&!str.equals("")){
			 list.add(str);
			 str="";
		 }
		 ss=new String[list.size()];
		 for(int j=0;j<list.size();j++){
			 ss[j]=list.get(j);
		 }
		 return ss;
	 }
}
