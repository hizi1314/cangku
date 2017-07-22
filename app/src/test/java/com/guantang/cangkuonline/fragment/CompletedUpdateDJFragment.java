package com.guantang.cangkuonline.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.activity.HistoryDJ_DetailsActivity;
import com.guantang.cangkuonline.adapter.MyDJAdapter;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.database.DataBaseOperateMethod;
import com.guantang.cangkuonline.swipemenulistview.SwipeMenu;
import com.guantang.cangkuonline.swipemenulistview.SwipeMenuCreator;
import com.guantang.cangkuonline.swipemenulistview.SwipeMenuItem;
import com.guantang.cangkuonline.swipemenulistview.SwipeMenuListView;
import com.guantang.cangkuonline.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class CompletedUpdateDJFragment extends Fragment implements OnMenuItemClickListener,OnItemClickListener{
	
	private SwipeMenuListView myListView;
	private List<Map<String,Object>> mlist = new ArrayList<Map<String,Object>>();
	private MyDJAdapter DJadapter;
	private DataBaseOperateMethod dm_op;
	private Context context;
	
	public static CompletedUpdateDJFragment getInstance(List<Map<String,Object>> mList){
		CompletedUpdateDJFragment cudj = new CompletedUpdateDJFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("list",(Serializable) mList);
		cudj.setArguments(bundle);
		return cudj;
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO 自动生成的方法存根
		super.onAttach(activity);
		context = activity;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		mlist=(List<Map<String, Object>>) getArguments().getSerializable("list");
		dm_op = new DataBaseOperateMethod(context);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		View view = inflater.inflate(R.layout.localdj_itemlist, null);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onActivityCreated(savedInstanceState);
		myListView = (SwipeMenuListView) getView().findViewById(R.id.djlist);
		init();
	}
	
	public void init(){
		DJadapter = new MyDJAdapter(context);
		myListView.setAdapter(DJadapter);
		DJadapter.setData(mlist);
		SwipeMenuCreator creator = new SwipeMenuCreator() {
			@Override
			public void create(SwipeMenu menu) {
				if (menu.getViewType() == 1) {
					createDeleteMenu(menu);
				}
			}
		};
		myListView.setMenuCreator(creator);
		myListView.setOnMenuItemClickListener(this);
		myListView.setOnItemClickListener(this);
	}
	
	public void createDeleteMenu(SwipeMenu menu){
		// create "delete" item
		SwipeMenuItem deleteItem = new SwipeMenuItem(context);
		// set item background
		deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
				0x3F, 0x25)));
		// set item width
		deleteItem.setWidth(dp2px(90));
		// set a icon
		deleteItem.setIcon(R.drawable.ic_delete);
		// add to menu
		menu.addMenuItem(deleteItem);
	}
	
	@Override
	public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
		// TODO 自动生成的方法存根
		Map<String, Object> map = mlist.get(position);
		if(menu.getViewType()==1){
			dm_op.Del_Moved(map.get(DataBaseHelper.HPM_ID).toString());
			dm_op.Del_Movem(map.get(DataBaseHelper.HPM_ID).toString());
			mlist.remove(map);
			DJadapter.setData(mlist);
		}
		
		return false;
	}
	
	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO 自动生成的方法存根
		Intent i = new Intent();
		Map<String, Object> map = (Map<String, Object>) arg0.getAdapter()
				.getItem(arg2);
		i.putExtra("HPM_ID", map.get(DataBaseHelper.HPM_ID).toString());
		// from的值等于1代表查看本地单据
		i.putExtra("from", 1);
		i.setClass(context, HistoryDJ_DetailsActivity.class);
		startActivity(i);
	}
	
}
