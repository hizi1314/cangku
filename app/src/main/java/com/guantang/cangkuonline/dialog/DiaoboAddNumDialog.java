package com.guantang.cangkuonline.dialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.application.MyApplication;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.database.DataBaseMethod;
import com.guantang.cangkuonline.database.DataBaseOperateMethod;
import com.guantang.cangkuonline.eventbusBean.ObjectFive;
import com.guantang.cangkuonline.eventbusBean.ObjectSix;
import com.guantang.cangkuonline.helper.DecimalsHelper;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;
import com.guantang.cangkuonline.webservice.WebserviceImport;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import de.greenrobot.event.EventBus;

public class DiaoboAddNumDialog extends Dialog implements android.view.View.OnClickListener {

	private Context context;
	private String kcsl = "", djid = "";
	private Map<String, Object> map;
	private DataBaseOperateMethod dm_op;
	private DataBaseMethod dm;
	private String hpid = "";
	private int sckid = -1;

	private TextView hpnameTxtView, kucunTxtView;
	private LinearLayout lxlayout;
	private ImageButton decImgBtn, plusImgBtn;
	private EditText numEdit;
	private Button cancelBtn, comfirmBtn;
	private ProgressBar loadProgressBar;

	// 调拨明细字段
	private String str2[] = { "hpid", "sl", "dj", "zj", "mid", "note" };

	public DiaoboAddNumDialog(Context context,int sckid, String kcsl, String djid, Map<String, Object> map, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.kcsl = kcsl;
		this.djid = djid;
		this.hpid = map.get(DataBaseHelper.ID).toString();
		this.map = map;
		dm_op = new DataBaseOperateMethod(context);
		dm = new DataBaseMethod(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.diaobo_dialog_layout);
		initView();
		init();
	}

	public void initView() {
		hpnameTxtView = (TextView) findViewById(R.id.hpnameTxtView);
		kucunTxtView = (TextView) findViewById(R.id.kucunTxtView);
		lxlayout = (LinearLayout) findViewById(R.id.lxlayout);
		decImgBtn = (ImageButton) findViewById(R.id.decImgBtn);
		plusImgBtn = (ImageButton) findViewById(R.id.plusImgBtn);
		numEdit = (EditText) findViewById(R.id.numEdit);
		cancelBtn = (Button) findViewById(R.id.cancelBtn);
		comfirmBtn = (Button) findViewById(R.id.comfirmBtn);
		loadProgressBar = (ProgressBar) findViewById(R.id.loadProgressBar);

		decImgBtn.setOnClickListener(this);
		plusImgBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
		comfirmBtn.setOnClickListener(this);

	}

	public void init() {
		kucunTxtView.setText(kcsl);
		List<Map<String, Object>> list2 = dm_op.Gt_DiaoboDetails(djid,
				hpid, str2);
		if (!list2.isEmpty()) {
			Map<String, Object> map = list2.get(0);
			numEdit.setText(map.get(DataBaseHelper.SL).toString());
		} else {
			numEdit.setText("");
		}
//		if (MyApplication.getInstance().getSharedPreferences().getInt(ShareprefenceBean.IS_LOGIN, -1) == 1) {
//			if (WebserviceImport.isOpenNetwork(context)) {
//				hpid = (String) map.get(DataBaseHelper.ID);
//				new Thread(firstrunnable).start();
//			} else {
//				Toast.makeText(context, "网络未连接", Toast.LENGTH_SHORT).show();
//			}
//		}
	}

//	private Runnable firstrunnable = new Runnable() {
//
//		@Override
//		public void run() {
//			// TODO 自动生成的方法存根
//			Message msg = Message.obtain();
//			mlist = WebserviceImport.GetHpInfo_byid(hpid, sckid, str, str1,
//					MyApplication.getInstance().getSharedPreferences());
//			msg.what = 0;
//			handler.sendMessage(msg);
//		}
//	};
//
//	private Handler handler = new Handler() {
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//			case 0:
//				if (!mlist.isEmpty()) {
//					loadProgressBar.setVisibility(View.GONE);
//					List<Map<String, Object>> list2 = dm_op.Gt_DiaoboDetails(djid,
//							mlist.get(0).get(DataBaseHelper.ID).toString(), str2);
//					if (!list2.isEmpty()) {
//						Map<String, Object> map = list2.get(0);
//						numEdit.setText(map.get(DataBaseHelper.SL).toString());
//					} else {
//						numEdit.setText("");
//					}
//					kucunTxtView.setText(mlist.get(0).get(DataBaseHelper.KCSL).toString());
//					lxlayout.setVisibility(View.VISIBLE);
//				} else {
//					Toast.makeText(context, "服务器数据异常，请刷新", Toast.LENGTH_SHORT).show();
//					dismiss();
//				}
//				break;
//			}
//		};
//	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.decImgBtn:
			if (numEdit.getText().toString().equals("")) {
				numEdit.setText("0");
			} else {
				double f = Double.parseDouble(numEdit.getText().toString());
				if (f >= 1) {
					numEdit.setText(DecimalsHelper.Transfloat(f-1,MyApplication.getInstance().getNumPoint()));
				}else{
					Toast.makeText(context, "数量不能为负数", Toast.LENGTH_LONG).show();
				}
			}
			break;
		case R.id.plusImgBtn:
			if (numEdit.getText().toString().equals("")) {
				numEdit.setText("1");
			} else {
				double f = Double.parseDouble(numEdit.getText().toString());
				numEdit.setText(String.valueOf(DecimalsHelper.Transfloat(f+1,MyApplication.getInstance().getNumPoint())));
			}
			break;
		case R.id.cancelBtn:
			dismiss();
			break;
		case R.id.comfirmBtn:
			if (TextUtils.isEmpty(numEdit.getText().toString())) {
				Toast.makeText(context, "请输入数量", Toast.LENGTH_SHORT).show();
			} else if (numEdit.getText().toString().equals("0")) {
				dm_op.Del_transd(djid, hpid);
				EventBus.getDefault().post(new ObjectFive(hpid, numEdit.getText().toString()));
				EventBus.getDefault().post(new ObjectSix("计算拣货筐数量"));
				Toast.makeText(context, "已删除货品", Toast.LENGTH_SHORT).show();
				dismiss();
			} else {
				dm_op.Del_transd(djid, hpid);
				dm_op.insert_into(DataBaseHelper.TB_TRANSD,str2,new String[] { hpid,numEdit.getText().toString(),"","",djid,"",});
				// 本地数据库是否存在有这个id的货品,不存在就添加货品
				if (!dm.searchID(hpid)) {
					dm.Addhp(Integer.parseInt(map.get(DataBaseHelper.ID)
							.toString()), map.get(DataBaseHelper.HPMC)
							.toString(), map.get(DataBaseHelper.HPBM)
							.toString(), map.get(DataBaseHelper.HPTM)
							.toString(), map.get(DataBaseHelper.GGXH)
							.toString(), map.get(DataBaseHelper.JLDW)
							.toString(), map.get(DataBaseHelper.JLDW2)
							.toString(), map.get(DataBaseHelper.BigNum)
							.toString(), map.get(DataBaseHelper.SCCS)
							.toString(), map.get(DataBaseHelper.HPSX)
							.toString(), map.get(DataBaseHelper.HPXX)
							.toString(), map.get(DataBaseHelper.RKCKJ)
							.toString(), map.get(DataBaseHelper.CKCKJ)
							.toString(), map.get(DataBaseHelper.CKCKJ2)
							.toString(),
							map.get(DataBaseHelper.LBS).toString(),
							map.get(DataBaseHelper.LBID).toString(),
							map.get(DataBaseHelper.LBIndex).toString(), map
									.get(DataBaseHelper.BZ).toString(), null,
							0, map.get(DataBaseHelper.RES1).toString(), map
									.get(DataBaseHelper.RES2).toString(), map
									.get(DataBaseHelper.RES3).toString(), map
									.get(DataBaseHelper.RES4).toString(), map
									.get(DataBaseHelper.RES5).toString(), map
									.get(DataBaseHelper.RES6).toString(), "",
							"", 0, null, map.get(DataBaseHelper.ImagePath)
									.toString());
				}
				EventBus.getDefault().post(new ObjectFive(hpid,numEdit.getText().toString()));
				EventBus.getDefault().post(new ObjectSix("计算拣货筐数量或者通知检测"));
				dismiss();
			}
			break;
		}
	}
}
