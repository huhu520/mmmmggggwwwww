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
package com.hx.hxchat.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mgw.member.manager.BaseApplication;

public class DbOpenHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 9;
	private static DbOpenHelper instance;

	// private static final String USERNAME_TABLE_CREATE = "CREATE TABLE "
	// + UserDao.TABLE_NAME + " (" + UserDao.COLUMN_NAME_NICK + " TEXT, "
	// + UserDao.COLUMN_NAME_ID + " TEXT, " + UserDao.COLUMN_NAME_PHOTO
	// + " TEXT PRIMARY KEY);";
	// private static final String USERNAME_TABLE_CREATE = "CREATE TABLE "
	// + UserDao.TABLE_NAME + " (" + UserDao.COLUMN_NAME_ID + " TEXT, "
	// + UserDao.COLUMN_NAME_NICK + " TEXT, " + UserDao.COLUMN_NAME_PHOTO
	// + " TEXT, " + UserDao.COLUMN_NAME_REFEREE + " TEXT PRIMARY KEY);";

//	/**
//	 * 用户表创建
//	 */
//	private static final String USERNAME_TABLE_CREATE = "CREATE TABLE " + UserDao.TABLE_NAME + " (" + UserDao.COLUMN_NAME_ID + " TEXT, " + UserDao.COLUMN_NAME_NICK + " TEXT, "
//			+ UserDao.COLUMN_NAME_PHOTO + " TEXT, " + UserDao.COLUMN_NAME_REFEREE + " TEXT);";
	/**
	 * 用户表创建v2
	 */
	private static final String USERNAME_TABLE_CREATE = "CREATE TABLE " + UserDao.TABLE_NAME + " (" + UserDao.COLUMN_NAME_ID + " TEXT, " + UserDao.COLUMN_NAME_NICK + " TEXT, "
			+ UserDao.COLUMN_NAME_PHOTO + " TEXT, "+UserDao.COLUMN_NAME_REFEREE + " TEXT, " + UserDao.COLUMN_NAME_COMMENT + " TEXT);";

	/**
	 * 邀请消息表创建
	 */
	private static final String INIVTE_MESSAGE_TABLE_CREATE = "CREATE TABLE " + InviteMessgeDao.TABLE_NAME + " (" + InviteMessgeDao.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ InviteMessgeDao.COLUMN_NAME_FROM + " TEXT, " + InviteMessgeDao.COLUMN_NAME_GROUP_ID + " TEXT, " + InviteMessgeDao.COLUMN_NAME_GROUP_Name + " TEXT, " + InviteMessgeDao.COLUMN_NAME_REASON
			+ " TEXT, " + InviteMessgeDao.COLUMN_NAME_STATUS + " INTEGER, " + InviteMessgeDao.COLUMN_NAME_ISINVITEFROMME + " INTEGER, " + InviteMessgeDao.COLUMN_NAME_TIME + " TEXT); ";

	// /**
	// * 临时联系人表
	// */
	// private static final String LINKER_TABLE_CREATE =
	// "CREATE TABLE  "+ LinkerDao.TABLE_NAME +
	// " ("
	// + LinkerDao.COLUMN_NAME_ID + " TEXT,"
	// + LinkerDao.COLUMN_NAME_UserID + " TEXT, "
	// + LinkerDao.COLUMN_NAME_NICK + " TEXT, "
	// + LinkerDao.COLUMN_NAME_PHOTO + " TEXT); ";
	//
	//
	/**
	 * 临时联系人表v2
	 */
	private static final String LINKER_TABLE_CREATE = "CREATE TABLE  " + LinkerDao.TABLE_NAME + " (" + LinkerDao.COLUMN_NAME_ID + " TEXT," + LinkerDao.COLUMN_NAME_UserID + " TEXT, "
			+ LinkerDao.COLUMN_NAME_NICK + " TEXT, " + LinkerDao.COLUMN_NAME_PHOTO + " TEXT, " + LinkerDao.COLUMN_LINKER_USER + " TEXT); ";

	/**
	 * 消息置顶置顶表
	 */
	private static final String TOPUSER_TABLE_CREATE = "CREATE TABLE " + TopUserDao.TABLE_NAME + " (" + TopUserDao.COLUMN_NAME_TIME + " TEXT, " + TopUserDao.COLUMN_NAME_IS_GOUP + " TEXT, "
			+ TopUserDao.COLUMN_NAME_ID + " TEXT PRIMARY KEY);";

	
	
	/**
	 * 消息置顶置顶表
	 */
	private static final String GROUPSICON_TABLE_CREATE = "CREATE TABLE " + GroupsDao.TABLE_NAME + " (" + GroupsDao.COLUMN_NAME_TIME + " TEXT, " + GroupsDao.COLUMN_NAME_ICON + " TEXT, "+ GroupsDao.COLUMN_NAME_GROUP + " TEXT, "
			+ GroupsDao.COLUMN_NAME_ID + " TEXT PRIMARY KEY);";
	
	
	
	
	
	private DbOpenHelper(Context context) {

		super(context, getUserDatabaseName(), null, DATABASE_VERSION);

	}

	/**
	 * 检查是否含有表
	 * 
	 * @param db
	 * @param tabName
	 * @return Created by Administrator
	 */
	public static boolean tabIsExist(SQLiteDatabase db, String tabName) {
		boolean result = false;
		if (tabName == null) {
			return false;
		}
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
	public static boolean isExistField(SQLiteDatabase db, String strTableName, String strFieldName) {
		StringBuilder builder = new StringBuilder();
		builder.append("name = '").append(strTableName).append("' AND sql LIKE '%").append(strFieldName).append("%'");
		Cursor cursor = null;
		try {
			cursor = db.query("sqlite_master", null, builder.toString(), null, null, null, null);
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

	public static DbOpenHelper getInstance(Context context) {
		if (instance == null) {
			instance = new DbOpenHelper(context.getApplicationContext());
		}
		return instance;
	}

	/**
	 * 获得用户数据库表的名字
	 * 
	 * @return Created by Administrator
	 */
	public static String getUserDatabaseName() {
		return BaseApplication.getApplication().getUserName() + "_demo.db";
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(USERNAME_TABLE_CREATE);
		db.execSQL(INIVTE_MESSAGE_TABLE_CREATE);
		db.execSQL(LINKER_TABLE_CREATE);
		db.execSQL(TOPUSER_TABLE_CREATE);
		db.execSQL(GROUPSICON_TABLE_CREATE);

	}

	/**
	 * 数据库更新
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		/**
		 * 1、第一次创建数据库的时候，这个方法不会走 2、清除数据后再次运行(相当于第一次创建)这个方法不会走
		 * 3、数据库已经存在，而且版本升高的时候，这个方法才会调用
		 */

		switch (newVersion) {

		case 6:
			Log.i("mayer", "#############数据库升级了##############:" + newVersion);

			db.execSQL(TOPUSER_TABLE_CREATE);

			db.execSQL("ALTER TABLE " + LinkerDao.TABLE_NAME + " ADD " + LinkerDao.COLUMN_LINKER_USER + " TEXT"); // 往表中增加一列

			
			
			break;
			
		case 7:
			db.execSQL("ALTER TABLE " + UserDao.TABLE_NAME + " ADD " + UserDao.COLUMN_NAME_COMMENT+ " TEXT"); // 往表中增加一列
			
			break;
		case 9:
			db.execSQL(GROUPSICON_TABLE_CREATE);
			
			break;

		}
	}

	public void closeDB() {
		if (instance != null) {
			try {
				SQLiteDatabase db = instance.getWritableDatabase();
				db.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			instance = null;
		}
	}

}
