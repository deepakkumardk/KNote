package com.deepak.knote.view.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.deepak.knote.R
import com.deepak.knote.util.*
import kotlinx.android.synthetic.main.activity_update_note.*
import org.jetbrains.anko.toast

/**
 * Activity to update note
 */
class UpdateNoteActivity : AppCompatActivity() {
    private var id: Int = 0
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
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_save_note -> {
                updateNote()
                true
            }
            R.id.home -> {
                supportFinishAfterTransition()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun loadNoteInfo() {
        id = intent.getIntExtra(NOTE_ID, 0)
        title = intent?.getStringExtra(NOTE_TITLE).toString()
        content = intent?.getStringExtra(NOTE_CONTENT).toString()
        position = intent.getIntExtra(POSITION, 0)
        requestCode = intent.getIntExtra(RC_ACTIVITY, RC_UPDATE_NOTE)

        if (requestCode == RC_TRASH_NOTE) {
            update_note_title.isEnabled = false
            update_note_content.isEnabled = false
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
            val intent = Intent()
            intent.putExtra(NOTE_ID, id)
            intent.putExtra(NOTE_TITLE, title)
            intent.putExtra(NOTE_CONTENT, content)
            intent.putExtra(POSITION, position)
            setResult(Activity.RESULT_OK, intent)
            supportFinishAfterTransition()
        } else {
            toast(R.string.empty_alert_message)
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
}