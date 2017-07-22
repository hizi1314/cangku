package com.guantang.cangkuonline.static_constant;

import android.os.Environment;


public class PathConstant {
	public static String PATH = android.os.Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/guantang/";
//	public static String PATH_export = PATH + "导出/";
//	public static String PATH_import = PATH + "导入/";
	
	public static String PATH_DATABASE = "data/data/com.guantang.cangkuonline/databases/strorage_moblie_ol.db";//数据库的路径
	public static String PATH_photo = PATH + "img_ol/";
	public static String PATH_backup = PATH + "备份/";
	public static String PATH_system = PATH + "appsystem/";
	public static String PATH_cachephoto = PATH + "photoCache/";
	public static String PATH_APKPACKAGE = PATH + "downapk/";
}
