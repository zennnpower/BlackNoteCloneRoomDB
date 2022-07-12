package com.example.blacknoteroomdb.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.blacknoteroomdb.R

class NoteListAdapter(
    private val itemClickListener: (Int, Note) -> Unit) : ListAdapter<Note, NoteListAdapter.NoteViewHolder>(NOTES_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val current_note = getItem(position)
        holder.bind(current_note, itemClickListener)
    }

    fun getNoteAt(position: Int): Note {
        return getItem(position)
    }

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val noteItemView: TextView = itemView.findViewById(R.id.note_title)

        fun bind(note: Note, itemClickListener: (Int, Note) -> Unit) {
            noteItemView.text = note.title
            itemView.setOnClickListener {
                itemClickListener(adapterPosition, note)
            }
        }

        companion object {
            fun create(parent: ViewGroup): NoteViewHolder {
                val view: View = LayoutInflater.from(parent.context).inflate(R.layout.notes_grid_item, parent, false)
                return NoteViewHolder(view)
            }
        }
    }

    companion object {
        private val NOTES_COMPARATOR = object : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.content == newItem.content
            }
        }
    }
}