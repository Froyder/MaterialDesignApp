package com.example.materialdesignapp.viewmodel

import android.graphics.Color
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MotionEventCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.materialdesignapp.R
import com.example.materialdesignapp.model.Note
import kotlinx.android.synthetic.main.notes_item.view.*

class NotesActivityAdapter(
    private var onListItemClickListener: OnListItemClickListener,
    private var data: MutableList<Pair<Note, Boolean>>,
    private val dragListener: OnStartDragListener
) :
    RecyclerView.Adapter<BaseViewHolder>(), ItemTouchHelperAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_NOTE -> NotesViewHolder(
                inflater.inflate(R.layout.notes_item, parent, false) as View
            )
            else -> HeaderViewHolder(
                inflater.inflate(R.layout.notes_header, parent, false) as View
            )
        }
    }

    fun appendItem() {
        generateItem()
        notifyItemInserted(itemCount - 1)
    }

//    private fun addNote (){
//        data.add(Pair(Note(note.text, note.title), expanded))
//    }

    private fun generateItem(): Pair<Note, Boolean> = Pair(Note("Title", ""), false)

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position == 0 -> TYPE_HEADER
            else -> TYPE_NOTE
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        data.removeAt(fromPosition).apply {
            data.add(if (toPosition > fromPosition) toPosition - 1 else toPosition, this)
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class NotesViewHolder(view: View) : BaseViewHolder(view), ItemTouchHelperViewHolder  {

        override fun bind(data: Pair<Note, Boolean>) {
            setIcon(data.first.importance)
            itemView.noteImage.setOnClickListener { onListItemClickListener.onItemClick(data.first) }
            itemView.addItemImageView.setOnClickListener { addItem() }
            itemView.removeItemImageView.setOnClickListener { removeItem() }
            itemView.moveItemDown.setOnClickListener { moveDown() }
            itemView.moveItemUp.setOnClickListener { moveUp() }
            itemView.noteText.visibility =
                if (data.second) View.VISIBLE else View.GONE
            itemView.noteTitle.setOnClickListener { toggleText() }
            itemView.noteText.text = data.first.text
            itemView.noteTitle.text = data.first.title
            itemView.note_date.text = data.first.date
            itemView.dragHandleImageView.setOnTouchListener { _, event ->
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    dragListener.onStartDrag(this)
                }
                false
            }
        }

        private fun setIcon (color: String) {
            when (color) {
                "red" -> itemView.noteImage.setImageResource(R.drawable.ic_note_red)
                "yellow" -> itemView.noteImage.setImageResource(R.drawable.ic_note_yellow)
                "green" -> itemView.noteImage.setImageResource(R.drawable.ic_note_green)
                "" -> itemView.noteImage.setImageResource(R.drawable.ic_note_green)
            }
        }

        private fun toggleText() {
            data[layoutPosition] = data[layoutPosition].let {
                it.first to !it.second
            }
            notifyItemChanged(layoutPosition)
        }

        private fun addItem() {
            data.add(layoutPosition, generateItem())
            notifyItemInserted(layoutPosition)
        }

        private fun removeItem() {
            data.removeAt(layoutPosition)
            notifyItemRemoved(layoutPosition)
        }

        private fun moveUp() {
            layoutPosition.takeIf { it > 1 }?.also { currentPosition ->
                data.removeAt(currentPosition).apply {
                    data.add(currentPosition - 1, this)
                }
                notifyItemMoved(currentPosition, currentPosition - 1)
            }
        }

        private fun moveDown() {
            layoutPosition.takeIf { it < data.size - 1 }?.also { currentPosition ->
                data.removeAt(currentPosition).apply {
                    data.add(currentPosition + 1, this)
                }
                notifyItemMoved(currentPosition, currentPosition + 1)
            }
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(Color.WHITE)
        }
    }

    interface OnStartDragListener {
        fun onStartDrag(viewHolder: RecyclerView.ViewHolder)
    }

    inner class HeaderViewHolder(view: View) : BaseViewHolder(view) {

        override fun bind(data: Pair<Note, Boolean>) {
            itemView.setOnClickListener { onListItemClickListener.onItemClick(data.first) }
        }
    }

    interface OnListItemClickListener {
        fun onItemClick(data: Note)
    }

//    interface ItemTouchHelperAdapter {
//        fun onItemMove(fromPosition: Int, toPosition: Int)
//
//        fun onItemDismiss(position: Int)
//    }

//    interface ItemTouchHelperViewHolder {
//
//        fun onItemSelected()
//
//        fun onItemClear()
//    }

    companion object Factory {
        private const val TYPE_NOTE = 0
        private const val TYPE_HEADER = 1
    }
}