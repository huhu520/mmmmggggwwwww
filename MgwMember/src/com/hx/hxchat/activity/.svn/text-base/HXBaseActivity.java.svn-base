/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hx.hxchat.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.Type;
import com.easemob.util.EasyUtils;
import com.hx.hxchat.domain.User;
import com.hx.hxchat.utils.CommonUtils;
import com.hx.hxchat.utils.UserUtils;
import com.mgw.member.manager.BaseApplication;
import com.mgw.member.ui.activity.BaseActivity;
import com.mgw.member.ui.activity.MainActivity;
import com.mgw.member.R;
import com.umeng.analytics.MobclickAgent;

public class HXBaseActivity extends BaseActivity {
	private static final int notifiId = 11;
	protected NotificationManager notificationManager;
	private String name;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		initBase();
		initViewBase();

	
	
	}

	protected void initBase() {
		// TODO Auto-generated method stub
		
	}

	protected void initViewBase() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		// onresume时，取消notification显示
		EMChatManager.getInstance().activityResumed();
		// umeng
		MobclickAgent.onResume(this);
	
	
	
	}

	@Override
	protected void onStart() {
		super.onStart();
		// umeng
		MobclickAgent.onPause(this);
	}

	/**
	 * 当应用在前台时，如果当前消息不是属于当前会话，在状态栏提示一下 如果不需要，注释掉即可
	 * 
	 * @param message
	 */
	protected void notifyNewMessage(EMMessage message) {
		// 如果是设置了不提醒只显示数目的群组(这个是app里保存这个数据的，demo里不做判断)
		// 以及设置了setShowNotificationInbackgroup:false(设为false后，后台时sdk也发送广播)
		if (!EasyUtils.isAppRunningForeground(this)) {
			return;
		}
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(getApplicationInfo().icon).setWhen(System.currentTimeMillis()).setAutoCancel(true);
		String ticker = CommonUtils.getMessageDigest(message, this);
		String st = getResources().getString(R.string.expression);
		if (message.getType() == Type.TXT)
			ticker = ticker.replaceAll("\\[.{2,3}\\]", st);
		User user = BaseApplication.getApplication().getContactList().get(message.getFrom());
		if (user == null) {
			name = message.getFrom();
		} else {
			name = user.getAvatar();
		}


		// 设置状态栏提示
		mBuilder.setTicker(name + ": " + ticker);

		// 必须设置pendingintent，否则在2.3的机器上会有bug
		Intent intent = new Intent(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, notifiId, intent, PendingIntent.FLAG_ONE_SHOT);
		mBuilder.setContentIntent(pendingIntent);

		Notification notification = mBuilder.build();
		notificationManager.notify(notifiId, notification);
		notificationManager.cancel(notifiId);
	}

	/**
	 * 返回
	 * 
	 * @param view
	 */
	public void back(View view) {
		finish();
	}

}
