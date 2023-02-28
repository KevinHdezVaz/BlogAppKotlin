package com.kevin.courseApp.core.categoriaAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.kevin.courseApp.R

class CategoriasAdapter(private val context: Context, private val categories: ArrayList<String>) : BaseAdapter() {

    override fun getCount(): Int {
        return categories.size
    }

    override fun getItem(position: Int): Any {
        return categories[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var cardView = convertView
        if (cardView == null) {
            cardView = LayoutInflater.from(context).inflate(R.layout.layout_gridview, parent, false)
        }

        val categoryName = cardView?.findViewById<TextView>(R.id.category_name)
        categoryName?.text = categories[position]

        return cardView!!
    }
}
