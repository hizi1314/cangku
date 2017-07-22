package com.guantang.cangkuonline.customview;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.activity.CaptureActivity;
import com.guantang.cangkuonline.activity.HpInfoActivity;
import com.guantang.cangkuonline.adapter.HpListBaseAdapter;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.database.DataBaseMethod;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;
import com.guantang.cangkuonline.webservice.WebserviceImport;

public class SearchPopupWindow implements OnClickListener,OnItemClickListener,TextWatcher{
	private PopupWindow popupWindow;
	private Context context;
	private LayoutInflater inflater;
	private View view;
	private EditText mEditText;
	private Button SearchBtn;
	private ListView mListView;
	private ImageButton scanImgBtn;
	private HpListBaseAdapter mySimpleAdapter;
	private ImageView delImgView;
	private String[] str2 = { DataBaseHelper.ID, DataBaseHelper.HPMC,
			DataBaseHelper.HPBM, DataBaseHelper.HPTM, DataBaseHelper.GGXH,
			DataBaseHelper.CurrKC };
	private List<Map<String, Object>> mList;
	private ExecutorService cacheThreadPool = Executors.newCachedThreadPool();
	private ProgressDialog proDialog;
	private DataBaseMethod dm;
	private SharedPreferences mSharedPreferences;
	private PopupWindow.OnDismissListener listener;
	public void setOnDismissListener(PopupWindow.OnDismissListener listener){
    	this.listener=listener;
    }
	public SearchPopupWindow(Context context) {
		this.context = context;
		mSharedPreferences = context.getSharedPreferences(ShareprefenceBean.SHAREPREFENCE_NAME, Context.MODE_PRIVATE);
		inflater = LayoutInflater.from(context);
		view = inflater.inflate(R.layout.popu_search_hp, null);
		dm = new DataBaseMethod(context);
		mList = new ArrayList<Map<String,Object>>();
	}
	public void showPopupWindow(){
		if(popupWindow==null){
			mEditText = (EditText) view.findViewById(R.id.ed_search);
			delImgView = (ImageView) view.findViewById(R.id.del_cha);
			SearchBtn = (Button) view.findViewById(R.id.bt_search);
    		mListView = (ListView) view.findViewById(R.id.list);
    		scanImgBtn = (ImageButton) view.findViewById(R.id.scan);
    		popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, false);
		}
		mListView.setOnItemClickListener(this);
		SearchBtn.setOnClickListener(this);
		scanImgBtn.setOnClickListener(this);
		delImgView.setOnClickListener(this);
		mEditText.addTextChangedListener(this);
		popupWindow.setOnDismissListener(listener);
		
		mySimpleAdapter=new HpListBaseAdapter(context);
		mListView.setAdapter(mySimpleAdapter);
		popupWindow.setFocusable(true); 
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(view, Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
        popupWindow.update();
	}
	
	public Boolean isShow(){
		return popupWindow.isShowing();
	}
	
	public void setAdapter() {
//		mySimpleAdapter.setListData(mList);
	}
	
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		Intent intent = new Intent();
		switch(v.getId()){
		case R.id.bt_search:
			if(mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1)==1){
				if(WebserviceImport.isOpenNetwork(context)){
					if(!mEditText.getText().toString().equals("")){
						proDialog = ProgressDialog.show(context, null, "正在从服务端加在数据");
						cacheThreadPool.execute(searchRunnable);
					}else{
						Toast.makeText(context, "请输入搜索内容", Toast.LENGTH_SHORT).show();
					}
					
				}else{
					Toast.makeText(context, "网络未连接", Toast.LENGTH_SHORT).show();
				}
			}else if(mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1)==-1){
				if(!mEditText.getText().toString().equals("")){
//					mList = dm.queryList(str2, mEditText.getText().toString());
					setAdapter();
				}else{
					Toast.makeText(context, "请输入搜索内容", Toast.LENGTH_SHORT).show();
				}
			}
			
			break;
		case R.id.scan:
			intent.setClass(context, CaptureActivity.class);
			intent.putExtra("SearchPopupWindow_tm", true);
			context.startActivity(intent);
			break;
		case R.id.del_cha:
			mEditText.setText("");
			break;
		}
	}
	
	public void searchTM(String str){
		mEditText.setText(str);
		if(mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1)==1){
			if(WebserviceImport.isOpenNetwork(context)){
				proDialog = ProgressDialog.show(context, null, "正在从服务端加在数据");
				cacheThreadPool.execute(search_tmRunnable);
			}else {
				Toast.makeText(context, "网络未连接", Toast.LENGTH_SHORT).show();
			}
		}else if(mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1)==-1){
			mList = dm.Gethp_tm(str2, mEditText.getText().toString());
			setAdapter();
			if (mList.isEmpty()) {
				Toast.makeText(context, "不存在扫描的货品信息", Toast.LENGTH_LONG).show();
			}
		}
		
	}
	
	private Runnable search_tmRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO 自动生成的方法存根
			Message msg = Message.obtain();
			String id = WebserviceImport.GetHP_ByTM(mEditText.getText()
					.toString(), false,mSharedPreferences);
			mList = WebserviceImport.GetHpInfo_byid(id, -1, str2, new String[] {
					"id", "HPMC", "HPBM","hptm", "GGXH","CurrKc", "kcsl" },mSharedPreferences);
			msg.what = 1;
			mHandler.sendMessage(msg);
		}

	};
	
	private Runnable searchRunnable = new Runnable() {
		@Override
		public void run() {
			Message msg = Message.obtain();
			mList = WebserviceImport.GetHpInfo_top_search(1000000, "0", 1, -1,
					mEditText.getText().toString(), str2, new String[] { "id",
							"HPMC", "HPBM","hptm", "GGXH", "CurrKc", "kcsl" },mSharedPreferences);
			msg.what = 1;
			mHandler.sendMessage(msg);
		}
	};
	private Handler mHandler=new Handler(){
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 1:
				proDialog.dismiss();
				if(mList!=null){
					setAdapter();
				}else{
					Toast.makeText(context, "搜索的货品不存在", Toast.LENGTH_SHORT).show();
				}
				break;
			}
			
		};
	};
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO 自动生成的方法存根
		Map<String,Object> map =(Map<String, Object>) arg0.getAdapter().getItem(arg2);
		Intent intent = new Intent(context,HpInfoActivity.class);
		intent.putExtra("id", map.get(DataBaseHelper.ID).toString());
		context.startActivity(intent);
	}
	@Override
	public void afterTextChanged(Editable s) {
		// TODO 自动生成的方法存根
		s.toString().replace("'", "\'");
		mList.clear();
		mySimpleAdapter.notifyDataSetChanged();
	}
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO 自动生成的方法存根
		
	}
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO 自动生成的方法存根
		if(s.length()>0){
			delImgView.setVisibility(View.VISIBLE);
		}else{
			delImgView.setVisibility(View.GONE);
		}
	}
}
