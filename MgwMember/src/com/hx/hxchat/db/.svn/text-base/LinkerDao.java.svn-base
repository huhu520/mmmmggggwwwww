package com.hx.hxchat.db;

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
import com.mgw.member.manager.BaseApplication;

/**
 * 临时会话联系人
 *
 */
public class LinkerDao {

	public static final String TABLE_NAME = "linker";
	/**
	 * mgw用户名
	 */
	public static final String COLUMN_NAME_ID = "username";
	/**
	 * 环信useid
	 */
	public static final String COLUMN_NAME_UserID = "userid";// 登录的用户编号
	/**
	 * 昵称
	 */
	public static final String COLUMN_NAME_NICK = "nick";
	/**
	 * 用户名头像
	 */
	public static final String COLUMN_NAME_PHOTO = "photo";
	
/**
 * 是否是其他缓存  默认-0 不是    ，1是
 */
	public static final String COLUMN_LINKER_USER = "linkeruser";
	private static final String TAG = "LinkerDao";

	private final DbOpenHelper dbHelper;

	public LinkerDao(Context context) {
		dbHelper = DbOpenHelper.getInstance(context);
	}

	public static Boolean checked = false;

	private void CheckTable(SQLiteDatabase db) {
		String USERNAME_TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_NAME_ID + " TEXT, " + COLUMN_NAME_NICK + " TEXT, " + COLUMN_NAME_PHOTO + " TEXT, " + COLUMN_LINKER_USER + " TEXT );";
		if (tabIsExist(TABLE_NAME)) {
			if (!DbOpenHelper.isExistField(db, TABLE_NAME, COLUMN_NAME_NICK) || !DbOpenHelper.isExistField(db, TABLE_NAME, COLUMN_NAME_ID)
					|| !DbOpenHelper.isExistField(db, TABLE_NAME, COLUMN_NAME_PHOTO)|| !DbOpenHelper.isExistField(db, TABLE_NAME, COLUMN_LINKER_USER)) {
				db.execSQL("drop table " + TABLE_NAME);
				db.execSQL(USERNAME_TABLE_CREATE);
			}
		} else {
			db.execSQL(USERNAME_TABLE_CREATE);
		}
		checked = true;

		Log.i(TAG, "CheckTable");
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

		Log.i(TAG, "tabIsExist");
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

	/**
	 * 
	 * 保存好友list
	 * 
	 * @param contactList
	 */
	public void saveContactList(List<User> contactList) {

		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (db.isOpen()) {
			if (!checked) {
				CheckTable(db);
			}
			db.delete(TABLE_NAME, null, null);

			for (User user : contactList) {

				ContentValues values = new ContentValues();

				values.put(COLUMN_NAME_ID, user.getUsername());
				values.put(COLUMN_NAME_UserID, BaseApplication.getApplication().m_user_id);
			
				if (user.getNick() != null)
					values.put(COLUMN_NAME_NICK, user.getNick());
				
				if(user.getAvatar()!=null&&!"".equals(user.getAvatar())){
					values.put(COLUMN_NAME_PHOTO, user.getAvatar());
				}
		
					
				if(user.getLinkcache()!=null&&!"".equals(user.getAvatar())){
					values.put(COLUMN_LINKER_USER, user.getLinkcache());
				}
				
				
				db.insert(TABLE_NAME, null, values);

			}
		}
	}

	/**
	 * 获取linkerlist
	 * 
	 * @return
	 */
	public synchronized Map<String, User> getContactList() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Map<String, User> users = new HashMap<String, User>();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where userid='" + BaseApplication.getApplication().m_user_id + "'"/*
																																			 * +
																																			 * " desc"
																																			 */, null);
			while (!cursor.isClosed()&&cursor.moveToNext()) {
				try {

					String username = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ID));
					String nick = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NICK));
					String photo = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PHOTO));
					String linkercache = cursor.getString(cursor.getColumnIndex(COLUMN_LINKER_USER));
					// String referee = cursor.getString(cursor
					// .getColumnIndex(COLUMN_NAME_REFEREE));
					User user = new User();
					user.setUsername(username);
					user.setNick(nick);
					user.setAvatar(photo);
					user.setLinkcache(linkercache);
					// user.setreferee(referee);
					String headerName = null;
					if (!TextUtils.isEmpty(user.getNick())) {
						headerName = user.getNick();
					} else {
						headerName = user.getUsername();
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
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			
	
			
		
		}
		return users;
	}

	/**
	 * 删除一个联系人
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
			db.close();
		
		}
	}

	/**
	 * 删除自己（从link数据库中）
	 */
	public void deleteContact() {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (db.isOpen()) {
			db.execSQL("delete from " + TABLE_NAME + " where userid<>'" + BaseApplication.getApplication().m_user_id + "'");
			db.close();
		}
	}

	/**
	 * 保存一个联系人
	 * 
	 * @param user
	 */
	public void saveContact(User user) {
		
		
		User contact = getContact(user.getUsername());
		
		if(contact!=null)
			return;
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String strSQL = "insert into " + TABLE_NAME + 
				"(" + COLUMN_NAME_ID + "," 
				+ COLUMN_NAME_UserID + "," 
				+ COLUMN_NAME_NICK + "," 
				+ COLUMN_NAME_PHOTO + "," 
				+ COLUMN_LINKER_USER + ")" + 
				" values ('" 
				+ user.getUsername() + "','"
				+ BaseApplication.getApplication().m_user_id + "','" 
				+  user.getNick() + "','" 
				+  user.getAvatar() + "','" 
				+ user.getLinkcache() + "'" + ")";

		if (db.isOpen()) {
			db.execSQL(strSQL);
			db.close();
		}
		
		
	}
	public void updateContact(User User) {
		
		User contact = getContact(User.getUsername());
		
		if(contact!=null){
			
			deleteContact(User.getUsername());
			
			saveContact(User);
			
		}else{
			saveContact(User);
			
		}
		
		
	}
	
	
	
	public void updateContact(String  user_id,String linkcache) {
		

		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (db.isOpen()) {
////			更新语句：update 表名 set 字段名=值 where 条件子句。如：update person set name=‘传智‘ where id=10
//			db.execSQL("delete from " + TABLE_NAME + " where userid<>'" + BaseApplication.getApplication().m_user_id + "'");
//		
			ContentValues values = new ContentValues();
			values.put(COLUMN_LINKER_USER, linkcache);//key为字段名，value为值
			db.update(TABLE_NAME, values, COLUMN_NAME_ID+"=?", new String[]{user_id}); 
//			db.close();
		}

	}
	
	
	
	public Boolean containsKey(String pKey) {
		Boolean oTrue = false;
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where " + COLUMN_NAME_ID + " = '" + pKey + "' and userid='" + BaseApplication.getApplication().m_user_id + "'", null);
			while (cursor.moveToNext()) {
				oTrue = true;
			}
			cursor.close();
		}

		return oTrue;
	}

	public User getContact(String mobile) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		User user = null;
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where " + COLUMN_NAME_ID + " = " + mobile , null);
			while (cursor.moveToNext()) {
				try {

					String username = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ID));
					String nick = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NICK));
					String photo = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PHOTO));
					String linkercache = cursor.getString(cursor.getColumnIndex(COLUMN_LINKER_USER));

					if(username==null||"".equals(username)){
						return null;
					}else{
						user=new User();
						user.setUsername(username);
						user.setNick(nick);
						user.setAvatar(photo);
						user.setLinkcache(linkercache);
					}
					

				} catch (Exception e) {
					return null;
				}
			}
			cursor.close();
		}
		return user;
	}
}
