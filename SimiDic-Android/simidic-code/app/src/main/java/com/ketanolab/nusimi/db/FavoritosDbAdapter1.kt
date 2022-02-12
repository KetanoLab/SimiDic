package com.ketanolab.nusimi.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class FavoritosDbAdapter(private val mCtx: Context) {
    private var mDbHelper: DatabaseHelper? = null
    private var mDb: SQLiteDatabase? = null

    private class DatabaseHelper internal constructor(context: Context?) :
        SQLiteOpenHelper(context, DATABASE_NOMBRE, null, DATABASE_VERSION) {
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(DATABASE_CREATE)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS favorites")
            onCreate(db)
        }
    }

    @Throws(SQLException::class)
    fun abrir(): FavoritosDbAdapter {
        mDbHelper = DatabaseHelper(mCtx)
        mDb = mDbHelper!!.writableDatabase
        return this
    }

    fun close() {
        mDbHelper!!.close()
    }

    fun addFavorite(palabra: String?, path: String?): Long {
        val initialValues = ContentValues()
        initialValues.put(KEY_PALABRA, palabra)
        initialValues.put(KEY_PATH_DICCIONARIO, path)
        return mDb!!.insert(DATABASE_TABLA, null, initialValues)
    }

    fun eraseFavorite(rowId: Long): Boolean {
        return mDb!!.delete(DATABASE_TABLA, KEY_ID + "=" + rowId, null) > 0
    }

    fun eraseFavorite(palabra: String, nombreArchivoDbDiccionario: String): Boolean {
        val args = arrayOf(palabra, nombreArchivoDbDiccionario)
        return mDb!!.delete(DATABASE_TABLA, "word=? and path=?", args) > 0
    }

    val favorites: Cursor
        get() = mDb!!.query(
            DATABASE_TABLA,
            arrayOf(KEY_ID, KEY_PALABRA, KEY_PATH_DICCIONARIO),
            null,
            null,
            null,
            null,
            null
        )

    fun isFavorite(word: String, dictionaryName: String): Boolean {
        val args = arrayOf(word, dictionaryName)
        val cursor = mDb!!.rawQuery("select * from favorites where word=? and path=?", args)
        return if (cursor.moveToFirst()) {
            true
        } else false
    }

    companion object {
        const val KEY_PALABRA = "word"
        const val KEY_PATH_DICCIONARIO = "path"
        const val KEY_ID = "_id"
        private const val DATABASE_CREATE =
            ("create table favorites (_id integer primary key autoincrement, "
                    + "word text not null, path text not null);")
        private const val DATABASE_NOMBRE = "favorites.db"
        private const val DATABASE_TABLA = "favorites"
        private const val DATABASE_VERSION = 1
    }
}