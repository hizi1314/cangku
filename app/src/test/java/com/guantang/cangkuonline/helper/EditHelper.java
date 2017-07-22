package com.guantang.cangkuonline.helper;

public class EditHelper {
	public static String cutString(String str) {
		if (str != null) {
			if (str.length() != 0) {
				int i = str.length();
				int j = 0;
				char[] ch = str.toCharArray();
				while (j < i && (ch[j] == ' ' || ch[j] == '\t')) {
					j++;
				}
				while (i > j && (ch[i - 1] == ' ' || ch[i - 1] == '\t')) {
					i--;
				}
				str = str.substring(j, i);
			}
		}
		return str;
	}

	public boolean checkDyh(String str) {

		if (str.indexOf("'") > -1) {
			return false;
		}
		return true;
	}

	public String GtDH_zore(String str, String ws) {
		if (ws.equals("")) {
			ws = "4";
		}
		if (Integer.parseInt(str) < 0 || Integer.parseInt(ws) < 0) {
			return "fu";
		} else if (str.length() > Integer.parseInt(ws)) {
			return "chang";
		} else {
			for (int i = str.length(); i < Integer.parseInt(ws); i++) {
				str = "0" + str;
			}
		}
		return str;
	}

	public static boolean checkDwName(String dwname) {
		if (dwname.indexOf("'") > -1 || dwname.indexOf("%") > -1
				|| dwname.indexOf("*") > -1 || dwname.indexOf("=") > -1
				|| dwname.indexOf("?") > -1 || dwname.indexOf("^") > -1
				|| dwname.indexOf(">") > -1 || dwname.indexOf("<") > -1) {
			return false;
		}
		return true;
	}

//	public static String SwitchCaps(String str) {  //大写转小写
//		StringBuffer strb = new StringBuffer();
//		for (int i = 0; i < str.length(); i++) {
//			char character = str.charAt(i);
//			if (character >= 'A' && character <= 'Z') {
//				strb.append(String.valueOf((char) (character + 'a' - 'A')));
//
//			} else {
//				strb.append(character);
//			}
//		}
//		return strb.toString();
//	}
//
//	public static String SwitchLow(String str) {  //小写转大写
//		StringBuffer strb = new StringBuffer();
//		for (int i = 0; i < str.length(); i++) {
//			char character = str.charAt(i);
//			if (character >= 'a' && character <= 'z') {
//				strb.append(String.valueOf((char) (character - ('a' - 'A'))));
//			} else {
//				strb.append(character);
//			}
//		}
//		return strb.toString();
//	}

	public static String CheckHttp(String str) {
		if (str != null && str.length() > 7) {
			if (!str.substring(0, 6).equals("http://")) {
				return "http://";
			} else {
				return "";
			}
		} else {
			return "http://";
		}
	}
}
