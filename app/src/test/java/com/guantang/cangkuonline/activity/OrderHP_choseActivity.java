package com.guantang.cangkuonline.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.Semaphore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.XListView.XListView;
import com.guantang.cangkuonline.XListView.XListView.IXListViewListener;
import com.guantang.cangkuonline.application.MyApplication;
import com.guantang.cangkuonline.commonadapter.CommonAdapter;
import com.guantang.cangkuonline.commonadapter.ViewHolder;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.database.DataBaseOperateMethod;
import com.guantang.cangkuonline.dialog.DDAddNumberDialog;
import com.guantang.cangkuonline.eventbusBean.ObjectFive;
import com.guantang.cangkuonline.eventbusBean.ObjectSeven;
import com.guantang.cangkuonline.eventbusBean.ObjectSix;
import com.guantang.cangkuonline.helper.DecimalsHelper;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;
import com.guantang.cangkuonline.swipemenulistview.SwipeMenuListView;
import com.guantang.cangkuonline.webservice.WebserviceImport;
import com.guantang.cangkuonline.webservice.WebserviceImport_more;

import de.greenrobot.event.EventBus;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class OrderHP_choseActivity extends Activity implements OnClickListener,
		IXListViewListener, TextWatcher {
	private ImageButton backImageButton, scanImageButton;
	private TextView titleTextView, numbershowTxtView, comfirmBtn;
	private Button deleteBtn, searchBtn2;
	private FrameLayout jianhuokuangLayout;
	private ImageView searchDelBtn;
	private EditText mEditText1;
	private XListView mXListView1;
	private SwipeMenuListView mListView2;
	private LinearLayout mLinearLayout;
	private DataBaseOperateMethod dm_op = new DataBaseOperateMethod(this);
	private HPMyAdapter mMyAdapter;
	private List<Map<String, Object>> myList = new ArrayList<Map<String, Object>>();
	private int dirc = 0;// 0 是采购订单，1是销售订单
	private String ddid = "", ddh = "";
	// 货品信息字段
	private String str[] = { DataBaseHelper.ID, DataBaseHelper.HPMC,
			DataBaseHelper.HPBM, DataBaseHelper.HPTM, DataBaseHelper.GGXH,
			DataBaseHelper.CurrKC };

	// 服务端货品信息字段
	private String str2[] = { "id", "HPMC", "HPBM", "HPTBM", "GGXH", "CurrKc" };
	// 订单明细表字段
	private String[] str1 = { DataBaseHelper.HPID, DataBaseHelper.orderID,
			DataBaseHelper.HPMC, DataBaseHelper.HPBM, DataBaseHelper.GGXH,
			DataBaseHelper.SL, DataBaseHelper.DJ, DataBaseHelper.ZJ,
			DataBaseHelper.ddirc };
	// 服务端订单明细表字段
	private String[] str3 = { "hpid", "orderId", "hpmc", "hpbm", "ggxh", "sl",
			"dj", "zj", "ddirc" };
	
	private Semaphore semaphore = new Semaphore(1);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hp_add);
		EventBus.getDefault().register(this);
		initControl();
		init();
	}

	@Override
	protected void onStart() {
		// TODO 自动生成的方法存根
		super.onStart();
	}

	@Override
	protected void onDestroy() {
		// TODO 自动生成的方法存根
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	public void initControl() {
		backImageButton = (ImageButton) findViewById(R.id.back);
		titleTextView = (TextView) findViewById(R.id.title);
		scanImageButton = (ImageButton) findViewById(R.id.scanImgBtn);
		mEditText1 = (EditText) findViewById(R.id.listtext1);
		mXListView1 = (XListView) findViewById(R.id.hplist1);
		jianhuokuangLayout = (FrameLayout) findViewById(R.id.add_hp);
		comfirmBtn = (TextView) findViewById(R.id.show_hp);
		mLinearLayout = (LinearLayout) findViewById(R.id.tabpager);
		numbershowTxtView = (TextView) findViewById(R.id.numbershow);
		searchDelBtn = (ImageView) findViewById(R.id.del_cha);

		searchDelBtn.setOnClickListener(this);
		backImageButton.setOnClickListener(this);
		scanImageButton.setOnClickListener(this);
		mXListView1.setPullLoadEnable(true);// 设置可以加载更多
		mXListView1.setPullRefreshEnable(true);// 设置可以刷新

		mXListView1.setXListViewListener(this);
		jianhuokuangLayout.setOnClickListener(this);
		comfirmBtn.setOnClickListener(this);
		mEditText1.addTextChangedListener(this);
		mEditText1.setOnClickListener(this);
		mEditText1
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {

					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_SEARCH
								|| (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {

						}
						return false;
					}
				});

		Intent intent = getIntent();
		dirc = intent.getIntExtra("dirc", 0);
		ddh = intent.getStringExtra("ddh");
		ddid = intent.getStringExtra("ddid");

		// 加载本单据已填写的货品种数
		Map<String, Object> map = dm_op.DDhp_Sum(ddid);
		String munstr = map.get("num").toString();
		if (!munstr.equals("0")) {
			numbershowTxtView.setVisibility(View.VISIBLE);
			if (Integer.parseInt(munstr) > 9) {
				numbershowTxtView.setBackgroundResource(R.drawable.numtip2);
			} else if (Integer.parseInt(munstr) > 99) {
				numbershowTxtView.setBackgroundResource(R.drawable.numtip3);
			} else {
				numbershowTxtView.setBackgroundResource(R.drawable.numtip1);
			}
			if (Integer.parseInt(munstr) > 999) {
				numbershowTxtView.setText("999");
			} else {
				numbershowTxtView.setText(munstr);
			}
			comfirmBtn.setTextColor(Color.WHITE);
			comfirmBtn.setBackground(getResources().getDrawable(
					R.drawable.addcomplete));
			comfirmBtn.setClickable(true);// 没有添加成功货品就不能点击
		} else {
			numbershowTxtView.setVisibility(View.GONE);
			comfirmBtn.setTextColor(R.color.ziti1);
			comfirmBtn.setBackgroundColor(getResources().getColor(
					R.color.transparent));
			comfirmBtn.setClickable(false);// 没有添加成功货品就不能点击
		}

	}

	public void init() {
		mMyAdapter = new HPMyAdapter(this, myList, R.layout.listitem2);
		mXListView1.setAdapter(mMyAdapter);
		if (WebserviceImport.isOpenNetwork(this)) {
			new HpLoadAnyctask().execute("10", "0");
		} else {
			Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
		}
	}

	public void onEventMainThread(ObjectFive obj) {
		ListIterator<Map<String, Object>> it = myList.listIterator();
		while (it.hasNext()) {
			Map<String, Object> map = (Map<String, Object>) it.next();
			if (map.get(DataBaseHelper.ID).toString().equals(obj.getHpid())) {
				map.put("caozuoshu", obj.getNum());
				it.set(map);
				break;
			}
		}
		mMyAdapter.notifyDataSetChanged();
	}

	public void onEventMainThread(ObjectSix obj) {
		Map<String, Object> map = dm_op.DDhp_Sum(ddid);
		String munstr = map.get("num").toString();

		if (!munstr.equals("0")) {
			numbershowTxtView.setVisibility(View.VISIBLE);
			if (Integer.parseInt(munstr) > 9) {
				numbershowTxtView.setBackgroundResource(R.drawable.numtip2);
			} else if (Integer.parseInt(munstr) > 99) {
				numbershowTxtView.setBackgroundResource(R.drawable.numtip3);
			} else {
				numbershowTxtView.setBackgroundResource(R.drawable.numtip1);
			}
			if (Integer.parseInt(munstr) > 999) {
				numbershowTxtView.setText("999");
			} else {
				numbershowTxtView.setText(munstr);
			}
			comfirmBtn.setTextColor(Color.WHITE);
			comfirmBtn.setBackground(getResources().getDrawable(
					R.drawable.addcomplete));
			comfirmBtn.setClickable(true);// 没有添加成功货品就不能点击
		} else {
			numbershowTxtView.setVisibility(View.GONE);
			comfirmBtn.setTextColor(R.color.ziti1);
			comfirmBtn.setBackgroundResource(R.color.transparent);
			comfirmBtn.setClickable(false);// 没有添加成功货品就不能点击
		}
	}

	public void onEventMainThread(ObjectSeven obj) {
		// map是数据条数
		Map<String, Object> map = dm_op.DDhp_Sum(ddid);
		String munstr = map.get("num").toString();
		if (!munstr.equals("0")) {
			numbershowTxtView.setVisibility(View.VISIBLE);
			if (Integer.parseInt(munstr) > 9) {
				numbershowTxtView.setBackgroundResource(R.drawable.numtip2);
			} else if (Integer.parseInt(munstr) > 99) {
				numbershowTxtView.setBackgroundResource(R.drawable.numtip3);
			} else {
				numbershowTxtView.setBackgroundResource(R.drawable.numtip1);
			}
			if (Integer.parseInt(munstr) > 999) {
				numbershowTxtView.setText("999");
			} else {
				numbershowTxtView.setText(munstr);
			}
			comfirmBtn.setTextColor(Color.WHITE);
			comfirmBtn.setBackground(getResources().getDrawable(
					R.drawable.addcomplete));
			comfirmBtn.setClickable(true);// 没有添加成功货品就不能点击
		} else {
			numbershowTxtView.setVisibility(View.GONE);
			comfirmBtn.setTextColor(R.color.ziti1);
			comfirmBtn.setBackgroundResource(R.color.transparent);
			comfirmBtn.setClickable(false);// 没有添加成功货品就不能点击
		}
		// 删除的map2
		Map<String, Object> map2 = obj.getMap();

		for (int i = 0; i < myList.size(); i++) {
			if (myList.get(i).get(DataBaseHelper.ID)
					.equals(map2.get(DataBaseHelper.HPID).toString())) {
				myList.get(i).put("caozuoshu", "0");
				break;
			}
		}
		mMyAdapter.notifyDataSetChanged();
	}

	class HpLoadAnyctask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO 自动生成的方法存根
			String jsonString = WebserviceImport_more.GetHpInfo_search_1_0(
					"search", params[0], params[1], "1", "-1", mEditText1.getText()
							.toString().trim());
			return jsonString;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO 自动生成的方法存根
			super.onPostExecute(result);
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(result);
				semaphore.release();
				switch (jsonObject.getInt("Status")) {
				case 1:
					parseJSON(jsonObject);
					break;
				case 2:
					parseJSON(jsonObject);
					Toast.makeText(OrderHP_choseActivity.this,
							jsonObject.getString("Message"), Toast.LENGTH_SHORT)
							.show();
					break;
				case -2:
					Toast.makeText(OrderHP_choseActivity.this,
							jsonObject.getString("Message"), Toast.LENGTH_SHORT)
							.show();
					break;
				case -3:
					Toast.makeText(OrderHP_choseActivity.this,
							jsonObject.getString("Message"), Toast.LENGTH_SHORT)
							.show();
					break;
				case -4:
					Toast.makeText(OrderHP_choseActivity.this,
							jsonObject.getString("Message"), Toast.LENGTH_SHORT)
							.show();
					break;
				default:
					Toast.makeText(OrderHP_choseActivity.this,
							jsonObject.getString("Message"), Toast.LENGTH_SHORT)
							.show();
					break;
				}
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			stopLoadface();
		}
	}

	public void parseJSON(JSONObject jsonObject) throws JSONException{
		JSONObject dataJSONObject = jsonObject
				.getJSONObject("Data");
		JSONArray dsJSONArray = dataJSONObject.getJSONArray("ds");
		int length = dsJSONArray.length();
		for (int i = 0; i < length; i++) {
			JSONObject myJSONObject = dsJSONArray.getJSONObject(i);
			Map<String, Object> map = new HashMap<String, Object>();
			for (int j = 0; j < 6; j++) {
				map.put(str[j], myJSONObject.getString(str2[j]));
			}
			List<Map<String, Object>> list2 = dm_op
					.Gt_OrderDetails(ddid,
							map.get(DataBaseHelper.ID).toString(),
							str1);
			if (!list2.isEmpty()) {
				map.put("caozuoshu",
						list2.get(0).get(DataBaseHelper.SL)
								.toString());
			} else {
				map.put("caozuoshu", "0");
			}
			myList.add(map);
		}

		mMyAdapter.setData(myList);
	}
	
	class SearchHPbyTMAsyncTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO 自动生成的方法存根
			String jsonString = WebserviceImport_more.GetHP_ByTM_1_0(params[0],
					false);
			return jsonString;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO 自动生成的方法存根
			super.onPostExecute(result);
			try {
				JSONObject jsonObject = new JSONObject(result);
				switch (jsonObject.getInt("Status")) {
				case 1:
					JSONObject dataJSONObject = jsonObject
							.getJSONObject("Data");
					JSONArray dsJSONArray = dataJSONObject.getJSONArray("ds");
					int length = dsJSONArray.length();
					for (int i = 0; i < length; i++) {
						JSONObject myJSONObject = dsJSONArray.getJSONObject(i);
						Map<String, Object> map = new HashMap<String, Object>();
						for (int j = 0; j < 6; j++) {
							map.put(str[j], myJSONObject.getString(str2[j]));
						}
						List<Map<String, Object>> list2 = dm_op
								.Gt_OrderDetails(ddid,
										map.get(DataBaseHelper.ID).toString(),
										str1);
						if (!list2.isEmpty()) {
							map.put("caozuoshu",
									list2.get(0).get(DataBaseHelper.SL)
											.toString());
						} else {
							map.put("caozuoshu", "0");
						}
						myList.add(map);
					}
					mMyAdapter.setData(myList);
					DDAddNumberDialog mAddNumberDialog = new DDAddNumberDialog(OrderHP_choseActivity.this, ddh, ddid,
							dirc, myList.get(0),R.style.ButtonDialogStyle);
					mAddNumberDialog.setCanceledOnTouchOutside(false);
					Window win = mAddNumberDialog.getWindow();
					win.getDecorView().setPadding(0, 0, 0, 0);
					win.setGravity(Gravity.BOTTOM);
					WindowManager.LayoutParams lp = win.getAttributes();
					lp.width = WindowManager.LayoutParams.MATCH_PARENT;
					lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
					win.setAttributes(lp);
					mAddNumberDialog.show();
					break;
				case -1:
					Toast.makeText(OrderHP_choseActivity.this,
							jsonObject.getString("Message"), Toast.LENGTH_SHORT)
							.show();
					break;
				case -2:
					Toast.makeText(OrderHP_choseActivity.this,
							jsonObject.getString("Message"), Toast.LENGTH_SHORT)
							.show();
					break;
				case -3:
					Toast.makeText(OrderHP_choseActivity.this,
							jsonObject.getString("Message"), Toast.LENGTH_SHORT)
							.show();
					break;
				default:
					Toast.makeText(OrderHP_choseActivity.this,
							jsonObject.getString("Message"), Toast.LENGTH_SHORT)
							.show();
					break;
				}
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.scanImgBtn:
			intent.setClass(this, CaptureActivity.class);
			startActivityForResult(intent, 1);
			break;
		case R.id.add_hp:
			intent.setClass(this, OrderDetailActivity.class);
			intent.putExtra("ddh", ddh);
			intent.putExtra("ddid", ddid);
			intent.putExtra("dirc", dirc);
			startActivity(intent);
			break;
		case R.id.show_hp:
			finish();
			break;
		case R.id.listtext1:
			intent.setClass(this, SearchActivity.class);
//			intent.putExtra("HP_choseActivityStart", (Boolean) true);
			intent.putExtra("hint", 1);
			startActivityForResult(intent, 2);
			break;
		case R.id.del_cha:
			mEditText1.setText("");
			break;

		}
	}

	@Override
	public void onRefresh() {
		// TODO 自动生成的方法存根
		onLoadTime();
		if (WebserviceImport.isOpenNetwork(this)) {
			myList.clear();
			try {
				semaphore.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			new HpLoadAnyctask().execute("10", "0");
		} else {
			stopLoadface();
			Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onLoadMore() {
		// TODO 自动生成的方法存根
		onLoadTime();
		if (WebserviceImport.isOpenNetwork(this)) {
			if (myList.isEmpty()) {
				try {
					semaphore.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				new HpLoadAnyctask().execute("10", "0");
			} else {
				new HpLoadAnyctask().execute("10", myList
						.get(myList.size() - 1).get(DataBaseHelper.ID)
						.toString());
			}
		} else {
			stopLoadface();
			Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
		}
	}

	public void onLoadTime() {
		Calendar c = Calendar.getInstance();
		String year = String.valueOf(c.get(Calendar.YEAR));
		String month = String.valueOf(new DecimalFormat("00").format(c
				.get(Calendar.MONTH) + 1));
		String day = String.valueOf(new DecimalFormat("00").format(c
				.get(Calendar.DAY_OF_MONTH)));
		String hour = String.valueOf(new DecimalFormat("00").format(c
				.get(Calendar.HOUR_OF_DAY)));
		String minute = String.valueOf(new DecimalFormat("00").format(c
				.get(Calendar.MINUTE)));
		String refreshDate = year + "-" + month + "-" + day + " " + hour + ":"
				+ minute;
		mXListView1.setRefreshTime(refreshDate);
	}

	/**
	 * 停止界面加载动画
	 * */
	public void stopLoadface() {
		mXListView1.stopRefresh();
		mXListView1.stopLoadMore();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO 自动生成的方法存根
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 2) {
			if (resultCode == 1) {
				if (data.getIntExtra("scanOrsearch", -1) == 1) {// 判断是否从搜索页面跳转过来的，1是从搜索页面的输入框条转过来的，2是从搜索页面的扫描条转过来的，-1是直接打开这个界面
					if (WebserviceImport.isOpenNetwork(this)) {
						mEditText1.setText(data.getStringExtra("searchString"));
						new HpLoadAnyctask().execute("10", "0");
					} else {
						Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT)
								.show();
					}
				} else if (data.getIntExtra("scanOrsearch", -1) == 2) {// 判断是否从搜索页面跳转过来的，1是从搜索页面的输入框条转过来的，2是从搜索页面的扫描条转过来的，-1是直接打开这个界面
					if (WebserviceImport.isOpenNetwork(this)) {
						mEditText1.setText(data.getStringExtra("searchString"));
						new SearchHPbyTMAsyncTask().execute(mEditText1
								.getText().toString());
					} else {
						Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT)
								.show();
					}
				}
			}
		} else if (requestCode == 1) {
			if (resultCode == 1) {
				if (WebserviceImport.isOpenNetwork(this)) {
					mEditText1.setText(data.getStringExtra("scan_tm"));
					new SearchHPbyTMAsyncTask().execute(mEditText1.getText()
							.toString());
				} else {
					Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO 自动生成的方法存根
		s.toString().replace("'", "");
		myList.clear();
		mMyAdapter.notifyDataSetChanged();
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO 自动生成的方法存根
		if (s.length() > 0) {
			searchDelBtn.setVisibility(View.VISIBLE);
		} else {
			searchDelBtn.setVisibility(View.GONE);
		}
	}

	class HPMyAdapter extends CommonAdapter<Map<String, Object>> {

		public HPMyAdapter(Context mContext, List<Map<String, Object>> mList,
				int LayoutId) {
			super(mContext, mList, LayoutId);
			// TODO 自动生成的构造函数存根
		}

		@Override
		public void convert(ViewHolder holder, final Map<String, Object> item) {
			// TODO 自动生成的方法存根
			TextView itemnameTxtView = holder.getView(R.id.itemname);
			TextView itemcodeTxtView = holder.getView(R.id.itemcode);
			TextView itemggTxtView = holder.getView(R.id.itemgg);
			TextView itemnumTxtView = holder.getView(R.id.itemnum);
			TextView caozuoNameTxtView = holder.getView(R.id.caozuoNameText);
			TextView caozuoshuTxtView = holder.getView(R.id.caozuoshu);
			ImageButton ImgBtn = holder.getView(R.id.showBtn);
			LinearLayout caozuoshuLayout = holder.getView(R.id.caozuoshulayout);

			Object hpmcObject = item.get(DataBaseHelper.HPMC);
			itemnameTxtView.setText(hpmcObject == null || hpmcObject.equals("null") ? "" : hpmcObject
					.toString());

			Object hpbmObject = item.get(DataBaseHelper.HPBM);
			itemcodeTxtView.setText(hpbmObject == null || hpbmObject.equals("null")? "" : hpbmObject
					.toString());

			Object ggxhObject = item.get(DataBaseHelper.GGXH);
			itemggTxtView.setText(ggxhObject == null || ggxhObject.equals("null")? "" : ggxhObject
					.toString());

			Object hpzkcObject = item.get(DataBaseHelper.CurrKC);
			itemnumTxtView.setText(hpzkcObject == null || hpzkcObject.equals("null")? "" : DecimalsHelper
					.Transfloat(Double.parseDouble(hpzkcObject.toString()),
							MyApplication.getInstance().getNumPoint()));

			if (dirc == 0) {
				caozuoNameTxtView.setText("采购数量:");
			} else if (dirc == 1) {
				caozuoNameTxtView.setText("销售数量");
			}

			String numstr = item.get("caozuoshu").toString();

			if (numstr.equals("0")) {
				ImgBtn.setBackgroundResource(R.drawable.hpadd);
				caozuoshuLayout.setVisibility(View.INVISIBLE);
			} else {
				caozuoshuLayout.setVisibility(View.VISIBLE);
				ImgBtn.setBackgroundResource(R.drawable.hpmodify);
			}
			caozuoshuTxtView.setText(numstr);

			holder.getConvertView().setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO 自动生成的方法存根
					Intent intent = new Intent(context,
							DDHpListDetailsWriteActivity.class);
					intent.putExtra("hpid", item.get(DataBaseHelper.ID)
							.toString());
					intent.putExtra("ddh", ddh);
					intent.putExtra("ddid", ddid);
					intent.putExtra("dirc", dirc);
					context.startActivity(intent);
				}
			});

			ImgBtn.setOnClickListener(new ListViewButtonOnClikListener(item));

		}

		class ListViewButtonOnClikListener implements OnClickListener {

			private Map<String, Object> item;

			public ListViewButtonOnClikListener(Map<String, Object> item) {
				this.item = item;
			}

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根

				DDAddNumberDialog mAddNumberDialog = new DDAddNumberDialog(
						context, ddh, ddid, dirc, item,
						R.style.ButtonDialogStyle);
				mAddNumberDialog.setCanceledOnTouchOutside(false);
				Window win = mAddNumberDialog.getWindow();
				win.getDecorView().setPadding(0, 0, 0, 0);
				win.setGravity(Gravity.BOTTOM);
				WindowManager.LayoutParams lp = win.getAttributes();
				lp.width = WindowManager.LayoutParams.MATCH_PARENT;
				lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
				win.setAttributes(lp);
				mAddNumberDialog.show();

			}
		}

	}

}
