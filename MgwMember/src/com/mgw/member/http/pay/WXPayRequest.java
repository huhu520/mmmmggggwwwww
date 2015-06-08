package com.mgw.member.http.pay;

import java.security.MessageDigest;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.mgw.member.constant.Define_C;
import com.mgw.member.R;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 微信支付
 */
public class WXPayRequest {
	private final IWXAPI api;
	Context mContext;
	JSONObject mItem = null;

	// feb4d7b6ad242d966f0d0780eb83b639
	// c2f53c437e6f1f26477150510088b724

	public WXPayRequest(Context context, JSONObject obj) {
		mContext = context;
		api = WXAPIFactory.createWXAPI(mContext, null);
		api.registerApp(Define_C.APP_ID);
		mItem = obj;
	}

	public void WXPay() {
		new GetAccessTokenTask().execute();
	}

	private String genPackage(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(Define_C.PARTNER_KEY); // 注意：不能hardcode在客户端，建议genPackage这个过程都由服务器端完成

		// 进行md5摘要前，params内容为原始内容，未经过url encode处理
		String packageSign = getMessageDigest(sb.toString().getBytes()).toUpperCase();

		return URLEncodedUtils.format(params, "utf-8") + "&sign=" + packageSign;
	}

	public final static String getMessageDigest(byte[] buffer) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(buffer);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}

	// /**
	// * Appid
	// */
	// public static final String APP_ID = "wx2d3a5e9147827ac9";
	//
	// /**
	// * 微信公众平台商户模块和商户约定的密钥
	// *
	// * 注意：不能hardcode在客户端，建议genPackage这个过程由服务器端完成
	// */
	// // private static final String PARTNER_KEY =
	// "8eafc1f9c81c1fabb7e2d255a546b6e4";
	//
	// /**
	// * 微信开放平台和商户约定的密钥
	// *
	// * 注意：不能hardcode在客户端，建议genSign这个过程由服务器端完成
	// */
	// // private static final String APP_SECRET =
	// "7d824c680093c16fb488ecf94f340ded"; // wxd930ea5d5a258f4f
	// /**
	// * 微信开放平台和商户约定的支付密钥
	// *
	// * 注意：不能hardcode在客户端，建议genSign这个过程由服务器端完成（客户端生成）
	// */
	// // private static final String APP_KEY =
	// "6jpZQmbQKorTReKxJe4EEwvcqqqj4hQFvjPpN8oWFx7j4jdjinDI8BXEaRCChXp6EvdFFW38nTV6OHhMLZlM6E44aPT3sQ0IY1zaa7KgOGRhwnfyMXloqvOxzjZbD3aJ";
	// // wxd930ea5d5a258f4f
	// 对应的支付密钥
	// TODO 1
	private class GetAccessTokenTask extends AsyncTask<Void, Void, GetAccessTokenResult> {

		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(mContext, "", "");
		}

		@Override
		protected void onPostExecute(GetAccessTokenResult result) {
			if (dialog != null) {
				dialog.dismiss();
			}

			if (result.localRetCode == LocalRetCode.ERR_OK) {
				GetPrepayIdTask getPrepayId = new GetPrepayIdTask(result.accessToken);
				getPrepayId.execute();
			} else {
				Toast.makeText(mContext, mContext.getString(R.string.get_access_token_fail, result.localRetCode.name()), Toast.LENGTH_LONG).show();
			}
		}

		@Override
		protected GetAccessTokenResult doInBackground(Void... params) {
			GetAccessTokenResult result = new GetAccessTokenResult();

			String url = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s", Define_C.APP_ID, Define_C.APP_SECRET);

			byte[] buf = WXUtil.httpGet(url);
			if (buf == null || buf.length == 0) {
				result.localRetCode = LocalRetCode.ERR_HTTP;
				return result;
			}

			String content = new String(buf);
			result.parseFrom(content);
			return result;
		}
	}

	/**
	 * 获得preayid
	 * 
	 */
	private class GetPrepayIdTask extends AsyncTask<Void, Void, GetPrepayIdResult> {

		private ProgressDialog dialog;
		private final String accessToken;

		public GetPrepayIdTask(String accessToken) {
			this.accessToken = accessToken;
		}

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(mContext, "", "");
		}

		@Override
		protected GetPrepayIdResult doInBackground(Void... params) {

			String url = String.format("https://api.weixin.qq.com/pay/genprepay?access_token=%s", accessToken);
			String entity = genProductArgs();

			GetPrepayIdResult result = new GetPrepayIdResult();
			/*
			 * Log.e("wxUrl",url); Log.e("wxentiy",entity);
			 */
			byte[] buf = WXUtil.httpPost(url, entity);
			if (buf == null || buf.length == 0) {
				result.localRetCode = LocalRetCode.ERR_HTTP;
				return result;
			}

			String content = new String(buf);
			result.parseFrom(content);
			return result;
		}

		@Override
		protected void onPostExecute(GetPrepayIdResult result) {
			if (dialog != null) {
				dialog.dismiss();
			}

			if (result.localRetCode == LocalRetCode.ERR_OK) {
				sendPayReq(result);
			} else {
				Toast.makeText(mContext, mContext.getString(R.string.get_prepayid_fail, result.localRetCode.name()), Toast.LENGTH_LONG).show();
			}
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

	}

	private static enum LocalRetCode {
		ERR_OK, ERR_HTTP, ERR_JSON, ERR_OTHER
	}

	/**
	 * AccessToken结果
	 * 
	 * @author Administrator
	 * 
	 */
	private static class GetAccessTokenResult {

		private static final String TAG = "MicroMsg.SDKSample.PayActivity.GetAccessTokenResult";

		public LocalRetCode localRetCode = LocalRetCode.ERR_OTHER;
		public String accessToken;
		public int expiresIn;
		public int errCode;
		public String errMsg;

		public void parseFrom(String content) {

			if (content == null || content.length() <= 0) {
				Log.e(TAG, "parseFrom fail, content is null");
				localRetCode = LocalRetCode.ERR_JSON;
				return;
			}

			try {
				JSONObject json = new JSONObject(content);
				if (json.has("access_token")) { // success case
					accessToken = json.getString("access_token");
					expiresIn = json.getInt("expires_in");
					localRetCode = LocalRetCode.ERR_OK;
				} else {
					errCode = json.getInt("errcode");
					errMsg = json.getString("errmsg");
					localRetCode = LocalRetCode.ERR_JSON;
				}

			} catch (Exception e) {
				localRetCode = LocalRetCode.ERR_JSON;
			}
		}
	}

	/**
	 * PrepayId结果
	 * 
	 * @author Administrator
	 * 
	 */
	private static class GetPrepayIdResult {

		private static final String TAG = "MicroMsg.SDKSample.PayActivity.GetPrepayIdResult";

		public LocalRetCode localRetCode = LocalRetCode.ERR_OTHER;
		public String prepayId;
		public int errCode;
		public String errMsg;

		public void parseFrom(String content) {

			if (content == null || content.length() <= 0) {
				Log.e(TAG, "parseFrom fail, content is null");
				localRetCode = LocalRetCode.ERR_JSON;
				return;
			}

			try {
				JSONObject json = new JSONObject(content);
				if (json.has("prepayid")) { // success case
					prepayId = json.getString("prepayid");
					localRetCode = LocalRetCode.ERR_OK;
				} else {
					localRetCode = LocalRetCode.ERR_JSON;
				}

				errCode = json.getInt("errcode");
				errMsg = json.getString("errmsg");

			} catch (Exception e) {
				localRetCode = LocalRetCode.ERR_JSON;
			}
		}
	}

	private String genNonceStr() {
		Random random = new Random();
		return getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
	}

	private long genTimeStamp() {
		return System.currentTimeMillis() / 1000;
	}

	/**
	 * 建议 traceid 字段包含用户信息及订单信息，方便后续对订单状态的查询和跟踪
	 */
	private String getTraceId() {
		return "crestxu_" + genTimeStamp();
	}

	/**
	 * 注意：商户系统内部的订单号,32个字符内、可包含字母,确保在商户系统唯一
	 */
	private String genOutTradNo() {
		Random random = new Random();
		return getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
	}

	private long timeStamp;
	private String nonceStr, packageValue;

	private String genSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		int i = 0;
		for (; i < params.size() - 1; i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append(params.get(i).getName());
		sb.append('=');
		sb.append(params.get(i).getValue());

		String sha1 = WXUtil.sha1(sb.toString());
		return sha1;
	}

	/**
	 * 获得产品参数
	 * 
	 * @return Created by Administrator
	 */
	private String genProductArgs() {
		JSONObject json = new JSONObject();

		try {
			json.put("appid", Define_C.APP_ID);
			String traceId = getTraceId(); // traceId
			// 由开发者自定义，可用于订单的查询与跟踪，建议根据支付用户信息生成此id
			json.put("traceid", traceId);
			nonceStr = genNonceStr();
			json.put("noncestr", nonceStr);

			float fee = Float.parseFloat(mItem.getString("amount"));
			String strFee = String.format("%d", (int) (fee * 100));

			List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
			packageParams.add(new BasicNameValuePair("bank_type", "WX"));
			packageParams.add(new BasicNameValuePair("body", mItem.getString("pname")));
			packageParams.add(new BasicNameValuePair("fee_type", "1"));
			packageParams.add(new BasicNameValuePair("input_charset", "UTF-8"));
			packageParams.add(new BasicNameValuePair("notify_url", mItem.getString("notify_url")));
			packageParams.add(new BasicNameValuePair("out_trade_no", mItem.getString("trade_no")));
			packageParams.add(new BasicNameValuePair("partner", Define_C.PARTNER_ID));
			packageParams.add(new BasicNameValuePair("spbill_create_ip", "196.168.1.1"));
			packageParams.add(new BasicNameValuePair("total_fee", strFee));
			packageValue = genPackage(packageParams);

			json.put("package", packageValue);
			timeStamp = genTimeStamp();
			json.put("timestamp", timeStamp);

			List<NameValuePair> signParams = new LinkedList<NameValuePair>();
			signParams.add(new BasicNameValuePair("appid", Define_C.APP_ID));
			// signParams.add(new BasicNameValuePair("appkey",
			// Define_C.APP_KEY));
			signParams.add(new BasicNameValuePair("noncestr", nonceStr));
			signParams.add(new BasicNameValuePair("package", packageValue));
			signParams.add(new BasicNameValuePair("timestamp", String.valueOf(timeStamp)));
			signParams.add(new BasicNameValuePair("traceid", traceId));
			json.put("app_signature", genSign(signParams));

			json.put("sign_method", "sha1");
		} catch (Exception e) {
			return null;
		}

		return json.toString();
	}

	/**
	 * 发送支付请求
	 * 
	 * @param result
	 *            Created by Administrator
	 */
	private void sendPayReq(GetPrepayIdResult result) {

		PayReq req = new PayReq();
		req.appId = Define_C.APP_ID;
		req.partnerId = Define_C.PARTNER_ID;
		req.prepayId = result.prepayId;
		req.nonceStr = nonceStr;
		req.timeStamp = String.valueOf(timeStamp);
		req.packageValue = "Sign=" + packageValue;

		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		signParams.add(new BasicNameValuePair("appid", req.appId));
		// signParams.add(new BasicNameValuePair("appkey", Define_C.APP_KEY));
		signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
		signParams.add(new BasicNameValuePair("package", req.packageValue));
		signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
		signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
		signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
		req.sign = genSign(signParams);

		api.sendReq(req);
	}

	public void sendmsgReq(String text) {
		WXTextObject wxTextObject = new WXTextObject();
		wxTextObject.text = text;
		WXMediaMessage mediaMessage = new WXMediaMessage();
		mediaMessage.mediaObject = wxTextObject;
		mediaMessage.description = text;
		SendMessageToWX.Req req = new SendMessageToWX.Req();

		req.transaction = String.valueOf(System.currentTimeMillis());
		req.message = mediaMessage;
		api.sendReq(req);
	}

}