package com.deepak.knote.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.deepak.knote.R
import com.deepak.knote.service.db.model.ToDo
import org.jetbrains.anko.find

/**
 * The Adapter for the RecyclerView to view all notes
 * This adapter class extending the ListAdapter
 * (not the RecyclerView.Adapter class because of the DiffUtil class)
 * ListAdapter is made on the top of DiffUtil class to provide the smooth animation
 */
class ToDoAdapter(private val listener: (ToDo, Int) -> Unit) :
        ListAdapter<ToDo, ToDoAdapter.ToDoViewHolder>(DiffUtilTodo()) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ToDoViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_todo, viewGroup, false)
        return ToDoViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ToDoViewHolder, position: Int) {
        val todo = getItem(position)
        viewHolder.todoTitle.text = todo.todoTitle
        viewHolder.todoDis.text = todo.todoDescription
        viewHolder.itemView.setOnClickListener { listener(todo, position) }
    }

    fun getTodoAt(position: Int): ToDo = getItem(position)

    class ToDoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var todoTitle = view.find<TextView>(R.id.item_todo_title)
        var todoDis = view.find<TextView>(R.id.item_todo_dis)
    }
}