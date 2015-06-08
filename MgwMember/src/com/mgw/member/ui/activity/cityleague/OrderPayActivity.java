package com.mgw.member.ui.activity.cityleague;

import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.android.app.sdk.AliPay;
import com.alipay.android.msp.Keys;
import com.alipay.android.msp.Rsa;
import com.loopj.android.http.RequestParams;
import com.mgw.member.R;
import com.mgw.member.constant.Define_C;
import com.mgw.member.http.pay.WXPayRequest1;
import com.mgw.member.uitls.MgqDataHandler;
import com.mgw.member.uitls.MgqRestClient;
import com.mgw.member.uitls.Utils;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

@SuppressLint({ "HandlerLeak", "DefaultLocale" })
public class OrderPayActivity extends BaseActivity2 implements OnClickListener {
	private JSONArray m_order_list = new JSONArray();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.order_pay_withposition);
		findViewById(R.id.ll_order_pay_address).setVisibility(View.GONE);
		initTitleBar();
		initPayKind();
		((TextView) findViewById(R.id.tv_order_pay_option)).setText(getIntent().getExtras().getString("region"));
		findViewById(R.id.bt_order_pay_ok).setOnClickListener(this);
		findViewById(R.id.ll_order_pay_wx).setOnClickListener(this);
		findViewById(R.id.ll_order_pay_zfb).setOnClickListener(this);
		getDataGetorder(true);
	}

	private void initView() {
		try {
			JSONObject item = m_order_list.getJSONObject(0);

			if (item.getInt("otype") == 0) {
				findViewById(R.id.ll_order_pay_address).setVisibility(View.VISIBLE);
				((TextView) findViewById(R.id.tv_order_pay_address)).setText(item.getString("shipto") + " , " + item.getString("phone") + "\n" + item.getString("addr"));
			}
			// else {
			// findViewById(R.id.ll_order_pay_address)
			// .setVisibility(View.GONE);
			// }

			((TextView) findViewById(R.id.tv_order_pay_pname)).setText(item.getString("pname").replaceAll(" ", ""));
			((TextView) findViewById(R.id.tv_order_pay_price)).setText(item.getString("sprice") + "元");
			((TextView) findViewById(R.id.tv_order_pay_count)).setText(item.getString("quantity"));
			((TextView) findViewById(R.id.tv_order_pay_totalprice)).setText(item.getString("amount") + "元");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void initTitleBar() {
		initTitleButton();
		((TextView) findViewById(R.id.tv_title_cent)).setText("订单支付");
	}

	int mPayType = 2;

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.bt_order_pay_ok:
			if (Utils.isFastDoubleClick()) {
				return;
			}
			if (mPayType == 0)
				payOrder();
			else {
				try {
					IWXAPI api = WXAPIFactory.createWXAPI(this, Define_C.APP_ID);
					if (!api.isWXAppInstalled() || !api.isWXAppSupportAPI()) {
						Toast.makeText(getApplicationContext(), "微信未安装或版本不支持微支付，请检查", Toast.LENGTH_SHORT).show();
						// ToastUtil.showToastWithAlertPic("微信未安装或版本不支持微支付，请检查");
						return;
					}

					JSONObject item = m_order_list.getJSONObject(0);
					item.put("pname", ((String) item.get("pname")).replaceAll(" ", ""));
					item.put("trade_no", getIntent().getExtras().getString("orderid"));
					SharedPreferences sp = getSharedPreferences("mgw_data", 0);
					item.put("notify_url", sp.getString("wz_tenpay", "http://pay.mgw.cc/WZ/tenpay_notify_url.aspx"));
					new WXPayRequest1(this, item).WXPay();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			break;

		case R.id.ll_order_pay_wx:
			mPayType = 2;
			break;

		case R.id.ll_order_pay_zfb:
			mPayType = 0;
			break;
		}
	}

	private void getDataGetorder(boolean show) {
		MgqDataHandler loginHandler = new MgqDataHandler(this, show, false) {
			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);

				try {
					JSONObject obj = new JSONObject(response);
					Log.e("createorder", obj.toString());
					if (obj.getInt("flag") == 0) {
						m_order_list = obj.getJSONArray("items");
						initView();
					} else {
						Toast.makeText(getApplicationContext(), obj.getString("msg"), Toast.LENGTH_SHORT).show();
						// ToastUtil.showToastWithAlertPic(obj.getString("msg"));

					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable ble) {

			}
		};

		RequestParams params = new RequestParams();
		params.put("type", "wzreposity.getorder");
		try {
			JSONObject obj = new JSONObject(getSharedPreferences("mgw_data", Context.MODE_PRIVATE).getString("mgw_data", ""));
			params.put("userid", obj.getString("UserID"));
			params.put("serial", obj.getString("serial"));
			params.put("orderid", getIntent().getExtras().getString("orderid"));

			params.put("telephone", obj.getString("Telephone"));
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		MgqRestClient.get(Define_C.mgw_url, params, loginHandler);

	}

	static public int mWXPayResult = -100;

	@Override
	protected void onResume() {
		super.onResume();
		if (mWXPayResult != -100) {
			if (mWXPayResult == 0) {
				try {
					Intent intent = new Intent(OrderPayActivity.this, PayCompletActivity.class);
					intent.putExtra("info", m_order_list.getJSONObject(0).toString());
					startActivity(intent);

					// int count = ((GlobelElements)
					// getApplicationContext()).mActivityArray
					// .size();
					// for (int i = count - 1; i >= 0; i--) {
					// Activity activity = ((GlobelElements)
					// getApplicationContext()).mActivityArray
					// .get(i);
					// activity.finish();
					// ((GlobelElements) getApplicationContext()).mActivityArray
					// .remove(i);
					// }

					finish();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else if (mWXPayResult == -2) {
				Toast.makeText(getApplicationContext(), "支付取消", Toast.LENGTH_SHORT).show();
				// ToastUtil.showToastWithAlertPic("支付取消");
			} else {
				Toast.makeText(getApplicationContext(), "支付失败", Toast.LENGTH_SHORT).show();
				// ToastUtil.showToastWithAlertPic("支付失败");
			}

			mWXPayResult = -100;
		}
	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String result = (String) msg.obj;

			if (result.contains("resultStatus={9000}") && result.contains("out_trade_no")) {
				try {
					Intent intent = new Intent(OrderPayActivity.this, PayCompletActivity.class);
					intent.putExtra("info", m_order_list.getJSONObject(0).toString());
					startActivity(intent);

					// int count = ((GlobelElements)
					// getApplicationContext()).mActivityArray
					// .size();
					// for (int i = count - 1; i >= 0; i--) {
					// Activity activity = ((GlobelElements)
					// getApplicationContext()).mActivityArray
					// .get(i);
					// activity.finish();
					// ((GlobelElements) getApplicationContext()).mActivityArray
					// .remove(i);
					// }

					finish();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	};

	@SuppressWarnings("deprecation")
	void payOrder() {
		try {
			String info = getNewOrderInfo();
			String sign = Rsa.sign(info, Keys.PRIVATE);
			sign = URLEncoder.encode(sign);
			info += "&sign=\"" + sign + "\"&" + getSignType();

			final String orderInfo = info;
			new Thread() {
				@Override
				public void run() {
					AliPay alipay = new AliPay(OrderPayActivity.this, mHandler);
					String result = alipay.pay(orderInfo);
					Message msg = new Message();
					msg.what = 0;
					msg.obj = result;
					mHandler.sendMessage(msg);
				}
			}.start();
		} catch (Exception ex) {
		}
	}

	private String getSignType() {
		return "sign_type=\"RSA\"";
	}

	@SuppressLint("DefaultLocale")
	@SuppressWarnings("deprecation")
	private String getNewOrderInfo() throws JSONException {
		JSONObject item = m_order_list.getJSONObject(0);

		double fee = item.getDouble("amount");
		String strFee = String.format("%.2f", fee);

		StringBuilder sb = new StringBuilder();
		sb.append("partner=\"");
		sb.append(Keys.DEFAULT_PARTNER);
		sb.append("\"&out_trade_no=\"");
		sb.append(getIntent().getExtras().getString("orderid"));
		sb.append("\"&subject=\"");
		sb.append(item.getString("pname").replaceAll(" ", ""));
		sb.append("\"&body=\"");
		sb.append(item.getString("quantity"));
		sb.append("\"&total_fee=\"");
		sb.append(strFee/* "0.01" */);
		sb.append("\"&notify_url=\"");

		SharedPreferences sp = getSharedPreferences("mgw_data", 0);
		// 网址需要做URL编码
		sb.append(URLEncoder.encode(sp.getString("wz_alipay", "http://pay.mgw.cc/WZ/alipay_notify_url.aspx")));
		sb.append("\"&service=\"mobile.securitypay.pay");
		sb.append("\"&_input_charset=\"UTF-8");
		sb.append("\"&return_url=\"");
		sb.append(URLEncoder.encode(sp.getString("wz_alipay_return_url", "http://pay.mgw.cc/WZ/alipay_return_url.aspx")));
		sb.append("\"&payment_type=\"1");
		sb.append("\"&seller_id=\"");
		sb.append(Keys.DEFAULT_SELLER);

		// 如果show_url值为空，可不传
		// sb.append("\"&show_url=\"");
		sb.append("\"&it_b_pay=\"1m");
		sb.append("\"");

		return new String(sb);
	}
}
