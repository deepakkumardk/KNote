package com.deepak.knote.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.deepak.knote.service.db.model.Note
import com.deepak.knote.service.repository.NotesRepository

/**
 * The ViewModel class for the LiveData implementation that will
 * observe all the changes to the list of note and then update it.
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {
    private var repository: NotesRepository = NotesRepository(application)
    private var allNotes: LiveData<MutableList<Note>> = repository.getAllNotes()
    private var allNotesList: MutableList<Note> = repository.getAllNotesList()

    private var allTrashNotes: LiveData<MutableList<Note>> = repository.getAllTrashNotes()
    private var allTrashNotesList: MutableList<Note> = repository.getAllTrashNotesList()

    fun getAllNotes(): LiveData<MutableList<Note>> = allNotes

    fun getAllNotesList(): MutableList<Note> = allNotesList

    fun getAllTrashNotes(): LiveData<MutableList<Note>> = allTrashNotes

    fun getAllTrashNotesList(): MutableList<Note> = allTrashNotesList

    @Suppress("unused")
    fun getNoteById(id: Int): LiveData<MutableList<Note>> = repository.getNoteById(id)

    fun insertNote(note: Note) = repository.insertNote(note)

    fun updateNote(note: Note) = repository.updateNote(note)

    fun deleteNote(note: Note) = repository.deleteNote(note)

}