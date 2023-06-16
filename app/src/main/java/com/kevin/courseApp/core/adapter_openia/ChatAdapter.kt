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
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.kevin.courseApp.R
import com.kevin.courseApp.utils.openai.preferences.Preferences

class ChatAdapter(data: ArrayList<HashMap<String, Any>>?, context: FragmentActivity, private val chatID: String) : AbstractChatAdapter(data, context) {

    @SuppressLint("InflateParams", "SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val layout  = Preferences.getPreferences(mContext, chatID).getLayout()

        val mView: View? = if (layout == "bubbles") {
            if (dataArray?.get(position)?.get("isBot") == true) {
                inflater.inflate(R.layout.view_assistant_bot_message, null)
            } else {
                inflater.inflate(R.layout.view_assistant_user_message, null)
            }
        } else {
            inflater.inflate(R.layout.view_message, null)
        }

        ui = mView!!.findViewById(R.id.ui)
        icon = mView.findViewById(R.id.icon)
        message = mView.findViewById(R.id.message)
        val username: TextView = mView.findViewById(R.id.username)
        dalleImage = mView.findViewById(R.id.dalle_image)
        // imageFrame = mView.findViewById(R.id.dalle_image)
        btnCopy = mView.findViewById(R.id.btn_copy)

        super.getView(position, mView, parent)

        if (layout == "bubbles") {
            if (dataArray?.get(position)?.get("isBot") == true) {
                icon?.setImageResource(R.drawable.logofinal)
            } else {
                icon?.setImageResource(R.drawable.ic_user)
            }
        } else {
            if (dataArray?.get(position)?.get("isBot") == true) {
                icon?.setImageResource(R.drawable.logofinal)
                username.text = mContext.resources.getString(R.string.app_name)
                ui?.setBackgroundResource(R.color.colorchat)
            } else {
                icon?.setImageResource(R.drawable.ic_user)
                username.text = "Usuario"
                ui?.setBackgroundResource(R.color.marker)
                btnCopy?.visibility = View.GONE
            }
        }

        return mView
    }
}