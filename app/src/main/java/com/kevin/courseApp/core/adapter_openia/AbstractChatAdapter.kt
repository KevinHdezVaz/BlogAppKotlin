/**************************************************************************
 * Copyright (c) 2023 Dmytro Ostapenko. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **************************************************************************/

package com.kevin.courseApp.core.adapter_openia


import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.elevation.SurfaceColors
import com.kevin.courseApp.R
import io.noties.markwon.Markwon
import org.checkerframework.checker.units.qual.m
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.Base64


abstract class AbstractChatAdapter(data: ArrayList<HashMap<String, Any>>?, context: FragmentActivity) : BaseAdapter() {
    protected val dataArray: ArrayList<HashMap<String, Any>>? = data
    protected val mContext: FragmentActivity = context
    override fun getCount(): Int {
        return dataArray!!.size
    }
    override fun getItem(position: Int): Any {
        return dataArray!![position]
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    protected var ui: ConstraintLayout? = null
    protected var icon: ImageView? = null
    protected var message: TextView? = null
    protected var dalleImage: ImageView? = null
    protected var btnCopy: ImageButton? = null

    @SuppressLint("InflateParams", "SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        btnCopy?.setImageResource(R.drawable.ic_copy)
        btnCopy?.setOnClickListener {
            val clipboard: ClipboardManager = mContext.getSystemService(FragmentActivity.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("response", dataArray?.get(position)?.get("message").toString())
            clipboard.setPrimaryClip(clip)
            Toast.makeText(mContext, "Texto copiado al portapaleles", Toast.LENGTH_SHORT).show()
        }

        if (dataArray?.get(position)?.get("message").toString().contains("data:image")) {
            dalleImage?.visibility = View.VISIBLE
            message?.visibility = View.GONE
            btnCopy?.visibility = View.GONE

            val requestOptions = RequestOptions().transform(CenterCrop(), RoundedCorners(convertDpToPixel(24f, mContext).toInt()))
            Glide.with(mContext).load(Uri.parse(dataArray?.get(position)?.get("message").toString())).apply(requestOptions).into(dalleImage!!)


        } else if (dataArray?.get(position)?.get("message").toString().contains("~file:")) {
            dalleImage?.visibility = View.VISIBLE
            message?.visibility = View.GONE
            btnCopy?.visibility = View.GONE


            val contents: String = dataArray?.get(position)?.get("message").toString()
            val fileName: String = contents.replace("~file:", "")
            try {
                val fullPath = mContext.getExternalFilesDir("images")?.absolutePath + "/" + fileName + ".png"
                mContext.contentResolver.openFileDescriptor(Uri.fromFile(
                    File(fullPath))
                    , "r")?.use {
                    FileInputStream(it.fileDescriptor).use {
                        val c: ByteArray = it.readBytes()
                       // val m: String = "data:image/png;base64," + Base64.getEncoder().encodeToString(c)



                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                dalleImage?.visibility = View.GONE
                message?.visibility = View.VISIBLE
                btnCopy?.visibility = View.VISIBLE
                message?.text = "<FILE NOT FOUND>"
            }
        } else {
            val src = dataArray?.get(position)?.get("message").toString()
            val markwon: Markwon = Markwon.create(mContext)
            markwon.setMarkdown(message!!, src)

            dalleImage?.visibility = View.GONE
            message?.visibility = View.VISIBLE
        }

        return convertView!!
    }

    private fun convertDpToPixel(dp: Float, context: Context): Float {
        return dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }
    protected fun getSurfaceColor(context: Context): Int {
        return SurfaceColors.SURFACE_2.getColor(context)
    }
}