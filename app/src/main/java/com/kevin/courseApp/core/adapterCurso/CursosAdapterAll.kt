package com.kevin.courseApp.core.adapterCurso

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kevin.courseApp.R
import com.kevin.courseApp.data.model.Cursos
 import com.kevin.courseApp.databinding.ItemCursoBinding
import com.kevin.courseApp.databinding.ItemCursoallBinding
import com.squareup.picasso.Picasso
class CursosAdapterAll(var cursos: List<Cursos>) : RecyclerView.Adapter<CursosAdapterAll.CursoViewHolderAll>() {

    private val ITEM_VIEW_TYPE_COURSE = 0
    private val ITEM_VIEW_TYPE_AD = 1

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(curso: Cursos)
    }


 override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CursoViewHolderAll {
        val binding = ItemCursoallBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CursoViewHolderAll(binding)
    }




    override fun onBindViewHolder(holder: CursoViewHolderAll, position: Int) {
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

    inner class CursoViewHolderAll(private val binding: ItemCursoallBinding) : RecyclerView.ViewHolder(binding.root) {
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
