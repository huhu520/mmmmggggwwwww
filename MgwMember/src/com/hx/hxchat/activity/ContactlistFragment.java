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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.util.HanziToPinyin;
import com.hx.hxchat.Constant;
import com.hx.hxchat.adapter.ContactAdapter;
import com.hx.hxchat.db.InviteMessgeDao;
import com.hx.hxchat.db.UserDao;
import com.hx.hxchat.domain.User;
import com.hx.hxchat.fragment.HXBaseFragment;
import com.hx.hxchat.fragment.NewsFragmentCopy;
import com.hx.hxchat.utils.CommonUtils;
import com.hx.hxchat.utils.UserUtils;
import com.hx.hxchat.widget.Sidebar;
import com.mgw.member.R;
import com.mgw.member.constant.Define_C;
import com.mgw.member.factory.FragmentFactory;
import com.mgw.member.http.Http;
import com.mgw.member.manager.BaseApplication;
import com.mgw.member.ottoEvent.FragmentNeedRefreshEvent;
import com.mgw.member.ui.activity.MainActivity;
import com.mgw.member.ui.fragment.NewsFragment;
import com.mgw.member.ui.widget.LoadingPage.LoadResult;
import com.mgw.member.uitls.LogUtils;
import com.squareup.otto.Subscribe;

/**
 * 联系人列表页
 * 
 */
public class ContactlistFragment extends HXBaseFragment{
	String TAG = ContactlistFragment.class.getSimpleName();
	private ContactAdapter adapter;
	private List<User> contactList;
	private ListView listView;
	private boolean hidden;
	/** 导航栏 */
	private Sidebar sidebar;
	ProgressDialog progressDialog;
	private InputMethodManager inputMethodManager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_contact_list, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		// TODO 待测 防止被T后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
		if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
			return;


		inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		listView = (ListView) getView().findViewById(R.id.list);
		sidebar = (Sidebar) getView().findViewById(R.id.sidebar);
		sidebar.setListView(listView);

		// 用户表
		contactList = new ArrayList<User>();
		// 获取设置contactlist
		getContactList();
		progressDialog = CommonUtils.getProgrossDialog(getActivity(), "正在努力读取数据...", true);
		// progressDialog.show();
		// 设置adapter
		initContactAdapter(getActivity(), contactList);

	}

	/**
	 * 初始化
	 * 
	 * @param activity
	 * @param contactList2
	 *            Created by Administrator
	 */
	private void initContactAdapter(FragmentActivity activity, final List<User> contactList) {
		if (contactList == null || sidebar == null)
			return;
		adapter = new ContactAdapter(getActivity(), R.layout.row_contact, contactList);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (EMChatManager.getInstance().isConnected()) {
					String username = adapter.getItem(position).getUsername();
					if (Constant.NEW_FRIENDS_USERNAME.equals(username)) {
						// 新的朋友
						User user = BaseApplication.getApplication().getContactList().get(Constant.NEW_FRIENDS_USERNAME);
						user.setUnreadMsgCount(0);
						startActivity(new Intent(getActivity(), NewFriendsMsgActivity.class));

					} else if (Constant.GROUP_USERNAME.equals(username)) {
						// TODO 进入群聊列表页面（此功能暂无）
						startActivity(new Intent(getActivity(), GroupsActivity.class));

					} else {
						// demo中直接进入聊天页面，实际一般是进入用户详情页
						// 设置昵称
						// TextView tv = (TextView)
						// view.findViewById(R.id.name); // 昵称
						String imageHeaderPath = adapter.getItem(position).getAvatar(); // 头像路径
						startActivity(new Intent(getActivity(), ChatActivity.class).putExtra("path", imageHeaderPath).putExtra("userId", adapter.getItem(position).getUsername())
								.putExtra("userName", contactList.get(position).getNick()));
						// startActivity(new Intent(getActivity(),
						// ChatActivity.class).putExtra("userId",
						// adapter.getItem(position).getUsername()));
					}
				} else {
					BaseApplication.getApplication().isHxLogined=false;
//					BaseApplication.getApplication().Hxlogin();
//					EMChatManager.getInstance().
					Toast.makeText(getActivity(), "正在连接，请稍后", 0).show();
				}
			}
		});
		listView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// 隐藏软键盘
				if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
					if (getActivity().getCurrentFocus() != null) {

						inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
					}
				}
				return false;
			}
		});

		registerForContextMenu(listView);

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		// 长按前两个不弹menu
		if (((AdapterContextMenuInfo) menuInfo).position > 0) {
			getActivity().getMenuInflater().inflate(R.menu.context_contact_list, menu);
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.delete_contact) {
			User tobeDeleteUser = adapter.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
			if (tobeDeleteUser.getreferee().equals("1")) {
				// 删除此联系人
				deleteContact(tobeDeleteUser);
				// 删除相关的邀请消息
				InviteMessgeDao dao = new InviteMessgeDao(getActivity());
				dao.deleteMessage(tobeDeleteUser.getUsername());
				return true;
			} else {
				Toast.makeText(getActivity(), "自己的代理商不允许删除！", Toast.LENGTH_SHORT).show();
				return true;
			}

		}

		// else if (item.getItemId() == R.id.add_to_blacklist) {
		// User user = adapter.getItem(((AdapterContextMenuInfo)
		// item.getMenuInfo()).position);
		// try {
		// // 加入到黑名单
		// EMContactManager.getInstance().addUserToBlackList(user.getUsername(),
		// true);
		// Toast.makeText(getActivity(), "移入黑名单成功", 0).show();
		// } catch (EaseMobException e) {
		// e.printStackTrace();
		// Toast.makeText(getActivity(), "移入黑名单失败", 0).show();
		// }
		// }

		return super.onContextItemSelected(item);
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		this.hidden = hidden;
		if (!hidden) {
			refresh();
		}

		LogUtils.i(TAG + ",onHiddenChanged,hidden=" + hidden);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (!hidden) {
			refresh();
			if(((NewsFragmentCopy)FragmentFactory.createFragment(FragmentFactory.TAB_NEWS)).m_currentTabIndex==1){
				((NewsFragmentCopy)FragmentFactory.createFragment(FragmentFactory.TAB_NEWS)).m_ChatHistoryFragment.refresh();
			}
			
			
		}
		LogUtils.i(TAG + ",onHiddenChanged,hidden=" + hidden);
	}

	/**
	 * 删除联系人
	 * 
	 * @param toDeleteUser
	 */
	public void deleteContact(final User tobeDeleteUser) {
		final ProgressDialog pd = CommonUtils.getProgrossDialog(getActivity(), "正在删除...", false);
		pd.setCanceledOnTouchOutside(false);
		pd.show();

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					JSONObject oJSONObject = UserUtils.delfriend(getActivity(), tobeDeleteUser.getUsername());
					if (oJSONObject != null) {
						if (oJSONObject.getString("flag").equals("0")) {
							// 删除db和内存中此用户的数据
							UserDao dao = new UserDao(getActivity());
							dao.deleteContact(tobeDeleteUser.getUsername());
							BaseApplication.getApplication().getContactList().remove(tobeDeleteUser.getUsername());
							getActivity().runOnUiThread(new Runnable() {
								@Override
								public void run() {
									pd.dismiss();
									adapter.remove(tobeDeleteUser);
									Define_C.s_HasChangeContent=true;
									adapter.notifyDataSetChanged();
									
								}
							});
							EMContactManager.getInstance().deleteContact(tobeDeleteUser.getUsername());
						} else {
							getActivity().runOnUiThread(new Runnable() {
								@Override
								public void run() {
									pd.dismiss();
									Toast.makeText(getActivity(), "删除失败", 1).show();
								}
							});
						}
					} else {
						getActivity().runOnUiThread(new Runnable() {
							@Override
							public void run() {
								pd.dismiss();
								Toast.makeText(getActivity(), "删除失败", 1).show();
							}
						});
					}
				} catch (final Exception e) {
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							pd.dismiss();
							Toast.makeText(getActivity(), "删除失败" + e.getMessage(), 1).show();
						}
					});
				}

			}
		}).start();

	}

	// 刷新ui
	public void refresh() {
		try {
			// 可能会在子线程中调到这方法
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// if (Define_C.s_HasChangeContent) {
					getContactList();
					adapter.notifyDataSetChanged();
					Define_C.s_HasChangeContent = false;
					// }

					// Message message = Message.obtain();
					// message.obj = contactList;
					// m_handler.sendMessage(message);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 从本地拿到数据（登录就将数据放到了本地）
	 */
	private void getContactList() {
		contactList.clear();
		// 获取本地好友列表
		Map<String, User> users = BaseApplication.getApplication().getContactList();
		Iterator<Entry<String, User>> iterator = users.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, User> entry = iterator.next();
			if (!entry.getKey().equals(Constant.NEW_FRIENDS_USERNAME) && !entry.getKey().equals(Constant.GROUP_USERNAME)) {

				User oUser = entry.getValue();
				// TODO
				SetHead(oUser);
				contactList.add(oUser);
			}

		}
		// 排序
		Collections.sort(contactList, new Comparator<User>() {

			@Override
			public int compare(User lhs, User rhs) {
				if (lhs != null && rhs != null) {
					if (lhs.getHeader() != null && !lhs.getHeader().trim().equals("") && rhs.getHeader() != null && !rhs.getHeader().trim().equals("")) {
						int oint = lhs.getHeader().trim().compareTo(rhs.getHeader().trim());
						return oint;
					} else {
						Log.e("czy", lhs.getHeader() + "==" + lhs.getNick() + "===" + rhs.getHeader() + "==" + rhs.getNick());
					}
				}
				return -1;
			}
		});

		User oUser = users.get(Constant.NEW_FRIENDS_USERNAME);
		if (oUser == null) {
			oUser = new User();
			oUser.setUsername(Constant.NEW_FRIENDS_USERNAME);
			oUser.setAvatar("");
		}
		User oUser1 = users.get(Constant.GROUP_USERNAME);
		if (oUser1 == null) {
			oUser1 = new User();
			oUser1.setUsername(Constant.GROUP_USERNAME);
			oUser1.setAvatar("");
		}

		//
		// Collections.sort(contactList, new Comparator<User>() {
		//
		// @Override
		// public int compare(User lhs, User rhs) {
		// if (lhs != null && rhs != null) {
		// if (lhs.getHeader() != null && !lhs.getHeader().trim().equals("") &&
		// rhs.getHeader() != null && !rhs.getHeader().trim().equals("")) {
		// int oint = lhs.getHeader().trim().compareTo(rhs.getHeader().trim());
		// return oint;
		// } else {
		// Log.e("czy", lhs.getHeader() + "==" + lhs.getNick() + "===" +
		// rhs.getHeader() + "==" + rhs.getNick());
		// }
		// }
		// return -1;
		// }
		// });
		// contactList.add(0, oUser);
		//
		// Message message = Message.obtain();
		// message.obj = contactList;
		// m_handler.sendMessage(message);
		// return;
		// handler.sendEmptyMessage(0);
		// // getHeadBynet(contactList);

		// 加入"申请与通知"和"群聊"
		if (users.get(Constant.GROUP_USERNAME) != null)
			contactList.add(0, oUser1);
		
		// 把"申请与通知"添加到首位
		if (users.get(Constant.NEW_FRIENDS_USERNAME) != null)
			contactList.add(0, oUser);
//		[糊糊, klm222, Yyy, 陈大鹏, 感觉, 陈工, 炫迈]
	}

	// Handler m_handler = new Handler() {
	// @Override
	// public void handleMessage(android.os.Message msg) {
	//
	// if (contactList == null || sidebar == null)
	// return;
	// adapter = new ContactAdapter(getActivity(), R.layout.row_contact,
	// contactList, sidebar);
	// listView.setAdapter(adapter);
	// listView.setOnItemClickListener(new OnItemClickListener() {
	//
	// @Override
	// public void onItemClick(AdapterView<?> parent, View view, int position,
	// long id) {
	// if (EMChatManager.getInstance().isConnected()) {
	// String username = adapter.getItem(position).getUsername();
	// if (Constant.NEW_FRIENDS_USERNAME.equals(username)) {
	// /*
	// * if(Define.bHxLogined) {
	// */
	// // 进入申请与通知页面
	// User user =
	// BaseApplication.getApplication().getContactList().get(Constant.NEW_FRIENDS_USERNAME);
	// user.setUnreadMsgCount(0);
	// startActivity(new Intent(getActivity(), NewFriendsMsgActivity.class));
	// /*
	// * } else { Toast.makeText(getActivity(),
	// * "正在努力连接聊天服务器，暂时不能添加好友", 1).show(); }
	// */
	// } else if (Constant.GROUP_USERNAME.equals(username)) {
	// // 进入群聊列表页面
	// startActivity(new Intent(getActivity(), GroupsActivity.class));
	//
	// } else {
	//
	// TextView tv = (TextView) view.findViewById(R.id.name); // 昵称
	// String path = adapter.getItem(position).getM_imagePath(); // 头像路径
	// // demo中直接进入聊天页面，实际一般是进入用户详情页
	// startActivity(new Intent(getActivity(),
	// ChatActivity.class).putExtra("path", path).putExtra("userId",
	// adapter.getItem(position).getUsername())
	// .putExtra("userName", tv.getText().toString()));
	//
	// }
	// } else {
	// BaseApplication.getApplication().Hxlogin();
	// Toast.makeText(getActivity(), "正在连接，请稍后", 0).show();
	// }
	// }
	// });
	// listView.setOnTouchListener(new OnTouchListener() {
	//
	// @Override
	// public boolean onTouch(View v, MotionEvent event) {
	// // 隐藏软键盘
	// if (getActivity().getWindow().getAttributes().softInputMode !=
	// WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
	// if (getActivity().getCurrentFocus() != null) {
	//
	// //
	// inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
	// // InputMethodManager.HIDE_NOT_ALWAYS);
	// }
	// }
	// return false;
	// }
	// });
	//
	// /*
	// * ImageView addContactView = (ImageView)
	// * getView().findViewById(R.id.iv_new_contact); // 进入添加好友页
	// * addContactView.setOnClickListener(new OnClickListener() {
	// *
	// * public void onClick(View v) { startActivity(new
	// * Intent(getActivity(), AddContactActivity.class)); } });
	// */
	// registerForContextMenu(listView);
	//
	// }
	// };

	void hideSoftKeyboard(Activity activity) {
		if (activity.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (activity.getCurrentFocus() != null)
				inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	public void getHeadBynet(final List<User> list) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String userLiString = "";
					for (User user : list) {
						if (userLiString.length() > 0)
							userLiString += ",";
						userLiString += user.getUsername();
					}

					if (userLiString.length() > 0) {
						JSONObject jo = Http.postUesrs(userLiString);
						Message msg = new Message();
						msg.obj = jo;
						msg.what = 2;
						handler.sendMessage(msg);
					} else {

						handler.sendEmptyMessage(0);

					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					handler.sendEmptyMessage(-1);
				}
			}
		}).start();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		if (((MainActivity) getActivity()).isConflict) {
			outState.putBoolean("isConflict", true);
		} else if (((MainActivity) getActivity()).getCurrentAccountRemoved()) {
			outState.putBoolean(Constant.ACCOUNT_REMOVED, true);
		}

	}

	private void SetHead(User puser) {
		String username = puser.getUsername();
		String headerName = null;
		if (!TextUtils.isEmpty(puser.getNick())) {
			headerName = puser.getNick();
		} else {
			headerName = puser.getUsername();
		}

		if (username.equals(Constant.NEW_FRIENDS_USERNAME) || username.equals(Constant.GROUP_USERNAME)) {
			puser.setHeader("");
		} else if (Character.isDigit(headerName.charAt(0))) {
			// 指示是否指定的字符是一个数字。
			puser.setHeader("#");
		} else {
			try {
				puser.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target.substring(0, 1).toUpperCase());
				char header = puser.getHeader().toLowerCase().charAt(0);
				if (header < 'a' || header > 'z') {
					puser.setHeader("#");
				}
			} catch (Exception ex) {
				puser.setHeader("#");
			}
		}

	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			progressDialog.dismiss();
			if (msg.what == -1) {
				Toast.makeText(getActivity(), "网络连接失败，请检查网络设置", Toast.LENGTH_LONG).show();
				return;
			} else if (msg.what == 0) {
				// User oUser = users.get(Constant.NEW_FRIENDS_USERNAME);
				// if (oUser == null) {
				// oUser = new User();
				// oUser.setUsername(Constant.NEW_FRIENDS_USERNAME);
				// oUser.setM_imagePath("");
				// }
				// Collections.sort(contactList, new Comparator<User>() {
				//
				// @Override
				// public int compare(User lhs, User rhs) {
				// if (lhs != null && rhs != null) {
				// if (lhs.getHeader() != null &&
				// !lhs.getHeader().trim().equals("") && rhs.getHeader() != null
				// && !rhs.getHeader().trim().equals("")) {
				// int oint =
				// lhs.getHeader().trim().compareTo(rhs.getHeader().trim());
				// return oint;
				// } else {
				// Log.e("czy", lhs.getHeader() + "==" + lhs.getNick() + "===" +
				// rhs.getHeader() + "==" + rhs.getNick());
				// }
				// }
				// return -1;
				// }
				// });
				// contactList.add(0, oUser);
				//
				// Message message = Message.obtain();
				// message.obj = contactList;
				// m_handler.sendMessage(message);
				// return;
			}
			/*
			 * JSONObject jo1 = (JSONObject) msg.obj; contactList.clear(); try {
			 * if (jo1 != null) { int flag = jo1.getInt("flag"); if (flag == 0)
			 * { if (!jo1.isNull("items")) { JSONArray array =
			 * jo1.getJSONArray("items"); for (int i = 0; i < array.length();
			 * i++) { jo1 = array.getJSONObject(i); User user = new User();
			 * user.setNick(jo1.getString("NickName"));
			 * user.setM_imagePath(jo1.getString("MemberPic"));
			 * user.setUsername(jo1.getString("UserId")); SetHead(user);
			 * contactList.add(user); }
			 * 
			 * } } } } catch (JSONException e) { // TODO Auto-generated catch
			 * block e.printStackTrace(); Log.i("错误", "获取好友信息错误"); }
			 */
			if (progressDialog.isShowing())
				progressDialog.dismiss();

			// // 排序
			// Collections.sort(contactList, new Comparator<User>() {
			//
			// @Override
			// public int compare(User lhs, User rhs) {
			// if (lhs != null && rhs != null) {
			// if (lhs.getHeader() != null && !lhs.getHeader().trim().equals("")
			// && rhs.getHeader() != null && !rhs.getHeader().trim().equals(""))
			// {
			// int oint =
			// lhs.getHeader().trim().compareTo(rhs.getHeader().trim());
			// return oint;
			// } else {
			// Log.e("czy", lhs.getHeader() + "==" + lhs.getNick() + "===" +
			// rhs.getHeader() + "==" + rhs.getNick());
			// }
			// }
			// return -1;
			// }
			// });

			// // 加入"申请与通知"和"群聊"
			// // contactList.add(0, users.get(Constant.GROUP_USERNAME));
			// // 把"申请与通知"添加到首位
			// User oUser2 = users.get(Constant.NEW_FRIENDS_USERNAME);
			// if (oUser2 == null) {
			// oUser2 = new User();
			// oUser2.setUsername(Constant.NEW_FRIENDS_USERNAME);
			// oUser2.setM_imagePath("");
			// }
			// contactList.add(0, oUser2);
			//
			// Message message = Message.obtain();
			// message.obj = contactList;
			// m_handler.sendMessage(message);

		};
	};

	
	
	/**
	 * 刷新adapter的消费者
	 * @param fragmentNeedRefreshEvent
	 */
	@Subscribe
	public void FragmentNeedRefreshEvent(FragmentNeedRefreshEvent fragmentNeedRefreshEvent) {
		if(TAG.equals(fragmentNeedRefreshEvent.getFragmentName())){
			refresh();
			LogUtils.i("otto", TAG+",refresh");
		}
	}


	
	
	
	
	@Override
	protected LoadResult load() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected View createLoadedView() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
}
