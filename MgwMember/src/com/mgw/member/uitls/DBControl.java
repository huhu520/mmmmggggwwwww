package com.mgw.member.uitls;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author tj
 */
public class DBControl {
	private static SQLiteDatabase g_easy_do_db = null;

	public DBControl(Context context) {
		OpenDataBase(context);
	}

	public boolean OpenDataBase(Context context) {
		if (g_easy_do_db == null) {
			g_easy_do_db = context.openOrCreateDatabase("mayerhyt.db", Context.MODE_PRIVATE, null);
		}
		LocalExecuteUpdate("CREATE TABLE IF NOT EXISTS " + " linker (" + " username TEXT, " + " userid TEXT, " + " nick TEXT, " + " photo TEXT PRIMARY KEY);");

		LocalExecuteUpdate("CREATE TABLE IF NOT EXISTS  " + DiscountDao.TABLE_NAME + " (" + DiscountDao.DISCOUNT_SHOPID + " TEXT, " + DiscountDao.DISCOUNT_SHOP_DISCOUNT + " TEXT);");
		// 商家广告
		LocalExecuteUpdate("CREATE TABLE IF NOT EXISTS ads (CreateDate text,ID text,Title text,Content text,MainPic text,Link text,BussinessID text)");
		// 城市列表
		LocalExecuteUpdate("CREATE TABLE IF NOT EXISTS city (cid integer primary key,cname text,pinyin text)");
		// 热门城市列表
		LocalExecuteUpdate("CREATE TABLE IF NOT EXISTS hostcity (cid integer primary key,cname text)");
		// 城市列表区域
		// LocalExecuteUpdate("CREATE TABLE IF NOT EXISTS cityArea (AreaID text primary key,Areaname text,cityid integer)");

		// 商家分类（首页第一层）
		LocalExecuteUpdate("CREATE TABLE IF NOT EXISTS shopfirstType (fstid text primary key,fstname text,icon text)");
		// 商家分类（首页第二层）
		LocalExecuteUpdate("CREATE TABLE IF NOT EXISTS shopsecondType (fstid text primary key,fstname text,icon text)");
		// 所有商家分类
		LocalExecuteUpdate("CREATE TABLE IF NOT EXISTS shopAllType (id text primary key,name text,icon text,upid text)");
		// 首页商家
		LocalExecuteUpdate("CREATE TABLE IF NOT EXISTS shopFirst (sid text primary key,rtype int,sname text,sdesc text,sposx float,sposy float,"
				+ "sdistance float,sdisc float,ssafe float,sconsume int,image text,score int,cityid int)");

		// 对话
		LocalExecuteUpdate("CREATE TABLE IF NOT EXISTS chat (id integer primary key autoincrement,user_id integer,bussiness_id integer,"
				+ "user_icon text,bussiness_url,user_name text,bussiness_name text,user_tel text,bussiness_tel text,context text," + "time text,unread integer,is_user integer)");
		return true;
	}

	public static SQLiteDatabase GetDB(Context context) {
		if (g_easy_do_db == null) {
			g_easy_do_db = context.openOrCreateDatabase("mayerhyt.db", Context.MODE_PRIVATE, null);
		}
		return g_easy_do_db;
	}

	public boolean tabIsExist(String tabName) {
		boolean result = false;
		if (tabName == null) {
			return false;
		}
		Cursor cursor = null;
		try {
			String sql = "select count(*) as c from sqlite_master where type ='table' and name ='" + tabName.trim() + "' ";
			cursor = g_easy_do_db.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				int count = cursor.getInt(0);
				if (count > 0) {
					result = true;
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	/**
	 * 判断数据库中指定表的指定字段是否存在
	 * 
	 * @param db
	 * @param strTableName
	 *            指定表名称
	 * @param strFieldName
	 *            执行字段名称
	 * @return
	 */
	public boolean isExistField(String strTableName, String strFieldName) {
		StringBuilder builder = new StringBuilder();
		builder.append("name = '").append(strTableName).append("' AND sql LIKE '%").append(strFieldName).append("%'");
		Cursor cursor = null;
		try {
			cursor = g_easy_do_db.query("sqlite_master", null, builder.toString(), null, null, null, null);
			return cursor.getCount() > 0;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return false;
	}

	public boolean GetDataFromLocalDB(ArrayList<JSONObject> array, String sql) {
		Cursor result = g_easy_do_db.rawQuery(sql, null);
		int count = result.getColumnCount();
		while (result.moveToNext()) {
			JSONObject obj = new JSONObject();
			for (int i = 0; i < count; i++) {
				try {
					String key = result.getColumnName(i);
					obj.put(key, result.getString(i));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			array.add(obj);
		}

		result.close();
		return true;
	}

	public boolean GetDataForJSONArray(JSONArray array, String sql) {
		Cursor result = g_easy_do_db.rawQuery(sql, null);
		int count = result.getColumnCount();
		while (result.moveToNext()) {
			JSONObject obj = new JSONObject();
			for (int i = 0; i < count; i++) {
				try {
					String key = result.getColumnName(i);
					obj.put(key, result.getString(i));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			array.put(obj);
		}

		result.close();
		return true;
	}

	public JSONObject GetDataFromLocalDB2Dictionary(String sql) {
		Cursor result = g_easy_do_db.rawQuery(sql, null);
		int count = result.getColumnCount();
		JSONObject obj = null;
		if (result.getCount() == 1) {
			obj = new JSONObject();
			result.moveToFirst();
			for (int i = 0; i < count; i++) {
				String key = result.getColumnName(i);
				try {
					obj.put(key, result.getString(i));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		result.close();
		return obj;
	}

	public String GetSelectStringByKey(String sql) {
		String res = "";
		Cursor result = g_easy_do_db.rawQuery(sql, null);
		if (result.getCount() > 0) {
			result.moveToFirst();
			res = result.getString(0);
		}

		result.close();
		return res;
	}

	public boolean LocalExecuteUpdate(String sql) {
		g_easy_do_db.execSQL(sql);
		return true;
	}
}