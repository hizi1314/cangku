package com.guantang.cangkuonline.JellyViewPager;

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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.activity.PayNoticeActivity;
import com.guantang.cangkuonline.application.MyApplication;
import com.guantang.cangkuonline.eventbusBean.NumberBitmap;
import com.guantang.cangkuonline.eventbusBean.ObjectFive;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;
import com.guantang.cangkuonline.static_constant.PathConstant;
import com.guantang.cangkuonline.webservice.WebserviceImport;
import com.guantang.cangkuonline.webservice.WebserviceImport_more;

import de.greenrobot.event.EventBus;
import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class NetPictureFragment extends Fragment {
	private ZoomImageView imageView;
	private int width = 0, height = 0;
	private ProgressBar mProgressBar;
	private String imagePath = "";
	private String hpid = "";

	@Override
	public void onAttach(Activity activity) {
		// TODO 自动生成的方法存根
		super.onAttach(activity);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.picture_layout, null);
		imageView = (ZoomImageView) v.findViewById(R.id.imageView1);
		mProgressBar = (ProgressBar) v.findViewById(R.id.mybar);
		Bundle bundle = getArguments();
		width = bundle.getInt("width", 0);
		height = bundle.getInt("height", 0);
		imagePath = bundle.getString("ImageName");
		hpid = bundle.getString("hpid");

		File file = new File(PathConstant.PATH_cachephoto + imagePath);
		if (file.exists()) {
			mProgressBar.setVisibility(View.VISIBLE);
			new GetImageAsyncTask().execute(PathConstant.PATH_cachephoto + imagePath);
		} else {
			if(WebserviceImport.isOpenNetwork(getActivity())){
				mProgressBar.setVisibility(View.VISIBLE);
				new GetImageNameAsyncTask().execute(hpid);
			}else{
				Toast.makeText(getActivity(), "网络未连接", Toast.LENGTH_SHORT).show();
			}
		}
		return v;
	}

	@Override
	public void onDestroy() {
		// TODO 自动生成的方法存根
		super.onDestroy();
		mProgressBar.setVisibility(View.GONE);
		if (imageView != null) {
			imageView.setImageDrawable(null);
		}
	}

//	class DownLoadAsyncTask extends AsyncTask<Void, Void, Void> {
//		@Override
//		protected Void doInBackground(Void... params) {
//			// TODO 自动生成的方法存根
//			String Base64String = WebserviceImport.GetImage(imagePath,
//					MyApplication.getInstance().getSharedPreferences());
//			byte[] bitmapArray = Base64.decode(Base64String, Base64.DEFAULT);
//			InputStream isBm = null;
//			FileOutputStream osBm = null;
//
//			try {
//				isBm = new ByteArrayInputStream(bitmapArray);
//				osBm = new FileOutputStream(new File(PathConstant.PATH_cachephoto+imagePath));
//				
//				byte bt[] = new byte[1024];
//				int c;
//				while ((c = isBm.read(bt)) != -1) {
//					osBm.write(bt, 0, c);
//				}
//			} catch (FileNotFoundException e) {
//				// TODO 自动生成的 catch 块
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO 自动生成的 catch 块
//				e.printStackTrace();
//			} finally {
//				try {
//					isBm.close();
//				} catch (IOException e) {
//					// TODO 自动生成的 catch 块
//					e.printStackTrace();
//				}
//				try {
//					osBm.close();
//				} catch (IOException e) {
//					// TODO 自动生成的 catch 块
//					e.printStackTrace();
//				}
//			}
//
//			return null;
//		}

//		@Override
//		protected void onPostExecute(Void result) {
//			// TODO 自动生成的方法存根
//			super.onPostExecute(result);
//			File file = new File(PathConstant.PATH_cachephoto + imagePath);
//			if (file.exists()) {
//				new GetImageAsyncTask().execute(PathConstant.PATH_cachephoto + imagePath);
//			}
//		}
//	};

	class GetImageAsyncTask extends AsyncTask<String, Void, Bitmap> {
		@Override
		protected Bitmap doInBackground(String... params) {
			// TODO 自动生成的方法存根
			return getImage(params[0]);
		}
		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO 自动生成的方法存根
			super.onPostExecute(result);
			mProgressBar.setVisibility(View.GONE);
			if(result!=null){
				imageView.setImageBitmap(result);
			}
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
					int length = jsonArray.length();
					for(int i=0;i<length;i++){
						JSONObject dataObject=jsonArray.getJSONObject(i);
						if(dataObject.getString("ImageURL").contains(imagePath)){
							String urlString  = MyApplication.getInstance().getSharedPreferences().getString(ShareprefenceBean.NET_URL, "")+dataObject.getString("ImageURL");
							new DownLoadBitmapAsyncTask().execute(urlString);
							break;
						}
					}
					break;
					default:
						Toast.makeText(getActivity(), jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
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
			URL url = null;
			HttpURLConnection conn = null;
			InputStream is = null;
			FileOutputStream fops = null;
			try {
				url = new URL(params[0]);
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoInput(true);
				conn.connect();
				is = conn.getInputStream();
				if(conn.getResponseCode()==200){
					fops = new FileOutputStream(new File(PathConstant.PATH_cachephoto+imagePath));
					byte bt[] = new byte[1024];
					int c;
					while ((c = is.read(bt)) != -1) {
						fops.write(bt, 0, c);
					}
					fops.flush();
				}else{
					Toast.makeText(getActivity(), "请求图片出错", Toast.LENGTH_SHORT).show();
				}

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
					if(fops != null){
						fops.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO 自动生成的方法存根
			super.onPostExecute(result);
			File file = new File(PathConstant.PATH_cachephoto + imagePath);
			if (file.exists()) {
				new GetImageAsyncTask().execute(PathConstant.PATH_cachephoto + imagePath);
			}
		}
	}
	
	
	public Bitmap getImage(String path) {
		Bitmap bitmap = null;
		try {
			BitmapFactory.Options option = new BitmapFactory.Options();
			option.inJustDecodeBounds = true;
			option.inPreferredConfig = Bitmap.Config.RGB_565;
			option.inSampleSize = 1;
			BitmapFactory.decodeFile(path, option);
			while ((option.outHeight / option.inSampleSize) > height
					&& (option.outWidth / option.inSampleSize) > width) {
				option.inSampleSize *= 2;
			}
			option.inPurgeable = true;
			option.inInputShareable = true;
			option.inJustDecodeBounds = false;
			bitmap = BitmapFactory.decodeFile(path, option);
			if (bitmap != null) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
				int options = 100;
				while (baos.toByteArray().length / 1024 > 512) { // 循环判断如果压缩后图片是否大于512kb,大于继续压缩
					baos.reset();// 重置baos即清空baos
					bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
					options -= 20;// 每次都减少20%
				}
				ByteArrayInputStream isBm = new ByteArrayInputStream(
						baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
				bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}
}
