package com.deepak.knote.view.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.deepak.knote.R
import com.deepak.knote.util.*
import kotlinx.android.synthetic.main.activity_note.*
import org.jetbrains.anko.toast

/**
 * Activity to write a new note
 */
class NewNoteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
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
            R.id.home -> {
                supportFinishAfterTransition()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun insertNote() {
        val title = note_title.text.toString()
        val content = note_content.text.toString()
        if (validateInput(title, content)) {
            hideSoftKeyboard()
            val intent = Intent()
            intent.putExtra(NOTE_TITLE, title)
            intent.putExtra(NOTE_CONTENT, content)
            intent.putExtra(IS_TRASHED, false)
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