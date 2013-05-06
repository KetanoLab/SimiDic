package com.ketanolab.simidic.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Dictionaries {

	public static ArrayList<String> scanDictionariesAndValidation(Context context) {
		ArrayList<String> paths = new ArrayList<String>();
		if (Util.checkExternalStorageAvailable()) {
			File directory = new File(Constants.PATH_DICTIONARIES);
			if (directory.exists()) {
				File[] files = directory.listFiles();
				if (files.length > 0) {
					Arrays.sort(files);
					for (int i = 0; i < files.length; i++) {
						if (Util.checkFilenameDictionary(files[i].getName())) {
							String path = files[i].getAbsolutePath();
							// Last question
							SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(path, null);
							try {
								Util.getNameAndAuthorDictionary(db); // test consistent file
								Log.i(Constants.DEBUG, "Find " + files[i].getName());
								paths.add(path);
							} catch (Exception ex) {
								Log.i(Constants.DEBUG, "El archivo esta dañado " + files[i].getName());
							}
							if (db != null) {
								db.close();
							}
						}
					}
				} else {
					Log.i(Constants.DEBUG, "No hay ningun archivo.");
					Util.showAlertNoDictionaries(context);
				}
			} else {
				Log.i(Constants.DEBUG, "No existe ruta.");
				Util.showAlertNoDictionaries(context);
			}
		} else {
			Log.i(Constants.DEBUG, "No hay SD.");
			Util.showAlertNoExternalStorage(context);
		}
		return paths;
	}
}
