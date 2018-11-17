package com.deepak.knote.service.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.deepak.knote.service.db.MyNoteDatabase
import com.deepak.knote.service.db.dao.ToDoDao
import com.deepak.knote.service.db.model.ToDo
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.jetbrains.anko.doAsync

open class ToDoRepository(context: Context) : ToDoDao {
    private var database: MyNoteDatabase? = MyNoteDatabase.getInstance(context)
    private lateinit var liveTodoList: LiveData<MutableList<ToDo>>
    private lateinit var todoList: MutableList<ToDo>

    /**
     * Retrieve all the todos from the database with live data
     */
    override fun getLiveTodo(): LiveData<MutableList<ToDo>> {
        runBlocking {
            async {
                liveTodoList = database?.todoDao()?.getLiveTodo()!!
                return@async liveTodoList
            }.await()
        }
        return liveTodoList
    }

    override fun getTodoList(): MutableList<ToDo> {
        runBlocking {
            async {
                todoList = database?.todoDao()?.getTodoList()!!
                return@async todoList
            }.await()
        }
        return todoList
    }

    override fun insert(todo: ToDo) = doAsync { database?.todoDao()?.insert(todo) }

    override fun update(todo: ToDo) = doAsync { database?.todoDao()?.update(todo) }

    override fun delete(todo: ToDo) = doAsync { database?.todoDao()?.delete(todo) }
}