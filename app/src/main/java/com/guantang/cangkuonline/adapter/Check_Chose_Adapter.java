package com.guantang.cangkuonline.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.database.DataBaseCheckMethod;
import com.guantang.cangkuonline.database.DataBaseHelper;

public class Check_Chose_Adapter extends BaseAdapter{
	private LayoutInflater mInflater;
	private List<Map<String, Object>> mData;
	
	Context mcontext;
	DataBaseCheckMethod dm_ck;
	public class HolderView {
		TextView name,bar,gg,num;
		CheckBox cb;
	}
	public Check_Chose_Adapter(Context context,List<Map<String, Object>>  data){
		this.mInflater = LayoutInflater.from(context);
		this.mcontext=context;
		this.mData=data;
		dm_ck=new DataBaseCheckMethod(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		HolderView holder=null;
		if(convertView== null ){
			holder = new HolderView();
			convertView = mInflater.inflate(R.layout.hplist_checkbox, null);
			holder.name = (TextView) convertView.findViewById(R.id.itemname);
			holder.bar= (TextView) convertView.findViewById(R.id.itemcode);
			holder.num=(TextView) convertView.findViewById(R.id.itemnum);
			holder.gg= (TextView) convertView.findViewById(R.id.itemgg);
			holder.cb = (CheckBox) convertView.findViewById(R.id.ck);
			convertView.setTag(holder);
		}else{
			holder = (HolderView)convertView.getTag();
		}
		if(mData.get(position)!=null){
			holder.cb.setChecked((Boolean) mData.get(position).get("check"));
		}
		Object hpmcObject = mData.get(position).get(DataBaseHelper.HPMC);
		holder.name.setText(hpmcObject==null?"":hpmcObject.toString());
		
		Object hpbmObject = mData.get(position).get(DataBaseHelper.HPBM);
		holder.bar.setText(hpbmObject==null?"":hpbmObject.toString());
		
		Object ggxhObject = mData.get(position).get(DataBaseHelper.GGXH);
		holder.gg.setText(ggxhObject==null?"":ggxhObject.toString());
		
		Object currKcObject = mData.get(position).get(DataBaseHelper.CurrKC);
		holder.num.setText(currKcObject==null?"":currKcObject.toString());
		return convertView;
	}

}
