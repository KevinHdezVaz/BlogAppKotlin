package com.kevin.courseApp.data.model

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Posts (

    @Exclude @JvmField
    var id: String = "",
    val profile_name : String="",
    @ServerTimestamp
        var created_at  : Date?= null,
    val post_image:String="",
    val post_description:String="",
    val poster: Posters ?=null,
    val likes : Long =0,

    @Exclude @JvmField
    var liked : Boolean =false

        )

data class Posters(val username:String?= null, val uid: String?= null,val profile_pictura: String ="",)