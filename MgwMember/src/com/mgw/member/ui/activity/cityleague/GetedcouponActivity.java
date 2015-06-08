package com.mgw.member.ui.activity.cityleague;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.mgw.member.R;
import com.mgw.member.ui.activity.GivecouponActivity;
import com.mgw.member.ui.activity.MymaterialActivity;

public class GetedcouponActivity extends Activity {
	private TextView tv_getedcoupon_name, tv_getedcoupon_canusetime,
			tv_attention, tv_seemycoupon;
	private Button bt_getedcoupon_toshop, ibBack_getedcoupon,
			bt_getedcoupon_givefriend;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.gettedcouponactivity);
		ibBack_getedcoupon = (Button) findViewById(R.id.ibBack_getedcoupon);
		bt_getedcoupon_toshop = (Button) findViewById(R.id.bt_getedcoupon_toshop);
		bt_getedcoupon_givefriend = (Button) findViewById(R.id.bt_getedcoupon_givefriend);
		tv_getedcoupon_name = (TextView) findViewById(R.id.tv_getedcoupon_name);
		tv_getedcoupon_canusetime = (TextView) findViewById(R.id.tv_getedcoupon_canusetime);
		tv_attention = (TextView) findViewById(R.id.tv_attention2);
		tv_seemycoupon = (TextView) findViewById(R.id.tv_seemycoupon);
		VipmaterialActivity.shouldflush = true;
		init();
	}

	public void init() {
		Intent intent = getIntent();
		tv_getedcoupon_name.setText(intent.getStringExtra("name"));
		String time = intent.getStringExtra("time");
		tv_getedcoupon_canusetime.setText(time);
		tv_attention.setText(intent.getStringExtra("desc"));
		final String sid = intent.getStringExtra("sid");
		final String coupon = intent.getStringExtra("coupon");
		final String cover = intent.getStringExtra("cover");
		final String name = intent.getStringExtra("name");
		bt_getedcoupon_toshop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(GetedcouponActivity.this,
						ShopDetailAndIntroduceActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("sid", sid);
				intent.putExtras(bundle);
				startActivity(intent);
				finish();
			}
		});
		ibBack_getedcoupon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
		tv_seemycoupon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent2 = new Intent(GetedcouponActivity.this,
						MymaterialActivity.class);
				startActivity(intent2);
				finish();

			}
		});
		bt_getedcoupon_givefriend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent3 = new Intent(GetedcouponActivity.this,
						GivecouponActivity.class);
				intent3.putExtra("coupon", coupon);
				intent3.putExtra("cover", cover);
				intent3.putExtra("name", name);
				startActivity(intent3);
				finish();

			}
		});
	}
}
