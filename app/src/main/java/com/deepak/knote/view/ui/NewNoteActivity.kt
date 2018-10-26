package com.deepak.knote.view.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import com.deepak.knote.R
import com.deepak.knote.util.hideSoftKeyboard
import kotlinx.android.synthetic.main.activity_note.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton

/**
 * Activity to write new note
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
            setResult(Activity.RESULT_OK, intent)
            supportFinishAfterTransition()
        } else {
            toast("Field(s) is Empty...")
        }
    }

    private fun validateInput(title: String, content: String): Boolean {
        return !(TextUtils.isEmpty(title.trim()) && TextUtils.isEmpty(content.trim()))
    }

    override fun onBackPressed() {
        val title = note_title.text.toString()
        val content = note_content.text.toString()

        if (title.isNotBlank() || content.isNotBlank()) {
            alert(getString(R.string.alert_message)) {
                yesButton { supportFinishAfterTransition() }
                noButton { it.dismiss() }
            }.show()
        } else {
            supportFinishAfterTransition()
        }
    }
}
