package com.ketanolab.nusimi.adapters

import android.content.Context
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.ketanolab.nusimi.R
import java.util.ArrayList

class DictionariesViewPagerAdapter(private val contexto: Context) : PagerAdapter() {
    private val listaTitulos: MutableList<String>
    private val listaSubtitulos: MutableList<String>
    private val listaExtras: MutableList<CharSequence>
    private val listaBitmaps: MutableList<Int>
    fun adicionarItem(bitmap: Int, titulo: String, subtitulo: String, extra: String) {
        listaTitulos.add(titulo)
        listaSubtitulos.add(subtitulo)
        listaExtras.add(Html.fromHtml("$extra<br /><br /><a href=\"http://creativecommons.org/licenses/by-nc-nd/3.0/deed.es\">Creative Commons-Atribución-NoComercial-SinDerivadas 3.0 Unported</a>"))
        listaBitmaps.add(bitmap)
    }

    override fun instantiateItem(collection: View, position: Int): Any {
        val linearLayout = View.inflate(contexto, R.layout.dictionary_view, null) as LinearLayout
        // titulo
        val textoTitulo = linearLayout.findViewById<View>(R.id.textview_titulo) as TextView
        textoTitulo.text = listaTitulos[position]
        // Subtitulo
        val textoSubtitulo = linearLayout.findViewById<View>(R.id.textview_subtitulo) as TextView
        textoSubtitulo.text = listaSubtitulos[position]
        // Extra
        val textoExtra = linearLayout.findViewById<View>(R.id.textview_extra) as TextView
        textoExtra.text = listaExtras[position]
        textoExtra.movementMethod = LinkMovementMethod.getInstance()
        // Imagen
        val imagen = linearLayout.findViewById<View>(R.id.imageview_logo) as ImageView
        imagen.setImageResource(listaBitmaps[position])
        (collection as ViewPager).addView(linearLayout, 0)
        return linearLayout
    }

    override fun getCount(): Int {
        return listaTitulos.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as LinearLayout
    }

    override fun destroyItem(collection: View, position: Int, view: Any) {
        (collection as ViewPager).removeView(view as LinearLayout)
    }

    fun getNombreProducto(posicion: Int): String {
        return listaTitulos[posicion]
    }

    init {
        listaTitulos = ArrayList()
        listaSubtitulos = ArrayList()
        listaExtras = ArrayList()
        listaBitmaps = ArrayList()
    }
}