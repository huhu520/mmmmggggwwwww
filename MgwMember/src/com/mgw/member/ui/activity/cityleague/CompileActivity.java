package com.mgw.member.ui.activity.cityleague;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.mgw.member.R;
import com.mgw.member.constant.Define_C;
import com.mgw.member.uitls.MgqDataHandler;
import com.mgw.member.uitls.MgqRestClient;
import com.mgw.member.uitls.UIUtils;

public class CompileActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compile);

		((Button) findViewById(R.id.bt_titlebar_left)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		((Button) findViewById(R.id.register)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				getData();
			}
		});

		((TextView) findViewById(R.id.tv_title_cent)).setText("投诉商家");
	}

	void getData() {
		String text = ((EditText) findViewById(R.id.tel)).getText().toString();
		if (text.length() == 0) {
			UIUtils.showToastWithAlertPic("您没有输入投诉内容");
			return;
		}

		MgqDataHandler loginHandler = new MgqDataHandler(this, true, false) {
			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);

				try {
					JSONObject obj = new JSONObject(response);
					Log.e("TakeCare", obj.toString());
					if (obj.getInt("flag") == 0) {
						UIUtils.showToastWithOkPic("投诉成功");
						finish();
					} else {
						UIUtils.showToastWithAlertPic(obj.getString("msg"));

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
		params.put("type", "wzreposity.complaints");
		try {
			JSONObject obj = new JSONObject(getSharedPreferences("mgw_data", Context.MODE_PRIVATE).getString("mgw_data", ""));
			params.put("userid", obj.getString("UserID"));
			params.put("serial", obj.getString("serial"));
			params.put("text", text);
			params.put("sid", getIntent().getExtras().getString("sid"));
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		MgqRestClient.get(Define_C.mgw_url, params, loginHandler);
	}
}
