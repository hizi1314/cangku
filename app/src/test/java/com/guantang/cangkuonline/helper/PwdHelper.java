package com.guantang.cangkuonline.helper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PwdHelper {
	MessageDigest digest=null;
	public PwdHelper(){
		try {
			digest = MessageDigest.getInstance("MD5" );
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String createMD5(String str){
		digest.update(str.getBytes());
		byte[] b=digest.digest();
		return getString(b);
	}
	public  String getString(byte[] b){  
	    StringBuffer sb = new StringBuffer();  
	    for(int i = 0; i < b.length; i ++){  
	    	if (Integer.toHexString(0xFF & b[i]).length() == 1)       
	              sb.append("0").append(Integer.toHexString(0xFF & b[i]));       
	          else       
	              sb.append(Integer.toHexString(0xFF & b[i]));   
	     }  
	     return sb.toString();
	} 
	public boolean checkMD5(String str,String md5){
		if(md5.equals(createMD5(str))){
			return true;
		}else{
			return false;
		}
	}
}
