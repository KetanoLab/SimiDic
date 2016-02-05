package com.ketanolab.simidic;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.TextSwitcher;

import com.ketanolab.simidic.anim.ImageLogo;
import com.ketanolab.simidic.anim.PrincipalText;
import com.ketanolab.simidic.anim.SecundaryText;

public class CreditsActivity extends ActionBarActivity {

	private ImageSwitcher imageSwitcher1;
	private TextSwitcher textSwicher1;
	private TextSwitcher textSwicher2;

	private int[] texts1 = { R.string.present, R.string.licence, R.string.authors, R.string.developer,
			R.string.linguist, R.string.coordinator_and_graphics, R.string.producers, R.string.special_thanks };
	private String[] texts2 = { "", "SimiDic 1.0.1",
			"Felix Layme Pairumani, Teofilo Laime Ajacopa, Comité HABLE Guaraní \n  Saturnino Callo, Elio Ortiz", "la comunidad", "Amos Batto",
			"Pedro Teran", "Amos Bato/Pedro Teran",
			"Educación Intercultural Bilingüe del Ministerio de Educación\nEstado Plurinacional de Bolivia" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(android.support.v7.appcompat.R.style.Theme_AppCompat_Light_DarkActionBar);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_credits);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);



		// --------------------
		imageSwitcher1 = (ImageSwitcher) findViewById(R.id.imageSwitcher1);
		imageSwitcher1.setFactory(new ImageLogo(this));

		textSwicher1 = (TextSwitcher) findViewById(R.id.textSwitcher1);
		textSwicher2 = (TextSwitcher) findViewById(R.id.textSwitcher2);
		textSwicher1.setFactory(new SecundaryText(this));
		textSwicher2.setFactory(new PrincipalText(this));

		Animation animationIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
		Animation animationOut = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);

		imageSwitcher1.setInAnimation(animationIn);
		imageSwitcher1.setOutAnimation(animationOut);

		textSwicher1.setInAnimation(animationIn);
		textSwicher1.setOutAnimation(animationOut);
		textSwicher2.setInAnimation(animationIn);
		textSwicher2.setOutAnimation(animationOut);

		new AnimationSwitcher().execute();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			this.finish();
		}
		return true;
	}

	public class AnimationSwitcher extends AsyncTask<Void, Integer, Void> {

		@Override
		protected void onPreExecute() {
			textSwicher1.setVisibility(View.GONE);
			textSwicher2.setVisibility(View.GONE);
			textSwicher1.setText("");
			textSwicher2.setText("");
			imageSwitcher1.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			publishProgress(-2);
			SystemClock.sleep(1200);
			publishProgress(-1);
			SystemClock.sleep(1200);
			publishProgress(0);
			SystemClock.sleep(1300);
			publishProgress(1);
			SystemClock.sleep(2000);
			for (int i = 2; i < texts1.length; i++) {
				publishProgress(i);
				SystemClock.sleep(1300);
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			if (values[0] == -2) {
				imageSwitcher1.setImageResource(R.drawable.logo_ketanolab);
			} else if (values[0] == -1) {
				imageSwitcher1.setImageResource(0);
				imageSwitcher1.setImageResource(R.drawable.logo_illa);
			} else {
				imageSwitcher1.setVisibility(View.GONE);
				imageSwitcher1.setImageResource(0);
				textSwicher1.setText("");
				textSwicher2.setText("");
				textSwicher1.setVisibility(View.VISIBLE);
				textSwicher2.setVisibility(View.VISIBLE);
				textSwicher1.setText(getResources().getString(texts1[values[0]]));
				textSwicher2.setText(texts2[values[0]]);
			}
		}

		@Override
		protected void onPostExecute(Void result) {

			new AnimationSwitcher().execute();
		}

	}

}