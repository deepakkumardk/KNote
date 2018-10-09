package com.deepak.knote.service.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = [Note::class], version = 1)
abstract class MyNoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao

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