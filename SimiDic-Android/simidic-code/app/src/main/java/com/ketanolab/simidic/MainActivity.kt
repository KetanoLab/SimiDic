package com.ketanolab.simidic

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.FilterQueryProvider
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.ketanolab.simidic.util.Constants
import com.ketanolab.simidic.util.Dictionaries
import com.ketanolab.simidic.util.Util
import java.util.ArrayList

class MainActivity : AppCompatActivity(), OnItemClickListener, ActionBar.OnNavigationListener {
    // Paths dictionaries
    private lateinit var pathsDictionaries: ArrayList<String>

    // Navigation
    private var itemSelectedNavigation = 0
    private var listNavigationAdapter: ArrayAdapter<String>? = null

    // List words
    private var wordsListView: ListView? = null
    private var WordsSimpleCursorAdapter: SimpleCursorAdapter? = null
    var db: SQLiteDatabase? = null

    // EditText
    private lateinit var askBox: EditText
    var mRegisterTask: AsyncTask<Void, Void, Void>? = null

    // END NOTIFICACION PUSH
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(Constants.DEBUG, "onCreate()")
        setContentView(R.layout.activity_main)
        val context = supportActionBar!!.themedContext
        listNavigationAdapter = ArrayAdapter(
            context,
            R.layout.action_bar_spinner_list_item
        )
        listNavigationAdapter!!
            .setDropDownViewResource(R.layout.action_bar_spinner_list_item)
        val actionBar = actionBar
        // List
        wordsListView = findViewById<View>(R.id.lista) as ListView
        wordsListView!!.onItemClickListener = this
        supportActionBar!!.setDisplayShowTitleEnabled(false) // Hide title
        supportActionBar!!.navigationMode = ActionBar.NAVIGATION_MODE_LIST
        supportActionBar!!.setListNavigationCallbacks(
            listNavigationAdapter,
            this
        )
        askBox = findViewById(R.id.caja_consulta)
        askBox.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int,
                count: Int
            ) {
                if (s.toString().length > 0) {
                    WordsSimpleCursorAdapter!!.filter.filter(s.toString())
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    override fun onStart() {
        super.onStart()
        Log.i(Constants.DEBUG, "onStart()")

        // Get dictionaries valid
        pathsDictionaries = Dictionaries.scanDictionariesAndValidation(this)
        // Put Navigation
        putDataDictionariesInNavigation()
        // Load words
        Log.i(Constants.DEBUG, "Cargando palabras...")
        if (pathsDictionaries.size > 0) {
            db = SQLiteDatabase.openOrCreateDatabase(
                pathsDictionaries.get(itemSelectedNavigation), null
            )
            loadAllWords()
        }
        // Re-query
        val s = askBox!!.text.toString()
        if (s.length > 0) {
            WordsSimpleCursorAdapter!!.filter.filter(s)
        }
    }

    private fun loadAllWords() {
        val columns = arrayOf("_id", "word", "summary")
        val to = intArrayOf(0, R.id.word_item, R.id.meaning_item)
        // Cursor cursor = db.rawQuery("SELECT _id, word, summary FROM words",
        // null);
        val cursor = db!!
            .query("words", columns, null, null, null, null, null)
        WordsSimpleCursorAdapter = SimpleCursorAdapter(
            this,
            R.layout.word_list_item, cursor, columns, to, 0
        )
        WordsSimpleCursorAdapter!!.filterQueryProvider = FilterQueryProvider { constraint ->
            val word = "%$constraint%"
            var cursor: Cursor? = null
            cursor = if (pathsDictionaries!![itemSelectedNavigation]
                    .contains("gn")
            ) {
                db!!
                    .rawQuery(
                        "SELECT _id, word, summary FROM words WHERE replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(word,'�','a'),'�','e'),'�','i'),'�','o'),'�','u'),'(',''),')',''),'�','n'),'�','i'),'�','a'),'�','e'),'�','o'),'�','u') LIKE ?",
                        arrayOf(word)
                    )
            } else {
                db!!
                    .rawQuery(
                        "SELECT _id, word, summary FROM words WHERE replace(replace(replace(replace(replace(replace(replace(word,'�','a'),'�','e'),'�','i'),'�','o'),'�','u'),'�','n'),'�','a') LIKE ?",
                        arrayOf(word)
                    )
            }
            cursor
        }
        wordsListView!!.adapter = WordsSimpleCursorAdapter
    }

    private fun putDataDictionariesInNavigation() {
        listNavigationAdapter!!.clear()
        for (i in pathsDictionaries!!.indices) {
            val db = SQLiteDatabase.openOrCreateDatabase(
                pathsDictionaries!![i], null
            )
            val nameAndAuthor = Util.getNameAndAuthorDictionary(db)
            listNavigationAdapter!!.add(nameAndAuthor!!.get(0)) // Get name
            db.close()
        }
        listNavigationAdapter!!.notifyDataSetChanged()
    }

    override fun onNavigationItemSelected(position: Int, id: Long): Boolean {
        itemSelectedNavigation = position
        if (pathsDictionaries!!.size > 0) {
            if (db != null) {
                db!!.close()
            }
            db = SQLiteDatabase.openOrCreateDatabase(
                pathsDictionaries!![itemSelectedNavigation], null
            )
            loadAllWords()
        }
        // Re-query
        val word = askBox!!.text.toString()
        if (word.length > 0) {
            WordsSimpleCursorAdapter!!.filter.filter(word)
        }
        return true
    }

    // ************************* MENU *************************
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_download -> {
                val intentDescarga = Intent(this, DownloadsActivity::class.java)
                startActivity(intentDescarga)
            }
            R.id.menu_favorites -> {
                val intentFavoritos = Intent(this, FavoritesActivity::class.java)
                startActivity(intentFavoritos)
            }
            R.id.menu_information -> {
                val intentCreditos = Intent(this, CreditsActivity::class.java)
                startActivity(intentCreditos)
            }
            R.id.menu_dictionaries -> {
                val intentDiccionarios = Intent(
                    this,
                    DiccionariesActivity::class.java
                )
                startActivity(intentDiccionarios)
            }
        }
        return true
    }

    // ************************* ITEMCLICK *************************
    override fun onItemClick(arg0: AdapterView<*>?, v: View, posicion: Int, id: Long) {
        // Start WordActivity
        val word = (v.findViewById<View>(R.id.word_item) as TextView).text
            .toString()
        val intent = Intent(this, WordActivity::class.java)
        intent.putExtra(
            "path", pathsDictionaries!![supportActionBar!!
                .getSelectedNavigationIndex()]
        )
        intent.putExtra("word", word)
        startActivity(intent)
    }

    override fun onStop() {
        super.onStop()
        Log.i(Constants.DEBUG, "onStop()")
        if (WordsSimpleCursorAdapter != null) {
            WordsSimpleCursorAdapter!!.cursor.close()
            WordsSimpleCursorAdapter = null
        }
        if (db != null) {
            db!!.close()
        }
    }
}