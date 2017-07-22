package com.guantang.cangkuonline.activity;
/*
 * 图片的网络操作（显示添加删除）
 * */

/*
 * 临时图片名称命名规则： yyyy-MM-dd_HH-mm-ss_5位随机数
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
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

import org.json.JSONException;
import org.json.JSONObject;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.R.color;
import com.guantang.cangkuonline.adapter.DownLoadImageAdapter;
import com.guantang.cangkuonline.adapter.ImageDeleteAdapter;
import com.guantang.cangkuonline.application.MyApplication;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.database.DataBaseMethod;
import com.guantang.cangkuonline.helper.ImageHelper;
import com.guantang.cangkuonline.helper.TemporarilyImageBean;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;
import com.guantang.cangkuonline.static_constant.PathConstant;
import com.guantang.cangkuonline.webservice.WebserviceImport;
import com.guantang.cangkuonline.webservice.WebserviceImport_more;

import android.R.integer;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

public class ModfiyPhotoActivity extends Activity implements OnClickListener,OnItemClickListener,OnCheckedChangeListener {
	private RelativeLayout all_layout, conf_layout;
	private TextView cancel, del_conf;
	private CheckBox radBtn;
	private GridView gridView;
	private ImageButton back, delete;
	private LinearLayout photo, look, select_layout;
	private int width=0,height=0;
	private String hpid = "";
	private String addImageName="";//新增图片名称，随机编排的名称不能，在存数据库之前需要重新命名
	private SharedPreferences mSharedPreferences;
	private DownLoadImageAdapter mDownLoadImageAdapter;
	private ImageDeleteAdapter mImageDeleteAdapter;
	private DataBaseMethod dm = new DataBaseMethod(this);
	private ExecutorService cacheThreadpool = Executors.newCachedThreadPool();
	private ProgressDialog NetProgressDialog;
	private int netPicNumber=0;
	private int upLoadImgNum=0;//正在上传图片的数量
	
	/**
	 * 引入信号量，防止图片没有上传成功就退出
	 */
	private volatile Semaphore mSemaphore;
	/*
	 *是否进入删除状态
	 * */
	private Boolean deleteFlagPager=false;
	/*用于存放图片，<图片名称(非缩略图),图片(缩略图)>
	 * */
	private List<TemporarilyImageBean> ImageList = new ArrayList<TemporarilyImageBean>();
	/*用于存放图片名称的集合(非缩略图)
	 * */
	private List<String> ImageUrlList = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_chose2);
		mSharedPreferences = getSharedPreferences(ShareprefenceBean.SHAREPREFENCE_NAME, Context.MODE_PRIVATE);
		initView();
		init();
	}
	public void initView(){
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
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels;
		height = dm.heightPixels;
	}
	
	public void init(){
		Intent intent = getIntent();
		hpid = intent.getStringExtra("hpid");
		if(mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1)==1){
			if(WebserviceImport.isOpenNetwork(this)){
				setNetAdapter();
				NetProgressDialog = ProgressDialog.show(ModfiyPhotoActivity.this, null, "正在获取网络图片数量");
				cacheThreadpool.execute(getImagenameRunnable);
			}else {
				Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	public void setNetAdapter(){
		mDownLoadImageAdapter = new DownLoadImageAdapter(this, mSharedPreferences,width);
		gridView.setAdapter(mDownLoadImageAdapter);
	}
	
	public void setDeleteAdapter(){
		mImageDeleteAdapter = new ImageDeleteAdapter(this, width);
		gridView.setAdapter(mImageDeleteAdapter);
	}
	
	Runnable getImagenameRunnable = new Runnable() {
		
		@Override
		public void run() {
			// TODO 自动生成的方法存根
			Message msg = Message.obtain();
			JSONObject json;
			msg.obj=WebserviceImport_more.GetImageName(hpid, mSharedPreferences);
			msg.what=1;
			handler.sendMessage(msg);
		}
	};
	
	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what=1) {
			case 1:
				NetProgressDialog.dismiss();
				if(WebserviceImport.isOpenNetwork(ModfiyPhotoActivity.this)){
					String[] urlArray = msg.obj.toString().split(",");
					if(urlArray.length>0){
						if(!urlArray[0].equals("")){
							netPicNumber= urlArray.length;
							for(int i=0;i<netPicNumber;i++){
								new MyAsyncTask().executeOnExecutor(cacheThreadpool, urlArray[i]);
							}
						}else{
							netPicNumber= 0;
						}
					}else{
						netPicNumber= 0;
					}
				}else{
					Toast.makeText(ModfiyPhotoActivity.this, "网络未连接", Toast.LENGTH_SHORT).show();
				}
				
				break;
			}
		};
	};
	class MyAsyncTask extends AsyncTask<String, Void, String>{
		String urlString =null;
		@Override
		protected String doInBackground(String... params) {
			// TODO 自动生成的方法存根
			urlString = params[0];
			String Base64String = WebserviceImport.GetImage("Compress_"+params[0],mSharedPreferences);
			return Base64String;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO 自动生成的方法存根
			super.onPostExecute(result);
			if(result.equals("-2")){
//				netPicNumber--;
				Toast.makeText(ModfiyPhotoActivity.this, "服务端图片正在压缩,请重新进入页面", Toast.LENGTH_SHORT).show();
			}else if(result.equals("-3")){
//				netPicNumber--;
			}else{
				Bitmap bitmap = null;
				try {
					byte[] bitmapArray;
					bitmapArray = Base64.decode(result, Base64.DEFAULT);
					BitmapFactory.Options option = new BitmapFactory.Options();
					option.inJustDecodeBounds = true;
					option.inPreferredConfig = Bitmap.Config.RGB_565;
					option.inSampleSize = 1;
					BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length, option);
					while ((option.outHeight / option.inSampleSize) > 300
							&& (option.outWidth / option.inSampleSize) > 300) {
						option.inSampleSize *= 2;
					}
					option.inPurgeable = true;
					option.inInputShareable = true;
					option.inJustDecodeBounds = false;
					bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,bitmapArray.length,option);
					if (bitmap != null) {
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中  
						int options = 100;  
						while ( baos.toByteArray().length / 1024>30) {  //循环判断如果压缩后图片是否大于30kb,大于继续压缩         
							baos.reset();//重置baos即清空baos  
							bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中  
							options -= 20;//每次都减少20  
						}  
						ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中  
						bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally{
					System.gc();
				}
				ImageUrlList.add(urlString);
				ImageList.add(new TemporarilyImageBean(urlString, bitmap, false));
				mDownLoadImageAdapter.setData(ImageList);
			}
		}
	};
	
	class DeleteImageAsyncTask extends AsyncTask<String, Void, Integer>{
		String imageName="";
		@Override
		protected Integer doInBackground(String... params) {
			// TODO 自动生成的方法存根
			imageName=params[0];
			int i = WebserviceImport_more.DeleteImage(Integer.parseInt(hpid), params[0], mSharedPreferences);
			return i;
		}
		@Override
		protected void onPostExecute(Integer result) {
			// TODO 自动生成的方法存根
			super.onPostExecute(result);
			if(result<0){
				Toast.makeText(ModfiyPhotoActivity.this, "有图片删除失败", Toast.LENGTH_SHORT).show();
			}else if(result==1){
				Iterator<TemporarilyImageBean> it=ImageList.iterator();
				while(it.hasNext()){
					TemporarilyImageBean temporarilyImageBean = it.next();
					if(temporarilyImageBean.getImageName().equals(imageName)){
						File file = new File(PathConstant.PATH_photo+temporarilyImageBean.getImageName());
						if(file.exists()){
							file.delete();
						}
						//删除图片时，也将其缓存图片删除
						File file2 = new File(PathConstant.PATH_cachephoto+temporarilyImageBean.getImageName());
						if(file2.exists()){
							file2.delete();
						}
						it.remove();
						ImageUrlList.remove(temporarilyImageBean.getImageName());
						dm.deletePIC_OneUrl(hpid, imageName);
//						netPicNumber--;
						break;
					}
				}
			}
			
			del_conf.setTextColor(color.black);
			del_conf.setText("删  除");
			
			mImageDeleteAdapter.setData(ImageList);
		}
	};
	
	class AdditionImageAsyncTask extends AsyncTask<String, Void, String>{
		String oldImageName = "";
		@Override
		protected String doInBackground(String... params) {
			// TODO 自动生成的方法存根
			String[] str = params[0].split("\\.");
			String type = "."+str[str.length-1];
			oldImageName = params[0];
			FileInputStream fis = null;
			String jsonString ="";
			try {
				File file = new File(PathConstant.PATH_photo+params[0]);
				if(!file.exists()){
            		file.createNewFile();
				}
				fis = new FileInputStream(file);
				BitmapFactory.Options opt=new BitmapFactory.Options();
				opt.inJustDecodeBounds=true;
				opt.inPurgeable = true;
				opt.inInputShareable = true;
				opt.inPreferredConfig = Bitmap.Config.RGB_565;
				opt.inSampleSize = 1;
				BitmapFactory.decodeFile(PathConstant.PATH_photo+params[0],opt);
				while ((opt.outHeight / opt.inSampleSize) > width
						&& (opt.outWidth / opt.inSampleSize) > height) {
					opt.inSampleSize *= 2;
				}
				opt.inJustDecodeBounds=false;
				Bitmap bm=BitmapFactory.decodeFile(PathConstant.PATH_photo+params[0],opt);
				if(bm==null){
					ByteArrayOutputStream bStream = new ByteArrayOutputStream();
					String base64string = Base64.encodeToString(bStream.toByteArray(), Base64.DEFAULT);
				    jsonString = WebserviceImport_more.AdditionImage(type, base64string, Integer.parseInt(hpid), mSharedPreferences);
				}else{
					ByteArrayOutputStream bStream = new ByteArrayOutputStream();
			        bm.compress(CompressFormat.JPEG, 100, bStream); 
				    int options = 100;  
				    while ( bStream.toByteArray().length / 1024>512) {//循环判断如果压缩后图片是否大于512kb,大于继续压缩         
				    	bStream.reset();//重置bStream即清空bStream
				    	bm.compress(Bitmap.CompressFormat.JPEG, options, bStream);//这里压缩options%，把压缩后的数据存放到baos中  
				    	options -= 10;//每次都减少10
				    }
				    String base64string = Base64.encodeToString(bStream.toByteArray(), Base64.DEFAULT);
				    jsonString = WebserviceImport_more.AdditionImage(type, base64string, Integer.parseInt(hpid), mSharedPreferences);
					System.gc();
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				try {
					fis.close();
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
			return jsonString;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO 自动生成的方法存根
			super.onPostExecute(result);
			upLoadImgNum--;
			try {
				JSONObject AdditionImageJSONObject = new JSONObject(result);
				if(AdditionImageJSONObject.getBoolean("Status")){
//					netPicNumber++;
					Toast.makeText(ModfiyPhotoActivity.this, AdditionImageJSONObject.getString("Message"), Toast.LENGTH_SHORT).show();
					String newImageName= AdditionImageJSONObject.getString("Data");
					ListIterator<String> iterator = ImageUrlList.listIterator();
					while(iterator.hasNext()){
						String ImageName = iterator.next();
						if(ImageName.equals(oldImageName)){
							ImageName=newImageName;
							iterator.set(ImageName);
							break;
						}
					}
					
					ListIterator<TemporarilyImageBean> it=ImageList.listIterator();
					while(it.hasNext()){
						TemporarilyImageBean temporarilyImageBean = it.next();
						if(temporarilyImageBean.getImageName().equals(oldImageName)){
							File file = new File(PathConstant.PATH_photo+temporarilyImageBean.getImageName());
							if(file.exists()){
								file.renameTo(new File(PathConstant.PATH_photo+newImageName));
							}
							temporarilyImageBean.setImageName(newImageName);
							it.set(temporarilyImageBean);
							break;
						}
					}
					dm.insertTB_PIC(hpid, newImageName);
					mDownLoadImageAdapter.setData(ImageList);
				}else{
					Toast.makeText(ModfiyPhotoActivity.this, AdditionImageJSONObject.getString("Message"), Toast.LENGTH_SHORT).show();
					ListIterator<String> iterator = ImageUrlList.listIterator();
					while(iterator.hasNext()){
						if(iterator.next().equals(oldImageName)){
							iterator.remove();
							break;
						}
					}
					
					ListIterator<TemporarilyImageBean> it=ImageList.listIterator();
					while(it.hasNext()){
						TemporarilyImageBean temporarilyImageBean = it.next();
						if(temporarilyImageBean.getImageName().equals(oldImageName)){
							File file = new File(PathConstant.PATH_photo+temporarilyImageBean.getImageName());
							if(file.exists()){
								file.delete();
							}
							it.remove();
							break;
						}
					}
					mDownLoadImageAdapter.setData(ImageList);
				}
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	};
	
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch(v.getId()){
		case R.id.back:
//			if(ImageList.size()==netPicNumber){
			if(upLoadImgNum<=0){
				deletePic(PathConstant.PATH_cachephoto);
				finish();
			}else{
				Toast.makeText(this, "正在上传图片,请耐心等待", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.delete:
			if(upLoadImgNum<=0){//证明图片已经加载完了，可以进行图片删除功能
				if(!deleteFlagPager){
					deleteFlagPager = true;
					all_layout.setVisibility(View.VISIBLE);
					conf_layout.setVisibility(View.VISIBLE);
					select_layout.setVisibility(View.GONE);
					delete.setVisibility(View.GONE);
					setDeleteAdapter();
					mImageDeleteAdapter.setData(ImageList);
				}else{
					deleteFlagPager = false;
					all_layout.setVisibility(View.INVISIBLE);
					conf_layout.setVisibility(View.GONE);
					select_layout.setVisibility(View.VISIBLE);
					delete.setVisibility(View.VISIBLE);
					setNetAdapter();
					mDownLoadImageAdapter.setData(ImageList);
				}
			}else {
				Toast.makeText(this, "正在上传图片，不能进行删除操作", Toast.LENGTH_SHORT).show();
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
			setNetAdapter();
			mDownLoadImageAdapter.setData(ImageList);
			break;
		case R.id.del_conf:
			if(mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1)==1){
				if(WebserviceImport.isOpenNetwork(ModfiyPhotoActivity.this)){
					Iterator<TemporarilyImageBean> it=ImageList.iterator();
					while(it.hasNext()){
						TemporarilyImageBean temporarilyImageBean = it.next();
						if(temporarilyImageBean.getDeletethis()){
							new DeleteImageAsyncTask().executeOnExecutor(cacheThreadpool, temporarilyImageBean.getImageName());
						}
					}
				}else{
					Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
				}
				
			}else if(mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1)==-1){
				Iterator<TemporarilyImageBean> it=ImageList.iterator();
				while(it.hasNext()){
					TemporarilyImageBean temporarilyImageBean = it.next();
					if(temporarilyImageBean.getDeletethis()){
						File file = new File(PathConstant.PATH_photo+temporarilyImageBean.getImageName());
						if(file.exists()){
							file.delete();
						}
						it.remove();
						ImageUrlList.remove(temporarilyImageBean.getImageName());
					}
				}
			}
			setDeleteAdapter();
			mImageDeleteAdapter.setData(ImageList);
			break;
		case R.id.all_layout:
			if(radBtn.isChecked()){
				radBtn.setChecked(false);
			}else{
				radBtn.setChecked(true);
			}
			break;
		case R.id.photo:
			if(ImageList.size()<4){
				addImageName=ImageHelper.TemporarilyImageName()+".jpg";
				File file = new File(PathConstant.PATH_photo + addImageName);
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
			break;
		case R.id.look:
			if(ImageList.size()<4){
				addImageName=ImageHelper.TemporarilyImageName();
				showChooser();
			}else{
				Toast.makeText(this, "每个货品只能添加4张图片", Toast.LENGTH_SHORT).show();
			}
			break;
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
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO 自动生成的方法存根
		if(!isChecked){
			for(int i=0;i<ImageList.size();i++){
				ImageList.get(i).setDeletethis(false);
			}
			mImageDeleteAdapter.setData(ImageList);
			CountDeleteImage();
		}else{
			for(int i=0;i<ImageList.size();i++){
				ImageList.get(i).setDeletethis(true);
			}
			mImageDeleteAdapter.setData(ImageList);
			CountDeleteImage();
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
			mImageDeleteAdapter.setData(ImageList);
			CountDeleteImage();
		}else if(!deleteFlagPager){
			Intent intent = new Intent(this,ShowImagePagerActivity.class);
			intent.putExtra("ImageNameList",(Serializable) ImageUrlList);
			intent.putExtra("hpid", hpid);
			intent.putExtra("nowShowImage", imageBean.getImageName());
			intent.putExtra("LocalOrNet", mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1));//-1代表访问本地图片，1代表访问网络图片
			startActivity(intent);
		}
	}
	
	private Bitmap getCompressBitmap(String path){
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
			if (bitmap == null) {
				bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.photo_del);
			}
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
		File file = new File(fromFile);
		if(!file.exists()){
			return -1;
		}
		
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
				fosfrom.close();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			try {
				fosto.close();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO 自动生成的方法存根
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK){
			if(requestCode==1){
				if(WebserviceImport.isOpenNetwork(this)){
					if(!addImageName.equals("")){
						File file = new File(PathConstant.PATH_photo+ addImageName);
						if(file.exists()){
							ImageList.add(new TemporarilyImageBean(addImageName, getCompressBitmap(PathConstant.PATH_photo+ addImageName),false));
							ImageUrlList.add(addImageName);
							mDownLoadImageAdapter.setData(ImageList);
							upLoadImgNum++;
							mSemaphore = new Semaphore(1);
							new AdditionImageAsyncTask().executeOnExecutor(cacheThreadpool, addImageName);
						}else{
							Toast.makeText(this, "照相失败", Toast.LENGTH_SHORT).show();
						}
					}
				}else{
					Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
				}
			}else if(requestCode==2){
				Uri uri = data.getData();
				if(uri != null){//不同手机封装的data不同，有的是放的图片路径有的是图片
					String[] filePathColumn = { MediaStore.Images.Media.DATA };
					Cursor cursor = this.getContentResolver().query(uri, filePathColumn,null, null, null);
					if(cursor!=null){
						if(cursor.moveToFirst()){
				            String path = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
				            cursor.close();
							String[] urlArray = path.split("\\.");
							if(!addImageName.equals("")){
								if(urlArray.length>1){//保持图片格式不变
									addImageName = addImageName+"."+urlArray[urlArray.length-1];
								}
								if(CopyFile(path, PathConstant.PATH_photo+addImageName)==0){
									File file = new File(PathConstant.PATH_photo+ addImageName);
									if(file.exists()){
										if(WebserviceImport.isOpenNetwork(this)){
											ImageList.add(new TemporarilyImageBean(addImageName, getCompressBitmap(PathConstant.PATH_photo+ addImageName),false));
											ImageUrlList.add(addImageName);
											mDownLoadImageAdapter.setData(ImageList);
											upLoadImgNum++;
											new AdditionImageAsyncTask().executeOnExecutor(cacheThreadpool, addImageName);
										}else{
											Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
										}
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
			               if(saveImage(photo, PathConstant.PATH_photo+ addImageName)){
			            	   File file = new File(PathConstant.PATH_photo+ addImageName);
			            	   if(file.exists()){
			            		   ImageList.add(new TemporarilyImageBean(addImageName, getCompressBitmap(PathConstant.PATH_photo+ addImageName+".jpg"),false));
				            	   ImageUrlList.add(addImageName);
				            	   mDownLoadImageAdapter.setData(ImageList);
				            	   upLoadImgNum++;
				            	   new AdditionImageAsyncTask().executeOnExecutor(cacheThreadpool, addImageName);
			            	   }else{
			            		   Toast.makeText(this, "图片选取失败", Toast.LENGTH_SHORT).show();
			            	   }
			               }else{
			            	   Toast.makeText(this, "图片选取失败", Toast.LENGTH_SHORT).show();
			               }
					}
				}
			}
			addImageName="";
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
	
	public void deletePic(String fileName){
    	File file = new File(fileName);
    	File[] fileList = file.listFiles();
    	if(fileList!=null && fileList.length>0){
    		for(File f : fileList){
        		if(f.isDirectory()){
        			deletePic(f.getName());//图片文件里面没有文件夹，一般不会执行的
        		}else{
        			f.delete();
        		}
        	}
    	}
    }
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO 自动生成的方法存根
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
			if(upLoadImgNum<=0){
				deletePic(PathConstant.PATH_cachephoto);
				finish();
				return true;
			}else{
				Toast.makeText(this, "正在上传图片,请耐心等待", Toast.LENGTH_SHORT).show();
				return false;
			}
		}else{
			return true;
		}
//		return super.onKeyDown(keyCode, event);
	}
}
