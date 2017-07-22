package com.guantang.cangkuonline.helper;

import android.os.CountDownTimer;

public class TimeCount extends CountDownTimer{
	private  tickImport tick;
	private finishImport finish;
	public TimeCount(long millisInFuture, long countDownInterval) {
		super(millisInFuture, countDownInterval);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onTick(long millisUntilFinished) {
		// TODO Auto-generated method stub
		if(null!=tick){
			tick.onTick(millisUntilFinished);
		}
	}

	@Override
	public void onFinish() {
		// TODO Auto-generated method stub
		if(null!=finish){
			finish.onFinish();
		}
	}
	public interface tickImport{
		public void onTick(long millisInFuture);
	}
	public void setTick(tickImport tick){
		this.tick=tick;
	}
	public interface finishImport{
		public void onFinish();
	}
	public void setFinish(finishImport finish){
		this.finish=finish;
	}
}
