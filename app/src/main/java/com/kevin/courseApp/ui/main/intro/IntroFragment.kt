package com.kevin.courseApp.ui.main.intro

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.icu.text.Transliterator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.tabs.TabLayoutMediator
import com.kevin.courseApp.R
import com.kevin.courseApp.databinding.FragmentIntroBinding
import kotlin.math.nextUp

class IntroFragment: Fragment() {

    private lateinit var mViewPager: ViewPager2
    private var _binding: FragmentIntroBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIntroBinding.inflate(inflater, container, false)
        val view = binding.root


        if (!isFirstTime(requireContext())) {
            findNavController().navigate(R.id.action_introFragment2_to_loginFragment)
        }



        val myViewPager: ViewPager2 = binding.viewPagerMainActivity


        myViewPager.adapter = MyViewPagerAdapter()
        val myMotionLayout: MotionLayout = binding.layoutMainMotionLayout
        val buttonNextPage: ImageButton = binding.buttonNextPage
        val progressIndicator: CircularProgressIndicator = binding.mainProgressInidicator


        buttonNextPage.setOnClickListener {
            if (myViewPager.currentItem != 2) {
                myViewPager.setCurrentItem(myViewPager.currentItem + 1, true)
            }
        }

        myViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                val currProgress = (position + positionOffset) / 2
                myMotionLayout.progress = currProgress
                progressIndicator.progress =
                    (((myViewPager.currentItem + 1) / 3f).nextUp() * 100).toInt()

                when (position) {
                    2 ->
                    {    buttonNextPage.setImageResource(R.drawable.ic_done)

                        binding.buttonNextPage.setOnClickListener {
                            findNavController().navigate(R.id.action_introFragment2_to_loginFragment)
                        }
                    }

                    else -> buttonNextPage.setImageResource(R.drawable.ic_next)
                }
            }
        })





        return view
    }


    private fun isFirstTime(context: Context): Boolean {
        val preferences = context.getSharedPreferences("myPreferencesFile",  MODE_PRIVATE)
        val ranBefore = preferences.getBoolean("RanBefore4", false)
        if (!ranBefore) {
            // first time
            val editor = preferences.edit()
            editor.putBoolean("RanBefore4", true)
            editor.apply()
        }
        return !ranBefore
    }



}
