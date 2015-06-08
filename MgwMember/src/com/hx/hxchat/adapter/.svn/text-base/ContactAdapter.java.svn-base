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
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.easemob.util.HanziToPinyin;
import com.hx.hxchat.Constant;
import com.hx.hxchat.domain.User;
import com.hx.hxchat.utils.UserUtils;
import com.mgw.member.R;
import com.mgw.member.manager.BaseApplication;

/**
 * 简单的好友Adapter实现
 */
public class ContactAdapter extends ArrayAdapter<User> implements SectionIndexer {

	List<String> list;
	List<User> userList;
	// 用于过滤
	List<User> copyUserList;

	private final LayoutInflater layoutInflater;
	private SparseIntArray positionOfSection;
	private SparseIntArray sectionOfPosition;

	private final int res;
	private int sLenth;

	public static String imagePath; // 头像路劲

	public ContactAdapter(Context context, int resource, List<User> objects) {
		super(context, resource, objects);
		this.res = resource;
		this.userList = objects;

		copyUserList = new ArrayList<User>();
		copyUserList.addAll(objects);

		layoutInflater = LayoutInflater.from(context);

	}

	//
	// @Override
	// public int getViewTypeCount() {
	//
	// return 2;
	// }
	//
	// @Override
	// public int getItemViewType(int position) {
	//
	// return position == 0 ? 0 : 1;
	// }
	private static class ViewHolder {

		/** 头像 */
		ImageView avatar;
		/** 未读消息 */
		TextView unreadMsgView;
		/** 消息 */
		TextView nameTextview;
		/** 头标签 */
		TextView tvHeader;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(res, null);
			holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
			holder.unreadMsgView = (TextView) convertView.findViewById(R.id.unread_msg_number);
			holder.nameTextview = (TextView) convertView.findViewById(R.id.name);
			holder.tvHeader = (TextView) convertView.findViewById(R.id.header);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();

		}

		User user = getItem(position);

		if (user != null) {

			Log.d("ContactAdapter", position + "");

			// 设置nick，demo里不涉及到完整user，用username代替nick显示

			String username = user.getNick();

			SetHead(user);

			String header = user.getHeader();
			// && !header.equals(firstHeader)
			String firstHeader = getItem(position).getHeader();
			if (position == 0 || header != null && (position != 0 && !header.equals(getItem(position - 1).getHeader()))) {
				if ("".equals(header)) {
					holder.tvHeader.setVisibility(View.GONE);
				} else {
					holder.tvHeader.setVisibility(View.VISIBLE);
					holder.tvHeader.setText(header);
				}
			} else {
				holder.tvHeader.setVisibility(View.GONE);
			}

			// 显示申请与通知item
			if (username.equals("申请与通知")) {

				holder.nameTextview.setText("新的朋友");
				holder.avatar.setImageResource(R.drawable.new_friends_icon);

				if (user.getUnreadMsgCount() > 0) {
					// int a = user.getUnreadMsgCount();
					holder.unreadMsgView.setVisibility(View.VISIBLE);
					holder.unreadMsgView.setText(user.getUnreadMsgCount() + "");
				} else {
					holder.unreadMsgView.setVisibility(View.INVISIBLE);
				}
			} else if (username.equals("群聊")) {
				// 群聊item
				holder.nameTextview.setText("群聊");
				holder.avatar.setImageResource(R.drawable.groups_icon);
			} else {
				holder.nameTextview.setText( UserUtils.getCommentOrNick(user,true));
				UserUtils.setUserAvatar(getContext(), user.getUsername(), holder.avatar);// 10655274
				if (holder.unreadMsgView != null)
					holder.unreadMsgView.setVisibility(View.INVISIBLE);

			}
		}
		return convertView;
	}


	private void SetHead(User puser) {
		String username = puser.getUsername();
		String headerName = null;
		if (!TextUtils.isEmpty(puser.getNick())) {

			headerName = puser.getNick();

		} else {

			headerName = puser.getUsername();

		}
		if (username.equals(Constant.NEW_FRIENDS_USERNAME)) {

		}
		if (username.equals(Constant.NEW_FRIENDS_USERNAME) || username.equals(Constant.GROUP_USERNAME)) {

			puser.setHeader("");

		} else if (Character.isDigit(headerName.charAt(0))) {
			puser.setHeader("#");
		} else {
			try {
				puser.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target.substring(0, 1).toUpperCase());
			} catch (Exception ex) {
				puser.setHeader("#");
			}
			char header = puser.getHeader().toLowerCase().charAt(0);
			if (header < 'a' || header > 'z') {
				puser.setHeader("#");
			}
		}

	}

	@Override
	public User getItem(int position) {
		// return position == 0 ? new User() : super.getItem(position - 1);
		return userList.get(position);
	}

	@Override
	public int getCount() {
		// 有搜索框，cout+1
		return userList.size();
	}

	@Override
	public int getPositionForSection(int section) {
		return positionOfSection.get(section);
	}

	@Override
	public int getSectionForPosition(int position) {
		return sectionOfPosition.get(position);
	}

	@Override
	public Object[] getSections() {
		positionOfSection = new SparseIntArray();
		sectionOfPosition = new SparseIntArray();
		int count = getCount();
		List<String> list = new ArrayList<String>();
		list.add(getContext().getString(R.string.search_header));
		positionOfSection.put(0, 0);
		sectionOfPosition.put(0, 0);
		for (int i = 0; i < count; i++) {

			String letter = getItem(i).getHeader();
			System.err.println("contactadapter getsection getHeader:" + letter + " name:" + getItem(i).getUsername());
			int section = list.size() - 1;
			if (list.get(section) != null && !list.get(section).equals(letter)) {
				list.add(letter);
				section++;
				positionOfSection.put(section, i);
			}
			sectionOfPosition.put(i, section);
		}
		return list.toArray(new String[list.size()]);
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		copyUserList.clear();
		copyUserList.addAll(userList);

	}
}
