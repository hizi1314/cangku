package com.guantang.cangkuonline.eventbusBean;

/**
 * 保存订单筛选信息的，在activity与fragment之间传递使用
 * 
 * */

public class ObjectNine {
	private String sql;
	private String sqfromtime;
	private String sqtotime;

	public ObjectNine(String sql, String sqfromtime, String sqtotime) {
		super();
		this.sql = sql;
		this.sqfromtime = sqfromtime;
		this.sqtotime = sqtotime;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getSqfromtime() {
		return sqfromtime;
	}

	public void setSqfromtime(String sqfromtime) {
		this.sqfromtime = sqfromtime;
	}

	public String getSqtotime() {
		return sqtotime;
	}

	public void setSqtotime(String sqtotime) {
		this.sqtotime = sqtotime;
	}

}
