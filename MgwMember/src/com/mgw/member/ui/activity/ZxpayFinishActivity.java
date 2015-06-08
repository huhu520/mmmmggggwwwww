package com.mgw.member.ui.activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hx.hxchat.utils.CommonUtils;
import com.mgw.member.R;
import com.mgw.member.constant.Define_C;
import com.mgw.member.http.Http;
import com.mgw.member.ui.activity.cityleague.BaseActivity2;

public class ZxpayFinishActivity extends BaseActivity2 implements OnClickListener {
	private String orderid;
	private String mplayerName;
	private String mUserID;
	private Button btnPay;
	private boolean mBCheckOut = false;
	private TextView txtcashmoney, txtordercode, txthyprice, txtprice_old, txtoutlet, txttitle;
	TextView tv_title_cent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xsfinish);
		// orderid = getIntent().getStringExtra("orderid");
		// mplayerName = getIntent().getStringExtra("playerName");
		initTitleBar();
		// txttitle = (TextView) findViewById(R.id.title);
		// txttitle.setText("已手机支付");
		mUserID = getIntent().getStringExtra("playerId");
		txtcashmoney = (TextView) findViewById(R.id.txtcashmoney);
		txtordercode = (TextView) findViewById(R.id.txtordercode);
		txthyprice = (TextView) findViewById(R.id.txthyprice);
		txtprice_old = (TextView) findViewById(R.id.txtprice_old);
		txtoutlet = (TextView) findViewById(R.id.txtoutlet);
		btnPay = (Button) findViewById(R.id.btnpay_xs);

		btnPay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mBCheckOut == false) {
					mBCheckOut = true;
					finish();
				}
			}
		});
		OpenOrder();

	}

	@Override
	public void onClick(View v) {

		super.onClick(v);
	}

	private void initTitleBar() {
		initTitleButton();
		tv_title_cent = (TextView) findViewById(R.id.tv_title_cent);
		tv_title_cent.setText("已手机支付");
	}

	private void OpenOrder() {
		new Thread() {
			@Override
			public void run() {
				try {
					JSONObject jo = Http.GetFinishbill(Define_C.s_orderId, Define_C.s_shopingId, Define_C.s_old);
					if (jo != null) {
						Message msg = new Message();
						msg.what = 0;
						msg.obj = jo;
						handler.sendMessage(msg);
					} else {
						Message msg = new Message();
						msg.what = -99;
						msg.obj = jo;
						handler.sendMessage(msg);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Message msg = new Message();
					msg.what = -99;
					msg.obj = null;
					handler.sendMessage(msg);
				}
			}
		}.start();
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case -99:
				Toast.makeText(getApplicationContext(), "网络不给力!", Toast.LENGTH_SHORT).show();
				finish();
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
							if (jsoffline.getString("status").equals("0") || jsoffline.getString("status").equals("1")) {
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
								if (jsoffline.getString("tradecode").equals("") && Float.parseFloat(jsoffline.getString("sprice")) == 0) {
									txtordercode.setText("");
									tv_title_cent.setText(mplayerName + " 消费已完成");
								} else {
									txtordercode.setText("支付单号：" + jsoffline.getString("tradecode"));
								}
								float cashmoney = Float.parseFloat(jsoffline.getString("sprice"));
								String cashmoney_s = String.format("%.2f", cashmoney);
								// txtcashmoney.setText("￥"
								// + CommonUtils.switchprice(jsoffline
								// .getString("sprice")));
								txtcashmoney.setText(cashmoney_s);
								float rmoney = Float.parseFloat(jsoffline.getString("rmoney"));
								String rmoney_s = String.format("%.2f", rmoney);
								// txthyprice.setText("折后价：￥"
								// + CommonUtils.switchprice(jsoffline
								// .getString("rmoney")));
								txthyprice.setText("折后价：￥" + rmoney_s);
								txtprice_old.setText("￥" + CommonUtils.switchprice(jsoffline.getString("mprice")));
								txtoutlet.setText("消费券抵用:￥" + CommonUtils.switchprice(jsoffline.getString("amount")));
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

}
