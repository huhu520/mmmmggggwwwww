package com.hx.hxchat.db;

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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.easemob.util.HanziToPinyin;
import com.hx.hxchat.Constant;
import com.hx.hxchat.domain.User;
import com.mgw.member.constant.Define_C;

/**
 * 用户
 */
public class UserDao {
	/**
	 * mgw用户名
	 */
	public static final String TABLE_NAME = "uers";
	/**
	 * 环信useid
	 */
	public static final String COLUMN_NAME_ID = "username";
	/**
	 * 昵称
	 */
	public static final String COLUMN_NAME_NICK = "nick";
	/**
	 * 用户名头像
	 */
	public static final String COLUMN_NAME_PHOTO = "photo";
	/**
	 * 是否是代理商
	 */
	public static final String COLUMN_NAME_REFEREE = "referee";
	/**
	 * 设置备注信息
	 */
	public static final String COLUMN_NAME_COMMENT = "comment";

	public static Boolean checked = false;
	private final DbOpenHelper dbHelper;
	

	public UserDao(Context context) {
		dbHelper = DbOpenHelper.getInstance(context);
	}

	private void CheckTable(SQLiteDatabase db) {
		String USERNAME_TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_NAME_ID + " TEXT, " + COLUMN_NAME_NICK + " TEXT, " + COLUMN_NAME_PHOTO + " TEXT, " + COLUMN_NAME_REFEREE + " TEXT, " + COLUMN_NAME_COMMENT
				+ " TEXT );";
		if (tabIsExist(TABLE_NAME)) {
			if (!DbOpenHelper.isExistField(db, TABLE_NAME, COLUMN_NAME_NICK) || !DbOpenHelper.isExistField(db, TABLE_NAME, COLUMN_NAME_ID)
					|| !DbOpenHelper.isExistField(db, TABLE_NAME, COLUMN_NAME_PHOTO) || !DbOpenHelper.isExistField(db, TABLE_NAME, COLUMN_NAME_REFEREE)|| !DbOpenHelper.isExistField(db, TABLE_NAME, COLUMN_NAME_COMMENT)) {
				db.execSQL("drop table " + TABLE_NAME);
				db.execSQL(USERNAME_TABLE_CREATE);
			}
		} else {
			db.execSQL(USERNAME_TABLE_CREATE);
		}
		checked = true;
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
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		StringBuilder builder = new StringBuilder();
		builder.append("name = '").append(strTableName).append("' AND sql LIKE '%").append(strFieldName).append("%'");
		Boolean bReturn = false;
		Cursor cursor = null;
		try {
			cursor = db.query("sqlite_master", null, builder.toString(), null, null, null, null);
			bReturn = cursor.getCount() > 0;
			cursor.close();
			return bReturn;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return false;
	}

	/*
	 * public void CheckTable(SQLiteDatabase db) { if (db.isOpen()) {
	 * if(DbOpenHelper.tabIsExist(db, TABLE_NAME)) {
	 * if(!DbOpenHelper.isExistField(db, TABLE_NAME,COLUMN_NAME_PHOTO)) {
	 * db.execSQL("ALTER TABLE "+TABLE_NAME+" ADD "+COLUMN_NAME_PHOTO+" TEXT");
	 * } } } }
	 */
	public boolean LocalExecuteUpdate(String sql) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (db.isOpen()) {
			db.execSQL(sql);
		}
		return true;
	}

	public boolean tabIsExist(String tabName) {
		boolean result = false;
		if (tabName == null) {
			return false;
		}
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (db.isOpen()) {
			Cursor cursor = null;
			try {
				String sql = "select count(*) as c from sqlite_master where type ='table' and name ='" + tabName.trim() + "' ";
				cursor = db.rawQuery(sql, null);
				if (cursor.moveToNext()) {
					int count = cursor.getInt(0);
					if (count > 0) {
						result = true;
					}
				}
				cursor.close();

			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return result;
	}

	/**
	 * �������list
	 * 
	 * @param contactList
	 */
	public void saveContactList(List<User> contactList) {
		Define_C.s_HasChangeContent = true;
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (db.isOpen()) {
			if (!checked) {
				CheckTable(db);
			}
			db.delete(TABLE_NAME, null, null);
			for (User user : contactList) {
				if (!user.getUsername().equals("")) {
					ContentValues values = new ContentValues();
					if (user.getNick() != null)
						values.put(COLUMN_NAME_NICK, user.getNick());
					values.put(COLUMN_NAME_ID, user.getUsername());
					if (user.getAvatar() != null)
						values.put(COLUMN_NAME_PHOTO, user.getAvatar());
					if (user.getreferee() != null) {
						values.put(COLUMN_NAME_REFEREE, user.getreferee());
					}
					if (user.getComment() != null) {
						values.put(COLUMN_NAME_COMMENT, user.getComment());
					}
					db.insert(TABLE_NAME, null, values);
				}
			}
		}
	}

	public Boolean containsKey(String pKey) {
		Boolean oTrue = false;
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		if (db.isOpen()) {
			if (!checked) {
				CheckTable(db);
			}
			Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where " + COLUMN_NAME_ID + " ='" + pKey + "'", null);
			while (cursor.moveToNext()) {
				oTrue = true;
			}
			cursor.close();
		}

		return oTrue;
	}

	/**
	 * 从数据库获取好友 列表
	 * 
	 * @return
	 */
	public Map<String, User> getContactList() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Map<String, User> users = new HashMap<String, User>();
		if (db.isOpen()) {
			if (!checked) {
				CheckTable(db);
			}
			// CheckTable(db);
			Cursor cursor = db.rawQuery("select * from " + TABLE_NAME /*
																	 * +
																	 * " order by "
																	 * +
																	 * COLUMN_NAME_NICK
																	 */, null);
			Boolean bExitFRIENDS_USERNAME = false;
			while (cursor.moveToNext()) {
				String username = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ID));
				String nick = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NICK));
				String referee = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_REFEREE));
				String oimage = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PHOTO));
				String comment = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_COMMENT));
				
				User user = new User();
				user.setUsername(username);
				user.setNick(nick);
				user.setAvatar(oimage);
				user.setreferee(referee);
				user.setComment(comment);
				String headerName = null;
				if (!TextUtils.isEmpty(user.getNick())) {
					headerName = user.getNick();
				} else {
					headerName = user.getUsername();
				}
				if (username.equals(Constant.NEW_FRIENDS_USERNAME)) {
					bExitFRIENDS_USERNAME = true;
				}
				if (username.equals(Constant.NEW_FRIENDS_USERNAME) || username.equals(Constant.GROUP_USERNAME)) {
					user.setHeader("");
				} else if (Character.isDigit(headerName.charAt(0))) {
					user.setHeader("#");
				} else {
					try {
						user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target.substring(0, 1).toUpperCase());
						char header = user.getHeader().toLowerCase().charAt(0);
						if (header < 'a' || header > 'z') {
							user.setHeader("#");
						}
					} catch (Exception ex) {
						user.setHeader("#");
					}
				}
				users.put(username, user);
			}
			cursor.close();
			if (bExitFRIENDS_USERNAME == false) {
				User user = new User();
				user.setUsername(Constant.NEW_FRIENDS_USERNAME);
				user.setNick("");
				user.setreferee("0");
				users.put(Constant.NEW_FRIENDS_USERNAME, user);
			}
		}
		return users;
	}

	/**
	 * 删除一个联系人 *
	 * 
	 * @param username
	 */
	public void deleteContact(String username) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (db.isOpen()) {
			if (!checked) {
				CheckTable(db);
			}
			db.delete(TABLE_NAME, COLUMN_NAME_ID + " = ?", new String[] { username });
		}
	}

	/**
	 * ����һ����ϵ��
	 * 
	 * @param user
	 */
	public void saveContact(User user) {
		Define_C.s_HasChangeContent = true;
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		
		if(containsKey(user.getUsername())){
			
			deleteContact(user.getUsername());
			
		}
			ContentValues values = new ContentValues();
			values.put(COLUMN_NAME_ID, user.getUsername());
			if (user.getNick() != null)
				values.put(COLUMN_NAME_NICK, user.getNick());
			if (user.getAvatar() != null)
				values.put(COLUMN_NAME_PHOTO, user.getAvatar());
			// if (user.getreferee() != null)
			// values.put(COLUMN_NAME_REFEREE, user.getreferee());
			values.put(COLUMN_NAME_REFEREE, "1");
			if (user.getComment() != null)
				values.put(COLUMN_NAME_COMMENT, user.getComment());
			// values.put(COLUMN_NAME_REFEREE, "1");
			if (db.isOpen()) {
				if (!checked) {
					CheckTable(db);
				}
				
				
				// CheckTable(db);
				long a = db.insert(TABLE_NAME, null, values);
				Log.i("插入执行了", "这是返回值：" + a);
			}
			
			
		
		
	
	}
	/**
	 * 设置备注信息
	 * 
	 * @param uid
	 * @param comment
	 */
	public void setComment(String uid,String comment) {
		Define_C.s_HasChangeContent = true;
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		
		ContentValues values = new ContentValues();
		if (comment!= null)
			values.put(COLUMN_NAME_COMMENT,comment);
		if (db.isOpen()) {
			if (!checked) {
				CheckTable(db);
			}
			int update = db.update(TABLE_NAME, values, "username=?", new String[]{uid});
			
			Log.i("插入执行了", "这是返回值：" + update);
		}
	}

	/**
	 * 设置备注信息
	 * 
	 * @param uid
	 * @param comment
	 * @return 
	 */
	public String getComment(String uid) {
		Define_C.s_HasChangeContent = true;
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		String comment = null;
		if (db.isOpen()) {
			if (!checked) {
				CheckTable(db);
			}
			Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where " + COLUMN_NAME_ID + " = " + uid, null);
			while (cursor.moveToNext()) {
				
				comment = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_COMMENT));
				
			}
			cursor.close();
		}
		
		return comment;
		
		
	}
	
	
	
	
	public User getContact(String mobile) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		User user = null;
		if (db.isOpen()) {
			if (!checked) {
				CheckTable(db);
			}
			Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where " + COLUMN_NAME_ID + " = " + mobile, null);
			while (cursor.moveToNext()) {
				try {

					String username = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ID));
					String nick = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NICK));
					String photo = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PHOTO));
					String oRe = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_REFEREE));
					String comment = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_COMMENT));
					if (username == null || "".equals(username)) {
						return null;
					} else {
						user = new User();
						user.setUsername(username);
						user.setNick(nick);
						user.setAvatar(photo);
						user.setreferee(oRe);
						user.setComment(comment);

					}

				} catch (Exception e) {
					return null;
					// TODO: handle exception
				}
			}
			cursor.close();
		}
		return user;
	}
}
