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
package com.hx.hxchat.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.fz.core.image.AsyncImageLoader;
import com.fz.core.net.RequestHelper;
import com.hx.hxchat.activity.NewFriendsMsgActivity;
import com.hx.hxchat.db.InviteMessgeDao;
import com.hx.hxchat.db.UserDao;
import com.hx.hxchat.domain.InviteMessage;
import com.hx.hxchat.domain.InviteMessage.InviteMesageStatus;
import com.hx.hxchat.domain.User;
import com.hx.hxchat.domain.UserFriendBean.Items;
import com.hx.hxchat.utils.CommonUtils;
import com.hx.hxchat.utils.UserUtils;
import com.mgw.member.R;
import com.mgw.member.ui.activity.MainActivity;

public class NewFriendsMsgAdapter extends ArrayAdapter<InviteMessage> {
	//
	// private Context context;
	// private InviteMessgeDao messgeDao;
	//
	// public NewFriendsMsgAdapter(Context context, int textViewResourceId,
	// List<InviteMessage> objects) {
	// super(context, textViewResourceId, objects);
	// this.context = context;
	// messgeDao = new InviteMessgeDao(context);
	// }
	//
	// @Override
	// public View getView(int position, View convertView, ViewGroup parent) {
	// final ViewHolder holder;
	// if (convertView == null) {
	// holder = new ViewHolder();
	// convertView = View.inflate(context, R.layout.row_invite_msg, null);
	// holder.avator = (ImageView) convertView.findViewById(R.id.avatar);
	// holder.reason = (TextView) convertView.findViewById(R.id.message);
	// holder.name = (TextView) convertView.findViewById(R.id.name);
	// holder.status = (Button) convertView.findViewById(R.id.user_state);
	// holder.groupContainer = (LinearLayout)
	// convertView.findViewById(R.id.ll_group);
	// holder.groupname = (TextView)
	// convertView.findViewById(R.id.tv_groupName);
	// // holder.time = (TextView) convertView.findViewById(R.id.time);
	// convertView.setTag(holder);
	// } else {
	// holder = (ViewHolder) convertView.getTag();
	// }
	//
	// String str1 =
	// context.getResources().getString(R.string.Has_agreed_to_your_friend_request);
	// String str2 = context.getResources().getString(R.string.agree);
	//
	// String str3 =
	// context.getResources().getString(R.string.Request_to_add_you_as_a_friend);
	// String str4 =
	// context.getResources().getString(R.string.Apply_to_the_group_of);
	// String str5 = context.getResources().getString(R.string.Has_agreed_to);
	// String str6 = context.getResources().getString(R.string.Has_refused_to);
	// final InviteMessage msg = getItem(position);
	// if (msg != null) {
	// if(msg.getGroupId() != null){ // 显示群聊提示
	// holder.groupContainer.setVisibility(View.VISIBLE);
	// holder.groupname.setText(msg.getGroupName());
	// } else{
	// holder.groupContainer.setVisibility(View.GONE);
	// }
	//
	// holder.reason.setText(msg.getReason());
	// holder.name.setText(msg.getFrom());
	// // holder.time.setText(DateUtils.getTimestampString(new
	// // Date(msg.getTime())));
	// if (msg.getStatus() == InviteMesageStatus.BEAGREED) {
	// holder.status.setVisibility(View.INVISIBLE);
	// holder.reason.setText(str1);
	// } else if (msg.getStatus() == InviteMesageStatus.BEINVITEED ||
	// msg.getStatus() == InviteMesageStatus.BEAPPLYED) {
	// holder.status.setVisibility(View.VISIBLE);
	// holder.status.setEnabled(true);
	// holder.status.setBackgroundResource(android.R.drawable.btn_default);
	// holder.status.setText(str2);
	// if(msg.getStatus() == InviteMesageStatus.BEINVITEED){
	// if (msg.getReason() == null) {
	// // 如果没写理由
	// holder.reason.setText(str3);
	// }
	// }else{ //入群申请
	// if (TextUtils.isEmpty(msg.getReason())) {
	// holder.reason.setText(str4 + msg.getGroupName());
	// }
	// }
	// // 设置点击事件
	// holder.status.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// // 同意别人发的好友请求
	// acceptInvitation(holder.status, msg);
	// }
	// });
	// } else if (msg.getStatus() == InviteMesageStatus.AGREED) {
	// holder.status.setText(str5);
	// holder.status.setBackgroundDrawable(null);
	// holder.status.setEnabled(false);
	// } else if(msg.getStatus() == InviteMesageStatus.REFUSED){
	// holder.status.setText(str6);
	// holder.status.setBackgroundDrawable(null);
	// holder.status.setEnabled(false);
	// }
	//
	// // 设置用户头像
	// }
	//
	// return convertView;
	// }
	//
	// /**
	// * 同意好友请求或者群申请
	// *
	// * @param button
	// * @param username
	// */
	// private void acceptInvitation(final Button button, final InviteMessage
	// msg) {
	// final ProgressDialog pd = new ProgressDialog(context);
	// String str1 = context.getResources().getString(R.string.Are_agree_with);
	// final String str2 =
	// context.getResources().getString(R.string.Has_agreed_to);
	// final String str3 =
	// context.getResources().getString(R.string.Agree_with_failure);
	// pd.setMessage(str1);
	// pd.setCanceledOnTouchOutside(false);
	// pd.show();
	//
	// new Thread(new Runnable() {
	// public void run() {
	// // 调用sdk的同意方法
	// try {
	// if(msg.getGroupId() == null) //同意好友请求
	// EMChatManager.getInstance().acceptInvitation(msg.getFrom());
	// else //同意加群申请
	// EMGroupManager.getInstance().acceptApplication(msg.getFrom(),
	// msg.getGroupId());
	// ((Activity) context).runOnUiThread(new Runnable() {
	//
	// @Override
	// public void run() {
	// pd.dismiss();
	// button.setText(str2);
	// msg.setStatus(InviteMesageStatus.AGREED);
	// // 更新db
	// ContentValues values = new ContentValues();
	// values.put(InviteMessgeDao.COLUMN_NAME_STATUS,
	// msg.getStatus().ordinal());
	// messgeDao.updateMessage(msg.getId(), values);
	// button.setBackgroundDrawable(null);
	// button.setEnabled(false);
	//
	// }
	// });
	// } catch (final Exception e) {
	// ((Activity) context).runOnUiThread(new Runnable() {
	//
	// @Override
	// public void run() {
	// pd.dismiss();
	// Toast.makeText(context, str3 + e.getMessage(), 1).show();
	// }
	// });
	//
	// }
	// }
	// }).start();
	// }
	//
	// private static class ViewHolder {
	// ImageView avator;
	// TextView name;
	// TextView reason;
	// Button status;
	// LinearLayout groupContainer;
	// TextView groupname;
	// // TextView time;
	// }

	private final Context context;
	private final InviteMessgeDao messgeDao;

	private final List<Items> m_User_s;
	private NewFriendsMsgActivity newFriendsMsgActivity;

	public NewFriendsMsgAdapter(Context context, int textViewResourceId, List<InviteMessage> objects, List<Items> user_s) {
		super(context, textViewResourceId, objects);
		this.context = context;
		messgeDao = new InviteMessgeDao(context);
		this.m_User_s = user_s;
		newFriendsMsgActivity = (NewFriendsMsgActivity) context;
	}

	private static class ViewHolder {
		ImageView avator;
		TextView name;
		TextView reason;
		Button status;
		LinearLayout groupContainer;
		TextView groupname;
		// TextView time;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.row_invite_msg, null);
			holder.avator = (ImageView) convertView.findViewById(R.id.avatar);
			holder.reason = (TextView) convertView.findViewById(R.id.message);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.status = (Button) convertView.findViewById(R.id.user_state);
			holder.groupContainer = (LinearLayout) convertView.findViewById(R.id.ll_group);
			holder.groupname = (TextView) convertView.findViewById(R.id.tv_groupName);
			// holder.time = (TextView) convertView.findViewById(R.id.time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final InviteMessage msg = getItem(position);
		if (msg != null) {
			if (msg.getGroupId() != null) { // 显示群聊提示
				holder.groupContainer.setVisibility(View.VISIBLE);
				holder.groupname.setText(msg.getGroupName());
			} else {
				holder.groupContainer.setVisibility(View.GONE);
			}

			holder.reason.setText(msg.getReason());

			boolean t_isHave = false;
			int t_num = 0;

			for (int t_i = 0; t_i < m_User_s.size(); t_i++) {

				final User oUser = new User();

				oUser.setUsername(m_User_s.get(t_i).getUserId());
				oUser.setreferee("1");

				if (msg.getFrom().equals(m_User_s.get(t_i).getUserId())) {

					oUser.setNick(m_User_s.get(t_i).getNickName());
					oUser.setAvatar(m_User_s.get(t_num).getMemberPic());
					oUser.netContainInvite = true;

				}

				if (oUser.netContainInvite) {

					holder.name.setText(oUser.getNick());
					UserUtils.setUserAvatar(context, Uri.parse(m_User_s.get(t_num).getMemberPic()), holder.avator);

				} else {
					holder.name.setText(oUser.getUsername());

				}

				// AsyncImageLoader t_AsyncImageLoader = new
				// AsyncImageLoader(null, 0, 0);
				// t_AsyncImageLoader.LoadDrawable(m_User_s.get(t_num).getM_imagePath(),
				// holder.avator);
				//
				if (msg.getStatus() == InviteMesageStatus.BEAGREED) {
					holder.status.setVisibility(View.INVISIBLE);
					holder.reason.setText("已同意你的好友请求");
				} else if (msg.getStatus() == InviteMesageStatus.BEINVITEED || msg.getStatus() == InviteMesageStatus.BEAPPLYED) {
					holder.status.setVisibility(View.VISIBLE);
					holder.status.setText("同意");
					if (msg.getStatus() == InviteMesageStatus.BEINVITEED) {
						if (msg.getReason() == null) {
							// 如果没写理由
							holder.reason.setText("请求加你为好友");
						}
					} else { // 入群申请
						if (TextUtils.isEmpty(msg.getReason())) {
							holder.reason.setText("申请加入群：" + msg.getGroupName());
						}
					}
					// 设置点击事件
					holder.status.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// 同意别人发的好友请求
							newFriendsMsgActivity.acceptInvitation(context, holder.status, oUser, msg);
						}
					});
				} else if (msg.getStatus() == InviteMesageStatus.AGREED) {
					holder.status.setText("已同意");
					holder.status.setBackgroundDrawable(null);
					holder.status.setEnabled(false);
				} else if (msg.getStatus() == InviteMesageStatus.REFUSED) {
					holder.status.setText("已拒绝");
					holder.status.setBackgroundDrawable(null);
					holder.status.setEnabled(false);
				}
			}

			// holder.time.setText(DateUtils.getTimestampString(new
			// Date(msg.getTime())));

		

			// 设置用户头像
		}

		return convertView;
	}

	// /**
	// * 同意好友请求或者群申请
	// *
	// * @param button
	// * @param username
	// */
	// private void acceptInvitation(final Button button, final User pUser,
	// final InviteMessage msg) {
	// final ProgressDialog pd = new ProgressDialog(context);
	// pd.setMessage("正在同意...");
	// pd.setCanceledOnTouchOutside(false);
	// pd.show();
	//
	// new Thread(new Runnable() {
	// @Override
	// public void run() {
	// // 调用sdk的同意方法
	// try {
	// if (msg.getGroupId() == null) // 同意好友请求
	// {
	// EMChatManager.getInstance().acceptInvitation(msg.getFrom());
	// addfriend(context, pUser.getUsername());
	//
	// // 存入db
	// UserDao dao = new UserDao(context);
	// pUser.setreferee("1");
	// dao.saveContact(pUser);
	// } else
	// // 同意加群申请
	// EMGroupManager.getInstance().acceptApplication(msg.getFrom(),
	// msg.getGroupId());
	//
	// ((Activity) context).runOnUiThread(new Runnable() {
	//
	// @Override
	// public void run() {
	// pd.dismiss();
	// button.setText("已同意");
	// UserDao oUserDao = new UserDao(context);
	// oUserDao.saveContact(pUser);
	// Define.s_HasChangeContent = true;
	//
	// msg.setStatus(InviteMesageStatus.AGREED);
	// // 更新db
	// ContentValues values = new ContentValues();
	// values.put(InviteMessgeDao.COLUMN_NAME_STATUS,
	// msg.getStatus().ordinal());
	// messgeDao.updateMessage(msg.getId(), values);
	// button.setBackgroundDrawable(null);
	// button.setEnabled(false);
	// }
	// });
	// } catch (final Exception e) {
	// ((Activity) context).runOnUiThread(new Runnable() {
	//
	// @Override
	// public void run() {
	// pd.dismiss();
	// Toast.makeText(context, "同意失败: " + e.getMessage(), 1).show();
	// }
	// });
	//
	// }
	// }
	// }).start();
	// }

	// /**
	// * 通知自己公司服务器同意添加某个好友
	// *
	// * @throws JSONException
	// */
	// public static JSONObject addfriend(Context context, String userid) throws
	// JSONException {
	//
	// SharedPreferences sp = context.getSharedPreferences("mgw_data", 0);
	// Map<String, String> params = new HashMap<String, String>();
	// params.put("type", "member.addfriend");
	// params.put("userid", sp.getString("mgw_userID", "0"));
	// params.put("serial", sp.getString("mgw_serial", "0"));
	// params.put("friend", userid);
	// String resultString = RequestHelper.PostBySingleBitmap(MainActivity.url,
	// params, null);
	//
	// if (resultString == null || resultString.equals("")) {
	// return null;
	// }
	// Log.i("postGetOrderState", resultString);
	//
	// JSONTokener jsonParser = new JSONTokener(resultString);
	// return (JSONObject) jsonParser.nextValue();
	// }
}
