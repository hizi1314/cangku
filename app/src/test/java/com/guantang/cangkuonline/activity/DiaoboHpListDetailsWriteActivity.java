package com.guantang.cangkuonline.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import de.greenrobot.event.EventBus;

public class DiaoboHpListDetailsWriteActivity extends Activity implements OnClickListener{
	
	private ImageButton backImgBtn;
	private TextView titleTextView, tmTextView, bmTextView,
			mcTextView, lbTextView, ggTextView, jldwTextView,
			hsslTextView, tdwTextView, ckjeTextView, picTextView, sxTextView,
			xxTextView, sccsTextView, bzTextView, rkckjTextView, ckckjTextView,
			ckckj2TextView, kcjeTextView, hsdwTextView, bignumTextView,
			res1TextView, res2TextView, res3TextView, res4TextView,
			res5TextView, res6TextView, res1_text, res2_text, res3_text,
			res4_text, res5_text, res6_text, rk_text,kcname;
	private Button commitBtn;
	private ImageButton refresh_numBtn ,plusButton, decButton;
	private LinearLayout mxlayout;
	private LinearLayout layout1, layout2, layout3, DJlayout,
			ZJlayout, KCJElayout,rkckj_layout,ckckj_layout;
	private TextView zmslTextView;
	private Button okBtn;
	private LinearLayout lxlayout;
	private EditText numEditText;
	private ProgressDialog pro_Dialog;
	private ExecutorService cacheThreadPool = Executors.newCachedThreadPool();
	private String[] str = { DataBaseHelper.ID, DataBaseHelper.HPMC,
			DataBaseHelper.HPBM, DataBaseHelper.HPTM, DataBaseHelper.GGXH,
			DataBaseHelper.CurrKC, DataBaseHelper.JLDW, DataBaseHelper.HPSX,
			DataBaseHelper.HPXX, DataBaseHelper.SCCS, DataBaseHelper.BZ,
			DataBaseHelper.RKCKJ, DataBaseHelper.CKCKJ, DataBaseHelper.CKCKJ2,
			DataBaseHelper.JLDW2, DataBaseHelper.BigNum, DataBaseHelper.RES1,
			DataBaseHelper.RES2, DataBaseHelper.RES3, DataBaseHelper.RES4,
			DataBaseHelper.RES5, DataBaseHelper.RES6, DataBaseHelper.LBS,
			DataBaseHelper.LBID, DataBaseHelper.LBIndex, DataBaseHelper.KCJE,
			DataBaseHelper.ImagePath, DataBaseHelper.KCSL };

	private String[] str1 = { "id", "HPMC", "HPBM", "HPTBM", "GGXH", "CurrKc",
			"JLDW", "HPSX", "HPXX", "SCCS", "BZ", "RKCKJ", "CKCKJ", "CKCKJ2",
			"JLDW2", "BigNum", "res1", "res2", "res3", "res4", "res5", "res6",
			"LBS", "LBID", "LBIndex", "kcje", "ImageUrl", "kcsl" };
	// 调拨明细字段
	private String str2[] = { "hpid", "sl", "dj", "zj", "mid", "note" };
	private List<Map<String,Object>> mlist = new ArrayList<Map<String,Object>>();
	private DataBaseOperateMethod dm_op = new DataBaseOperateMethod(this);
	private DataBaseMethod dm = new DataBaseMethod(this);
	private String hpid = "",djid = "";
	private int sckid = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_diaobo_hplistdetaildetail_layout);
		Intent intent = getIntent();
		hpid = intent.getStringExtra("hpid");
		djid = intent.getStringExtra("djid");
		sckid = intent.getIntExtra("sckid", -1);
		initView();
		init();
	}
	
	public void initView(){
		backImgBtn = (ImageButton) findViewById(R.id.back);
		titleTextView = (TextView) findViewById(R.id.title);
		tmTextView = (TextView) findViewById(R.id.tm);
		bmTextView = (TextView) findViewById(R.id.bm);
		mcTextView = (TextView) findViewById(R.id.mc);
		lbTextView = (TextView) findViewById(R.id.lb);
		ggTextView = (TextView) findViewById(R.id.gg);
		lbTextView = (TextView) findViewById(R.id.lb);
		jldwTextView = (TextView) findViewById(R.id.dw);
		hsslTextView = (TextView) findViewById(R.id.num2);
		tdwTextView = (TextView) findViewById(R.id.tdw);
		kcjeTextView = (TextView) findViewById(R.id.kcje);
		sxTextView = (TextView) findViewById(R.id.sx);
		xxTextView = (TextView) findViewById(R.id.xx);
		sccsTextView = (TextView) findViewById(R.id.sccs);
		bzTextView = (TextView) findViewById(R.id.bz);
		rkckjTextView = (TextView) findViewById(R.id.rkckj);
		ckckjTextView = (TextView) findViewById(R.id.ckckj);
		ckckj2TextView = (TextView) findViewById(R.id.ckckj2);
		hsdwTextView = (TextView) findViewById(R.id.dw2);
		bignumTextView = (TextView) findViewById(R.id.bignum);
		res1TextView = (TextView) findViewById(R.id.res1);
		res2TextView = (TextView) findViewById(R.id.res2);
		res3TextView = (TextView) findViewById(R.id.res3);
		res4TextView = (TextView) findViewById(R.id.res4);
		res5TextView = (TextView) findViewById(R.id.res5);
		res6TextView = (TextView) findViewById(R.id.res6);
		res1_text = (TextView) findViewById(R.id.res1_text);
		res2_text = (TextView) findViewById(R.id.res2_text);
		res3_text = (TextView) findViewById(R.id.res3_text);
		res4_text = (TextView) findViewById(R.id.res4_text);
		res5_text = (TextView) findViewById(R.id.res5_text);
		res6_text = (TextView) findViewById(R.id.res6_text);
		kcname = (TextView) findViewById(R.id.kcname);
		rk_text = (TextView) findViewById(R.id.rk_text);
		plusButton = (ImageButton) findViewById(R.id.plus);
		decButton = (ImageButton) findViewById(R.id.dec);
		layout1 = (LinearLayout) findViewById(R.id.layout_1);
		layout2 = (LinearLayout) findViewById(R.id.layout_2);
		layout3 = (LinearLayout) findViewById(R.id.layout_3);
		DJlayout = (LinearLayout) findViewById(R.id.dj_layout);
		ZJlayout = (LinearLayout) findViewById(R.id.zj_layout);
		KCJElayout = (LinearLayout) findViewById(R.id.kcje_layout);

		zmslTextView = (TextView) findViewById(R.id.zs);
		okBtn = (Button) findViewById(R.id.ok);
		numEditText = (EditText) findViewById(R.id.num);
		mxlayout = (LinearLayout) findViewById(R.id.mxlayout);
		lxlayout = (LinearLayout) findViewById(R.id.lxlayout);
		rkckj_layout = (LinearLayout) findViewById(R.id.rkckj_layout);
		ckckj_layout = (LinearLayout) findViewById(R.id.ckckj_layout);
		
		okBtn.setOnClickListener(this);
		plusButton.setOnClickListener(this);
		decButton.setOnClickListener(this);
		backImgBtn.setOnClickListener(this);
		
	}
	
	public void init(){
		if (MyApplication.getInstance().getSharedPreferences().getInt(ShareprefenceBean.IS_LOGIN, -1) == 1) {
			if (WebserviceImport.isOpenNetwork(this)) {
				pro_Dialog = ProgressDialog.show(this, null, "正在加载……");
				pro_Dialog.setCancelable(true);
				cacheThreadPool.execute(firstrunnable);
			} else {
				Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	public void setView(List<Map<String, Object>> list) {

		Map<String, Object> map = list.get(0);

		String Hpsx = "", Hpxx = "", kcje = "";
		if (map.get(DataBaseHelper.HPSX) == null
				|| map.get(DataBaseHelper.HPSX).equals("")) {
			Hpsx = "";
		} else if (map.get(DataBaseHelper.HPSX).equals("0")) {
			Hpsx = "";
		} else {
			Hpsx = DecimalsHelper.Transfloat(Double.parseDouble((String) map
					.get(DataBaseHelper.HPSX)),MyApplication.getInstance().getNumPoint());
		}

		if (map.get(DataBaseHelper.HPXX) == null
				|| map.get(DataBaseHelper.HPXX).equals("")) {
			Hpxx = "";
		} else if (map.get(DataBaseHelper.HPXX).equals("0")) {
			Hpxx = "";
		} else {
			Hpxx = DecimalsHelper.Transfloat(Double.parseDouble((String) map
					.get(DataBaseHelper.HPXX)),MyApplication.getInstance().getNumPoint());
		}

		if (map.get(DataBaseHelper.KCJE) == null
				|| map.get(DataBaseHelper.KCJE).equals("")) {
			kcjeTextView.setText("");
		} else if (map.get(DataBaseHelper.KCJE).equals("0")) {
			kcjeTextView.setText("");
		} else {
			kcjeTextView.setText(DecimalsHelper.Transfloat(Double.parseDouble((String) map.get(DataBaseHelper.KCJE)),MyApplication.getInstance().getJePoint()));
		}
		if (map.get(DataBaseHelper.RKCKJ) == null
				|| map.get(DataBaseHelper.RKCKJ).equals("")) {
			rkckjTextView.setText("0");
		} else {
			rkckjTextView.setText(DecimalsHelper.Transfloat(Double.parseDouble((String) map
					.get(DataBaseHelper.RKCKJ)),MyApplication.getInstance().getDjPoint()));
		}

		if (map.get(DataBaseHelper.KCSL) == null
				|| map.get(DataBaseHelper.KCSL).equals("")) {
			zmslTextView.setText("0");
//			numEditText.setText("0");
		} else {
			String mystr = DecimalsHelper.Transfloat(Double.parseDouble((String) map.get(DataBaseHelper.KCSL)),MyApplication.getInstance().getNumPoint());
			zmslTextView.setText(mystr);
//			numEditText.setText(mystr);
		}
		tmTextView.setText(map.get(DataBaseHelper.HPTM).toString());
		bmTextView.setText(map.get(DataBaseHelper.HPBM).toString());
		mcTextView.setText(map.get(DataBaseHelper.HPMC).toString());
		lbTextView.setText(map.get(DataBaseHelper.LBS).toString());
		ggTextView.setText(map.get(DataBaseHelper.GGXH).toString());
		jldwTextView.setText(map.get(DataBaseHelper.JLDW).toString());
		sxTextView.setText(Hpsx);
		xxTextView.setText(Hpxx);
		bignumTextView.setText(map.get(DataBaseHelper.BigNum).toString());
		sccsTextView.setText(map.get(DataBaseHelper.SCCS).toString());
		bzTextView.setText(map.get(DataBaseHelper.BZ).toString());
		

		if (map.get(DataBaseHelper.CKCKJ) == null
				|| map.get(DataBaseHelper.CKCKJ).equals("")) {
			ckckjTextView.setText("0");
		} else {
			ckckjTextView.setText(DecimalsHelper.Transfloat(Double.parseDouble((String) map.get(DataBaseHelper.CKCKJ)),MyApplication.getInstance().getDjPoint()));
		}
		
		
		if (map.get(DataBaseHelper.CKCKJ2) == null
				|| map.get(DataBaseHelper.CKCKJ2).equals("")) {
			ckckj2TextView.setText("");
		} else {
			ckckj2TextView.setText(DecimalsHelper.Transfloat(Double.parseDouble((String) map.get(DataBaseHelper.CKCKJ2)),MyApplication.getInstance().getDjPoint()));
		}

		hsdwTextView.setText(map.get(DataBaseHelper.JLDW2).toString());
		if (map.get(DataBaseHelper.BigNum) == null
				|| map.get(DataBaseHelper.BigNum).equals("")) {
			bignumTextView.setText("");
		} else {
			bignumTextView.setText(DecimalsHelper.Transfloat(Double.parseDouble((String) map.get(DataBaseHelper.BigNum)),MyApplication.getInstance().getNumPoint()));
		}
		res1TextView.setText((String) map.get(DataBaseHelper.RES1));
		res2TextView.setText((String) map.get(DataBaseHelper.RES2));
		res3TextView.setText((String) map.get(DataBaseHelper.RES3));
		res4TextView.setText((String) map.get(DataBaseHelper.RES4));
		res5TextView.setText((String) map.get(DataBaseHelper.RES5));
		res6TextView.setText((String) map.get(DataBaseHelper.RES6));
		
		List<Map<String, Object>> list2 = dm_op.Gt_DiaoboDetails(djid,
				mlist.get(0).get(DataBaseHelper.ID).toString(), str2);
		if (!list2.isEmpty()) {
			Map<String, Object> mapp = list2.get(0);
			numEditText.setText(mapp.get(DataBaseHelper.SL).toString());
		}
	}

	public void setEmptyView() {
		kcjeTextView.setText("");
		tmTextView.setText("");
		bmTextView.setText("");
		mcTextView.setText("");
		lbTextView.setText("");
		ggTextView.setText("");
		jldwTextView.setText("");
		sxTextView.setText("");
		xxTextView.setText("");
		bignumTextView.setText("");
		sccsTextView.setText("");
		bzTextView.setText("");
		rkckjTextView.setText("");
		ckckjTextView.setText("");
		ckckj2TextView.setText("");
		hsdwTextView.setText("");
		bignumTextView.setText("");
		res1TextView.setText("");
		res2TextView.setText("");
		res3TextView.setText("");
		res4TextView.setText("");
		res5TextView.setText("");
		res6TextView.setText("");
	}

	
	private Runnable firstrunnable = new Runnable() {

		@Override
		public void run() {
			// TODO 自动生成的方法存根
			Message msg = Message.obtain();
			mlist = WebserviceImport.GetHpInfo_byid(hpid, sckid, str, str1,
					MyApplication.getInstance().getSharedPreferences());
			msg.what = 0;
			handler.sendMessage(msg);
		}
	};
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			
			switch (msg.what) {
			case 0:
				pro_Dialog.dismiss();
				if (!mlist.isEmpty()) {
					setView(mlist);
				} else {
					setEmptyView();
					Toast.makeText(DiaoboHpListDetailsWriteActivity.this,
							"服务器数据异常，请刷新", Toast.LENGTH_SHORT).show();
				}
				break;
			
			}
		};
	};
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.back:
			finish();
			break;
		case R.id.ok:
			if(!mlist.isEmpty()){
				if(numEditText.getText().toString().equals("")){
					Toast.makeText(this, "请输入实有数", Toast.LENGTH_SHORT).show();
				}else if (numEditText.getText().toString().equals("0")) {
					dm_op.Del_OrderDetails(djid, hpid);
					EventBus.getDefault().post(new ObjectFive(hpid, numEditText.getText().toString()));
					EventBus.getDefault().post(new ObjectSix("计算拣货筐数量"));
					Toast.makeText(this, "已删除货品", Toast.LENGTH_SHORT).show();
					finish();
				} else {
					dm_op.Del_transd(djid, hpid);
					dm_op.insert_into(DataBaseHelper.TB_TRANSD,str2,
							new String[] { hpid,numEditText.getText().toString(),"","",djid,"",});
					// 本地数据库是否存在有这个id的货品,不存在就添加货品
					if (!dm.searchID(hpid)) {
						Map<String,Object> map = mlist.get(0);
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
					EventBus.getDefault().post(new ObjectFive(hpid,numEditText.getText().toString()));
					EventBus.getDefault().post(new ObjectSix("计算拣货筐数量或者通知检测"));
					finish();
				}
			}else{
				Toast.makeText(this, "没有货品信息，不能保存", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.dec:
			if (numEditText.getText().toString().equals("")) {
				numEditText.setText("0");
			} else {
				float f = Float.parseFloat(numEditText.getText().toString());
				if (f >=1) {
					numEditText.setText(DecimalsHelper.Transfloat(f-1,MyApplication.getInstance().getNumPoint()));
				}else{
					Toast.makeText(this, "数量不能为负数", Toast.LENGTH_LONG).show();
				}
			}
			break;
		case R.id.plus:
			if (numEditText.getText().toString().equals("")) {
				numEditText.setText("1");
			} else {
				double f = Double.parseDouble(numEditText.getText().toString());
				numEditText.setText(String.valueOf(DecimalsHelper.Transfloat(f+1,MyApplication.getInstance().getNumPoint())));
			}
			break;
		}
	}
	
}
