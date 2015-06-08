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
package com.hx.hxchat.domain;

import java.util.List;
import java.util.Map;

import com.easemob.chat.EMContact;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.hx.hxchat.domain.UserFriendBean.Items;

public class EMConvations extends EMConversation {

	public EMConvations(String username) {
		super(username);
	}

	public EMConvations(String username, boolean isGroup) {
		super(username, isGroup);
	}

	private boolean isGroup;
	private List<EMMessage> allMessages;
	private int allMsgCount;
	private int extField;
	private EMMessage lastMessage;
	private int msgCount;
	private int unreadMsgCount;
	private String userName;

	public List<EMMessage> getAllMessages() {
		return allMessages;
	}

	public void setAllMessages(List<EMMessage> allMessages) {
		this.allMessages = allMessages;
	}

	public int getAllMsgCount() {
		return allMsgCount;
	}

	public void setAllMsgCount(int allMsgCount) {
		this.allMsgCount = allMsgCount;
	}

	public int getExtField() {
		return extField;
	}

	public void setExtField(int extField) {
		this.extField = extField;
	}

	public EMMessage getLastMessage() {
		return lastMessage;
	}

	public void setLastMessage(EMMessage lastMessage) {
		this.lastMessage = lastMessage;
	}

	public int getMsgCount() {
		return msgCount;
	}

	public void setMsgCount(int msgCount) {
		this.msgCount = msgCount;
	}

	public int getUnreadMsgCount() {
		return unreadMsgCount;
	}

	public void setUnreadMsgCount(int unreadMsgCount) {
		this.unreadMsgCount = unreadMsgCount;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public boolean isGroup() {
		return isGroup;
	}

	public void setGroup(boolean isGroup) {
		this.isGroup = isGroup;
	}

}
