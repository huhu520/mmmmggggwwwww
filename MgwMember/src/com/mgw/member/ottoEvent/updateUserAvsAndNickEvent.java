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
 * 更新用户头像和昵称信息（更新数据库）
 * @author huyan
 *备注：采用eventbus事件订阅总线框架
 */
public class updateUserAvsAndNickEvent {
	
	

	private int  progress;
	private boolean updateAvs;
	private boolean updateNick;
	private boolean Allupdate;
	public int getProgress() {
		return progress;
	}
	public void setProgress(int progress) {
		this.progress = progress;
	}
	public boolean isUpdateAvs() {
		return updateAvs;
	}
	public void setUpdateAvs(boolean updateAvs) {
		this.updateAvs = updateAvs;
	}
	public boolean isUpdateNick() {
		return updateNick;
	}
	public void setUpdateNick(boolean updateNick) {
		this.updateNick = updateNick;
	}
	public boolean isAllupdate() {
		return Allupdate;
	}
	public void setAllupdate(boolean allupdate) {
		Allupdate = allupdate;
	}
	
	




	
	
}
