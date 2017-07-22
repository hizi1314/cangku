package com.guantang.cangkuonline.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;










import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.database.DataBaseCheckMethod;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.database.DataBaseMethod;
import com.guantang.cangkuonline.dialog.CommonDialog;
import com.guantang.cangkuonline.helper.CheckEditWatcher;
import com.guantang.cangkuonline.helper.DJWatcher;
import com.guantang.cangkuonline.helper.EditHelper;
import com.guantang.cangkuonline.helper.NumberWatcher;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;
import com.guantang.cangkuonline.static_constant.PathConstant;
import com.guantang.cangkuonline.webservice.WebserviceImport;
import com.guantang.cangkuonline.webservice.WebserviceImport_more;

public class AddActivity extends Activity implements OnClickListener{
	private Button leibiexuanzeBtn;
	private ImageButton backImg_Btn, saveImg_Btn;
	private ImageView scanImgView;
	private LinearLayout res2_layout, res3_layout, morelayout, img;
	private TextView sccsTextView,lbTextView,imgTextView, imgtext, title, res1_text, res2_text,
			res3_text, res4_text, res5_text, res6_text,sameTextView;
	private EditText tmEditText, bmEditText, nameEditText, ggEditText,
			dwEditText, sxEditText, xxEditText, rkckjEditText,
			ckckjEditText, ckckjEditText2, dwEditText2, bignumEditText,
			res1EditText, res2EditText, res3EditText, res4EditText,
			res5EditText, res6EditText, bzEditText;
	private ScrollView scrollView;
	public  String stm = "", sbm = "", sname = "", sgg = "", sdw = "",
			ssx = "", sxx = "", ssccs = "", srkckj = "", sckckj = "",
			sckckj2 = "", sdw2 = "", sbignum = "", sres1 = "", sres2 = "",
			sres3 = "", sbz = "", slb = "未选择", slbid = "-1", sindex = "",
			sid = "", sres4 = "", sres5 = "", sres6 = "";
	String[] str={DataBaseHelper.HPMC,DataBaseHelper.HPBM,DataBaseHelper.HPTM,DataBaseHelper.GGXH,
			DataBaseHelper.CurrKC,DataBaseHelper.JLDW,DataBaseHelper.HPSX,DataBaseHelper.HPXX,
			DataBaseHelper.SCCS,DataBaseHelper.BZ,DataBaseHelper.RKCKJ,DataBaseHelper.CKCKJ,DataBaseHelper.LBIndex,
			DataBaseHelper.CKCKJ2,DataBaseHelper.JLDW2,DataBaseHelper.BigNum,DataBaseHelper.RES1,DataBaseHelper.LBID,
			DataBaseHelper.RES2,DataBaseHelper.RES3,DataBaseHelper.LBS,DataBaseHelper.ImagePath,DataBaseHelper.KCJE
			,DataBaseHelper.JLDW2};
	String[] str1={"HPMC","HPBM","HPTBM","GGXH","JLDW","HPSX","HPXX","SCCS","BZ","RKCKJ","CKCKJ",
			"BigNum","res1","res2","res3","res4","res5","res6","LBS","LBIndex","LBID","JLDW2","ImageUrl"};
	private List<Map<String, Object>> ls, ls1;
	private List<Map<String, Object>> lis = new ArrayList<Map<String, Object>>();
	private List<String> ImageNameList = new ArrayList<String>();
	private DataBaseMethod dm = new DataBaseMethod(this);
	private DataBaseCheckMethod dm_ck = new DataBaseCheckMethod(this);
	private ProgressDialog pro_dialog;
	private SimpleDateFormat formatter;
	private NumberWatcher numberWatcher = new NumberWatcher();
	private DJWatcher djWatcher = new DJWatcher();
	private CheckEditWatcher cked = new CheckEditWatcher();
	private String[] values_name;
	private static 	int REQUEST_CODE=1;
	private int pic_num=0;
	private SharedPreferences mSharedPreferences;
	private int width=0,height=0;
	private String hpid="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_activity);
		mSharedPreferences = getSharedPreferences(ShareprefenceBean.SHAREPREFENCE_NAME, Context.MODE_PRIVATE);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels;
		height = dm.heightPixels;
		initControl();
		init();
	}
	
	@Override
	protected void onStart() {
		// TODO 自动生成的方法存根
		super.onStart();
	}
	
	public void initControl() {
		res2_layout = (LinearLayout) findViewById(R.id.res2_layout);
		res3_layout = (LinearLayout) findViewById(R.id.res3_layout);
		morelayout = (LinearLayout) findViewById(R.id.morelayout);
		img = (LinearLayout) findViewById(R.id.img);
		scrollView = (ScrollView) findViewById(R.id.addscoll);
		scanImgView = (ImageView) findViewById(R.id.scan);
		sameTextView = (TextView) findViewById(R.id.same);
		imgTextView= (TextView) findViewById(R.id.imgBtn);
		backImg_Btn = (ImageButton) findViewById(R.id.back);
		saveImg_Btn = (ImageButton) findViewById(R.id.ok);
		lbTextView = (TextView) findViewById(R.id.lb);
		res1_text = (TextView) findViewById(R.id.res1_text);
		res2_text = (TextView) findViewById(R.id.res2_text);
		res3_text = (TextView) findViewById(R.id.res3_text);
		res4_text = (TextView) findViewById(R.id.res4_text);
		res5_text = (TextView) findViewById(R.id.res5_text);
		res6_text = (TextView) findViewById(R.id.res6_text);
		title = (TextView) findViewById(R.id.title);
		tmEditText = (EditText) findViewById(R.id.tm);
		bmEditText = (EditText) findViewById(R.id.bm);
		nameEditText = (EditText) findViewById(R.id.name);
		ggEditText = (EditText) findViewById(R.id.gg);
		dwEditText = (EditText) findViewById(R.id.dw);
		sxEditText = (EditText) findViewById(R.id.sx);
		xxEditText = (EditText) findViewById(R.id.xx);
		sccsTextView = (TextView) findViewById(R.id.sccs);
		rkckjEditText = (EditText) findViewById(R.id.rkckj);
		ckckjEditText = (EditText) findViewById(R.id.ckckj);
		ckckjEditText2 = (EditText) findViewById(R.id.ckckj2);
		dwEditText2 = (EditText) findViewById(R.id.dw2);
		bignumEditText = (EditText) findViewById(R.id.bignum);
		bzEditText = (EditText) findViewById(R.id.bz);
		res1EditText = (EditText) findViewById(R.id.res1);
		res2EditText = (EditText) findViewById(R.id.res2);
		res3EditText = (EditText) findViewById(R.id.res3);
		res4EditText = (EditText) findViewById(R.id.res4);
		res5EditText = (EditText) findViewById(R.id.res5);
		res6EditText = (EditText) findViewById(R.id.res6);
		
		lbTextView.setOnClickListener(this);
		img.setOnClickListener(this);
		scanImgView.setOnClickListener(this);
		sameTextView.setOnClickListener(this);
		backImg_Btn.setOnClickListener(this);
		saveImg_Btn.setOnClickListener(this);
		sccsTextView.setOnClickListener(this);
		img.setOnClickListener(this);
		tmEditText.addTextChangedListener(cked);
		bmEditText.addTextChangedListener(cked);
		nameEditText.addTextChangedListener(cked);
		ggEditText.addTextChangedListener(cked);
		dwEditText.addTextChangedListener(cked);
		dwEditText2.addTextChangedListener(cked);
		res1EditText.addTextChangedListener(cked);
		res2EditText.addTextChangedListener(cked);
		res3EditText.addTextChangedListener(cked);
		res4EditText.addTextChangedListener(cked);
		res5EditText.addTextChangedListener(cked);
		res6EditText.addTextChangedListener(cked);
		bzEditText.addTextChangedListener(cked);
		sxEditText.addTextChangedListener(numberWatcher);
		xxEditText.addTextChangedListener(numberWatcher);
		rkckjEditText.addTextChangedListener(djWatcher);
		ckckjEditText.addTextChangedListener(djWatcher);
		ckckjEditText2.addTextChangedListener(djWatcher);
		bignumEditText.addTextChangedListener(numberWatcher);
		
		ls1 = new ArrayList<Map<String, Object>>();
		ls1 = dm_ck.Gt_Res();
		if (ls1.size() != 0){
			setRes(ls1);
		}
	}
	
	public void init() {
		formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}
	
	public void intiTextView(){
		tmEditText.setText("");
		bmEditText.setText("");
		nameEditText.setText("");
		ggEditText.setText("");
		dwEditText.setText("");
		sxEditText.setText("");
		xxEditText.setText("");
		sccsTextView.setText("未选择");
		rkckjEditText.setText("");
		ckckjEditText.setText("");
		ckckjEditText2.setText("");
		dwEditText2.setText("");
		bignumEditText.setText("");
		bzEditText.setText("");
		res1EditText.setText("");
		res2EditText.setText("");
		res3EditText.setText("");
		res4EditText.setText("");
		res5EditText.setText("");
		res6EditText.setText("");
		lbTextView.setText("未选择");
		imgTextView.setText("无");
		ImageNameList.clear();
	}
	
	private void setRes(List<Map<String, Object>> list) {
		if (list.size() != 0) {
			String str;
			str = (String) list.get(0).get("文本型1");
			if (str == null || str.equals("")) {
				res1_text.setText("扩展字段1");
			} else {
				res1_text.setText(str);
			}
			str = (String) list.get(1).get("文本型2");
			if (str == null || str.equals("")) {
				res2_text.setText("扩展字段2");
			} else {
				res2_text.setText(str);
			}
			str = (String) list.get(2).get("文本型3");
			if (str == null || str.equals("")) {
				res3_text.setText("扩展字段3");
			} else {
				res3_text.setText(str);
			}
			str = (String) list.get(3).get("文本型4");
			if (str == null || str.equals("")) {
				res4_text.setText("扩展字段4");
			} else {
				res4_text.setText(str);
			}
			str = (String) list.get(4).get("文本型5");
			if (str == null || str.equals("")) {
				res5_text.setText("扩展字段5");
			} else {
				res5_text.setText(str);
			}
			str = (String) list.get(5).get("文本型6");
			if (str == null || str.equals("")) {
				res6_text.setText("扩展字段6");
			} else {
				res6_text.setText(str);
			}
		}
	}
	
	Runnable addRun = new Runnable(){

		@Override
		public void run() {
			Message msg = new Message();
			String json=WebserviceImport.AddHP_1_0(values_name,str1, tmEditText.getText().toString(),mSharedPreferences);
			try {
				JSONObject addHPNew_ReturnBean = new JSONObject(json);
				if(addHPNew_ReturnBean.getInt("Status")==1){
					JSONObject addHPNew_Data = addHPNew_ReturnBean.getJSONObject("Data");
					hpid = addHPNew_Data.getString("id");//获取新添加的货品的id
					JSONArray imageUrlArray=addHPNew_Data.getJSONArray("Imageurl");//获取多张图片名称JSON数组
					String Hpxx="0",Hpsx="0";
					if(sxEditText.getText().toString().equals("")){
						Hpsx="0";
					}else{
						Hpsx=sxEditText.getText().toString();
					}
					if(xxEditText.getText().toString().equals("")){
						Hpxx="0";
					}else{
						Hpxx=xxEditText.getText().toString();
					}
					dm.del_hp(hpid);
					String date=formatter.format(new Date(System.currentTimeMillis()));
					dm.Addhp(Integer.parseInt(hpid),nameEditText.getText().toString().trim(),
							addHPNew_Data.getString("HPBM"),tmEditText.getText().toString().trim(), 
							ggEditText.getText().toString().trim(),dwEditText.getText().toString().trim(),
							dwEditText2.getText().toString().trim(),
							bignumEditText.getText().toString().trim(), "0", "0",
							sccsTextView.getText().toString().trim(),Hpsx.trim(), 
							Hpxx.trim(),rkckjEditText.getText().toString().trim(),
							ckckjEditText.getText().toString().trim(), 
							ckckjEditText2.getText().toString().trim(),lbTextView.getText().toString().trim(),sid,sindex,
							bzEditText.getText().toString().trim(),"", 0, 
							res1EditText.getText().toString().trim(),
							res2EditText.getText().toString().trim(),res3EditText.getText().toString().trim(),
							res4EditText.getText().toString().trim(),res5EditText.getText().toString().trim(),
							res6EditText.getText().toString().trim(), "", "", 0, date,"");
					dm.deletePICurl(hpid);
					
					int ImageNameListSize = ImageNameList.size();
					List<String> errorList = new ArrayList<String>();//图片上传失败信息集合
					for(int i=0;i<ImageNameListSize;i++){
						String imageUrl=ImageNameList.get(i);
						FileInputStream fis = null;
						File file = null;
						try {
							file = new File(PathConstant.PATH_photo+imageUrl);
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
							BitmapFactory.decodeFile(PathConstant.PATH_photo+imageUrl,opt);
							while ((opt.outHeight / opt.inSampleSize) > width
									&& (opt.outWidth / opt.inSampleSize) > height) {
								opt.inSampleSize *= 2;
							}
							opt.inJustDecodeBounds=false;
							Bitmap bm=BitmapFactory.decodeFile(PathConstant.PATH_photo+imageUrl,opt);
							if(bm==null){
								ByteArrayOutputStream bStream = new ByteArrayOutputStream();
							    String base64string = Base64.encodeToString(bStream.toByteArray(), Base64.DEFAULT);
							    int j=WebserviceImport.AddImage(base64string,imageUrlArray.getString(i),Integer.parseInt(hpid),mSharedPreferences);
							    if(j<0){
							    	errorList.add(imageUrlArray.getString(i));
							    }else{
							    	dm.insertTB_PIC(hpid, imageUrlArray.getString(i));
							    }
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
							    int j=WebserviceImport.AddImage(base64string,imageUrlArray.getString(i),Integer.parseInt(hpid),mSharedPreferences);
							    if(j<0){
							    	errorList.add(imageUrlArray.getString(i));
							    }else{
							    	dm.insertTB_PIC(hpid, imageUrlArray.getString(i));
							    }
							    bm.recycle();
								bm=null;
								bStream=null;
								System.gc();
							}
							
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							errorList.add(imageUrlArray.getString(i));
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							errorList.add(imageUrlArray.getString(i));
							e.printStackTrace();
						}finally{
							file.renameTo(new File(PathConstant.PATH_photo+imageUrlArray.getString(i)));
							try {
								fis.close();
							} catch (IOException e) {
								// TODO 自动生成的 catch 块
								e.printStackTrace();
							}
						}
					}
					if(errorList.isEmpty()){
						msg.what=1;
						msg.obj=addHPNew_ReturnBean.getString("Message");
						mAddHandler.sendMessage(msg);
					}else{
						msg.what=-1;
						msg.obj=errorList;
						mAddHandler.sendMessage(msg);
					}
				}else{
					msg.what=-2;
					msg.obj=addHPNew_ReturnBean.getString("Message");
					mAddHandler.sendMessage(msg);
				}
			} catch (JSONException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
				msg.what=1;
				msg.obj="Json解析出错";
				mAddHandler.sendMessage(msg);
			}	
		}
	};
	
	
	Handler mAddHandler=new Handler(){
		public void handleMessage(Message msg) {
			pro_dialog.dismiss();
			switch(msg.what){
			case 1://货品图片上传成功
				intiTextView();
				Toast.makeText(AddActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
				break;
			case -1://图片上传失败，货品上传成功
				final List<String> errorList = (List<String>) msg.obj;
				String mystr = "";
				int errorListSize = errorList.size();
				for(int i=0;i<errorListSize;i++){
					if(mystr.equals("")){
						mystr=errorList.get(i);
					}else{
						mystr += ","+errorList.get(i);
					}
				}
				Toast.makeText(AddActivity.this, "图片"+mystr+"上传失败", Toast.LENGTH_SHORT).show();
				
//				CommonDialog commonDialog1 = new CommonDialog(this, layout, style);
				AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);
				builder.setMessage("有图片上传失败，是否续传？");
				builder.setNegativeButton("放弃", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO 自动生成的方法存根
						pro_dialog = ProgressDialog.show(AddActivity.this, null, "删除图片信息");
						new DelImageUrlAsynctask().execute(errorList);
						for(int i=0;i<errorList.size();i++){
							File file = new File(PathConstant.PATH_photo+errorList.get(i));
							if(file.exists()){
								file.delete();
							}
						}
						dialog.dismiss();
					}
				});
				builder.setPositiveButton("继续上传", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO 自动生成的方法存根
						pro_dialog = ProgressDialog.show(AddActivity.this, null, "继续上传图片信息");
						new UpLoadAsynctask().execute(errorList);
						dialog.dismiss();
					}
				});
				
				break;
			case -2://货品上传失败
//				intiTextView();
				if(msg.obj.toString().equals("图片名称获取失败")){
					//跳转到图片获取修改界面
					Toast.makeText(AddActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(AddActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
				}
				break;
			}
		};
	};
	
	class DelImageUrlAsynctask extends AsyncTask<List<String>, Void, Integer>{
		private List<String> imageUrlList = new ArrayList<String>();
		private int hpid = 0,imageUrlListSize = 0;
		@Override
		protected Integer doInBackground(List<String>... params) {
			// TODO 自动生成的方法存根
			imageUrlList = params[0];
			hpid = Integer.parseInt(imageUrlList.get(0).substring(0, imageUrlList.get(0).indexOf("-")));
			imageUrlListSize = imageUrlList.size();
			String mystr = "";
			for(int i=0;i<imageUrlListSize;i++){
				if(mystr.equals("")){
					mystr=imageUrlList.get(i);
				}else{
					mystr += ","+imageUrlList.get(i);
				}
			}
			String value=WebserviceImport_more.DeleteImageRecord(mystr, hpid, mSharedPreferences);
			return Integer.parseInt(value);
		}
		@Override
		protected void onPostExecute(Integer result) {
			// TODO 自动生成的方法存根
			super.onPostExecute(result);
			//删除手机的图片和路径
			intiTextView();
			for(int i=0;i<imageUrlListSize;i++){
				String imageName = imageUrlList.get(i);
				File file = new File(PathConstant.PATH_photo+imageName);
				if(file.exists()){
					file.delete();
				}
				dm.deletePIC_OneUrl(String.valueOf(hpid), imageName);
			}
			pro_dialog.dismiss();
		}
	}
	
	class UpLoadAsynctask extends AsyncTask<List<String>, Void, List<String>>{

		@Override
		protected List<String> doInBackground(List<String>... params) {
			// TODO 自动生成的方法存根
			List<String> imageUrlList = params[0];
			List<String> errorList = new ArrayList<String>();//图片上传失败信息集合
			int imageUrlListSize = imageUrlList.size();
			for(int i=0;i<imageUrlListSize;i++){
				String imageUrl=imageUrlList.get(i);
				FileInputStream fis = null;
				File file = null;
				try {
					file = new File(PathConstant.PATH_photo+imageUrl);
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
					BitmapFactory.decodeFile(PathConstant.PATH_photo+imageUrl,opt);
					while ((opt.outHeight / opt.inSampleSize) > width
							&& (opt.outWidth / opt.inSampleSize) > height) {
						opt.inSampleSize *= 2;
					}
					opt.inJustDecodeBounds=false;
					Bitmap bm=BitmapFactory.decodeFile(PathConstant.PATH_photo+imageUrl,opt);
					if(bm==null){
						ByteArrayOutputStream bStream = new ByteArrayOutputStream();
					    String base64string = Base64.encodeToString(bStream.toByteArray(), Base64.DEFAULT);
					    int j=WebserviceImport.AddImage(base64string,imageUrl,Integer.parseInt(imageUrl.substring(0, imageUrl.indexOf("-"))),mSharedPreferences);
					    if(j<0){
					    	errorList.add(imageUrl);
					    }else{
					    	dm.insertTB_PIC(hpid, imageUrl);
					    }
					}else{
						ByteArrayOutputStream bStream = new ByteArrayOutputStream();
				        bm.compress(CompressFormat.JPEG, 100, bStream); 
					    int options = 100;  
					    while ( bStream.toByteArray().length / 1024>512) {//循环判断如果压缩后图片是否大于500kb,大于继续压缩         
					    	bStream.reset();//重置bStream即清空bStream
					    	bm.compress(Bitmap.CompressFormat.JPEG, options, bStream);//这里压缩options%，把压缩后的数据存放到baos中  
					    	options -= 10;//每次都减少10
					    }
					    String base64string = Base64.encodeToString(bStream.toByteArray(), Base64.DEFAULT);
					    int j=WebserviceImport.AddImage(base64string,imageUrl,Integer.parseInt(imageUrl.substring(0, imageUrl.indexOf("-"))),mSharedPreferences);
					    if(j<0){
					    	errorList.add(imageUrl);
					    }else{
					    	dm.insertTB_PIC(hpid, imageUrl);
					    }
					    bm.recycle();
						bm=null;
						bStream=null;
						System.gc();
					}
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					errorList.add(imageUrl);
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					errorList.add(imageUrl);
					e.printStackTrace();
				}finally{
					try {
						fis.close();
					} catch (IOException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
				}
			}
			if(errorList.isEmpty()){
				return errorList;
			}else{
				return errorList;
			}
		}
		@Override
		protected void onPostExecute(List<String> result) {
			// TODO 自动生成的方法存根
			super.onPostExecute(result);
			pro_dialog.dismiss();
			final List<String> errorList = result;
			if(errorList.isEmpty()){
				intiTextView();
				Toast.makeText(AddActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
			}else{
				AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);
				builder.setMessage("有图片上传失败，我们将删除错误图片信息，您可以在货品详情页面继续添加图片！");
				builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO 自动生成的方法存根
						pro_dialog = ProgressDialog.show(AddActivity.this, null, "删除图片信息");
						new DelImageUrlAsynctask().execute(errorList);
						for(int i=0;i<errorList.size();i++){
							File file = new File(PathConstant.PATH_photo+errorList.get(i));
							if(file.exists()){
								file.delete();
							}
						}
						dialog.dismiss();
					}
				});
				
			}
		}
	};
	
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		Intent intent=new Intent();
		switch(v.getId()){
		case R.id.ok:
			if(mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1)==1){
				if(!nameEditText.getText().toString().trim().equals("")){
					if(!sxEditText.getText().toString().isEmpty() && !xxEditText.getText().toString().isEmpty()){
						if(Double.parseDouble(sxEditText.getText().toString())<Double.parseDouble(xxEditText.getText().toString())){
							Toast.makeText(AddActivity.this, "禁止货品下限大于货品上线", Toast.LENGTH_SHORT).show();
							return;
						}
					}
					int ImageNameListSize = ImageNameList.size();
					String imageSuffix = "";
					for(int i=0;i<ImageNameListSize;i++){
						String[] strArray=ImageNameList.get(i).split("\\.");
						if(strArray.length>1){
							if(imageSuffix.equals("")){
								imageSuffix = "."+strArray[strArray.length-1];
							}else{
								imageSuffix +=",."+ strArray[strArray.length-1];
							}
						}else{
							imageSuffix +=",";
						}
						
					}
					String data_time=formatter.format(new Date(System.currentTimeMillis()));
					
					values_name=new String[] {nameEditText.getText().toString().trim(),
							bmEditText.getText().toString().replace("\"", "“").trim(),tmEditText.getText().toString().trim(),
							ggEditText.getText().toString().trim(),dwEditText.getText().toString().trim(),
							sxEditText.getText().toString().trim(),xxEditText.getText().toString().trim(),
							sccsTextView.getText().toString().trim(),bzEditText.getText().toString().trim(),
							rkckjEditText.getText().toString().trim(),ckckjEditText.getText().toString().trim(),
							bignumEditText.getText().toString().trim(),res1EditText.getText().toString().trim(),
							res2EditText.getText().toString().trim(),res3EditText.getText().toString().trim(),
							res4EditText.getText().toString().trim(),res5EditText.getText().toString().trim(),
							res6EditText.getText().toString().trim(),lbTextView.getText().toString().trim(),
							sindex,sid,dwEditText2.getText().toString().trim(),imageSuffix};
					String hpsx="0",hpxx="0";
					if(sxEditText.getText().toString().equals("")){
						hpsx="0";
					}else{
						hpsx=sxEditText.getText().toString();
					}
					if(xxEditText.getText().toString().equals("")){
						hpsx="0";
					}else{
						hpsx=xxEditText.getText().toString();
					}
					if(Float.parseFloat(hpsx)<Float.parseFloat(hpxx)&& (!hpsx.equals("0")&& !hpxx.equals("0"))){
						Toast.makeText(this, "货品上限不能小于货品下限", Toast.LENGTH_SHORT).show();
					}else{
						if(WebserviceImport.isOpenNetwork(this)){
							pro_dialog = ProgressDialog.show(AddActivity.this,null, "正在添加数据");
							new Thread(addRun).start();
						}else{
							Toast.makeText(this, "网络未连接，请连接网络", Toast.LENGTH_SHORT).show();
						}
					}
				}else{
					Toast.makeText(this, "货品名称不能为空", Toast.LENGTH_SHORT).show();
				}
			}else if(mSharedPreferences.getInt(ShareprefenceBean.IS_LOGIN, -1)==-1){
				Toast.makeText(this, "离线模式下不能进行新增货品操作", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.same:
			bmEditText.setText(tmEditText.getText().toString());
			break;
		case R.id.scan:
			intent.setClass(this, CaptureActivity.class);
			startActivityForResult(intent, REQUEST_CODE);
			break;
		case R.id.back:
			if((!ImageNameList.isEmpty()||!bmEditText.getText().toString().equals(""))||(!nameEditText.getText().toString().equals(""))){
				final AlertDialog.Builder builder=new AlertDialog.Builder(this);
				builder.setMessage("请保存数据，如果退出，你所填写的数据不会保存且图片会被删除，确定退出？");
				builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO 自动生成的方法存根
						Iterator<String> it=ImageNameList.iterator();
						while(it.hasNext()){
							File file=new File(PathConstant.PATH_photo+it.next());
							if(file.exists()){
								file.delete();
							}
						}
//						dm.updatehp_byBM(DataBaseHelper.ImagePath, null, bmEditText.getText().toString());
						finish();
					}
				});
				builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO 自动生成的方法存根
						builder.create().dismiss();
					}
				});
				builder.create().show();
			}else{
				finish();
			}
			break;
		case R.id.sccs:
			intent.setClass(this, DwListActivity.class);
			startActivityForResult(intent, 2);
			break;
		case R.id.lb:
			intent.setClass(this, LB_ChoseActivity.class);
			startActivityForResult(intent, 3);
			break;
		case R.id.img:
//			if(bmEditText.getText().toString().equals(null)||bmEditText.getText().toString().equals("")){
//				Toast.makeText(this, "货品编码是图片唯一标识，请填写\n货品编码并保证货品编码正确", Toast.LENGTH_LONG).show();
//			}else{
//				intent.putExtra("imgpath", imgpath);
//				intent.putExtra("bmh", bmEditText.getText().toString());
//				intent.putExtra("showpic_add", true);//在添加货品时能查看每上传的图片
//				intent.setClass(this, Img_chose.class);
//				startActivityForResult(intent, 0);
//			}
			intent.setClass(this, AddPhotoActivity.class);
			intent.putExtra("ImageNameList",(Serializable) ImageNameList);
			startActivityForResult(intent, 0);
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO 自动生成的方法存根
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if(!ImageNameList.isEmpty()||(!bmEditText.getText().toString().equals(""))||(!nameEditText.getText().toString().equals(""))){
				final AlertDialog.Builder builder=new AlertDialog.Builder(this);
				builder.setMessage("请保存数据，如果退出，你所填写的数据不会保存且图片会被删除，确定退出？");
				builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO 自动生成的方法存根
						Iterator<String> it=ImageNameList.iterator();
						while(it.hasNext()){
							File file = new File(PathConstant.PATH_photo+it.next());
							if(file.exists()){
								file.delete();
							}
						}
						finish();
					}
				});
				builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO 自动生成的方法存根
						builder.create().dismiss();
					}
				});
				builder.create().show();
			}else{
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==REQUEST_CODE){
			if(resultCode==1){
				String scan_tm=data.getStringExtra("scan_tm");
				tmEditText.setText(scan_tm);
			}
		}else if(requestCode==0){
			if(resultCode==1){
				ImageNameList= (List<String>) data.getSerializableExtra("ImageNameList");
				imgTextView.setText(String.valueOf(ImageNameList.size())+"张图片");
			}
		}else if(requestCode==2){
			if(resultCode==1){
				Map<String,Object> map = (Map<String, Object>) data.getSerializableExtra("dwmap");
				sccsTextView.setText(map.get(DataBaseHelper.DWName).toString());
				
			}
		}else if(requestCode==3){
			if(resultCode==1){
				lbTextView.setText(data.getStringExtra("lb"));
				sindex = data.getStringExtra("index");
				sid = data.getStringExtra("lbid");
			}
		}
	}
	
}
