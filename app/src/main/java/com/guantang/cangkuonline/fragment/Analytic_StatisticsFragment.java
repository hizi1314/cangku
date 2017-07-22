package com.guantang.cangkuonline.fragment;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Highlight;
import com.github.mikephil.charting.utils.ValueFormatter;
import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.adapter.CkListViewAdapter;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.mpchart.custom.MyMarkerView;
import com.guantang.cangkuonline.mpchart.custom.MyValueFormatter_BI;
import com.guantang.cangkuonline.mpchart.custom.MyValueFormatter_QIANBI;
import com.guantang.cangkuonline.mpchart.custom.MyValueFormatter_WANYUAN;
import com.guantang.cangkuonline.mpchart.custom.MyValueFormatter_YUAN;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;
import com.guantang.cangkuonline.webservice.WebserviceImport;
import com.guantang.cangkuonline.webservice.WebserviceImport_more;

public class Analytic_StatisticsFragment extends Fragment implements OnClickListener,OnChartValueSelectedListener{
	private BarChart mChart1;
	private ProgressBar progressBar1;
	private Button cangkubtn,fromtimeBtn,totimeBtn;
	private ImageView searchImgView;
	private TextView coverView;
	private Typeface tf;
	private String[] XAxisItem = {"入库","出库","盘盈","盘亏"};
	private SharedPreferences mSharedPreferences;
	private String[] str1={"HPZL","RKBS","RKJE","CKBS","CKJE","PYBS","PYJE","PKBS","PKJE"};
	private String[] str2 = { "ID", "CKMC", "FZR", "TEL", "ADDR", "inact",
			"outact", "BZ" };
	private String[] str_ck = { DataBaseHelper.ID, DataBaseHelper.CKMC,
			DataBaseHelper.FZR, DataBaseHelper.TEL, DataBaseHelper.ADDR,
			DataBaseHelper.INACT, DataBaseHelper.OUTACT, DataBaseHelper.BZ };
	private List<Map<String, Object>> ckinfo_List = new ArrayList<Map<String,Object>>();
	private List<Map<String, Object>> ck_list = new ArrayList<Map<String,Object>>();
	private List<Map<String, Object>> ckNameAndID = new ArrayList<Map<String,Object>>();
	/**
	 * 引入一个值为1的信号量，防止ckNameAndID装数据的时候就刷新界面
	 */
	private volatile Semaphore mSemaphore = new Semaphore(1);
	private int ckid =-1;
	private ExecutorService cacheThreadpool = Executors.newCachedThreadPool();
	private ProgressDialog pro_Dialog;
	private AlertDialog myDialog;
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	private YAxis yl,yr;
	private Context context;
	
	@Override
	public void onAttach(Activity activity) {
		// TODO 自动生成的方法存根
		super.onAttach(activity);
		context = activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mSharedPreferences = context.getSharedPreferences(ShareprefenceBean.SHAREPREFENCE_NAME, Context.MODE_PRIVATE);
		// TODO 自动生成的方法存根
		View v = inflater.inflate(R.layout.contenttwo, null);
		return v;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onActivityCreated(savedInstanceState);
		cangkubtn = (Button) getView().findViewById(R.id.cangkubtn);
		fromtimeBtn = (Button) getView().findViewById(R.id.fromtimeBtn);
		totimeBtn = (Button) getView().findViewById(R.id.totimeBtn);
		searchImgView = (ImageView) getView().findViewById(R.id.searchBtn);
		coverView = (TextView) getView().findViewById(R.id.coverView);
		progressBar1 = (ProgressBar) getView().findViewById(R.id.progressBar1);
		
		
		fromtimeBtn.setText(formatter.format(new Date(System.currentTimeMillis())));
		totimeBtn.setText(formatter.format(new Date(System.currentTimeMillis())));
		
		
		
		mChart1 = (BarChart) getView().findViewById(R.id.chart1);
		mChart1.setOnChartValueSelectedListener(this);
		mChart1.setDrawBarShadow(false);
        mChart1.setDrawValueAboveBar(true);
        mChart1.setDescription("");
        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart1.setMaxVisibleValueCount(18);

        // scaling can now only be done on x- and y-axis separately
        mChart1.setPinchZoom(false);

        mChart1.setDrawGridBackground(false);

     // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        MyMarkerView mv = new MyMarkerView(context, R.layout.custom_marker_view);

        // define an offset to change the original position of the marker
        // (optional)
        // mv.setOffsets(-mv.getMeasuredWidth() / 2, -mv.getMeasuredHeight());

        // set the marker to the chart
        mChart1.setMarkerView(mv);
        
        tf = Typeface.createFromAsset(context.getAssets(), "OpenSans-Regular.ttf");

        Legend l = mChart1.getLegend();
        l.setPosition(LegendPosition.BELOW_CHART_LEFT);
        l.setTypeface(tf);
        l.setForm(LegendForm.SQUARE);

        XAxis xl = mChart1.getXAxis();
        xl.setTypeface(tf);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(false);
        xl.setPosition(XAxisPosition.BOTTOM);
        xl.setGridLineWidth(0.3f);

       
        
        yl = mChart1.getAxisLeft();
        yl.setTypeface(tf);
        yl.setDrawGridLines(true);
        yl.setTextColor(Color.rgb(104, 241, 175));
        yl.setStartAtZero(true);
        yl.setSpaceTop(10f);
        
        yr = mChart1.getAxisRight();
        yr.setTypeface(tf);
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(false);
        yr.setTextColor(Color.rgb(164, 228, 251));
        yr.setStartAtZero(true);
        yr.setSpaceTop(10f);
//        yr.setInverted(true);

        mChart1.getAxisRight().setEnabled(true);
        mChart1.animateY(2500);
        
        if(mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1)==1){
        	cangkubtn.setOnClickListener(this);
    		fromtimeBtn.setOnClickListener(this);
    		totimeBtn.setOnClickListener(this);
    		searchImgView.setOnClickListener(this);
        	progressBar1.setVisibility(View.VISIBLE);
            if(WebserviceImport.isOpenNetwork(context)){
            	cacheThreadpool.execute(ck_run);
            }else {
            	Toast.makeText(context, "网络未连接", Toast.LENGTH_SHORT).show();
            }
        }else if(mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1)==-1){
        	Animation translate_animation = AnimationUtils.loadAnimation(context, R.anim.push_left_in);
        	coverView.setVisibility(View.VISIBLE);
        	coverView.startAnimation(translate_animation);
        }
	}
	
	public void setData(float[] BSitem, float[] JEitem){
		ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < 4; i++) {
            xVals.add(XAxisItem[i]);
        }

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
//------------当数据为零的时候，也要保证y轴的数值的梯度--------------
        float maxBinum=0;
        for (int i = 0; i < 4; i++) {
        	if(maxBinum<BSitem[i]){
        		maxBinum=BSitem[i];
        	}
            yVals1.add(new BarEntry(BSitem[i], i));
        }
        if(maxBinum<=6){
        	yl.setAxisMaxValue(6);
        }else{
        	yl.setAxisMaxValue(maxBinum*9/7);
        }
        float maxYuannum=0;
        for (int i = 0; i < 4; i++) {
        	if(maxYuannum<JEitem[i]){
        		maxYuannum=JEitem[i];
        	}
            yVals2.add(new BarEntry(JEitem[i], i));
        }
        if(maxYuannum<=6){
        	yr.setAxisMaxValue(6);
        }else{
        	yr.setAxisMaxValue(maxYuannum*9/7);
        }
        //-------根据数值大小变换单位-----------
        if(maxBinum>1000){
        	ValueFormatter custom1 = new MyValueFormatter_QIANBI();
        	yl.setValueFormatter(custom1);
        }else{
        	ValueFormatter custom1 = new MyValueFormatter_BI();
        	yl.setValueFormatter(custom1);
        	
        }
        if(maxYuannum>10000){
        	ValueFormatter custom2 = new MyValueFormatter_WANYUAN();
        	yr.setValueFormatter(custom2);
        }else{
        	ValueFormatter custom2 = new MyValueFormatter_YUAN();
        	yr.setValueFormatter(custom2);
        	
        }
//--------------------------------------------------------
        // create 3 datasets with different types
        BarDataSet set1 = new BarDataSet(yVals1, "单据笔数");
        // set1.setColors(ColorTemplate.createColors(getApplicationContext(),
        // ColorTemplate.FRESH_COLORS));
        set1.setAxisDependency(AxisDependency.LEFT);
        set1.setColor(Color.rgb(104, 241, 175));
        BarDataSet set2 = new BarDataSet(yVals2, "对应金额");
        set2.setColor(Color.rgb(164, 228, 251));
        set2.setAxisDependency(AxisDependency.RIGHT);

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set1);
        dataSets.add(set2);

        BarData data = new BarData(xVals, dataSets);
//        data.setValueFormatter(new LargeValueFormatter());
        
        // add space between the dataset groups in percent of bar-width
        data.setGroupSpace(80f);
        data.setValueTypeface(tf);

        mChart1.setData(data);
        mChart1.invalidate();
	}
	private Runnable ck_run = new Runnable() {

		@Override
		public void run() {
			// TODO 自动生成的方法存根
			synchronized (this) {
				Message msg = new Message();
				ck_list = WebserviceImport.GetCK(str_ck, str2,mSharedPreferences);
				msg.what = 2;
				mHandler.sendMessage(msg);
			}
		}
	};
	Runnable ckinfoLoadRunnable = new Runnable() {
		
		@Override
		public void run() {
			// TODO 自动生成的方法存根
			synchronized (this) {
				Message msg = new Message();
				ckinfo_List = WebserviceImport_more.GtCKHZ(str1, ckid, fromtimeBtn.getText().toString(), totimeBtn.getText().toString(), mSharedPreferences);
				msg.what=1;
				mHandler.sendMessage(msg);
			}
		}
	};
	Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			switch(msg.what){
				case 1:
					progressBar1.setVisibility(View.GONE);
					if(ckinfo_List.isEmpty()){
						mChart1.setDescription("没有数据");
					}else{
						HashMap<String, Object> map = (HashMap<String, Object>) ckinfo_List.get(0);
						float[] BSitem = new float[4];
						float[] JEitem = new float[4];
						BSitem[0]=(float)Float.valueOf(map.get("RKBS").toString());
						BSitem[1]=(float)Float.valueOf(map.get("CKBS").toString());
						BSitem[2]=(float)Float.valueOf(map.get("PYBS").toString());
						BSitem[3]=(float)Float.valueOf(map.get("PKBS").toString());
						JEitem[0]=(float)Float.valueOf(map.get("RKJE").toString());
						JEitem[1]=(float)Float.valueOf(map.get("CKJE").toString());
						JEitem[2]=(float)Float.valueOf(map.get("PYJE").toString());
						JEitem[3]=(float)Float.valueOf(map.get("PKJE").toString());
						setData(BSitem, JEitem);
					}
					break;
				case 2:
					if(!ck_list.isEmpty()){
						ckNameAndID.clear();
						try {
							mSemaphore.acquire();
							Iterator<Map<String, Object>> it=ck_list.iterator();
							while(it.hasNext()){
								Map<String, Object> map2 = new HashMap<String, Object>();
								Map<String, Object> map1 = it.next();
								map2.put("ckmc", map1.get(DataBaseHelper.CKMC).toString());
								map2.put("ckid", map1.get(DataBaseHelper.ID).toString());
								ckNameAndID.add(map2);
							}
							mSemaphore.release();
						} catch (InterruptedException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						}
						cangkubtn.setText(ckNameAndID.get(0).get("ckmc").toString());
						ckid = Integer.valueOf(ckNameAndID.get(0).get("ckid").toString());
						progressBar1.setVisibility(View.VISIBLE);
						cacheThreadpool.execute(ckinfoLoadRunnable);
					}else{
						cangkubtn.setText("没有找到仓库");
						//没有仓库，ckid = -2;
						ckid = -2;
					}
					break;
			}
		};
	};
	@Override
	public void onNothingSelected() {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void onValueSelected(Entry arg0, int arg1, Highlight arg2) {
		// TODO 自动生成的方法存根
		 if (arg0 == null)
	            return;
	        
	        RectF bounds = mChart1.getBarBounds((BarEntry) arg0);
	        PointF position = mChart1.getPosition(arg0, mChart1.getData().getDataSetByIndex(arg1)
	                .getAxisDependency());
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		Calendar calender = Calendar.getInstance();
		switch(v.getId()){
		case R.id.cangkubtn:
			if(ckNameAndID.isEmpty()){
				Toast.makeText(context, "仓库信息还在获取，稍后再试！", Toast.LENGTH_SHORT).show();
			}else{
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				LayoutInflater inflater = LayoutInflater.from(context);
				View view = inflater.inflate(R.layout.popupwindow_list, null);
				ListView myListView = (ListView) view.findViewById(R.id.popuplist);
				CkListViewAdapter ckListViewAdapter = new CkListViewAdapter(context);
				try {
					mSemaphore.acquire();
					ckListViewAdapter.setData(ckNameAndID);
					mSemaphore.release();
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				myListView.setAdapter(ckListViewAdapter);
				myListView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO 自动生成的方法存根
						Map<String, Object> getMap = (Map<String, Object>) arg0.getAdapter().getItem(arg2);
						cangkubtn.setText(getMap.get("ckmc").toString());
						ckid = Integer.parseInt(getMap.get("ckid").toString());
						myDialog.dismiss();
					}
				});
				builder.setView(view);
				myDialog = builder.show();
				
			}
			break;
		case R.id.fromtimeBtn:
			DatePickerDialog dateDialog = new DatePickerDialog(context, new OnDateSetListener() {
				
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear,
						int dayOfMonth) {
					// TODO 自动生成的方法存根
					fromtimeBtn.setText(year+"-"+new DecimalFormat("00").format(monthOfYear+1)+"-"+ new DecimalFormat("00").format(dayOfMonth));
				}
			}, calender.get(Calendar.YEAR), calender.get(Calendar.MONTH), calender.get(Calendar.DAY_OF_MONTH));
			dateDialog.show();
			break;
		case R.id.totimeBtn:
			DatePickerDialog dateDialog1 = new DatePickerDialog(context, new OnDateSetListener() {
				
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear,
						int dayOfMonth) {
					// TODO 自动生成的方法存根
					totimeBtn.setText(year+"-"+new DecimalFormat("00").format(monthOfYear+1)+"-"+ new DecimalFormat("00").format(dayOfMonth));
				}
			}, calender.get(Calendar.YEAR), calender.get(Calendar.MONTH), calender.get(Calendar.DAY_OF_MONTH));
			dateDialog1.show();
			break;
		case R.id.searchBtn:
			if(WebserviceImport.isOpenNetwork(context)){
				progressBar1.setVisibility(View.VISIBLE);
				cacheThreadpool.execute(ckinfoLoadRunnable);
			}else{
				Toast.makeText(context, "网络未连接", Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}
}
