package com.kevin.blogappkotlin.ui.main.adapter

import android.content.Context
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kevin.blogappkotlin.core.BaseViewHolder
import com.kevin.blogappkotlin.data.model.Posts
import com.kevin.blogappkotlin.databinding.PostItemBinding


//Para implementar parte del reyclerView
class HomeScreenAdapter(private val postlist: List<Posts>): RecyclerView.Adapter<BaseViewHolder<*>>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {

        val itemBinding = PostItemBinding.inflate(LayoutInflater.from(parent.context),parent, false)
    return HomeScreenViewHolder(itemBinding, parent.context)

    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {

        when(holder){
             is HomeScreenViewHolder -> holder.bind(postlist[position])
        }
    }

    override fun getItemCount(): Int {
      //retorna la cantidad de items dentro del recyclreview
            return postlist.size
    }


    private inner class HomeScreenViewHolder(
        val binding: PostItemBinding,
        val context : Context
    ): BaseViewHolder<Posts>(binding.root)
    {
        override fun bind(item: Posts) {
             Glide.with(context).load(item.post_image).centerCrop().into(binding.postImage)
            Glide.with(context).load(item.profile_pictura).centerCrop().into(binding.profilePicture)
            binding.profileName.text = item.profile_name
            binding.postTimestamp.text = "Hace 2 horas"
            if(item.post_description.isEmpty()){
                binding.postDescription.visibility = View.GONE
            }else{
                binding.postDescription.text = item.post_description
            }
        }

    }
}