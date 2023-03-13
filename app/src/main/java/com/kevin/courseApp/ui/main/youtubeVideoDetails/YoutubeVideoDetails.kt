package com.kevin.courseApp.ui.main.youtubeVideoDetails


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class YoutubeVideoDetails : Fragment()   {

    private var currentPosition = 0
    private val videoIds = arrayOf("6JYIGclVQdw", "LvetJ9U_tVY", "S0Q4gqBUs7c")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

}
