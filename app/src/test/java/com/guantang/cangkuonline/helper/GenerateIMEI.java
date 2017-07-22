package com.guantang.cangkuonline.helper;

import java.util.Random;

import javax.security.auth.PrivateCredentialPermission;

import android.R.integer;

public class GenerateIMEI {
	private static char[] LETTER = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
	private static final char[] CHARS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
	public static String produceIMEI(){
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(String.valueOf(System.currentTimeMillis()+"-"));
		Random random = new Random();
		for(int i=0;i<10;i++){
			if(i%2==0){
				strBuilder.append(LETTER[random.nextInt(26)]);
			}else if(i%2==1){
				strBuilder.append(CHARS[random.nextInt(10)]);
			}
		}
		return strBuilder.toString();
	}
}
