package com.guantang.cangkuonline.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.guantang.cangkuonline.R;

import android.R.integer;
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
import android.renderscript.Element;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.guantang.cangkuonline.application.MyApplication;
import com.guantang.cangkuonline.database.DataBaseCheckMethod;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.database.DataBaseImport;
import com.guantang.cangkuonline.database.DataBaseMethod;
import com.guantang.cangkuonline.database.DataBaseOperateMethod;
import com.guantang.cangkuonline.helper.CheckEditWatcher;
import com.guantang.cangkuonline.mulu.ElementBean;
import com.guantang.cangkuonline.mulu.MuLuFlowLayout;
import com.guantang.cangkuonline.mulu.MuluAdapter;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;
import com.guantang.cangkuonline.webservice.WebserviceImport;
import com.guantang.cangkuonline.webservice.WebserviceImport_more;

import de.greenrobot.event.EventBus;

public class AddDepActivity extends Activity implements
		OnClickListener, OnItemClickListener {
	private ImageButton back, add;
//	private Button up;
	private TextView title,dingcengTxtView;
	private ListView list;
	private HorizontalScrollView hScrollView;
	private DataBaseOperateMethod dm_op = new DataBaseOperateMethod(this);
	private DataBaseCheckMethod dm_ck = new DataBaseCheckMethod(this);
	private DataBaseMethod dm = new DataBaseMethod(this);
	private SimpleAdapter listItemAdapter;
	private List<Map<String, Object>> ls;
	private boolean isadd = false, isdel = false;
	private ProgressDialog pro_dialog;
	private String pid = "0";
	private String addId = "0";//新添加部门所在的id;
	private String[] str16 = { "ID", "name", "dwlevel", "PID", "dwOrder",
			"dwIndex" };
	private String[] str17 = { DataBaseHelper.ID, DataBaseHelper.Name,
			DataBaseHelper.depLevel, DataBaseHelper.PID,
			DataBaseHelper.depOrder, DataBaseHelper.depindex };
	private SharedPreferences mSharedPreferences;
	private MuLuFlowLayout mMuLuFlowLayout;
	private MuluAdapter mMuluAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.operate_type);
		mSharedPreferences = getSharedPreferences(
				ShareprefenceBean.SHAREPREFENCE_NAME, Context.MODE_PRIVATE);
		EventBus.getDefault().register(this);
		initControl();
		init();
	}
	
	@Override
	protected void onDestroy() {
		// TODO 自动生成的方法存根
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
	
	public void initControl() {
		back = (ImageButton) findViewById(R.id.back);
		add = (ImageButton) findViewById(R.id.add);
//		up = (Button) findViewById(R.id.up);
		title = (TextView) findViewById(R.id.title);
		list = (ListView) findViewById(R.id.list);
		dingcengTxtView =(TextView) findViewById(R.id.dingcengTxtView);
		mMuLuFlowLayout = (MuLuFlowLayout) findViewById(R.id.firstLagFlowLayout);
		hScrollView = (HorizontalScrollView) findViewById(R.id.hScrollView);
		
		mMuluAdapter = new MuluAdapter(this);
		mMuLuFlowLayout.setAdapter(mMuluAdapter);
		
		back.setOnClickListener(this);
		add.setOnClickListener(this);
		dingcengTxtView.setOnClickListener(this);
//		up.setOnClickListener(this);
		list.setOnItemClickListener(this);
		title.setText("部门类型设置");
	}

	public void init() {
		ls = new ArrayList<Map<String, Object>>();
		if (mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1) == 1) {
			if (WebserviceImport.isOpenNetwork(this)) {
				pro_dialog = ProgressDialog.show(this, null, "正在刷新数据");
				new firstLoadTask().execute();
			} else {
				Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
			}
		} else {
			ls = dm.GetDep("0");
			if (ls != null) {
				setAdapter(ls);
			}
		}

	}

	public void setAdapter(List<Map<String, Object>> ls) {
		listItemAdapter = new SimpleAdapter(this, ls, R.layout.lbchoseitem,
				new String[] { "name" }, new int[] { R.id.lbitem });
		list.setAdapter(listItemAdapter);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.add:
			if (mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1) == 1) {
				if(!mSharedPreferences.getString(ShareprefenceBean.DWNAME_LOGIN, "").equals("测试") && mSharedPreferences.getInt(ShareprefenceBean.TIYANZHANGHAO, 0)!=1){
					modifyDWName();
				}else{
					Toast.makeText(this, "测试账户不能添加部门类型", Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(this, "离线模式不能添加部门类型", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.dingcengTxtView:
			mMuluAdapter.addData(new ArrayList<ElementBean>());
			pid = "0";
			ls = dm.GetDep("0");
			if (ls != null) {
				setAdapter(ls);
			}
			break;
//		case R.id.up:
//			if(!pid.equals("0")){
//				pid = dm.gettb_deptreePid(Integer.parseInt(pid));
//				ls = dm.GetDep(pid);
//				setAdapter(ls);
//				mMuluAdapter.removeLastElementBean();
//			}
//			break;
		}
	}
	
	public void modifyDWName(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		layout.setGravity(Gravity.CENTER);
		final EditText myEditText = new EditText(this);
		lp.setMargins(10, 10, 10, 10);
		lp.gravity = Gravity.CENTER;
		myEditText.setLayoutParams(lp);
		myEditText.setBackgroundResource(R.drawable.dare_edittext);
		myEditText.setPadding(10, 10, 10, 10);
		layout.addView(myEditText);
		builder.setTitle("添加本级部门类型");
		builder.setView(layout);
		builder.setNegativeButton("取消",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO 自动生成的方法存根
						dialog.dismiss();
					}
				});
		builder.setPositiveButton("确认",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO 自动生成的方法存根
						if(!myEditText.getText().toString().trim().equals("")){
							if(WebserviceImport.isOpenNetwork(AddDepActivity.this)){
								pro_dialog = ProgressDialog.show(AddDepActivity.this, null, "正在新增数据");
								new addDepAsyncTask().execute(myEditText.getText().toString().trim(),pid);
							}else{
								Toast.makeText(AddDepActivity.this, "网络未连接", Toast.LENGTH_SHORT).show();
							}
							dialog.dismiss();
						}else {
							Toast.makeText(AddDepActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
						}
						
					}
				});
		builder.create().show();
	}
	
	class firstLoadTask extends AsyncTask<Void, Void, Integer>{

		@Override
		protected Integer doInBackground(Void... params) {
			// TODO 自动生成的方法存根
			int what = 0;
			try {
				ls = WebserviceImport.GetDep(str17, str16, mSharedPreferences);
				String[] values = new String[str16.length];
				dm_op.del_tableContent(DataBaseHelper.TB_depTree,DataBaseImport.getInstance(AddDepActivity.this).getReadableDatabase());
				if (ls != null && ls.size() > 0) {
					for (int i = 0; i < ls.size(); i++) {
						for (int j = 0; j < str16.length; j++) {
							values[j] = (String) ls.get(i).get(str17[j]);
						}
						dm_op.insert_into(DataBaseHelper.TB_depTree, str17,values);
					}
					what = 1;
				} else {
					what = -2;
				}
			} catch (Exception e) {
				what = -1;
			}
			return what;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			// TODO 自动生成的方法存根
			super.onPostExecute(result);
			switch (result) {
			case 1:
				pro_dialog.dismiss();
				if(addId.equals("0")){
					ls = dm.GetDep("0");
					pid="0";
				}else{
					pid=dm.gettb_deptreePid(Integer.parseInt(addId));
					ls = dm.GetDep(pid);
					addId="0";
				}
				if (ls != null) {
					setAdapter(ls);
				}
				break;
			case -1:
				pro_dialog.dismiss();
				Toast.makeText(AddDepActivity.this, "获取数据异常",Toast.LENGTH_SHORT).show();
				break;
			case -2:
				pro_dialog.dismiss();
				Toast.makeText(AddDepActivity.this, "没有获得新数据",Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
	}
	
	class addDepAsyncTask extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO 自动生成的方法存根
			String JSONString = WebserviceImport_more.AddDepType_1_0(params[0], Integer.parseInt(params[1]), mSharedPreferences);
			return JSONString;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO 自动生成的方法存根
			super.onPostExecute(result);
			try {
				JSONObject jsonObject = new JSONObject(result);
				switch(jsonObject.getInt("Status")){
				case 1:
					if (WebserviceImport.isOpenNetwork(AddDepActivity.this)) {
						addId=jsonObject.getString("Data");
						new firstLoadTask().execute();
						Toast.makeText(AddDepActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(AddDepActivity.this, "网络未连接，没有刷新成功", Toast.LENGTH_SHORT).show();
					}
					break;
				case -1:
					pro_dialog.dismiss();
					Toast.makeText(AddDepActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -2:
					pro_dialog.dismiss();
					Toast.makeText(AddDepActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -3:
					pro_dialog.dismiss();
					Toast.makeText(AddDepActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -4:
					pro_dialog.dismiss();
					Toast.makeText(AddDepActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -5:
					pro_dialog.dismiss();
					Toast.makeText(AddDepActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				default:
					pro_dialog.dismiss();
					Toast.makeText(AddDepActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				}
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				pro_dialog.dismiss();
				e.printStackTrace();
			}
			
		}
	}

	public void onEventMainThread(ElementBean bean) {
		pid = bean.getId();
		ls = dm.GetDep(pid);
		if (ls != null) {
			setAdapter(ls);
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
			Map<String, Object> map = ls.get(arg2);
			pid = map.get(DataBaseHelper.ID).toString();
			
			ElementBean elementBean = new ElementBean();
//			elementBean.setPid(map.get(DataBaseHelper.PID).toString());
			elementBean.setId(map.get(DataBaseHelper.ID).toString());
			elementBean.setName(map.get(DataBaseHelper.Name).toString());
			elementBean.setLev(Integer.parseInt(map.get(DataBaseHelper.depLevel).toString()));
			mMuluAdapter.addData(elementBean);
			hScrollView.setFillViewport(true);
			ls = dm.GetDep(pid);
			if (ls != null) {
				setAdapter(ls);
			}
			hScrollView.post(new Runnable() {//开启一个线程滑动到最末端
				
				@Override
				public void run() {
					// TODO 自动生成的方法存根
					hScrollView.fullScroll(ScrollView.FOCUS_RIGHT);
//					hScrollView.fullScroll(ScrollView.FOCUS_DOWN);
				}
			});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if(!pid.equals("0")){
				pid = dm.gettb_deptreePid(Integer.parseInt(pid));
				ls = dm.GetDep(pid);
				setAdapter(ls);
				mMuluAdapter.removeLastElementBean();
				return false;
			}else{
				finish();
				return true;
			}
		}else{
			return false;
		}
//		return super.onKeyDown(keyCode, event);
	}
}
