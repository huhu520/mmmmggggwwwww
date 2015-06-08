package com.mgw.member.http.pay;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;

import com.mgw.member.constant.Define_C;
import com.mgw.member.R;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
/**
 * 微信分享
 * @author Administrator
 *
 */
public class WXshare {
	private final IWXAPI api;
	Context mContext;

	// feb4d7b6ad242d966f0d0780eb83b639
	// c2f53c437e6f1f26477150510088b724

	public WXshare(Context context) {
		mContext = context;
		api = WXAPIFactory.createWXAPI(mContext, Define_C.APP_ID);
		api.registerApp(Define_C.APP_ID);

	}

	public void sendmsgReq(int flag, String coupon, String couponname) {
		// WXTextObject wxTextObject = new WXTextObject();
		// wxTextObject.text = text;
		// WXMediaMessage mediaMessage = new WXMediaMessage();
		// mediaMessage.mediaObject = wxTextObject;
		// mediaMessage.description = text;
		// SendMessageToWX.Req req = new SendMessageToWX.Req();
		//
		// req.transaction = String.valueOf(System.currentTimeMillis());
		// req.message = mediaMessage;
		// api.sendReq(req);
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(
				"mgw_data", 0);
		String shareurl = sharedPreferences.getString("register_page",
				"http://m.mgw.cc/register.aspx") + coupon;
		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = shareurl;
		WXMediaMessage msg = new WXMediaMessage(webpage);

		msg.title = "【抢卷了】" + sharedPreferences.getString("mgw_name", "美顾问")
				+ "赠送1个消费卷：" + couponname + "大家快来抢吧！";
		msg.description = "【抢卷了】"
				+ sharedPreferences.getString("mgw_name", "美顾问") + "赠送1个消费卷："
				+ couponname + "大家快来抢吧！";

		// 这里替换一张自己工程里的图片资源
		Bitmap thumb = BitmapFactory.decodeResource(getResources(),
				R.drawable.wxshareimg);
		thumb = ThumbnailUtils.extractThumbnail(thumb, 95, 95,
				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		msg.setThumbImage(thumb);

		SendMessageToWX.Req req = new SendMessageToWX.Req();

		req.transaction = String.valueOf(System.currentTimeMillis());
		req.message = msg;
		req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession
				: SendMessageToWX.Req.WXSceneTimeline;
		api.sendReq(req);
	}

	private Resources getResources() {
		// TODO Auto-generated method stub
		return null;
	}
}
