package com.guantang.cangkuonline.database;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.guantang.cangkuonline.application.MyApplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataBaseCheckMethod {
	Context context;
    public DataBaseCheckMethod(Context mcontext){
		this.context=mcontext;
	}
	
    public List<Map<String, Object>> Gt_CheckList(String[] s,String mid,String type){
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
		Cursor c = db.rawQuery("select "+str+" from "+DataBaseHelper.TB_MoveD+" where "+DataBaseHelper.MVDType+"='"
				+type+"' and "+DataBaseHelper.MID+"='"+mid+"'",null);
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
    public List<Map<String, Object>> Gt_CheckList_details(String[] s,String mid,String type){
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
		Cursor c = db.rawQuery("select "+str+" from "+DataBaseHelper.TB_MoveD+" as b,"+DataBaseHelper.TB_HP+" as a where "
			+"a."+DataBaseHelper.ID+"=b."+DataBaseHelper.HPID+" and b."+DataBaseHelper.MVDType+"='"
				+type+"' and b."+DataBaseHelper.MID+"='"+mid+"'",null);
		 c.moveToFirst();
		 while (!c.isAfterLast()){
			 HashMap<String, Object> map = new HashMap<String, Object>();
			 for(int j=0;j<s.length;j++){
				 map.put(c.getColumnName(j), c.getString(j));
			 }
			 list.add(map);
			 c.moveToNext();
		 }
		 c.close();
		 db.close();
		return list;
    }
    public List<Map<String, Object>> queryList_CheckList_details(String[] s,String mid,String type,String text){
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
		Cursor c = db.rawQuery("select "+str+" from "+DataBaseHelper.TB_MoveD+" as b,"+DataBaseHelper.TB_HP+" as a where "
			+"a."+DataBaseHelper.ID+"=b."+DataBaseHelper.HPID+" and b."+DataBaseHelper.MVDType+"='"
				+type+"' and b."+DataBaseHelper.MID+"='"+mid+"'and (a."+DataBaseHelper.HPMC+" LIKE '%"+text+"%' OR a."+
					DataBaseHelper.HPTM+" LIKE '%"+text+"%' OR a."+DataBaseHelper.GGXH+" LIKE '%"+text+"%' OR a."+
				 DataBaseHelper.HPBM+" LIKE '%"+text+"%')",null);
		 c.moveToFirst();
		 while (!c.isAfterLast()){
			 HashMap<String, Object> map = new HashMap<String, Object>();
			 for(int j=0;j<s.length;j++){
				 map.put(c.getColumnName(j), c.getString(j));
			 }
			 list.add(map);
			 c.moveToNext();
		 }
		 c.close();
		 db.close();
		return list;
    }
    public boolean isCheck_Moved(String hpid,String djid){
    	
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery("select * from "+DataBaseHelper.TB_MoveD+" where "+DataBaseHelper.HPID+"='"
				+hpid+"' and "+DataBaseHelper.MID+"='"+djid+"'",null);
		if(c.moveToFirst()){
			c.close();
			db.close();
			return false;
		}else{
			c.close();
			db.close();
			return true;
		}
		
    }
    public List<Map<String, Object>> Gt_notChose_ck_hpInfo(String[] s,String mid,String type){
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
		Cursor c = db.rawQuery("select "+str+" from "+DataBaseHelper.TB_MoveD+" where "+DataBaseHelper.MVDType+"='"
				+type+"' and "+DataBaseHelper.MID+"='"+mid+"'",null);
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
    
    public void update_movem(String columnName,String columnValue,String djid){
    	
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		db.execSQL("update "+DataBaseHelper.TB_MoveM+" set "+columnName+" = '"+columnValue+"' where "+DataBaseHelper.HPM_ID+" = '"+djid+"'" );
		db.close();
    }
    
    public void update_order(String columnName,String columnValue,String ddid){
    	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		db.execSQL("update "+DataBaseHelper.TB_Order+" set "+columnName+" = '"+columnValue+"' where "+DataBaseHelper.ID+" = '"+ddid+"'" );
		db.close();
    }
    
    public void update_transm(String columnName,String columnValue,String djid){
    	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		db.execSQL("update "+DataBaseHelper.TB_TRANSM+" set "+columnName+" = '"+columnValue+"' where "+DataBaseHelper.ID+" = '"+djid+"'" );
		db.close();
    }
    
    public void Check_save_movem(String djid,String mvdt,String details,String dh,String jbr,String bz,String ck,
    		int ckid,String details2,String lrr,String lrsj,int mType){
    	
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		db.execSQL("update "+DataBaseHelper.TB_MoveM+" set "+DataBaseHelper.MVDT+"='"+mvdt+"',"+DataBaseHelper.hpDetails+"='"+details+"',"
				+DataBaseHelper.MVDH+"='"+dh+"',"+DataBaseHelper.JBR+"='"+jbr+"',"+DataBaseHelper.BZ+"='"
				+bz+"',"+DataBaseHelper.CKMC+"='"+ck+"',"+DataBaseHelper.CKID+"='"+String.valueOf(ckid)
				+"',"+DataBaseHelper.Details+"='"+details2+"',"+DataBaseHelper.Lrr+"='"+lrr+"',"+DataBaseHelper.Lrsj+"='"+lrsj+"',"+DataBaseHelper.mType+"='"+mType+"' where "+
				DataBaseHelper.HPM_ID+"='"+djid+"'");
		Cursor cursor=null;
		db.close();
    }
    public void Check_saveCK_movem(String djid,String dh,String jbr,String bz,String ck,String Depname,String DwName,String actType,int ckid,String depid,String dwid,int mType){
    	
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		db.execSQL("update "+DataBaseHelper.TB_MoveM+" set "
				+DataBaseHelper.MVDH+"='"+dh+"',"+DataBaseHelper.JBR+"='"+jbr+"',"+DataBaseHelper.BZ+"='"
				+bz+"',"+DataBaseHelper.CKMC+"='"+ck+"',"+DataBaseHelper.DepName+"='"+Depname+"',"+DataBaseHelper.DWName+"='"+
				DwName+"',"+DataBaseHelper.actType+"='"+actType+"',"+DataBaseHelper.CKID+"='"+String.valueOf(ckid)+"',"+DataBaseHelper.DepID+"='"+depid+"',"
				+DataBaseHelper.DWID+"='"+dwid+"',"+DataBaseHelper.mType+"='"+mType+"' where "+DataBaseHelper.HPM_ID+"='"+djid+"'");
		db.close();
    }
    public void Check_savePDCK_movem(String djid,String dh,String jbr,String bz,String ck,int ckid){
    	
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		db.execSQL("update "+DataBaseHelper.TB_MoveM+" set "
				+DataBaseHelper.MVDH+"='"+dh+"',"+DataBaseHelper.JBR+"='"+jbr+"',"+DataBaseHelper.BZ+"='"
				+bz+"',"+DataBaseHelper.CKMC+"='"+ck+"',"+DataBaseHelper.CKID+"='"+String.valueOf(ckid)
				+"' where "+DataBaseHelper.HPM_ID+"='"+djid+"'");
		db.close();
    }
    
    public void Check_save_moved(String djid,String dh){
    	
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		db.execSQL("update "+DataBaseHelper.TB_MoveD+" set "+DataBaseHelper.DH+"='"+dh+"'"
					+" where "+DataBaseHelper.MID+"='"+djid+"'");
		db.close();
    }
    public void Check_moved_before_num(String djid,String hpid,float num){
    	
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		db.execSQL("update "+DataBaseHelper.TB_MoveD+" set "+DataBaseHelper.BTKC+"='"+num+"'"
					+" where "+DataBaseHelper.MID+"='"+djid+"' and "+DataBaseHelper.HPID+"='"+hpid+"'");
		db.close();
    }
    public void Check(String djid,String hpid,String num,String before_num,String time,int ckid){
    	float f;
    	if(before_num.equals("")){
    		before_num="0";
    	}
    	f=Float.parseFloat(before_num)-Float.parseFloat(num);
    	
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		if(f<=0){
			db.execSQL("insert into "+DataBaseHelper.TB_MoveD+" ("+DataBaseHelper.BTKC+" , "+
					DataBaseHelper.ATKC+","+DataBaseHelper.MVDType+", "+DataBaseHelper.MVDDATE
					+","+DataBaseHelper.MVDirect+","+DataBaseHelper.MSL+","+DataBaseHelper.MVType+","
					+DataBaseHelper.MID+","+DataBaseHelper.HPID+","+DataBaseHelper.CKID+") values ( '"+before_num+"','"
					+num+"',0,'"+time+"',1,'"+String.valueOf(Math.abs(f))+"','盘赢','"+djid+"','"+hpid+"',"+ckid+")");
		}else{
			db.execSQL("insert into "+DataBaseHelper.TB_MoveD+" ("+DataBaseHelper.BTKC+" , "+
					DataBaseHelper.ATKC+","+DataBaseHelper.MVDType+", "+DataBaseHelper.MVDDATE
					+","+DataBaseHelper.MVDirect+","+DataBaseHelper.MSL+","+DataBaseHelper.MVType
					+","+DataBaseHelper.MID+","+DataBaseHelper.HPID+","+DataBaseHelper.CKID+") values ( '"+before_num+"','"
					+num+"',0,'"+time+"',2,'"+String.valueOf(f)+"','盘亏','"+djid+"','"+hpid+"',"+ckid+")");
		}
		db.close();
    }
    public String GtAmount_checked(String djid){
    	String str="0";
    	
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery("SELECT COUNT(*) FROM "+DataBaseHelper.TB_MoveD+" where "+DataBaseHelper.MID
				+"='"+djid+"'",null);
		c.moveToFirst();
		str=c.getString(0);
		c.close();
		db.close();
    	return str;
    }
    public String GtAmount_DDchecked(String djid){
    	String str="0";
    	
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery("SELECT COUNT(*) FROM "+DataBaseHelper.TB_OrderDetail+" where "+DataBaseHelper.orderID
				+"='"+djid+"'",null);
		c.moveToFirst();
		str=c.getString(0);
		c.close();
		db.close();
    	return str;
    }
    public String GtAmount_DDmsl(String djid){
    	String str="0";
    	
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery("SELECT SUM(sl) FROM "+DataBaseHelper.TB_OrderDetail+" where "+DataBaseHelper.orderID
				+"='"+djid+"'",null);
		c.moveToFirst();
		str=c.getString(0);
		c.close();
		db.close();
		if(str==null||str.equals("")){
			str="0";
		}
    	return str;
    }
    public String GtAmount_DDzje(String djid){
    	String str="0";
    	
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery("SELECT SUM(zj) FROM "+DataBaseHelper.TB_OrderDetail+" where "+DataBaseHelper.orderID
				+"='"+djid+"'",null);
		c.moveToFirst();
		str=c.getString(0);
		c.close();
		db.close();
		if(str==null||str.equals("")){
			str="0";
		}
    	return str;
    }
    public String GtAmount_check(String djid){
    	String str="0";
    	
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery("SELECT COUNT(*) FROM "+DataBaseHelper.TB_MoveD+" where "+DataBaseHelper.MID+"='"
				+djid+"'",null);
		c.moveToFirst();
		str=c.getString(0);
		c.close();
		db.close();
    	return str;
    }
    public String GtAmount_notcheck(String djid){
    	String str="0";
    	
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery("SELECT COUNT(*) FROM "+DataBaseHelper.TB_MoveD+" where "+DataBaseHelper.MID
				+"="+djid+" and "+DataBaseHelper.MVDType+"=4",null);
		c.moveToFirst();
		str=c.getString(0);
		c.close();
		db.close();
    	return str;
    }
    public String GtAmount_profit(String djid){
    	String str="0";
    	
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery("SELECT COUNT(*) FROM "+DataBaseHelper.TB_MoveD+" where "+DataBaseHelper.BTKC+"<"
				+DataBaseHelper.ATKC+" and "+DataBaseHelper.MID+"='"+djid+"'",null);
		c.moveToFirst();
		str=c.getString(0);
		c.close();
		db.close();
    	return str;
    }
    public String GtAmount_lose(String djid){
    	String str="0";
    	
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery("SELECT COUNT(*) FROM "+DataBaseHelper.TB_MoveD+" where "+DataBaseHelper.BTKC+">"
				+DataBaseHelper.ATKC+" and "+DataBaseHelper.MID+"='"+djid+"'",null);
		c.moveToFirst();
		str=c.getString(0);
		c.close();
		db.close();
    	return str;
    }
    public String Gt_check_djid(){
    	String str="";
    	
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery("SELECT "+DataBaseHelper.MID+" FROM "+DataBaseHelper.TB_MoveD+" where "+
				DataBaseHelper.MVDType+"=4",null);
		c.moveToFirst();
		str=c.getString(0);
		c.close();
		db.close();
    	return str;
    }
    public void del_notcheck(){
    	
    	SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
    	db.execSQL("delete from "+DataBaseHelper.TB_MoveD+" where "+DataBaseHelper.MVDType+"=4");
    	db.close();
    }
    public List<Map<String, Object>> Gt_Res(){
		List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
		synchronized (list) {
			SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
			Cursor c = db.rawQuery("select "+DataBaseHelper.ItemID+","+DataBaseHelper.ItemV+" from "+DataBaseHelper.TB_Conf
					+" where "+DataBaseHelper.GID+"='自定义字段' ",null);
			HashMap<String, Object> map = new HashMap<String, Object>();
			while (c.moveToNext()){
				map.put(c.getString(0), c.getString(1));
				list.add(map);
				
			}
			int n=list.size();
			for(int i=0;i<6-n;i++){
				map.put("", "");
				list.add(map);
			}
			c.close();
			db.close();
		}
		return list;
    }
    public void Insert_Res(String str1,String str2,String str3){
		
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		db.execSQL("insert into "+DataBaseHelper.TB_Conf+" ("+DataBaseHelper.GID+","+DataBaseHelper.ItemID+","
					+DataBaseHelper.ItemV+") values ( '"+str1+"','"+str2+"','"+str3+"' )");
		db.close();
    }
    public void Del_Res(){
		
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		db.execSQL("delete from "+DataBaseHelper.TB_Conf
				+" where "+DataBaseHelper.GID+"='自定义字段' ");
		db.close();
    }
    public String Gt_Ord(String str){
    	String str1;
		
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery("select "+DataBaseHelper.Ord+" from "+DataBaseHelper.TB_Conf
				+" where "+DataBaseHelper.GID+"='"+str+"' order by "+DataBaseHelper.Ord+" desc",null);
		if(c.moveToFirst()){
			str1=c.getString(0);
		}else{
			str1="";
		}
		c.close();
		db.close();
		return str1;
    }
    public List<Map<String, Object>> Gt_Type(String str,String str1){
    	List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
		
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery("select "+DataBaseHelper.ItemV+" from "+DataBaseHelper.TB_Conf
				+" where "+DataBaseHelper.GID+"='"+str+"' order by "+DataBaseHelper.Ord,null);
		 c.moveToFirst();
		 while (!c.isAfterLast()){
			 HashMap<String, Object> map = new HashMap<String, Object>();
			 map.put(str1, c.getString(0));
			 list.add(map);
			 c.moveToNext();
		 }
		 c.close();
		 db.close();
		return list;
    }
    public int Gt_Vision(){
    	int v;
		
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery("select "+DataBaseHelper.ItemV+" from "+DataBaseHelper.TB_Conf
				+" where "+DataBaseHelper.GID+"='移动端版本信息' ",null);
		
		if(c!=null&&c.moveToFirst()==true){
			try{
				v=Integer.parseInt(c.getString(0));
			}catch(Exception e){
				v=-2;
			}
		}else{
			v=-1;
		}
		c.close();
		db.close();
		return v;
    }
    public void Insert_Version(int v){
		
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		db.execSQL("insert into "+DataBaseHelper.TB_Conf+" ("+DataBaseHelper.GID+","
					+DataBaseHelper.ItemV+") values ( '移动端版本信息','"+v+"' )");
		db.close();
    }
    public void Update_Version(int v){
		
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		db.execSQL("update  "+DataBaseHelper.TB_Conf+" set "
					+DataBaseHelper.ItemV+"='"+v+"' where "+DataBaseHelper.GID+"='移动端版本信息'");
		db.close();
    }
    public void Del_Type(String str,String str1){
		
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		db.execSQL("delete from "+DataBaseHelper.TB_Conf
				+" where "+DataBaseHelper.GID+"='"+str+"' and "+DataBaseHelper.ItemV+"='"+str1+"'");
		db.close();
    }
    public boolean ischeck_Name(String str,String str1){
		
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery("select * from "+DataBaseHelper.TB_Conf
				+" where "+DataBaseHelper.GID+"='"+str+"' and "+DataBaseHelper.ItemV+"='"+str1+"'",null);
		if(c.moveToFirst()){
			c.close();
			db.close();
			return false;
		}else{
			c.close();
			db.close();
			return true;
		}
    }

	public String GtAmount_msl(String djid){
		String str="0";
		
		SQLiteDatabase db = DataBaseImport.getInstance(context).getReadableDatabase();
		Cursor c = db.rawQuery("SELECT SUM(msl) FROM "+DataBaseHelper.TB_MoveD+" where "+DataBaseHelper.MID
				+"='"+djid+"'",null);
		c.moveToFirst();
		str=c.getString(0);
		c.close();
		db.close();
		if(str==null||str.equals("")){
			str="0";
		}
		return str;
	}
}
