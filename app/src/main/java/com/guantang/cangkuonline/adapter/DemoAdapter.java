package com.guantang.cangkuonline.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DemoAdapter extends BaseExpandableListAdapter {
	private Context context;
	private List<Map<String, Object>> grouplist = new ArrayList<Map<String, Object>>();
	private List<List<Map<String, Object>>> childlist = new ArrayList<List<Map<String, Object>>>();
	public DemoAdapter(Context context){
		this.context=context;
	}
	public void setData(List<Map<String, Object>> ckmc_array,List<List<Map<String, Object>>> childList2){
		this.grouplist=ckmc_array;
		this.childlist=childList2;
		notifyDataSetChanged();
	}
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO 自动生成的方法存根
		return childlist.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO 自动生成的方法存根
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO 自动生成的方法存根
		LinearLayout linearLayout = new LinearLayout(context);
		linearLayout.setPadding(dp2px(40), dp2px(5), dp2px(0), dp2px(5));
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);
		
        TextView textView = new TextView(context);
        textView.setText(getGroup(groupPosition).toString());
        textView.setPadding(dp2px(5), dp2px(0), dp2px(0), dp2px(0));
        textView.setText(childlist.get(groupPosition).get(childPosition).get("ckmc").toString());
        textView.setTextSize(18);
        linearLayout.addView(textView);
		return linearLayout;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO 自动生成的方法存根
		return childlist.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO 自动生成的方法存根
		return grouplist.get(groupPosition)==null?"":grouplist.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		// TODO 自动生成的方法存根
		return grouplist.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO 自动生成的方法存根
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO 自动生成的方法存根
		LinearLayout linearLayout = new LinearLayout(context);
		linearLayout.setPadding(dp2px(10), dp2px(10), dp2px(0), dp2px(10));
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);
//		ImageView imgIndicator = new ImageView(context);
//		imgIndicator.setLayoutParams(new LinearLayout.LayoutParams(dp2px(20), dp2px(20)));
        TextView textView = new TextView(context);
        textView.setText(getGroup(groupPosition).toString());
        textView.setPadding(dp2px(15), dp2px(0), dp2px(0), dp2px(0));
        
        Object ckmcObject = grouplist.get(groupPosition).get("ckmc");
        textView.setText(ckmcObject==null?"":ckmcObject.toString());
        
        textView.setTextSize(20);
//        if(isExpanded){
//        	 imgIndicator.setBackgroundResource(R.drawable.arrow2);
//        }else{
//        	 imgIndicator.setBackgroundResource(R.drawable.arrow_2);
//        }
        
        linearLayout.addView(textView);
        
		return linearLayout;
	}

	@Override
	public boolean hasStableIds() {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO 自动生成的方法存根
		return true;
	}
	
	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				context.getResources().getDisplayMetrics());
	}
	
	private int sp2px(int sp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
				context.getResources().getDisplayMetrics());
	}
}
