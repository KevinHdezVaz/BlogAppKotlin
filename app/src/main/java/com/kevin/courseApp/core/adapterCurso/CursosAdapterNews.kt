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
import com.kevin.courseApp.databinding.ItemCursonewBinding
import com.squareup.picasso.Picasso
class CursosAdapterNews(var cursos: List<Cursos>) : RecyclerView.Adapter<CursosAdapterNews.CursoViewHoldernew>() {

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(curso: Cursos)
    }


 override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CursoViewHoldernew {
        val binding = ItemCursonewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CursoViewHoldernew(binding)
    }




    override fun onBindViewHolder(holder: CursoViewHoldernew, position: Int) {
         val curso = cursos[position]
         holder.bind(curso)


        holder.itemView.setOnClickListener {
            listener?.onItemClick(curso)
        }
    }



    override fun getItemCount(): Int {
        return cursos.size
    }

    inner class CursoViewHoldernew(private val binding: ItemCursonewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(curso: Cursos) {



            binding.tituloTextView.text = curso.titulo
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
