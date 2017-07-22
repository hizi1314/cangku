package com.guantang.cangkuonline.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.activity.AddDepActivity.addDepAsyncTask;
import com.guantang.cangkuonline.activity.AddDepActivity.firstLoadTask;
import com.guantang.cangkuonline.application.MyApplication;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.database.DataBaseImport;
import com.guantang.cangkuonline.database.DataBaseMethod;
import com.guantang.cangkuonline.database.DataBaseOperateMethod;
import com.guantang.cangkuonline.mulu.ElementBean;
import com.guantang.cangkuonline.mulu.MuLuFlowLayout;
import com.guantang.cangkuonline.mulu.MuluAdapter;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;
import com.guantang.cangkuonline.webservice.WebserviceImport;
import com.guantang.cangkuonline.webservice.WebserviceImport_more;

import de.greenrobot.event.EventBus;
import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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

public class AddLBActivity extends Activity implements OnClickListener,
		OnItemClickListener {
	private ImageButton backImgBtn, addImgBtn;
//	private Button upBtn;
	private TextView titleTextView,dingcengTxtView;
	private ListView mListView;
	private HorizontalScrollView hScrollView;
	private List<Map<String, Object>> mlist = new ArrayList<Map<String, Object>>();
	private ProgressDialog pro_dialog;
	private DataBaseMethod dm = new DataBaseMethod(this);
	private DataBaseOperateMethod dm_op = new DataBaseOperateMethod(this);
	private SimpleAdapter listItemAdapter;
	private String pid = "0";
	private String addId = "0";//新添加部门所在的id;
	private String[] str2 = { DataBaseHelper.ID, DataBaseHelper.Name,
			DataBaseHelper.Lev, DataBaseHelper.PID, DataBaseHelper.Ord,
			DataBaseHelper.Sindex };
	private String[] str3 = { "ID", "name", "lev", "PID", "ord", "sindex" };
	private MuLuFlowLayout mMuLuFlowLayout;
	private MuluAdapter mMuluAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.operate_type);
		EventBus.getDefault().register(this);
		initView();
		init();
	}
	
	@Override
	protected void onDestroy() {
		// TODO 自动生成的方法存根
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
	
	public void initView() {
		backImgBtn = (ImageButton) findViewById(R.id.back);
		addImgBtn = (ImageButton) findViewById(R.id.add);
//		upBtn = (Button) findViewById(R.id.up);
		titleTextView = (TextView) findViewById(R.id.title);
		mListView = (ListView) findViewById(R.id.list);
		dingcengTxtView =(TextView) findViewById(R.id.dingcengTxtView);
		mMuLuFlowLayout = (MuLuFlowLayout) findViewById(R.id.firstLagFlowLayout);
		hScrollView = (HorizontalScrollView) findViewById(R.id.hScrollView);
		
		mMuluAdapter = new MuluAdapter(this);
		mMuLuFlowLayout.setAdapter(mMuluAdapter);
		
		dingcengTxtView.setOnClickListener(this);
		backImgBtn.setOnClickListener(this);
		addImgBtn.setOnClickListener(this);
//		upBtn.setOnClickListener(this);
		mListView.setOnItemClickListener(this);

		titleTextView.setText("货品类型设置");
	}

	public void init() {

		if (MyApplication.getInstance().getSharedPreferences()
				.getInt(ShareprefenceBean.IS_LOGIN, -1) == 1) {
			if (WebserviceImport.isOpenNetwork(this)) {
				pro_dialog = ProgressDialog.show(this, null, "正在刷新数据");
				new firstLoadTask().execute();
			} else {
				Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
			}
		} else {
			mlist = dm.GetLB("0");
			if (mlist != null) {
				setAdapter(mlist);
			}
		}

	}

	public void setAdapter(List<Map<String, Object>> ls) {
		listItemAdapter = new SimpleAdapter(this, ls, R.layout.lbchoseitem,
				new String[] { "name" }, new int[] { R.id.lbitem });
		mListView.setAdapter(listItemAdapter);
	}

	class firstLoadTask extends AsyncTask<Void, Void, Integer> {

		@Override
		protected Integer doInBackground(Void... params) {
			// TODO 自动生成的方法存根
			int what = 0;
			try {
				mlist = WebserviceImport.GetHPLB(-1, "0", str2, str3,MyApplication.getInstance().getSharedPreferences());
				String[] values=new String[str2.length];
				dm_op.del_tableContent(DataBaseHelper.TB_hplbTree,DataBaseImport.getInstance(AddLBActivity.this).getReadableDatabase());
				if(mlist!=null&&mlist.size()!=0){
     				for(int i=0;i<mlist.size();i++){
    					for(int j=0;j<str2.length;j++){
    						values[j]=(String) mlist.get(i).get(str2[j]);
    					}
    					dm_op.insert_into_fromfile(DataBaseHelper.TB_hplbTree, str2, 
    							values, DataBaseImport.getInstance(AddLBActivity.this).getReadableDatabase());
     				}
     				what = 1;
     			}else{
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
					mlist = dm.GetLB("0");
					pid="0";
				}else{
					pid=dm.gettb_hplbtreePid(Integer.parseInt(addId));
					mlist = dm.GetLB(pid);
					addId="0";
				}
				if (mlist != null) {
					setAdapter(mlist);
				}
				break;
			case -1:
				pro_dialog.dismiss();
				Toast.makeText(AddLBActivity.this, "获取数据异常",Toast.LENGTH_SHORT).show();
				break;
			case -2:
				pro_dialog.dismiss();
				Toast.makeText(AddLBActivity.this, "没有获得新数据",Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}

	}
	
	class addHplbAsynctask extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO 自动生成的方法存根
			String jsonString = WebserviceImport_more.AddHpType_1_0(params[0], Integer.parseInt(params[1]), MyApplication.getInstance().getSharedPreferences());
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
					if (WebserviceImport.isOpenNetwork(AddLBActivity.this)) {
						addId=jsonObject.getString("Data");
						new firstLoadTask().execute();
						Toast.makeText(AddLBActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(AddLBActivity.this, "网络未连接,没有刷新成功", Toast.LENGTH_SHORT).show();
					}
					break;
				case -1:
					pro_dialog.dismiss();
					Toast.makeText(AddLBActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -2:
					pro_dialog.dismiss();
					Toast.makeText(AddLBActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -3:
					pro_dialog.dismiss();
					Toast.makeText(AddLBActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -4:
					pro_dialog.dismiss();
					Toast.makeText(AddLBActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -5:
					pro_dialog.dismiss();
					Toast.makeText(AddLBActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				default:
					pro_dialog.dismiss();
					Toast.makeText(AddLBActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				}
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				pro_dialog.dismiss();
				e.printStackTrace();
			}
			
		}
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.add:
			if (MyApplication.getInstance().getSharedPreferences()
					.getInt(ShareprefenceBean.IS_LOGIN, -1) == 1) {
				if(!MyApplication.getInstance().getSharedPreferences().getString(ShareprefenceBean.DWNAME_LOGIN, "").equals("测试") && MyApplication.getInstance().getSharedPreferences().getInt(ShareprefenceBean.TIYANZHANGHAO, 0)!=1){
					modifyDWName();
				}else{
					Toast.makeText(this, "测试账户不能添加货品类型", Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(this, "离线模式不能添加货品类型", Toast.LENGTH_SHORT).show();
			}
			
			break;
		case R.id.dingcengTxtView:
			mMuluAdapter.addData(new ArrayList<ElementBean>());
			pid = "0";
			mlist = dm.GetLB("0");
			if (mlist != null) {
				setAdapter(mlist);
			}
			break;
//		case R.id.up:
//			if(!pid.equals("0")){
//				pid = dm.gettb_hplbtreePid(Integer.parseInt(pid));
//				mlist = dm.GetLB(pid);
//				setAdapter(mlist);
//				mMuluAdapter.removeLastElementBean();
//			}
//			break;
		}
	}

	public void modifyDWName() {
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
		builder.setTitle("添加本级货品类型");
		builder.setView(layout);
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO 自动生成的方法存根
				dialog.dismiss();
			}
		});
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO 自动生成的方法存根
				if (!myEditText.getText().toString().trim().equals("")) {
					if (WebserviceImport.isOpenNetwork(AddLBActivity.this)){
						pro_dialog = ProgressDialog.show(AddLBActivity.this,null, "正在新增数据");
						new addHplbAsynctask().execute(myEditText.getText().toString().trim(), pid);
					} else {
						Toast.makeText(AddLBActivity.this, "网络未连接",
								Toast.LENGTH_SHORT).show();
					}
					dialog.dismiss();
				} else {
					Toast.makeText(AddLBActivity.this, "内容不能为空",
							Toast.LENGTH_SHORT).show();
				}

			}
		});
		builder.create().show();
	}

	public void onEventMainThread(ElementBean bean) {
		pid = bean.getId();
		mlist = dm.GetLB(pid);
		if (mlist != null) {
			setAdapter(mlist);
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO 自动生成的方法存根
//		pid = (String) mlist.get(arg2).get("pid");
		Map<String, Object> map = mlist.get(arg2);
		pid = map.get(DataBaseHelper.ID).toString();
		
		ElementBean elementBean = new ElementBean();
//		elementBean.setPid(map.get(DataBaseHelper.PID).toString());
		elementBean.setId(map.get(DataBaseHelper.ID).toString());
		elementBean.setName(map.get(DataBaseHelper.Name).toString());
		elementBean.setLev(Integer.parseInt(map.get(DataBaseHelper.Lev).toString()));
		mMuluAdapter.addData(elementBean);
		mlist = dm.GetLB(pid);
		if (mlist != null) {
			setAdapter(mlist);
		}
		hScrollView.post(new Runnable() {//开启一个线程滑动到最末端
			
			@Override
			public void run() {
				// TODO 自动生成的方法存根
				hScrollView.fullScroll(ScrollView.FOCUS_RIGHT);
//				hScrollView.fullScroll(ScrollView.FOCUS_DOWN); 
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if(!pid.equals("0")){
				pid = dm.gettb_hplbtreePid(Integer.parseInt(pid));
				mlist = dm.GetLB(pid);
				setAdapter(mlist);
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
