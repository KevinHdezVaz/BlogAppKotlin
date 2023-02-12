package com.kevin.courseApp.core

import java.util.concurrent.TimeUnit
private const val SECOND_MILS =1
private const val MINUTE_MILS = 60* SECOND_MILS
private const val HOURS_MILS = 60* MINUTE_MILS
private const val DAYS_MILS = 24* HOURS_MILS

object TimeAgo {
    fun getTimeAgo(time:Int):String{
        val now =   TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
        if(time >= now || time <= 0 ){
            return "in the future"
        }
        val diff = now- time
        return when{
            diff < MINUTE_MILS -> "Just now"
            diff < 2* MINUTE_MILS -> "a Minutes ago"
            diff < 60 * MINUTE_MILS -> "${diff / MINUTE_MILS} minutes ago"
            diff < 2 * HOURS_MILS -> "an Hour ago"
            diff < 24*  HOURS_MILS -> "${diff / HOURS_MILS} hours ago"
            diff < 48* HOURS_MILS -> "Yesterday"
            else -> "${diff / DAYS_MILS} days ago"





        }
    }
}