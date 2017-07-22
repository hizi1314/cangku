package com.guantang.cangkuonline.helper;

import java.text.DecimalFormat;
import java.util.regex.Pattern;

import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.database.DataBaseOperateMethod;

import android.R.integer;
import android.content.Context;
import android.text.Editable;

public class DecimalsHelper {
	
	public static boolean stringIsNumBer(String string){
        if(string != null){
            if(string.matches("^(-?\\d+)(\\.\\d+)?$")){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
	
	
	/*
	 * 价格小数位数，如果没有设置就默认两位小数。如果大于8位就是8位。
	 *	
	 * number 是小数点后面的位数
	 * */
	public static String Transfloat(double f,int number){
		if(Math.round(f)-f==0){
			   return String.valueOf((long)f);
			  }else{
				  DecimalFormat df = null;
				  if(number>=8){
					  df = new DecimalFormat("0000000000.00000000");
				  }else{
					  StringBuilder dexString = new StringBuilder("0000000000.");
					  for(int i=1;i<=number;i++){
						  dexString.append("0");
					  }
					  df = new DecimalFormat(dexString.toString());
				  }
				  f = Double.parseDouble(df.format(f));
				  return String.valueOf(f);
			  }
	}
	
	/**
	 * 判断输入的数字字符串的范围是否在decimal(18, 8)范围内，如果在这个范围内就返回true,如果不是在这个范围内就返回false
	 * @param numberString 输入的数字字符串
	 * 
	 * */
	public static Boolean NumBerStringIsFormat(String numberString){
		if(!numberString.isEmpty()){
			int pos = numberString.length() - 1;
			int position = numberString.toString().indexOf(".");

			if(pos-position>8 && position != -1){
				return false;
			}
			if(position==-1 && numberString.toString().length()>10){
				char[] numberStrings =numberString.toString().toCharArray();
				if(numberStrings.length>10 && !String.valueOf(numberStrings[10]).equals(".")){//如果第11位不是小数点就删除
					return false;
				}
			}
		}
		return true;
//		if(!numberString.isEmpty()){
//			String[] numberArray = numberString.split(".");
//			int i = numberArray.length;
//			if(numberArray.length==2){
//				return (numberArray[0].length()<=10 && numberArray[1].length()<=2);
//			}else if(numberArray.length==1){
//				return numberArray[0].length()<=11;
//			}else if(numberArray.length==0){
//				return numberString.length()<=10;
//			}
//		}else{
//			return true;
//		}
//		return false;
	}
}
