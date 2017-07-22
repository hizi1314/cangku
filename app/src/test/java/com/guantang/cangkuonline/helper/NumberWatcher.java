package com.guantang.cangkuonline.helper;

import com.guantang.cangkuonline.application.MyApplication;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

public class NumberWatcher implements TextWatcher{

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		
		int pos = s.length() - 1;
		int position = s.toString().indexOf(".");
		
		if (pos - position > MyApplication.getInstance().getNumPoint() && position != -1) {
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
	}

}
