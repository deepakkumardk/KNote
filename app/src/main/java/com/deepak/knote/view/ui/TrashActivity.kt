package com.deepak.knote.view.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.deepak.knote.R
import com.deepak.knote.service.db.model.Note
import com.deepak.knote.service.db.model.TrashNote
import com.deepak.knote.util.hide
import com.deepak.knote.util.init
import com.deepak.knote.util.show
import com.deepak.knote.view.adapter.TrashAdapter
import com.deepak.knote.viewmodel.MainViewModel
import com.deepak.knote.viewmodel.TrashViewModel
import kotlinx.android.synthetic.main.activity_trash.*
import kotlinx.android.synthetic.main.empty_view.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton

/**
 * Trash activity to view the deleted notes
 */
class TrashActivity : AppCompatActivity() {
    private lateinit var trashAdapter: TrashAdapter
    private lateinit var trashList: MutableList<TrashNote>
    private lateinit var trashViewModel: TrashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trash)

        trashViewModel = ViewModelProviders.of(this)[TrashViewModel::class.java]
        trashViewModel.getLiveTrashNotes().observe(this, Observer {
            trashAdapter.submitList(it as MutableList<TrashNote>)
            trashList = it
            checkEmptyView()
        })

        trashList = trashViewModel.getTrashNotesList()
        trashAdapter = TrashAdapter { note, position -> onItemClick(note, position) }
        recycler_view_trash.init(applicationContext)
        recycler_view_trash.adapter = trashAdapter
    }

    /**
     * Check if the recycler view is empty or not
     */
    private fun checkEmptyView() {
        if (trashList.isEmpty()) {
            empty_view.show()
            empty_text_view.text = getString(R.string.empty_trash_message)
            recycler_view_trash.hide()
        } else {
            empty_view.hide()
            recycler_view_trash.show()
        }
    }

    /**
     * Open activity to view with transition
     */
    private fun onItemClick(note: TrashNote?, position: Int) {
        val id = note?.id
        val title = note?.noteTitle.toString()
        val content = note?.noteContent.toString()
        val noteToRestore = Note(noteTitle = title, noteContent = content)

        alert("Do you want to restore the note?") {
            yesButton { restoreNote(noteToRestore) }
            noButton { it.dismiss() }
        }.show()

    }

    private fun restoreNote(note: Note) {
        MainViewModel(application).insertNote(note)
        toast("Note Restored Successfully")
    }
}