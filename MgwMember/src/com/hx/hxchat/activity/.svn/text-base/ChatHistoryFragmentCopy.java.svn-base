/**
\ * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContact;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.google.gson.Gson;
import com.hx.hxchat.adapter.ChatHistoryAdapter;
import com.hx.hxchat.adapter.ChatHistoryAdapterCopy;
import com.hx.hxchat.db.InviteMessgeDao;
import com.hx.hxchat.db.TopUserDao;
import com.hx.hxchat.domain.TopUser;
import com.hx.hxchat.domain.User;
import com.hx.hxchat.fragment.HXBaseFragment;
import com.hx.hxchat.holder.protocol.ChatHistoryListProtocol;
import com.hx.hxchat.interfaces.isLoadDataListener;
import com.hx.hxchat.otto.ChatHistoryneedRefeshEvent;
import com.hx.hxchat.utils.UserUtils;
import com.mgw.member.R;
import com.mgw.member.manager.BaseApplication;
import com.mgw.member.ottoEvent.FragmentNeedRefreshEvent;
import com.mgw.member.ui.widget.LoadingPage.LoadResult;
import com.mgw.member.uitls.LogUtils;
import com.mgw.member.uitls.UIUtils;
import com.squareup.otto.Subscribe;

/**
 * 聊天记录Fragment
 * 
 */
public class ChatHistoryFragmentCopy extends HXBaseFragment {

	public static final String TAG = ChatHistoryFragmentCopy.class
			.getSimpleName();
	private InputMethodManager inputMethodManager;
	private ListView listView;
	private Context context;
	private Map<String, User> contactList;
	private ChatHistoryAdapterCopy adapter;

	private boolean hidden;
	private boolean onPostExecuting = false;
	private Gson gson;
	private Dialog progressDialog;
	private List<EMContact> m_EmContacts;

	private Vibrator vibrator;
	// 置顶消息 start
	private List<EMContact> top_lists = new ArrayList<EMContact>();
	private Map<String, TopUser> topMap;
	// 置顶消息 end

	private BroadcastReceiver myReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			// Toast.makeText(context, "ssssssssssssss",
			// Toast.LENGTH_SHORT).show();
			// errorItem.setVisibility(View.GONE);
		}

	};

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		context = getActivity();
		LogUtils.i(TAG, "onActivityCreated");
		// IntentFilter intentFilter = new IntentFilter();
		// intentFilter.addAction("ishxlogined");
		// getActivity().registerReceiver(myReceiver, intentFilter);
		// inputMethodManager = (InputMethodManager)
		// getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		// //
		// // contact list
		// progressDialog = CommonUtils.getUserDefinedDialog(context,
		// "正在为你卖命加载...", false, true);
		// listView = (ListView) getView().findViewById(R.id.list);
		// // 注册上下文菜单

	}

	private void initData() {
		// listView.setOnItemClickListener(new OnItemClickListener() {
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view, int
		// position, long id) {
		// EMContact emContact = adapter.getItem(position);
		// if
		// (adapter.getItem(position).getUsername().equals(BaseApplication.getApplication().getUserName()))
		// Toast.makeText(getActivity(), "不能和自己聊天", 0).show();
		// else {
		// if (EMChatManager.getInstance().isConnected()) {
		// // 进入聊天页面
		// Intent intent = new Intent(getActivity(), ChatActivity.class);
		// if (emContact instanceof EMGroup) {
		// // it is group chat
		// intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
		// intent.putExtra("groupId", ((EMGroup) emContact).getGroupId());
		// } else {
		// // it is single chat
		// intent.putExtra("userId", emContact.getUsername());
		// intent.putExtra("userName", emContact.getNick());
		//
		// // intent.putExtra("userId",
		// // adapter.m_User_s.get(position).getM_name());
		// }
		// startActivity(intent);
		// } else {
		// BaseApplication.getApplication().isHxLogined = false;
		// // BaseApplication.getApplication().Hxlogin();
		// Toast.makeText(getActivity(), "正在连接服务器，请稍后", 0).show();
		// }
		// }
		// }
		// });
		//
		// // 隐藏输入法
		// listView.setOnTouchListener(new OnTouchListener() {
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// // 隐藏软键盘
		// if (getActivity().getWindow().getAttributes().softInputMode !=
		// WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
		// if (getActivity().getCurrentFocus() != null)
		// inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
		// InputMethodManager.HIDE_NOT_ALWAYS);
		// }
		// return false;
		// }
		// });
		//
		//
		// listView.setOnItemLongClickListener(new OnItemLongClickListener() {
		//
		// @Override
		// public boolean onItemLongClick(AdapterView<?> parent, View view, int
		// position, long id) {
		// if(vibrator==null)
		// vibrator = (Vibrator)
		// getActivity().getSystemService(Context.VIBRATOR_SERVICE);
		//
		// long[] pattern={100,100};
		// vibrator.vibrate(30);
		//
		// String title="";
		// EMContact item = adapter.getItem(position);
		//
		// if(item instanceof EMGroup){
		// title=((EMGroup) item).getGroupName();
		// }else{
		//
		// title= item.getNick();
		//
		// }
		//
		// showMyDialog(title, adapter, position);
		// return false;
		// }
		// });
		// registerForContextMenu(listView);
		//
		// if (!CommonUtils.isNetWorkConnected(context)) {
		// return;
		// }
		// // （数据拿到了）
		//
		// new GetDataAcyncTask().execute();
		// setLoadDataComplete(new isLoadDataListener() {
		// @Override
		// public void loadStart() {
		// onPostExecuting = true;
		// if (!progressDialog.isShowing() && !hidden) {
		// // progressDialog.show();
		//
		// }
		// }
		//
		// @Override
		// public void loadComplete() {
		// onPostExecuting = false;
		// if (progressDialog.isShowing() && !hidden) {
		// // progressDialog.dismiss();
		//
		// }
		// }
		//
		// @Override
		// public void progress(int progress) {
		//
		// }
		// });

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		// if (((AdapterContextMenuInfo) menuInfo).position > 0) {
		getActivity().getMenuInflater().inflate(R.menu.delete_message, menu);
		// }
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		// if (item.getItemId() == R.id.delete_message) {
		// EMContact tobeDeleteUser = adapter.getItem(((AdapterContextMenuInfo)
		// item.getMenuInfo()).position);
		// boolean isGroup = false;
		// if (tobeDeleteUser instanceof EMGroup)
		// isGroup = true;
		//
		// // 删除此会话
		// boolean deleteConversation =
		// EMChatManager.getInstance().deleteConversation(tobeDeleteUser.getUsername());
		// InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(getActivity());
		// inviteMessgeDao.deleteMessage(tobeDeleteUser.getUsername());
		//
		// BaseApplication.getApplication().GetLinkerDao().deleteContact(tobeDeleteUser.getUsername());
		//
		// adapter.remove(tobeDeleteUser);
		// adapter.notifyDataSetChanged();
		//
		// // 更新消息未读数
		// // TODO
		// // ((MainActivity) getActivity()).updateUnreadLabel();
		//
		// return true;
		// }
		return super.onContextItemSelected(item);
	}

	// /**
	// * 刷新页面（）
	// */
	// public void refreshNoDialog() {
	// if (!onPostExecuting) {
	// new GetDataAcyncTask().execute();
	// }
	//
	// }

	/**
	 * 获取有聊天记录的users和groups
	 * 
	 * @param context
	 * @return
	 */
	private List<EMContact> loadUsersWithRecentChat() {

		topMap = BaseApplication.getApplication().getTopUserList();
		// =========没聊天页显示在上面
		Map<String, User> linkcontactList = BaseApplication.getApplication()
				.GetLinkerDao().getContactList();

		List<EMContact> resultList = new ArrayList<EMContact>();
		List<EMContact> topList = new ArrayList<EMContact>();
		// 包含陌生人的会话
		if (EMChatManager.getInstance().isConnected()) {

			Hashtable<String, EMConversation> conversations = EMChatManager
					.getInstance().getAllConversations();

			Enumeration<String> keys = conversations.keys();

			while (keys.hasMoreElements()) {
				String key = keys.nextElement();
				LogUtils.i(TAG, "nextElement" + key);
				EMConversation emConversation = conversations.get(key);

				if (emConversation != null
						&& !linkcontactList.containsKey(emConversation
								.getUserName())) {

					// if("admin".equals(emConversation.getUserName())){
					// String admin="00001";
					//
					//
					// // if
					// (!BaseApplication.getApplication().GetLinkerDao().containsKey(admin))
					// {
					// // User user1=new User(admin);
					// //
					// BaseApplication.getApplication().GetLinkerDao().saveContact(user1);
					// // linkcontactList.put(user1.getUsername(), user1);
					// // }else{
					// // User contact =
					// BaseApplication.getApplication().GetLinkerDao().getContact(admin);
					// // linkcontactList.put(contact.getUsername(), contact);
					// // }
					// //
					// linkcontactList.put("admin", new User("admin"));
					//
					// }else{

					User user = UserUtils.getUseFromNet(emConversation
							.getUserName());
					if (user != null) {
						user.setLinkcache("0");
						if (!BaseApplication.getApplication().GetLinkerDao()
								.containsKey(user.getUsername())) {
							BaseApplication.getApplication().GetLinkerDao()
									.saveContact(user);
						}
						linkcontactList.put(user.getUsername(), user);
					}

					// }

				} else if (emConversation != null
						&& linkcontactList.containsKey(emConversation
								.getUserName())
						&& "1".equals(linkcontactList.get(
								emConversation.getUserName()).getLinkcache())) {

					User user = linkcontactList.get(emConversation
							.getUserName());
					user.setLinkcache("0");
					BaseApplication.getApplication().GetLinkerDao()
							.updateContact(user);

				}

			}

		}
		// 将好友会话和临时会话存入返回
		for (User user : linkcontactList.values()) {
			if ((!("1".equals(user.getLinkcache())))
					&& (!topMap.containsKey(user.getUsername()))) {
				resultList.add(user);
			} else if ((topMap.containsKey(user.getUsername()))) {
				topList.add(user);
			}
		}

		for (EMGroup group : EMGroupManager.getInstance().getAllGroups()) {
			EMConversation conversation = EMChatManager.getInstance()
					.getConversation(group.getGroupId());
			if (conversation.getMsgCount() > 0) {

				if (!topMap.containsKey(group.getGroupId())) {

					resultList.add(group);
				} else {

					topList.add(group);
				}
			}
		}

		top_lists.clear();
		top_lists.addAll(topList);

		// 排序
		sortUserByLastChatTime(resultList);
		sortUserByLastChatTime(top_lists);
		return resultList;
	}

	/**
	 * 根据最后一条消息的时间排序
	 * 
	 * @param usernames
	 */
	private void sortUserByLastChatTime(List<EMContact> contactList) {
		Collections.sort(contactList, new Comparator<EMContact>() {
			@Override
			public int compare(final EMContact user1, final EMContact user2) {
				EMConversation conversation1 = EMChatManager.getInstance()
						.getConversation(user1.getUsername());
				EMConversation conversation2 = EMChatManager.getInstance()
						.getConversation(user2.getUsername());
				EMMessage user2LastMessage = conversation2.getLastMessage();
				if (user2LastMessage == null) {
					return -1;
				}
				EMMessage user1LastMessage = conversation1.getLastMessage();
				if (user1LastMessage == null) {
					return -1;
				}
				if (user2LastMessage.getMsgTime() == user1LastMessage
						.getMsgTime()) {
					return 0;
				} else if (user2LastMessage.getMsgTime() > user1LastMessage
						.getMsgTime()) {
					return 1;
				} else {
					return -1;
				}
			}

		});
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		this.hidden = hidden;
		if (!hidden) {
			refresh();
		}
		LogUtils.i(TAG, "onHiddenChanged=" + hidden);
		// String string = getActivity().getSharedPreferences("mgw_data",
		// 0).getString("mgw_data", "d");
		if (!EMChatManager.getInstance().isConnected()) {
			// BaseApplication.getApplication().Hxlogin();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		LogUtils.i(TAG, "onResume");
		// if (!hidden) {
		// refresh();
		// }
		//
		// if (errorItem.getVisibility() == View.VISIBLE) {
		//
		// }

		

		if (mSucceedViewCreated){
			refresh();
		}else{
			show();
		}

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		LogUtils.i(TAG, "onPause");
	}

	// public class GetDataAcyncTask extends AsyncTask<Object, Void,
	// List<Items>> {
	//
	// @Override
	// protected void onPreExecute() {
	// onPostExecuting = true;
	// if (loadLisneter != null) {
	// loadLisneter.loadStart();
	// }
	// contactList = BaseApplication.getApplication().getContactList();
	// }
	//
	// @Override
	// protected List<Items> doInBackground(Object... params) {
	// List<Items> m_User_s = new ArrayList<Items>();
	// m_EmContacts = loadUsersWithRecentChat();
	//
	// // for(EMContact dd: m_EmContacts){
	// // System.out.println(dd.getEid()+dd.getNick()+dd.getUsername());
	// // }
	// //
	// // TODO 可能出现ConcurrentModificationException
	// for (EMContact dd : m_EmContacts) {
	// UserDao dao = new UserDao(context);
	// User user = dao.getContact(dd.getUsername());
	//
	// if (user != null) {
	// m_User_s.add(new Items(user.getUsername(), user.getNick(),
	// user.getAvatar()));
	//
	// } else {
	// LinkerDao linkerDao = new LinkerDao(context);
	// User user2 = linkerDao.getContact(dd.getUsername());
	// if (user2 == null) {
	// User user3 = UserUtils.getUseFromNet(dd.getUsername());
	// if (user3 == null) {
	// m_User_s.add(new Items(dd.getUsername(), dd.getUsername(), ""));
	// } else {
	// m_User_s.add(new Items(user3.getUsername(), user3.getNick(),
	// user3.getAvatar()));
	// }
	//
	// } else {
	// m_User_s.add(new Items(user2.getUsername(), user2.getNick(),
	// user2.getAvatar()));
	// }
	// }
	//
	// // Log.i("chath", user.toString());
	// }
	// //
	//
	// return m_User_s;
	// }
	//
	// @Override
	// protected void onPostExecute(List<Items> result) {
	// if (m_EmContacts == null) {
	// m_EmContacts = loadUsersWithRecentChat();
	// }
	// adapter = new ChatHistoryAdapterCopy(MainActivity.mainActivity,
	// R.layout.row_chat_history, m_EmContacts, result,top_lists);
	// listView.setAdapter(adapter);
	// adapter.notifyDataSetChanged();
	// if (loadLisneter != null) {
	// loadLisneter.loadComplete();
	// }
	//
	// }
	//
	// @Override
	// protected void onProgressUpdate(Void... values) {
	// // TODO Auto-generated method stub
	// super.onProgressUpdate(values);
	// }
	//
	// }

	// 声明这一接口变量

	private isLoadDataListener loadLisneter;

	// 给接口赋值，得到接口对象

	public void setLoadDataComplete(isLoadDataListener dataComplete) {
		this.loadLisneter = dataComplete;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		try {
			Field childFragmentManager = Fragment.class
					.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);

		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated m

		super.onDestroy();
//		if (myReceiver != null)
//			getActivity().unregisterReceiver(myReceiver);
	}

	/**
	 * 刷新adapter的消费者
	 * 
	 * @param fragmentNeedRefreshEvent
	 */
	@Subscribe
	public void FragmentNeedRefreshEvent(
			FragmentNeedRefreshEvent fragmentNeedRefreshEvent) {
		if (TAG.equals(fragmentNeedRefreshEvent.getFragmentName())) {
			refresh();
			LogUtils.i("otto", TAG + ",refresh");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		LogUtils.i(TAG, ",onCreateView");
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	protected LoadResult load() {
		ChatHistoryListProtocol protocol = new ChatHistoryListProtocol(context);
		mDatas = protocol.load(0);
		LogUtils.e(TAG, "load");
		return check(mDatas);
	}

	public RelativeLayout errorItem;
	public TextView errorText;
	public FrameLayout fl_list;
	private ListView mListView;
	private ChatHistoryAdapterCopy mAdapter;
	private List<EMConversation> mDatas;
	private List<EMContact> top_listss;
	public boolean mSucceedViewCreated = false;

	@Override
	protected View createLoadedView() {
		View view = UIUtils.inflate(R.layout.fragment_conversation_history);
		errorItem = (RelativeLayout) view.findViewById(R.id.rl_error_item);
		errorText = (TextView) errorItem.findViewById(R.id.tv_connect_errormsg);
		fl_list = (FrameLayout) view.findViewById(R.id.fl_list);
		mListView = (ListView) view.findViewById(R.id.list);
		mAdapter = new ChatHistoryAdapterCopy(context, 1, mDatas, mListView);

		mListView.setAdapter(mAdapter);
		mSucceedViewCreated = true;
		LogUtils.i(TAG, "createLoadedView");
		return view;
	}

	
	
	/**
	 * 刷新页面(获取数据)
	 */
	public void refresh() {
		
		if(!mSucceedViewCreated)
			return;
		
		LogUtils.i(TAG, "refresh");
		load();
		mAdapter = new ChatHistoryAdapterCopy(context, 1, mDatas, mListView);
		mListView.setAdapter(mAdapter);

		mAdapter.notifyDataSetChanged();

	}

	@Subscribe
	public void ChatHistoryneedRefeshEvent(
			ChatHistoryneedRefeshEvent ChatHistoryneedRefeshEvent) {
		if (ChatHistoryneedRefeshEvent != null
				&& ChatHistoryneedRefeshEvent.isNeedRefresh()) {
			// 刷新
			// isRes = true;
			
				refresh();
		
			LogUtils.i("otto", "开始刷新，ChatHistoryneedRefeshEvent");
		}

	}

}
