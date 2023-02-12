package com.kevin.courseApp.domain.home

import com.kevin.courseApp.core.Result
import com.kevin.courseApp.data.model.Posts


interface HomeScreenRepo
{
    suspend fun getLatestPost(): Result<List<Posts>>

    suspend fun registerLikeButtonState(postId: String, liked: Boolean)

}