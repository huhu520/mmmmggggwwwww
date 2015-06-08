package com.mgw.member.uitls;

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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 折扣dao
 */
public class DiscountDao {
	public static final String TABLE_NAME = "discount";
	public static final String DISCOUNT_SHOPID = "id";
	public static final String DISCOUNT_SHOP_DISCOUNT = "discount";

	private SQLiteDatabase dbHelper;

	public DiscountDao(Context context) {
		dbHelper = DBControl.GetDB(context);
	}

	public void delete(String id) {
		SQLiteDatabase db = dbHelper;
		if (db.isOpen()) {
			db.delete(TABLE_NAME, DISCOUNT_SHOPID + " = ?", new String[] { id });
		}
	}

	public void save(String id, String discount) {
		delete(id);
		SQLiteDatabase db = dbHelper;
		ContentValues values = new ContentValues();
		values.put(DISCOUNT_SHOPID, id);
		values.put(DISCOUNT_SHOP_DISCOUNT, discount);
		if (db.isOpen()) {
			db.insert(TABLE_NAME, null, values);
		}
	}

	public String getDiscount(String id) {
		String resultString = "0";
		SQLiteDatabase db = dbHelper;
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where " + DISCOUNT_SHOPID + " = " + id, null);
			while (cursor.moveToNext()) {
				try {

					resultString = cursor.getString(cursor.getColumnIndex(DISCOUNT_SHOP_DISCOUNT));

				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			cursor.close();
		}

		return resultString;
	}

}
