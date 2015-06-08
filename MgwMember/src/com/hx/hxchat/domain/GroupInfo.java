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

import com.easemob.chat.EMGroup;

/**
 * 环信用户
 * 
 * @author Administrator
 * 
 */
public class GroupInfo extends EMGroup {

	
	
	public GroupInfo(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	private EMGroup emGroup;
	
	public EMGroup getEmGroup() {
		return emGroup;
	}

	public void setEmGroup(EMGroup emGroup) {
		this.emGroup = emGroup;
	}

	private String groupIcon;

	public String getGroupIcon() {
		return groupIcon;
	}

	public void setGroupIcon(String groupIcon) {
		this.groupIcon = groupIcon;
	}

	


}
