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

import com.google.gson.Gson;
import com.mgw.member.manager.LinkToBeanManager;
import com.mgw.member.uitls.LogUtils;
import com.squareup.otto.Produce;


/**
 * 获得produce生产者
 * @author huyan
 *备注：采用eventbus事件订阅总线框架
 */
public class GetOttoProduceManager {
	private static GetOttoProduceManager instance;
	public static  GetOttoProduceManager getInstance() {
		if (instance == null) {
			instance = new GetOttoProduceManager();
		}
		return instance;
	}
	
	
	
	
	/**
	 * 获得fragment刷新的事件订阅实体
	 * @return
	 */
	@Produce
	public FragmentNeedRefreshEvent getFragmentNeedRefreshEvent() {
		LogUtils.i("otto" + "FragmentNeedRefreshEvent");
		FragmentNeedRefreshEvent fragmentNeedRefreshEvent = new FragmentNeedRefreshEvent();
		return fragmentNeedRefreshEvent;
	}
	



	
	
}
