package com.ketanolab.simidic;

import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;


import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.ketanolab.simidic.adapters.DictionariesViewPagerAdapter;
import com.ketanolab.simidic.util.Dictionaries;
import com.ketanolab.simidic.viewpager.CirclePageIndicator;

public class DiccionariesActivity extends AppCompatActivity {

	// Paginado
	private ViewPager viewPager;
	private CirclePageIndicator indicator;
	private DictionariesViewPagerAdapter adaptadorViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dictionaries);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// Paginado
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		adaptadorViewPager = new DictionariesViewPagerAdapter(this);
		viewPager.setAdapter(adaptadorViewPager);
		indicator = (CirclePageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(viewPager);
		// Fin Paginado
		getDictionaries();


	}

	public void getDictionaries() {
		ArrayList<String> paths = Dictionaries.scanDictionariesAndValidation(this);
		for (int i = 0; i < paths.size(); ++i) {
			SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(paths.get(i), null);
			setDictionaryData(db);
			db.close();
		}
	}
	private void setDictionaryData(SQLiteDatabase db) {
		Cursor cursor = db.rawQuery("SELECT author, language_begin, language_end, description FROM info", null);
		if (cursor.moveToFirst()) {
			String author = cursor.getString(0);
			String beginLanguage = cursor.getString(1);
			String endLanguage = cursor.getString(2);
			String description = cursor.getString(3);
			cursor.close();
			adaptadorViewPager.adicionarItem(R.drawable.img_dictionary, beginLanguage + " - " + endLanguage, author,
					description);
			adaptadorViewPager.notifyDataSetChanged();
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
