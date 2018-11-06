package com.deepak.knote.view.adapter

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.deepak.knote.R
import com.deepak.knote.service.db.model.TrashNote
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk27.coroutines.onClick

/**
 * The Adapter for the RecyclerView to view all notes
 * This adapter class extending the ListAdapter
 * (not the RecyclerView.Adapter class because of the DiffUtil class)
 * ListAdapter is made on the top of DiffUtil class to provide the smooth animation
 */
class TrashAdapter(private val listener: (TrashNote, Int) -> Unit) :
        ListAdapter<TrashNote, TrashAdapter.TrashViewHolder>(DiffUtilTrash()) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): TrashViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_note, viewGroup, false)
        return TrashViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: TrashViewHolder, position: Int) {
        val note = getItem(position)
        viewHolder.noteTitle.text = note.noteTitle
        viewHolder.noteContent.text = note.noteContent
        viewHolder.itemView.onClick { listener(note, position) }
    }

    fun getNoteAt(position: Int): TrashNote = getItem(position)

    class TrashViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var noteTitle = itemView.find<TextView>(R.id.item_note_title)
        var noteContent = itemView.find<TextView>(R.id.item_note_content)
    }
}