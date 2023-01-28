package com.kevin.blogappkotlin.domain.home

import com.kevin.blogappkotlin.core.Result
import com.kevin.blogappkotlin.data.model.Posts
import com.kevin.blogappkotlin.data.remote.home.HomeScreenDataSource
import kotlinx.coroutines.flow.Flow


class HomeScreenRepoImplement(private val dataSource: HomeScreenDataSource): HomeScreenRepo {
override suspend fun getLatestPost(): Flow<Result<List<Posts>>> = dataSource.getLatestPost()
}