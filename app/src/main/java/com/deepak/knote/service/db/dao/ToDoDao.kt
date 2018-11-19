package com.deepak.knote.service.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.deepak.knote.service.db.model.ToDo

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
    fun insert(todo: ToDo)

    @Update
    fun update(todo: ToDo)

    @Delete
    fun delete(todo: ToDo)
}