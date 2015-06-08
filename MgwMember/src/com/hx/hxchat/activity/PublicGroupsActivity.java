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
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupInfo;
import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;
import com.hx.hxchat.adapter.DefaultAdapter;
import com.hx.hxchat.domain.GroupInfo;
import com.hx.hxchat.domain.User;
import com.hx.hxchat.holder.BaseHolder;
import com.hx.hxchat.holder.GroupListInfoHolder;
import com.hx.hxchat.holder.PublicGroupsListItemHolder;
import com.hx.hxchat.holder.protocol.GroupsListProtocol;
import com.hx.hxchat.holder.protocol.PublicGroupsListProtocol;
import com.hx.hxchat.utils.LoadUserAvatar;
import com.hx.hxchat.utils.UserUtils;
import com.hx.hxchat.utils.LoadUserAvatar.ImageDownloadedCallBack;
import com.mgw.member.R;
import com.mgw.member.ottoEvent.BusProvider;
import com.mgw.member.ui.widget.LoadingPage;
import com.mgw.member.ui.widget.LoadingPage.LoadResult;
import com.mgw.member.uitls.FileUtils;
import com.mgw.member.uitls.LogUtils;
import com.mgw.member.uitls.UIUtils;

public class PublicGroupsActivity extends HXBaseActivity implements OnClickListener {
	public static final String TAG = PublicGroupsActivity.class.getSimpleName();
	private ProgressBar pb;
	private ListView listView;
	private EditText query;
	private ImageButton clearSearch;
	private PublicGroupsAdapter adapter;

	// 各区域布局及其holder
	private FrameLayout fl_list;
	private GroupListInfoHolder GInfoHolder;
	private InputMethodManager inputMethodManager;
	private LoadingPage page;

	/** 初始化布局 */
	protected void initViewBase() {
		BusProvider.getInstance().register(this);
		page = new LoadingPage(this) {
			@Override
			public LoadResult load() {
				return PublicGroupsActivity.this.load();
			}

			@Override
			public View createLoadedView() {
				return PublicGroupsActivity.this.createLoadedView();
			}

			@Override
			protected View createLoadingView() {
				// TODO Auto-generated method stub
				View view = UIUtils.inflate(R.layout.activity_public_groups);
				FrameLayout findViewById = (FrameLayout) view.findViewById(R.id.fl_content);
				View viewloading = super.createLoadingView();
				view.findViewById(R.id.back).setOnClickListener(PublicGroupsActivity.this);
				findViewById.addView(viewloading);
				return view;

			}
			@Override
			protected View createErrorView() {
				View view = UIUtils.inflate(R.layout.activity_public_groups);
				FrameLayout findViewById = (FrameLayout) view.findViewById(R.id.fl_content);
				View viewloading = super.createErrorView();
				view.findViewById(R.id.back).setOnClickListener(PublicGroupsActivity.this);
				findViewById.addView(viewloading);
				return view;
			}
			
			@Override
			protected View createEmptyView() {
				// TODO Auto-generated method stub
				View view = UIUtils.inflate(R.layout.activity_public_groups);
				FrameLayout findViewById = (FrameLayout) view.findViewById(R.id.fl_content);
				View viewloading = super.createEmptyView();
				view.findViewById(R.id.back).setOnClickListener(PublicGroupsActivity.this);
				findViewById.addView(viewloading);
				return view;
			}
			
		};
		setContentView(page);
		page.show();
	}

	protected View createLoadedView() {
		View view = UIUtils.inflate(R.layout.activity_public_groups);
		
		
		listView = (ListView) view.findViewById(R.id.list);
		adapter = new PublicGroupsAdapter(this, 1, mDatas, listView);
		view.findViewById(R.id.back).setOnClickListener(this);
		listView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		return view;
	}

	private class PublicGroupsAdapter extends DefaultAdapter<GroupInfo> {
		private Context context;
		public PublicGroupsAdapter(Context context, int resource,
				List<GroupInfo> datas, AbsListView listView) {
			super(context, resource, datas, listView);
			
			this.context=context;
		}

		@Override
		protected BaseHolder<GroupInfo> getHolder() {
			return new PublicGroupsListItemHolder();
		}
		
		@Override
		public void onItemClickInner(int position) {
		
			 EMGroupInfo info = new EMGroupInfo(getItem(position).getGroupId(),
			 getItem(position).getGroupName());
			
			 context.startActivity(new Intent(PublicGroupsActivity.this,
			 GroupSimpleDetailActivity.class).putExtra("groupinfo", info));
			
			
		}

	}

	private List<GroupInfo> mDatas;

	protected LoadResult load() {
		PublicGroupsListProtocol protocol = new PublicGroupsListProtocol();
		protocol.setPackageName(TAG);
		mDatas = protocol.load(0);
		if (mDatas == null) {
			return LoadResult.ERROR;
		}
		return LoadResult.SUCCEED;
	}

	private boolean isRes = false;

	@Override
	public void onResume() {
		super.onResume();
		// if (isRes) {
		// load();
		// GInfoHolder.setData(mDatas);
		// isRes = false;
		// }
	}

	// @Override
	// protected void onCreate(Bundle savedInstanceState) {
	// super.onCreate(savedInstanceState);
	// setContentView(R.layout.activity_public_groups);
	//
	// pb = (ProgressBar) findViewById(R.id.progressBar);
	// listView = (ListView) findViewById(R.id.list);
	//
	// new Thread(new Runnable() {
	// public void run() {
	// try {
	// // 从服务器获取所用公开的群聊
	// final List<EMGroupInfo> groupsList =
	// EMGroupManager.getInstance().getAllPublicGroupsFromServer();
	//
	// final List<EMGroup> group = new ArrayList<EMGroup>();
	// for (EMGroupInfo dd : groupsList) {
	//
	// LogUtils.i(TAG, "groupsList:id" + dd.getGroupId());
	// EMGroup groupFromServer =
	// EMGroupManager.getInstance().getGroupFromServer(dd.getGroupId());
	// group.add(groupFromServer);
	//
	// }
	//
	// runOnUiThread(new Runnable() {
	//
	// public void run() {
	// pb.setVisibility(View.INVISIBLE);
	// adapter = new GroupsAdapter(PublicGroupsActivity.this, 1, group);
	// listView.setAdapter(adapter);
	//
	// // 设置item点击事件
	// listView.setOnItemClickListener(new OnItemClickListener() {
	//
	// @Override
	// public void onItemClick(AdapterView<?> parent, View view, int position,
	// long id) {
	//
	// EMGroupInfo info = new
	// EMGroupInfo(adapter.getItem(position).getGroupId(),
	// adapter.getItem(position).getGroupName());
	//
	// startActivity(new Intent(PublicGroupsActivity.this,
	// GroupSimpleDetailActivity.class).putExtra("groupinfo", info));
	// }
	// });
	//
	// // 搜索框
	// query = (EditText) findViewById(R.id.query);
	// // 搜索框中清除button
	// clearSearch = (ImageButton) findViewById(R.id.search_clear);
	// query.addTextChangedListener(new TextWatcher() {
	// public void onTextChanged(CharSequence s, int start, int before, int
	// count) {
	//
	// adapter.getFilter().filter(s);
	// if (s.length() > 0) {
	// clearSearch.setVisibility(View.VISIBLE);
	// } else {
	// clearSearch.setVisibility(View.INVISIBLE);
	// }
	// }
	//
	// public void beforeTextChanged(CharSequence s, int start, int count, int
	// after) {
	// }
	//
	// public void afterTextChanged(Editable s) {
	// }
	// });
	// clearSearch.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// query.getText().clear();
	//
	// }
	// });
	// }
	// });
	// } catch (EaseMobException e) {
	// e.printStackTrace();
	// runOnUiThread(new Runnable() {
	// public void run() {
	// pb.setVisibility(View.INVISIBLE);
	//
	// }
	// });
	// }
	// }
	// }).start();

	// }



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;

		default:
			break;
		}
		
	}

}
