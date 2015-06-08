package com.mgw.member.uitls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mgw.member.http.WZHttp;
import com.mgw.member.manager.BaseApplication;

public class DBLoad {

	/*
	 * 是否加载过所有城市
	 */
	public static Boolean IsLoadCity = false;
	/*
	 * 是否加载过热门城市
	 */
	public static Boolean IsLoadHostCity = false;

	public static void LoadHostCity(String pUserID, String pSerial) {
		try {
			JSONObject obj = WZHttp.GetHostCity(pUserID, pSerial);
			if (obj != null && obj.getInt("flag") == 0) {
				JSONArray jarr = obj.getJSONArray("items");
				if (jarr != null && jarr.length() > 0) {
					IsLoadHostCity = true;
					String sql = "delete from hostcity";
					BaseApplication.getApplication().GetDbhandler().LocalExecuteUpdate(sql);
					for (int i = 0; i < jarr.length(); i++) {
						obj = jarr.getJSONObject(i);
						sql = "insert into hostcity (cid,cname) values ('" + obj.getString("cid") + "'," + "'" + obj.getString("cname") + "')";
						BaseApplication.getApplication().GetDbhandler().LocalExecuteUpdate(sql);
					}
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void LoadAllCity(String pUserID, String pSerial) {
		try {
			JSONObject obj = WZHttp.GetCity(pUserID, pSerial, "");
			if (obj != null && obj.getInt("flag") == 0) {
				JSONArray jarr = obj.getJSONArray("items");
				if (jarr != null && jarr.length() > 0) {
					IsLoadCity = true;
					String sql = "delete from city";
					BaseApplication.getApplication().GetDbhandler().LocalExecuteUpdate(sql);
					for (int i = 0; i < jarr.length(); i++) {
						obj = jarr.getJSONObject(i);
						sql = "insert into city (cid,cname,pinyin) values ('" + obj.getString("cid") + "'," + "'" + obj.getString("cname") + "','" + obj.getString("pinyin") + "')";
						BaseApplication.getApplication().GetDbhandler().LocalExecuteUpdate(sql);
					}

				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
