package com.guantang.cangkuonline.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.adapter.DDdetailAdapter;
import com.guantang.cangkuonline.adapter.DJdetailAdapter;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.database.DataBaseOperateMethod;
import com.guantang.cangkuonline.eventbusBean.ObjectSeven;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;
import com.guantang.cangkuonline.sortlistview.CharacterParser;
import com.guantang.cangkuonline.sortlistview.ClearEditText;
import com.guantang.cangkuonline.sortlistview.PinyinComparator;
import com.guantang.cangkuonline.sortlistview.SideBar;
import com.guantang.cangkuonline.sortlistview.SortModel;
import com.guantang.cangkuonline.sortlistview.SideBar.OnTouchingLetterChangedListener;
import com.guantang.cangkuonline.swipemenulistview.SwipeMenu;
import com.guantang.cangkuonline.swipemenulistview.SwipeMenuCreator;
import com.guantang.cangkuonline.swipemenulistview.SwipeMenuItem;
import com.guantang.cangkuonline.swipemenulistview.SwipeMenuListView;
import com.guantang.cangkuonline.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;

import de.greenrobot.event.EventBus;

public class OrderDetailActivity extends Activity implements OnItemClickListener,
		OnMenuItemClickListener, OnClickListener {
	private SharedPreferences mSharedPreferences;
	private ImageButton backImgBtn, delImgBtn;
	private SwipeMenuListView mListView;
	private TextView dialog, detectiontxtView;
	private SideBar sideBar;

	// 订单明细表字段
	private String[] str1 = { DataBaseHelper.HPID, DataBaseHelper.orderID,
			DataBaseHelper.HPMC, DataBaseHelper.HPBM, DataBaseHelper.GGXH,
			DataBaseHelper.SL, DataBaseHelper.DJ, DataBaseHelper.ZJ,
			DataBaseHelper.ddirc };

	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	private DataBaseOperateMethod dm_op = new DataBaseOperateMethod(this);
	private int where = 0, dirc = 0;
	private String ddh = "", ddid = "";

	private DDdetailAdapter DDAdapter;
	private ClearEditText mClearEditText;
	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;

	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		setContentView(R.layout.activity_djdetail);
		mSharedPreferences = getSharedPreferences(
				ShareprefenceBean.SHAREPREFENCE_NAME, Context.MODE_PRIVATE);

		Intent intent = getIntent();
		ddh = intent.getStringExtra("ddh");
		ddid = intent.getStringExtra("ddid");
		dirc = intent.getIntExtra("dirc", 0);

		initView();
		init();
	}

	public void initView() {
		backImgBtn = (ImageButton) findViewById(R.id.back);
		delImgBtn = (ImageButton) findViewById(R.id.del);
		mListView = (SwipeMenuListView) findViewById(R.id.hplist2);
		detectiontxtView = (TextView) findViewById(R.id.detectiontxtView);
		detectiontxtView.setVisibility(View.GONE);

		DDAdapter = new DDdetailAdapter(this, ddid, dirc);
		mListView.setAdapter(DDAdapter);

		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();

		pinyinComparator = new PinyinComparator();

		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);
		sideBar.setTextView(dialog);

		backImgBtn.setOnClickListener(this);

		SwipeMenuCreator creator = new SwipeMenuCreator() {
			@Override
			public void create(SwipeMenu menu) {
				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(
						getApplicationContext());
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
		};
		mListView.setMenuCreator(creator);
		mListView.setOnMenuItemClickListener(this);
		mListView.setOnItemClickListener(this);
	}

	public void init() {

		list = dm_op.Gt_OrderDetails(ddid, str1);
		// 设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = DDAdapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					mListView.setSelection(position);
				}

			}
		});
		// 根据输入框输入值的改变来过滤搜索
		mClearEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				// 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				filterData(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}

		});
		mListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO 自动生成的方法存根
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					where = mListView.getFirstVisiblePosition();
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO 自动生成的方法存根

			}
		});
		
		if(!list.isEmpty()){
			SourceDateList = filledData();
			// 根据a-z进行排序
			Collections.sort(SourceDateList, pinyinComparator);
			DDAdapter.setData(SourceDateList);
			mListView.setSelection(where);
		}
	}

	/**
	 * 为ListView填充数据,并为每个元素配上字母
	 * 
	 * @param date
	 * @return
	 */
	public List<SortModel> filledData() {
		List<SortModel> mSortList = new ArrayList<SortModel>();
		for (int i = 0; i < list.size(); i++) {
			SortModel sortModel = new SortModel();
			sortModel.setMyMap(list.get(i));
			// 汉字转换成拼音
			String pinyin = characterParser.getSelling(list.get(i)
					.get(DataBaseHelper.HPMC).toString());
			String sortString = pinyin.substring(0, 1).toUpperCase();

			// 正则表达式，判断首字母是否是英文字母
			if (sortString.matches("[A-Z]")) {
				sortModel.setSortLetters(sortString.toUpperCase());
			} else {
				sortModel.setSortLetters("#");
			}
			mSortList.add(sortModel);
		}

		return mSortList;
	}

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * 
	 * @param filterStr
	 */
	private void filterData(String filterStr) {
		List<SortModel> filterDateList = new ArrayList<SortModel>();
		SourceDateList = filledData();
		if (TextUtils.isEmpty(filterStr)) {

		} else {
			for (SortModel sortModel : SourceDateList) {
				String name = (String) sortModel.getMyMap().get(
						DataBaseHelper.HPMC);
				if (name.indexOf(filterStr.toString()) != -1
						|| characterParser.getSelling(name).startsWith(
								filterStr.toString())) {
					filterDateList.add(sortModel);
				}
			}
			SourceDateList.clear();
			SourceDateList = filterDateList;
		}
		// 根据a-z进行排序
		Collections.sort(SourceDateList, pinyinComparator);
		DDAdapter.setData(SourceDateList);
		mListView.setSelection(where);
	}

	public void setDelList(Map<String, Object> map) {
		dm_op.Del_OrderDetails(ddid, map.get(DataBaseHelper.HPID).toString());
		list.remove(map);

	}

	@Override
	public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
		// TODO 自动生成的方法存根
		SortModel model = SourceDateList.get(position);
		switch (index) {
		case 0:
			setDelList(model.getMyMap());
			SourceDateList.remove(position);
			DDAdapter.setData(SourceDateList);
			mListView.setSelection(where);
			EventBus.getDefault().post(new ObjectSeven(model.getMyMap()));
			break;

		}
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO 自动生成的方法存根
		SortModel model = (SortModel) arg0.getAdapter().getItem(arg2);
		Map<String, Object> map = model.getMyMap();
		Intent intent = new Intent(this, DDHpListDetailsWriteActivity.class);
		intent.putExtra("hpid", map.get(DataBaseHelper.HPID).toString());
		intent.putExtra("ddh", ddh);
		intent.putExtra("ddid", ddid);
		intent.putExtra("dirct", dirc);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		}
	}
	
	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}
}
