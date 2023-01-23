package com.kevin.blogappkotlin.domain.home

import com.kevin.blogappkotlin.core.Result
import com.kevin.blogappkotlin.data.model.Posts
import com.kevin.blogappkotlin.data.remote.home.HomeScreenDataSource

class HomeScreenRepoImplement(private val dataSource: HomeScreenDataSource): HomeScreenRepo {
    override suspend fun getLatestPost(): Result<List<Posts>> {
        return dataSource.getLatestPost()
    }
}