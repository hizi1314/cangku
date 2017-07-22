package com.guantang.cangkuonline.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.database.DataBaseOperateMethod;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemCanDeleteAdapter extends BaseAdapter {
	
	private DataBaseOperateMethod dm_op;
	private LayoutInflater inflater;
	private Context context;
	private List<Map<String,Object>> mList = new ArrayList<Map<String,Object>>();
	
	public ItemCanDeleteAdapter(Context mcontext,List<Map<String,Object>> mlist){
		context = mcontext;
		inflater = LayoutInflater.from(context);
		mList = mlist;
		dm_op = new DataBaseOperateMethod(context);
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO 自动生成的方法存根
		ViewHolder holder = null;
		if(convertView==null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.list_item_candelete, null);
			holder.mTextView = (TextView) convertView.findViewById(R.id.mytext1);
			holder.mImageView = (ImageView) convertView.findViewById(R.id.mydeleteImgVIew);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		final Map<String,Object> map = mList.get(position);
		String ssss = map.get(DataBaseHelper.username).toString();
		holder.mTextView.setText(map.get(DataBaseHelper.username).toString());
		holder.mImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				dm_op.deleteLoginInfo(map.get(DataBaseHelper.company).toString(), map.get(DataBaseHelper.username).toString());
				mList.remove(map);
				notifyDataSetChanged();
			}
		});
		
		return convertView;
	}
	
	class ViewHolder{
		TextView mTextView;
		ImageView mImageView;
	}
	
}
