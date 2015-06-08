/*
 * Copyright (C) 2012 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mgw.member.ottoEvent;


/**
 * 环信未读邀请消息事件
 * @author huyan
 *备注：采用eventbus事件订阅总线框架
 */
public class UnreadInviteRefeshEvent {
	
	
	/**
	 * 消息数量
	 */
	private int  count;
	
	/**
	 * 是否要刷新
	 */
	private boolean isRef;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public boolean isRef() {
		return isRef;
	}

	public void setRef(boolean isRef) {
		this.isRef = isRef;
	}

	
	
}
