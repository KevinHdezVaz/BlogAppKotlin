package com.kevin.courseApp.ui.fragments.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.elevation.SurfaceColors
import com.kevin.courseApp.R
class LanguageSelectorDialogFragment : DialogFragment() {
    companion object {
        fun newInstance(name: String, chatId: String) : LanguageSelectorDialogFragment {
            val languageSelectorDialogFragment = LanguageSelectorDialogFragment()

            val args = Bundle()
            args.putString("name", name)
            args.putString("chatId", chatId)

            languageSelectorDialogFragment.arguments = args

            return languageSelectorDialogFragment
        }
    }

    private var builder: AlertDialog.Builder? = null

    private var context: Context? = null

    private var listener: StateChangesListener? = null

    private var language = "en"

    private var lngEn: RadioButton? = null
    private var lngFr: RadioButton? = null
    private var lngDe: RadioButton? = null
    private var lngIt: RadioButton? = null
    private var lngJp: RadioButton? = null
    private var lngKp: RadioButton? = null
    private var lngCnS: RadioButton? = null
    private var lngCnT: RadioButton? = null
    private var lngEs: RadioButton? = null
    private var lngUk: RadioButton? = null
    private var lngRu: RadioButton? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        builder = MaterialAlertDialogBuilder(this.requireContext(), )

        val view: View = this.layoutInflater.inflate(R.layout.fragment_language_selector_dialog, null)

        lngEn = view.findViewById(R.id.lngEn)
        lngFr = view.findViewById(R.id.lngFr)
        lngDe = view.findViewById(R.id.lngDe)
        lngIt = view.findViewById(R.id.lngIt)
        lngJp = view.findViewById(R.id.lngJp)
        lngKp = view.findViewById(R.id.lngKp)
        lngCnS = view.findViewById(R.id.lngCnS)
        lngCnT = view.findViewById(R.id.lngCnT)
        lngEs = view.findViewById(R.id.lngEs)
        lngUk = view.findViewById(R.id.lngUk)
        lngRu = view.findViewById(R.id.lngRu)

        builder!!.setView(view)
            .setCancelable(false)
            .setPositiveButton("OK") { _, _ -> validateForm() }
            .setNegativeButton("Cancel") { _, _ ->  }

        language = requireArguments().getString("name").toString()


        lngEs?.isChecked = language == "es"

        when (language) {

            "es" -> {
                clearSelection()
                lngEs?.setTextColor(ContextCompat.getColor(requireActivity(), R.color.window_background))
            }

        }


        lngEs?.setOnClickListener {
            language = "es"
            clearSelection()
            lngEs?.setTextColor(ContextCompat.getColor(requireActivity(), R.color.window_background))
         }

        return builder!!.create()
    }

    private fun clearSelection() {

        lngEs?.background = getDarkAccentDrawable(
            ContextCompat.getDrawable(requireActivity(), R.drawable.btn_accent_tonal_selector_v3)!!, requireActivity())
        lngEs?.setTextColor(ContextCompat.getColor(requireActivity(), R.color.neutral_200))


    }

    private fun getDarkAccentDrawable(drawable: Drawable, context: Context) : Drawable {
        DrawableCompat.setTint(DrawableCompat.wrap(drawable), getSurfaceColor(context))
        return drawable
    }

    private fun getSurfaceColor(context: Context) : Int {
        return SurfaceColors.SURFACE_3.getColor(context)
    }

    private fun validateForm() {
        if (language != "") {
            listener!!.onSelected(language)
        } else {
            listener!!.onFormError(language)
        }
    }

    fun setStateChangedListener(listener: StateChangesListener) {
        this.listener = listener
    }

    interface StateChangesListener {
        fun onSelected(name: String)
        fun onFormError(name: String)
    }
}