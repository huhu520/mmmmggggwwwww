package com.mgw.member.ui.activity.login;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.util.HanziToPinyin;
import com.google.gson.Gson;
import com.hx.hxchat.MGWHXSDKHelper;
import com.hx.hxchat.db.UserDao;
import com.hx.hxchat.domain.User;
import com.hx.hxchat.otto.GetHxInfoNoticeEvent;
import com.hx.hxchat.otto.GroupListsRefeshEvent;
import com.hx.hxchat.utils.UserUtils;
import com.loopj.android.http.RequestParams;
import com.mgw.member.R;
import com.mgw.member.RememberLoginName;
import com.mgw.member.bean.UserInfoBean;
import com.mgw.member.constant.Define_C;
import com.mgw.member.constant.imp_Define;
import com.mgw.member.manager.BaseApplication;
import com.mgw.member.manager.GreenDaoDBHelper;
import com.mgw.member.ottoEvent.BusProvider;
import com.mgw.member.ui.activity.MGWBaseActivity;
import com.mgw.member.ui.activity.MainActivity;
import com.mgw.member.uitls.AppManager;
import com.mgw.member.uitls.LocalUserInfo;
import com.mgw.member.uitls.LogUtils;
import com.mgw.member.uitls.MgqDataHandler;
import com.mgw.member.uitls.MgqRestClient;
import com.mgw.member.uitls.PreferenceHelper;
import com.mgw.member.uitls.UIUtils;
import com.squareup.otto.Produce;

//import com.tencent.android.tpush.XGPushManager;

public class LoginActivity extends MGWBaseActivity implements OnClickListener, imp_Define {
	private final String TAG = LoginActivity.class.getSimpleName();
	/**
	 * 环信昵称
	 */
	private static final int REQUEST_CODE_SETNICK = 1;

	/** 登录计数 */
	private byte m_num;

	private ProgressDialog progressDialog;

	/**
	 * 登录号码
	 */
	public static String mLoginTel = null;

	private boolean progressShow;
	private boolean autoLogin = false;
	private Gson gson;

	@Override
	protected void onResume() {
		super.onResume();
		BusProvider.getInstance().register(this);
		if (mLoginTel != null && mLoginTel.length() > 0) {
			((AutoCompleteTextView) findViewById(R.id.tel)).setText(mLoginTel);
		}
		if (autoLogin) {
			return;
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		BusProvider.getInstance().unregister(this);
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:

				if (progressDialog != null && progressDialog.isShowing()) {
					progressDialog.dismiss();
				}

				break;

			case 003:
//				processContactsAndGroups((String) msg.obj);
				break;
			case 004:
				sendLoadHxInfoEvent((String) msg.obj);
				login();
				break;
			case 005:
				login();
				break;

			default:
				break;
			}

		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 如果环信已经登录了直接进入主页面
		LogUtils.i(TAG, "onCreate hx is logined? =" + MGWHXSDKHelper.getInstance().isLogined() + ", BaseApplication.getApplication().logined" + PreferenceHelper.getInstance(mContext).getAppLogined()
				+ ",EMChatManager.getInstance().isConnected()=" + EMChatManager.getInstance().isConnected());
		// if (PreferenceHelper.getInstance(mContext).getAppLogined()) {
		//
		// autoLogin = true;
		// startActivity(new Intent(LoginActivity.this, MainActivity.class));
		// this.finish();
		// return;
		// }
		// 如果用户名密码都有，直接进入主页面

		BaseApplication.getDaoSession(LoginActivity.this);

		if (EMChatManager.getInstance().isConnected() && MGWHXSDKHelper.getInstance().isLogined()) {
			autoLogin = true;
			startActivity(new Intent(LoginActivity.this, MainActivity.class));
			this.finish();
			return;
		}
		setContentView(R.layout.login);
		AppManager.getAppManager().addActivity(this);
		mLoginTel = null;
		progressDialog = new ProgressDialog(LoginActivity.this);
		findViewById(R.id.login).setOnClickListener(this);
		findViewById(R.id.register).setOnClickListener(this);

		m_num = 0;

		initTitle();

		findViewById(R.id.tv_login_problem).setOnClickListener(this);
		findViewById(R.id.bj).setOnClickListener(this);

		setinitName();

		// // 退出环信
		// if (MGWHXSDKHelper.getInstance().isLogined()) {
		// MGWHXSDKHelper.getInstance().logout(null);
		// LogUtils.i(TAG, "MGWHXSDKHelper.getInstance().isLogined()");
		// }

		gson = new Gson();

	}

	private void setinitName() {
		// ((EditText) findViewById(R.id.tel)).setText(strID);

		List<String> rememberLoginName = GreenDaoDBHelper.getInstance(mContext).getRememberLoginName();
		String[] array = new String[rememberLoginName.size()];

		for (int i = 0; i < array.length; i++) {
			array[i] = rememberLoginName.get(i);
		}
		AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.tel);// 定义AutoCompleteTextView控件
		ArrayAdapter adapter = new ArrayAdapter(this, // 定义匹配源的adapter
				R.layout.simple_list_item_autocomplete_1, array);
		textView.setAdapter(adapter); // 设置 匹配源的adapter 到 AutoCompleteTextView控件

		if (array.length > 0) {

			((AutoCompleteTextView) findViewById(R.id.tel)).setText(array[array.length - 1]);

		}

	}

	private void initTitle() {
		((Button) findViewById(R.id.bt_titlebar_left)).setVisibility(View.GONE);
		((TextView) findViewById(R.id.tv_title_cent)).setText("美顾问");
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);

		// 防止在设置中退出登录，直接焦点到用户名输入框，导致自动补全list弹出，影响美观
		// 补充，如果list为空焦点落在登录上
		List<String> rememberLoginName = GreenDaoDBHelper.getInstance(mContext).getRememberLoginName();
		if (hasFocus) {
			if (rememberLoginName.size() > 0) {
				((EditText) findViewById(R.id.password)).requestFocus();
				((EditText) findViewById(R.id.password)).requestFocusFromTouch();

			} else {

				((AutoCompleteTextView) findViewById(R.id.tel)).requestFocus();
				((AutoCompleteTextView) findViewById(R.id.tel)).requestFocusFromTouch();

			}

		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login:
			// 登录
			mLoginTel = null;
			getData();
			break;
		case R.id.register:
			// 注册会员
			mLoginTel = null;
			startActivity(new Intent(this, RegisterActivity.class));
			break;

		case R.id.tv_login_problem: {
			// 登录出现问题
			mLoginTel = null;
			Intent intent = new Intent(this, InputTelActivity.class);
			startActivity(intent);
		}
			break;

		case R.id.bj:
			// 商家卡注册会员
			mLoginTel = null;
			startActivity(new Intent(this, BJRegisterActivity.class));
			break;
		}
	}

	/**
	 * 第一步 点击登录mgw
	 */
	private void getData() {
		mgwusername = ((AutoCompleteTextView) findViewById(R.id.tel)).getText().toString();
		mgwpassword = ((EditText) findViewById(R.id.password)).getText().toString();

		m_num = 0;

		if (IS_BEBUG) {
			mgwusername = "10566334";
			mgwpassword = "123456";
		} else {
			if (!(mgwusername.length() != 0 && mgwusername.length() != 0)) {
				Toast.makeText(this, "所有信息不能为空", Toast.LENGTH_LONG).show();
				return;
			}
		}

		MgqDataHandler loginHandler = new MgqDataHandler(this, true, false) {
			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				UIUtils.runInMainThread(new Runnable() {
					@Override
					public void run() {
						((Button) findViewById(R.id.login)).setSelected(true);
						// findViewById(R.id.login).setOnClickListener(null);
						((Button) findViewById(R.id.login)).setText("登录中 ...");
					}
				});

				loginHxAndInitData(response);

			}

			// meibangkeji b866b866
			@Override
			public void onFailure(Throwable ble) {
				Toast.makeText(LoginActivity.this, "网络连接失败", Toast.LENGTH_LONG).show();

			}
		};

		RequestParams params = new RequestParams();
		params.put("type", "user.apploading");
		params.put("telephone", "13888888888");
		params.put("pmID", mgwusername);
		params.put("format", "json");
		params.put("pKey", mgwpassword);
		params.put("app", "beauty");
		params.put("token", "");
		MgqRestClient.get(Define_C.mgw_url, params, loginHandler);
	}

	/**
	 * 
	 * 第二步保存登录数据，并登录环信 登录环信并初始化数据（保存到数据库）
	 * 
	 * @param response
	 */
	private void loginHxAndInitData(String response) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				progressDialog.setMessage("正在登录...");
				progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progressDialog.show();

			}
		});

		try {
			// {
			// "item": {
			// "MemberID": "3517MD-1408141813320000000711",
			// "UserName": "王二波",
			// "CardCode": "973410388",
			// "GroupID": 3279,
			// "Telephone": "13711111112",
			// "Gender": "女",
			// "Email": "",
			// "UID": 3517,
			// "UserID": "10565692",
			// "RelationID": "",
			// "Balance": "0",
			// "Score": 0,
			// "Type": 0,
			// "MemberPic":
			// "http://app.mgw.cc/Member/Images/avatar-100.png",
			// "GradeName": "消费卡"
			// },
			// "flag": 0,
			// "msg": "获取成功",
			// "type": "user.apploading",
			// "telephone": "13888888888"
			// }
			JSONObject obj = new JSONObject(response);
			UserInfoBean fromJson = gson.fromJson(response, UserInfoBean.class);
			BaseApplication.getApplication().setBean(fromJson);

			if (fromJson == null || fromJson.item == null) {
				UIUtils.showToastSafe("用户名或密码错误，请重新登录");
				handler.sendEmptyMessage(1);

				((Button) findViewById(R.id.login)).setSelected(false);
				((Button) findViewById(R.id.login)).setText("登录");
				return;
			}
			String nick = fromJson.item.MemberName;
			String hxid = fromJson.item.UserID;
			String password = hxid;
			if (!saveMyInfo(obj)) {
				handler.sendEmptyMessage(1);
				return;
			}

			// 记录登录号码到公用数据库
			GreenDaoDBHelper.getInstance(mContext).addRememberLoginNameDao(
					new RememberLoginName(((AutoCompleteTextView) findViewById(R.id.tel)).getText().toString(), ((EditText) findViewById(R.id.password)).getText().toString(), new Date(), false));

			Huanxinglogin(hxid, password, nick);

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	// m_num = 2;

	/**
	 * 保存自身信息
	 * 
	 * @param obj
	 *            登录返回的信息
	 */
	private boolean saveMyInfo(JSONObject obj) {
		try {
			if (obj.getInt("flag") != 0) {
				Toast.makeText(LoginActivity.this, obj.getString("msg"), Toast.LENGTH_LONG).show();
				handler.sendEmptyMessage(1);
				LogUtils.i(TAG, "保存自身信息異常" + obj.toString());
				return false;
			}

			if (obj.getInt("flag") == 0) {
				LogUtils.i(TAG, "保存自身信息" + obj.toString());
				LocalUserInfo.getInstance(LoginActivity.this).setUserInfo("mgw_account", mgwusername);
				LocalUserInfo.getInstance(LoginActivity.this).setUserInfo("mgw_pwd", mgwpassword);

				JSONObject obj1 = obj.getJSONObject("item");
				LocalUserInfo.getInstance(LoginActivity.this).setUserInfo("mgw_data", obj1.toString());
				LocalUserInfo.getInstance(LoginActivity.this).setUserInfo("mgw_userID", obj1.getString("UserID"));
				LocalUserInfo.getInstance(LoginActivity.this).setUserInfo("mgw_serial", obj1.getString("serial"));

				LocalUserInfo.getInstance(LoginActivity.this).setUserInfo("mgw_owner", obj1.getString("owner"));
				LocalUserInfo.getInstance(LoginActivity.this).setUserInfo("mgw_mall", obj1.getString("mall"));
				LocalUserInfo.getInstance(LoginActivity.this).setUserInfo("mgw_name", obj1.getString("MemberName"));

				LocalUserInfo.getInstance(LoginActivity.this).setUserInfo("mgw_sid", !obj1.toString().contains("sid") ? "" : obj1.getString("sid"));
				LocalUserInfo.getInstance(LoginActivity.this).setUserInfo("mgw_uid", !obj1.toString().contains("uid") ? "" : obj1.getString("uid"));
				LocalUserInfo.getInstance(LoginActivity.this).setUserInfo("mgw_groupid", !obj1.toString().contains("groupid") ? "" : obj1.getString("groupid"));
				LocalUserInfo.getInstance(LoginActivity.this).setUserInfo("mgw_memberid", !obj1.toString().contains("memberid") ? "" : obj1.getString("memberid"));

				LocalUserInfo.getInstance(LoginActivity.this).setUserInfo("s_RelationID", obj1.getString("RelationID"));

				LocalUserInfo.getInstance(LoginActivity.this).setUserInfo("wz_alipay", obj1.getString("wz_alipay"));
				LocalUserInfo.getInstance(LoginActivity.this).setUserInfo("wz_alipay_return_url", obj1.getString("wz_alipay_return_url"));
				LocalUserInfo.getInstance(LoginActivity.this).setUserInfo("wz_tenpay", obj1.getString("wz_tenpay"));

				LocalUserInfo.getInstance(LoginActivity.this).setUserInfo("mall_alipay", obj1.getString("mall_alipay"));
				LocalUserInfo.getInstance(LoginActivity.this).setUserInfo("mall_alipay_return_url", obj1.getString("mall_alipay_return_url"));
				LocalUserInfo.getInstance(LoginActivity.this).setUserInfo("mall_tenpay", obj1.getString("mall_tenpay"));

				LocalUserInfo.getInstance(LoginActivity.this).setUserInfo("xx_alipay", obj1.getString("xx_alipay"));
				LocalUserInfo.getInstance(LoginActivity.this).setUserInfo("xx_alipay_return_url", obj1.getString("xx_alipay_return_url"));
				LocalUserInfo.getInstance(LoginActivity.this).setUserInfo("xx_tenpay", obj1.getString("xx_tenpay"));

				LocalUserInfo.getInstance(LoginActivity.this).setUserInfo("NewVision", obj1.getString("serial"));
				LocalUserInfo.getInstance(LoginActivity.this).setUserInfo("register_page", obj1.getString("register_page"));
				LocalUserInfo.getInstance(LoginActivity.this).setUserInfo("s_RelationID", obj1.getString("RelationID"));

				LocalUserInfo.getInstance(LoginActivity.this).setUserInfo("logined", true);

				Define_C.s_RelationID = obj1.getString("RelationID");
				return true;
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUtils.e("保存自身信息異常" + e.toString());
			handler.sendEmptyMessage(1);

			return false;
		}
		return false;

	}

	/**
	 * 
	 * 
	 * 第三步 登录环信 环信登录
	 * 
	 * @param hxid
	 *            环信登录id
	 * @param password
	 *            环信登录密码
	 * @param nick
	 *            环信nick
	 */
	private void Huanxinglogin(final String hxid, String hxpassword, final String nick) {

		// * 如果该用户没有环信账号 要先注册 再登录

		String s_RelationID = LocalUserInfo.getInstance(LoginActivity.this).getUserInfo("s_RelationID");

		// 注册环信账号在 注册页面进行 不再这里进行
		if (s_RelationID == null || s_RelationID.equals("")) {
			// registerHaunXin(m_user_id, m_user_id);
			UIUtils.showToastSafe("没有注册环信id，请注册");
			LogUtils.e("没有注册环信id，请注册");
		} else {

			LogUtils.i(TAG + "loginHuanXin_username=" + hxid + ",pwd=" + hxpassword);
			runOnUiThread(new Runnable() {
				public void run() {
					progressDialog.setMessage(getString(R.string.list_is_for));
				}
			});
			// 调用sdk登录方法登录聊天服务器
			EMChatManager.getInstance().login(hxid, hxpassword, new EMCallBack() {
				@Override
				public void onSuccess() {

					BaseApplication.getApplication().m_user_id = hxid;
					BaseApplication.getApplication().setUserName(hxid);
					BaseApplication.getApplication().m_playerName = nick;
					PreferenceHelper.getInstance(mContext).setAppLogined(true);

				

					// ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
					// ** manually load all local groups and conversations in
					// case we are auto login
					// 更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
					boolean updatenick = EMChatManager.getInstance().updateCurrentUserNick(nick);
					if (!updatenick) {
						LogUtils.e("LoginActivity", "update current user nick fail");
					}
					LogUtils.e("LoginActivity", "登录成功");
					// EMGroupManager.getInstance().loadAllGroups();
					// EMChatManager.getInstance().loadAllConversations();
					// // 处理好友和群组

					Message message = Message.obtain();
					message.what = 004;
					message.obj = hxid;
					handler.sendMessage(message);
				}

				@Override
				public void onProgress(int progress, String status) {

					// LogUtils.i(TAG+"onProgress_="+progress+",status="+status);
				}

				@Override
				public void onError(int code, final String message) {

					handler.sendEmptyMessage(1);
					UIUtils.showToastSafe("hx login failed!");
					handler.sendEmptyMessage(005);
				}

			});

		}

	}

	/**
	 * 第四步加载好友信息和群组信息 load friend and group blacklist after hx login successed
	 * 
	 * @param hxid
	 */
	private void processContactsAndGroups(String hxid) {

		AsyncTask<Void, Void, Boolean> asyncTask = new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				boolean login = false;
				Map<String, User> userlist = new HashMap<String, User>();
				try {
					JSONObject jo = UserUtils.getfriendinfo();

					int flag = jo.getInt("flag");

					if (flag == 0) {
						if (!jo.isNull("items")) {
							JSONArray array = jo.getJSONArray("items");
							userlist.clear();
							for (int i = 0; i < array.length(); i++) {
								jo = array.getJSONObject(i);
								User user = new User();
								user.setNick(jo.getString("NickName"));
								user.setAvatar(jo.getString("MemberPic"));
								user.setUsername(jo.getString("UserId"));
								user.setreferee(jo.getString("referee"));
								userlist.put(user.getUsername(), user);
							}
						}
					}

					// }
					// 添加user"申请与通知"
					User newFriends = new User();
					newFriends.setUsername(com.hx.hxchat.Constant.NEW_FRIENDS_USERNAME);
					newFriends.setNick("申请与通知");
					newFriends.setHeader("");
					userlist.put(com.hx.hxchat.Constant.NEW_FRIENDS_USERNAME, newFriends);

					// 添加"群聊"
					User groupUser = new User();
					groupUser.setUsername(com.hx.hxchat.Constant.GROUP_USERNAME);
					groupUser.setNick("群聊");
					groupUser.setHeader("");
					userlist.put(com.hx.hxchat.Constant.GROUP_USERNAME, groupUser);

					// TODO 存入内存
					BaseApplication.getApplication().setContactList(userlist);

					// TODO 存入db
					UserDao dao = new UserDao(mContext);
					List<User> users = new ArrayList<User>(userlist.values());
					dao.saveContactList(users);

					// TODO 获取黑名单列表
					List<String> blackList = EMContactManager.getInstance().getBlackListUsernamesFromServer();
					EMContactManager.getInstance().saveBlackList(blackList);
					// TODO
					// 获取群聊列表,sdk会把群组存入到EMGroupManager和db中(),群聊里只有groupid和groupname等简单信息，不包含members
					List<EMGroup> groupsFromServer = EMGroupManager.getInstance().getGroupsFromServer();

					// for (EMGroup dd : groupsFromServer) {
					// EMGroup groupFromServer =
					// EMGroupManager.getInstance().getGroupFromServer(dd.getGroupId());
					// List<String> members = groupFromServer.getMembers();
					// EMGroupManager.getInstance().createOrUpdateLocalGroup(groupFromServer);
					// // EMGroup group =
					// //
					// EMGroupManager.getInstance().getGroup(dd.getGroupId());
					// LogUtils.i(TAG, "members:" + members.toString() +
					// "local members:");
					// }

					login = true;
					return login;
				} catch (Exception ex) {
					ex.printStackTrace();
					login = false;
					UIUtils.showToastSafe("get list failed");
					LogUtils.e(TAG, ex.toString());
				}
				return login;
			}
		};

		try {
			Boolean login = asyncTask.execute().get();
			LogUtils.i(TAG, "asyncTask login=" + login);
			if (login) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						login();

					}
				});

			} else {
				UIUtils.showToastSafe("login fail!");
			}
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			UIUtils.showToastSafe("login fail!");
			LogUtils.e(TAG, e.toString());
		}

	}

	/**
	 * 设置hearder属性，方便通讯中对联系人按header分类显示，以及通过右侧ABCD...字母栏快速定位联系人
	 * 
	 * @param username
	 * @param user
	 */
	@SuppressLint("DefaultLocale")
	protected void setUserHearder(String username, User user) {
		String headerName = null;
		if (!TextUtils.isEmpty(user.getNick())) {
			headerName = user.getNick();
		} else {
			headerName = user.getUsername();
		}
		headerName = headerName.trim();
		if (username.equals(com.hx.hxchat.Constant.NEW_FRIENDS_USERNAME)) {
			user.setHeader("");
		} else if (Character.isDigit(headerName.charAt(0))) {
			user.setHeader("#");
		} else {
			user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target.substring(0, 1).toUpperCase());
			char header = user.getHeader().toLowerCase().charAt(0);
			if (header < 'a' || header > 'z') {
				user.setHeader("#");
			}
		}
	}

	private String m_name;

	private void login() {
		m_num++;
		if (m_num >= 1) {
			handler.sendEmptyMessage(1);
			startActivity(new Intent(LoginActivity.this, MainActivity.class));
			this.finish();

		}
	}

	private long mExitTime;
	private String mgwusername;
	private String mgwpassword;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();
			} else {
				// 防止退出后 再次点击 直接进入主页
				PreferenceHelper.getInstance(mContext).setAppLogined(false);
				AppManager.getAppManager().AppExit(mContext);

			}

			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 密码错误 打电话
	 */
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

	/**
	 * 个手机号码只能拥有一个账号
	 */
	protected void registerDialog() {
		Builder builder = new Builder(this);
		builder.setMessage("请注意：一个手机号码只能拥有一个账号，如您已是财富卡会员，请直接获取帐号，无需注册！");
		builder.setTitle("提示");
		builder.setPositiveButton("返回", null);

		builder.setNegativeButton("继续注册", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		// 结束Activity&从堆栈中移除
		AppManager.getAppManager().finishActivity(this);
	}

	/**
	 * 第一次登录环信 初始化 数据
	 * 
	 * @param username
	 * @param password2
	 *            Created by Administrator
	 */
	private void initData(final String username, final String password2) {
		// flag = true;
		// 登录成功，保存用户名密码
		try {
			// 登录成功，保存用户名密码
			BaseApplication.getApplication().setUserName(username);
			BaseApplication.getApplication().setPassword(password2);

		} catch (Exception e) {
			e.printStackTrace();
		}
		// 保存用户名

		SharedPreferences.Editor sharedata = getSharedPreferences("mgw_data", 0).edit();
		sharedata.putString("hxLogOut", "1");
		sharedata.commit();
	}

	@Produce
	public GetHxInfoNoticeEvent getGetHxInfoNoticeEvent() {
		// LogUtils.i("otto" + "getGroupListsRefeshEvent");
		GetHxInfoNoticeEvent GetHxInfoNoticeEvent = new GetHxInfoNoticeEvent();
		return GetHxInfoNoticeEvent;
	}

	private void sendLoadHxInfoEvent(String id) {
		GetHxInfoNoticeEvent GetHxInfoNoticeEvent = getGetHxInfoNoticeEvent();
		GetHxInfoNoticeEvent.setNeedRefresh(true);
		GetHxInfoNoticeEvent.setGroupId(id);
		BusProvider.getInstance().post(GetHxInfoNoticeEvent);

	}

}
