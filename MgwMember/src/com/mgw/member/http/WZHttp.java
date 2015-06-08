package com.mgw.member.http;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;

import com.android.volley.toolbox.Volley;
import com.fz.core.net.RequestHelper;
import com.mgw.member.constant.Define_C;
import com.mgw.member.constant.imp_Define;


/**
 * 
 *
 */
public class WZHttp implements imp_Define
{
	/*
	 * 用户登录
	 */
	public static JSONObject Getgetapplist(String lastid) throws JSONException
	{
		String url;
		url = Define_C.ONLINE_UPDATE_URL;
	 
		Map<String, String> params = new HashMap<String, String>();
		params.put("type", "member.getapplist");
		params.put("telephone", "13888888888");
		params.put("lastid", lastid); 
		params.put("token", "");
		params.put("key", "");
		params.put("format", "json");		
		String resultString = RequestHelper.PostBySingleBitmap(url, params, null);
		
		if (resultString == null || resultString.equals("")) { return null; } 
		
		JSONTokener jsonParser = new JSONTokener(resultString);
		return (JSONObject) jsonParser.nextValue();
	}
	
	/*
	 * 用户登录
	 */
	public static JSONObject GetApploading(String userid,String pKey) throws JSONException
	{
		String url;
		url = Define_C.ONLINE_UPDATE_URL;
	 
		Map<String, String> params = new HashMap<String, String>();
		params.put("type", "user.apploading");
		params.put("telephone", "13888888888");
		params.put("pmID", userid);
		params.put("pKey", pKey);
		params.put("token", "");
		params.put("key", "");
		params.put("format", "json");
		
		String resultString = RequestHelper.PostBySingleBitmap(url, params, null);
							
		if (resultString == null || resultString.equals("")) { return null; } 
		
		JSONTokener jsonParser = new JSONTokener(resultString);
		return (JSONObject) jsonParser.nextValue();
	}
	/**
	 * 获取商家分类第一部分
	 * */
	public static JSONObject Getfirstshoptype(String userid,String serial) throws JSONException
	{
		String url;
		url = Define_C.ONLINE_UPDATE_URL;
	 
		Map<String, String> params = new HashMap<String, String>();
		params.put("type", "wzreposity.firstshoptype");
		params.put("userid", userid);
		params.put("serial", serial);
		params.put("token", "");
		params.put("key", "");
		params.put("format", "json");
		
		String resultString = RequestHelper.PostBySingleBitmap(url, params, null);
		
		if (resultString == null || resultString.equals("")) { return null; } 
		
		JSONTokener jsonParser = new JSONTokener(resultString);
		return (JSONObject) jsonParser.nextValue();
	}
	
	/**
	 * 获取商家分类第二部分
	 * */
	public static JSONObject GetHostshoptype(String userid,String serial) throws JSONException
	{
		String url;
		url = Define_C.ONLINE_UPDATE_URL;
	 
		Map<String, String> params = new HashMap<String, String>();
		params.put("type", "wzreposity.hotshoptype");
		params.put("userid", userid);
		params.put("serial", serial);
		params.put("token", "");
		params.put("key", "");
		params.put("format", "json");
		
		String resultString = RequestHelper.PostBySingleBitmap(url, params, null);
		
		if (resultString == null || resultString.equals("")) { return null; } 
		
		JSONTokener jsonParser = new JSONTokener(resultString);
		return (JSONObject) jsonParser.nextValue();
	}
	/**
	 * 热门城市
	 * */
	public static JSONObject GetHostCity(String userid,String serial) throws JSONException
	{
		String url;
		url = Define_C.ONLINE_UPDATE_URL;
	 
		Map<String, String> params = new HashMap<String, String>();
		params.put("type", "wzreposity.hotcity");
		params.put("userid", userid);
		params.put("serial", serial);
		params.put("token", "");
		params.put("key", "");
		params.put("format", "json");
		
		String resultString = RequestHelper.PostBySingleBitmap(url, params, null);
		
		if (resultString == null || resultString.equals("")) { return null; } 
		
		JSONTokener jsonParser = new JSONTokener(resultString);
		return (JSONObject) jsonParser.nextValue();
	}
	
	/**
	 * 获取城市
	 * */
	public static JSONObject GetCity(String userid,String serial,String ckey) throws JSONException
	{
		String url;
		url = Define_C.ONLINE_UPDATE_URL;
	 
		Map<String, String> params = new HashMap<String, String>();
		params.put("type", "wzreposity.citysearch");
		params.put("userid", userid);
		params.put("serial", serial);
		params.put("ckey", ckey);
		params.put("token", "");
		params.put("key", "");
		params.put("format", "json");
		
		String resultString = RequestHelper.PostBySingleBitmap(url, params, null);
		if (resultString == null || resultString.equals("")) { return null; } 		
		JSONTokener jsonParser = new JSONTokener(resultString);
		return (JSONObject) jsonParser.nextValue();
	}
	
	/**
	 * 查询商家
	 * */
	public static JSONObject Getshopsearch(String userid,String serial,String city,String pindex,String special
			,String posx,String posy,String stype,String circle,String sort,String skey) throws JSONException
	{
		String url;
		url = Define_C.ONLINE_UPDATE_URL;
	 
		Map<String, String> params = new HashMap<String, String>();
		params.put("type", "wzreposity.shopsearch");
		params.put("userid", userid);
		params.put("serial", serial);		
		params.put("city", city);
		params.put("pindex", pindex);
		params.put("special", special);
		
		params.put("posx", posx);
		params.put("posy", posy);
		params.put("stype", stype);
		
		params.put("circle", circle);
		params.put("sort", sort);
		params.put("skey", skey); 
		params.put("token", "");
		params.put("key", "");
		params.put("format", "json");
		
		String resultString = RequestHelper.PostBySingleBitmap(url, params, null);
		
		if (resultString == null || resultString.equals("")) { return null; } 
		
		JSONTokener jsonParser = new JSONTokener(resultString);
		return (JSONObject) jsonParser.nextValue();
	}
}
