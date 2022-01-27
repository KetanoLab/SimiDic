package com.ketanolab.simidic;

import java.util.ArrayList;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

//LIBRERIAS IMPORTADAS PARA NOTIFICACION PUSH


import android.os.AsyncTask;


//


import com.ketanolab.simidic.UIUtils.MenuDrawer;
import com.ketanolab.simidic.adapters.MenuListAdapter;
import com.ketanolab.simidic.util.Constants;
import com.ketanolab.simidic.util.Dictionaries;
import com.ketanolab.simidic.util.Util;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

public class MainActivity extends AppCompatActivity implements  ListView.OnItemClickListener, ActionBar.OnNavigationListener{

	// Paths dictionaries
	private ArrayList<String> pathsDictionaries;

	// Navigation
	private int itemSelectedNavigation = 0;
	private ArrayAdapter<String> listNavigationAdapter;

	// List words
	private ListView wordsListView;
	private SimpleCursorAdapter WordsSimpleCursorAdapter;
	SQLiteDatabase db;

	//list menu
	private ListView menuListView;
	private MenuListAdapter menuListAdapter;
	// EditText
	private EditText askBox;


	//MenuDrawer Button
	private ActionBarDrawerToggle mMenu;

	AsyncTask<Void, Void, Void> mRegisterTask;

	// END NOTIFICACION PUSH

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(Constants.DEBUG, "onCreate()");
		setContentView(R.layout.activity_main);

		//menuListView.setOnItemClickListener(this);
		// Navigation
		Context context = getSupportActionBar().getThemedContext();
		listNavigationAdapter = new ArrayAdapter<String>(context,
				R.layout.action_bar_spinner_list_item);
		listNavigationAdapter
				.setDropDownViewResource(R.layout.action_bar_spinner_list_item);

		final android.app.ActionBar actionBar = getActionBar();
		// List
		wordsListView = (ListView) findViewById(R.id.lista);
		wordsListView.setOnItemClickListener(this);
		getSupportActionBar().setDisplayShowTitleEnabled(false); // Hide title
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		getSupportActionBar().setListNavigationCallbacks(listNavigationAdapter,
				this);
		DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mMenu = MenuDrawer.getMenuDrawer(this,getSupportActionBar(),drawerLayout);
		menuListView = (ListView)findViewById(R.id.list_slidermenu);
		menuListView.setAdapter(new MenuListAdapter(this));
		askBox = (EditText) findViewById(R.id.caja_consulta);
		askBox.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
									  int count) {
				if (s.toString().length() > 0) {
					WordsSimpleCursorAdapter.getFilter().filter(s.toString());
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {
			}

			public void afterTextChanged(Editable s) {
			}
		});

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
		String s = askBox.getText().toString();
		if (s.toString().length() > 0) {
			WordsSimpleCursorAdapter.getFilter().filter(s.toString());
		}
	}

	private void loadAllWords() {
		final String[] columns = { "_id", "word", "summary" };
		int[] to = { 0, R.id.word_item, R.id.meaning_item };
		// Cursor cursor = db.rawQuery("SELECT _id, word, summary FROM words",
		// null);
		Cursor cursor = db
				.query("words", columns, null, null, null, null, null);

		WordsSimpleCursorAdapter = new SimpleCursorAdapter(this,
				R.layout.word_list_item, cursor, columns, to, 0);

		WordsSimpleCursorAdapter.setFilterQueryProvider(new FilterQueryProvider() {
			public Cursor runQuery(CharSequence constraint) {

				String word = "%" + constraint.toString() + "%";
				Cursor cursor = null;
				if (pathsDictionaries.get(itemSelectedNavigation)
						.contains("gn")) {
					cursor = db
							.rawQuery(
									"SELECT _id, word, summary FROM words WHERE replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(word,'�','a'),'�','e'),'�','i'),'�','o'),'�','u'),'(',''),')',''),'�','n'),'�','i'),'�','a'),'�','e'),'�','o'),'�','u') LIKE ?",
									new String[]{word});
				} else {
					cursor = db
							.rawQuery(
									"SELECT _id, word, summary FROM words WHERE replace(replace(replace(replace(replace(replace(replace(word,'�','a'),'�','e'),'�','i'),'�','o'),'�','u'),'�','n'),'�','a') LIKE ?",
									new String[]{word});
				}

				return cursor;
			}
		});

		wordsListView.setAdapter(WordsSimpleCursorAdapter);

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
		String word = askBox.getText().toString();
		if (word.length() > 0) {
			WordsSimpleCursorAdapter.getFilter().filter(word);
		}
		return true;
	}

	// ************************* MENU *************************
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mMenu.onOptionsItemSelected(item)) {
			return true;
		}
		switch (item.getItemId()) {
		case R.id.menu_download:
			Intent intentDescarga = new Intent(this, DownloadsActivity.class);
			startActivity(intentDescarga);
			break;
		case R.id.menu_favorites:
			Intent intentFavoritos = new Intent(this, FavoritesActivity.class);
			startActivity(intentFavoritos);
			break;
		case R.id.menu_information:
			Intent intentCreditos = new Intent(this, CreditsActivity.class);
			startActivity(intentCreditos);
			break;
		case R.id.menu_dictionaries:
			Intent intentDiccionarios = new Intent(this,
					DiccionariesActivity.class);
			startActivity(intentDiccionarios);
			break;
		}
		return true;
	}

	// ************************* ITEMCLICK *************************
	public void onItemClick(AdapterView<?> arg0, View v, int posicion, long id) {
		// Start WordActivity
		String word = ((TextView) v.findViewById(R.id.word_item)).getText()
				.toString();
		Intent intent = new Intent(this, WordActivity.class);
		intent.putExtra("path", pathsDictionaries.get(getSupportActionBar()
				.getSelectedNavigationIndex()));
		intent.putExtra("word", word);
		startActivity(intent);
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.i(Constants.DEBUG, "onStop()");
		if (WordsSimpleCursorAdapter != null) {
			WordsSimpleCursorAdapter.getCursor().close();
			WordsSimpleCursorAdapter = null;
		}
		if (db != null) {
			db.close();
		}
	}



}
