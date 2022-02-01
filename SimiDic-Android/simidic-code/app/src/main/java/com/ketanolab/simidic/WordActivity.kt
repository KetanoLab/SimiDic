package com.ketanolab.simidic;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ketanolab.simidic.db.FavoritosDbAdapter;
import com.ketanolab.simidic.util.Constants;
import com.ketanolab.simidic.util.Util;

public class WordActivity extends AppCompatActivity {

	private TextView textoPalabra;
	private TextView textoSignificado;
	private TextView textoDiccionario;
	private boolean favorite = false;
	private String path = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_word);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		// GUI
		textoPalabra = (TextView) findViewById(R.id.textoPalabra);
		textoSignificado = (TextView) findViewById(R.id.textoSignificado);
		textoDiccionario = (TextView) findViewById(R.id.textoDiccionario);
		// Get extras
		String pathDictionary = getIntent().getStringExtra("path");
		path = pathDictionary;
		String word = getIntent().getStringExtra("word");
		// Meaning
		loadMeanings(pathDictionary, word);

		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(pathDictionary, null);
		try {
			String[] nameAndAuthor = Util.getNameAndAuthorDictionary(db);
			setTitle(nameAndAuthor[0]);
			textoDiccionario.setText(nameAndAuthor[1]);
		} catch (Exception ex) {
			Log.e(Constants.DEBUG, "Error en la consulta a la base de datos.");
		}
		db.close();

		textoPalabra.setText(word);

		if (esFavorito(word, pathDictionary)) {
			favorite = true;
		} else {
			favorite = false;
		}

	}

	private void loadMeanings(String pathDictionary, String word) {
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(pathDictionary, null);
		putAllMeanings(db, word);
		db.close();
	}

	private void putAllMeanings(SQLiteDatabase db, String word) {
		CharSequence meanings = "";
		Cursor cursor = db.rawQuery("SELECT meaning from words where word=?", new String[] { word });
		if (cursor.moveToFirst()) {
			do {
				String meaning = cursor.getString(0);
				// meaning = meaning.replaceAll("<i>", "</i>");
				// meaning = meaning.replaceAll("<span foreground",
				// "<font color");
				// meaning = meaning.replaceAll("</span>", "</font>");
				// ***
				meaning = meaning.replace("\\n", "<br />");
				// ***
				if (meanings.equals("")) {
					meanings = Html.fromHtml(meaning);
				} else {
					meanings = TextUtils.concat(meanings, "\n\n", Html.fromHtml(meaning));
				}
			} while (cursor.moveToNext());
		}
		cursor.close();
		textoSignificado.setText(meanings);

	}

	private boolean esFavorito(String palabra, String nombreDiccionario) {
		FavoritosDbAdapter db = new FavoritosDbAdapter(this);
		db.abrir();
		boolean sw = db.isFavorite(palabra, nombreDiccionario);
		db.close();
		if (sw) {
			return true;
		}
		return false;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (favorite) {
			menu.getItem(0).setIcon(R.drawable.ic_action_star_ok);
		} else {
			menu.getItem(0).setIcon(R.drawable.ic_action_star);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_word, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			this.finish();
		} else if (item.getItemId() == R.id.item_favorite) {
			FavoritosDbAdapter db = new FavoritosDbAdapter(this);
			db.abrir();
			if (favorite) {
				// Delete favorite
				item.setIcon(R.drawable.ic_action_star);
				db.eraseFavorite(textoPalabra.getText().toString(), path);
				favorite = false;
				Toast.makeText(this, R.string.no_favorite_now, Toast.LENGTH_SHORT).show();
			} else {
				// Add favorite
				item.setIcon(R.drawable.ic_action_star_ok);
				db.addFavorite(textoPalabra.getText().toString(), path);
				favorite = true;
				Toast.makeText(this, R.string.favorite_now, Toast.LENGTH_SHORT).show();
			}
			db.close();
		}
		return true;
	}

}
