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

import java.util.List;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMContactManager;
import com.google.gson.Gson;
import com.hx.hxchat.adapter.AddcontactAdapter;
import com.hx.hxchat.domain.UserFriendBean;
import com.hx.hxchat.domain.UserFriendBean.Items;
import com.hx.hxchat.interfaces.Control;
import com.hx.hxchat.utils.CommonUtils;
import com.mgw.member.R;
import com.mgw.member.constant.imp_Define;
import com.mgw.member.http.Http;

/**
 * 添加好友
 * 
 * @author Administrator
 * 
 */
public class AddContactActivity extends HXBaseActivity implements imp_Define {
	private EditText editText;
	private LinearLayout searchedUserLayout;
	private TextView nameText;
	private Button searchBtn;
	private Context context;
	private ImageView avatar;
	private InputMethodManager inputMethodManager;

	private String toAddUsername;
	private String toAddUserID;
	private Dialog progressDialog;
	private ListView lv;
	private AddcontactAdapter adapter;
	private JSONObject t_JsonObject;
	private List<Items> items;
	private Gson gson;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (progressDialog != null) {
				progressDialog.dismiss();
			} else if (msg.obj == null) {
				progressDialog.dismiss();
				return;
			}

			switch (msg.what) {
			case MESSAGE_TYPE_FIND_FRIEND:
				/*
				 * { "flag": 0, "type": "user.queryuser", "telephone": null,
				 * "msg": "检索成功", "items": [ { "FUser_ID": "10647241",
				 * "NickName": "为爱", "MemberPic":
				 * "http://file.mgw.cc/avatar-100.png" }, { "FUser_ID":
				 * "10644549", "NickName": "感觉", "MemberPic":
				 * "http://file.mgw.cc/MemberPhoto/2015-02-08/a204a4f1-fe12-4562-a6f3-48ca2f46eab5.jpeg"
				 * }, { "FUser_ID": "10644547", "NickName": "炫迈", "MemberPic":
				 * "http://file.mgw.cc/MemberPhoto/2015-03-27/2c2aeb53-3267-4e7e-9fa3-2002f403ce9b.jpg"
				 * }, { "FUser_ID": "10643369", "NickName": "胡勇彪", "MemberPic":
				 * "http://file.mgw.cc/MemberPhoto/2015-03-29/b2c7ac4d-5674-41bc-abd8-f869b9a7c45a.jpg"
				 * } ] }
				 */
				try {
					t_JsonObject = (JSONObject) msg.obj;

					UserFriendBean fromJson = gson.fromJson(t_JsonObject.toString(), UserFriendBean.class);

					if (fromJson != null) {
						if (fromJson.flag.equals("0")) {
							items = fromJson.getItems();
							searchedUserLayout.setVisibility(View.VISIBLE);
							//
							adapter = new AddcontactAdapter(AddContactActivity.this, items);
							lv.setAdapter(adapter);
							adapter.notifyDataSetChanged();

			

						} else {
							Toast.makeText(AddContactActivity.this, t_JsonObject.getString("msg"), Toast.LENGTH_LONG).show();
						}
						Toast.makeText(AddContactActivity.this, t_JsonObject.getString("msg"), Toast.LENGTH_LONG).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 001:
				Toast.makeText(context, "发送请求成功,等待对方验证", 1).show();
				break;
			case 002:
				Toast.makeText(context, "请求添加好友失败:" + msg.obj, 1).show();
				break;

			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_contact);
		context = this;
		editText = (EditText) findViewById(R.id.edit_note);
		searchedUserLayout = (LinearLayout) findViewById(R.id.ll_user);
		searchBtn = (Button) findViewById(R.id.search);
		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		gson = new Gson();

		lv = (ListView) findViewById(R.id.listview);
		// user_s = new ArrayList<UserFriend>();
		lv.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub

				CommonUtils.hideSoftKeyboard(AddContactActivity.this);
				return false;
			}
		});
		progressDialog = CommonUtils.getUserDefinedDialog(context, "正在发生请求", false,false);
	}

	/**
	 * 查找contact
	 * 
	 * @param v
	 */
	public void searchContact(View v) {
		final String name = editText.getText().toString();
		String saveText = "查找";

		if (getString(R.string.button_search).equals(saveText)) {
			toAddUsername = name;
			if (TextUtils.isEmpty(name)) {
				startActivity(new Intent(this, AlertDialog.class).putExtra("msg", "请输入用户名"));
				return;
			}

			editText.setText("");
			if (items != null) {
				items.clear();
				adapter.notifyDataSetChanged();
			}
			// FIXME ??????添加好友需要接口

			progressDialog.show();

			new Thread() {
				@Override
				public void run() {
					try {
						Message message = Message.obtain();
						message.what = MESSAGE_TYPE_FIND_FRIEND;
						message.obj = Http.postGetUesrsForName(toAddUsername);

						mHandler.sendMessage(message);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}.start();

		}
	}

	private Control control;

	public Control getControl() {
		return control;
	}

	public void setControl(Control control) {
		this.control = control;
	}

	public void back(View v) {
		finish();
	}

	public void addFriend(final Items user) {

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// demo写死了个reason，实际应该让用户手动填入
					EMContactManager.getInstance().addContact(user.getFUser_ID(), "加个好友吧");
					adapter.progressDialog.dismiss();
					Message msg = new Message();
					msg.what = 001;
					mHandler.sendMessage(msg);

				} catch (final Exception e) {
					adapter.progressDialog.dismiss();
					Message msg = new Message();
					msg.what = 002;
					msg.obj = e.getMessage();
					mHandler.sendMessage(msg);

				}
			}
		}).start();
	}
}
