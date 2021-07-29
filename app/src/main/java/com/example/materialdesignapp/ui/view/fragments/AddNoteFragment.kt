package com.example.materialdesignapp.ui.view.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.example.materialdesignapp.NotesActivity
import com.example.materialdesignapp.R
import com.example.materialdesignapp.model.Note
import com.example.materialdesignapp.viewmodel.NotesRepository
import com.example.materialdesignapp.viewmodel.room.AppDatabase
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.add_note_layout.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class AddNoteFragment : Fragment(){

    private var notesRepository = NotesRepository.Companion
    private var importance :String = ""


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
                    "Need it!" -> importance = "red"
                    "Not really" -> importance = "yellow"
                    "Just forget" -> importance = "green"
                }
            }
        }

        add_note_button.setOnClickListener {

            if (title_input.text.toString() != "") {
                val date = getDate()
                val color = importance
                notesRepository.addNote(
                    Note(title_input.text.toString(), text_input.text.toString(), date, color),
                    true
                )


                activity?.recreate()
                activity?.supportFragmentManager?.popBackStack()
            } else {
                title_input.text = getString(R.string.note_title_warning).toEditable()
                Toast.makeText(context, "Note title can't be empty!", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

    private fun getDate (): String {
        val cal = Calendar.getInstance()
        val dateFormat: DateFormat = SimpleDateFormat("hh:mm, dd-MM-yyy", Locale.ENGLISH)

        cal.add(Calendar.DATE, 0)

        return dateFormat.format(cal.time)
    }

}