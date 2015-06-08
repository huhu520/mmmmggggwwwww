package com.hx.hxchat.holder.protocol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.SystemClock;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.exceptions.EaseMobException;
import com.hx.hxchat.domain.GroupInfo;
import com.hx.hxchat.domain.TopUser;
import com.hx.hxchat.otto.ChatHistoryneedRefeshEvent;
import com.hx.hxchat.utils.UserUtils;
import com.mgw.member.manager.BaseApplication;
import com.mgw.member.manager.ThreadManager;
import com.mgw.member.ottoEvent.BusProvider;
import com.mgw.member.uitls.LogUtils;
import com.mgw.member.uitls.StringUtils;
import com.mgw.member.uitls.UIUtils;
import com.squareup.otto.Produce;

/**
 * 数据加载器 （只负责数据加载，逻辑和视图无关）
 * 
 * @author Administrator
 * 
 */
public class ChatHistoryListProtocol extends BaseProtocol<List<EMConversation>> {
	private String mPackageName = "";

	public ChatHistoryListProtocol(Context context) {
		// TODO Auto-generated constructor stub
	}

	public void setPackageName(String packageName) {
		mPackageName = packageName;
	}

	@Override
	protected String getKey() {
		return "detail";
	}

	@Override
	protected String getParames() {
		if (StringUtils.isEmpty(mPackageName)) {
			return super.getParames();
		} else {
			return "&packageName=" + mPackageName;
		}
	}

	@Override
	protected List<EMConversation> parseFromJson(String json) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EMConversation> load(int index) {

		normal_list.clear();
		
		List<EMConversation> loadConversationsWithRecentChat = loadConversationsWithRecentChat();
		normal_list.addAll(top_list);
		normal_list.addAll(loadConversationsWithRecentChat);
		checkGroupsIcon(normal_list);

		return normal_list;
	}

	private Map<String, TopUser> topMap;
	private List<EMConversation> normal_list = new ArrayList<EMConversation>();
	private List<EMConversation> top_list = new ArrayList<EMConversation>();

	/**
	 * 获取所有会话
	 * 
	 * @param context
	 * @return +
	 */
	private List<EMConversation> loadConversationsWithRecentChat() {
		topMap = BaseApplication.getApplication().getTopUserList();
		// 获取所有会话，包括陌生人
		Hashtable<String, EMConversation> conversations = EMChatManager.getInstance().getAllConversations();

		List<EMConversation> list = new ArrayList<EMConversation>();
		List<EMConversation> topList1 = new ArrayList<EMConversation>();

		// 置顶列表再刷新一次

		// 过滤掉messages seize为0的conversation
		for (EMConversation conversation : conversations.values()) {

//			if (conversation.getAllMessages().size() != 0) {
			if (conversation.getAllMessages().size() >= 0) {
				// 不在置顶列表里面
				if (!topMap.containsKey(conversation.getUserName())) {
					list.add(conversation);
				} else {
					// 在置顶列表里面
					topList1.add(conversation);
				}

			}

		}
		top_list.clear();
		top_list.addAll(topList1);
		// 排序
		sortConversationByLastChatTime(list);
		sortConversationByLastChatTime(top_list);
		return list;
	}

	/**
	 * 根据最后一条消息的时间排序
	 * 
	 * @param usernames
	 */
	private void sortConversationByLastChatTime(List<EMConversation> conversationList) {
		Collections.sort(conversationList, new Comparator<EMConversation>() {
			@Override
			public int compare(final EMConversation con1, final EMConversation con2) {

				EMMessage con2LastMessage = con2.getLastMessage();
				EMMessage con1LastMessage = con1.getLastMessage();
				
				
				if(con2LastMessage==null||con1LastMessage==null){
					if(con2LastMessage==null){
						return -1;
					}else{
						return con1LastMessage==null?1:-1;
						
					}
					
					
					
				}
					
					
				if (con2LastMessage.getMsgTime() == con1LastMessage.getMsgTime()) {
					return 0;
				} else if (con2LastMessage.getMsgTime() > con1LastMessage.getMsgTime()) {
					return 1;
				} else {
					return -1;
				}
			}

		});
	}

	/**
	 * 检查群是否有头像 没有的话 下载并储存，有的话过
	 * 
	 * @param normal_list2
	 */
	private void checkGroupsIcon(List<EMConversation> normal_list2) {
		ArrayList<String> groupsname = new ArrayList<>();

		for (EMConversation ee : normal_list2) {
			if (ee.isGroup()) {
				String gropsIcon = BaseApplication.getApplication().GetGroupsDao().getGropsIcon(ee.getUserName());
				if (com.hx.hxchat.utils.StringUtils.isEmpty(gropsIcon)) {
					groupsname.add(ee.getUserName());
				}

			}

		}

		loadGroupsIcon(groupsname);

	}

	
	/**
	 * 加载群头像
	 * 
	 * @param groupsname
	 */
	private void loadGroupsIcon(final ArrayList<String> groupsid) {

		if (groupsid.size() == 0) {
			return;
		}
		Runnable runable = new Runnable() {

			@Override
			public void run() {

				for (String id : groupsid) {

					
						try {
							EMGroup	groupFromServer = EMGroupManager.getInstance().getGroupFromServer(id);
							EMGroupManager.getInstance().createOrUpdateLocalGroup(groupFromServer);
					
						
						
						} catch (EaseMobException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							LogUtils.e(e);
						}
						GroupInfo gg=new GroupInfo(id);
						UserUtils.setGroupICon(gg);
						
					
					
					
				}

				UIUtils.post(new Runnable() {
					
					@Override
					public void run() {
						BusProvider.getInstance().register(this);
						//执行完毕：刷新ui
						sendChatHistoryneedRefeshEvent(true);
						BusProvider.getInstance().unregister(this);
					}
				});
				
			}
		};
		runable.run();
//		ThreadManager.getLongPool().execute(runable);

	}

	@Produce
	public ChatHistoryneedRefeshEvent getChatHistoryneedRefeshEvent() {

		ChatHistoryneedRefeshEvent ChatHistoryneedRefeshEvent = new ChatHistoryneedRefeshEvent();
		return ChatHistoryneedRefeshEvent;
	}

	private void sendChatHistoryneedRefeshEvent(boolean b) {
		if (b) {
			LogUtils.i("otto" , "sendChatHistoryneedRefeshEvent");
			ChatHistoryneedRefeshEvent ChatHistoryneedRefeshEvent = getChatHistoryneedRefeshEvent();
			ChatHistoryneedRefeshEvent.setNeedRefresh(b);
			BusProvider.getInstance().post(ChatHistoryneedRefeshEvent);

		}
	}
	
	
	
}
