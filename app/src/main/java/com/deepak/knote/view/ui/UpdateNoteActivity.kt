package com.deepak.knote.view.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.deepak.knote.R
import com.deepak.knote.service.db.model.Note
import com.deepak.knote.util.*
import kotlinx.android.synthetic.main.activity_update_note.*
import org.jetbrains.anko.toast

/**
 * Activity to update note
 */
class UpdateNoteActivity : AppCompatActivity() {
    private var id: Int? = 0
    private var title: String? = null
    private var content: String? = null
    private var position: Int = 0
    private var requestCode: Int = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_note)

        loadNoteInfo()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.note_menu, menu)
        if (requestCode == RC_TRASH_NOTE) {
            menu?.findItem(R.id.action_save_note)?.isVisible = false
            setTitle("Note")
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_save_note -> {
                updateNote()
                true
            }
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        val title = update_note_title.text.toString()
        val content = update_note_content.text.toString()
        when {
            this.title == title && this.content == content -> supportFinishAfterTransition()
            title.isNotEmpty() || content.isNotEmpty() -> updateNote()
            else -> supportFinishAfterTransition()
        }
    }

    private fun loadNoteInfo() {
        val note = intent.getParcelableExtra<Note>(EXTRA_NOTE)
        id = note?.id
        title = note?.noteTitle.toString()
        content = note?.noteContent.toString()
        position = intent.getIntExtra(EXTRA_POSITION, 0)
        requestCode = intent.getIntExtra(EXTRA_RC, RC_UPDATE_NOTE)

        if (requestCode == RC_TRASH_NOTE) {
            update_note_title.isClickable = false
            update_note_content.isClickable = false
            invalidateOptionsMenu()
        }
        update_note_title.setText(title)
        update_note_title.selectionEnd
        update_note_content.setText(content)
    }

    private fun updateNote() {
        val title = update_note_title.text.toString()
        val content = update_note_content.text.toString()
        if (validateInput(title, content)) {
            hideSoftKeyboard()
            val note = Note(id!!, title, content)
            val intent = Intent()
            intent.putExtra(EXTRA_NOTE, note)
            intent.putExtra(EXTRA_POSITION, position)
            setResult(Activity.RESULT_OK, intent)
            supportFinishAfterTransition()
        } else {
            toast(R.string.empty_alert_message)
        }
    }
}