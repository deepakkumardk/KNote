package com.deepak.knote.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.deepak.knote.service.db.model.ToDo

/**
 * DiffUtil class which takes care of all the data insertion and deletion
 * to provide smooth transition
 */
class DiffUtilTodo : DiffUtil.ItemCallback<ToDo>() {
    override fun areItemsTheSame(oldToDo: ToDo, newToDo: ToDo): Boolean {
        return oldToDo.id == newToDo.id
    }

    override fun areContentsTheSame(oldToDo: ToDo, newToDo: ToDo): Boolean {
        return (oldToDo.id == newToDo.id
                && oldToDo.todoTitle == newToDo.todoTitle
                && oldToDo.todoDescription == newToDo.todoDescription)
    }
}