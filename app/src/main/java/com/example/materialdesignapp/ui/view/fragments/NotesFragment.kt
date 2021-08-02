package com.example.materialdesignapp.ui.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.materialdesignapp.R
import com.example.materialdesignapp.model.Note
import com.example.materialdesignapp.viewmodel.ItemTouchHelperCallback
import com.example.materialdesignapp.viewmodel.NotesActivityAdapter
import com.example.materialdesignapp.viewmodel.NotesProvider
import com.example.materialdesignapp.viewmodel.NotesViewModel
import kotlinx.android.synthetic.main.notes_fragment.*

class NotesFragment: Fragment() {

    private var notesProvider = NotesProvider.Companion
    lateinit var itemTouchHelper: ItemTouchHelper
    private val viewModel: NotesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

        val adapter = NotesActivityAdapter(
            object : NotesActivityAdapter.OnListItemClickListener {
                override fun onItemClick(data: Note) {
                    Toast.makeText(context, data.title, Toast.LENGTH_SHORT).show()
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
        recyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))

        val observer = Observer<Pair<Note, Boolean>> { renderData(it) }
        viewModel.getData().observe(viewLifecycleOwner, observer)
    }

    private fun renderData(data: Pair<Note, Boolean>) {
        notesProvider.addNote(data.first, data.second)
        Toast.makeText(context, "List was changed", Toast.LENGTH_LONG).show()
    }
}