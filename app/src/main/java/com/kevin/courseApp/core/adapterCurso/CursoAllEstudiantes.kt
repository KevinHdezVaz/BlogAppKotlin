package com.kevin.courseApp.core.adapterCurso

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kevin.courseApp.data.model.Cursos
import com.kevin.courseApp.databinding.ItemCursoallBinding
import com.kevin.courseApp.databinding.ItemEstudianteallBinding

class CursoAllEstudiantes(var cursos: List<Cursos>) : RecyclerView.Adapter<CursoAllEstudiantes.CursoViewHolderAllEstudiante>() {

    private val ITEM_VIEW_TYPE_COURSE = 0
    private val ITEM_VIEW_TYPE_AD = 1

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(curso: Cursos)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CursoViewHolderAllEstudiante {
        val binding = ItemEstudianteallBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CursoViewHolderAllEstudiante(binding)
    }




    override fun onBindViewHolder(holder: CursoViewHolderAllEstudiante, position: Int) {
        val curso = cursos[position]
        holder.bind(curso)



        holder.itemView.setOnClickListener {
            listener?.onItemClick(curso)
        }
    }





    override fun getItemViewType(position: Int): Int {
        return if (position % 5 == 0) {
            ITEM_VIEW_TYPE_AD
        } else {
            ITEM_VIEW_TYPE_COURSE
        }
    }

    override fun getItemCount(): Int {
        return cursos.size
    }

    inner class CursoViewHolderAllEstudiante(private val binding: ItemEstudianteallBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(curso: Cursos) {



            binding.tituloTextView.text = curso.titulo
            binding.categoriaCurso.text = curso.empresa
            Glide.with(itemView.context).load(curso.imagenUrl).into(binding.imagenImageView)
            Glide.with(itemView.context).load(curso.imagenFondo).into(binding.fondoo)



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
