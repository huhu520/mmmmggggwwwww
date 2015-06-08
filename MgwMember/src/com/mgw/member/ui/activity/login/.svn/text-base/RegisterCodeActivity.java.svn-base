package com.mgw.member.ui.activity.login;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.loopj.android.http.RequestParams;
import com.mgw.member.R;
import com.mgw.member.constant.Define_C;
import com.mgw.member.http.Http;
import com.mgw.member.uitls.LogUtils;
import com.mgw.member.uitls.MgqDataHandler;
import com.mgw.member.uitls.MgqRestClient;
import com.mgw.member.uitls.UIUtils;

public class RegisterCodeActivity extends Activity implements OnClickListener {
	private static final String TAG = RegisterCodeActivity.class.getSimpleName();
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
				mCodeBtn.setOnClickListener(RegisterCodeActivity.this);
				mCodeBtn.setText("获取验证码");
			} else {
				mCodeBtn.setText(String.valueOf(mSeconds));
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_code);

		mCodeBtn = (Button) findViewById(R.id.code);

		mCodeBtn.setSelected(true);
		mCodeBtn.setText(String.valueOf(mSeconds));
		findViewById(R.id.login).setOnClickListener(this);

		((TextView) findViewById(R.id.tv_title_cent)).setText("手机号码验证");

		findViewById(R.id.login).setOnClickListener(this);
		findViewById(R.id.bt_titlebar_left).setOnClickListener(this);
		findViewById(R.id.password0).setOnClickListener(this);
		findViewById(R.id.bt_titlebar_left).setOnClickListener(this);

		// startTimer();
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login:
			getData();
			break;

		case R.id.password0:
			showDialog();
			break;

		case R.id.code:
			getCode();
			break;

		case R.id.bt_titlebar_left:
			finish();
			break;

		}
	}

	public void showDialog() {
		final Dialog dialog = new Dialog(this, R.style.dialog);
		dialog.setContentView(R.layout.dialog_sex);
		dialog.setCancelable(true);
		dialog.findViewById(R.id.male).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
				((TextView) findViewById(R.id.password0)).setText("男");
			}
		});
		dialog.findViewById(R.id.female).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
				((TextView) findViewById(R.id.password0)).setText("女");
			}
		});
		dialog.show();
	}

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
		params.put("tel", getIntent().getExtras().getString("tel"));
		MgqRestClient.get(Define_C.mgw_url, params, loginHandler);
	}

	private void getData() {
		// final String code =
		// ((EditText)findViewById(R.id.tel)).getText().toString();
		final String nick = ((EditText) findViewById(R.id.password)).getText().toString();
		final String sex = ((TextView) findViewById(R.id.password0)).getText().toString();
		if (nick.length() == 0 || sex.length() == 0) {
			UIUtils.showToastWithAlertPic("请输入所有信息");
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
						
						
						UIUtils.showToastWithOkPic("注册成功，请登录");
						LoginActivity.mLoginTel = getIntent().getExtras().getString("tel");
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
		params.put("type", "member.createreg");
		params.put("card", getIntent().getExtras().getString("card"));
		params.put("pwd", getIntent().getExtras().getString("pwd"));
		params.put("tel", getIntent().getExtras().getString("tel"));
		// params.put("code", code);
		params.put("nick", nick);
		params.put("gender", sex);
		MgqRestClient.get(Define_C.mgw_url, params, loginHandler);
	}
	
	
	
	private void registerHaunXin( String username,  String pwd) {
		try {
			LogUtils.i(TAG + "registerHaunXin_username=" + username + ",pwd=" + pwd);
			// 调用sdk注册方法
			EMChatManager.getInstance().createAccountOnServer(username, pwd);

			
			// 更新美顾问数据
			Http.postUesrs2(username, username);
		

		} catch (final Exception e) {
			try {
				if (e.getMessage().equals("conflict(409)")) {
					// 保存用户名
					// SystemApplication.getInstance().setUserName(username);

					// etphLoginActivity.isReg=true;
					Define_C.s_RelationID = username;
					// 更新美顾问数据
					// Http.postUesrs2(username, username);
					// 保存用户名
					SharedPreferences sharedata3 = getSharedPreferences("mgw_data", 0);
					SharedPreferences.Editor sharedata = sharedata3.edit();
					String data = sharedata3.getString("mgw_data", null);
					if (data != null && data.length() > 0) {

						sharedata.putString("RelationID", username);
						Define_C.s_isDelete = 1;
						sharedata.commit();
					}
				}
			} catch (final Exception e1) {

			}
		}
	}
	
	
}
