package com.deepak.knote.view.ui

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
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
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_to_do.*
import kotlinx.android.synthetic.main.empty_view.*
import org.jetbrains.anko.toast

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
                    val swipedNote = todoAdapter.getTodoAt(position)
                    removeTodo(swipedNote, position)
                }
            }

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                val item = viewHolder.itemView
                if (dX > 0) {
                    val background = setBackground(item, dX)
                    background?.draw(c)
                    val icon = getDeleteIcon(item)
                    icon?.draw(c)
                    item.setAlphaAnimation(dX)
                } else {
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(recycler_view)

        fab.setOnClickListener {
            val intent = Intent(this, NewNoteActivity::class.java)
            startActivityForResults(intent, RC_NEW_NOTE, this)
        }
    }


    /**
     * Check if the recycler view is empty or not
     */
    private fun checkEmptyView() {
        if (todoList.isEmpty()) {
            empty_view.show()
            empty_text_view.text = "Empty Todo"
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
        toast("Note moved to Trash")
    }

    private fun setBackground(item: View, dX: Float): ColorDrawable? {
        //Draw the background of note item with material red color
        //set the bounds for background of item
        val background = ColorDrawable(Color.parseColor("#f44336"))
        background.setBounds(item.left, item.top, (item.left + dX).toInt(), item.bottom)
        return background
    }

    private fun getDeleteIcon(item: View): Drawable? {
        //Draw the icon over the background
        val icon = ContextCompat.getDrawable(applicationContext, R.drawable.ic_delete)
        val iconWidth = icon?.intrinsicWidth as Int
        val iconHeight = icon.intrinsicHeight
        val cellHeight = item.bottom - item.top
        val margin = (cellHeight - iconHeight) / 2
        val iconTop = item.top + margin
        val iconBottom = iconTop + iconHeight
        val iconLeft = 48
        val iconRight = iconLeft + iconWidth

        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
        return icon
    }

    // Open activity to edit note with transition
    private fun onItemClick(todo: ToDo?, position: Int) {
        val id = todo?.id
        val title = todo?.todoTitle.toString()
        val content = todo?.todoDescription.toString()

    }
}
