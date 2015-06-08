package com.mgw.member.ui.fragment;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.chat.CmdMessageBody;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactListener;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.EMMessage.Type;
import com.easemob.chat.EMNotifier;
import com.easemob.chat.GroupChangeListener;
import com.easemob.chat.TextMessageBody;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.EMLog;
import com.easemob.util.HanziToPinyin;
import com.easemob.util.NetUtils;
import com.hx.hxchat.Constant;
import com.hx.hxchat.activity.ChatActivity;
import com.hx.hxchat.activity.ChatHistoryFragment;
import com.hx.hxchat.activity.ChatHistoryFragmentCopy;
import com.hx.hxchat.activity.ContactlistFragment;
import com.hx.hxchat.activity.GroupsActivity;
import com.hx.hxchat.activity.SettingActivity;
import com.hx.hxchat.db.InviteMessgeDao;
import com.hx.hxchat.db.UserDao;
import com.hx.hxchat.domain.InviteMessage;
import com.hx.hxchat.domain.InviteMessage.InviteMesageStatus;
import com.hx.hxchat.domain.User;
import com.hx.hxchat.fragment.HXFragmentFactory;
import com.hx.hxchat.utils.CommonUtils;
import com.hx.hxchat.utils.UserUtils;
import com.mgw.member.R;
import com.mgw.member.constant.Define_C;
import com.mgw.member.factory.FragmentFactory;
import com.mgw.member.factory.MgwWebViewFactory;
import com.mgw.member.http.Http;
import com.mgw.member.manager.BaseApplication;
import com.mgw.member.ottoEvent.BusProvider;
import com.mgw.member.ottoEvent.UnreadInviteRefeshEvent;
import com.mgw.member.ui.activity.MainActivity;
import com.mgw.member.ui.activity.login.LoginActivity;
import com.mgw.member.uitls.AppManager;
import com.mgw.member.uitls.LogUtils;
import com.mgw.member.uitls.PreferenceHelper;
import com.mgw.member.uitls.UIUtils;
import com.squareup.otto.Produce;

/**
 * 
 * @author huyan
 */
public class NewsFragment extends BaseFragment implements OnClickListener {
	private String TAG = NewsFragment.class.getSimpleName().toString();
	private MainActivity ac;
	// ===
	private View m_View_2;
	private View m_View_3;

	private FrameLayout click_2;
	private FrameLayout click_3;
	// 未读消息textview
	private TextView unreadLabel;
	// 未读通讯录textview
	private TextView unreadAddressLable;
	// 当前fragment的title
	public TextView m_TextView_title;
	/** 会话 */
	public ChatHistoryFragmentCopy m_ChatHistoryFragment;
	/** 通讯录 */
	public ContactlistFragment m_ContactlistFragment;

	private Fragment[] fragments;
	private byte m_index;
	public byte m_currentTabIndex;

	private android.app.AlertDialog.Builder accountRemovedBuilder;

	private NewMessageBroadcastReceiver msgReceiver;
	// 账号在别处登录
	public boolean isConflict = false;
	// 账号被移除
	private boolean isCurrentAccountRemoved = false;
	private InviteMessgeDao inviteMessgeDao;
	private UserDao userDao;
	// ===

	private Button btn_setting;

	private RelativeLayout rl_dialog;

	@Override
	public View initView(LayoutInflater inflater) {
		View view = UIUtils.inflate(R.layout.activity_message);
		m_TextView_title = (TextView) view.findViewById(R.id.TextView_title);
		/** 未读的消息条数 */
		unreadLabel = (TextView) view.findViewById(R.id.unread_msg_number);
		/** 未读的好友添加请求条数 */
		unreadAddressLable = (TextView) view.findViewById(R.id.unread_address_number);
		btn_setting = (Button) view.findViewById(R.id.btn_setting);

		rl_dialog = (RelativeLayout) view.findViewById(R.id.rl_dialog);

		// m_View_1 = view.findViewById(R.id.View_1);
		m_View_2 = view.findViewById(R.id.View_2);
		m_View_3 = view.findViewById(R.id.View_3);

		// click_1 = (FrameLayout) view.findViewById(R.id.click_1);
		click_2 = (FrameLayout) view.findViewById(R.id.click_2);
		click_3 = (FrameLayout) view.findViewById(R.id.click_3);

		// click_1.setOnClickListener(this);
		click_2.setOnClickListener(this);
		click_3.setOnClickListener(this);
		btn_setting.setOnClickListener(this);

		initHX();
		return view;
	}

	private void initHX() {
		// if(savedInstanceState != null &&
		// savedInstanceState.getBoolean(Constant.ACCOUNT_REMOVED, false)){
		// // 防止被移除后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
		// // 三个fragment里加的判断同理
		// BaseApplication.getApplication().logout(null);
		// finish();
		// startActivity(new Intent(this, LoginActivity.class));
		// return;
		// }else if (savedInstanceState != null &&
		// savedInstanceState.getBoolean("isConflict", false)) {
		// // 防止被T后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
		// // 三个fragment里加的判断同理
		// finish();
		// startActivity(new Intent(this, LoginActivity.class));
		// return;
		// }

		// MobclickAgent.setDebugMode( true );
		// --?--
		// MobclickAgent.updateOnlineConfig(context);

		// if (getIntent().getBooleanExtra("conflict", false) &&
		// !isConflictDialogShow){
		// showConflictDialog();
		// }else if(getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false)
		// && !isAccountRemovedDialogShow){
		// showAccountRemovedDialog();
		// }

		m_index = 0;
		m_currentTabIndex = 0;
		inviteMessgeDao = new InviteMessgeDao(context);
		userDao = new UserDao(context);
		// 这个fragment只显示好友和群组的聊天记录
		// chatHistoryFragment = new ChatHistoryFragment();
		// 显示所有人消息记录的fragment
		
		// m_RecommendedFragment = new RecommendedFragm();
//			m_ContactlistFragment = new ContactlistFragment();
		m_ContactlistFragment = (ContactlistFragment) HXFragmentFactory.createFragment(HXFragmentFactory.TAB_NEWS_FRIENDS);
			m_ChatHistoryFragment =  (ChatHistoryFragmentCopy) HXFragmentFactory.createFragment(HXFragmentFactory.TAB_NEWS_CHATHISTORY);

		// fragments = new Fragment[] { m_RecommendedFragment,
		// m_ChatHistoryFragment, m_ContactlistFragment };
		fragments = new Fragment[] { m_ChatHistoryFragment, m_ContactlistFragment };
		// inviteMessgeDao = new InviteMessgeDao(this);
		// userDao = new UserDao(this);

		
		getChildFragmentManager().beginTransaction().add(R.id.fragment_container2, m_ChatHistoryFragment).add(R.id.fragment_container2, m_ContactlistFragment).hide(m_ContactlistFragment);
		
		m_ChatHistoryFragment.show();
		registerReceiver();
		initHXListener();

	}

	/**
	 * 初始化环信的 状态 监听
	 */
	private void initHXListener() {
		// setContactListener监听联系人的变化等
		EMContactManager.getInstance().setContactListener(new MyContactListener());
		// 注册一个监听连接状态的listener
		EMChatManager.getInstance().addConnectionListener(new MyConnectionListener());
		// 注册群聊相关的listener
		EMGroupManager.getInstance().addGroupChangeListener(new MyGroupChangeListener());
		// 通知sdk，UI 已经初始化完毕，注册了相应的receiver和listener, 可以接受broadcast了
		EMChat.getInstance().setAppInited();

		// 导入会话
		if (EMChatManager.getInstance().isConnected()) {
			EMChatManager.getInstance().loadAllConversations();
		}
	}

	/**
	 * 动态注册广播接受者
	 */
	private void registerReceiver() {
		// 注册一个接收消息的BroadcastReceiver
		msgReceiver = new NewMessageBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(EMChatManager.getInstance().getNewMessageBroadcastAction());
		intentFilter.setPriority(10);
		context.registerReceiver(msgReceiver, intentFilter);

		// 注册一个ack回执消息的BroadcastReceiver
		IntentFilter ackMessageIntentFilter = new IntentFilter(EMChatManager.getInstance().getAckMessageBroadcastAction());
		ackMessageIntentFilter.setPriority(3);
		context.registerReceiver(ackMessageReceiver, ackMessageIntentFilter);

		// 注册一个透传消息的BroadcastReceiver
		IntentFilter cmdMessageIntentFilter = new IntentFilter(EMChatManager.getInstance().getCmdMessageBroadcastAction());
		cmdMessageIntentFilter.setPriority(3);
		context.registerReceiver(cmdMessageReceiver, cmdMessageIntentFilter);
	}

	@Override
	public void initData() {

	}

	@Override
	public boolean onBackPressed() {
		return false;
	}

	@Override
	public void showTwoPage(WebView view, String url) {

	}

	@Override
	public void hideErrorPage() {

	}

	@Override
	public void open2OhterActivitye(Intent intent) {

	}

	@Override
	protected void openWebview(WebView view, String url) {

	}

	// 方案一
	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.btn_setting:
			// 跳转到设置页面
			getActivity().startActivity(new Intent(getActivity(), SettingActivity.class));

			break;

		case R.id.click_2:
			m_index = 0;
			// m_View_1.setBackgroundColor(0xffD4D4D4);
			m_View_2.setBackgroundColor(0xffF5A443);
			m_View_3.setBackgroundColor(0xffD4D4D4);

			m_TextView_title.setText("消息");

			if (m_currentTabIndex != m_index) {
				FragmentTransaction trx = getChildFragmentManager().beginTransaction();
				trx.hide(fragments[m_currentTabIndex]);
				if (!fragments[m_index].isAdded()) {
					trx.add(R.id.fragment_container2, fragments[m_index]);
				}
				trx.show(fragments[m_index]).commit();
				m_ChatHistoryFragment.reset();
				m_ChatHistoryFragment.show();
			}
			m_currentTabIndex = m_index;
			break;
		case R.id.click_3:
			m_index = 1;
			// m_View_1.setBackgroundColor(0xffD4D4D4);
			m_View_2.setBackgroundColor(0xffD4D4D4);
			m_View_3.setBackgroundColor(0xffF5A443);

			m_TextView_title.setText("通讯录");

			if (m_currentTabIndex != m_index) {
				FragmentTransaction trx = getChildFragmentManager().beginTransaction();
				trx.hide(fragments[m_currentTabIndex]);
				if (!fragments[m_index].isAdded()) {
					trx.add(R.id.fragment_container2, fragments[m_index]);
				}
				trx.show(fragments[m_index]).commit();
			}
			m_currentTabIndex = m_index;
			break;
		}

	}

	public Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 001:
				updateUnreadLabel();
				if (m_currentTabIndex == 0 || m_currentTabIndex == 1) {
					// 当前页面如果为聊天历史页面，刷新此页面
					if (m_ChatHistoryFragment != null) {
						m_ChatHistoryFragment.refresh();
					}
				}
				break;
			case 002:
				// Toast.makeText(context, "请求添加好友失败:" + msg.obj, 1).show();
				break;
			case 003:
				m_ContactlistFragment.refresh();
				m_ChatHistoryFragment.refresh();
				break;
			case 004:
				m_ContactlistFragment.refresh();
				break;
			default:
				break;
			}
		};
	};
	private boolean isAccountRemovedDialogShow;

	/**
	 * 刷新申请与通知消息数
	 */
	// TODO
	public void updateUnreadAddressLable() {

		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				int count = 0;
				if (EMChatManager.getInstance().isConnected()) {
					count = BaseApplication.getApplication().getUnreadAddressCountTotal();
				}
				if (count > 0) {
					unreadAddressLable.setText(String.valueOf(count));
					unreadAddressLable.setVisibility(View.VISIBLE);
				} else {
					unreadAddressLable.setVisibility(View.INVISIBLE);
				}

				// otto 发布邀请消息s
				UnreadInviteRefeshEvent unreadInviteRefeshEvent = getUnreadInviteRefeshEvent();
				unreadInviteRefeshEvent.setCount(count);
				BusProvider.getInstance().post(unreadInviteRefeshEvent);

			}

		});
	}

	// /**
	// * 获取未读申请与通知消息
	// *
	// * @return
	// */
	// public int getUnreadAddressCountTotal() {
	// int unreadAddressCountTotal = 0;
	// if
	// (BaseApplication.getApplication().getContactList().get(Constant.NEW_FRIENDS_USERNAME)
	// != null) {
	// unreadAddressCountTotal =
	// BaseApplication.getApplication().getContactList().get(Constant.NEW_FRIENDS_USERNAME).getUnreadMsgCount();
	// }
	//
	// return unreadAddressCountTotal;
	// }

	/**
	 * 获取未读消息数
	 * 
	 * @return
	 */
	public int getUnreadMsgCountTotal() {
		int unreadMsgCountTotal = 0;
		unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
		return unreadMsgCountTotal;
	}

	/**
	 * 刷新未读消息数
	 */
	public void updateUnreadLabel() {
		int count = 0;
		if (EMChatManager.getInstance().isConnected()) {
			count = getUnreadMsgCountTotal();
		}
		
		if (count > 0) {

			unreadLabel.setText(String.valueOf(count));
			unreadLabel.setVisibility(View.VISIBLE);

		} else {

			unreadLabel.setVisibility(View.INVISIBLE);

		}
		// m_ChatHistoryFragment.refresh();

		updateMainActButtonLabel(count);
	}

	private void updateMainActButtonLabel(final int count) {
		// if (((MainActivity) getActivity()).click_m_index != 2) {
		// 当前页面如果为聊天历史页面，刷新此页面
		UIUtils.runInMainThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				((MainActivity) getActivity()).updateUnreadLabel(count);
			}
		});

	}

	/**
	 * 刷新未读消息数
	 */
	public void updateUnreadLabel(TextView unreadLabel) {
		int count = 0;
		if (EMChatManager.getInstance().isConnected()) {
			count = getUnreadMsgCountTotal();
		}

		if (count > 0) {
			unreadLabel.setText(String.valueOf(count));
			unreadLabel.setVisibility(View.INVISIBLE);
		} else {
			unreadLabel.setVisibility(View.INVISIBLE);
		}

	}

	// ======================xiaoxi count refash end

	public void Change(final String pUSerID) {
		new Thread() {
			@Override
			public void run() {
				
				
				String msgFrom = pUSerID;
				
				
				
				if (pUSerID == null || pUSerID.equals("")) {
					return;
				}
				
				User user = new User();
				
				
				user.setAvatar("");
				
				
				user.setNick(pUSerID);
				
				
				user.setUsername(pUSerID);
				
				
				user.setreferee("1");
				
				
				if (!BaseApplication.getApplication().GetLinkerDao().containsKey(user.getUsername())) {
					
					JSONObject oJSONObject;
					
					try {
						oJSONObject = Http.postUesrs(msgFrom);

						if (!oJSONObject.isNull("items")) {
							JSONArray t_JsonArray = oJSONObject.getJSONArray("items");

							for (int t_i = 0; t_i < t_JsonArray.length(); t_i++) {
								oJSONObject = t_JsonArray.getJSONObject(t_i);
								user.setUsername(oJSONObject.getString("UserId"));
								user.setNick(oJSONObject.getString("NickName"));
								user.setAvatar(oJSONObject.getString("MemberPic"));
								// user.setreferee("1");
							}

							String headerName = null;
							if (!TextUtils.isEmpty(user.getNick())) {
								headerName = user.getNick();
							} else {
								headerName = user.getUsername();
							}
							if (user.getUsername().equals(Constant.NEW_FRIENDS_USERNAME)) {
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
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// 暂时有个bug，添加好友时可能会回调added方法两次

					BaseApplication.getApplication().GetLinkerDao().saveContact(user);
				}
				// SystemApplication.getInstance().GetLinkerDao().saveContact(oUser);

				// Log.d("main", "new message id:" + msgId + " from:" + msgFrom
				// + " type:" + msgType + " body:" + msgBody);
				// 收到这个广播的时候，message已经在db和内存里了，可以通过id获取mesage对象
				// 刷新bottom bar消息未读数
				handler.sendEmptyMessage(001);
			}
		}.start();
	}

	// TODO HUANXING
	/**
	 * 
	 * 新消息广播接收者
	 * 
	 */
	private class NewMessageBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 主页面收到消息后，主要为了提示未读，实际消息内容需要到chat页面查看

			// 消息id
			String msgId = intent.getStringExtra("msgid");
			String from = intent.getStringExtra("from");
			// String msgId = intent.getStringExtra("msgid");
			EMMessage message = EMChatManager.getInstance().getMessage(msgId);
			// // 2014-10-22 修复在某些机器上，在聊天页面对方发消息过来时不立即显示内容的bug
			if (ChatActivity.activityInstance != null) {
				if (message.getChatType() == ChatType.GroupChat) {
					if (message.getTo().equals(ChatActivity.activityInstance.getToChatUsername()))
						return;
				} else {
					if (from.equals(ChatActivity.activityInstance.getToChatUsername()))
						return;
				}
			}
			// Change(from);
			handler.sendEmptyMessage(001);
			// 注销广播接收者，否则在ChatActivity中会收到这个广播
			abortBroadcast();

			// notifyNewMessage(message);

			// 刷新bottom bar消息未读数
			updateUnreadLabel();

			LogUtils.i(TAG, "NewMessageBroadcastReceiver");
		}
	}

	/**
	 * 帐号被移除的dialog
	 */
	private void showAccountRemovedDialog() {
		isAccountRemovedDialogShow = true;
		BaseApplication.getApplication().logout(null);
		String st5 = getResources().getString(R.string.Remove_the_notification);
		// if (!MessageActivity.this.isFinishing()) {
		if (true) {
			// clear up global variables
			try {
				if (accountRemovedBuilder == null)
					accountRemovedBuilder = new android.app.AlertDialog.Builder(context);
				accountRemovedBuilder.setTitle(st5);
				accountRemovedBuilder.setMessage(R.string.em_user_remove);
				accountRemovedBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						accountRemovedBuilder = null;
						getActivity().finish();
						startActivity(new Intent(context, LoginActivity.class));
					}
				});
				accountRemovedBuilder.setCancelable(false);
				accountRemovedBuilder.create().show();
				isCurrentAccountRemoved = true;
			} catch (Exception e) {
				EMLog.e(TAG, "---------color userRemovedBuilder error" + e.getMessage());
			}

		}

	}

	// ============== 接受者 start

	/**
	 * 消息回执BroadcastReceiver
	 */
	private BroadcastReceiver ackMessageReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// abortBroadcast();
			//
			// String msgid = intent.getStringExtra("msgid");
			// String from = intent.getStringExtra("from");
			//
			// EMConversation conversation =
			// EMChatManager.getInstance().getConversation(from);
			// if (conversation != null) {
			// // 把message设为已读
			// EMMessage msg = conversation.getMessage(msgid);
			//
			// if (msg != null) {
			//
			// // 2014-11-5 修复在某些机器上，在聊天页面对方发送已读回执时不立即显示已读的bug
			// if (ChatActivity.activityInstance != null) {
			// if (msg.getChatType() == ChatType.Chat) {
			// if
			// (from.equals(ChatActivity.activityInstance.getToChatUsername()))
			// return;
			// }
			// }
			//
			// msg.isAcked = true;
			// }
			// }
			String msgid = intent.getStringExtra("msgid");
			String from = intent.getStringExtra("from");
			EMConversation conversation = EMChatManager.getInstance().getConversation(from);
			if (conversation != null) {
				// 把message设为已读
				EMMessage msg = conversation.getMessage(msgid);
				if (msg != null) {
					msg.isAcked = true;
				}
			}
			abortBroadcast();
		}
	};

	/**
	 * 透传消息BroadcastReceiver
	 */
	private BroadcastReceiver cmdMessageReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			abortBroadcast();
			EMLog.d(TAG, "收到透传消息");
			// 获取cmd message对象
			String msgId = intent.getStringExtra("msgid");
			EMMessage message = intent.getParcelableExtra("message");
			// 获取消息body
			CmdMessageBody cmdMsgBody = (CmdMessageBody) message.getBody();
			String action = cmdMsgBody.action;// 获取自定义action

			// 获取扩展属性 此处省略
			// message.getStringAttribute("");
			EMLog.d(TAG, String.format("透传消息：action:%s,message:%s", action, message.toString()));

			switch (action) {
			case "touchuan:updateAvc":

				break;
			case "touchuan:updateNick":

				break;
			case "touchuan:updateAll":

				break;

			default:
				break;
			}
			String st9 = getResources().getString(R.string.receive_the_passthrough);
			Toast.makeText(context, st9 + action, Toast.LENGTH_SHORT).show();
			LogUtils.i("touchuan", "rec ok,content=" + action);

		}
	};

	/***
	 * 好友变化listener
	 * 
	 */
	private class MyContactListener implements EMContactListener {

		@Override
		public void onContactAdded(List<String> usernameList) {

			// 保存增加的联系人
			Map<String, User> localUsers = BaseApplication.getApplication().getContactList();
			Map<String, User> toAddUsers = new HashMap<String, User>();
			for (String username : usernameList) {
				User user = new User();
				user.setUsername(username);

				String headerName = null;
				if (!TextUtils.isEmpty(user.getNick())) {
					headerName = user.getNick();
				} else {
					headerName = user.getUsername();
				}
				if (username.equals(Constant.NEW_FRIENDS_USERNAME)) {
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
				// 暂时有个bug，添加好友时可能会回调added方法两次
				if (!localUsers.containsKey(username)) {
					String userLiString = username;
					try {
						JSONObject jo = Http.postUesrs(userLiString);
						int flag = jo.getInt("flag");
						if (flag == 0) {
							if (!jo.isNull("items")) {
								JSONArray array = jo.getJSONArray("items");
								for (int i = 0; i < array.length(); i++) {
									jo = array.getJSONObject(i);
									user.setNick(jo.getString("NickName"));
									user.setAvatar(jo.getString("MemberPic"));
									user.setreferee("1");
									user.setUsername(jo.getString("UserId"));

								}
							}
						}
					} catch (JSONException e) {

						e.printStackTrace();
					}
					userDao.saveContact(user);
					toAddUsers.put(username, user);
				}

			}
			localUsers.putAll(toAddUsers);
			// 刷新ui
			// m_ContactlistFragment.refresh();
			handler.sendEmptyMessage(004);
		}

		@Override
		public void onContactDeleted(final List<String> usernameList) {
			LogUtils.i(TAG, "onContactDeleted");
			// // 被删除
			Map<String, User> localUsers = BaseApplication.getApplication().getContactList();
			for (String username : usernameList) {
				localUsers.remove(username);
				userDao.deleteContact(username);
				inviteMessgeDao.deleteMessage(username);
				BaseApplication.getApplication().getContactList().remove(username);
				BaseApplication.getApplication().GetLinkerDao().updateContact(username, "1");
				LogUtils.i(TAG, "onContactDeleted:" + username);
			}
			getActivity().runOnUiThread(new Runnable() {
				public void run() {
					// 如果正在与此用户的聊天页面
					String st10 = getResources().getString(R.string.have_you_removed);
					if (ChatActivity.activityInstance != null && usernameList.contains(ChatActivity.activityInstance.getToChatUsername())) {
						Toast.makeText(context, ChatActivity.activityInstance.getToChatUsername() + st10, 1).show();
						ChatActivity.activityInstance.finish();
					}
					updateUnreadLabel();
					// 刷新ui

					// m_ContactlistFragment.refresh();
					// m_ChatHistoryFragment.refresh();
					handler.sendEmptyMessage(003);
				}
			});

			// BusProvider.getInstance().post(arg0);
		}

		@Override
		public void onContactInvited(String username, String reason) {
			// 接到邀请的消息，如果不处理(同意或拒绝)，掉线后，服务器会自动再发过来，所以客户端不要重复提醒
			List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
			for (InviteMessage inviteMessage : msgs) {
				if (inviteMessage.getGroupId() == null && inviteMessage.getFrom().equals(username)) {
					return;
				}
			}
			// 自己封装的javabean
			InviteMessage msg = new InviteMessage();
			msg.setFrom(username);
			msg.setTime(System.currentTimeMillis());
			msg.setReason(reason);
			Log.d("", username + "请求加你为好友,reason: " + reason);
			// 设置相应status
			msg.setStatus(InviteMesageStatus.BEINVITEED);

			notifyNewIviteMessage(msg);
			if (m_currentTabIndex == 1) {
				// m_ContactlistFragment.refresh();
				handler.sendEmptyMessage(004);
			}
			updateUnreadLabel();
		}

		@Override
		public void onContactAgreed(String username) {
			List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
			for (InviteMessage inviteMessage : msgs) {
				if (inviteMessage.getFrom().equals(username)) {
					return;
				}
			}
			// 自己封装的javabean
			InviteMessage msg = new InviteMessage();
			msg.setFrom(username);
			User userInfo = UserUtils.getUserInfo(username);
			msg.setTime(System.currentTimeMillis());
			Log.d("", userInfo == null ? username : userInfo.getNick() + "同意了你的好友请求");
			msg.setStatus(InviteMesageStatus.BEAGREED);
			notifyNewIviteMessage(msg);

		}

		@Override
		public void onContactRefused(String username) {
			// 参考同意，被邀请实现此功能,demo未实现
			Log.d(username, username + "拒绝了你的好友请求");

		}

	}

	private int loginCount = 0;

	/**
	 * 连接监听listener
	 * 
	 */
	private class MyConnectionListener implements EMConnectionListener {

		@Override
		public void onConnected() {
			((Activity) context).runOnUiThread(new Runnable() {

				@Override
				public void run() {
					loginCount = 0;
//					m_ChatHistoryFragment.errorItem.setVisibility(View.GONE);
				}

			});
		}

		@Override
		public void onDisconnected(final int error) {
			final String st1 = getResources().getString(R.string.Less_than_chat_server_connection);
			final String st2 = getResources().getString(R.string.the_current_network);
			((Activity) context).runOnUiThread(new Runnable() {

				@Override
				public void run() {

					if (error == EMError.USER_REMOVED) {
						// 显示帐号已经被移除
						showAccountRemovedDialog();
					} else if (error == EMError.CONNECTION_CONFLICT) {
						// 显示帐号在其他设备登陆dialog
						showConflictDialog(ac);
					} else {
//						m_ChatHistoryFragment.errorItem.setVisibility(View.VISIBLE);

						if (NetUtils.hasNetwork(getActivity()))
							m_ChatHistoryFragment.errorText.setText(st1);
						else
							m_ChatHistoryFragment.errorText.setText(st2);

						// if (loginCount < 1) {
						// LogUtils.i(TAG,
						// "onDisconnected 2 Hxlogin  loginCount=" +
						// loginCount);
						// BaseApplication.getApplication().isHxLogined = false;
						// loginCount++;
						//
						// }

					}
				}

			});
		}
	}

	public boolean isConflictDialogShow;
	public Builder conflictBuilder;

	/**
	 * 显示帐号在别处登录dialog
	 */
	public void showConflictDialog(final Activity context) {

		UIUtils.runInMainThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (context != null && !((MainActivity) context).isFinishing() && !isConflictDialogShow) {
					// clear up global variables
					try {

						if (conflictBuilder == null) {
							conflictBuilder = new android.app.AlertDialog.Builder(context);
						}
						conflictBuilder.setTitle("下线通知");
						conflictBuilder.setMessage(R.string.connect_conflict);
						conflictBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								SharedPreferences.Editor sharedata = getActivity().getSharedPreferences("mgw_data", 0).edit();
								sharedata.putString("mgw_data", "");
								sharedata.putString("mgw_pwd", "");
								sharedata.putString("mgw_account", "");
								sharedata.commit();

								// BaseApplication.getApplication().logout(mContext);
								dialog.dismiss();
								conflictBuilder = null;
								isConflictDialogShow = false;
								LogUtils.i(TAG, "退出成功10");
								PreferenceHelper.getInstance(getActivity()).setAppLogined(false);
								LogUtils.i(TAG, "退出成功11");
								MgwWebViewFactory.getInstance(context).clearWebVector();
								LogUtils.i(TAG, "退出成功12");
								FragmentFactory.clearFragments();
								LogUtils.i(TAG, "退出成功13");
								AppManager.getAppManager().finishAllActivity(LoginActivity.class);
								LogUtils.i(TAG, "退出成功14");
								context.startActivity(new Intent(context, LoginActivity.class));

							}
						});
						conflictBuilder.setCancelable(false);
						conflictBuilder.create().show();
						isConflictDialogShow = true;
						((NewsFragment) FragmentFactory.createFragment(FragmentFactory.TAB_NEWS)).isConflict = true;
					} catch (Exception e) {
						Log.e("###", "---------showConflictDialog conflictBuilder error" + e.getMessage());
					}
				}
			}
		});

	}

	/**
	 * MyGroupChangeListener
	 */
	private class MyGroupChangeListener implements GroupChangeListener {

		@Override
		public void onInvitationReceived(final String groupId, String groupName, String inviter, String reason) {
			// 被邀请
			User user = UserUtils.getUserInfo(inviter);

			if (user != null) {

				EMMessage msg = EMMessage.createReceiveMessage(Type.TXT);
				msg.setChatType(ChatType.GroupChat);
				msg.setFrom(inviter);
				msg.setTo(groupId);
				msg.setMsgId(UUID.randomUUID().toString());
				msg.addBody(new TextMessageBody(user.getNick() == null ? inviter : user.getNick() + "邀请你加入了兴趣圈"));

				// 保存邀请消息
				EMChatManager.getInstance().saveMessage(msg);
				// 提醒新消息
				EMNotifier.getInstance(getActivity().getApplicationContext()).notifyOnNewMsg();

			}

			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					updateUnreadLabel();
					// 刷新ui
					handler.sendEmptyMessage(001);
					updateGroupDb(groupId);
					;
					if (CommonUtils.getTopActivity(context).equals(GroupsActivity.class.getName())) {
						GroupsActivity.instance.onResume();
					}
				}
			});

		}

		@Override
		public void onInvitationAccpted(String groupId, String inviter, String reason) {
			// 邀请同意
			/**
			 * 1.刷新两个listview
			 */
			if (CommonUtils.getTopActivity(context).equals(MainActivity.class.getName())) {
				handler.sendEmptyMessage(003);
				LogUtils.i(TAG, "onInvitationAccpted");
			}

		}

		@Override
		public void onInvitationDeclined(String groupId, String invitee, String reason) {
			// 邀请拒绝
			if (CommonUtils.getTopActivity(context).equals(MainActivity.class.getName())) {
				handler.sendEmptyMessage(003);
				LogUtils.i(TAG, "onInvitationAccpted");
			}
		}

		@Override
		public void onUserRemoved(final String groupId, final String groupName) {
			// 提示用户被T了，demo省略此步骤
			// 刷新ui
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					try {
						Toast.makeText(ac, groupName + "群组已将你移除！", 0).show();
						updateUnreadLabel();
						handler.sendEmptyMessage(001);
						updateGroupDb(groupId);

						if (CommonUtils.getTopActivity(context).equals(GroupsActivity.class.getName())) {
							GroupsActivity.instance.onResume();
						} else if (CommonUtils.getTopActivity(context).equals(MainActivity.class.getName())) {

							handler.sendEmptyMessage(003);

							LogUtils.i(TAG, "onUserRemoved-groupName:" + groupName);
						}

					} catch (Exception e) {
						Log.e("###", "refresh exception " + e.getMessage());
					}
				}
			});
		}

		@Override
		public void onGroupDestroy(final String groupId, final String groupName) {
			// 群被解散
			// 提示用户群被解散,demo省略
			// 刷新ui
			((Activity) context).runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(ac, groupName + "群组已被群主解散！", 0).show();
					updateUnreadLabel();
					handler.sendEmptyMessage(001);
					updateGroupDb(groupId);
					if (CommonUtils.getTopActivity(getActivity()).equals(GroupsActivity.class.getName())) {
						GroupsActivity.instance.onResume();
					}
				}
			});

		}

		@Override
		public void onApplicationReceived(String groupId, String groupName, String applyer, String reason) {
			// 用户申请加入群聊
			InviteMessage msg = new InviteMessage();
			msg.setFrom(applyer);
			msg.setTime(System.currentTimeMillis());
			msg.setGroupId(groupId);
			msg.setGroupName(groupName);
			msg.setReason(reason);
			User userInfo = UserUtils.getUserInfo(applyer);
			Log.d("", userInfo == null ? applyer : userInfo.getNick() + " 申请加入兴趣圈：" + groupName);
			msg.setStatus(InviteMesageStatus.BEAPPLYED);
			notifyNewIviteMessage(msg);

		}

		@Override
		public void onApplicationAccept(final String groupId, String groupName, String accepter) {
			String st4 = getResources().getString(R.string.Agreed_to_your_group_chat_application);
			// 加群申请被同意
			EMMessage msg = EMMessage.createReceiveMessage(Type.TXT);
			msg.setChatType(ChatType.GroupChat);
			msg.setFrom(accepter);
			msg.setTo(groupId);
			msg.setMsgId(UUID.randomUUID().toString());
			User userInfo = UserUtils.getUserInfo(accepter);
			msg.addBody(new TextMessageBody(userInfo == null ? accepter : userInfo.getNick() + st4));
			// 保存同意消息
			EMChatManager.getInstance().saveMessage(msg);
			// 提醒新消息
			EMNotifier.getInstance(getActivity().getApplicationContext()).notifyOnNewMsg();

			((Activity) context).runOnUiThread(new Runnable() {
				public void run() {
					updateUnreadLabel();
					// 刷新ui
					// if (m_currentTabIndex == 0)
					handler.sendEmptyMessage(001);
					updateGroupDb(groupId);

					if (CommonUtils.getTopActivity(getActivity()).equals(GroupsActivity.class.getName())) {
						GroupsActivity.instance.onResume();
					}
				}

			});
		}

		@Override
		public void onApplicationDeclined(String groupId, String groupName, String decliner, String reason) {
			// 加群申请被拒绝，demo未实现
		}

	}

	/**
	 * 保存提示新消息
	 * 
	 * @param msg
	 */
	private void notifyNewIviteMessage(InviteMessage msg) {
		saveInviteMsg(msg);
		// 提示有新消息
		EMNotifier.getInstance(getActivity().getApplicationContext()).notifyOnNewMsg();

		// 刷新bottom bar消息未读数
		updateUnreadAddressLable();
		// 刷新好友页面ui
		Define_C.s_HasChangeContent = false;
		if (m_currentTabIndex == 0)
			// m_ContactlistFragment.refresh();
			handler.sendEmptyMessage(004);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// getActivity 已经在mainActivity中注册了
		// BusProvider.getInstance().register(getActivity());

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}

	/**
	 * 保存邀请等msg
	 * 
	 * @param msg
	 */
	private void saveInviteMsg(InviteMessage msg) {
		// 保存msg
		inviteMessgeDao.saveMessage(msg);
		// 未读数加1
		User user = BaseApplication.getApplication().getContactList().get(Constant.NEW_FRIENDS_USERNAME);
		// if (user.getUnreadMsgCount() == 0)

		// user.setUnreadMsgCount(user.getUnreadMsgCount() + 1);

		int unreadAddressCountTotal = BaseApplication.getApplication().getUnreadAddressCountTotal();
		user.setUnreadMsgCount(unreadAddressCountTotal);
	}

	/**
	 * set head
	 * 
	 * @param username
	 * @return
	 */
	User setUserHead(String username) {
		User user = new User();
		user.setUsername(username);
		String headerName = null;
		if (!TextUtils.isEmpty(user.getNick())) {
			headerName = user.getNick();
		} else {
			headerName = user.getUsername();
		}
		if (username.equals(Constant.NEW_FRIENDS_USERNAME)) {
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
		return user;
	}

	// ============== 接受者 end
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
//		checkGroupsMembers(EMGroupManager.getInstance().getAllGroups());
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		LogUtils.i(TAG + "onResume=currentLoadProgress=");
		// huanx start
		if (!isConflict || !isCurrentAccountRemoved) {
			updateUnreadLabel();
			updateUnreadAddressLable();
			EMChatManager.getInstance().activityResumed();
		}
		if (((MainActivity) getActivity()).click_m_index != 2) {
			// 当前页面如果为聊天历史页面，刷新此页面
			UIUtils.runInMainThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					updateUnreadLabel(((MainActivity) getActivity()).unreadLabel);
				}
			});

		}
		// huanx end
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// huanx
		// outState.putBoolean("isConflict", isConflict);
		// outState.putBoolean(Constant.ACCOUNT_REMOVED,
		// isCurrentAccountRemoved);
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		unRegisterReceiver();

		// if (conflictBuilder != null) {
		// conflictBuilder.create().dismiss();
		// conflictBuilder = null;
		// }

	}

	/**
	 * 注销广播接收者
	 */
	public void unRegisterReceiver() {

		if (context != null && msgReceiver != null) {

			context.unregisterReceiver(msgReceiver);
		}
		if (context != null && ackMessageReceiver != null) {

			context.unregisterReceiver(ackMessageReceiver);
		}
		if (context != null && cmdMessageReceiver != null) {

			context.unregisterReceiver(cmdMessageReceiver);
		}

	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		ac = (MainActivity) activity;
		// 将onAttach里面的ac=activity改成ac=getActivity（）也可以
	}

	@Override
	public void onDetach() {
		super.onDetach();
		try {
			Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);

		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 更新群成员数据库（hx）
	 * 
	 * @param groupId
	 */
	private synchronized void updateGroupDb(String groupId) {

		EMGroup groupFromServer;
		try {
			groupFromServer = EMGroupManager.getInstance().getGroupFromServer(groupId);

			List<String> members = groupFromServer.getMembers();
			EMGroupManager.getInstance().createOrUpdateLocalGroup(groupFromServer);

			if (!members.contains(BaseApplication.getApplication().m_user_id)) {

				EMGroupManager.getInstance().deleteLocalGroup(groupId);

			}

			LogUtils.i(TAG, "members:" + members.toString() + "local members:");
		} catch (EaseMobException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUtils.e(TAG, "EaseMobException,e=" + e.toString());
		}

	}

	private boolean checkGroupsMembersDialog;

	/**
	 * 检查groups成员是否为空
	 * 
	 * @param grouplist2
	 */
	private void checkGroupsMembers(List<EMGroup> grouplist2) {
		// TODO Auto-generated method stub

		if (grouplist2.size() <= 0) {
			return;
		}
		EMGroup emGroup = grouplist2.get(0);

		List<String> members = emGroup.getMembers();

		if (members.size() == 0) {

			new AsyncTask<Void, Void, Void>() {

				private ProgressDialog progressDialog;

				protected void onPreExecute() {

					rl_dialog.setVisibility(View.VISIBLE);
				};

				@Override
				protected Void doInBackground(Void... params) {

					// TODO
					// 获取群聊列表,sdk会把群组存入到EMGroupManager和db中(),群聊里只有groupid和groupname等简单信息，不包含members
					List<EMGroup> groupsFromServer;
					try {
						groupsFromServer = EMGroupManager.getInstance().getGroupsFromServer();

						for (EMGroup dd : groupsFromServer) {
							EMGroup groupFromServer = EMGroupManager.getInstance().getGroupFromServer(dd.getGroupId());
							List<String> members = groupFromServer.getMembers();
							EMGroupManager.getInstance().createOrUpdateLocalGroup(groupFromServer);
							// EMGroup group =
							// EMGroupManager.getInstance().getGroup(dd.getGroupId());
							LogUtils.i(TAG, "members:" + members.toString() + "local members:");
						}
					} catch (EaseMobException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					return null;
				}

				protected void onPostExecute(Void result) {

					rl_dialog.setVisibility(View.GONE);
				};
			}.execute();

			return;
		}

	}

	@Produce
	public UnreadInviteRefeshEvent getUnreadInviteRefeshEvent() {
		LogUtils.i("otto" + "UnreadInviteRefeshEvent");
		UnreadInviteRefeshEvent UnreadInviteRefeshEvent = new UnreadInviteRefeshEvent();
		return UnreadInviteRefeshEvent;
	}

}
