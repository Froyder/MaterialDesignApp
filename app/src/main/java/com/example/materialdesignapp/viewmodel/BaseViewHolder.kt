package com.example.materialdesignapp.viewmodel

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.materialdesignapp.model.Note

abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(data: Pair<Note, Boolean>)
}