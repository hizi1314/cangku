package com.guantang.cangkuonline.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
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

public class TablelistAdapter2 extends BaseAdapter {
	private LayoutInflater inflater;
	private List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
	private Context context;
	public TablelistAdapter2(Context context){
		inflater=LayoutInflater.from(context);
		this.context = context;
	}
	
	public void setData(List<Map<String,Object>> list){
		this.list=list;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO 自动生成的方法存根
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO 自动生成的方法存根
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO 自动生成的方法存根
		ViewHolder holder;
		if(convertView==null){
			holder=new ViewHolder();
			convertView=inflater.inflate(R.layout.moved_dj_item2, null);
			holder.dhTextView=(TextView) convertView.findViewById(R.id.dh);
			holder.typeTextView=(TextView) convertView.findViewById(R.id.type);
			holder.dateTextView=(TextView) convertView.findViewById(R.id.date);
			holder.numTextView=(TextView) convertView.findViewById(R.id.num);
			holder.depTextView=(TextView) convertView.findViewById(R.id.dep);
			holder.dwTextView=(TextView) convertView.findViewById(R.id.dw);
			holder.jbrTextView=(TextView) convertView.findViewById(R.id.jbr);
			holder.mcTextView=(TextView) convertView.findViewById(R.id.mc);
			holder.bmTextView=(TextView) convertView.findViewById(R.id.bm);
			holder.ggTextView=(TextView) convertView.findViewById(R.id.gg);
			holder.jldwTextView=(TextView) convertView.findViewById(R.id.jldw);
			holder.lbTextView=(TextView) convertView.findViewById(R.id.lb);
			holder.kcTextView=(TextView) convertView.findViewById(R.id.kc);
			holder.ckTextView=(TextView) convertView.findViewById(R.id.ck);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();	
		}
		final Map<String,Object> map=list.get(position);
		
		Object mvdhObject = map.get(DataBaseHelper.MVDH);
		holder.dhTextView.setText(mvdhObject==null?"":mvdhObject.toString());

		Object mvtypeObject = map.get(DataBaseHelper.MVType);
		holder.typeTextView.setText(mvtypeObject==null?"":mvtypeObject.toString());

		Object mvddateObject = map.get(DataBaseHelper.MVDDATE);
		holder.dateTextView.setText(mvddateObject==null?"":mvddateObject.toString());

		Object mslObject = map.get(DataBaseHelper.MSL);
		holder.numTextView.setText(mslObject==null?"":mslObject.toString());
		
		Object depnameObject = map.get(DataBaseHelper.DepName);
		holder.depTextView.setText(depnameObject==null?"":depnameObject.toString());
		
		Object dwnameObject = map.get(DataBaseHelper.DWName);
		holder.dwTextView.setText(dwnameObject==null?"":dwnameObject.toString());

		Object jbrObject = map.get(DataBaseHelper.JBR);
		holder.jbrTextView.setText(jbrObject==null?"":jbrObject.toString());
		
		Object hpmcObject = map.get(DataBaseHelper.HPMC);
		holder.mcTextView.setText(hpmcObject==null?"":hpmcObject.toString());

		Object hpbmObject = map.get(DataBaseHelper.HPBM);
		holder.bmTextView.setText(hpbmObject==null?"":hpbmObject.toString());

		Object ggxhObject = map.get(DataBaseHelper.GGXH);
		holder.ggTextView.setText(ggxhObject==null?"":ggxhObject.toString());

		Object jldwObject = map.get(DataBaseHelper.JLDW);
		holder.jldwTextView.setText(jldwObject==null?"":jldwObject.toString());

		Object lbsObject = map.get(DataBaseHelper.LBS);
		holder.lbTextView.setText(lbsObject==null?"":lbsObject.toString());

		Object azkcObject = map.get(DataBaseHelper.AZKC);
		holder.kcTextView.setText(azkcObject==null?"":azkcObject.toString());	
		
		Object ckmcObject = map.get(DataBaseHelper.CKMC);
		holder.ckTextView.setText(ckmcObject==null?"":ckmcObject.toString());
		
		if((position%2)==0){
			convertView.setBackgroundColor(Color.parseColor("#FFFFFF"));
		}else if((position%2)==1){
			convertView.setBackgroundColor(Color.parseColor("#F0F0F0"));
		}
		if((Boolean) map.get("click_Color")){
			convertView.setBackgroundColor(Color.parseColor("#80EE0000"));
		}
		
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				//true 是点击之后的颜色  红色，false没有点击的颜色  白色
				ListIterator<Map<String, Object>> it=list.listIterator();
				while(it.hasNext()){
					Map<String, Object> map=it.next();
					map.put("click_Color", (Boolean)false);
					it.set(map);
				}
				map.put("click_Color",(Boolean)true);
				notifyDataSetChanged();
			}
		});	
		convertView.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				ListIterator<Map<String, Object>> it=list.listIterator();
				while(it.hasNext()){
					Map<String, Object> map=it.next();
					map.put("click_Color", (Boolean)false);
					it.set(map);
				}
				map.put("click_Color",(Boolean)true);
				notifyDataSetChanged();
				
				Intent intent = new Intent(context, MX_InfoActivity.class);
				intent.putExtra(DataBaseHelper.MVDH, map.get(DataBaseHelper.MVDH).toString());
				intent.putExtra(DataBaseHelper.MVType,map.get(DataBaseHelper.MVType).toString());
				intent.putExtra(DataBaseHelper.MVDDATE, map.get(DataBaseHelper.MVDDATE).toString());
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
				intent.putExtra(DataBaseHelper.AZKC, map.get(DataBaseHelper.AZKC).toString());
				intent.putExtra(DataBaseHelper.CKMC, map.get(DataBaseHelper.CKMC).toString());
				intent.putExtra(DataBaseHelper.BZ, map.get(DataBaseHelper.BZ).toString());
				context.startActivity(intent);
				return false;
			}
		});
		
		return convertView;
	}
	class ViewHolder{
		TextView dhTextView;
		TextView typeTextView;
		TextView dateTextView;
		TextView numTextView;
		TextView depTextView;
		TextView dwTextView;
		TextView jbrTextView;
		TextView mcTextView;
		TextView bmTextView;
		TextView ggTextView;
		TextView jldwTextView;
		TextView lbTextView;
		TextView kcTextView;
		TextView ckTextView;
	}
}
