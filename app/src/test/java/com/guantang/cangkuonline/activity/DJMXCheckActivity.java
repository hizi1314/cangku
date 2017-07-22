package com.guantang.cangkuonline.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.database.DataBaseCheckMethod;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.database.DataBaseMethod;
import com.guantang.cangkuonline.database.DataBaseOperateMethod;
import com.guantang.cangkuonline.dialog.AddNumberDialog;
import com.guantang.cangkuonline.eventbusBean.ObjectSix;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;
import com.guantang.cangkuonline.webservice.WebserviceImport;

import de.greenrobot.event.EventBus;

public class DJMXCheckActivity extends Activity implements OnClickListener,OnItemClickListener {
	private ImageButton backImgBtn,refreshImgBtn;
	private TextView titleTextView,errorTextView;
	private ListView mListView;
	private int op_type, ckid;
	private String djid = "",dh = "",dacttype = "";
	private ProgressDialog progressDialog;
	private List<Map<String, Object>> mList = new ArrayList<Map<String,Object>>();
	private List<Map<String, Object>> ErrorList = new ArrayList<Map<String,Object>>();
	private DataBaseOperateMethod dm_op = new DataBaseOperateMethod(this);
	private DataBaseMethod dm = new DataBaseMethod(this);
	private DataBaseCheckMethod dm_ck=new DataBaseCheckMethod(this);
	private String[] str2 = { DataBaseHelper.ID, DataBaseHelper.HPMC,
			DataBaseHelper.HPBM, DataBaseHelper.HPTM, DataBaseHelper.GGXH };
	private String[] str1 = { DataBaseHelper.HPID, DataBaseHelper.BTKC,
			DataBaseHelper.MSL, DataBaseHelper.ATKC,DataBaseHelper.MID };
	private SharedPreferences mSharedPreferences;
	private SimpleAdapter listItemAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_djmxcheck);
		Intent intent = getIntent();
		op_type = intent.getIntExtra("op_type", 2);
		ckid = intent.getIntExtra("ckid", -1);
		djid = intent.getStringExtra("djid");
		dacttype = intent.getStringExtra("dacttype");
		dh = intent.getStringExtra("dh");
		mSharedPreferences = getSharedPreferences(ShareprefenceBean.SHAREPREFENCE_NAME, Context.MODE_PRIVATE);
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
		titleTextView = (TextView) findViewById(R.id.title);
		refreshImgBtn = (ImageButton) findViewById(R.id.refresh);
		errorTextView = (TextView) findViewById(R.id.showerrortext);
		mListView = (ListView) findViewById(R.id.checkList);
		mListView.setOnItemClickListener(this);
		backImgBtn.setOnClickListener(this);
		refreshImgBtn.setOnClickListener(this);
		if (op_type == 2) {
			titleTextView.setText("检测货品不足明细");
		} else if (op_type == 0) {
			titleTextView.setText("检测有误账面数量明细");
		}
	}

	public void init() {
		mList = dm_op.Gt_Moved(djid, str1);
		for (int i = 0; i < mList.size(); i++) {
			String str = (String) mList.get(i).get(DataBaseHelper.HPID);
			List<Map<String, Object>> hpList = dm.Gethp(str2, str);
			mList.get(i).put(DataBaseHelper.HPMC,
					hpList.get(0).get(DataBaseHelper.HPMC).toString());
			mList.get(i).put(DataBaseHelper.HPBM,
					hpList.get(0).get(DataBaseHelper.HPBM).toString());
			mList.get(i).put(DataBaseHelper.GGXH,
					hpList.get(0).get(DataBaseHelper.GGXH).toString());
			mList.get(i).put(DataBaseHelper.ID,
					hpList.get(0).get(DataBaseHelper.ID).toString());
		}
		if (WebserviceImport.isOpenNetwork(this)) {
			progressDialog = ProgressDialog.show(this, null, "正在检测");
			new Thread(downloadRun2).start();
		} else {
			Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void onEventMainThread(ObjectSix obj){
		init();
	}
	
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.refresh:
			init();
			break;
		}
	}
	Runnable downloadRun2 = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message msg = new Message();
			ErrorList.clear();
			for (int i = 0; i < mList.size(); i++) {
				float[] ff = WebserviceImport.GetHP_CurrKC((String) mList.get(i).get(DataBaseHelper.HPID), ckid,mSharedPreferences);
				if(ff[0]<0){
					if(op_type==2){
						mList.get(i).put(DataBaseHelper.BTKC, "获取失败");
					}else if(op_type==0){
						mList.get(i).put("new_zm", "获取失败");
					}
					ErrorList.add(mList.get(i));
				}else{
					if(op_type==2){
						mList.get(i).put(DataBaseHelper.BTKC, String.valueOf(ff[1]));
						String btkc=(String) mList.get(i).get(DataBaseHelper.BTKC);
						String msl=(String) mList.get(i).get(DataBaseHelper.MSL);
						dm_op.Update_CKKC((String) mList.get(i).get(DataBaseHelper.HPID), ckid, ff[1]);
						dm_ck.Check_moved_before_num((String) mList.get(i).get(DataBaseHelper.MID),
								(String) mList.get(i).get(DataBaseHelper.HPID),ff[1]);
						if(btkc==null||btkc.equals("")||Float.parseFloat(btkc)-Float.parseFloat(msl)<0){
							ErrorList.add(mList.get(i));
						}
					}else if(op_type==0){
						String old_zm=(String) mList.get(i).get(DataBaseHelper.BTKC);
						if(old_zm==null||old_zm.equals("")||Float.parseFloat(old_zm)!=ff[1]){
							mList.get(i).put("new_zm", ff[1]);
//							dm_op.Update_CKKC((String) mList.get(i).get(DataBaseHelper.HPID), ckid, ff[1]);
//							dm_ck.Check_moved_before_num((String) mList.get(i).get(DataBaseHelper.MID),
//									(String) mList.get(i).get(DataBaseHelper.HPID),ff[1]);
							ErrorList.add(mList.get(i));
						}
					}
				}
			}
			msg.setTarget(mHandler);
			mHandler.sendMessage(msg);
		}

	};
	public Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			progressDialog.dismiss();
			if(ErrorList.isEmpty()){
				errorTextView.setVisibility(View.VISIBLE);
				if (op_type == 2) {
					 setAdapter_chuku();
					errorTextView.setText("没有货品不足明细");
				} else if (op_type == 0) {
					setAdapter_check_zm();
					errorTextView.setText("没有错误账面数量明细");
				}
			}else{
				if(op_type==2){
					 setAdapter_chuku();
				 }else{
					 setAdapter_check_zm();
				 }
			}
			
		}
	};
	public void setAdapter_chuku(){
		listItemAdapter = new SimpleAdapter(this, ErrorList, R.layout.djdetailsitem_chuku,
				new String[]{DataBaseHelper.HPMC,DataBaseHelper.HPBM,DataBaseHelper.GGXH,
				DataBaseHelper.BTKC,DataBaseHelper.MSL},
				new int[]{R.id.itemname,R.id.itemcode,R.id.itemgg,R.id.itemnum,R.id.itemmsl});
		mListView.setAdapter(listItemAdapter);
	}
	public void setAdapter_check_zm(){
		listItemAdapter = new SimpleAdapter(this, ErrorList, R.layout.djdetailsitem_check_zm,
				new String[]{DataBaseHelper.HPMC,DataBaseHelper.HPBM,DataBaseHelper.GGXH,
				DataBaseHelper.BTKC,DataBaseHelper.ATKC,"new_zm"},
				new int[]{R.id.itemname,R.id.itemcode,R.id.itemgg,R.id.itemnum,R.id.itematkc,R.id.itemnum_zm});
		mListView.setAdapter(listItemAdapter);  
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO 自动生成的方法存根
		Map<String, Object> map=(Map<String, Object>) arg0.getAdapter().getItem(arg2);
		AddNumberDialog mAddNumberDialog = new AddNumberDialog(this, op_type, map, djid, dh, ckid, dacttype,"0",R.style.ButtonDialogStyle);
		mAddNumberDialog.setCanceledOnTouchOutside(false);
		Window win = mAddNumberDialog.getWindow();
		win.getDecorView().setPadding(0, 0, 0, 0);
		win.setGravity(Gravity.BOTTOM);
		WindowManager.LayoutParams lp = win.getAttributes();
		        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		        win.setAttributes(lp);
		mAddNumberDialog.show();
	}
}
