package com.ketanolab.nusimi.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.ketanolab.nusimi.R
import java.util.ArrayList

class DownloadsListAdapter(contexto: Context?) : BaseAdapter() {
    private val inflater: LayoutInflater
    private val images: ArrayList<Int>
    private val names: ArrayList<String>
    private val authors: ArrayList<String>
    private val descriptions: ArrayList<String>
    private val extras: ArrayList<String>
    private val bars: ArrayList<Boolean>
    private val values: ArrayList<Int>
    override fun getCount(): Int {
        return names.size
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
            convertView = inflater.inflate(R.layout.download_list_item, null)
            holder = ViewHolder()
            holder.imageView = convertView
                .findViewById<View>(R.id.descargaImageView) as ImageView
            holder.nameTextView = convertView
                .findViewById<View>(R.id.nameItem) as TextView
            holder.authorTextView = convertView
                .findViewById<View>(R.id.authorItem) as TextView
            holder.descriptionTextView = convertView
                .findViewById<View>(R.id.descriptionItem) as TextView
            holder.sizeTextView = convertView
                .findViewById<View>(R.id.extraItem) as TextView
            holder.progressBar = convertView
                .findViewById<View>(R.id.progressBar) as ProgressBar
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        holder.imageView!!.setImageResource(images[position])
        holder.nameTextView!!.text = names[position]
        holder.authorTextView!!.text = authors[position]
        holder.descriptionTextView!!.text = descriptions[position]
        holder.sizeTextView!!.text = extras[position]
        if (bars[position]) {
            holder.progressBar!!.visibility = View.VISIBLE
        } else {
            holder.progressBar!!.visibility = View.GONE
        }
        holder.progressBar!!.progress = values[position]
        return convertView!!
    }

    internal class ViewHolder {
        var imageView: ImageView? = null
        var nameTextView: TextView? = null
        var authorTextView: TextView? = null
        var descriptionTextView: TextView? = null
        var sizeTextView: TextView? = null
        var progressBar: ProgressBar? = null
    }

    fun adicionarItem(
        image: Int, name: String, author: String,
        description: String, extra: String
    ) {
        images.add(image)
        names.add(name)
        authors.add(author)
        descriptions.add(description)
        extras.add(extra)
        bars.add(false)
        values.add(0)
        notifyDataSetChanged()
    }

    fun updateItem(position: Int, image: Int, text: String, bar: Boolean) {
        images[position] = image
        extras[position] = text
        bars[position] = bar
        notifyDataSetChanged()
    }

    fun updateProgress(position: Int, progress: Int) {
        values[position] = progress
    }

    fun eliminarTodo() {
        images.clear()
        names.clear()
        authors.clear()
        descriptions.clear()
        extras.clear()
        notifyDataSetChanged()
    }

    init {
        inflater = LayoutInflater.from(contexto)
        images = ArrayList()
        names = ArrayList()
        authors = ArrayList()
        descriptions = ArrayList()
        extras = ArrayList()
        bars = ArrayList()
        values = ArrayList()
    }
}