package com.guantang.cangkuonline.mulu;


public class ElementBean {
	private String id;
	private String pid;//元素上一层的父id
	private String name;
	private int lev;//当前对象处在第多少层
	
	public int getLev() {
		return lev;
	}
	public void setLev(int lev) {
		this.lev = lev;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
