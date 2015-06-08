package com.mgw.member.ui.activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hx.hxchat.utils.CommonUtils;
import com.loopj.android.http.RequestParams;
import com.mgw.member.R;
import com.mgw.member.constant.Define_C;
import com.mgw.member.http.Http;
import com.mgw.member.ui.activity.cityleague.BaseActivity2;
import com.mgw.member.uitls.MgqDataHandler;
import com.mgw.member.uitls.MgqRestClient;

/**
 * @author 欧阳嘉 2014-8-19 下午8:25:39
 */

public class OrderFinishActivity extends BaseActivity2 implements OnClickListener {
	TextView txtcashmoney;
	private TextView txthyprice;
	private TextView txtoutlet;
	private Button btnpay;
	TextView tv_title_cent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xxfinished);
		initTitleBar();
		((TextView) findViewById(R.id.tv_title_cent)).setText("您正在收银中");

		btnpay = (Button) findViewById(R.id.btnpay);

		btnpay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		txtcashmoney = (TextView) findViewById(R.id.cashmoney);
		txthyprice = (TextView) findViewById(R.id.hyprice);
		txtoutlet = (TextView) findViewById(R.id.outlet);
		Intent intent = getIntent();
		try {
			intent.getStringExtra("isfinish").equals("yes");
			tv_title_cent.setText("收银已完成");
			((TextView) findViewById(R.id.label)).setText("您本次付款");
			getfinishdata();
		} catch (Exception e) {
			OpenOrderID();
		}
		// if (intent.getStringExtra("isfinish").equals("yes")) {
		// getfinishdata();
		// } else {
		// OpenOrderID();
		// }
	}

	private void initTitleBar() {
		initTitleButton();
		tv_title_cent = (TextView) findViewById(R.id.tv_title_cent);
		tv_title_cent.setText("已手机支付");
	}

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

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {

			case 9:
				Toast.makeText(getApplicationContext(), "网络不给力，请与店内沟通完成", Toast.LENGTH_SHORT).show();
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
								txtcashmoney.setText("￥" + jsoffline.getString("sprice"));
								float deb = Float.parseFloat(jsoffline.getString("mprice")) - Float.parseFloat(jsoffline.getString("sprice"));
								txthyprice.setText("为您节省：￥" + String.format("%.2f", deb));

								txtoutlet.setText("获得返利金:￥" + jsoffline.getString("smoney"));

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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	/**
	 * 在消费券刚好抵消掉折后价时会调用这个方法
	 * 
	 * */
	private void getfinishdata() {
		MgqDataHandler loginHandler = new MgqDataHandler(this, true, false) {
			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				try {
					JSONObject obj = new JSONObject(response);
					Log.e("json", obj.toString());
					if (obj.getInt("flag") == 0) {
						JSONObject offline = obj.getJSONObject("item").getJSONObject("offline");
						JSONArray pArr = obj.getJSONObject("item").getJSONArray("coupon");
						JSONArray pArr2 = obj.getJSONObject("item").getJSONArray("online");
						float zhehoujia = Float.parseFloat(offline.getString("rmoney"));

						float yuanjia = Float.parseFloat(offline.getString("mprice"));
						float jiesheng = 0;
						if (zhehoujia > yuanjia) {
							jiesheng = zhehoujia;
						} else {
							jiesheng = yuanjia;
						}
						float diyong = Float.parseFloat(offline.getString("amount"));
						String jieyue = "为您节省: ￥" + String.format("%.2f", jiesheng);

						txthyprice.setText(jieyue);
						txtoutlet.setText("消费券抵用: ￥" + String.format("%.2f", diyong));
						if (pArr.length() > 0) {
							FillConsumcode(pArr, true);
						}
						if (pArr2.length() > 0) {
							FillPTOrder(pArr2, true);
						}

					} else {
						// ToastUtil.showToastWithAlertPic(obj.getString("msg"));
						Toast.makeText(getApplicationContext(), obj.getString("msg"), Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onFinish() {

			}
		};
		String type = "cashier.getfinishbill";
		RequestParams params = new RequestParams();

		params.add("type", type);
		params.add("fmbid", Define_C.s_orderId);
		params.add("sid", Define_C.s_shopingId);
		params.add("oid", Define_C.s_old);
		MgqRestClient.get(Define_C.mgw_url, params, loginHandler);

	}
}
