package com.hx.hxchat.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hx.hxchat.domain.GroupInfo;
import com.hx.hxchat.domain.TopUser;
import com.hx.hxchat.domain.User;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class GroupsDao {
    public static final String TABLE_NAME = "groups";
    public static final String COLUMN_NAME_ID = "groupid";
    public static final String COLUMN_NAME_GROUP = "groupname";
    public static final String COLUMN_NAME_TIME = "time";
    public static final String COLUMN_NAME_ICON = "groupicon";

    private DbOpenHelper dbHelper;

    public GroupsDao(Context context) {
        dbHelper = DbOpenHelper.getInstance(context);
    }

    /**
     * 保存好友list
     * 
     * @param contactList
     */
    public void saveGroupInfoList(List<GroupInfo> contactList) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete(TABLE_NAME, null, null);
            for (GroupInfo user : contactList) {
                ContentValues values = new ContentValues();
                values.put(COLUMN_NAME_ID, user.getGroupId());
                values.put(COLUMN_NAME_GROUP, user.getGroupName());

                values.put(COLUMN_NAME_TIME, user.getLastModifiedTime());

                values.put(COLUMN_NAME_ICON, user.getGroupIcon());
                db.replace(TABLE_NAME, null, values);
            }
        }
    }

    /**
     * 获取好友list
     * 
     * @return
     */
    @SuppressLint("DefaultLocale")
    public Map<String, GroupInfo> getGroupInfoList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Map<String, GroupInfo> users = new HashMap<String, GroupInfo>();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + TABLE_NAME
                    + " order by time asc ", null);
            while (cursor.moveToNext()) {
                String username = cursor.getString(cursor
                        .getColumnIndex(COLUMN_NAME_ID));
                long time = cursor.getLong(cursor
                        .getColumnIndex(COLUMN_NAME_TIME));
                String is_group = cursor.getString(cursor
                        .getColumnIndex(COLUMN_NAME_ICON));
                GroupInfo user = new GroupInfo(username);
                user.setGroupIcon(is_group);
                users.put(username, user);
            }
            cursor.close();
        }
        return users;
    }

    /**
     * 删除一个联系人
     * 
     * @param username
     */
    public void deleteGroupInfo(String username) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete(TABLE_NAME, COLUMN_NAME_ID + " = ?",
                    new String[] { username });
        }
    }

    /**
     * 保存一个联系人
     * 
     * @param user
     */
    public void saveGroupInfo(GroupInfo user) {
    	
    	
    	if(getGropsIcon(user.getGroupId())!=null){
    		return;
    	}
    	
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_ID, user.getGroupId());

        values.put(COLUMN_NAME_GROUP, user.getGroupName());

        values.put(COLUMN_NAME_ICON, user.getGroupIcon());
        if (db.isOpen()) {
            db.replace(TABLE_NAME, null, values);
        }
    }
    
    public String getGropsIcon(String mobile) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		String icon="";
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where " + COLUMN_NAME_ID + " = " + mobile , null);
			while (cursor.moveToNext()) {
				try {

				icon = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ICON));
			
					if(icon==null||"".equals(icon)){
						return null;
					}else{
						return icon;
					}
					

				} catch (Exception e) {
					return null;
				}
			}
			cursor.close();
		}
		return null;
	}
}
