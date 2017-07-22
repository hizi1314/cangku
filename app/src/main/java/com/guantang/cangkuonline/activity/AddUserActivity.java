package com.guantang.cangkuonline.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.commonadapter.CommonAdapter;
import com.guantang.cangkuonline.commonadapter.ViewHolder;
import com.guantang.cangkuonline.dialog.CommonDialog;
import com.guantang.cangkuonline.dialog.CommonDialog.DialogContentListener;
import com.guantang.cangkuonline.webservice.WebserviceImport;
import com.guantang.cangkuonline.webservice.WebserviceImport_more;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SearchViewCompat.OnCloseListenerCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AddUserActivity extends Activity implements OnClickListener{
	
	private ImageButton backImgBtn;
	private EditText userEditText,passwordEditText,confirmpasswordEditText;
	private LinearLayout layout1,layout2,layout3,layout4;
	private TextView textview1,textview2,textview3,textview4;
	private Button commitBtn;
	private List<Map<String,Object>> MyList = new ArrayList<Map<String,Object>>();
	private List<String> userGrouplist = new ArrayList<String>();
	private List<String> employeelist = new ArrayList<String>();
	private List<Map<String,Object>> ckArraylist = new ArrayList<Map<String,Object>>();
	private List<Map<String,Object>> tb_hplbtreelist = new ArrayList<Map<String,Object>>();
	private ProgressDialog pro_Dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_adduser);
		init();
	}
	
	public void init(){
		backImgBtn=(ImageButton) findViewById(R.id.back);
		userEditText = (EditText) findViewById(R.id.userEditText);
		passwordEditText = (EditText) findViewById(R.id.passwordEditText);
		confirmpasswordEditText = (EditText) findViewById(R.id.confirmpasswordEditText);
		layout1 = (LinearLayout) findViewById(R.id.layout1);
		layout2 = (LinearLayout) findViewById(R.id.layout2);
		layout3 = (LinearLayout) findViewById(R.id.layout3);
		layout4 = (LinearLayout) findViewById(R.id.layout4);
		textview1 = (TextView) findViewById(R.id.textview1);
		textview2 = (TextView) findViewById(R.id.textview2);
		textview3 = (TextView) findViewById(R.id.textview3);
		textview4 = (TextView) findViewById(R.id.textview4);
		commitBtn = (Button) findViewById(R.id.commitBtn);
		
		backImgBtn.setOnClickListener(this);
		commitBtn.setOnClickListener(this);
		layout1.setOnClickListener(this);
		layout2.setOnClickListener(this);
		layout3.setOnClickListener(this);
		layout4.setOnClickListener(this);
		
		if(WebserviceImport.isOpenNetwork(this)){
			pro_Dialog = ProgressDialog.show(this, null, "正在加载");
			new Get_AllInformationAsyncTask().execute();
		}else{
			Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch(v.getId()){
		case R.id.back:
			finish();
			break;
		case R.id.layout1:
			myDialog(textview1, employeelist);
			break;
		case R.id.layout2:
			myDialog(textview2, userGrouplist);
			break;
		case R.id.layout3:
			RadioButtonDialog(tb_hplbtreelist);
			break;
		case R.id.layout4:
			CheckBoxDialog(ckArraylist);
			break;
		case R.id.commitBtn:
			if(passwordEditText.getText().toString().trim().equals(confirmpasswordEditText.getText().toString().trim())){
//				{"Username":"张三","Password":"123","Group":"测试用户","Employee":"张三","Hplb":"成品货物","Ck":"实验仓库"}
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("Username", userEditText.getText().toString().trim());
				map.put("Password", passwordEditText.getText().toString().trim());
				map.put("Group", textview2.getText().toString().trim());
				map.put("Employee", textview1.getText().toString().trim());
				map.put("Hplb", textview3.getText().toString().trim());
				
				List<String> list = new ArrayList<String>();
				Iterator<Map<String,Object>> it=MyList.iterator();
				while(it.hasNext()){
					list.add(it.next().get("ckname").toString());
				}
				JSONArray jsonArray = new JSONArray(list);
				map.put("Ck", jsonArray);
				JSONObject jsonObject = new JSONObject(map);
				if(WebserviceImport.isOpenNetwork(this)){
					pro_Dialog = ProgressDialog.show(this, null, "正在提交");
					new AddUserAsyncTask().execute(jsonObject.toString());
				}else{
					Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
				}
			}else{
				Toast.makeText(this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}
	
	class Get_AllInformationAsyncTask extends AsyncTask<Void, Void, String>{

		@Override
		protected String doInBackground(Void... params) {
			// TODO 自动生成的方法存根
			String json = WebserviceImport_more.Get_AddUserFillInformation_1_0();
			return json;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO 自动生成的方法存根
			super.onPostExecute(result);
			try {
				JSONObject jsonObject = new JSONObject(result);
				switch(jsonObject.getInt("Status")){
				case 1:
					JSONObject dataJSONObject = jsonObject.getJSONObject("Data");
					JSONArray userGroup = dataJSONObject.getJSONArray("userGroup");
					int length = userGroup.length();
					for(int i=0;i<length;i++){
						userGrouplist.add(userGroup.getString(i));
					}
					
					JSONArray employee = dataJSONObject.getJSONArray("employee");
					length = employee.length();
					for(int i=0;i<length;i++){
						employeelist.add(employee.getString(i));
					}
					
					JSONArray ckArray = dataJSONObject.getJSONArray("ck");
					length = ckArray.length();
					for(int i=0;i<=length;i++){
						Map<String,Object> map = new HashMap<String, Object>();
						if(i==0){
							map.put("ckname", "所有仓库");
							map.put("select", (Boolean)false);
						}else{
							map.put("ckname", ckArray.getString(i-1));
							map.put("select", (Boolean)false);
						}
						ckArraylist.add(map);
					}
					
					JSONArray tb_hplbtree = dataJSONObject.getJSONArray("tb_hplbTree");
					length = tb_hplbtree.length();
					for(int i=0;i<=length;i++){
						Map<String,Object> map = new HashMap<String, Object>();
						if(i==0){
							map.put("hplb", "所有类型");
							map.put("select", (Boolean)false);
						}else{
							map.put("hplb", tb_hplbtree.getString(i-1));
							map.put("select", (Boolean)false);
						}
						tb_hplbtreelist.add(map);
					}
					break;
				case -1:
					Toast.makeText(AddUserActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -2:
					Toast.makeText(AddUserActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -3:
					Toast.makeText(AddUserActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				default:
					Toast.makeText(AddUserActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				}
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}finally{
				pro_Dialog.dismiss();
			}
		}
	}
	
	
	class AddUserAsyncTask extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO 自动生成的方法存根
			String json = WebserviceImport_more.Add_User_1_0(params[0]);
			return json;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO 自动生成的方法存根
			super.onPostExecute(result);
			try {
				JSONObject jsonObject = new JSONObject(result);
				switch(jsonObject.getInt("Status")){
				case 1:
					finish();
					Toast.makeText(AddUserActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -1:
					Toast.makeText(AddUserActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -2:
					Toast.makeText(AddUserActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -3:
					Toast.makeText(AddUserActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -4:
					Toast.makeText(AddUserActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -5:
					Toast.makeText(AddUserActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				default:
					Toast.makeText(AddUserActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break; 
				}
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}finally{
				pro_Dialog.dismiss();
			}
		}
		
	}
	
	public void myDialog(final TextView v, final List<String> list){
		final CommonDialog mDialog = new CommonDialog(this, R.layout.popupwindow_list, R.style.yuanjiao_activity);
		mDialog.setCancelable(true);
		mDialog.setDialogContentListener(new DialogContentListener() {

			@Override
			public void contentExecute(View parent, Dialog dialog,Object[] objs) {
				// TODO 自动生成的方法存根
				ListView myListView = (ListView) parent.findViewById(R.id.popuplist);
				MyAdapter myAdapter = new MyAdapter(AddUserActivity.this,list, R.layout.popupwindow_list_textview);
				myListView.setAdapter(myAdapter);
				myListView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO 自动生成的方法存根
						String str = (String) arg0.getAdapter().getItem(arg2);
						v.setText(str);
						mDialog.dismiss();
					}
				});
			}
		},null);
		mDialog.show();
	}
	
	public void RadioButtonDialog(List<Map<String,Object>> list){
		final CommonDialog mDialog = new CommonDialog(this,R.layout.select_dialog,R.style.yuanjiao_activity);
		mDialog.setCancelable(false);
		mDialog.setDialogContentListener(new DialogContentListener() {
			
			@Override
			public void contentExecute(View parent, Dialog dialog,Object[] objs) {
				// TODO 自动生成的方法存根
				TextView mTextView = (TextView) parent.findViewById(R.id.titletextview);
				ListView myListView = (ListView) parent.findViewById(R.id.mylist);
				Button confirmBtn = (Button) parent.findViewById(R.id.confirmBtn);
				mTextView.setText("管辖货品管理");
				confirmBtn.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO 自动生成的方法存根
						
						mDialog.dismiss();
					}
				});
				RadioBtnAdapter mRadioBtnAdapter = new RadioBtnAdapter(AddUserActivity.this, tb_hplbtreelist, R.layout.checkbutton_item_layout);
				myListView.setAdapter(mRadioBtnAdapter);
			}
		},null);
		mDialog.show();
	}
	
	public void CheckBoxDialog(List<Map<String,Object>> list){
		final CommonDialog mDialog = new CommonDialog(this,R.layout.select_dialog,R.style.yuanjiao_activity);
		mDialog.setCancelable(false);
		mDialog.setDialogContentListener(new DialogContentListener() {
			
			@Override
			public void contentExecute(View parent, Dialog dialog,Object[] objs) {
				// TODO 自动生成的方法存根
				ListView myListView = (ListView) parent.findViewById(R.id.mylist);
				Button confirmBtn = (Button) parent.findViewById(R.id.confirmBtn);
				confirmBtn.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO 自动生成的方法存根
						StringBuilder sb = new StringBuilder();
						Iterator<Map<String,Object>> it = MyList.iterator();
						
						while(it.hasNext()){
							if(sb.length()<1){
								sb.append(it.next().get("ckname").toString());
							}else{
								sb.append(","+it.next().get("ckname").toString());
							}
						}
						textview4.setText(sb.toString());
						mDialog.dismiss();
					}
				});
				CheckBoxAdapter mCheckBoxAdapter = new CheckBoxAdapter(AddUserActivity.this, ckArraylist, R.layout.checkbutton_item_layout);
				myListView.setAdapter(mCheckBoxAdapter);
			}
		},null);
		mDialog.show();
	}
	
	
	
	class MyAdapter extends CommonAdapter<String>{

		public MyAdapter(Context mContext, List<String> mList, int LayoutId) {
			super(mContext, mList, LayoutId);
			// TODO 自动生成的构造函数存根
		}

		@Override
		public void convert(ViewHolder holder, String item) {
			// TODO 自动生成的方法存根
			TextView mTextView = holder.getView(R.id.textview_popup);
			mTextView.setText(item);
		}
		
	}
	
	class RadioBtnAdapter extends CommonAdapter<Map<String,Object>>{

		public RadioBtnAdapter(Context mContext, List<Map<String,Object>> mList, int LayoutId) {
			super(mContext, mList, LayoutId);
			// TODO 自动生成的构造函数存根
		}

		@Override
		public void convert(ViewHolder holder, Map<String,Object> item) {
			// TODO 自动生成的方法存根
			ImageView mImageView = holder.getView(R.id.checkBtn);
			TextView mTextView = holder.getView(R.id.mTextView);
			mTextView.setText(item.get("hplb").toString());
			
			if((Boolean) item.get("select")){
				mImageView.setBackground(getResources().getDrawable(R.drawable.check_selected));
			}else{
				mImageView.setBackground(getResources().getDrawable(R.drawable.check_default));
			}
			holder.getConvertView().setOnClickListener(new MyOnClickListener(item));
		}
		
		class MyOnClickListener implements OnClickListener{
//			private int mPosition;
			private Map<String,Object> item;
			public MyOnClickListener(Map<String,Object> item){
				this.item = item;
			}
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Iterator<Map<String,Object>> it = getList().iterator();
				while(it.hasNext()){
					Map<String,Object> map = it.next();
					map.put("select", (Boolean)false);
				}
				item.put("select", true);
				textview3.setText(item.get("hplb").toString());
				//将改变的list给全局变量，下次打开dialog还能保持上次的状态。
				tb_hplbtreelist=getList();
				notifyDataSetChanged();
			}
		}
	}
	
	class CheckBoxAdapter extends CommonAdapter<Map<String,Object>>{

		public CheckBoxAdapter(Context mContext, List<Map<String,Object>> mList,
				int LayoutId) {
			super(mContext, mList, LayoutId);
			// TODO 自动生成的构造函数存根
		}

		@Override
		public void convert(ViewHolder holder,Map<String,Object> item) {
			// TODO 自动生成的方法存根
			ImageView mImageView = holder.getView(R.id.checkBtn);
			TextView mTextView = holder.getView(R.id.mTextView);
			mTextView.setText(item.get("ckname").toString());
			if((Boolean) item.get("select")){
				mImageView.setBackground(getResources().getDrawable(R.drawable.check_selected));
			}else{
				mImageView.setBackground(getResources().getDrawable(R.drawable.check_default));
			}
			holder.getConvertView().setOnClickListener(new MyOnClickListener(mImageView, item));
		}
		
		class MyOnClickListener implements OnClickListener{
			
			private CheckBox mCheckBox;
			private Map<String,Object> item;
			public MyOnClickListener(ImageView mImageView,Map<String,Object> item){
				this.mCheckBox = mCheckBox;
				this.item = item;
			}
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if(item.get("ckname").toString().equals("所有仓库")){//选择所有仓库其他仓库就不能选了
					Iterator<Map<String,Object>> it = getList().iterator();
					while(it.hasNext()){
						Map<String,Object> map = it.next();
						map.put("select", (Boolean)false);
					}
					item.put("select", true);
					MyList.clear();
					MyList.add(item);
				}else{
					if((Boolean)item.get("select")){
						item.put("select", (Boolean)false);
						MyList.remove(item);
					}else{
						item.put("select", (Boolean)true);
						MyList.add(item);
					}
					//点击其他仓库，所有仓库就去掉
					MyList.remove(getList().get(0));
					getList().get(0).put("select", false);
				}
				
				//将改变的list给全局变量，下次打开dialog还能保持上次的状态。
				ckArraylist=getList();
				notifyDataSetChanged();
			}
			
		}
		
	}
	
}
