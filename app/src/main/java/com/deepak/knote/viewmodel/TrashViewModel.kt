package com.deepak.knote.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.deepak.knote.service.db.model.TrashNote
import com.deepak.knote.service.repository.TrashRepository

class TrashViewModel(application: Application) : AndroidViewModel(application) {
    private var repository: TrashRepository = TrashRepository(application)
    private var liveTrashNotes: LiveData<MutableList<TrashNote>> = repository.getLiveTrashNotes()
    private var trashNotesList: MutableList<TrashNote> = repository.getTrashNotesList()

    fun getLiveTrashNotes(): LiveData<MutableList<TrashNote>> = liveTrashNotes

    fun getTrashNotesList(): MutableList<TrashNote> = trashNotesList

    fun insertTrash(note: TrashNote) = repository.insertTrash(note)

    fun updateTrash(note: TrashNote) = repository.updateTrash(note)

    fun deleteTrash(note: TrashNote) = repository.deleteTrash(note)

}