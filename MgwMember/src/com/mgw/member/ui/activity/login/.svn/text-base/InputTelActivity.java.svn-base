package com.mgw.member.ui.activity.login;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.mgw.member.R;
import com.mgw.member.constant.Define_C;
import com.mgw.member.uitls.MgqDataHandler;
import com.mgw.member.uitls.MgqRestClient;
import com.mgw.member.uitls.UIUtils;
import com.mgw.member.uitls.Utils;

@SuppressLint("HandlerLeak")
public class InputTelActivity extends Activity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_input_tel);

		((TextView) findViewById(R.id.tv_title_cent)).setText("手机验证码找回密码");

		findViewById(R.id.login).setOnClickListener(this);
		findViewById(R.id.bt_titlebar_left).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login:
			getData();
			break;

		case R.id.bt_titlebar_left:
			finish();
			break;
		}
	}

	private void getData() {
		final String tel = ((TextView) findViewById(R.id.tel)).getText().toString();
		if (!Utils.isMobileNO(tel)) {
			// ToastUtil.showToastWithAlertPic("您输入的电话号码不正确");
			UIUtils.showToastSafe("您输入的电话号码不正确");
			return;
		}

		MgqDataHandler loginHandler = new MgqDataHandler(this, true, false) {
			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);

				try {
					JSONObject obj = new JSONObject(response);
					Log.e("json", obj.toString());
					if (obj.getInt("flag") == 0) {
						Intent intent = new Intent(InputTelActivity.this, ForgetPasswordActivity.class);
						intent.putExtra("tel", tel);
						intent.putExtra("userid", obj.getJSONObject("item").getString("userid"));
						startActivity(intent);
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
		params.put("type", "member.sentmsg");
		params.put("tel", tel);
		MgqRestClient.get(Define_C.mgw_url, params, loginHandler);
	}
}
