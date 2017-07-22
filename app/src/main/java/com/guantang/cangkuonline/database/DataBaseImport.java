package com.guantang.cangkuonline.database;

import com.guantang.cangkuonline.helper.PwdHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseImport extends SQLiteOpenHelper {

//	private static DataBaseImport DBHELPER = null;
//
	public static DataBaseImport getInstance(Context context) {
//		if (DBHELPER == null) {
//			synchronized (DataBaseImport.class) {
//				if (DBHELPER == null) {
		DataBaseImport	DBHELPER = new DataBaseImport(context, DataBaseHelper.DB,
							null, DataBaseHelper.DB_VERSION);
//				}
//			}
//		}
		return DBHELPER;
	}

	private DataBaseImport(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE IF NOT EXISTS " + DataBaseHelper.TB_HP + " ("
				+ "id INTEGER   PRIMARY KEY   not null,"
				+ "hpmc NVARCHAR(255) null," + "hpbm NVARCHAR(255) null,"
				+ "hptm NVARCHAR(255) null," + "ggxh NVARCHAR(255) null,"
				+ "jldw NVARCHAR(50) null," + "sccs NVARCHAR(255) null,"
				+ "jldw2 NVARCHAR(50) null," + "lbs NVARCHAR(255) null,"
				+ "lbid INTEGER(4) null," + "rkckj float(8) null,"
				+ "ckckj float(8) null," + "hpsx float(8) null,"
				+ "hpxx float(8) null," + "bz ntext(16) null,"
				+ "CurrKC decimal(18, 8) DEFAULT 0,"
				+ "LBindex VARCHAR(255) NULL," + "ckckj2 float(8) null,"
				+ "bignum decimal(18, 8) null," + "kcje decimal(18, 8) null,"
				+ "type INTEGER(2) DEFAULT 0," + "imgpath VARCHAR(255) NULL,"
				+ "gxdate datetime null," + "res1 VARCHAR(255) NULL,"
				+ "res2 VARCHAR(255) NULL," + "res3 VARCHAR(255) NULL,"
				+ "res4 VARCHAR(255) NULL," + "res5 VARCHAR(255) NULL,"
				+ "res6 VARCHAR(255) NULL," + "resd1 datetime NULL,"
				+ "resd2 datetime NULL," + "StopTag bit(1) NULL,"
				+ "hppy NVARCHAR(50) null)");
		db.execSQL("CREATE TABLE IF NOT EXISTS " + DataBaseHelper.TB_CKKC
				+ " (" + "id INTEGER   PRIMARY KEY   AUTOINCREMENT,"
				+ "hpid INTEGER(4) null," + "ckid INTEGER(4) null,"
				+ "kcsl decimal(18, 8) null)");
		db.execSQL("CREATE TABLE IF NOT EXISTS " + DataBaseHelper.TB_CK + " ("
				+ "id INTEGER(4)   PRIMARY KEY   not null,"
				+ "ckmc NVARCHAR(50) null," + "fzr NVARCHAR(50) null,"
				+ "tel NVARCHAR(50) null," + "addr NVARCHAR(255) null,"
				+ "inact NVARCHAR(50) null," + "outact NVARCHAR(50) null,"
				+ "bz NVARCHAR(255) null)");
		db.execSQL("CREATE TABLE IF NOT EXISTS " + DataBaseHelper.TB_MoveD
				+ " (" + "hpd_id INTEGER   PRIMARY KEY   AUTOINCREMENT,"
				+ "hpid INTEGER(4) null," + "ckid INTEGER(4) null,"
				+ "bzkc decimal(18, 8) null," + "azkc decimal(18, 8) null,"
				+ "btkc decimal(18, 8) null," + "atkc decimal(18, 8) null,"
				+ "mid INTEGER(4) null," + "mvddt smalldatetime(4) null,"
				+ "dactType NVARCHAR(255) null," + "mvddirect INTEGER(4) null,"
				+ "msl decimal(18, 8) null," + "dj float(8) null,"
				+ "zj float(8) null," + "mvdType INTEGER(4) null,"
				+ "mvddh varchar(50) null," + "bkcje float(8) null,"
				+ "ordIndex varchar(50) null," + "tax float(8) null,"
				+ "sqdj float(8) null," + "sqzj float(8) null,"
				+ "msl2 float(8) null," + "jldw2 varchar(50) null,"
				+ "dwxs float(8) null," + "res1 varchar(255) null,"
				+ "res2 varchar(255) null," + "resf1 float(8) null,"
				+ "resf2 float(8) null)");
		db.execSQL("CREATE TABLE IF NOT EXISTS " + DataBaseHelper.TB_MoveM
				+ " (" + "hpm_id INTEGER   PRIMARY KEY   AUTOINCREMENT,"
				+ "mvdh NVARCHAR(50) NULL," + "mvdt smalldatetime(4) null,"
				+ "ckmc NVARCHAR(50) null," + "ckid INTEGER(4) null,"
				+ "hpzj decimal(18, 8) null," + "depName NVARCHAR(255) null,"
				+ "depId INTEGER(4) null," + "dwName NVARCHAR(255) null,"
				+ "jbr NVARCHAR(50) null," + "shr NVARCHAR(50) null,"
				+ "lyr NVARCHAR(50) null," + "hpDetails NVARCHAR(255) null,"
				+ "actType NVARCHAR(255) null," + "Details NVARCHAR(255) null,"
				+ "mType INTEGER(4) null," + "dwid INTEGER(4) null,"
				+ "orderIndex varchar(255) null," + "bigJLDW bit(1) null,"
				+ "bz mtext(16) null," + "DJType INTEGER(4) null,"
				+ "res1 varchar(255) null," + "res2 varchar(255) null,"
				+ "res3 varchar(255) null," + "lrr NVARCHAR(50) null,"
				+ "lrsj datetime null," + "tjsj datetime null)");
		db.execSQL("CREATE TABLE IF NOT EXISTS " + DataBaseHelper.TB_hplbTree
				+ " (" + "id INTEGER   PRIMARY KEY   AUTOINCREMENT,"
				+ "name nvarchar(50) null," + "lev INTEGER(4) null,"
				+ "PID INTEGER(4) null," + "ord INTEGER(4) null,"
				+ "sindex nvarchar(50) null," + "lbs NVARCHAR(255) null)");
		db.execSQL("CREATE TABLE IF NOT EXISTS " + DataBaseHelper.TB_Company
				+ " (" + "id INTEGER   PRIMARY KEY   not null,"
				+ "dwName varchar(255) not null," + "py  NVARCHAR(50) null,"
				+ "tel NVARCHAR(50) null," + "addr NVARCHAR(255) null,"
				+ "fax NVARCHAR(50) null," + "lxr NVARCHAR(50) null,"
				+ "email NVARCHAR(50) null," + "qq NVARCHAR(50) null,"
				+ "net NVARCHAR(255) null," + "dwtype INTEGER(4) null,"
				+ "phone NVARCHAR(50) null," + "ord INTEGER(4) null,"
				+ "yb NVARCHAR(50) null," + "bz ntext(16) null)");
		db.execSQL("CREATE TABLE IF NOT EXISTS " + DataBaseHelper.TB_Conf
				+ " (" + "id INTEGER   PRIMARY KEY   AUTOINCREMENT,"
				+ "GID NVARCHAR(50) null," + "ItemID NVARCHAR(50) null,"
				+ "ItemV NVARCHAR(255) null," + "ord INTEGER null)");
		db.execSQL("CREATE TABLE IF NOT EXISTS " + DataBaseHelper.TB_depTree
				+ " (" + "id INTEGER   PRIMARY KEY   not null,"
				+ "name nvarchar(50) null," + "deplevel INTEGER(4) null,"
				+ "PID INTEGER(4) null," + "depOrder INTEGER(4) null,"
				+ "depindex nvarchar(50) null)");
		db.execSQL("CREATE TABLE IF NOT EXISTS " + DataBaseHelper.TB_Order
				+ " (" + "id INTEGER   PRIMARY KEY   AUTOINCREMENT,"
				+ "status INTEGER null," + "contry nvarchar(50) null,"
				+ "state varchar(50) null," + "ContryCode nvarchar(50) null,"
				+ "city nvarchar(50) null," + "address nvarchar(1024) null,"
				+ "dwName nvarchar(255) null," + "lxr nvarchar(50) null,"
				+ "orderindex nvarchar(255) null,"
				+ "trades nvarchar(1024) null," + "sqdt datetime null,"
				+ "zipcode nvarchar(50) null," + "cw nvarchar(50) null,"
				+ "addr2 nvarchar(50) null," + "tel nvarchar(50) null,"
				+ "PRI INTEGER null," + "zj decimal(18, 8) null,"
				+ "zje decimal(18, 8) null," + "yfje decimal(18, 8) null,"
				+ "syje decimal(18, 8) null," + "bz nvarchar(1024) null,"
				+ "dirc INTEGER null," + "shdt datetime null,"
				+ "shr varchar(50) null," + "shyj varchar(1024) null,"
				+ "shrID INTEGER null," + "sqrID INTEGER null,"
				+ "sqr varchar(50) null," + "ReqDate datetime null,"
				+ "depName varchar(255) null," + "sjzje decimal(18, 8) null,"
				+ "dwid INTEGER null," + "jsfs varchar(50) null,"
				+ "res1 VARCHAR(255) NULL," + "res2 VARCHAR(255) NULL,"
				+ "res3 VARCHAR(255) NULL," + "res4 VARCHAR(255) NULL,"
				+ "resf1 float(8) null," + "resf2 float(8) null,"
				+ "resd1 datetime NULL," + "resd2 datetime NULL)");
		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+ DataBaseHelper.TB_OrderDetail + " ("
				+ "id INTEGER   PRIMARY KEY   AUTOINCREMENT,"
				+ "hpid INTEGER   null," + "orderId INTEGER   null,"
				+ "hpmc NVARCHAR(255) null," + "hpbm NVARCHAR(255) null,"
				+ "ggxh NVARCHAR(255) null," + "sl decimal(18, 8) null,"
				+ "dj decimal(18, 8) null," + "zj decimal(18, 8) null,"
				+ "ord INTEGER   null," + "zxsl decimal(18, 8) null,"
				+ "notes VARCHAR(255) NULL," + "ddirc INTEGER   null,"
				+ "yjCbdj decimal(18, 8) null," + "yjCbzj decimal(18, 8) null,"
				+ "sjCbdj decimal(18, 8) null," + "lrje decimal(18, 8) null)");
		db.execSQL("insert into " + DataBaseHelper.TB_Conf + " ( "
				+ DataBaseHelper.GID + "," + DataBaseHelper.ItemID + ","
				+ DataBaseHelper.ItemV + " ) values ( '入库类型','null','采购入库')");
		db.execSQL("insert into " + DataBaseHelper.TB_Conf + " ( "
				+ DataBaseHelper.GID + "," + DataBaseHelper.ItemID + ","
				+ DataBaseHelper.ItemV + " ) values ( '入库类型','null','生产入库')");
		db.execSQL("insert into " + DataBaseHelper.TB_Conf + " ( "
				+ DataBaseHelper.GID + "," + DataBaseHelper.ItemID + ","
				+ DataBaseHelper.ItemV + " ) values ( '出库类型','null','销售出库')");
		db.execSQL("insert into " + DataBaseHelper.TB_Conf + " ( "
				+ DataBaseHelper.GID + "," + DataBaseHelper.ItemID + ","
				+ DataBaseHelper.ItemV + " ) values ( '出库类型','null','领用出库')");
		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+ DataBaseHelper.TB_PIC
				+ " ( id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "hpid INTEGER null , ImageURL NVARCHAR(255) null, CompressImageURL NVARCHAR(255) null, UpLoadStatus INTEGER(4) null)");
		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+ DataBaseHelper.TB_LOGIN
				+ " ( id INTEGER PRIMARY KEY AUTOINCREMENT, company NVARCHAR(255) null,username NVARCHAR(255) null,password NVARCHAR(255) null,miwenpassword NVARCHAR(255) null)");
		db.execSQL("create table if not exists "+DataBaseHelper.TB_TRANSD+" ( id integer primary key autoincrement, hpid integer null,sl decimal(18,8) null,dj decimal(18,8) null, zj decimal(18,8) null,mid integer null, note ntext null)");
		db.execSQL("create table if not exists "+DataBaseHelper.TB_TRANSM+" ( id integer primary key autoincrement, sckName nvarchar(50) null,sckid integer null, dckName nvarchar(50) null, dckid integer null, mvdh nvarchar(50) null, mvdt smalldatetime(4) null,"
				+ "jbr nvarchar(50) null, hpnames nvarchar(255) null, bz nvarchar(255) null, hpzjz decimal(255) null, ActStatus integer null ,shyj nvarchar(255) null)");
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		if(oldVersion<=newVersion){
			db.beginTransaction();
//			if (oldVersion < 2) {
//				onCreate(db);
//			}
			if (oldVersion < 5) {
				onCreate(db);
			}
			db.setTransactionSuccessful();
			db.endTransaction();
		}
	}

}
