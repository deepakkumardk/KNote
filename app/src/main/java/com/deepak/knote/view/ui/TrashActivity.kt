package com.deepak.knote.view.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.deepak.knote.R
import com.deepak.knote.service.db.model.Note
import com.deepak.knote.util.*
import com.deepak.knote.view.adapter.KNoteAdapter
import com.deepak.knote.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_trash.*
import kotlinx.android.synthetic.main.empty_view.*

class TrashActivity : AppCompatActivity() {
    private lateinit var trashAdapter: KNoteAdapter
    private lateinit var trashList: MutableList<Note>
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trash)

        mainViewModel = ViewModelProviders.of(this)[MainViewModel::class.java]
        mainViewModel.getAllTrashNotes().observe(this, Observer {
            trashAdapter.submitList(it as MutableList<Note>)
            trashList = it
            checkEmptyView()
        })

        trashList = mainViewModel.getAllTrashNotesList()
        trashAdapter = KNoteAdapter { note, position -> onItemClick(note, position) }
        recycler_view_trash.apply {
            hasFixedSize()
            adapter = trashAdapter
            layoutManager = LinearLayoutManager(applicationContext)
        }
    }


    /**
     * Check if the recycler view is empty or not
     */
    private fun checkEmptyView() {
        if (trashList.isEmpty()) {
            empty_view.show()
            empty_text_view.text = "Trash is Empty"
            recycler_view_trash.hide()
        } else {
            empty_view.hide()
            recycler_view_trash.show()
        }
    }

    /**
     * Open activity to edit note with transition
     * TODO("decide if it's needed of not")
     */
    private fun onItemClick(note: Note?, position: Int) {
        val id = note?.id
        val title = note?.noteTitle.toString()
        val content = note?.noteContent.toString()

        val intent = Intent(this@TrashActivity, UpdateNoteActivity::class.java)
        intent.putExtra(NOTE_ID, id)
        intent.putExtra(NOTE_TITLE, title)
        intent.putExtra(NOTE_CONTENT, content)
        intent.putExtra(POSITION, position)

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@TrashActivity)
            startActivityForResult(intent, RC_UPDATE_NOTE, options.toBundle())
        } else {
            startActivityForResult(intent, RC_UPDATE_NOTE)
        }
    }
}
