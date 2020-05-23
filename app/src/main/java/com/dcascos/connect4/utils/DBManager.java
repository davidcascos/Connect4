package com.dcascos.connect4.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {

	private DBHelper dbHelper;
	private Context context;
	private SQLiteDatabase db;

	public DBManager(Context c) {
		context = c;
	}

	public DBManager open() throws SQLException {
		dbHelper = new DBHelper(context);
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		dbHelper.close();
	}

	public void insert(String alias, String date, int size, boolean timing, String time, String result, byte[] image) {
		ContentValues contentValue = new ContentValues();
		contentValue.put(DBHelper.ALIAS, alias);
		contentValue.put(DBHelper.DATE, alias);
		contentValue.put(DBHelper.SIZE, alias);
		contentValue.put(DBHelper.TIMING, alias);
		contentValue.put(DBHelper.TIME, alias);
		contentValue.put(DBHelper.RESULT, alias);
		contentValue.put(DBHelper.IMAGE, alias);
		db.insert(DBHelper.TABLE_NAME, null, contentValue);
	}
}
