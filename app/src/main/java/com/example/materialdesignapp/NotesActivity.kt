package com.example.materialdesignapp

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.materialdesignapp.model.Note
import com.example.materialdesignapp.ui.view.fragments.AddNoteFragment
import com.example.materialdesignapp.viewmodel.ItemTouchHelperCallback
import com.example.materialdesignapp.viewmodel.NotesActivityAdapter
import com.example.materialdesignapp.viewmodel.NotesRepository
import com.example.materialdesignapp.viewmodel.room.AppDatabase
import kotlinx.android.synthetic.main.notes_fragment.*


class NotesActivity: AppCompatActivity() {

    private var notesRepository = NotesRepository.Companion
    lateinit var itemTouchHelper: ItemTouchHelper

    override fun onCreate(savedInstanceState: Bundle?) {

        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        setTheme(sharedPref.getInt("Theme", 1))

        super.onCreate(savedInstanceState)
        setContentView(R.layout.notes_fragment)

        notesFAB.setOnClickListener{
            notesFAB.visibility = View.GONE
            supportFragmentManager.beginTransaction().add(R.id.notes_container, AddNoteFragment())
                .addToBackStack("")
                .commit()
        }

        val adapter = NotesActivityAdapter(
            object : NotesActivityAdapter.OnListItemClickListener {
                override fun onItemClick(data: Note) {
                    Toast.makeText(this@NotesActivity, data.title, Toast.LENGTH_SHORT).show()
                }
            },
            notesRepository.getNotesList(),
            object : NotesActivityAdapter.OnStartDragListener {
                override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
                    itemTouchHelper.startDrag(viewHolder)
                }
            }
        )

        recyclerView.adapter = adapter
        itemTouchHelper = ItemTouchHelper(ItemTouchHelperCallback(adapter))
        itemTouchHelper.attachToRecyclerView(recyclerView)
        recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

    }

}