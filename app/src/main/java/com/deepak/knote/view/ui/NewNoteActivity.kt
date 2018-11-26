package com.deepak.knote.view.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.deepak.knote.R
import com.deepak.knote.service.db.model.Note
import com.deepak.knote.util.EXTRA_NOTE
import com.deepak.knote.util.hideSoftKeyboard
import com.deepak.knote.util.validateInput
import kotlinx.android.synthetic.main.activity_new_note.*
import org.jetbrains.anko.toast

/**
 * Activity to write a new note
 */
class NewNoteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_note)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.note_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_save_note -> {
                insertNote()
                true
            }
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun insertNote() {
        val title = note_title.text.toString()
        val content = note_content.text.toString()
        val note = Note(noteTitle = title, noteContent = content)
        if (validateInput(title, content)) {
            hideSoftKeyboard()
            val intent = Intent()
            intent.putExtra(EXTRA_NOTE, note)
            setResult(Activity.RESULT_OK, intent)
            supportFinishAfterTransition()
        } else {
            toast(R.string.empty_alert_message)
        }
    }

    override fun onBackPressed() {
        val title = note_title.text.toString()
        val content = note_content.text.toString()

        when {
            title.isNotBlank() || content.isNotBlank() -> insertNote()
            else -> supportFinishAfterTransition()
        }
    }
}