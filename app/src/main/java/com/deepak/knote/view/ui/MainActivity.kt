package com.deepak.knote.view.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.deepak.knote.R
import com.deepak.knote.service.db.MyNoteDatabase
import com.deepak.knote.service.db.model.Note
import com.deepak.knote.service.db.model.TrashNote
import com.deepak.knote.util.*
import com.deepak.knote.view.adapter.KNoteAdapter
import com.deepak.knote.viewmodel.MainViewModel
import com.deepak.knote.viewmodel.TrashViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

/**
 * Main Launcher activity where we can see all the saved notes in a recycler view
 */
class MainActivity : AppCompatActivity() {
    private lateinit var noteAdapter: KNoteAdapter
    private lateinit var noteList: MutableList<Note>
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initToolbar()
        mainViewModel = ViewModelProviders.of(this)[MainViewModel::class.java]
        mainViewModel.getAllNotes().observe(this, Observer {
            noteAdapter.submitList(it as MutableList<Note>)
            noteList = it
            checkEmptyView()
        })

        noteList = mainViewModel.getAllNotesList()
        noteAdapter = KNoteAdapter { note, position -> onItemClick(note, position) }
        recycler_view.init(applicationContext)
        recycler_view.adapter = noteAdapter

        val simpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (direction == ItemTouchHelper.RIGHT) {
                    val position = viewHolder.adapterPosition
                    val swipedNote = noteAdapter.getNoteAt(position)
                    removeNote(swipedNote, position)
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
        itemTouchHelper.attachToRecyclerView(recycler_view)

        fab.setOnClickListener {
            val intent = Intent(this, NewNoteActivity::class.java)
            startActivityForResults(intent, RC_NEW_NOTE, this)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_trash -> {
                startActivity<TrashActivity>()
                true
            }
            R.id.action_todo -> {
                startActivity<ToDoActivity>()
                true
            }
            R.id.action_about_app -> {
                startActivity<AboutActivity>()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            val note = data.getParcelableExtra<Note>(EXTRA_NOTE)
            if (requestCode == RC_NEW_NOTE) {
                mainViewModel.insertNote(note)
                recycler_view.smoothScrollToPosition(noteList.size)
                toast("Note Saved...")
            } else if (requestCode == RC_UPDATE_NOTE) {
                val position = data.getIntExtra(EXTRA_POSITION, 0)
                mainViewModel.updateNote(note)
                noteAdapter.notifyItemChanged(position)
                recycler_view.smoothScrollToPosition(position)
                toast("Note Saved")
            }
        }
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        title = getString(R.string.app_name)
    }

    // Check if the recycler view is empty or not
    private fun checkEmptyView() {
        if (noteList.isEmpty()) {
            empty_view.show()
            recycler_view.hide()
        } else {
            empty_view.hide()
            recycler_view.show()
        }
    }

    private fun removeNote(swipedNote: Note, position: Int) {
        //Move the note to the trash and delete it from the main notes
        val trashNote = TrashNote(swipedNote.id, swipedNote.noteTitle, swipedNote.noteContent)
        TrashViewModel(application).insertTrash(trashNote)

        mainViewModel.deleteNote(swipedNote)
        noteList.removeAt(position)
        noteAdapter.notifyItemRemoved(position)
        noteAdapter.submitList(noteList)
        toast("Note moved to Trash")
    }

    // Open activity to edit note with transition
    private fun onItemClick(note: Note?, position: Int) {
        val intent = Intent(this, UpdateNoteActivity::class.java)
        intent.putExtra(EXTRA_POSITION, position)
        intent.putExtra(EXTRA_NOTE, note)
        startActivityForResults(intent, RC_UPDATE_NOTE, this)
    }

    override fun onDestroy() {
        MyNoteDatabase.destroyInstance()
        super.onDestroy()
    }

}