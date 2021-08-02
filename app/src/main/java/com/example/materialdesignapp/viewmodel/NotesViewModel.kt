package com.example.materialdesignapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.materialdesignapp.model.Note

class NotesViewModel(private val liveDataToObserve: MutableLiveData<Pair<Note, Boolean>> = MutableLiveData()) :
    ViewModel() {

    fun getData(): MutableLiveData<Pair<Note, Boolean>> {
            return liveDataToObserve
    }

    fun addNote (newNote : Pair<Note, Boolean>) {
        liveDataToObserve.postValue(newNote)
    }
}