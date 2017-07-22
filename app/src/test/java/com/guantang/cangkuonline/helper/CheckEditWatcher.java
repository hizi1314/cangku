package com.guantang.cangkuonline.helper;

import android.text.Editable;
import android.text.TextWatcher;

public class CheckEditWatcher implements TextWatcher{

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		//SQL中'是特殊字符,所以删除'
//			if(s.toString().indexOf("'")>-1){
//				int pos=s.toString().indexOf("'");
//				s.delete(pos,pos+1);
//			}
			s.toString().replace("'", "''");
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
