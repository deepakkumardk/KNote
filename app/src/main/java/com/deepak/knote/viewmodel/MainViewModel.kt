package com.deepak.knote.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.deepak.knote.service.db.Note
import com.deepak.knote.service.repository.NotesRepository

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private var repository: NotesRepository = NotesRepository(application)
    private var allNotes: LiveData<List<Note>> = repository.getAllNotes()
    private var allNotesList: List<Note> = repository.getAllNotesList()

    fun getAllNotes() : LiveData<List<Note>> = allNotes

    fun getAllNotesList() : List<Note> = allNotesList

    @Suppress("unused")
    fun getNoteById(id: Int) : LiveData<List<Note>> = repository.getNoteById(id)

    fun insertNote(note: Note) = repository.insertNote(note)

    fun updateNote(note: Note) = repository.updateNote(note)

    fun deleteNote(note: Note) = repository.deleteNote(note)

}