package com.guantang.cangkuonline.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.dialog.CommonDialog;
import com.guantang.cangkuonline.dialog.CommonDialog.DialogContentListener;
import com.guantang.cangkuonline.static_constant.PathConstant;
import com.guantang.cangkuonline.swipemenulistview.SwipeMenu;
import com.guantang.cangkuonline.swipemenulistview.SwipeMenuCreator;
import com.guantang.cangkuonline.swipemenulistview.SwipeMenuItem;
import com.guantang.cangkuonline.swipemenulistview.SwipeMenuListView;
import com.guantang.cangkuonline.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;

public class DataBaseManagerActivity extends Activity implements OnMenuItemClickListener{
	private ImageButton backButton;
	private SwipeMenuListView mListView;
	private List<String> items = new ArrayList<String>();
//	private AlertDialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_databasemanager);
		initView();
		init();
	}
	public void initView(){
		backButton= (ImageButton) findViewById(R.id.back);
		mListView=(SwipeMenuListView) findViewById(R.id.datamanagerlistview);
		
		backButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				finish();
			}
		});
	}
	public void init(){
		SwipeMenuCreator creator = new SwipeMenuCreator() {
			@Override
			public void create(SwipeMenu menu) {
				SwipeMenuItem ModifyItem = new SwipeMenuItem(
						getApplicationContext());
				// set item background
				ModifyItem.setBackground(getResources().getDrawable(R.color.grey));
				// set item width
				ModifyItem.setWidth(dp2px(80));
				//
				ModifyItem.setTitle("重命名");
				// set item title fontsize
				ModifyItem.setTitleSize(18);
				// set item title font color
				ModifyItem.setTitleColor(Color.WHITE);
				// add to menu
				menu.addMenuItem(ModifyItem);
				
				
				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(
						getApplicationContext());
				// set item background
				deleteItem.setBackground(new ColorDrawable(Color.parseColor("#ff5757")));
				// set item width
				deleteItem.setWidth(dp2px(80));
//				// set a icon
//				deleteItem.setIcon(R.drawable.ic_delete);
				deleteItem.setTitle("删除");
				deleteItem.setTitleSize(18);
				deleteItem.setTitleColor(Color.WHITE);
				// add to menu
				menu.addMenuItem(deleteItem);
			}
		};
		mListView.setMenuCreator(creator);
		mListView.setOnMenuItemClickListener(this);
		
		setAdapter();
	}
	
	public void setAdapter(){
		File[] files;
		items.clear();
		File file = new File(PathConstant.PATH_backup);
		if(file.exists()==false){
			file.mkdirs();
		}
		files = file.listFiles();
		if(files!=null){
			for(File wenjian : files){
				items.add(wenjian.getName().toString());
			} 
		}
		if(items.size()==0){
			items.add("抱歉，没有备份的数据文件");
		}
		ArrayAdapter<String> fileList = new ArrayAdapter<String>(this,
				R.layout.list_item, items);
		mListView.setAdapter(fileList);
	}
	/**文件重命名
	 * */
	public int renameFile(String oldname,String newname){
		File oldfile=new File(PathConstant.PATH_backup+oldname);
		File newfile=new File(PathConstant.PATH_backup+newname);
		if(!oldfile.exists()){
			Toast.makeText(this, "原文件不存在", Toast.LENGTH_SHORT).show();
			return -1;
		}
		if(newfile.exists()){
			Toast.makeText(this, "新命名不能与已有文件名相同", Toast.LENGTH_SHORT).show();
			return -1;
		}
		oldfile.renameTo(newfile);
		return 1;
	}
	
	public void delete_File(String filename){
		File file = new File(PathConstant.PATH_backup+filename);
		if(!file.exists()){
			Toast.makeText(this, "原文件不存在", Toast.LENGTH_SHORT).show();
		}else{
			file.delete();  
			Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
				    
		}
	}
	
	@Override
	public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
		// TODO 自动生成的方法存根
		
		switch(index){
		case 0:
			String oldname=items.get(position);
			
			CommonDialog commonDialog = new CommonDialog(this, R.layout.edit_dialog_layout, R.style.yuanjiao_activity);
			commonDialog.setCancelable(false);
			commonDialog.setDialogContentListener(new DialogContentListener() {
				
				@Override
				public void contentExecute(View parent, final Dialog dialog,final Object[] objs) {
					// TODO 自动生成的方法存根
					final EditText nameEditText = (EditText) parent.findViewById(R.id.nameEditText);
					TextView cancelTextView = (TextView) parent.findViewById(R.id.cancel);
					TextView confirmTextView = (TextView) parent.findViewById(R.id.confirm);
					cancelTextView.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO 自动生成的方法存根
							dialog.dismiss();
						}
					});
					confirmTextView.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO 自动生成的方法存根
							String newname = nameEditText.getText().toString().trim();
							int i=renameFile(objs[0].toString(), newname);
							if(i==1){
								setAdapter();
								dialog.dismiss();
								
							}
						}
					});
				}
			},new Object[]{oldname});
			commonDialog.show();
			
//			AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.yuanjiao_activity);
//			LayoutInflater inflater = LayoutInflater.from(this);
//			View view = inflater.inflate(R.layout.edit_dialog_layout, null);
//			final EditText nameEditText = (EditText) view.findViewById(R.id.nameEditText);
//			TextView cancelTextView = (TextView) view.findViewById(R.id.cancel);
//			TextView confirmTextView = (TextView) view.findViewById(R.id.confirm);
//			cancelTextView.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO 自动生成的方法存根
//					dialog.dismiss();
//				}
//			});
//			confirmTextView.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO 自动生成的方法存根
//					String newname = nameEditText.getText().toString().trim();
//					int i=renameFile(oldname, newname);
//					if(i==1){
//						setAdapter();
//						dialog.dismiss();
//						
//					}
//				}
//			});
//			builder.setView(view);
//			dialog = builder.create();
//			dialog.show();
			break;
		case 1:
			String filename = items.get(position);
			delete_File(filename);
			setAdapter();
			break;
		}
		return false;
	}
	
	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}
	
}
