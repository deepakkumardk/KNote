package com.deepak.knote.view.ui

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View.GONE
import android.view.View.VISIBLE
import com.deepak.knote.R
import com.deepak.knote.service.db.MyNoteDatabase
import com.deepak.knote.service.db.Note
import com.deepak.knote.view.adapter.KNoteAdapter
import com.deepak.knote.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast

const val NOTE_ID = "NOTE_ID"
const val NOTE_TITLE = "NOTE_TITLE"
const val NOTE_CONTENT = "NOTE_CONTENT"
const val POSITION = "POSITION"
const val RC_NEW_NOTE = 101
const val RC_UPDATE_NOTE = 102

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: KNoteAdapter
    private lateinit var noteList: MutableList<Note>
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = ViewModelProviders.of(this)[MainViewModel::class.java]
        mainViewModel.getAllNotes().observe(this, Observer {
            adapter.setNotes(it as MutableList<Note>)
            noteList = it
            checkEmptyView()
        })

        noteList = mainViewModel.getAllNotesList()
        checkEmptyView()

        recycler_view.layoutManager = LinearLayoutManager(applicationContext)
        recycler_view.hasFixedSize()
        adapter = KNoteAdapter(noteList) { note, position -> onItemClick(note, position) }
        recycler_view.adapter = adapter

        val simpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (direction == ItemTouchHelper.RIGHT) {
                    val position = viewHolder.adapterPosition
                    val swipedNote = adapter.getNoteAt(position)
                    mainViewModel.deleteNote(swipedNote)
                    noteList.removeAt(position)
                    adapter.notifyItemRemoved(position)
                    checkEmptyView()
                    toast("Note Deleted Successfully")
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(recycler_view)

        fab.onClick {
            val intent = Intent(this@MainActivity, NoteActivity::class.java)
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity)
                startActivityForResult(intent, RC_NEW_NOTE, options.toBundle())
            } else {
                startActivityForResult(intent, RC_NEW_NOTE)
            }
        }

    }

    private fun checkEmptyView() {
        if (noteList.isEmpty()) {
            recycler_view.visibility = GONE
            empty_view.visibility = VISIBLE
        } else {
            recycler_view.visibility = VISIBLE
            empty_view.visibility = GONE
        }
    }

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == RC_NEW_NOTE) {
                val title = data.getStringExtra(NOTE_TITLE).toString()
                val content = data.getStringExtra(NOTE_CONTENT).toString()
                val note = Note(noteTitle = title, noteContent = content)
                mainViewModel.insertNote(note)
                checkEmptyView()
                toast("Note Saved...")
            } else if (requestCode == RC_UPDATE_NOTE) {
                val id = data.getIntExtra(NOTE_ID, 0)
                val title = data.getStringExtra(NOTE_TITLE).toString()
                val content = data.getStringExtra(NOTE_CONTENT).toString()
                val position = data.getIntExtra(POSITION, 0)
                val note = Note(id, title, content)
                mainViewModel.updateNote(note)
                adapter.notifyItemChanged(position)
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