package com.guantang.cangkuonline.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 去掉滚动条的GridView
 * 
 * */
public class TagsGridView extends GridView {
	
	
	public TagsGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO 自动生成的构造函数存根
	}

	public TagsGridView(Context context) {
		super(context);
		// TODO 自动生成的构造函数存根
	}
	
	public TagsGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO 自动生成的构造函数存根
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	int expandSpec=MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
	super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
