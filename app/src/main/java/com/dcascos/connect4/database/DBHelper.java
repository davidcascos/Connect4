package com.dcascos.connect4.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.dcascos.connect4.R;
import com.dcascos.connect4.logic.Status;

import java.io.ByteArrayOutputStream;

public class DBHelper extends SQLiteOpenHelper {
	Context context;

	// Database Information
	static final String DB_NAME = "CONNECT4.DB";
	// database version
	static final int DB_VERSION = 1;
	// Table Name
	public static final String TABLE_NAME = "GAMES";
	public static final String TABLE_NAME2 = "PARTIDAS2";

	// Table columns
	public static final String _ID = "_id";
	public static final String ALIAS = "alias";
	public static final String DATE = "date";
	public static final String SIZE = "size";
	public static final String TIMING = "timing";
	public static final String TIME = "time";
	public static final String RESULT = "result";
	public static final String IMAGE = "image";
	public static final String IMAGERESULTADO = "imageResultado";

	private static final String createTable = "CREATE TABLE " + TABLE_NAME
			+ "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ ALIAS + " VARCHAR, "
			+ DATE + " TEXT, "
			+ SIZE + " INT, "
			+ TIMING + " BOOLEAN, "
			+ TIME + " TEXT, "
			+ RESULT + " VARCHAR, "
			+ IMAGE + " BLOB);";

	private static final String sqlCreate2 = "CREATE TABLE " + TABLE_NAME2
			+ "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ ALIAS + " VARCHAR, "
			+ DATE + " TEXT, "
			+ SIZE + " INT, "
			+ TIMING + " BOOLEAN, "
			+ TIME + " TEXT, "
			+ RESULT + " VARCHAR, "
			+ IMAGE + " BLOB, "
			+ IMAGERESULTADO + " BLOB);";

	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(createTable);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion < newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
			db.execSQL(sqlCreate2);
			upgradeVersion(db);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		} else {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			db.execSQL(createTable);

		}
	}

	private void upgradeVersion(SQLiteDatabase db) {
		String[] columns = new String[]{DBHelper._ID, DBHelper.ALIAS, DBHelper.DATE, DBHelper.SIZE, DBHelper.TIMING,
				DBHelper.TIME, DBHelper.RESULT, DBHelper.IMAGE};
		Cursor cursor = db.query(DBHelper.TABLE_NAME, columns, null, null, null, null, null);

		ContentValues contentValue = new ContentValues();

		if (cursor.moveToFirst()) {
			do {
				contentValue.put(DBHelper.ALIAS, cursor.getString(cursor.getColumnIndex(DBHelper.ALIAS)));
				contentValue.put(DBHelper.DATE, cursor.getString(cursor.getColumnIndex(DBHelper.DATE)));
				contentValue.put(DBHelper.SIZE, cursor.getString(cursor.getColumnIndex(DBHelper.SIZE)));
				contentValue.put(DBHelper.TIMING, cursor.getString(cursor.getColumnIndex(DBHelper.TIMING)));
				contentValue.put(DBHelper.TIME, cursor.getString(cursor.getColumnIndex(DBHelper.TIME)));
				//EL CAMPO SE MANTIENE YA QUE SINO DESESTRUCTURA TODA LA APLICACION EN CUANTO A LOS CARDVIEWS Y TOAST PERSONALIZADOS
				//PERO IGUALMENTE SE HA ACTUALIZADO LA BBDD PARA QUE GUARDE UN BLOB DE LAS IMAGENES DEL ENUNCIADO EN CUANTO SE MODIFICA LA VERSION
				contentValue.put(DBHelper.RESULT, cursor.getString(cursor.getColumnIndex(DBHelper.RESULT)));
				contentValue.put(DBHelper.IMAGE, cursor.getBlob(cursor.getColumnIndex(DBHelper.IMAGE)));

				Bitmap icon;

				if (cursor.getString(cursor.getColumnIndex(DBHelper.RESULT)).equals(Status.PLAYER1_WINS.toString())) {
					icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.victoria);
				} else if (cursor.getString(cursor.getColumnIndex(DBHelper.RESULT)).equals(Status.DRAW.toString())) {
					icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.empate);
				} else if (cursor.getString(cursor.getColumnIndex(DBHelper.RESULT)).equals(Status.ALL_PLAYER_LOSE.toString())) {
					icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.tiempoagotado);
				} else {
					icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.derrota);
				}

				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				icon.compress(Bitmap.CompressFormat.PNG, 0, stream);

				contentValue.put(DBHelper.IMAGERESULTADO, stream.toByteArray());

				db.insert(DBHelper.TABLE_NAME2, null, contentValue);


			} while (cursor.moveToNext());
		}
	}

}