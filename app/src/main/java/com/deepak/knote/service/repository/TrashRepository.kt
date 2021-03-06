package com.deepak.knote.service.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.deepak.knote.service.db.MyNoteDatabase
import com.deepak.knote.service.db.model.TrashNote
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.jetbrains.anko.doAsync

class TrashRepository(context: Context) {
    private var database: MyNoteDatabase? = MyNoteDatabase.getInstance(context)
    private lateinit var liveTrashList: LiveData<MutableList<TrashNote>>
    private lateinit var trashList: MutableList<TrashNote>

    /**
     * Retrieve all the trashed notes from the database with live data
     */
    fun getLiveTrashNotes(): LiveData<MutableList<TrashNote>> {
        runBlocking {
            async {
                liveTrashList = database?.trashDao()?.getLiveTrashNotes()!!
                return@async liveTrashList
            }.await()
        }
        return liveTrashList
    }

    fun getTrashNotesList(): MutableList<TrashNote> {
        runBlocking {
            async {
                trashList = database?.trashDao()?.getTrashNotesList()!!
                return@async trashList
            }.await()
        }
        return trashList
    }

    fun insertTrash(note: TrashNote) = doAsync { database?.trashDao()?.insert(note) }

    fun updateTrash(note: TrashNote) = doAsync { database?.trashDao()?.update(note) }

    fun deleteTrash(note: TrashNote) = doAsync { database?.trashDao()?.delete(note) }
}