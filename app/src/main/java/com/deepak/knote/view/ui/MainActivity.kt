package com.deepak.knote.view.ui

import android.app.Activity
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
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
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast

const val NOTE_ID = "NOTE_ID"
const val NOTE_TITLE = "NOTE_TITLE"
const val NOTE_CONTENT = "NOTE_CONTENT"
const val POSITION = "POSITION"

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: KNoteAdapter
    private lateinit var noteList: MutableList<Note>
    private lateinit var liveNoteList: LiveData<MutableList<Note>>
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = ViewModelProviders.of(this)[MainViewModel::class.java]
        mainViewModel.getAllNotes().observe(this, Observer {
            //            adapter.setNotes(noteList)
            Log.d("DEBUG", "note List changed")
        })

        liveNoteList = mainViewModel.getAllNotes()
        noteList = mainViewModel.getAllNotesList()

        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.hasFixedSize()
        adapter = KNoteAdapter(noteList) { note, position -> onItemClick(note, position) }
        recyclerView.adapter = adapter

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
                    toast("Note Deleted Successfully")
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        fab.onClick {
            startActivity(intentFor<NoteActivity>().clearTop())
//            val intent = Intent(this@MainActivity, NoteActivity::class.java)
//            startActivityForResult(intent, 101)
        }

    }

    private fun onItemClick(note: Note?, position: Int) {
        val id = note?.id
        val title = note?.noteTitle.toString()
        val content = note?.noteContent.toString()

//        TODO("pass the adapterPosition how??? for animation using notifyItemChanged()")

        startActivity(intentFor<UpdateNoteActivity>(
                NOTE_ID to id, NOTE_TITLE to title,
                NOTE_CONTENT to content, POSITION to position).clearTop())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK &&
                data != null && data.data != null) {
            if (requestCode == 101) {
                val title = intent?.getStringExtra(NOTE_TITLE).toString()
                val content = intent?.getStringExtra(NOTE_CONTENT).toString()
            } else if (requestCode == 102) {
                val id = intent.getIntExtra(NOTE_ID, 0)
                val title = intent?.getStringExtra(NOTE_TITLE).toString()
                val content = intent?.getStringExtra(NOTE_CONTENT).toString()
                val position = intent.getIntExtra(POSITION, 0)
                adapter.notifyItemInserted(position)
            }
        }
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