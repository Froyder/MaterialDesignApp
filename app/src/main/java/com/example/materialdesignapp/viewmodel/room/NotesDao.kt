package com.example.materialdesignapp.viewmodel.room

import androidx.room.*
import com.example.materialdesignapp.model.Note

@Dao
interface NotesDao {

    @Query("SELECT * FROM note")
    fun getAll(): List<Note>

//    @Query("SELECT * FROM Note WHERE nid IN (:notesIds)")
//    fun loadAllByIds(notesIds: IntArray): List<Note>

    @Query("SELECT * FROM note WHERE title LIKE :title AND " +
            "text LIKE :text LIMIT 1")
    fun findByTitle(title: String, text: String): Note

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(note: Note)

    @Insert
    fun insertAll(vararg notes: Note)

    @Delete
    fun delete(user: Note)
}