package com.deepak.knote.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.deepak.knote.R
import com.deepak.knote.service.db.Note
import org.jetbrains.anko.find

class KNoteAdapter(private var noteList: List<Note>, private val listener: (Note, Int) -> Unit) : RecyclerView.Adapter<KNoteAdapter.KNoteViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): KNoteViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_note, viewGroup, false)
        return KNoteViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: KNoteViewHolder, position: Int) {
        val note = noteList[position]
        viewHolder.noteTitle?.text = note.noteTitle
        viewHolder.noteContent?.text = note.noteContent
        viewHolder.itemView.setOnClickListener { listener(note, position) }
    }

    override fun getItemCount(): Int = noteList.size

    fun getNoteAt(position: Int): Note = noteList[position]

    fun setNotes(notes: MutableList<Note>) {
        noteList = notes
        notifyDataSetChanged()
    }

    class KNoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var noteTitle: TextView? = itemView.find(R.id.item_note_title) as TextView
        var noteContent: TextView? = itemView.find(R.id.item_note_content) as TextView
    }
}