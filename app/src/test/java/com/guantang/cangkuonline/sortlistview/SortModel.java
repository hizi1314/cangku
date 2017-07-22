package com.guantang.cangkuonline.sortlistview;

import java.util.Map;

//SortModel 一个实体类，里面一个是ListView的name,另一个就是显示的name拼音的首字母
public class SortModel {

	private Map<String,Object> myMap;   //显示的数据
	private String sortLetters;  //显示数据拼音的首字母
	
	
	public Map<String, Object> getMyMap() {
		return myMap;
	}
	public void setMyMap(Map<String, Object> myMap) {
		this.myMap = myMap;
	}
	public String getSortLetters() {
		return sortLetters;
	}
	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}
}
