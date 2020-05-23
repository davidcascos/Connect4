package com.dcascos.connect4.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	// Database Information
	static final String DB_NAME = "CONNECT4.DB";
	// database version
	static final int DB_VERSION = 1;
	// Table Name
	public static final String TABLE_NAME = "GAMES";
	// Table columns
	public static final String _ID = "_id";
	public static final String ALIAS = "alias";
	public static final String DATE = "date";
	public static final String SIZE = "size";
	public static final String TIMING = "timing";
	public static final String TIME = "time";
	public static final String RESULT = "result";
	public static final String IMAGE = "image";


	private static final String createTable = "CREATE TABLE " + TABLE_NAME
			+ "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ ALIAS + " VARCHAR, "
			+ DATE + " TEXT, "
			+ SIZE + " INT, "
			+ TIMING + " BOOLEAN, "
			+ TIME + " TEXT, "
			+ RESULT + " VARCHAR, "
			+ IMAGE + " BLOB);";


	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(createTable);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		db.execSQL(createTable);
	}
}