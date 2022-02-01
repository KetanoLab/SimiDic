package com.ketanolab.simidic

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.ketanolab.simidic.adapters.WordsAdapter
import com.ketanolab.simidic.db.FavoritosDbAdapter
import com.ketanolab.simidic.util.Constants
import com.ketanolab.simidic.util.Util
import java.util.ArrayList

class FavoritesActivity : AppCompatActivity(), OnItemClickListener {
    private var lista: ListView? = null
    private var adaptadorLista: WordsAdapter? = null
    private var paths: ArrayList<String>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        lista = findViewById<View>(R.id.listaFavoritos) as ListView
        adaptadorLista = WordsAdapter(this)
        lista!!.adapter = adaptadorLista
        lista!!.onItemClickListener = this
        paths = ArrayList()
    }

    override fun onStart() {
        super.onStart()
        paths!!.clear()
        adaptadorLista!!.eliminarTodo()
        cargarFavoritos()
    }

    private fun cargarFavoritos() {
        val db = FavoritosDbAdapter(this)
        db.abrir()
        val cursor = db.favorites
        if (cursor.moveToFirst()) {
            do {
                val word = cursor.getString(1)
                val pathDictionary = cursor.getString(2)
                paths!!.add(pathDictionary)
                // *****
                var name = ""
                var author = ""
                val dbWords = SQLiteDatabase.openOrCreateDatabase(pathDictionary, null)
                try {
                    val nameAndAuthor = Util.getNameAndAuthorDictionary(dbWords)
                    name = nameAndAuthor!![0]!!
                    author = nameAndAuthor!![1]!!
                } catch (ex: Exception) {
                    Log.e(Constants.DEBUG, "Error en la consulta a la base de datos.")
                }
                dbWords.close()
                // ****
                adaptadorLista!!.addItem(
                    R.drawable.ic_menu_star, word, """
     $name
     $author
     """.trimIndent()
                )
            } while (cursor.moveToNext())
        } else {
            adaptadorLista!!.addItem(R.drawable.ic_menu_star, getString(R.string.no_favorites), " ")
        }
        cursor.close()
        db.close()
        adaptadorLista!!.notifyDataSetChanged()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return true
    }

    override fun onItemClick(arg0: AdapterView<*>?, v: View, posicion: Int, arg3: Long) {
        val textViewPalabra = v.findViewById<View>(R.id.titulo_item) as TextView
        val word = textViewPalabra.text.toString()
        val intent = Intent(this, WordActivity::class.java)
        intent.putExtra("path", paths!![posicion])
        intent.putExtra("word", word)
        startActivity(intent)
    }
}