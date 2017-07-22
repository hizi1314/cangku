package com.guantang.cangkuonline.database;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.guantang.cangkuonline.application.MyApplication;
import com.umeng.analytics.c;

import android.R.integer;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataBaseMethod {
	Context context;

	public DataBaseMethod(Context mcontext) {
		this.context = mcontext;
	}

	public List<Map<String, Object>> Gethp(String[] s) {
		String str = "";
		for (int i = 0; i < s.length; i++) {
			if (i != s.length - 1) {
				str = str + s[i] + ",";
			} else {
				str = str + s[i];
			}
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db
				.rawQuery("select " + str + " from " + DataBaseHelper.TB_HP
						+ " order by " + DataBaseHelper.ID, null);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			for (int j = 0; j < s.length; j++) {
				if(s[j].equals(DataBaseHelper.CurrKC)){
					map.put(s[j], String.valueOf(c.getDouble(j)));
				}else{
					map.put(s[j], c.getString(j));
				}
			}
			list.add(map);
			c.moveToNext();
		}
		c.close();
		db.close();
		return list;
	}
	
	public List<Map<String, Object>> GethpByckid(int ckid) {

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db
				.rawQuery(
						"select a.id,hpmc,hpbm,hptm,ggxh,CurrKC,kcsl from tb_hp as a left join tb_ckkc as b on a.id = b.hpid where "
								+ DataBaseHelper.CKID
								+ "='"
								+ ckid
								+ "' order by a.id", null);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			for (int j = 0; j < 6; j++) {
				map.put(c.getColumnName(j), c.getString(j));
			}
			list.add(map);
			c.moveToNext();
		}
		c.close();
		db.close();
		return list;
	}

	public List<Map<String, Object>> Gethp_xx(String[] s) {
		String str = "";
		for (int i = 0; i < s.length; i++) {
			if (i != s.length - 1) {
				str = str + s[i] + ",";
			} else {
				str = str + s[i];
			}
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery("select " + str + " from tb_hp where "
				+ DataBaseHelper.CurrKC + " !='' and " + DataBaseHelper.HPXX
				+ " !='' and " + DataBaseHelper.CurrKC + " < "
				+ DataBaseHelper.HPXX + " order by " + DataBaseHelper.ID, null);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			for (int j = 0; j < s.length; j++) {
				map.put(c.getColumnName(j), c.getString(j));
			}
			list.add(map);
			c.moveToNext();
		}
		c.close();
		db.close();
		return list;
	}

	public List<Map<String, Object>> Gethp_xxByckid(int ckid) {

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db
				.rawQuery(
						"select a.id,hpmc,hpbm,hptm,ggxh,CurrKC,kcsl from tb_hp as a left join tb_ckkc as b on a.id = b.hpid where "
								+ DataBaseHelper.CurrKC
								+ " !='' and "
								+ DataBaseHelper.HPXX
								+ " !='' and "
								+ DataBaseHelper.CurrKC
								+ " < "
								+ DataBaseHelper.HPXX
								+ "  and "
								+ DataBaseHelper.CKID
								+ "='"
								+ ckid
								+ "' order by a.id", null);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			for (int j = 0; j < 6; j++) {
				map.put(c.getColumnName(j), c.getString(j));
			}
			list.add(map);
			c.moveToNext();
		}
		c.close();
		db.close();
		return list;
	}

	public List<Map<String, Object>> Gethp_sx(String[] s) {
		String str = "";
		for (int i = 0; i < s.length; i++) {
			if (i != s.length - 1) {
				str = str + s[i] + ",";
			} else {
				str = str + s[i];
			}
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery("select " + str + " from tb_hp where "
				+ DataBaseHelper.CurrKC + " !='' and " + DataBaseHelper.HPSX
				+ " !='' and " + DataBaseHelper.CurrKC + " > "
				+ DataBaseHelper.HPSX + " order by " + DataBaseHelper.ID, null);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			for (int j = 0; j < s.length; j++) {
				map.put(c.getColumnName(j), c.getString(j));
			}
			list.add(map);
			c.moveToNext();
		}
		c.close();
		db.close();
		return list;
	}

	public List<Map<String, Object>> Gethp_sxByckid(int ckid) {

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db
				.rawQuery(
						"select  a.id,hpmc,hpbm,hptm,ggxh,CurrKC,kcsl from tb_hp as a left join tb_ckkc as b on a.id = b.hpid where "
								+ DataBaseHelper.CurrKC
								+ " !='' and "
								+ DataBaseHelper.HPSX
								+ " !='' and "
								+ DataBaseHelper.CurrKC
								+ " > "
								+ DataBaseHelper.HPSX
								+ " and "
								+ DataBaseHelper.CKID
								+ "='"
								+ ckid
								+ "' order by a.id", null);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			for (int j = 0; j < 6; j++) {
				map.put(c.getColumnName(j), c.getString(j));
			}
			list.add(map);
			c.moveToNext();
		}
		c.close();
		db.close();
		return list;
	}

	public List<Map<String, Object>> Gethp_todaychange(String date) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery("select DISTINCT id,hpmc,hpbm,hptm,ggxh,CurrKC  from tb_hp as a,tb_moved as b where a.id=b.hpid and "
				+ DataBaseHelper.MVDDATE + ">='" + date + " 00:00:00' and "
				+ DataBaseHelper.MVDDATE + "<'" + date
				+ " 23:59:59' order by a.id ", null);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			for (int j = 0; j < 6; j++) {
				map.put(c.getColumnName(j), c.getString(j));
			}
			list.add(map);
			c.moveToNext();
		}
		c.close();
		db.close();
		return list;
	}

	public List<Map<String, Object>> Gethp_todaychangeByckid(int ckid,
			String date) {

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db
				.rawQuery(
						"select DISTINCT a.id,hpmc,hpbm,hptm,ggxh,CurrKC,kcsl from tb_hp as a,tb_moved as b where a.id=b.hpid and "
								+ DataBaseHelper.MVDDATE
								+ ">='"
								+ date
								+ " 00:00:00' and "
								+ DataBaseHelper.MVDDATE
								+ "<'"
								+ date
								+ " 23:59:59' and "
								+ DataBaseHelper.CKID
								+ "='"
								+ ckid
								+ "' order by a.id ", null);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			for (int j = 0; j < 6; j++) {
				map.put(c.getColumnName(j), c.getString(j));
			}
			list.add(map);
			c.moveToNext();
		}
		c.close();
		db.close();
		return list;
	}

	public List<Map<String, Object>> GetHp_complex(String sql, int ckid,int hasckkc) {

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();

		Cursor c = null;
		if (ckid == -1) {
			if(hasckkc==1){ //是否有库存 有库存1，没有库存-1，0有无库存都显示
				c = db.rawQuery("select id,HPMC,HPBM,hptm,GGXH,CurrKc"
						+ " from tb_hp where 1=1 and CurrKc !=0 " + sql, null);
			}else if(hasckkc==-1){
				c = db.rawQuery("select id,HPMC,HPBM,hptm,GGXH,CurrKc"
						+ " from tb_hp where 1=1 and CurrKc =0 " + sql, null);
			}else if(hasckkc==0){
				c = db.rawQuery("select id,HPMC,HPBM,hptm,GGXH,CurrKc"
						+ " from tb_hp where 1=1 " + sql, null);
			}
		} else {
			if(hasckkc==1){ //是否有库存 有库存1，没有库存-1，0有无库存都显示
				c = db.rawQuery(
						"select a.id,HPMC,HPBM,hptm,GGXH,CurrKc"
								+ " from tb_hp as a left join tb_ckkc as b on a.id = b.hpid where b.kcsl !=0 and "
								+ DataBaseHelper.CKID + " = '" + ckid + "' " + sql,null);
			}else if(hasckkc==-1){
				c = db.rawQuery(
						"select a.id,HPMC,HPBM,hptm,GGXH,CurrKc"
								+ " from tb_hp as a left join tb_ckkc as b on a.id = b.hpid where b.kcsl =0 and "
								+ DataBaseHelper.CKID + " = '" + ckid + "' " + sql,null);
			}else if(hasckkc==0){
				c = db.rawQuery(
						"select a.id,HPMC,HPBM,hptm,GGXH,CurrKc"
								+ " from tb_hp as a left join tb_ckkc as b on a.id = b.hpid where "
								+ DataBaseHelper.CKID + " = '" + ckid + "' " + sql,null);
			}
		}
		c.moveToFirst();
		while (!c.isAfterLast()) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			for (int j = 0; j < 6; j++) {
				map.put(c.getColumnName(j), c.getString(j));
			}
			list.add(map);
			c.moveToNext();
		}
		c.close();
		db.close();
		return list;
	}

	public List<Map<String, Object>> Gethp_KCSL(int ckid, String[] s) {
		String str = "";
		for (int i = 0; i < s.length; i++) {
			if (i != s.length - 1) {
				str = str + s[i] + ",";
			} else {
				str = str + s[i];
			}
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db
				.rawQuery(
						"select "
								+ str
								+ " from tb_hp as a left join (select hpid,kcsl from tb_ckkc where ckid='"
								+ ckid + "') as b on a.id=b.hpid", null);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			for (int j = 0; j < s.length; j++) {
				map.put(c.getColumnName(j), c.getString(j));
			}
			list.add(map);
			c.moveToNext();
		}
		c.close();
		db.close();
		return list;
	}

	public List<Map<String, Object>> Gethp_ByCKID(int ckid, String[] s) {
		String str = "";
		for (int i = 0; i < s.length; i++) {
			if (i != s.length - 1) {
				str = str + s[i] + ",";
			} else {
				str = str + s[i];
			}
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery("select " + str + " from "
				+ DataBaseHelper.TB_HP + "  as a," + DataBaseHelper.TB_CKKC
				+ "  as b where a.id=b.hpid and " + DataBaseHelper.CKID + "='"
				+ ckid + "'", null);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			for (int j = 0; j < s.length; j++) {
				map.put(c.getColumnName(j), c.getString(j));
			}
			list.add(map);
			c.moveToNext();
		}
		c.close();
		db.close();
		return list;
	}

	public List<Map<String, Object>> MyGethp_ByCKID(int ckid) {

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery("select a.id,hpmc,hpbm,hptm,ggxh,CurrKC from "
				+ DataBaseHelper.TB_HP + "  as a," + DataBaseHelper.TB_CKKC
				+ "  as b where a.id=b.hpid and " + DataBaseHelper.CKID + "='"
				+ ckid + "'", null);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			for (int j = 0; j < 6; j++) {
				map.put(c.getColumnName(j), c.getString(j));
			}
			list.add(map);
			c.moveToNext();
		}
		c.close();
		db.close();
		return list;
	}

	public List<Map<String, Object>> Gethp_time(String[] s, String time) {
		String str = "";
		for (int i = 0; i < s.length; i++) {
			if (i != s.length - 1) {
				str = str + s[i] + ",";
			} else {
				str = str + s[i];
			}
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery("select " + str + " from "
				+ DataBaseHelper.TB_HP + " where " + DataBaseHelper.GXDate
				+ "='" + time + "'", null);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			for (int j = 0; j < s.length; j++) {
				map.put(s[j], c.getString(j));
			}
			list.add(map);
			c.moveToNext();
		}
		c.close();
		db.close();
		return list;
	}

	public List<Map<String, Object>> Gethp(String[] s, int type) {
		String str = "";
		for (int i = 0; i < s.length; i++) {
			if (i != s.length - 1) {
				str = str + s[i] + ",";
			} else {
				str = str + s[i];
			}
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery("select " + str + " from "
				+ DataBaseHelper.TB_HP + " where " + DataBaseHelper.Type + "='"
				+ type + "'", null);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			for (int j = 0; j < s.length; j++) {
				map.put(s[j], c.getString(j));
			}
			list.add(map);
			c.moveToNext();
		}
		c.close();
		db.close();
		return list;
	}

	public boolean isAdded(String bm) {
		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery("select * from " + DataBaseHelper.TB_HP
				+ " where " + DataBaseHelper.HPBM + "='" + bm + "'", null);
		if (c.moveToFirst()) {
			c.close();
			db.close();
			return false;
		} else {
			c.close();
			db.close();
			return true;
		}
	}

	public List<Map<String, Object>> Gethp(String[] s, String id) {
		String str = "";
		for (int i = 0; i < s.length; i++) {
			if (i != s.length - 1) {
				str = str + s[i] + ",";
			} else {
				str = str + s[i];
			}
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery("select " + str + " from "
				+ DataBaseHelper.TB_HP + " where " + DataBaseHelper.ID + "='"
				+ id + "' order by " + DataBaseHelper.HPBM, null);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			for (int j = 0; j < s.length; j++) {
				map.put(s[j], c.getString(j));
			}
			list.add(map);
			c.moveToNext();
		}
		c.close();
		db.close();
		return list;
	}

	public List<Map<String, Object>> GethpAndKCSL(String id, int ckid,boolean searchAllHP) {

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		
		if (searchAllHP) {
			Cursor c = db.rawQuery(
					"select tb_hp.id,hpmc,hpbm,hptm,ggxh,CurrKC,jldw,hpsx,hpxx,sccs,bz,rkckj,ckckj,ckckj2"
							+ ",jldw2,bignum,res1,res2,res3,res4,res5,res6,lbs,kcje,imgpath from "
							+ DataBaseHelper.TB_HP
							+ " where  tb_hp.id='"
							+ id + "'", null);
			Cursor c2 = db.rawQuery("select kcsl from "+DataBaseHelper.TB_CKKC+" where hpid = '"+id+"' and ckid = '"+ckid+"'", null);
			
			HashMap<String, Object> map = new HashMap<String, Object>();
			
			if(c.moveToFirst()){
				for (int j = 0; j < 25; j++) {
					String ColumnName = c.getColumnName(j);
					if(ColumnName.equals(DataBaseHelper.CurrKC)){
						map.put(ColumnName, String.valueOf(c.getDouble(j)));
					}else{
						map.put(ColumnName, c.getString(j));
					}
				}
			}
			
			if(c2.moveToFirst()){
				map.put("kcsl", String.valueOf(c2.getDouble(0)));
			}else{
				map.put("kcsl", "0");
			}

			list.add(map);
			c.close();
			
		} else {
			Cursor c = db.rawQuery(
					"select tb_hp.id,hpmc,hpbm,hptm,ggxh,CurrKC,jldw,hpsx,hpxx,sccs,bz,rkckj,ckckj,ckckj2"
							+ ",jldw2,bignum,res1,res2,res3,res4,res5,res6,lbs,kcje,imgpath,kcsl from "
							+ DataBaseHelper.TB_HP
							+ "  left join "
							+ DataBaseHelper.TB_CKKC
							+ "  on tb_hp.id=tb_ckkc.hpid where  tb_hp.id='"
							+ id
							+ "' and "
							+ DataBaseHelper.CKID
							+ " = '"
							+ ckid + "' ", null);
			
			c.moveToFirst();
			while (!c.isAfterLast()) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				for (int j = 0; j < 26; j++) {
					String ColumnName = c.getColumnName(j);
					if(ColumnName.equals(DataBaseHelper.CurrKC)||ColumnName.equals(DataBaseHelper.KCSL)){
						map.put(ColumnName, String.valueOf(c.getDouble(j)));
					}else{
						map.put(ColumnName, c.getString(j));
					}
				}
				list.add(map);
				c.moveToNext();
			}
			c.close();
		}
		db.close();
		return list;
	}

	public List<Map<String, Object>> Gethp_export(String[] s, String id,
			SQLiteDatabase db) {
		String str = "";
		for (int i = 0; i < s.length; i++) {
			if (i != s.length - 1) {
				str = str + s[i] + ",";
			} else {
				str = str + s[i];
			}
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Cursor c = db.rawQuery("select " + str + " from "
				+ DataBaseHelper.TB_HP + " where " + DataBaseHelper.ID + "='"
				+ id + "'", null);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			for (int j = 0; j < s.length; j++) {
				map.put(s[j], c.getString(j));
			}
			list.add(map);
			c.moveToNext();
		}
		c.close();
		return list;
	}

	public void Update_HPtype(String id, SQLiteDatabase db) {
		db.execSQL("update " + DataBaseHelper.TB_HP + " set "
				+ DataBaseHelper.Type + "=0" + " where " + DataBaseHelper.ID
				+ "=" + id);
	}

	public void Update_HPtype_back(List<Map<String, Object>> list) {
		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		for (int i = 0; i < list.size(); i++) {
			db.execSQL("update " + DataBaseHelper.TB_HP + " set "
					+ DataBaseHelper.Type + "=1" + " where "
					+ DataBaseHelper.ID + "="
					+ (String) list.get(i).get(DataBaseHelper.ID));
		}
		db.close();
	}

	public List<Map<String, Object>> Gethp_tm(String[] s, String tm) {
		String str = "";
		for (int i = 0; i < s.length; i++) {
			if (i != s.length - 1) {
				str = str + s[i] + ",";
			} else {
				str = str + s[i];
			}
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery("select " + str + " from "
				+ DataBaseHelper.TB_HP + " where " + DataBaseHelper.HPTM + "='"
				+ tm + "'", null);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			for (int j = 0; j < s.length; j++) {
				map.put(s[j], c.getString(j));
			}
			list.add(map);
			c.moveToNext();
		}
		c.close();
		db.close();
		return list;
	}
	
	public List<Map<String, Object>> Gethp_tmByCkid(String[] s, String tm,int ckid) {
		String str = "";
		for (int i = 0; i < s.length; i++) {
			if (i != s.length - 1) {
				str = str + s[i] + ",";
			} else {
				str = str + s[i];
			}
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();		
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = null;
		if(ckid == -1){
			c = db.rawQuery("select " + str + " from from  tb_hp as a left join tb_ckkc as b on a.id = b.hpid where " + DataBaseHelper.HPTM + "='"
					+ tm + "'", null);
		}else{
			c = db.rawQuery("select " + str + " from from  tb_hp as a left join tb_ckkc as b on a.id = b.hpid where " + DataBaseHelper.HPTM + "='"
					+ tm + "' and b.ckid = "+ckid, null);
		}
		c.moveToFirst();
		while (!c.isAfterLast()) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			for (int j = 0; j < s.length; j++) {
				map.put(s[j], c.getString(j));
			}
			list.add(map);
			c.moveToNext();
		}
		c.close();
		db.close();
		return list;
	}

	public List<Map<String, Object>> GetHP_byTM_CKID(String tm, int ckid) {

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery(
				"select a.id,hpmc,hpbm,hptm,ggxh, CurrKc,b.kcsl from "
						+ DataBaseHelper.TB_HP + " as a left join "
						+ DataBaseHelper.TB_CKKC
						+ " as b on a.id=b.hpid where  a."
						+ DataBaseHelper.HPTM + "='" + tm + "' and b."
						+ DataBaseHelper.CKID + " = " + ckid, null);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			for (int j = 0; j < 7; j++) {
				map.put(c.getColumnName(j), c.getString(j));
			}
			list.add(map);
			c.moveToNext();
		}
		c.close();
		db.close();
		return list;
	}

	public List<Map<String, Object>> Gethp_tm_match(String[] s, String tm) {
		String str = "";
		for (int i = 0; i < s.length; i++) {
			if (i != s.length - 1) {
				str = str + s[i] + ",";
			} else {
				str = str + s[i];
			}
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery("select  * from (select   substr('" + tm
				+ "',1,length(" + DataBaseHelper.HPTM + ")) as sx," + "length("
				+ DataBaseHelper.HPTM + ") as slen," + str + " from "
				+ DataBaseHelper.TB_HP + ") where sx=" + DataBaseHelper.HPTM
				+ " and slen!=0 order by slen desc", null);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			for (int j = 0; j < s.length; j++) {
				map.put(s[j], c.getString(j + 2));
			}
			list.add(map);
			c.moveToNext();
		}
		c.close();
		db.close();
		return list;
	}

	public List<Map<String, Object>> queryList(String[] s, String text, int ckid) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = null;
		if(ckid == -1){
			c = db.rawQuery(
					"select id,hpmc,hpbm,hptm,ggxh,CurrKC from  tb_hp  WHERE " + DataBaseHelper.HPMC + " LIKE '%"
							+ text + "%' OR " + DataBaseHelper.HPTM
							+ " LIKE '%" + text + "%' OR "
							+ DataBaseHelper.GGXH + " LIKE '%" + text
							+ "%' OR " + DataBaseHelper.HPBM + " LIKE '%"
							+ text + "%' OR "+DataBaseHelper.LBS+" LIKE '%"
									+ text + "%'", null);
		}else{
			c = db.rawQuery(
					"select a.id,hpmc,hpbm,hptm,ggxh,CurrKC,kcsl from  tb_hp as a left join tb_ckkc as b on a.id = b.hpid WHERE (" + DataBaseHelper.HPMC + " LIKE '%"
							+ text + "%' OR " + DataBaseHelper.HPTM
							+ " LIKE '%" + text + "%' OR "
							+ DataBaseHelper.GGXH + " LIKE '%" + text
							+ "%' OR " + DataBaseHelper.HPBM + " LIKE '%"
							+ text + "%' OR "+DataBaseHelper.LBS+" LIKE '%"
									+ text + "%') and b.ckid = "+ ckid, null);
		}
		while (c.moveToNext()) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			for (int j = 0; j < s.length; j++) {
				map.put(s[j], c.getString(j));
			}
			list.add(map);
		}
		c.close();
		db.close();
		return list;
	}

	/**
	 * 值获取查询的部分内容
	 * */
	public List<Map<String, Object>> queryList(String[] s, String text,
			String hpid) {
		String str = "";
		for (int i = 0; i < s.length; i++) {
			if (i != s.length - 1) {
				str = str + s[i] + ",";
			} else {
				str = str + s[i];
			}
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c;
		try {

			c = db.rawQuery("select " + str + " from " + DataBaseHelper.TB_HP
					+ " WHERE (" + DataBaseHelper.HPMC + " LIKE '%" + text
					+ "%' OR " + DataBaseHelper.HPTM + " LIKE '%" + text
					+ "%' OR " + DataBaseHelper.GGXH + " LIKE '%" + text
					+ "%' OR " + DataBaseHelper.HPBM + " LIKE '%" + text
					+ "%') and " + DataBaseHelper.ID + " > '" + hpid
					+ "' ORDER BY " + DataBaseHelper.ID + " asc limit 0,10",
					null);
		} catch (Exception e) {
			c = null;
		}
		c.moveToFirst();
		while (!c.isAfterLast()) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			for (int j = 0; j < s.length; j++) {
				String ColumnName = c.getColumnName(j);
				if(ColumnName.equals(DataBaseHelper.CurrKC)||ColumnName.equals(DataBaseHelper.KCSL)){
					map.put(ColumnName, String.valueOf(c.getDouble(j)));
				}else{
					map.put(ColumnName, c.getString(j));
				}
			}
			list.add(map);
			c.moveToNext();
		}
		c.close();
		db.close();
		return list;
	}

	public List<Map<String, Object>> queryList_time(String[] s, String text,
			String time) {
		String str = "";
		for (int i = 0; i < s.length; i++) {
			if (i != s.length - 1) {
				str = str + s[i] + ",";
			} else {
				str = str + s[i];
			}
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		String string = "select " + str + " from " + DataBaseHelper.TB_HP
				+ " WHERE (" + DataBaseHelper.HPMC + " LIKE '%" + text
				+ "%' OR " + DataBaseHelper.HPTM + " LIKE '%" + text + "%' OR "
				+ DataBaseHelper.GGXH + " LIKE '%" + text + "%' OR "
				+ DataBaseHelper.HPBM + " LIKE '%" + text + "%') and "
				+ DataBaseHelper.GXDate + "='" + time + "'";
		Cursor c = db.rawQuery("select " + str + " from "
				+ DataBaseHelper.TB_HP + " WHERE (" + DataBaseHelper.HPMC
				+ " LIKE '%" + text + "%' OR " + DataBaseHelper.HPTM
				+ " LIKE '%" + text + "%' OR " + DataBaseHelper.GGXH
				+ " LIKE '%" + text + "%' OR " + DataBaseHelper.HPBM
				+ " LIKE '%" + text + "%') and " + DataBaseHelper.GXDate + "='"
				+ time + "'", null);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			for (int j = 0; j < s.length; j++) {
				map.put(s[j], c.getString(j));
			}
			list.add(map);
			c.moveToNext();
		}
		c.close();
		db.close();
		return list;

	}

	public List<Map<String, Object>> queryList_type(String[] s, String text,
			int type) {
		String str = "";
		for (int i = 0; i < s.length; i++) {
			if (i != s.length - 1) {
				str = str + s[i] + ",";
			} else {
				str = str + s[i];
			}
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery("select " + str + " from "
				+ DataBaseHelper.TB_HP + " WHERE (" + DataBaseHelper.HPMC
				+ " LIKE '%" + text + "%' OR " + DataBaseHelper.HPTM
				+ " LIKE '%" + text + "%' OR " + DataBaseHelper.GGXH
				+ " LIKE '%" + text + "%' OR " + DataBaseHelper.HPBM
				+ " LIKE '%" + text + "%') and " + DataBaseHelper.Type + "="
				+ type, null);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			for (int j = 0; j < s.length; j++) {
				map.put(s[j], c.getString(j));
			}
			list.add(map);
			c.moveToNext();
		}
		c.close();
		db.close();
		return list;
	}

	public List<Map<String, Object>> queryList_ckid(String text, String hpid,
			int ckid) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery(
				"select a.id,hpmc,hpbm,hptm,ggxh,CurrKc,b.kcsl from "
						+ DataBaseHelper.TB_HP + "  as a left join "
						+ DataBaseHelper.TB_CKKC
						+ "  as b on a.id=b.hpid where (" + DataBaseHelper.HPMC
						+ " LIKE '%" + text + "%' OR " + DataBaseHelper.HPTM
						+ " LIKE '%" + text + "%' OR " + DataBaseHelper.GGXH
						+ " LIKE '%" + text + "%' OR " + DataBaseHelper.HPBM
						+ " LIKE '%" + text + "%') and a.id > '" + hpid
						+ "' and " + DataBaseHelper.CKID + "='" + ckid
						+ "' ORDER BY a.id asc limit 0,10", null);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			for (int j = 0; j < 7; j++) {
				String ColumnName = c.getColumnName(j);
				if(ColumnName.equals(DataBaseHelper.CurrKC)||ColumnName.equals(DataBaseHelper.KCSL)){
					map.put(ColumnName, String.valueOf(c.getDouble(j)));
				}else{
					map.put(ColumnName, c.getString(j));
				}
			}
			list.add(map);
			c.moveToNext();
		}
		c.close();
		db.close();
		return list;
	}

	public List<Map<String, Object>> queryList_SX(String[] s, String text) {
		String str = "";
		for (int i = 0; i < s.length; i++) {
			if (i != s.length - 1) {
				str = str + s[i] + ",";
			} else {
				str = str + s[i];
			}
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery("select " + str + " from "
				+ DataBaseHelper.TB_HP + " WHERE (" + DataBaseHelper.HPMC
				+ " LIKE '%" + text + "%' OR " + DataBaseHelper.HPTM
				+ " LIKE '%" + text + "%' OR " + DataBaseHelper.GGXH
				+ " LIKE '%" + text + "%' OR " + DataBaseHelper.HPBM
				+ " LIKE '%" + text + "%') and " + DataBaseHelper.CurrKC + ">="
				+ DataBaseHelper.HPSX + " and " + DataBaseHelper.HPSX + "!=0",
				null);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			for (int j = 0; j < s.length; j++) {
				map.put(s[j], c.getString(j));
			}
			list.add(map);
			c.moveToNext();
		}
		c.close();
		db.close();
		return list;

	}

	public List<Map<String, Object>> queryList_bm(String[] s, String text) {
		String str = "";
		for (int i = 0; i < s.length; i++) {
			if (i != s.length - 1) {
				str = str + s[i] + ",";
			} else {
				str = str + s[i];
			}
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery("select " + str + " from "
				+ DataBaseHelper.TB_HP + " WHERE " + DataBaseHelper.HPBM
				+ " ='" + text + "'", null);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			for (int j = 0; j < s.length; j++) {
				map.put(s[j], c.getString(j));
			}
			list.add(map);
			c.moveToNext();
		}
		c.close();
		db.close();
		return list;

	}

	public List<Map<String, Object>> queryList_XX(String[] s, String text) {
		String str = "";
		for (int i = 0; i < s.length; i++) {
			if (i != s.length - 1) {
				str = str + s[i] + ",";
			} else {
				str = str + s[i];
			}
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery("select " + str + " from "
				+ DataBaseHelper.TB_HP + " WHERE (" + DataBaseHelper.HPMC
				+ " LIKE '%" + text + "%' OR " + DataBaseHelper.HPTM
				+ " LIKE '%" + text + "%' OR " + DataBaseHelper.GGXH
				+ " LIKE '%" + text + "%' OR " + DataBaseHelper.HPBM
				+ " LIKE '%" + text + "%') and " + DataBaseHelper.CurrKC + "<="
				+ DataBaseHelper.HPXX + " and " + DataBaseHelper.HPXX + "!=0",
				null);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			for (int j = 0; j < s.length; j++) {
				map.put(s[j], c.getString(j));
			}
			list.add(map);
			c.moveToNext();
		}
		c.close();
		db.close();
		return list;
	}

	public List<Map<String, Object>> queryList_index(String[] s, String text,
			String index) {
		String str = "";
		for (int i = 0; i < s.length; i++) {
			if (i != s.length - 1) {
				str = str + s[i] + ",";
			} else {
				str = str + s[i];
			}
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery("select " + str + " from "
				+ DataBaseHelper.TB_HP + " WHERE (" + DataBaseHelper.HPMC
				+ " LIKE '%" + text + "%' OR " + DataBaseHelper.HPTM
				+ " LIKE '%" + text + "%' OR " + DataBaseHelper.GGXH
				+ " LIKE '%" + text + "%' OR " + DataBaseHelper.HPBM
				+ " LIKE '%" + text + "%') and " + DataBaseHelper.LBIndex
				+ " like '" + index + "%'", null);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			for (int j = 0; j < s.length; j++) {
				map.put(s[j], c.getString(j));
			}
			list.add(map);
			c.moveToNext();
		}
		c.close();
		db.close();
		return list;

	}

	public List<Map<String, Object>> queryList_details(String[] s, String text,
			String djid) {
		String str = "";
		for (int i = 0; i < s.length; i++) {
			if (i != s.length - 1) {
				str = str + s[i] + ",";
			} else {
				str = str + s[i];
			}
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery("select " + str + " from "
				+ DataBaseHelper.TB_HP + " as a," + DataBaseHelper.TB_MoveD
				+ " as b WHERE a.id=b.hpid  and (" + DataBaseHelper.HPMC
				+ " LIKE '%" + text + "%' OR " + DataBaseHelper.HPTM
				+ " LIKE '%" + text + "%' OR " + DataBaseHelper.GGXH
				+ " LIKE '%" + text + "%' OR " + DataBaseHelper.HPBM
				+ " LIKE '%" + text + "%')  and " + DataBaseHelper.MID + "='"
				+ djid + "'", null);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			for (int j = 0; j < s.length; j++) {
				map.put(s[j], c.getString(j));
			}
			list.add(map);
			c.moveToNext();
		}
		c.close();
		db.close();
		return list;

	}

	public List<Map<String, Object>> Gethp_Index(String[] s, String index) {
		String str = "";
		for (int i = 0; i < s.length; i++) {
			if (i != s.length - 1) {
				str = str + s[i] + ",";
			} else {
				str = str + s[i];
			}
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery(
				"select " + str + " from " + DataBaseHelper.TB_HP + " where "
						+ DataBaseHelper.LBIndex + " like '" + index + "%'"
						+ " order by " + DataBaseHelper.HPBM, null);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			for (int j = 0; j < s.length; j++) {
				map.put(s[j], c.getString(j));
			}
			list.add(map);
			c.moveToNext();
		}
		c.close();
		db.close();
		return list;
	}

	public void Addhp(int id, String hpmc, String hpbm, String txm, String gg,
			String dw, String dw2, String bignum, String kc, String kcje,
			String sccs, String sx, String xx, String rkckj, String ckckj,
			String ckckj2, String lbs, String lbid, String lbIndex, String bz,
			String hppy, int stop, String res1, String res2, String res3,
			String res4, String res5, String res6, String resd1, String resd2,
			int type, String gxtime, String imgpath) {
		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		db.execSQL("insert into " + DataBaseHelper.TB_HP + " ("
				+ DataBaseHelper.ID + "," + DataBaseHelper.HPMC + ","
				+ DataBaseHelper.HPBM + "," + DataBaseHelper.HPTM + ","
				+ DataBaseHelper.GGXH + "," + DataBaseHelper.JLDW + ","
				+ DataBaseHelper.JLDW2 + "," + DataBaseHelper.BigNum + ","
				+ DataBaseHelper.CurrKC + "," + DataBaseHelper.KCJE + ","
				+ DataBaseHelper.SCCS + "," + DataBaseHelper.HPSX + ","
				+ DataBaseHelper.HPXX + "," + DataBaseHelper.RKCKJ + ","
				+ DataBaseHelper.CKCKJ + "," + DataBaseHelper.CKCKJ2 + ","
				+ DataBaseHelper.LBS + "," + DataBaseHelper.LBID + ","
				+ DataBaseHelper.LBIndex + "," + DataBaseHelper.BZ + ","
				+ DataBaseHelper.HPPY + "," + DataBaseHelper.StopTag + ","
				+ DataBaseHelper.RES1 + "," + DataBaseHelper.RES2 + ","
				+ DataBaseHelper.RES3 + "," + DataBaseHelper.RES4 + ","
				+ DataBaseHelper.RES5 + "," + DataBaseHelper.RES6 + ","
				+ DataBaseHelper.RESD1 + "," + DataBaseHelper.RESD2 + ","
				+ DataBaseHelper.Type + "," + DataBaseHelper.GXDate + ","
				+ DataBaseHelper.ImagePath + ") values ('" + id + "','" + hpmc
				+ "','" + hpbm + "','" + txm + "','" + gg + "','" + dw + "','"
				+ dw2 + "','" + bignum + "','" + kc + "','" + kcje + "','"
				+ sccs + "','" + sx + "','" + xx + "','" + rkckj + "','"
				+ ckckj + "','" + ckckj2 + "','" + lbs + "','" + lbid + "','"
				+ lbIndex + "','" + bz + "','" + hppy + "','" + stop + "','"
				+ res1 + "','" + res2 + "','" + res3 + "','" + res4 + "','"
				+ res5 + "','" + res6 + "','" + resd1 + "','" + resd2 + "','"
				+ type + "','" + gxtime + "','" + imgpath + "')");
		db.close();

	}

	/**
	 * 根据货品编码修改货品信息
	 * 
	 * @param str1
	 *            要修改的字段
	 * @param str2
	 *            修改字段的值
	 * @param str3
	 *            修改条件
	 * */
	public void updatehp_byBM(String str1, String str2, String str3) {

		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		db.execSQL("UPDATE " + DataBaseHelper.TB_HP + " SET " + str1 + " = '"
				+ str2 + "' WHERE " + DataBaseHelper.HPBM + " ='" + str3 + "'");
		db.close();
	}

	public void Addhp(int id, String hpmc, String hpbm, String tm, String gg,
			String Currkc) {
		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		db.execSQL("insert into " + DataBaseHelper.TB_HP + " ("
				+ DataBaseHelper.ID + "," + DataBaseHelper.HPMC + ","
				+ DataBaseHelper.HPBM + "," + DataBaseHelper.HPTM + ","
				+ DataBaseHelper.GGXH + "," + DataBaseHelper.CurrKC
				+ ") values ('" + id + "','" + hpmc + "','" + hpbm + "','" + tm
				+ "','" + gg + "','" + Currkc + "')");
		db.close();

	}

	public void Addhp(int id, String hpmc, String hpbm, String txm, String gg,
			String dw, String dw2, String bignum, String sccs, String sx,
			String xx, String rkckj, String ckckj, String ckckj2, String lbs,
			String lbid, String lbIndex, String bz, String hppy, int stop,
			String res1, String res2, String res3, String res4, String res5,
			String res6, String resd1, String resd2, int type, String gxtime,
			String imgpath) {
		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		db.execSQL("insert into " + DataBaseHelper.TB_HP + " ("
				+ DataBaseHelper.ID + "," + DataBaseHelper.HPMC + ","
				+ DataBaseHelper.HPBM + "," + DataBaseHelper.HPTM + ","
				+ DataBaseHelper.GGXH + "," + DataBaseHelper.JLDW + ","
				+ DataBaseHelper.JLDW2 + "," + DataBaseHelper.BigNum + ","
				+ DataBaseHelper.SCCS + "," + DataBaseHelper.HPSX + ","
				+ DataBaseHelper.HPXX + "," + DataBaseHelper.RKCKJ + ","
				+ DataBaseHelper.CKCKJ + "," + DataBaseHelper.CKCKJ2 + ","
				+ DataBaseHelper.LBS + "," + DataBaseHelper.LBID + ","
				+ DataBaseHelper.LBIndex + "," + DataBaseHelper.BZ + ","
				+ DataBaseHelper.HPPY + "," + DataBaseHelper.StopTag + ","
				+ DataBaseHelper.RES1 + "," + DataBaseHelper.RES2 + ","
				+ DataBaseHelper.RES3 + "," + DataBaseHelper.RES4 + ","
				+ DataBaseHelper.RES5 + "," + DataBaseHelper.RES6 + ","
				+ DataBaseHelper.RESD1 + "," + DataBaseHelper.RESD2 + ","
				+ DataBaseHelper.Type + "," + DataBaseHelper.GXDate + ","
				+ DataBaseHelper.ImagePath + ") values ('" + id + "','" + hpmc
				+ "','" + hpbm + "','" + txm + "','" + gg + "','" + dw + "','"
				+ dw2 + "','" + bignum + "','" + sccs + "','" + sx + "','" + xx
				+ "','" + rkckj + "','" + ckckj + "','" + ckckj2 + "','" + lbs
				+ "','" + lbid + "','" + lbIndex + "','" + bz + "','" + hppy
				+ "','" + stop + "','" + res1 + "','" + res2 + "','" + res3
				+ "','" + res4 + "','" + res5 + "','" + res6 + "','" + resd1
				+ "','" + resd2 + "','" + type + "','" + gxtime + "','"
				+ imgpath + "')");
		db.close();

	}

	// 查询是否存在某个id的货品
	public Boolean searchID(String id) {
		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery("select * from " + DataBaseHelper.TB_HP
				+ " where " + DataBaseHelper.ID + "='" + id + "'", null);
		Boolean flag = c.moveToFirst();
		c.close();
		db.close();
		return flag;
	}

	public void del_hp(String id) {
		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		db.execSQL("delete from " + DataBaseHelper.TB_HP + " where "
				+ DataBaseHelper.ID + "='" + id + "'");
		db.close();
	}

	public void del_hp() {
		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		db.execSQL("delete from " + DataBaseHelper.TB_HP);
		db.close();
	}
	
	public void deletePICurl(String hpid){
		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		db.execSQL("delete from " + DataBaseHelper.TB_PIC+ " where "+DataBaseHelper.HPID+" = '"+hpid+"'");
		db.close();
	}
	
	public void deletePIC_OneUrl(String hpid,String imageUrl){
		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		db.execSQL("delete from " + DataBaseHelper.TB_PIC+ " where "+DataBaseHelper.HPID+" = '"+hpid+"' and ImageURL = '"+imageUrl+"'");
		db.close();
	}
	
	public void insertTB_PIC(String hpid,String[] imageUrlArray){
		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		int imageUrlArrayLenth = imageUrlArray.length;
		for(int i =0;i<imageUrlArrayLenth;i++){
			db.execSQL("insert into " + DataBaseHelper.TB_PIC+ " ( hpid,ImageURL,CompressImageURL) values ('"+hpid+"','"+imageUrlArray[i]+"','Compress_"+imageUrlArray[i]+"')");
		}
		db.close();
	}
	
	public void insertTB_PIC(String hpid,String imageUrl){
		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		db.execSQL("insert into " + DataBaseHelper.TB_PIC+ " ( hpid,ImageURL,CompressImageURL) values ('"+hpid+"','"+imageUrl+"','Compress_"+imageUrl+"')");
		db.close();
	}
//------------------------------数据库图片路径转移-----------------------------------------	
	public void insertTB_PIC(List<Map<String, String>> list){
		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Iterator<Map<String, String>> it=list.iterator();
		while(it.hasNext()){
			Map<String, String> map = it.next();
			String hpid = map.get("hpid");
			String imageUrl = map.get("imgpath");
			db.execSQL("insert into " + DataBaseHelper.TB_PIC+ " ( hpid,ImageURL,CompressImageURL) values ('"+hpid+"','"+imageUrl+"','Compress_"+imageUrl+"')");
		}
		db.close();
	}
	
	public List<Map<String, String>> searchTB_HPImgpath(){
		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		List<Map<String, String>> mList = new ArrayList<Map<String,String>>();
		Cursor c = db.rawQuery("select "+DataBaseHelper.ID+","+DataBaseHelper.ImagePath+" from "+DataBaseHelper.TB_HP+" where "+DataBaseHelper.ImagePath
				+" is not null and "+DataBaseHelper.ImagePath+"!='' ", null);
		if(c.moveToFirst()){
			while(c.isAfterLast()){
				Map<String, String> map = new HashMap<String, String>();
				map.put("hpid", c.getString(c.getColumnIndex(DataBaseHelper.ID)));
				map.put("imgpath", c.getString(c.getColumnIndex(DataBaseHelper.ImagePath)));
				mList.add(map);
				c.moveToNext();
			}
		}
		c.close();
		db.close();
		return mList;
	}
//-------------------------------------------------------------------------------------------
	public List<String> getTB_PIC_ALLUrlByhpid(String hpid){
		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		List<String> mList = new ArrayList<String>();
		Cursor cursor = db.rawQuery(" select ImageURL from tb_pic where hpid = '"+hpid+"'", null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			mList.add(cursor.getString(cursor.getColumnIndex("ImageURL")));
			cursor.moveToNext();
		}
		cursor.close();
		db.close();
		return mList;
	}
	/*
	 * -1代表未上传，1代表已上传
	 * */
//	public void setTB_PIC_UpLoadStatus(String ImageURL,int status){
//		
//		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
//		db.execSQL("update "+DataBaseHelper.TB_PIC +" set "+DataBaseHelper.UpLoadStatus+" = "+status+" where "+DataBaseHelper.ImageURL+" = '"+ImageURL+"'");
//		db.close();
//	}
	
	public void update_hp_CurrKC(String id, float currkc) {
		
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		db.execSQL("update  " + DataBaseHelper.TB_HP + " set "
				+ DataBaseHelper.CurrKC + "=" + currkc + " where "
				+ DataBaseHelper.ID + "='" + id + "'");
		db.close();
	}

	public String GethpAmount(int ckid) {
		
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c;
		if (ckid < 0) {
			c = db.rawQuery("SELECT COUNT(*) FROM " + DataBaseHelper.TB_HP,
					null);
		} else {
			c = db.rawQuery("SELECT COUNT(*) FROM " + DataBaseHelper.TB_CKKC
					+ " where " + DataBaseHelper.CKID + "='" + ckid + "'", null);
		}
		String str = "0";
		if(c.moveToFirst()){
			str = c.getString(0);
		}
		c.close();
		db.close();
		return str;
	}

	public String GethpAmount_SX(int ckid) {
		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c;
		if (ckid < 0) {
			c = db.rawQuery("SELECT COUNT(*) FROM " + DataBaseHelper.TB_HP
					+ " where " + DataBaseHelper.CurrKC + ">"
					+ DataBaseHelper.HPSX + " and " + DataBaseHelper.HPSX
					+ "!='' and "+DataBaseHelper.HPSX+" is not null and " + DataBaseHelper.CurrKC + "!='' and "
					+ DataBaseHelper.CurrKC+" is not null ", null);
		} else {
			c = db.rawQuery("SELECT COUNT(*) FROM " + DataBaseHelper.TB_HP
					+ " as a," + DataBaseHelper.TB_CKKC + " as b"
					+ " where a.id=b.hpid and b." + DataBaseHelper.CKID + "='"
					+ ckid + "' and " + DataBaseHelper.CurrKC + ">"
					+ DataBaseHelper.HPSX + " and " + DataBaseHelper.HPSX
					+ "!='' and "+DataBaseHelper.HPSX+" is not null and " + DataBaseHelper.CurrKC + "!='' and "
					+ DataBaseHelper.CurrKC+" is not null ", null);
		}
		
		String str ="0";
		if(c.moveToFirst()){
			str = c.getString(0);
		}
		c.close();
		db.close();
		return str;
	}
	
	public String Gethptodaychange(int ckid, String date) {
		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = null;
		if (ckid < 0) {
			c = db.rawQuery("select count(DISTINCT hpid) as num from "
					+ DataBaseHelper.TB_MoveD + " where "
					+ DataBaseHelper.MVDDATE + " >= '" + date + " 00:00:00' and "
					+ DataBaseHelper.MVDDATE + " < '" + date + " 23:59:59' ", null);
		} else {
			c = db.rawQuery("select count(DISTINCT hpid) as num from "
					+ DataBaseHelper.TB_MoveD + " where " + DataBaseHelper.CKID
					+ " = " + ckid + " and " + DataBaseHelper.MVDDATE + " >= '"
					+ date + " 00:00:00' and " + DataBaseHelper.MVDDATE + " < '"
					+ date + " 23:59:59' ", null);
		}
		String str = "0";
		if(c.moveToFirst()){
			str = c.getString(0);
		}
		c.close();
		db.close();
		return str;
	}

	public String GethpAmount_XX(int ckid) {
		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c;
		if (ckid < 0) {
			c = db.rawQuery("SELECT COUNT(*) FROM " + DataBaseHelper.TB_HP
					+ " where " + DataBaseHelper.CurrKC + " < "
					+ DataBaseHelper.HPXX + " and " + DataBaseHelper.HPXX
					+ "!='' and "+DataBaseHelper.HPXX+" is not null and " + DataBaseHelper.CurrKC + "!='' and "
					+ DataBaseHelper.CurrKC + " is not null ", null);
		} else {
			c = db.rawQuery("SELECT COUNT(*) FROM " + DataBaseHelper.TB_HP
					+ " as a," + DataBaseHelper.TB_CKKC + " as b"
					+ " where a.id=b.hpid and b." + DataBaseHelper.CKID + "='"
					+ ckid + "' and " + DataBaseHelper.CurrKC + " < "
					+ DataBaseHelper.HPXX + " and " + DataBaseHelper.HPXX
					+ "!='' and "+DataBaseHelper.HPXX+" is not null and " + DataBaseHelper.CurrKC + "!='' and "
					+ DataBaseHelper.CurrKC + " is not null ", null);
		}
		String str = "0";
		if(c.moveToFirst()){
			str = c.getString(0);
		}
		c.close();
		db.close();
		return str;
	}
	
	public String gettb_deptreePid(int id){
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery("select "+DataBaseHelper.PID+" from "+DataBaseHelper.TB_depTree+" where "+DataBaseHelper.ID+" = "+id,null);
		if(c.moveToFirst()){
			return c.getString(c.getColumnIndex(DataBaseHelper.PID));
		}else {
			return "";
		}
	}
	
	public List<Map<String, Object>> GetDep(String pid) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery("select * from " + DataBaseHelper.TB_depTree
				+ " where " + DataBaseHelper.PID + "='" + pid + "'", null);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("id", c.getString(0));
			map.put("name", c.getString(1));
			map.put(DataBaseHelper.depLevel, c.getString(2));
			map.put(DataBaseHelper.PID, c.getString(3));
			map.put(DataBaseHelper.depOrder, c.getString(4));
			map.put(DataBaseHelper.depindex, c.getString(5));
			list.add(map);
			c.moveToNext();
		}
		c.close();
		db.close();
		return list;
	}

	public void del_Dep(String id, SQLiteDatabase db) {
		db.execSQL("delete from " + DataBaseHelper.TB_depTree + " where "
				+ DataBaseHelper.ID + "='" + id + "'");
	}

	public void del_Dep(String id) {		
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		db.execSQL("delete from " + DataBaseHelper.TB_depTree + " where "
				+ DataBaseHelper.ID + "='" + id + "'");
		db.close();
	}
	
	public String gettb_hplbtreePid(int id){
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery("select "+DataBaseHelper.PID+" from "+DataBaseHelper.TB_hplbTree+" where "+DataBaseHelper.ID+" = "+id,null);
		if(c.moveToFirst()){
			return c.getString(c.getColumnIndex(DataBaseHelper.PID));
		}else{
			return "";
		}
		
	}
	
	public List<Map<String, Object>> GetLB(String pid) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery("select * from " + DataBaseHelper.TB_hplbTree
				+ " where " + DataBaseHelper.PID + "='" + pid + "'", null);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("id", c.getString(0));
			map.put("name", c.getString(1));
			map.put("lev", c.getString(2));
			map.put("pid", c.getString(3));
			map.put("ord", c.getString(4));
			map.put("sindex", c.getString(5));
			map.put("lbs", c.getString(6));
			list.add(map);
			c.moveToNext();
		}
		c.close();
		db.close();
		return list;
	}
	
	public String GetLBIndex(String id) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();		
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery("select * from " + DataBaseHelper.TB_hplbTree
				+ " where " + DataBaseHelper.ID + "='" + id + "'", null);
		c.moveToFirst();
		String str = c.getString(c.getColumnIndex("sindex"));
		c.close();
		db.close();
		return str;
	}

	public List<Map<String, Object>> GetLB_up(String id) {
		String str = "";
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery("select * from " + DataBaseHelper.TB_hplbTree
				+ " where " + DataBaseHelper.ID + "='" + id + "'", null);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("id", c.getString(0));
			map.put("name", c.getString(1));
			map.put("lev", c.getString(2));
			map.put("pid", c.getString(3));
			map.put("ord", c.getString(4));
			map.put("index", c.getString(5));
			map.put("lbs", c.getString(6));
			list.add(map);
			c.moveToNext();
		}
		c.close();
		db.close();

		return list;
	}

	public List<Map<String, Object>> GetLB_index(String index) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery("select * from " + DataBaseHelper.TB_hplbTree
				+ " where " + DataBaseHelper.Sindex + " = '" + index + "'",
				null);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("id", c.getString(0));
			map.put("name", c.getString(1));
			map.put("lev", c.getString(2));
			map.put("pid", c.getString(3));
			map.put("ord", c.getString(4));
			map.put("index", c.getString(5));
			map.put("lbs", c.getString(6));
			list.add(map);
			c.moveToNext();
		}
		c.close();
		db.close();
		return list;
	}

	public String GetLB_name(String index, SQLiteDatabase db) {
		Cursor c = db.rawQuery("select * from " + DataBaseHelper.TB_hplbTree
				+ " where " + DataBaseHelper.Sindex + "='" + index + "'", null);
		c.moveToFirst();
		String str = c.getString(1);
		c.close();
		return str;
	}

	public String GetLBS_index(String index) {
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		String str = "";
		for (int i = 2; i < index.length() + 1; i = i + 2) {
			if (i < index.length()) {
				str = str + GetLB_name(index.substring(0, i), db) + "\\";
			} else {
				str = str + GetLB_name(index.substring(0, i), db);
			}
		}
		if (index.length() == 0) {
			str = "";
		}
		db.close();
		return str;
	}

//	public void Update_LB(List<Map<String, Object>> ls) {
//		
//				
//		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
//		for (int i = 0; i < ls.size(); i++) {
//			String index = (String) ls.get(i).get("index");
//			String lbs = GetLBS_index(index, db);
//			db.execSQL("update " + DataBaseHelper.TB_HP + " set "
//					+ DataBaseHelper.LBS + "='" + lbs + "'" + " where "
//					+ DataBaseHelper.LBIndex + " = '" + index + "'");
//			db.execSQL("update " + DataBaseHelper.TB_hplbTree + " set "
//					+ DataBaseHelper.LBS + "='" + lbs + "'" + " where "
//					+ DataBaseHelper.Sindex + " ='" + index + "'");
//		}
//		db.close();
//	}

	public String GetLBS(String index) {
		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = null;
		String lbs = "";
		for (int i = 2; i < index.length() + 1; i = i + 2) {
			c = db.rawQuery("select name from " + DataBaseHelper.TB_hplbTree
					+ " where " + DataBaseHelper.Sindex + "='" + index + "'",
					null);
			c.moveToFirst();
			if (lbs == null || lbs.equals("")) {
				lbs = c.getString(0);
			} else {
				lbs = lbs + "/" + c.getString(0);
			}
		}
		c.close();
		db.close();
		return lbs;
	}

	public void insertLB(String name, int lev, String pid, int ord,
			String index, String lbs) {
		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		db.execSQL("insert into " + DataBaseHelper.TB_hplbTree + " ("
				+ DataBaseHelper.Name + "," + DataBaseHelper.Lev + ","
				+ DataBaseHelper.PID + "," + DataBaseHelper.Ord + ","
				+ DataBaseHelper.Sindex + "," + DataBaseHelper.LBS
				+ " ) values ('" + name + "','" + lev + "','" + pid + "','"
				+ ord + "','" + index + "','" + lbs + "' )");
		db.close();
	}

	public int addLB(String name, String index) {
		if (!index.equals("")) {
			
			SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
			Cursor c = db.rawQuery("select * from "
					+ DataBaseHelper.TB_hplbTree + " where "
					+ DataBaseHelper.Sindex + "='" + index + "'", null);
			c.moveToFirst();
			String id = c.getString(0);
			int lev = Integer.parseInt(c.getString(2));
			String lbs = c.getString(6);
			c.close();
			db.close();
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			list = GetLB(id);
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).get("name").equals(name)) {
					return -1;
				}
			}
			int ord = 1;
			if (list.size() == 0) {
				insertLB(name, lev + 1, id, 1, index + "01", lbs + "/" + name);
				return 2;
			} else if (list.size() > 98) {
				return -2;
			} else {
				ord = Integer.parseInt((String) list.get(list.size() - 1).get(
						"ord"));
				insertLB(name, lev + 1, id, ord + 1, index
						+ new DecimalFormat("00").format(ord + 1), lbs + "/"
						+ name);
			}
			return 1;
		} else {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			list = GetLB("0");
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).get("name").equals(name)) {
					return -1;
				}
			}
			int ord = 1;
			if (list.size() == 0) {
				insertLB(name, 0, "0", 1, "01", name);
				return 2;
			} else if (list.size() > 98) {
				return -2;
			} else {
				ord = Integer.parseInt((String) list.get(list.size() - 1).get(
						"ord"));
				insertLB(name, 0, "0", ord + 1,
						new DecimalFormat("00").format(ord + 1), name);
			}
			return 1;
		}
	}

	public void delLB(String index) {
		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		db.execSQL("delete from " + DataBaseHelper.TB_hplbTree + " where "
				+ DataBaseHelper.Sindex + " like'" + index + "%'");
		db.close();

	}

	public void del_ByID(String tb, String id, SQLiteDatabase db) {

		db.execSQL("delete from " + tb + " where " + DataBaseHelper.ID + "='"
				+ id + "'");

	}

	public void Update_hpLB(String index) {
		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		db.execSQL("update " + DataBaseHelper.TB_HP + " set "
				+ DataBaseHelper.LBS + "='', " + DataBaseHelper.LBIndex
				+ "='', " + DataBaseHelper.LBID + "=''" + " where "
				+ DataBaseHelper.LBIndex + " like '" + index + "%'");
		db.close();
	}

	public void Update_hpLB_lbs(String index, String lbs) {
		
				
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		db.execSQL("update " + DataBaseHelper.TB_HP + " set "
				+ DataBaseHelper.LBS + "='" + lbs + "'" + " where "
				+ DataBaseHelper.LBIndex + " = '" + index + "'");
		db.close();
	}

	// public void Update_lbs(String index,String lbs){
	// DataBaseImport data=DataBaseImport.getInstance(context) null,
	// DataBaseHelper.DB_VERSION);
	// SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	// db.execSQL("update "+DataBaseHelper.TB_hplbTree+" set "+DataBaseHelper.LBS+"='"+lbs+"'"+" where "+
	// DataBaseHelper.Sindex+" ='"+index+"'");
	// db.close();
	// }
	public void renameLB(String name, String index) {
		
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		
		db.execSQL("update " + DataBaseHelper.TB_hplbTree + " set "
				+ DataBaseHelper.Name + "='" + name + "'" + " where "
				+ DataBaseHelper.Sindex + "='" + index + "'");
		db.close();
	}

	public List<Map<String, Object>> getAllCK() {

		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery("select id,ckmc from " + DataBaseHelper.TB_CK,
				null);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		while (c.moveToNext()) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(DataBaseHelper.ID,
					c.getInt(c.getColumnIndex(DataBaseHelper.ID)));
			map.put(DataBaseHelper.CKMC,
					c.getString(c.getColumnIndex(DataBaseHelper.CKMC)));
			list.add(map);
		}
		c.close();
		db.close();
		return list;
	}

}
