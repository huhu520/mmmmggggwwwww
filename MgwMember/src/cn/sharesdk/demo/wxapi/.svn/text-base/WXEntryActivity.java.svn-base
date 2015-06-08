/*
 * 官网地站:http://www.mob.com
 * 技术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第一时间通过微信将版本更新内容推送给您。如果使用过程中有任何问题，也可以通过微信与我们取得联系，我们将会在24小时内给予回复）
 *
 * Copyright (c) 2013年 mob.com. All rights reserved.
 */

package cn.sharesdk.demo.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.mgw.member.ui.activity.GivecouponActivity;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/** 微信客户端回调activity示例 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
	private IWXAPI api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		api = WXAPIFactory.createWXAPI(this, "wx194b6acec5f31871", false);
		api.handleIntent(getIntent(), this);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResp(BaseResp resp) {
		// LogManager.show(Tag, "resp.errCode:" + resp.errCode + ",resp.errStr:"
		// + resp.errStr, 1);
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			GivecouponActivity.WXsharecod = resp.errCode;
			// Toast.makeText(getApplicationContext(), "分享成功！",
			// Toast.LENGTH_SHORT)
			// .show();
			finish();
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			// 分享取消
			GivecouponActivity.WXsharecod = resp.errCode;
			finish();
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			// 分享拒绝
			GivecouponActivity.WXsharecod = resp.errCode;
			Toast.makeText(getApplicationContext(), "分享被拒绝！", Toast.LENGTH_SHORT).show();
			finish();
			break;
		}
	}

	@Override
	public void onReq(BaseReq arg0) {
		// TODO Auto-generated method stub

	}

}
