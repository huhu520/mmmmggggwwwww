package com.mgw.member.uitls;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;

import com.google.gson.Gson;
import com.mgw.member.R;
import com.mgw.member.bean.UserInfoBean;
import com.mgw.member.constant.Define_C;
import com.mgw.member.manager.BaseApplication;

/**
 * 用户保存SharePreferences的工具类
 * 
 */
public class PreferenceUtils {

	static String TAG = PreferenceUtils.class.getSimpleName();
	/**
	 * 保存Preference的name
	 */
	public static final String PREFERENCE_NAME = "mgw_data";
	private static SharedPreferences mSharedPreferences;
	private static PreferenceUtils mPreferenceUtils;
	private static SharedPreferences.Editor editor;

	/**
	 * 应用是否登录（回退用）
	 */
	private String SHARED_KEY_APP_LOGINED = "shared_key_app_logined";
	private String SHARED_KEY_LOGIN_DATA = "shared_key_mgw_login_data";
	private String SHARED_KEY_LOGIN_DATA_ITEM = "shared_key_mgw_login_data_item";
	private String SHARED_KEY_LOGIN_DATA_DES = "mgw_data";

	private PreferenceUtils(Context cxt) {
		mSharedPreferences = cxt.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		editor = mSharedPreferences.edit();
	}

	public static synchronized void init(Context cxt) {
		if (mPreferenceUtils == null) {
			mPreferenceUtils = new PreferenceUtils(cxt);
		}
	}

	/**
	 * 单例模式，获取instance实例
	 * 
	 * @param cxt
	 * @return
	 */
	public static PreferenceUtils getInstance() {
		if (mPreferenceUtils == null) {
			throw new RuntimeException("please init first!");
		}

		return mPreferenceUtils;
	}

	public void setAppLogined(boolean paramBoolean) {
		editor.putBoolean(SHARED_KEY_APP_LOGINED, paramBoolean);
		editor.commit();
	}

	public boolean getAppLogined() {
		return mSharedPreferences.getBoolean(SHARED_KEY_APP_LOGINED, false);
	}

	public String getLoginInfo(UserInfoBean bean) {
		// bean.item.groupid = groupid;
		// bean.item.sid = sid;
		// bean.item.uid = uid;
		String json = new Gson().toJson(bean);
		//
		LogUtils.i(TAG, "getLoginInfo,json=" + json.toString());

		// JSONObject jsonObject = new JSONObject(json);
		// JSONObject jsonObject2 = jsonObject.getJSONObject("item");
		// sharedata.putString("mgw_data", jsonObject2.toString());
		// sharedata.commit();
		return json;

	}

	public void setLoginInfo(String param) {
		JSONObject obj;
		try {
			obj = new JSONObject(param);

			UserInfoBean fromJson = new Gson().fromJson(param, UserInfoBean.class);
			BaseApplication.getApplication().setBean(fromJson);
			JSONObject obj1 = obj.getJSONObject("item");
			editor.putString("mgw_data", obj1.toString());
			editor.putString("mgw_userID", obj1.getString("UserID"));
			editor.putString("mgw_serial", obj1.getString("serial"));
			//
			editor.putString("mgw_owner", obj1.getString("owner"));
			editor.putString("mgw_mall", obj1.getString("mall"));
			editor.putString("mgw_name", obj1.getString("MemberName"));

			editor.putString("mgw_sid", !obj1.toString().contains("sid") ? "" : obj1.getString("sid"));
			editor.putString("mgw_uid", !obj1.toString().contains("uid") ? "" : obj1.getString("uid"));
			editor.putString("mgw_groupid", !obj1.toString().contains("groupid") ? "" : obj1.getString("groupid"));
			editor.putString("mgw_memberid", !obj1.toString().contains("memberid") ? "" : obj1.getString("memberid"));

			editor.putString("s_RelationID", obj1.getString("RelationID"));

			editor.putString("wz_alipay", obj1.getString("wz_alipay"));
			editor.putString("wz_alipay_return_url", obj1.getString("wz_alipay_return_url"));
			editor.putString("wz_tenpay", obj1.getString("wz_tenpay"));
			editor.putString("mall_alipay", obj1.getString("mall_alipay"));
			editor.putString("mall_alipay_return_url", obj1.getString("mall_alipay_return_url"));
			editor.putString("mall_tenpay", obj1.getString("mall_tenpay"));
			editor.putString("xx_alipay", obj1.getString("xx_alipay"));
			editor.putString("xx_alipay_return_url", obj1.getString("xx_alipay_return_url"));
			editor.putString("xx_tenpay", obj1.getString("xx_tenpay"));
			editor.putString("NewVision", obj1.getString("serial"));
			editor.putString("register_page", obj1.getString("register_page"));
			Define_C.s_RelationID = obj1.getString("RelationID");
			editor.putBoolean("logined", true);
			editor.commit();

			editor.commit();
			
			LogUtils.i(TAG,"保存了用户登录详细信息");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUtils.e(TAG, e.toString());
		}
		
		
	}

	public void setLoginInfo(UserInfoBean bean) throws JSONException {
		if (bean == null)
			return;

		String json = new Gson().toJson(bean);
		JSONObject jsonObject = new JSONObject(json);
		JSONObject jsonObject2 = jsonObject.getJSONObject("item");

		editor.putString("mgw_data", jsonObject2.toString());
		editor.putString("mgw_userID", jsonObject2.getString("UserID"));
		editor.putString("mgw_serial", jsonObject2.getString("serial"));

		// sharedata
		// .putString("mgw_owner", obj.getString("owner"));
		editor.putString("mgw_owner", jsonObject2.getString("owner"));
		editor.putString("mgw_mall", jsonObject2.getString("mall"));
		editor.putString("mgw_name", jsonObject2.getString("MemberName"));

		editor.putString("mgw_sid", !jsonObject2.toString().contains("sid") ? "" : jsonObject2.getString("sid"));
		editor.putString("mgw_uid", !jsonObject2.toString().contains("uid") ? "" : jsonObject2.getString("uid"));
		editor.putString("mgw_groupid", !jsonObject2.toString().contains("groupid") ? "" : jsonObject2.getString("groupid"));
		editor.putString("mgw_memberid", !jsonObject2.toString().contains("memberid") ? "" : jsonObject2.getString("memberid"));

		editor.putString("s_RelationID", jsonObject2.getString("RelationID"));

		editor.putString("wz_alipay", jsonObject2.getString("wz_alipay"));
		editor.putString("wz_alipay_return_url", jsonObject2.getString("wz_alipay_return_url"));
		editor.putString("wz_tenpay", jsonObject2.getString("wz_tenpay"));
		editor.putString("mall_alipay", jsonObject2.getString("mall_alipay"));
		editor.putString("mall_alipay_return_url", jsonObject2.getString("mall_alipay_return_url"));
		editor.putString("mall_tenpay", jsonObject2.getString("mall_tenpay"));
		editor.putString("xx_alipay", jsonObject2.getString("xx_alipay"));
		editor.putString("xx_alipay_return_url", jsonObject2.getString("xx_alipay_return_url"));
		editor.putString("xx_tenpay", jsonObject2.getString("xx_tenpay"));
		editor.putString("NewVision", jsonObject2.getString("serial"));
		editor.putString("register_page", jsonObject2.getString("register_page"));

		Define_C.s_RelationID = jsonObject2.getString("RelationID");

		BaseApplication.getApplication().m_user_id = jsonObject2.getString("UserID");
		BaseApplication.getApplication().setUserName(jsonObject2.getString("UserID"));

		BaseApplication.getApplication().m_user_id = jsonObject2.getString("UserID");
		BaseApplication.getApplication().m_playerName = jsonObject2.getString("MemberName");
		BaseApplication.getApplication().setUserName(jsonObject2.getString("UserID"));

		editor.commit();
		
	}

	public void saveLoginCountPassword(String ac, String password) {
		editor.putString("mgw_pwd", ac);
		editor.putString("mgw_account", password);
		editor.commit();
	}
	
	
	public String[] getLoginCountPassword() {
		String [] strings= new String[2];
		strings[0]=mSharedPreferences.getString("mgw_account", "");
		strings[1]=mSharedPreferences.getString("mgw_pwd", "");
		return strings;
	}

}
