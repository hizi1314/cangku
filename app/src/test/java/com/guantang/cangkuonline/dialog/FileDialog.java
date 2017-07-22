package com.guantang.cangkuonline.dialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.eventbusBean.ObjectTwo;

import de.greenrobot.event.EventBus;

public class FileDialog extends AlertDialog {
	private Context context;
	public ListView mListView;
	private List<String> items;
	public FileDialog(Context context) {
		super(context);
		// TODO 自动生成的构造函数存根
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filedialog);
		mListView = (ListView) this.findViewById(R.id.filelist);
		setAdapter();
		mListView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				EventBus.getDefault().post(new ObjectTwo(arg0.getAdapter().getItem(arg2).toString()));
				dismiss();
			}
		});
	}
	public void setAdapter(){
		File[] files;
		items = new ArrayList<String>();
		File fl=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/guantang/备份/");
		if(fl.exists()==false){
			fl.mkdirs();
		}
		files=fl.listFiles();
		for (File file : files){
            items.add(file.getName().toString());
		}
		if(items.size()==0){
			items.add("  抱歉，没找到要恢复的数据库");
		}
		ArrayAdapter<String> fileList = new ArrayAdapter<String>(context,
                R.layout.list_item, items);
		mListView.setAdapter(fileList);
		
	}
	
}
