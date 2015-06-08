package com.mgw.member.ui.activity.login;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.mgw.member.R;
import com.mgw.member.constant.Define_C;
import com.mgw.member.uitls.MgqDataHandler;
import com.mgw.member.uitls.MgqRestClient;
import com.mgw.member.uitls.UIUtils;

public class ForgetPasswordActivity extends Activity implements OnClickListener {
	Button mCodeBtn = null;
	int mSeconds = 120;
	Timer mTimer = null;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (mSeconds == 0) {
				mTimer.cancel();
				mTimer = null;

				mSeconds = 120;
				mCodeBtn.setSelected(false);
				mCodeBtn.setOnClickListener(ForgetPasswordActivity.this);
				mCodeBtn.setText("获取验证码");
			} else {
				mCodeBtn.setText(String.valueOf(mSeconds));
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_forget_password);

		mCodeBtn = (Button) findViewById(R.id.code);

		mCodeBtn.setSelected(true);
		mCodeBtn.setText(String.valueOf(mSeconds));
		findViewById(R.id.login).setOnClickListener(this);

		((TextView) findViewById(R.id.tv_title_cent)).setText("手机验证码找回密码");

		findViewById(R.id.login).setOnClickListener(this);
		findViewById(R.id.bt_titlebar_left).setOnClickListener(this);
		findViewById(R.id.tv_login_problem).setOnClickListener(this);

		startTimer();
	}

	void startTimer() {
		mCodeBtn.setOnClickListener(null);
		mCodeBtn.setSelected(true);

		mTimer = new Timer();
		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (mSeconds == 0) {
					handler.sendEmptyMessage(0);
					return;
				}

				mSeconds--;
				handler.sendEmptyMessage(0);
			}
		}, 1000, 1000);
	}

	public void showDialog() {
		final Dialog dialog = new Dialog(this, R.style.dialog);
		dialog.setContentView(R.layout.dialog_query);
		dialog.setCancelable(true);
		dialog.findViewById(R.id.call).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();

				Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:0731-82680939"));
				startActivity(phoneIntent);
			}
		});
		dialog.findViewById(R.id.cancel).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
		((TextView) dialog.findViewById(R.id.tip)).setText("请使用此手机号码致电美顾问找回密码\n" + "电话: 0731-82680939\n" + "受理时间: 9:00 - 18:00");
		dialog.show();
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

		case R.id.code:
			getCode();
			break;

		case R.id.tv_login_problem: {
			showDialog();
		}
			break;
		}
	}

	/**
	 * 获取验证码
	 */
	private void getCode() {
		MgqDataHandler loginHandler = new MgqDataHandler(this, true, false) {
			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);

				try {
					JSONObject obj = new JSONObject(response);
					Log.e("json", obj.toString());
					if (obj.getInt("flag") == 0) {
						startTimer();
					} else {
						// ToastUtil.showToastWithAlertPic(obj.getString("msg"));
						UIUtils.showToastSafe(obj.getString("msg"));
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
		params.put("tel", getIntent().getExtras().getString("tel"));
		MgqRestClient.get(Define_C.mgw_url, params, loginHandler);
	}

	/**
	 * 完成 提交数据
	 */
	private void getData() {
		final String code = ((TextView) findViewById(R.id.tel)).getText().toString();
		final String pwd = ((TextView) findViewById(R.id.password)).getText().toString();
		final String pwd0 = ((TextView) findViewById(R.id.password0)).getText().toString();
		if (code.length() == 0 || pwd.length() == 0 || pwd0.length() == 0) {
			UIUtils.showToastSafe("请输入验证码&密码");
			return;
		}

		if (!pwd.equals(pwd0)) {
			// ToastUtil.showToastWithAlertPic("两次密码输入不一致，请重新输入");
			UIUtils.showToastSafe("两次密码输入不一致，请重新输入");
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
						// ToastUtil.showToastWithOkPic("密码修改成功，请登录");
						UIUtils.showToastSafe("密码修改成功，请登录");

						LoginActivity.mLoginTel = getIntent().getExtras().getString("tel");
						finish();
					} else {
						// ToastUtil.showToastWithAlertPic(obj.getString("msg"));
						UIUtils.showToastSafe(obj.getString("msg"));
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
		params.put("type", "member.resetpwd");
		params.put("userid", getIntent().getExtras().getString("userid"));
		params.put("tel", getIntent().getExtras().getString("tel"));
		params.put("code", code);
		params.put("newpwd", pwd);
		params.put("snewpwd", pwd0);
		MgqRestClient.get(Define_C.mgw_url, params, loginHandler);
	}
}
