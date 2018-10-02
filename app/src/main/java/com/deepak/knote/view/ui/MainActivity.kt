package com.deepak.knote.view.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import com.deepak.knote.R
import com.deepak.knote.service.db.MyNoteDatabase
import com.deepak.knote.service.db.Note
import com.deepak.knote.view.adapter.KNoteAdapter
import com.deepak.knote.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

const val NOTE_ID = "NOTE_ID"
const val NOTE_TITLE = "NOTE_TITLE"
const val NOTE_CONTENT = "NOTE_CONTENT"

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: KNoteAdapter
    private lateinit var noteList: MutableList<Note>
    private lateinit var liveNoteList : LiveData<List<Note>>
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = ViewModelProviders.of(this)[MainViewModel::class.java]
        mainViewModel.getAllNotes().observe(this, Observer {
            Log.d("DEBUG","note List changed")
        })

        liveNoteList = mainViewModel.getAllNotes()
        noteList = mainViewModel.getAllNotesList()

        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.hasFixedSize()
        adapter = KNoteAdapter(noteList) { note -> onItemClick(note) }
        recyclerView.adapter = adapter

        val simpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.layoutPosition
                if (direction == ItemTouchHelper.RIGHT) {
                    val note = noteList[position]
                    val sNote = Note(note.id, note.noteTitle, note.noteContent)
                    adapter.removeNote(position)
                    mainViewModel.deleteNote(sNote)
                    toast("Note deleted successfully...")
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        fab.onClick {
            //            TODO("It's not the best practice (It's brothers too) also it causing the transition b/w activities grumpy")
            finish()
            startActivity<NoteActivity>()
        }

    }

    private fun onItemClick(note: Note?) {
        val id = note?.id
        val title = note?.noteTitle.toString()
        val content = note?.noteContent.toString()
        finish()
        startActivity<UpdateNoteActivity>(NOTE_ID to id, NOTE_TITLE to title, NOTE_CONTENT to content)
    }

    override fun onResume() {
        super.onResume()
        //it's not helping
        mainViewModel = ViewModelProviders.of(this)[MainViewModel::class.java]
        mainViewModel.getAllNotes().observe(this, Observer {
            Log.d("DEBUG", "note List changed")
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        MyNoteDatabase.destroyInstance()
    }

}