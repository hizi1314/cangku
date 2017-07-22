package com.guantang.cangkuonline.fragment;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.activity.AddPhotoActivity;
import com.guantang.cangkuonline.activity.HpInfoActivity;
import com.guantang.cangkuonline.activity.HpInformationActivity;
import com.guantang.cangkuonline.activity.ModfiyPhotoActivity;
import com.guantang.cangkuonline.application.MyApplication;
import com.guantang.cangkuonline.database.DataBaseCheckMethod;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.database.DataBaseMethod;
import com.guantang.cangkuonline.database.DataBaseOperateMethod;
import com.guantang.cangkuonline.helper.DecimalsHelper;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;
import com.guantang.cangkuonline.webservice.WebserviceImport;

public class HpInformationFragment extends Fragment implements OnClickListener{

	private TextView modTextView, tiaomaTextView, bianmaTextView,
			mingchengTextView, xinghaoTextView, typeTextVIew, kcjeTextView,
			kucunTextView, kucunTextView2, danweiTextView2, jldwTextView,
			shangxianTextView, xiaxianText, sccsTextView, beizhuTextView,
			picTextView, rkckjTextView, ckckjTextVIew, ckckjTextView2,
			bignumTextView, res1_TextView, res2_TextView, res3_TextView,
			res4_TextView, res5_TextView, res6_TextView, res1_text, res2_text,
			res3_text, res4_text, res5_text, res6_text;
	private LinearLayout picLayout;
	private String hpid = "";
	private List<Map<String,Object>> resList = new ArrayList<Map<String,Object>>();
	private List<Map<String,Object>> hpList = new ArrayList<Map<String,Object>>();
	private String[] str={DataBaseHelper.ID,DataBaseHelper.HPMC,DataBaseHelper.HPBM,DataBaseHelper.HPTM,DataBaseHelper.GGXH,
			DataBaseHelper.CurrKC,DataBaseHelper.JLDW,DataBaseHelper.HPSX,DataBaseHelper.HPXX,
			DataBaseHelper.SCCS,DataBaseHelper.BZ,DataBaseHelper.RKCKJ,DataBaseHelper.CKCKJ,
			DataBaseHelper.CKCKJ2,DataBaseHelper.JLDW2,DataBaseHelper.BigNum,DataBaseHelper.RES1,
			DataBaseHelper.RES2,DataBaseHelper.RES3,DataBaseHelper.RES4,DataBaseHelper.RES5,DataBaseHelper.RES6,
			DataBaseHelper.LBS,DataBaseHelper.KCJE,DataBaseHelper.ImagePath};
	private String[] str1={"id","HPMC","HPBM","HPTBM","GGXH","CurrKc","JLDW","HPSX","HPXX","SCCS","BZ","RKCKJ","CKCKJ",
			"CKCKJ2","JLDW2","BigNum","res1","res2","res3","res4","res5","res6","LBS","kcje","ImageUrl"};
	private DataBaseCheckMethod dm_ck;
	private DataBaseMethod dm;
	private DataBaseOperateMethod dm_op;
	private SharedPreferences mSharedPreferences;
	private ProgressDialog pro_dialog;
	private Context context;

	public static HpInformationFragment getInstance(String hpid) {
		HpInformationFragment HpIn = new HpInformationFragment();
		Bundle bundle = new Bundle();
		bundle.putString("hpid", hpid);
		HpIn.setArguments(bundle);
		return HpIn;
	}

	private HpInformationFragment() {
		super();
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO 自动生成的方法存根
		super.onAttach(activity);
		context = activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		hpid = getArguments().getString("hpid");
		dm_ck = new DataBaseCheckMethod(context);
		dm = new DataBaseMethod(context);
		dm_op=new DataBaseOperateMethod(context);
		mSharedPreferences = MyApplication.getInstance().getSharedPreferences();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		View view = inflater.inflate(R.layout.hpinformationfragment, null);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onActivityCreated(savedInstanceState);
		initView();
		init();
	}
	
	public void initView(){
		modTextView = (TextView) getView().findViewById(R.id.mod);
		tiaomaTextView = (TextView) getView().findViewById(R.id.tm);
		bianmaTextView = (TextView) getView().findViewById(R.id.bm);
		jldwTextView = (TextView) getView().findViewById(R.id.jldw);
		mingchengTextView = (TextView) getView().findViewById(R.id.name);
		xinghaoTextView = (TextView) getView().findViewById(R.id.gg);
		typeTextVIew = (TextView) getView().findViewById(R.id.lb);
		kcjeTextView = (TextView) getView().findViewById(R.id.kcje);
		kucunTextView = (TextView) getView().findViewById(R.id.kc);
		kucunTextView2 = (TextView) getView().findViewById(R.id.kc2);
		danweiTextView2 = (TextView) getView().findViewById(R.id.dw2);
		picTextView = (TextView) getView().findViewById(R.id.pic);
		shangxianTextView = (TextView) getView().findViewById(R.id.sx);
		xiaxianText = (TextView) getView().findViewById(R.id.xx);
		sccsTextView = (TextView) getView().findViewById(R.id.sccs);
		beizhuTextView = (TextView) getView().findViewById(R.id.bz);
		rkckjTextView = (TextView) getView().findViewById(R.id.rkckj);
		ckckjTextVIew = (TextView) getView().findViewById(R.id.ckckj);
		ckckjTextView2 = (TextView) getView().findViewById(R.id.ckckj2);
		bignumTextView = (TextView) getView().findViewById(R.id.bignum);
		res1_TextView = (TextView) getView().findViewById(R.id.res1);
		res2_TextView = (TextView) getView().findViewById(R.id.res2);
		res3_TextView = (TextView) getView().findViewById(R.id.res3);
		res4_TextView = (TextView) getView().findViewById(R.id.res4);
		res5_TextView = (TextView) getView().findViewById(R.id.res5);
		res6_TextView = (TextView) getView().findViewById(R.id.res6);
		res1_text = (TextView) getView().findViewById(R.id.res1_text);
		res2_text = (TextView) getView().findViewById(R.id.res2_text);
		res3_text = (TextView) getView().findViewById(R.id.res3_text);
		res4_text = (TextView) getView().findViewById(R.id.res4_text);
		res5_text = (TextView) getView().findViewById(R.id.res5_text);
		res6_text = (TextView) getView().findViewById(R.id.res6_text);
		picLayout = (LinearLayout) getView().findViewById(R.id.piclayout);
		picLayout.setOnClickListener(this);
		
		resList = new ArrayList<Map<String, Object>>();
		resList = dm_ck.Gt_Res();
		if (resList.size() != 0) {
			setRes(resList);
		}
	}
	
	public void init(){
		if(mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1)==1){
			if(WebserviceImport.isOpenNetwork(context)){
				pro_dialog = ProgressDialog.show(context,null, "正在从服务端加载数据");
				pro_dialog.setCancelable(false);
				new Thread(refreshRunnable).start();
			}else{
				Toast.makeText(context, "网络未连接", Toast.LENGTH_SHORT).show();
			}
		}else if(mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1)==-1){
			hpList=dm.Gethp(str,hpid);
			if(!hpList.isEmpty()){
				setTextView();
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
		Map<String,Object> map = hpList.get(0);
		if (map.get(DataBaseHelper.HPSX) == null || map.get(DataBaseHelper.HPSX).equals("")) {
			Hpsx = "";
		}  else {
			Hpsx = DecimalsHelper.Transfloat(Double.parseDouble((String) map.get(DataBaseHelper.HPSX)),MyApplication.getInstance().getNumPoint());
		}
		
		if (map.get(DataBaseHelper.HPXX) == null || map.get(DataBaseHelper.HPXX).equals("")) {
			Hpxx = "";
		} else {
			Hpxx = DecimalsHelper.Transfloat(Double.parseDouble((String) map.get(DataBaseHelper.HPXX)),MyApplication.getInstance().getNumPoint());
		}

		if (map.get(DataBaseHelper.BigNum) != null) {
			bignum = (String) map.get(DataBaseHelper.BigNum);
			if (!bignum.equals("") && Float.parseFloat(bignum)!=0) {
				bignumTextView.setText(DecimalsHelper.Transfloat(Double.parseDouble(bignum),MyApplication.getInstance().getNumPoint()));
				try {
					f = Float.parseFloat(bignum);
					num = Float.parseFloat((String) map.get(
							DataBaseHelper.CurrKC));
					BigDecimal b1 = new BigDecimal(Float.toString(f));
					BigDecimal b2 = new BigDecimal(Float.toString(num));
					
					num2 = DecimalsHelper.Transfloat(b2.divide(b1, 4, RoundingMode.HALF_UP).doubleValue(),MyApplication.getInstance().getNumPoint());
				} catch (Exception e) {
					num2 = "";
					Toast.makeText(context, "换算比例错误", Toast.LENGTH_SHORT).show();
				}
			}
		}
		
		kucunTextView2.setText(num2);
		tiaomaTextView.setText((String) map.get(DataBaseHelper.HPTM));
		bianmaTextView.setText((String) map.get(DataBaseHelper.HPBM));
		mingchengTextView.setText((String) map.get(DataBaseHelper.HPMC));
		xinghaoTextView.setText((String) map.get(DataBaseHelper.GGXH));
		if(!map.get(DataBaseHelper.CurrKC).toString().equals("")&&map.get(DataBaseHelper.CurrKC)!=null){
			kucunTextView.setText(DecimalsHelper.Transfloat(Double.parseDouble(map.get(DataBaseHelper.CurrKC).toString()),MyApplication.getInstance().getNumPoint())+" "+map.get(DataBaseHelper.JLDW));
		}
		
		typeTextVIew.setText((String) map.get(DataBaseHelper.LBS));
		jldwTextView.setText((String) map.get(DataBaseHelper.JLDW));
		((HpInformationActivity) context).setJldw_Lb(map.get(DataBaseHelper.JLDW).toString(), map.get(DataBaseHelper.LBS).toString());
		
		shangxianTextView.setText(Hpsx);
		xiaxianText.setText(Hpxx);
		if(null!=map.get(DataBaseHelper.KCJE) && !map.get(DataBaseHelper.KCJE).toString().equals("")){
			kcjeTextView.setText(DecimalsHelper.Transfloat(Double.parseDouble(map.get(DataBaseHelper.KCJE).toString()),MyApplication.getInstance().getJePoint()));
		}
		
		sccsTextView.setText((String) map.get(DataBaseHelper.SCCS));
		beizhuTextView.setText((String) map.get(DataBaseHelper.BZ));
		if(!map.get(DataBaseHelper.RKCKJ).toString().equals("")&&map.get(DataBaseHelper.RKCKJ)!=null){
			rkckjTextView.setText(DecimalsHelper.Transfloat(Double.parseDouble(map.get(DataBaseHelper.RKCKJ).toString()),MyApplication.getInstance().getDjPoint()));
		}
		if(!map.get(DataBaseHelper.CKCKJ).toString().equals("")&&map.get(DataBaseHelper.CKCKJ)!=null){
			ckckjTextVIew.setText(DecimalsHelper.Transfloat(Double.parseDouble(map.get(DataBaseHelper.CKCKJ).toString()),MyApplication.getInstance().getDjPoint()));
		}
		ckckjTextView2.setText((String) map.get(DataBaseHelper.CKCKJ2));
		danweiTextView2.setText((String) map.get(DataBaseHelper.JLDW2));
		res1_TextView.setText((String) map.get(DataBaseHelper.RES1));
		res2_TextView.setText((String) map.get(DataBaseHelper.RES2));
		res3_TextView.setText((String) map.get(DataBaseHelper.RES3));
		res4_TextView.setText((String) map.get(DataBaseHelper.RES4));
		res5_TextView.setText((String) map.get(DataBaseHelper.RES5));
		res6_TextView.setText((String) map.get(DataBaseHelper.RES6));
	}
	
	Runnable refreshRunnable= new Runnable() {
		
		@Override
		public void run() {	
			Message msg=Message.obtain();
			//获取货品信息
			hpList=WebserviceImport.GetHpInfo_byid(hpid, -1, str, str1,mSharedPreferences);
			if(hpList!=null&&hpList.size()!=0){
				String[] ss= new String[str.length];
				for(int i=0;i<str.length;i++){
					ss[i]=hpList.get(0).get(str[i]).toString();
				}
				dm.del_hp(hpList.get(0).get("id").toString());
				dm_op.insert_into(DataBaseHelper.TB_HP, str, ss);
			}
			msg.what=1;
			mHandler.sendMessage(msg);
		}
	};
	private Handler mHandler=new Handler(){
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 1:
				if(!hpList.isEmpty()){
					pro_dialog.dismiss();
					setTextView();
				}else if(hpList.isEmpty()){
					pro_dialog.dismiss();
					Toast.makeText(context, "获取数据失败,可能没有该货品的信息", Toast.LENGTH_SHORT).show();
				}
				break;
			}
		};
	};

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		Intent intent = new Intent();
		switch(v.getId()){
		case R.id.piclayout:
			if(mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1)==1){
				intent.setClass(context, ModfiyPhotoActivity.class);
				intent.putExtra("hpid", hpid);
				startActivity(intent);
			}else if(mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1)==-1){
				intent.setClass(context, AddPhotoActivity.class);
				intent.putExtra("ImageNameList",(Serializable) dm.getTB_PIC_ALLUrlByhpid(hpid));
				startActivity(intent);
			}
			break;
		}
	}
}
