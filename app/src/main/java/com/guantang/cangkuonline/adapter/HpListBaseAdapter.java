package com.guantang.cangkuonline.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.R.integer;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.application.MyApplication;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.helper.DecimalsHelper;

public class HpListBaseAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<Map<String, Object>> mlist= new ArrayList<Map<String, Object>>();
	private int ckid = -1;
	
	public HpListBaseAdapter(Context context){
		inflater=LayoutInflater.from(context);	
		
	}
	public void setListData(List<Map<String,Object>> list,int ckid) {
		mlist = list;
		this.ckid = ckid;
		notifyDataSetChanged();
	}

	public void addListData(List<Map<String,Object>> list) {
		mlist.addAll(list);
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
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView holder;
		if(convertView==null){
			holder=new HolderView();
			convertView=inflater.inflate(R.layout.listitem, null);
			holder.itemname=(TextView) convertView.findViewById(R.id.itemname);
			holder.itemcode=(TextView) convertView.findViewById(R.id.itemcode);
			holder.itemgg=(TextView) convertView.findViewById(R.id.itemgg);
			holder.itemnum=(TextView) convertView.findViewById(R.id.itemnum);
			holder.changeTextView= (TextView) convertView.findViewById(R.id.changeTextView);
			convertView.setTag(holder);
		}else{
			holder=(HolderView) convertView.getTag();
		}
		
		Map<String,Object> map=(Map<String, Object>) getItem(position);	
		
		Object hpmcObject = map.get(DataBaseHelper.HPMC);
		holder.itemname.setText(textviewValue(hpmcObject));

		Object hpbmObject = map.get(DataBaseHelper.HPBM);
		holder.itemcode.setText(textviewValue(hpbmObject));

		Object ggxhObject = map.get(DataBaseHelper.GGXH);
		holder.itemgg.setText(textviewValue(ggxhObject));

		if(ckid == -1){
			Object currkcObject = map.get(DataBaseHelper.CurrKC);
			holder.changeTextView.setText("货品总库存：");
			holder.itemnum.setText(textviewValue(currkcObject, MyApplication.getInstance().getNumPoint()));
		}else{
			Object kcslObject = map.get(DataBaseHelper.KCSL);
			holder.changeTextView.setText("本仓库库存：");
			holder.itemnum.setText(textviewValue(kcslObject, MyApplication.getInstance().getNumPoint()));
		}
		
		return convertView;
	}
	
	public String textviewValue(Object obj){
		String objString = obj.toString();
		return (objString=="null"||objString==null||objString=="")?"":objString;
	}
	
	public String textviewValue(Object obj,int numPoint){
		String objString = obj.toString();
		return (objString=="null"||objString==null||objString=="")?"":DecimalsHelper.Transfloat(Double.parseDouble(objString),numPoint);
	}
}

	
class HolderView {
	public TextView itemname;
	public TextView itemcode;
	public TextView itemgg;
	public TextView itemnum;
	public TextView changeTextView;
}
