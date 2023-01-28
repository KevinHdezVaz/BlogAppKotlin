package com.kevin.blogappkotlin.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Posts (
    val profile_pictura: String ="",
    val profile_name : String="",
    @ServerTimestamp
        var created_at  : Date?= null,
    val post_image:String="",
    val post_description:String="",
    val uid :String=""
        )