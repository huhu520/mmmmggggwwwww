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
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;



import android.content.ClipData.Item;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContact;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMMessage;
import com.easemob.chat.MessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.util.DateUtils;
import com.fz.core.image.AsyncImageLoader;
import com.google.gson.JsonObject;
import com.hx.hxchat.Constant;
import com.hx.hxchat.domain.User;
import com.hx.hxchat.domain.UserFriend;
import com.hx.hxchat.domain.UserFriendBean.Items;
import com.hx.hxchat.utils.CommonUtils;
import com.hx.hxchat.utils.LoadUserAvatar;
import com.hx.hxchat.utils.SmileUtils;
import com.hx.hxchat.utils.UserUtils;
import com.hx.hxchat.utils.LoadUserAvatar.ImageDownloadedCallBack;
import com.mgw.member.R;
import com.mgw.member.manager.BaseApplication;
import com.mgw.member.uitls.FileUtils;
import com.mgw.member.uitls.LogUtils;

/**
 * 聊天记录adpater
 * 
 */
public class ChatHistoryAdapter extends ArrayAdapter<EMContact> {

	private static final String TAG = ChatHistoryAdapter.class.getSimpleName();
	private LayoutInflater inflater;
	public List<Items> m_User_s = null;
	private List<EMContact> emContacts;
	private LoadUserAvatar avatarLoader;
	private Context context;
	private List<EMContact> top_list;

	public ChatHistoryAdapter(Context context, int textViewResourceId, List<EMContact> objects, List<Items> User_s, List<EMContact> top_lists) {
		super(context, textViewResourceId, objects);
		inflater = LayoutInflater.from(context);
		m_User_s = User_s;
		this.emContacts = objects;
		this.context = context;
		this.top_list = top_lists;
		avatarLoader = new LoadUserAvatar(context, FileUtils.getCacheDir());
	}

	public int getCount() {
		if (emContacts == null)
			return 0;
		return emContacts.size() + top_list.size();
	}

	@Override
	public EMContact getItem(int position) {

		if (position < top_list.size()) {
			return top_list.get(position);

		} else {
			return emContacts.get(position - top_list.size());
		}

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.row_chat_history, parent, false);
		}
		ViewHolder holder = (ViewHolder) convertView.getTag();
		if (holder == null) {
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.unreadLabel = (TextView) convertView.findViewById(R.id.unread_msg_number);
			holder.message = (TextView) convertView.findViewById(R.id.message);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
			holder.avatar1 = (ImageView) convertView.findViewById(R.id.iv_avatar1);
			holder.avatar2 = (ImageView) convertView.findViewById(R.id.iv_avatar2);
			holder.avatar3 = (ImageView) convertView.findViewById(R.id.iv_avatar3);
			holder.avatar4 = (ImageView) convertView.findViewById(R.id.iv_avatar4);
			holder.rl_group_avatar = (RelativeLayout) convertView.findViewById(R.id.rl_group_avatar);
			holder.msgState = convertView.findViewById(R.id.msg_state);
			holder.list_item_layout = (RelativeLayout) convertView.findViewById(R.id.list_item_layout);
			convertView.setTag(holder);
		}
		if (position % 2 == 0) {
			holder.list_item_layout.setBackgroundResource(R.drawable.mm_listitem);
		} else {
			holder.list_item_layout.setBackgroundResource(R.drawable.mm_listitem_grey);
		}

		EMContact user = getItem(position);


		if (user != null) {


			if (getItem(position) instanceof EMGroup) {
				holder.name.setText(((EMGroup) getItem(position)).getGroupName());

				// 群聊消息，显示群聊头像

				holder.rl_group_avatar.setVisibility(View.VISIBLE);
				holder.avatar.setVisibility(View.GONE);
				List<String> members = ((EMGroup) user).getMembers();
				setGroupMembersAvs(members, holder);
			
			} else {
				holder.rl_group_avatar.setVisibility(View.GONE);
				holder.avatar.setVisibility(View.VISIBLE);
				EMContact item = getItem(position);
				User user2 = BaseApplication.getApplication().getContactList().get(item.getUsername());
				holder.name.setText(user2==null?getItem(position).getNick():UserUtils.getCommentOrNick(user2,true));
				showUserAvatar(holder.avatar,UserUtils.getUserInfo(getItem(position).getUsername()).getAvatar());

			}

			String username = user.getUsername();
			// 获取与此用户/群组的会话
			EMConversation conversation = EMChatManager.getInstance().getConversation(username);
			// holder.name.setText(user.getNick() != null ? user.getNick() :
			// username);
			if (conversation.getUnreadMsgCount() > 0) {
				// 显示与此用户的消息未读数
				holder.unreadLabel.setText(String.valueOf(conversation.getUnreadMsgCount()));
				holder.unreadLabel.setVisibility(View.VISIBLE);
			} else {
				holder.unreadLabel.setVisibility(View.INVISIBLE);
			}

			if (conversation.getMsgCount() != 0) {
				// 把最后一条消息的内容作为item的message内容
				EMMessage lastMessage = conversation.getLastMessage();
				
				if(lastMessage.getStringAttribute(Constant.SHARE_PRODUCT_LINK, "").equals(Constant.SHARE_PRODUCT_LINK)){
					
					
					TextMessageBody txtBody = (TextMessageBody) lastMessage.getBody();
					
					String jsom = txtBody.getMessage();
					try {	
					JSONObject jsonObject=new JSONObject(jsom);
			
						if(jsonObject!=null&&!jsonObject.getString("title").equals("")){
							holder.message.setText("[分享]"+jsonObject.getString("title"));
						}else{
							holder.message.setText("[分享]"+jsom);
							
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}else{
					holder.message.setText(SmileUtils.getSmiledText(getContext(), CommonUtils.getMessageDigest(lastMessage, (this.getContext()))), BufferType.SPANNABLE);
					
				}
				

				holder.time.setText(DateUtils.getTimestampString(new Date(lastMessage.getMsgTime())));
				
				
				if (lastMessage.direct == EMMessage.Direct.SEND && lastMessage.status == EMMessage.Status.FAIL) {
					holder.msgState.setVisibility(View.VISIBLE);
				} else {
					holder.msgState.setVisibility(View.GONE);
				}
			}
			
			
			
			
		}
		
		 if (position < top_list.size()) {
	            // 加入删除后
	            holder.list_item_layout
	            .setBackgroundColor(0xFFF5FFF1);

	        }
		return convertView;
	}

	private void setGroupMembersAvs(List<String> members, ViewHolder holder) {
		if (members == null || members.size() == 0) {
			return;
		}

		String[] avatars = new String[members.size()];
		avatars = getGroupAvs(members, avatars);

		switch (members.size()) {
		case 1:
			showUserAvatar(holder.avatar1, avatars[0]);
			break;
		case 2:
			showUserAvatar(holder.avatar1, avatars[0]);

			showUserAvatar(holder.avatar2, avatars[1]);
			break;
		case 3:

			showUserAvatar(holder.avatar1, avatars[0]);
			showUserAvatar(holder.avatar2, avatars[1]);
			showUserAvatar(holder.avatar3, avatars[2]);
			break;
		case 4:

			showUserAvatar(holder.avatar1, avatars[0]);
			showUserAvatar(holder.avatar2, avatars[1]);
			showUserAvatar(holder.avatar3, avatars[2]);
			showUserAvatar(holder.avatar4, avatars[3]);

			break;

		default:
			showUserAvatar(holder.avatar1, avatars[0]);
			showUserAvatar(holder.avatar2, avatars[1]);
			showUserAvatar(holder.avatar3, avatars[2]);
			showUserAvatar(holder.avatar4, avatars[3]);

			break;
		}

	}

	private static class ViewHolder {
		/** 和谁的聊天记录 */
		TextView name;
		/** 消息未读数 */
		TextView unreadLabel;
		/** 最后一条消息的内容 */
		TextView message;
		/** 最后一条消息的时间 */
		TextView time;
		/** 用户头像 */
		ImageView avatar;
		/** qun头像 */
		ImageView avatar1;
		/** qun头像 */
		ImageView avatar2;
		/** qun头像 */
		ImageView avatar3;
		/** qun头像 */
		ImageView avatar4;
		/** qun头像 */
		RelativeLayout rl_group_avatar;
		/** 最后一条消息的发送状态 */
		View msgState;
		/** 整个list中每一行总布局 */
		RelativeLayout list_item_layout;

	}

	/**
	 * 鏍规嵁娑堟伅鍐呭鍜屾秷鎭被鍨嬭幏鍙栨秷鎭唴瀹规彁绀�
	 * 
	 * @param message
	 * @param context
	 * @return
	 */
	public String name(int index) {
		return m_User_s.get(index).getNickName();
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
}
