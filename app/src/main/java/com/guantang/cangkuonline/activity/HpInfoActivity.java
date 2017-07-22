package com.guantang.cangkuonline.activity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.PrivateCredentialPermission;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.adapter.ShowCKandKCSLAdapter;
import com.guantang.cangkuonline.application.MyApplication;
import com.guantang.cangkuonline.database.DataBaseCheckMethod;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.database.DataBaseMethod;
import com.guantang.cangkuonline.database.DataBaseOperateMethod;
import com.guantang.cangkuonline.eventbusBean.ObjectThree;
import com.guantang.cangkuonline.helper.CheckEditWatcher;
import com.guantang.cangkuonline.helper.DecimalsHelper;
import com.guantang.cangkuonline.helper.ImageHelper;
import com.guantang.cangkuonline.helper.RightsHelper;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;
import com.guantang.cangkuonline.static_constant.PathConstant;
import com.guantang.cangkuonline.webservice.WebserviceImport;
import com.guantang.cangkuonline.webservice.WebserviceImport_more;

import de.greenrobot.event.EventBus;

public class HpInfoActivity extends Activity implements OnClickListener {
	private ImageButton backBtn, ckkc_butBtn;
	private ImageView updownImageView;
//	private Button modifytmBtn;
	private LinearLayout moreLayout,lookMorelayout,picLayout;
	private EditText myEditText;
	private ScrollView scrollView;
	private ListView ckkcListView;
	private TextView modTextView,tiaomaTextView, bianmaTextView, mingchengTextView,
			xinghaoTextView, typeTextVIew, kcjeTextView, kucunTextView,
			kucunTextView2, danweiTextView2,jldwTextView,
			shangxianTextView, xiaxianText, sccsTextView, beizhuTextView,picTextView,
			rkckjTextView, ckckjTextVIew, ckckjTextView2,bignumTextView, res1_TextView,
			res2_TextView, res3_TextView, res4_TextView, res5_TextView,
			res6_TextView, res1_text, res2_text, res3_text, res4_text,
			res5_text, res6_text;
	private List<Map<String, Object>> ls, ls1,ls2;//ls 货品信息
	private String[] str={DataBaseHelper.ID,DataBaseHelper.HPMC,DataBaseHelper.HPBM,DataBaseHelper.HPTM,DataBaseHelper.GGXH,
			DataBaseHelper.CurrKC,DataBaseHelper.JLDW,DataBaseHelper.HPSX,DataBaseHelper.HPXX,
			DataBaseHelper.SCCS,DataBaseHelper.BZ,DataBaseHelper.RKCKJ,DataBaseHelper.CKCKJ,
			DataBaseHelper.CKCKJ2,DataBaseHelper.JLDW2,DataBaseHelper.BigNum,DataBaseHelper.RES1,
			DataBaseHelper.RES2,DataBaseHelper.RES3,DataBaseHelper.RES4,DataBaseHelper.RES5,DataBaseHelper.RES6,
			DataBaseHelper.LBS,DataBaseHelper.KCJE,DataBaseHelper.ImagePath};
	private String[] str1={"id","HPMC","HPBM","HPTBM","GGXH","CurrKc","JLDW","HPSX","HPXX","SCCS","BZ","RKCKJ","CKCKJ",
			"CKCKJ2","JLDW2","BigNum","res1","res2","res3","res4","res5","res6","LBS","kcje","ImageUrl"};
	private String[] str2={DataBaseHelper.ID,DataBaseHelper.HPID,DataBaseHelper.CKID,DataBaseHelper.KCSL};
	private String[] str3={"id","hpid","ckid","kcsl"};
	private int width;
	private int height;
	private DataBaseMethod dm = new DataBaseMethod(this);
	private DataBaseCheckMethod dm_ck = new DataBaseCheckMethod(this);
	private DataBaseOperateMethod dm_op=new DataBaseOperateMethod(this);
	private String hpid = "";
	private Matrix matrix = new Matrix();
	private DecimalsHelper decimals = new DecimalsHelper();
	private ProgressDialog pro_dialog;
	private SharedPreferences mSharedPreferences;
	/**
	 * 加载本页面是，判断是否有图片
	 * */
	private Boolean pic_serFlag=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.hpinfo_activity);
		mSharedPreferences = getSharedPreferences(ShareprefenceBean.SHAREPREFENCE_NAME, Context.MODE_PRIVATE);
		EventBus.getDefault().register(this);
		Intent intent = getIntent();
		hpid = intent.getStringExtra("id");
		ls = new ArrayList<Map<String, Object>>();
		ls2=new ArrayList<Map<String, Object>>();
		initControl();	
	}
	
	@Override
	protected void onStart() {
		// TODO 自动生成的方法存根
		super.onStart();
		init_data();
	}
	
	@Override
	protected void onDestroy() {
		// TODO 自动生成的方法存根
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
	
	private void initControl() {
		backBtn = (ImageButton) this.findViewById(R.id.back);
		modTextView = (TextView) this.findViewById(R.id.mod);
		ckkc_butBtn = (ImageButton) this.findViewById(R.id.ckkc_but);
		lookMorelayout = (LinearLayout) this.findViewById(R.id.look_moved);
		moreLayout = (LinearLayout) this.findViewById(R.id.morelayout);
		picLayout = (LinearLayout) this.findViewById(R.id.piclayout);
		scrollView = (ScrollView) this.findViewById(R.id.addscoll);
		ckkcListView=(ListView) this.findViewById(R.id.ckkc);
		tiaomaTextView = (TextView) this.findViewById(R.id.tm);
		bianmaTextView = (TextView) this.findViewById(R.id.bm);
		jldwTextView = (TextView) this.findViewById(R.id.jldw);
		mingchengTextView = (TextView) this.findViewById(R.id.name);
		xinghaoTextView = (TextView) this.findViewById(R.id.gg);
		typeTextVIew = (TextView) this.findViewById(R.id.lb);
		kcjeTextView = (TextView) this.findViewById(R.id.kcje);
		kucunTextView = (TextView) this.findViewById(R.id.kc);
		kucunTextView2 = (TextView) this.findViewById(R.id.kc2);
		danweiTextView2 = (TextView) this.findViewById(R.id.dw2);
		picTextView = (TextView) this.findViewById(R.id.pic);
		shangxianTextView = (TextView) this.findViewById(R.id.sx);
		xiaxianText = (TextView) this.findViewById(R.id.xx);
		sccsTextView = (TextView) this.findViewById(R.id.sccs);
		beizhuTextView = (TextView) this.findViewById(R.id.bz);
		rkckjTextView = (TextView) this.findViewById(R.id.rkckj);
		ckckjTextVIew = (TextView) this.findViewById(R.id.ckckj);
		ckckjTextView2 = (TextView) this.findViewById(R.id.ckckj2);
		bignumTextView = (TextView) this.findViewById(R.id.bignum);
		res1_TextView = (TextView) this.findViewById(R.id.res1);
		res2_TextView = (TextView) this.findViewById(R.id.res2);
		res3_TextView = (TextView) this.findViewById(R.id.res3);
		res4_TextView = (TextView) this.findViewById(R.id.res4);
		res5_TextView = (TextView) this.findViewById(R.id.res5);
		res6_TextView = (TextView) this.findViewById(R.id.res6);
		res1_text = (TextView) this.findViewById(R.id.res1_text);
		res2_text = (TextView) this.findViewById(R.id.res2_text);
		res3_text = (TextView) this.findViewById(R.id.res3_text);
		res4_text = (TextView) this.findViewById(R.id.res4_text);
		res5_text = (TextView) this.findViewById(R.id.res5_text);
		res6_text = (TextView) this.findViewById(R.id.res6_text);
//		modifytmBtn = (Button) this.findViewById(R.id.modifytmBtn);
		
		lookMorelayout.setOnClickListener(this);
		modTextView.setOnClickListener(this);
		backBtn.setOnClickListener(this);
		ckkc_butBtn.setOnClickListener(this);
		picLayout.setOnClickListener(this);
//		modifytmBtn.setOnClickListener(this);
		ls1 = new ArrayList<Map<String, Object>>();
		ls1 = dm_ck.Gt_Res();
		if (ls1.size() != 0) {
			setRes(ls1);
		}
	};
	
	public void init_data(){
		if(mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1)==1){
			if(WebserviceImport.isOpenNetwork(this)){
				pro_dialog = ProgressDialog.show(HpInfoActivity.this,null, "正在从服务端加载数据");
				pro_dialog.setCancelable(false);
				new Thread(refreshRunnable).start();
			}else{
				Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
			}
		}else if(mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1)==-1){
			ls=dm.Gethp(str,hpid);
			if(ls!=null){
				setTextView();
			}
			ls2=dm_op.Gt_ckkc_byhpid(hpid, str2);
			if(ls2!=null){
				setCKKC();
			}
		}
	}
	
	private void setRes(List<Map<String, Object>> list) {
		if (list.size() != 0) {
			String str;
			str = (String) list.get(0).get("文本型1");
			if (str == null || str.equals("")) {
				res1_text.setText("扩展字段1:");
			} else {
				res1_text.setText(str+":");
			}
			str = (String) list.get(1).get("文本型2");
			if (str == null || str.equals("")) {
				res2_text.setText("扩展字段2:");
			} else {
				res2_text.setText(str+":");
			}
			str = (String) list.get(2).get("文本型3");
			if (str == null || str.equals("")) {
				res3_text.setText("扩展字段3:");
			} else {
				res3_text.setText(str+":");
			}
			str = (String) list.get(3).get("文本型4");
			if (str == null || str.equals("")) {
				res4_text.setText("扩展字段4:");
			} else {
				res4_text.setText(str+":");
			}
			str = (String) list.get(4).get("文本型5");
			if (str == null || str.equals("")) {
				res5_text.setText("扩展字段5:");
			} else {
				res5_text.setText(str+":");
			}
			str = (String) list.get(5).get("文本型6");
			if (str == null || str.equals("")) {
				res6_text.setText("扩展字段6:");
			} else {
				res6_text.setText(str+":");
			}
		}
	}
	
	public void setTextView() {
		String Hpsx = "", Hpxx = "", bignum = "", num2 = "", Kcje = "", ckj = "";
		Float f, num;
		if (ls.get(0).get(DataBaseHelper.HPSX) == null || ls.get(0).get(DataBaseHelper.HPSX).equals("")) {
			Hpsx = "";
		}  else {
			Hpsx = DecimalsHelper.Transfloat(Double.parseDouble((String) ls.get(0).get(DataBaseHelper.HPSX)),MyApplication.getInstance().getNumPoint());
		}
		
		if (ls.get(0).get(DataBaseHelper.HPXX) == null || ls.get(0).get(DataBaseHelper.HPXX).equals("")) {
			Hpxx = "";
		} else {
			Hpxx = DecimalsHelper.Transfloat(Double.parseDouble((String) ls.get(0).get(DataBaseHelper.HPXX)),MyApplication.getInstance().getNumPoint());
		}

		if (ls.get(0).get(DataBaseHelper.BigNum) != null) {
			bignum = (String) ls.get(0).get(DataBaseHelper.BigNum);
			if (!bignum.equals("") && Float.parseFloat(bignum)!=0) {
				bignumTextView.setText(DecimalsHelper.Transfloat(Double.parseDouble(bignum),MyApplication.getInstance().getNumPoint()));
				try {
					f = Float.parseFloat(bignum);
					num = Float.parseFloat((String) ls.get(0).get(
							DataBaseHelper.CurrKC));
					BigDecimal b1 = new BigDecimal(Float.toString(f));
					BigDecimal b2 = new BigDecimal(Float.toString(num));
					
					num2 = DecimalsHelper.Transfloat(b2.divide(b1, 4, RoundingMode.HALF_UP).doubleValue(),MyApplication.getInstance().getNumPoint());
				} catch (Exception e) {
					num2 = "";
					Toast.makeText(this, "换算比例错误", Toast.LENGTH_SHORT).show();
				}
			}
		}
		
		kucunTextView2.setText(num2);
		tiaomaTextView.setText((String) ls.get(0).get(DataBaseHelper.HPTM));
		bianmaTextView.setText((String) ls.get(0).get(DataBaseHelper.HPBM));
		jldwTextView.setText((String)ls.get(0).get(DataBaseHelper.JLDW));
		mingchengTextView.setText((String) ls.get(0).get(DataBaseHelper.HPMC));
		xinghaoTextView.setText((String) ls.get(0).get(DataBaseHelper.GGXH));
		if(!ls.get(0).get(DataBaseHelper.CurrKC).toString().equals("")&&ls.get(0).get(DataBaseHelper.CurrKC)!=null){
			kucunTextView.setText(DecimalsHelper.Transfloat(Double.parseDouble(ls.get(0).get(DataBaseHelper.CurrKC).toString()),MyApplication.getInstance().getNumPoint())+" "+ls.get(0).get(DataBaseHelper.JLDW));
		}
		typeTextVIew.setText((String) ls.get(0).get(DataBaseHelper.LBS));
		shangxianTextView.setText(Hpsx);
		xiaxianText.setText(Hpxx);
		if(null!=ls.get(0).get(DataBaseHelper.KCJE) && !ls.get(0).get(DataBaseHelper.KCJE).toString().equals("")){
			kcjeTextView.setText(DecimalsHelper.Transfloat(Double.parseDouble(ls.get(0).get(DataBaseHelper.KCJE).toString()),MyApplication.getInstance().getJePoint()));
		}
		
		sccsTextView.setText((String) ls.get(0).get(DataBaseHelper.SCCS));
		beizhuTextView.setText((String) ls.get(0).get(DataBaseHelper.BZ));
		if(!ls.get(0).get(DataBaseHelper.RKCKJ).toString().equals("")&&ls.get(0).get(DataBaseHelper.RKCKJ)!=null){
			rkckjTextView.setText(DecimalsHelper.Transfloat(Double.parseDouble(ls.get(0).get(DataBaseHelper.RKCKJ).toString()),MyApplication.getInstance().getDjPoint()));
		}
		if(!ls.get(0).get(DataBaseHelper.CKCKJ).toString().equals("")&&ls.get(0).get(DataBaseHelper.CKCKJ)!=null){
			ckckjTextVIew.setText(DecimalsHelper.Transfloat(Double.parseDouble(ls.get(0).get(DataBaseHelper.CKCKJ).toString()),MyApplication.getInstance().getDjPoint()));
		}
		ckckjTextView2.setText((String) ls.get(0).get(DataBaseHelper.CKCKJ2));
		danweiTextView2.setText((String) ls.get(0).get(DataBaseHelper.JLDW2));
		res1_TextView.setText((String) ls.get(0).get(DataBaseHelper.RES1));
		res2_TextView.setText((String) ls.get(0).get(DataBaseHelper.RES2));
		res3_TextView.setText((String) ls.get(0).get(DataBaseHelper.RES3));
		res4_TextView.setText((String) ls.get(0).get(DataBaseHelper.RES4));
		res5_TextView.setText((String) ls.get(0).get(DataBaseHelper.RES5));
		res6_TextView.setText((String) ls.get(0).get(DataBaseHelper.RES6));
	}
	
	public void setCKKC(){
		String ckid,ckmc,kcsl,str="";
		ShowCKandKCSLAdapter showCKandKCSLAdapter = new ShowCKandKCSLAdapter(this);
		if(ls2!=null&&ls2.size()!=0){
			ckkc_butBtn.setImageResource(R.drawable.down);
//			ckkcListView.setVisibility(View.VISIBLE);
			List<String> list = new ArrayList<String>();
			for(int i=0;i<ls2.size();i++){
				Map<String, Object> map = new HashMap<String, Object>();
				ckid=ls2.get(i).get(DataBaseHelper.CKID).toString();
				kcsl=ls2.get(i).get(DataBaseHelper.KCSL).toString();
				ckmc=dm_op.Gt_ck_name(ckid);
				String string = ckmc+"\t:\t"+kcsl;
				list.add(string);
			}
			if(!list.isEmpty()){
				showCKandKCSLAdapter.setData(list);
				ckkcListView.setAdapter(showCKandKCSLAdapter);
			}
		}else{
			ckkc_butBtn.setImageResource(R.drawable.down);
			List<String> list = new ArrayList<String>();
			String string = "无分仓库库存";
			list.add(string);
			showCKandKCSLAdapter.setData(list);
			ckkcListView.setAdapter(showCKandKCSLAdapter);
		}
		//默认情况下Android是禁止在ScrollView中放入另外的ScrollView的，它的高度是无法计算的，上面的ckkcListView是嵌套在ScrollView中的。
		int totalHight = 0;
		for(int i=0;i<showCKandKCSLAdapter.getCount();i++){
			View listItem = showCKandKCSLAdapter.getView(i, null, ckkcListView);
			listItem.measure(0, 0);
			totalHight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = ckkcListView.getLayoutParams();
		params.height = totalHight + ckkcListView.getPaddingBottom()+ckkcListView.getPaddingTop()+(ckkcListView.getDividerHeight() * (ckkcListView.getCount() - 1));
		ckkcListView.setLayoutParams(params);
	}
	
	@Override
	public void onClick(View v) {
		Intent intent=new Intent();
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
//		case R.id.modifytmBtn:
//			modifyHPTM();
//			break;
		case R.id.look_moved:
			if(!ls.isEmpty()){
				intent.setClass(this, Moved_hpActivity.class);
				intent.putExtra("hpid", hpid);
				intent.putExtra("hpmc", ls.get(0).get(DataBaseHelper.HPMC).toString());
				intent.putExtra("hpbm", ls.get(0).get(DataBaseHelper.HPBM).toString());
				intent.putExtra("ggxh", ls.get(0).get(DataBaseHelper.GGXH).toString());
				intent.putExtra("jldw", ls.get(0).get(DataBaseHelper.JLDW).toString());
				intent.putExtra("lb", ls.get(0).get(DataBaseHelper.LBS).toString());
				startActivity(intent);
			}
			break;
		case R.id.mod:
//			init_data();
			if(mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1)==1){
				if (RightsHelper.isHaveRight(RightsHelper.hp_addedit,mSharedPreferences) == true){
					intent.setClass(HpInfoActivity.this, ModifyHPActivity.class);
					intent.putExtra("hpid", hpid);
					startActivity(intent);
				}else{
					Toast.makeText(HpInfoActivity.this, "对不起，你没有的添加、修改货品的权限",
							Toast.LENGTH_SHORT).show();
				}
			}else{
				Toast.makeText(this, "离线模式下不能修改货品", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.ckkc_but:
			if(!ckkcListView.isShown()){
				ckkc_butBtn.setImageResource(R.drawable.up);
				ckkcListView.setVisibility(View.VISIBLE);
			}else{
				ckkcListView.setVisibility(View.GONE);
				ckkc_butBtn.setImageResource(R.drawable.down);
			}
			break;
		case R.id.piclayout:
			if(mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1)==1){
				intent.setClass(this, ModfiyPhotoActivity.class);
				intent.putExtra("hpid", hpid);
				startActivity(intent);
			}else if(mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1)==-1){
				intent.setClass(this, AddPhotoActivity.class);
				intent.putExtra("ImageNameList",(Serializable) dm.getTB_PIC_ALLUrlByhpid(hpid));
				startActivity(intent);
			}
			break;
		}

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO 自动生成的方法存根
		super.onActivityResult(requestCode, resultCode, data);
	}
	 
	 
	Runnable refreshRunnable= new Runnable() {
		
		@Override
		public void run() {	
			Message msg=Message.obtain();
			//获取货品信息
			ls=WebserviceImport.GetHpInfo_byid(hpid, -1, str, str1,mSharedPreferences);
			if(ls!=null&&ls.size()!=0){
				String[] ss= new String[str.length];
				for(int i=0;i<str.length;i++){
					ss[i]=ls.get(0).get(str[i]).toString();
				}
				dm.del_hp(hpid);
				dm_op.insert_into(DataBaseHelper.TB_HP, str, ss);
			}
			//获取货品在各仓库库存数量
			ls2=WebserviceImport.GetCKKC(hpid, str2, str3,mSharedPreferences);
			if(ls2!=null&&ls2.size()!=0){
				for(int i=0;i<ls2.size();i++){//循环一次获取一个仓库的库存数量
					String[] ss=new String[str2.length];
					for(int j=0;j<str2.length;j++){
						ss[j]=ls2.get(i).get(str2[j]).toString();
					}
					dm_op.Del_CKKC(ls2.get(i).get(DataBaseHelper.ID).toString());
					dm_op.insert_into(DataBaseHelper.TB_CKKC, str2, ss);
				}
			}
			msg.what=1;
			mHandler.sendMessage(msg);
		}
	};
	
	Runnable modifyHPTMRunnable = new Runnable() {
		
		@Override
		public void run() {
			// TODO 自动生成的方法存根
			Message msg = Message.obtain();
			String resultString=WebserviceImport_more.edit_tb_hp_HPTBM(Integer.parseInt(hpid), myEditText.getText().toString(), mSharedPreferences);
			msg.obj=resultString;
			msg.what = 2;
			mHandler.sendMessage(msg);
		}
	};
	
	private Handler mHandler=new Handler(){
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 1:
				if(!ls.isEmpty()){
					pro_dialog.dismiss();
					setTextView();
				}else if(ls.isEmpty()){
					pro_dialog.dismiss();
					Toast.makeText(HpInfoActivity.this, "获取数据失败,可能没有该货品的信息", Toast.LENGTH_SHORT).show();
				}
				if(!ls2.isEmpty()){
					pro_dialog.dismiss();
					setCKKC();
				}
				break;
			case 2:
				pro_dialog.dismiss();
				if(msg.obj.toString().equals("修改成功")){
					tiaomaTextView.setText(myEditText.getText().toString());
					Toast.makeText(HpInfoActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
				}else if(msg.obj.toString().equals("货品条码重复")){
					Toast.makeText(HpInfoActivity.this, msg.obj.toString()+",请重新修改", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(HpInfoActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
				}
				break;
			}
		};
	};
	
	public void modifyHPTM(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LinearLayout.LayoutParams scanButtonlp = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		LinearLayout.LayoutParams EditTextlp = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		layout.setGravity(Gravity.CENTER);
		scanButtonlp.setMargins(25, 10, 15, 10);
		scanButtonlp.gravity = Gravity.CENTER;
		EditTextlp.setMargins(10, 10, 10, 10);
		EditTextlp.gravity = Gravity.CENTER;
		ImageButton scanButton = new ImageButton(this);
		scanButton.setLayoutParams(scanButtonlp);
		scanButton.setBackgroundResource(R.drawable.scanblack);
		scanButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent intent = new Intent();
				intent.setClass(HpInfoActivity.this, CaptureActivity.class);
				intent.putExtra("SearchPopupWindow_tm", true);
				startActivity(intent);
			}
		});
		myEditText = new EditText(this);
		myEditText.setLayoutParams(EditTextlp);
		myEditText.setBackgroundResource(R.drawable.dare_edittext);
		myEditText.setPadding(10, 10, 10, 10);
		CheckEditWatcher cedw = new CheckEditWatcher();
		myEditText.addTextChangedListener(cedw);
		layout.addView(scanButton);
		layout.addView(myEditText);
		builder.setTitle("修改货品条码");
		builder.setView(layout);
		builder.setNegativeButton("取消修改",
				new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO 自动生成的方法存根
						dialog.dismiss();
					}
				});
		builder.setPositiveButton("确认",
				new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO 自动生成的方法存根
						pro_dialog = ProgressDialog.show(HpInfoActivity.this,null, "正在修改条码");
						new Thread(modifyHPTMRunnable).start();
						dialog.dismiss();
					}
				});
		builder.create().show();
	}
	
	public void onEventMainThread(ObjectThree Obj) {
		if (Obj.getMsg() != null) {
			myEditText.setText(Obj.getMsg());
		}
	}
	
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}
	
	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}
	
}
