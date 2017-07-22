package com.guantang.cangkuonline.application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.activity.FinalMainActivity;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.database.DataBaseImport;
import com.guantang.cangkuonline.database.DataBaseOperateMethod;
import com.guantang.cangkuonline.helper.GenerateIMEI;
import com.guantang.cangkuonline.helper.RightsHelper;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;
import com.guantang.cangkuonline.static_constant.PathConstant;
import com.guantang.cangkuonline.webservice.WebserviceHelper;
import com.guantang.cangkuonline.webservice.WebserviceImport;
import com.umeng.fb.FeedbackAgent;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Looper;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

/*
 * Java中已经提供了一个接口Thread.UncaughtExceptionHandler来对运行时的异常进行处理。
 * */
//implements UncaughtExceptionHandler
public class MyApplication extends Application implements UncaughtExceptionHandler{
	private CheckBox hpxinxiCheckBox,hpleixinCheckBox,dwxinxiCheckBox,canshuxinxiCheckBox,cangkuxinxiCheckBox;
	
	private int numPoint=8,djPoint=2,jePoint=2;
	private DataBaseOperateMethod dm_op;
	private String[] str6={DataBaseHelper.GID,DataBaseHelper.ItemID,DataBaseHelper.ItemV,DataBaseHelper.Ord};
	private SharedPreferences mSharedPreferences;
	/*手机串号
	 * **/
	private String IMEI="";
	/*是否显示询问更新dialog,在本次打开app的时候已经更新了，之后就不在显示dialog.
	 * */
	private boolean showupDataFlag = true;
//	/*自定义SQLiteOpenHelper对象
//	 **/
//	private DataBaseImport data = null;
	
	private static MyApplication mInstance ;
	public static MyApplication getInstance() {
		return mInstance;
	}
	
	@Override
	public void onCreate() {// 程序创建的时候执行
		// TODO 自动生成的方法存根
		super.onCreate();
		mInstance = this;
		mSharedPreferences = getSharedPreferences(ShareprefenceBean.SHAREPREFENCE_NAME, Context.MODE_PRIVATE);
		Thread.setDefaultUncaughtExceptionHandler(this);
		dm_op = new DataBaseOperateMethod(getApplicationContext());
//		getDataBaseImport();
		createDirs();
		setALLPoint();
		generateIEMI();
//		FeedbackAPI.init(this, "23514936");
	}
	@Override
	public void onTerminate() {// 程序终止的时候执行
		// TODO 自动生成的方法存根
		super.onTerminate();
		new Thread().start();
	}
	@Override
	public void onLowMemory() {// 低内存的时候执行
		// TODO 自动生成的方法存根
		super.onLowMemory();
	}
	@Override
	public void onTrimMemory(int level) {// 程序在内存清理的时候执行
		// TODO 自动生成的方法存根
		super.onTrimMemory(level);
		new Thread().start();
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {//配置改变时触发这个方法
		// TODO 自动生成的方法存根
		super.onConfigurationChanged(newConfig);
	}
//----------------------------------------------------------------------------------------------
	
	public SharedPreferences getSharedPreferences(){
		return mSharedPreferences;
	}
	
	public void createDirs() {
		File file;
		file = new File(PathConstant.PATH_photo);
		if (!file.exists()) {
			file.mkdirs();
		}
		file = new File(PathConstant.PATH_backup);
		if (!file.exists()) {
			file.mkdirs();
		}
		file = new File(PathConstant.PATH_system);
		if (!file.exists()) {
			file.mkdirs();
		}
		file = new File(PathConstant.PATH_cachephoto);
		if(!file.exists()){
			file.mkdirs();
		}
	}
	
	public void setALLPoint(){
		List<Map<String, Object>> mlist = dm_op.Gt_ConfByGID("财务信息", str6);
		if(!mlist.isEmpty()){
			Iterator<Map<String, Object>> it = mlist.iterator();
			while (it.hasNext()) {
				Map<String, Object> map = it.next();
				String itemIDString = map.get(DataBaseHelper.ItemID).toString();
				if(itemIDString.equals("数量小数位数")){
					if(map.get(DataBaseHelper.ItemV)!=null){
						if(map.get(DataBaseHelper.ItemV).toString().matches("\\d+?")){
							numPoint=Integer.parseInt(map.get(DataBaseHelper.ItemV).toString());
						}else{
							numPoint=8;
						}
					}
				}else if(itemIDString.equals("单价小数位数")){
					if(map.get(DataBaseHelper.ItemV)!=null){
						if(map.get(DataBaseHelper.ItemV).toString().matches("\\d+?")){
							djPoint=Integer.parseInt(map.get(DataBaseHelper.ItemV).toString());
						}else{
							djPoint=2;
						}
					}
				}else if(itemIDString.equals("小数位数")){
					if(map.get(DataBaseHelper.ItemV)!=null){
						if(map.get(DataBaseHelper.ItemV).toString().matches("\\d+?")){
							jePoint=Integer.parseInt(map.get(DataBaseHelper.ItemV).toString());
						}else{
							jePoint=2;
						}
					}
				}
			}
		}
	}
	
	public int getNumPoint(){
		return numPoint;
	}
	public int getDjPoint(){
		return djPoint;
	}
	public int getJePoint(){
		return jePoint;
	}
	
	public boolean getshowupDataFlag(){
		return showupDataFlag;
	}
	public void setshowupDataFlag(boolean flag){
		showupDataFlag = flag;
	}
	
//	/*生成数据库对象
//	 * **/
//	public DataBaseImport getDataBaseImport(){
//		if(data==null){
//			data = DataBaseImport.getInstance(getApplicationContext());
//		}
//		return data;
//	}
	
	/*获取app版本号
	 * */
	public int getVisionCode() {
		PackageManager manager = this.getPackageManager();
		PackageInfo info = null;
		try {
			info = manager.getPackageInfo(this.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int version = info.versionCode;
		return version;
	}
	
	/*获取app版本名称
	 * */
	public String getVisionName() {
		PackageManager manager = this.getPackageManager();
		PackageInfo info = null;
		try {
			info = manager.getPackageInfo(this.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return info.versionName;
	}
	
	/**
	 * 获取或生成手机串号
	 * */
	public void generateIEMI(){
		IMEI = ((TelephonyManager) getApplicationContext().getSystemService(TELEPHONY_SERVICE)).getDeviceId();
		if(IMEI==null || IMEI.equals("")){
			File file = new File(PathConstant.PATH_system+"Customchuan.gt");
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
			BufferedReader reader = null;
			BufferedWriter writer = null;
			try {
				reader = new BufferedReader(new FileReader(PathConstant.PATH_system+"Customchuan.gt"));
				writer  = new BufferedWriter(new FileWriter(PathConstant.PATH_system+"Customchuan.gt"));
				StringBuffer sb = new StringBuffer();
				String line = "";
				while((line=reader.readLine())!=null){
					sb.append(line);
				}
				IMEI = sb.toString();
			    if(IMEI.equals("") || IMEI==null){
			    	IMEI=GenerateIMEI.produceIMEI();
			    	writer.write(IMEI.toCharArray());
			    	writer.flush();
			    }
			}catch (FileNotFoundException e) {  
			    e.printStackTrace();  
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}finally{
				try {
					reader.close();
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				try {
					writer.close();
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
			
		}
	}
	
	public String getIEMI(){
		if(IMEI==null || IMEI.equals("")){
			generateIEMI();
		}
		return IMEI;
	}
	
	@Override
	public void uncaughtException(Thread arg0, Throwable arg1) {// 在这里你可以处理当 程序崩溃时你想做的事情
		// TODO 自动生成的方法存根
		Toast.makeText(getApplicationContext(), "程序崩溃！我们一直在尽力完善软件，欢迎您的反馈。", Toast.LENGTH_SHORT).show();
		new Thread(ExitloadRun).start();
	}
	 
	Runnable ExitloadRun = new Runnable(){
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message msg = Message.obtain();
			synchronized (msg) {
				msg.what=WebserviceImport.delMEI(MyApplication.getInstance().getIEMI(),mSharedPreferences);
			}
		} 
	 };
}
