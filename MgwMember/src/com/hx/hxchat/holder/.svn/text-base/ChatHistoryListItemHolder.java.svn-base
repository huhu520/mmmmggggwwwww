package com.hx.hxchat.holder;

import java.util.Date;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.DateUtils;
import com.hx.hxchat.Constant;
import com.hx.hxchat.domain.TopUser;
import com.hx.hxchat.domain.User;
import com.hx.hxchat.utils.SmileUtils;
import com.hx.hxchat.utils.UserUtils;
import com.mgw.member.R;
import com.mgw.member.manager.BaseApplication;
import com.mgw.member.uitls.UIUtils;
import com.squareup.picasso.Picasso;

/**
 * 视图展示（不负责数据加载）
 * 
 * @author huyan
 * 
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
@SuppressLint("NewApi")
public class ChatHistoryListItemHolder extends BaseHolder<EMConversation> {

	public ChatHistoryListItemHolder() {
		super();
	}

	@Override
	public void recycle() {

	}

	/** 和谁的聊天记录 */
	private TextView tv_name;
	/** 消息未读数 */
	private TextView tv_unread;
	/** 最后一条消息的内容 */
	private TextView tv_content;
	/** 最后一条消息的时间 */
	private TextView tv_time;
	/** 用户头像 */
	private ImageView iv_avatar;
	/** qun头像 */
	private ImageView avatar1;
	/** qun头像 */
	private ImageView avatar2;
	/** qun头像 */
	private ImageView avatar3;
	/** qun头像 */
	private ImageView avatar4;
	/** qun头像 */
	private RelativeLayout rl_group_avatar;
	/** 最后一条消息的发送状态 */
	private View msgState;
	/** 整个list中每一行总布局 */
	private RelativeLayout list_item_layout;

	@Override
	protected View initView() {
		View convertView = UIUtils.inflate(R.layout.row_chat_history);

		tv_name = (TextView) convertView.findViewById(R.id.name);
		tv_unread = (TextView) convertView.findViewById(R.id.unread_msg_number);
		tv_content = (TextView) convertView.findViewById(R.id.message);
		tv_time = (TextView) convertView.findViewById(R.id.time);
		iv_avatar = (ImageView) convertView.findViewById(R.id.avatar);
		avatar1 = (ImageView) convertView.findViewById(R.id.iv_avatar1);
		avatar2 = (ImageView) convertView.findViewById(R.id.iv_avatar2);
		avatar3 = (ImageView) convertView.findViewById(R.id.iv_avatar3);
		avatar4 = (ImageView) convertView.findViewById(R.id.iv_avatar4);
		rl_group_avatar = (RelativeLayout) convertView
				.findViewById(R.id.rl_group_avatar);
		msgState = convertView.findViewById(R.id.msg_state);
		list_item_layout = (RelativeLayout) convertView
				.findViewById(R.id.list_item_layout);

		return convertView;
	}

	@Override
	public void setData(EMConversation data) {
		super.setData(data);
	}

	@Override
	public void refreshView() {
		// 获取与此用户/群组的会话
		EMConversation conversation = getData();
		String nick;
		// 获取用户username或者群组groupid
		final String userid = conversation.getUserName();
		List<EMGroup> groups = EMGroupManager.getInstance().getAllGroups();
		if (conversation.isGroup()) {
			iv_avatar.setVisibility(View.GONE);
			rl_group_avatar.setVisibility(View.VISIBLE);
			EMGroup group = EMGroupManager.getInstance().getGroup(userid);
			tv_name.setText(group != null ? group.getGroupName() : conversation
					.getUserName());

			String groupsicons = BaseApplication.getApplication()
					.GetGroupsDao().getGropsIcon(userid);

			if (groupsicons != null && !"".equals(groupsicons)) {
				String[] split = groupsicons.split("#");
				UserUtils.setGroupsIcon(avatar1, avatar2, avatar3, avatar4,
						split);
			}
		} else {
			iv_avatar.setVisibility(View.VISIBLE);
			rl_group_avatar.setVisibility(View.GONE);
			tv_name.setText(conversation.getUserName() + "gere");
			// 从好友列表中加载该用户的资料
			User user = UserUtils.getUserInfo(userid);
			if (user != null) {
				nick = user.getNick();
				
				String comment = BaseApplication.getApplication().GetUserDao().getComment(user.getUsername());
				
				String avatar = user.getAvatar();
				// 显示昵称
				tv_name.setText(comment!=null?comment+"["+nick+"]":nick);
				// 显示头像
				UserUtils.showUserAvatar(iv_avatar, avatar);
			} else {
				EMMessage message = conversation.getLastMessage();
				if (message.direct == EMMessage.Direct.RECEIVE) {
					try {
						nick = message.getStringAttribute("myUserNick");
						String avatar = message
								.getStringAttribute("myUserAvatar");
						// 显示昵称
						tv_name.setText(nick);
						// 显示头像
						UserUtils.showUserAvatar(iv_avatar, avatar);

					} catch (EaseMobException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {
					try {
						nick = message.getStringAttribute("toUserNick");
						String avatar = message
								.getStringAttribute("toUserAvatar");
						// 显示昵称
						tv_name.setText(nick);
						// 显示头像
						UserUtils.showUserAvatar(iv_avatar, avatar);

					} catch (EaseMobException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

		if (conversation.getUnreadMsgCount() > 0) {
			// 显示与此用户的消息未读数
			tv_unread.setText(String.valueOf(conversation.getUnreadMsgCount()));
			tv_unread.setVisibility(View.VISIBLE);
		} else {
			tv_unread.setVisibility(View.INVISIBLE);
		}

		if (conversation.getMsgCount() != 0) {
			// 把最后一条消息的内容作为item的message内容
			EMMessage lastMessage = conversation.getLastMessage();
			tv_content.setText(
					SmileUtils.getSmiledText(
							UIUtils.getContext(),
							UserUtils.getMessageDigest(lastMessage,
									UIUtils.getContext())),
					BufferType.SPANNABLE);
			tv_time.setText(DateUtils.getTimestampString(new Date(lastMessage
					.getMsgTime())));
			if (lastMessage.direct == EMMessage.Direct.SEND
					&& lastMessage.status == EMMessage.Status.FAIL) {
				msgState.setVisibility(View.VISIBLE);
			} else {
				msgState.setVisibility(View.GONE);
			}
		}

		Map<String, TopUser> topUserList = BaseApplication.getApplication()
				.getTopUserList();
		if (topUserList.containsKey(conversation.getUserName())) {
			list_item_layout.setBackgroundResource(R.drawable.mm_listtopitem);

		}else{
			
			if (getPosition() % 2 == 0) {
				list_item_layout.setBackgroundResource(R.drawable.mm_listitem);
			} else {
				list_item_layout.setBackgroundResource(R.drawable.mm_listitem_grey);
			}

		}
	}

}
