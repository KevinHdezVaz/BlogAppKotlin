package com.kevin.blogappkotlin.domain.home

import com.kevin.blogappkotlin.core.Result
import com.kevin.blogappkotlin.data.model.Posts

interface HomeScreenRepo
{
    suspend fun getLatestPost(): Result<List<Posts>>
}