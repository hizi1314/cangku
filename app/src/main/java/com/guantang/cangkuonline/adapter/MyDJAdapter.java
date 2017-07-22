package com.guantang.cangkuonline.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.database.DataBaseHelper;

public class MyDJAdapter extends BaseAdapter {
	private Context context;
	private List<Map<String,Object>> mlist = new ArrayList<Map<String,Object>>();
	private LayoutInflater inflater;
	
	public MyDJAdapter(Context context){
		this.context = context;
		inflater = LayoutInflater.from(context);
	}
	
	public void setData(List<Map<String,Object>> list){
		mlist=list;
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
		if(Integer.parseInt(mlist.get(position).get(DataBaseHelper.DJType).toString())==1){
			return 1;
		}else if(Integer.parseInt(mlist.get(position).get(DataBaseHelper.DJType).toString())==-1){
			return -1;
		}else if(Integer.parseInt(mlist.get(position).get(DataBaseHelper.DJType).toString())==0){
			return 0;
		}
		return super.getItemViewType(position);
	}

	@Override
	public int getViewTypeCount() {
		// TODO 自动生成的方法存根
		return 3;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		if(convertView==null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_danjubendi, null);
			holder.Icon_TextView = (TextView) convertView.findViewById(R.id.Icon_TxtView);
			holder.dh_TextView = (TextView) convertView.findViewById(R.id.dh);
			holder.djtime_TextView = (TextView) convertView.findViewById(R.id.djtime);
			holder.lrtime_TextView = (TextView) convertView.findViewById(R.id.lrtime);
			holder.details_TextView = (TextView) convertView.findViewById(R.id.details);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}

		Map<String,Object> map = mlist.get(position);
		
		if(Integer.parseInt(map.get(DataBaseHelper.DJType).toString())==-1){
//			holder.Icon_TextView.setTextColor(context.getResources().getColor(R.color.red));
			holder.Icon_TextView.setText("未上传");
//			holder.Icon_TextView.setBackgroundResource(R.drawable.unupload);
			holder.details_TextView.setText(map.get(DataBaseHelper.Details).toString());
		}else if(Integer.parseInt(map.get(DataBaseHelper.DJType).toString())==1){
//			holder.Icon_TextView.setTextColor(context.getResources().getColor(R.color.blue));
			holder.Icon_TextView.setText("已上传");
//			holder.Icon_TextView.setBackgroundResource(R.drawable.uploaded);
			holder.details_TextView.setText(map.get(DataBaseHelper.Details).toString());
		}else if(Integer.parseInt(map.get(DataBaseHelper.DJType).toString())==0){
//			holder.Icon_TextView.setTextColor(context.getResources().getColor(R.color.red));
			holder.Icon_TextView.setText("未完成");
//			holder.Icon_TextView.setBackgroundResource(R.drawable.unupload);
			holder.details_TextView.setText("未完成单据，请完成单据！");
		}
		
		if(map.get(DataBaseHelper.MVDH)==null||map.get(DataBaseHelper.MVDH).toString().equals("")){
			if(map.get(DataBaseHelper.mType).toString().equals("1")){
				holder.dh_TextView.setText("自动编号  ——入库");
			}else if(map.get(DataBaseHelper.mType).toString().equals("2")){
				holder.dh_TextView.setText("自动编号  ——出库");
			}else if(map.get(DataBaseHelper.mType).toString().equals("0")){
				holder.dh_TextView.setText("自动编号  ——盘点");
			}
		}else{
			String dhstr=map.get(DataBaseHelper.MVDH).toString();
			if(map.get(DataBaseHelper.mType).toString().equals("1")){
				holder.dh_TextView.setText(dhstr+"  ——入库");
			}else if(map.get(DataBaseHelper.mType).toString().equals("2")){
				holder.dh_TextView.setText(dhstr+"  ——出库");
			}else if(map.get(DataBaseHelper.mType).toString().equals("0")){
				holder.dh_TextView.setText(dhstr+"  ——盘点");
			}
		}
		
		Object djsjObject = map.get(DataBaseHelper.MVDT);
		holder.djtime_TextView.setText(djsjObject==null?"":djsjObject.toString());
		
		Object lrsjObject = map.get(DataBaseHelper.Lrsj);
		holder.lrtime_TextView.setText(lrsjObject==null?"":lrsjObject.toString());
		return convertView;
	}
	
	
	
//	@Override
//	public boolean isEnabled(int position) {
//		// TODO 自动生成的方法存根
//		if(Integer.parseInt(mlist.get(position).get(DataBaseHelper.DJType).toString())==0){
//			return false;
//		}else{
//			return true;
//		}
//	}



	class ViewHolder{
		private TextView Icon_TextView;
		private TextView dh_TextView;
		private TextView djtime_TextView,lrtime_TextView;
		private TextView details_TextView;
	}
}
