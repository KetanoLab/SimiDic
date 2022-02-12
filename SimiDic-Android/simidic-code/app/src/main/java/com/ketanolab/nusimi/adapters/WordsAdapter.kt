package com.ketanolab.nusimi.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.ketanolab.nusimi.R
import java.util.ArrayList

class WordsAdapter(context: Context?) : BaseAdapter() {
    private val inflater: LayoutInflater
    private val imagenes: MutableList<Int>
    private val titulos: MutableList<String>
    private val subtitulos: MutableList<CharSequence>
    override fun getCount(): Int {
        return titulos.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val holder: ViewHolder
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_lista, null)
            holder = ViewHolder()
            holder.imagen = convertView
                .findViewById<View>(R.id.imagen_item) as ImageView
            holder.titulo = convertView
                .findViewById<View>(R.id.titulo_item) as TextView
            holder.subtitulo = convertView
                .findViewById<View>(R.id.subtitulo_item) as TextView
            convertView.tag = holder
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
                holder.titulo!!.setTextIsSelectable(true)
                holder.subtitulo!!.setTextIsSelectable(true)
            } else {
            }
        } else {
            holder = convertView.tag as ViewHolder
        }
        holder.imagen!!.setImageResource(imagenes[position])
        holder.titulo!!.text = titulos[position]
        holder.subtitulo!!.text = subtitulos[position]
        return convertView!!
    }

    internal class ViewHolder {
        var imagen: ImageView? = null
        var titulo: TextView? = null
        var subtitulo: TextView? = null
    }

    fun addItem(recurso: Int, titulo: String, subtitulo: CharSequence) {
        imagenes.add(recurso)
        titulos.add(titulo)
        subtitulos.add(subtitulo)
        notifyDataSetChanged()
    }

    fun addItem(titulo: String, subtitulo: CharSequence) {
        imagenes.add(0)
        titulos.add(titulo)
        subtitulos.add(subtitulo)
    }

    fun addItem(titulo: String) {
        imagenes.add(0)
        titulos.add(titulo)
        subtitulos.add("")
    }

    fun eliminarTodo() {
        imagenes.clear()
        titulos.clear()
        subtitulos.clear()
        notifyDataSetChanged()
    }

    init {
        inflater = LayoutInflater.from(context)
        imagenes = ArrayList()
        titulos = ArrayList()
        subtitulos = ArrayList()
    }
}