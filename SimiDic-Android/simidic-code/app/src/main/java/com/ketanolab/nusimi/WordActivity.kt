package com.ketanolab.nusimi

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ketanolab.nusimi.db.FavoritosDbAdapter
import com.ketanolab.nusimi.util.Constants
import com.ketanolab.nusimi.util.Util

class WordActivity : AppCompatActivity() {
    private var textoPalabra: TextView? = null
    private var textoSignificado: TextView? = null
    private var textoDiccionario: TextView? = null
    private var favorite = false
    private var path: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        // GUI
        textoPalabra = findViewById<View>(R.id.textoPalabra) as TextView
        textoSignificado = findViewById<View>(R.id.textoSignificado) as TextView
        textoDiccionario = findViewById<View>(R.id.textoDiccionario) as TextView
        // Get extras
        val pathDictionary = intent.getStringExtra("path")
        path = pathDictionary
        val word = intent.getStringExtra("word")
        // Meaning
        loadMeanings(pathDictionary, word)
        val db = SQLiteDatabase.openOrCreateDatabase(pathDictionary!!, null)
        try {
            val nameAndAuthor = Util.getNameAndAuthorDictionary(db)
            title = nameAndAuthor!![0]
            textoDiccionario!!.text = nameAndAuthor!![1]
        } catch (ex: Exception) {
            Log.e(Constants.DEBUG, "Error en la consulta a la base de datos.")
        }
        db.close()
        textoPalabra!!.text = word
        favorite = if (esFavorito(word, pathDictionary)) {
            true
        } else {
            false
        }
    }

    private fun loadMeanings(pathDictionary: String?, word: String?) {
        val db = SQLiteDatabase.openOrCreateDatabase(pathDictionary!!, null)
        putAllMeanings(db, word)
        db.close()
    }

    private fun putAllMeanings(db: SQLiteDatabase, word: String?) {
        var meanings: CharSequence = ""
        val cursor = db.rawQuery("SELECT meaning from words where word=?", arrayOf(word))
        if (cursor.moveToFirst()) {
            do {
                var meaning = cursor.getString(0)
                // meaning = meaning.replaceAll("<i>", "</i>");
                // meaning = meaning.replaceAll("<span foreground",
                // "<font color");
                // meaning = meaning.replaceAll("</span>", "</font>");
                // ***
                meaning = meaning.replace("\\n", "<br />")
                // ***
                meanings = if (meanings == "") {
                    Html.fromHtml(meaning)
                } else {
                    TextUtils.concat(meanings, "\n\n", Html.fromHtml(meaning))
                }
            } while (cursor.moveToNext())
        }
        cursor.close()
        textoSignificado!!.text = meanings
    }

    private fun esFavorito(palabra: String?, nombreDiccionario: String?): Boolean {
        val db = FavoritosDbAdapter(this)
        db.abrir()
        val sw = db.isFavorite(palabra!!, nombreDiccionario!!)
        db.close()
        return if (sw) {
            true
        } else false
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        if (favorite) {
            menu.getItem(0).setIcon(R.drawable.ic_action_star_ok)
        } else {
            menu.getItem(0).setIcon(R.drawable.ic_action_star)
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_word, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == R.id.item_favorite) {
            val db = FavoritosDbAdapter(this)
            db.abrir()
            if (favorite) {
                // Delete favorite
                item.setIcon(R.drawable.ic_action_star)
                db.eraseFavorite(textoPalabra!!.text.toString(), path!!)
                favorite = false
                Toast.makeText(this, R.string.no_favorite_now, Toast.LENGTH_SHORT).show()
            } else {
                // Add favorite
                item.setIcon(R.drawable.ic_action_star_ok)
                db.addFavorite(textoPalabra!!.text.toString(), path)
                favorite = true
                Toast.makeText(this, R.string.favorite_now, Toast.LENGTH_SHORT).show()
            }
            db.close()
        }
        return true
    }
}