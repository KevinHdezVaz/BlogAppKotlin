package com.kevin.courseApp.core.categoriaAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.kevin.courseApp.R
import com.kevin.courseApp.core.adapterCurso.CursosAdapter
import com.kevin.courseApp.data.model.Categoria
import com.kevin.courseApp.data.model.Cursos



class CategoriasAdapter(private val context: Context, private val data: List<Categoria>) : BaseAdapter() {

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.item_categoria, parent, false)
        val categoria = data[position]

        val imageView = view.findViewById<ImageView>(R.id.imageView)
        val textView = view.findViewById<TextView>(R.id.textViewTitle)

        imageView.setImageResource(categoria.image)
        textView.text = categoria.title

        return view
    }
}
