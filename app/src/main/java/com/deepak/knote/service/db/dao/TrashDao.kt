package com.deepak.knote.service.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.deepak.knote.service.db.model.TrashNote

/**
 * The Data Access Object(Dao) implementation for the Room
 */
@Dao
interface TrashDao {
    @Query("SELECT * FROM trash_notes")
    fun getLiveTrashNotes(): LiveData<MutableList<TrashNote>>

    @Query("SELECT * FROM trash_notes")
    fun getTrashNotesList(): MutableList<TrashNote>

    @Insert
    fun insert(note: TrashNote)

    @Update
    fun update(note: TrashNote)

    @Delete
    fun delete(note: TrashNote)
}