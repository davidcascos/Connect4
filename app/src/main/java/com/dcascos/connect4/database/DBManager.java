package com.dcascos.connect4.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {

	private DBHelper dbHelper;
	private final Context context;
	private SQLiteDatabase db;

	public DBManager(Context c) {
		context = c;
	}

	public void openWrite() throws SQLException {
		dbHelper = new DBHelper(context);
		db = dbHelper.getWritableDatabase();
	}

	public void openRead() throws SQLException {
		dbHelper = new DBHelper(context);
		db = dbHelper.getReadableDatabase();
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

	public Cursor getCursorByName(String[] columns, String value) {
		Cursor cursor = db.query(DBHelper.TABLE_NAME, columns, "alias like ?", new String[]{value}, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}

	public void delete(long _id) {
		db.delete(DBHelper.TABLE_NAME, DBHelper._ID + "=" + _id, null);
	}
}