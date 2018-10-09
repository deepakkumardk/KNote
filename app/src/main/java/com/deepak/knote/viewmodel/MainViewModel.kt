package com.deepak.knote.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.deepak.knote.service.db.Note
import com.deepak.knote.service.repository.NotesRepository

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private var repository: NotesRepository = NotesRepository(application)
    private var allNotes: LiveData<MutableList<Note>> = repository.getAllNotes()
    private var allNotesList: MutableList<Note> = repository.getAllNotesList()

    fun getAllNotes(): LiveData<MutableList<Note>> = allNotes

    fun getAllNotesList(): MutableList<Note> = allNotesList

    @Suppress("unused")
    fun getNoteById(id: Int): LiveData<MutableList<Note>> = repository.getNoteById(id)

    fun insertNote(note: Note) = repository.insertNote(note)

    fun updateNote(note: Note) = repository.updateNote(note)

    fun deleteNote(note: Note) = repository.deleteNote(note)

}