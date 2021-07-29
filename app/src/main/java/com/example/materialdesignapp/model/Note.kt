package com.example.materialdesignapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo val title: String,
    @ColumnInfo val text: String = "Text",
    @ColumnInfo val date: String = "Date",
    @ColumnInfo val importance: String = "Importance",
    @ColumnInfo val expanded: Boolean = true
    )
