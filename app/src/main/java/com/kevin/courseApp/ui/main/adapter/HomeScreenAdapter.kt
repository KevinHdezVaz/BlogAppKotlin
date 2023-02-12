package com.kevin.courseApp.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kevin.courseApp.R
import com.kevin.courseApp.core.BaseViewHolder
import com.kevin.courseApp.core.TimeAgo
import com.kevin.courseApp.data.model.Posts
import com.kevin.courseApp.databinding.PostItemBinding


//Para implementar parte del reyclerView
class HomeScreenAdapter(private val postlist: List<Posts>, private val onPostClickListener: onPostClickListener):
    RecyclerView.Adapter<BaseViewHolder<*>>() {


    private var postClickListener: onPostClickListener?= null
    init {
        postClickListener = onPostClickListener
    }

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


            setupProfile(item)
        addPostTimeStamp(item)
            setupPostProfile(item)
        setupPostDescription(item)
            like(item)
            setupLikeCount(item)
            setlikeClicAction(item)
        }


        private fun setupProfile(post:Posts)
        {
            Glide.with(context).load(post.poster?.profile_pictura).centerCrop().into(binding.profilePicture)
            binding.profileName.text = post.poster?.username
        }

        private fun addPostTimeStamp(post: Posts){
            val createdAT = post.created_at?.time?.div(1000L)?.let {
                TimeAgo.getTimeAgo(it.toInt())
            }

            binding.postTimestamp.text = createdAT
        }

        private fun setupPostProfile(post:Posts){
            Glide.with(context).load(post.post_image).centerCrop().into(binding.postImage)
        }

        private fun setupPostDescription(post:Posts)
        {
            if(post.post_description.isEmpty()){
                binding.postDescription.visibility = View.GONE
            }else{
                binding.postDescription.text = post.post_description
            }
        }

        private fun like(post: Posts){

            if(!post.liked){
               // binding.btnLike.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_favorite_border_24))
             //   binding.btnLike.setColorFilter(ContextCompat.getColor(context, R.color.black))
            }
            else{
           //     binding.btnLike.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_favorite_24))
         //       binding.btnLike.setColorFilter(ContextCompat.getColor(context, R.color.purple_200))

            }
        }  private fun setupLikeCount(post: Posts) {
        if( post.likes > 0 ){
        binding.likeCount.visibility = View.VISIBLE
            binding.likeCount.text ="${post.likes} likes"
        }else{
            binding.likeCount.visibility = View.GONE

        }

    }

        private fun setlikeClicAction(item: Posts) {
            binding.btnLike.setOnClickListener{
                if(item.liked) item.apply {
                    liked = false
                }else item.apply { liked = true }

                like(item)
                postClickListener?.onlikeBtnClick(item , item.liked)
            }
        }
    }


}

interface onPostClickListener{
    fun onlikeBtnClick(post: Posts, liked:Boolean){



    }
}