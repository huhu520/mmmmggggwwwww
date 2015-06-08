package com.mgw.member.ui.activity;

import java.net.URLEncoder;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.android.app.sdk.AliPay;
import com.alipay.android.msp.Keys;
import com.alipay.android.msp.Rsa;
import com.hx.hxchat.utils.CommonUtils;
import com.mgw.member.R;
import com.mgw.member.constant.Define_C;
import com.mgw.member.constant.imp_Define;
import com.mgw.member.http.Http;
import com.mgw.member.http.pay.WXPayRequest1;
import com.mgw.member.uitls.Utils;

public class PayTypeActivity extends BaseActivity implements OnClickListener, imp_Define {

	TextView txtcashmoney;
	private TextView txthyprice;
	private TextView txtprice_old;
	private TextView txtoutlet;
	private boolean mBCheckOut;
	private Button btnpay, paywx, payzfb, payxx;
	TextView wx_value, txtzfb_value;
	RelativeLayout lywx, lyzfb, lyxx;
	private AudioManager audioManager;
	private int mPayType = 0;// 0 代表微支付，1 支付宝 2 线下
	private Button ibBack_pay_type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_type);

		ibBack_pay_type = (Button) findViewById(R.id.ibBack_pay_type);
		audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		btnpay = (Button) findViewById(R.id.btnpay);
		paywx = (Button) findViewById(R.id.paywx);
		payzfb = (Button) findViewById(R.id.payzfb);
		payxx = (Button) findViewById(R.id.payxx);

		wx_value = (TextView) findViewById(R.id.txtwx_value);
		txtzfb_value = (TextView) findViewById(R.id.txtzfb_value);
		lywx = (RelativeLayout) findViewById(R.id.layout_wx);
		lyzfb = (RelativeLayout) findViewById(R.id.layout_alipay);
		lyxx = (RelativeLayout) findViewById(R.id.layout_xxpay);
		ibBack_pay_type.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
		lyzfb.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				//
				int eventaction = event.getAction();
				switch (eventaction) {
				case MotionEvent.ACTION_UP:
					audioManager.playSoundEffect(AudioManager.FX_KEY_CLICK);
					paywx.setBackgroundResource(R.drawable.ic_check);
					payzfb.setBackgroundResource(R.drawable.ic_check_s);
					payxx.setBackgroundResource(R.drawable.ic_check);
					mPayType = 1;
					break;
				}
				return true;
			}
		});
		lyxx.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				//
				int eventaction = event.getAction();
				switch (eventaction) {
				case MotionEvent.ACTION_UP:
					audioManager.playSoundEffect(AudioManager.FX_KEY_CLICK);
					paywx.setBackgroundResource(R.drawable.ic_check);
					payzfb.setBackgroundResource(R.drawable.ic_check);
					payxx.setBackgroundResource(R.drawable.ic_check_s);
					mPayType = 2;
					break;
				}
				return true;
			}
		});
		lywx.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				//
				int eventaction = event.getAction();
				switch (eventaction) {
				case MotionEvent.ACTION_UP:
					audioManager.playSoundEffect(AudioManager.FX_KEY_CLICK);
					paywx.setBackgroundResource(R.drawable.ic_check_s);
					payzfb.setBackgroundResource(R.drawable.ic_check);
					payxx.setBackgroundResource(R.drawable.ic_check);
					mPayType = 0;
					break;
				}
				return true;
			}
		});
		btnpay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 快速点击两次不作出响应
				if (Utils.isFastDoubleClick()) {
					Toast.makeText(getApplicationContext(), "请不要重复提交！", Toast.LENGTH_SHORT).show();
					return;
				}
				if (mBCheckOut == false) {
					pay_method_num = 1;
					// mBCheckOut = true;
					// btnpay.setBackgroundResource(R.drawable.btn_gray_selector);
					// btnpay.setText("正在提交");

					CheckOrder();// 支付
				}
			}
		});

		txtcashmoney = (TextView) findViewById(R.id.cashmoney);
		txthyprice = (TextView) findViewById(R.id.hyprice);
		txtprice_old = (TextView) findViewById(R.id.price_old);
		txtoutlet = (TextView) findViewById(R.id.outlet);
		OpenOrderID();
	}

	private void CheckOrder() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					JSONObject t_JsonObject = Http.postGetOrderState(Define_C.s_orderId);
					if (t_JsonObject != null) {
						if (t_JsonObject.getInt("flag") == 0) {// 成功d
							JSONArray t_JsonArray = t_JsonObject.getJSONArray("items");
							t_JsonObject = t_JsonArray.getJSONObject(0);
							//
							if (t_JsonObject.getString("Status").equals("0")) {
								handler.sendEmptyMessage(6);
							} else if (t_JsonObject.getString("Status").equals("1")) {
								if (t_JsonObject.getString("FMB_Payment").equals("2")) // 现金支付
								{
									// 已经支付需要跳转成功页面
									// payMethod(pay_method_num);
								} else {
									// 等待选择跳转进入支付实际支付-2
									handler.sendEmptyMessage(-2);
								}
								mBCheckOut = true;
								return;
							} else if (t_JsonObject.getString("Status").equals("2")) {
								handler.sendEmptyMessage(7);
								return;
							}
						} else {
							Message oMessage = new Message();
							oMessage.what = -11;
							;
							oMessage.obj = t_JsonObject.get("msg");
							handler.sendMessage(oMessage);
							return;
						}

					} else {
						Message oMessage = new Message();
						oMessage.what = -10;
						;
						oMessage.obj = null;
						handler.sendMessage(oMessage);
						return;

						// Toast.makeText(getApplicationContext(),
						// "网络不是很好，请检查网络",Toast.LENGTH_LONG).show();
					}

				} catch (org.json.JSONException e) {
					// TODO Auto-generated catch block
					Message oMessage = new Message();
					oMessage.what = -10;
					;
					oMessage.obj = null;
					handler.sendMessage(oMessage);
				}
			}
		}).start();
	}

	// 根据单据编号获取单据信息
	HashMap<String, Object> result = new HashMap<String, Object>();

	// 根据订单编号打开单据
	private void OpenOrderID() {
		new Thread() {
			@Override
			public void run() {
				try {
					JSONObject jo = Http.postShopingDetails(Define_C.s_orderId, Define_C.s_shopingId, Define_C.s_old);
					if (jo != null) {
						Message msg = new Message();
						msg.what = 0;
						msg.obj = jo;
						handler.sendMessage(msg);
					} else {
						Message msg = new Message();
						msg.what = 9;
						msg.obj = jo;
						handler.sendMessage(msg);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Message msg = new Message();
					msg.what = 9;
					msg.obj = null;
					handler.sendMessage(msg);
				}
			}
		}.start();
	}

	private void WXPayed() {
		try {
			JSONObject item = new JSONObject();
			item.put("trade_no", Define_C.s_orderId);
			item.put("amount", mPayMoneyString);
			item.put("pname", Define_C.s_shopingName.replace(" ", "") + "店内消费");
			SharedPreferences sp = getSharedPreferences("mgw_data", 0);
			item.put("notify_url", sp.getString("xx_tenpay", "http://pay.mgw.cc/XX/tenpay_notify_url.aspx"));
			new WXPayRequest1(PayTypeActivity.this, item).WXPay();
		} catch (JSONException e) {
			mHandler.sendEmptyMessage(MESSAGE_TYPE_NOWX);
		}
	}

	String mPayMoneyString = "0";

	private String getNewOrderInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append("partner=\"");
		sb.append(Keys.DEFAULT_PARTNER);
		sb.append("\"&out_trade_no=\"");
		sb.append(Define_C.s_orderId);
		sb.append("\"&subject=\"");
		sb.append(Define_C.s_shopingName.replace(" ", "") + "店内消费");
		sb.append("\"&body=\"");
		sb.append(Define_C.s_shopingName.replace(" ", "") + "店内消费");
		sb.append("\"&total_fee=\"");
		// sb.append(sProducts[position].price.replace("一口价:", ""));
		sb.append(mPayMoneyString);
		// sb.append("0.01");
		sb.append("\"&notify_url=\"");

		SharedPreferences sp = getSharedPreferences("mgw_data", 0);

		// 网址需要做URL编码
		sb.append(URLEncoder.encode(sp.getString("xx_alipay", "http://pay.mgw.cc/XX/alipay_notify_url.aspx")));
		sb.append("\"&service=\"mobile.securitypay.pay");
		sb.append("\"&_input_charset=\"UTF-8");
		sb.append("\"&return_url=\"");
		sb.append(URLEncoder.encode(sp.getString("xx_alipay_return_url", "http://pay.mgw.cc/XX/alipay_return_url.aspx")));
		sb.append("\"&payment_type=\"1");
		sb.append("\"&seller_id=\"");
		sb.append(Keys.DEFAULT_SELLER);

		// 如果show_url值为空，可不传
		// sb.append("\"&show_url=\"");
		sb.append("\"&it_b_pay=\"1m");
		sb.append("\"");

		return new String(sb);
	}

	/** 支付宝支付 */
	private void ZfbPayed() {
		try {
			Log.i("ExternalPartner", "onItemClick");
			String info = getNewOrderInfo();
			String sign = Rsa.sign(info, Keys.PRIVATE);
			sign = URLEncoder.encode(sign);
			info += "&sign=\"" + sign + "\"&" + getSignType();
			// start the pay.
			// final String orderInfo = info;
			payToAli(info);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/** 支付宝支付的实际操作方法 */
	private void payToAli(final String orderInfo) {
		new Thread() {
			@Override
			public void run() {

				AliPay alipay = new AliPay(PayTypeActivity.this, mHandler);

				// 设置为沙箱模式，不设置默认为线上环境
				// alipay.setSandBox(true);

				String result = "";
				String m_OrderInfoString = "";
				m_OrderInfoString = orderInfo;
				try {
					result = alipay.pay(m_OrderInfoString);
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (result.equals("")) {
					Message msg = new Message();
					msg.what = 6;
					mHandler2.sendMessage(msg);
				} else {
					Message msg = new Message();
					msg.what = 5;
					msg.obj = result;
					mHandler2.sendMessage(msg);
				}
			}
		}.start();

	}

	boolean finishPay = true;
	Handler mHandler2 = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			finishPay = true;
			mBCheckOut = false;

			if (msg.what == 5) {
				String t_String = (String) msg.obj;

				if (t_String.indexOf("success") != -1) {
					int t_num = t_String.indexOf("success");
					if (t_String.substring(t_num + 7, t_num + 14).equals("=\"true\"")) {// 支付成功
						finish3();
					}
				}
			} else if (msg.what == 6) {
				Toast.makeText(PayTypeActivity.this, "暂不能支付，请等待更新修复", Toast.LENGTH_LONG).show();
			}
		}
	};
	/** 支付宝支付时用的handler */
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			JSONObject t_JsonObject;
			JSONArray t_JsonArray;
			switch (msg.what) {
			case -10:

				// finish();
				break;

			case MESSAGE_TYPE_SHOPING_ID2:
				try {
					t_JsonObject = (JSONObject) msg.obj;

					String oresult = msg.obj.toString();
					mBCheckOut = false;
					if (oresult.contains("resultStatus={9000}") && oresult.contains("out_trade_no")) {
						// 进入手机支付完成页面
						Intent intent = new Intent(PayTypeActivity.this, ZxpayFinishActivity.class);
						PayTypeActivity.this.startActivity(intent);
						Toast.makeText(getApplicationContext(), "支付成功！", Toast.LENGTH_SHORT).show();
						finish();
					} else {
						mBCheckOut = false;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case MESSAGE_TYPE_GET_ORDER_ID:
				Intent intent1 = new Intent(PayTypeActivity.this, ZxpayFinishActivity.class);

				PayTypeActivity.this.startActivity(intent1);
				Toast.makeText(getApplicationContext(), "支付成功！", Toast.LENGTH_SHORT).show();
				finish();
			case MESSAGE_TYPE_STATE_CHENGE:
			case MESSAGE_TYPE_STATE_FINISH:
				try {
					t_JsonObject = (JSONObject) msg.obj;
					if (t_JsonObject.getInt("flag") == 0) {
						Intent intent = new Intent(PayTypeActivity.this, OrderFinishActivity.class);

						PayTypeActivity.this.startActivity(intent);
						finish();
					} else {
						Toast.makeText(PayTypeActivity.this, t_JsonObject.getString("msg"), Toast.LENGTH_LONG).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case MESSAGE_TYPE_NOWX:
				// ToastUtil.showToastWithAlertPic("微信未安装或版本不支持微支付，请检查");
				Toast.makeText(getApplicationContext(), "微信未安装或版本不支持微支付，请检查", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	private void finish3() {
		/*
		 * new Thread() {
		 * 
		 * @Override public void run() { try { Message message =
		 * Message.obtain(); message.what = MESSAGE_TYPE_SHOPING_ID2; JSONObject
		 * t_JsonObject = new JSONObject(); JSONObject t_JsonObject2 = new
		 * JSONObject(); t_JsonObject.put("title", "message");
		 * t_JsonObject.put("description", "description");
		 * t_JsonObject2.put("type", "5"); t_JsonObject2.put("playerId",
		 * GlobelElements.getInstance().m_user_id);
		 * t_JsonObject2.put("shopingId", Define.s_shopingId); t_JsonObject2
		 * .put("playerName", ((GlobelElements)
		 * getApplicationContext()).m_playerName); t_JsonObject2.put("orderId",
		 * Define.s_orderId); t_JsonObject2.put("payment_type", "1");
		 * t_JsonObject.put("custom_content", t_JsonObject2); message.obj =
		 * Http.postShopingOrder(Define.s_old, t_JsonObject.toString());
		 * mHandler.sendMessage(message); } catch (Exception e) {
		 * e.printStackTrace(); }
		 * 
		 * } }.start();
		 */

		Intent intent = new Intent(PayTypeActivity.this, ZxpayFinishActivity.class);
		PayTypeActivity.this.startActivity(intent);
		Toast.makeText(getApplicationContext(), "支付成功！", Toast.LENGTH_SHORT).show();
		finish();
	}

	private String getSignType() {
		return "sign_type=\"RSA\"";
	}

	private void XXPayed() {
		new Thread() {
			@Override
			public void run() {
				try {
					Message message = Message.obtain();
					message.what = 22;
					String t_String = "2";

					message.obj = Http.postOrderChengeState(Define_C.s_orderId, getSharedPreferences("mgw_data", 0).getString("mgw_userID", null), Define_C.s_shopingId, Define_C.s_old, t_String);
					handler.sendMessage(message);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}.start();
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 21:
				String oresult = (String) msg.obj;

				if (oresult.contains("resultStatus={9000}") && oresult.contains("out_trade_no")) {
					// 进入手机支付完成页面
					Intent intent = new Intent(PayTypeActivity.this, OrderFinishActivity.class);
					PayTypeActivity.this.startActivity(intent);

					finish();
				}
				break;

			case 22:
				if (msg.obj == null) {
					Toast.makeText(getApplicationContext(), "网络不给力,请重新提交!", Toast.LENGTH_SHORT).show();
					mBCheckOut = false;
					btnpay.setBackgroundResource(R.drawable.corner_b);
					btnpay.setText("立即支付");
				} else {
					JSONObject t_JsonObject = (JSONObject) msg.obj;
					try {
						if (t_JsonObject.getInt("flag") == 0) {
							// 进入支付完成页面
							Intent intent = new Intent(PayTypeActivity.this, OrderFinishActivity.class);
							PayTypeActivity.this.startActivity(intent);
							finish();
						} else {
							Toast.makeText(PayTypeActivity.this, t_JsonObject.getString("msg"), Toast.LENGTH_LONG).show();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			case -2:
				if (mPayType == 0) {
					WXPayed();
					// 微支付
				} else if (mPayType == 1) {
					// 支付宝支付
					ZfbPayed();
				} else {
					// 线下支付
					XXPayed();
				}
				break;
			case 9:
				Toast.makeText(getApplicationContext(), "网络不给力!", Toast.LENGTH_SHORT).show();
				finish();
				break;
			case 2:// 支付
				int status2 = (Integer) result.get("status");//
				String message2 = (String) result.get("msg");
				switch (status2) {
				case 1:
					Toast.makeText(getApplicationContext(), message2, Toast.LENGTH_LONG).show();
					mBCheckOut = false;
					btnpay.setBackgroundResource(R.drawable.corner_b);
					btnpay.setText("立即支付");

					break;
				case 0:// 跳转到快捷支付
						// Toast.makeText(getApplicationContext(), message2,
						// Toast.LENGTH_LONG).show();
					/*
					 * Intent intent = new Intent(AddAccountActivity.this,
					 * shortcutMoneyActivity.class); startActivity(intent);
					 */
					/*
					 * Intent intent = new Intent(CashCheckOutActivity.this,
					 * SendCouponActivity.class); intent.putExtra("orderid",
					 * orderid); intent.putExtra("playerId", mUserID);
					 * intent.putExtra("playerName", mplayerName);
					 * startActivity(intent);
					 */
					finish();
					break;
				case 9:
					Toast.makeText(getApplicationContext(), message2, Toast.LENGTH_LONG).show();
					mBCheckOut = false;
					btnpay.setBackgroundResource(R.drawable.corner_b);
					btnpay.setText("立即支付");

					break;
				}
				break;
			case 6:// 检测数据后的flag<>0
				Toast.makeText(getApplicationContext(), "参数错误!", Toast.LENGTH_SHORT).show();
				finish();
				break;
			case 7:// 检测数据后的flag<>0
				Toast.makeText(getApplicationContext(), "单据已结账或者已撤销!", Toast.LENGTH_SHORT).show();
				finish();
				break;
			case -11:// 检测数据后的flag<>0
				Toast.makeText(getApplicationContext(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
				finish();
				break;
			case -10:// 确定后的网络异常
				Toast.makeText(getApplicationContext(), "网络不给力啊!", Toast.LENGTH_SHORT).show();
				mBCheckOut = false;
				btnpay.setBackgroundResource(R.drawable.corner_b);
				btnpay.setText("立即支付");
				break;
			case 0:// 打开单据
				JSONObject jo = (JSONObject) msg.obj;
				try {
					if (jo.getInt("flag") == 0) // 获取数据成功
					{
						JSONObject joitem = jo.getJSONObject("item");

						JSONObject jsoffline = joitem.getJSONObject("offline");
						if (jsoffline.isNull("status")) {
							Toast.makeText(getApplicationContext(), "参数异常!", Toast.LENGTH_SHORT).show();
							finish();
						} else {
							if (jsoffline.getString("status").equals("0")) {
								Toast.makeText(getApplicationContext(), "参数异常!", Toast.LENGTH_SHORT).show();
								finish();
							} else {
								JSONArray mjo = joitem.getJSONArray("coupon");
								if (mjo.length() > 0) {
									FillConsumcode(mjo, true);
								}
								JSONArray mjoonline = joitem.getJSONArray("online");
								if (mjoonline.length() > 0) {
									FillPTOrder(mjoonline, false);
								}
								mPayMoneyString = jsoffline.getString("sprice");
								/*
								 * item.put("amount",
								 * jsoffline.getString("sprice"));
								 * item.put("pname",
								 * Define.s_shopingName.replace(" ", "") +
								 * "店内消费"); item.put("quantity", "1");
								 */
								txtcashmoney.setText("￥" + jsoffline.getString("sprice"));
								txthyprice.setText("折后价：￥" + CommonUtils.switchprice(jsoffline.getString("rmoney")));
								txtprice_old.setText("￥" + CommonUtils.switchprice(jsoffline.getString("mprice")));
								txtoutlet.setText("消费券抵用:￥" + CommonUtils.switchprice(jsoffline.getString("amount")));
								// wx_value.setText("付款后，立获￥" +
								// CommonUtils.switchprice(jsoffline.getString("FMBI_SafeMoney"))
								// + "返利金");
								// txtzfb_value.setText("付款后，立获￥" +
								// CommonUtils.switchprice(jsoffline.getString("FMBI_SafeMoney"))
								// + "返利金");

							}
						}
					} else {
						Toast.makeText(getApplicationContext(), jo.getString("msg"), Toast.LENGTH_SHORT).show();
						finish();
					}
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
			}
		}
	};

	private int pay_method_num;

	private void FillPTOrder(JSONArray pArr, Boolean bSelected) {
		// 平台订单
		LinearLayout layoutConcumecode = (LinearLayout) this.findViewById(R.id.layoutptOrder);
		// layoutConcumecode.removeAllViews();
		layoutConcumecode.setVisibility(View.VISIBLE);

		LinearLayout.LayoutParams olayoutParams = new LinearLayout.LayoutParams(WindowManager.LayoutParams.FILL_PARENT, 1);
		for (int i = 0; i < pArr.length(); i++) {
			try {
				final JSONObject oObj = pArr.getJSONObject(i);
				View rowView = getLayoutInflater().inflate(R.layout.item_cashcheckout, null);
				TextView txtwx_label = (TextView) rowView.findViewById(R.id.wx_label);
				txtwx_label.setText(oObj.getString("name"));

				TextView txtwx_money = (TextView) rowView.findViewById(R.id.wx_money);
				txtwx_money.setText("￥" + CommonUtils.switchprice(oObj.getString("amount")));

				layoutConcumecode.addView(rowView);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// 填充消费卷
	private void FillConsumcode(JSONArray pArr, Boolean bSelected) {
		LinearLayout layoutConcumecode = (LinearLayout) this.findViewById(R.id.layoutConcumecode);
		// layoutConcumecode.removeAllViews();
		layoutConcumecode.setVisibility(View.VISIBLE);

		LinearLayout.LayoutParams olayoutParams = new LinearLayout.LayoutParams(WindowManager.LayoutParams.FILL_PARENT, 1);
		for (int i = 0; i < pArr.length(); i++) {
			try {
				final JSONObject oObj = pArr.getJSONObject(i);
				View rowView = getLayoutInflater().inflate(R.layout.item_cashcheckout, null);
				TextView txtwx_label = (TextView) rowView.findViewById(R.id.wx_label);
				txtwx_label.setText(oObj.getString("name"));
				TextView txtwx_money = (TextView) rowView.findViewById(R.id.wx_money);
				txtwx_money.setText("￥" + CommonUtils.switchprice(oObj.getString("amount")));
				layoutConcumecode.addView(rowView);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onRestart() {

		super.onRestart();
		mBCheckOut = false;
		// btnpay.setBackgroundResource(R.drawable.select_slowblue2_btn);
		// btnpay.setText("立即支付");
	}

	/**
 * 
 * 
 * 
 * */
	static public int mWXPayResult = -100;

	@Override
	protected void onResume() {
		super.onResume();
		if (mWXPayResult != -100) {
			if (mWXPayResult == 0) {
				finish3();

			} else if (mWXPayResult == -2) {
				mBCheckOut = false;
				// ToastUtil.showToastWithAlertPic("支付取消");
				Toast.makeText(getApplicationContext(), "支付取消", Toast.LENGTH_SHORT).show();
			} else {
				mBCheckOut = false;
				// ToastUtil.showToastWithAlertPic("支付失败");
				Toast.makeText(getApplicationContext(), "支付失败", Toast.LENGTH_SHORT).show();
			}

			mWXPayResult = -100;
		}
	}

}
