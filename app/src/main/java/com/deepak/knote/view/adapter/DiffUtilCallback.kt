package com.deepak.knote.view.adapter

import android.support.v7.util.DiffUtil
import com.deepak.knote.service.db.Note

/**
 * DiffUtil class which takes care of all the data insertion and deletion
 * to provide smooth transition
 */
class DiffUtilCallback : DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldNote: Note, newNote: Note): Boolean {
        return oldNote.id == newNote.id
    }

    override fun areContentsTheSame(oldNote: Note, newNote: Note): Boolean {
        return (oldNote.id == newNote.id
                && oldNote.noteTitle == newNote.noteTitle
                && oldNote.noteContent == newNote.noteContent)
    }
}