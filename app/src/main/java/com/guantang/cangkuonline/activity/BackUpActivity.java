package com.guantang.cangkuonline.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.database.DataBaseOperateMethod;
import com.guantang.cangkuonline.dialog.FileDialog;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;
import com.guantang.cangkuonline.static_constant.PathConstant;

public class BackUpActivity extends Activity implements OnClickListener{
	private ImageButton back;
	private Button ok;
	public static EditText ed;
	private FileDialog filedialog;
	private Button managerBtn;
//	private SharedPreferences shared;
//	private List<Map<String, Object>> ls = new ArrayList<Map<String, Object>>();
	private DataBaseOperateMethod dm_op = new DataBaseOperateMethod(this);
//	private String[] s = new String[] { DataBaseHelper.GID,
//			DataBaseHelper.ItemV };
	private SimpleDateFormat formatter;
	private Boolean flag=true;
	private SharedPreferences mSharedPreferences;
	
	private OnTouchListener edittouch=new OnTouchListener(){
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			if(event.getAction()==MotionEvent.ACTION_DOWN){
				flag=false;
				ed.setHint("");
			}
			return false;
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.back_up);
		initControl();
		init();
	}

	public void initControl() {
		back = (ImageButton) findViewById(R.id.back);
		ok = (Button) findViewById(R.id.ok);
		ed = (EditText) findViewById(R.id.ed);
		managerBtn = (Button) findViewById(R.id.managerBtn);
		back.setOnClickListener(this);
		ok.setOnClickListener(this);
		managerBtn.setOnClickListener(this);
		ed.setOnTouchListener(edittouch);
		mSharedPreferences = this.getSharedPreferences(
				ShareprefenceBean.SHAREPREFENCE_NAME, Context.MODE_PRIVATE);
//		shared = this.getSharedPreferences(
//				ShareprefenceBean.SHAREPREFENCE_DB_CONF, Context.MODE_PRIVATE);
	}
	
	public void init(){
		formatter=new SimpleDateFormat("yyyyMMdd_HHmmss");
	}
	@Override
	protected void onStart() {
		// TODO 自动生成的方法存根
		super.onStart();
		ed.setHint(mSharedPreferences.getString(ShareprefenceBean.DWNAME_LOGIN, "")+"_"+mSharedPreferences.getString(ShareprefenceBean.USERNAME, "")+"_"+formatter.format(new Date(System.currentTimeMillis())));	
	}
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.ok:
			File file = new File(PathConstant.PATH_DATABASE);
			if (file.exists() == false) {
				file.mkdirs();
			}
			String filename ="";
			if(flag){
				filename=ed.getHint().toString();
			}else{
				filename=ed.getText().toString();
			}
			if (filename.equals("")) {
				Toast.makeText(this, "请输入备份数据名称", Toast.LENGTH_SHORT).show();
			} else {
				Boolean f=isbeifenExists(filename);
				if(f==false){
					int j = CopyFile(PathConstant.PATH_DATABASE, PathConstant.PATH_backup + filename+".db");//数据库备份出来
					if (j == -1) {
						Toast.makeText(this, "备份失败", Toast.LENGTH_SHORT).show();
					} else if (j == 0) {
						Toast.makeText(this, "备份成功", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(this, "备份异常", Toast.LENGTH_SHORT).show();
					}
				}
			}
			break;
		case R.id.managerBtn:
			Intent intent = new Intent();
			intent.setClass(this, DataBaseManagerActivity.class);
			startActivity(intent);
			break;
		}
	}
	
	public Boolean isbeifenExists(String filename){
		File file = new File(PathConstant.PATH_backup);
		if(file.exists()==false){
			file.mkdirs();
		}
		File[] files = file.listFiles();
		if (files!=null && files.length > 0) {
			for(File wenjian : files){
				if(wenjian.getName().toString().equals(filename+".db")){
					Toast.makeText(this, "该备份文件已经存在，请重新命名", Toast.LENGTH_SHORT).show();
					return true;
				}
			}
		}
		return false;
	}
	
	public int CopyFile(String fromFile, String toFile) {		
		InputStream fosfrom = null;
		OutputStream fosto =null;
		try {
			File file = new File(toFile);
			if (file.exists()) {
				file.delete();
			}
			fosfrom = new FileInputStream(fromFile);
			fosto = new FileOutputStream(toFile);
			byte bt[] = new byte[1024];
			int c;
			while ((c = fosfrom.read(bt)) > -1) {
				fosto.write(bt, 0, c);
			}
			fosto.flush();
			return 0;

		} catch (Exception ex) {
			return -1;
		}finally{
			if(fosfrom!=null || fosto!=null){
				try {
					fosfrom.close();
					fosto.close();
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					throw new RuntimeException("IOException occurred. ", e);
				}
			}
		}
	}
	
}
