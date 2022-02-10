package com.ketanolab.nusimi.util

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.net.ConnectivityManager
import android.os.Environment
import android.util.Log
import com.ketanolab.nusimi.DownloadsActivity
import com.ketanolab.nusimi.R
import java.io.File

object Util {
    fun isOnline(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cm.activeNetworkInfo
        return if (networkInfo != null && networkInfo.isConnected) {
            true
        } else false
    }

    fun isDownloaded(file: String): Boolean {
        var sw = false
        if (checkExternalStorageAvailable()) {
            val directory = File(file)
            if (directory.exists()) {
                val files = directory.parentFile.listFiles()
                if (files.size > 0) {
                    for (i in files.indices) {
                        if (checkFilenameDictionary(files[i].name)) {
                            val path = files[i].absolutePath
                            // Last question
                            val db = SQLiteDatabase.openOrCreateDatabase(path, null)
                            try {
                                getNameAndAuthorDictionary(db) // test
                                // consistent
                                // file
                                if (files[i].name == file) {
                                    Log.i(Constants.DEBUG, "Find " + files[i].name)
                                    sw = true
                                }
                            } catch (ex: Exception) {
                                Log.i(
                                    Constants.DEBUG,
                                    "El archivo isFavorite dañado " + files[i].name
                                )
                            }
                            db?.close()
                        }
                    }
                } else {
                    Log.i(Constants.DEBUG, "No hay ningun archivo.")
                }
            } else {
                Log.i(Constants.DEBUG, "No existe ruta.")
            }
        } else {
            Log.i(Constants.DEBUG, "No hay SD.")
        }
        return sw
    }

    fun checkFilenameDictionary(name: String): Boolean {
        return if (name.length == 11 && name.contains(".db")) {
            true
        } else false
    }

    fun checkExternalStorageAvailable(): Boolean {
        val state = Environment.getExternalStorageState()
        return if (Environment.MEDIA_MOUNTED == state) {
            true
        } else false
    }

    fun showAlertNoExternalStorage(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.sd_required)
        builder.setMessage(R.string.you_dont_have_sd)
        builder.setPositiveButton(android.R.string.ok) { dialog, id -> (context as Activity).finish() }
        val alert = builder.create()
        alert.setCancelable(false)
        alert.show()
    }

    fun showAlertNoDictionaries(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.download_dictionaries)
        builder.setMessage(R.string.you_dont_have_dictionaries)
        builder.setPositiveButton(android.R.string.yes) { dialog, id ->
            val intent = Intent(context, DownloadsActivity::class.java)
            context.startActivity(intent)
        }
        builder.setNegativeButton(android.R.string.no) { dialog, id -> (context as Activity).finish() }
        val alert = builder.create()
        alert.setCancelable(false)
        alert.show()
    }

    fun showAlertNoInternet(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.no_internet)
        builder.setMessage(R.string.you_dont_have_conection)
        builder.setCancelable(false)
        builder.setPositiveButton(android.R.string.ok) { dialog, id -> (context as Activity).finish() }
        val alert = builder.create()
        alert.show()
    }

    fun getNameAndAuthorDictionary(db: SQLiteDatabase): Array<String>? {
        val cursor = db.rawQuery("SELECT name, author FROM info", null)
        if (cursor.moveToFirst()) {
            val name = cursor.getString(0)
            val author = cursor.getString(1)
            cursor.close()
            return arrayOf(name, author)
        }
        cursor.close()
        return null
    }
}