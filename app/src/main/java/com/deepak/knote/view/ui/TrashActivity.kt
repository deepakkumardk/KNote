package com.deepak.knote.view.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.deepak.knote.R
import com.deepak.knote.service.db.model.Note
import com.deepak.knote.service.db.model.TrashNote
import com.deepak.knote.util.*
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
        val noteToDelete = TrashNote(id!!, title, content)

        alert("Do you want to restore the note?") {
            yesButton { restoreNote(noteToRestore, noteToDelete, position) }
            noButton { it.dismiss() }
        }.show()

    }

    private fun viewNote(id: Int?, title: String, content: String) {
        val intent = Intent(this@TrashActivity, UpdateNoteActivity::class.java)
        intent.putExtra(EXTRA_NOTE_ID, id)
        intent.putExtra(EXTRA_NOTE_TITLE, title)
        intent.putExtra(EXTRA_NOTE_CONTENT, content)

        startActivityForResults(intent, RC_TRASH_NOTE, this)
    }

    private fun restoreNote(note: Note, trashNote: TrashNote, position: Int) {
        MainViewModel(application).insertNote(note)

        trashViewModel.deleteTrash(trashNote)
        trashList.removeAt(position)
        trashAdapter.notifyItemRemoved(position)
        trashAdapter.submitList(trashList)
        toast("Note Restored Successfully")
    }
}