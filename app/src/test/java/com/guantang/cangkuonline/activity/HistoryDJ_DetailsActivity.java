package com.guantang.cangkuonline.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.R.anim;
import android.R.integer;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.application.MyApplication;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.database.DataBaseOperateMethod;
import com.guantang.cangkuonline.helper.DecimalsHelper;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;
import com.guantang.cangkuonline.webservice.WebserviceHelper;
import com.guantang.cangkuonline.webservice.WebserviceImport;

public class HistoryDJ_DetailsActivity extends Activity implements
		OnClickListener, OnScrollListener{
	private LinearLayout layout1, layout2, typelayout, dwlayout, bottom_layout,bzLayout;
	private TextView dhTxtView, dateTxtView, lrsjTxtView, dwTxtView,
			jbrTxtView, text1TxtView, text2TxtView, znumTxtView, zjeTxtView,
			typeTxtView, ckTxtView, depTxtView,bzTextView;
	private ImageButton backImgBtn;
	private ListView mListView;
	private String djid = "",bztxt="";
	private int MaxMXID=0;//本单据最大明细id;
	private int from = 0;// 1 代表从本地数据库获取，0代表从服务端获取
	private DataBaseOperateMethod dm_op = new DataBaseOperateMethod(this);
	private List<Map<String, Object>> DJList = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> MXList = new ArrayList<Map<String, Object>>();
	private String[] str1 = { DataBaseHelper.MVDH, DataBaseHelper.MVDT,
			DataBaseHelper.DWName, DataBaseHelper.JBR, DataBaseHelper.BZ,
			DataBaseHelper.mType, DataBaseHelper.actType,
			DataBaseHelper.DepName, DataBaseHelper.DepID, DataBaseHelper.CKMC,
			DataBaseHelper.CKID, DataBaseHelper.Lrr, DataBaseHelper.Lrsj,
			DataBaseHelper.DWID, DataBaseHelper.Details, DataBaseHelper.HPzj };
	private String[] str11 = { DataBaseHelper.HPBM, DataBaseHelper.HPMC,
			DataBaseHelper.GGXH, DataBaseHelper.JLDW, DataBaseHelper.MSL,
			DataBaseHelper.ZJ, DataBaseHelper.DJ };
	private String[] str2 = { DataBaseHelper.HPBM, DataBaseHelper.HPMC,
			DataBaseHelper.GGXH, DataBaseHelper.JLDW, DataBaseHelper.MSL,
			DataBaseHelper.ZJ, DataBaseHelper.DJ, DataBaseHelper.HPD_ID };
	private String[] strs2 = {"HPBM","HPMC","GGXH","JLDW","msl","zj","dj","hpd_id"};
	private String[] str4 = { DataBaseHelper.HPBM, DataBaseHelper.HPMC,
			DataBaseHelper.GGXH, DataBaseHelper.JLDW, DataBaseHelper.BTKC,
			DataBaseHelper.ATKC, "profit", "lose" };
	private String[] strs4 = {"HPBM","HPMC","GGXH","JLDW","ZMSL","PDSL","YSL","KSL"};
	private String[] str7 = { DataBaseHelper.HPBM, DataBaseHelper.HPMC,
			DataBaseHelper.GGXH, DataBaseHelper.JLDW, DataBaseHelper.MSL,
			DataBaseHelper.HPD_ID, DataBaseHelper.BTKC, DataBaseHelper.ATKC,
			DataBaseHelper.MVDirect };
	private ExecutorService cacheThreadPool = Executors.newCachedThreadPool();
	private SharedPreferences mSharedPreferences;
	private ProgressDialog progressDialog;
//	private Moved_DetailsAdapter moved_DetailsAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_dj_details);
		initView();
		init();

	}

	public void initView() {
		layout1 = (LinearLayout) findViewById(R.id.layout1);
		layout2 = (LinearLayout) findViewById(R.id.layout2);
		typelayout = (LinearLayout) findViewById(R.id.typelayout);
		dwlayout = (LinearLayout) findViewById(R.id.dwlayout);
		bottom_layout = (LinearLayout) findViewById(R.id.layout_bottom);
		dhTxtView = (TextView) findViewById(R.id.dh);
		typeTxtView = (TextView) findViewById(R.id.type);
		dateTxtView = (TextView) findViewById(R.id.date);
		lrsjTxtView = (TextView) findViewById(R.id.lrsj);
		dwTxtView = (TextView) findViewById(R.id.dw);
		depTxtView = (TextView) findViewById(R.id.dep);
		ckTxtView = (TextView) findViewById(R.id.ck);
		jbrTxtView = (TextView) findViewById(R.id.jbr);
		text1TxtView = (TextView) findViewById(R.id.text1);
		text2TxtView = (TextView) findViewById(R.id.text2);
		znumTxtView = (TextView) findViewById(R.id.zs);
		zjeTxtView = (TextView) findViewById(R.id.zje);
		backImgBtn = (ImageButton) findViewById(R.id.back);
		mListView = (ListView) findViewById(R.id.list);
		bzLayout = (LinearLayout) findViewById(R.id.bzlayout);
		bzTextView  = (TextView) findViewById(R.id.bz);
		
		bzLayout.setOnClickListener(this);
		backImgBtn.setOnClickListener(this);
		mListView.setOnScrollListener(this);
		
		mSharedPreferences = getSharedPreferences(ShareprefenceBean.SHAREPREFENCE_NAME, Context.MODE_PRIVATE);
		
	}

	public void init() {
		Intent intent = getIntent();
		djid = intent.getStringExtra("HPM_ID");
		from = intent.getIntExtra("from", 0);
		switch (from) {
		case 0:
			Bundle bundle = intent.getExtras();
			DJList = (List<Map<String, Object>>) bundle.getSerializable("DJDetails");
			break;
		case 1:
			DJList = dm_op.Gt_Movem(djid, str1);
			break;
		}
		Map<String, Object> map = DJList.get(0);
		bztxt = map.get(DataBaseHelper.BZ).toString();
		if (map.get(DataBaseHelper.mType).toString().equals("0")){
			layout1.setVisibility(View.GONE);
			layout2.setVisibility(View.VISIBLE);
			text1TxtView.setText("盘盈数量");
			text2TxtView.setText("盘亏数量");
			typelayout.setVisibility(View.GONE);
			if (map.get(DataBaseHelper.MVDH).toString().equals("anyType{}")){
				dhTxtView.setText("服务端自动编号");
			} else {
				dhTxtView.setText(map.get(DataBaseHelper.MVDH).toString());
			}
			if(map.get(DataBaseHelper.CKMC).toString().equals("anyType{}")){
				ckTxtView.setText("");
			}else{
				ckTxtView.setText(map.get(DataBaseHelper.CKMC).toString());
			}
			if(map.get(DataBaseHelper.JBR).toString().equals("anyType{}")){
				jbrTxtView.setText("");
			}else{
				jbrTxtView.setText(map.get(DataBaseHelper.JBR).toString());
			}
			if(map.get(DataBaseHelper.MVDT).toString().equals("anyType{}")){
				dateTxtView.setText("");
			}else{
				dateTxtView.setText(map.get(DataBaseHelper.MVDT).toString().replace("T", " ").subSequence(0, 10));
			}
			if(map.get(DataBaseHelper.Lrsj).toString().equals("anyType{}")){
				lrsjTxtView.setText("");
			}else{
				lrsjTxtView.setText(map.get(DataBaseHelper.Lrsj).toString().replace("T", " "));
			}
			if(bztxt.equals("anyType{}")){
				bzTextView.setText("");
			}else{
				bzTextView.setText(bztxt);
			}
		} else {
			layout2.setVisibility(View.GONE);
			layout1.setVisibility(View.VISIBLE);
			typelayout.setVisibility(View.VISIBLE);
			text1TxtView.setText("总数量");
			text2TxtView.setText("总金额");
			if (map.get(DataBaseHelper.MVDH)==null||map.get(DataBaseHelper.MVDH).toString().equals("anyType{}")) {
				dhTxtView.setText("服务端自动编号");
			} else {
				dhTxtView.setText(map.get(DataBaseHelper.MVDH).toString());
			}
			if(map.get(DataBaseHelper.CKMC)==null||map.get(DataBaseHelper.CKMC).toString().equals("anyType{}")){
				ckTxtView.setText("");
			}else{
				ckTxtView.setText(map.get(DataBaseHelper.CKMC).toString());
			}
			if(map.get(DataBaseHelper.JBR)==null||map.get(DataBaseHelper.JBR).toString().equals("anyType{}")){
				jbrTxtView.setText("");
			}else{
				jbrTxtView.setText(map.get(DataBaseHelper.JBR).toString());
			}
			if(map.get(DataBaseHelper.MVDT)==null||map.get(DataBaseHelper.MVDT).toString().equals("anyType{}")){
				dateTxtView.setText("");
			}else{
				dateTxtView.setText(map.get(DataBaseHelper.MVDT).toString().replace("T", " ").subSequence(0, 10));
			}
			if(map.get(DataBaseHelper.Lrsj)==null||map.get(DataBaseHelper.Lrsj).toString().equals("anyType{}")){
				lrsjTxtView.setText("");
			}else{
				lrsjTxtView.setText(map.get(DataBaseHelper.Lrsj).toString().replace("T", " "));
			}
			if(map.get(DataBaseHelper.DepName)==null||map.get(DataBaseHelper.DepName).toString().equals("anyType{}")){
				depTxtView.setText("");
			}else{
				depTxtView.setText(map.get(DataBaseHelper.DepName).toString());
			}
			if(map.get(DataBaseHelper.actType)==null||map.get(DataBaseHelper.actType).toString().equals("anyType{}")){
				typeTxtView.setText("");
			}else{
				typeTxtView.setText(map.get(DataBaseHelper.actType).toString());
			}
			if(map.get(DataBaseHelper.DWName)==null||map.get(DataBaseHelper.DWName).toString().equals("anyType{}")){
				dwTxtView.setText("");
			}else{
				dwTxtView.setText(map.get(DataBaseHelper.DWName).toString());
			}
			if(bztxt.equals("anyType{}")){
				bzTextView.setText("");
			}else{
				bzTextView.setText(bztxt);
			}
			bottom_layout.setVisibility(View.VISIBLE);
		}
		switch (from) {
		case 0://服务端单据明细
			progressDialog = ProgressDialog.show(this, null, "正在加载数据");
			cacheThreadPool.execute(addmovedRunnable);
			break;
		case 1://本地单据明细
			if (map.get(DataBaseHelper.mType).toString().equals("0")) {
				MXList = dm_op.Gt_Moved_details(djid, str7);
				double ying=0,kui=0;
				for (int i = 0; i < MXList.size(); i++) {
					if (MXList.get(i).get(DataBaseHelper.MVDirect).equals("1")) {
						MXList.get(i).put("profit",
								(String) MXList.get(i).get(DataBaseHelper.MSL));
						MXList.get(i).put("lose", "");
						ying+=Float.parseFloat(MXList.get(i).get("profit").toString());
					} else {
						MXList.get(i).put("profit", "");
						MXList.get(i).put("lose",
								(String) MXList.get(i).get(DataBaseHelper.MSL));
						kui+=Float.parseFloat(MXList.get(i).get("lose").toString());
					}
				}
				znumTxtView.setText(DecimalsHelper.Transfloat(ying,MyApplication.getInstance().getNumPoint()));
				zjeTxtView.setText(DecimalsHelper.Transfloat(kui,MyApplication.getInstance().getJePoint()));
				setAdapter(MXList, 0);// 0 代表是盘点单据
			} else {
				MXList = dm_op.Gt_Moved_details(djid, str2);
				setAdapter(MXList, 1);// 1、2 代表不是盘点单据
				znumTxtView.setText(dm_op.Gt_Moved_znum(djid));
				zjeTxtView.setText(dm_op.Gt_Moved_zje(djid));
			}
			break;
		}
	}

	public void setAdapter(List<Map<String, Object>> list, int op_type) {
//		moved_DetailsAdapter = new Moved_DetailsAdapter(this, op_type);
//		moved_DetailsAdapter.setData(list);
//		mListView.setAdapter(moved_DetailsAdapter);
		
		if (op_type == 0) {
			SimpleAdapter listItemAdapter = new SimpleAdapter(this, list,
					R.layout.djdetail_item_check, str4, new int[] { R.id.bm,
							R.id.mc, R.id.gg, R.id.jldw, R.id.znum, R.id.snum,
							R.id.profit, R.id.lose });
			mListView.setAdapter(listItemAdapter);
		} else {
			SimpleAdapter listItemAdapter = new SimpleAdapter(this, list,
					R.layout.djdetail_item, str11, new int[] { R.id.bm,
							R.id.mc, R.id.gg, R.id.jldw, R.id.num, R.id.zj,
							R.id.dj });
			mListView.setAdapter(listItemAdapter);
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.bzlayout:
			View view =  LayoutInflater.from(this).inflate(R.layout.simple_textview, null);
			TextView textView= (TextView)view.findViewById(R.id.simple_textview);
			if(bztxt.equals("anyType{}")){
				textView.setText("备注：\n\t"+"");
			}else{
				textView.setText("备注：\n\t"+bztxt);
			}
			
			PopupWindow popupWindow = new PopupWindow(view,LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			// 这个是为了点击“返回Back”也能使其消失.
			popupWindow.setBackgroundDrawable(new BitmapDrawable());
			// 使其聚焦
			popupWindow.setFocusable(true);
			// 设置允许在外点击消失
			popupWindow.setOutsideTouchable(true);
			// 刷新状态
			popupWindow.update();
			int[] location = new int[2];
			view.getLocationOnScreen(location);
			popupWindow.showAtLocation(view, Gravity.CENTER, location[0], location[1]-popupWindow.getHeight());
			break;
		}
	}

	 Runnable addmovedRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO 自动生成的方法存根
			Message msg = Message.obtain();
			if(DJList.get(0).get(DataBaseHelper.mType).toString().equals("0")){
				MXList=WebserviceImport.Gt_PDDJ_details(100000000, "0", DJList.get(0).get(DataBaseHelper.HPM_ID).toString(), str4, strs4, WebserviceHelper.Gt_PDDJ_details, mSharedPreferences);
			}else{
				MXList=WebserviceImport.Gt_DJ_details(1000000000, "0", DJList.get(0).get(DataBaseHelper.HPM_ID).toString(),str2,strs2, WebserviceHelper.Gt_DJ_details, mSharedPreferences);
//				MaxMXID=WebserviceImport.GtMaxID_DJ_details(DJList.get(0).get(DataBaseHelper.HPM_ID).toString(),WebserviceHelper.GtMaxID_DJ_details,mSharedPreferences);
			}
			msg.what=1;
			handler.sendMessage(msg);
		}
	 };
	 
	 Handler handler = new Handler(){
		 public void handleMessage(android.os.Message msg) {
			 switch(msg.what){
			 case 1:
				 if(DJList.get(0).get(DataBaseHelper.mType).toString().equals("0")){
					 double ying=0,kui=0;
					 for(int i=0;i<MXList.size();i++){
						 if(!MXList.get(i).get("profit").toString().equals("")){
							 ying+=Float.parseFloat(MXList.get(i).get("profit").toString()); 
						 }
						 if(!MXList.get(i).get("lose").toString().equals("")){
							 kui+=Float.parseFloat(MXList.get(i).get("lose").toString());
						 }
					 }
					 znumTxtView.setText(DecimalsHelper.Transfloat(ying,MyApplication.getInstance().getNumPoint()));
					 zjeTxtView.setText(DecimalsHelper.Transfloat(kui,MyApplication.getInstance().getJePoint()));
					 setAdapter(MXList, 0);// 0 代表不是盘点单据
				 }else{
					 double znum=0,zje=0;
					 for(int i=0;i<MXList.size();i++){
						 if(!MXList.get(i).get(DataBaseHelper.MSL).toString().equals("")){
							 znum+=Float.parseFloat(MXList.get(i).get(DataBaseHelper.MSL).toString());
						 }
						 if(!MXList.get(i).get(DataBaseHelper.ZJ).toString().equals("")){
							 zje+=Float.parseFloat(MXList.get(i).get(DataBaseHelper.ZJ).toString());
						 }
					 }
					 znumTxtView.setText(DecimalsHelper.Transfloat(znum,MyApplication.getInstance().getNumPoint()));
					 zjeTxtView.setText(DecimalsHelper.Transfloat(zje,MyApplication.getInstance().getJePoint()));
					 setAdapter(MXList, 1);// 1 代表不是盘点单据
				 }
				 progressDialog.dismiss();
				 break;
			 }
		 };
	 };
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO 自动生成的方法存根

	}
	
}
