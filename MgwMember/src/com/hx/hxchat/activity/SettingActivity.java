package com.hx.hxchat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.hx.applib.controller.HXSDKHelper;
import com.hx.applib.utils.HXPreferenceUtils;
import com.hx.hxchat.utils.CommonUtils;
import com.mgw.member.R;

public class SettingActivity extends HXBaseActivity implements OnClickListener {
	//
	/**
	 * 设置新消息通知布局
	 */
	private RelativeLayout rl_switch_notification;
	/**
	 * 设置声音布局
	 */
	private RelativeLayout rl_switch_sound;
	/**
	 * 设置震动布局
	 */
	private RelativeLayout rl_switch_vibrate;
	/**
	 * 设置扬声器布局
	 */
	private RelativeLayout rl_switch_speaker;
	/**
	 * 设置通知栏
	 */
	private RelativeLayout rl_switch_notices;

	/**
	 * 打开新消息通知imageView
	 */
	private ImageView iv_switch_open_notification;
	/**
	 * 关闭新消息通知imageview
	 */
	private ImageView iv_switch_close_notification;
	/**
	 * 打开声音提示imageview
	 */
	private ImageView iv_switch_open_sound;
	/**
	 * 关闭声音提示imageview
	 */
	private ImageView iv_switch_close_sound;
	/**
	 * 打开消息震动提示
	 */
	private ImageView iv_switch_open_vibrate;
	/**
	 * 关闭消息震动提示
	 */
	private ImageView iv_switch_close_vibrate;
	/**
	 * 打开扬声器播放语音
	 */
	private ImageView iv_switch_open_speaker;
	/**
	 * 关闭扬声器播放语音
	 */
	private ImageView iv_switch_close_speaker;
	/**
	 * 打开通知栏消息通知
	 */
	private ImageView iv_switch_open_notices;
	/**
	 * 关闭通知栏消息通知
	 */
	private ImageView iv_switch_close_notices;

	/**
	 * 声音和震动中间的那条线
	 */
	private TextView textview1, textview2;

	private LinearLayout blacklistContainer;

	/**
	 * 退出按钮
	 */
	private Button logoutBtn;

	private EMChatOptions chatOptions;

	/**
	 * 诊断
	 */
	private LinearLayout llDiagnose;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);

		setContentView(R.layout.fragment_conversation_settings);
		initView();
	}

	private void initView() {

		rl_switch_notification = (RelativeLayout) findViewById(R.id.rl_switch_notification);
		rl_switch_sound = (RelativeLayout) findViewById(R.id.rl_switch_sound);
		rl_switch_vibrate = (RelativeLayout) findViewById(R.id.rl_switch_vibrate);
		rl_switch_speaker = (RelativeLayout) findViewById(R.id.rl_switch_speaker);
		rl_switch_notices = (RelativeLayout) findViewById(R.id.rl_switch_notices);

		iv_switch_open_notification = (ImageView) findViewById(R.id.iv_switch_open_notification);
		iv_switch_close_notification = (ImageView) findViewById(R.id.iv_switch_close_notification);
		iv_switch_open_sound = (ImageView) findViewById(R.id.iv_switch_open_sound);
		iv_switch_close_sound = (ImageView) findViewById(R.id.iv_switch_close_sound);
		iv_switch_open_vibrate = (ImageView) findViewById(R.id.iv_switch_open_vibrate);
		iv_switch_close_vibrate = (ImageView) findViewById(R.id.iv_switch_close_vibrate);
		iv_switch_open_speaker = (ImageView) findViewById(R.id.iv_switch_open_speaker);
		iv_switch_close_speaker = (ImageView) findViewById(R.id.iv_switch_close_speaker);
		iv_switch_open_notices = (ImageView) findViewById(R.id.iv_switch_open_notices);
		iv_switch_close_notices = (ImageView) findViewById(R.id.iv_switch_close_notices);
		logoutBtn = (Button) findViewById(R.id.btn_logout);
		if (!TextUtils.isEmpty(EMChatManager.getInstance().getCurrentUser())) {
			logoutBtn.setText(getString(R.string.button_logout) + "(" + EMChatManager.getInstance().getCurrentUser() + ")");
		}

		textview1 = (TextView) findViewById(R.id.textview1);
		textview2 = (TextView) findViewById(R.id.textview2);

		blacklistContainer = (LinearLayout) findViewById(R.id.ll_black_list);
		llDiagnose = (LinearLayout) findViewById(R.id.ll_diagnose);
		blacklistContainer.setOnClickListener(this);
		rl_switch_notification.setOnClickListener(this);
		rl_switch_sound.setOnClickListener(this);
		rl_switch_vibrate.setOnClickListener(this);
		rl_switch_speaker.setOnClickListener(this);
		rl_switch_notices.setOnClickListener(this);
		logoutBtn.setOnClickListener(this);
		llDiagnose.setOnClickListener(this);
		chatOptions = EMChatManager.getInstance().getChatOptions();
		if (chatOptions.getNotificationEnable()) {
			iv_switch_open_notification.setVisibility(View.VISIBLE);
			iv_switch_close_notification.setVisibility(View.INVISIBLE);
		} else {
			iv_switch_open_notification.setVisibility(View.INVISIBLE);
			iv_switch_close_notification.setVisibility(View.VISIBLE);
		}

		if (chatOptions.getNoticedBySound()) {
			iv_switch_open_sound.setVisibility(View.VISIBLE);
			iv_switch_close_sound.setVisibility(View.INVISIBLE);
		} else {
			iv_switch_open_sound.setVisibility(View.INVISIBLE);
			iv_switch_close_sound.setVisibility(View.VISIBLE);
		}

		if (chatOptions.getNoticedByVibrate()) {
			iv_switch_open_vibrate.setVisibility(View.VISIBLE);
			iv_switch_close_vibrate.setVisibility(View.INVISIBLE);
		} else {
			iv_switch_open_vibrate.setVisibility(View.INVISIBLE);
			iv_switch_close_vibrate.setVisibility(View.VISIBLE);
		}

		if (chatOptions.getUseSpeaker()) {
			iv_switch_open_speaker.setVisibility(View.VISIBLE);
			iv_switch_close_speaker.setVisibility(View.INVISIBLE);
		} else {
			iv_switch_open_speaker.setVisibility(View.INVISIBLE);
			iv_switch_close_speaker.setVisibility(View.VISIBLE);
		}
		
		if (HXPreferenceUtils.getInstance().getSettingNotification()) {
			iv_switch_open_notices.setVisibility(View.VISIBLE);
			iv_switch_close_notices.setVisibility(View.INVISIBLE);
		} else {
			iv_switch_open_notices.setVisibility(View.INVISIBLE);
			iv_switch_close_notices.setVisibility(View.VISIBLE);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_switch_notification:
			if (iv_switch_open_notification.getVisibility() == View.VISIBLE) {
				iv_switch_open_notification.setVisibility(View.INVISIBLE);
				iv_switch_close_notification.setVisibility(View.VISIBLE);
				rl_switch_sound.setVisibility(View.GONE);
				rl_switch_vibrate.setVisibility(View.GONE);
				rl_switch_notices.setVisibility(View.GONE);
				textview1.setVisibility(View.GONE);
				textview2.setVisibility(View.GONE);
				chatOptions.setNotificationEnable(false);
				EMChatManager.getInstance().setChatOptions(chatOptions);

				HXSDKHelper.getInstance().getModel().setSettingMsgNotification(false);
				HXPreferenceUtils.getInstance().setSettingNotification(false);
			} else {
				iv_switch_open_notification.setVisibility(View.VISIBLE);
				iv_switch_close_notification.setVisibility(View.INVISIBLE);
				rl_switch_sound.setVisibility(View.VISIBLE);
				rl_switch_vibrate.setVisibility(View.VISIBLE);
				rl_switch_notices.setVisibility(View.VISIBLE);
				textview1.setVisibility(View.VISIBLE);
				textview2.setVisibility(View.VISIBLE);
				chatOptions.setNotificationEnable(true);
				EMChatManager.getInstance().setChatOptions(chatOptions);
				HXSDKHelper.getInstance().getModel().setSettingMsgNotification(true);
				HXPreferenceUtils.getInstance().setSettingNotification(true);
			}
			break;
		case R.id.rl_switch_sound:
			if (iv_switch_open_sound.getVisibility() == View.VISIBLE) {
				iv_switch_open_sound.setVisibility(View.INVISIBLE);
				iv_switch_close_sound.setVisibility(View.VISIBLE);
				chatOptions.setNoticeBySound(false);
				EMChatManager.getInstance().setChatOptions(chatOptions);
				HXSDKHelper.getInstance().getModel().setSettingMsgSound(false);
			} else {
				iv_switch_open_sound.setVisibility(View.VISIBLE);
				iv_switch_close_sound.setVisibility(View.INVISIBLE);
				chatOptions.setNoticeBySound(true);
				EMChatManager.getInstance().setChatOptions(chatOptions);
				HXSDKHelper.getInstance().getModel().setSettingMsgSound(true);
			}
			break;
		case R.id.rl_switch_vibrate:
			if (iv_switch_open_vibrate.getVisibility() == View.VISIBLE) {
				iv_switch_open_vibrate.setVisibility(View.INVISIBLE);
				iv_switch_close_vibrate.setVisibility(View.VISIBLE);
				chatOptions.setNoticedByVibrate(false);
				EMChatManager.getInstance().setChatOptions(chatOptions);
				HXSDKHelper.getInstance().getModel().setSettingMsgVibrate(false);
			} else {
				iv_switch_open_vibrate.setVisibility(View.VISIBLE);
				iv_switch_close_vibrate.setVisibility(View.INVISIBLE);
				chatOptions.setNoticedByVibrate(true);
				EMChatManager.getInstance().setChatOptions(chatOptions);
				HXSDKHelper.getInstance().getModel().setSettingMsgVibrate(true);
			}
			break;
		case R.id.rl_switch_speaker:
			if (iv_switch_open_speaker.getVisibility() == View.VISIBLE) {
				iv_switch_open_speaker.setVisibility(View.INVISIBLE);
				iv_switch_close_speaker.setVisibility(View.VISIBLE);
				chatOptions.setUseSpeaker(false);
				EMChatManager.getInstance().setChatOptions(chatOptions);
				HXSDKHelper.getInstance().getModel().setSettingMsgSpeaker(false);
			} else {
				iv_switch_open_speaker.setVisibility(View.VISIBLE);
				iv_switch_close_speaker.setVisibility(View.INVISIBLE);
				chatOptions.setUseSpeaker(true);
				EMChatManager.getInstance().setChatOptions(chatOptions);
				HXSDKHelper.getInstance().getModel().setSettingMsgVibrate(true);
			}
			break;
		case R.id.rl_switch_notices:
			// 设置通知栏消息是否提示
			if (iv_switch_open_notices.getVisibility() == View.VISIBLE) {
				iv_switch_open_notices.setVisibility(View.INVISIBLE);
				iv_switch_close_notices.setVisibility(View.VISIBLE);

				HXPreferenceUtils.getInstance().setSettingNotification(false);
			} else {
				iv_switch_open_notices.setVisibility(View.VISIBLE);
				iv_switch_close_notices.setVisibility(View.INVISIBLE);
				HXPreferenceUtils.getInstance().setSettingNotification(true);
			}

			break;
		case R.id.btn_logout: // 退出登陆
			// logout();
			break;
		case R.id.ll_black_list:
			startActivity(new Intent(this, BlacklistActivity.class));
			break;
		case R.id.ll_diagnose:
			startActivity(new Intent(this, DiagnoseActivity.class));
			break;
		default:
			break;
		}

	}

	/**
	 * 返回
	 * 
	 * @param view
	 */
	public void back(View view) {
		finish();
	}
	// void logout() {
	// final ProgressDialog pd = new ProgressDialog(this);
	// String st = getResources().getString(R.string.Are_logged_out);
	// pd.setMessage(st);
	// pd.setCanceledOnTouchOutside(false);
	// pd.show();
	// BaseApplication.getApplication().logout(new EMCallBack() {
	//
	// @Override
	// public void onSuccess() {
	// getActivity().runOnUiThread(new Runnable() {
	// public void run() {
	// pd.dismiss();
	// // 重新显示登陆页面
	// ((MessageActivity) getActivity()).finish();
	// startActivity(new Intent(getActivity(), LoginActivity.class));
	//
	// }
	// });
	// }
	//
	// @Override
	// public void onProgress(int progress, String status) {
	//
	// }
	//
	// @Override
	// public void onError(int code, String message) {
	//
	// }
	// });
	// }

}
