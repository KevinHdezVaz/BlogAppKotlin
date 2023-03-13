package com.kevin.courseApp.ui.main.youtubeVideoDetails

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.kevin.courseApp.R

class YoutubeVideoActvity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube_video_actvity)
        val viewPager = findViewById<ViewPager>(R.id.view_pager)
        val viewPagerAdapter: PagerAdapter = ViewPagerAdapter(supportFragmentManager)
        viewPager.adapter = viewPagerAdapter
    }

    private class ViewPagerAdapter internal constructor(fm: FragmentManager?) :
        FragmentStatePagerAdapter(fm!!) {
        override fun getItem(position: Int): Fragment {
            return YoutubeVideoDetails()
        }

        override fun getCount(): Int {
            return 3
            //CAMBIAR PARAMETRO DESPUES
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return "Page $position"
        }
    }
}