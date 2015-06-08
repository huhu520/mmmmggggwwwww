package com.mgw.member.ui.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.mgw.member.constant.Define_C;
import com.mgw.member.uitls.MgqDataHandler;
import com.mgw.member.uitls.MgqRestClient;
import com.mgw.member.R;

public class SaomadengluActivity extends Activity {
	private TextView tv_smdqx;
	private Button btn_dlmgw;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_saomadenglu);
		tv_smdqx = (TextView) findViewById(R.id.tv_smdqx);
		btn_dlmgw = (Button) findViewById(R.id.btn_dlmgw);
		init();
	}

	private void init() {
		tv_smdqx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
		btn_dlmgw.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				confirmData();
			}
		});
	}

	private void confirmData() {
		MgqDataHandler loginHandler = new MgqDataHandler(this, true, false) {
			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				try {
					JSONObject obj = new JSONObject(response);
					Log.e("json", obj.toString());
					if (obj.getInt("flag") == 0) {
						Toast.makeText(getApplicationContext(), "登录成功！", Toast.LENGTH_SHORT).show();
						finish();

					} else {
						// ToastUtil.showToastWithAlertPic(obj.getString("msg"));
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

		Intent intent = getIntent();
		String str = intent.getStringExtra("str");
		String scanserial = str.substring(21, str.length());
		Log.i("str--->", str);
		Log.i("scanserial--->", scanserial);
		RequestParams params = new RequestParams();
		String type = "platinum.scanloading";
		try {
			JSONObject obj = new JSONObject(getSharedPreferences("mgw_data", Context.MODE_PRIVATE).getString("mgw_data", ""));
			params.add("type", type);
			params.add("userid", obj.getString("UserID"));
			params.add("scanserial", scanserial);
			MgqRestClient.get(Define_C.mgw_url, params, loginHandler);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

	}

}
