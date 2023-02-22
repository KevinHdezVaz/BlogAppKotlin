package com.kevin.courseApp.core.categoriaAdapter

import android.content.Context
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kevin.courseApp.R
import com.kevin.courseApp.data.model.Categoria
import com.kevin.courseApp.databinding.ItemCategoriaBinding

class CategoriasAdapter(private var categorias: List<String>) : BaseAdapter() {

    private class ViewHolder(val binding: ItemCategoriaBinding)

    override fun getCount() = categorias.size

    override fun getItem(position: Int) = categorias[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            val binding = ItemCategoriaBinding.inflate(LayoutInflater.from(parent?.context), parent, false)
            view = binding.root
            holder = ViewHolder(binding)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }

        holder.binding.categoriaTexto.text = categorias[position]

        return view
    }

    fun setCategorias(categorias: List<String>) {
        this.categorias = categorias
        notifyDataSetChanged()
    }
}
