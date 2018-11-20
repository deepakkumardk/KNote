package com.deepak.knote.view.ui

import android.graphics.Canvas
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.deepak.knote.R
import com.deepak.knote.service.db.model.ToDo
import com.deepak.knote.util.*
import com.deepak.knote.view.adapter.ToDoAdapter
import com.deepak.knote.viewmodel.ToDoViewModel
import kotlinx.android.synthetic.main.activity_to_do.*
import kotlinx.android.synthetic.main.empty_view.*
import org.jetbrains.anko.*

class ToDoActivity : AppCompatActivity() {
    private lateinit var todoAdapter: ToDoAdapter
    private lateinit var todoList: MutableList<ToDo>
    private lateinit var todoViewModel: ToDoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_do)

        todoViewModel = ViewModelProviders.of(this)[ToDoViewModel::class.java]
        todoViewModel.getLiveTodos().observe(this, Observer {
            todoAdapter.submitList(it as MutableList<ToDo>)
            todoList = it
            checkEmptyView()
        })

        todoList = todoViewModel.getTodosList()
        todoAdapter = ToDoAdapter { note, position -> onItemClick(note, position) }
        recycler_view_todo.init(applicationContext)
        recycler_view_todo.adapter = todoAdapter

        val simpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (direction == ItemTouchHelper.RIGHT) {
                    val position = viewHolder.adapterPosition
                    val swipedTodo = todoAdapter.getTodoAt(position)
                    removeTodo(swipedTodo, position)
                }
            }

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                val item = viewHolder.itemView
                if (dX > 0) {
                    val background = setBackground(item, dX)
                    background?.draw(c)
                    val iconDelete = ContextCompat.getDrawable(applicationContext, R.drawable.ic_delete)
                    val icon = getDeleteIcon(item, iconDelete)
                    icon?.draw(c)
                    item.setAlphaAnimation(dX)
                } else {
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(recycler_view_todo)

        fab_todo.setOnClickListener { view ->
            alert("Add new Todo") {
                lateinit var title: EditText
                lateinit var description: EditText
                customView {
                    verticalLayout {
                        padding = dip(8)
                        title = editText { hint = "Title";singleLine = true }
                        description = editText { hint = "Description" }
                    }
                }
                okButton { saveTodo(title.text.toString(), description.text.toString()) }
                cancelButton { it.dismiss() }
            }.show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                supportFinishAfterTransition()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Check if the recycler view is empty or not
     */
    private fun checkEmptyView() {
        if (todoList.isEmpty()) {
            empty_view.show()
            empty_text_view.text = getString(R.string.empty_todo_message)
            recycler_view_todo.hide()
        } else {
            empty_view.hide()
            recycler_view_todo.show()
        }
    }

    private fun removeTodo(todo: ToDo, position: Int) {
        todoViewModel.delete(todo)
        todoList.removeAt(position)
        todoAdapter.notifyItemRemoved(position)
        todoAdapter.submitList(todoList)
        toast("Todo removed")
    }

    // Open activity to edit note with transition
    private fun onItemClick(todo: ToDo?, position: Int) {
        val id = todo?.id
        val todoTitle = todo?.todoTitle.toString()
        val todoDes = todo?.todoDescription.toString()

        alert("", "TODO") {
            lateinit var title: EditText
            lateinit var description: EditText
            customView {
                verticalLayout {
                    padding = dip(8)
                    title = editText(text = todoTitle) { hint = "Title"; singleLine = true }
                    description = editText(text = todoDes) { hint = "Description" }
                }
            }
            okButton { updateTodo(id!!, title.text.toString(), description.text.toString()) }
            cancelButton { it.dismiss() }
        }.show()
    }

    private fun saveTodo(title: String, description: String) {
        if (validateInput(title, description)) {
            val todo = ToDo(todoTitle = title, todoDescription = description)
            todoViewModel.insert(todo)
            toast("Todo Saved")
        }
    }

    private fun updateTodo(id: Int, title: String, description: String) {
        if (validateInput(title, description)) {
            val todo = ToDo(id, title, description)
            todoViewModel.update(todo)
            toast("Todo Updated")
        }
    }
}
