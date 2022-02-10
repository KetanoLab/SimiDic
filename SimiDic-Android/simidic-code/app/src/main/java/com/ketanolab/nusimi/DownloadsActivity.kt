package com.ketanolab.nusimi

import android.os.AsyncTask
import android.os.Bundle
import android.os.PowerManager
import android.os.PowerManager.WakeLock
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ketanolab.nusimi.adapters.DownloadsListAdapter
import com.ketanolab.nusimi.util.Constants
import com.ketanolab.nusimi.util.Util
import org.json.JSONArray
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.ArrayList
import java.util.BitSet

class DownloadsActivity : AppCompatActivity(), OnItemClickListener {
    // URLs
    private var urls: ArrayList<String>? = null
    private var fileNames: ArrayList<String>? = null

    // List
    private var listView: ListView? = null
    private var listAdapter: DownloadsListAdapter? = null

    // Loading
    private var layoutCargando: RelativeLayout? = null
    private var layoutMensaje: RelativeLayout? = null

    // Tasks
    private var tasks: ArrayList<DownloadFile>? = null
    private var downloading: BitSet? = null
    private var dictionariesDirPath: String? = null
    private lateinit var wl: WakeLock
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_downloads)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // show home icon
        dictionariesDirPath = applicationContext.getExternalFilesDir(null)!!.absolutePath
        // Loading
        layoutCargando = findViewById<View>(R.id.layoutCargando) as RelativeLayout
        layoutMensaje = findViewById<View>(R.id.layoutMensaje) as RelativeLayout
        layoutMensaje!!.visibility = View.GONE

        // List
        listView = findViewById<View>(R.id.lista) as ListView
        listAdapter = DownloadsListAdapter(this)
        listView!!.adapter = listAdapter
        listView!!.onItemClickListener = this
        if (Util.isOnline(this)) {
            LoadJSON(dictionariesDirPath).execute(Constants.URL_JSON)
        } else {
            Util.showAlertNoInternet(this)
        }
        tasks = ArrayList()
    }

    //method Deprecated 
    override fun onStart() {
        super.onStart()
        val pm = getSystemService(POWER_SERVICE) as PowerManager
        wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "Simidic:dictionaries")
        wl.acquire()
    }

    inner class LoadJSON(private val directoryPath: String?) :
        AsyncTask<String?, String?, Void?>() {
        override fun onPreExecute() {
            listAdapter!!.eliminarTodo()
            urls = ArrayList()
            fileNames = ArrayList()
            downloading = BitSet()
            layoutMensaje!!.visibility = View.GONE
            layoutCargando!!.visibility = View.VISIBLE
            tasks = ArrayList<DownloadFile>()

        }

        override fun doInBackground(vararg values: String?): Void? {
            try {
                val repo = URL(Constants.URL_JSON)
                val con = repo.openConnection() as HttpURLConnection
                con.requestMethod = "GET"
                val responseCode = con.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val `in` = BufferedReader(
                        InputStreamReader(
                            con.inputStream
                        )
                    )
                    var inputLine: String?
                    val response = StringBuffer()
                    while (`in`.readLine().also { inputLine = it } != null) {
                        response.append(inputLine)
                    }
                    `in`.close()
                    val resultado = response.toString()
                    val jsonArray = JSONArray(resultado)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val name = jsonObject.getString("name")
                        val author = jsonObject.getString("author")
                        val descripcion = jsonObject.getString("description")
                        val file = jsonObject.getString("file")
                        val url = jsonObject.getString("url")
                        val size = jsonObject.getString("size")
                        publishProgress(name, author, descripcion, file, url, size)
                    }
                }
            } catch (ex: Exception) {
                Log.i(Constants.DEBUG, "Error al cargar JSON: $ex")
            }
            return null
        }

        override fun onProgressUpdate(vararg values: String?) {
            super.onProgressUpdate(*values)
            if (!Util.isDownloaded(directoryPath + "/" + values[3])) {
                fileNames!!.add(values[3]!!)
                urls!!.add(values[4]!!)
                tasks!!.add(DownloadFile(tasks!!.size+2, values[4]!!))
                listAdapter!!.adicionarItem(
                    R.drawable.ic_menu_download,
                    values[0]!!,
                    values[1]!!,
                    values[2]!!,
                    values[5]!!
                )
                Log.i(Constants.DEBUG, "Added item download: " + values[0])
            }
            Log.i(Constants.DEBUG, "Added item (all) download: " + values[0])
        }

        override fun onPostExecute(result: Void?) {
            layoutCargando!!.visibility = View.GONE
            if (listAdapter!!.count == 0) {
                layoutMensaje!!.visibility = View.VISIBLE
            }
        }
    }

    override fun onItemClick(arg0: AdapterView<*>?, view: View, posicion: Int, id: Long) {
        Log.i(Constants.DEBUG, "Descargando... " + fileNames!![posicion])
        if (downloading!![posicion]) {
            Toast.makeText(this, R.string.is_downloaded_or_downloading, Toast.LENGTH_SHORT).show()
        } else {
            tasks!!.add(
                posicion, DownloadFile(
                    posicion, applicationContext.getExternalFilesDir(null)!!
                        .absolutePath
                )
            )
            tasks!![posicion].execute(urls!![posicion], fileNames!![posicion])
            downloading!!.set(posicion)
        }
    }

    private inner class DownloadFile(private val position: Int, private val path: String) :
        AsyncTask<String?, Int?, Long>() {
        private var fileLength: Long = 0
        override fun onPreExecute() {
            super.onPreExecute()
            listAdapter!!.updateItem(
                position, R.drawable.ic_action_cancel, resources
                    .getString(R.string.downloading), true
            )
        }

        override fun doInBackground(vararg args: String?): Long {
            try {
                val directorio = File(path)
                if (!directorio.exists()) {
                    directorio.mkdirs()
                }
                val url = URL(args[0])
                val connection = url.openConnection()
                connection.connect()
                fileLength = connection.contentLength.toLong()
                Log.i(Constants.DEBUG, "> Tamaño del archivo a descargar $fileLength")

                // Download file
                val input: InputStream = BufferedInputStream(url.openStream())
                val output: OutputStream = FileOutputStream(path + "/" + args[1])
                val data = ByteArray(1024)
                var total: Long = 0
                var count: Int
                while (input.read(data).also { count = it } != -1) {
                    total += count.toLong()
                    publishProgress((total * 100 / fileLength).toInt())
                    output.write(data, 0, count)
                }
                output.flush()
                output.close()
                input.close()
                val file = File(path, args[1])
                return file.length()
            } catch (e: Exception) {
                Log.i(Constants.DEBUG, "Error al descargar: $e")
            }
            return 0L
        }

        override fun onProgressUpdate(vararg progress: Int?) {
            super.onProgressUpdate(*progress)
            listAdapter!!.updateProgress(position, progress[0]!!)
            listAdapter!!.notifyDataSetChanged()
        }

        override fun onPostExecute(result: Long) {
            super.onPostExecute(result)
            Log.i(Constants.DEBUG, "> Download finish: size: $result")
            if (fileLength == result) {
                listAdapter!!.updateItem(
                    position, R.drawable.ic_action_ok,
                    resources.getString(R.string.downloaded), false
                )
                downloading!!.set(position)
            } else {
                Toast.makeText(this@DownloadsActivity, R.string.download_failed, Toast.LENGTH_SHORT)
                    .show()
                listAdapter!!.updateItem(
                    position, R.drawable.ic_menu_download,
                    resources.getString(R.string.try_again), false
                )
                downloading!![position] = false
            }
        }
    }

    // ************************* MENU *************************
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_download, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            0 -> {
                finish()
                return false
            }
            android.R.id.home -> {
                finish()
                return false
            }
            R.id.item_update -> try {
                if (Util.isOnline(this)) {
                    LoadJSON(dictionariesDirPath).execute(Constants.URL_JSON)
                } else {
                    Util.showAlertNoInternet(this)
                }
            } catch (ex: Exception) {
                Toast.makeText(this, R.string.download_failed, Toast.LENGTH_SHORT).show()
                if (Util.isOnline(this)) {
                    LoadJSON(dictionariesDirPath).execute(Constants.URL_JSON)
                } else {
                    Util.showAlertNoInternet(this)
                }
            }
        }
        return true
    }

    override fun onStop() {
        super.onStop()
        wl!!.release()
    }
}