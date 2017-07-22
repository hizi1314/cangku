package com.guantang.cangkuonline.database;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.guantang.cangkuonline.application.MyApplication;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataBaseOperateMethod {
	Context context;
	public DataBaseOperateMethod(Context mcontext){
		this.context=mcontext;
	}
	public boolean check_DH(String dh){
		
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery("select * from "+DataBaseHelper.TB_MoveM+" where "+DataBaseHelper.MVDH+
				"='"+dh+"'",null);
		if(c.moveToFirst()==true){
			c.close();
			db.close();
			return false;
		}else{
			c.close();
			db.close();
			return true;
		}
	}
	public boolean check_DDDH(String dh){
		
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery("select * from "+DataBaseHelper.TB_Order+" where "+DataBaseHelper.OrderIndex+
				"='"+dh+"'",null);
		if(c.moveToFirst()==true){
			c.close();
			db.close();
			return false;
		}else{
			c.close();
			db.close();
			return true;
		}
	}
	public void Add_DW(int type,String name,String addr,String fax,String yb,String net,String lxr,String phone,String tel
			,String qq,String email,String bz){
		
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		db.execSQL("insert into "+DataBaseHelper.TB_Company+" ( "+DataBaseHelper.DWtype+","+DataBaseHelper.DWName+","
		        +DataBaseHelper.ADDR+","+DataBaseHelper.FAX+","+DataBaseHelper.YB+","+DataBaseHelper.Net+","+
				DataBaseHelper.LXR+","+DataBaseHelper.Phone+","+DataBaseHelper.TEL+","+DataBaseHelper.QQ+","+
		        DataBaseHelper.Email+","+DataBaseHelper.BZ+" ) values ( '"+type+"','"+name+"','"+addr+"','"+fax+"','"
				+yb+"','"+net+"','"+lxr+"','"+phone+"','"+tel+"','"+qq+"','"+email+"','"+bz+"')");
		db.close();
	}
	public List<Map<String, Object>> search_Info(String tb,String[] s){
		String str="";
		for(int i=0;i<s.length;i++){
			if(i!=s.length-1){
				str=str+s[i]+",";
			}else{
				str=str+s[i];
			}
		}
		List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
		
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery("select "+str+" from "+tb,null);
		 c.moveToFirst();
		 while (!c.isAfterLast()){
			 HashMap<String, Object> map = new HashMap<String, Object>();
			 for(int j=0;j<s.length;j++){
				 map.put(s[j], c.getString(j));
			 }
			 list.add(map);
			 c.moveToNext();
		 }
		 c.close();
		 db.close();
		return list;
	}
	public List<Map<String, Object>> Gt_cp(int type,String[] s){
		String str="";
		for(int i=0;i<s.length;i++){
			if(i!=s.length-1){
				str=str+s[i]+",";
			}else{
				str=str+s[i];
			}
		}
		List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
		
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery("select "+str+" from "+DataBaseHelper.TB_Company+" where "+DataBaseHelper.DWtype+"='"
				+type+"'",null);
		 c.moveToFirst();
		 while (!c.isAfterLast()){
			 HashMap<String, Object> map = new HashMap<String, Object>();
			 for(int j=0;j<s.length;j++){
				 map.put(s[j], c.getString(j));
			 }
			 list.add(map);
			 c.moveToNext();
		 }
		 c.close();
		 db.close();
		return list;
	}
	public List<Map<String, Object>> Gt_cp(String[] s){
		String str="";
		for(int i=0;i<s.length;i++){
			if(i!=s.length-1){
				str=str+s[i]+",";
			}else{
				str=str+s[i];
			}
		}
		List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
		
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery("select "+str+" from "+DataBaseHelper.TB_Company,null);
		 c.moveToFirst();
		 while (!c.isAfterLast()){
			 HashMap<String, Object> map = new HashMap<String, Object>();
			 for(int j=0;j<s.length;j++){
				 map.put(s[j], c.getString(j));
			 }
			 list.add(map);
			 c.moveToNext();
		 }
		 c.close();
		 db.close();
		return list;
	}
	public List<Map<String, Object>> Gt_ck(String[] s){
		String str="";
		for(int i=0;i<s.length;i++){
			if(i!=s.length-1){
				str=str+s[i]+",";
			}else{
				str=str+s[i];
			}
		}
		List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
		
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery("select "+str+" from "+DataBaseHelper.TB_CK,null);
		 c.moveToFirst();
		 while (!c.isAfterLast()){
			 HashMap<String, Object> map = new HashMap<String, Object>();
			 for(int j=0;j<s.length;j++){
				 map.put(s[j], c.getString(j));
			 }
			 list.add(map);
			 c.moveToNext();
		 }
		 c.close();
		 db.close();
		return list;
	}
	public List<Map<String, Object>> Gt_ckkc_byhpid(String hpid,String[] s){
		String str="";
		for(int i=0;i<s.length;i++){
			if(i!=s.length-1){
				str=str+s[i]+",";
			}else{
				str=str+s[i];
			}
		}
		List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
		
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery("select "+str+" from "+DataBaseHelper.TB_CKKC+" where "+DataBaseHelper.HPID
				+"='"+hpid+"'",null);
		 c.moveToFirst();
		 while (!c.isAfterLast()){
			 HashMap<String, Object> map = new HashMap<String, Object>();
			 for(int j=0;j<s.length;j++){
				 map.put(s[j], c.getString(j));
			 }
			 list.add(map);
			 c.moveToNext();
		 }
		 c.close();
		 db.close();
		return list;
	}
	
	public List<Map<String,Object>> get_ckkcAndckmc(String hpid){
		List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery("select  a.ckid,a.kcsl,b.ckmc from "+DataBaseHelper.TB_CKKC+" as a left join "+DataBaseHelper.TB_CK+" as b on a.ckid = b.id where a.hpid = '"+hpid+"'", null);
		while(c.moveToNext()){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("ckid", c.getString(c.getColumnIndex("ckid")));
			map.put("kcsl", c.getDouble(c.getColumnIndex("kcsl")));
			map.put("ckmc", c.getString(c.getColumnIndex("ckmc")));
			list.add(map);
		}
		c.close();
		db.close();
		return list;
	}
	
	public String Gt_ckkc(String hpid,int ckid){
		String str;
		
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery("select kcsl from "+DataBaseHelper.TB_CKKC+" where "+DataBaseHelper.HPID
				+"='"+hpid+"' and "+DataBaseHelper.CKID+"='"+ckid+"'",null);
		 if(c.moveToFirst()){
			 str=c.getString(0);
		 }else{
			 str=null;
		 }
		 c.close();
		 db.close();
		return str;
	}
	public String Gt_ck_name(String ckid){
		String str;
		
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery("select "+DataBaseHelper.CKMC+" from "+DataBaseHelper.TB_CK+" where "+
				DataBaseHelper.ID+"='"+ckid+"'",null);
		 if(c.moveToFirst()){
			 str=c.getString(0);
		 }else{
			 str="";
		 }
		 c.close();
		 db.close();
		return str;
	}
	public List<Map<String, Object>> Gt_cp(int type,String[] s,String id){
		String str="";
		for(int i=0;i<s.length;i++){
			if(i!=s.length-1){
				str=str+s[i]+",";
			}else{
				str=str+s[i];
			}
		}
		List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
		
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery("select "+str+" from "+DataBaseHelper.TB_Company+" where "+DataBaseHelper.DWtype+"='"
				+type+"' and "+DataBaseHelper.ID+"='"+id+"'",null);
		 c.moveToFirst();
		 while (!c.isAfterLast()){
			 HashMap<String, Object> map = new HashMap<String, Object>();
			 for(int j=0;j<s.length;j++){
				 map.put(s[j], c.getString(j));
			 }
			 list.add(map);
			 c.moveToNext();
		 }
		 c.close();
		 db.close();
		return list;
	}
	
	
	
	public List<Map<String, Object>> Gt_cp(String[] s,String id){
		String str="";
		for(int i=0;i<s.length;i++){
			if(i!=s.length-1){
				str=str+s[i]+",";
			}else{
				str=str+s[i];
			}
		}
		List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
		
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery("select "+str+" from "+DataBaseHelper.TB_Company+" where "
				+DataBaseHelper.ID+"='"+id+"'",null);
		 c.moveToFirst();
		 while (!c.isAfterLast()){
			 HashMap<String, Object> map = new HashMap<String, Object>();
			 for(int j=0;j<s.length;j++){
				 map.put(s[j], c.getString(j));
			 }
			 list.add(map);
			 c.moveToNext();
		 }
		 c.close();
		 db.close();
		return list;
	}
	public String Gt_cp_id(){
		String str = null;
		
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery("select "+DataBaseHelper.ID+" from "+DataBaseHelper.TB_Company+" order by "+
				DataBaseHelper.ID+" desc",null);
		c.moveToFirst();
		str=String.valueOf(Integer.parseInt(c.getString(0)));
		c.close();
		db.close();
		return str;
	}
	public List<Map<String, Object>> Gt_cp(int type,String[] s,String id,SQLiteDatabase db){
		String str="";
		for(int i=0;i<s.length;i++){
			if(i!=s.length-1){
				str=str+s[i]+",";
			}else{
				str=str+s[i];
			}
		}
		List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
		Cursor c = db.rawQuery("select "+str+" from "+DataBaseHelper.TB_Company+" where "+DataBaseHelper.DWtype+"='"
				+type+"' and "+DataBaseHelper.ID+"='"+id+"'",null);
		 c.moveToFirst();
		 while (!c.isAfterLast()){
			 HashMap<String, Object> map = new HashMap<String, Object>();
			 for(int j=0;j<s.length;j++){
				 map.put(s[j], c.getString(j));
			 }
			 list.add(map);
			 c.moveToNext();
		 }
		 c.close();
		return list;
	}
	public List<Map<String, Object>> queryList_CP(int type,String[] s,String text){
		String str="";
		for(int i=0;i<s.length;i++){
			if(i!=s.length-1){
				str=str+s[i]+",";
			}else{
				str=str+s[i];
			}
		}
		List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
		
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c;
		try{
			c = db.rawQuery("select "+str+" from "+DataBaseHelper.TB_Company+" where "
					+DataBaseHelper.DWName+" like '%"+text+"%'",null);
		}catch(Exception e){
			c=null;
		}
		 c.moveToFirst();
		 while (!c.isAfterLast()){
			 HashMap<String, Object> map = new HashMap<String, Object>();
			 for(int j=0;j<s.length;j++){
				 map.put(s[j], c.getString(j));
			 }
			 list.add(map);
			 c.moveToNext();
		 }
		 c.close();
		 db.close();
		return list;
	}
	public List<Map<String, Object>> Gt_Conf(String[] s){
		String str="";
		for(int i=0;i<s.length;i++){
			if(i!=s.length-1){
				str=str+s[i]+",";
			}else{
				str=str+s[i];
			}
		}
		List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
		
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery("select "+str+" from "+DataBaseHelper.TB_Conf,null);
		 c.moveToFirst();
		 while (!c.isAfterLast()){
			 HashMap<String, Object> map = new HashMap<String, Object>();
			 for(int j=0;j<s.length;j++){
				 map.put(s[j], c.getString(j));
			 }
			 list.add(map);
			 c.moveToNext();
		 }
		 c.close();
		 db.close();
		return list;
	}
/*
 * 根据tb_conf表的GID查询记录
 * */
public List<Map<String, Object>> Gt_ConfByGID(String searchString,String[] s){
	String str="";
	for(int i=0;i<s.length;i++){
		if(i!=s.length-1){
			str=str+s[i]+",";
		}else{
			str=str+s[i];
		}
	}
	List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	Cursor c = db.rawQuery("select "+str+" from "+DataBaseHelper.TB_Conf+" where "+DataBaseHelper.GID+" = '"+searchString+"'",null);
	 c.moveToFirst();
	 while (!c.isAfterLast()){
		 HashMap<String, Object> map = new HashMap<String, Object>();
		 for(int j=0;j<s.length;j++){
			 map.put(s[j], c.getString(j));
		 }
		 list.add(map);
		 c.moveToNext();
	 }
	 c.close();
	 db.close();
	return list;
}
	
	/**同步仓库信息的方法
	 * */
public void insert_into1(String tb,String[] s1,String[] s2,SQLiteDatabase db){
		String str1="",str2="'";
		for(int i=0;i<s1.length;i++){
			if(i!=s1.length-1){
				str1=str1+s1[i]+",";
			}else{
				str1=str1+s1[i];
			}
		}
		for(int i=0;i<s2.length;i++){
			if(i!=s2.length-1){
				str2=str2+s2[i]+"','";
			}else{
				str2=str2+s2[i]+"'";
			}
		}
		db.execSQL("insert into "+tb+" ("+str1+") values ("+str2+")");
	}

public void insert_into(String tb,String[] s1,String[] s2){
		String str1="",str2="'";
		for(int i=0;i<s1.length;i++){
			if(i!=s1.length-1){
				str1=str1+s1[i]+",";
			}else{
				str1=str1+s1[i];
			}
		}
		for(int i=0;i<s2.length;i++){
			if(i!=s2.length-1){
				str2=str2+s2[i]+"','";
			}else{
				str2=str2+s2[i]+"'";
			}
		}
		synchronized (new Object()) {
			SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
			db.execSQL("insert into "+tb+" ("+str1+") values ("+str2+")");
			db.close();
		}
	}

public void insert_ckkc(String hpid,int ckid,float kcsl){
	
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	db.execSQL("insert into "+DataBaseHelper.TB_CKKC+" (hpid,ckid,kcsl) values ("+hpid+","+ckid+","+kcsl+")");
	db.close();
}

public String search_DJID(String dh){
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	Cursor c = db.rawQuery("select "+DataBaseHelper.HPM_ID+" from "+DataBaseHelper.TB_MoveM+" where "+
			DataBaseHelper.MVDH+"='"+dh+"'",null);
	c.moveToFirst();
	String id=c.getString(0);
	c.close();
	db.close();
	return id;	
}

public String GtMax_DJID(){
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	Cursor c = db.rawQuery("select max("+DataBaseHelper.HPM_ID+") from "+DataBaseHelper.TB_MoveM,null);
	c.moveToFirst();
	String id=c.getString(0);
	c.close();
	db.close();
	return id;
}

/*
 * 查询是否有未完成的单据(入库、出库、盘点)
 * 
 * */
public String searchUncompleteDJ(int mType){
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	Cursor c = db.rawQuery("select "+DataBaseHelper.HPM_ID+" from "+DataBaseHelper.TB_MoveM+" where "+DataBaseHelper.DJType+
			" = 0 and "+DataBaseHelper.mType+" = "+mType,null);
	String djid="";
	if(c.moveToFirst()){
		djid=c.getString(c.getColumnIndex(DataBaseHelper.HPM_ID));
	}
	c.close();
	db.close();
	return djid;
}

/*
 * 查询订单状态
 * 
 * Status -5 订单未完成,-4 未上传,-3 已上传, 0 待审核, 1 未通过审核, 2 待出库, 3 部分出库 , 5 已出库 , 7 超量出库
 */
public String searchUncompleteDD(int dirc){
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	Cursor c = db.rawQuery("select "+DataBaseHelper.ID+" from "+DataBaseHelper.TB_Order+" where "+
	DataBaseHelper.Status+" = -5 and "+DataBaseHelper.dirc+" = "+dirc, null);
	
	String ddid="";
	if(c.moveToFirst()){
		ddid = c.getString(c.getColumnIndex(DataBaseHelper.ID));
	}
	return ddid;
}

public String GtMax_DDDJID(){
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	Cursor c = db.rawQuery("select max("+DataBaseHelper.ID+") from "+DataBaseHelper.TB_Order,null);
	c.moveToFirst();
	String id=c.getString(0);
	c.close();
	db.close();
	return id;
}

public String GtMax_DBDJID(){
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	Cursor c = db.rawQuery("select max("+DataBaseHelper.ID+") from "+DataBaseHelper.TB_TRANSM,null);
	c.moveToFirst();
	String id=c.getString(0);
	c.close();
	db.close();
	return id;
}

public void Update_HPKC(String id,String num){
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	db.execSQL("update "+DataBaseHelper.TB_HP+" set "+DataBaseHelper.CurrKC+"='"+num+
			"' where "+DataBaseHelper.ID+"='"+id+"'");
	db.close();
}
public void Update_CKKC(String hpid,int ckid,float num){
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	db.execSQL("update "+DataBaseHelper.TB_CKKC+" set "+DataBaseHelper.KCSL+"="+num+
			" where "+DataBaseHelper.HPID+"='"+hpid+"' and "+DataBaseHelper.CKID+"="+ckid);
	db.close();
}
public Map<String, Object> Djhp_Sum(String djid){
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	Cursor c = db.rawQuery("select count(mid) as num from "+DataBaseHelper.TB_MoveD+" where "+DataBaseHelper.MID+" = '"+djid+"'", null);
	c.moveToFirst();
	Map<String, Object> map = new HashMap<String, Object>();
	map.put("num", c.getString(c.getColumnIndex("num")));
	c.close();
	db.close();
	return map;
}

public Map<String, Object> DDhp_Sum(String ddid) {
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	Cursor c = db.rawQuery("select count(id) as num from "
			+ DataBaseHelper.TB_OrderDetail + " where " + DataBaseHelper.orderID
			+ " = '" + ddid + "'", null);
	c.moveToFirst();
	Map<String, Object> map = new HashMap<String, Object>();
	map.put("num", c.getString(c.getColumnIndex("num")));
	return map;
}

public Map<String, Object> diaoBohp_Sum(String djid) {
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	Cursor c = db.rawQuery("select count(id) as num from "
			+ DataBaseHelper.TB_TRANSD + " where " + DataBaseHelper.MID
			+ " = '" + djid + "'", null);
	c.moveToFirst();
	Map<String, Object> map = new HashMap<String, Object>();
	map.put("num", c.getString(c.getColumnIndex("num")));
	return map;
}

public List<Map<String, Object>> search_DJ_from(String date,String[] s,int flag){
	String str="";
	for(int i=0;i<s.length;i++){
		if(i!=s.length-1){
			str=str+s[i]+",";
		}else{
			str=str+s[i];
		}
	}
	List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	String time = date+" 00:00:00";
	Cursor c = db.rawQuery("select "+str+" from "+DataBaseHelper.TB_MoveM+" where "+DataBaseHelper.MVDT+">'"
			+time+"' and "+DataBaseHelper.mType+"="+flag+" order by "+
					DataBaseHelper.MVDT+" desc",null);
	c.moveToFirst();
	while (!c.isAfterLast()){
		 HashMap<String, Object> map = new HashMap<String, Object>();
		 for(int j=0;j<s.length;j++){
			 map.put(s[j], c.getString(j));
		 }
		 list.add(map);
		 c.moveToNext();
	 }
	c.close();
	 db.close();
	return list;
}
public List<Map<String, Object>> search_DJ_date(String date,String[] s,int flag){
	String str="";
	for(int i=0;i<s.length;i++){
		if(i!=s.length-1){
			str=str+s[i]+",";
		}else{
			str=str+s[i];
		}
	}
	List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	String time = date+" 00:00:00";
	String time2 = date +" 24:00:00";
	Cursor c = db.rawQuery("select "+str+" from "+DataBaseHelper.TB_MoveM+" where "+DataBaseHelper.MVDT+">'"
			+time+"'and "+DataBaseHelper.MVDT+"<'"+time2+"' and "+DataBaseHelper.mType+"="+flag+" order by "+
			DataBaseHelper.MVDT+" desc",null);
	c.moveToFirst();
	while (!c.isAfterLast()){
		 HashMap<String, Object> map = new HashMap<String, Object>();
		 for(int j=0;j<s.length;j++){
			 map.put(s[j], c.getString(j));
		 }
		 list.add(map);
		 c.moveToNext();
	 }
	c.close();
	db.close(); 
	return list;
}
public String Gt_DJ_count(int type){
	String str,time;
	SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
	time=formatter.format(new Date(System.currentTimeMillis()));
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	Cursor c = db.rawQuery("select count(hpm_id) from "+DataBaseHelper.TB_MoveM+" where "+
	DataBaseHelper.mType+"='"+type+"' and "+DataBaseHelper.MVDT+"<'"+time+" 23:59:59' and "
	+DataBaseHelper.MVDT+">='"+time+"' and "+DataBaseHelper.MVDH+"!=''",null);
	if(c.moveToFirst()){
		str=c.getString(0);
	}else{
		str="-1";
	}
	c.close();
	db.close();
	return str;
}
public String Gt_unDJ_count(int type){
	String str;
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	Cursor c = db.rawQuery("select count(hpm_id) from "+DataBaseHelper.TB_MoveM+" where "+
	DataBaseHelper.DJType+"='"+type+"' and "+DataBaseHelper.MVDH+"!=''",null);
	if(c.moveToFirst()){
		str=c.getString(0);
	}else{
		str="-1";
	}
	c.close();
	db.close();
	return str;
}

public List<Map<String, Object>> search_DJ(String wherestr,String[] s,int ckid){
	String str="";
	for(int i=0;i<s.length;i++){
		if(i!=s.length-1){
			str=str+s[i]+",";
		}else{
			str=str+s[i];
		}
	}
	List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	Cursor c=null;
	if(ckid==-1){
		c = db.rawQuery("select "+str+" from "+DataBaseHelper.TB_MoveM+wherestr,null);
	}else{
		wherestr = wherestr+DataBaseHelper.CKID+" = "+ckid+" order by lrsj desc";
		c = db.rawQuery("select "+str+" from "+DataBaseHelper.TB_MoveM+wherestr,null);
	}
	c.moveToFirst();
	while (!c.isAfterLast()){
		 HashMap<String, Object> map = new HashMap<String, Object>();
		 for(int j=0;j<s.length;j++){
			 map.put(s[j], c.getString(j));
		 }
		 list.add(map);
		 c.moveToNext();
	 }
	c.close();
	db.close(); 
	return list;
}

public String search_todayDJnum(int mType,String time,int ckid){
	
	List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	Cursor c = null;
	if(ckid == -1){
		c = db.rawQuery("select count(mType) as num from "+DataBaseHelper.TB_MoveM+" where "+DataBaseHelper.mType+"='"+mType+"' and "+
				DataBaseHelper.MVDT+"='"+time+"'",null);
	}else{
		c = db.rawQuery("select count(mType) as num from "+DataBaseHelper.TB_MoveM+" where "+DataBaseHelper.mType+"='"+mType+"' and "+
				DataBaseHelper.MVDT+"='"+time+"' and "+DataBaseHelper.CKID+" = "+ckid,null);
	}
	
	String num="";
	if(c!=null){
		c.moveToFirst();
		num = c.getString(0);
	}
	c.close();
	db.close(); 
	return num;
}

public List<Map<String, Object>> search_DDDJ(String wherestr,String[] s){
	String str="";
	for(int i=0;i<s.length;i++){
		if(i!=s.length-1){
			str=str+s[i]+",";
		}else{
			str=str+s[i];
		}
	}
	List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	Cursor c = db.rawQuery("select "+str+" from "+DataBaseHelper.TB_Order+wherestr,null);
	c.moveToFirst();
	while (!c.isAfterLast()){
		 HashMap<String, Object> map = new HashMap<String, Object>();
		 for(int j=0;j<s.length;j++){
			 map.put(s[j], c.getString(j));
		 }
		 list.add(map);
		 c.moveToNext();
	 }
	c.close();
	db.close(); 
	return list;
}
public List<Map<String, Object>> search_DJ_from(String date,String[] s,int flag,int op){
	String str="";
	for(int i=0;i<s.length;i++){
		if(i!=s.length-1){
			str=str+s[i]+",";
		}else{
			str=str+s[i];
		}
	}
	List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	String time = date;
	Cursor c = null;
	switch(op){
	case 0:
		c = db.rawQuery("select "+str+" from "+DataBaseHelper.TB_MoveM+" where "+DataBaseHelper.MVDT+">'"
				+time+"' and "+DataBaseHelper.mType+"="+flag+" order by "+
						DataBaseHelper.MVDT+" desc",null);
		break;
	case -1:
		c = db.rawQuery("select "+str+" from "+DataBaseHelper.TB_MoveM+" where "+DataBaseHelper.MVDT+">'"
				+time+"' and "+DataBaseHelper.mType+"="+flag+" and "+DataBaseHelper.DJType+"=-1"+" order by "+
						DataBaseHelper.MVDT+" desc",null);
		break;
	case 1:
		c = db.rawQuery("select "+str+" from "+DataBaseHelper.TB_MoveM+" where "+DataBaseHelper.MVDT+">'"
				+time+"' and "+DataBaseHelper.mType+"="+flag+" and "+DataBaseHelper.DJType+"=1"+" order by "+
						DataBaseHelper.MVDT+" desc",null);
		break;
		default:
			break;
	}
	c.moveToFirst();
	while (!c.isAfterLast()){
		 HashMap<String, Object> map = new HashMap<String, Object>();
		 for(int j=0;j<s.length;j++){
			 map.put(s[j], c.getString(j));
		 }
		 list.add(map);
		 c.moveToNext();
	 }
	c.close();
	db.close();
	return list;
}
public List<Map<String, Object>> search_DJ_from(String[] s,String dt1,String dt2,int flag,int op){
	String str="";
	for(int i=0;i<s.length;i++){
		if(i!=s.length-1){
			str=str+s[i]+",";
		}else{
			str=str+s[i];
		}
	}
	List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
//	dt1 = dt1+" 00:00:00";
	dt2 = dt2+" 24:00:00";
	Cursor c = null;
	switch(op){
	case 0:
		c = db.rawQuery("select "+str+" from "+DataBaseHelper.TB_MoveM+" where "+DataBaseHelper.MVDT+">='"
				+dt1+"' and "+DataBaseHelper.MVDT+"<'"+dt2+"' and "+DataBaseHelper.mType+"="+flag+
				" and "+DataBaseHelper.MVDH+"!=''"+" order by "+DataBaseHelper.MVDT+" desc",null);
		break;
	case -1:
		c = db.rawQuery("select "+str+" from "+DataBaseHelper.TB_MoveM+" where "+DataBaseHelper.MVDT+">='"
				+dt1+"' and "+DataBaseHelper.MVDT+"<'"+dt2+"' and "+DataBaseHelper.mType+"="+flag+" and "
				+DataBaseHelper.DJType+"=-1"+" and "+DataBaseHelper.MVDH+"!=''"+" order by "+DataBaseHelper.MVDT+" desc",null);
		break;
	case 1:
		c = db.rawQuery("select "+str+" from "+DataBaseHelper.TB_MoveM+" where "+DataBaseHelper.MVDT+">='"
				+dt1+"' and "+DataBaseHelper.MVDT+"<'"+dt2+"' and "+DataBaseHelper.mType+"="+flag+" and "
				+DataBaseHelper.DJType+"=1"+" and "+DataBaseHelper.MVDH+"!=''"+" order by "+DataBaseHelper.MVDT+" desc",null);
		break;
	case 2:
		c = db.rawQuery("select "+str+" from "+DataBaseHelper.TB_MoveM+" where "+DataBaseHelper.MVDT+">='"
				+dt1+"' and "+DataBaseHelper.MVDT+"<'"+dt2+"' and "+DataBaseHelper.DJType+"=1"+" and "+
				DataBaseHelper.MVDH+"!=''"+" order by "+DataBaseHelper.MVDT+" desc",null);
		break;
		default:
			break;
	}
	c.moveToFirst();
	while (!c.isAfterLast()){
		 HashMap<String, Object> map = new HashMap<String, Object>();
		 for(int j=0;j<s.length;j++){
			 map.put(s[j], c.getString(j));
		 }
		 list.add(map);
		 c.moveToNext();
	 }
	c.close();
	db.close();
	return list;
}
public List<Map<String, Object>> search_DJ_date(String date,String[] s,int flag,int op){
	String str="";
	for(int i=0;i<s.length;i++){
		if(i!=s.length-1){
			str=str+s[i]+",";
		}else{
			str=str+s[i];
		}
	}
	List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	String time = date;
	String time2 = date +" 24:00:00";
	Cursor c = null;
	switch(op){
	case 0:
		c = db.rawQuery("select "+str+" from "+DataBaseHelper.TB_MoveM+" where "+DataBaseHelper.MVDT+">'"
				+time+"'and "+DataBaseHelper.MVDT+"<'"+time2+"' and "+DataBaseHelper.mType+"="+flag+" order by "+
				DataBaseHelper.MVDT+" desc",null);
		break;
	case -1:
		c = db.rawQuery("select "+str+" from "+DataBaseHelper.TB_MoveM+" where "+DataBaseHelper.MVDT+">'"
				+time+"'and "+DataBaseHelper.MVDT+"<'"+time2+"' and "+DataBaseHelper.mType+"="+flag+" and "
				+DataBaseHelper.DJType+"=-1"+" order by "+
				DataBaseHelper.MVDT+" desc",null);
		break;
	case 1:
		c = db.rawQuery("select "+str+" from "+DataBaseHelper.TB_MoveM+" where "+DataBaseHelper.MVDT+">'"
				+time+"'and "+DataBaseHelper.MVDT+"<'"+time2+"' and "+DataBaseHelper.mType+"="+flag+" and "
				+DataBaseHelper.DJType+"=1"+" order by "+
				DataBaseHelper.MVDT+" desc",null);
		break;
		default:
			break;
	}
	c.moveToFirst();
	while (!c.isAfterLast()){
		 HashMap<String, Object> map = new HashMap<String, Object>();
		 for(int j=0;j<s.length;j++){
			 map.put(s[j], c.getString(j));
		 }
		 list.add(map);
		 c.moveToNext();
	 }
	c.close();
	 db.close(); 
	return list;
}

public void Update_DJtype(String djid,int type){
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	db.execSQL("update "+DataBaseHelper.TB_MoveM+" set "+DataBaseHelper.DJType+"="+type+" where "
			+DataBaseHelper.HPM_ID+" = '"+djid+"'");
	db.close();
}

public void Update_DDDJtype(String djid, int type) {
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	db.execSQL("update " + DataBaseHelper.TB_Order + " set "
			+ DataBaseHelper.Status + " = "+type+ " where " + DataBaseHelper.ID
			+ "='" + djid+"'");
	db.close();
}

public void Update_DDDJtype(String djid){
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	db.execSQL("update "+DataBaseHelper.TB_Order+" set "+DataBaseHelper.Status+"=-2"+" where "
			+DataBaseHelper.ID+"='"+djid+"'");
	db.close();
}
public void Update_DJtype_back(String djid){
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	db.execSQL("update "+DataBaseHelper.TB_MoveM+" set "+DataBaseHelper.DJType+"=-1"+" where "
			+DataBaseHelper.HPM_ID+"='"+djid+"'");
	db.close();
}

public void UpdateCurrKc_byhpid(String hpid,float msl,int mvdirect){
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	if(mvdirect==1){
		db.execSQL("update "+DataBaseHelper.TB_HP+" set "+DataBaseHelper.CurrKC+" = "+DataBaseHelper.CurrKC+" + "+msl+
				" where "+DataBaseHelper.ID+ "='"+hpid+"'");
	}else if(mvdirect==2){
		db.execSQL("update "+DataBaseHelper.TB_HP+" set "+DataBaseHelper.CurrKC+" = "+DataBaseHelper.CurrKC+" - "+msl+
				" where "+DataBaseHelper.ID+ "='"+hpid+"'");
	}
	db.close();
}

public List<Map<String, Object>> Gt_Moved(String djid,String[] s){
	String str="";
	for(int i=0;i<s.length;i++){
		if(i!=s.length-1){
			str=str+s[i]+",";
		}else{
			str=str+s[i];
		}
	}
	List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	Cursor c = db.rawQuery("select "+str+" from "+DataBaseHelper.TB_MoveD+" where "+DataBaseHelper.MID+"='"
			+djid+"'",null);
	c.moveToFirst();
	while (!c.isAfterLast()){
		 HashMap<String, Object> map = new HashMap<String, Object>();
		 for(int j=0;j<s.length;j++){
			 if(s[j].equals(DataBaseHelper.ATKC)||s[j].equals(DataBaseHelper.BTKC)||s[j].equals(DataBaseHelper.MSL)){
				 map.put(s[j], String.valueOf(c.getDouble(c.getColumnIndex(s[j]))));
			 }else{
				 map.put(s[j], c.getString(c.getColumnIndex(s[j])));
			 }
		 }
		 list.add(map);
		 c.moveToNext();
	 }
	 c.close();
	 db.close();
	return list;
}

public List<Map<String, Object>> Gt_Transd(String djid,String[] s){
	String str="";
	for(int i=0;i<s.length;i++){
		if(i!=s.length-1){
			str=str+s[i]+",";
		}else{
			str=str+s[i];
		}
	}
	List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	Cursor c = db.rawQuery("select "+str+" from "+DataBaseHelper.TB_TRANSD+" where "+DataBaseHelper.MID+"='"
			+djid+"'",null);
	c.moveToFirst();
	while (!c.isAfterLast()){
		 HashMap<String, Object> map = new HashMap<String, Object>();
		 for(int j=0;j<s.length;j++){
			 if(s[j].equals(DataBaseHelper.ATKC)||s[j].equals(DataBaseHelper.BTKC)||s[j].equals(DataBaseHelper.MSL)){
				 map.put(s[j], String.valueOf(c.getDouble(c.getColumnIndex(s[j]))));
			 }else{
				 map.put(s[j], c.getString(c.getColumnIndex(s[j])));
			 }
		 }
		 list.add(map);
		 c.moveToNext();
	 }
	 c.close();
	 db.close();
	return list;
}

public List<Map<String, Object>> Gt_Transm(String djid,String[] s){
	String str="";
	for(int i=0;i<s.length;i++){
		if(i!=s.length-1){
			str=str+s[i]+",";
		}else{
			str=str+s[i];
		}
	}
	List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	Cursor c = db.rawQuery("select "+str+" from "+DataBaseHelper.TB_TRANSM+" where "+DataBaseHelper.ID+"='"
			+djid+"'",null);
	c.moveToFirst();
	while (!c.isAfterLast()){
		 HashMap<String, Object> map = new HashMap<String, Object>();
		 for(int j=0;j<s.length;j++){
			 map.put(s[j], c.getString(j));
		 }
		 list.add(map);
		 c.moveToNext();
	 }
	c.close();
	db.close();
	return list;
}

public List<Map<String, Object>> GetckrkMoved_byIdTime(String hpid,String[] s,String fromdate,String todate){
	String str="";
	for(int i=0;i<s.length;i++){
		if(i!=s.length-1){
			str=str+s[i]+",";
		}else{
			str=str+s[i];
		}
	}
	List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	Cursor c = db.rawQuery("select "+str+" from "+DataBaseHelper.TB_MoveD+" left join "+DataBaseHelper.TB_MoveM+" on mid = hpm_id where "+DataBaseHelper.HPID+"='"
			+hpid+"' and "+DataBaseHelper.MVDDT+">='"+fromdate+" 00:00:00' and "+DataBaseHelper.MVDDT+"<'"
			+todate+" 23:59:59'  order by hpd_id ",null);
	c.moveToFirst();
	while (!c.isAfterLast()){
		 HashMap<String, Object> map = new HashMap<String, Object>();
		 for(int j=0;j<s.length;j++){
			 map.put(s[j], c.getString(j));
		 }
		 list.add(map);
		 c.moveToNext();
	 }
	 c.close();
	 db.close();
	return list;
}

public String Gt_Moved_znum(String djid){
	String str;
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	Cursor c = db.rawQuery("select sum(msl) from "+DataBaseHelper.TB_MoveD+
			" where "+DataBaseHelper.MID+"='"+djid+"'",null);
	if(c.moveToFirst()){
		str=c.getString(0);
	}else{
		str="-1";
	}
	c.close();
	db.close();
	return str;
}
public String Gt_Moved_zje(String djid){
	String str;
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	Cursor c = db.rawQuery("select sum(zj) from "+DataBaseHelper.TB_MoveD+
			" where "+DataBaseHelper.MID+"='"+djid+"'",null);
	if(c.moveToFirst()){
		str=c.getString(0);
	}else{
		str="-1";
	}
	c.close();
	db.close();
	return str;
}
public List<Map<String, Object>> Gt_Moved_details(String djid,String[] s){
	String str="";
	for(int i=0;i<s.length;i++){
		if(i!=s.length-1){
			str=str+s[i]+",";
		}else{
			str=str+s[i];
		}
	}
	List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	Cursor c = db.rawQuery("select "+str+" from "+DataBaseHelper.TB_MoveD+" as a,"+DataBaseHelper.TB_HP+
			" as b"+" where a.hpid=b.id and "+DataBaseHelper.MID+"='"
			+djid+"'",null);
	c.moveToFirst();
	while (!c.isAfterLast()){
		 HashMap<String, Object> map = new HashMap<String, Object>();
		 for(int j=0;j<s.length;j++){
			 map.put(s[j], c.getString(j));
		 }
		 list.add(map);
		 c.moveToNext();
	 }
	 c.close();
	 db.close();
	return list;
}
public List<Map<String, Object>> Gt_Order(String djid,String[] s){
	String str="";
	for(int i=0;i<s.length;i++){
		if(i!=s.length-1){
			str=str+s[i]+",";
		}else{
			str=str+s[i];
		}
	}
	List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	Cursor c = db.rawQuery("select "+str+" from "+DataBaseHelper.TB_Order+" where "+DataBaseHelper.ID+"='"
			+djid+"'",null);
	c.moveToFirst();
	while (!c.isAfterLast()){
		 HashMap<String, Object> map = new HashMap<String, Object>();
		 for(int j=0;j<s.length;j++){
			 map.put(s[j], c.getString(j));
		 }
		 list.add(map);
		 c.moveToNext();
	 }
	c.close();
	db.close();
	return list;
}
public List<Map<String, Object>> Gt_OrderDetails(String djid,String[] s){
	String str="";
	for(int i=0;i<s.length;i++){
		if(i!=s.length-1){
			str=str+s[i]+",";
		}else{
			str=str+s[i];
		}
	}
	List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	Cursor c = db.rawQuery("select "+str+" from "+DataBaseHelper.TB_OrderDetail+" where "+DataBaseHelper.orderID+"='"
			+djid+"'",null);
	c.moveToFirst();
	while (!c.isAfterLast()){
		 HashMap<String, Object> map = new HashMap<String, Object>();
		 for(int j=0;j<s.length;j++){
			 map.put(s[j], c.getString(j));
		 }
		 list.add(map);
		 c.moveToNext();
	 }
	c.close();
	db.close();
	return list;
}
public List<Map<String, Object>> Gt_Movem(String djid,String[] s){
	String str="";
	for(int i=0;i<s.length;i++){
		if(i!=s.length-1){
			str=str+s[i]+",";
		}else{
			str=str+s[i];
		}
	}
	List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	Cursor c = db.rawQuery("select "+str+" from "+DataBaseHelper.TB_MoveM+" where "+DataBaseHelper.HPM_ID+"='"
			+djid+"'",null);
	c.moveToFirst();
	while (!c.isAfterLast()){
		 HashMap<String, Object> map = new HashMap<String, Object>();
		 for(int j=0;j<s.length;j++){
			 map.put(s[j], c.getString(j));
		 }
		 list.add(map);
		 c.moveToNext();
	 }
	c.close();
	db.close();
	return list;
}
public List<Map<String, Object>> Gt_Movem(String djid,String[] s,SQLiteDatabase db){
	String str="";
	for(int i=0;i<s.length;i++){
		if(i!=s.length-1){
			str=str+s[i]+",";
		}else{
			str=str+s[i];
		}
	}
	List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
	Cursor c = db.rawQuery("select "+str+" from "+DataBaseHelper.TB_MoveM+" where "+DataBaseHelper.HPM_ID+"='"
			+djid+"'",null);
	c.moveToFirst();
	while (!c.isAfterLast()){
		 HashMap<String, Object> map = new HashMap<String, Object>();
		 for(int j=0;j<s.length;j++){
			 map.put(s[j], c.getString(j));
		 }
		 list.add(map);
		 c.moveToNext();
	 }
	c.close();
	return list;
}
public List<Map<String, Object>> Gt_Moved(String djid,String hpid,String[] s){
	String str="";
	for(int i=0;i<s.length;i++){
		if(i!=s.length-1){
			str=str+s[i]+",";
		}else{
			str=str+s[i];
		}
	}
	List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	Cursor c = db.rawQuery("select "+str+" from "+DataBaseHelper.TB_MoveD+" where "+DataBaseHelper.MID+"='"
			+djid+"' and "+DataBaseHelper.HPID+"='"+hpid+"'",null);
	c.moveToFirst();
	while (!c.isAfterLast()){
		 HashMap<String, Object> map = new HashMap<String, Object>();
		 for(int j=0;j<s.length;j++){
			 if(s[j].equals(DataBaseHelper.ATKC)||s[j].equals(DataBaseHelper.BTKC)||s[j].equals(DataBaseHelper.MSL)){
				 map.put(s[j], String.valueOf(c.getDouble(c.getColumnIndex(s[j]))));
			 }else{
				 map.put(s[j], c.getString(c.getColumnIndex(s[j])));
			 }
		 }
		 list.add(map);
		 c.moveToNext();
	 }
	c.close();
	db.close(); 
	return list;
}

public void Del_Conf(String[] ss){
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	for(int i=0;i<ss.length;i++){
		db.execSQL("delete from "+DataBaseHelper.TB_Conf+" where "+DataBaseHelper.GID+"='"
				+ss[i]+"'");
	}
	db.close();
}
public void Del_Conf(String[] ss,SQLiteDatabase db ){
	
	for(int i=0;i<ss.length;i++){
		db.execSQL("delete from "+DataBaseHelper.TB_Conf+" where "+DataBaseHelper.GID+"='"
				+ss[i]+"'");
	}
	
}
public void Del_Moved_id(String id){
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	db.execSQL("delete from "+DataBaseHelper.TB_MoveD+" where "+DataBaseHelper.HPD_ID+"='"
			+id+"'");
	db.close();
}

public void Del_Moved_id(String id,SQLiteDatabase db){
	db.execSQL("delete from "+DataBaseHelper.TB_MoveD+" where "+DataBaseHelper.HPD_ID+"='"
			+id+"'");
}
public void Del_Moved(String djid){
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	db.execSQL("delete from "+DataBaseHelper.TB_MoveD+" where "+DataBaseHelper.MID+"='"
			+djid+"'");
	db.close();
}
public void Del_Order(String djid){
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	db.execSQL("delete from "+DataBaseHelper.TB_Order+" where "+DataBaseHelper.ID+"='"
			+djid+"'");
	db.close();
}
public void Del_OrderDetails(String djid){
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	db.execSQL("delete from "+DataBaseHelper.TB_OrderDetail+" where "+DataBaseHelper.orderID+"='"
			+djid+"'");
	db.close();
}
public void Del_OrderDetails(String djid,String hpid){
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	db.execSQL("delete from "+DataBaseHelper.TB_OrderDetail+" where "+DataBaseHelper.orderID+"='"
			+djid+"' and "+DataBaseHelper.HPID+"='"+hpid+"'");
	db.close();
}

public List<Map<String, Object>> Gt_OrderDetails(String ddid, String hpid, String[] s) {
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
			+ DataBaseHelper.TB_OrderDetail + " where "
			+ DataBaseHelper.orderID + "='" + ddid + "' and "+DataBaseHelper.HPID+" = '"+hpid+"'", null);
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

public List<Map<String, Object>> Gt_DiaoboDetails(String djid, String hpid, String[] s) {
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
			+ DataBaseHelper.TB_TRANSD + " where "
			+ DataBaseHelper.MID + "='" + djid + "' and "+DataBaseHelper.HPID+" = '"+hpid+"'", null);
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

public void Del_Moved(String djid,String hpid){
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	db.execSQL("delete from "+DataBaseHelper.TB_MoveD+" where "+DataBaseHelper.MID+"='"
			+djid+"' and "+DataBaseHelper.HPID+"='"+hpid+"'");
	db.close();
}

/**删除该单据所有货品明细
 * */
public void DelAll_Moved(String djid){
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	db.execSQL("delete from "+DataBaseHelper.TB_MoveD+" where "+DataBaseHelper.MID+"='"
			+djid+"'");
	db.close();
}

/*获取某货品明细的出入库数量
 * */
public String getMoved_msl(String djid,String hpid){
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	Cursor c = db.rawQuery("select "+DataBaseHelper.MSL+" from "+DataBaseHelper.TB_MoveD+" where "+DataBaseHelper.MID+"='"+djid+
			"' and "+DataBaseHelper.HPID+" ='"+hpid+"'",null);
	String str="";
	if(c.moveToFirst()){
		str=c.getString(c.getColumnIndex(DataBaseHelper.MSL));
	}
	c.close();
	db.close();
	return str;
}

public void Del_DDdetails(String djid,String hpid){
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	db.execSQL("delete from "+DataBaseHelper.TB_OrderDetail+" where "+DataBaseHelper.orderID+"='"
			+djid+"' and "+DataBaseHelper.HPID+"='"+hpid+"'");
	db.close();
}

public void Del_transd(String djid,String hpid){
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	db.execSQL("delete from "+DataBaseHelper.TB_TRANSD+" where "+DataBaseHelper.MID+"='"
			+djid+"' and "+DataBaseHelper.HPID+"='"+hpid+"'");
	db.close();
}

public void Del_transd(String djid){
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	db.execSQL("delete from "+DataBaseHelper.TB_TRANSD+" where "+DataBaseHelper.MID+"='"
			+djid+"'");
	db.close();
}

public void Del_transm(String djid){
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	db.execSQL("delete from "+DataBaseHelper.TB_TRANSM+" where "+DataBaseHelper.ID+"='"
			+djid+"'");
	db.close();
}

public void Del_Movem(String djid){
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	db.execSQL("delete from "+DataBaseHelper.TB_MoveM+" where "+DataBaseHelper.HPM_ID+"='"
			+djid+"'");
	db.close(); 
}

public void Del_Movem(String djid,SQLiteDatabase db){
	db.execSQL("delete from "+DataBaseHelper.TB_MoveM+" where "+DataBaseHelper.HPM_ID+"='"
			+djid+"'");
}
public void Del_CKKC(String hpid,String ckid,SQLiteDatabase db){
	db.execSQL("delete from "+DataBaseHelper.TB_CKKC+" where "+DataBaseHelper.HPID+"='"
			+hpid+"' and "+DataBaseHelper.CKID+"='"+ckid+"'");
}
public void Del_CKKC(String hpid,int ckid){
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	db.execSQL("delete from "+DataBaseHelper.TB_CKKC+" where "+DataBaseHelper.HPID+"='"
			+hpid+"' and "+DataBaseHelper.CKID+"='"+ckid+"'");
	db.close(); 
}
public void Del_CP(String id){
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	db.execSQL("delete from "+DataBaseHelper.TB_Company+" where "+DataBaseHelper.ID+"='"
			+id+"'");
	db.close(); 
}
public void Del_CKKC(String id){
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	db.execSQL("delete from "+DataBaseHelper.TB_CKKC+" where "+DataBaseHelper.ID+"='"
			+id+"'  ");
	db.close(); 
}
public void Del_CK(String id){
	synchronized (id) {
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		db.execSQL("delete from "+DataBaseHelper.TB_CK+" where "+DataBaseHelper.ID+"='"
				+id+"'");
		db.close(); 
	}
}

public void Del_CK(){
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	db.execSQL("delete from "+DataBaseHelper.TB_CK );
	db.close(); 
}

public List<Map<String, Object>> Gt_movem_last(String[] s,String type){
	String str="";
	for(int i=0;i<s.length;i++){
		if(i!=s.length-1){
			str=str+s[i]+",";
		}else{
			str=str+s[i];
		}
	}
	List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	Cursor c = db.rawQuery("select "+str+" from "+DataBaseHelper.TB_MoveM+" where "+DataBaseHelper.mType+"='"
			+type+"' order by "+DataBaseHelper.MVDT+" desc ",null);
	c.moveToFirst();
	while (!c.isAfterLast()){
		 HashMap<String, Object> map = new HashMap<String, Object>();
		 for(int j=0;j<s.length;j++){
			 map.put(s[j], c.getString(j));
		 }
		 list.add(map);
		 c.moveToNext();
	 }
	c.close();
	db.close();
	return list;
}

public void Update_MoveM(String id,String dh,String dw,String jbr,String bz,String type){
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	db.execSQL("update "+DataBaseHelper.TB_MoveM+" set "+DataBaseHelper.MVDH+"='"+dh+"',"+DataBaseHelper.DWName+"='"
			+dw+"',"+DataBaseHelper.JBR+"='"+jbr+"',"+DataBaseHelper.BZ+"='"+bz+"',"+DataBaseHelper.actType+"='"+type+
			"'  where "+DataBaseHelper.HPM_ID+"='"+id+"'");
	db.close();
}
public void Update_Order(String id,String dh,String dw,String status,String bz,String lxr,String dirc,String reqdate,
		String dwid,String dep,String trades,String sqdt,String tel,String zje,String yfje,String syje,String sqr,String sqrid){
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	db.execSQL("update "+DataBaseHelper.TB_Order+" set "+DataBaseHelper.OrderIndex+"='"+dh+"',"
			+DataBaseHelper.DWName+"='"+dw+"',"+DataBaseHelper.Status+"='"+status+"',"+DataBaseHelper.BZ+"='"
			+bz+"',"+DataBaseHelper.LXR+"='"+lxr+"',"+DataBaseHelper.DWID+"='"+dwid+"',"+
			DataBaseHelper.DepName+"='"+dep+"',"+DataBaseHelper.Trades+"='"+trades+"',"
			+DataBaseHelper.Sqdt+"='"+sqdt+"',"+DataBaseHelper.TEL+"='"+tel+"',"+DataBaseHelper.ReqDate+"='"+reqdate+"',"+
			DataBaseHelper.zje+"='"+zje+"',"+DataBaseHelper.yfje+"='"+yfje+"',"+
			DataBaseHelper.syje+"='"+syje+"',"+DataBaseHelper.dirc+"='"+dirc
			+"',"+DataBaseHelper.sqr+"='"+sqr+"',"+DataBaseHelper.sqrID+"='"+sqrid+"'  where "+DataBaseHelper.ID+"='"+id+"'");
	db.close();
}
public void Update_MoveM(String id,String dh,String mvdt,String dw,String jbr,String bz,String type,String ck
		,int ckid,String details,String lrr,String lrsj,int mType,String dwid,String dep,String depid,String hpzjz,
		String res1,String res2,String res3){
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	db.execSQL("update "+DataBaseHelper.TB_MoveM+" set "+DataBaseHelper.MVDH+"='"+dh+"',"+DataBaseHelper.MVDT+"='"+mvdt+"',"
			+DataBaseHelper.DWName+"='"+dw+"',"+DataBaseHelper.JBR+"='"+jbr+"',"+DataBaseHelper.BZ+"='"
			+bz+"',"+DataBaseHelper.actType+"='"+type+"',"+DataBaseHelper.CKMC+"='"+ck+"',"+
			DataBaseHelper.CKID+"='"+String.valueOf(ckid)+"',"+DataBaseHelper.mType+"='"+String.valueOf(mType)+"',"
			+DataBaseHelper.Lrr+"='"+lrr+"',"+DataBaseHelper.Lrsj+"='"+lrsj+"',"+DataBaseHelper.HPzj+"='"+hpzjz+"',"+
			DataBaseHelper.RES1+"='"+res1+"',"+DataBaseHelper.RES2+"='"+res2+"',"+DataBaseHelper.RES3+"='"+res3+"',"+
			DataBaseHelper.Details+"='"+details+"',"+DataBaseHelper.DWID+"='"+dwid+"',"+
			DataBaseHelper.DepName+"='"+dep+"',"+DataBaseHelper.DepID+"='"+depid
			+"',"+DataBaseHelper.DJType+"='-1'  where "+DataBaseHelper.HPM_ID+"='"+id+"'");
	db.close();
}

public void Update_transm(String sckName,int sckid,String dckName,int dckid,String mvdh,String mvdt,String jbr,String hpnames,String bz,String hpzjz){
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	db.execSQL("update "+DataBaseHelper.TB_TRANSM+" set "+DataBaseHelper.sckName+" = '"+sckName+"',"+DataBaseHelper.sckid+" = "+sckid+","
			+DataBaseHelper.dckName+" = '"+dckName+"',"+DataBaseHelper.dckid+" = "+dckid+","+DataBaseHelper.mvdh+" ='"+mvdh+"',"
			+DataBaseHelper.mvdt+" ='"+mvdt+"',"+DataBaseHelper.JBR+" ='"+jbr+"',"+DataBaseHelper.hpnames+" ='"+hpnames+"',"+DataBaseHelper.BZ+" ='"+bz+"',"
			+DataBaseHelper.hpzjz+" ='"+hpzjz+"'");
	db.close();
}

public String GetHpzjz(String djid){
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	Cursor c = db.rawQuery("select sum(zj) from "+DataBaseHelper.TB_MoveD+" where "+DataBaseHelper.MID+"='"
			+djid+"'",null);
	c.moveToFirst();
	String str=c.getString(0);
	db.close();
	return str;
}
public void Update_MoveD_type(String id,String type){
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	db.execSQL("update "+DataBaseHelper.TB_MoveD+" set "+DataBaseHelper.MVType+"='"+type+
			"'  where "+DataBaseHelper.MID+"='"+id+"'");
	db.close();
}
public void insert_into_fromfile(String tb,String[] s1,String[] s2,SQLiteDatabase db){
	String str1="",str2="'";
//	for(int i=0;i<s1.length;i++){
//		if(i!=s1.length-1){
//			str1=str1+s1[i]+",";
//		}else{
//			str1=str1+s1[i];
//		}
//	}
//	for(int i=0;i<s2.length;i++){
//		if(i!=s2.length-1){
//			str2=str2+s2[i]+"','";
//		}else{
//			str2=str2+s2[i]+"'";
//		}
//	}
//	db.execSQL("insert into "+tb+" ("+str1+") values ("+str2+")");
	ContentValues values = new ContentValues();
	for(int i=0;i<s2.length;i++){
		values.put(s1[i], s2[i]);
	}
	db.insert(tb, null, values);
}
public void insert_into_fromfile(String tb,String[] s1,String[] s2,SQLiteDatabase db,String time){
//	String str1="",str2="'";
//	for(int i=0;i<s1.length;i++){
//		str1=str1+s1[i]+",";
//	}
//	str1=str1+DataBaseHelper.GXDate;
//	for(int i=0;i<s2.length;i++){
//		str2=str2+s2[i]+"','";
//	}
//	str2=str2+time+"'";
	ContentValues values = new ContentValues();
			for(int i=0;i<s2.length;i++){
				values.put(s1[i], s2[i]);
			}
			values.put(DataBaseHelper.GXDate, time);
	db.insert(tb, null, values);
//	db.execSQL("insert into "+tb+" ("+str1+") values ("+str2+")");
}
public boolean isCheck_bm(String bm,SQLiteDatabase db){
	Cursor c = db.rawQuery("select "+DataBaseHelper.ID+" from "+DataBaseHelper.TB_HP+" where "+DataBaseHelper.HPBM+"='"
			+bm+"'",null);
	if(c.moveToFirst()){
		c.close();
		return true;
	}else{
		c.close();
		return false;
	}
}
public boolean isCheck_id(String id,SQLiteDatabase db){
	Cursor c = db.rawQuery("select "+DataBaseHelper.ID+" from "+DataBaseHelper.TB_HP+" where "+DataBaseHelper.ID+"='"
			+id+"'",null);
	if(c.moveToFirst()){
		c.close();
		return true;
	}else{
		c.close();
		return false;
	}
}
public boolean isCK_exist(String ckmc){
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	Cursor c = db.rawQuery("select * from "+DataBaseHelper.TB_CK+" where "
				+DataBaseHelper.CKMC+"='"+ckmc+"'",null);
	if(c.moveToFirst()){
		c.close();
		db.close();
		return true;
	}else{
		c.close();
		db.close();
		return false;
	}
}
public void  update_ggtime(String time,String id,SQLiteDatabase db){
	db.execSQL("update  "+DataBaseHelper.TB_HP+" set "+DataBaseHelper.GXDate+"='"
			+time+"'  where "+DataBaseHelper.ID+"='"+id+"'");
}
public String Gt_hpid(String bm,SQLiteDatabase db){
	String str;
	Cursor c = db.rawQuery("select "+DataBaseHelper.ID+" from "+DataBaseHelper.TB_HP+" where "+DataBaseHelper.HPBM+"='"
			+bm+"'",null);
	c.moveToFirst();
	str=c.getString(0);
	c.close();
	return str;
}
public void del_tableContent(String table,SQLiteDatabase db){
	db.execSQL("delete from "+table);
}
public void del_repeat(String bm,SQLiteDatabase db){
	db.execSQL("delete from "+DataBaseHelper.TB_HP+" where "+DataBaseHelper.HPBM+"='"
			+bm+"'");
}
public void del_repeat_byid(String id,SQLiteDatabase db){
	db.execSQL("delete from "+DataBaseHelper.TB_HP+" where "+DataBaseHelper.ID+"='"
			+id+"'");
}
public void update_moved(String hpid,String old_id,SQLiteDatabase db){
	db.execSQL("update "+DataBaseHelper.TB_MoveD+" set"+DataBaseHelper.HPID+"='"
			+hpid+"' where "+DataBaseHelper.HPID+"='"+old_id+"'");
}
public List<Map<String, Object>> GetLB(String lbs,SQLiteDatabase db){
	List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
	Cursor c = db.rawQuery("select * from "+DataBaseHelper.TB_hplbTree+" where "+DataBaseHelper.LBS+"='"
			+lbs+"'",null);
	 c.moveToFirst();
	 while (!c.isAfterLast()){
		 HashMap<String, Object> map = new HashMap<String, Object>();
		 map.put("id", c.getString(0));
		 map.put("name", c.getString(1));
		 map.put("lev", c.getString(2));
		 map.put("pid", c.getString(3));
		 map.put("ord", c.getString(4));
		 map.put("index", c.getString(5));
		 list.add(map);
		 c.moveToNext();
	 }
	 c.close();
	return list;
}

public List<Map<String, Object>> search_by_hpid(String[] s,String hpid){
	String str="";
	for(int i=0;i<s.length;i++){
		if(i!=s.length-1){
			str=str+s[i]+",";
		}else{
			str=str+s[i];
		}
	}
	List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	Cursor c = db.rawQuery("select "+str+" from "+DataBaseHelper.TB_MoveD+" where "+DataBaseHelper.HPID+"='"
			+hpid+"'",null);
	c.moveToFirst();
	while (!c.isAfterLast()){
		 HashMap<String, Object> map = new HashMap<String, Object>();
		 for(int j=0;j<s.length;j++){
			 map.put(s[j], c.getString(j));
		 }
		 list.add(map);
		 c.moveToNext();
	 }
	c.close();
	db.close();
	return list;
}
public List<Map<String, Object>> search_by_hpid(String[] s,String hpid,SQLiteDatabase db ){
	String str="";
	for(int i=0;i<s.length;i++){
		if(i!=s.length-1){
			str=str+s[i]+",";
		}else{
			str=str+s[i];
		}
	}
	List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
	Cursor c = db.rawQuery("select "+str+" from "+DataBaseHelper.TB_MoveD+" where "+DataBaseHelper.HPID+"='"
			+hpid+"'",null);
	c.moveToFirst();
	while (!c.isAfterLast()){
		 HashMap<String, Object> map = new HashMap<String, Object>();
		 for(int j=0;j<s.length;j++){
			 map.put(s[j], c.getString(j));
		 }
		 list.add(map);
		 c.moveToNext();
	 }
	c.close();
	return list;
}
public List<Map<String, Object>> search_by_hpid(String[] s,String hpid,String dt1,String dt2){
	String str="";
	for(int i=0;i<s.length;i++){
		if(i!=s.length-1){
			str=str+s[i]+",";
		}else{
			str=str+s[i];
		}
	}
	dt1=dt1+" 00:00:00";
	dt2=dt2+" 24:00:00";
	List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	Cursor c = db.rawQuery("select "+str+" from "+DataBaseHelper.TB_MoveD+" where "+DataBaseHelper.HPID+"='"
			+hpid+"' and "+DataBaseHelper.MVDDATE+">'"+dt1+"' and "+DataBaseHelper.MVDDATE+"<'"+dt2+"' order by "
			+DataBaseHelper.MVDDATE+" desc ",null);
	c.moveToFirst();
	while (!c.isAfterLast()){
		 HashMap<String, Object> map = new HashMap<String, Object>();
		 for(int j=0;j<s.length;j++){
			 map.put(s[j], c.getString(j));
		 }
		 list.add(map);
		 c.moveToNext();
	 }
	c.close();
	db.close();
	return list;
}
public List<Map<String, Object>> search_by_hpid_moved_churu(String[] s,String hpid,String dt1,String dt2){
	String str="";
	for(int i=0;i<s.length;i++){
		if(i!=s.length-1){
			str=str+s[i]+",";
		}else{
			str=str+s[i];
		}
	}
	dt1=dt1+" 00:00:00";
	dt2=dt2+" 24:00:00";
	List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	Cursor c = db.rawQuery("select "+str+" from "+DataBaseHelper.TB_MoveD+" where "+DataBaseHelper.HPID+"='"
			+hpid+"'  and "+DataBaseHelper.MVDDATE+">'"+dt1+"' and "+
			DataBaseHelper.MVDDATE+"<'"+dt2+"' order by "
			+DataBaseHelper.MVDDATE+" desc ",null);
	c.moveToFirst();
	while (!c.isAfterLast()){
		 HashMap<String, Object> map = new HashMap<String, Object>();
		 for(int j=0;j<s.length;j++){
			 map.put(s[j], c.getString(j));
		 }
		 list.add(map);
		 c.moveToNext();
	 }
	db.close();
	return list;
}
public List<Map<String, Object>> search_moved(String[] s,int type,String dt1,String dt2){
	String str="";
	for(int i=0;i<s.length;i++){
		if(i!=s.length-1){
			str=str+s[i]+",";
		}else{
			str=str+s[i];
		}
	}
	dt1=dt1+" 00:00:00";
	dt2=dt2+" 24:00:00";
	List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	Cursor c = db.rawQuery("select "+str+" from "+DataBaseHelper.TB_MoveD+" where "+DataBaseHelper.MVDType+"="
			+type+" and "+DataBaseHelper.MVDDATE+">'"+dt1+"' and "+DataBaseHelper.MVDDATE+"<'"+dt2+"' order by "
			+DataBaseHelper.MVDDATE+" desc ",null);
	c.moveToFirst();
	while (!c.isAfterLast()){
		 HashMap<String, Object> map = new HashMap<String, Object>();
		 for(int j=0;j<s.length;j++){
			 map.put(s[j], c.getString(j));
		 }
		 list.add(map);
		 c.moveToNext();
	 }
	c.close();
	db.close();
	return list;
}
public void Update_MoveM(String detail,String id){
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	db.execSQL("update "+DataBaseHelper.TB_MoveM+" set "+DataBaseHelper.hpDetails+"='"+detail+"'  where "+DataBaseHelper.HPM_ID+
			"='"+id+"'");
	db.close();
}
public void Update_MoveM(String detail,String id,SQLiteDatabase db ){
	db.execSQL("update "+DataBaseHelper.TB_MoveM+" set "+DataBaseHelper.hpDetails+"='"+detail+"'  where "+DataBaseHelper.HPM_ID+
			"='"+id+"'");
}
public void Update_MoveD(String msl,String id,String hpid,String dj,String zj){
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	db.execSQL("update "+DataBaseHelper.TB_MoveD+" set "+DataBaseHelper.MSL+"='"+msl+"',"+DataBaseHelper.DJ+"='"+dj+"',"
			+DataBaseHelper.ZJ+"='"+zj+"'  where "+DataBaseHelper.MID+
			"='"+id+"' and "+DataBaseHelper.HPID+"='"+hpid+"'");
	db.close();
}
public void Update_MoveD(String djid,String dh,String type,int ckid){
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	db.execSQL("update "+DataBaseHelper.TB_MoveD+" set "+DataBaseHelper.DH+"='"+dh+"',"+DataBaseHelper.MVType
			+"='"+type+"',"+DataBaseHelper.CKID+"='"+String.valueOf(ckid)+"'  where "+DataBaseHelper.MID+
			"='"+djid+"'");
	db.close();
}
public void Update_MoveD(String msl,String id,String hpid){
	
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	db.execSQL("update "+DataBaseHelper.TB_MoveD+" set "+DataBaseHelper.MSL+"='"+msl+"'  where "+DataBaseHelper.MID+
			"='"+id+"' and "+DataBaseHelper.HPID+"='"+hpid+"'");
	db.close();
}

/**
 * 条码识别，判断条码是否部分匹配。
 * @return Map<String, Integer> 第0元素 为1是部分匹配，为0是完全匹配,为-1是部分匹配设置有误，如果是部分匹配后续还有4个元素，分别是货品开始、货品截至、数量开始、数量截至。
 * */
public Map<String, Integer> getTMMacth(){
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	Cursor cursor = db.rawQuery("select "+DataBaseHelper.ItemID+","+DataBaseHelper.ItemV+" from "+DataBaseHelper.TB_Conf+" where "+DataBaseHelper.GID+" = "+"'条码识别'"
			+ " and ("+DataBaseHelper.ItemID+"='匹配方式' or "+DataBaseHelper.ItemID+"='货品开始' or "+DataBaseHelper.ItemID+"='货品截止' or "+DataBaseHelper.ItemID+"='数量开始' or "
			+DataBaseHelper.ItemID+"='数量截止')", null);
	Map<String, Integer> MatchMap = new HashMap<String, Integer>();
	if(cursor.moveToFirst()){
		do{
			String itemIdString = cursor.getString(cursor.getColumnIndex(DataBaseHelper.ItemID));
			String itemVString = cursor.getString(cursor.getColumnIndex(DataBaseHelper.ItemV));
			if(itemIdString.equals("匹配方式")){
				if(itemVString.equals("部分匹配")){
					MatchMap.put("partMatch", 1);
				}else{
					MatchMap.put("partMatch", 0);
				}
			}else if(itemIdString.equals("货品开始")){
				if(itemVString.matches("^[1-9]+\\d*$")){//匹配是不是正整数
					MatchMap.put("hpks", Integer.parseInt(cursor.getString(cursor.getColumnIndex(DataBaseHelper.ItemV))));
				}else{
					MatchMap.put("partMatch", -1);
					db.close();
					return MatchMap;
				}
			}else if(itemIdString.equals("货品截止")){
				if(itemVString.matches("^[1-9]+\\d*$")){//匹配是不是正整数
					MatchMap.put("hpjz",Integer.parseInt(cursor.getString(cursor.getColumnIndex(DataBaseHelper.ItemV))));
				}else{
					MatchMap.put("partMatch", -1);
					db.close();
					return MatchMap;
				}
			}else if(itemIdString.equals("数量开始")){
				if(itemVString.matches("^[1-9]+\\d*$")){//匹配是不是正整数
					MatchMap.put("slks",Integer.parseInt(cursor.getString(cursor.getColumnIndex(DataBaseHelper.ItemV))));
				}else{
					MatchMap.put("partMatch", -1);
					db.close();
					return MatchMap;
				}
			}else if(itemIdString.equals("数量截止")){
				if(itemVString.matches("^[1-9]+\\d*$")){//匹配是不是正整数
					MatchMap.put("sljz",Integer.parseInt(cursor.getString(cursor.getColumnIndex(DataBaseHelper.ItemV))));
				}else{
					MatchMap.put("partMatch", -1);
					db.close();
					return MatchMap;
				}
			}
		}while(cursor.moveToNext());
	}else{
		MatchMap.put("partMatch", 0);
		db.close();
		return MatchMap;
	}
	db.close();
	return MatchMap;
}

public void saveLoginInfo(String company,String username,String password,String miwenpassword){
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();

	db.execSQL("delete from "+DataBaseHelper.TB_LOGIN+" where "+DataBaseHelper.company+"='"+company+"' and "+DataBaseHelper.username+"='"+username+"'");
	db.execSQL("insert into "+DataBaseHelper.TB_LOGIN+" ("+DataBaseHelper.company+","+DataBaseHelper.username+","+DataBaseHelper.password+","+DataBaseHelper.miwenpassword+
			") values ('"+company+"','"+username+"','"+password+"','"+miwenpassword+"')");
	db.close();
}

public List<Map<String,Object>> getLoginInfo_byCompany(String company){
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	Cursor c= null;
	if(company==null||company.equals("")){
		c=db.rawQuery("select * from "+DataBaseHelper.TB_LOGIN,null);
	}else{
		c=db.rawQuery("select * from "+DataBaseHelper.TB_LOGIN+" where "+DataBaseHelper.company+"='"+company+"'",null);
	}
	List<Map<String,Object>> mList = new ArrayList<Map<String,Object>>();
	if(c.moveToFirst()){
		do{
			Map<String,Object> map = new HashMap<String, Object>();
			int num=c.getColumnCount();
			for(int i=0;i<num;i++){
				map.put(c.getColumnName(i), c.getString(i));
			}
			mList.add(map);
		}while(c.moveToNext());
	}
	db.close();
	return mList;
}

public void deleteLoginInfo(String company,String username){
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	db.execSQL("delete from "+DataBaseHelper.TB_LOGIN+" where "+DataBaseHelper.company+"='"+company+"' and "+DataBaseHelper.username+"='"+username+"'");
	db.close();
}

public List<Map<String, Object>> getDJRes(){
	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
	List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
	Cursor c = db.rawQuery("select "+DataBaseHelper.ItemID+","+DataBaseHelper.ItemV+" from "+DataBaseHelper.TB_Conf
			+" where "+DataBaseHelper.GID+"='单据自定义字段' ",null);
	Map<String,Object> map = new HashMap<String, Object>();
	while(c.moveToNext()){
		map.put(c.getString(0), c.getString(1));
		list.add(map);
	}
	c.close();
	db.close();
	return list;
}

}
