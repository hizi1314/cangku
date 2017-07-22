package com.guantang.cangkuonline.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.adapter.AllFunctionAdapter;
import com.guantang.cangkuonline.customview.TagsGridView;

import de.greenrobot.event.EventBus;

public class AllFunctionDialog extends AlertDialog implements
		android.view.View.OnClickListener, OnItemClickListener {
	private Context context;
	private String[] functionNameArray = new String[10];
	private TagsGridView mTagsGridView;
	private AllFunctionAdapter mAllFunctionAdapter;
	public AllFunctionDialog(Context context, String[] functionNameArray) {
		super(context);
		this.context = context;
		this.functionNameArray = functionNameArray;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tagsgridview);
		setTitle("所有功能");
		mTagsGridView = (TagsGridView) findViewById(R.id.gridview1);
		mTagsGridView.setOnItemClickListener(this);
		setAdapter();
	}
	
	public void setAdapter(){
		mAllFunctionAdapter = new AllFunctionAdapter(context, functionNameArray);
		mTagsGridView.setAdapter(mAllFunctionAdapter);
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		dismiss();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO 自动生成的方法存根
		String name_str = arg0.getAdapter().getItem(arg2).toString();		
		EventBus.getDefault().post(name_str);
		
	}

}
