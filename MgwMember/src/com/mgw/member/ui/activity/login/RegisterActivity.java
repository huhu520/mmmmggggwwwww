package com.mgw.member.ui.activity.login;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.loopj.android.http.RequestParams;
import com.mgw.member.R;
import com.mgw.member.RememberLoginName;
import com.mgw.member.constant.Define_C;
import com.mgw.member.http.Http;
import com.mgw.member.manager.GreenDaoDBHelper;
import com.mgw.member.ui.activity.MGWBaseActivity;
import com.mgw.member.uitls.LogUtils;
import com.mgw.member.uitls.MgqDataHandler;
import com.mgw.member.uitls.MgqRestClient;
import com.mgw.member.uitls.UIUtils;
import com.mgw.member.uitls.Utils;

public class RegisterActivity extends MGWBaseActivity implements OnClickListener {
	private static final String TAG = RegisterActivity.class.getSimpleName();
	boolean mShow = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);

		findViewById(R.id.bt_titlebar_left).setOnClickListener(this);
		findViewById(R.id.show).setOnClickListener(this);
		findViewById(R.id.login).setOnClickListener(this);
		((TextView) findViewById(R.id.tv_title_cent)).setText("会员注册");
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.bt_titlebar_left:
			finish();
			break;

		case R.id.login:
			getData();
			break;

		case R.id.show:
			if (!mShow) {
				mShow = true;
				((ImageButton) findViewById(R.id.show)).setSelected(true);
				((EditText) findViewById(R.id.password)).setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
			} else {
				mShow = false;
				((ImageButton) findViewById(R.id.show)).setSelected(false);
				((EditText) findViewById(R.id.password)).setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
			}
			break;
		}
	}

	private void getData() {
		final String tel = ((TextView) findViewById(R.id.tel)).getText().toString();
		final String pwd = ((TextView) findViewById(R.id.password)).getText().toString();
		final String card = ((TextView) findViewById(R.id.recommand)).getText().toString();
		if (!Utils.isMobileNO(tel)) {
			UIUtils.showToastWithAlertPic("您输入的电话号码不正确");
			return;
		}

		if (pwd.length() == 0 || card.length() == 0) {
			UIUtils.showToastWithAlertPic("请完善所有信息");
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
						
						
						Intent intent = new Intent(RegisterActivity.this, RegisterCodeActivity.class);
						intent.putExtra("tel", tel);
						intent.putExtra("pwd", pwd);
						intent.putExtra("card", card);
						GreenDaoDBHelper.getInstance(mContext).addRememberLoginNameDao(new RememberLoginName(tel, pwd, new Date(),false));
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
		params.put("type", "member.register");
		params.put("tel", tel);
		params.put("pwd", pwd);
		params.put("card", card);
		MgqRestClient.get(Define_C.mgw_url, params, loginHandler);
	}
	
	

}
