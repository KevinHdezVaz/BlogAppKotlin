package com.kevin.courseApp.core.adapterCurso

import com.bumptech.glide.Glide
import com.kevin.courseApp.R
import com.kevin.courseApp.data.model.Cursos
import com.kevin.courseApp.databinding.ItemCursonewBinding
import com.kevin.courseApp.databinding.ItemLayoutToolsBinding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kevin.courseApp.data.model.Item

class AdapterTools : RecyclerView.Adapter<AdapterTools.ViewHolder>() {
    private val data = mutableListOf<Item>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageViewVis)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tools, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]

        holder.imageView.setImageResource(item.imagen)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    // Función para agregar un nuevo elemento al RecyclerView
    fun addItem(item: Item) {
        data.add(item)
        notifyItemInserted(data.size - 1)
    }
}
