
package com.kevin.courseApp.core.adapter_openia

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import com.google.android.material.elevation.SurfaceColors
import com.kevin.courseApp.R
import com.kevin.courseApp.ui.fragments.dialogs.AddChatDialogFragment
import com.kevin.courseApp.ui.fragments.tabs.ChatsListFragment
import com.kevin.courseApp.ui.openia.Menu.ChatActivity
import com.kevin.courseApp.utils.openai.Hash
import com.kevin.courseApp.utils.openai.preferences.Preferences

class ChatListAdapter(data: ArrayList<HashMap<String, String>>?, context: Fragment) : BaseAdapter() {
    private val dataArray: ArrayList<HashMap<String, String>>? = data
    private val mContext: Fragment = context

    override fun getCount(): Int {
        return if (dataArray == null) 0 else dataArray.size
    }

    override fun getItem(position: Int): Any {
        return dataArray!![position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("InflateParams", "SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = mContext.requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        var mView: View? = convertView

        if (mView == null) {
            mView = inflater.inflate(R.layout.view_chat_name, null)
        }

        val name: TextView = mView!!.findViewById(R.id.name)
        val selector: ConstraintLayout = mView.findViewById(R.id.chat_selector)
        val icon: ImageView = mView.findViewById(R.id.chat_icon)

        icon.setImageResource(R.drawable.chatgpt_icon)

        name.text = dataArray?.get(position)?.get("name").toString()

        val model: String = Preferences.getPreferences(mContext.requireActivity(), Hash.hash(dataArray?.get(position)?.get("name").toString())).getModel()


        if (position % 2 == 0) {
            selector.setBackgroundResource(R.drawable.btn_accent_selector_v2)
            selector.background = getDarkAccentDrawable(
                ContextCompat.getDrawable(mContext.requireActivity(), R.drawable.btn_accent_selector_v2)!!, mContext.requireActivity())
            icon.setBackgroundResource(R.drawable.btn_accent_tonal_v3)

        } else {
            selector.setBackgroundResource(R.drawable.btn_accent_selector)
            icon.setBackgroundResource(R.drawable.btn_accent_tonal_v3)

            icon.background = getDarkAccentDrawable(
                ContextCompat.getDrawable(mContext.requireActivity(), R.drawable.btn_accent_tonal_v3)!!, mContext.requireActivity())
        }

        selector.setOnClickListener {
            val i = Intent(
                     mContext.requireActivity(),
                    ChatActivity::class.java
            )

            i.putExtra("name", dataArray?.get(position)?.get("name").toString())
            i.putExtra("chatId", Hash.hash(dataArray?.get(position)?.get("name").toString()))

            mContext.requireActivity().startActivity(i)
        }

        selector.setOnLongClickListener {
            val chatDialogFragment: AddChatDialogFragment = AddChatDialogFragment.newInstance(name.text.toString(), false)
             chatDialogFragment.setStateChangedListener((mContext as ChatsListFragment).chatListUpdatedListener)
             chatDialogFragment.show(mContext.parentFragmentManager.beginTransaction(), "AddChatDialog")

            return@setOnLongClickListener true
        }

        return mView
    }

    private fun getDarkAccentDrawable(drawable: Drawable, context: Context) : Drawable {
        DrawableCompat.setTint(DrawableCompat.wrap(drawable), getSurfaceColor(context))
        return drawable
    }


    private fun getSurfaceColor(context: Context) : Int {
        return SurfaceColors.SURFACE_2.getColor(context)
    }


}