package com.deepak.knote.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.deepak.knote.service.db.model.ToDo
import com.deepak.knote.service.repository.ToDoRepository

class ToDoViewModel(application: Application) : AndroidViewModel(application) {
    private var repository = ToDoRepository(application)
    private var liveTodos = repository.getLiveTodo()
    private var todoList = repository.getTodoList()

    fun getLiveTodos(): LiveData<MutableList<ToDo>> = liveTodos

    fun getTodosList(): MutableList<ToDo> = todoList

    fun insert(todo: ToDo) = repository.insert(todo)

    fun update(todo: ToDo) = repository.update(todo)

    fun delete(todo: ToDo) = repository.delete(todo)
}