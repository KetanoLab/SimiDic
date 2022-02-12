package com.ketanolab.nusimi

import android.os.AsyncTask
import android.os.Bundle
import android.os.SystemClock
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageSwitcher
import android.widget.TextSwitcher
import androidx.appcompat.app.AppCompatActivity
import com.ketanolab.nusimi.anim.ImageLogo
import com.ketanolab.nusimi.anim.PrincipalText
import com.ketanolab.nusimi.anim.SecundaryText

class CreditsActivity : AppCompatActivity() {
     lateinit var imageSwitcher1: ImageSwitcher
     lateinit var textSwicher1: TextSwitcher
     lateinit var textSwicher2: TextSwitcher
    private val  texts1 = intArrayOf(
        R.string.present,
        R.string.licence,
        R.string.authors,
        R.string.developer,
        R.string.linguist,
        R.string.coordinator_and_graphics,
        R.string.producers,
        R.string.special_thanks
    )
    private val texts2 = arrayOf(
        "...",
        "Nusimi 1.0.1",
        "Felix Layme Pairumani, Teofilo Laime Ajacopa, Comité HABLE Guaraní \n  Saturnino Callo, Elio Ortiz",
        "Community",
        "Amos Batto",
        "Pedro Teran",
        "Amos Bato/Pedro Teran",
        "Educación Intercultural Bilingüe del Ministerio de Educación\nEstado Plurinacional de Bolivia"
    )

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credits)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        imageSwitcher1 = findViewById(R.id.imageSwitcher1)
        imageSwitcher1.setFactory(ImageLogo(this))
        textSwicher1 = findViewById(R.id.textSwitcher1)
        textSwicher2 = findViewById(R.id.textSwitcher2)
        textSwicher1.setFactory(SecundaryText(this))
        textSwicher2.setFactory(PrincipalText(this))
        val animationIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        val animationOut = AnimationUtils.loadAnimation(this, android.R.anim.fade_out)
        imageSwitcher1.setInAnimation(animationIn)
        imageSwitcher1.setOutAnimation(animationOut)
        textSwicher1.setInAnimation(animationIn)
        textSwicher1.setOutAnimation(animationOut)
        textSwicher2.setInAnimation(animationIn)
        textSwicher2.setOutAnimation(animationOut)
        AnimationSwitcher().execute()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return true
    }

    inner class AnimationSwitcher : AsyncTask<Void?, Int?, Void?>() {
        override fun onPreExecute() {
            textSwicher1!!.visibility = View.GONE
            textSwicher2!!.visibility = View.GONE
            textSwicher1!!.setText("")
            textSwicher2!!.setText("")
            imageSwitcher1!!.visibility = View.VISIBLE
        }

        override fun onProgressUpdate(vararg values: Int?) {
            if (values[0] == -2) {
                imageSwitcher1!!.setImageResource(R.drawable.logo_ketanolab)
            } else if (values[0] == -1) {
                imageSwitcher1!!.setImageResource(0)
                imageSwitcher1!!.setImageResource(R.drawable.logo_illa)
            } else {
                imageSwitcher1!!.visibility = View.GONE
                imageSwitcher1!!.setImageResource(0)
                textSwicher1!!.setText("")
                textSwicher2!!.setText("")
                textSwicher1!!.visibility = View.VISIBLE
                textSwicher2!!.visibility = View.VISIBLE
                textSwicher1!!.setText(resources.getString(texts1[values[0]!!]))
                textSwicher2!!.setText(texts2[values[0]!!])
            }
        }

        override fun onPostExecute(result: Void?) {
            AnimationSwitcher().execute()
        }

        override fun doInBackground(vararg params: Void?): Void? {
            publishProgress(-2)
            SystemClock.sleep(1200)
            publishProgress(-1)
            SystemClock.sleep(1200)
            publishProgress(0)
            SystemClock.sleep(1300)
            publishProgress(1)
            SystemClock.sleep(2000)
            for (i in 2 until texts1.size) {
                publishProgress(i)
                SystemClock.sleep(1300)
            }
            return null        }
    }
}