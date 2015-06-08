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

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.hx.hxchat.activity.ChatActivity;
import com.hx.hxchat.activity.GroupsActivity;
import com.hx.hxchat.activity.NewGroupActivity;
import com.hx.hxchat.domain.GroupInfo;
import com.hx.hxchat.holder.BaseHolder;
import com.hx.hxchat.holder.GroupAddHolder;
import com.hx.hxchat.holder.GroupListItemHolder;
import com.hx.hxchat.utils.LoadUserAvatar;
import com.mgw.member.R;
import com.mgw.member.uitls.FileUtils;

public class GroupAdaptercopy extends DefaultAdapter<GroupInfo> {
	private int mCurrentPosition;

	public GroupAdaptercopy(Context context, int resource, List<GroupInfo> datas, AbsListView listView) {
		super(context, resource, datas, listView);
		newGroups_title = context.getResources().getString(R.string.The_new_group_chat);
		avatarLoader = new LoadUserAvatar(context, FileUtils.getCacheDir());
		this.grouplist = datas;
		this.context = context;
	}

	@Override
	protected BaseHolder<GroupInfo> getHolder() {
		GroupInfo groupInfo = getData().get(mCurrentPosition);
		if ("new".equals(groupInfo.getGroupId())) {
			return getTitleHolder();
		} else {
			return getItemHolder();
		}

	}

	// 是告诉listView总共有几种样式的item
	@Override
	public int getViewTypeCount() {
		return super.getViewTypeCount() + 1;
	}

	protected BaseHolder<GroupInfo> getTitleHolder() {
		return new GroupAddHolder(context);
	}

	protected BaseHolder<GroupInfo> getItemHolder() {
		return new GroupListItemHolder();

	}

	public int getItemViewTypeInner(int position) {

		GroupInfo groupInfo = getData().get(position);
		if ("new".equals(groupInfo.getGroupId())) {
			return super.getItemViewTypeInner(position) + 1;
		} else {
			return super.getItemViewTypeInner(position);
		}
	};

	String TAG = GroupAdaptercopy.class.getSimpleName();
	private String newGroups_title;
	private List<GroupInfo> grouplist;
	private LoadUserAvatar avatarLoader;
	private Context context;

	@Override
	public void onItemClickInner(int position) {
		List<GroupInfo> data = getData();

		if (position < data.size()) {

			if (position == 0) {

				((GroupsActivity) context).startActivityForResult(new Intent((GroupsActivity) context, NewGroupActivity.class), 0);

			} else {

				// 进入群聊
				Intent intent = new Intent(context, ChatActivity.class);
				// it is group chat
				intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
				intent.putExtra("groupId", data.get(position).getGroupId());
				((GroupsActivity) context).startActivityForResult(intent, 0);

			}

		}

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		mCurrentPosition = position;
		return super.getView(position, convertView, parent);
	}
}