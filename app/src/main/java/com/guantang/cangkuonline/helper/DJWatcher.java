package com.guantang.cangkuonline.helper;

import com.guantang.cangkuonline.application.MyApplication;

import android.text.Editable;
import android.text.TextWatcher;

public class DJWatcher implements TextWatcher{

	@Override
	public void afterTextChanged(Editable s) {
		// TODO 自动生成的方法存根
		int pos = s.length() - 1;
		int position = s.toString().indexOf(".");
		
		if (pos - position > MyApplication.getInstance().getDjPoint() && position != -1) {
			s.delete(pos, pos + 1);
		}else if(pos>10 && position == -1){
			s.delete(pos, pos + 1);
		}
		if (position == -1 && s.toString().length() > 10) {
			char[] numberStrings = s.toString().toCharArray();
			if (numberStrings.length > 10 && !String.valueOf(numberStrings[10]).equals(".")) {// 如果第11位不是小数点就删除
				s.delete(10, s.toString().length());
			}
		}
		
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO 自动生成的方法存根
		
	}

}
