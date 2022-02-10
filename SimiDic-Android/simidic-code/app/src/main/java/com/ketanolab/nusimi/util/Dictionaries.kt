package com.ketanolab.nusimi.util

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import java.util.ArrayList
import java.util.Arrays

object Dictionaries {
    fun scanDictionariesAndValidation(context: Context): ArrayList<String> {
        val paths = ArrayList<String>()
        if (Util.checkExternalStorageAvailable()) {
            val directory = context.getExternalFilesDir(null)
            if (directory!!.exists()) {
                val files = directory.listFiles()
                if (files.size > 0) {
                    Arrays.sort(files)
                    for (i in files.indices) {
                        if (Util.checkFilenameDictionary(files[i].name)) {
                            val path = files[i].absolutePath
                            // Last question
                            val db = SQLiteDatabase.openOrCreateDatabase(path, null)
                            try {
                                Util.getNameAndAuthorDictionary(db) // test consistent file
                                Log.i(Constants.DEBUG, "Find " + files[i].name)
                                paths.add(path)
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
                    Util.showAlertNoDictionaries(context)
                }
            } else {
                Log.i(Constants.DEBUG, "No existe ruta.")
                Util.showAlertNoDictionaries(context)
            }
        } else {
            Log.i(Constants.DEBUG, "No hay SD.")
            Util.showAlertNoExternalStorage(context)
        }
        return paths
    }
}