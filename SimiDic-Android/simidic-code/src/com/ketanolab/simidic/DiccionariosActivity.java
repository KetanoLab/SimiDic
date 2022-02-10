package com.ketanolab.nusimi;

import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.ketanolab.nusimi.adapters.AdaptadorViewPager;
import com.ketanolab.nusimi.util.Dictionaries;
import com.viewpagerindicator.CirclePageIndicator;

public class DiccionariosActivity extends SherlockActivity {

	// Paginado
	private ViewPager viewPager;
	private CirclePageIndicator indicator;
	private AdaptadorViewPager adaptadorViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//setTheme(R.style.Theme_Sherlock_Light_DarkActionBar_ForceOverflow);
		setTheme(R.style.Theme_Sherlock_Light_DarkActionBar);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_diccionarios);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// Paginado
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		adaptadorViewPager = new AdaptadorViewPager(this);
		viewPager.setAdapter(adaptadorViewPager);
		indicator = (CirclePageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(viewPager);
		// Fin Paginado

		ArrayList<String> paths = Dictionaries.scanDictionariesAndValidation(this);
		for (int i = 0; i < paths.size(); ++i) {
			SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(paths.get(i), null);
			setDataDictionary(db);
			db.close();
		}
	}

	private void setDataDictionary(SQLiteDatabase db) {
		Cursor cursor = db.rawQuery("SELECT author, language_begin, language_end, description FROM info", null);
		if (cursor.moveToFirst()) {
			String author = cursor.getString(0);
			String beginLanguage = cursor.getString(1);
			String endLanguage = cursor.getString(2);
			String description = cursor.getString(3);
			cursor.close();
			adaptadorViewPager.adicionarItem(R.drawable.img_dictionary, beginLanguage + " - " + endLanguage, author,
					description);
		}
		cursor.close();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			this.finish();
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		return true;

	}

}
