package com.kevin.blogappkotlin.domain

import com.kevin.blogappkotlin.core.Resource
import com.kevin.blogappkotlin.data.model.Posts
import com.kevin.blogappkotlin.data.remote.HomeScreenDataSource
import javax.sql.DataSource

class HomeScreenRepoImplement(private val dataSource: HomeScreenDataSource): HomeScreenRepo {
    override suspend fun getLatestPost(): Resource<List<Posts>> {
        return dataSource.getLatestPost()
    }
}