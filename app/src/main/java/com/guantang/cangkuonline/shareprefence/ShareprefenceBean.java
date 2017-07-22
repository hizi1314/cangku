package com.guantang.cangkuonline.shareprefence;

public class ShareprefenceBean {
	public static String SHAREPREFENCE_NAME="shareprefence";
	public static String SHAREPREFENCE_DB_CONF="db_conf";
	public static String LOGIN_FLAG="loginflag"; //loginflag   是1是注册制0是独立域名
	public static String IS_LOGIN="isLogin";//islogin 是否在线登陆过  1是在线登陆  -1离线登陆
	public static String SER_SELECT="ser_select";//服务器选择  0官方服务器(上海),1官方服务器(广东),2官方服务器(北京),3独立域名用户
	public static String ISFIRST_LOGIN="isfirst_login";//是否是第一次登录,true表示第一次登录，false表示不是第一次登录
	public static String LIXIAN="lixian";//离线登录，true是离线登陆，false 不是离线等录
	public static String JIZHUMIMA="jizhumima";//true是记住密码，false 不记住密码
	public static String SHOWPASS="showpass";//true是显示密码，false不显示密码
	public static String TIYANZHANGHAO="tiyanzhanghao";//1是体验帐号，0不是体验账号
	public static String SERID="serid";
	public static String RIGHTS="rights";
	
	public static String GetVERSIONCODE="getVersionCode";//获取app版本号
	
	public static String SERVER_NUM="server_num";//保存选择的第几个服务器，0 上海，1 广东，2北京，3其他(独立域名服务器)
	public static String USERNAME="username";
	public static String PASSWORD="password";
	
	public static String LIXIAN_USERNAME="lixianusername";
	public static String LIXIAN_PASSWORD="lixianpassword";
	public static String DWNAME_LOGIN="dwname_login";//登录时的单位名称
	public static String NET_URL="net_url";//上次登录服务器地址
//	public static String HISTORY_USER="history_user";//历史登录用户
//	public static String CKID_LIST="ckidlist";//仓库权限，规定使用这能看那几个仓库
	public static String FIRST_PROMPT_DJ="firstPrompt_dj";////1 第一次提示网页端需要审核，2不提示
	
//	--------------------------单位切换需要清空的配置信息-----------------------------------
	public static String UPDATE_TIME_HP="update_time_hp";//货品信息更新状态
	public static String UPDATE_TIME_CKKC="update_time_CKKC";//仓库库存信息更新状态
	public static String UPDATE_TIME_LB="update_time_lb";//货品类型更新状态
	public static String UPDATE_TIME_DW="update_time_dw";//往来单位信息更新状态
	public static String UPDATE_TIME_TYPE="update_time_type";//参数信息更新状态
	public static String UPDATE_TIME_CK="update_time_ck";//仓库信息更新状态
	public static String SHOUYE_CKMC="shouye_ckmc";//仓库名称
	public static String SHOUYE_CKID="shouye_ckid";//仓库id
//	public static String CUSTOM_DW="custom_dw";
	
	public static String SERVICE_IDN_URL1="service_idn_url1";//第一个服务器地址
	public static String SERVICE_IDN_URL2="service_idn_url2";//第二个服务器地址
	public static String SERVICE_IDN_URL3="service_idn_url3";//第三个服务器地址
	public static String SERVICE_NAME1="service_name1";//第一个服务器名称
	public static String SERVICE_NAME2="service_name2";//第二个服务器名称
	public static String SERVICE_NAME3="service_name3";//第三个服务器名称
	public static String ALONE_SERVICE_NAME="alone_service_name";//独立域名服务器名称
	public static String IDN_ALONE_URL="idn_alone_url";//独立域名地址
	
	public static String GRIDVIEW_ISFIRST="gridview_isfirst";//true 是第一次使用，false 不是第一次使用
	public static String GRIDVIEW_ITEM0="gridview_item0";//显示gridview第一项
	public static String GRIDVIEW_ITEM1="gridview_item1";//显示gridview第二项
	public static String GRIDVIEW_ITEM2="gridview_item2";//显示gridview第三项
	public static String GRIDVIEW_ITEM3="gridview_item3";//显示gridview第四项
	public static String GRIDVIEW_ITEM4="gridview_item4";//显示gridview第五项
	public static String GRIDVIEW_ITEM5="gridview_item5";//显示gridview第六项
	public static String GRIDVIEW_ITEM6="gridview_item6";//显示gridview第七项
	public static String GRIDVIEW_ITEM7="gridview_item7";//显示gridview第八项
	public static String GRIDVIEW_IMG0="gridview_img0";//显示gridview第一项 图片
	public static String GRIDVIEW_IMG1="gridview_img1";//显示gridview第二项图片
	public static String GRIDVIEW_IMG2="gridview_img2";//显示gridview第三项图片
	public static String GRIDVIEW_IMG3="gridview_img3";//显示gridview第四项图片
	public static String GRIDVIEW_IMG4="gridview_img4";//显示gridview第五项图片
	public static String GRIDVIEW_IMG5="gridview_img5";//显示gridview第六项图片
	public static String GRIDVIEW_IMG6="gridview_img6";//显示gridview第七项图片
	public static String GRIDVIEW_IMG7="gridview_img7";//显示gridview第八项图片
	
	public static String MIWENPASSWORD = "miwenpassword";//密文密码
	public static String AREYOULOAD="areyouload";//是否在快速切换时提示更新
	
	//-------------------------货品历史搜索-----------------------
	public static String HISTORYSEARCHITEM_1 = "historysearchitem_1";
	public static String HISTORYSEARCHITEM_2 = "historysearchitem_2";
	public static String HISTORYSEARCHITEM_3 = "historysearchitem_3";
	public static String HISTORYSEARCHITEM_4 = "historysearchitem_4";
	public static String HISTORYSEARCHITEM_5 = "historysearchitem_5";
	
	//---------------公共shareprefence--------------------
	public static String NET_URL2 = "net_url2";//公共接口地址
	public static String NOT_UPDATA = "not_updata";//暂不更新
	public static String SerConfLastModifyTime = "SerConfLastModifyTime";//服务端参数上次修改时间
	
	
}
