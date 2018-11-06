package com.deepak.knote.view.adapter

import android.support.v7.util.DiffUtil
import com.deepak.knote.service.db.model.TrashNote

/**
 * DiffUtil class which takes care of all the data insertion and deletion
 * to provide smooth transition
 */
class DiffUtilTrash : DiffUtil.ItemCallback<TrashNote>() {
    override fun areItemsTheSame(oldTrashNote: TrashNote, newTrashNote: TrashNote): Boolean {
        return oldTrashNote.id == newTrashNote.id
    }

    override fun areContentsTheSame(oldTrashNote: TrashNote, newTrashNote: TrashNote): Boolean {
        return (oldTrashNote.id == newTrashNote.id
                && oldTrashNote.noteTitle == newTrashNote.noteTitle
                && oldTrashNote.noteContent == newTrashNote.noteContent)
    }
}