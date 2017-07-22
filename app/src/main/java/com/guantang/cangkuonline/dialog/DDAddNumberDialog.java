package com.guantang.cangkuonline.dialog;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

import de.greenrobot.event.EventBus;

public class DDAddNumberDialog extends Dialog implements
		android.view.View.OnClickListener, OnTouchListener {
	private Context context;
	private String ddh = "", ddid = "", hpid = "";
	private int dd_dirct = 0;
	private int watch = -1;// 自定义获取焦点，0 数量获取焦点，1 单价获取焦点， 2 总价获取焦点
	private Map<String, Object> map;
	private TextView hpmcTextView,description_kcTextView, benckTxt, rk_text;
	private ImageButton plus1Btn, dec1Btn;
	private Button cancel1Btn, ok1Btn;
	private EditText rkEdit, djEdit, zjEdit;
	private ProgressBar loadProgressBar;
	private LinearLayout mxLayout;

	private SharedPreferences mSharedPreferences;
	private ExecutorService cacheThreadPool = Executors.newCachedThreadPool();
	private List<Map<String, Object>> mlist = new ArrayList<Map<String, Object>>();
	private DataBaseOperateMethod dm_op;
	private DataBaseMethod dm;
	private String[] str = { DataBaseHelper.ID, DataBaseHelper.HPMC,
			DataBaseHelper.HPBM, DataBaseHelper.HPTM, DataBaseHelper.GGXH,
			DataBaseHelper.CurrKC, DataBaseHelper.JLDW, DataBaseHelper.HPSX,
			DataBaseHelper.HPXX, DataBaseHelper.SCCS, DataBaseHelper.BZ,
			DataBaseHelper.RKCKJ, DataBaseHelper.CKCKJ, DataBaseHelper.CKCKJ2,
			DataBaseHelper.JLDW2, DataBaseHelper.BigNum, DataBaseHelper.RES1,
			DataBaseHelper.RES2, DataBaseHelper.RES3, DataBaseHelper.RES4,
			DataBaseHelper.RES5, DataBaseHelper.RES6, DataBaseHelper.LBS,
			DataBaseHelper.LBID, DataBaseHelper.LBIndex, DataBaseHelper.KCJE,
			DataBaseHelper.ImagePath };
	private String[] str1 = { "id", "HPMC", "HPBM", "HPTBM", "GGXH", "CurrKc",
			"JLDW", "HPSX", "HPXX", "SCCS", "BZ", "RKCKJ", "CKCKJ", "CKCKJ2",
			"JLDW2", "BigNum", "res1", "res2", "res3", "res4", "res5", "res6",
			"LBS", "LBID", "LBIndex", "kcje", "ImageUrl" };
	// 订单明细表字段
	private String[] str2 = { DataBaseHelper.HPID, DataBaseHelper.orderID,
			DataBaseHelper.HPMC, DataBaseHelper.HPBM, DataBaseHelper.GGXH,
			DataBaseHelper.SL, DataBaseHelper.DJ, DataBaseHelper.ZJ,
			DataBaseHelper.ddirc };
	// 服务端订单明细表字段
	private String[] str3 = { "hpid", "orderId", "hpmc", "hpbm", "ggxh", "sl",
			"dj", "zj", "ddirc" };

	public DDAddNumberDialog(Context context, String ddh, String ddid,
			int dd_dirct, Map<String, Object> map,int theme) {
		super(context, theme);
		this.context = context;
		this.ddh = ddh;
		this.ddid = ddid;
		this.dd_dirct = dd_dirct;// 0 采购订单，1销售订单
		this.map = map;
		dm_op = new DataBaseOperateMethod(context);
		dm = new DataBaseMethod(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		mSharedPreferences = context.getSharedPreferences(
				ShareprefenceBean.SHAREPREFENCE_NAME, Context.MODE_PRIVATE);
		setContentView(R.layout.addnumber);
		initView();
		init();
	}

	private void initView() {
		hpmcTextView = (TextView) findViewById(R.id.hpname1);
		description_kcTextView = (TextView) findViewById(R.id.kcname);
		benckTxt = (TextView) findViewById(R.id.benckTxt);
		rk_text = (TextView) findViewById(R.id.rk_text);
		plus1Btn = (ImageButton) findViewById(R.id.plus1);
		dec1Btn = (ImageButton) findViewById(R.id.dec1);
		cancel1Btn = (Button) findViewById(R.id.cancel1);
		ok1Btn = (Button) findViewById(R.id.ok1);
		rkEdit = (EditText) findViewById(R.id.rk);
		djEdit = (EditText) findViewById(R.id.dj);
		zjEdit = (EditText) findViewById(R.id.zj);
		loadProgressBar = (ProgressBar) findViewById(R.id.loadBar);
		mxLayout = (LinearLayout) findViewById(R.id.mxlayout);
		
		plus1Btn.setOnClickListener(this);
		dec1Btn.setOnClickListener(this);
		cancel1Btn.setOnClickListener(this);
		ok1Btn.setOnClickListener(this);
		
		plus1Btn.setOnTouchListener(this);
		dec1Btn.setOnTouchListener(this);
		rkEdit.setOnTouchListener(this);
		djEdit.setOnTouchListener(this);
		zjEdit.setOnTouchListener(this);

		rkEdit.addTextChangedListener(djTextWatcher);
		djEdit.addTextChangedListener(djTextWatcher);
		zjEdit.addTextChangedListener(zjTextWatcher);
		
		hpmcTextView.setText(map.get(DataBaseHelper.HPMC).toString());
		description_kcTextView.setText("货品总库存");
		if (dd_dirct == 0) {
			rk_text.setText("采购数量");
		} else {
			rk_text.setText("销售数量");
		}
	}

	public void init() {
		if (mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1) == 1) {
			if (WebserviceImport.isOpenNetwork(context)) {
				hpid = (String) map.get(DataBaseHelper.ID);
				cacheThreadPool.execute(firstrunnable);
			} else {
				Toast.makeText(context, "网络未连接", Toast.LENGTH_SHORT).show();
			}
		}
	}

	private Runnable firstrunnable = new Runnable() {

		@Override
		public void run() {
			// TODO 自动生成的方法存根
			Message msg = Message.obtain();
			mlist = WebserviceImport.GetHpInfo_byid(hpid, -1, str, str1,
					mSharedPreferences);
			msg.what = 0;
			handler.sendMessage(msg);
		}
	};

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (!mlist.isEmpty()) {
					loadProgressBar.setVisibility(View.GONE);
					List<Map<String, Object>> list2 = dm_op.Gt_OrderDetails(
							ddid, mlist.get(0).get(DataBaseHelper.ID)
									.toString(), str2);
					if (!list2.isEmpty()) {
						Map<String, Object> map = list2.get(0);
						rkEdit.setText(map.get(DataBaseHelper.SL).toString());
						djEdit.setText(map.get(DataBaseHelper.DJ).toString());
						zjEdit.setText(map.get(DataBaseHelper.ZJ).toString());
					} else {
						if (TextUtils.isEmpty(mlist.get(0).get(DataBaseHelper.RKCKJ).toString())) {
							djEdit.setText("");
						} else {
							djEdit.setText(DecimalsHelper.Transfloat(Double
									.parseDouble((String) mlist.get(0).get(DataBaseHelper.RKCKJ)),MyApplication.getInstance().getDjPoint()));
						}
						rkEdit.setText("");
						zjEdit.setText("");
					}
					if(DecimalsHelper.stringIsNumBer(mlist.get(0).get(DataBaseHelper.CurrKC).toString())){
						benckTxt.setText(DecimalsHelper.Transfloat(Double.parseDouble(mlist.get(0).get(DataBaseHelper.CurrKC).toString()),MyApplication.getInstance().getNumPoint()));
					}else{
						benckTxt.setText("");
					}
					
					mxLayout.setVisibility(View.VISIBLE);
				} else {
					Toast.makeText(context, "服务器数据异常，请刷新", Toast.LENGTH_SHORT).show();
					dismiss();
				}
				break;
			}
		};
	};
	
	TextWatcher djTextWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO 自动生成的方法存根
			if (!rkEdit.getText().toString().equals("")) {
				//为了不让两个输入文本死循环，必须要触摸监听才能改变另一个输入框
				if(watch==1){
					if(!s.toString().isEmpty()){
						if (!DecimalsHelper.NumBerStringIsFormat(s.toString())) {
							Toast.makeText(context, "输入框最多保存10位整数和2位小数", Toast.LENGTH_SHORT)
									.show();
						}
						if(s.toString().equals("0")){
							zjEdit.setText("");
						}else{
							if(!rkEdit.getText().toString().equals("")){
								BigDecimal b1 = new BigDecimal(s.toString());
								BigDecimal b2 = new BigDecimal(rkEdit.getText().toString());
								zjEdit.setText(DecimalsHelper.Transfloat(b1.multiply(b2).floatValue(),MyApplication.getInstance().getDjPoint()));
							}
						}
					}else{
						zjEdit.setText("");
					}
				}else if(watch==0){
					if(!s.toString().isEmpty()){
						if (!DecimalsHelper.NumBerStringIsFormat(s.toString())) {
							Toast.makeText(context, "输入框最多保存10位整数和2位小数", Toast.LENGTH_SHORT)
									.show();
						}
						if(s.toString().equals("0")){
							zjEdit.setText("");
						}else{
							if(!djEdit.getText().toString().equals("")){
								BigDecimal b1 = new BigDecimal(s.toString());
								BigDecimal b2 = new BigDecimal(djEdit.getText().toString());
								zjEdit.setText(DecimalsHelper.Transfloat(b1.multiply(b2).floatValue(),MyApplication.getInstance().getDjPoint()));
							}
						}
					}else{
						zjEdit.setText("");
					}
				}
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO 自动生成的方法存根

		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO 自动生成的方法存根
			int pos = s.length() - 1;
			int position = s.toString().indexOf(".");

			if (pos - position > 2 && position != -1) {
				s.delete(pos, pos + 1);
			}
			if (position == -1 && s.toString().length() > 10) {
				char[] numberStrings = s.toString().toCharArray();
				if (!String.valueOf(numberStrings[10]).equals(".")) {// 如果第11位不是小数点就删除
					s.delete(10, s.toString().length());
				}
			}
		}
	};
	
	TextWatcher zjTextWatcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO 自动生成的方法存根
			if (!rkEdit.getText().toString().equals("") && !rkEdit.getText().toString().equals("0")) {
				//为了不让两个输入文本死循环，必须要触摸监听才能改变另一个输入框
				if(watch==2){
					if(!s.toString().isEmpty()){
						if (!DecimalsHelper.NumBerStringIsFormat(s.toString())) {
							Toast.makeText(context, "输入框最多保存10位整数和2位小数", Toast.LENGTH_SHORT)
									.show();
						}
						if(!rkEdit.getText().toString().equals("")||!rkEdit.getText().toString().equals("0")){
							BigDecimal b1 = new BigDecimal(s.toString());
							BigDecimal b2 = new BigDecimal(rkEdit.getText().toString());
							djEdit.setText(DecimalsHelper.Transfloat(b1.divide(b2, 4,RoundingMode.HALF_UP).floatValue(),MyApplication.getInstance().getDjPoint()));
						}
					}
				}
			}
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO 自动生成的方法存根

		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO 自动生成的方法存根
			int pos = s.length() - 1;
			int position = s.toString().indexOf(".");

			if (pos - position > 2 && position != -1) {
				s.delete(pos, pos + 1);
			}
			if (position == -1 && s.toString().length() > 10) {
				char[] numberStrings = s.toString().toCharArray();
				if (!String.valueOf(numberStrings[10]).equals(".")) {// 如果第11位不是小数点就删除
					s.delete(10, s.toString().length());
				}
			}
		}
	};
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO 自动生成的方法存根
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			switch (v.getId()) {
			case R.id.rk:
				watch = 0;
				break;
			case R.id.plus1:
				watch = 0;
				break;
			case R.id.dec1:
				watch = 0;
				break;
			case R.id.dj:
				watch = 1;
				break;
			case R.id.zj:
				watch = 2;
				break;
			}
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.plus1:
			if (rkEdit.getText().toString().equals("")) {
				rkEdit.setText("1");
			} else {
				float f = Float.parseFloat(rkEdit.getText().toString());
				if ((Math.round(f) - f) == 0) {
					rkEdit.setText(String.valueOf((int) f + 1));
				} else {
					rkEdit.setText(String.valueOf(f + 1));
				}
			}
			break;
		case R.id.dec1:
			if (rkEdit.getText().toString().equals("")) {
				rkEdit.setText("0");
			} else {
				float f = Float.parseFloat(rkEdit.getText().toString());
				if (f > 0) {
					if ((Math.round(f) - f) == 0) {
						rkEdit.setText(String.valueOf((int) f - 1));
					} else {
						rkEdit.setText(String.valueOf(f - 1));
					}
				}
			}
			break;
		case R.id.cancel1:
			dismiss();
			break;
		case R.id.ok1:
			String sssss="";
			if (TextUtils.isEmpty(rkEdit.getText().toString())) {
				Toast.makeText(context, "请输入数量", Toast.LENGTH_SHORT).show();
			} else if (rkEdit.getText().toString().equals("0")) {
				dm_op.Del_OrderDetails(ddid, hpid);
				EventBus.getDefault().post(new ObjectFive(hpid,rkEdit.getText().toString())); 
				EventBus.getDefault().post(new ObjectSix("计算拣货筐数量"));
				Toast.makeText(context, "已删除货品", Toast.LENGTH_SHORT).show();
				dismiss();
			} else {
				if (DecimalsHelper.NumBerStringIsFormat(rkEdit
						.getText().toString())
						&& DecimalsHelper.NumBerStringIsFormat(djEdit
								.getText().toString())
						&& DecimalsHelper.NumBerStringIsFormat(zjEdit
								.getText().toString())) {// 保证输入的数字不超过decimal(18,8)
					dm_op.Del_OrderDetails(ddid, hpid);
					Map<String, Object> map = mlist.get(0);
					dm_op.insert_into(
							DataBaseHelper.TB_OrderDetail,
							str2,
							new String[] { hpid, ddid,
									map.get(DataBaseHelper.HPMC).toString(),
									map.get(DataBaseHelper.HPBM).toString(),
									map.get(DataBaseHelper.GGXH).toString(),
									rkEdit.getText().toString(),
									djEdit.getText().toString(),
									zjEdit.getText().toString(),
									String.valueOf(dd_dirct) });
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
					EventBus.getDefault().post(new ObjectFive(hpid,rkEdit.getText().toString()));
					EventBus.getDefault().post(new ObjectSix("计算拣货筐数量或者通知检测"));
					dismiss();
				} else {
					Toast.makeText(context, "输入框最多保存10位整数和2位小数",
							Toast.LENGTH_SHORT).show();
				}
			}
			break;
		}
	}
}
