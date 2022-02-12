package com.ketanolab.nusimi;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.BitSet;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.ketanolab.nusimi.adapters.DownloadsListAdapter;
import com.ketanolab.nusimi.util.Constants;
import com.ketanolab.nusimi.util.Util;

public class DescargaActivity extends SherlockActivity implements OnItemClickListener {

	// URLs
	private ArrayList<String> urls;
	private ArrayList<String> fileNames;

	// List
	private ListView listView;
	private DownloadsListAdapter listAdapter;

	// Loading
	private RelativeLayout layoutCargando;
	private RelativeLayout layoutMensaje;

	// Tasks
	private ArrayList<DownloadFile> tasks;
	private BitSet downloading;

	private PowerManager.WakeLock wl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Theme_Sherlock_Light_DarkActionBar);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_descarga);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true); // show home icon

		// Loading
		layoutCargando = (RelativeLayout) findViewById(R.id.layoutCargando);
		layoutMensaje = (RelativeLayout) findViewById(R.id.layoutMensaje);
		layoutMensaje.setVisibility(View.GONE);

		// List
		listView = (ListView) findViewById(R.id.lista);
		listAdapter = new DownloadsListAdapter(this);
		listView.setAdapter(listAdapter);
		listView.setOnItemClickListener(this);

		if (Util.isOnline(this)) {
			new LoadJSON().execute(Constants.URL_JSON);
		} else {
			Util.showAlertNoInternet(this);
		}

		tasks = new ArrayList<DownloadFile>();
	}

	//method Deprecated 
	@Override
	protected void onStart() {
		super.onStart();
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "Simidic");
		wl.acquire();
	}

	public class LoadJSON extends AsyncTask<String, String, Void> {

		@Override
		protected void onPreExecute() {
			listAdapter.eliminarTodo();
			urls = new ArrayList<String>();
			fileNames = new ArrayList<String>();
			downloading = new BitSet();
			layoutMensaje.setVisibility(View.GONE);
			layoutCargando.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(String... values) {
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(Constants.URL_JSON);
			httpGet.setHeader("content-type", "application/json");
			try {
				HttpResponse httpResponse = httpClient.execute(httpGet);
				String resultado = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);
				JSONArray jsonArray = new JSONArray(resultado);
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					String name = jsonObject.getString("name");
					String author = jsonObject.getString("author");
					String descripcion = jsonObject.getString("description");
					String file = jsonObject.getString("file");
					String url = jsonObject.getString("url");
					String size = jsonObject.getString("size");
					publishProgress(name, author, descripcion, file, url, size);
				}
			} catch (Exception ex) {
				Log.i(Constants.DEBUG, "Error al cargar JSON: " + ex.toString());
			}

			return null;
		}

		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);
			if (!Util.isDownloaded(values[3])) {
				fileNames.add(values[3]);
				urls.add(values[4]);
				listAdapter.adicionarItem(R.drawable.ic_menu_download, values[0], values[1], values[2], values[5]);
				Log.i(Constants.DEBUG, "Added item download: " + values[0]);
			}
			Log.i(Constants.DEBUG, "Added item (all) download: " + values[0]);
		}

		@Override
		protected void onPostExecute(Void result) {
			layoutCargando.setVisibility(View.GONE);
			if (listAdapter.getCount() == 0) {
				layoutMensaje.setVisibility(View.VISIBLE);
			}
			for (int i = 0; i < listAdapter.getCount(); i++) {
				tasks.add(new DownloadFile(i));
			}
		}
	}

	public void onItemClick(AdapterView<?> arg0, View view, int posicion, long id) {
		Log.i(Constants.DEBUG, "Descargando... " + fileNames.get(posicion));
		if (downloading.get(posicion)) {
			Toast.makeText(this, R.string.is_downloaded_or_downloading, Toast.LENGTH_SHORT).show();
		} else {
			tasks.add(posicion, new DownloadFile(posicion));
			tasks.get(posicion).execute(urls.get(posicion), fileNames.get(posicion));
			downloading.set(posicion);
		}
	}

	private class DownloadFile extends AsyncTask<String, Integer, Long> {

		private int position;
		private long fileLength;

		public DownloadFile(int position) {
			this.position = position;
			this.fileLength = 0;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			listAdapter.updateItem(position, R.drawable.ic_action_cancel, getResources()
					.getString(R.string.downloading), true);
		}

		@Override
		protected Long doInBackground(String... args) {
			try {
				File directorio = new File(Constants.PATH_DICTIONARIES);
				if (!directorio.exists()) {
					directorio.mkdirs();
				}
				URL url = new URL(args[0]);
				URLConnection connection = url.openConnection();
				connection.connect();

				fileLength = connection.getContentLength();
				Log.i(Constants.DEBUG, "> Tamaño del archivo a descargar " + fileLength);

				// Download file
				InputStream input = new BufferedInputStream(url.openStream());
				OutputStream output = new FileOutputStream(Constants.PATH_DICTIONARIES + args[1]);

				byte data[] = new byte[1024];
				long total = 0;
				int count;
				while ((count = input.read(data)) != -1) {
					total += count;
					publishProgress((int) (total * 100 / fileLength));
					output.write(data, 0, count);
				}
				output.flush();
				output.close();
				input.close();

				File file = new File(Constants.PATH_DICTIONARIES + args[1]);
				return file.length();
			} catch (Exception e) {
				Log.i(Constants.DEBUG, "Error al descargar: " + e.toString());
			}
			return 0l;
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			super.onProgressUpdate(progress);
			listAdapter.updateProgress(position, progress[0]);
			listAdapter.notifyDataSetChanged();
		}

		@Override
		protected void onPostExecute(Long result) {
			super.onPostExecute(result);
			Log.i(Constants.DEBUG, "> Download finish: size: " + result);

			if (fileLength == result) {
				listAdapter.updateItem(position, R.drawable.ic_action_ok,
						getResources().getString(R.string.downloaded), false);
				downloading.set(position);
			} else {
				Toast.makeText(DescargaActivity.this, R.string.download_failed, Toast.LENGTH_SHORT).show();
				listAdapter.updateItem(position, R.drawable.ic_menu_download,
						getResources().getString(R.string.try_again), false);
				downloading.set(position, false);
			}
		}
	}

	// ************************* MENU *************************
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_download, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			finish();
			return false;
		case android.R.id.home:
			finish();
			return false;
		case R.id.item_update:
			try {
				if (Util.isOnline(this)) {
					new LoadJSON().execute(Constants.URL_JSON);
				} else {
					Util.showAlertNoInternet(this);
				}
			} catch (Exception ex) {
				Toast.makeText(this, R.string.download_failed, Toast.LENGTH_SHORT).show();
				if (Util.isOnline(this)) {
					new LoadJSON().execute(Constants.URL_JSON);
				} else {
					Util.showAlertNoInternet(this);
				}
			}
			break;
		}
		return true;
	}

	@Override
	protected void onStop() {
		super.onStop();
		wl.release();
	}
}