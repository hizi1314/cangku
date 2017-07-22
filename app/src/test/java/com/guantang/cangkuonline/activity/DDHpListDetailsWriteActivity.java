package com.guantang.cangkuonline.activity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.LruCache;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.application.MyApplication;
import com.guantang.cangkuonline.database.DataBaseCheckMethod;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.database.DataBaseMethod;
import com.guantang.cangkuonline.database.DataBaseOperateMethod;
import com.guantang.cangkuonline.eventbusBean.ObjectFive;
import com.guantang.cangkuonline.eventbusBean.ObjectSix;
import com.guantang.cangkuonline.helper.DecimalsHelper;
import com.guantang.cangkuonline.helper.ImageHelper;
import com.guantang.cangkuonline.helper.NumberWatcher;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;
import com.guantang.cangkuonline.static_constant.PathConstant;
import com.guantang.cangkuonline.webservice.WebserviceImport;

import de.greenrobot.event.EventBus;

public class DDHpListDetailsWriteActivity extends Activity implements
		OnTouchListener, OnClickListener {
	private ImageButton toggleImgBtn, refreshImgBtn;
	private TextView titleTextView, tmTextView, bmTextView,
			mcTextView, lbTextView, ggTextView, kcTextView, jldwTextView,
			hsslTextView, tdwTextView, ckjeTextView, picTextView, sxTextView,
			xxTextView, sccsTextView, bzTextView, rkckjTextView, ckckjTextView,
			ckckj2TextView, kcjeTextView, hsdwTextView, bignumTextView,
			res1TextView, res2TextView, res3TextView, res4TextView,
			res5TextView, res6TextView, res1_text, res2_text, res3_text,
			res4_text, res5_text, res6_text, rk_text,kcname;
	private EditText rkEditText, djEditText, zjEditText;
	private Button saveButton, commitBtn;
	private CheckBox hsdwCheckBox;
	private ImageButton backImgBtn, refresh_numBtn ,pdplusBtn,pddecBtn,plusButton, decButton;
	private LinearLayout mxlayout;
	private LinearLayout layout1, layout2, layout3, DJlayout,
			ZJlayout, KCJElayout,rkckj_layout,ckckj_layout;
	private TextView zmslTextView;
	private Button okBtn;
	private LinearLayout lxlayout;
	private EditText numEditText;
	private String baseUnit = "", hsUnit = "";// 基本单位和换算后的单位
	private String rkckj = "",ckckj = "";// 获取的入库参考价
	private String hpid = "";
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

	// 订单明细表字段
		private String[] str3 = { DataBaseHelper.HPID, DataBaseHelper.orderID,
				DataBaseHelper.HPMC, DataBaseHelper.HPBM, DataBaseHelper.GGXH,
				DataBaseHelper.SL, DataBaseHelper.DJ, DataBaseHelper.ZJ,
				DataBaseHelper.ddirc };

	private ProgressDialog pro_Dialog;
	private List<Map<String, Object>> ls = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> zdyls = new ArrayList<Map<String, Object>>();
	private DataBaseCheckMethod dm_ck = new DataBaseCheckMethod(this);
	private ExecutorService cacheThreadPool = Executors.newCachedThreadPool();
	private DataBaseOperateMethod dm_op = new DataBaseOperateMethod(this);
	private DataBaseMethod dm = new DataBaseMethod(this);
	private int watch = 0;
	private String detailname = "";
	private SimpleDateFormat formatter;
	private int dirc = 0;// 0采购订单，1销售订单
	private String ddh = "",ddid = "";
	private String imgpath = "", stringbase64;
//	private int width, height;
	private Boolean flag = true;// 是否flag里面的代码
	private SharedPreferences mSharedPreferences;
	
//	// 获取应用程序最大可用内存
//	int maxMemory = (int) Runtime.getRuntime().maxMemory();
//	int cacheSize = maxMemory / 8;
//	// 设置图片缓存大小为程序最大可用内存的1/8 LruCache使用最近最少原则做的缓存
//	private LruCache<String, Bitmap> mMemoryCache = new LruCache<String, Bitmap>(
//			cacheSize) {
//		@Override
//		protected int sizeOf(String key, Bitmap bitmap) {
//			return bitmap.getByteCount();
//		}
//	};

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 不去重新绘制界面
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		setContentView(R.layout.hpdetailswrite);
		mSharedPreferences = getSharedPreferences(
				ShareprefenceBean.SHAREPREFENCE_NAME, Context.MODE_PRIVATE);
		Intent intent = getIntent();
		hpid = intent.getStringExtra("hpid");
		ddh = intent.getStringExtra("ddh");
		ddid = intent.getStringExtra("ddid");
		dirc = intent.getIntExtra("dirc", 0);
		formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DisplayMetrics mDisplayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
//		width = mDisplayMetrics.widthPixels;
//		height = mDisplayMetrics.heightPixels;

		initControl();
		init();
	}

	@Override
	protected void onDestroy() {
		// TODO 自动生成的方法存根
		super.onDestroy();
	}

	public void initControl() {
		toggleImgBtn = (ImageButton) findViewById(R.id.back);
		refreshImgBtn = (ImageButton) findViewById(R.id.refresh_info);
		titleTextView = (TextView) findViewById(R.id.title);
		tmTextView = (TextView) findViewById(R.id.tm);
		bmTextView = (TextView) findViewById(R.id.bm);
		mcTextView = (TextView) findViewById(R.id.mc);
		lbTextView = (TextView) findViewById(R.id.lb);
		ggTextView = (TextView) findViewById(R.id.gg);
		lbTextView = (TextView) findViewById(R.id.lb);
		kcTextView = (TextView) findViewById(R.id.kc);
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
		saveButton = (Button) findViewById(R.id.save);
		plusButton = (ImageButton) findViewById(R.id.plus2);
		decButton = (ImageButton) findViewById(R.id.dec2);
		rkEditText = (EditText) findViewById(R.id.rk);
		djEditText = (EditText) findViewById(R.id.dj);
		zjEditText = (EditText) findViewById(R.id.zj);
		hsdwCheckBox = (CheckBox) findViewById(R.id.ck);
		layout1 = (LinearLayout) findViewById(R.id.layout_1);
		layout2 = (LinearLayout) findViewById(R.id.layout_2);
		layout3 = (LinearLayout) findViewById(R.id.layout_3);
		DJlayout = (LinearLayout) findViewById(R.id.dj_layout);
		ZJlayout = (LinearLayout) findViewById(R.id.zj_layout);
		KCJElayout = (LinearLayout) findViewById(R.id.kcje_layout);

		zmslTextView = (TextView) findViewById(R.id.zs);
		refresh_numBtn = (ImageButton) findViewById(R.id.refresh_num);
		pdplusBtn = (ImageButton) findViewById(R.id.plus);
		pddecBtn = (ImageButton) findViewById(R.id.dec);
		okBtn = (Button) findViewById(R.id.ok);
		numEditText = (EditText) findViewById(R.id.num);
		mxlayout = (LinearLayout) findViewById(R.id.mxlayout);
		lxlayout = (LinearLayout) findViewById(R.id.lxlayout);
		rkckj_layout = (LinearLayout) findViewById(R.id.rkckj_layout);
		ckckj_layout = (LinearLayout) findViewById(R.id.ckckj_layout);

		refresh_numBtn.setOnClickListener(this);
		pdplusBtn.setOnClickListener(this);
		pddecBtn.setOnClickListener(this);
		okBtn.setOnClickListener(this);
		plusButton.setOnClickListener(this);
		decButton.setOnClickListener(this);
		toggleImgBtn.setOnClickListener(this);
		refreshImgBtn.setOnClickListener(this);
		saveButton.setOnClickListener(this);
		rkEditText.addTextChangedListener(numWatcher);
		djEditText.addTextChangedListener(djWatcher);
		zjEditText.addTextChangedListener(zjWatcher);
		numEditText.addTextChangedListener(numberWatcher);
		zjEditText.setOnTouchListener(this);
		rkEditText.setOnTouchListener(this);
		djEditText.setOnTouchListener(this);
		plusButton.setOnTouchListener(this);
		decButton.setOnTouchListener(this);
		hsdwCheckBox.setOnTouchListener(this);

		if (dirc == 0) {
			titleTextView.setText("采购货品操作");
			rk_text.setText("采购数量");
			kcname.setText("货品总库存");
			ckckj_layout.setVisibility(View.GONE);
		} else if (dirc == 1) {
			titleTextView.setText("销售货品操作");
			rk_text.setText("销售数量");
			kcname.setText("货品总库存");
			rkckj_layout.setVisibility(View.GONE);
		}

		lxlayout.setVisibility(View.GONE);
		mxlayout.setVisibility(View.VISIBLE);
		
		hsdwCheckBox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO 自动生成的方法存根
						djEditText.setText("");
						zjEditText.setText("");
						if (isChecked) {
							if (!TextUtils.isEmpty(rkckj)
									&& !TextUtils.isEmpty(bignumTextView
											.getText().toString())
									&& !hsUnit.isEmpty()) {
								double f = new BigDecimal(rkckj).multiply(
										new BigDecimal(bignumTextView
												.getText().toString()))
										.doubleValue();
								rkckjTextView.setText(String
										.valueOf(DecimalsHelper
												.Transfloat(f,MyApplication.getInstance().getDjPoint()))
										+ "元/" + hsUnit);
								jldwTextView.setText(hsUnit);
							} else {
								rkckjTextView.setText(rkckj + "元/"
										+ baseUnit);
							}
							if(!TextUtils.isEmpty(ckckj)&& !TextUtils.isEmpty(bignumTextView.getText().toString())&& !hsUnit.isEmpty()){
								double f = new BigDecimal(ckckj).multiply(
										new BigDecimal(bignumTextView
												.getText().toString()))
										.doubleValue();
								ckckjTextView.setText(String
										.valueOf(DecimalsHelper
												.Transfloat(f,MyApplication.getInstance().getDjPoint()))
										+ "元/" + hsUnit);
								jldwTextView.setText(hsUnit);
							} else {
								ckckjTextView.setText(ckckj + "元/"+ baseUnit);
							}
						} else {
							rkckjTextView.setText(rkckj + "元/" + baseUnit);
							ckckjTextView.setText(ckckj + "元/" + baseUnit);
							jldwTextView.setText(baseUnit);
						}
					}
				});
		zdyls = new ArrayList<Map<String, Object>>();
		zdyls = dm_ck.Gt_Res();
		if (zdyls.size() != 0) {
			setRes(zdyls);
		}

	}

	public void init() {
		if (mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1) == 1) {
			if (WebserviceImport.isOpenNetwork(this)) {
				pro_Dialog = ProgressDialog.show(this, null, "正在加载……");
				pro_Dialog.setCancelable(true);
				cacheThreadPool.execute(firstrunnable);
			} else {
				Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
			}
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

	/**
	 * @param list
	 */
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
			rkckj = "0";
		} else {
			rkckj = DecimalsHelper.Transfloat(Double.parseDouble((String) map
					.get(DataBaseHelper.RKCKJ)),MyApplication.getInstance().getDjPoint());
		}

		if (map.get(DataBaseHelper.CurrKC) == null
				|| map.get(DataBaseHelper.CurrKC).equals("")) {
			kcTextView.setText("0");
//			zmslTextView.setText("0");
//			numEditText.setText("0");
		} else {
//			String mystr = DecimalsHelper.Transfloat(Double.parseDouble((String) map.get(DataBaseHelper.KCSL)),8);
//			zmslTextView.setText(mystr);
//			numEditText.setText(mystr);
			kcTextView.setText(DecimalsHelper.Transfloat(Double.parseDouble((String) map.get(DataBaseHelper.CurrKC)),MyApplication.getInstance().getNumPoint()));
		}
		
		baseUnit = map.get(DataBaseHelper.JLDW).toString();
		hsUnit = map.get(DataBaseHelper.JLDW2).toString();
		tmTextView.setText(map.get(DataBaseHelper.HPTM).toString());
		bmTextView.setText(map.get(DataBaseHelper.HPBM).toString());
		mcTextView.setText(map.get(DataBaseHelper.HPMC).toString());
		lbTextView.setText(map.get(DataBaseHelper.LBS).toString());
		ggTextView.setText(map.get(DataBaseHelper.GGXH).toString());
		jldwTextView.setText(baseUnit);
		sxTextView.setText(Hpsx);
		xxTextView.setText(Hpxx);
		bignumTextView.setText(map.get(DataBaseHelper.BigNum).toString());
		sccsTextView.setText(map.get(DataBaseHelper.SCCS).toString());
		bzTextView.setText(map.get(DataBaseHelper.BZ).toString());
		rkckjTextView.setText(rkckj + "元/" + baseUnit);

		if (ls.get(0).get(DataBaseHelper.CKCKJ) == null
				|| ls.get(0).get(DataBaseHelper.CKCKJ).equals("")) {
			ckckj = "0";
		} else {
			ckckj = DecimalsHelper.Transfloat(Double.parseDouble((String) map.get(DataBaseHelper.CKCKJ)),MyApplication.getInstance().getDjPoint());
		}
		ckckjTextView.setText(ckckj + "元/" + baseUnit);
		
		if (map.get(DataBaseHelper.CKCKJ2) == null
				|| map.get(DataBaseHelper.CKCKJ2).equals("")) {
			ckckj2TextView.setText("");
		} else {
			ckckj2TextView.setText(DecimalsHelper.Transfloat(Double.parseDouble((String) map.get(DataBaseHelper.CKCKJ2)),MyApplication.getInstance().getDjPoint()));
		}

		if (map.get(DataBaseHelper.RKCKJ) == null
				|| map.get(DataBaseHelper.RKCKJ).equals("")) {
			djEditText.setText("");
		} else {
			djEditText.setText(DecimalsHelper.Transfloat(Double.parseDouble((String) map.get(DataBaseHelper.RKCKJ)),MyApplication.getInstance().getDjPoint()));
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

		// String bar = (String) map.get(DataBaseHelper.HPBM);
		// for (int i = 1; i <= 3; i++) {
		// File file = new File(PathConstant.PATH_photo + bar + "_"
		// + String.valueOf(i) + ".jpg");
		// if (file.exists()) {
		//
		// if (imgpath.equals("")) {
		// imgpath = bar + "_" + String.valueOf(i) + ".jpg";
		// } else {
		// imgpath = imgpath + "\t" + bar + "_" + String.valueOf(i)
		// + ".jpg";
		// }
		// }
		// }
		
		List<Map<String, Object>> list1 = dm_op.Gt_OrderDetails(ddid, hpid,str3);
		if (!list1.isEmpty()) {
			Map<String, Object> mapp = list1.get(0);
			rkEditText.setText(mapp.get(DataBaseHelper.SL).toString());
			djEditText.setText(mapp.get(DataBaseHelper.DJ).toString());
			zjEditText.setText(mapp.get(DataBaseHelper.ZJ).toString());
		}

		hsdwCheckBox.setChecked(false);
	}

	public void setEmptyView() {
		kcjeTextView.setText("");
		kcTextView.setText("");
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
		djEditText.setText("");
		hsdwTextView.setText("");
		bignumTextView.setText("");
		res1TextView.setText("");
		res2TextView.setText("");
		res3TextView.setText("");
		res4TextView.setText("");
		res5TextView.setText("");
		res6TextView.setText("");
		rkEditText.setText("");
		djEditText.setText("");
		zjEditText.setText("");
		hsdwCheckBox.setChecked(false);
	}

	

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		final Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.refresh_info:
			if (mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1) == 1) {
				if (WebserviceImport.isOpenNetwork(this)) {
					pro_Dialog = ProgressDialog.show(this, null, "正在加载……");
					cacheThreadPool.execute(firstrunnable);
				} else {
					Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
				}
			}
			break;
//		case R.id.more:
//			if (moreLayout.isShown() == false) {
//				moreLayout.setVisibility(View.VISIBLE);
//				updownImageView.setImageDrawable(getResources().getDrawable(
//						R.drawable.arrow_up));
//				shownameTextView.setText("关闭更多字段");
//			} else {
//				moreLayout.setVisibility(View.GONE);
//				updownImageView.setImageDrawable(getResources().getDrawable(
//						R.drawable.arrow_down1));
//				shownameTextView.setText("显示更多字段");
//			}
//			break;
		case R.id.plus2:
			if (rkEditText.getText().toString().equals("")) {
				rkEditText.setText("1");
			} else {
				double f = Double.parseDouble(rkEditText.getText().toString());
//				if ((Math.round(f) - f) == 0) {
//					rkEditText.setText(String.valueOf((int) f + 1));
//				} else {
//				}
				rkEditText.setText(String.valueOf(DecimalsHelper.Transfloat(f+1,MyApplication.getInstance().getNumPoint())));
			}
			break;
		case R.id.dec2:
			if (rkEditText.getText().toString().equals("")) {
				rkEditText.setText("0");
			} else {
				float f = Float.parseFloat(rkEditText.getText().toString());
				if (f >=1) {
//					if ((Math.round(f) - f) == 0) {
//						rkEditText.setText(String.valueOf((int) f - 1));
//					} else {
//					}
					rkEditText.setText(DecimalsHelper.Transfloat(f-1,MyApplication.getInstance().getNumPoint()));
				}else{
					Toast.makeText(this, "数量不能为负数", Toast.LENGTH_LONG).show();
				}
			}
			break;
		case R.id.save:
			if (!ls.isEmpty()) {
				String sl = null;
				if (hsdwCheckBox.isChecked() == true
						&& !hsslTextView.getText().toString().equals("")
						&& (!bignumTextView.getText().toString().equals("")
								&& !bignumTextView.getText().toString()
										.equals("0") && !(bignumTextView
								.getText().toString() == null))) {
					sl = hsslTextView.getText().toString();
				} else {
					sl = rkEditText.getText().toString();
				}
				if (TextUtils.isEmpty(rkEditText.getText().toString())) {
					Toast.makeText(this, "请输入数量", Toast.LENGTH_SHORT).show();
				} else if (rkEditText.getText().toString().equals("0")) {
					dm_op.Del_OrderDetails(ddid, hpid);
					EventBus.getDefault().post(new ObjectFive(hpid, ""));
					EventBus.getDefault().post(new ObjectSix("计算拣货筐数量"));
					Toast.makeText(this, "已移除货品", Toast.LENGTH_SHORT).show();
				} else {
					if (DecimalsHelper.NumBerStringIsFormat(rkEditText
							.getText().toString())
							&& DecimalsHelper.NumBerStringIsFormat(djEditText
									.getText().toString())
							&& DecimalsHelper.NumBerStringIsFormat(zjEditText
									.getText().toString())) {
						dm_op.Del_OrderDetails(ddid, hpid);
						Map<String, Object> map = ls.get(0);
						dm_op.insert_into(
								DataBaseHelper.TB_OrderDetail,
								str3,
								new String[] {
										hpid,
										ddid,
										map.get(DataBaseHelper.HPMC).toString(),
										map.get(DataBaseHelper.HPBM).toString(),
										map.get(DataBaseHelper.GGXH).toString(),
										sl, djEditText.getText().toString(),
										zjEditText.getText().toString(),
										String.valueOf(dirc) });
						// 本地数据库是否存在有这个id的货品,不存在就添加货品
						if (!dm.searchID(hpid)) {
							dm.Addhp(Integer.parseInt(map
									.get(DataBaseHelper.ID).toString()), map
									.get(DataBaseHelper.HPMC).toString(), map
									.get(DataBaseHelper.HPBM).toString(), map
									.get(DataBaseHelper.HPTM).toString(), map
									.get(DataBaseHelper.GGXH).toString(), map
									.get(DataBaseHelper.JLDW).toString(), map
									.get(DataBaseHelper.JLDW2).toString(), map
									.get(DataBaseHelper.BigNum).toString(), map
									.get(DataBaseHelper.SCCS).toString(), map
									.get(DataBaseHelper.HPSX).toString(), map
									.get(DataBaseHelper.HPXX).toString(), map
									.get(DataBaseHelper.RKCKJ).toString(), map
									.get(DataBaseHelper.CKCKJ).toString(), map
									.get(DataBaseHelper.CKCKJ2).toString(), map
									.get(DataBaseHelper.LBS).toString(), map
									.get(DataBaseHelper.LBID).toString(), map
									.get(DataBaseHelper.LBIndex).toString(),
									map.get(DataBaseHelper.BZ).toString(),
									null, 0, map.get(DataBaseHelper.RES1)
											.toString(),
									map.get(DataBaseHelper.RES2).toString(),
									map.get(DataBaseHelper.RES3).toString(),
									map.get(DataBaseHelper.RES4).toString(),
									map.get(DataBaseHelper.RES5).toString(),
									map.get(DataBaseHelper.RES6).toString(),
									"", "", 0, null,
									map.get(DataBaseHelper.ImagePath)
											.toString());
						}
						EventBus.getDefault().post(
								new ObjectFive(hpid, rkEditText.getText()
										.toString()));
						EventBus.getDefault().post(
								new ObjectSix("计算拣货筐数量或者通知检测"));
						Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
						finish();

					} else {
						Toast.makeText(DDHpListDetailsWriteActivity.this,
								"输入框最多保存10位整数和2位小数", Toast.LENGTH_SHORT).show();
					}
				}

			} else {
				Toast.makeText(this, "没有货品信息，不能保存", Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}
	
	TextWatcher numberWatcher = new TextWatcher(){
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

	TextWatcher zjWatcher = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
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

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub

			String bgn = bignumTextView.getText().toString();
			String n = rkEditText.getText().toString();
			String sl;
			if (bgn != null && !bgn.equals("") && !bgn.equals("0")
					&& !hsdwTextView.getText().toString().equals("")
					&& !jldwTextView.getText().toString().equals("")
					&& !n.equals("")) {
				if (hsdwCheckBox.isChecked() == true) {
					sl = hsslTextView.getText().toString();
				} else {
					sl = n;
				}
			} else {
				sl = n;
			}
			if (watch == 2) {
				String price_zj = zjEditText.getText().toString();
				if (!price_zj.equals("")) {
					if (!DecimalsHelper.NumBerStringIsFormat(s.toString())) {
						Toast.makeText(DDHpListDetailsWriteActivity.this,
								"输入框最多保存10位整数和8位小数", Toast.LENGTH_SHORT).show();
					}
					if (rkEditText.getText().toString().equals("")) {
						zjEditText.setText("");
					} else {
						BigDecimal b1 = new BigDecimal(price_zj);
						BigDecimal b2 = new BigDecimal(sl);
						if(Double.parseDouble(sl)!=0){
							djEditText.setText(DecimalsHelper.Transfloat(b1.divide(
									b2, 2, RoundingMode.HALF_UP).doubleValue(),MyApplication.getInstance().getDjPoint()));
						}
					}
				}
			}

		}

	};

	TextWatcher djWatcher = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			int pos = s.length() - 1;
			int position = s.toString().indexOf(".");

			if (pos - position > MyApplication.getInstance().getDjPoint() && position != -1) {
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

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub

			String bgn = bignumTextView.getText().toString();
			String n = rkEditText.getText().toString();
			String sl;
			if (bgn != null && !bgn.equals("") && !bgn.equals("0")
					&& !hsdwTextView.getText().toString().equals("")
					&& !jldwTextView.getText().toString().equals("")
					&& !n.equals("")) {
				BigDecimal b1 = new BigDecimal(bgn);
				BigDecimal b2 = new BigDecimal(n);
				String n2="";
				if (hsdwCheckBox.isChecked() == true) {
					tdwTextView.setText(baseUnit);
					jldwTextView.setText(hsUnit);
					n2 = DecimalsHelper
							.Transfloat(b2.multiply(b1).doubleValue(),MyApplication.getInstance().getNumPoint());
					rkckjTextView.setText(String.valueOf(Float
							.parseFloat(rkckj) * Float.parseFloat(bgn))
							+ "元/" + hsUnit);
					sl = n2;
				} else {
					sl = n;
					tdwTextView.setText(hsUnit);
					jldwTextView.setText(baseUnit);
					if(Double.parseDouble(bgn)!=0){
						n2 = DecimalsHelper.Transfloat(b2.divide(b1, 4,
								RoundingMode.HALF_UP).doubleValue(),MyApplication.getInstance().getNumPoint());
					}

					rkckjTextView.setText(rkckj + "元/" + baseUnit);
				}
				hsslTextView.setText(n2);
			} else {
				sl = n;
				hsslTextView.setText("");
				tdwTextView.setText("");
			}
			if (watch == 1) {
				String price = djEditText.getText().toString();
				if (!price.equals("")) {
					if (!DecimalsHelper.NumBerStringIsFormat(s.toString())) {
						Toast.makeText(DDHpListDetailsWriteActivity.this,
								"输入框最多保存10位整数和8位小数", Toast.LENGTH_SHORT).show();
					}
					if (rkEditText.getText().toString().equals("")) {
						zjEditText.setText("");
					} else {
						BigDecimal b1 = new BigDecimal(price);
						BigDecimal b2 = new BigDecimal(sl);
						zjEditText.setText(String.valueOf(DecimalsHelper
								.Transfloat(b1.multiply(b2).doubleValue(),MyApplication.getInstance().getJePoint())));
					}
				} else {
					zjEditText.setText("");
				}
			}

		}

	};
	
	TextWatcher numWatcher = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			int pos = s.length() - 1;
			int position = s.toString().indexOf(".");

			if (pos - position > MyApplication.getInstance().getNumPoint() && position != -1) {
				s.delete(pos, pos + 1);
			}

			if (position == -1 && s.toString().length() > 10) {
				char[] numberStrings = s.toString().toCharArray();
				if (!String.valueOf(numberStrings[10]).equals(".")) {// 如果第11位不是小数点就删除
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
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub

			String bgn = bignumTextView.getText().toString();
			String n = rkEditText.getText().toString();
			String sl;
			if (bgn != null && !bgn.equals("") && !bgn.equals("0")
					&& !hsdwTextView.getText().toString().equals("")
					&& !jldwTextView.getText().toString().equals("")
					&& !n.equals("")) {
				BigDecimal b1 = new BigDecimal(bgn);
				BigDecimal b2 = new BigDecimal(n);
				String n2="";
				if (hsdwCheckBox.isChecked() == true) {
					tdwTextView.setText(baseUnit);
					jldwTextView.setText(hsUnit);
					n2 = DecimalsHelper
							.Transfloat(b2.multiply(b1).doubleValue(),MyApplication.getInstance().getNumPoint());
					rkckjTextView.setText(String.valueOf(Float
							.parseFloat(rkckj) * Float.parseFloat(bgn))
							+ "元/" + hsUnit);
					sl = n2;
				} else {
					sl = n;
					tdwTextView.setText(hsUnit);
					jldwTextView.setText(baseUnit);
					if(Double.parseDouble(bgn)!=0){
						n2 = DecimalsHelper.Transfloat(b2.divide(b1, 4,
								RoundingMode.HALF_UP).doubleValue(),MyApplication.getInstance().getNumPoint());
					}
					rkckjTextView.setText(rkckj + "元/" + baseUnit);
				}
				hsslTextView.setText(n2);
			} else {
				sl = n;
				hsslTextView.setText("");
				tdwTextView.setText("");
			}
			
			if (watch == 0) {
				String price = djEditText.getText().toString();
				if (!price.equals("")) {
					if (!DecimalsHelper.NumBerStringIsFormat(s.toString())) {
						Toast.makeText(DDHpListDetailsWriteActivity.this,
								"输入框最多保存10位整数和8位小数", Toast.LENGTH_SHORT).show();
					}
					if (rkEditText.getText().toString().equals("")) {
						zjEditText.setText("");
					} else {
						BigDecimal b1 = new BigDecimal(price);
						BigDecimal b2 = new BigDecimal(sl);
						zjEditText.setText(String.valueOf(DecimalsHelper
								.Transfloat(b1.multiply(b2).doubleValue(),MyApplication.getInstance().getJePoint())));
					}
				} else {
					zjEditText.setText("");
				}
			}

		}

	};

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			switch (v.getId()) {
			case R.id.plus2:
				watch = 0;
				break;
			case R.id.dec2:
				watch = 0;
				break;
			case R.id.dj:
				watch = 1;
				break;
			case R.id.zj:
				watch = 2;
				break;
			case R.id.rk:
				watch = 0;
				break;
			case R.id.ck:
				watch = 0;
				break;
			}
		}
		return false;
	}

	private Runnable firstrunnable = new Runnable() {

		@Override
		public void run() {
			// TODO 自动生成的方法存根
			Message msg = Message.obtain();
			ls = WebserviceImport.GetHpInfo_byid(hpid, -1, str, str1,
					mSharedPreferences);
			msg.what = 0;
			handler.sendMessage(msg);
		}
	};
	private Runnable refreshRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO 自动生成的方法存根
			Message msg = Message.obtain();
			float[] ff = WebserviceImport.GetHP_CurrKC(hpid, -1,
					mSharedPreferences);
			if (ff[0] < 0) {
				Toast.makeText(DDHpListDetailsWriteActivity.this, "获取数据失败",
						Toast.LENGTH_SHORT).show();
			} else {
				msg.obj = String.valueOf(ff[1]);
			}
			msg.what = 3;
			handler.sendMessage(msg);
		}
	};
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			
			switch (msg.what) {
			case 0:
				pro_Dialog.dismiss();
				if (!ls.isEmpty()) {
					setView(ls);
				} else {
					setEmptyView();
					Toast.makeText(DDHpListDetailsWriteActivity.this,
							"服务器数据异常，请刷新", Toast.LENGTH_SHORT).show();
				}

				break;
			case 1:
				if (!ls.isEmpty()) {
					setView(ls);
				} else {
					pro_Dialog.dismiss();
					setEmptyView();
					Toast.makeText(DDHpListDetailsWriteActivity.this,
							"服务器数据异常，请刷新", Toast.LENGTH_SHORT).show();
				}

				break;
			case 2:
				if (!ls.isEmpty()) {
					setView(ls);
				} else {
					pro_Dialog.dismiss();
					setEmptyView();
					Toast.makeText(DDHpListDetailsWriteActivity.this,
							"服务器数据异常，请刷新", Toast.LENGTH_SHORT).show();
				}
				break;
			case 3:
				pro_Dialog.dismiss();
				zmslTextView.setText(DecimalsHelper.Transfloat(Double.parseDouble(msg.obj.toString()),8));
				break;
			}
		};
	};

}
