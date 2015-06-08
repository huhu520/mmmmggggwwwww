/*
\ * Copyright (C) 2010 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mgw.member.manager;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.hx.hxchat.domain.GoodShareInfo;
import com.mgw.member.R;

/**
 * @author huyan
 * 
 */
public class LinkToBeanManager {

	private static LinkToBeanManager instance;

	public static Gson gson;

	public static synchronized LinkToBeanManager getInstance() {
		if (instance == null) {
			instance = new LinkToBeanManager();
			gson = new Gson();
		}
		return instance;
	}

	/**
	 * 判断链接是否是网址
	 * 
	 * @param url
	 * @return
	 */
	private boolean isNetLink(String url) {
		return false;

	}

	/**
	 * json转换对象
	 * 
	 * @param json
	 * @return
	 */
	public GoodShareInfo ConvertNetLink(String json) {

		// {
		// "asgoodshare": "1",
		// "avatarurl": "sadadasd",
		// "content": "专业洗面乃",
		// "pid": "1055144",
		// "sid": "12544",
		// "title": "洁面系列",
		// "url": "adads"
		// }

		if (json != null && json.contains("asgoodshare")) {
			GoodShareInfo shareInfo = gson.fromJson(json, GoodShareInfo.class);
			if (shareInfo != null) {
				return shareInfo;
			}

		}

		// 1.判断是否是网址

		// 2.是网址，请求网络并下载获得标题 正文 图片 如果没网提示

		// 3.有网 但是页面错误返回 错误信息 表示不能转换

		return null;

	}

}
