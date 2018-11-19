package com.deepak.knote.service.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.deepak.knote.service.db.dao.NoteDao
import com.deepak.knote.service.db.dao.ToDoDao
import com.deepak.knote.service.db.dao.TrashDao
import com.deepak.knote.service.db.model.Note
import com.deepak.knote.service.db.model.ToDo
import com.deepak.knote.service.db.model.TrashNote

/**
 * Database class to instantiate the RoomDatabase object
 */
@Database(entities = [Note::class, TrashNote::class, ToDo::class], version = 3, exportSchema = false)
abstract class MyNoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun trashDao(): TrashDao
    abstract fun todoDao(): ToDoDao

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