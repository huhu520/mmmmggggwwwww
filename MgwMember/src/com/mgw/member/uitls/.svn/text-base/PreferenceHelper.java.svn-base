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
package com.mgw.member.uitls;

import org.json.JSONObject;

import com.hx.hxchat.MGWHXSDKHelper;
import com.mgw.member.R;
import com.mgw.member.constant.Define_C;
import com.mgw.member.manager.BaseApplication;
import com.mgw.member.ui.activity.login.LoginActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 
 * 用于保存shareprefrence键值对的帮助类
 * 
 */
public class PreferenceHelper {
	String TAG="PreferenceHelper";
	/**
	 * 保存Preference的name
	 */
	// public static final String PREFERENCE_NAME = "mgw_member";
	private static PreferenceHelper mPreferenceUtils;
private Context context;
	private PreferenceHelper() {
		
	}

	/**
	 * 单例模式，获取instance实例
	 * 
	 * @param cxt
	 * @return
	 */
	public synchronized static PreferenceHelper getInstance(Context cxt) {
		if (mPreferenceUtils == null) {
			mPreferenceUtils = new PreferenceHelper();
			PreferenceUtils.init(cxt);
		}
		return mPreferenceUtils;
	}

	/**
	 * 保存用户登录登录状态（非环信）
	 * 
	 * @param paramBoolean
	 */
	public void setAppLogined(boolean paramBoolean) {
		PreferenceUtils.getInstance().setAppLogined(paramBoolean);
	
		LogUtils.i(TAG, "onCreate hx is logined? =" +", BaseApplication.getApplication().logined" +getAppLogined());

	}

	/**
	 * 获得用户登录登录状态（非环信）
	 * @param paramBoolean
	 */
	public boolean getAppLogined() {
		return PreferenceUtils.getInstance().getAppLogined();
	}
	/**
	 * 保存用户登录信息
	 * @param paramBoolean
	 */
	public void setLoginInfo(String param) {
		PreferenceUtils.getInstance().setLoginInfo(param);
	}
	/**
	 * 保存用户登录密码和 用户名
	 * @param paramBoolean
	 */
	public void saveLoginCountPassword(String ac,String psw) {
		PreferenceUtils.getInstance().saveLoginCountPassword(ac,psw);
	}
	/**
	 *huoqu用户登录密码和 用户名
	 * @param paramBoolean
	 */
	public String[] getLoginCountPassword() {
		return PreferenceUtils.getInstance().getLoginCountPassword();
	}
	
//	/**
//	 * 获得用户登录登录信息
//	 * @param paramBoolean
//	 */
//	public String getLoginInfo() {
//		return PreferenceUtils.getInstance().getLoginInfo();
//	}

	
	
	
//	
//	/**
//	 * 对象保存到
//	 * 
//	 * @param paramBoolean
//	 */
//	public void setLoginInfo(Object obj) {
//		PreferenceUtils.getInstance().setLoginInfo(param);
//	}
//	
//	/**
//	 * 获得用户登录登录信息
//	 * 
//	 * @param paramBoolean
//	 */
//	public String getLoginInfo() {
//		return PreferenceUtils.getInstance().getLoginInfo();
//	}

	

	
//	{
//	SharedPreferences.Editor sharedata = getActivity().getSharedPreferences("mgw_data", 0).edit();
//
//	UserInfoBean bean = BaseApplication.getApplication().getBean();
//	bean.item.groupid = groupid;
//	bean.item.sid = sid;
//	bean.item.uid = uid;
//	String json = new Gson().toJson(bean);
//
//	LogUtils.i(TAG, "setClientInfo,json=" + json.toString());
//	try {
//		JSONObject jsonObject = new JSONObject(json);
//		JSONObject jsonObject2 = jsonObject.getJSONObject("item");
//		sharedata.putString("mgw_data", jsonObject2.toString());
//		sharedata.commit();
//	} catch (JSONException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//
//}

	
	
}
