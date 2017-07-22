package com.guantang.cangkuonline.helper;

import java.util.Random;

public class CodeHelper {

	private static final char[] CHARS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
	private static final int DEFAULT_CODE_LENGTH = 6;
	
	
	public static String createCode() {
		 Random random = new Random();
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < DEFAULT_CODE_LENGTH; i++) {
			buffer.append(CHARS[random.nextInt(10)]);
		}
		return buffer.toString();
	}
}
