package com.guantang.cangkuonline.JellyViewPager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Serializable;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.static_constant.PathConstant;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class PictureFragment extends Fragment {
	private int width,height;
	private String imagePath="";
	private ZoomImageView imageView;
	private ProgressBar mProgressBar;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		View v = inflater.inflate(R.layout.picture_layout, null);
		imageView = (ZoomImageView) v.findViewById(R.id.imageView1);
		mProgressBar = (ProgressBar) v.findViewById(R.id.mybar);
		Bundle bundle = getArguments();
		width = bundle.getInt("width", 0);
		height = bundle.getInt("height", 0);
		imagePath = bundle.getString("ImageName");
		File file = new File(PathConstant.PATH_photo + imagePath);
		if (file.exists()) {
			mProgressBar.setVisibility(View.VISIBLE);
			new GetImageAsyncTask().execute(PathConstant.PATH_photo + imagePath);
		}
		
		return v;
	}
	
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
