package com.guantang.cangkuonline.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.commonadapter.CommonAdapter;
import com.guantang.cangkuonline.commonadapter.ViewHolder;

public class HistorySearchAdapter extends CommonAdapter<String> {
	
	public HistorySearchAdapter(Context mContext,List<String> mList, int LayoutId) {
		super(mContext,mList, LayoutId);
		// TODO 自动生成的构造函数存根
	}

	@Override
	public void convert(ViewHolder holder, String item) {
		// TODO 自动生成的方法存根
		TextView textView=holder.getView(R.id.textview_popup);
		textView.setText(item);
	}

}
