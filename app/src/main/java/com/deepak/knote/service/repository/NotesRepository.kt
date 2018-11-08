package com.deepak.knote.service.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.deepak.knote.service.db.MyNoteDatabase
import com.deepak.knote.service.db.model.Note
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.jetbrains.anko.doAsync

/**
 * Repository class for the implementation of the Dao functions
 */
class NotesRepository(context: Context) {
    private var database: MyNoteDatabase? = MyNoteDatabase.getInstance(context)
    private lateinit var liveNoteList: LiveData<MutableList<Note>>
    private lateinit var noteList: MutableList<Note>

    /**
     * Retrieve all the notes from the database with live data
     */
    fun getAllNotes(): LiveData<MutableList<Note>> {
        runBlocking {
            async {
                liveNoteList = database?.noteDao()?.getAllNotes()!!
                return@async liveNoteList
            }.await()
        }
        return liveNoteList
    }

    fun getAllNotesList(): MutableList<Note> {
        runBlocking {
            async {
                noteList = database?.noteDao()?.getAllNotesList()!!
                return@async noteList
            }.await()
        }
        return noteList
    }

    fun getNoteById(id: Int): LiveData<MutableList<Note>> {
        runBlocking {
            async {
                liveNoteList = database?.noteDao()?.getNoteById(id)!!
                return@async liveNoteList
            }.await()
        }
        return liveNoteList
    }

    fun insertNote(note: Note) = doAsync { database?.noteDao()?.insertNote(note) }

    fun updateNote(note: Note) = doAsync { database?.noteDao()?.updateNote(note) }

    fun deleteNote(note: Note) = doAsync { database?.noteDao()?.deleteNote(note) }
}