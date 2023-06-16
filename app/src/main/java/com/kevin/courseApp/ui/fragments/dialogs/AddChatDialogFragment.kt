package com.kevin.courseApp.ui.fragments.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kevin.courseApp.R
import com.kevin.courseApp.utils.openai.Hash
import com.kevin.courseApp.utils.openai.preferences.ChatPreferences
import com.kevin.courseApp.utils.openai.preferences.Preferences

class AddChatDialogFragment : DialogFragment() {

    companion object {
        public fun newInstance(name: String, fromFile: Boolean) : AddChatDialogFragment {
            val addChatDialogFragment = AddChatDialogFragment()

            val args = Bundle()
            args.putString("name", name)
            args.putBoolean("fromFile", fromFile)

            addChatDialogFragment.arguments = args

            return addChatDialogFragment
        }
    }

    private var builder: AlertDialog.Builder? = null

    private var context: Context? = null

    private var nameInput: EditText? = null

    private var listener: StateChangesListener? = null

    private var isEdit = false

    private var chatPreferences: ChatPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this.activity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_chat_dialog, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        builder = MaterialAlertDialogBuilder(this.requireContext())

        chatPreferences = ChatPreferences.getChatPreferences()

        val view: View = this.layoutInflater.inflate(R.layout.fragment_add_chat_dialog, null)

        nameInput = view.findViewById(R.id.field_name)

        val dialogTitle: TextView = view.findViewById(R.id.dialog_title)

        if (requireArguments().getString("name") != "") {
            dialogTitle.text = requireActivity().resources.getString(R.string.title_edit_chat)

            nameInput?.setText(requireArguments().getString("name"))

            builder!!.setView(view)
                .setCancelable(false)
                .setPositiveButton("OK") { _, _ -> validateForm() }
                .setNeutralButton("Delete") { _, _ -> confirmDeletion(requireActivity()) }
                .setNegativeButton("Cancel") { _, _ -> listener!!.onCanceled() }

            isEdit = true

            return builder!!.create()
        } else {
            dialogTitle.text = requireActivity().resources.getString(R.string.title_new_chat)

            nameInput?.setText("New chat ${chatPreferences?.getAvailableChatId(requireActivity())}")

            builder!!.setView(view)
                .setCancelable(false)
                .setPositiveButton("OK") { _, _ -> validateForm() }
                .setNegativeButton("Cancel") { _, _: Int -> listener!!.onCanceled() }

            return builder!!.create()
        }
    }

    private fun validateForm() {
        if (nameInput?.text.toString() == "") {
            listener!!.onError(arguments?.getBoolean("fromFile") == true)
        } else {
            if (chatPreferences?.checkDuplicate(requireActivity(), nameInput?.text.toString()) == false) {
                val preferences: Preferences = if (isEdit) {
                    chatPreferences?.editChat(requireActivity(), nameInput?.text.toString(), requireArguments().getString("name").toString())
                    listener!!.onEdit(nameInput?.text.toString(), Hash.hash(nameInput?.text.toString()))

                    // Transfer settings
                    Preferences.getPreferences(requireActivity(), Hash.hash(arguments?.getString("name").toString()))
                } else {
                    chatPreferences?.addChat(requireActivity(), nameInput?.text.toString())
                    listener!!.onAdd(nameInput?.text.toString(), Hash.hash(nameInput?.text.toString()), arguments?.getBoolean("fromFile") == true)

                    // Copy settings from default
                    Preferences.getPreferences(requireActivity(), "")
                }

                // Write settings
                val resolution = preferences.getResolution()
                val speech = preferences.getAudioModel()
                val model = preferences.getModel()
                val maxTokens = preferences.getMaxTokens()
                val prefix = preferences.getPrefix()
                val endSeparator = preferences.getEndSeparator()
                val activationPrompt = preferences.getPrompt()
                val layout = preferences.getLayout()
                val silent = preferences.getSilence()

                preferences.setPreferences(Hash.hash(nameInput?.text.toString()), requireActivity())
                preferences.setResolution(resolution)
                preferences.setAudioModel(speech)
                preferences.setModel(model)
                preferences.setMaxTokens(maxTokens)
                preferences.setPrefix(prefix)
                preferences.setEndSeparator(endSeparator)
                preferences.setPrompt(activationPrompt)
                preferences.setLayout(layout)
                preferences.setSilence(silent)
            } else {
                listener!!.onDuplicate()
            }
        }
    }

    private fun confirmDeletion(context: Context) {
        MaterialAlertDialogBuilder(requireActivity() )
            .setTitle("Confirm deletion")
            .setMessage("This action can not be undone.")
            .setPositiveButton("Delete") { _, _ -> delete(context) }
            .setNegativeButton("Cancel") { _, _ -> }
            .show()
    }

    private fun delete(context: Context) {
        chatPreferences?.deleteChat(context, requireArguments().getString("name").toString())
        listener!!.onDelete()
    }

    fun setStateChangedListener(listener: StateChangesListener) {
        this.listener = listener
    }

    interface StateChangesListener {
        fun onAdd(name: String, id: String, fromFile: Boolean)
        fun onEdit(name: String, id: String)
        fun onError(fromFile: Boolean)
        fun onCanceled()
        fun onDelete()
        fun onDuplicate()
    }
}