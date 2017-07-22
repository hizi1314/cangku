package com.guantang.cangkuonline.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
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

public class Moved_DetailsAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private Context context;
	private int op_type;
	private List<Map<String, Object>> mList = new ArrayList<Map<String,Object>>();
	
	public Moved_DetailsAdapter(Context context,int op_type){
		this.context = context;
		this.op_type = op_type;
		inflater = LayoutInflater.from(context);
	}
	public void setData(List<Map<String, Object>> list){
		mList=list;
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO 自动生成的方法存根
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO 自动生成的方法存根
		return position;
	}
	
	
	
	@Override
	public int getItemViewType(int position) {
		// TODO 自动生成的方法存根
		if(op_type==1||op_type==2){
			return 1;//有财务计算
		}else if(op_type==0){
			return 0;//没有财务计算
		}
		return super.getItemViewType(position);
	}
	@Override
	public int getViewTypeCount() {
		// TODO 自动生成的方法存根
		return 2;
	}
	
	public View getckruView(int position, View convertView, ViewGroup parent) {
		// TODO 自动生成的方法存根
		ViewHolder holder=null;
		if(convertView==null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.djdetail_item, null);
			holder.bmTextView = (TextView) convertView.findViewById(R.id.bm);
			holder.mcTextView = (TextView) convertView.findViewById(R.id.mc);
			holder.ggTextView = (TextView) convertView.findViewById(R.id.gg);
			holder.jldwTextView = (TextView) convertView.findViewById(R.id.jldw);
			holder.numTextView = (TextView) convertView.findViewById(R.id.num);
			holder.djTextView = (TextView) convertView.findViewById(R.id.dj);
			holder.zjTextView = (TextView) convertView.findViewById(R.id.zj);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		final Map<String, Object> map = mList.get(position);
		
		Object hpbmObject = map.get(DataBaseHelper.HPBM);
		holder.bmTextView.setText(hpbmObject==null?"":hpbmObject.toString());
		
		Object hpmcObject = map.get(DataBaseHelper.HPMC);
		holder.mcTextView.setText(hpmcObject==null?"":hpmcObject.toString());
		
		Object ggxhObject = map.get(DataBaseHelper.GGXH);
		holder.ggTextView.setText(ggxhObject==null?"":ggxhObject.toString());
		
		Object jldwObject = map.get(DataBaseHelper.JLDW);
		holder.jldwTextView.setText(jldwObject==null?"":jldwObject.toString());
		
		Object mslObject = map.get(DataBaseHelper.MSL);
		holder.numTextView.setText(mslObject==null?"":mslObject.toString());
		
		Object djObject = map.get(DataBaseHelper.DJ);
		holder.djTextView.setText(djObject==null?"":djObject.toString());
		
		Object zjObject = map.get(DataBaseHelper.ZJ);
		holder.zjTextView.setText(zjObject==null?"":zjObject.toString());
		
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
				for(int i=0;i<mList.size();i++){
					mList.get(i).put("click_Color", false);//true 是点击之后的颜色  红色，false没有点击的颜色  白色
				}
				map.put("click_Color",true);
				notifyDataSetChanged();
			}
		});	
		convertView.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO 自动生成的方法存根
				for(int i=0;i<mList.size();i++){
					mList.get(i).put("click_Color", false);//true 是点击之后的颜色  红色，false没有点击的颜色  白色
				}
				map.put("click_Color",true);
				notifyDataSetChanged();
				Intent intent = new Intent(context, MX_InfoActivity.class);
				
					
				intent.putExtra(DataBaseHelper.MVDH, map.get(DataBaseHelper.MVDH).toString());
				intent.putExtra(DataBaseHelper.MVType,map.get(DataBaseHelper.MVType).toString());
				intent.putExtra(DataBaseHelper.MVDDATE, map.get(DataBaseHelper.MVDT).toString());
				intent.putExtra(DataBaseHelper.MSL, map.get(DataBaseHelper.MSL).toString());
				intent.putExtra(DataBaseHelper.DJ, map.get(DataBaseHelper.DJ).toString());
				intent.putExtra(DataBaseHelper.ZJ, map.get(DataBaseHelper.ZJ).toString());
				intent.putExtra(DataBaseHelper.DepName, map.get(DataBaseHelper.DepName).toString());
				intent.putExtra(DataBaseHelper.DWName, map.get(DataBaseHelper.DWName).toString());
				intent.putExtra(DataBaseHelper.JBR, map.get(DataBaseHelper.JBR).toString());
				intent.putExtra(DataBaseHelper.HPMC, map.get(DataBaseHelper.HPMC).toString());
				intent.putExtra(DataBaseHelper.HPBM, map.get(DataBaseHelper.HPBM).toString());
				intent.putExtra(DataBaseHelper.GGXH, map.get(DataBaseHelper.GGXH).toString());
				intent.putExtra(DataBaseHelper.JLDW, map.get(DataBaseHelper.JLDW).toString());
				intent.putExtra(DataBaseHelper.LBS, map.get(DataBaseHelper.LBS).toString());
				intent.putExtra(DataBaseHelper.CKMC, map.get(DataBaseHelper.CKMC).toString());
				context.startActivity(intent);
				return false;
			}
		});
		return convertView;
	}
	
	public View getpdView(int position, View convertView, ViewGroup parent) {
		// TODO 自动生成的方法存根
		ViewHolder holder=null;
		if(convertView==null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.djdetail_item, null);
			holder.bmTextView = (TextView) convertView.findViewById(R.id.bm);
			holder.mcTextView = (TextView) convertView.findViewById(R.id.mc);
			holder.ggTextView = (TextView) convertView.findViewById(R.id.gg);
			holder.jldwTextView = (TextView) convertView.findViewById(R.id.jldw);
			holder.znumTextView = (TextView) convertView.findViewById(R.id.znum);
			holder.snumTextView = (TextView) convertView.findViewById(R.id.snum);
			holder.profitTextView = (TextView) convertView.findViewById(R.id.profit);
			holder.loseTextView = (TextView) convertView.findViewById(R.id.lose);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		final Map<String, Object> map = mList.get(position);
		
		Object hpbmObject = map.get(DataBaseHelper.HPBM);
		holder.bmTextView.setText(hpbmObject==null?"":hpbmObject.toString());
		
		Object hpmcObject = map.get(DataBaseHelper.HPMC);
		holder.mcTextView.setText(hpmcObject==null?"":hpmcObject.toString());
		
		Object ggxhObject = map.get(DataBaseHelper.GGXH);
		holder.ggTextView.setText(ggxhObject==null?"":ggxhObject.toString());
		
		Object jldwObject = map.get(DataBaseHelper.JLDW);
		holder.jldwTextView.setText(jldwObject==null?"":jldwObject.toString());
		
		Object btkcObject = map.get(DataBaseHelper.BTKC);
		holder.znumTextView.setText(map.get(DataBaseHelper.BTKC).toString());
		
		Object atkcObject = map.get(DataBaseHelper.ATKC);
		holder.snumTextView.setText(atkcObject==null?"":atkcObject.toString());
		
		Object profitObject = map.get("profit");
		holder.profitTextView.setText(profitObject==null?"":profitObject.toString());
		Object loseObject = map.get("lose");
		holder.loseTextView.setText(loseObject==null?"":profitObject.toString());
		
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
				for(int i=0;i<mList.size();i++){
					mList.get(i).put("click_Color", false);//true 是点击之后的颜色  红色，false没有点击的颜色  白色
				}
				map.put("click_Color",true);
				notifyDataSetChanged();
			}
		});	
		
		return convertView;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO 自动生成的方法存根
		if(getItemViewType(position)==1){
			getckruView(position, convertView, parent);
		}else if(getItemViewType(position)==0){
			getpdView(position, convertView, parent);
		}
		return convertView;
	}
	class ViewHolder{
		TextView bmTextView,mcTextView,ggTextView,jldwTextView,numTextView,djTextView,zjTextView,
				znumTextView,snumTextView,profitTextView,loseTextView;
	}
}
