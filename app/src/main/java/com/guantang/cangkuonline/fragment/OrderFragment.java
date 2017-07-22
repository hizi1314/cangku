package com.guantang.cangkuonline.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.activity.AddOrderActivity;
import com.guantang.cangkuonline.activity.MyOrderActivity;
import com.guantang.cangkuonline.activity.SomeOneTypeOrderActivity;
import com.guantang.cangkuonline.application.MyApplication;
import com.guantang.cangkuonline.helper.RightsHelper;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;
import com.guantang.cangkuonline.webservice.WebserviceImport;
import com.guantang.cangkuonline.webservice.WebserviceImport_more;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class OrderFragment extends Fragment implements OnClickListener{
	
	private TextView today_orderTxtView,notcheckTxtView,theReturnTxtView,completeTxtView,coverView;
	private LinearLayout cgorderLayout,xsorderLayout,myorderLayout;
	private Context context;
	
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
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		View view = inflater.inflate(R.layout.orderfragment_layout, null);
		
		today_orderTxtView = (TextView) view.findViewById(R.id.today_order);
		notcheckTxtView = (TextView) view.findViewById(R.id.notcheck);
		theReturnTxtView = (TextView) view.findViewById(R.id.theReturn);
		completeTxtView = (TextView) view.findViewById(R.id.complete);
		cgorderLayout = (LinearLayout) view.findViewById(R.id.cgorderLayout);
		xsorderLayout = (LinearLayout) view.findViewById(R.id.xsorderLayout);
		myorderLayout = (LinearLayout) view.findViewById(R.id.myorderLayout);
		coverView = (TextView) view.findViewById(R.id.coverView);
		
		
		
		if(MyApplication.getInstance().getSharedPreferences().getInt(ShareprefenceBean.IS_LOGIN, -1)==1){
			today_orderTxtView.setOnClickListener(this);
			notcheckTxtView.setOnClickListener(this);
			theReturnTxtView.setOnClickListener(this);
			completeTxtView.setOnClickListener(this);
			cgorderLayout.setOnClickListener(this);
			xsorderLayout.setOnClickListener(this);
			myorderLayout.setOnClickListener(this);
			coverView.setOnClickListener(this);
        }else if(MyApplication.getInstance().getSharedPreferences().getInt(ShareprefenceBean.IS_LOGIN, -1)==-1){
        	Animation translate_animation = AnimationUtils.loadAnimation(context, R.anim.push_left_in);
        	coverView.setVisibility(View.VISIBLE);
        	coverView.startAnimation(translate_animation);
        }
		return view;

	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public void onStart() {
		// TODO 自动生成的方法存根
		super.onStart();
		if(WebserviceImport.isOpenNetwork(context)){
			new DDNumInfoAsyncTask().execute();
		}else{
			Toast.makeText(context, "网络未连接", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		Intent intent = new Intent();
		switch(v.getId()){
		case R.id.today_order:
			intent.setClass(context, SomeOneTypeOrderActivity.class);
			intent.putExtra("myorderstart", 0);//0 今日订单，1未审核订单，2被驳回订单，3已完成订单
			startActivity(intent);
			break;
		case R.id.notcheck:
			intent.setClass(context, SomeOneTypeOrderActivity.class);
			intent.putExtra("myorderstart", 1);//0 今日订单，1未审核订单，2被驳回订单，3已完成订单
			startActivity(intent);
			break;
		case R.id.theReturn:
			intent.setClass(context, SomeOneTypeOrderActivity.class);
			intent.putExtra("myorderstart", 2);//0 今日订单，1未审核订单，2被驳回订单，3已完成订单
			startActivity(intent);
			break;
		case R.id.complete:
			intent.setClass(context, SomeOneTypeOrderActivity.class);
			intent.putExtra("myorderstart", 3);//0 今日订单，1未审核订单，2被驳回订单，3已完成订单
			startActivity(intent);
			break;
		case R.id.cgorderLayout:
			if(RightsHelper.isHaveRight(RightsHelper.dd_cg_add, MyApplication.getInstance().getSharedPreferences())){
				intent.setClass(context, AddOrderActivity.class);
				intent.putExtra("dirc", 0);
				startActivity(intent);
			}else{
				Toast.makeText(context, "你没有添加采购订单的权限", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.xsorderLayout:
			if(RightsHelper.isHaveRight(RightsHelper.dd_xs_add, MyApplication.getInstance().getSharedPreferences())){
				intent.setClass(context, AddOrderActivity.class);
				intent.putExtra("dirc", 1);
				startActivity(intent);
			}else{
				Toast.makeText(context, "你没有添加销售订单的权限", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.myorderLayout:
			intent.setClass(context, MyOrderActivity.class);
			intent.putExtra("myorderstart", 0);//0 跳转到MyOrderActivity显示第一个fragment,1跳转到MyOrderActivity显示第二个fragment，3跳转到MyOrderActivity显示第四个fragment，4跳转到MyOrderActivity显示第五个fragment；
			startActivity(intent);
			break;
		}
	}
	
	class DDNumInfoAsyncTask extends AsyncTask<Void, Void, String>{

		@Override
		protected String doInBackground(Void... params) {
			// TODO 自动生成的方法存根
			String jsonString = WebserviceImport_more.GetDDNumInfo();
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
					JSONObject dataJSONObject=jsonObject.getJSONObject("Data");
					today_orderTxtView.setText(dataJSONObject.getString("TodayDDInfo"));
					notcheckTxtView.setText(dataJSONObject.getString("NotAuditedInfo"));
					theReturnTxtView.setText(dataJSONObject.getString("RejectInfo"));
					completeTxtView.setText(dataJSONObject.getString("FinishedInfo"));
					break;
				case -1:
					Toast.makeText(context, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				case -2:
					Toast.makeText(context, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				default:
					Toast.makeText(context, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
					break;
				}
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
		
	}
	
}
