package com.mgw.member.js.dao;

import android.webkit.JavascriptInterface;

/**
 * js与本地交互接口（定义公共操作部分）
 * 
 * @author huyan
 * 
 */
public interface JsCnative {
	/** js调用对象 */
	public String mgwjs = "mgwjs";
	/**
	 * android中的方法名字是 showToast
	 */
	public static String SHOW_TOAST = "showToast";

	/**
	 * 返回用户信息（json对象键值对）
	 * 
	 * @return Created by huyan
	 */
	@JavascriptInterface
	public void getUserInfo();

	/**
	 * 跳转控制
	 * 
	 * @param url
	 * @param parm
	 *            Created by huyan
	 */
	@JavascriptInterface
	public void transferTo(String url, String parm);

	/**
	 * 会员绑定店铺
	 * 
	 * @param groupid
	 * @param uid
	 *            Created by huyan
	 * 
	 * 
	 */
	@JavascriptInterface
	public void setClientInfo(String groupid, String uid, String sid);

	/**
	 * 咨询
	 * 
	 * @param userid环信
	 *            id Created by huyan
	 * 
	 * 
	 */
	@JavascriptInterface
	public void toConsulter(String userid);

	/**
	 * 进入城市联盟商家页
	 * 
	 * @param sid
	 *            Created by huyan
	 * 
	 * 
	 */
	@JavascriptInterface
	public void toSupplier(String sid);

	/**
	 * 进入城市联盟商品页
	 * 
	 * @param sid
	 * @param pid
	 *            Created by huyan
	 * 
	 * 
	 */
	@JavascriptInterface
	public void buyInSupplier(String sid, String pid);

	
	
	
	
}
