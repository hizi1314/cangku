package com.guantang.cangkuonline.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.sortlistview.SortModel;

public class DJdetailAdapter extends BaseAdapter implements SectionIndexer{
	private List<SortModel> mlist=new ArrayList<SortModel>();
	private Context context;
	private LayoutInflater inflater;
	private int op_type=1;
	private int ckid=-1;
	private String djid="";
	
	public DJdetailAdapter(Context context,int ckid,String djid,int op_type){
		this.context=context;
		inflater=LayoutInflater.from(context);
		this.op_type=op_type;
		this.djid=djid;

	}
	
	public void setData(List<SortModel> list){
		mlist=list;
		notifyDataSetChanged();
	}
	
	
	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		return mlist.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO 自动生成的方法存根
		return mlist.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO 自动生成的方法存根
		return position;
	}
	
//	@Override
//	public int getItemViewType(int position) {
//		if () {
//			return LAYOUT_LEFT;
//		} else {
//			return LAYOUT_BUTTOM;
//		}
//
//	}
	
	@Override
	public int getItemViewType(int position) {
		// TODO 自动生成的方法存根
		if(op_type==0){
			return 0;
		}else if(op_type==1){
			return 1;
		}else if(op_type==2){
			return 2;
		}
		return super.getItemViewType(position);
	}
	
	@Override
	public int getViewTypeCount() {
		// TODO 自动生成的方法存根
		return 3;
	}
	
	
	public View getRKView(int position, View convertView, ViewGroup parent) {
		// TODO 自动生成的方法存根
		ViewHolder holder;
		if(convertView==null){
			holder=new ViewHolder();
			convertView=inflater.inflate(R.layout.djdetailsitem_ruku, null);
			holder.itemcode=(TextView) convertView.findViewById(R.id.itemcode);
			holder.itemgg= (TextView) convertView.findViewById(R.id.itemgg);
			holder.itemmsl= (TextView) convertView.findViewById(R.id.itemmsl);
			holder.itemname= (TextView) convertView.findViewById(R.id.itemname);
			holder.itemnum= (TextView) convertView.findViewById(R.id.itemnum);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		
		Map<String,Object> map=mlist.get(position).getMyMap();
		if(map!=null){
			Object hpbmObject = map.get(DataBaseHelper.HPBM);
			holder.itemcode.setText(hpbmObject==null?"":hpbmObject.toString());
			
			Object ggxhObject = map.get(DataBaseHelper.GGXH);
			holder.itemgg.setText(ggxhObject==null?"":ggxhObject.toString());
			
			Object mslObject = map.get(DataBaseHelper.MSL);
			holder.itemmsl.setText(mslObject==null?"":mslObject.toString());
			
			Object hpmcObject = map.get(DataBaseHelper.HPMC);
			holder.itemname.setText(hpmcObject==null?"":hpmcObject.toString());
			
			Object btkcObject = map.get(DataBaseHelper.BTKC);
			holder.itemnum.setText(btkcObject==null?"":btkcObject.toString());
		}

		
//		convertView.setOnClickListener(new myOnClikListener(map));
		
		return convertView;
	}
	
	public View getCKView(int position, View convertView, ViewGroup parent) {
		// TODO 自动生成的方法存根
		ViewHolder holder=new ViewHolder();
		if(convertView==null){
			convertView=inflater.inflate(R.layout.djdetailsitem_chuku, null);
			holder.itemcode=(TextView) convertView.findViewById(R.id.itemcode);
			holder.itemgg= (TextView) convertView.findViewById(R.id.itemgg);
			holder.itemmsl= (TextView) convertView.findViewById(R.id.itemmsl);
			holder.itemname= (TextView) convertView.findViewById(R.id.itemname);
			holder.itemnum= (TextView) convertView.findViewById(R.id.itemnum);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		
		Map<String,Object> map=mlist.get(position).getMyMap();
		if(map!=null){
			Object hpbmObject = map.get(DataBaseHelper.HPBM);
			holder.itemcode.setText(hpbmObject==null?"":hpbmObject.toString());
			
			Object ggxhObject = map.get(DataBaseHelper.GGXH);
			holder.itemgg.setText(ggxhObject==null?"":ggxhObject.toString());
			
			Object mslObject = map.get(DataBaseHelper.MSL);
			holder.itemmsl.setText(mslObject==null?"":mslObject.toString());
			
			Object hpmcObject = map.get(DataBaseHelper.HPMC);
			holder.itemname.setText(hpmcObject==null?"":hpmcObject.toString());
			
			Object btkcObject = map.get(DataBaseHelper.BTKC);
			holder.itemnum.setText(btkcObject==null?"":btkcObject.toString());
		}
		
//		convertView.setOnClickListener(new myOnClikListener(map));
		
		return convertView;
	}
	
	public View getPDView(int position, View convertView, ViewGroup parent) {
		// TODO 自动生成的方法存根
		ViewHolder holder=new ViewHolder();
		if(convertView==null){
			convertView=inflater.inflate(R.layout.djdetailsitem_check, null);
			holder.itemcode=(TextView) convertView.findViewById(R.id.itemcode);
			holder.itemgg= (TextView) convertView.findViewById(R.id.itemgg);
			holder.itemmsl= (TextView) convertView.findViewById(R.id.itemmsl);
			holder.itemname= (TextView) convertView.findViewById(R.id.itemname);
			holder.itemnum= (TextView) convertView.findViewById(R.id.itemnum);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		
		Map<String,Object> map=mlist.get(position).getMyMap();
		if(map!=null){
			Object hpbmObject = map.get(DataBaseHelper.HPBM);
			holder.itemcode.setText(hpbmObject==null?"":hpbmObject.toString());
			
			Object ggxhObject = map.get(DataBaseHelper.GGXH);
			holder.itemgg.setText(ggxhObject==null?"":ggxhObject.toString());
			
			Object mslObject = map.get(DataBaseHelper.ATKC);
			holder.itemmsl.setText(mslObject==null?"":mslObject.toString());
			
			Object hpmcObject = map.get(DataBaseHelper.HPMC);
			holder.itemname.setText(hpmcObject==null?"":hpmcObject.toString());
			
			Object btkcObject = map.get(DataBaseHelper.BTKC);
			holder.itemnum.setText(btkcObject==null?"":btkcObject.toString());
		}
		
//		convertView.setOnClickListener(new myOnClikListener(map));
		
		return convertView;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO 自动生成的方法存根
		if(getItemViewType(position)==0){
			return getPDView(position, convertView, parent);
		}else if(getItemViewType(position)==1){
			return getRKView(position, convertView, parent);
		}else if(getItemViewType(position)==2){
			return getCKView(position, convertView, parent);
		}
		return convertView;
	}
	
//	class myOnClikListener implements OnClickListener{
//		private Map<String,Object> map;
//		public myOnClikListener(Map<String,Object> map){
//			this.map=map;
//		}
//		
//		@Override
//		public void onClick(View v) {
//			// TODO 自动生成的方法存根
//			Intent intent = new Intent(context, HpListDetailsWriteActivity.class);
//			intent.putExtra("hpid", map.get(DataBaseHelper.HPID).toString());
//			intent.putExtra("dh", dh);
//			intent.putExtra("djid", djid);
//			intent.putExtra("ckid", ckid);
//			intent.putExtra("op_type", op_type);
//			intent.putExtra("dacttype", dacttype);
//			context.startActivity(intent);
//		}
//		
//	}
	
	class ViewHolder{
		TextView itemname,itemcode,itemgg,itemnum,itemmsl;
	}
	
	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	@Override
	public int getPositionForSection(int arg0) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = ((SortModel) mlist.get(i)).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == arg0) {
				return i;
			}
		}
		
		return -1;
	}

	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	@Override
	public int getSectionForPosition(int position) {
		// TODO Auto-generated method stub
		return ((SortModel) mlist.get(position)).getSortLetters().charAt(0);
	}

	/**
	 * 提取英文的首字母，非英文字母用#代替。
	 * 
	 * @param str
	 * @return
	 */
	private String getAlpha(String str) {
		String  sortStr = str.trim().substring(0, 1).toUpperCase();
		// 正则表达式，判断首字母是否是英文字母
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}

	@Override
	public Object[] getSections() {
		// TODO 自动生成的方法存根
		return null;
	}
}
