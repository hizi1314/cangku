package com.guantang.cangkuonline.eventbusBean;

public class ObjectFive {
	String hpid="";
	String num="";
	String shuying="";
	public ObjectFive(String hpid,String num) {
		super();
		this.hpid = hpid;
		this.num = num;
	}

	public ObjectFive(String hpid, String num, String shuying) {
		super();
		this.hpid = hpid;
		this.num = num;
		this.shuying = shuying;
	}

	public String getShuying() {
		return shuying;
	}

	public void setShuying(String shuying) {
		this.shuying = shuying;
	}

	public String getHpid() {
		return hpid;
	}

	public String getNum() {
		return num;
	}
	
}
