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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.fz.core.net.RequestHelper;
import com.google.gson.Gson;
import com.hx.hxchat.Constant;
import com.hx.hxchat.adapter.NewFriendsMsgAdapter;
import com.hx.hxchat.db.InviteMessgeDao;
import com.hx.hxchat.db.UserDao;
import com.hx.hxchat.domain.InviteMessage;
import com.hx.hxchat.domain.InviteMessage.InviteMesageStatus;
import com.hx.hxchat.domain.User;
import com.hx.hxchat.domain.UserFriendBean;
import com.hx.hxchat.domain.UserFriendBean.Items;
import com.hx.hxchat.utils.CommonUtils;
import com.hx.hxchat.utils.UserUtils;
import com.mgw.member.R;
import com.mgw.member.constant.Define_C;
import com.mgw.member.http.Http;
import com.mgw.member.manager.BaseApplication;
import com.mgw.member.ui.activity.MainActivity;

/**
 * 申请与通知
 * 
 */
public class NewFriendsMsgActivity extends HXBaseActivity implements com.mgw.member.constant.imp_Define {
	private ListView listView;
	private Gson gson;

	public void back(View view) {
		finish();
	}

	public void iv_new_contact(View view) {
		startActivity(new Intent(this, AddContactActivity.class));
	}

	// 弹框
	private Dialog progressDialog;
	NewFriendsMsgAdapter adapter;
	List<Items> t_User_s;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (progressDialog != null) {
				progressDialog.dismiss();
			} else if (msg.obj == null) {
				progressDialog.dismiss();
				return;
			}

			JSONObject t_JsonObject;
			JSONArray t_JsonArray;
			switch (msg.what) {
			case MESSAGE_TYPE_USERS:
				try {
					// {
					// "items": [
					// {
					// "UserId": "10566334",
					// "MemberPic":
					// "http://app.mgw.cc/Member/Images/avatar-100.png",
					// "NickName": "21000007"
					// }
					// ],
					// "flag": 0,
					// "msg": "获取成功",
					// "type": "user.getuserinfo",
					// "telephone": null
					// }

					t_JsonObject = (JSONObject) msg.obj;

					UserFriendBean fromJson = gson.fromJson(t_JsonObject.toString(), UserFriendBean.class);
					// t_User_s = new ArrayList<User_>();
					//
					// if (!t_JsonObject.isNull("items")) {
					// t_JsonArray = t_JsonObject.getJSONArray("items");
					// for (int t_i = 0; t_i < t_JsonArray.length(); t_i++) {
					// t_JsonObject = t_JsonArray.getJSONObject(t_i);
					// t_User_s.add(new User_(t_JsonObject
					// .getString("UserId"), t_JsonObject
					// .getString("NickName"), t_JsonObject
					// .getString("MemberPic")));
					// }
					// }

					if (fromJson != null && fromJson.getItems() != null) {
						t_User_s = fromJson.getItems();

						// 设置adapter
						adapter = new NewFriendsMsgAdapter(NewFriendsMsgActivity.this, 1, msgs, t_User_s);
						listView.setAdapter(adapter);
						adapter.notifyDataSetChanged();
					}

					listView.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
							InviteMessage emContact = adapter.getItem(position);
							if (adapter.getItem(position).getFrom().equals(BaseApplication.getApplication().getUserName()))
								Toast.makeText(NewFriendsMsgActivity.this, "不能和自己聊天", 0).show();
							else {
								String userName = "";
								for (int i = 0; i < t_User_s.size(); i++) {
									if (emContact.getFrom().equals(t_User_s.get(i).getFUser_ID())) {
										userName = t_User_s.get(position).getNickName();
										break;
									}
								}
								// 进入聊天页面
								Intent intent = new Intent(NewFriendsMsgActivity.this, ChatActivity.class);
								intent.putExtra("userId", emContact.getFrom());
								intent.putExtra("Name", userName);
								startActivity(intent);
							}
						}
					});

					BaseApplication.getApplication().getContactList().get(Constant.NEW_FRIENDS_USERNAME).setUnreadMsgCount(0);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
		}
	};

	private List<InviteMessage> msgs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_friends_msg);

		listView = (ListView) findViewById(R.id.list);
		InviteMessgeDao dao = new InviteMessgeDao(this);
		msgs = dao.getMessagesList();
		gson = new Gson();

		// listView = (ListView) findViewById(R.id.list);
		// InviteMessgeDao dao = new InviteMessgeDao(this);
		// List<InviteMessage> msgs = dao.getMessagesList();
		// // 设置adapter
		// NewFriendsMsgAdapter adapter = new NewFriendsMsgAdapter(this, 1,
		// msgs);
		// listView.setAdapter(adapter);
		// BaseApplication.getApplication().getContactList().get(Constant.NEW_FRIENDS_USERNAME).setUnreadMsgCount(0);
		//
		//

		progressDialog = CommonUtils.getUserDefinedDialog(NewFriendsMsgActivity.this, "", false, false);
		progressDialog.show();
		new Thread() {
			@Override
			public void run() {
				try {
					// 获得头像和用户名
					Message message = Message.obtain();
					message.what = MESSAGE_TYPE_USERS;
					String t_String = "";
					for (InviteMessage temp : msgs) {
						t_String += temp.getFrom() + ",";
					}
					message.obj = Http.postUesrs(t_String);
					mHandler.sendMessage(message);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

//		if (Define_C.s_HasChangeContent) {
//
//			adapter.notifyDataSetChanged();
//
//		}

	}

	/**
	 * 同意好友请求或者群申请
	 * 
	 * @param button
	 * @param username
	 */
	public void acceptInvitation(final Context context, final Button button, final User pUser, final InviteMessage msg) {
		final ProgressDialog pd = new ProgressDialog(context);
		pd.setMessage("正在同意...");
		pd.setCanceledOnTouchOutside(false);
		pd.show();

		new Thread(new Runnable() {
			@Override
			public void run() {
				// 调用sdk的同意方法
				try {
					if (msg.getGroupId() == null) // 同意好友请求
					{
						EMChatManager.getInstance().acceptInvitation(msg.getFrom());
						UserUtils.addfriend(context, pUser.getUsername());

						// // 存入db
						// UserDao dao = new UserDao(context);
						// pUser.setreferee("1");
						// dao.saveContact(pUser);
					} else {

						// 同意加群申请
						EMGroupManager.getInstance().acceptApplication(msg.getFrom(), msg.getGroupId());
					}

					((Activity) context).runOnUiThread(new Runnable() {

						@Override
						public void run() {
							pd.dismiss();
							button.setText("已同意");
							UserDao oUserDao = new UserDao(context);
							pUser.setreferee("1");
							oUserDao.saveContact(pUser);
							Define_C.s_HasChangeContent = true;

							msg.setStatus(InviteMesageStatus.AGREED);
							// 更新db
							ContentValues values = new ContentValues();
							values.put(InviteMessgeDao.COLUMN_NAME_STATUS, msg.getStatus().ordinal());
							InviteMessgeDao messgeDao = new InviteMessgeDao(context);
							messgeDao.updateMessage(msg.getId(), values);
							button.setBackgroundDrawable(null);
							button.setEnabled(false);
						}
					});
				} catch (final Exception e) {
					((Activity) context).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							pd.dismiss();
							Toast.makeText(context, "同意失败: " + e.getMessage(), 1).show();
						}
					});

				}
			}
		}).start();
	}

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
