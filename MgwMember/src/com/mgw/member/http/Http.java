package com.mgw.member.http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import u.aly.de;

import android.util.Log;

import com.fz.core.net.RequestHelper;
import com.fz.core.security.MD5;
import com.mgw.member.constant.Define_C;
import com.mgw.member.constant.imp_Define;
import com.mgw.member.ui.activity.MainActivity;

/**
 * @author 欧阳嘉 2014-7-7 上午11:52:18
 */

public class Http implements imp_Define {


	
	
	public static final String TOKEN = "";
	public static final String KEY = "";
	public static final String FORMAT = "json";
	public static final String VERSION = "1.0";

	/** 正式地址 */
	// public static final String Define_C.ONLINE_UPDATE_URL_CART =
	// "http://cart.api.yoobaby.net";

	/**
	 * 挂单
	 * 
	 * @param userid
	 *            自己的用户名
	 * @oaram supplierid 商户的编号
	 * */
	public static JSONObject postShoping(String userid, String supplierid,
			String oid) throws JSONException {
		String url;
		if (IS_SEVER) {
			url = Define_C.ONLINE_UPDATE_URL;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("type", "user.getbill");
		params.put("userid", userid);
		params.put("supplierid", supplierid);
		params.put("oid", oid);
		params.put("token", "");
		params.put("key", "");
		params.put("format", "json");

		String resultString = RequestHelper.PostBySingleBitmap(url, params,
				null);
		if (resultString == null || resultString.equals("")) {
			return null;
		}
		Log.i("postShoping", resultString);

		JSONTokener jsonParser = new JSONTokener(resultString);
		return (JSONObject) jsonParser.nextValue();
	}

	/**
	 * 得到用户的头像和用户名
	 * 
	 * @param userid
	 *            自己的用户名
	 * */
	public static JSONObject postUesrs(String userid) throws JSONException {
		String url;
		if (IS_SEVER) {
			url = Define_C.ONLINE_UPDATE_URL;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("type", "user.getuserinfo");
		params.put("userid", userid);
		params.put("token", "");
		params.put("key", "");
		params.put("format", "json");

		String resultString = RequestHelper.PostBySingleBitmap(url, params,
				null);

		if (resultString == null || resultString.equals("")) {
			return null;
		}
		Log.i("postUesrs", resultString);

		JSONTokener jsonParser = new JSONTokener(resultString);
		return (JSONObject) jsonParser.nextValue();
	}

	/**
	 * 查找用户名老获得用户
	 * 
	 * @param userid
	 *            自己的用户名
	 * */
	public static JSONObject postGetUesrsForName(String name)
			throws JSONException {
		String url;
		if (IS_SEVER) {
			url = Define_C.ONLINE_UPDATE_URL;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("type", "user.queryuser");
		params.put("pkey", name);
		params.put("token", "");
		params.put("key", "");
		params.put("format", "json");

		String resultString = RequestHelper.PostBySingleBitmap(url, params,
				null);

		if (resultString == null || resultString.equals("")) {
			return null;
		}
		Log.i("postGetUesrsForName", resultString);

		JSONTokener jsonParser = new JSONTokener(resultString);
		return (JSONObject) jsonParser.nextValue();
	}

	/** 绑定 */
	public static JSONObject postUesrs2(String UserID, String RelationID)
			throws JSONException {
		String url;
		if (IS_SEVER) {
			url = Define_C.ONLINE_UPDATE_URL;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("type", "user.updaterelationid");
		params.put("UserID", UserID);
		params.put("RelationID", RelationID);
		params.put("token", "");
		params.put("key", "");
		params.put("format", "json");

		String resultString = RequestHelper.PostBySingleBitmap(url, params,
				null);

		if (resultString == null || resultString.equals("")) {
			return null;
		}
		Log.i("postUesrs2", resultString);

		JSONTokener jsonParser = new JSONTokener(resultString);
		return (JSONObject) jsonParser.nextValue();
	}

	/**
	 * 得到订单详情
	 * 
	 * @param userid
	 *            自己的用户名
	 * @oaram supplierid 商户的编号
	 * */
	public static JSONObject postShopingDetails(String orderId,
			String supplierid, String oid) throws JSONException {
		String url;
		if (IS_SEVER) {
			url = Define_C.ONLINE_UPDATE_URL;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("type", "cashier.getbillitem");
		params.put("fmbid", orderId);
		params.put("sid", supplierid);
		params.put("oid", oid);
		params.put("token", "");
		params.put("key", "");
		params.put("format", "json");

		String resultString = RequestHelper.PostBySingleBitmap(url, params,
				null);

		if (resultString == null || resultString.equals("")) {
			return null;
		}
		Log.i("postShopingDetails", resultString);

		JSONTokener jsonParser = new JSONTokener(resultString);
		return (JSONObject) jsonParser.nextValue();
	}

	/**
	 * 得到订单详情
	 * 
	 * @param userid
	 *            自己的用户名
	 * @oaram supplierid 商户的编号
	 * */
	public static JSONObject postShopingDetails2(String orderId,
			String supplierid, String oid) throws JSONException {
		String url;
		if (IS_SEVER) {
			url = Define_C.ONLINE_UPDATE_URL;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("type", "cashier.getfinishbill");
		params.put("fmbid", orderId);
		params.put("sid", supplierid);
		params.put("oid", oid);
		params.put("token", "");
		params.put("key", "");
		params.put("format", "json");

		String resultString = RequestHelper.PostBySingleBitmap(url, params,
				null);

		if (resultString == null || resultString.equals("")) {
			return null;
		}
		Log.i("postShopingDetails", resultString);

		JSONTokener jsonParser = new JSONTokener(resultString);
		return (JSONObject) jsonParser.nextValue();
	}

	/**
	 * 改变订单状态
	 * 
	 * @param fmbid
	 *            ：挂单编号
	 * @param userid
	 *            ： 用户编号
	 * @param sid
	 *            ：商家编号
	 * @param oid
	 *            ：操作员编号
	 * @param payment
	 *            ：支付方式
	 * 
	 * @oaram supplierid 商户的编号
	 * */
	public static JSONObject postOrderChengeState(String fmbid, String userid,
			String sid, String oid, String payment) throws JSONException {
		// “cashier.updatepayment”
		// 请求参数为：
		String url;
		if (IS_SEVER) {
			url = Define_C.ONLINE_UPDATE_URL;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("type", "cashier.updatepayment");
		params.put("fmbid", fmbid);
		params.put("userid", userid);
		params.put("sid", sid);
		params.put("oid", oid);
		params.put("payment", payment);
		params.put("token", "");
		params.put("key", "");
		params.put("format", "json");

		String resultString = RequestHelper.PostBySingleBitmap(url, params,
				null);

		if (resultString == null || resultString.equals("")) {
			return null;
		}
		Log.i("postOrderChengeState", resultString);

		JSONTokener jsonParser = new JSONTokener(resultString);
		return (JSONObject) jsonParser.nextValue();
	}

	/**
	 * 完成订单
	 * 
	 * @param FSupplier_ID
	 *            ：商家编号
	 * @param FUser_ID
	 *            ： 用户编号
	 * @param FMB_ID
	 *            ：订单编号
	 * @param FMB_Payment
	 *            ：手机支付1， 店内支付2
	 * @param FMBI_ID
	 *            ：明细编号
	 * @param number2
	 *            流水号
	 * 
	 * @oaram supplierid 商户的编号
	 * */
	public static JSONObject postOrderFinish(String FSupplier_ID,
			String FUser_ID, String FMB_ID, String FMB_Payment, String FMBI_ID,
			String number2) throws JSONException {
		// “cashier.updatepayment”
		// 请求参数为：
		String url;
		if (IS_SEVER) {
			url = Define_C.ONLINE_UPDATE_URL;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("type", "supplier.finishbill");
		params.put("FSupplier_ID", FSupplier_ID);
		params.put("FUser_ID", FUser_ID);
		params.put("FMB_ID", FMB_ID);
		params.put("FMB_Payment", FMB_Payment);
		params.put("FMBI_ID", FMBI_ID);
		params.put("FMBI_TradeCode", number2);
		params.put("FMBI_PayType", "0");
		params.put("token", "");
		params.put("key", "");
		params.put("format", "json");

		String resultString = RequestHelper.PostBySingleBitmap(url, params,
				null);

		if (resultString == null || resultString.equals("")) {
			return null;
		}
		Log.i("postOrderFinish", resultString);

		JSONTokener jsonParser = new JSONTokener(resultString);
		return (JSONObject) jsonParser.nextValue();
	}

	/**
	 * 得到商圈列表
	 * 
	 * @param groupid
	 *            ：用户集团编号
	 * @param userid
	 *            ：用户编号
	 * @param lastid
	 *            ：已获取的最后一条推广信息编号，没有则为0
	 * */
	public static JSONObject postGetShopings(String groupid, String userid,
			String lastid) throws JSONException {
		String url;
		if (IS_SEVER) {
			url = Define_C.ONLINE_UPDATE_URL;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("type", "user.getappadsend");
		params.put("groupid", groupid);
		params.put("userid", userid);
		params.put("lastid", lastid);
		params.put("token", "");
		params.put("key", "");
		params.put("format", "json");

		String resultString = RequestHelper.PostBySingleBitmap(url, params,
				null);

		if (resultString == null || resultString.equals("")) {
			return null;
		}
		Log.i("postGetShopings", resultString);

		JSONTokener jsonParser = new JSONTokener(resultString);
		return (JSONObject) jsonParser.nextValue();
	}

	/** 得到关注的商家 */
	public static JSONObject postGetShoping(String userid) throws JSONException {
		String url;
		if (IS_SEVER) {
			url = Define_C.ONLINE_UPDATE_URL;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("type", "user.getattentionlist");
		params.put("userid", userid);
		params.put("token", "");
		params.put("key", "");
		params.put("format", "json");

		String resultString = RequestHelper.PostBySingleBitmap(url, params,
				null);

		if (resultString == null || resultString.equals("")) {
			return null;
		}
		Log.i("postGetShoping", resultString);

		JSONTokener jsonParser = new JSONTokener(resultString);
		return (JSONObject) jsonParser.nextValue();
	}

	/** 得到订单的状态 */
	public static JSONObject postGetOrderState(String orderId)
			throws JSONException {
		String url;
		if (IS_SEVER) {
			url = Define_C.ONLINE_UPDATE_URL;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("type", "cashier.getbillstatus");
		params.put("FMB_ID", orderId);
		params.put("token", "");
		params.put("key", "");
		params.put("format", "json");

		String resultString = RequestHelper.PostBySingleBitmap(url, params,
				null);

		if (resultString == null || resultString.equals("")) {
			return null;
		}
		Log.i("postGetOrderState", resultString);

		JSONTokener jsonParser = new JSONTokener(resultString);
		return (JSONObject) jsonParser.nextValue();
	}

	/**
	 * 推送消息给商户
	 * 
	 * @param userid
	 *            发送的用户ID 也是标签
	 * */
	public static JSONObject postShopingOrder(String userid, String message)
			throws JSONException {
		String url;
		if (IS_SEVER) {
			url = "http://channel.api.duapp.com/rest/2.0/channel/channel";
		}
		Long t_Long = System.currentTimeMillis();
		Map<String, String> params = new HashMap<String, String>();
		params.put("method", "push_msg");
		params.put("apikey", "7TRDske5fCtHcbC28dSzuIHm");
		params.put("push_type", "2");
		params.put("tag", userid);
		params.put("messages", message);
		params.put("msg_keys", "1");
		params.put("timestamp", String.valueOf(t_Long));

		ArrayList<String> t_strings = new ArrayList<String>();

		t_strings.add("apikey=7TRDske5fCtHcbC28dSzuIHm");
		t_strings.add("messages=" + message);
		t_strings.add("method=push_msg");
		t_strings.add("msg_keys=" + "1");
		t_strings.add("push_type=2");
		t_strings.add("tag=" + userid);
		t_strings.add("timestamp=" + String.valueOf(t_Long));

		String t_str = "";

		for (int t_i = 0; t_i < t_strings.size(); t_i++) {
			t_str += t_strings.get(t_i);
		}

		t_str += "aL6CBOHRBKzNyg6BIucPGYoVumrgB8WU";

		try {
			params.put("sign", MD5.ToMD5(URLEncoder.encode("POST"
					+ "http://channel.api.duapp.com/rest/2.0/channel/channel"
					+ t_str, "UTF-8")));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		String resultString = RequestHelper.PostBySingleBitmap(url, params,
				null);

		if (resultString == null || resultString.equals("")) {
			return null;
		}
		Log.i("postShopingOrder", resultString);

		JSONTokener jsonParser = new JSONTokener(resultString);
		return (JSONObject) jsonParser.nextValue();
	}

	/**
	 * 获取分公司发布的广告
	 * 
	 * 
	 * */
	public static JSONObject GetcomadData(String userid, String serial)
			throws JSONException {
		String url;
		if (IS_SEVER) {
			url = Define_C.ONLINE_UPDATE_URL;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("type", "member.getcomad");
		params.put("userid", userid);
		params.put("serial", serial);
		params.put("token", "");
		params.put("key", "");
		params.put("format", "json");

		String resultString = RequestHelper.PostBySingleBitmap(url, params,
				null);
		if (resultString == null || resultString.equals("")) {
			return null;
		}
		Log.i("postUesrs", resultString);

		JSONTokener jsonParser = new JSONTokener(resultString);
		return (JSONObject) jsonParser.nextValue();
	}

	/**
	 * 获取代理商发布的广告
	 * 
	 * 
	 * */
	public static JSONObject GetAgentad(String userid, String serial)
			throws JSONException {
		String url;
		if (IS_SEVER) {
			url = Define_C.ONLINE_UPDATE_URL;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("type", "member.getagentad");
		params.put("userid", userid);
		params.put("serial", serial);
		params.put("token", "");
		params.put("key", "");
		params.put("format", "json");

		String resultString = RequestHelper.PostBySingleBitmap(url, params,
				null);
		if (resultString == null || resultString.equals("")) {
			return null;
		}
		Log.i("postUesrs", resultString);

		JSONTokener jsonParser = new JSONTokener(resultString);
		return (JSONObject) jsonParser.nextValue();
	}

	/**
	 * 获取代理商发布的广告
	 * 
	 * 
	 * */
	public static JSONObject Getplatinumad(String userid, String serial,
			int index) throws JSONException {
		String url;
		if (IS_SEVER) {
			url = Define_C.ONLINE_UPDATE_URL;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("type", "member.getplatinumad");
		params.put("userid", userid);
		params.put("serial", serial);
		params.put("pindex", index + "");
		params.put("token", "");
		params.put("key", "");
		params.put("format", "json");

		String resultString = RequestHelper.PostBySingleBitmap(url, params,
				null);
		if (resultString == null || resultString.equals("")) {
			return null;
		}
		Log.i("postUesrs", resultString);

		JSONTokener jsonParser = new JSONTokener(resultString);
		return (JSONObject) jsonParser.nextValue();
	}

	/**
	 * 获取当前店可用消费券
	 * 
	 * 
	 * */
	public static JSONObject canusecoupon(String userid, String sid)
			throws JSONException {

		String url;
		if (IS_SEVER) {
			url = Define_C.mgw_url;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("type", "coupon.usercoupon");
		params.put("userid", userid);
		params.put("sid", sid);
		String resultString = RequestHelper.PostBySingleBitmap(url, params,
				null);
		if (resultString == null || resultString.equals("")) {
			return null;
		}
		Log.i("postUesrs", resultString);

		JSONTokener jsonParser = new JSONTokener(resultString);
		return (JSONObject) jsonParser.nextValue();
	}

	// 获取收集支付时已完成单据
	public static JSONObject GetFinishbill(String fmbid, String sid, String oid)
			throws JSONException {
		Map<String, String> params = new HashMap<String, String>();
		String url = Define_C.ONLINE_UPDATE_URL;
		params.put("token", TOKEN);
		params.put("key", KEY);
		params.put("format", FORMAT);
		params.put("version", VERSION);
		params.put("type", "cashier.getfinishbill");
		params.put("fmbid", fmbid);
		params.put("sid", sid);
		params.put("oid", oid);
		String resultString = RequestHelper.PostBySingleBitmap(url, params,
				null);
		if (resultString == null) {
			return null;
		}
		JSONTokener jsonParser = new JSONTokener(resultString);
		return (JSONObject) jsonParser.nextValue();
	}
}
