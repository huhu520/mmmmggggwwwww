package com.mgw.member.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.mgw.member.constant.Define_C;
import com.mgw.member.ui.activity.PayTypeActivity;
import com.mgw.member.ui.activity.cityleague.OrderPayActivity;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	private IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		 setContentView(R.layout.pay_result);

		api = WXAPIFactory.createWXAPI(this, Define_C.APP_ID);
		api.handleIntent(getIntent(), this);

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			OrderPayActivity.mWXPayResult = resp.errCode;
			PayTypeActivity.mWXPayResult = resp.errCode;
			finish();
		}
	}
}