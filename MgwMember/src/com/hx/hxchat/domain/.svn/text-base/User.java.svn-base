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

import com.easemob.chat.EMContact;

/**
 * 环信用户
 * 
 * @author Administrator
 * 
 */
public class User extends EMContact {
	/** 未读count */
	private int unreadMsgCount;
	/** 头标签（由于排序） */
	private String header;
	/** 头像路径 */
	
	
	private String avatar;
	/** 标记是否是代理商 0：是，1：不是 */
	private String referee;
	/** 临时联系人用 是够是缓存 0不是  1是*/
	private String linkcache;


	/** 设置好友备注信息*/
	private String comment;
	
	

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	private String m_str;
	/**
	 * 公司内网含有邀请用户的信息
	 */
	public boolean netContainInvite;

	public void setreferee(String referee) {
		this.referee = referee;
	}

	/**
	 * 获取用户是否是代理商的标签
	 * 
	 * @return
	 */
	public String getreferee() {
		return referee;
	}

	//
	// public String getM_imagePath() {
	// return m_imagePath;
	// }
	//
	// public void setM_imagePath(String m_imagePath) {
	// this.m_imagePath = m_imagePath;
	// }
	// public void setM_name(String m_name, String imagePath, String str) {
	// this.m_name = m_name;
	// m_imagePath = imagePath;
	// m_str = str;
	// }
	// ---
	public String getLinkcache() {
		return linkcache;
	}

	public void setLinkcache(String linkcache) {
		this.linkcache = linkcache;
	}
	public User() {
	}

	public User(String username) {
		this.username = username;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public int getUnreadMsgCount() {
		return unreadMsgCount;
	}

	public void setUnreadMsgCount(int unreadMsgCount) {
		this.unreadMsgCount = unreadMsgCount;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	@Override
	public int hashCode() {
		return 17 * getUsername().hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof User)) {
			return false;
		}
		return getUsername().equals(((User) o).getUsername());
	}

	@Override
	public String toString() {
		return nick == null ? username : nick;
	}
}
