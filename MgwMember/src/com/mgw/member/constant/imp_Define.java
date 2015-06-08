package com.mgw.member.constant;
/**
 * @author 欧阳�?2014-5-13 上午11:14:36
 */

public interface imp_Define
{
	/** 是否测试 */
	public final static boolean IS_BEBUG = false;
	
	/** 是否再用测试服务�? */
	public static final boolean IS_SEVER = true;
	
	/** 商业圈列表 */
	public final static byte MESSAGE_TYPE_SHOPING = 0;
	/** 发送商户编号 */
	public final static byte MESSAGE_TYPE_SHOPING_ID = 1;
	public final static byte MESSAGE_TYPE_SHOPING_ID2 = 11;
	/** 推送消息给商户 */
	public final static byte MESSAGE_TYPE_SHOPING_MESSAGE_1 = 2;
	public final static byte MESSAGE_TYPE_SHOPING_MESSAGE_TIME = 100;
	/** 得到订单详情 */
	public final static byte MESSAGE_TYPE_ORDER_ID = 3;
	/** 得到用户的头像和用户名 */
	public final static byte MESSAGE_TYPE_USERS = 4;
	/** 得到用户的头像和用户名  消息 */
	public final static byte MESSAGE_TYPE_USERS_2 = 5;
	/** 通知改变订单状态 */
	public final static byte MESSAGE_TYPE_STATE_CHENGE = 6;
	/** 绑定 */
	public final static byte MESSAGE_TYPE_BINDING = 7;
	/** 查找好友 */
	public final static byte MESSAGE_TYPE_FIND_FRIEND = 9;
	/** 完成订单 */
	public final static byte MESSAGE_TYPE_STATE_FINISH = 8;
	/** 得到关注的商家 */
	public final static byte MESSAGE_TYPE_GETSHOPING = 10;
	/** 得到订单状态 */
	public final static byte MESSAGE_TYPE_GET_ORDER_ID = 12;
	/**未安装微信**/
	public final static int MESSAGE_TYPE_NOWX = 205;
	/**未找到对应参数**/
	public final static int MESSAGE_TYPE_PARAMERR = 206;
	
	/**微信支付**/
	public final static int MESSAGE_TYPE_WXPAY = 207;
	
	/**网络连接错误**/
	public final static int MESSAGE_TYPE_NETERROR = 208;
	
	/**加载图片**/
	public final static int MESSAGE_TYPE_STARTSETIMAGE = 209;
	/**图片移动到最后一张**/
	public final static int MESSAGE_TYPE_LASTIMAGE = 210;
	
	
	/**退出登录*/
	public final static int MESSAGE_TYPE_USER_ = 211;
	
	/**找不到网页**/
	public final static int MESSAGE_TYPE_WEBPAGER_ERROR = 212;
	/**刷新网页*/
	public final static int MESSAGE_TYPE_WEBPAGER_REFRESH = 213;
	/**网页刷新成功*/
	public final static int MESSAGE_TYPE_WEBPAGER_OK = 214;
	
	
}