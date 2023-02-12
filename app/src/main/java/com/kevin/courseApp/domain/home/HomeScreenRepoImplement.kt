package com.kevin.courseApp.domain.home

import com.kevin.courseApp.core.Result
import com.kevin.courseApp.data.model.Posts
import com.kevin.courseApp.data.remote.home.HomeScreenDataSource


class HomeScreenRepoImplement(private val dataSource: HomeScreenDataSource): HomeScreenRepo {
override suspend fun getLatestPost(): Result<List<Posts>>   = dataSource.getLatestPost()
    override suspend fun registerLikeButtonState(postId: String, liked: Boolean) = dataSource.registerLikeButtonState(postId,liked)
}