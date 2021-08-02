package com.example.materialdesignapp.ui.view.fragments

import android.app.Activity
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.materialdesignapp.R
import com.example.materialdesignapp.model.Note
import com.example.materialdesignapp.viewmodel.NotesProvider
import com.example.materialdesignapp.viewmodel.NotesViewModel
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.add_note_layout.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class AddNoteFragment : Fragment(){

    private var importance :String = ""
    private val viewModel: NotesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_note_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chipGroup.setOnCheckedChangeListener { chipGroup, position ->
            chipGroup.findViewById<Chip>(position)?.let {
                when (it.text.toString()) {
                    "High" -> importance = "red"
                    "Normal" -> importance = "yellow"
                    "Low" -> importance = "green"
                }
            }
        }

        add_note_button.setOnClickListener {
            if (title_input.text.toString() != "") {
                val date = getDate()
                val color = importance
                viewModel.addNote(Pair(
                    Note(title_input.text.toString(), text_input.text.toString(), date, color), true)
                )

                activity?.supportFragmentManager?.popBackStack()

                hideKeyboard(title_input)
            } else {
                title_input.text = getString(R.string.note_title_warning).toEditable()
                Toast.makeText(context, "Note title can't be empty!", Toast.LENGTH_SHORT).show()
            }

        }
    }

    fun hideKeyboard(view: View) {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

    private fun getDate (): String {
        val cal = Calendar.getInstance()
        val dateFormat: DateFormat = SimpleDateFormat("hh:mm, dd-MM-yyy", Locale.ENGLISH)

        cal.add(Calendar.DATE, 0)

        return dateFormat.format(cal.time)
    }

}