package com.ketanolab.simidic.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavoritosDbAdapter {

	public static final String KEY_PALABRA = "word";
	public static final String KEY_PATH_DICCIONARIO = "path";
	public static final String KEY_ID = "_id";

	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	private static final String DATABASE_CREATE = "create table favorites (_id integer primary key autoincrement, "
			+ "word text not null, path text not null);";

	private static final String DATABASE_NOMBRE = "favorites.db";
	private static final String DATABASE_TABLA = "favorites";
	private static final int DATABASE_VERSION = 1;

	private final Context mCtx;

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NOMBRE, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS favorites");
			onCreate(db);
		}
	}

	public FavoritosDbAdapter(Context ctx) {
		this.mCtx = ctx;
	}

	public FavoritosDbAdapter abrir() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void cerrar() {
		mDbHelper.close();
	}

	public long addFavorite(String palabra, String path) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_PALABRA, palabra);
		initialValues.put(KEY_PATH_DICCIONARIO, path);
		return mDb.insert(DATABASE_TABLA, null, initialValues);
	}

	public boolean deleteFavorite(long rowId) {
		return mDb.delete(DATABASE_TABLA, KEY_ID + "=" + rowId, null) > 0;
	}

	public boolean eliminarFavorito(String palabra, String nombreArchivoDbDiccionario) {
		String[] args = { palabra, nombreArchivoDbDiccionario };
		return mDb.delete(DATABASE_TABLA, "word=? and path=?", args) > 0;
	}

	public Cursor obtenerTodosFavoritos() {
		return mDb.query(DATABASE_TABLA, new String[] { KEY_ID, KEY_PALABRA, KEY_PATH_DICCIONARIO }, null, null, null, null, null);
	}

	public boolean esta(String palabra, String nombreDiccionario) {
		String[] args = new String[] { palabra, nombreDiccionario };
		Cursor cursor = mDb.rawQuery("select * from favorites where word=? and path=?", args);
		if (cursor.moveToFirst()) {
			return true;
		}
		return false;
	}

}
