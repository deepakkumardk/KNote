package com.deepak.knote.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import com.deepak.knote.R
import com.deepak.knote.service.db.model.Note
import org.jetbrains.anko.find

/**
 * The Adapter for the RecyclerView to view all notes
 * This adapter class extending the ListAdapter
 * (not the RecyclerView.Adapter class because of the DiffUtil class)
 * ListAdapter is made on the top of DiffUtil class to provide the smooth animation
 */
class KNoteAdapter(private val listener: (Note, Int) -> Unit) :
        ListAdapter<Note, KNoteAdapter.KNoteViewHolder>(DiffUtilMain()) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): KNoteViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_note, viewGroup, false)
        return KNoteViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: KNoteViewHolder, position: Int) {
        val note = getItem(position)
        viewHolder.noteTitle.text = note.noteTitle
        viewHolder.noteContent.text = note.noteContent
        viewHolder.itemView.setOnClickListener { listener(note, position) }
    }

    fun getNoteAt(position: Int): Note = getItem(position)

    class KNoteViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        var noteTitle = itemView.find<TextView>(R.id.item_note_title)
        var noteContent = itemView.find<TextView>(R.id.item_note_content)
    }
}