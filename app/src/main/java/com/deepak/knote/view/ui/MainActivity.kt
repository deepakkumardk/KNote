package com.deepak.knote.view.ui

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.MenuItem
import com.deepak.knote.R
import com.deepak.knote.service.db.MyNoteDatabase
import com.deepak.knote.service.db.Note
import com.deepak.knote.util.*
import com.deepak.knote.view.adapter.KNoteAdapter
import com.deepak.knote.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

/**
 * Main Launcher activity we can see all the  notes in a recycler view
 */
class MainActivity : AppCompatActivity() {
    private lateinit var noteAdapter: KNoteAdapter
    private lateinit var noteList: MutableList<Note>
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = ViewModelProviders.of(this)[MainViewModel::class.java]
        mainViewModel.getAllNotes().observe(this, Observer {
            noteAdapter.submitList(it as MutableList<Note>)
            noteList = it
            checkEmptyView()
        })

        noteList = mainViewModel.getAllNotesList()
        noteAdapter = KNoteAdapter { note, position -> onItemClick(note, position) }
        recycler_view.apply {
            recycler_view.hasFixedSize()
            recycler_view.adapter = noteAdapter
            layoutManager = LinearLayoutManager(applicationContext)
        }

        val simpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (direction == ItemTouchHelper.RIGHT) {
                    val position = viewHolder.adapterPosition
                    val swipedNote = noteAdapter.getNoteAt(position)
                    mainViewModel.deleteNote(swipedNote)
                    noteList.removeAt(position)
                    noteAdapter.notifyItemRemoved(position)
                    noteAdapter.submitList(noteList)
                    toast("Note Deleted Successfully")
                }
            }

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                val item = viewHolder.itemView
                if (dX > 0) {
                    //Draw the background of note item with material red color
                    //set the bounds for background of item
                    val background = ColorDrawable(Color.parseColor("#f44336"))
                    background.setBounds(item.left, item.top, (item.left + dX).toInt(), item.bottom)
                    background.draw(c)

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
                    icon.draw(c)

                    //set the alpha animation on swipe
                    val alphaAnimation = 1.0f - Math.abs(dX) / item.width.toFloat()
                    item.alpha = alphaAnimation
                    item.translationX = dX
                } else {
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                }
            }

            override fun onMoved(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, fromPos: Int, target: RecyclerView.ViewHolder, toPos: Int, x: Int, y: Int) {
                super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y)
                toast("on move")
            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(recycler_view)

        //Start the NewNoteActivity with smooth transition
        fab.onClick {
            val intent = Intent(this@MainActivity, NewNoteActivity::class.java)
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity)
                startActivityForResult(intent, RC_NEW_NOTE, options.toBundle())
            } else {
                startActivityForResult(intent, RC_NEW_NOTE)
            }
        }

    }

    /**
     * Check if the recycler view is empty or not
     */
    private fun checkEmptyView() {
        if (noteList.isEmpty()) {
            empty_view.show()
            recycler_view.hide()
        } else {
            empty_view.hide()
            recycler_view.show()
        }
    }

    /**
     * Open activity to edit note with transition
     */
    private fun onItemClick(note: Note?, position: Int) {
        val id = note?.id
        val title = note?.noteTitle.toString()
        val content = note?.noteContent.toString()

        val intent = Intent(this@MainActivity, UpdateNoteActivity::class.java)
        intent.putExtra(NOTE_ID, id)
        intent.putExtra(NOTE_TITLE, title)
        intent.putExtra(NOTE_CONTENT, content)
        intent.putExtra(POSITION, position)

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity)
            startActivityForResult(intent, RC_UPDATE_NOTE, options.toBundle())
        } else {
            startActivityForResult(intent, RC_UPDATE_NOTE)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_about_app -> {
                startActivity<AboutActivity>()
                true
            }
            R.id.home -> {
                supportFinishAfterTransition()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == RC_NEW_NOTE) {
                val title = data.getStringExtra(NOTE_TITLE).toString()
                val content = data.getStringExtra(NOTE_CONTENT).toString()
                val note = Note(noteTitle = title, noteContent = content)
                mainViewModel.insertNote(note)
                toast("Note Saved...")
            } else if (requestCode == RC_UPDATE_NOTE) {
                val id = data.getIntExtra(NOTE_ID, 0)
                val title = data.getStringExtra(NOTE_TITLE).toString()
                val content = data.getStringExtra(NOTE_CONTENT).toString()
                val position = data.getIntExtra(POSITION, 0)
                val note = Note(id, title, content)
                mainViewModel.updateNote(note)
                noteAdapter.notifyItemChanged(position)
                recycler_view.scrollToPosition(position)
                toast("Note Saved")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        MyNoteDatabase.destroyInstance()
    }

}