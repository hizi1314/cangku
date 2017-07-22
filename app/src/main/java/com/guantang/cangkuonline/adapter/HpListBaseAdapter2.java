package com.guantang.cangkuonline.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.activity.HpListDetailsWriteActivity;
import com.guantang.cangkuonline.application.MyApplication;
import com.guantang.cangkuonline.database.DataBaseHelper;
import com.guantang.cangkuonline.database.DataBaseMethod;
import com.guantang.cangkuonline.database.DataBaseOperateMethod;
import com.guantang.cangkuonline.dialog.AddNumberDialog;
import com.guantang.cangkuonline.helper.DecimalsHelper;

public class HpListBaseAdapter2 extends BaseAdapter {
	private LayoutInflater inflater;
	private Context context;
	private List<Map<String, Object>> mlist = new ArrayList<Map<String, Object>>();
	private DataBaseOperateMethod dm_op;
	private DataBaseMethod dm;
	private String djid = "",dh ="";
	private int op_type=1,ckid=-1;
	private String dacttype = "";

	public HpListBaseAdapter2(Context context,int op_type,String djid,String dh,int ckid,String dacttype) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		dm_op = new DataBaseOperateMethod(context);
		dm = new DataBaseMethod(context);
		this.djid = djid;
		this.op_type=op_type;
		this.dh=dh;
		this.ckid=ckid;
		this.dacttype=dacttype;
	}

	public void setDataList(List<Map<String, Object>> list) {
		mlist = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		return mlist.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO 自动生成的方法存根
		return mlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO 自动生成的方法存根
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView holder =null;
		Boolean flag = true; // true 表示添加货品，表示不添加货品
		if (convertView == null) {
			holder = new HolderView();
			convertView = inflater.inflate(R.layout.listitem2, null);
			holder.itemname = (TextView) convertView
					.findViewById(R.id.itemname);
			holder.itemcode = (TextView) convertView
					.findViewById(R.id.itemcode);
			holder.itemgg = (TextView) convertView.findViewById(R.id.itemgg);
			holder.itemnum = (TextView) convertView.findViewById(R.id.itemnum);
			holder.ImgButton = (ImageButton) convertView.findViewById(R.id.showBtn);
			holder.caozuoTextView = (TextView) convertView.findViewById(R.id.caozuoshu);
			holder.caozuoshuLayout = (LinearLayout) convertView.findViewById(R.id.caozuoshulayout);
			holder.caozuonameTextView = (TextView) convertView.findViewById(R.id.caozuoNameText);
			holder.kucunnameTextView = (TextView) convertView.findViewById(R.id.kucunname);
			convertView.setTag(holder);
		} else {
			holder = (HolderView) convertView.getTag();
		}
		final Map<String, Object> map = mlist.get(position);
		
		Object hpmcObject = map.get(DataBaseHelper.HPMC);
		holder.itemname.setText(hpmcObject==null?"":hpmcObject.toString());

		Object hpbmObject = map.get(DataBaseHelper.HPBM);
		holder.itemcode.setText(hpbmObject==null?"":hpbmObject.toString());

		Object ggxhObject = map.get(DataBaseHelper.GGXH);
		holder.itemgg.setText(ggxhObject==null?"":ggxhObject.toString());
		
		
		if(op_type==1){
			holder.kucunnameTextView.setText("货品总库存：");
			Object currkcObject = map.get(DataBaseHelper.CurrKC);
			holder.itemnum.setText((currkcObject==null ||  currkcObject.toString().endsWith("null"))?"":DecimalsHelper.Transfloat(Double.parseDouble(currkcObject.toString()),MyApplication.getInstance().getNumPoint()));
		}else if(op_type==0||op_type==2){
			holder.kucunnameTextView.setText("本仓库库存：");
			Object kcslObject = map.get(DataBaseHelper.KCSL);
			holder.itemnum.setText((kcslObject==null || kcslObject.toString().endsWith("null"))?"":DecimalsHelper.Transfloat(Double.parseDouble(kcslObject.toString()),MyApplication.getInstance().getNumPoint()));
		}
		
		String numstr = map.get("caozuoshu").toString();
		
		if(op_type==0){
			holder.caozuonameTextView.setText("实际数量:");
			holder.caozuoTextView.setText(numstr);
			if(numstr.equals("无")){
				holder.caozuoshuLayout.setVisibility(View.INVISIBLE);
				holder.ImgButton.setBackgroundResource(R.drawable.hpadd);
			}else{
				holder.caozuoshuLayout.setVisibility(View.VISIBLE);
				holder.ImgButton.setBackgroundResource(R.drawable.hpmodify);
				if(map.get("shuying").toString().equals("盘赢")){
					holder.caozuoTextView.setTextColor(Color.RED);
				}else if(map.get("shuying").toString().equals("盘亏")){
					holder.caozuoTextView.setTextColor(Color.GREEN);
				}
			}
		}else{
			holder.caozuonameTextView.setText("操作数:");
			if(numstr.equals("0")){
				holder.caozuoshuLayout.setVisibility(View.INVISIBLE);
				holder.ImgButton.setBackgroundResource(R.drawable.hpadd);
			}else{
				holder.caozuoshuLayout.setVisibility(View.VISIBLE);
				holder.ImgButton.setBackgroundResource(R.drawable.hpmodify);
			}
			holder.caozuoTextView.setText(numstr);
		}
		
		
		holder.ImgButton.setOnClickListener(new ListViewButtonOnClikListener(
				position));
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent intent = new Intent(context, HpListDetailsWriteActivity.class);
				intent.putExtra("hpid", map.get(DataBaseHelper.ID).toString());
				intent.putExtra("dh", dh);
				intent.putExtra("djid", djid);
				intent.putExtra("ckid", ckid);
				intent.putExtra("op_type", op_type);
				intent.putExtra("dacttype", dacttype);
				context.startActivity(intent);
			}
		});
		return convertView;
	}

	class HolderView {
		public TextView itemname;
		public TextView itemcode;
		public TextView itemgg;
		public TextView itemnum;
		public TextView caozuonameTextView;
		public TextView caozuoTextView;
		public TextView kucunnameTextView;
		public ImageButton ImgButton;
		public LinearLayout caozuoshuLayout;
	}

	class ListViewButtonOnClikListener implements OnClickListener {
		private int position;

		public ListViewButtonOnClikListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			Map<String, Object> map = mlist.get(position);
			AddNumberDialog mAddNumberDialog = new AddNumberDialog(context, op_type, map, djid, dh, ckid, dacttype,"",R.style.ButtonDialogStyle);
//			CallbackInterface mCallbackInterface = new CallbackInterface() {
//				
//				@Override
//				public void CallbackExecute(ObjectFive obj) {
//					// TODO 自动生成的方法存根
//					for(int i=0;i<mlist.size();i++){
//						if(mlist.get(i).get(DataBaseHelper.ID).toString().equals(obj.getHpid())){
//							mlist.get(i).put("caozuoshu", obj.getNum());
//							break;
//						}
//					}
//					notifyDataSetChanged();
//				}
//			};
//			mAddNumberDialog.setCallbackInterface(mCallbackInterface);
			mAddNumberDialog.setCanceledOnTouchOutside(false);
			Window win = mAddNumberDialog.getWindow();
			win.getDecorView().setPadding(0, 0, 0, 0);
			win.setGravity(Gravity.BOTTOM);
			WindowManager.LayoutParams lp = win.getAttributes();
			        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
			        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
			        win.setAttributes(lp);
			mAddNumberDialog.show();
			
//			if (((Button) v).getText().toString().equals("添加")) {
//				((Button) v).setText("修改");
//				((Button) v).setBackgroundResource(R.drawable.shape8);
//				((HP_choseActivity) context).setAddList(map);
//				notifyDataSetChanged();
//
//			} else if (((Button) v).getText().toString().equals("修改")) {
//				((Button) v).setText("添加");
//				((Button) v).setBackgroundResource(R.drawable.shape7);
//				((HP_choseActivity) context).setDelList(map);
//				notifyDataSetChanged();
//				dm_op.Del_Moved(djid, hpid);
//			}
		}
	}
//	public void onEventMainThread(ObjectFive obj) {
//		for(int i=0;i<mlist.size();i++){
//			if(mlist.get(i).get(DataBaseHelper.ID).toString().equals(obj.getHpid())){
//				mlist.get(i).put("caozuoshu", obj.getNum());
//				break;
//			}
//		}
//		notifyDataSetChanged();
//	}
	
}
