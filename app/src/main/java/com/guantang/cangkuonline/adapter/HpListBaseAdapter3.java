package com.guantang.cangkuonline.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.sortlistview.SortModel;


public class HpListBaseAdapter3 extends BaseAdapter implements SectionIndexer{
	private LayoutInflater inflater;
	private Context context;
	private List<SortModel> mlist = new ArrayList<SortModel>();
	private LinearLayout huadonglayout;
	private float downX,upX;
	
	
	public HpListBaseAdapter3(Context context,List<SortModel> list) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		mlist=list;
	}
	
	public void updateListView(List<SortModel> list){
		mlist = list;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		final SortModel mContent = (SortModel) mlist.get(position);
		HolderView holder;
		if (convertView == null) {
			holder = new HolderView();
			convertView = inflater.inflate(R.layout.listitem3, null);
			holder.itemname = (TextView) convertView
					.findViewById(R.id.itemname);
			holder.itemcode = (TextView) convertView
					.findViewById(R.id.itemcode);
			holder.itemgg = (TextView) convertView.findViewById(R.id.itemgg);
			holder.itemnum = (TextView) convertView.findViewById(R.id.itemnum);
			holder.tvLetter = (TextView) convertView.findViewById(R.id.catalog);
			holder.itemlayLayout = (LinearLayout) convertView.findViewById(R.id.itemlayout);
			convertView.setTag(holder);
		} else {
			holder = (HolderView) convertView.getTag();
		}
		//根据position获取分类的首字母的Char ascii值
		int section = getSectionForPosition(position);
		
		//如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		if(position == getPositionForSection(section)){
			holder.tvLetter.setVisibility(View.VISIBLE);
			holder.tvLetter.setText(mContent.getSortLetters());
		}else{
			holder.tvLetter.setVisibility(View.GONE);
		}
		
		Map<String, Object> map = mlist.get(position).getMyMap();
		
		Object hpmcObject = map.get(DataBaseHelper.HPMC);
		holder.itemname.setText(hpmcObject==null?"":hpmcObject.toString());
		
		Object hpbmObject = map.get(DataBaseHelper.HPBM);
		holder.itemcode.setText(hpbmObject==null?"":hpmcObject.toString());
		
		Object ggxhObject = map.get(DataBaseHelper.GGXH);
		holder.itemgg.setText(ggxhObject==null?"":ggxhObject.toString());

		Object currkcObject = map.get(DataBaseHelper.CurrKC);
		holder.itemnum.setText(currkcObject==null?"":currkcObject.toString());

		return convertView;
	}

	class HolderView {
		public TextView itemname;
		public TextView itemcode;
		public TextView itemgg;
		public TextView itemnum;
		public TextView text;
		public Button chakanBtn;
		public ImageButton delImgbtn;
		public LinearLayout rightlayout, itemlayLayout;
		public TextView tvLetter;
	}

	
	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				context.getResources().getDisplayMetrics());
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
		return null;
	}
}
