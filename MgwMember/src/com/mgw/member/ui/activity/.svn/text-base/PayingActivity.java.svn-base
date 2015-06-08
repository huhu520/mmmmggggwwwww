package com.mgw.member.ui.activity;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.mgw.member.R;
import com.mgw.member.constant.Define_C;
import com.mgw.member.constant.imp_Define;
import com.mgw.member.http.Http;
import com.mgw.member.uitls.MgqDataHandler;
import com.mgw.member.uitls.MgqRestClient;

public class PayingActivity extends Activity implements imp_Define {
	JSONArray my_Array_list = new JSONArray();
	private TextView hour, min, outlet, fanli;
	private ListView lv_paying_canusecoupon;
	private String m_ShopingId;
	long m_time;
	private Timer m_Timer;
	private TimerTask m_TimerTask;
	private ProgressDialog progressDialog;
	private MyAdapter myAdapter;
	private Button ibBack_paying;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_paying);
		hour = (TextView) findViewById(R.id.hour);
		min = (TextView) findViewById(R.id.min);
		outlet = (TextView) findViewById(R.id.outlet);
		fanli = (TextView) findViewById(R.id.fanli);
		lv_paying_canusecoupon = (ListView) findViewById(R.id.lv_paying_canusecoupon);
		Intent t_Intent = getIntent();
		m_ShopingId = t_Intent.getStringExtra("ShopingId");
		outlet.setText(Define_C.s_discount);
		fanli.setText(Define_C.s_return_money);
		ibBack_paying = (Button) findViewById(R.id.ibBack_paying);
		new Thread() {
			@Override
			public void run() {
				try {
					Message message = Message.obtain();
					message.what = MESSAGE_TYPE_SHOPING_MESSAGE_1;
					JSONObject t_JsonObject = new JSONObject();
					JSONObject t_JsonObject2 = new JSONObject();
					t_JsonObject.put("title", "message");
					t_JsonObject.put("description", "description");
					t_JsonObject2.put("type", "1");
					t_JsonObject2.put("playerId", getSharedPreferences("mgw_data", 0).getString("mgw_userID", null));
					t_JsonObject2.put("playerName", getSharedPreferences("mgw_data", 0).getString("mgw_name", null));
					t_JsonObject2.put("shopingId", m_ShopingId);
					t_JsonObject2.put("orderId", Define_C.s_orderId);
					t_JsonObject.put("custom_content", t_JsonObject2);
					message.obj = Http.postShopingOrder(Define_C.s_old, t_JsonObject.toString());
					mHandler.sendMessage(message);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}.start();

		m_Timer = new Timer();
		m_TimerTask = new TimerTask() {
			@Override
			public void run() {
				Message message = Message.obtain();
				message.what = MESSAGE_TYPE_SHOPING_MESSAGE_TIME;
				mHandler.sendMessage(message);
			}
		};

		m_Timer.schedule(m_TimerTask, 1000, 1000);
		myAdapter = new MyAdapter();
		lv_paying_canusecoupon.setAdapter(myAdapter);
		startGetStatus();
		init();
		getcoupon();
	}

	public void init() {
		ibBack_paying.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
	}

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			JSONObject t_JsonObject;
			switch (msg.what) {
			case MESSAGE_TYPE_GET_ORDER_ID:
				try {
					t_JsonObject = (JSONObject) msg.obj;
					if (t_JsonObject == null) {
						Toast.makeText(PayingActivity.this, "网络异常！", Toast.LENGTH_LONG).show();
						startGetStatus();
						return;
					}
					if (t_JsonObject.getInt("flag") == 0) {// 成功
						JSONArray t_JsonArray = t_JsonObject.getJSONArray("items");
						t_JsonObject = t_JsonArray.getJSONObject(0);
						Intent intent;
						switch (t_JsonObject.getInt("Status")) {
						case 0:
							startGetStatus();
							break;
						case 1:
							intent = new Intent(PayingActivity.this, PayTypeActivity.class);

							PayingActivity.this.startActivity(intent);
							finish();
							break;
						default:
							intent = new Intent(PayingActivity.this, OrderFinishActivity.class);
							intent.putExtra("fmbid", Define_C.s_orderId);
							// if (OrderActivity.s_Instance != null) {
							// OrderActivity.s_Instance.m_JsonArray = null;
							// }
							switch (t_JsonObject.getInt("FMB_Payment")) {
							case 1:
								intent.putExtra("type", (byte) 0);
								break;
							case 2:
								intent.putExtra("type", (byte) 1);
								break;
							}
							intent.putExtra("isfinish", "yes");
							PayingActivity.this.startActivity(intent);
							finish();
							break;
						}
					} else if (t_JsonObject.getInt("flag") == 10) {
						Toast.makeText(PayingActivity.this, t_JsonObject.getString("msg"), Toast.LENGTH_LONG).show();
						finish();
					} else {
						Toast.makeText(PayingActivity.this, t_JsonObject.getString("msg"), Toast.LENGTH_LONG).show();

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

			case MESSAGE_TYPE_SHOPING_MESSAGE_1:
				// {"request_id":1761317002,"response_params":{"success_amount":1}}
				try {
					t_JsonObject = (JSONObject) msg.obj;

					if (!t_JsonObject.isNull("response_params")) {// 成功
						if (!t_JsonObject.getJSONObject("response_params").isNull("success_amount")) {

						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case MESSAGE_TYPE_SHOPING_MESSAGE_TIME:
				if (m_time == 0)
					m_time = System.currentTimeMillis();
				long t_time = System.currentTimeMillis() - m_time;
				Date t_Date = new Date(t_time);
				hour.setText(String.valueOf(t_Date.getMinutes()));
				min.setText(String.valueOf(t_Date.getSeconds()));
				break;
			}

		};
	};

	void startGetStatus() {
		new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(2000);
					Message message = Message.obtain();
					message.what = MESSAGE_TYPE_GET_ORDER_ID;
					message.obj = Http.postGetOrderState(Define_C.s_orderId);
					mHandler.sendMessage(message);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if (my_Array_list == null) {
				return 0;
			}
			return my_Array_list.length();
		}

		@Override
		public Object getItem(int position) {

			return null;
		}

		@Override
		public long getItemId(int position) {

			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder mHolder;
			if (convertView == null) {
				mHolder = new ViewHolder();
				LayoutInflater mInflater = LayoutInflater.from(PayingActivity.this);
				convertView = mInflater.inflate(R.layout.item_canusecoupon, null);
				mHolder.quan0 = (TextView) convertView.findViewById(R.id.quan0);
				mHolder.quan0_sum = (TextView) convertView.findViewById(R.id.quan0_sum);
				convertView.setTag(mHolder);

			} else {
				mHolder = (ViewHolder) convertView.getTag();
			}
			try {
				JSONObject item = my_Array_list.getJSONObject(position);
				mHolder.quan0.setText(item.getString("name"));
				mHolder.quan0_sum.setText("￥" + item.getString("amount"));

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return convertView;
		}
	}

	class ViewHolder {
		public TextView quan0;
		public TextView quan0_sum;
	}

	/**
	 * 获取消费者在当前店的消费券信息
	 * 
	 * 
	 * */
	public void getcoupon() {
		progressDialog = ProgressDialog.show(this, "", "正在加载中...", true, false);
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		if (myAdapter == null) {
			my_Array_list = new JSONArray();
			myAdapter = new MyAdapter();
			lv_paying_canusecoupon.setAdapter(myAdapter);
		}
		MgqDataHandler loginHandler = new MgqDataHandler(getApplicationContext(), false, false) {
			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				try {
					JSONObject obj = new JSONObject(response);
					Log.e("json", obj.toString());
					if (obj.getInt("flag") == 0) {
						JSONArray array = obj.getJSONArray("items");
						int len = array.length();
						if (len == 0) {
							Toast.makeText(PayingActivity.this, "您没有我家可用的消费券！", Toast.LENGTH_SHORT).show();
						}
						if (progressDialog != null) {
							progressDialog.dismiss();
						}
						int count = my_Array_list.length();
						for (int i = 0; i < len; i++) {
							my_Array_list.put(i + count, array.getJSONObject(i));
						}
						myAdapter.notifyDataSetChanged();
					} else {
						if (progressDialog != null) {
							progressDialog.dismiss();
						}
						Toast.makeText(getApplicationContext(), obj.getString("msg"), Toast.LENGTH_SHORT).show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable ble) {

			}

			@Override
			public void onFinish() {
				if (progressDialog != null) {
					progressDialog.dismiss();
				}

			}
		};
		RequestParams params = new RequestParams();
		String type = "coupon.usercoupon";

		params.add("type", type);
		params.add("userid", getSharedPreferences("mgw_data", 0).getString("mgw_userID", null));
		params.add("sid", m_ShopingId);
		MgqRestClient.get(Define_C.mgw_url, params, loginHandler);

	}
}
