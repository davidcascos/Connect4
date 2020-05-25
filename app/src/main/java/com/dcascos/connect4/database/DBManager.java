package com.dcascos.connect4.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {

	private DBHelper dbHelper;
	private Context context;
	private SQLiteDatabase db;

	public DBManager(Context c) {
		context = c;
	}

	public DBManager openWrite() throws SQLException {
		dbHelper = new DBHelper(context);
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public DBManager openRead() throws SQLException {
		dbHelper = new DBHelper(context);
		db = dbHelper.getReadableDatabase();
		return this;
	}

	public void close() {
		db.close();
		dbHelper.close();
	}

	public void insert(String alias, String date, int size, boolean timing, String time, String result, byte[] image) {
		ContentValues contentValue = new ContentValues();
		contentValue.put(DBHelper.ALIAS, alias);
		contentValue.put(DBHelper.DATE, date);
		contentValue.put(DBHelper.SIZE, size);
		contentValue.put(DBHelper.TIMING, timing);
		contentValue.put(DBHelper.TIME, time);
		contentValue.put(DBHelper.RESULT, result);
		contentValue.put(DBHelper.IMAGE, image);
		db.insert(DBHelper.TABLE_NAME, null, contentValue);
	}

	public Cursor getCursor(String[] columns) {
		Cursor cursor = db.query(DBHelper.TABLE_NAME, columns, null, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}

//	public int update(long _id, String name, String desc) {
//		ContentValues contentValues = new ContentValues();
//		contentValues.put(DatabaseHelper.SUBJECT, name);
//		contentValues.put(DatabaseHelper.DESC, desc);
//		int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + _id, null);
//		return i;
//	}
//
//	public void delete(long _id) {
//		database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._ID + "=" + _id, null);
//	}
}
