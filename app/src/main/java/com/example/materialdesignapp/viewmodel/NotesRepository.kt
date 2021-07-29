package com.example.materialdesignapp.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.materialdesignapp.model.Note

class NotesRepository {

    companion object {

        val data = mutableListOf(
            Pair(Note("Header"), false)
        )

        fun addNotesFromDB (list : List<Note>) {

        }

        fun getNotesList(): MutableList<Pair<Note, Boolean>> {
            return data
        }

        fun addNote(note: Note, expanded: Boolean) {
            data.add(Pair(Note(note.title, note.text, note.date, note.importance), expanded))
        }

        val dataTest = MutableLiveData<Pair<Note, Boolean>>()

        fun getTestList(): MutableLiveData<Pair<Note, Boolean>>{
            return dataTest
        }

        fun addTest() {
            dataTest.postValue(Pair(Note("Header"), false))
            dataTest.postValue(Pair(Note("TestLD", "Text 333", "Yesterday"), false))
            dataTest.postValue(Pair(Note("TestLD", "Text 333", "Yesterday"), false))
            dataTest.postValue(Pair(Note("TestLD", "Text 333", "Yesterday"), false))
        }

        fun addTestNote(note: Note, expanded: Boolean) {
            data.add(Pair(Note(note.title, note.text, "Yesterday"), expanded))
        }

    }
}