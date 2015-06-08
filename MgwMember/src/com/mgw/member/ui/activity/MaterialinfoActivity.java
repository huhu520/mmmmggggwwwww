package com.mgw.member.ui.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.loopj.android.http.RequestParams;
import com.mgw.member.R;
import com.mgw.member.constant.Define_C;
import com.mgw.member.ui.activity.cityleague.CityleagueActivity;
import com.mgw.member.ui.activity.cityleague.GetedcouponActivity;
import com.mgw.member.ui.activity.cityleague.VipmaterialActivity;
import com.mgw.member.uitls.MgqDataHandler;
import com.mgw.member.uitls.MgqRestClient;

public class MaterialinfoActivity extends Activity {
	private ImageView imageView_vipmaterialinfo_cover;
	private Button button_vipmaterialinfo_get, ibBack_materialinfo, bt_materialinfo_call;
	private TextView tv_vipmaterialinfo_name, tv_vipmaterialinfo_no, tv_materialinfo_sname, tv_materialinfo_saddr, tv_materialinfo_distance, tv_materialinfo_attention, tv_materialinfo_stand;
	private ProgressDialog progressDialog = null;
	BitmapUtils bitmapUtils;
	String coupon = "";
	String cover = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_materialinfo);
		imageView_vipmaterialinfo_cover = (ImageView) findViewById(R.id.imageView_vipmaterialinfo_cover);
		bitmapUtils = new BitmapUtils(this);
		button_vipmaterialinfo_get = (Button) findViewById(R.id.button_vipmaterialinfo_get);
		ibBack_materialinfo = (Button) findViewById(R.id.ibBack_materialinfo);
		tv_vipmaterialinfo_name = (TextView) findViewById(R.id.tv_vipmaterialinfo_name);
		tv_vipmaterialinfo_no = (TextView) findViewById(R.id.tv_vipmaterialinfo_no);
		tv_materialinfo_sname = (TextView) findViewById(R.id.tv_materialinfo_sname);
		tv_materialinfo_saddr = (TextView) findViewById(R.id.tv_materialinfo_saddr);
		tv_materialinfo_distance = (TextView) findViewById(R.id.tv_materialinfo_distance);
		tv_materialinfo_attention = (TextView) findViewById(R.id.tv_materialinfo_attention);
		tv_materialinfo_stand = (TextView) findViewById(R.id.tv_materialinfo_stand);
		bt_materialinfo_call = (Button) findViewById(R.id.bt_materialinfo_call);

		getdata();
		init();
	}

	String time = "";

	public void init() {
		ibBack_materialinfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
		Intent intent = getIntent();
		coupon = intent.getStringExtra("coupon");
		if (intent.getStringExtra("type").equals("vipmaterial")) {
			if (intent.getIntExtra("canUse", 5) == 0) {
				button_vipmaterialinfo_get.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						getcoupon();

					}
				});
				button_vipmaterialinfo_get.setText("立即领取");
			} else {
				button_vipmaterialinfo_get.setBackgroundResource(R.drawable.bg_grey);
				button_vipmaterialinfo_get.setText("已领");

			}
		} else {
			button_vipmaterialinfo_get.setVisibility(View.GONE);
			findViewById(R.id.tv_vipmaterialinfo_had).setVisibility(View.GONE);
			tv_vipmaterialinfo_no.setVisibility(View.GONE);
			findViewById(R.id.tv_vipmaterialinfo_zhang).setVisibility(View.GONE);
		}

	}

	/**
	 * 获取消费券详情
	 * */
	public void getdata() {

		MgqDataHandler loginHandler = new MgqDataHandler(getApplicationContext(), false, false) {
			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				try {
					JSONObject obj = new JSONObject(response);

					Log.e("json", obj.toString());
					if (obj.getInt("flag") == 0) {
						JSONObject item = obj.getJSONArray("items").getJSONObject(0);
						cover = item.getString("cover");
						bitmapUtils.display(imageView_vipmaterialinfo_cover, cover);
						// ImageLoaderHelper.displayImage(R.drawable.img_loading,
						// imageView_vipmaterialinfo_cover,
						// item.getString("cover"));
						tv_vipmaterialinfo_name.setText(item.getString("name"));
						tv_vipmaterialinfo_no.setText(item.getString("num"));
						time = item.getString("expire");
						tv_materialinfo_attention.setText(time);
						tv_materialinfo_stand.setText(item.getString("desc"));
						if (item.getInt("canUse") != 0) {
							button_vipmaterialinfo_get.setClickable(false);
							button_vipmaterialinfo_get.setBackgroundResource(R.drawable.bg_grey);
							button_vipmaterialinfo_get.setText("已领");
						}
					} else {
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
				getdatajiben();
			}
		};
		RequestParams params = new RequestParams();
		String type = "coupon.couponinfo";
		Intent intent = getIntent();
		// String coupon = intent.getStringExtra("coupon");
		String intenttype = intent.getStringExtra("type");

		if (intenttype.equals("mycoupon")) {
			params.add("coupon", intent.getStringExtra("cid"));
		} else {
			params.add("coupon", intent.getStringExtra("coupon"));
		}
		try {
			JSONObject obj = new JSONObject(getSharedPreferences("mgw_data", Context.MODE_PRIVATE).getString("mgw_data", ""));
			params.add("type", type);
			params.add("userid", obj.getString("UserID"));
			params.add("serial", obj.getString("serial"));

			MgqRestClient.get(Define_C.mgw_url, params, loginHandler);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

	}

	/**
	 * 获取商家基本信息
	 * 
	 * 
	 * */
	public void getdatajiben() {

		MgqDataHandler loginHandler = new MgqDataHandler(getApplicationContext(), false, false) {
			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				try {
					JSONObject obj = new JSONObject(response);
					final JSONObject item = obj.getJSONArray("items").getJSONObject(0);
					Log.e("json", obj.toString());
					if (obj.getInt("flag") == 0) {
						tv_materialinfo_sname.setText(item.get("sname").toString());
						tv_materialinfo_saddr.setText(item.getString("saddr"));
						tv_materialinfo_distance.setText(String.format("< %.2fkm", Double.valueOf(item.get("distance").toString())));
						if (!item.getString("Tel").equals("")) {
							bt_materialinfo_call.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									Intent intentCall;
									try {
										intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + item.getString("Tel")));
										startActivity(intentCall);
									} catch (JSONException e) {

										e.printStackTrace();
									}

								}
							});

						}

					} else {
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

			}
		};
		RequestParams params = new RequestParams();
		String type = "wzreposity.supplierinfo";
		Intent intent = getIntent();
		String sid = intent.getStringExtra("sid");

		params.add("type", type);
		params.add("userid", getSharedPreferences("mgw_data", 0).getString("mgw_userID", null));
		params.add("serial", getSharedPreferences("mgw_data", 0).getString("mgw_serial", null));
		params.add("sid", sid);
		params.add("posx", CityleagueActivity.m_lng + "");
		params.add("posy", CityleagueActivity.m_lat + "");
		MgqRestClient.get(Define_C.mgw_url, params, loginHandler);

	}

	/**
	 * 领取消费券
	 * 
	 * 
	 * */
	public void getcoupon() {
		if (progressDialog == null) {
			progressDialog = ProgressDialog.show(this, "", "正在加载中...", true, false);
			progressDialog.setCancelable(false);
			progressDialog.setCanceledOnTouchOutside(false);
		}

		MgqDataHandler loginHandler = new MgqDataHandler(getApplicationContext(), false, false) {
			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				try {
					JSONObject obj = new JSONObject(response);
					Log.e("json", obj.toString());
					if (obj.getInt("flag") == 0) {
						if (progressDialog != null) {
							progressDialog.dismiss();
							progressDialog = null;
						}

						Intent intent = new Intent(MaterialinfoActivity.this, GetedcouponActivity.class);

						intent.putExtra("name", obj.getJSONObject("item").getString("name"));
						intent.putExtra("desc", obj.getJSONObject("item").getString("desc"));
						intent.putExtra("sid", obj.getJSONObject("item").getString("sid"));
						intent.putExtra("cover", cover);
						intent.putExtra("time", time);
						intent.putExtra("coupon", obj.getJSONObject("item").getString("citem"));
						startActivity(intent);
						Toast.makeText(getApplicationContext(), "领取成功！", Toast.LENGTH_SHORT).show();
					} else {
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
					progressDialog = null;
				}

			}
		};
		RequestParams params = new RequestParams();
		String type = "coupon.getcoupon";

		try {
			JSONObject obj = new JSONObject(getSharedPreferences("mgw_data", Context.MODE_PRIVATE).getString("mgw_data", ""));
			params.add("type", type);
			params.add("userid", obj.getString("UserID"));
			params.add("serial", obj.getString("serial"));
			params.add("coupon", coupon);
			MgqRestClient.get(Define_C.mgw_url, params, loginHandler);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

	}

	@Override
	protected void onRestart() {

		super.onRestart();
		if (VipmaterialActivity.shouldflush) {
			getdata();
		}

	}

}
