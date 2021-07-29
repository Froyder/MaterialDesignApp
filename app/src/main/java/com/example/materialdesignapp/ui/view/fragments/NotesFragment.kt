package com.example.materialdesignapp.ui.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.materialdesignapp.R
import com.example.materialdesignapp.model.Note
import com.example.materialdesignapp.viewmodel.ItemTouchHelperCallback
import com.example.materialdesignapp.viewmodel.NotesActivityAdapter
import com.example.materialdesignapp.viewmodel.NotesRepository
import kotlinx.android.synthetic.main.notes_fragment.*

class NotesFragment: Fragment() {

    private var notesRepository = NotesRepository.Companion

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setFragmentResultListener("NOTE_REQUEST") { requestKey, bundle ->
            val noteTitle : String = (bundle.getString("title")) as String
            val noteText : String = (bundle.getString("text"))as String
        }

        return inflater.inflate(R.layout.notes_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notesFAB.setOnClickListener{
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.notes_container, AddNoteFragment())
                ?.addToBackStack("notes")
                ?.commit()
        }

//        val adapter = NotesActivityAdapter(
//            object : NotesActivityAdapter.OnListItemClickListener {
//                override fun onItemClick(data: Note) {
//                    Toast.makeText(context, data.title, Toast.LENGTH_SHORT).show()
//                }
//            },
//            notesRepository.getNotesList()
//        )
//
//        recyclerView.adapter = adapter
//        ItemTouchHelper(ItemTouchHelperCallback(adapter)).attachToRecyclerView(recyclerView)
    }

}