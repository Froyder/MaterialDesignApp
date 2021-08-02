package com.example.materialdesignapp.ui.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.materialdesignapp.R
import com.example.materialdesignapp.model.Note
import com.example.materialdesignapp.ui.view.fragments.AddNoteFragment
import com.example.materialdesignapp.viewmodel.ItemTouchHelperCallback
import com.example.materialdesignapp.viewmodel.NotesActivityAdapter
import com.example.materialdesignapp.viewmodel.NotesProvider
import com.example.materialdesignapp.viewmodel.NotesViewModel
import kotlinx.android.synthetic.main.notes_fragment.*


class NotesActivity: AppCompatActivity() {

    private var notesProvider = NotesProvider.Companion
    lateinit var itemTouchHelper: ItemTouchHelper

    override fun onCreate(savedInstanceState: Bundle?) {

        val sharedPref = getSharedPreferences("THEME", MODE_PRIVATE)
        setTheme(sharedPref.getInt("Theme", 1))

        super.onCreate(savedInstanceState)
        setContentView(R.layout.notes_fragment)

        val viewModel = ViewModelProvider(this).get(NotesViewModel::class.java)

        notesFAB.setOnClickListener{
            //notesFAB.visibility = View.GONE
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
            notesProvider.getNotesList(),
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

        val observer = Observer<Pair<Note, Boolean>> { renderData(it) }
        viewModel.getData().observe(this, observer)
    }

    private fun renderData(data: Pair<Note, Boolean>) {
        notesProvider.addNote(data.first, data.second)
        Toast.makeText(this, "List was changed", Toast.LENGTH_LONG).show()
    }

}