package com.guantang.cangkuonline.dialog;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.R.integer;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.activity.HpListDetailsWriteActivity;
import com.guantang.cangkuonline.application.MyApplication;
import com.guantang.cangkuonline.database.DataBaseCheckMethod;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.database.DataBaseMethod;
import com.guantang.cangkuonline.database.DataBaseOperateMethod;
import com.guantang.cangkuonline.eventbusBean.ObjectFive;
import com.guantang.cangkuonline.eventbusBean.ObjectSix;
import com.guantang.cangkuonline.helper.DecimalsHelper;
import com.guantang.cangkuonline.helper.NumberWatcher;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;
import com.guantang.cangkuonline.webservice.WebserviceImport;

import de.greenrobot.event.EventBus;

public class AddNumberDialog extends Dialog implements OnTouchListener,
		android.view.View.OnClickListener {
	private Map<String, Object> map = new HashMap<String, Object>();
	private Context context;
	private int op_type = 1;
	private FrameLayout myFrameLayout;
	private LinearLayout mxLayout, lxLayout;
	private TextView rukuTextView, zsTextView, benckTextView,hpname1TextView,hpname2TextView,kcnameTextView;
	private Button cancel1Btn, ok1Btn,
			cancel2Btn, ok2Btn;
	private ImageButton plus1Btn, dec1Btn, plus2Btn, dec2Btn;
	private ImageButton refreshImgBtn;
	private EditText numberEditText, djEditText, zjEditText, pdNumberEditText;
	private ProgressBar loadProgressBar;
	private String djid = "", dh = "", hpid = "",currkc="";//currkc 在入库单的时候传递的总库存，其他单据不需要使用
	private int ckid = -1;
	private int watch = -1;
	private String dacttype = "";
	private String numString = "0";
	private DataBaseOperateMethod dm_op;
	private DataBaseMethod dm;
	private DataBaseCheckMethod dm_ck;
	private SimpleDateFormat formatter;
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
			"LBS", "LBID", "LBIndex", "kcje", "ImageUrl", "kcsl"};

	private String[] str3 = { DataBaseHelper.HPID, DataBaseHelper.MID,
			DataBaseHelper.MVDDATE, DataBaseHelper.MSL, DataBaseHelper.MVDType,
			DataBaseHelper.DH, DataBaseHelper.BTKC, DataBaseHelper.ATKC,
			DataBaseHelper.MVDirect, DataBaseHelper.DJ, DataBaseHelper.ZJ,
			DataBaseHelper.MVType, DataBaseHelper.CKID };
	private ExecutorService cacheThreadPool = Executors.newCachedThreadPool();
	private List<Map<String, Object>> ls = new ArrayList<Map<String, Object>>();
	private SharedPreferences mSharedPreferences;

	private CallbackInterface mCallbackInterface;

	public interface CallbackInterface {
		public void CallbackExecute(ObjectFive obj);
	}

	public void setCallbackInterface(CallbackInterface mCallbackInterface) {
		this.mCallbackInterface = mCallbackInterface;
	}

	public AddNumberDialog(Context context, int op_type,
			Map<String, Object> map, String djid, String dh, int ckid,
			String dacttype,String numString,int theme) {
		super(context,theme);
		// TODO 自动生成的构造函数存根
		this.context = context;
		this.map = map;
		this.djid = djid;
		this.dh = dh;
		this.ckid = ckid;
		this.dacttype = dacttype;
		this.op_type = op_type;
		this.numString = numString;
		mSharedPreferences = context.getSharedPreferences(
				ShareprefenceBean.SHAREPREFENCE_NAME, Context.MODE_PRIVATE);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.addnumber);
		
		myFrameLayout = (FrameLayout) findViewById(R.id.myFrameLayout);
		mxLayout = (LinearLayout) findViewById(R.id.mxlayout);
		lxLayout = (LinearLayout) findViewById(R.id.lxlayout);
		hpname1TextView = (TextView) findViewById(R.id.hpname1);
		hpname2TextView = (TextView) findViewById(R.id.hpname2);
		rukuTextView = (TextView) findViewById(R.id.rk_text);
		zsTextView = (TextView) findViewById(R.id.zs);
		benckTextView = (TextView) findViewById(R.id.benckTxt);
		plus1Btn = (ImageButton) findViewById(R.id.plus1);
		dec1Btn = (ImageButton) findViewById(R.id.dec1);
		plus2Btn = (ImageButton) findViewById(R.id.plus2);
		dec2Btn = (ImageButton) findViewById(R.id.dec2);
		cancel1Btn = (Button) findViewById(R.id.cancel1);
		ok1Btn = (Button) findViewById(R.id.ok1);
		cancel2Btn = (Button) findViewById(R.id.cancel2);
		ok2Btn = (Button) findViewById(R.id.ok2);
		refreshImgBtn = (ImageButton) findViewById(R.id.refresh_num);
		numberEditText = (EditText) findViewById(R.id.rk);
		djEditText = (EditText) findViewById(R.id.dj);
		zjEditText = (EditText) findViewById(R.id.zj);
		kcnameTextView = (TextView) findViewById(R.id.kcname);
		pdNumberEditText = (EditText) findViewById(R.id.num);
		loadProgressBar = (ProgressBar) findViewById(R.id.loadBar);
		
		hpname1TextView.setText(map.get(DataBaseHelper.HPMC).toString());
		hpname2TextView.setText(map.get(DataBaseHelper.HPMC).toString());

		plus1Btn.setOnClickListener(this);
		dec1Btn.setOnClickListener(this);
		plus2Btn.setOnClickListener(this);
		dec2Btn.setOnClickListener(this);
		cancel1Btn.setOnClickListener(this);
		ok1Btn.setOnClickListener(this);
		cancel2Btn.setOnClickListener(this);
		ok2Btn.setOnClickListener(this);
		refreshImgBtn.setOnClickListener(this);

		dec1Btn.setOnTouchListener(this);
		plus1Btn.setOnTouchListener(this);
		djEditText.setOnTouchListener(this);
		zjEditText.setOnTouchListener(this);
		numberEditText.setOnTouchListener(this);

		numberEditText.addTextChangedListener(numTextWatcher);
		djEditText.addTextChangedListener(djTextWatcher);
		zjEditText.addTextChangedListener(zjTextWatcher);
		pdNumberEditText.addTextChangedListener(myNumberWatcher);

		dm = new DataBaseMethod(context);
		dm_op = new DataBaseOperateMethod(context);
		dm_ck = new DataBaseCheckMethod(context);
		formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		if (op_type == 1) {
			rukuTextView.setText("入库数量");
//			kcnameTextView.setText("货品总库存");
		} else if (op_type == 2) {
			rukuTextView.setText("出库数量");
		}
		
		if (mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1) == 1) {
			if (WebserviceImport.isOpenNetwork(context)) {
				hpid = (String) map.get(DataBaseHelper.ID);
				cacheThreadPool.execute(firstrunnable);
			} else {
				Toast.makeText(context, "网络未连接", Toast.LENGTH_SHORT).show();
			}
		} else if (mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1) == -1) {

			hpid = (String) map.get(DataBaseHelper.ID);
			if (op_type == 1) {
				ls = dm.GethpAndKCSL(hpid, ckid,true);
			} else {
				ls = dm.GethpAndKCSL(hpid, ckid,false);
			}

			if (!ls.isEmpty()) {
				loadProgressBar.setVisibility(View.GONE);
				if (op_type == 1) {
					mxLayout.setVisibility(View.VISIBLE);
					lxLayout.setVisibility(View.GONE);
				} else if (op_type == 2) {
					mxLayout.setVisibility(View.VISIBLE);
					lxLayout.setVisibility(View.GONE);
				} else if (op_type == 0) {
					lxLayout.setVisibility(View.VISIBLE);
					mxLayout.setVisibility(View.GONE);
				}

				List<Map<String, Object>> list1 = dm_op.Gt_Moved(djid, hpid,
						str3);
				if (op_type == 1 || op_type == 2) {
					if (!list1.isEmpty()) {
						Map<String, Object> mapp = list1.get(0);
						numberEditText.setText(mapp.get(DataBaseHelper.MSL)
								.toString());
						djEditText.setText(mapp.get(DataBaseHelper.DJ)
								.toString());
						zjEditText.setText(mapp.get(DataBaseHelper.ZJ)
								.toString());
						benckTextView.setText(mapp.get(DataBaseHelper.BTKC)
								.toString());
					} else {
						if (ls.get(0).get(DataBaseHelper.KCSL) == null
								|| ls.get(0).get(DataBaseHelper.KCSL)
										.toString().equals("")) {
							benckTextView.setText("0");
						} else {
							benckTextView.setText(ls.get(0)
									.get(DataBaseHelper.KCSL).toString());
						}
						if(op_type==1){
							if (ls.get(0).get(DataBaseHelper.RKCKJ) == null
									|| ls.get(0).get(DataBaseHelper.RKCKJ)
											.equals("")) {
								djEditText.setText("");
							} else {
								djEditText
										.setText(DecimalsHelper.Transfloat(Double
												.parseDouble((String) ls
														.get(0)
														.get(DataBaseHelper.RKCKJ)),MyApplication.getInstance().getNumPoint()));
							}
						}else if(op_type==2){
							if (ls.get(0).get(DataBaseHelper.CKCKJ) == null
									|| ls.get(0).get(DataBaseHelper.CKCKJ)
											.equals("")) {
								djEditText.setText("");
							} else {
								djEditText
										.setText(DecimalsHelper.Transfloat(Double
												.parseDouble((String) ls
														.get(0)
														.get(DataBaseHelper.CKCKJ)),MyApplication.getInstance().getNumPoint()));
							}
						}
						numberEditText.setText(numString);
//						zjEditText.setText("");
					}
				} else if (op_type == 0) {
					if (!list1.isEmpty()) {
						Map<String, Object> mapp = list1.get(0);
						zsTextView.setText(mapp.get(DataBaseHelper.BTKC)
								.toString());
						pdNumberEditText.setText(mapp.get(DataBaseHelper.ATKC)
								.toString());
					} else {
						if (ls.get(0).get(DataBaseHelper.KCSL) == null
								|| ls.get(0).get(DataBaseHelper.KCSL)
										.equals("")) {
							zsTextView.setText("0");
							pdNumberEditText.setText("0");
						} else {
							String mystr = DecimalsHelper.Transfloat(Double
									.parseDouble((String) ls.get(0).get(
											DataBaseHelper.KCSL)),8);
							zsTextView.setText(mystr);
							pdNumberEditText.setText(mystr);
						}
					}
				}
			} else {
				if (op_type == 1) {
					Toast.makeText(context, "本地货品信息不存在", Toast.LENGTH_SHORT)
							.show();
				} else {
					Toast.makeText(context, "本仓库货品信息不存在", Toast.LENGTH_SHORT)
							.show();
				}
			}

		}
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.plus1:
			if (numberEditText.getText().toString().equals("")) {
				numberEditText.setText("1");
			} else {
				double f = Double.parseDouble(numberEditText.getText().toString());
//				if ((Math.round(f) - f) == 0){
//					numberEditText.setText(String.valueOf((int) f + 1));
//				} else {
//				}
				
				numberEditText.setText(String.valueOf(DecimalsHelper.Transfloat(f+1,MyApplication.getInstance().getNumPoint())));
			}
			break;
		case R.id.dec1:
			if (numberEditText.getText().toString().equals("")) {
				numberEditText.setText("0");
			} else {
				double f = Double.parseDouble(numberEditText.getText().toString());
				if (f >= 1) {
					numberEditText.setText(DecimalsHelper.Transfloat(f-1,MyApplication.getInstance().getNumPoint()));
				}else{
					Toast.makeText(context, "数量不能为负数", Toast.LENGTH_LONG).show();
				}
			}
			break;
		case R.id.plus2:
			if (pdNumberEditText.getText().toString().equals("")) {
				pdNumberEditText.setText("1");
			} else {
				double f = Double.parseDouble(pdNumberEditText.getText().toString());
				pdNumberEditText.setText(String.valueOf(DecimalsHelper.Transfloat(f+1,8)));
			}
			break;
		case R.id.dec2:
			if (pdNumberEditText.getText().toString().equals("")) {
				pdNumberEditText.setText("0");
			} else {
				double f = Double.parseDouble(pdNumberEditText.getText().toString());
				pdNumberEditText.setText(DecimalsHelper.Transfloat(f-1,8));
			}
			break;
		case R.id.cancel1:
			dismiss();
			break;
		case R.id.ok1:
			if (numberEditText.getText().toString().equals("")) {
				Toast.makeText(context, "请输入数量", Toast.LENGTH_SHORT).show();
			} else if (numberEditText.getText().toString().equals("0")) {
				dm_op.Del_Moved(djid, hpid);
				EventBus.getDefault().post(
						new ObjectFive(hpid, numberEditText.getText()
								.toString()));
				EventBus.getDefault().post(new ObjectSix("计算拣货筐数量"));
				Toast.makeText(context, "数量为0，货品从单据中移除", Toast.LENGTH_SHORT).show();
				dismiss();
			} else {
				if (DecimalsHelper.NumBerStringIsFormat(numberEditText
						.getText().toString())
						&& DecimalsHelper.NumBerStringIsFormat(djEditText
								.getText().toString())
						&& DecimalsHelper.NumBerStringIsFormat(zjEditText
								.getText().toString())) {// 保证输入的数字不超过decimal(18,
															// 8)
					dm_op.Del_Moved(djid, hpid);
					String date = formatter.format(new Date(System
							.currentTimeMillis()));
					String sl = numberEditText.getText().toString();

					if (op_type == 1) {
						dm_op.insert_into(
								DataBaseHelper.TB_MoveD,str3,
								new String[] {hpid,djid,date,sl,"1",dh,
										benckTextView.getText().toString(),
										String.valueOf(Float
												.parseFloat(benckTextView
														.getText().toString())
												+ Float.parseFloat(sl)), "1",
										djEditText.getText().toString(),
										zjEditText.getText().toString(),
										dacttype, String.valueOf(ckid) });
					} else if (op_type == 2) {
						dm_op.insert_into(
								DataBaseHelper.TB_MoveD,
								str3,
								new String[] {
										hpid,
										djid,
										date,
										sl,
										"2",
										dh,
										benckTextView.getText().toString(),
										String.valueOf(Float
												.parseFloat(benckTextView
														.getText().toString())
												- Float.parseFloat(sl)), "2",
										djEditText.getText().toString(),
										zjEditText.getText().toString(),
										dacttype, String.valueOf(ckid) });
					}

					// 本地数据库是否存在有这个id的货品,不存在就添加货品
					if (!dm.searchID(hpid)) {
						Map<String, Object> littleMap = ls.get(0);
						dm.Addhp(
								Integer.parseInt(littleMap.get(
										DataBaseHelper.ID).toString()),
								littleMap.get(DataBaseHelper.HPMC).toString(),
								littleMap.get(DataBaseHelper.HPBM).toString(),
								littleMap.get(DataBaseHelper.HPTM).toString(),
								littleMap.get(DataBaseHelper.GGXH).toString(),
								littleMap.get(DataBaseHelper.JLDW).toString(),
								littleMap.get(DataBaseHelper.JLDW2).toString(),
								littleMap.get(DataBaseHelper.BigNum).toString(),
								littleMap.get(DataBaseHelper.SCCS).toString(),
								littleMap.get(DataBaseHelper.HPSX).toString(),
								littleMap.get(DataBaseHelper.HPXX).toString(),
								littleMap.get(DataBaseHelper.RKCKJ).toString(),
								littleMap.get(DataBaseHelper.CKCKJ).toString(),
								littleMap.get(DataBaseHelper.CKCKJ2).toString(),
								littleMap.get(DataBaseHelper.LBS).toString(),
								littleMap.get(DataBaseHelper.LBID).toString(),
								littleMap.get(DataBaseHelper.LBIndex)
										.toString(),
								littleMap.get(DataBaseHelper.BZ).toString(),
								null, 0, littleMap.get(DataBaseHelper.RES1)
										.toString(),
								littleMap.get(DataBaseHelper.RES2).toString(),
								littleMap.get(DataBaseHelper.RES3).toString(),
								littleMap.get(DataBaseHelper.RES4).toString(),
								littleMap.get(DataBaseHelper.RES5).toString(),
								littleMap.get(DataBaseHelper.RES6).toString(),
								"", "", 0, date,
								littleMap.get(DataBaseHelper.ImagePath)
										.toString());
					}

					EventBus.getDefault().post(
							new ObjectFive(hpid, numberEditText.getText()
									.toString()));
					EventBus.getDefault().post(new ObjectSix("计算拣货筐数量或者通知检测"));
					dismiss();
				} else {
					Toast.makeText(context, "输入框最多保存10位整数和8位小数",
							Toast.LENGTH_SHORT).show();
				}
			}

			break;
		case R.id.cancel2:
			dismiss();
			break;
		case R.id.refresh_num:
			if (mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1) == 1) {
				if (WebserviceImport.isOpenNetwork(context)) {
					loadProgressBar.setVisibility(View.VISIBLE);
					cacheThreadPool.execute(refreshRunnable);
				} else {
					Toast.makeText(context, "网络未连接", Toast.LENGTH_SHORT).show();
				}
			} else {
				ls = dm.GethpAndKCSL(hpid, ckid,false);
				
				List<Map<String, Object>> list1 = dm_op.Gt_Moved(djid, hpid,
						str3);
				if (!list1.isEmpty()) {
					Map<String, Object> mapp = list1.get(0);
					zsTextView
							.setText(mapp.get(DataBaseHelper.BTKC).toString());
					pdNumberEditText.setText(mapp.get(DataBaseHelper.ATKC)
							.toString());
				} else {
					if (ls.get(0).get(DataBaseHelper.KCSL) == null
							|| ls.get(0).get(DataBaseHelper.KCSL).equals("")) {
						zsTextView.setText("0");
						pdNumberEditText.setText("0");
					} else {
						String mystr = DecimalsHelper.Transfloat(Double
								.parseDouble((String) ls.get(0).get(
										DataBaseHelper.KCSL)),8);
						zsTextView.setText(mystr);
						pdNumberEditText.setText(mystr);
					}
				}
			}

			break;
		case R.id.ok2:
			if (pdNumberEditText.getText().toString().equals("")) {
				Toast.makeText(context, "请输入实有数", Toast.LENGTH_SHORT).show();
			} else {
				String date = formatter.format(new Date(System
						.currentTimeMillis()));
				dm_op.Del_Moved(djid, hpid);
				dm_ck.Check(djid, hpid, pdNumberEditText.getText().toString(),
						zsTextView.getText().toString(), date, ckid);
				// dm_op.Update_CKKC(hpid, ckid,
				// Float.parseFloat(pdNumberEditText.getText().toString()));
				// Toast.makeText(context, "盘点成功", Toast.LENGTH_SHORT).show();

				// 本地数据库是否存在有这个id的货品,不存在就添加货品
				if (!dm.searchID(hpid)) {
					Map<String, Object> littleMap = ls.get(0);
					dm.Addhp(Integer.parseInt(littleMap.get(DataBaseHelper.ID)
							.toString()), littleMap.get(DataBaseHelper.HPMC)
							.toString(), littleMap.get(DataBaseHelper.HPBM)
							.toString(), littleMap.get(DataBaseHelper.HPTM)
							.toString(), littleMap.get(DataBaseHelper.GGXH)
							.toString(), littleMap.get(DataBaseHelper.JLDW)
							.toString(), littleMap.get(DataBaseHelper.JLDW2)
							.toString(), littleMap.get(DataBaseHelper.BigNum)
							.toString(), littleMap.get(DataBaseHelper.SCCS)
							.toString(), littleMap.get(DataBaseHelper.HPSX)
							.toString(), littleMap.get(DataBaseHelper.HPXX)
							.toString(), littleMap.get(DataBaseHelper.RKCKJ)
							.toString(), littleMap.get(DataBaseHelper.CKCKJ)
							.toString(), littleMap.get(DataBaseHelper.CKCKJ2)
							.toString(), littleMap.get(DataBaseHelper.LBS)
							.toString(), littleMap.get(DataBaseHelper.LBID)
							.toString(), littleMap.get(DataBaseHelper.LBIndex)
							.toString(), littleMap.get(DataBaseHelper.BZ)
							.toString(), null, 0,
							littleMap.get(DataBaseHelper.RES1).toString(),
							littleMap.get(DataBaseHelper.RES2).toString(),
							littleMap.get(DataBaseHelper.RES3).toString(),
							littleMap.get(DataBaseHelper.RES4).toString(),
							littleMap.get(DataBaseHelper.RES5).toString(),
							littleMap.get(DataBaseHelper.RES6).toString(), "",
							"", 0, date, littleMap
									.get(DataBaseHelper.ImagePath).toString());
				}
				// mCallbackInterface.CallbackExecute(new
				// ObjectFive(hpid,pdNumberEditText.getText().toString()));
				float f = Float.parseFloat(zsTextView.getText().toString())
						- Float.parseFloat(pdNumberEditText.getText()
								.toString());
				if (f <= 0) {
					EventBus.getDefault().post(
							new ObjectFive(hpid, pdNumberEditText.getText()
									.toString(), "盘赢"));
				} else if (f > 0) {
					EventBus.getDefault().post(
							new ObjectFive(hpid, pdNumberEditText.getText()
									.toString(), "盘亏"));
				}
				EventBus.getDefault().post(new ObjectSix("计算拣货筐数量或者通知检测"));
				dismiss();
			}
			break;
		}
	}

	private Runnable firstrunnable = new Runnable() {

		@Override
		public void run() {
			// TODO 自动生成的方法存根
			Message msg = Message.obtain();
			if (op_type == 1) {
//				ls = WebserviceImport.GetHpInfo_byid(hpid, -1, str, str1,
//						mSharedPreferences);
				ls = WebserviceImport.GetHpAllInfocksl_ByCkId_1_0(hpid, ckid, str, str1,
						mSharedPreferences);
			} else {
				ls = WebserviceImport.GetHpInfo_byid(hpid, ckid, str, str1,
						mSharedPreferences);
			}
			msg.what = 0;
			handler.sendMessage(msg);
		}
	};

	private Runnable refreshRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO 自动生成的方法存根
			Message msg = Message.obtain();
			float[] ff = WebserviceImport.GetHP_CurrKC(hpid, ckid,
					mSharedPreferences);
			if (ff[0] < 0) {
				Toast.makeText(context, "获取数据失败", Toast.LENGTH_SHORT).show();
			} else {

				msg.obj = String.valueOf(ff[1]);
			}
			msg.what = 1;
			handler.sendMessage(msg);
		}
	};

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case 0:
				if (!ls.isEmpty()) {
					loadProgressBar.setVisibility(View.GONE);
					List<Map<String, Object>> list1 = dm_op.Gt_Moved(djid,
							hpid, str3);
					if (op_type == 1 || op_type == 2) {
						mxLayout.setVisibility(View.VISIBLE);
						lxLayout.setVisibility(View.GONE);
						benckTextView.setText(DecimalsHelper.Transfloat(Double
								.parseDouble(ls.get(0).get(DataBaseHelper.KCSL)
										.toString()),MyApplication.getInstance().getNumPoint()));
						if (!list1.isEmpty()) {
							Map<String, Object> mapp = list1.get(0);
							numberEditText.setText(mapp.get(DataBaseHelper.MSL)
									.toString());
							djEditText.setText(mapp.get(DataBaseHelper.DJ)
									.toString());
							zjEditText.setText(mapp.get(DataBaseHelper.ZJ)
									.toString());
						} else {
							
							if(op_type==1){
								if (ls.get(0).get(DataBaseHelper.RKCKJ) == null
										|| ls.get(0).get(DataBaseHelper.RKCKJ)
												.equals("")) {
									djEditText.setText("");
								} else {
									djEditText
											.setText(DecimalsHelper.Transfloat(Double
													.parseDouble((String) ls
															.get(0)
															.get(DataBaseHelper.RKCKJ)),MyApplication.getInstance().getNumPoint()));
								}
							}else if(op_type==2){
								if (ls.get(0).get(DataBaseHelper.CKCKJ) == null
										|| ls.get(0).get(DataBaseHelper.CKCKJ)
												.equals("")) {
									djEditText.setText("");
								} else {
									djEditText
											.setText(DecimalsHelper.Transfloat(Double
													.parseDouble((String) ls
															.get(0)
															.get(DataBaseHelper.CKCKJ)),MyApplication.getInstance().getNumPoint()));
								}
							}
							numberEditText.setText(numString);
//							zjEditText.setText("");
						}
					} else if (op_type == 0) {
						lxLayout.setVisibility(View.VISIBLE);
						mxLayout.setVisibility(View.GONE);
						if (!list1.isEmpty()) {
							Map<String, Object> mapp = list1.get(0);
							zsTextView.setText(mapp.get(DataBaseHelper.BTKC)
									.toString());
							pdNumberEditText.setText(mapp.get(
									DataBaseHelper.ATKC).toString());
						} else {
							if (ls.get(0).get(DataBaseHelper.KCSL) == null
									|| ls.get(0).get(DataBaseHelper.KCSL)
											.equals("")) {
								zsTextView.setText("0");
								pdNumberEditText.setText("0");
							} else {
								String mystr = DecimalsHelper.Transfloat(Double
										.parseDouble((String) ls.get(0).get(
												DataBaseHelper.KCSL)),8);
								zsTextView.setText(mystr);
								pdNumberEditText.setText(mystr);
							}
						}

					}

				} else {
					mxLayout.setVisibility(View.GONE);
					lxLayout.setVisibility(View.GONE);
					loadProgressBar.setVisibility(View.VISIBLE);
					Toast.makeText(context, "服务器数据异常，请刷新", Toast.LENGTH_SHORT).show();
					dismiss();
				}
				break;
			case 1:
				loadProgressBar.setVisibility(View.GONE);
				zsTextView.setText(DecimalsHelper.Transfloat(Double
						.parseDouble(msg.obj.toString()),8));
				break;
			}
		};
	};
	
	TextWatcher myNumberWatcher = new TextWatcher(){

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
			int pos = s.length() - 1;
			int position = s.toString().indexOf(".");
			
			if (pos - position > 8 && position != -1) {
				s.delete(pos, pos + 1);
			}else if(pos>10 && position == -1){
				s.delete(pos, pos + 1);
			}
			if (position == -1 && s.toString().length() > 10) {
				char[] numberStrings = s.toString().toCharArray();
				if (numberStrings.length > 10 && !String.valueOf(numberStrings[10]).equals(".")) {// 如果第11位不是小数点就删除
					s.delete(10, s.toString().length());
				}
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
		}
		
	};
	
	TextWatcher numTextWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO 自动生成的方法存根

			if (watch == 0) {
				if (!s.toString().equals("")) {
					if (!DecimalsHelper.NumBerStringIsFormat(s.toString())) {
						Toast.makeText(context, "输入框最多保存10位整数和8位小数", Toast.LENGTH_SHORT)
								.show();
					}
					if (s.toString().equals("0")) {
						zjEditText.setText("");
					} else {
						if (!djEditText.getText().toString().equals("")) {
							if(!s.toString().isEmpty()){
								BigDecimal b1 = new BigDecimal(s.toString());
								BigDecimal b2 = new BigDecimal(djEditText.getText().toString());
								zjEditText.setText(String.valueOf(DecimalsHelper.Transfloat(b1.multiply(b2).doubleValue(),MyApplication.getInstance().getJePoint())));
							}
						}
					}

				} else {
					zjEditText.setText("");
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

			if (pos - position > MyApplication.getInstance().getNumPoint() && position != -1) {
				s.delete(pos, pos + 1);
			}
			if (position == -1 && s.toString().length() > 10) {
				char[] numberStrings = s.toString().toCharArray();
				if (numberStrings.length > 10
						&& !String.valueOf(numberStrings[10]).equals(".")) {// 如果第11位不是小数点就删除
					s.delete(10, s.toString().length());
				}
			}
		}
	};
	
	TextWatcher djTextWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO 自动生成的方法存根
			
			if (!numberEditText.getText().toString().equals("")) {
				// 为了不让两个输入文本死循环，必须要触摸监听才能改变另一个输入框
				if (watch == 1) {
					if (!s.toString().equals("")) {
						if (!DecimalsHelper.NumBerStringIsFormat(s.toString())) {
							Toast.makeText(context, "输入框最多保存10位整数和8位小数", Toast.LENGTH_SHORT)
									.show();
						}
						if (s.toString().equals("0")) {
							zjEditText.setText("");
						} else {
							if (!numberEditText.getText().toString().equals("")) {
								BigDecimal b1 = new BigDecimal(s.toString());
								BigDecimal b2 = new BigDecimal(numberEditText
										.getText().toString());
								zjEditText.setText(String.valueOf(DecimalsHelper.Transfloat(b1.multiply(b2).doubleValue(),MyApplication.getInstance().getJePoint())));
							}
						}

					} else {
						zjEditText.setText("");
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
			
			if (pos - position > MyApplication.getInstance().getNumPoint() && position != -1) {
				s.delete(pos, pos + 1);
			}else if(pos>10 && position == -1){
				s.delete(pos, pos + 1);
			}
			if (position == -1 && s.toString().length() > 10) {
				char[] numberStrings = s.toString().toCharArray();
				if (numberStrings.length > 10 && !String.valueOf(numberStrings[10]).equals(".")) {// 如果第11位不是小数点就删除
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
			
			if (!numberEditText.getText().toString().equals("")
					&& !numberEditText.getText().toString().equals("0")) {
				// 为了不让两个输入文本死循环，必须要触摸监听才能改变另一个输入框
				if (watch == 2) {
					if (!s.toString().equals("")) {
						if (!DecimalsHelper.NumBerStringIsFormat(s.toString())) {
							Toast.makeText(context, "输入框最多保存10位整数和设置的小数", Toast.LENGTH_SHORT)
									.show();
						}
							BigDecimal b1 = new BigDecimal(s.toString());
							BigDecimal b2 = new BigDecimal(numberEditText
									.getText().toString());
							djEditText.setText(DecimalsHelper.Transfloat(b1
									.divide(b2, 4, RoundingMode.HALF_UP)
									.doubleValue(),MyApplication.getInstance().getDjPoint()));
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

			if (pos - position > MyApplication.getInstance().getJePoint() && position != -1) {
				s.delete(pos, pos + 1);
			}else if(pos>10 && position == -1){
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
}
