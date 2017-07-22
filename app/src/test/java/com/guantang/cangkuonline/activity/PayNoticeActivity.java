package com.guantang.cangkuonline.activity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.application.MyApplication;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;
import com.guantang.cangkuonline.webservice.WebserviceImport;
import com.guantang.cangkuonline.webservice.WebserviceImport_more;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Xml;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class PayNoticeActivity extends Activity {
	private ImageButton backImgBtn;
	private WebView myWebView;
	private ImageView mImageView;
	private LinearLayout titlelayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_paynotice);
		initView();
		init();
//		upStringAsyncTask.execute("试一下行不行");
//		upBitmapAsyncTask.execute();
//		getHttpBitmap();
	}
	
	public void initView(){
		titlelayout = (LinearLayout) findViewById(R.id.titlelayout);
		backImgBtn = (ImageButton) findViewById(R.id.back);
		mImageView = (ImageView) findViewById(R.id.myImgView);
		myWebView = (WebView) findViewById(R.id.webview1);
		backImgBtn.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				finish();
			}
		});
	}
	
	public void init(){
		WebSettings myWebSettings = myWebView.getSettings();
		myWebSettings.setDefaultTextEncodingName("UTF-8");
		myWebSettings.setJavaScriptEnabled(true);  
		myWebView.loadUrl("http://www.gtcangku.com/buying.html");
//		设置此属性，仅支持双击缩放，不支持触摸缩放（在android4.0是这样，其他平台没试过）
		myWebSettings.setSupportZoom(true);
		
//		如果设置了此属性，那么webView.getSettings().setSupportZoom(true);也默认设置为true
		myWebSettings.setBuiltInZoomControls(true);
//		myWebView.setInitialScale(100); 
//		myWebView.getSettings().setUseWideViewPort(true);//设置此属性，可任意比例缩放。
		myWebView.requestFocus();
//		myWebView.setScrollBarStyle(0);
		myWebView.setWebViewClient(new WebViewClient(){
		 	
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
            	
            	view.loadUrl(url);
                    return true;
            }
		});
		
		// 第三步添加js接口
		myWebView.addJavascriptInterface(new JavaScriptToAndroid(),"control");
	}
	
	final class JavaScriptToAndroid {
		@JavascriptInterface
		public void detTitle(int i,String url) {// 跳转后去掉title
//			titlelayout.setVisibility(View.GONE);
			Uri uri = Uri.parse(url);
			Intent it  = new Intent(Intent.ACTION_VIEW,uri);

//			Intent it = new Intent();
//			it.setAction(Intent.ACTION_VIEW);
//			it.setData(uri);
			startActivity(it);
        }
	}
	
	
	
	public void getHttpBitmap(){
		if(WebserviceImport.isOpenNetwork(this)){
			new GetImageNameAsyncTask().execute("322");
		}else{
			Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
		}
	}
	
	class GetImageNameAsyncTask extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO 自动生成的方法存根
			String jsonString = WebserviceImport_more.GetImageUrl_1_0(params[0], MyApplication.getInstance().getSharedPreferences());
			return jsonString;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO 自动生成的方法存根
			super.onPostExecute(result);
			try {
				JSONObject jsonObject = new JSONObject(result);
				switch(jsonObject.getInt("Status")){
				case 1:
					JSONArray jsonArray = jsonObject.getJSONArray("Data");
					JSONObject dataObject=jsonArray.getJSONObject(0);
					String urlString  = MyApplication.getInstance().getSharedPreferences().getString(ShareprefenceBean.NET_URL, "")+dataObject.getString("ImageURL");
					new DownLoadBitmapAsyncTask().execute(urlString);
					break;
					default:
						Toast.makeText(PayNoticeActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
						break;
				}
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}
	
	class DownLoadBitmapAsyncTask  extends AsyncTask<String, Void, Bitmap>{

		@Override
		protected Bitmap doInBackground(String... params) {
			// TODO 自动生成的方法存根
			Bitmap bm = downLoadBitmap(params[0]);
			return  bm;
		}
		
		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO 自动生成的方法存根
			super.onPostExecute(result);
			if(null == result){
				;
			}
			mImageView.setImageBitmap(result);
		}
	}
	
	public Bitmap downLoadBitmap(String imageUrl){
		URL url = null;
		HttpURLConnection conn = null;
		InputStream is = null;
		Bitmap mBitmap = null;
		try {
			url = new URL(imageUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.connect();
			
			is = conn.getInputStream();
			mBitmap = BitmapFactory.decodeStream(is);

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (conn != null) {
					conn.disconnect();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(null == mBitmap){
			;
		}
		return mBitmap;
	}
	
	AsyncTask<String, Void, String> upStringAsyncTask = new AsyncTask<String, Void, String>(){

		@Override
		protected String doInBackground(String... params) {
			// TODO 自动生成的方法存根
			String sssString = upLoadString(params[0]);
			return sssString;
		}
		protected void onPostExecute(String result) {
			Toast.makeText(PayNoticeActivity.this, result, Toast.LENGTH_SHORT).show();
		};
	};
	
	public String upLoadString(String content){
//		String myString=new String(content.getBytes(), "utf-8");
		String myContent = ("name="+content);
		StringBuffer returnString = new StringBuffer();
		URL url = null;
		HttpURLConnection conn = null;
		OutputStream ops =null;
		try {
			byte[] data = myContent.getBytes("utf-8");
			url = new URL("http://ck1.gtcangku.com/GtStorageWebservice.asmx/getname");
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Content-Length", String.valueOf(data.length));
//			conn.setRequestProperty("name", myContent);
			conn.setConnectTimeout(10000);
			conn.connect();
			
//			PrintWriter pw = new PrintWriter(conn.getOutputStream());
//			pw.print("adfbhgsd");
//			pw.flush();
			
			ops = conn.getOutputStream();
			ops.write(data);
			ops.flush();
			int CodeNum = conn.getResponseCode();
			if(conn.getResponseCode()==200){  
				// 创建包装流
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
				returnString.append(myXMLparser(br));
//				// 定义String类型用于储存单行数据
//				String line = null;
//				// 创建StringBuffer对象用于存储所有数据
//				while ((line = br.readLine()) != null) {
//					returnString.append(line);
//				}
	        }
		} catch (MalformedURLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} finally{
			try {
				ops.close();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			conn.disconnect();
		}
		return returnString.toString();
	}
	
	
	AsyncTask<Void, Void, String> upBitmapAsyncTask = new AsyncTask<Void, Void, String>(){

		@Override
		protected String doInBackground(Void... params) {
			// TODO 自动生成的方法存根
			String sssString = upLoadBitmap();
			return sssString;
		}
		protected void onPostExecute(String result) {
			Toast.makeText(PayNoticeActivity.this, result, Toast.LENGTH_SHORT).show();
		};
	};
	
	public String upLoadBitmap(){
		StringBuffer returnString = new StringBuffer();
		URL url = null;
		HttpURLConnection conn = null;
		OutputStream ops =null;
		try {
			InputStream picInputStream = getAssets().open("mather.png");
			Bitmap bm = BitmapFactory.decodeStream(picInputStream);
			ByteArrayOutputStream bStream = new ByteArrayOutputStream();
	        bm.compress(CompressFormat.JPEG, 100, bStream);
			byte[] data = bStream.toByteArray();
			byte[] data2 = "streamByte=".getBytes();
			url = new URL("http://ck1.gtcangku.com/GtStorageWebservice.asmx/ReturnPhotobyte");
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Content-Length", String.valueOf(data.length+data2.length));
			conn.setConnectTimeout(10000);
			conn.connect();
			ops = conn.getOutputStream();
			ops.write(data2);
			ops.write(data);
			ops.flush();
			int CodeNum = conn.getResponseCode();
			if(conn.getResponseCode()==200){  
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
				returnString.append(myXMLparser(br));
//				// 定义String类型用于储存单行数据
//				String line = null;
//				// 创建StringBuffer对象用于存储所有数据
//				while ((line = br.readLine()) != null) {
//					returnString.append(line);
//				}
	        }
		} catch (MalformedURLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} finally{
			try {
				ops.close();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			conn.disconnect();
		}
		return returnString.toString();
	}
	
	public String myXMLparser(BufferedReader br){
		String sss = "";
		XmlPullParser parser = Xml.newPullParser();// 由android.util.Xml创建一个XmlPullParser实例
		try {
			parser.setInput(br);
			int eventType = parser.getEventType();//返回当前项类型,如:START_TAG(开始标签), END_TAG(结束标签), TEXT(文本内容), etc.)
			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					if (parser.getName().equals("string")) {
						eventType = parser.next();
						sss=parser.getText();
					}
					break;
				case XmlPullParser.END_TAG:
					break;
				}
				eventType = parser.next();
			}
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}  catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return sss;
	}
}
