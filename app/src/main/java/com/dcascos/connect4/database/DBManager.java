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

	public void insert(String alias, String date, int size, boolean timing, String time, String result, byte[] image, byte[] imageResultado) {
		ContentValues contentValue = new ContentValues();

		if (DBHelper.DB_VERSION < 2) {
			contentValue.put(DBHelper.ALIAS, alias);
			contentValue.put(DBHelper.DATE, date);
			contentValue.put(DBHelper.SIZE, size);
			contentValue.put(DBHelper.TIMING, timing);
			contentValue.put(DBHelper.TIME, time);
			contentValue.put(DBHelper.RESULT, result);
			contentValue.put(DBHelper.IMAGE, image);
			db.insert(DBHelper.TABLE_NAME, null, contentValue);

		} else {
			contentValue.put(DBHelper.ALIAS, alias);
			contentValue.put(DBHelper.DATE, date);
			contentValue.put(DBHelper.SIZE, size);
			contentValue.put(DBHelper.TIMING, timing);
			contentValue.put(DBHelper.TIME, time);
			//EL CAMPO SE MANTIENE YA QUE SINO DESESTRUCTURA TODA LA APLICACION EN CUANTO A LOS CARDVIEWS Y TOAST PERSONALIZADOS
			//PERO IGUALMENTE SE HA ACTUALIZADO LA BBDD PARA QUE GUARDE UN BLOB DE LAS IMAGENES DEL ENUNCIADO EN CUANTO SE MODIFICA LA VERSION
			contentValue.put(DBHelper.RESULT, result);
			contentValue.put(DBHelper.IMAGE, image);
			contentValue.put(DBHelper.IMAGERESULTADO, imageResultado);
			db.insert(DBHelper.TABLE_NAME2, null, contentValue);
		}
	}

	public Cursor getCursor(String[] columns) {
		Cursor cursor;
		if (DBHelper.DB_VERSION < 2) {
			cursor = db.query(DBHelper.TABLE_NAME, columns, null, null, null, null, null);
		} else {
			cursor = db.query(DBHelper.TABLE_NAME2, columns, null, null, null, null, null);
		}
		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}

	public Cursor getCursorByName(String[] columns, String value) {
		Cursor cursor;
		if (DBHelper.DB_VERSION < 2) {
			cursor = db.query(DBHelper.TABLE_NAME, columns, "alias like ?", new String[]{value}, null, null, null);
		} else {
			cursor = db.query(DBHelper.TABLE_NAME2, columns, "alias like ?", new String[]{value}, null, null, null);
		}
		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}

	public void delete(long _id) {
		if (DBHelper.DB_VERSION < 2) {
			db.delete(DBHelper.TABLE_NAME, DBHelper._ID + "=" + _id, null);
		} else {
			db.delete(DBHelper.TABLE_NAME2, DBHelper._ID + "=" + _id, null);

		}
	}

	public int getVersion() {
		return DBHelper.DB_VERSION;
	}
}