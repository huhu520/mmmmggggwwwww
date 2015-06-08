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

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import com.hx.hxchat.domain.GroupInfo;
import com.hx.hxchat.holder.GroupListInfoHolder;
import com.hx.hxchat.holder.protocol.GroupsListProtocol;
import com.hx.hxchat.otto.GroupListsRefeshEvent;
import com.hx.hxchat.utils.CommonUtils;
import com.mgw.member.R;
import com.mgw.member.ottoEvent.BusProvider;
import com.mgw.member.ui.widget.LoadingPage;
import com.mgw.member.ui.widget.LoadingPage.LoadResult;
import com.mgw.member.uitls.LogUtils;
import com.mgw.member.uitls.UIUtils;
import com.squareup.otto.Subscribe;

public class GroupsActivity extends HXBaseActivity implements OnClickListener {
	public static String TAG = GroupsActivity.class.getSimpleName();
	public List<GroupInfo> mDatas;
	// 各区域布局及其holder
	private FrameLayout fl_list;
	private GroupListInfoHolder GInfoHolder;
	private InputMethodManager inputMethodManager;

	@Override
	protected void initBase() {
		instance = this;
		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	}

	/** 初始化布局 */
	protected void initViewBase() {
		BusProvider.getInstance().register(this);
		page = new LoadingPage(this) {
			@Override
			public LoadResult load() {
				return GroupsActivity.this.load();
			}
			@Override
			public View createLoadedView() {
				return GroupsActivity.this.createLoadedView();
			}
			
			@Override
			protected View createLoadingView() {
				View view = UIUtils.inflate(R.layout.fragment_groups);
				FrameLayout	fl_list = (FrameLayout) view.findViewById(R.id.fl_list);
				View viewloading = super.createLoadingView();
				fl_list.addView(viewloading);
				return view;
			}
		};
		setContentView(page);
		page.show();
	}

	/** 加载数据 */
	private LoadResult load() {
		GroupsListProtocol protocol = new GroupsListProtocol();
		protocol.setPackageName(TAG);
		mDatas = protocol.load(0);
		if (mDatas == null) {
			return LoadResult.ERROR;
		}
		return LoadResult.SUCCEED;
	}

	public static GroupsActivity instance;

	private LoadingPage page;

	private boolean isRes = false;

	/** 获取数据后的显示的View */
	private View createLoadedView() {
		View view = UIUtils.inflate(R.layout.fragment_groups);
		// 添 加信息区域
		fl_list = (FrameLayout) view.findViewById(R.id.fl_list);
		GInfoHolder = new GroupListInfoHolder(this);
		GInfoHolder.setData(mDatas);
		fl_list.addView(GInfoHolder.getRootView());
		view.findViewById(R.id.iv_new_contact).setOnClickListener(this);
		view.findViewById(R.id.back).setOnClickListener(this);
		return view;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (isRes) {
			load();
			GInfoHolder.setData(mDatas);
			isRes = false;
		}
	}



	@Override
	protected void onDestroy() {
		BusProvider.getInstance().unregister(this);
		super.onDestroy();
		instance = null;
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		// groupListView = (ListView)findViewById(R.id.list);
		// ViewGroup.LayoutParams params = groupListView.getLayoutParams();
		// RelativeLayout layout = (RelativeLayout) findViewById(R.id.layout);
		// params.height = layout.getHeight();
		// //需要设置的listview的高度，你可以设置成一个定值，也可以设置成其他容器的高度，如果是其他容器高度，那么不要在oncreate中执行，需要做延时处理，否则高度为0
		// groupListView.setLayoutParams(params);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			CommonUtils.hideSoftKeyboard(GroupsActivity.this);
			finish();
			break;
		case R.id.iv_new_contact:
			startActivity(new Intent(this, PublicGroupsActivity.class));
			break;

		default:
			break;
		}

	}


	@Subscribe
	public void GroupListsRefeshEvent(GroupListsRefeshEvent GroupListsRefeshEvent) {
		if (GroupListsRefeshEvent != null && GroupListsRefeshEvent.isNeedRefresh()) {
			// 刷新
			isRes = true;
			LogUtils.i("otto", "开始刷新，GroupListsRefeshEvent");
		}


	}
	{
//
		// @Override
		// protected void onCreate(Bundle savedInstanceState) {
		// super.onCreate(savedInstanceState);
		// setContentView(R.layout.fragment_groups);
		//
		// instance = this;
		// inputMethodManager =
		// (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		// grouplist = EMGroupManager.getInstance().getAllGroups();
		// groupListView = (ListView)findViewById(R.id.list);
		//
		//
		//
		// groupAdapter = new GroupAdapter(this, 1, grouplist);
		// groupListView.setAdapter(groupAdapter);
		// groupListView.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view, int
		// position,
		// long id) {
		// if (position == groupAdapter.getCount() - 1) {
		// //新建群聊
		// startActivityForResult(new Intent(GroupsActivity.this,
		// NewGroupActivity.class), 0);
		// } else {
		//
		// //进入群聊
		// Intent intent = new Intent(GroupsActivity.this, ChatActivity.class);
		// // it is group chat
		// intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
		// intent.putExtra("groupId",
		// groupAdapter.getItem(position-1).getGroupId());
		// startActivityForResult(intent, 0);
		// }
		// }
		//
		// });
		//
		//
		// //
		// //
		// //final EditText query = (EditText) findViewById(R.id.query);
		// // final ImageButton clearSearch = (ImageButton)
		// findViewById(R.id.search_clear);
		// // query.addTextChangedListener(new TextWatcher() {
		// // public void onTextChanged(CharSequence s, int start, int before,
		// int
		// count) {
		// //// groupAdapter.getFilter().filter(s);
		// //
		// //// grouplist = EMGroupManager.getInstance().getAllGroups();
		// //// Iterator<EMGroup> it = grouplist.iterator();
		// //// while (it.hasNext()) {
		// //// EMGroup value = it.next();
		// ////
		// ////
		// //// if (!value.getGroupName().contains(s)){
		// //// it.remove();
		// //// }
		// //// }
		// //
		// //
		// //
		// //
		// // groupAdapter.notifyDataSetChanged();
		// //
		// //
		// //
		// //
		// // if (s.length() > 0) {
		// // clearSearch.setVisibility(View.VISIBLE);
		// // } else {
		// // clearSearch.setVisibility(View.INVISIBLE);
		// // }
		// // LogUtils.i(TAG, "onTextChanged,CharSequence"+s);
		// // }
		// //
		// // public void beforeTextChanged(CharSequence s, int start, int
		// count,
		// int after) {
		// // LogUtils.i(TAG, "beforeTextChanged,CharSequence"+s);
		// //
		// // }
		// //
		// // public void afterTextChanged(Editable s) {
		// // LogUtils.i(TAG, "afterTextChanged,Editable"+s);
		// // }
		// // });
		// // clearSearch.setOnClickListener(new OnClickListener() {
		// // @Override
		// // public void onClick(View v) {
		// // query.getText().clear();
		// // }
		// // });
		// //
		//
		//
		//
		//
		//
		//
		//
		//
		// groupListView.setOnTouchListener(new OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// if (getWindow().getAttributes().softInputMode !=
		// WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
		// if (getCurrentFocus() != null)
		// inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
		// InputMethodManager.HIDE_NOT_ALWAYS);
		// }
		// return false;
		// }
		// });
		//
		//

		// }
	}
}
