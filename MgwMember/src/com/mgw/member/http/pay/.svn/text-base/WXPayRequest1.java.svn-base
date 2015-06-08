package com.mgw.member.http.pay;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import com.mgw.member.R;
import com.mgw.member.constant.Define_C;
import com.mgw.member.uitls.LogUtils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 微信支付(v3)
 */
public class WXPayRequest1 {
	private static final String TAG = WXPayRequest1.class.getSimpleName();

	PayReq req;
	StringBuffer sb;
	Map<String, String> resultunifiedorder;
	private final IWXAPI api;
	Context mContext;
	JSONObject mItem = null;

	// feb4d7b6ad242d966f0d0780eb83b639
	// c2f53c437e6f1f26477150510088b724

	public WXPayRequest1(Context context, JSONObject obj) {
		mContext = context;
		req = new PayReq();
		sb = new StringBuffer();
		api = WXAPIFactory.createWXAPI(mContext, null);
		api.registerApp(Define_C.APP_ID);
		mItem = obj;
	}

	public void WXPay() {
		GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
		getPrepayId.execute();
	}

	private class GetPrepayIdTask extends AsyncTask<Void, Void, Map<String, String>> {

		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(mContext, mContext.getString(R.string.app_tip), mContext.getString(R.string.getting_prepayid));
		}

		@Override
		protected void onPostExecute(Map<String, String> result) {
			if (dialog != null) {
				dialog.dismiss();
			}
			sb.append("prepay_id\n" + result.get("prepay_id") + "\n\n");
			LogUtils.i(TAG, sb.toString());

			resultunifiedorder = result;

			genPayReq(resultunifiedorder);
			sendPayReq();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected Map<String, String> doInBackground(Void... params) {

			String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
			String entity = genProductArgs();

			Log.e("orion", entity);

			
			byte[] buf = Util.httpPost(url, entity);

			String content = new String(buf);
			Log.e("orion", content);
			Map<String, String> xml = decodeXml(content);

			return xml;
		}
	}

	private void genPayReq(Map<String, String> resultunifiedorder) {
		// PayReq req = new PayReq();
		// req.appId = Define_C.APP_ID;
		// req.partnerId = Define_C.PARTNER_ID;
		// req.prepayId = result.prepayId;
		// req.nonceStr = nonceStr;
		// req.timeStamp = String.valueOf(timeStamp);
		// req.packageValue = "Sign=" + packageValue;
		//
		// List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		// signParams.add(new BasicNameValuePair("appid", req.appId));
		// // signParams.add(new BasicNameValuePair("appkey",
		// Define_C.APP_KEY));
		// signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
		// signParams.add(new BasicNameValuePair("package", req.packageValue));
		// signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
		// signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
		// signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
		// req.sign = genSign(signParams);
		//
		// api.sendReq(req);
		//
		//

		req.appId = Define_C.APP_ID;
		req.partnerId = Define_C.PARTNER_ID;
		req.prepayId = resultunifiedorder.get("prepay_id");
		req.packageValue = "prepay_id=" + resultunifiedorder.get("prepay_id");
		// TODO
		req.nonceStr = genNonceStr();
		req.timeStamp = String.valueOf(genTimeStamp());

		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		signParams.add(new BasicNameValuePair("appid", req.appId));
		signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
		signParams.add(new BasicNameValuePair("package", req.packageValue));
		signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
		signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
		signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

		req.sign = genAppSign(signParams);

		sb.append("sign\n" + req.sign + "\n\n");

		LogUtils.i(TAG, sb.toString());

		Log.e("orion", signParams.toString());

	}

	private void sendPayReq() {

		api.registerApp(Define_C.APP_ID);
		api.sendReq(req);
	}

	private String genAppSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(Define_C.PARTNER_KEY);

		this.sb.append("sign str\n" + sb.toString() + "\n\n");
		String appSign = MD5.getMessageDigest(sb.toString().getBytes());
		Log.e("orion", appSign);
		return appSign;
	}

	public Map<String, String> decodeXml(String content) {

		try {
			Map<String, String> xml = new HashMap<String, String>();
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(new StringReader(content));
			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {

				String nodeName = parser.getName();
				switch (event) {
				case XmlPullParser.START_DOCUMENT:

					break;
				case XmlPullParser.START_TAG:

					if ("xml".equals(nodeName) == false) {
						// 实例化student对象
						xml.put(nodeName, parser.nextText());
					}
					break;
				case XmlPullParser.END_TAG:
					break;
				}
				event = parser.next();
			}

			return xml;
		} catch (Exception e) {
			Log.e("orion", e.toString());
		}
		return null;

	}

	/**
	 * 获得产品参数
	 * 
	 * @return Created by Administrator
	 */
	private String genProductArgs() {
		StringBuffer xml = new StringBuffer();
		try {
			String nonceStr = genNonceStr();
			xml.append("</xml>");
			
			float fee = Float.parseFloat(mItem.getString("amount"));
			String strFee = String.format("%d", (int) (fee * 100));
			
			List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
			packageParams.add(new BasicNameValuePair("appid", Define_C.APP_ID));
			packageParams.add(new BasicNameValuePair("body", mItem.getString("pname").toString()));
//			packageParams.add(new BasicNameValuePair("body", "mgw"));
			packageParams.add(new BasicNameValuePair("mch_id", Define_C.PARTNER_ID));
			packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
			packageParams.add(new BasicNameValuePair("notify_url", mItem.getString("notify_url")));
			packageParams.add(new BasicNameValuePair("out_trade_no", mItem.getString("trade_no")));
			packageParams.add(new BasicNameValuePair("spbill_create_ip", "196.168.1.1"));
			packageParams.add(new BasicNameValuePair("total_fee", strFee));
			packageParams.add(new BasicNameValuePair("trade_type", "APP"));

			String sign = genPackageSign(packageParams);
			packageParams.add(new BasicNameValuePair("sign", sign));
			String xmlstring = toXml(packageParams);
			Log.e(TAG, "genProductArgs  = " + xmlstring.toString());
			
			
			return xmlstring;

		} catch (Exception e) {
			Log.e(TAG, "genProductArgs fail, ex = " + e.getMessage());

			return null;
		}

	}
	private String genOutTradNo() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
	}
	/**
	 * 生成签名
	 */

	private String genPackageSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		// API密钥，在商户平台设置
		sb.append(Define_C.PARTNER_KEY);
		String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
		Log.e("orion", packageSign);
		return packageSign;
	}

	private String toXml(List<NameValuePair> params) throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		for (int i = 0; i < params.size(); i++) {
			sb.append("<" + params.get(i).getName() + ">");

			sb.append(params.get(i).getValue());
			sb.append("</" + params.get(i).getName() + ">");
		}
		sb.append("</xml>");

		Log.e("orion", sb.toString());
		
		
		String string = new String(sb.toString().getBytes(), "ISO8859-1");
		
		
		return string; 
	}

	private String genNonceStr() {
		Random random = new Random();
		return  MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
	}

	private long genTimeStamp() {
		return System.currentTimeMillis() / 1000;
	}
	

}
