package com.kevin.blogappkotlin.domain.home

import com.kevin.blogappkotlin.core.Resource
import com.kevin.blogappkotlin.data.model.Posts

interface HomeScreenRepo
{
    suspend fun getLatestPost(): Resource<List<Posts>>
}