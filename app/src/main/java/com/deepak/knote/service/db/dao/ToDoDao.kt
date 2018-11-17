package com.deepak.knote.service.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.deepak.knote.service.db.model.ToDo
import java.util.concurrent.Future

/**
 * The Data Access Object(Dao) implementation for the Room
 */
@Dao
interface ToDoDao {
    @Query("SELECT * FROM todo")
    fun getLiveTodo(): LiveData<MutableList<ToDo>>

    @Query("SELECT * FROM todo")
    fun getTodoList(): MutableList<ToDo>

    @Insert
    fun insert(note: ToDo): Future<Unit>

    @Update
    fun update(note: ToDo): Future<Unit>

    @Delete
    fun delete(note: ToDo): Future<Unit>
}