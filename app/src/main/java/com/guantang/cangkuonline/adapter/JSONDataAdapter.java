package com.guantang.cangkuonline.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.database.DataBaseHelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class JSONDataAdapter extends BaseAdapter {
	private Context context;
	private List<JSONObject> mlist = new ArrayList<JSONObject>();
	private LayoutInflater inflater;
	
	public JSONDataAdapter(Context context){
		this.context = context;
		inflater = LayoutInflater.from(this.context);
		
	}
	
	public void setData(List<JSONObject> list){
		mlist = list;
		notifyDataSetChanged();
	}
	public void addDataList(List<JSONObject> list){
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
		// TODO 自动生成的方法存根
		ViewHolder holder = null;
		if(convertView==null){
			holder = new ViewHolder();
			convertView=inflater.inflate(R.layout.item_netdj, null);
			holder.dhTextView = (TextView) convertView.findViewById(R.id.dh);
			holder.djdateTextView = (TextView) convertView.findViewById(R.id.djdate);
			holder.lrdateTextView = (TextView) convertView.findViewById(R.id.lrdate);
			holder.hpDetailsTextView = (TextView) convertView.findViewById(R.id.hpDetails);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}

		JSONObject jsonObject = mlist.get(position);

		try {
			Object mvdhObject = jsonObject.get(DataBaseHelper.MVDH);
			holder.dhTextView.setText(mvdhObject==null?"":mvdhObject.toString());
			
			Object mvdtObject = jsonObject.get(DataBaseHelper.MVDT);
			holder.djdateTextView.setText(mvdtObject==null?"":mvdtObject.toString().substring(0, 10));

			Object lrsjObject = jsonObject.get(DataBaseHelper.Lrsj);
			holder.lrdateTextView.setText(lrsjObject==null?"":lrsjObject.toString().replace("T", " "));

			Object hpDetaiObject = jsonObject.get(DataBaseHelper.hpDetails);
			holder.hpDetailsTextView.setText(hpDetaiObject==null?"":hpDetaiObject.toString());
		} catch (JSONException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

		return convertView;
	}
	class ViewHolder {
		public TextView dhTextView,djdateTextView,lrdateTextView,hpDetailsTextView;
	}
}
