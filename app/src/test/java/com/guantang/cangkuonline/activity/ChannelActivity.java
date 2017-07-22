package com.guantang.cangkuonline.activity;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.channel.ChannelItem;
import com.guantang.cangkuonline.channel.DragAdapter;
import com.guantang.cangkuonline.channel.DragGrid;
import com.guantang.cangkuonline.channel.OtherAdapter;
import com.guantang.cangkuonline.channel.OtherGridView;
import com.guantang.cangkuonline.eventbusBean.ArrayFunctionNameEvent;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;

import de.greenrobot.event.EventBus;



public class ChannelActivity extends Activity implements OnItemClickListener {
	/** 用户栏目的GRIDVIEW */
	private DragGrid userGridView;
	/** 其它栏目的GRIDVIEW */
	private OtherGridView otherGridView;
	/** 用户栏目对应的适配器，可以拖动 */
	DragAdapter userAdapter;
	/** 其它栏目对应的适配器 */
	OtherAdapter otherAdapter;
	/** 其它栏目列表 */
	ArrayList<ChannelItem> otherChannelList = new ArrayList<ChannelItem>();
	/** 用户栏目列表 */
	ArrayList<ChannelItem> userChannelList = new ArrayList<ChannelItem>();
	/** 是否在移动，由于这边是动画结束后才进行的数据更替，设置这个限制为了避免操作太频繁造成的数据错乱。 */
	boolean isMove = false;
	private static int IS_SELECT=1;
	private static int ISNOT_SELECT=0;
	private SharedPreferences mSharedPreferences;
	private ImageButton backImgbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscribe_activity);
        mSharedPreferences = getSharedPreferences(ShareprefenceBean.SHAREPREFENCE_NAME, Context.MODE_PRIVATE);
        initView();
		initData();
    }

    /** 初始化布局 */
	private void initView() {	
		
		backImgbtn = (ImageButton) findViewById(R.id.back);
		userGridView = (DragGrid) findViewById(R.id.userGridView);
		otherGridView = (OtherGridView) findViewById(R.id.otherGridView);
		backImgbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				saveChannel();
				finish();
			}
		});
	}
	
    /** 初始化数据 */
	private void initData(){
		getUserOtherItem();
		userAdapter = new DragAdapter(this, userChannelList);
		userGridView.setAdapter(userAdapter);
		otherAdapter = new OtherAdapter(this, otherChannelList);
		otherGridView.setAdapter(this.otherAdapter);
		// 设置GRIDVIEW的ITEM的点击监听
		otherGridView.setOnItemClickListener(this);
		userGridView.setOnItemClickListener(this);
	}
	
	/** 获取用户的功能选项和其他功能选项*/
	public void getUserOtherItem(){
		ArrayList<String> function_name = new ArrayList<String>();
		function_name.add("新增货品");
		function_name.add("新增入库");
		function_name.add("新增出库");
		function_name.add("新增盘点");
		function_name.add("货品管理");
		function_name.add("本地单据");
		function_name.add("单据明细");
		function_name.add("数据同步");
		function_name.add("服务端单据");
		function_name.add("反馈");
		for(int i=0;i<8;i++){
			String userfunctionItem = mSharedPreferences.getString("gridview_item"+i, "");
			if(!userfunctionItem.equals("")){
				function_name.remove(userfunctionItem);
				ChannelItem channelitem = new ChannelItem(i, userfunctionItem, i, IS_SELECT);
				userChannelList.add(channelitem);
			}
		}
		for(int i=0;i<10-userChannelList.size();i++){
			ChannelItem channelitem = new ChannelItem(i, function_name.get(i), i, ISNOT_SELECT);
			otherChannelList.add(channelitem);
		}
		
	}
	
	/** GRIDVIEW对应的ITEM点击监听接口 */
	@Override
	public void onItemClick(AdapterView<?> parent, final View view,
			final int position, long id) {
		// 如果点击的时候，之前动画还没结束，那么就让点击事件无效
		if (isMove) {
			return;
		}
		switch (parent.getId()) {
		case R.id.userGridView:
			final ImageView moveImageView1 = getView(view);
			if (moveImageView1 != null) {
				TextView newTextView = (TextView) view
						.findViewById(R.id.text_item);
				final int[] startLocation = new int[2];
				newTextView.getLocationInWindow(startLocation);
				final ChannelItem channel = ((DragAdapter) parent.getAdapter())
						.getItem(position);// 获取点击的频道内容
				otherAdapter.setVisible(false);
				// 添加到最后一个
				otherAdapter.addItem(channel);
				new Handler().postDelayed(new Runnable() {
					public void run() {
						try {
							int[] endLocation = new int[2];
							// 获取终点的坐标
							otherGridView.getChildAt(
									otherGridView.getLastVisiblePosition())
									.getLocationInWindow(endLocation);
							MoveAnim(moveImageView1, startLocation,
									endLocation, channel, userGridView);
							userAdapter.setRemove(position);
						} catch (Exception localException) {
						}
					}
				}, 50L);
			}
			break;
		case R.id.otherGridView:
			final ImageView moveImageView2 = getView(view);
			if (moveImageView2 != null) {
				TextView newTextView = (TextView) view
						.findViewById(R.id.text_item);
				final int[] startLocation = new int[2];
				newTextView.getLocationInWindow(startLocation);
				final ChannelItem channel = ((OtherAdapter) parent.getAdapter())
						.getItem(position);
				if(userChannelList.size()<8){
					// 添加到最后一个
					userAdapter.setVisible(false);
					userAdapter.addItem(channel);
					new Handler().postDelayed(new Runnable() {
						public void run() {
							try {
								int[] endLocation = new int[2];
								// 获取终点的坐标
								userGridView.getChildAt(
										userGridView.getLastVisiblePosition())
										.getLocationInWindow(endLocation);
								MoveAnim(moveImageView2, startLocation,
										endLocation, channel, otherGridView);
								otherAdapter.setRemove(position);
							} catch (Exception localException) {
							}
						}
					}, 50L);
				}else{
					Toast.makeText(this, "常用功能选项最多添加8个", Toast.LENGTH_SHORT).show();
				}
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 点击ITEM移动动画
	 * 
	 * @param moveView
	 * @param startLocation
	 * @param endLocation
	 * @param moveChannel
	 * @param clickGridView
	 */
	private void MoveAnim(View moveView, int[] startLocation,
			int[] endLocation, final ChannelItem moveChannel,
			final GridView clickGridView) {
		int[] initLocation = new int[2];
		// 获取传递过来的VIEW的坐标
		moveView.getLocationInWindow(initLocation);
		// 得到要移动的VIEW,并放入对应的容器中
		final ViewGroup moveViewGroup = getMoveViewGroup();
		final View mMoveView = getMoveView(moveViewGroup, moveView,
				initLocation);
		// 创建移动动画
		TranslateAnimation moveAnimation = new TranslateAnimation(
				startLocation[0], endLocation[0], startLocation[1],
				endLocation[1]);
		moveAnimation.setDuration(300L);// 动画时间
		// 动画配置
		AnimationSet moveAnimationSet = new AnimationSet(true);
		moveAnimationSet.setFillAfter(false);// 动画效果执行完毕后，View对象不保留在终止的位置
		moveAnimationSet.addAnimation(moveAnimation);
		mMoveView.startAnimation(moveAnimationSet);
		moveAnimationSet.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				isMove = true;
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				moveViewGroup.removeView(mMoveView);
				// instanceof 方法判断2边实例是不是一样，判断点击的是DragGrid还是OtherGridView
				if (clickGridView instanceof DragGrid) {
					otherAdapter.setVisible(true);
					otherAdapter.notifyDataSetChanged();
					userAdapter.remove();
				} else {
					userAdapter.setVisible(true);
					userAdapter.notifyDataSetChanged();
					otherAdapter.remove();
				}
				isMove = false;
			}
		});
	}

	/**
	 * 获取移动的VIEW，放入对应ViewGroup布局容器
	 * 
	 * @param viewGroup
	 * @param view
	 * @param initLocation
	 * @return
	 */
	private View getMoveView(ViewGroup viewGroup, View view, int[] initLocation){
		int x = initLocation[0];
		int y = initLocation[1];
		viewGroup.addView(view);
		LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mLayoutParams.leftMargin = x;
		mLayoutParams.topMargin = y;
		view.setLayoutParams(mLayoutParams);
		return view;
	}

	/**
	 * 创建移动的ITEM对应的ViewGroup布局容器
	 */
	private ViewGroup getMoveViewGroup() {
		ViewGroup moveViewGroup = (ViewGroup) getWindow().getDecorView();
		LinearLayout moveLinearLayout = new LinearLayout(this);
		moveLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		moveViewGroup.addView(moveLinearLayout);
		return moveLinearLayout;
	}

	/**
	 * 获取点击的Item的对应View，
	 * 
	 * @param view
	 * @return
	 */
	private ImageView getView(View view) {
		view.destroyDrawingCache();
		view.setDrawingCacheEnabled(true);
		Bitmap cache = Bitmap.createBitmap(view.getDrawingCache());
		view.setDrawingCacheEnabled(false);
		ImageView iv = new ImageView(this);
		iv.setImageBitmap(cache);
		return iv;
	}

	/** 退出时候保存选择后数据库的设置 */
	private void saveChannel() {
//		ChannelManage.getManage(AppApplication.getApp().getSQLHelper())
//				.deleteAllChannel();
//		ChannelManage.getManage(AppApplication.getApp().getSQLHelper())
//				.saveUserChannel(userAdapter.getChannnelLst());
//		ChannelManage.getManage(AppApplication.getApp().getSQLHelper())
//				.saveOtherChannel(otherAdapter.getChannnelLst());
		Iterator<ChannelItem> it=userChannelList.iterator();
		ArrayList<String> userfunctionname = new ArrayList<String>();
		while(it.hasNext()){
			userfunctionname.add(it.next().getName());
		}
		EventBus.getDefault().post(new ArrayFunctionNameEvent(userfunctionname));
	}
	
	@Override
	public void onBackPressed() {
		saveChannel();
		super.onBackPressed();
	}
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	// TODO 自动生成的方法存根
    	if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
    		saveChannel();
    		finish();
    	}
    	return super.onKeyDown(keyCode, event);
    }
}
