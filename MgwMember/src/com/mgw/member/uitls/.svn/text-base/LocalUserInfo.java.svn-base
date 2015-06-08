package com.mgw.member.uitls;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalUserInfo {

    /**
     * 保存Preference的name
     */
    public static final String PREFERENCE_NAME = "mgw_data";
    private static SharedPreferences mSharedPreferences;
    private static LocalUserInfo mPreferenceUtils;
    private static SharedPreferences.Editor editor;

    private LocalUserInfo(Context cxt) {
        mSharedPreferences = cxt.getSharedPreferences(PREFERENCE_NAME,
                Context.MODE_PRIVATE);
    }

    /**
     * 单例模式，获取instance实例
     * 
     * @param cxt
     * @return
     */
    public static LocalUserInfo getInstance(Context cxt) {
        if (mPreferenceUtils == null) {
            mPreferenceUtils = new LocalUserInfo(cxt);
        }
        editor = mSharedPreferences.edit();
        return mPreferenceUtils;
    }

    //
    public void setUserInfo(String str_name, String str_value) {

        editor.putString(str_name, str_value);
        editor.commit();
    }
    //
    public void setUserInfo(String str_name, boolean str_value) {
    	
    	editor.putBoolean(str_name, str_value);
    	editor.commit();
    }

    public String getUserInfo(String str_name) {

        return mSharedPreferences.getString(str_name, "");

    }
    public boolean getUserInfo_boolean(String str_name) {
    	return mSharedPreferences.getBoolean(str_name, false);
    	
    }

}
