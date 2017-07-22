package com.guantang.cangkuonline.database;

import java.security.PublicKey;



public class DataBaseHelper {
	
	public static int DB_VERSION=5;//数据库当前版本,升级时数字加一
	
	public static final String DB="strorage_moblie_ol.db";
	public static final String TB_HP="tb_hp"; //货品信息
	public static final String TB_CKKC="tb_ckkc";//仓库库存（关联货品和仓库）
	public static final String TB_CK="tb_ck";//仓库信息
	public static final String TB_MoveD="tb_moved";//出入库明细
	public static final String TB_MoveM="tb_movem";//出入库单据
	public static final String TB_hplbTree="tb_hplbTree";//货品列表
	public static final String TB_Company="tb_company";//往来单位
	public static final String TB_Conf="tb_conf";//配置
	public static final String TB_depTree="tb_depTree";//部门列表
	public static final String TB_Order="tb_order";//订单
	public static final String TB_OrderDetail="tb_orderDetail";//订单明细
	public static final String TB_PIC = "tb_pic";//货品图片表
	public static final String TB_LOGIN = "tb_login";//登录信息表
	public static final String TB_TRANSD = "tb_transd";//调拨明细表
	public static final String TB_TRANSM = "tb_transm";//调拨单据表
	
	public static final String ID="id";
	public static final String HPMC="hpmc";
	public static final String HPBM="hpbm";
	public static final String HPTM="hptm";
	public static final String GGXH="ggxh";
	public static final String JLDW="jldw";
	public static final String SCCS="sccs"; //生产厂商
	public static final String JLDW2="jldw2";
	public static final String LBS="lbs";//类别描述
	public static final String LBID="lbid";
	public static final String RKCKJ="rkckj";//入库参考价
	public static final String CKCKJ="ckckj";//出库参考价
	public static final String HPSX="hpsx";//货品上限
	public static final String HPXX="hpxx";//货品下限
	public static final String BZ="bz"; //备注
	public static final String CurrKC="CurrKC";//当前库存
	public static final String LBIndex="LBindex";//类别索引
	public static final String CKCKJ2="ckckj2";
	public static final String BigNum="bignum";//换算比例
	public static final String KCJE="kcje";//库存金额
	public static final String Type="type";//新添加标记    1:新添加  0：从系统导入
	public static final String GXDate="gxdate";//更新时间
	public static final String RES1="res1";//扩展字段1
	public static final String RES2="res2";
	public static final String RES3="res3";
	public static final String RES4="res4";
	public static final String RES5="res5";
	public static final String RES6="res6";
	public static final String RESD1="resd1";//日期扩展字段1
	public static final String RESD2="resd2";
	public static final String HPPY="hppy";//货品拼音
	public static final String StopTag="StopTag";//停用标记
	public static final String ImagePath="imgpath";
	
	public static final String HPID="hpid";
	public static final String CKID="ckid";
	public static final String KCSL="kcsl"; //某一仓库某个货品的库存数
	
	public static final String CKMC="ckmc";
	public static final String FZR="fzr";
	public static final String TEL="tel";
	public static final String ADDR="addr";
	public static final String INACT="inact"; //默认入库类型
	public static final String OUTACT="outact";//默认出库类型
	
	public static final String HPD_ID="hpd_id"; //明细ID
	public static final String BZKC="bzkc"; //操作前总库存
	public static final String AZKC="azkc";//操作后总库存
	public static final String BTKC="btkc"; //操作前本仓库库存
	public static final String ATKC="atkc"; //操作后本仓库库存
	public static final String MID="mid"; //单据ID
	public static final String MVDDATE="mvddt";//出入库时间
	public static final String MVType="dactType"; //出入库类型
	public static final String MVDirect="mvddirect"; //出入库标记 1：入；2：出    盘点标记1：入；2：出；
	public static final String MSL="msl";  //出入库数量
	public static final String DJ="dj"; 
	public static final String ZJ="zj";
	public static final String MVDType="mvdType";//操作类型 0盘点1入库2出库3调拨
	public static final String DH="mvddh";
	public static final String Bkcje="bkcje"; //出入库前的库存金额
	public static final String DDDH="ordIndex";//订单单号
	public static final String TAX="tax";//税率
	public static final String SQDJ="sqdj";//税前单价
	public static final String SQZJ="sqzj";//税前总价
	public static final String RESF1="resf1";//明细扩展字段1 数字
	public static final String RESF2="resf2";
	public static final String MSL2="msl2";//换算数量
	public static final String DWXS="dwxs";//换算系数
	
	public static final String HPM_ID="hpm_id";//单据ID
	public static final String MVDH="mvdh";//单据单号
	public static final String MVDDH="mvddh";//明细单号
	public static final String MVDT="mvdt";//单据表单据日期
	public static final String MVDDT="mvddt";//明细表单据日期
	public static final String DepName="depName";//部门名称
	public static final String DepID="depId";//部门ID
	public static final String DWName="dwName";//对方单位名称
	public static final String JBR="jbr";//经办人
	public static final String hpDetails="hpDetails";//出入库货品描述
	public static final String Details="Details";//出入库货品描述
	public static final String actType="actType";//出入库类型
	public static final String mType="mType";//操作类型  0盘点1入库2出库3调拨
	public static final String DWID="dwid";//单位ID
	public static final String bigJLDW="bigJLDW";//是否启用了单位换算
	public static final String DJType="DJType";//单据状态  -1：未上传或未导出  1：已上传或已导出，0代表没有保存
	public static final String Lrr="lrr";
	public static final String Lrsj="lrsj";
	public static final String Tjsj="tjsj";
	public static final String HPzj="hpzj";
	
	public static final String Name="name";
	public static final String Lev="lev";//树级别
	public static final String PID="PID";//父节点ID
	public static final String Ord="ord";//序号
	public static final String Sindex="sindex";//索引
	
	public static final String PY="py"; 
	public static final String Email="email";
	public static final String Net="net";
	public static final String LXR="lxr";
	public static final String DWtype="dwtype";
	public static final String YB="yb";//邮编
	public static final String QQ="qq";
	public static final String FAX="fax";
	public static final String Phone="phone";
	
	
	public static final String GID="GID";
	public static final String ItemID="ItemID";
	public static final String ItemV="ItemV";
	
	public static final String depLevel="deplevel";//树级别
	public static final String depOrder="depOrder";//序号
	public static final String depindex="depindex";//索引
	
	public static final String Status="status";
	public static final String Country="country";
	public static final String State="state";
	public static final String CountryCode="CountryCode";
	public static final String City="city";
	public static final String OrderIndex="orderindex";
	public static final String Trades="trades";
	public static final String Sqdt="sqdt";
	public static final String ZipCode="zipcode";
	public static final String Cw="cw";
	public static final String Addr2="addr2";
	public static final String PRI="PRI";
	public static final String zje="zje";
	public static final String yfje="yfje";
	public static final String syje="syje";
	public static final String dirc="dirc";
	public static final String shdt="shdt";
	public static final String shr="shr";
	public static final String shyj="shyj";//审核意见
	public static final String shrID="shrID";
	public static final String sqrID="sqrID";
	public static final String sqr="sqr";
	public static final String ReqDate="ReqDate";
	public static final String sjzje="sjzje";
	public static final String jsfs="jsfs";
		
	public static final String orderID="orderId";
	public static final String ord="ord";
	public static final String zxsl="zxsl";
	public static final String SL="sl";
	public static final String Notes="notes";
	public static final String ddirc="ddirc";
	public static final String yjCbdj="yjCbdj";
	public static final String sjCbdj="sjCbdj";
	public static final String lrje="lrje";
	public static final String yjCbzj="yjCbzj";
	
	public static final String company="company";
	public static final String username="username";
	public static final String password = "password";
	public static final String miwenpassword = "miwenpassword";
	public static final String neturl = "neturl";
	public static final String loginflag = "loginflag";
	public static final String lastlogin = "lastlogin";
	
	public static final String ImageURL = "ImageURL";
	public static final String CompressImageURL = "CompressImageURL";
	public static final String UpLoadStatus = "UpLoadStatus";
	
	public static final String sckName = "sckName";
	public static final String sckid = "sckid";
	public static final String dckName = "dckName";
	public static final String dckid = "dckid";
	public static final String mvdh = "mvdh";
	public static final String mvdt = "mvdt";
	public static final String hpnames = "hpnames";//货品摘要
	public static final String hpzjz = "hpzjz";
	public static final String ActStatus = "ActStatus";
	public static final String Note = "note";
	
}
