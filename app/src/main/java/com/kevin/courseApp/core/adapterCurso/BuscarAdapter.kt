package com.kevin.courseApp.core.adapterCurso

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kevin.courseApp.R
import com.kevin.courseApp.data.model.Cursos
import com.kevin.courseApp.databinding.ItemCursoBinding
import com.kevin.courseApp.databinding.ItemCursoBuscarBinding

class BuscarAdapter(var cursos: List<Cursos>) : RecyclerView.Adapter<BuscarAdapter.BuscarViewHolder>() {

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(curso: Cursos)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuscarViewHolder {
        val binding2 = ItemCursoBuscarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BuscarViewHolder(binding2)
    }

    override fun onBindViewHolder(holder: BuscarViewHolder, position: Int) {
        val curso = cursos[position]
        holder.bind(curso)


        holder.itemView.setOnClickListener {
            listener?.onItemClick(curso)
        }
    }


    override fun getItemCount(): Int {
        return cursos.size
    }

    inner class BuscarViewHolder(private val binding: ItemCursoBuscarBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(curso: Cursos) {



            binding.tituloTextView.text = curso.titulo
            binding.descripcionTextView.text = curso.empresa

            // Agregar el oyente de clics al elemento del RecyclerView
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onItemClick(cursos[position])
                }
            }
        }
    }

}
