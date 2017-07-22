package com.guantang.cangkuonline.eventbusBean;

import java.util.Map;

public class ObjectFour {
	private Map<String, Object> map;
	private Boolean thesame;
	
	public ObjectFour(Map<String, Object> map, Boolean thesame) {
		super();
		this.map = map;
		this.thesame = thesame;
	}
	public Map<String, Object> getMap() {
		return map;
	}
	public void setMap(Map<String, Object> map) {
		this.map = map;
	}
	public Boolean getThesame() {
		return thesame;
	}
	public void setThesame(Boolean thesame) {
		this.thesame = thesame;
	}
	
}
