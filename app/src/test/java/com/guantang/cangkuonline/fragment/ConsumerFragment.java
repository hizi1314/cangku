package com.guantang.cangkuonline.fragment;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.activity.AddVisitActivity;
import com.guantang.cangkuonline.activity.CustomerListActivity;
import com.guantang.cangkuonline.application.MyApplication;
import com.guantang.cangkuonline.helper.RightsHelper;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

public class ConsumerFragment extends Fragment implements OnClickListener{
	
	private LinearLayout customerLayout,supplierLayout,addvisitorLayout;
	private TextView coverView;
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
		View view = inflater.inflate(R.layout.consumerfragment_layout, null);
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onActivityCreated(savedInstanceState);
		customerLayout = (LinearLayout) getView().findViewById(R.id.customerLayout);
		supplierLayout = (LinearLayout) getView().findViewById(R.id.supplierLayout);
		addvisitorLayout = (LinearLayout) getView().findViewById(R.id.addvisitorLayout);
		coverView = (TextView) getView().findViewById(R.id.coverView);
		
		
		
		if(MyApplication.getInstance().getSharedPreferences().getInt(ShareprefenceBean.IS_LOGIN, -1)==1){
			customerLayout.setOnClickListener(this);
			supplierLayout.setOnClickListener(this);
			addvisitorLayout.setOnClickListener(this);
			coverView.setOnClickListener(this);
        }else if(MyApplication.getInstance().getSharedPreferences().getInt(ShareprefenceBean.IS_LOGIN, -1)==-1){
        	Animation translate_animation = AnimationUtils.loadAnimation(context, R.anim.push_left_in);
        	coverView.setVisibility(View.VISIBLE);
        	coverView.startAnimation(translate_animation);
        }
	}
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		Intent intent= new Intent();
		switch(v.getId()){
		case R.id.customerLayout:
			if(RightsHelper.isHaveRight(RightsHelper.dw_view_KH, MyApplication.getInstance().getSharedPreferences())){
				intent.setClass(context, CustomerListActivity.class);
				intent.putExtra("dwType", "1");//1:客户，2:供应商，3:即是客户也是供应商
				startActivity(intent);
			}else{
				Toast.makeText(context, "你没有查看客户权限", Toast.LENGTH_SHORT).show();
			}
			
			break;
		case R.id.supplierLayout:
			if(RightsHelper.isHaveRight(RightsHelper.dw_view_GYS, MyApplication.getInstance().getSharedPreferences())){
				intent.setClass(context, CustomerListActivity.class);
				intent.putExtra("dwType", "2");
				startActivity(intent);
			}else{
				Toast.makeText(context, "你没有查看供应商权限", Toast.LENGTH_SHORT).show();
			}
			
			break;
		case R.id.addvisitorLayout:
			intent.setClass(context, AddVisitActivity.class);
			startActivity(intent);
			break;
		}
	}
}
