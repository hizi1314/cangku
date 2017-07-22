package com.guantang.cangkuonline.dialog;

import java.util.concurrent.Semaphore;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.webservice.WebserviceImport;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddtypeDialog extends Dialog implements
		android.view.View.OnClickListener {

	private int type;
	private Context context;
	private TextView dialogTitle, cancelTextView, confirmTextView;
	private EditText typeEditText, prevEditText;
	
	public interface OnMyClickListener{
		void execute(String typeString,String prevString);
	}
	public OnMyClickListener mOnMyClickListener;
	public void setOnMyClicklistener(OnMyClickListener mOnMyClickListener){
		this.mOnMyClickListener = mOnMyClickListener;
	}
	
	public AddtypeDialog(Context context, int type,int theme) {
		super(context,theme);
		// TODO 自动生成的构造函数存根
		this.type = type;
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_addtype);
		TextView dialogTitle = (TextView) this.findViewById(R.id.dialogTitle);
		typeEditText = (EditText) this.findViewById(R.id.typeEditText);
		prevEditText = (EditText) this.findViewById(R.id.prevEditText);
		cancelTextView = (TextView) this.findViewById(R.id.cancel);
		confirmTextView = (TextView) this.findViewById(R.id.confirm);
		if (type == 1) {
			dialogTitle.setText("添加入库类型");
			prevEditText.setText("RK-");
		} else if (type == 2) {
			dialogTitle.setText("添加出库类型");
			prevEditText.setText("CK-");
		}
		cancelTextView.setOnClickListener(this);
		confirmTextView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch(v.getId()){
		case R.id.cancel:
			dismiss();
			break;
		case R.id.confirm:
			if(getTypeString().equals("")){
				Toast.makeText(getContext(), "添加类型不能为空", Toast.LENGTH_SHORT).show();
			}else{
				mOnMyClickListener.execute(getTypeString(), getPrevString());
			}
			dismiss();
			break;
		}
	}
	
	public String getTypeString(){
		return typeEditText.getText().toString().trim();
	}
	
	public String getPrevString(){
		return prevEditText.getText().toString().trim();
	}
	
}
