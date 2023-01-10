package com.kevin.blogappkotlin.data.model

import com.google.firebase.Timestamp

data class Posts (
    val profile_pictura: String ="",
    val profile_name : String="",
        val post_timestamp: Timestamp?= null,
    val post_image:String=""
        )