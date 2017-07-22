package com.guantang.cangkuonline.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.application.MyApplication;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.database.DataBaseImport;
import com.guantang.cangkuonline.database.DataBaseMethod;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;
import com.guantang.cangkuonline.static_constant.PathConstant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

public class MainActivity extends Activity {
	private Handler mHander;
	private SharedPreferences mSharedPreferences;
	private DataBaseMethod dm = new DataBaseMethod(this);
	private boolean on_off = false;//点击引导页上面的按钮，就不会在运行子线程里面的内容。
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mSharedPreferences = getSharedPreferences(ShareprefenceBean.SHAREPREFENCE_NAME, Context.MODE_PRIVATE);
        initSharedPreferences();
        int versionCode = MyApplication.getInstance().getVisionCode();
        if(mSharedPreferences.getInt(ShareprefenceBean.GetVERSIONCODE, 0)!=versionCode){
        	new Thread(upDataPicRunnable).start();
			mSharedPreferences.edit().putInt(ShareprefenceBean.GetVERSIONCODE,versionCode).commit();
			mSharedPreferences.edit().putBoolean(ShareprefenceBean.GRIDVIEW_ISFIRST,true).commit();
			Intent intent=new Intent(MainActivity.this,WelcomeActivity.class);
			startActivity(intent);
			finish();
		}else{
			setContentView(R.layout.activity_main);
			ImageView gotoImgView = (ImageView) findViewById(R.id.gotoImgView);
			gotoImgView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					on_off = true;
					Intent intent=new Intent(MainActivity.this,New11Activity.class);
					startActivity(intent);
					finish();
				}
			});
			mHander=new Handler();
	        mHander.postDelayed(new Runnable() {
	        	@Override
	        	public void run() {
	        		// TODO 自动生成的方法存根
	        		if (!on_off) {
	        			Intent intent=new Intent(MainActivity.this,NewLoginActivity.class);
		    			startActivity(intent);
		    			finish();
					}
	        	}
	        }, 2000);
		}

    }
    
    public void initSharedPreferences(){
    	mSharedPreferences.edit().putInt(ShareprefenceBean.IS_LOGIN, -1).commit();
    	
    	//修改上海服务器地址，等两个版本之后就删掉
    	if(mSharedPreferences.getString(ShareprefenceBean.SERVICE_IDN_URL1, "www3.gtcangku.com").equals("sh.gtcangku.com")){
    		mSharedPreferences.edit().putString(ShareprefenceBean.SERVICE_IDN_URL1, "www3.gtcangku.com").commit();
    	}
    	if(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "www3.gtcangku.com").equals("sh.gtcangku.com")){
    		mSharedPreferences.edit().putString(ShareprefenceBean.NET_URL, "www3.gtcangku.com").commit();
    	}
    }
    
    
    private Runnable upDataPicRunnable = new Runnable() {
		
		@Override
		public void run() {
			// TODO 自动生成的方法存根
			upDataPic();
		}
	};
	
    public void upDataPic(){
    	List<Map<String, String>> mList = new ArrayList<Map<String,String>>();
    	SQLiteDatabase db = DataBaseImport.getInstance(MainActivity.this).getReadableDatabase();
    	Cursor c = db.rawQuery("select id,imgpath from tb_hp where imgpath is not null and imgpath !=''", null);
    	if(c.moveToFirst()){
    		do{
    			Map<String, String> map = new HashMap<String, String>();
    			map.put("id", c.getString(c.getColumnIndex(DataBaseHelper.ID)));
    			map.put("imgpath", c.getString(c.getColumnIndex(DataBaseHelper.ImagePath)));
    			mList.add(map);
    		}while(c.moveToNext());
    	}
    	
    	Iterator<Map<String, String>> iterator = mList.iterator();
    	while(iterator.hasNext()){
    		Map<String, String> map = iterator.next();
    		String hpid = map.get("id");
    		String imgpath = map.get("imgpath");
    		File file = new File(PathConstant.PATH_photo+imgpath);
    		if(file.exists()){
//    			String[] name = imgpath.split("\\.");
//    			String suffix = name[name.length-1];
//    			String ImageURL = hpid+"_0001."+suffix;
//    			String CompressImageURL = "Compress_"+ImageURL;
    			db.execSQL("insert into tb_pic  (hpid,ImageURL,CompressImageURL) values ('"+hpid+"','"+imgpath+"','')");
//    			file.renameTo(new File(PathConstant.PATH_photo+ImageURL));
    			file.delete();
    		}
    	}
    	c.close();
    	db.close();
    }
}
