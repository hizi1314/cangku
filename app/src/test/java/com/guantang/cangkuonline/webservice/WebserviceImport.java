package com.guantang.cangkuonline.webservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;
import org.myksoap2.SoapEnvelope;
import org.myksoap2.serialization.SoapObject;
import org.myksoap2.serialization.SoapSerializationEnvelope;
import org.myksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.StringDef;
import android.util.Log;

import com.guantang.cangkuonline.application.MyApplication;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.helper.DecimalsHelper;
import com.guantang.cangkuonline.helper.EditHelper;
import com.guantang.cangkuonline.helper.PwdHelper;
import com.guantang.cangkuonline.helper.RightsHelper;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;

public class WebserviceImport {
	public static boolean isOpenNetwork(Context context) {
	    ConnectivityManager connManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
	    if(networkInfo != null && networkInfo.isConnected()) {
	        return true;
	    }
	    return false;
	}
	
	public static boolean login(String name,String pwd){
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.CheckAuthorize);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("UserName", name); 
	    soapObject.addProperty("password", pwd); 
	    soapObject.addProperty("SerId", WebserviceHelper.serid); 
	    soapObject.addProperty("LoginFlag", WebserviceHelper.loginflag);
        envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        envelope.encodingStyle="UTF-8";
        HttpTransportSE httpTranstation=new HttpTransportSE(WebserviceHelper.URL+WebserviceHelper.GtStorageWebservice,15000);
        httpTranstation.debug=true;
        
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.CheckAuthorize, envelope);
            Object result=envelope.getResponse();
            if(result!=null){
            	return  result.toString().equals("true");
            }else{
            	return false;
            } 
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } 
		
	}
	
	public static int loginNew(String name,String pwd,String IMEI){
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.CheckAuthorize_1_0);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("UserName", name); 
	    soapObject.addProperty("password", pwd); 
	    soapObject.addProperty("IMEI", IMEI);
	    soapObject.addProperty("SerId", WebserviceHelper.serid); 
	    soapObject.addProperty("LoginFlag", WebserviceHelper.loginflag);
        envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        envelope.encodingStyle="UTF-8";
        HttpTransportSE httpTranstation=new HttpTransportSE(WebserviceHelper.URL+WebserviceHelper.GtStorageWebservice,15000);
        httpTranstation.debug=true;
        
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.CheckAuthorize_1_0, envelope);
            Object result=envelope.getResponse();
            if(result!=null){
            	return  Integer.parseInt(result.toString());
            }else{
            	return -1;
            } 
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -1;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -1;
        } 
		
	}
	
	/**还未使用
	 * */
	public static boolean Check_OrderIndex(String orderIndex){
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.Check_OrderIndex);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("orderIndex", orderIndex);
	    soapObject.addProperty("SerId", WebserviceHelper.serid); 
	    soapObject.addProperty("LoginFlag", WebserviceHelper.loginflag);
        envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        envelope.encodingStyle="UTF-8";
        HttpTransportSE httpTranstation=new HttpTransportSE(WebserviceHelper.URL+WebserviceHelper.GtStorageWebservice,15000);
        httpTranstation.debug=true;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.Check_OrderIndex, envelope);
            Object result=envelope.getResponse();
            if(result!=null){
            	return  result.toString().equals("true");
            }else{
            	return false;
            } 
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } 
		
	}
	public static String CheckIServer(String dwname){
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.CheckServer);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("dwname", dwname); 
        envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        envelope.encodingStyle="UTF-8";
        HttpTransportSE httpTranstation=new HttpTransportSE(WebserviceHelper.URL+WebserviceHelper.GtStorageWebservice,15000);
        httpTranstation.debug=true;
        
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.CheckServer, envelope);
            Object result=envelope.getResponse();
            if(result!=null){
            	return  result.toString();
            }else{
            	return "-104";
            } 
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "-103";
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "-103";
        } 
		
	}
	public static int AddReg_1_0(String dwname,int UserNumber,int MobileNumber,String contact,String regFrom,String phone,String serviceUrl,SharedPreferences mSharedPreferences){
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.AddReg_1_0);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("dwname", dwname); 
	    soapObject.addProperty("UserNumber", UserNumber);
	    soapObject.addProperty("MobileNumber", MobileNumber);
	    soapObject.addProperty("contact", contact);
	    soapObject.addProperty("regFrom",regFrom);
	    soapObject.addProperty("phone", phone);
        envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        envelope.encodingStyle="UTF-8";
        HttpTransportSE httpTranstation=new HttpTransportSE(serviceUrl+WebserviceHelper.GtStorageWebservice,15000);
        httpTranstation.debug=true;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.AddReg_1_0, envelope);
            Object result=envelope.getResponse();
            if(result!=null){
            	return  Integer.parseInt(result.toString());
            }else{
            	return -2;
            } 
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -2;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -2;
        } 
		
	}
	/**还未使用,核查窜号对不对
	 * */
	public static int CheckIMEI(String IMEI,String name){
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.CheckIMEI_1_0);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("IMEI", IMEI); 
	    soapObject.addProperty("username", name);
	    soapObject.addProperty("SerId", WebserviceHelper.serid); 
	    soapObject.addProperty("LoginFlag", WebserviceHelper.loginflag);
        envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        envelope.encodingStyle="UTF-8";
        HttpTransportSE httpTranstation=new HttpTransportSE(WebserviceHelper.URL+WebserviceHelper.GtStorageWebservice,15000);
        httpTranstation.debug=true;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.CheckIMEI_1_0, envelope);
            Object result=envelope.getResponse();
            if(result!=null){
            	return  Integer.parseInt(result.toString());
            }else{
            	return -2;
            } 
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -2;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -2;
        } 
		
	}
	/**还未使用
	 * */
	public static int delMEI(String IMEI,SharedPreferences mSharedPreferences){
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.delIMEI);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("IMEI", IMEI); 
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
        envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        envelope.encodingStyle="UTF-8";
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,8000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.delIMEI, envelope);
            Object result=envelope.getResponse();
            if(result!=null){
            	return  Integer.parseInt(result.toString());
            }else{
            	return -4;
            } 
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -5;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -6;
        } 
		
	}
	public static List<Map<String, Object>> Gt_Rights(String name,String pwd,String[] str){
		List<Map<String, Object>> ls=new ArrayList<Map<String, Object>>();
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.Gt_Rights);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("username", name);
	    soapObject.addProperty("pwd", pwd);
	    soapObject.addProperty("SerId", WebserviceHelper.serid);
	    soapObject.addProperty("LoginFlag", WebserviceHelper.loginflag);
        envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        envelope.encodingStyle="UTF-8";
        HttpTransportSE httpTranstation=new HttpTransportSE(WebserviceHelper.URL+"/"+WebserviceHelper.GtStorageWebservice,15000);
        httpTranstation.debug=true;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.Gt_Rights, envelope);
            SoapObject result=( SoapObject) envelope.getResponse();
            if(result!=null){
            	for(int   i=0;i <result.getPropertyCount();i++){
            		SoapObject   soapChilds  =(SoapObject)result.getProperty(i);
            		HashMap<String, Object> map = new HashMap<String, Object>();
            		for(int   j=0;j <str.length;j++){
            			try{
            				String s=soapChilds.getProperty(str[j]).toString();
							if(s.equals("anyType{}")){
								map.put(str[j],  "");
							}else{
								map.put(str[j],  s);
							}
            			}catch(Exception e){
            				map.put(str[j],  "");
            			}
            		}
            		ls.add(map);
            	}
            	return ls;
            
            }else{
            	return ls;
            } 
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ls;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ls;
        } 
		
	}
	public static String Gt_Name(String name,String pwd){
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.Gt_Name);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("username", name);
	    soapObject.addProperty("pwd", pwd);
	    soapObject.addProperty("SerId", WebserviceHelper.serid); 
	    soapObject.addProperty("LoginFlag", WebserviceHelper.loginflag);
        envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        envelope.encodingStyle="UTF-8";
        HttpTransportSE httpTranstation=new HttpTransportSE(WebserviceHelper.URL+"/"+WebserviceHelper.GtStorageWebservice,15000);
        httpTranstation.debug=true;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.Gt_Name, envelope);
            Object result=envelope.getResponse();
            if(result!=null){
            	
            	return result.toString();
            }else{
            	return null;
            } 
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } 
		
	}
	public static int AddDJ(String[] values_movem,String[] name_movem,
			String[] values_moved,String[] name_moved,String methodname,SharedPreferences mSharedPreferences){
		JSONObject obj = new JSONObject();
		JSONArray json_values = new JSONArray();
		JSONArray json_name = new JSONArray();
		JSONArray json_values2 = new JSONArray();
		JSONArray json_name2 = new JSONArray();
		for(int i=0;i<values_movem.length;i++){
			json_values.put(values_movem[i]);
		}
		for(int i=0;i<name_movem.length;i++){
			json_name.put(name_movem[i]);
		}
		for(int i=0;i<values_moved.length;i++){
			json_values2.put(values_moved[i]);
		}
		for(int i=0;i<name_moved.length;i++){
			json_name2.put(name_moved[i]);
		}
		try {
			obj.put("movem_values",json_values );
			obj.put("movem_name",json_name );
			obj.put("moved_values",json_values2);
			obj.put("moved_name",json_name2 );
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String str=obj.toString();
		SoapObject soapObject=new SoapObject("http://guantang.net/",methodname);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("jsonstring", str);
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+methodname, envelope);
            Object result=envelope.getResponse();
            if(result!=null){
            	return  Integer.parseInt(result.toString());
            }else{
            	return -10;
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -11;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -12;
        } 
	}
	public static int AddChuKu(String[] values_movem,String[] name_movem,
			String[] values_moved,String[] name_moved,boolean isck,SharedPreferences mSharedPreferences){
		JSONObject obj = new JSONObject();
		JSONArray json_values = new JSONArray();
		JSONArray json_name = new JSONArray();
		JSONArray json_values2 = new JSONArray();
		JSONArray json_name2 = new JSONArray();
		for(int i=0;i<values_movem.length;i++){
			json_values.put(values_movem[i]);
		}
		for(int i=0;i<name_movem.length;i++){
			json_name.put(name_movem[i]);
		}
		for(int i=0;i<values_moved.length;i++){
			json_values2.put(values_moved[i]);
		}
		for(int i=0;i<name_moved.length;i++){
			json_name2.put(name_moved[i]);
		}
		try {
			obj.put("movem_values",json_values );
			obj.put("movem_name",json_name );
			obj.put("moved_values",json_values2);
			obj.put("moved_name",json_name2 );
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String str=obj.toString();
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.AddChuKu_1_0);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("jsonstring", str);
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    soapObject.addProperty("isck", isck);
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.AddChuKu_1_0, envelope);
            Object result=envelope.getResponse();
            if(result!=null){
            	return  Integer.parseInt(result.toString());
            }else{
            	return -10;
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
        	
            e.printStackTrace();
            return -11;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -12;
        } 
	}
	public static int AddDDDJ(String[] values_movem,String[] name_movem,
			String[] values_moved,String[] name_moved,String dh,SharedPreferences mSharedPreferences){
		JSONObject obj = new JSONObject();
		JSONArray json_values = new JSONArray();
		JSONArray json_name = new JSONArray();
		JSONArray json_values2 = new JSONArray();
		JSONArray json_name2 = new JSONArray();
		for(int i=0;i<values_movem.length;i++){
			json_values.put(values_movem[i]);
		}
		for(int i=0;i<name_movem.length;i++){
			json_name.put(name_movem[i]);
		}
		for(int i=0;i<values_moved.length;i++){
			json_values2.put(values_moved[i]);
		}
		for(int i=0;i<name_moved.length;i++){
			json_name2.put(name_moved[i]);
		}
		try {
			obj.put("order_values",json_values );
			obj.put("order_name",json_name );
			obj.put("details_values",json_values2);
			obj.put("details_name",json_name2 );
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String str=obj.toString();
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.AddDDDJ);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("jsonstring", str);
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    soapObject.addProperty("dh", dh);
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.AddDDDJ, envelope);
            Object result=envelope.getResponse();
            if(result!=null){
            	
            	return  Integer.parseInt(result.toString());
            }else{
            	return -2;
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
        	
            e.printStackTrace();
            return -2;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -2;
        } 
	}
	public static int AddHP(String[] values,String[] name,String HPBM,SharedPreferences mSharedPreferences){
		JSONObject obj = new JSONObject();
		JSONArray json_values = new JSONArray();
		JSONArray json_name = new JSONArray();
		for(int i=0;i<values.length;i++){
			json_values.put(values[i]);
		}
		for(int i=0;i<name.length;i++){
			json_name.put(name[i]);
		}
		try {
			obj.put("values",json_values );
			obj.put("name",json_name );
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String str=obj.toString();
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.AddHP);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("jsonstring", str);
	    soapObject.addProperty("HPTBM", HPBM);
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.AddHP, envelope);
            Log.v("tag", "执行新增货品中");
            Object result=envelope.getResponse();
            if(result!=null){
            	return  Integer.parseInt(result.toString());
            }else{
            	return -2;
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
        	
            e.printStackTrace();
            return -2;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -2;
        } 
	}
	
	public static String AddHP_1_0(String[] values,String[] name,String HPTM,SharedPreferences mSharedPreferences){
		JSONObject obj = new JSONObject();
		JSONArray json_values = new JSONArray();
		JSONArray json_name = new JSONArray();
		for(int i=0;i<values.length;i++){
			json_values.put(values[i]);
		}
		for(int i=0;i<name.length;i++){
			json_name.put(name[i]);
		}
		try {
			obj.put("values",json_values );
			obj.put("name",json_name );
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String str=obj.toString();
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.AddHP_1_0);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("jsonstring", str);
	    soapObject.addProperty("HPTBM", HPTM);
	    String ss1 = mSharedPreferences.getString(ShareprefenceBean.SERID, "");
	    soapObject.addProperty("SerId", ss1); 
	    int ss2 = mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1);
	    soapObject.addProperty("LoginFlag", ss2);
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        String ss3 = mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice;
        HttpTransportSE httpTranstation=new HttpTransportSE(ss3,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.AddHP_1_0, envelope);
            Object result=envelope.getResponse();
            if(result!=null){
            	return  result.toString();
            }else{
            	return "";
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        } 
	}
	
	public static int AddImage(String base64string,String name,int id,SharedPreferences mSharedPreferences){
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.AddImage);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("base64string", base64string);
	    soapObject.addProperty("name", name);
	    soapObject.addProperty("id", id);
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,100000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.AddImage, envelope);
            Object result=envelope.getResponse();
            if(result!=null&&!result.toString().equals("anyType{}")){
            	return  Integer.parseInt(result.toString());
            }else{
            	return -3;
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
        	
            e.printStackTrace();
            return -3;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -3;
        } 
	}
	public static String GetImage(String imgpath,SharedPreferences mSharedPreferences){
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.GetImage);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("imgpath", imgpath);
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,100000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.GetImage, envelope);
            Object result=envelope.getResponse();
            if(result!=null){
            	return  result.toString();
            }else{
            	return "-3";
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "-3";
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "-3";
        } 
	}
	public static int Get_ispass(int status,SharedPreferences mSharedPreferences){
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.Get_ispass);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("status", status); 
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.Get_ispass, envelope);
            Object result=envelope.getResponse();
            if(result!=null){
            	
            	return Integer.parseInt(result.toString());
            }else{
            	return -3;
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
        	
            e.printStackTrace();
            return -3;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -3;
        } 
	}
	public static int GetMaxID_tbm_movem(int status,String dt1,String dt2,SharedPreferences mSharedPreferences){
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.GetMaxID_tbm_movem);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("status", status); 
	    soapObject.addProperty("dt1", dt1);
	    soapObject.addProperty("dt2", dt2);
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.GetMaxID_tbm_movem, envelope);
            Object result=envelope.getResponse();
            if(result!=null){
            	
            	return Integer.parseInt(result.toString());
            }else{
            	return -3;
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
        	
            e.printStackTrace();
            return -3;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -3;
        } 
	}
	public static int Get_Total(String methodname,int ckid,SharedPreferences mSharedPreferences){
		SoapObject soapObject=new SoapObject("http://guantang.net/",methodname);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("ckid", ckid); 
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+methodname, envelope);
            Object result=envelope.getResponse();
            if(result!=null){
            	
            	return Integer.parseInt(result.toString());
            }else{
            	return -1;
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
        	
            e.printStackTrace();
            return -1;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -1;
        } 
	}
	public static int GetHp_Total_Leave(String id,SharedPreferences mSharedPreferences){
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.GetHp_Total_Leave);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("id", id);
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.GetHp_Total_Leave, envelope);
            Object result=envelope.getResponse();
            if(result!=null){
            	
            	return Integer.parseInt(result.toString());
            }else{
            	return -1;
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -1;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -1;
        } 
	}
	public static int GetMaxID_LB(SharedPreferences mSharedPreferences){
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.GetMaxID_LB);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1)); 
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.GetMaxID_LB, envelope);
            Object result=envelope.getResponse();
            if(result!=null){
            	
            	return Integer.parseInt(result.toString());
            }else{
            	return -1;
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
        	
            e.printStackTrace();
            return -1;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -1;
        } 
	}
	public static int GetMaxID(int ckid,int op,String index,SharedPreferences mSharedPreferences){
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.GetMaxID);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11); 
	    soapObject.addProperty("ckid", ckid); 
	    soapObject.addProperty("op",op);
	    soapObject.addProperty("index", index); 
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.GetMaxID, envelope);
            Object result=envelope.getResponse();
            if(result!=null){
            	
            	return Integer.parseInt(result.toString());
            }else{
            	return -1;
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
        	
            e.printStackTrace();
            return -1;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -1;
        } 
	}
	public static int GetMinID(int ckid,int op,String index,SharedPreferences mSharedPreferences){
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.GetMinID);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11); 
	    soapObject.addProperty("ckid", ckid);
	    soapObject.addProperty("op",op);
	    soapObject.addProperty("index", index);
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.GetMinID, envelope);
            Object result=envelope.getResponse();
            if(result!=null){
            	
            	return Integer.parseInt(result.toString());
            }else{
            	return -1;
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
        	
            e.printStackTrace();
            return -1;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -1;
        } 
	}
	public static int GetMaxID_search(int ckid,String search,SharedPreferences mSharedPreferences){
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.GetMaxID_search);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11); 
	    soapObject.addProperty("ckid", ckid); 
	    soapObject.addProperty("search",search);
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.GetMaxID_search, envelope);
            Object result=envelope.getResponse();
            if(result!=null){
            	
            	return Integer.parseInt(result.toString());
            }else{
            	return -1;
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
        	
            e.printStackTrace();
            return -1;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -1;
        } 
	}
	public static int GetMinID_search(int ckid,String search,SharedPreferences mSharedPreferences){
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.GetMinID_search);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11); 
	    soapObject.addProperty("ckid", ckid); 
	    soapObject.addProperty("search",search);
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.GetMinID_search, envelope);
            Object result=envelope.getResponse();
            if(result!=null){
            	
            	return Integer.parseInt(result.toString());
            }else{
            	return -1;
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
        	
            e.printStackTrace();
            return -1;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -1;
        } 
	}
	public static float[] GetHP_CurrKC(String hpid,int ckid,SharedPreferences mSharedPreferences){
		float[] ff=new float[2];
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.GetHP_CurrKC);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("ckid", ckid); 
	    soapObject.addProperty("hpid", hpid);
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.GetHP_CurrKC, envelope);
            Object result=envelope.getResponse();
            if(result!=null){
            	if(Float.parseFloat(result.toString())==52730.81){
            		ff[0]=-2;
            	}else{
            		ff[0]=1;//1表示ff[1]数是正常的，-2表示头验证错误，-1表示ff[1]的数是异常的
                	ff[1]=Float.parseFloat(result.toString());
            	}
            	return ff;
            }else{
            	ff[0]=-1;
            	return ff;
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
        	ff[0]=-1;
            e.printStackTrace();
            return ff;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
        	ff[0]=-1;
            e.printStackTrace();
            return ff;
        } 
	}
	public static List<Map<String, Object>> GetHpInfo_top(int top ,String id ,int direction,int ckid,String[] str
			,String[] str1,SharedPreferences mSharedPreferences){
		List<Map<String, Object>> ls=new ArrayList<Map<String, Object>>();
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.GetHpInfo_top);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("top", top);
	    soapObject.addProperty("id", id);
	    soapObject.addProperty("ckid", ckid);
	    soapObject.addProperty("direction", direction); 
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.GetHpInfo_top, envelope);
            SoapObject result=(SoapObject) envelope.getResponse();
            if(result!=null){
            	for(int   i=0;i <result.getPropertyCount();i++){
            		SoapObject   soapChilds ;
            		if(direction>0){
            			soapChilds   =(SoapObject)result.getProperty(i);
            		}else{
            			soapChilds   =(SoapObject)result.getProperty(result.getPropertyCount()-1-i);
            		}
            		HashMap<String, Object> map = new HashMap<String, Object>();
            		for(int   j=0;j <str.length;j++){
            			try{
            				if(str[j].equals(DataBaseHelper.KCSL)||str[j].equals(DataBaseHelper.CurrKC)){
            					
            					map.put(str[j],
            					DecimalsHelper.Transfloat(Double.parseDouble(soapChilds.getProperty(str1[j]).toString()),8));
            				}else{
            					map.put(str[j],  soapChilds.getProperty(str1[j]).toString());
            				}
            			}catch(Exception e){
            				if(str[j].equals(DataBaseHelper.KCSL)){
            					map.put(str[j],  "0");
            				}else{
            					map.put(str[j],  "");
            				}
            			}
            		}
            		ls.add(map);
            	}
            	return ls;
            }else{
            	return ls;
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ls;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ls;
        } 
	}
	public static List<Map<String, Object>> GetHpInfo_top_search(int top ,String id ,int direction,int ckid
			,String search,String[] str,String[] str1,SharedPreferences mSharedPreferences){
		List<Map<String, Object>> ls=new ArrayList<Map<String, Object>>();
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.GetHpInfo_top_search);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("top", top); 
	    soapObject.addProperty("id", id); 
	    soapObject.addProperty("ckid", ckid); 
	    soapObject.addProperty("direction", direction);
	    soapObject.addProperty("search", search);
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.GetHpInfo_top_search, envelope);
            SoapObject result=(SoapObject) envelope.getResponse();
            if(result!=null){
            	for(int   i=0;i <result.getPropertyCount();i++){
            		SoapObject   soapChilds ;
            		if(direction>0){
            			soapChilds   =(SoapObject)result.getProperty(i);
            		}else{
            			soapChilds   =(SoapObject)result.getProperty(result.getPropertyCount()-1-i);
            		}
            		HashMap<String, Object> map = new HashMap<String, Object>();
            		for(int   j=0;j <str.length;j++){
            			try{
            				if(str[j].equals(DataBaseHelper.KCSL)||str[j].equals(DataBaseHelper.CurrKC)){
            					
            					map.put(str[j],
            					DecimalsHelper.Transfloat(Double.parseDouble(soapChilds.getProperty(str1[j]).toString()),MyApplication.getInstance().getNumPoint()));
            				}else{
            					map.put(str[j],  soapChilds.getProperty(str1[j]).toString());
            				}
            			}catch(Exception e){
            				if(str[j].equals(DataBaseHelper.KCSL)){
            					map.put(str[j],  "0");
            				}else{
            					map.put(str[j],  "");
            				}
            			}
            		}
            		ls.add(map);
            	}
            	return ls;
            }else{
            	return ls;
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ls;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ls;
        } 
	}
	public static List<Map<String, Object>> GetHpInfoLBS_top(int top ,String id ,int direction,int ckid,String index,
			String[] str,String[] str1,SharedPreferences mSharedPreferences){
		List<Map<String, Object>> ls=new ArrayList<Map<String, Object>>();
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.GetHpInfoLBS_top);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("top", top); 
	    soapObject.addProperty("id", id); 
	    soapObject.addProperty("ckid", ckid); 
	    soapObject.addProperty("direction", direction); 
	    soapObject.addProperty("index", index);
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.GetHpInfoLBS_top, envelope);
            SoapObject result=(SoapObject) envelope.getResponse();
            if(result!=null){
            	for(int   i=0;i <result.getPropertyCount();i++){
            		SoapObject   soapChilds ;
            		if(direction>0){
            			soapChilds   =(SoapObject)result.getProperty(i);
            		}else{
            			soapChilds   =(SoapObject)result.getProperty(result.getPropertyCount()-1-i);
            		}
            		HashMap<String, Object> map = new HashMap<String, Object>();
            		for(int   j=0;j <str.length;j++){
            			try{
            				if(str[j].equals(DataBaseHelper.KCSL)||str[j].equals(DataBaseHelper.CurrKC)){
            					
            					map.put(str[j],
            					DecimalsHelper.Transfloat(Double.parseDouble(soapChilds.getProperty(str1[j]).toString()),MyApplication.getInstance().getNumPoint()) );
            				}else{
            					map.put(str[j],  soapChilds.getProperty(str1[j]).toString());
            				}
            			}catch(Exception e){
            				if(str[j].equals(DataBaseHelper.KCSL)){
            					map.put(str[j],  "0");
            				}else{
            					map.put(str[j],  "");
            				}
            			}
            		}
            		ls.add(map);
            	}
            	return ls;
            }else{
            	return ls;
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ls;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ls;
        } 
	}
	public static List<Map<String, Object>> GetHpInfoSX_top(int top ,String id ,int direction,int ckid,String[] str
			,String[] str1,SharedPreferences mSharedPreferences){
		List<Map<String, Object>> ls=new ArrayList<Map<String, Object>>();
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.GetHpInfoSX_top);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("top", top); 
	    soapObject.addProperty("id", id); 
	    soapObject.addProperty("ckid", ckid); 
	    soapObject.addProperty("direction", direction); 
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.GetHpInfoSX_top, envelope);
            SoapObject result=(SoapObject) envelope.getResponse();
            if(result!=null){
            	for(int   i=0;i <result.getPropertyCount();i++){
            		SoapObject   soapChilds ;
            		if(direction>0){
            			soapChilds   =(SoapObject)result.getProperty(i);
            		}else{
            			soapChilds   =(SoapObject)result.getProperty(result.getPropertyCount()-1-i);
            		}
            		HashMap<String, Object> map = new HashMap<String, Object>();
            		for(int   j=0;j <str.length;j++){
            			try{
            				if(str[j].equals(DataBaseHelper.KCSL)||str[j].equals(DataBaseHelper.CurrKC)){
            					map.put(str[j],
            					DecimalsHelper.Transfloat(Double.parseDouble(soapChilds.getProperty(str1[j]).toString()),MyApplication.getInstance().getNumPoint()));
            				}else{
            					map.put(str[j],  soapChilds.getProperty(str1[j]).toString());
            				}
            			}catch(Exception e){
            				if(str[j].equals(DataBaseHelper.KCSL)){
            					map.put(str[j],  "0");
            				}else{
            					map.put(str[j],  "");
            				}
            			}
            		}
            		ls.add(map);
            	}
            	return ls;
            }else{
            	return ls;
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ls;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ls;
        } 
	}
	public static List<Map<String, Object>> GetHpInfoXX_top(int top ,String id ,int direction,int ckid,String[] str
			,String[] str1,SharedPreferences mSharedPreferences){
		List<Map<String, Object>> ls=new ArrayList<Map<String, Object>>();
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.GetHpInfoXX_top);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("top", top); 
	    soapObject.addProperty("id", id); 
	    soapObject.addProperty("ckid", ckid); 
	    soapObject.addProperty("direction", direction); 
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.GetHpInfoXX_top, envelope);
            SoapObject result=(SoapObject) envelope.getResponse();
            if(result!=null){
            	for(int   i=0;i <result.getPropertyCount();i++){
            		SoapObject   soapChilds ;
            		if(direction>0){
            			soapChilds   =(SoapObject)result.getProperty(i);
            		}else{
            			soapChilds   =(SoapObject)result.getProperty(result.getPropertyCount()-1-i);
            		}
            		HashMap<String, Object> map = new HashMap<String, Object>();
            		for(int   j=0;j <str.length;j++){
            			try{
            				if(str[j].equals(DataBaseHelper.KCSL)||str[j].equals(DataBaseHelper.CurrKC)){
            					map.put(str[j],
            					DecimalsHelper.Transfloat(Double.parseDouble(soapChilds.getProperty(str1[j]).toString()),MyApplication.getInstance().getNumPoint()) );
            				}else{
            					map.put(str[j],  soapChilds.getProperty(str1[j]).toString());
            				}
            			}catch(Exception e){
            				if(str[j].equals(DataBaseHelper.KCSL)){
            					map.put(str[j],  "0");
            				}else{
            					map.put(str[j],  "");
            				}
            			}
            		}
            		ls.add(map);
            	}
            	return ls;
            }else{
            	return ls;
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ls;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ls;
        } 
	}
	
	public static List<Map<String, Object>> GetHpInfo_byid(String id ,int ckid,String[] str
			,String[] str1,SharedPreferences mSharedPreferences){
		List<Map<String, Object>> ls=new ArrayList<Map<String, Object>>();
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.GetHpInfo_ById);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("hpid", id);
	    soapObject.addProperty("ckid", ckid);
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.GetHpInfo_ById, envelope);
            SoapObject result=(SoapObject) envelope.getResponse();
            if(result!=null){
            	for(int   i=0;i <result.getPropertyCount();i++){
            		SoapObject   soapChilds  =(SoapObject)result.getProperty(i);
            		HashMap<String, Object> map = new HashMap<String, Object>();
            		for(int   j=0;j <str.length;j++){
            			try{
            				if(RightsHelper.isHaveRight(RightsHelper.system_cw,mSharedPreferences)==true){
            					try{
                    				if(str[j].equals(DataBaseHelper.KCSL)||str[j].equals(DataBaseHelper.CurrKC)){
                    					
                    					map.put(str[j],
                    					DecimalsHelper.Transfloat(Double.parseDouble(soapChilds.getProperty(str1[j]).toString()),8));//小数点设置为8，主要是防止在盘点时有小数位数限制。货品列表已经对数字进行限制了
                    				}else{
                    					map.put(str[j],  soapChilds.getProperty(str1[j]).toString());
                    				}
                    			}catch(Exception e){
                    				if(str[j].equals(DataBaseHelper.KCSL)){
                    					map.put(str[j],  "0");
                    				}else{
                    					map.put(str[j],  "");
                    				}
                    			}
							}else{
								try{
		            				if(str[j].equals(DataBaseHelper.KCSL)||str[j].equals(DataBaseHelper.CurrKC)){
		            					map.put(str[j],DecimalsHelper.Transfloat(Double.parseDouble(soapChilds.getProperty(str1[j]).toString()),8));//小数点设置为8，主要是防止在盘点时有小数位数限制。
		            				}if(str[j].equals(DataBaseHelper.RKCKJ)||str[j].equals(DataBaseHelper.CKCKJ)
											||str[j].equals(DataBaseHelper.CKCKJ2)||str[j].equals(DataBaseHelper.KCJE)){
										map.put(str[j],  "");
									}else{
		            					map.put(str[j],  soapChilds.getProperty(str1[j]).toString());
		            				}
		            			}catch(Exception e){
		            				if(str[j].equals(DataBaseHelper.KCSL)){
		            					map.put(str[j],  "0");
		            				}else{
		            					map.put(str[j],  "");
		            				}
		            			}
							}
            			}catch(Exception e){
            				map.put(str[j],  "");
            			}
            		}
            		ls.add(map);
            	}
            	return ls;
            }else{
            	return ls;
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ls;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ls;
        } 
	}
	
	public static List<Map<String, Object>> GetHpAllInfocksl_ByCkId_1_0(String id ,int ckid,String[] str
			,String[] str1,SharedPreferences mSharedPreferences){
		List<Map<String, Object>> ls=new ArrayList<Map<String, Object>>();
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.GetHpAllInfocksl_ByCkId_1_0);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("hpid", id);
	    soapObject.addProperty("ckid", ckid);
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.GetHpAllInfocksl_ByCkId_1_0, envelope);
            SoapObject result=(SoapObject) envelope.getResponse();
            if(result!=null){
            	for(int   i=0;i <result.getPropertyCount();i++){
            		SoapObject   soapChilds  =(SoapObject)result.getProperty(i);
            		HashMap<String, Object> map = new HashMap<String, Object>();
            		for(int   j=0;j <str.length;j++){
            			try{
            				if(RightsHelper.isHaveRight(RightsHelper.system_cw,mSharedPreferences)==true){
            					try{
                    				if(str[j].equals(DataBaseHelper.KCSL)||str[j].equals(DataBaseHelper.CurrKC)){
                    					
                    					map.put(str[j],
                    					DecimalsHelper.Transfloat(Double.parseDouble(soapChilds.getProperty(str1[j]).toString()),8));//小数点设置为8，主要是防止在盘点时有小数位数限制。货品列表已经对数字进行限制了
                    				}else{
                    					map.put(str[j],  soapChilds.getProperty(str1[j]).toString());
                    				}
                    			}catch(Exception e){
                    				if(str[j].equals(DataBaseHelper.KCSL)){
                    					map.put(str[j],  "0");
                    				}else{
                    					map.put(str[j],  "");
                    				}
                    			}
							}else{
								try{
		            				if(str[j].equals(DataBaseHelper.KCSL)||str[j].equals(DataBaseHelper.CurrKC)){
		            					map.put(str[j],DecimalsHelper.Transfloat(Double.parseDouble(soapChilds.getProperty(str1[j]).toString()),8));//小数点设置为8，主要是防止在盘点时有小数位数限制。
		            				}if(str[j].equals(DataBaseHelper.RKCKJ)||str[j].equals(DataBaseHelper.CKCKJ)
											||str[j].equals(DataBaseHelper.CKCKJ2)||str[j].equals(DataBaseHelper.KCJE)){
										map.put(str[j],  "");
									}else{
		            					map.put(str[j],  soapChilds.getProperty(str1[j]).toString());
		            				}
		            			}catch(Exception e){
		            				if(str[j].equals(DataBaseHelper.KCSL)){
		            					map.put(str[j],  "0");
		            				}else{
		            					map.put(str[j],  "");
		            				}
		            			}
							}
            			}catch(Exception e){
            				map.put(str[j],  "");
            			}
            		}
            		ls.add(map);
            	}
            	return ls;
            }else{
            	return ls;
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ls;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ls;
        } 
	}
	
	public static List<Map<String, Object>> GetDep(String[] str
			,String[] str1,SharedPreferences mSharedPreferences){
		List<Map<String, Object>> ls=new ArrayList<Map<String, Object>>();
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.GetDep);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.GetDep, envelope);
            SoapObject result=(SoapObject) envelope.getResponse();
            if(result!=null){
            	for(int  i=0;i <result.getPropertyCount();i++){
            		SoapObject   soapChilds  =(SoapObject)result.getProperty(i);
            		HashMap<String, Object> map = new HashMap<String, Object>();
            		for(int   j=0;j <str.length;j++){
            			try{
            				map.put(str[j],  soapChilds.getProperty(str1[j]).toString());
            			}catch(Exception e){
            				map.put(str[j],  "");
            			}
            		}
            		ls.add(map);
            	}
            	return ls;
            }else{
            	return ls;
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ls;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ls;
        } 
	}
	public static List<Map<String, Object>> GetCK(String[] str
			,String[] str1,SharedPreferences mSharedPreferences){
		List<Map<String, Object>> ls=new ArrayList<Map<String, Object>>();
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.GetCK);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.GetCK, envelope);
            SoapObject result=(SoapObject) envelope.getResponse();
            if(result!=null){
            	for(int   i=0;i <result.getPropertyCount();i++){
            		SoapObject   soapChilds  =(SoapObject)result.getProperty(i);
            		HashMap<String, Object> map = new HashMap<String, Object>();
            		for(int   j=0;j <str.length;j++){
            			try{
            				map.put(str[j],  soapChilds.getProperty(str1[j]).toString());
            			}catch(Exception e){
            				map.put(str[j],  "");
            			}
            		}
            		ls.add(map);
            	}
            	return ls;
            }else{
            	return ls;
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ls;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ls;
        } 
	}
	
	public static List<Map<String, Object>> GetHPLBTree(int top ,int PID,String id,String[] str,String[] str1,SharedPreferences mSharedPreferences){
		List<Map<String, Object>> ls=new ArrayList<Map<String, Object>>();
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.GetHPLBTree);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("top", top); 
	    soapObject.addProperty("PID", PID); 
	    soapObject.addProperty("id", id);
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.GetHPLBTree, envelope);
            SoapObject result=(SoapObject) envelope.getResponse();
            if(result!=null&&!result.toString().equals("anyType{}")){
            	for(int   i=0;i <result.getPropertyCount();i++){
            		SoapObject   soapChilds ;
            		soapChilds   =(SoapObject)result.getProperty(i);
            		HashMap<String, Object> map = new HashMap<String, Object>();
            		for(int   j=0;j <str.length;j++){
            			try{
            				map.put(str[j],  soapChilds.getProperty(str1[j]).toString());
            			}catch(Exception e){
            				map.put(str[j],  "");
            			}
            		}
            		ls.add(map);
            	}
            	return ls;
            }else{
            	return ls;
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ls;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ls;
        } 
	}
	
	public static List<Map<String, Object>> GetConf(String GID,String ItemID,String[] str,String[] str1,SharedPreferences mSharedPreferences){
		List<Map<String, Object>> ls=new ArrayList<Map<String, Object>>();
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.GetConf);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("GID", GID); 
	    soapObject.addProperty("ItemID", ItemID); 
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.GetConf, envelope);
            SoapObject result=(SoapObject) envelope.getResponse();
            if(result!=null){
            	for(int   i=0;i <result.getPropertyCount();i++){
            		SoapObject   soapChilds ;
            		soapChilds   =(SoapObject)result.getProperty(i);
            		HashMap<String, Object> map = new HashMap<String, Object>();
            		for(int   j=0;j <str.length;j++){
            			try{
            				map.put(str[j],  soapChilds.getProperty(str1[j]).toString());
            			}catch(Exception e){
            				map.put(str[j],  "");
            			}
            		}
            		ls.add(map);
            	}
            	return ls;
            }else{
            	
                return ls;
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ls;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ls;
        } 
	}
	public static List<Map<String, Object>> GetIOType(String[] str,String[] str1,SharedPreferences mSharedPreferences){
		List<Map<String, Object>> ls=new ArrayList<Map<String, Object>>();
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.GetIOType);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.GetIOType, envelope);
            SoapObject result=(SoapObject) envelope.getResponse();
            if(result!=null){
            	for(int   i=0;i <result.getPropertyCount();i++){
            		SoapObject   soapChilds ;
            		soapChilds   =(SoapObject)result.getProperty(i);
            		HashMap<String, Object> map = new HashMap<String, Object>();
            		for(int   j=0;j <str.length;j++){
            			try{
            				map.put(str[j],  soapChilds.getProperty(str1[j]).toString());
            			}catch(Exception e){
            				map.put(str[j],  "");
            			}
            		}
            		ls.add(map);
            	}
            	return ls;
            }else{
            	
                return ls;
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ls;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ls;
        } 
	}
	public static List<Map<String, Object>> GetHPLB(int top,String id,String[] str,String[] str1,SharedPreferences mSharedPreferences){
		List<Map<String, Object>> ls=new ArrayList<Map<String, Object>>();
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.GetLB);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    soapObject.addProperty("ID",id); 
	    soapObject.addProperty("top", top); 
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.GetLB, envelope);
            SoapObject result=(SoapObject) envelope.getResponse();
            if(result!=null){
            	for(int   i=0;i <result.getPropertyCount();i++){
            		SoapObject   soapChilds ;
            		soapChilds   =(SoapObject)result.getProperty(i);
            		HashMap<String, Object> map = new HashMap<String, Object>();
            		for(int   j=0;j <str.length;j++){
            			try{
            				map.put(str[j],  soapChilds.getProperty(str1[j]).toString());
            			}catch(Exception e){
            				map.put(str[j],  "");
            			}
            		}
            		ls.add(map);
            	}
            	return ls;
            }else{
            	return ls;
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ls;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ls;
        } 
	}
	public static List<Map<String, Object>> GetCKKC(String hpid,String[] str,String[] str1,SharedPreferences mSharedPreferences){
		List<Map<String, Object>> ls=new ArrayList<Map<String, Object>>();
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.GetCKKC);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    soapObject.addProperty("hpid",hpid);
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.GetCKKC, envelope);
            SoapObject result=(SoapObject) envelope.getResponse();
            if(result!=null){
            	for(int   i=0;i <result.getPropertyCount();i++){
            		SoapObject   soapChilds ;
            		soapChilds   =(SoapObject)result.getProperty(i);
            		HashMap<String, Object> map = new HashMap<String, Object>();
            		for(int   j=0;j <str.length;j++){
            			try{
            				if(str[j].equals(DataBaseHelper.KCSL)){
            					map.put(str[j],
            					DecimalsHelper.Transfloat(Double.parseDouble(soapChilds.getProperty(str1[j]).toString()),MyApplication.getInstance().getNumPoint()));
            				}else{
            					map.put(str[j],  soapChilds.getProperty(str1[j]).toString());
            				}
            			}catch(Exception e){
            				map.put(str[j],  "");
            			}
            		}
            		ls.add(map);
            	}
            	return ls;
            }else{
            	return ls;
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ls;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ls;
        } 
	}
	public static String GetHPLBLBS(String index,SharedPreferences mSharedPreferences){
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.GetHPLBLBS);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    soapObject.addProperty("sindex", index);
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.GetHPLBLBS, envelope);
            Object result=envelope.getResponse();
            if(result!=null&&!result.toString().equals("anyType{}")){
            	
            	return result.toString();
            }else{
            	return "";
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
        	
            e.printStackTrace();
            return "";
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
	}
	public static String GetHPLB_PID(String id,SharedPreferences mSharedPreferences){
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.GetHPLB_PID);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    soapObject.addProperty("id", id); 
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.GetHPLB_PID, envelope);
            Object result=envelope.getResponse();
            if(result!=null){
            	
            	return result.toString();
            }else{
            	return "";
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
        	
            e.printStackTrace();
            return "";
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        } 
	}
	public static String GetHP_ByTM(String tm,boolean ismatch,SharedPreferences mSharedPreferences){
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.GetHP_ByTM);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("tm", tm); 
	    soapObject.addProperty("ismatch", ismatch); 
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.GetHP_ByTM, envelope);
            Object result=envelope.getResponse();
            if(result!=null){
            	if(result.toString().equals("anyType{}")){
            		return "";
            	}
            	return result.toString();
            }else{
            	return "";
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
        	
            e.printStackTrace();
            return "";
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        } 
	}
	public static List<Map<String, Object>> GetDW(int top,String id,String[] str,String[] str1,SharedPreferences mSharedPreferences){
		List<Map<String, Object>> ls=new ArrayList<Map<String, Object>>();
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.GetDW);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("id",id); 
	    soapObject.addProperty("top", top); 
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.GetDW, envelope);
            SoapObject result=(SoapObject) envelope.getResponse();
            if(result!=null){
            	for(int   i=0;i <result.getPropertyCount();i++){
            		SoapObject   soapChilds ;
            		soapChilds   =(SoapObject)result.getProperty(i);
            		HashMap<String, Object> map = new HashMap<String, Object>();
            		for(int   j=0;j <str.length;j++){
            			try{
            				map.put(str[j],  soapChilds.getProperty(str1[j]).toString());
            			}catch(Exception e){
            				map.put(str[j],  "");
            			}
            		}
            		ls.add(map);
            	}
            	return ls;
            }else{
            	return ls;
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ls;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ls;
        } 
	}
	public static List<Map<String, Object>> GetDW_search(int top,String id,String search,String[] str
						,String[] str1,SharedPreferences mSharedPreferences){
		List<Map<String, Object>> ls=new ArrayList<Map<String, Object>>();
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.GetDW_search);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("id",id); 
	    soapObject.addProperty("top", top); 
	    soapObject.addProperty("search", search); 
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.GetDW_search, envelope);
            SoapObject result=(SoapObject) envelope.getResponse();
            if(result!=null){
            	for(int   i=0;i <result.getPropertyCount();i++){
            		SoapObject   soapChilds ;
            		soapChilds   =(SoapObject)result.getProperty(i);
            		HashMap<String, Object> map = new HashMap<String, Object>();
            		for(int   j=0;j <str.length;j++){
            			try{
            				map.put(str[j],  soapChilds.getProperty(str1[j]).toString());
            			}catch(Exception e){
            				map.put(str[j],  "");
            			}
            		}
            		ls.add(map);
            	}
            	return ls;
            }else{
            	return ls;
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ls;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ls;
        } 
	}
	public static List<Map<String, Object>> GetDW_byid(String id,String[] str,String[] str1,SharedPreferences mSharedPreferences){
		List<Map<String, Object>> ls=new ArrayList<Map<String, Object>>();
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.GetDW_BYID);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("id",id); 
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.GetDW_BYID, envelope);
            SoapObject result=(SoapObject) envelope.getResponse();
            if(result!=null){
            	for(int   i=0;i <result.getPropertyCount();i++){
            		SoapObject   soapChilds ;
            		soapChilds   =(SoapObject)result.getProperty(i);
            		HashMap<String, Object> map = new HashMap<String, Object>();
            		for(int   j=0;j <str.length;j++){
            			try{
            				map.put(str[j],  soapChilds.getProperty(str1[j]).toString());
            			}catch(Exception e){
            				map.put(str[j],  "");
            			}
            		}
            		ls.add(map);
            	}
            	return ls;
            }else{
            	return ls;
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ls;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ls;
        } 
	}
	public static List<Map<String, Object>> Gt_DJ_details(int top,String id,String mid,
			String[] str,String[] str1,String method,SharedPreferences mSharedPreferences){
		List<Map<String, Object>> ls=new ArrayList<Map<String, Object>>();
		SoapObject soapObject=new SoapObject("http://guantang.net/",method);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("id",id);
	    soapObject.addProperty("mid",mid);
	    soapObject.addProperty("top",top);
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
        	
            httpTranstation.call("http://guantang.net/"+method, envelope);
            SoapObject result=(SoapObject) envelope.getResponse();
            if(result!=null){
            	for(int   i=0;i <result.getPropertyCount();i++){
            		SoapObject   soapChilds ;
            		soapChilds   =(SoapObject)result.getProperty(i);
            		HashMap<String, Object> map = new HashMap<String, Object>();
            		for(int   j=0;j <str.length;j++){
            			try{
            				if(str[j].equals(DataBaseHelper.MSL)
            						||str[j].equals(DataBaseHelper.BTKC)
            						||str[j].equals(DataBaseHelper.ATKC)||str[j].equals("profit")
            						||str[j].equals("lose")){
            					map.put(str[j],
        	            				DecimalsHelper.Transfloat(Double.parseDouble(
        	            						soapChilds.getProperty(str1[j]).toString()),MyApplication.getInstance().getNumPoint()));
            				}else if(str[j].equals(DataBaseHelper.DJ)){
            					map.put(str[j],
        	            				DecimalsHelper.Transfloat(Double.parseDouble(
        	            						soapChilds.getProperty(str1[j]).toString()),MyApplication.getInstance().getDjPoint()));
            				}else if(str[j].equals(DataBaseHelper.ZJ)){
            					map.put(str[j],
        	            				DecimalsHelper.Transfloat(Double.parseDouble(
        	            						soapChilds.getProperty(str1[j]).toString()),MyApplication.getInstance().getJePoint()));
            				}else{
            					map.put(str[j],  soapChilds.getProperty(str1[j]).toString());
            				}
            			}catch(Exception e){
            				map.put(str[j],  "");
            			}
            		}
            		ls.add(map);
            	}
            	return ls;
            }else{
            	return ls;
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ls;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ls;
        } 
	}
	
	public static List<Map<String, Object>> Gt_PDDJ_details(int top,String id,String mid,
			String[] str,String[] str1,String method,SharedPreferences mSharedPreferences){
		List<Map<String, Object>> ls=new ArrayList<Map<String, Object>>();
		SoapObject soapObject=new SoapObject("http://guantang.net/",method);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("id",id); 
	    soapObject.addProperty("mid",mid);
	    soapObject.addProperty("top",top);
	    String sssssssss=mSharedPreferences.getString(ShareprefenceBean.SERID, "");
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		try {
            httpTranstation.call("http://guantang.net/"+method, envelope);
            SoapObject result=(SoapObject) envelope.getResponse();
            if(result!=null){
            	for(int   i=0;i <result.getPropertyCount();i++){
            		SoapObject   soapChilds ;
            		soapChilds   =(SoapObject)result.getProperty(i);
            		HashMap<String, Object> map = new HashMap<String, Object>();
            		for(int   j=0;j <str.length;j++){
            			try{
            				if(str[j].equals(DataBaseHelper.MSL)
            						||str[j].equals(DataBaseHelper.BTKC)
            						||str[j].equals(DataBaseHelper.ATKC)||str[j].equals("profit")
            						||str[j].equals("lose")){
            					map.put(str[j],
        	            				DecimalsHelper.Transfloat(Double.parseDouble(
        	            						soapChilds.getProperty(str1[j]).toString()),MyApplication.getInstance().getNumPoint()));
            				}else if(str[j].equals(DataBaseHelper.DJ)){
            					map.put(str[j],
        	            				DecimalsHelper.Transfloat(Double.parseDouble(
        	            						soapChilds.getProperty(str1[j]).toString()),MyApplication.getInstance().getDjPoint()));
            				}else if(str[j].equals(DataBaseHelper.ZJ)){
            					map.put(str[j],
        	            				DecimalsHelper.Transfloat(Double.parseDouble(
        	            						soapChilds.getProperty(str1[j]).toString()),MyApplication.getInstance().getJePoint()));
            				}else{
            					map.put(str[j],  soapChilds.getProperty(str1[j]).toString());
            				}
            			}catch(Exception e){
            				map.put(str[j],  "");
            			}
            		}
            		ls.add(map);
            	}
            	return ls;
            }else{
            	return ls;
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ls;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ls;
        } 
	}
	
	public static int GtMaxID_DJ_details(String mid ,String method,SharedPreferences mSharedPreferences){
		SoapObject soapObject=new SoapObject("http://guantang.net/",method);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11); 
	    soapObject.addProperty("mid",mid);
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+method, envelope);
            Object result=envelope.getResponse();
            if(result!=null){
            	
            	return Integer.parseInt(result.toString());
            }else{
            	return -3;
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
        	
            e.printStackTrace();
            return -3;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -3;
        } 
	}
	public static float Get_DJ_detail_tj(String mid,String method,SharedPreferences mSharedPreferences){
		SoapObject soapObject=new SoapObject("http://guantang.net/",method);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11); 
	    soapObject.addProperty("mid",mid);
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+method, envelope);
            Object result=envelope.getResponse();
            if(result!=null){
            	
            	return Float.parseFloat(result.toString());
            }else{
            	return -3;
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
        	
            e.printStackTrace();
            return -3;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -3;
        } 
	}
	public static List<Map<String, Object>> Get_tbm_movem(int top,int status,String dt1,String dt2,
			String[] str,String[] str1,String id,SharedPreferences mSharedPreferences){
		List<Map<String, Object>> ls=new ArrayList<Map<String, Object>>();
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.Get_tbm_movem);
		SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapObject.addProperty("status",status); 
		soapObject.addProperty("top", top); 
		soapObject.addProperty("dt1", dt1); 
		soapObject.addProperty("dt2", dt2); 
		soapObject.addProperty("id", id);
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet=true;
		HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
		httpTranstation.debug=true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		try {
		httpTranstation.call("http://guantang.net/"+WebserviceHelper.Get_tbm_movem, envelope);
		SoapObject result=(SoapObject) envelope.getResponse();
		if(result!=null){
			for(int   i=0;i <result.getPropertyCount();i++){
				SoapObject   soapChilds ;
				soapChilds   =(SoapObject)result.getProperty(i);
				HashMap<String, Object> map = new HashMap<String, Object>();
				for(int   j=0;j <str.length;j++){
					try{
						if(str1[j].equals("lrsj")||str1[j].equals("tjsj")||str1[j].equals("tjsj")
								||str1[j].equals("mvdt")||str1[j].equals("shsj")){
							map.put(str[j], soapChilds.getProperty(str1[j]).toString().replace("T", " "));
						}else{
							String s=soapChilds.getProperty(str1[j]).toString();
							if(s.equals("anyType{}")){
								map.put(str[j],  "");
							}else{
								map.put(str[j],  s);
							}
							
						}
					}catch(Exception e){
						map.put(str[j],  "");
					}
				}
				ls.add(map);
			}
			return ls;
		}else{
			return ls;
		}

		} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return ls;
		} catch (XmlPullParserException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return ls;
		} 
}	
	public static int Del_tbm_movem(String id,SharedPreferences mSharedPreferences){
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.Del_tbm_movem);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("id",id);
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.Del_tbm_movem, envelope);
            Object result=envelope.getResponse();
            if(result!=null){
            	
            	return Integer.parseInt(result.toString());
            }else{
            	return -3;
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
        	
            e.printStackTrace();
            return -3;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -3;
        } 
	}
	public static int Del_Order(String id,SharedPreferences mSharedPreferences){
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.Del_Order);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("id",id);
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.Del_Order, envelope);
            Object result=envelope.getResponse();
            if(result!=null){
            	
            	return Integer.parseInt(result.toString());
            }else{
            	return -3;
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
        	
            e.printStackTrace();
            return -3;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -3;
        } 
	}
	public static double Get_Moved_sl_znum(String dt1,String dt2,int direct,int ckid,String where,SharedPreferences mSharedPreferences){
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.Get_Moved_msl_znum);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11); 
	    soapObject.addProperty("dt2",dt2);
	    soapObject.addProperty("direct",direct); 
	    soapObject.addProperty("ckid",ckid);
	    soapObject.addProperty("dt1",dt1);
	    soapObject.addProperty("where",where);
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.Get_Moved_msl_znum, envelope);
            Object result=envelope.getResponse();
            if(result!=null){
            	return Double.parseDouble(result.toString());
            }else{
            	return -3;
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
        	
            e.printStackTrace();
            return -3;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -3;
        } 
	}
	public static float Get_Moved_zj_znum(String dt1,String dt2,int direct,int ckid,String where,SharedPreferences mSharedPreferences){
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.Get_Moved_zj_znum);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11); 
	    soapObject.addProperty("dt2",dt2);
	    soapObject.addProperty("direct",direct); 
	    soapObject.addProperty("ckid",ckid);
	    soapObject.addProperty("dt1",dt1);
	    soapObject.addProperty("where",where);
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.Get_Moved_zj_znum, envelope);
            Object result=envelope.getResponse();
            if(result!=null){
            	
            	return Float.parseFloat(result.toString());
            }else{
            	return -3;
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
        	
            e.printStackTrace();
            return -3;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -3;
        } 
	}
	public static double Get_Moved_hp_znum(String hpid,String dt1,String dt2,int direct,int ckid,SharedPreferences mSharedPreferences){
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.Get_Moved_hp_znum);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11); 
	    soapObject.addProperty("dt2",dt2);
	    soapObject.addProperty("direct",direct); 
	    soapObject.addProperty("ckid",ckid);
	    soapObject.addProperty("dt1",dt1);
	    soapObject.addProperty("hpid",hpid);
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.Get_Moved_hp_znum, envelope);
            Object result=envelope.getResponse();
            if(result!=null){
            	
            	return Double.parseDouble(result.toString());
            }else{
            	return -3;
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
        	
            e.printStackTrace();
            return -3;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -3;
        } 
	}
	public static List<Map<String, Object>> GetMoved_DJ(int top,String id,String dt1,String dt2,int direct,int ckid,
			String[] str,String[] str1,String where,SharedPreferences mSharedPreferences){
		List<Map<String, Object>> ls=new ArrayList<Map<String, Object>>();
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.Gt_Moved_DJ);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("dt1",dt1); 
	    soapObject.addProperty("top",top);
	    soapObject.addProperty("id",id); 
	    soapObject.addProperty("dt2",dt2);
	    soapObject.addProperty("direct",0); 
	    soapObject.addProperty("ckid",ckid); 
	    soapObject.addProperty("where",where);
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.Gt_Moved_DJ, envelope);
            SoapObject result=(SoapObject) envelope.getResponse();
            if(result!=null){
            	for(int   i=0;i <result.getPropertyCount();i++){
            		SoapObject   soapChilds ;
            		soapChilds   =(SoapObject)result.getProperty(i);
            		HashMap<String, Object> map = new HashMap<String, Object>();
            		for(int   j=0;j <str.length;j++){
            			try{
            				if(str[j].equals(DataBaseHelper.MSL)
            						||str[j].equals(DataBaseHelper.BTKC)
            						||str[j].equals(DataBaseHelper.ATKC)||str[j].equals(DataBaseHelper.AZKC)||str[j].equals("profit")
            						||str[j].equals("lose")){
            					map.put(str[j],
        	            				DecimalsHelper.Transfloat(Double.parseDouble(
        	            						soapChilds.getProperty(str1[j]).toString()),MyApplication.getInstance().getNumPoint()));
            				}else if(str[j].equals(DataBaseHelper.DJ)){
            					if(RightsHelper.isHaveRight(RightsHelper.system_cw,mSharedPreferences)){
            						map.put(str[j],DecimalsHelper.Transfloat(Double.parseDouble(soapChilds.getProperty(str1[j]).toString()),MyApplication.getInstance().getDjPoint()));
            					}else{
            						map.put(str[j],  "");
            					}
            				}else if(str[j].equals(DataBaseHelper.ZJ)){
            					if(RightsHelper.isHaveRight(RightsHelper.system_cw,mSharedPreferences)){
            						map.put(str[j],
            	            				DecimalsHelper.Transfloat(Double.parseDouble(
            	            						soapChilds.getProperty(str1[j]).toString()),MyApplication.getInstance().getJePoint()));
            					}else{
            						map.put(str[j],  "");
            					}
            					
            				}else{
            					if(str[j].equals(DataBaseHelper.MVDDATE)){
            						map.put(str[j],  soapChilds.getProperty(str1[j]).toString().replace("T", " "));
            					}else{
            						map.put(str[j],  soapChilds.getProperty(str1[j]).toString());
            					}
            				}
            			}catch(Exception e){
            				map.put(str[j],  "");
            			}
            		}
            		ls.add(map);
            	}
            	return ls;
            }else{
            	return ls;
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ls;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ls;
        } 
	}
	public static int GetMaxID_DW(SharedPreferences mSharedPreferences){
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.GetMaxID_DW);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11); 
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.GetMaxID_DW, envelope);
            Object result=envelope.getResponse();
            if(result!=null){
            	
            	return Integer.parseInt(result.toString());
            }else{
            	return -1;
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
        	
            e.printStackTrace();
            return -1;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -1;
        } 
	}
	public static int GetMaxID_DW_search(String search,SharedPreferences mSharedPreferences){
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.GetMaxID_DW_search);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    soapObject.addProperty("search", search);
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.GetMaxID_DW_search, envelope);
            Object result=envelope.getResponse();
            if(result!=null){
            	
            	return Integer.parseInt(result.toString());
            }else{
            	return -1;
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
        	
            e.printStackTrace();
            return -1;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -1;
        } 
	}
	public static int GetCount_DW_search(String search,SharedPreferences mSharedPreferences){
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.GetCount_DW_search);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("search", search);
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.GetCount_DW_search, envelope);
            Object result=envelope.getResponse();
            if(result!=null){
            	
            	return Integer.parseInt(result.toString());
            }else{
            	return -1;
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
        	
            e.printStackTrace();
            return -1;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -1;
        } 
	}
	public static int GetCKKC_Total_Leave(String id,SharedPreferences mSharedPreferences){
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.GetCKKC_Total_Leave);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    soapObject.addProperty("id", id); 
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.GetCKKC_Total_Leave, envelope);
            Object result=envelope.getResponse();
            if(result!=null){
            	
            	return Integer.parseInt(result.toString());
            }else{
            	return -1;
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
        	
            e.printStackTrace();
            return -1;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -1;
        } 
	}
	public static int GetMaxID_Moved_DJ(String dt1,String dt2,int direct,int ckid,String where,SharedPreferences mSharedPreferences){
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.GetMaxID_Moved_DJ);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("dt1", dt1); 
	    soapObject.addProperty("dt2", dt2); 
	    soapObject.addProperty("direct", direct); 
	    soapObject.addProperty("ckid", ckid); 
	    soapObject.addProperty("where",where);
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.GetMaxID_Moved_DJ, envelope);
            Object result=envelope.getResponse();
            if(result!=null){
            	return Integer.parseInt(result.toString());
            }else{
            	return -1;
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
        	
            e.printStackTrace();
            return -1;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -1;
        } 
	}
	public static int GetMaxID_Order(String strwhere,SharedPreferences mSharedPreferences){
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.GtMaxID_Order);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("strwhere", strwhere); 
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.GtMaxID_Order, envelope);
            Object result=envelope.getResponse();
            if(result!=null){
            	return Integer.parseInt(result.toString());
            }else{
            	return -3;
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
        	
            e.printStackTrace();
            return -3;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -3;
        } 
	}
	public static int GetMaxID_Order_details(String id,SharedPreferences mSharedPreferences){
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.GtMaxID_Order_details);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("id", id); 
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.GtMaxID_Order_details, envelope);
            Object result=envelope.getResponse();
            if(result!=null){
            	return Integer.parseInt(result.toString());
            }else{
            	return -3;
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
        	
            e.printStackTrace();
            return -3;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -3;
        } 
	}
	public static int GetMaxID_Moved_DJ_ByHpid(String hpid,String dt1,String dt2,int direct,int ckid,SharedPreferences mSharedPreferences){
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.GetMaxID_Moved_DJ_ByHpid);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("dt1", dt1); 
	    soapObject.addProperty("dt2", dt2); 
	    soapObject.addProperty("direct", direct); 
	    soapObject.addProperty("ckid", ckid); 
	    soapObject.addProperty("hpid", hpid);
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.GetMaxID_Moved_DJ_ByHpid, envelope);
            Object result=envelope.getResponse();
            if(result!=null){
            	return Integer.parseInt(result.toString());
            }else{
            	return -1;
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
        	
            e.printStackTrace();
            return -1;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -1;
        } 
	}
	public static int GetCKKC_Total(SharedPreferences mSharedPreferences){
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.GetCKKC_Total);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11); 
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.GetCKKC_Total, envelope);
            Object result=envelope.getResponse();
            if(result!=null){
            	String str=result.toString();
            	
            	return Integer.parseInt(str);
            }else{
            	return -1;
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
        	
            e.printStackTrace();
            return -1;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -1;
        } 
	}
	public static List<Map<String, Object>> GetCKKC_top(int top ,String id ,String[] str
			,String[] str1,SharedPreferences mSharedPreferences){
		List<Map<String, Object>> ls=new ArrayList<Map<String, Object>>();
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.GetCKKC_top);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("top", top); 
	    soapObject.addProperty("id", id);  
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.GetCKKC_top, envelope);
            SoapObject result=(SoapObject) envelope.getResponse();
            if(result!=null){
            	for(int   i=0;i <result.getPropertyCount();i++){
            		SoapObject   soapChilds ;
            		soapChilds   =(SoapObject)result.getProperty(i);
            		HashMap<String, Object> map = new HashMap<String, Object>();
            		for(int   j=0;j <str.length;j++){
            			try{
            				if(str[j].equals(DataBaseHelper.KCSL)){
            					map.put(str[j],
            					DecimalsHelper.Transfloat(Double.parseDouble(soapChilds.getProperty(str1[j]).toString()),8));
            				}else{
            					map.put(str[j],  soapChilds.getProperty(str1[j]).toString());
            				}
            			}catch(Exception e){
            				if(str[j].equals(DataBaseHelper.KCSL)){
            					map.put(str[j],  "0");
            				}else{
            					map.put(str[j],  "");
            				}
            			}
            		}
            		ls.add(map);
            	}
            	return ls;
            }else{
            	return ls;
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ls;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ls;
        } 
	}
	public static List<Map<String, Object>> GetMoved_DJ_ByHpid(int top,String id,String hpid
			,String dt1,String dt2,int direct,int ckid,String[] str,String[] str1,SharedPreferences mSharedPreferences){
		List<Map<String, Object>> ls=new ArrayList<Map<String, Object>>();
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.Gt_Moved_DJ_ByHpid);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("dt1",dt1); 
	    soapObject.addProperty("top",top);
	    soapObject.addProperty("id",id); 
	    soapObject.addProperty("dt2",dt2);
	    soapObject.addProperty("direct",direct); 
	    soapObject.addProperty("ckid",ckid);
	    soapObject.addProperty("hpid",hpid);
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
        try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.Gt_Moved_DJ_ByHpid, envelope);
            SoapObject result=(SoapObject) envelope.getResponse();
            if(result!=null){
            	for(int   i=0;i <result.getPropertyCount();i++){
            		SoapObject   soapChilds ;
            		soapChilds   =(SoapObject)result.getProperty(i);
            		HashMap<String, Object> map = new HashMap<String, Object>();
            		for(int   j=0;j <str.length;j++){
            			try{
            				if(str[j].equals(DataBaseHelper.MSL)
            						||str[j].equals(DataBaseHelper.BTKC)
            						||str[j].equals(DataBaseHelper.ATKC)||str[j].equals("profit")
            						||str[j].equals("lose")){
            					map.put(str[j],
        	            				DecimalsHelper.Transfloat(Double.parseDouble(
        	            						soapChilds.getProperty(str1[j]).toString()),MyApplication.getInstance().getNumPoint()));
            				}else if(str[j].equals(DataBaseHelper.DJ)){
            					if(RightsHelper.isHaveRight(RightsHelper.system_cw,mSharedPreferences)){
            						map.put(str[j],DecimalsHelper.Transfloat(Double.parseDouble(soapChilds.getProperty(str1[j]).toString()),MyApplication.getInstance().getDjPoint()));
            					}else{
            						map.put(str[j],  "");
            					}
            				}else if(str[j].equals(DataBaseHelper.ZJ)){
            					if(RightsHelper.isHaveRight(RightsHelper.system_cw,mSharedPreferences)){
            						map.put(str[j],
            	            				DecimalsHelper.Transfloat(Double.parseDouble(
            	            						soapChilds.getProperty(str1[j]).toString()),MyApplication.getInstance().getJePoint()));
            					}else{
            						map.put(str[j],  "");
            					}
            					
            				}else{
            					if(str[j].equals(DataBaseHelper.MVDT)){
            						map.put(str[j],  soapChilds.getProperty(str1[j]).toString().replace("T", " "));
            					}else{
            						map.put(str[j],  soapChilds.getProperty(str1[j]).toString());
            					}
            				}
            			}catch(Exception e){
            				map.put(str[j],  "");
            			}
            		}
            		ls.add(map);
            	}
            	return ls;
            }else{
            	return ls;
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ls;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ls;
        } 
	}
	public static List<Map<String, Object>> GtOrder_top(int top,String strwhere,String filedorder,String id,int dirc,
			String[] s,String[] s1,int flag,SharedPreferences mSharedPreferences){
		List<Map<String, Object>> ls=new ArrayList<Map<String, Object>>();
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.GetOrder_top);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("Top",top); 
	    soapObject.addProperty("strWhere",strwhere); 
	    soapObject.addProperty("filedOrder",filedorder);  
	    soapObject.addProperty("id",id);
	    soapObject.addProperty("dirc",dirc); 
	    soapObject.addProperty("flag",flag);
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.GetOrder_top, envelope);
            SoapObject result=(SoapObject) envelope.getResponse();
            if(result!=null){
            	for(int   i=0;i <result.getPropertyCount();i++){
            		SoapObject   soapChilds ;
            		soapChilds   =(SoapObject)result.getProperty(i);
            		HashMap<String, Object> map = new HashMap<String, Object>();
            		for(int   j=0;j <s.length;j++){
            			try{
            				if(s[j].equals(DataBaseHelper.Sqdt)||s[j].equals(DataBaseHelper.ReqDate)){
            					map.put(s[j],  soapChilds.getProperty(s1[j]).toString().replace("T", " ").substring(0,10));
            				}else{
            					if(s[j].equals(DataBaseHelper.zje)||s[j].equals(DataBaseHelper.yfje)){
            						map.put(s[j],  
                							DecimalsHelper.Transfloat(Double.parseDouble(soapChilds.getProperty(s1[j]).toString()),MyApplication.getInstance().getJePoint()));
            					}else{
                    				map.put(s[j],  soapChilds.getProperty(s1[j]).toString());
            					}
            				}
            			}catch(Exception e){
            				map.put(s[j],  "");
            			}
            		}
            		ls.add(map);
            	}
            }else{
            	return ls;
            }
    		return ls;
		}catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ls;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ls;
        } 
	}
	public static List<Map<String, Object>> GtOrder_detail_top(int top,String orderid,String id,
			String[] s,String[] s1,SharedPreferences mSharedPreferences){
		List<Map<String, Object>> ls=new ArrayList<Map<String, Object>>();
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.GetOrderDetails_top);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("Top",top); 
	    soapObject.addProperty("orderid",orderid); 
	    soapObject.addProperty("id",id);
	    soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, "")); 
	    soapObject.addProperty("LoginFlag", mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
	    envelope.bodyOut = soapObject;
        envelope.dotNet=true;
        HttpTransportSE httpTranstation=new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,10000);
        httpTranstation.debug=true;
        Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		try {
            httpTranstation.call("http://guantang.net/"+WebserviceHelper.GetOrderDetails_top, envelope);
            SoapObject result=(SoapObject) envelope.getResponse();
            if(result!=null){
            	for(int   i=0;i <result.getPropertyCount();i++){
            		SoapObject   soapChilds ;
            		soapChilds   =(SoapObject)result.getProperty(i);
            		HashMap<String, Object> map = new HashMap<String, Object>();
            		for(int   j=0;j <s.length;j++){
            			try{
            				if(s[j].equals(DataBaseHelper.SL)
            				||s[j].equals(DataBaseHelper.zxsl)){
            					map.put(s[j],DecimalsHelper.Transfloat(Double.parseDouble(soapChilds.getProperty(s1[j]).toString()),MyApplication.getInstance().getNumPoint()));
            				}else if(s[j].equals(DataBaseHelper.DJ)){
            					map.put(s[j],DecimalsHelper.Transfloat(Double.parseDouble(soapChilds.getProperty(s1[j]).toString()),MyApplication.getInstance().getDjPoint()));
            				}else if(s[j].equals(DataBaseHelper.ZJ)){
            					map.put(s[j],DecimalsHelper.Transfloat(Double.parseDouble(soapChilds.getProperty(s1[j]).toString()),MyApplication.getInstance().getJePoint()));
            				}else{
                				map.put(s[j],  soapChilds.getProperty(s1[j]).toString());
            				}
            			}catch(Exception e){
            				map.put(s[j],  "");
            			}
            		}
            		ls.add(map);
            	}
            }else{
            	return null;
            }
    		return ls;
		}catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ls;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ls;
        } 
	}
	public static Element[] getSoapHeader(SharedPreferences mSharedPreferences) {
		 Element[] header = new Element[1];
		 header[0] = new Element().createElement("http://guantang.net/",
					"MySoapHeader");
		 Element username = new Element().createElement(
					"http://guantang.net/", "UserName");
		 Element pass = new Element().createElement("http://guantang.net/","password");
		 PwdHelper pwdhelper = new PwdHelper();
		 if(mSharedPreferences.getInt(ShareprefenceBean.TIYANZHANGHAO, 0)==1){//是否是体验账户
				username.addChild(Node.TEXT,"admin");
				pass.addChild(Node.TEXT, pwdhelper.createMD5("admin" + "#cd@guantang").toUpperCase());
			}else{
				username.addChild(Node.TEXT,mSharedPreferences.getString(ShareprefenceBean.USERNAME, ""));
				pass.addChild(Node.TEXT, pwdhelper.createMD5(mSharedPreferences.getString(ShareprefenceBean.PASSWORD, "") + "#cd@guantang").toUpperCase());
			}
		 header[0].addChild(Node.ELEMENT, username);
		 header[0].addChild(Node.ELEMENT, pass);
		 return header;
		}
}
