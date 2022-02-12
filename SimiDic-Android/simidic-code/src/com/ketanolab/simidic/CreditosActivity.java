package com.ketanolab.nusimi;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.TextSwitcher;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.ketanolab.nusimi.anim.ImageLogo;
import com.ketanolab.nusimi.anim.TextPrincipal;
import com.ketanolab.nusimi.anim.TextSecundary;

public class CreditosActivity extends SherlockActivity {

	private ImageSwitcher imageSwitcher1;
	private TextSwitcher textSwicher1;
	private TextSwitcher textSwicher2;

	private int[] texts1 = { R.string.present, R.string.licence, R.string.authors, R.string.developer,
			R.string.linguist, R.string.coordinator_and_graphics, R.string.producers, R.string.special_thanks };
	private String[] texts2 = { "", "SimiDic 1.0.1",
			"Felix Layme Pairumani, Teofilo Laime Ajacopa, Comit� HABLE Guaran�", "Daniel Alvarez", "Amos Batto",
			"Pedro Teran", "Hardy Beltran\nClement Lamy",
			"Educaci�n Intercultural Biling�e del Ministerio de Educaci�n\nEstado Plurinacional de Bolivia" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//setTheme(R.style.Theme_Sherlock_Light_DarkActionBar_ForceOverflow);
		setTheme(R.style.Theme_Sherlock_Light_DarkActionBar);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_creditos);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// TextView urlKetanolab = (TextView) findViewById(R.id.urlKetanolab);
		// urlKetanolab.setText("www.ketanolab.com");
		// Linkify.addLinks(urlKetanolab, Linkify.ALL);
		//
		// TextView urlIllaa = (TextView) findViewById(R.id.urlIllaa);
		// urlIllaa.setText("www.illa-a.org");
		// Linkify.addLinks(urlIllaa, Linkify.ALL);

		// --------------------
		imageSwitcher1 = (ImageSwitcher) findViewById(R.id.imageSwitcher1);
		imageSwitcher1.setFactory(new ImageLogo(this));

		textSwicher1 = (TextSwitcher) findViewById(R.id.textSwitcher1);
		textSwicher2 = (TextSwitcher) findViewById(R.id.textSwitcher2);
		textSwicher1.setFactory(new TextSecundary(this));
		textSwicher2.setFactory(new TextPrincipal(this));

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