package com.ketanolab.nusimi;

import java.util.ArrayList;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.TextView;

//LIBRERIAS IMPORTADAS PARA NOTIFICACION PUSH

import static com.ketanolab.nusimi.CommonUtilities.SENDER_ID;
import static com.ketanolab.nusimi.CommonUtilities.SERVER_URL;

import android.os.AsyncTask;

import com.google.android.gcm.GCMRegistrar;

//

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import com.ketanolab.nusimi.util.Constants;
import com.ketanolab.nusimi.util.Dictionaries;
import com.ketanolab.nusimi.util.Util;

public class MainActivity extends SherlockActivity implements
		ActionBar.OnNavigationListener, OnItemClickListener {

	// Paths dictionaries
	private ArrayList<String> pathsDictionaries;

	// Navigation
	private int itemSelectedNavigation = 0;
	private ArrayAdapter<String> listNavigationAdapter;

	// List
	private ListView listView;
	private SimpleCursorAdapter simpleCursorAdapter;
	SQLiteDatabase db;

	// EditText
	private EditText cajaConsulta;

	// NOTIFICACION PUSH
	// alert dialog manager
	// AlertDialogManager alert = new AlertDialogManager();

	// Internet detector
	ConnectionDetector cd;
	// UI elements
	// Asyntask

	AsyncTask<Void, Void, Void> mRegisterTask;

	// END NOTIFICACION PUSH

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// setTheme(R.style.Theme_Sherlock_Light_DarkActionBar_ForceOverflow);
		setTheme(R.style.Theme_Sherlock_Light_DarkActionBar);
		super.onCreate(savedInstanceState);
		Log.i(Constants.DEBUG, "onCreate()");
		setContentView(R.layout.activity_main);
		getSupportActionBar().setDisplayShowTitleEnabled(false); // Hide title
		getSupportActionBar().setDisplayShowHomeEnabled(false); // Hide icon

		// Navigation
		Context context = getSupportActionBar().getThemedContext();
		listNavigationAdapter = new ArrayAdapter<String>(context,
				R.layout.sherlock_spinner_item);
		listNavigationAdapter
				.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);
		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		getSupportActionBar().setListNavigationCallbacks(listNavigationAdapter,
				this);

		// List
		listView = (ListView) findViewById(R.id.lista);
		listView.setOnItemClickListener(this);

		cajaConsulta = (EditText) findViewById(R.id.caja_consulta);
		cajaConsulta.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.toString().length() > 0) {
					simpleCursorAdapter.getFilter().filter(s.toString());
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void afterTextChanged(Editable s) {
			}
		});
		// resgistrando
		try {
			
			ConnectionDetector cd;
			cd = new ConnectionDetector(getApplicationContext());
			registra(cd);

		} catch (Exception e) {

		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.i(Constants.DEBUG, "onStart()");

		// Get dictionaries valid
		pathsDictionaries = Dictionaries.scanDictionariesAndValidation(this);
		// Put Navigation
		putDataDictionariesInNavigation();
		// Load words
		Log.i(Constants.DEBUG, "Cargando palabras...");
		if (pathsDictionaries.size() > 0) {
			db = SQLiteDatabase.openOrCreateDatabase(
					pathsDictionaries.get(itemSelectedNavigation), null);
			loadAllWords();
		}
		// Re-query
		String s = cajaConsulta.getText().toString();
		if (s.toString().length() > 0) {
			simpleCursorAdapter.getFilter().filter(s.toString());
		}
	}

	private void loadAllWords() {
		final String[] columns = { "_id", "word", "summary" };
		int[] to = { 0, R.id.word_item, R.id.meaning_item };
		// Cursor cursor = db.rawQuery("SELECT _id, word, summary FROM words",
		// null);
		Cursor cursor = db
				.query("words", columns, null, null, null, null, null);

		simpleCursorAdapter = new SimpleCursorAdapter(this,
				R.layout.word_list_item, cursor, columns, to, 0);

		simpleCursorAdapter.setFilterQueryProvider(new FilterQueryProvider() {
			public Cursor runQuery(CharSequence constraint) {

				String word = constraint.toString() + "%";
				Cursor cursor = null;
				if (pathsDictionaries.get(itemSelectedNavigation)
						.contains("gn")) {
					cursor = db
							.rawQuery(
									"SELECT _id, word, summary FROM words WHERE replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(word,'�','a'),'�','e'),'�','i'),'�','o'),'�','u'),'(',''),')',''),'�','n'),'�','i'),'�','a'),'�','e'),'�','o'),'�','u') LIKE ?",
									new String[] { word });
				} else {
					cursor = db
							.rawQuery(
									"SELECT _id, word, summary FROM words WHERE replace(replace(replace(replace(replace(replace(replace(word,'�','a'),'�','e'),'�','i'),'�','o'),'�','u'),'�','n'),'�','a') LIKE ?",
									new String[] { word });
				}

				return cursor;
			}
		});

		listView.setAdapter(simpleCursorAdapter);

	}

	private void putDataDictionariesInNavigation() {
		listNavigationAdapter.clear();
		for (int i = 0; i < pathsDictionaries.size(); i++) {
			SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(
					pathsDictionaries.get(i), null);
			String[] nameAndAuthor = Util.getNameAndAuthorDictionary(db);
			listNavigationAdapter.add(nameAndAuthor[0]); // Get name
			db.close();
		}
		listNavigationAdapter.notifyDataSetChanged();
	}

	public boolean onNavigationItemSelected(int position, long id) {
		itemSelectedNavigation = position;
		if (pathsDictionaries.size() > 0) {
			if (db != null) {
				db.close();
			}
			db = SQLiteDatabase.openOrCreateDatabase(
					pathsDictionaries.get(itemSelectedNavigation), null);
			loadAllWords();
		}
		// Re-query
		String word = cajaConsulta.getText().toString();
		if (word.length() > 0) {
			simpleCursorAdapter.getFilter().filter(word);
		}
		return true;
	}

	// ************************* MENU *************************
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home || item.getItemId() == 0) {
			return false;
		}
		switch (item.getItemId()) {
		case R.id.menu_download:
			Intent intentDescarga = new Intent(this, DescargaActivity.class);
			startActivity(intentDescarga);
			break;
		case R.id.menu_favorites:
			Intent intentFavoritos = new Intent(this, FavoritosActivity.class);
			startActivity(intentFavoritos);
			break;
		case R.id.menu_information:
			Intent intentCreditos = new Intent(this, CreditosActivity.class);
			startActivity(intentCreditos);
			break;
		case R.id.menu_dictionaries:
			Intent intentDiccionarios = new Intent(this,
					DiccionariosActivity.class);
			startActivity(intentDiccionarios);
			break;
		}
		return true;
	}

	// ************************* ITEMCLICK *************************
	public void onItemClick(AdapterView<?> arg0, View v, int posicion, long id) {
		// Start PalabraActivity
		String word = ((TextView) v.findViewById(R.id.word_item)).getText()
				.toString();
		Intent intent = new Intent(this, PalabraActivity.class);
		GCMRegistrar.register(this, SENDER_ID);
		intent.putExtra("path", pathsDictionaries.get(getSupportActionBar()
				.getSelectedNavigationIndex()));
		intent.putExtra("word", word);
		startActivity(intent);
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.i(Constants.DEBUG, "onStop()");
		if (simpleCursorAdapter != null) {
			simpleCursorAdapter.getCursor().close();
			simpleCursorAdapter = null;
		}
		if (db != null) {
			db.close();
		}
	}

	// notification push method regis
	// NOTIFICACION PUSH METODOS
	public void registra(ConnectionDetector cdxon) {
			cd = cdxon;


		if (!cd.isConnectingToInternet()) {
			// Internet Connection is not present
			// No hay conexion a internet
			return;
		}

		// verifica si estan las configuracion GCM
		// Check if GCM configuration is set
		if (SERVER_URL == null || SENDER_ID == null || SERVER_URL.length() == 0
				|| SENDER_ID.length() == 0) {
			// GCM sernder id / server url is missing
			// stop executing code by return
			return;
		}

		if (cd.isConnectingToInternet()) {


			// Internet Connection is present

			// Make sure the device has the proper dependencies.

			GCMRegistrar.checkDevice(this);

			// Make sure the manifest was properly set - comment out this line
			// while developing the app, then uncomment it when it's ready.


			GCMRegistrar.checkManifest(this);
			// GCMRegistrar.checkManifest(this);
			// Get GCM registration id

			final String regId = GCMRegistrar.getRegistrationId(this);


			// Check if regid already presents
			if (regId.equals("")) {
				// Registration is not present, register now with GCM


				GCMRegistrar.register(this, SENDER_ID);
			} else {
				// Device is already registered on GCM
				if (GCMRegistrar.isRegisteredOnServer(this)) {
					// Skips registration.
				
				} else {
					// Try to register again, but not in the UI thread.
					// It's also necessary to cancel the thread onDestroy(),
					// hence the use of AsyncTask instead of a raw thread.
					final Context context = this;
					mRegisterTask = new AsyncTask<Void, Void, Void>() {

						@Override
						protected Void doInBackground(Void... params) {
							// Register on our server
							// On server creates a new user
							ServerUtilities.register(context, regId);
							return null;
						}

						@Override
						protected void onPostExecute(Void result) {
							mRegisterTask = null;
						}

					};
					mRegisterTask.execute(null, null, null);
				}
			}

		} else {

			// user doen't filled that data
			// ask him to fill the form

		}

	}



}
