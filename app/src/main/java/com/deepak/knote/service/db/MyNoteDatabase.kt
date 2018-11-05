package com.deepak.knote.service.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.deepak.knote.service.db.model.Note
import com.deepak.knote.service.db.model.TrashNote

/**
 * Database class to instantiate the RoomDatabase object
 */
@Database(entities = [Note::class, TrashNote::class], version = 2, exportSchema = false)
abstract class MyNoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun trashDao(): TrashDao

    companion object {
        private var INSTANCE: MyNoteDatabase? = null
        fun getInstance(context: Context): MyNoteDatabase? {
            if (INSTANCE == null) {
                synchronized(MyNoteDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context, MyNoteDatabase::class.java, "notes_db").allowMainThreadQueries().fallbackToDestructiveMigration().build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}