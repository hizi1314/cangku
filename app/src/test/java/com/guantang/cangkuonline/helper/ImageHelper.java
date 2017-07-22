package com.guantang.cangkuonline.helper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import android.graphics.Bitmap;

public class ImageHelper {
	public static int Img(String str){
		if(str==null||str.equals("")){
			return 0;
		}else{
			String[] ss=str.split("\t");
			return ss.length;
		}
	}
	public static Bitmap GetBm(String path){
		Bitmap bm = null;
		return bm;
	}
	/*
	 * 临时图片名称命名规则： yyyy-MM-dd_HH-mm-ss_5位随机数,没有图片后缀名
	 * */
	public static String TemporarilyImageName(){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String timeString=formatter.format(new Date(System.currentTimeMillis()));
		Random random = new Random();
		return timeString+"_"+String.valueOf(random.nextInt(99999));
	}
}
