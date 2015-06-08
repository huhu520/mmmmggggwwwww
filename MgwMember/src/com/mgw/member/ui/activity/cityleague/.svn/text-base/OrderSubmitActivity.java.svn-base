package com.mgw.member.ui.activity.cityleague;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.mgw.member.R;
import com.mgw.member.constant.Define_C;
import com.mgw.member.uitls.MgqDataHandler;
import com.mgw.member.uitls.MgqRestClient;
import com.mgw.member.uitls.Utils;

public class OrderSubmitActivity extends BaseActivity2 implements OnClickListener {
	private int count = 1;
	private double price = 0;
	private double totalprice = 0;
	private double ssafe = 0;
	public static String sid;
	private int ptype;

	JSONArray mAddress = null;
	int mSelectAddr = 0;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.order_submit_withposition);
		price = getIntent().getExtras().getDouble("price");
		sid = getIntent().getExtras().getString("sid");
		ptype = getIntent().getExtras().getInt("ptype");
		ssafe = getIntent().getExtras().getDouble("ssafe");
		initTitleButton();

		findViewById(R.id.tv_order_submit_withposition_address).setOnClickListener(this);
		findViewById(R.id.bt_order_submit_add).setOnClickListener(this);
		findViewById(R.id.bt_order_submit_reduce).setOnClickListener(this);
		findViewById(R.id.bt_order_submit_ok).setOnClickListener(this);
		((EditText) findViewById(R.id.et_order_submit_count)).addTextChangedListener(new MyTextWatch());
		((TextView) findViewById(R.id.tv_order_submit_price)).setText(price + "元");
		((TextView) findViewById(R.id.tv_order_sumbit_totalprice)).setText(price + "元");
		((TextView) findViewById(R.id.tv_order_submit_pname)).setText(getIntent().getExtras().getString("pname"));
		double backmoney = price * ssafe * 100;
		java.text.DecimalFormat num = new DecimalFormat("0");
		num.setRoundingMode(RoundingMode.DOWN);
		backmoney = Integer.parseInt(num.format(backmoney));
		backmoney = backmoney / 100;
		((TextView) findViewById(R.id.tv_BackMoney)).setText(backmoney + "元");
		initAddress();

		if (ptype == 0)
			getDataAddr();
		findViewById(R.id.time).setOnClickListener(this);

		findViewById(R.id.tv_titlebar_right).setVisibility(View.INVISIBLE);
		((TextView) findViewById(R.id.tv_title_cent)).setText("提交订单");
	}

	private void initAddress() {
		if (ptype == 1) {
			findViewById(R.id.ll_order_submit_address).setVisibility(View.GONE);
			return;
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.bt_order_submit_add:
			String strcount = ((EditText) findViewById(R.id.et_order_submit_count)).getText().toString().trim();
			if (strcount.equals("")) {
				strcount = "1";
			}
			count = Integer.parseInt(strcount) + 1;
			if (count >= 100) {
				count = 99;
				break;
			}

			((EditText) findViewById(R.id.et_order_submit_count)).setText(count + "");
			break;
		case R.id.bt_order_submit_reduce:
			String strcount2 = ((EditText) findViewById(R.id.et_order_submit_count)).getText().toString().trim();
			if (strcount2.equals("")) {
				strcount2 = "2";
			}
			count = Integer.parseInt(strcount2) - 1;
			if (count <= 0) {
				count = 1;
				break;
			}

			((EditText) findViewById(R.id.et_order_submit_count)).setText(count + "");
			break;
		case R.id.bt_order_submit_ok:
			if (Utils.isFastDoubleClick()) {
				return;
			}
			getDatacreateorder(true);

			break;

		case R.id.time:
			showTimeDialog();
			break;

		case R.id.tv_order_submit_withposition_address:
			// {
			// Intent intent = new Intent(OrderSubmitActivity.this,
			// SelectAddressMainActivity.class);
			// intent.putExtra("array", mAddress == null ? "" :
			// mAddress.toString());
			// startActivityForResult(intent, 10);
			// }
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 可以根据多个请求代码来作相应的操作
		if (10 == resultCode) {
			try {
				mAddress = new JSONArray(data.getExtras().getString("array"));
				mSelectAddr = data.getExtras().getInt("index");
				((TextView) findViewById(R.id.tv_order_submit_withposition_address)).setText(mAddress.getJSONObject(mSelectAddr).getString("addr"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	void showTimeDialog() {
		final String[] title = new String[] { "只限工作日", "只限周末", "工作日、周末均可", "取消" };
		new AlertDialog.Builder(this).setTitle("请选择送货时间").setItems(title, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which < 3)
					((TextView) findViewById(R.id.tv_order_submit_withposition_method)).setText(title[which]);
				dialog.dismiss();
			}
		}).show();
	}

	private class MyTextWatch implements TextWatcher {
		@SuppressLint("NewApi")
		@Override
		public void afterTextChanged(Editable s) {
			String num = ((EditText) findViewById(R.id.et_order_submit_count)).getText().toString();
			count = Integer.parseInt(num);
			totalprice = price * count;
			DecimalFormat num5 = new DecimalFormat("#.00");
			((TextView) findViewById(R.id.tv_order_sumbit_totalprice)).setText(num5.format(totalprice) + "元");
			double backmoney = totalprice * ssafe * 100;
			java.text.DecimalFormat num3 = new DecimalFormat("0");
			num3.setRoundingMode(RoundingMode.DOWN);
			backmoney = Integer.parseInt(num3.format(backmoney));
			backmoney = backmoney / 100;
			((TextView) findViewById(R.id.tv_BackMoney)).setText(backmoney + "元");
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		// ((GlobelElements) getApplicationContext()).mActivityArray.add(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// ((GlobelElements)
		// getApplicationContext()).mActivityArray.remove(this);
	}

	private void getDatacreateorder(boolean show) {
		MgqDataHandler loginHandler = new MgqDataHandler(this, show, false) {
			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);

				try {
					JSONObject obj = new JSONObject(response);
					Log.e("createorder", obj.toString());
					if (obj.getInt("flag") == 0) {
						JSONArray item = obj.getJSONArray("items");
						Intent intent = new Intent(OrderSubmitActivity.this, OrderPayActivity.class);
						intent.putExtra("orderid", item.getJSONObject(0).getString("orderid"));
						intent.putExtra("region", ((TextView) findViewById(R.id.tv_order_submit_withposition_method)).getText().toString());
						startActivity(intent);
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
		params.put("type", "wzreposity.createorder");
		try {
			JSONObject obj = new JSONObject(getSharedPreferences("mgw_data", Context.MODE_PRIVATE).getString("mgw_data", ""));
			params.put("userid", obj.getString("UserID"));
			params.put("serial", obj.getString("serial"));
			params.put("sid", sid);
			params.put("pid", getIntent().getExtras().getString("pid"));
			params.put("quantity", count + "");
			Log.e("count", count + "....");
			params.put("telephone", obj.getString("Telephone"));

			if (mAddress != null) {
				JSONObject item = mAddress.getJSONObject(mSelectAddr);
				params.put("shipto", item.getString("shipto"));
				params.put("addr", item.getString("addr"));
				params.put("cellphone", item.getString("phone"));
				params.put("zipcode", item.getString("zipcode"));
				params.put("regionid", item.getString("regionid"));
			}
			params.put("remark", ((EditText) findViewById(R.id.et_order_submit_option)).getText().toString());
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		MgqRestClient.get(Define_C.mgw_url, params, loginHandler);

	}

	private void getDataAddr() {
		MgqDataHandler loginHandler = new MgqDataHandler(this, true, false) {
			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);

				try {
					JSONObject obj = new JSONObject(response);
					Log.e("createorder", obj.toString());
					if (obj.getInt("flag") == 0) {
						mAddress = obj.getJSONArray("items");
						if (mAddress.length() > 0) {
							mSelectAddr = 0;
							((TextView) findViewById(R.id.tv_order_submit_withposition_address)).setText(mAddress.getJSONObject(mSelectAddr).getString("addr"));
						}
					}

					else {
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
		params.put("type", "wzreposity.getaddr");
		try {
			JSONObject obj = new JSONObject(getSharedPreferences("mgw_data", Context.MODE_PRIVATE).getString("mgw_data", ""));
			params.put("userid", obj.getString("UserID"));
			params.put("serial", obj.getString("serial"));
			params.put("addrid", "0");
			params.put("isdefault", "1");
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		MgqRestClient.post(Define_C.mgw_url, params, loginHandler);
	}
}
