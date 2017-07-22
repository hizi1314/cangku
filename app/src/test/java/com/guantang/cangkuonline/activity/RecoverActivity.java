package com.guantang.cangkuonline.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.application.MyApplication;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.database.DataBaseImport;
import com.guantang.cangkuonline.database.DataBaseOperateMethod;
import com.guantang.cangkuonline.dialog.FileDialog;
import com.guantang.cangkuonline.eventbusBean.ObjectTwo;
import com.guantang.cangkuonline.static_constant.PathConstant;

import de.greenrobot.event.EventBus;

public class RecoverActivity extends Activity implements OnClickListener {
	private ImageButton back, look;
	private Button ok;
	public EditText ed;
	private FileDialog filedialog;
	private Button managerBtn;
//	private SharedPreferences shared;
//	private List<Map<String, Object>> ls = new ArrayList<Map<String, Object>>();
	private DataBaseOperateMethod dm_op = new DataBaseOperateMethod(this);
//	private String[] s = new String[] { DataBaseHelper.GID,
//			DataBaseHelper.ItemV };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.recover);
		EventBus.getDefault().register(this);
		initControl();
	}
	@Override
	protected void onDestroy() {
		// TODO 自动生成的方法存根
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
	public void initControl() {
		back = (ImageButton) findViewById(R.id.back);
		look = (ImageButton) findViewById(R.id.look);
		ok = (Button) findViewById(R.id.ok);
		ed = (EditText) findViewById(R.id.ed);
		managerBtn = (Button) findViewById(R.id.managerBtn);
		back.setOnClickListener(this);
		ok.setOnClickListener(this);
		look.setOnClickListener(this);
		managerBtn.setOnClickListener(this);
//		shared = this.getSharedPreferences(
//				ShareprefenceBean.SHAREPREFENCE_DB_CONF, Context.MODE_PRIVATE);
	}
	
	public void onEventMainThread(ObjectTwo objecttwo) {
		ed.setText(objecttwo.getMsg());
	}
	
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.look:
			filedialog = new FileDialog(this);
			filedialog.show();
			break;
		case R.id.ok:
			File file = new File(PathConstant.PATH_DATABASE);
			if (file.exists() == false) {
				file.mkdirs();
			}
			String filename = ed.getText().toString();
			if (filename.equals("")) {
				Toast.makeText(this, "请选择文件名", Toast.LENGTH_SHORT).show();
			} else {
				int j = CopyFile(PathConstant.PATH_backup + filename, PathConstant.PATH_DATABASE);
				if (j == -1) {
					Toast.makeText(this, "恢复失败", Toast.LENGTH_SHORT).show();
				} else if (j == 0) {
					Toast.makeText(this, "恢复成功", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(this, "恢复异常", Toast.LENGTH_SHORT).show();
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
			while ((c = fosfrom.read(bt)) > 0) {
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
