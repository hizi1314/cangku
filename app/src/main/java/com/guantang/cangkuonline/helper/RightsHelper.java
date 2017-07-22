package com.guantang.cangkuonline.helper;

import android.content.SharedPreferences;

import com.guantang.cangkuonline.shareprefence.ShareprefenceBean;



public class RightsHelper {
	public static int system_usermanage = 0;
    public static int system_config = 1;
    public static int system_cw = 2;//财务
    //货品
    public static int hp_addedit = 10;
    public static int hp_del = 11;
    //单位
    public static int dw_view_GYS = 20;
    public static int dw_addedit_GYS = 21;
    public static int dw_edit_GYS = 22;
    public static int dw_del_GYS = 23;

    public static int dw_view_KH = 25;
    public static int dw_addedit_KH = 26;
    public static int dw_edit_KH = 27;
    public static int dw_del_KH = 28;
    //入库
    public static int dj_rk_show = 30;
    public static int dj_rk_add = 31;
    public static int dj_rk_edit = 32;
    public static int dj_rk_del = 33;
    //出库
    public static int dj_ck_show = 40;
    public static int dj_ck_add = 41;
    public static int dj_ck_edit = 42;
    public static int dj_ck_del = 43;
    //盘点
    public static int dj_pd_show = 50;
    public static int dj_pd_add = 51;
    //public static int dj_pd_edit = 52;
    public static int dj_pd_del = 53;
    //采购订单
    public static int dd_cg_add = 70;
    public static int dd_cg_showself = 71;
    public static int dd_cg_showall = 72;
    public static int dd_cg_editself = 73;
    public static int dd_cg_editall = 74;
    public static int dd_cg_sh = 75;
    public static int dd_cg_cw = 76;
    public static int dd_cg_del = 77;
    public static int dd_cg_do = 78;
    //销售订单
    public static int dd_xs_add = 80;
    public static int dd_xs_showself = 81;
    public static int dd_xs_showall = 82;
    public static int dd_xs_editself = 83;
    public static int dd_xs_editall = 84;
    public static int dd_xs_sh = 85;
    public static int dd_xs_cw = 86;
    public static int dd_xs_del = 87;
    public static int dd_xs_do = 88;
    //领用申请订单
    public static int dd_ly_add = 90;
    public static int dd_ly_showself = 91;
    public static int dd_ly_showall = 92;
    public static int dd_ly_editself = 93;
    public static int dd_ly_editall = 94;
    public static int dd_ly_sh = 95;
    public static int dd_ly_del = 96;
    public static int dd_ly_do = 97;

    public static int tj = 110;
    public static Boolean isHaveRight(int n,SharedPreferences mSharedPreferences){
    	if(mSharedPreferences.getString(ShareprefenceBean.RIGHTS, "").equals("")){
    		return false;
    	}else{
    		String str=mSharedPreferences.getString(ShareprefenceBean.RIGHTS, "").substring(n,n+1);
    		return str.equals("1");
    	}
    	
    }
}
