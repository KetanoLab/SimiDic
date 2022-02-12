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
import com.ketanolab.nusimi.models.Dictionary
import java.util.ArrayList

class DownloadsListAdapter(var context: Context?) : BaseAdapter() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val images: ArrayList<Int>
    private val bars: ArrayList<Boolean>
    private val dictionaries: ArrayList<Dictionary>
    private val values: ArrayList<Int>
    override fun getCount(): Int {
        return dictionaries.size
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
        holder.nameTextView!!.text = dictionaries[position].name
        holder.authorTextView!!.text = dictionaries[position].author
        holder.descriptionTextView!!.text = dictionaries[position].description
        holder.sizeTextView!!.text = dictionaries[position].size
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

    fun addItem(
        image: Int, dictionary: Dictionary
    ) {
        images.add(image)
        dictionaries.add(dictionary)
        bars.add(false)
        values.add(0)
        notifyDataSetChanged()
    }

    fun updateItem(position: Int, image: Int, text: String, bar: Boolean) {
        images[position] = image
        dictionaries[position] = dictionaries[position].copy(size = text)
        bars[position] = bar
        notifyDataSetChanged()
    }

    fun updateProgress(position: Int, progress: Int) {
        values[position] = progress
    }

    fun eraseAll() {
        images.clear()
        dictionaries.clear()
        notifyDataSetChanged()
    }

    fun markDownloaded(dictionary: String) {
        for (i in dictionaries.indices) {
            if (dictionaries[i].file == dictionary) {
                images[i] = R.drawable.ic_action_ok
                dictionaries[i] = dictionaries[i].copy(size = context!!.resources.getString(R.string.downloaded))
                bars[i] = false
            }
        }
    }

    init {
        images = ArrayList()
        dictionaries = ArrayList()
        bars = ArrayList()
        values = ArrayList()
    }
}