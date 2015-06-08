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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import cn.sharesdk.framework.authorize.a;

import com.alibaba.fastjson.JSONObject;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;
import com.hx.hxchat.domain.User;
import com.hx.hxchat.utils.LoadUserAvatar;
import com.hx.hxchat.utils.LoadUserAvatar.ImageDownloadedCallBack;
import com.hx.hxchat.utils.UserUtils;
import com.mgw.member.R;
import com.mgw.member.uitls.FileUtils;
import com.mgw.member.uitls.LogUtils;

public class GroupAdapter extends ArrayAdapter<EMGroup> {
	String TAG = GroupAdapter.class.getSimpleName();
	private LayoutInflater inflater;
	private String str;
	// start
	List<EMGroup> grouplist;
	private LoadUserAvatar avatarLoader;

	// end
	public GroupAdapter(Context context, int res, List<EMGroup> groups) {
		super(context, res, groups);
		this.inflater = LayoutInflater.from(context);
		str = context.getResources().getString(R.string.The_new_group_chat);
		avatarLoader = new LoadUserAvatar(context, FileUtils.getCacheDir());
		this.grouplist = groups;
	}

	@Override
	public int getViewTypeCount() {
		return 3;
	}

	@Override
	public int getItemViewType(int position) {
		if (position == 0) {
			return 0;
		} else if (position == getCount() - 1) {
			return 1;
		} else {
			return 2;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LogUtils.i(TAG, "getView");
		if (getItemViewType(position) == 0) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.search_bar_with_padding, null);
			}
			final EditText query = (EditText) convertView.findViewById(R.id.query);
			final ImageButton clearSearch = (ImageButton) convertView.findViewById(R.id.search_clear);
			query.addTextChangedListener(new TextWatcher() {
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					getFilter().filter(s);

					if (s.length() > 0) {
						clearSearch.setVisibility(View.VISIBLE);
					} else {
						clearSearch.setVisibility(View.INVISIBLE);
					}
					LogUtils.i(TAG, "onTextChanged,CharSequence" + s);
				}

				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					LogUtils.i(TAG, "beforeTextChanged,CharSequence" + s);

				}

				public void afterTextChanged(Editable s) {
					LogUtils.i(TAG, "afterTextChanged,Editable" + s);
				}
			});
			clearSearch.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					query.getText().clear();
				}
			});
		} else if (getItemViewType(position) == 1) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.row_add_group, null);
			}
			((ImageView) convertView.findViewById(R.id.avatar)).setImageResource(R.drawable.roominfo_add_btn);
			((TextView) convertView.findViewById(R.id.name)).setText(str);
		} else {

			ViewHolder holder = new ViewHolder();
			EMGroup group = grouplist.get(position - 1);
			String groupId = group.getGroupId();

			int membersNum = 1;
			List<String> members = group.getMembers();

			LogUtils.i(TAG, "members:" + members.toString());
			String[] avatars = new String[members.size()];

			avatars = getGroupAvs(members, avatars);

			if (avatars == null) {
				return convertView = creatConvertView(membersNum);
			}
			membersNum = avatars.length;

			// if (convertView == null) {
			convertView = creatConvertView(membersNum);
			LogUtils.i(TAG, "convertView" + membersNum);
			// }

			// ((TextView)convertView.findViewById(R.id.name)).setText(getItem(position-1).getGroupName());
			String groupName = getItem(position - 1).getGroupName();
			// LogUtils.i(TAG, "groupName="+groupName);
			if (membersNum == 1) {
				holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
				holder.tv_name.setText(groupName);
				holder.iv_avatar1 = (ImageView) convertView.findViewById(R.id.iv_avatar1);
				showUserAvatar(holder.iv_avatar1, avatars[0]);
			} else if (membersNum == 2) {
				holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
				holder.tv_name.setText(groupName);
				holder.iv_avatar1 = (ImageView) convertView.findViewById(R.id.iv_avatar1);

				holder.iv_avatar2 = (ImageView) convertView.findViewById(R.id.iv_avatar2);
				showUserAvatar(holder.iv_avatar1, avatars[0]);
				showUserAvatar(holder.iv_avatar2, avatars[1]);
			} else if (membersNum == 3) {
				holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
				holder.tv_name.setText(groupName);
				holder.iv_avatar1 = (ImageView) convertView.findViewById(R.id.iv_avatar1);

				holder.iv_avatar2 = (ImageView) convertView.findViewById(R.id.iv_avatar2);
				holder.iv_avatar3 = (ImageView) convertView.findViewById(R.id.iv_avatar3);
				showUserAvatar(holder.iv_avatar3, avatars[2]);
				showUserAvatar(holder.iv_avatar1, avatars[0]);
				showUserAvatar(holder.iv_avatar2, avatars[1]);
			} else if (membersNum == 4) {
				holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
				holder.tv_name.setText(groupName);

				holder.iv_avatar1 = (ImageView) convertView.findViewById(R.id.iv_avatar1);
				holder.iv_avatar2 = (ImageView) convertView.findViewById(R.id.iv_avatar2);
				holder.iv_avatar3 = (ImageView) convertView.findViewById(R.id.iv_avatar3);
				holder.iv_avatar4 = (ImageView) convertView.findViewById(R.id.iv_avatar4);

				showUserAvatar(holder.iv_avatar4, avatars[3]);
				showUserAvatar(holder.iv_avatar3, avatars[2]);
				showUserAvatar(holder.iv_avatar1, avatars[0]);
				showUserAvatar(holder.iv_avatar2, avatars[1]);
			} else if (membersNum > 4) {
				holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
				holder.tv_name.setText(groupName);
				holder.iv_avatar1 = (ImageView) convertView.findViewById(R.id.iv_avatar1);

				holder.iv_avatar2 = (ImageView) convertView.findViewById(R.id.iv_avatar2);
				holder.iv_avatar3 = (ImageView) convertView.findViewById(R.id.iv_avatar3);
				holder.iv_avatar4 = (ImageView) convertView.findViewById(R.id.iv_avatar4);
				holder.iv_avatar5 = (ImageView) convertView.findViewById(R.id.iv_avatar5);
				// showUserAvatar(holder.iv_avatar5, avatars[4]);
				showUserAvatar(holder.iv_avatar4, avatars[3]);
				showUserAvatar(holder.iv_avatar3, avatars[2]);
				showUserAvatar(holder.iv_avatar1, avatars[0]);
				showUserAvatar(holder.iv_avatar2, avatars[1]);
			}

			//

		}

		return convertView;
	}

	private String[] getGroupAvs(final List<String> members, String[] avatars) {
		LogUtils.i(TAG, "members" + members.toString() + ",groupName_temp=");
		//
		for (int i = 0; i < members.size(); i++) {

			String uid = members.get(i);
			if (i < 5) {

				User useFromNetAsync = UserUtils.getUserInfo(uid);
				// User useFromNetAsync=null;
				if (useFromNetAsync != null) {

					avatars[i] = useFromNetAsync.getAvatar() != null ? useFromNetAsync.getAvatar() : "";
				}
				LogUtils.i(TAG, "avatars=" + avatars[i] + ",uid=" + uid);
			}
		}
		return avatars;

	}

	private EMGroup getEMGroupAsync(final String groupId) {
		final EMGroup roup1 = null;

		AsyncTask<Void, Void, EMGroup> asy = new AsyncTask<Void, Void, EMGroup>() {

			@Override
			protected EMGroup doInBackground(Void... params) {
				try {
					EMGroup roup11 = EMGroupManager.getInstance().getGroupFromServer(groupId);
					return roup11;

				} catch (EaseMobException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return roup1;
			}

		};

		try {
			EMGroup emGroup = asy.execute().get();
			return emGroup;
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

		// TODO Auto-generated method stub

	}

	@Override
	public int getCount() {
		return super.getCount() + 2;
	}

	private static class ViewHolder {
		TextView tv_name;
		ImageView iv_avatar1;
		ImageView iv_avatar2;
		ImageView iv_avatar3;
		ImageView iv_avatar4;
		ImageView iv_avatar5;
	}

	private void showUserAvatar(ImageView iamgeView, String avatar) {
		final String url_avatar = avatar;
		iamgeView.setTag(url_avatar);
		if (url_avatar != null && !url_avatar.equals("")) {
			Bitmap bitmap = avatarLoader.loadImage(iamgeView, url_avatar, new ImageDownloadedCallBack() {

				public void onImageDownloaded(ImageView imageView, Bitmap bitmap) {
					if (imageView.getTag() == url_avatar) {
						imageView.setImageBitmap(bitmap);

					}
				}

			});
			if (bitmap != null)
				iamgeView.setImageBitmap(bitmap);

		}
	}

	private View creatConvertView(int size) {
		View convertView;
		if (size > 4) {
			size = 4;
		}
		switch (size) {
		case 1:
			convertView = inflater.inflate(R.layout.item_chatroom_1, null);

			break;
		case 2:
			convertView = inflater.inflate(R.layout.item_chatroom_2, null, false);
			break;
		case 3:
			convertView = inflater.inflate(R.layout.item_chatroom_3, null, false);
			break;
		case 4:
			convertView = inflater.inflate(R.layout.item_chatroom_4, null, false);
			break;
		case 5:
			convertView = inflater.inflate(R.layout.item_chatroom_5, null, false);
		default:
			convertView = inflater.inflate(R.layout.item_chatroom_5, null, false);
			break;

		}
		return convertView;
	}
}