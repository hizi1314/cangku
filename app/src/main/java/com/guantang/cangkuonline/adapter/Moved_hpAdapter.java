package com.guantang.cangkuonline.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.activity.MX_InfoActivity;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.helper.RightsHelper;

public class Moved_hpAdapter extends BaseAdapter{
	private LayoutInflater inflater;
	private Context context;
	private SharedPreferences mSharedPreferences;
	private List<Map<String,Object>> mlist = new ArrayList<Map<String,Object>>();
	private String hpmc="",hpbm="",ggxh="",jldw="",lb="";
	
	public Moved_hpAdapter(Context context,SharedPreferences mSharedPreferences,String hpmc,String hpbm,String ggxh,String jldw,String lb){
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.mSharedPreferences = mSharedPreferences;
		this.hpmc=hpmc;
		this.hpbm=hpbm;
		this.ggxh=ggxh;
		this.jldw=jldw;
		this.lb=lb;
	}
	
	public void setData(List<Map<String,Object>> list){
		mlist = list;
		notifyDataSetChanged();
	}
	
	public void addData(){
		mlist.addAll(mlist);
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		return mlist.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO 自动生成的方法存根
		return mlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO 自动生成的方法存根
		return position;
	}
	
	@Override
	public int getItemViewType(int position) {
		// TODO 自动生成的方法存根
		if(RightsHelper.isHaveRight(RightsHelper.system_cw,mSharedPreferences)){
			return 1;//有财务计算
		}else {
			return 0;//没有财务计算
		}
	}
	
	@Override
	public int getViewTypeCount() {
		// TODO 自动生成的方法存根
		return 2;
	}
	
	public View getcwView(int position, View convertView, ViewGroup parent){
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.moved_hp_item, null);
			holder.depNameTextView = (TextView) convertView.findViewById(R.id.depName);
			holder.dhTextView = (TextView) convertView.findViewById(R.id.dh);
			holder.dateTextView = (TextView) convertView.findViewById(R.id.date);
			holder.opTextView = (TextView) convertView.findViewById(R.id.op);
			holder.typeTextView = (TextView) convertView.findViewById(R.id.type);
			holder.numTextView = (TextView) convertView.findViewById(R.id.num);
			holder.djTextView = (TextView) convertView.findViewById(R.id.dj);
			holder.zjTextView = (TextView) convertView.findViewById(R.id.zj);
			holder.dwTextView = (TextView) convertView.findViewById(R.id.dw);
			holder.jbrTextView = (TextView) convertView.findViewById(R.id.jbr);
			holder.ckTextView = (TextView) convertView.findViewById(R.id.ck);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		final Map<String, Object> map = mlist.get(position);
		
		Object mvdhObject = map.get(DataBaseHelper.MVDH);
		holder.dhTextView.setText(mvdhObject==null?"":mvdhObject.toString());

		Object mvdtObject = map.get(DataBaseHelper.MVDT);
		holder.dateTextView.setText(mvdtObject==null?"":mvdtObject.toString());
		
		Object mvtypeObject = map.get(DataBaseHelper.MVType);
		holder.typeTextView.setText(mvtypeObject==null?"":mvtypeObject.toString());

		Object mslObject = map.get(DataBaseHelper.MSL);
		holder.numTextView.setText(mslObject==null?"":mslObject.toString());

		Object djObject = map.get(DataBaseHelper.DJ);
		holder.djTextView.setText(djObject==null?"":djObject.toString());

		Object zjObject = map.get(DataBaseHelper.ZJ);
		holder.zjTextView.setText(zjObject==null?"":zjObject.toString());

		Object dwnameObject = map.get(DataBaseHelper.DWName);
		holder.dwTextView.setText(dwnameObject==null?"":dwnameObject.toString());

		Object jbrObject = map.get(DataBaseHelper.JBR);
		holder.jbrTextView.setText(jbrObject==null?"":jbrObject.toString());

		Object ckmcObject = map.get(DataBaseHelper.CKMC);
		holder.ckTextView.setText(ckmcObject==null?"":ckmcObject.toString());

		Object depnameObject = map.get(DataBaseHelper.DepName);
		holder.depNameTextView.setText(depnameObject==null?"":depnameObject.toString());
		
		String mvdirectString=map.get(DataBaseHelper.MVDirect).toString();
		if(mvdirectString.equals("1")){
			holder.opTextView.setText("入库");
		}else if(mvdirectString.equals("2")){
			holder.opTextView.setText("出库");
		}else{
			holder.opTextView.setText(mvdirectString);
		}
		
		if((position%2)==0){
			convertView.setBackgroundColor(Color.parseColor("#FFFFFF"));
		}else if((position%2)==1){
			convertView.setBackgroundColor(Color.parseColor("#E5E5E5"));
		}
		
		if((Boolean) map.get("click_Color")){
			convertView.setBackgroundColor(Color.parseColor("#80EE0000"));
		}
		
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				for(int i=0;i<mlist.size();i++){
					mlist.get(i).put("click_Color", (Boolean)false);//true 是点击之后的颜色  红色，false没有点击的颜色  白色
				}
				map.put("click_Color",(Boolean)true);
				notifyDataSetChanged();
			}
		});	
		
		convertView.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO 自动生成的方法存根
				for(int i=0;i<mlist.size();i++){
					mlist.get(i).put("click_Color", (Boolean)false);//true 是点击之后的颜色  红色，false没有点击的颜色  白色
				}
				map.put("click_Color",(Boolean)true);
				notifyDataSetChanged();
				Intent intent = new Intent(context, MX_InfoActivity.class);
				
				intent.putExtra(DataBaseHelper.MVDH, map.get(DataBaseHelper.MVDH).toString());
				intent.putExtra(DataBaseHelper.MVType,map.get(DataBaseHelper.MVType).toString());		
				intent.putExtra(DataBaseHelper.MVDDATE, map.get(DataBaseHelper.MVDT).toString());
				intent.putExtra(DataBaseHelper.MSL, map.get(DataBaseHelper.MSL).toString());
				intent.putExtra(DataBaseHelper.DJ, map.get(DataBaseHelper.DJ).toString());
				intent.putExtra(DataBaseHelper.DepName, map.get(DataBaseHelper.DepName).toString());
				intent.putExtra(DataBaseHelper.DWName, map.get(DataBaseHelper.DWName).toString());				
				intent.putExtra(DataBaseHelper.JBR, map.get(DataBaseHelper.JBR).toString());
				intent.putExtra(DataBaseHelper.CKMC, map.get(DataBaseHelper.CKMC).toString());
				
				intent.putExtra(DataBaseHelper.HPMC, hpmc);
				intent.putExtra(DataBaseHelper.HPBM, hpbm);
				intent.putExtra(DataBaseHelper.GGXH, ggxh);
				intent.putExtra(DataBaseHelper.JLDW, jldw);
				intent.putExtra(DataBaseHelper.LBS, lb);
				
				context.startActivity(intent);
				return false;
			}
		});
		
		return convertView;
	}
	
	public View getnocwView(int position, View convertView, ViewGroup parent){
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.moved_hp_item2, null);
			holder.depNameTextView = (TextView) convertView.findViewById(R.id.depName);
			holder.dhTextView = (TextView) convertView.findViewById(R.id.dh);
			holder.dateTextView = (TextView) convertView.findViewById(R.id.date);
			holder.opTextView = (TextView) convertView.findViewById(R.id.op);
			holder.typeTextView = (TextView) convertView.findViewById(R.id.type);
			holder.numTextView = (TextView) convertView.findViewById(R.id.num);
			holder.dwTextView = (TextView) convertView.findViewById(R.id.dw);
			holder.jbrTextView = (TextView) convertView.findViewById(R.id.jbr);
			holder.ckTextView = (TextView) convertView.findViewById(R.id.ck);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		final Map<String, Object> map = mlist.get(position);

		Object mvdhObject = map.get(DataBaseHelper.MVDH);
		holder.dhTextView.setText(mvdhObject==null?"":mvdhObject.toString());
		
		Object mvdtObject = map.get(DataBaseHelper.MVDT);
		holder.dateTextView.setText(mvdtObject==null?"":mvdtObject.toString());
		
		Object mvtypeObject = map.get(DataBaseHelper.MVType);
		holder.typeTextView.setText(mvtypeObject==null?"":mvtypeObject.toString());
		
		Object mslObject = map.get(DataBaseHelper.MSL);
		holder.numTextView.setText(mslObject==null?"":mslObject.toString());
		
		Object dwnameObject = map.get(DataBaseHelper.DWName);
		holder.dwTextView.setText(dwnameObject==null?"":dwnameObject.toString());
		
		Object jbrObject = map.get(DataBaseHelper.JBR);
		holder.jbrTextView.setText(jbrObject==null?"":jbrObject.toString());
		
		Object ckmcObject = map.get(DataBaseHelper.CKMC);
		holder.ckTextView.setText(ckmcObject==null?"":ckmcObject.toString());
		
		Object depnameObject = map.get(DataBaseHelper.DepName);
		holder.depNameTextView.setText(depnameObject==null?"":depnameObject.toString());
		
		String mvdirectString=map.get(DataBaseHelper.MVDirect).toString();
		if(mvdirectString.equals("1")){
			holder.opTextView.setText("入库");
		}else if(mvdirectString.equals("2")){
			holder.opTextView.setText("出库");
		}else{
			holder.opTextView.setText(mvdirectString);
		}
		
		if((position%2)==0){
			convertView.setBackgroundColor(Color.parseColor("#FFFFFF"));
		}else if((position%2)==1){
			convertView.setBackgroundColor(Color.parseColor("#E5E5E5"));
		}
		
		if((Boolean) map.get("click_Color")){
			convertView.setBackgroundColor(Color.parseColor("#80EE0000"));
		}
		
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				for(int i=0;i<mlist.size();i++){
					mlist.get(i).put("click_Color", (Boolean)false);//true 是点击之后的颜色  红色，false没有点击的颜色  白色
				}
				map.put("click_Color",true);
				notifyDataSetChanged();
			}
		});	
		convertView.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO 自动生成的方法存根
				for(int i=0;i<mlist.size();i++){
					mlist.get(i).put("click_Color", (Boolean)false);//true 是点击之后的颜色  红色，false没有点击的颜色  白色
				}
				map.put("click_Color",(Boolean)true);
				notifyDataSetChanged();
				Intent intent = new Intent(context, MX_InfoActivity.class);
				intent.putExtra(DataBaseHelper.MVDH, map.get(DataBaseHelper.MVDH).toString());
				intent.putExtra(DataBaseHelper.MVType,map.get(DataBaseHelper.MVType).toString());
				intent.putExtra(DataBaseHelper.MVDDATE, map.get(DataBaseHelper.MVDT).toString());
				intent.putExtra(DataBaseHelper.MSL, map.get(DataBaseHelper.MSL).toString());
				intent.putExtra(DataBaseHelper.DepName, map.get(DataBaseHelper.DepName).toString());
				intent.putExtra(DataBaseHelper.DWName, map.get(DataBaseHelper.DWName).toString());
				intent.putExtra(DataBaseHelper.JBR, map.get(DataBaseHelper.JBR).toString());
				
				intent.putExtra(DataBaseHelper.HPMC, hpmc);
				intent.putExtra(DataBaseHelper.HPBM, hpbm);
				intent.putExtra(DataBaseHelper.GGXH, ggxh);
				intent.putExtra(DataBaseHelper.JLDW, jldw);
				intent.putExtra(DataBaseHelper.LBS, lb);
				intent.putExtra(DataBaseHelper.CKMC, map.get(DataBaseHelper.CKMC).toString());
				context.startActivity(intent);
				return false;
			}
		});
		return convertView;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO 自动生成的方法存根
		if(getItemViewType(position)==1){
			return getcwView(position, convertView, parent);
		}else if(getItemViewType(position)==0){
			return getnocwView(position, convertView, parent);
		}
		return convertView;
	}
	class ViewHolder{
		TextView dhTextView,dateTextView,opTextView,typeTextView,numTextView,djTextView,zjTextView,
				dwTextView,jbrTextView,ckTextView,depNameTextView;
	}
	
	
	public void setJldwAndLb(String jldw,String lb){
		if(jldw!=null){
			this.jldw = jldw;
		}
		if(lb!=null){
			this.lb = lb;
		}
	}
}
