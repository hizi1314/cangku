package com.guantang.cangkuonline.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.activity.DJMXCheckActivity;
import com.guantang.cangkuonline.activity.HistoryDJ_DetailsActivity;
import com.guantang.cangkuonline.activity.LocalDJActivity;
import com.guantang.cangkuonline.adapter.MyDJAdapter;
import com.guantang.cangkuonline.application.MyApplication;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.database.DataBaseOperateMethod;
import com.guantang.cangkuonline.helper.RightsHelper;
import com.guantang.cangkuonline.swipemenulistview.SwipeMenu;
import com.guantang.cangkuonline.swipemenulistview.SwipeMenuCreator;
import com.guantang.cangkuonline.swipemenulistview.SwipeMenuItem;
import com.guantang.cangkuonline.swipemenulistview.SwipeMenuListView;
import com.guantang.cangkuonline.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.guantang.cangkuonline.webservice.WebserviceHelper;
import com.guantang.cangkuonline.webservice.WebserviceImport;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

public class NoUpdateDJFragment extends Fragment implements
		OnMenuItemClickListener,OnClickListener,OnItemClickListener {

	private List<Map<String, Object>> mlist = new ArrayList<Map<String, Object>>();
	private SwipeMenuListView myListView;
	private LinearLayout alluploadLayout;
	private SharedPreferences mSharedPreferences;
	private ProgressDialog progDialog,allLoaDialog;
	private String uploadDJID = "", dh = "", dacttype = "";
	private int op_type = 1, ckid = -1;
	private ExecutorService cacheThreadPool = Executors.newCachedThreadPool();
	private String[] strs1 = { DataBaseHelper.MVDH, DataBaseHelper.MVDT,
			DataBaseHelper.DWName, DataBaseHelper.JBR, DataBaseHelper.BZ,
			DataBaseHelper.mType, DataBaseHelper.actType,
			DataBaseHelper.DepName, DataBaseHelper.DepID, DataBaseHelper.CKMC,
			DataBaseHelper.CKID, DataBaseHelper.Lrr, DataBaseHelper.Lrsj,
			DataBaseHelper.DWID, DataBaseHelper.Details, DataBaseHelper.HPzj };
	private String[] strs2 = { "mvdh", "mvdt", "dwName", "jbr", "bz", "mType",
			"actType", "depName", "depId", "ckName", "ckid", "lrr", "lrsj",
			"dwid", "hpDetails", "hpzjz" };
	private String[] strs3 = { DataBaseHelper.HPID, DataBaseHelper.MVDDATE,
			DataBaseHelper.MSL, DataBaseHelper.MVDType, DataBaseHelper.DH,
			DataBaseHelper.MVDirect, DataBaseHelper.DJ, DataBaseHelper.ZJ,
			DataBaseHelper.MVType, DataBaseHelper.CKID, DataBaseHelper.BTKC,
			DataBaseHelper.ATKC };
	private String[] strs4 = { "hpid", "mvddt", "msl", "mdType", "mvddh",
			"mddirect", "dj", "zj", "dactType", "ckid", "Btkc", "Atkc" };
	private DataBaseOperateMethod dm_op;
	private MyDJAdapter DJadapter;
	private Context context;
	
	public interface RefreshInterface{
		void execute();
	}
	public RefreshInterface mRefreshInterface;
	

	public static NoUpdateDJFragment getInstance(List<Map<String, Object>> mList) {
		NoUpdateDJFragment nudj = new NoUpdateDJFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("list", (Serializable) mList);
		nudj.setArguments(bundle);
		return nudj;
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
		mlist = (List<Map<String, Object>>) getArguments().getSerializable(
				"list");
		mSharedPreferences = MyApplication.getInstance().getSharedPreferences();
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
		alluploadLayout = (LinearLayout) getView().findViewById(R.id.alluploadLayout);
		alluploadLayout.setVisibility(View.VISIBLE);
		alluploadLayout.setOnClickListener(this);
		init();
	}

	public void init() {
		DJadapter = new MyDJAdapter(context);
		myListView.setAdapter(DJadapter);
		DJadapter.setData(mlist);
		SwipeMenuCreator creator = new SwipeMenuCreator() {
			@Override
			public void create(SwipeMenu menu) {
				if (menu.getViewType() == -1) {
					createUpLoadMenu(menu);
					createDeleteMenu(menu);
				}
			}
		};
		myListView.setMenuCreator(creator);
		myListView.setOnMenuItemClickListener(this);
		myListView.setOnItemClickListener(this);
	}

	public void createUpLoadMenu(SwipeMenu menu) {
		SwipeMenuItem UploadItem = new SwipeMenuItem(context);
		// set item background
		UploadItem.setBackground(getResources().getDrawable(R.color.grey));
		// set item width
		UploadItem.setWidth(dp2px(90));
		//
		UploadItem.setTitle("上传");
		// set item title fontsize
		UploadItem.setTitleSize(18);
		// set item title font color
		UploadItem.setTitleColor(Color.WHITE);
		// set a icon
		UploadItem.setIcon(R.drawable.gpc);
		// add to menu
		menu.addMenuItem(UploadItem);
	}

	public void createDeleteMenu(SwipeMenu menu) {
		// create "delete" item
		SwipeMenuItem deleteItem = new SwipeMenuItem(context);
		// set item background
		deleteItem
				.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
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
		if (menu.getViewType() == -1) {
			switch(index){
			case 0:
				if (WebserviceImport.isOpenNetwork(context)) {
					String s1 = "", s2 = "", s3 = "";
					if (RightsHelper.isHaveRight(RightsHelper.dj_rk_add,
							mSharedPreferences) == false) {
						s1 = "入库单据  ";
					}
					if (RightsHelper.isHaveRight(RightsHelper.dj_ck_add,
							mSharedPreferences) == false) {
						s2 = "出库单据  ";
					}
					if (RightsHelper.isHaveRight(RightsHelper.dj_pd_add,
							mSharedPreferences) == false) {
						s3 = "盘点单据";
					}
					if (s1.equals("") && s2.equals("") && s3.equals("")) {
						progDialog = ProgressDialog.show(context, null,
								"正在上传数据");
						uploadDJID = map.get(DataBaseHelper.HPM_ID).toString();
						op_type = Integer.valueOf(map.get(DataBaseHelper.mType)
								.toString());
						ckid = Integer.valueOf(map.get(DataBaseHelper.CKID)
								.toString());
						dh = map.get(DataBaseHelper.MVDH).toString();
						if (map.get(DataBaseHelper.actType) != null) {
							dacttype = map.get(DataBaseHelper.actType).toString();
						}
						cacheThreadPool.execute(addRun);
						// if(map.get(DataBaseHelper.DJType).equals("-1")){
						//
						// }else if(map.get(DataBaseHelper.DJType).equals("1")){
						// Toast.makeText(this, "单据已上传", Toast.LENGTH_SHORT).show();
						// }else if(map.get(DataBaseHelper.DJType).equals("0")){
						// Toast.makeText(this, "请完成单据在上传",
						// Toast.LENGTH_SHORT).show();
						// }
					} else {
						AlertDialog.Builder builder = new AlertDialog.Builder(
								context);
						builder.setMessage("对不起，你没有" + s1 + s2 + s3 + "的添加权限。");
						builder.setNegativeButton("确认",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										dialog.dismiss();
									}
								});
						builder.create().show();
					}
				} else {
					Toast.makeText(context, "网络未连接……", Toast.LENGTH_SHORT)
							.show();
				}
				break;
			case 1:
				dm_op.Del_Moved(map.get(DataBaseHelper.HPM_ID).toString());
				dm_op.Del_Movem(map.get(DataBaseHelper.HPM_ID).toString());
				mlist.remove(map);
				DJadapter.setData(mlist);
				break;
			}
			
		}
		return false;
	}

	Runnable addRun = new Runnable() {

		@Override
		public void run() {
			Message msg = new Message();
			String[] values_movem = new String[strs1.length];
			List<Map<String, Object>> ls2 = dm_op.Gt_Movem(uploadDJID, strs1);
			if (ls2 != null) {
				for (int j = 0; j < strs1.length; j++) {
					values_movem[j] = (String) ls2.get(0).get(strs1[j]);
				}
			}
			List<Map<String, Object>> lss = dm_op.Gt_Moved(uploadDJID, strs3);
			String[] values_moved = new String[lss.size() * strs3.length];
			for (int j = 0; j < lss.size(); j++) {
				for (int n = 0; n < strs3.length; n++) {
					values_moved[j * strs3.length + n] = (String) lss.get(j)
							.get(strs3[n]);
				}
			}
			int flag = -1;
			try {
				String type = (String) ls2.get(0).get(DataBaseHelper.mType);
				switch (Integer.parseInt(type)) {
				case 0:
					flag = WebserviceImport.AddDJ(values_movem, strs2,
							values_moved, strs4, WebserviceHelper.AddPD,
							mSharedPreferences);
					break;
				case 1:
					flag = WebserviceImport.AddDJ(values_movem, strs2,
							values_moved, strs4, WebserviceHelper.AddRK,
							mSharedPreferences);
					break;
				case 2:
					flag = WebserviceImport.AddChuKu(values_movem, strs2,
							values_moved, strs4, false, mSharedPreferences);
					break;

				}
			} catch (Exception e) {
				flag = -1;
			}
			if (flag > 0) {
				dm_op.Update_DJtype(uploadDJID, 1);// type
													// 0代表没有保存，1代表上传和保存本地，-1代表保存本地没上传
			} else {
				dm_op.Update_DJtype(uploadDJID, -1);
			}
			msg.what = flag;
			msg.setTarget(mHandler);
			mHandler.sendMessage(msg);
		}
	};

	Runnable addRun2 = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub

			Message msg = new Message();
			String[] values_movem = new String[strs1.length];
			List<Map<String, Object>> ls2 = dm_op.Gt_Movem(uploadDJID, strs1);
			if (ls2 != null) {
				for (int j = 0; j < strs1.length; j++) {
					values_movem[j] = (String) ls2.get(0).get(strs1[j]);
				}
			}
			List<Map<String, Object>> lss = dm_op.Gt_Moved(uploadDJID, strs3);
			String[] values_moved = new String[lss.size() * strs3.length];
			for (int j = 0; j < lss.size(); j++) {
				for (int n = 0; n < strs3.length; n++) {
					values_moved[j * strs3.length + n] = (String) lss.get(j)
							.get(strs3[n]);
				}
			}
			int flag;
			try {
				String type = (String) ls2.get(0).get(DataBaseHelper.mType);
				switch (Integer.parseInt(type)) {
				case 0:
					flag = WebserviceImport.AddDJ(values_movem, strs2,
							values_moved, strs4, WebserviceHelper.AddPD,
							mSharedPreferences);
					break;
				case 1:
					flag = WebserviceImport.AddDJ(values_movem, strs2,
							values_moved, strs4, WebserviceHelper.AddRK,
							mSharedPreferences);
					break;
				case 2:
					flag = WebserviceImport.AddChuKu(values_movem, strs2,
							values_moved, strs4, true, mSharedPreferences);
					break;
				default:
					flag = -1;
					break;
				}
			} catch (Exception e) {
				flag = -1;
			}
			if (flag > 0) {
				dm_op.Update_DJtype(uploadDJID, 1);// type
													// 0代表没有保存，1代表上传和保存本地，-1代表保存本地没上传
			} else {
				dm_op.Update_DJtype(uploadDJID, -1);
			}
			msg.what = flag;
			msg.setTarget(mHandler);
			mHandler.sendMessage(msg);
		}
	};

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			progDialog.dismiss();
			if (msg.what > 0) {
				for (int i = 0; i < mlist.size(); i++) {
					if (mlist.get(i).get(DataBaseHelper.HPM_ID).toString().equals(uploadDJID)) {
						mlist.get(i).put(DataBaseHelper.DJType, 1);
						break;
					}
				}
				DJadapter.setData(mlist);
				uploadDJID = "";
			} else if (msg.what == -501) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						context);
				builder.setMessage("有货品不足，是否继续出库？");
				builder.setNegativeButton("允许出库",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
								if (WebserviceImport
										.isOpenNetwork(context)) {
									progDialog = ProgressDialog.show(
											context, null, "正在上传数据");
									cacheThreadPool.execute(addRun2);
								} else {
									Toast.makeText(context, "网络未连接",
											Toast.LENGTH_SHORT).show();
								}
							}
						});
				
				builder.setPositiveButton("禁止出库",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								Intent intent = new Intent(context,
										DJMXCheckActivity.class);
								intent.putExtra("op_type", op_type);
								intent.putExtra("djid", uploadDJID);
								intent.putExtra("ckid", ckid);
								intent.putExtra("dh", dh);
								intent.putExtra("dacttype", dacttype);
								startActivity(intent);
								dialog.dismiss();
							}
						});
				builder.create().show();
			} else if (msg.what == -502) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						context);
				builder.setMessage("账面数有误,请修改盘点单后再上传。");
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO 自动生成的方法存根
								dialog.dismiss();
							}
						});
				builder.setPositiveButton("修改账面数",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO 自动生成的方法存根
								Intent intent = new Intent(context,
										DJMXCheckActivity.class);
								intent.putExtra("op_type", op_type);
								intent.putExtra("djid", uploadDJID);
								intent.putExtra("ckid", ckid);
								intent.putExtra("dh", dh);
								intent.putExtra("dacttype", dacttype);
								startActivity(intent);
							}
						});
				builder.create().show();
			}else if(msg.what == -500){
				AlertDialog.Builder builder = new AlertDialog.Builder(
						context);
				builder.setMessage("库存不足,禁止出库,请修改单据后再上传。");
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});
				
				builder.setPositiveButton("修改",
						new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								Intent intent = new Intent(context,
										DJMXCheckActivity.class);
								intent.putExtra("op_type", op_type);
								intent.putExtra("djid", uploadDJID);
								intent.putExtra("ckid", ckid);
								intent.putExtra("dh", dh);
								intent.putExtra("dacttype", dacttype);
								startActivity(intent);
								dialog.dismiss();
							}
						});
				builder.create().show();
				
			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						context);
				switch (msg.what) {
				case -101:
					builder.setMessage("仓库信息有误,请更新仓库信息后再上传。");
					break;
				case -102:
					builder.setMessage("出入库类型有误,请更新参数信息后再上传。");
					break;
				case -103:
					builder.setMessage("单号已存在,请修改单号后再上传。");
					break;
				case -104:
					builder.setMessage("单据日期错误,禁止插入单据,请修改单据后再上传。");
					break;
				case -3:
					builder.setMessage("帐号信息验证错误。");
					break;
				case -2:
					builder.setMessage("权限不足,不能上传单据。");
					break;
				case -10:
					builder.setMessage("服务端异常。");
					break;
				default:
					builder.setMessage("提交失败");
					break;
				}
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});
				builder.create().show();
			}
		};
	};

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch(v.getId()){
		case R.id.alluploadLayout:
			allLoaDialog = new ProgressDialog(context);
			allLoaDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			allLoaDialog.setTitle("正在上传按条件筛选的单据");
			allLoaDialog.setMax(mlist.size());
			allLoaDialog.setProgress(0);
			allLoaDialog.setIndeterminate(false); 
			allLoaDialog.setIndeterminate(false);
			allLoaDialog.setCancelable(false);
			allLoaDialog.show();
			new allUpLoadAsyncTask().executeOnExecutor(cacheThreadPool,mlist);
			break;
		}
	}
	
	class allUpLoadAsyncTask extends AsyncTask<List<Map<String,Object>>, Integer, Void>{
		int progress = 0;
		int success = 0;
		int fail = 0;
		@Override
		protected Void doInBackground(List<Map<String,Object>>... params){
			// TODO 自动生成的方法存根
			ListIterator<Map<String, Object>> it=params[0].listIterator();
			while(it.hasNext()){
				progress++;
				Map<String, Object> map = it.next();
				String thisDJid = map.get(DataBaseHelper.HPM_ID).toString();
				String[] values_movem = new String[strs1.length];
				List<Map<String, Object>> ls2 = dm_op.Gt_Movem(thisDJid, strs1);
				if (ls2 != null) {
					for (int j = 0; j < strs1.length; j++) {
						values_movem[j] = (String) ls2.get(0).get(strs1[j]);
					}
				}
				List<Map<String, Object>> lss = dm_op.Gt_Moved(thisDJid, strs3);
				String[] values_moved = new String[lss.size() * strs3.length];
				for (int j = 0; j < lss.size(); j++) {
					for (int n = 0; n < strs3.length; n++) {
						values_moved[j * strs3.length + n] = (String) lss.get(j)
								.get(strs3[n]);
					}
				}
				int flag = -1;
				try {
					String type = (String) ls2.get(0).get(DataBaseHelper.mType);
					switch (Integer.parseInt(type)) {
					case 0:
						flag = WebserviceImport.AddDJ(values_movem, strs2,
								values_moved, strs4, WebserviceHelper.AddPD,mSharedPreferences);
						break;
					case 1:
						flag = WebserviceImport.AddDJ(values_movem, strs2,
								values_moved, strs4, WebserviceHelper.AddRK,mSharedPreferences);
						break;
					case 2:
						flag = WebserviceImport.AddChuKu(values_movem, strs2,
								values_moved, strs4, false,mSharedPreferences);
						break;

					}
				} catch (Exception e) {
					flag = -1;
				}
				if (flag > 0) {
					dm_op.Update_DJtype(thisDJid, 1);// type 0代表没有保存，1代表上传和保存本地，-1代表保存本地没上传
					map.put(DataBaseHelper.DJType,1);
					it.set(map);
				} else {
					dm_op.Update_DJtype(thisDJid, -1);
					map.put(DataBaseHelper.DJType,-1);
					it.set(map);
				}
				publishProgress(flag);
			}
			return null;
		}
		protected void onProgressUpdate(Integer[] values) {
			if(values[0]>0){
				success +=1;
			}else{
				fail +=1;
			}
			allLoaDialog.setProgress(GtProgress(progress, mlist.size()));
		};
		protected void onPostExecute(Void result) {
			allLoaDialog.dismiss();
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			if(progress>0){
				if(fail>0){
					builder.setMessage("上传成功"+success+"笔单据，上传失败"+fail+"笔单据。失败的的单据请单独上传！");
				}else{
					builder.setMessage("单据全部上传成功");
				}
			}else{
				builder.setMessage("当前条件下，没有可以上传的单据！");
			}
			builder.setPositiveButton("确认",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							// TODO Auto-generated method stub
							mRefreshInterface = (RefreshInterface) context;
							mRefreshInterface.execute();
							dialog.dismiss();
						}
					});
			builder.create().show();
		};
	};

	
	
	private int GtProgress(int now, int total) {
		if (now == total) {
			return 100;
		} else {
			return now * 100 / total;
		}
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
