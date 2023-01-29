package com.kevin.blogappkotlin.domain.home

import com.kevin.blogappkotlin.core.Result
import com.kevin.blogappkotlin.data.model.Posts
import kotlinx.coroutines.flow.Flow


interface HomeScreenRepo
{
    suspend fun getLatestPost(): Result<List<Posts>>

    suspend fun registerLikeButtonState(postId: String, liked: Boolean)

}