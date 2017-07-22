package com.guantang.cangkuonline.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.commonadapter.CommonAdapter;
import com.guantang.cangkuonline.commonadapter.ViewHolder;

public class HpCkkcAdapter extends CommonAdapter<Map<String,Object>> {

	public HpCkkcAdapter(Context mContext,List<Map<String, Object>> mList, int LayoutId) {
		super(mContext, mList, LayoutId);
		// TODO 自动生成的构造函数存根
	}

	@Override
	public void convert(ViewHolder holder, Map<String, Object> item) {
		// TODO 自动生成的方法存根
		TextView ckTextView = holder.getView(R.id.ckTextView);
		TextView numTextView = holder.getView(R.id.numTextView);
		String ckmc = item.get("ckmc").toString();
		ckTextView.setText(ckmc==null||ckmc.equals("null")?"":ckmc);
		String kcsl = item.get("kcsl").toString();
		numTextView.setText(kcsl==null||kcsl.equals("null")?"":kcsl);
	}

}
