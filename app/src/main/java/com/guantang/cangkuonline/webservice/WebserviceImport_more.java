package com.guantang.cangkuonline.webservice;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;
import org.myksoap2.SoapEnvelope;
import org.myksoap2.serialization.SoapObject;
import org.myksoap2.serialization.SoapSerializationEnvelope;
import org.myksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.R.integer;
import android.R.string;
import android.content.SharedPreferences;

import com.guantang.cangkuonline.application.MyApplication;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.helper.DecimalsHelper;
import com.guantang.cangkuonline.helper.EditHelper;
import com.guantang.cangkuonline.helper.PwdHelper;
import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

public class WebserviceImport_more {

	public static Element[] getSoapHeader(SharedPreferences mSharedPreferences) {
		Element[] header = new Element[1];
		header[0] = new Element().createElement("http://guantang.net/",
				"MySoapHeader");
		Element username = new Element().createElement("http://guantang.net/",
				"UserName");
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

	public static String sendCode(String tel, String msg, int mm,
			SharedPreferences mSharedPreferences) {
		SoapObject soapObject = new SoapObject("http://guantang.net/",
				WebserviceHelper.SendMsg);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		soapObject.addProperty("tel", tel);
		soapObject.addProperty("msg", msg);
		soapObject.addProperty("mm", mm);
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTranstation = new HttpTransportSE(EditHelper.CheckHttp(mSharedPreferences.getString(ShareprefenceBean.SERVICE_IDN_URL1, "sh.gtcangku.com"))+ mSharedPreferences.getString(ShareprefenceBean.SERVICE_IDN_URL1, "sh.gtcangku.com")+"/"
						+ WebserviceHelper.APIService, 2000);
		httpTranstation.debug = true;
		try {
			httpTranstation.call("http://guantang.net/"
					+ WebserviceHelper.SendMsg, envelope);
			Object result = envelope.getResponse();
			if (result != null) {
				if (result.toString().equals("anyType{}")) {
					return "-1";
				}
				return result.toString();
			} else {
				return "";
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

	public static String Gt_Movem_Amount(String strWhere,
			SharedPreferences mSharedPreferences) {
		SoapObject soapObject = new SoapObject("http://guantang.net/",
				WebserviceHelper.Gt_Movem_Amount);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		soapObject.addProperty("strWhere", strWhere);
		soapObject.addProperty("SerId",
				mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",
				mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTranstation = new HttpTransportSE(
				mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")
						+ WebserviceHelper.GtStorageWebservice, 8000);
		httpTranstation.debug = true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		try {
			httpTranstation.call("http://guantang.net/"
					+ WebserviceHelper.Gt_Movem_Amount, envelope);
			Object result = envelope.getResponse();
			if (result!=null&&!result.toString().equals("anyType{}")) {
				return result.toString();
			} else {
				return "";
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		}
	}

	public static int GetHp_Today_Total(String methodname, int ckid,
			SharedPreferences mSharedPreferences) {
		SoapObject soapObject = new SoapObject("http://guantang.net/",
				methodname);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		soapObject.addProperty("ckid", ckid);
		soapObject.addProperty("SerId",
				mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",
				mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTranstation = new HttpTransportSE(
				mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")
						+ WebserviceHelper.GtStorageWebservice, 8000);
		httpTranstation.debug = true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		try {
			httpTranstation.call("http://guantang.net/" + methodname, envelope);
			Object result = envelope.getResponse();
			if (result!=null&&!result.toString().equals("anyType{}")) {
				return Integer.parseInt(result.toString());
			} else {
				return -1;
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return -1000;
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return -1001;
		}
	}

	public static List<Map<String, Object>> GetHpInfoToday_top(int top,
			String id, int direction, int ckid, String[] str, String[] str1,
			SharedPreferences mSharedPreferences) {
		SoapObject soapObject = new SoapObject("http://guantang.net/",
				WebserviceHelper.GetHpInfoToday_top);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		soapObject.addProperty("top", top);
		soapObject.addProperty("id", id);
		soapObject.addProperty("direction", direction);
		soapObject.addProperty("ckid", ckid);
		soapObject.addProperty("SerId",
				mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",
				mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTranstation = new HttpTransportSE(
				mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")
						+ WebserviceHelper.GtStorageWebservice, 8000);
		httpTranstation.debug = true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;

		List<Map<String, Object>> ls = new ArrayList<Map<String, Object>>();
		try {
			httpTranstation.call("http://guantang.net/"
					+ WebserviceHelper.GetHpInfoToday_top, envelope);
			SoapObject result = (SoapObject) envelope.getResponse();
			if (result != null) {
				for (int i = 0; i < result.getPropertyCount(); i++) {
					SoapObject soapChilds;
					if (direction > 0) {
						soapChilds = (SoapObject) result.getProperty(i);
					} else {
						soapChilds = (SoapObject) result.getProperty(result
								.getPropertyCount() - 1 - i);
					}
					HashMap<String, Object> map = new HashMap<String, Object>();
					for (int j = 0; j < str.length; j++) {
						try {
							if (str[j].equals(DataBaseHelper.KCSL)
									|| str[j].equals(DataBaseHelper.CurrKC)) {
								map.put(str[j], DecimalsHelper.Transfloat(Double.parseDouble(soapChilds.getProperty(
												str1[j]).toString()),MyApplication.getInstance().getNumPoint()));
							} else {
								map.put(str[j], soapChilds.getProperty(str1[j])
										.toString());
							}
						} catch (Exception e) {
							if (str[j].equals(DataBaseHelper.KCSL)
									|| str[j].equals(DataBaseHelper.CurrKC)) {
								map.put(str[j], "0");
							} else {
								map.put(str[j], "");
							}
						}
					}
					ls.add(map);
				}
				return ls;
			} else {
				return ls;
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return ls;
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return ls;
		}

	}

//	public static List<Map<String, Object>> Gt_Movem(String strWhere,
//			String orderby, int startIndex, int endIndex, String[] str1,
//			String[] str2, SharedPreferences mSharedPreferences) {
//		SoapObject soapObject = new SoapObject("http://guantang.net/",
//				WebserviceHelper.Gt_Movem);
//		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
//				SoapEnvelope.VER11);
//		soapObject.addProperty("strWhere", strWhere);
//		soapObject.addProperty("orderby", orderby);
//		soapObject.addProperty("startIndex", startIndex);
//		soapObject.addProperty("endIndex", endIndex);
//		soapObject.addProperty("SerId",
//				mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
//		soapObject.addProperty("LoginFlag",
//				mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
//		envelope.bodyOut = soapObject;
//		envelope.dotNet = true;
//		envelope.encodingStyle = "UTF-8";
//		HttpTransportSE httpTranstation = new HttpTransportSE(
//				mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")
//						+ WebserviceHelper.GtStorageWebservice, 8000);
//		httpTranstation.debug = true;
//		Element[] header = getSoapHeader(mSharedPreferences);
//		envelope.headerOut = header;
//
//		List<Map<String, Object>> ls = new ArrayList<Map<String, Object>>();
//		try {
//			httpTranstation.call("http://guantang.net/"
//					+ WebserviceHelper.Gt_Movem, envelope);
//			SoapObject result = (SoapObject) envelope.getResponse();
//			if (result != null) {
//				for (int i = 0; i < result.getPropertyCount(); i++) {
//					SoapObject soapChilds = (SoapObject) result.getProperty(i);
//					HashMap<String, Object> map = new HashMap<String, Object>();
//					for (int j = 0; j < str1.length; j++) {
//						try {
//							if (str1.equals(DataBaseHelper.HPM_ID)
//									|| str1.equals(DataBaseHelper.CKID)
//									|| str1.equals(DataBaseHelper.mType)
//									|| str1.equals(DataBaseHelper.DWID)) {
//								map.put(str1[j], DecimalsHelper
//										.Transfloat(Double.parseDouble(soapChilds
//												.getProperty(str2[j])
//												.toString()),2));
//							} else {
//								map.put(str1[j], soapChilds
//										.getProperty(str2[j]).toString());
//							}
//						} catch (Exception e) {
//							if (str1[j].equals(DataBaseHelper.HPM_ID)
//									|| str1.equals(DataBaseHelper.CKID)
//									|| str1.equals(DataBaseHelper.mType)
//									|| str1.equals(DataBaseHelper.DWID)) {
//								map.put(str1[j], "-2");
//							} else {
//								map.put(str1[j], "");
//							}
//						}
//					}
//					ls.add(map);
//				}
//				return ls;
//			} else {
//				return ls;
//			}
//		} catch (IOException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//			return ls;
//		} catch (XmlPullParserException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//			return ls;
//		}
//	}

	public static List<Map<String, Object>> GetHpInfo_top_search_complex(
			int top, String id, int direction, int ckid, String[] str1,
			String[] str2, String search, SharedPreferences mSharedPreferences) {
		SoapObject soapObject = new SoapObject("http://guantang.net/",
				WebserviceHelper.GetHpInfo_top_search_complex);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		soapObject.addProperty("top", top);
		soapObject.addProperty("id", id);
		soapObject.addProperty("direction", direction);
		soapObject.addProperty("ckid", ckid);
		soapObject.addProperty("search", search);
		soapObject.addProperty("SerId",
				mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",
				mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTranstation = new HttpTransportSE(
				mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")
						+ WebserviceHelper.GtStorageWebservice, 8000);
		httpTranstation.debug = true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;

		List<Map<String, Object>> ls = new ArrayList<Map<String, Object>>();
		try {
			httpTranstation.call("http://guantang.net/"
					+ WebserviceHelper.GetHpInfo_top_search_complex, envelope);
			SoapObject result = (SoapObject) envelope.getResponse();
			if (result != null) {
				for (int i = 0; i < result.getPropertyCount(); i++) {
					SoapObject soapChilds = (SoapObject) result.getProperty(i);
					HashMap<String, Object> map = new HashMap<String, Object>();
					for (int j = 0; j < str1.length; j++) {
						try {
							if (str1[j].equals(DataBaseHelper.KCSL)
									|| str1[j].equals(DataBaseHelper.CurrKC)) {
								map.put(str1[j], DecimalsHelper
										.Transfloat(Double.parseDouble(soapChilds
												.getProperty(str2[j])
												.toString()),MyApplication.getInstance().getNumPoint()));
							} else {
								map.put(str1[j], soapChilds
										.getProperty(str2[j]).toString());
							}
						} catch (Exception e) {
							if (str1[j].equals(DataBaseHelper.KCSL)) {
								map.put(str1[j], "0");
							} else {
								map.put(str1[j], "");
							}
						}
					}
					ls.add(map);
				}
			}
			return ls;
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return ls;
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return ls;
		}
	}
	
	public static String GetHpInfo_top_search_complex_1_0(int top, String id, int direction, int ckid,String search,int ckkc, SharedPreferences mSharedPreferences) {
		SoapObject soapObject = new SoapObject("http://guantang.net/",
				WebserviceHelper.GetHpInfo_top_search_complex_1_0);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		soapObject.addProperty("top", top);
		soapObject.addProperty("id", id);
		soapObject.addProperty("direction", direction);
		soapObject.addProperty("ckid", ckid);
		soapObject.addProperty("search", search);
		soapObject.addProperty("ckkc",ckkc);
		soapObject.addProperty("SerId",
				mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",
				mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTranstation = new HttpTransportSE(
				mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")
						+ WebserviceHelper.GtStorageWebservice, 8000);
		httpTranstation.debug = true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		try {
			httpTranstation.call("http://guantang.net/"
					+ WebserviceHelper.GetHpInfo_top_search_complex_1_0, envelope);
			Object result =  envelope.getResponse();
			if(result!=null&&!result.toString().equals("anyType{}")){
				return result.toString();
			}else{
				return "";
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		}
	}

//	public static int GetMaxID_search_complex(int ckid, String search,
//			SharedPreferences mSharedPreferences) {
//		SoapObject soapObject = new SoapObject("http://guantang.net/",
//				WebserviceHelper.GetMaxID_search_complex);
//		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
//				SoapEnvelope.VER11);
//		soapObject.addProperty("ckid", ckid);
//		soapObject.addProperty("search", search);
//		soapObject.addProperty("SerId",
//				mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
//		soapObject.addProperty("LoginFlag",
//				mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
//		envelope.bodyOut = soapObject;
//		envelope.dotNet = true;
//		envelope.encodingStyle = "UTF-8";
//		HttpTransportSE httpTranstation = new HttpTransportSE(
//				mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")
//						+ WebserviceHelper.GtStorageWebservice, 8000);
//		httpTranstation.debug = true;
//		Element[] header = getSoapHeader(mSharedPreferences);
//		envelope.headerOut = header;
//		try {
//			httpTranstation.call("http://guantang.net/"
//					+ WebserviceHelper.GetHpInfo_top_search_complex, envelope);
//			SoapObject result = (SoapObject) envelope.getResponse();
//			if (result!=null&&!result.toString().equals("anyType{}")) {
//				return Integer.parseInt(result.toString());
//			} else {
//				return -1;
//			}
//		} catch (IOException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//			return -1000;
//		} catch (XmlPullParserException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//			return -1001;
//		}
//
//	}

	public static List<Map<String, Object>> GtCKHZ(String[] str, int ckid,
			String fromtime, String totime, SharedPreferences mSharedPreferences) {
		SoapObject soapObject = new SoapObject("http://guantang.net/",
				WebserviceHelper.GtCKHZ);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		soapObject.addProperty("dt1", fromtime);
		soapObject.addProperty("dt2", totime);
		soapObject.addProperty("ckid", ckid);
		soapObject.addProperty("SerId",
				mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",
				mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTranstation = new HttpTransportSE(
				mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")
						+ WebserviceHelper.GtStorageWebservice, 8000);
		httpTranstation.debug = true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			httpTranstation.call("http://guantang.net/"+ WebserviceHelper.GtCKHZ, envelope);
			SoapObject result = (SoapObject) envelope.getResponse();
			if (result != null) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				for (int j = 0; j < str.length; j++) {
					if (result.getProperty(str[j]) == null) {
						map.put(str[j], 0);
					} else {
						map.put(str[j],
								DecimalsHelper.Transfloat(Double.parseDouble(result
										.getProperty(str[j]).toString()),MyApplication.getInstance().getJePoint()));
					}
				}
				list.add(map);
			}
			return list;

		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return list;
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return list;
		}
	}
	
	public static String edit_tb_hp_HPTBM(int id,String HPTM,SharedPreferences mSharedPreferences){
		SoapObject soapObject = new SoapObject("http://guantang.net/",WebserviceHelper.EDIT_TB_HP_HPTBM);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapObject.addProperty("id", id);
		soapObject.addProperty("HPTBM", HPTM);
		soapObject.addProperty("SerId",
				mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",
				mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTranstation = new HttpTransportSE(
				mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")
						+ WebserviceHelper.GtStorageWebservice, 8000);
		httpTranstation.debug = true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		
		try {
			httpTranstation.call("http://guantang.net/"+ WebserviceHelper.EDIT_TB_HP_HPTBM, envelope);
			Object result =  envelope.getResponse();
			if(result!=null&&!result.toString().equals("anyType{}")){
				return result.toString();
			}else{
				return "修改失败";
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "修改失败";
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "修改失败";
		}
	}
	public static String GetImageName(String hpid,SharedPreferences mSharedPreferences){
		SoapObject soapObject = new SoapObject("http://guantang.net/",WebserviceHelper.GetImageName);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapObject.addProperty("hpid",hpid);
		soapObject.addProperty("SerId",
				mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",
				mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTranstation = new HttpTransportSE(
				mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")
						+ WebserviceHelper.GtStorageWebservice, 8000);
		httpTranstation.debug = true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		try {
			httpTranstation.call("http://guantang.net/"+ WebserviceHelper.GetImageName, envelope);
			Object result =  envelope.getResponse();
			if(result!=null&&!result.toString().equals("anyType{}")){
				return result.toString();
			}else{
				return "";
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		}
	}
	public static int DeleteImage(int hpid,String imageName,SharedPreferences mSharedPreferences){
		SoapObject soapObject = new SoapObject("http://guantang.net/",WebserviceHelper.DeleteImage);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapObject.addProperty("id", hpid);
		soapObject.addProperty("name", imageName);
		soapObject.addProperty("SerId",
				mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",
				mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTranstation = new HttpTransportSE(
				mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")
						+ WebserviceHelper.GtStorageWebservice, 8000);
		httpTranstation.debug = true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		try {
			httpTranstation.call("http://guantang.net/"+ WebserviceHelper.DeleteImage, envelope);
			Object result =  envelope.getResponse();
			if(result!=null&&!result.toString().equals("anyType{}")){
				return Integer.valueOf(result.toString());
			}else{
				return -1;
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return -1;
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return -1;
		}
	}
	
	public static String DeleteImageRecord(String Imageurl,int  hpid,SharedPreferences mSharedPreferences){
		SoapObject soapObject = new SoapObject("http://guantang.net/",WebserviceHelper.DeleteImageRecord);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapObject.addProperty("Imageurl", Imageurl);
		soapObject.addProperty("id", hpid);
		soapObject.addProperty("SerId",
				mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",
				mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTranstation = new HttpTransportSE(
				mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")
						+ WebserviceHelper.GtStorageWebservice, 8000);
		httpTranstation.debug = true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		try {
			httpTranstation.call("http://guantang.net/"+ WebserviceHelper.DeleteImageRecord, envelope);
			Object result =  envelope.getResponse();
			if(result!=null&&!result.toString().equals("anyType{}")){
				return result.toString();
			}else{
				return "-1";
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "-1";
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "-1";
		}
	}
	
	public static String AdditionImage(String type,String base64string,int hpid,SharedPreferences mSharedPreferences){
		SoapObject soapObject = new SoapObject("http://guantang.net/",WebserviceHelper.AdditionImage);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapObject.addProperty("id", hpid);
		soapObject.addProperty("base64string",base64string);
		soapObject.addProperty("type",type);
		soapObject.addProperty("SerId",
				mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",
				mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTranstation = new HttpTransportSE(
				mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")
						+ WebserviceHelper.GtStorageWebservice, 8000);
		httpTranstation.debug = true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		try {
			httpTranstation.call("http://guantang.net/"+ WebserviceHelper.AdditionImage, envelope);
			Object result =  envelope.getResponse();
			if(result!=null&&!result.toString().equals("anyType{}")){
				return result.toString();
			}else{
				return "";
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		}
	}
	
	public static String Change_Password_1_0(String name, String oldpwd, String newpwd, SharedPreferences mSharedPreferences){
		SoapObject soapObject = new SoapObject("http://guantang.net/",WebserviceHelper.Change_Password_1_0);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapObject.addProperty("name",name);
		soapObject.addProperty("oldpwd",oldpwd);
		soapObject.addProperty("newpwd",newpwd);
		soapObject.addProperty("SerId",
				mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",
				mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTranstation = new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+ WebserviceHelper.GtStorageWebservice, 8000);
		httpTranstation.debug = true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		try {
			httpTranstation.call("http://guantang.net/"+ WebserviceHelper.Change_Password_1_0, envelope);
			Object result =  envelope.getResponse();
			if(result!=null&&!result.toString().equals("anyType{}")){
				return result.toString();
			}else{
				return "";
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		}
	}
	
	public static String AddCK_1_0(String Json,SharedPreferences mSharedPreferences){
		SoapObject soapObject = new SoapObject("http://guantang.net/",WebserviceHelper.AddCK_1_0);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapObject.addProperty("Json",Json);
		soapObject.addProperty("SerId",mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTranstation = new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+ WebserviceHelper.GtStorageWebservice, 8000);
		httpTranstation.debug = true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		
		try {
			httpTranstation.call("http://guantang.net/"+ WebserviceHelper.AddCK_1_0, envelope);
			Object result =  envelope.getResponse();
			if(result!=null&&!result.toString().equals("anyType{}")){
				return result.toString();
			}else{
				return "";
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		}
	}
	
	public static String GetSingeIOType_1_0(int dirc,SharedPreferences mSharedPreferences){
		SoapObject soapObject = new SoapObject("http://guantang.net/",WebserviceHelper.GetSingeIOType_1_0);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapObject.addProperty("dirc",dirc);
		soapObject.addProperty("SerId",mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTranstation = new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+ WebserviceHelper.GtStorageWebservice, 8000);
		httpTranstation.debug = true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		try {
			httpTranstation.call("http://guantang.net/"+ WebserviceHelper.GetSingeIOType_1_0, envelope);
			Object result =  envelope.getResponse();
			if(result!=null&&!result.toString().equals("anyType{}")){
				return result.toString();
			}else{
				return "";
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		}
	}
	
	public static String AddIotype_1_0(String name,int Iotype,String Dhprev,SharedPreferences mSharedPreferences){
		SoapObject soapObject = new SoapObject("http://guantang.net/",WebserviceHelper.AddIotype_1_0);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapObject.addProperty("name",name);
		soapObject.addProperty("Iotype",Iotype);
		soapObject.addProperty("Dhprev",Dhprev);
		soapObject.addProperty("SerId",mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTranstation = new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+ WebserviceHelper.GtStorageWebservice, 8000);
		httpTranstation.debug = true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		try {
			httpTranstation.call("http://guantang.net/"+ WebserviceHelper.AddIotype_1_0, envelope);
			Object result =  envelope.getResponse();
			if(result!=null&&!result.toString().equals("anyType{}")){
				return result.toString();
			}else{
				return "";
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		}
	}
	
	public static String AddCustomField_1_0(String Json,SharedPreferences mSharedPreferences){
		SoapObject soapObject = new SoapObject("http://guantang.net/",WebserviceHelper.AddCustomField_1_0);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapObject.addProperty("Json",Json);
		soapObject.addProperty("SerId",mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTranstation = new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+ WebserviceHelper.GtStorageWebservice, 8000);
		httpTranstation.debug = true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		try {
			httpTranstation.call("http://guantang.net/"+ WebserviceHelper.AddCustomField_1_0, envelope);
			Object result =  envelope.getResponse();
			if(result!=null&&!result.toString().equals("anyType{}")){
				return result.toString();
			}else{
				return "";
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		}
	}
	
	public static String AddDepType_1_0(String name, int PID, SharedPreferences mSharedPreferences){
		SoapObject soapObject = new SoapObject("http://guantang.net/",WebserviceHelper.AddDepType_1_0);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapObject.addProperty("name",name);
		soapObject.addProperty("PID",PID);
		soapObject.addProperty("SerId",mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTranstation = new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+ WebserviceHelper.GtStorageWebservice, 8000);
		httpTranstation.debug = true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		try {
			httpTranstation.call("http://guantang.net/"+ WebserviceHelper.AddDepType_1_0, envelope);
			Object result =  envelope.getResponse();
			if(result!=null&&!result.toString().equals("anyType{}")){
				return result.toString();
			}else{
				return "";
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		}
	}
	
	public static String AddHpType_1_0(String name,int PID,SharedPreferences mSharedPreferences){
		SoapObject soapObject = new SoapObject("http://guantang.net/",WebserviceHelper.AddHpType_1_0);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapObject.addProperty("name",name);
		soapObject.addProperty("PID",PID);
		soapObject.addProperty("SerId",mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTranstation = new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+ WebserviceHelper.GtStorageWebservice, 8000);
		httpTranstation.debug = true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		try {
			httpTranstation.call("http://guantang.net/"+ WebserviceHelper.AddHpType_1_0, envelope);
			Object result =  envelope.getResponse();
			if(result!=null&&!result.toString().equals("anyType{}")){
				return result.toString();
			}else{
				return "";
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		}
	}
	
	public static String UpdateCK_1_0(int ID, String Json, SharedPreferences mSharedPreferences){
		SoapObject soapObject = new SoapObject("http://guantang.net/",WebserviceHelper.UpdateCK_1_0);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapObject.addProperty("ID",ID);
		soapObject.addProperty("Json",Json);
		soapObject.addProperty("SerId",mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTranstation = new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+ WebserviceHelper.GtStorageWebservice, 8000);
		httpTranstation.debug = true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		try {
			httpTranstation.call("http://guantang.net/"+ WebserviceHelper.UpdateCK_1_0, envelope);
			Object result =  envelope.getResponse();
			if(result!=null&&!result.toString().equals("anyType{}")){
				return result.toString();
			}else{
				return "";
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return"";
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return"";
		}
	}
	
	public static String Gt_MovemByMy_1_0(String strWhere,String orderby,int startIndex,int endIndex,SharedPreferences mSharedPreferences){
		SoapObject soapObject = new SoapObject("http://guantang.net/",WebserviceHelper.Gt_MovemByMy_1_0);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapObject.addProperty("strWhere",strWhere);
		soapObject.addProperty("orderby",orderby);
		soapObject.addProperty("startIndex",startIndex);
		soapObject.addProperty("endIndex",endIndex);
		soapObject.addProperty("SerId",mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTranstation = new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+ WebserviceHelper.GtStorageWebservice, 8000);
		httpTranstation.debug = true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		try {
			httpTranstation.call("http://guantang.net/"+ WebserviceHelper.Gt_MovemByMy_1_0, envelope);
			Object result =  envelope.getResponse();
			if(result!=null&&!result.toString().equals("anyType{}")){
				return result.toString();
			}else{
				return "";
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return"";
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return"";
		}
	}
	
	public static String Gt_MovemByMy_2_0(String strWhere,String orderby,int startIndex,int endIndex,int ckid,SharedPreferences mSharedPreferences){
		SoapObject soapObject = new SoapObject("http://guantang.net/",WebserviceHelper.Gt_MovemByMy_2_0);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapObject.addProperty("strWhere",strWhere);
		soapObject.addProperty("orderby",orderby);
		soapObject.addProperty("startIndex",startIndex);
		soapObject.addProperty("endIndex",endIndex);
		soapObject.addProperty("ckid",ckid);
		soapObject.addProperty("SerId",mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTranstation = new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+ WebserviceHelper.GtStorageWebservice, 8000);
		httpTranstation.debug = true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		try {
			httpTranstation.call("http://guantang.net/"+ WebserviceHelper.Gt_MovemByMy_2_0, envelope);
			Object result =  envelope.getResponse();
			if(result!=null&&!result.toString().equals("anyType{}")){
				return result.toString();
			}else{
				return "";
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return"";
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return"";
		}
	}
	
	public static String Gt_MovemByPermissions_1_0(String strWhere,String orderby,int startIndex,int endIndex,SharedPreferences mSharedPreferences){
		SoapObject soapObject = new SoapObject("http://guantang.net/",WebserviceHelper.Gt_MovemByPermissions_1_0);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapObject.addProperty("strWhere",strWhere);
		soapObject.addProperty("orderby",orderby);
		soapObject.addProperty("startIndex",startIndex);
		soapObject.addProperty("endIndex",endIndex);
		soapObject.addProperty("SerId",mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTranstation = new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+ WebserviceHelper.GtStorageWebservice, 8000);
		httpTranstation.debug = true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		try {
			httpTranstation.call("http://guantang.net/"+ WebserviceHelper.Gt_MovemByPermissions_1_0, envelope);
			Object result =  envelope.getResponse();
			if(result!=null&&!result.toString().equals("anyType{}")){
				return result.toString();
			}else{
				return "";
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return"";
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return"";
		}
	}
	
	public static String Gt_MovemByMyNum_1_0(String MvdtStartTime,String MvdtEndTime,SharedPreferences mSharedPreferences){
		SoapObject soapObject = new SoapObject("http://guantang.net/",WebserviceHelper.Gt_MovemByMyNum_1_0);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapObject.addProperty("MvdtStartTime",MvdtStartTime);
		soapObject.addProperty("MvdtEndTime",MvdtEndTime);
		soapObject.addProperty("SerId",mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTranstation = new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+ WebserviceHelper.GtStorageWebservice, 8000);
		httpTranstation.debug = true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		try {
			httpTranstation.call("http://guantang.net/"+ WebserviceHelper.Gt_MovemByMyNum_1_0, envelope);
			Object result =  envelope.getResponse();
			if(result!=null&&!result.toString().equals("anyType{}")){
				return result.toString();
			}else{
				return "";
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return"";
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return"";
		}
	}
	
	public static String Gt_MovemByMyNum_2_0(String MvdtStartTime,String MvdtEndTime,int ckid,SharedPreferences mSharedPreferences){
		SoapObject soapObject = new SoapObject("http://guantang.net/",WebserviceHelper.Gt_MovemByMyNum_2_0);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapObject.addProperty("MvdtStartTime",MvdtStartTime);
		soapObject.addProperty("MvdtEndTime",MvdtEndTime);
		soapObject.addProperty("ckid",ckid);
		soapObject.addProperty("SerId",mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTranstation = new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+ WebserviceHelper.GtStorageWebservice, 8000);
		httpTranstation.debug = true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		try {
			httpTranstation.call("http://guantang.net/"+ WebserviceHelper.Gt_MovemByMyNum_2_0, envelope);
			Object result =  envelope.getResponse();
			if(result!=null&&!result.toString().equals("anyType{}")){
				return result.toString();
			}else{
				return "";
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return"";
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return"";
		}
	}
	
	public static String Gt_MovemByPermissionsNum_1_0(String MvdtStartTime,String MvdtEndTime,SharedPreferences mSharedPreferences){
		SoapObject soapObject = new SoapObject("http://guantang.net/",WebserviceHelper.Gt_MovemByPermissionsNum_1_0);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapObject.addProperty("MvdtStartTime",MvdtStartTime);
		soapObject.addProperty("MvdtEndTime",MvdtEndTime);
		soapObject.addProperty("SerId",mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTranstation = new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+ WebserviceHelper.GtStorageWebservice, 8000);
		httpTranstation.debug = true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		try {
			httpTranstation.call("http://guantang.net/"+ WebserviceHelper.Gt_MovemByPermissionsNum_1_0, envelope);
			Object result =  envelope.getResponse();
			if(result!=null&&!result.toString().equals("anyType{}")){
				return result.toString();
			}else{
				return "";
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return"";
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return"";
		}
	}
	
	public static String Gt_MovemLevel_1_0(SharedPreferences mSharedPreferences){
		SoapObject soapObject = new SoapObject("http://guantang.net/",WebserviceHelper.Gt_MovemLevel_1_0);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapObject.addProperty("SerId",mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTranstation = new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+ WebserviceHelper.GtStorageWebservice, 8000);
		httpTranstation.debug = true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		try {
			httpTranstation.call("http://guantang.net/"+ WebserviceHelper.Gt_MovemLevel_1_0, envelope);
			Object result =  envelope.getResponse();
			if(result!=null&&!result.toString().equals("anyType{}")){
				return result.toString();
			}else{
				return "";
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return"";
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return"";
		}
	}
	
	public static String GetImageUrl_1_0(String hpid,SharedPreferences mSharedPreferences){
		SoapObject soapObject = new SoapObject("http://guantang.net/",WebserviceHelper.GetImageUrl_1_0);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapObject.addProperty("hpid", hpid);
		soapObject.addProperty("SerId",mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTranstation = new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+ WebserviceHelper.GtStorageWebservice, 8000);
		httpTranstation.debug = true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		try {
			httpTranstation.call("http://guantang.net/"+ WebserviceHelper.GetImageUrl_1_0, envelope);
			Object result =  envelope.getResponse();
			if(result!=null&&!result.toString().equals("anyType{}")){
				return result.toString();
			}else{
				return "";
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return"";
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return"";
		}
	}
	
	public static String GtLastupdate_1_0(SharedPreferences mSharedPreferences){
		SoapObject soapObject = new SoapObject("http://guantang.net/",WebserviceHelper.GtLastupdate_1_0);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		String ssssString = mSharedPreferences.getString(ShareprefenceBean.SERID, "");
		int s1 = mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1);
		soapObject.addProperty("SerId",mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTranstation = new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+ WebserviceHelper.GtStorageWebservice, 8000);
		httpTranstation.debug = true;
		try {
			httpTranstation.call("http://guantang.net/"+ WebserviceHelper.GtLastupdate_1_0, envelope);
			Object result =  envelope.getResponse();
			if(result!=null&&!result.toString().equals("anyType{}")){
				return result.toString();
			}else{
				return "";
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return"";
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return"";
		}
	}
	
	public static String UpdateHP_1_0(String hpid,String Json,String HPTBM,SharedPreferences mSharedPreferences){
		SoapObject soapObject = new SoapObject("http://guantang.net/",WebserviceHelper.UpdateHP_1_0);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapObject.addProperty("id", hpid);
		soapObject.addProperty("HPTBM",HPTBM);
		soapObject.addProperty("Json",Json);
		soapObject.addProperty("SerId",mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTranstation = new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+ WebserviceHelper.GtStorageWebservice, 8000);
		httpTranstation.debug = true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		try {
			httpTranstation.call("http://guantang.net/"+ WebserviceHelper.UpdateHP_1_0, envelope);
			Object result =  envelope.getResponse();
			if(result!=null&&!result.toString().equals("anyType{}")){
				return result.toString();
			}else{
				return "";
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return"";
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return"";
		}
	}
	
	public static String Get_USER_RIGHT(int USER_RIGHTNum,SharedPreferences mSharedPreferences){
		SoapObject soapObject = new SoapObject("http://guantang.net/",WebserviceHelper.Get_USER_RIGHT);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapObject.addProperty("USER_RIGHTNum",USER_RIGHTNum);
		soapObject.addProperty("SerId",mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTranstation = new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+ WebserviceHelper.GtStorageWebservice, 8000);
		httpTranstation.debug = true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		try {
			httpTranstation.call("http://guantang.net/"+ WebserviceHelper.Get_USER_RIGHT, envelope);
			Object result =  envelope.getResponse();
			if(result!=null&&!result.toString().equals("anyType{}")){
				return result.toString();
			}else{
				return "";
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return"";
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return"";
		}
	}
	
	/**
	 * 新登录接口，接口融合了以前登录是复杂的6个接口，优化登录速度
	 * */
	public static String Login_Validate_1_0(String Json){
		SoapObject soapObject = new SoapObject("http://guantang.net/", WebserviceHelper.Login_Validate_1_0);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapObject.addProperty("Json",Json);
		soapObject.addProperty("LoginFlag",WebserviceHelper.loginflag);
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTranstation = new HttpTransportSE(WebserviceHelper.URL+WebserviceHelper.GtStorageWebservice, 15000);
		httpTranstation.debug = true;
		try {
			httpTranstation.call("http://guantang.net/"+ WebserviceHelper.Login_Validate_1_0, envelope);
			Object result = envelope.getResponse();
			if(result!=null&&!result.toString().equals("anyType{}")){
				return result.toString();
			}else{
				return "";
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		}
	}
	
	public static String AndroidApkUpdate(int version){
		SoapObject soapObject = new SoapObject("http://guantang.net/", WebserviceHelper.AndroidApkUpdate);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapObject.addProperty("version",version);
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTranstation = new HttpTransportSE("http://www3.gtcangku.com/"+WebserviceHelper.GtStorageWebservice, 15000);
//		HttpTransportSE httpTranstation = new HttpTransportSE("http://192.168.0.9/GtStorageWebservice.asmx");
		httpTranstation.debug = true;
		try {
			httpTranstation.call("http://guantang.net/"+ WebserviceHelper.AndroidApkUpdate, envelope);
			Object result = envelope.getResponse();
			if(result!=null&&!result.toString().equals("anyType{}")){
				return result.toString();
			}else{
				return "";
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		}
	}
	
	public static String GetCKKC_1_0(String hpid){
		SharedPreferences mSharedPreferences = MyApplication.getInstance().getSharedPreferences();
		SoapObject soapObject = new SoapObject("http://guantang.net/",WebserviceHelper.GetCKKC_1_0);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapObject.addProperty("hpid",hpid);
		soapObject.addProperty("SerId",mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTranstation = new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+ WebserviceHelper.GtStorageWebservice, 8000);
		httpTranstation.debug = true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		try {
			httpTranstation.call("http://guantang.net/"+ WebserviceHelper.GetCKKC_1_0, envelope);
			Object result =  envelope.getResponse();
			if(result!=null&&!result.toString().equals("anyType{}")){
				return result.toString();
			}else{
				return "";
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return"";
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return"";
		}
	}
	
	/**
	 * 获取该帐号所有用户1.0接口
	 * 
	 * 反回结果：
	 * {"Status":"1","Message":"获取用户信息成功","Data":[{"id":1,"name":"admin","pwd":"D577F31D559F8278F3F36B6EADBF6F6F","gid":2,"eid":0,"ckidlist":"",
	 * "rights":"111000000011000000001101011010111110000011111000001101100000111100000011111111101111111110111111110000000000001000000000111111110000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
	 * "hplxindex":"","loginflag":null}]}
	 * */
	public static String Get_User_1_0(){
		SharedPreferences mSharedPreferences = MyApplication.getInstance().getSharedPreferences();
		SoapObject soapObject = new SoapObject("http://guantang.net/", WebserviceHelper.Get_User_1_0);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapObject.addProperty("SerId",mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTranstation = new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+ WebserviceHelper.GtStorageWebservice, 8000);
		httpTranstation.debug = true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		try {
			httpTranstation.call("http://guantang.net/"+ WebserviceHelper.Get_User_1_0, envelope);
			Object result =  envelope.getResponse();
			if(result!=null&&!result.toString().equals("anyType{}")){
				return result.toString();
			}else{
				return "";
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return"";
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return"";
		}
	}
	
	/**
	 * 获取添加用户时的填充信息 员工 用户组 货品类型 仓库
	 * 
	 * 返回结果：
	 * {"Status":"1","Message":"获取用户填充信息成功","Data":{"ck":"","userGroup":"超级管理员,测试用户","employee":"","tb_hplbTree":""}}
	 * */
	public static String Get_AddUserFillInformation_1_0(){
		SharedPreferences mSharedPreferences = MyApplication.getInstance().getSharedPreferences();
		SoapObject soapObject = new SoapObject("http://guantang.net/", WebserviceHelper.Get_AddUserFillInformation_1_0);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTranstation = new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice,8000);
		httpTranstation.debug = true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		try {
			httpTranstation.call("http://guantang.net/"+WebserviceHelper.Get_AddUserFillInformation_1_0, envelope);
			Object result = envelope.getResponse();
			if(result!=null && !result.toString().equals("anyType{}")){
				return result.toString();
			}else{
				return "";
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * 添加用户信息 Json参数 用户名:Username 对应员工:Employee 密码:Password（进行MD5加密） 所在分组:Group 货品类型:Hplb 管辖仓库：Ck
	 * 例：{"Username":"张三","Password":"123","Group":"测试用户","Employee":"张三","Hplb":"成品货物","Ck":"实验仓库"}
	 * */
	public static String Add_User_1_0(String json){
		SharedPreferences mSharedPreferences = MyApplication.getInstance().getSharedPreferences();
		SoapObject soapObject = new SoapObject("http://guantang.net/", WebserviceHelper.Add_User_1_0);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapObject.addProperty("Json", json);
		soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTransportation = new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice, 8000);
		httpTransportation.debug = true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		try {
			httpTransportation.call("http://guantang.net/"+WebserviceHelper.Add_User_1_0, envelope);
			Object result = envelope.getResponse();
			if(result!=null && !result.toString().equals("anyType{}")){
				return result.toString();
			}else{
				return "";
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		}
	}
	
	public static String GetHpInfo_1_0(String type,String top,String id,String direction,String ckid){
		SharedPreferences mSharedPreferences = MyApplication.getInstance().getSharedPreferences();
		SoapObject soapObject = new SoapObject("http://guantang.net/", WebserviceHelper.GetHpInfo_1_0);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapObject.addProperty("type",type);
		soapObject.addProperty("top",top);
		soapObject.addProperty("id",id);
		soapObject.addProperty("direction",direction);
		soapObject.addProperty("ckid",ckid);
		soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTransportation = new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice, 8000);
		httpTransportation.debug = true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		try {
			httpTransportation.call("http://guantang.net/"+WebserviceHelper.GetHpInfo_1_0, envelope);
			Object result = envelope.getResponse();
			if(result!=null && !result.toString().equals("anyType{}")){
				return result.toString();
			}else{
				return "";
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		}
	}
	
	public static String GetHpInfo_search_1_0(String type,String top,String id,String direction,String ckid,String search){
		SharedPreferences mSharedPreferences = MyApplication.getInstance().getSharedPreferences();
		SoapObject soapObject = new SoapObject("http://guantang.net/", WebserviceHelper.GetHpInfo_search_1_0);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapObject.addProperty("type",type);
		soapObject.addProperty("top",top);
		soapObject.addProperty("id",id);
		soapObject.addProperty("direction",direction);
		soapObject.addProperty("ckid",ckid);
		soapObject.addProperty("search",search);
		soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTransportation = new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice, 8000);
		httpTransportation.debug = true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		try {
			httpTransportation.call("http://guantang.net/"+WebserviceHelper.GetHpInfo_search_1_0, envelope);
			Object result = envelope.getResponse();
			if(result!=null && !result.toString().equals("anyType{}")){
				return result.toString();
			}else{
				return "";
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		}
	}
	
	public static String GetHpInfo_top_search_complex_2_0(String top, String id, String direction, String ckid,String search,String ckkc) {
		SharedPreferences mSharedPreferences = MyApplication.getInstance().getSharedPreferences();
		SoapObject soapObject = new SoapObject("http://guantang.net/",
				WebserviceHelper.GetHpInfo_top_search_complex_2_0);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		soapObject.addProperty("top", top);
		soapObject.addProperty("id", id);
		soapObject.addProperty("direction", direction);
		soapObject.addProperty("ckid", ckid);
		soapObject.addProperty("search", search);
		soapObject.addProperty("ckkc",ckkc);
		soapObject.addProperty("SerId",
				mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",
				mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTranstation = new HttpTransportSE(
				mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")
						+ WebserviceHelper.GtStorageWebservice, 8000);
		httpTranstation.debug = true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		try {
			httpTranstation.call("http://guantang.net/"
					+ WebserviceHelper.GetHpInfo_top_search_complex_2_0, envelope);
			Object result =  envelope.getResponse();
			if(result!=null&&!result.toString().equals("anyType{}")){
				return result.toString();
			}else{
				return "";
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		}
	}
	
	public static String GetDDNumInfo(){
		SharedPreferences mSharedPreferences = MyApplication.getInstance().getSharedPreferences();
		SoapObject soapObject = new SoapObject("http://guantang.net/", WebserviceHelper.GetDDNumInfo);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTransportation = new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice, 8000);
		httpTransportation.debug = true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		try {
			httpTransportation.call("http://guantang.net/"+WebserviceHelper.GetDDNumInfo, envelope);
			Object result = envelope.getResponse();
			if(result!=null && !result.toString().equals("anyType{}")){
				return result.toString();
			}else{
				return "";
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		}
	}
	
	public static String GetHP_ByTM_1_0(String tm,boolean ismatch){
		SharedPreferences mSharedPreferences = MyApplication.getInstance().getSharedPreferences();
		SoapObject soapObject = new SoapObject("http://guantang.net/", WebserviceHelper.GetHP_ByTM_1_0);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapObject .addProperty("tm",tm);
		soapObject.addProperty("ismatch",ismatch);
		soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTransportation = new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice, 8000);
		httpTransportation.debug = true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		try {
			httpTransportation.call("http://guantang.net/"+WebserviceHelper.GetHP_ByTM_1_0, envelope);
			Object result = envelope.getResponse();
			if(result!=null && !result.toString().equals("anyType{}")){
				return result.toString();
			}else{
				return "";
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		}
	}
	
	public static String GetHP_ByTM_2_0(String tm,int ckid,boolean ismatch){
		SharedPreferences mSharedPreferences = MyApplication.getInstance().getSharedPreferences();
		SoapObject soapObject = new SoapObject("http://guantang.net/", WebserviceHelper.GetHP_ByTM_2_0);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapObject .addProperty("tm",tm);
		soapObject.addProperty("ismatch",ismatch);
		soapObject .addProperty("ckid",ckid);
		soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTransportation = new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice, 8000);
		httpTransportation.debug = true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		try {
			httpTransportation.call("http://guantang.net/"+WebserviceHelper.GetHP_ByTM_2_0, envelope);
			Object result = envelope.getResponse();
			if(result!=null && !result.toString().equals("anyType{}")){
				return result.toString();
			}else{
				return "";
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		}
	}
	
	public static String AddDDDJ_1_0(String jsonstring,String dh,SharedPreferences mSharedPreferences){
		SoapObject soapObject=new SoapObject("http://guantang.net/",WebserviceHelper.AddDDDJ_1_0);
	    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    soapObject.addProperty("jsonstring", jsonstring);
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
			httpTranstation.call("http://guantang.net/"+WebserviceHelper.AddDDDJ_1_0, envelope);
			Object result=envelope.getResponse();
			if(result!=null&&!result.toString().equals("anyType{}")){
				return result.toString();
			}else{
				return "";
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		}
	}
	
	public static String GetEmployee_1_0(){
		SharedPreferences mSharedPreferences = MyApplication.getInstance().getSharedPreferences();
		SoapObject soapObject = new SoapObject("http://guantang.net/", WebserviceHelper.GetEmployee_1_0);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTransportation = new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice, 8000);
		httpTransportation.debug = true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		try {
			httpTransportation.call("http://guantang.net/"+WebserviceHelper.GetEmployee_1_0, envelope);
			Object result = envelope.getResponse();
			if(result!=null && !result.toString().equals("anyType{}")){
				return result.toString();
			}else{
				return "";
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		}
	}
	
	public static String GetDDAllInfo_1_0(int top, int direction){
		SharedPreferences mSharedPreferences = MyApplication.getInstance().getSharedPreferences();
		SoapObject soapObject = new SoapObject("http://guantang.net/", WebserviceHelper.GetDDAllInfo_1_0);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapObject.addProperty("top",top);
		soapObject.addProperty("direction",direction);
		soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTransportation = new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice, 8000);
		httpTransportation.debug = true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		try {
			httpTransportation.call("http://guantang.net/"+WebserviceHelper.GetDDAllInfo_1_0, envelope);
			Object result = envelope.getResponse();
			if(result!=null && !result.toString().equals("anyType{}")){
				return result.toString();
			}else{
				return "";
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		}
	}
	
	public static String GetDDInfo_1_0(String type, String orderid, String top, String dt1, String dt2, String wherestr, String direction){
		SharedPreferences mSharedPreferences = MyApplication.getInstance().getSharedPreferences();
		SoapObject soapObject = new SoapObject("http://guantang.net/", WebserviceHelper.GetDDInfo_1_0);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapObject.addProperty("type",type);
		soapObject.addProperty("orderid",orderid);
		soapObject.addProperty("top",top);
		soapObject.addProperty("dt1",dt1);
		soapObject.addProperty("dt2",dt2);
		soapObject.addProperty("wherestr",wherestr);
		soapObject.addProperty("direction",direction);
		soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTransportation = new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice, 8000);
		httpTransportation.debug = true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		try {
			httpTransportation.call("http://guantang.net/"+WebserviceHelper.GetDDInfo_1_0, envelope);
			Object result = envelope.getResponse();
			if(result!=null && !result.toString().equals("anyType{}")){
				return result.toString();
			}else{
				return "";
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		}
	}
	
	public static String GetDDLog_1_0(String orderid){
		SharedPreferences mSharedPreferences = MyApplication.getInstance().getSharedPreferences();
		SoapObject soapObject = new SoapObject("http://guantang.net/", WebserviceHelper.GetDDLog_1_0);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapObject.addProperty("orderid",orderid);
		soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTransportation = new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice, 8000);
		httpTransportation.debug = true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		try {
			httpTransportation.call("http://guantang.net/"+WebserviceHelper.GetDDLog_1_0, envelope);
			Object result = envelope.getResponse();
			if(result!=null && !result.toString().equals("anyType{}")){
				return result.toString();
			}else{
				return "";
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		}
	}
	
	public static String GetDDDetail_1_0(String orderid){
		SharedPreferences mSharedPreferences = MyApplication.getInstance().getSharedPreferences();
		SoapObject soapObject = new SoapObject("http://guantang.net/", WebserviceHelper.GetDDDetail_1_0);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapObject.addProperty("orderid",orderid);
		soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTransportation = new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice, 8000);
		httpTransportation.debug = true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		try {
			httpTransportation.call("http://guantang.net/"+WebserviceHelper.GetDDDetail_1_0, envelope);
			Object result = envelope.getResponse();
			if(result!=null && !result.toString().equals("anyType{}")){
				return result.toString();
			}else{
				return "";
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		}
	}
	
	public static String GetCompanyInfo_1_0(String id,String top,int direction,String dwType,String search){
		SharedPreferences mSharedPreferences = MyApplication.getInstance().getSharedPreferences();
		SoapObject soapObject = new SoapObject("http://guantang.net/", WebserviceHelper.GetCompanyInfo_1_0);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapObject.addProperty("id",id);
		soapObject.addProperty("top",top);
		soapObject.addProperty("direction",direction);
		soapObject.addProperty("search",search);
		soapObject.addProperty("dwType",dwType);
		soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTransportation = new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice, 8000);
		httpTransportation.debug = true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		try {
			httpTransportation.call("http://guantang.net/"+WebserviceHelper.GetCompanyInfo_1_0, envelope);
			Object result = envelope.getResponse();
			if(result!=null && !result.toString().equals("anyType{}")){
				return result.toString();
			}else{
				return "";
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		}
	}
	
	public static String GetRdwTree_1_0(){
		SharedPreferences mSharedPreferences = MyApplication.getInstance().getSharedPreferences();
		SoapObject soapObject = new SoapObject("http://guantang.net/", WebserviceHelper.GetRdwTree_1_0);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTransportation = new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice, 8000);
		httpTransportation.debug = true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		try {
			httpTransportation.call("http://guantang.net/"+WebserviceHelper.GetRdwTree_1_0, envelope);
			Object result = envelope.getResponse();
			if(result!=null && !result.toString().equals("anyType{}")){
				return result.toString();
			}else{
				return "";
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		}
	}
	
	public static String AddCompany_1_0(String Json){
		SharedPreferences mSharedPreferences = MyApplication.getInstance().getSharedPreferences();
		SoapObject soapObject = new SoapObject("http://guantang.net/", WebserviceHelper.AddCompany_1_0);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapObject.addProperty("Json", Json);
		soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTransportation = new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice, 8000);
		httpTransportation.debug = true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		try {
			httpTransportation.call("http://guantang.net/"+WebserviceHelper.AddCompany_1_0, envelope);
			Object result = envelope.getResponse();
			if(result!=null && !result.toString().equals("anyType{}")){
				return result.toString();
			}else{
				return "";
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		}
	}
	
	public static String AddCrmlog_1_0(String Json){
		SharedPreferences mSharedPreferences = MyApplication.getInstance().getSharedPreferences();
		SoapObject soapObject = new SoapObject("http://guantang.net/", WebserviceHelper.AddCrmlog_1_0);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapObject.addProperty("Json", Json);
		soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTransportation = new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice, 8000);
		httpTransportation.debug = true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		try {
			httpTransportation.call("http://guantang.net/"+WebserviceHelper.AddCrmlog_1_0, envelope);
			Object result = envelope.getResponse();
			if(result!=null && !result.toString().equals("anyType{}")){
				return result.toString();
			}else{
				return "";
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		}
	}
	
	public static String GetCrmlog_1_0(String dwid){
		SharedPreferences mSharedPreferences = MyApplication.getInstance().getSharedPreferences();
		SoapObject soapObject = new SoapObject("http://guantang.net/", WebserviceHelper.GetCrmlog_1_0);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapObject.addProperty("dwid", dwid);
		soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTransportation = new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice, 8000);
		httpTransportation.debug = true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		try {
			httpTransportation.call("http://guantang.net/"+WebserviceHelper.GetCrmlog_1_0, envelope);
			Object result = envelope.getResponse();
			if(result!=null && !result.toString().equals("anyType{}")){
				return result.toString();
			}else{
				return "";
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		}
	}
	
	public static String GetCompanyDD_1_0(int top,String id,int direction,String dwid){
		SharedPreferences mSharedPreferences = MyApplication.getInstance().getSharedPreferences();
		SoapObject soapObject = new SoapObject("http://guantang.net/", WebserviceHelper.GetCompanyDD_1_0);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapObject.addProperty("top", top);
		soapObject.addProperty("id", id);
		soapObject.addProperty("direction", direction);
		soapObject.addProperty("dwid", dwid);
		soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTransportation = new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice, 8000);
		httpTransportation.debug = true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		try {
			httpTransportation.call("http://guantang.net/"+WebserviceHelper.GetCompanyDD_1_0, envelope);
			Object result = envelope.getResponse();
			if(result!=null && !result.toString().equals("anyType{}")){
				return result.toString();
			}else{
				return "";
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		}
	}
	
	public static String GetCompanyMoved_1_0(int top,String id,int direction,String dwid){
		SharedPreferences mSharedPreferences = MyApplication.getInstance().getSharedPreferences();
		SoapObject soapObject = new SoapObject("http://guantang.net/", WebserviceHelper.GetCompanyMoved_1_0);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapObject.addProperty("top", top);
		soapObject.addProperty("id", id);
		soapObject.addProperty("direction", direction);
		soapObject.addProperty("dwid", dwid);
		soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTransportation = new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice, 8000);
		httpTransportation.debug = true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		try {
			httpTransportation.call("http://guantang.net/"+WebserviceHelper.GetCompanyMoved_1_0, envelope);
			Object result = envelope.getResponse();
			if(result!=null && !result.toString().equals("anyType{}")){
				return result.toString();
			}else{
				return "";
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		}
	}
	
	public static String EditCompany_1_0(String Json){
		SharedPreferences mSharedPreferences = MyApplication.getInstance().getSharedPreferences();
		SoapObject soapObject = new SoapObject("http://guantang.net/", WebserviceHelper.EditCompany_1_0);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapObject.addProperty("Json", Json);
		soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTransportation = new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice, 8000);
		httpTransportation.debug = true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		try {
			httpTransportation.call("http://guantang.net/"+WebserviceHelper.EditCompany_1_0, envelope);
			Object result = envelope.getResponse();
			if(result!=null && !result.toString().equals("anyType{}")){
				return result.toString();
			}else{
				return "";
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		}
	}
	
	public static String GetRegInfo(){
		SharedPreferences mSharedPreferences = MyApplication.getInstance().getSharedPreferences();
		SoapObject soapObject = new SoapObject("http://guantang.net/", WebserviceHelper.GetRegInfo);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTransportation = new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice, 8000);
		httpTransportation.debug = true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		try {
			httpTransportation.call("http://guantang.net/"+WebserviceHelper.GetRegInfo, envelope);
			Object result = envelope.getResponse();
			if(result!=null && !result.toString().equals("anyType{}")){
				return result.toString();
			}else{
				return "";
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		}
	}
	
	public static String AddDB_1_0(String jsonstring){
		SharedPreferences mSharedPreferences = MyApplication.getInstance().getSharedPreferences();
		SoapObject soapObject = new SoapObject("http://guantang.net/", WebserviceHelper.AddDB_1_0);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapObject.addProperty("jsonstring",jsonstring);
		soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTransportation = new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice, 8000);
		httpTransportation.debug = true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		try {
			httpTransportation.call("http://guantang.net/"+WebserviceHelper.AddDB_1_0, envelope);
			Object result = envelope.getResponse();
			if(result!=null && !result.toString().equals("anyType{}")){
				return result.toString();
			}else{
				return "";
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		}
	}
	
	public static String Gettransm_1_0(String strWhere,String orderby,String startIndex,String endIndex){
		SharedPreferences mSharedPreferences = MyApplication.getInstance().getSharedPreferences();
		SoapObject soapObject = new SoapObject("http://guantang.net/", WebserviceHelper.Gettransm_1_0);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapObject.addProperty("strWhere",strWhere);
		soapObject.addProperty("orderby",orderby);
		soapObject.addProperty("startIndex",startIndex);
		soapObject.addProperty("endIndex",endIndex);
		soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTransportation = new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice, 8000);
		httpTransportation.debug = true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		try {
			httpTransportation.call("http://guantang.net/"+WebserviceHelper.Gettransm_1_0, envelope);
			Object result = envelope.getResponse();
			if(result!=null && !result.toString().equals("anyType{}")){
				return result.toString();
			}else{
				return "";
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		}
	}
	
	public static String Gettransd_1_0(String mid){
		SharedPreferences mSharedPreferences = MyApplication.getInstance().getSharedPreferences();
		SoapObject soapObject = new SoapObject("http://guantang.net/", WebserviceHelper.Gettransd_1_0);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapObject.addProperty("mid",mid);
		soapObject.addProperty("SerId", mSharedPreferences.getString(ShareprefenceBean.SERID, ""));
		soapObject.addProperty("LoginFlag",mSharedPreferences.getInt(ShareprefenceBean.LOGIN_FLAG, 1));
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.encodingStyle = "UTF-8";
		HttpTransportSE httpTransportation = new HttpTransportSE(mSharedPreferences.getString(ShareprefenceBean.NET_URL, "")+WebserviceHelper.GtStorageWebservice, 8000);
		httpTransportation.debug = true;
		Element[] header = getSoapHeader(mSharedPreferences);
		envelope.headerOut = header;
		try {
			httpTransportation.call("http://guantang.net/"+WebserviceHelper.Gettransd_1_0, envelope);
			Object result = envelope.getResponse();
			if(result!=null && !result.toString().equals("anyType{}")){
				return result.toString();
			}else{
				return "";
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		}
	}
	
}
