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
 * 联系人有变更消息事件消息事件
 * @author huyan
 *备注：采用eventbus事件订阅总线框架
 */
public class FirendsHasChangeContentEvent {
	/**
	 * 是否有变更
	 */
	private boolean hasChange;

	public FirendsHasChangeContentEvent(boolean hasChange) {
		super();
		this.hasChange = hasChange;
	}

	public FirendsHasChangeContentEvent() {
		super();
	}
	
}
