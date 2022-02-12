package com.ketanolab.nusimi

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.ketanolab.nusimi.adapters.DictionariesViewPagerAdapter
import com.ketanolab.nusimi.util.Dictionaries
import com.ketanolab.nusimi.viewpager.CirclePageIndicator

class DiccionariesActivity : AppCompatActivity() {
    // Paginado
    private var viewPager: ViewPager? = null
    private var indicator: CirclePageIndicator? = null
    private var adaptadorViewPager: DictionariesViewPagerAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dictionaries)

        // Paginado
        viewPager = findViewById<View>(R.id.viewPager) as ViewPager
        adaptadorViewPager = DictionariesViewPagerAdapter(this)
        viewPager!!.adapter = adaptadorViewPager
        indicator = findViewById<View>(R.id.indicator) as CirclePageIndicator
        indicator!!.setViewPager(viewPager)
        // Fin Paginado
        dictionaries
    }

    val dictionaries: Unit
        get() {
            val paths = Dictionaries.scanDictionariesAndValidation(this)
            for (i in paths.indices) {
                val db = SQLiteDatabase.openOrCreateDatabase(paths[i], null)
                setDictionaryData(db)
                db.close()
            }
        }

    private fun setDictionaryData(db: SQLiteDatabase) {
        val cursor =
            db.rawQuery("SELECT author, language_begin, language_end, description FROM info", null)
        if (cursor.moveToFirst()) {
            val author = cursor.getString(0)
            val beginLanguage = cursor.getString(1)
            val endLanguage = cursor.getString(2)
            val description = cursor.getString(3)
            cursor.close()
            adaptadorViewPager!!.adicionarItem(
                R.drawable.img_dictionary, "$beginLanguage - $endLanguage", author,
                description
            )
            adaptadorViewPager!!.notifyDataSetChanged()
        }
        cursor.close()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }
}