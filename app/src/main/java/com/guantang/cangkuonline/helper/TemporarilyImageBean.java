package com.guantang.cangkuonline.helper;

import android.graphics.Bitmap;

/*
 * 存放临时图片和信息
 * */

public class TemporarilyImageBean {
	private String ImageName;
	private Bitmap bm;
	private Boolean Deletethis;
	public TemporarilyImageBean(String imageName, Bitmap bm, Boolean Deletethis) {
		super();
		ImageName = imageName;
		this.bm = bm;
		this.Deletethis = Deletethis;
	}
	public String getImageName() {
		return ImageName;
	}
	public void setImageName(String imageName) {
		ImageName = imageName;
	}
	public Bitmap getBm() {
		return bm;
	}
	public void setBm(Bitmap bm) {
		this.bm = bm;
	}
	public Boolean getDeletethis() {
		return Deletethis;
	}
	public void setDeletethis(Boolean deletethis) {
		Deletethis = deletethis;
	}

}
