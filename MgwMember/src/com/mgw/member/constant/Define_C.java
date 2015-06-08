package com.mgw.member.constant;

import android.os.Environment;

/**
 * @author huyan
 * 
 */

public class Define_C {

	/** 手机串号 */
	public static String s_DeviceId = "";

	/** 消费的商户ID */
	public static String s_shopingId = "";

	/** 操作员ID */
	public static String s_old = "";

	/** 消费的店铺名称 */
	public static String s_shopingName = "";

	/** 折扣 */
	public static String s_discount = "";

	/** 返利金 */
	public static String s_return_money = "";

	/** 订单号 */
	public static String s_orderId = "";

	/** 是否删除 类型 */
	public static byte s_isDelete = 0;
	/** 联系人是否有变更 */
	public static Boolean s_HasChangeContent = false;
	/** 是否需要刷新contactlist */
	public static Boolean s_shouldflushcontactlist = true;
	/** 是否注册了环信 */
	public static String s_RelationID = "";
	/** 环信是否登录 */
	public static Boolean bHxLogined = false;
	public static boolean coveimg = false;
	public static boolean firstimg = false;
	public static boolean nexttimg = false;
	public static boolean thirdtimg = false;
	public static String coveimgid = "";
	public static String firstimgid = "";
	public static String nexttimgid = "";
	public static String thirdtimgid = "";

	 public static final String ONLINE_UPDATE_URL ="http://Android5.mgw.cc/index.aspx";
	 public static String mgw_url = "http://android5.mgw.cc/index.aspx";
	 public static String mgw_url2 = "http://android5.mgw.cc";

	/**
	 * 储存用户名标签
	 */
	public static String PREF_USERNAME = "username";
	/**
	 * 储存密码标签
	 */
	public static final String PREF_PWD = "pwd";
	/** 个人地盘url */
	public static final String MY_INFO_URL = "file://" + Environment.getExternalStorageDirectory().getPath() + "/mgw" + "/unziphtml" + "/html" + "/userarea.html";
	/** 主页 */
	public static final String HOME_URL = "file://" + Environment.getExternalStorageDirectory().getPath() + "/mgw" + "/unziphtml" + "/html" + "/index.html";
	// public static final String HOME_URL =
	// "file:///android_asset/html2/default.htm";
	/** 消息 */
	public static final String NEWS_URL = "file://" + Environment.getExternalStorageDirectory().getPath() + "/mgw" + "/unziphtml" + "/html" + "/userarea.html";
	/** 发现 */
	public static final String FIND_URL = "file://" + Environment.getExternalStorageDirectory().getPath() + "/mgw" + "/unziphtml" + "/html" + "/discover.html";
	/** 商城 */
	public static final String MALL_URL = "http://Androidweb.mgw.cc/default.aspx?type=2&a=6Ufv2BEgRXjOLg7EaNRBv6zXZIwLtRQy";

	

	public static final String PARTNER_KEY = "asdsfsafasf";
	public static final String PARTNER_ID = "1dasdfsafs2";
	public static final String APP_ID = "wx2d3asdsfdaac9";
	public static final String APP_SECRET = "b1f74asdfsfsadfwe6da85cf1";//

	public static class ShowMsgActivity {
		public static final String STitle = "showmsg_title";
		public static final String SMessage = "showmsg_message";
		public static final String BAThumbData = "showmsg_thumb_data";
	}
}