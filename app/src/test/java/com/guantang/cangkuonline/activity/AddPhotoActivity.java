package com.guantang.cangkuonline.activity;
/*
 * 临时图片名称命名规则： yyyy-MM-dd_HH-mm-ss_5位随机数
 * */

/*
 * 图片的本地操作（显示添加删除）
 * */
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.R.color;
import com.guantang.cangkuonline.activity.ModfiyPhotoActivity.AdditionImageAsyncTask;
import com.guantang.cangkuonline.adapter.ImageDeleteAdapter;
import com.guantang.cangkuonline.adapter.MyPicAdapter;
import com.guantang.cangkuonline.database.DataBaseMethod;
import com.guantang.cangkuonline.helper.ImageHelper;
import com.guantang.cangkuonline.helper.TemporarilyImageBean;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;
import com.guantang.cangkuonline.static_constant.PathConstant;
import com.guantang.cangkuonline.webservice.WebserviceImport;
import com.guantang.cangkuonline.webservice.WebserviceImport_more;

public class AddPhotoActivity extends Activity implements OnClickListener,OnItemClickListener,OnCheckedChangeListener {
	private RelativeLayout all_layout, conf_layout;
	private TextView cancel, del_conf;
	private CheckBox radBtn;
	private GridView gridView;
	private ImageButton back, delete;
	private LinearLayout photo, look, select_layout;
	private int width, height;
	private String imageName = "";//临时存放图片名称
	private MyPicAdapter myPicAdapter;
	private SharedPreferences mSharedPreferences;
	private ImageDeleteAdapter imageDeleteAdapter;
	private DataBaseMethod dm = new DataBaseMethod(this);
	private String Compressimagepath="";
	/*用于存放图片，<自定义图片对象>
	 * */
	private List<TemporarilyImageBean> ImageList = new ArrayList<TemporarilyImageBean>();
	private List<String> ImageNameList = new ArrayList<String>();
	/*
	 *是否进入删除状态
	 * */
	private Boolean deleteFlagPager=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_chose2);
		mSharedPreferences = getSharedPreferences(ShareprefenceBean.SHAREPREFENCE_NAME, Context.MODE_PRIVATE);
		initControl();
		init();
	}
	
	public void initControl() {
		gridView = (GridView) findViewById(R.id.gridView1);
		back = (ImageButton) findViewById(R.id.back);
		photo = (LinearLayout) findViewById(R.id.photo);
		look = (LinearLayout) findViewById(R.id.look);
		delete = (ImageButton) findViewById(R.id.delete);
		all_layout = (RelativeLayout) findViewById(R.id.all_layout);
		conf_layout = (RelativeLayout) findViewById(R.id.conf_layout);
		cancel = (TextView) findViewById(R.id.cancel);
		del_conf = (TextView) findViewById(R.id.del_conf);
		radBtn = (CheckBox) findViewById(R.id.radBtn);
		select_layout = (LinearLayout) findViewById(R.id.select_layout);
		delete.setOnClickListener(this);
		all_layout.setOnClickListener(this);
		conf_layout.setOnClickListener(this);
		cancel.setOnClickListener(this);
		del_conf.setOnClickListener(this);
		select_layout.setOnClickListener(this);
		back.setOnClickListener(this);
		look.setOnClickListener(this);
		photo.setOnClickListener(this);
		delete.setOnClickListener(this);
		gridView.setOnItemClickListener(this);
		radBtn.setOnCheckedChangeListener(this);
		DisplayMetrics dms = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dms);
		width = dms.widthPixels;
		height = dms.heightPixels;
	}
	
	public void init(){
		myPicAdapter = new MyPicAdapter(this, width);
		imageDeleteAdapter = new ImageDeleteAdapter(this, width);
		
		Intent intent = getIntent();
		ImageNameList=(List<String>) intent.getSerializableExtra("ImageNameList");
		ListIterator<String> it = ImageNameList.listIterator();
		while(it.hasNext()){
			String urlString = it.next();
			File file = new File(PathConstant.PATH_photo + urlString);
			if(file.exists()){
				ImageList.add(new TemporarilyImageBean(urlString, getBitmap(PathConstant.PATH_photo+ urlString),false));
			}else{
				dm.deletePIC_OneUrl(urlString.subSequence(0, urlString.indexOf("_")).toString(), imageName);
				it.remove();
			}
		}
		setPicAdapter();
		if(mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1)==-1){
			Toast.makeText(this, "离线模式下只显示本设备保存的图片,不能显示服务端图片!", Toast.LENGTH_LONG).show();
//			AlertDialog.Builder builder = new AlertDialog.Builder(this);
//			LayoutInflater inflater = LayoutInflater.from(this);
//			TextView v = (TextView) inflater.inflate(android.R.layout.simple_list_item_1, null);
//			v.setText("\t\t离线模式下只显示本设备添加成功并保存在本地的图片,不能显示服务端图片!");
//			v.setTextSize(16);
//			builder.setView(v);
//			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//				
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					// TODO 自动生成的方法存根
//					dialog.dismiss();
//				}
//			});
//			builder.create().show();
		}
	}
	
	public void setPicAdapter(){
		gridView.setAdapter(myPicAdapter);
		myPicAdapter.setData(ImageList);
	}
	
	public void setDeletePicAdapter(){
		gridView.setAdapter(imageDeleteAdapter);
		imageDeleteAdapter.setData(ImageList);
	}

	
	@Override
	public void onClick(View arg0) {
		// TODO 自动生成的方法存根
		switch(arg0.getId()){
		case R.id.back:
//			ImageNameList.clear();
//			for(int i=0;i<ImageList.size();i++){
//				ImageNameList.add(ImageList.get(i).getImageName());
//			}
			Intent in = new Intent();
			in.putExtra("ImageNameList", (Serializable) ImageNameList);
			setResult(1,in);
			finish();
			break;
		case R.id.delete:
			if(!deleteFlagPager){
				deleteFlagPager = true;
				if(mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1)==-1){
					Toast.makeText(this, "离线模式下只能删除本地保存的图片，在线模式下能彻底删除图片!", Toast.LENGTH_LONG).show();
//					AlertDialog.Builder builder = new AlertDialog.Builder(this);
//					LayoutInflater inflater = LayoutInflater.from(this);
//					TextView v = (TextView) inflater.inflate(android.R.layout.simple_list_item_1, null);
//					v.setText("\t\t离线模式下只能删除本地保存图片，如果要彻底删除图片，请在在线模式下操作");
//					v.setTextSize(16);
//					builder.setView(v);
//					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//						
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							// TODO 自动生成的方法存根
//							dialog.dismiss();
//						}
//					});
//					builder.create().show();
				}
				all_layout.setVisibility(View.VISIBLE);
				conf_layout.setVisibility(View.VISIBLE);
				select_layout.setVisibility(View.GONE);
				delete.setVisibility(View.GONE);
				setDeletePicAdapter();
			}else{
				deleteFlagPager = false;
				all_layout.setVisibility(View.INVISIBLE);
				conf_layout.setVisibility(View.GONE);
				select_layout.setVisibility(View.VISIBLE);
				delete.setVisibility(View.VISIBLE);
				setPicAdapter();
			}
			break;
		case R.id.all_layout:
			if(radBtn.isChecked()){
				radBtn.setChecked(false);
			}else{
				radBtn.setChecked(true);
			}
			break;
		case R.id.cancel:
			deleteFlagPager = false;
			all_layout.setVisibility(View.INVISIBLE);
			conf_layout.setVisibility(View.GONE);
			select_layout.setVisibility(View.VISIBLE);
			delete.setVisibility(View.VISIBLE);
			
			radBtn.setChecked(false);
			for(int i=0;i<ImageList.size();i++){
				ImageList.get(i).setDeletethis(false);
			}
			setPicAdapter();
			break;
		case R.id.del_conf:
			Iterator<TemporarilyImageBean> it=ImageList.iterator();
			while(it.hasNext()){
				TemporarilyImageBean temporarilyImageBean = it.next();
				if(temporarilyImageBean.getDeletethis()){
					File file = new File(PathConstant.PATH_photo+temporarilyImageBean.getImageName());
					if(file.exists()){
						file.delete();
					}
					String imageName=temporarilyImageBean.getImageName();
					String hpid = imageName.substring(0, imageName.indexOf("_"));
					dm.deletePIC_OneUrl(hpid, imageName);
					ImageNameList.remove(imageName);
					it.remove();
				}
			}
			setDeletePicAdapter();
			break;
		case R.id.photo:
			if(mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1)==1){
				if(ImageList.size()<4){
					imageName=ImageHelper.TemporarilyImageName()+".jpg";
					File file = new File(PathConstant.PATH_photo + imageName);
					if(file.exists()){
						file.delete();
					}
					// 调用系统照相功能
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					intent.addCategory(Intent.CATEGORY_DEFAULT);
					intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
					intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(file));
					startActivityForResult(intent, 1);
				}else{
					Toast.makeText(this, "每个货品只能添加4张图片", Toast.LENGTH_SHORT).show();
				}
			}else if(mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1)==-1){
				Toast.makeText(this, "离线模式只能查看本手机保存的图片", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.look:
			if(mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1)==1){
				if(ImageList.size()<4){
					imageName=ImageHelper.TemporarilyImageName();
					showChooser();
				}else{
					Toast.makeText(this, "每个货品只能添加4张图片", Toast.LENGTH_SHORT).show();
				}
			}else if(mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1)==-1){
				Toast.makeText(this, "离线模式只能查看本手机保存的图片", Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO 自动生成的方法存根
		TemporarilyImageBean imageBean=(TemporarilyImageBean) arg0.getAdapter().getItem(arg2);
		if(deleteFlagPager){
			if(imageBean.getDeletethis()){
				ImageList.get(arg2).setDeletethis(false);
			}else{
				ImageList.get(arg2).setDeletethis(true);
			}
			setDeletePicAdapter();
			CountDeleteImage();
		}else if(!deleteFlagPager){
			Intent intent = new Intent(this,ShowImagePagerActivity.class);
			intent.putExtra("ImageNameList",(Serializable) ImageNameList);
			intent.putExtra("nowShowImage", imageBean.getImageName());
			intent.putExtra("LocalOrNet", -1);//-1代表访问本地图片，1代表访问网络图片
			startActivity(intent);
		}
	}
	
	private void showChooser() {
		Intent intent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		try {
			startActivityForResult(intent, 2);
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(this, "请安装图片浏览器", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO 自动生成的方法存根
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK){
			if(requestCode==1){
				if(!imageName.equals("")){
					File file = new File(PathConstant.PATH_photo+ imageName);
					if(file.exists()){
						ImageList.add(new TemporarilyImageBean(imageName, getBitmap(PathConstant.PATH_photo+ imageName),false));
						ImageNameList.add(imageName);
						myPicAdapter.setData(ImageList);
					}else{
						Toast.makeText(this, "照相失败", Toast.LENGTH_SHORT).show();
					}
				}
			}else if(requestCode==2){
				Uri uri = data.getData();
				if(uri != null){
					String[] filePathColumn = { MediaStore.Images.Media.DATA };
					Cursor cursor = this.getContentResolver().query(uri, filePathColumn,null, null, null);
					if(cursor!=null){
						if(cursor.moveToFirst()){
							String path = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
				            cursor.close();
							String[] urlArray = path.split("\\.");
							if(!imageName.equals("")){
								if(urlArray.length>1){//保持图片格式不变
									imageName = imageName+"."+urlArray[urlArray.length-1];
								}
								if(CopyFile(path, PathConstant.PATH_photo+imageName)==0){
									File file = new File(PathConstant.PATH_photo+ imageName);
									if(file.exists()){
										ImageList.add(new TemporarilyImageBean(imageName, getBitmap(PathConstant.PATH_photo+ imageName),false));
										ImageNameList.add(imageName);
										myPicAdapter.setData(ImageList);
									}else{
										Toast.makeText(this, "图片选取失败", Toast.LENGTH_SHORT).show();
									}
								}else{
									Toast.makeText(this, "图片选取失败", Toast.LENGTH_SHORT).show();
								}
							}
						}
					}else{
						Toast.makeText(this, "图片选取失败", Toast.LENGTH_SHORT).show();
					}
				}else{
					Bundle bundle = data.getExtras();
					if (bundle != null) {                 
			               Bitmap  photo = (Bitmap) bundle.get("data"); //get bitmap  
			               //spath :生成图片取个名字和路径包含类型      
			               if(saveImage(photo, PathConstant.PATH_photo+ imageName)){
			            	   File file = new File(PathConstant.PATH_photo+ imageName);
			            	   if(file.exists()){
			            		   ImageList.add(new TemporarilyImageBean(imageName, getBitmap(PathConstant.PATH_photo+ imageName+".jpg"),false));
			            		   ImageNameList.add(imageName);
			            		   myPicAdapter.setData(ImageList);
			            	   }else{
			            		   Toast.makeText(this, "图片选取失败", Toast.LENGTH_SHORT).show();
			            	   }
			               }else{
			            	   Toast.makeText(this, "图片选取失败", Toast.LENGTH_SHORT).show();
			               }
					}
				}
			}
			imageName="";
		}	
	}
	
	public boolean saveImage(Bitmap photo, String spath){
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(spath+".jpg", false));
			photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return false;
		}finally{
			try {
				bos.flush();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}  
            try {
				bos.close();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		} 
		return true;
	}
	
	private Bitmap getBitmap(String path){
		BitmapFactory.Options option = new BitmapFactory.Options();
		option.inJustDecodeBounds = true;
		option.inPreferredConfig = Bitmap.Config.RGB_565;
		option.inSampleSize = 1;
		BitmapFactory.decodeFile(path, option);
		while ((option.outHeight / option.inSampleSize) > 300
				&& (option.outWidth / option.inSampleSize) > 300) {
			option.inSampleSize *= 2;
		}
		option.inPurgeable = true;
		option.inInputShareable = true;
		option.inJustDecodeBounds = false;
		Bitmap bm =null;
		try{
			bm = BitmapFactory.decodeFile(path, option);
		}catch(OutOfMemoryError e){
			Toast.makeText(this, "手机运行内存不足，无法显示该张图片", Toast.LENGTH_SHORT).show();
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 30) { // 循环判断如果压缩后图片是否大于30kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			bm.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10%
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(
				baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap =null;
		try{
			bitmap = BitmapFactory
					.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		}catch(OutOfMemoryError e){
			Toast.makeText(this, "手机运行内存不足，正试图压缩图片显示", Toast.LENGTH_SHORT).show();
			bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			int opt = 100;
			while (baos.toByteArray().length / 1024 > 20) {
				baos.reset();
				bm.compress(Bitmap.CompressFormat.JPEG, opt, baos);
				options -= 10;
			}
		}finally{
			System.gc();
		}
		return bitmap;
	}
	
	public int CopyFile(String fromFile, String toFile) {
		InputStream fosfrom =null;
		OutputStream fosto =null;
		try {
			fosfrom = new FileInputStream(fromFile);
			fosto = new FileOutputStream(toFile);
			byte bt[] = new byte[1024];
			int c;
			while ((c = fosfrom.read(bt)) != -1) {
				fosto.write(bt, 0, c);
			}
			
			return 0;
		} catch (Exception ex) {
			return -1;
		} finally{
			try {
				if(fosfrom!=null){
					fosfrom.close();
				}
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			try {
				if(fosto!=null){
					fosto.close();
				}
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}

	public void CountDeleteImage(){
		int count = 0;
		for(int i=0;i<ImageList.size();i++){
			if(ImageList.get(i).getDeletethis()){
				count += 1;
			}
			if(count>0){
				del_conf.setTextColor(color.ziti1);
				del_conf.setText("删  除 ("+String.valueOf(count)+")");
			}else{
				del_conf.setTextColor(color.black);
				del_conf.setText("删  除");
			}
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO 自动生成的方法存根
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
//			ImageNameList.clear();
//			for(int i=0;i<ImageList.size();i++){
//				ImageNameList.add(ImageList.get(i).getImageName());
//			}
			Intent in = new Intent();
			in.putExtra("ImageNameList",(Serializable) ImageNameList);
			setResult(1,in);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO 自动生成的方法存根
		if(!isChecked){
			for(int i=0;i<ImageList.size();i++){
				ImageList.get(i).setDeletethis(false);
			}
			setDeletePicAdapter();
			CountDeleteImage();
		}else{
			for(int i=0;i<ImageList.size();i++){
				ImageList.get(i).setDeletethis(true);
			}
			setDeletePicAdapter();
			CountDeleteImage();
		}
	}
	
}
