package com.example.materialdesignapp.viewmodel.room

import androidx.fragment.app.FragmentActivity
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.materialdesignapp.model.Note

@Database(entities = arrayOf(Note::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun notesDao(): NotesDao

}