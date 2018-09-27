package com.deepak.knote.view.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import com.deepak.knote.viewmodel.MainViewModel
import com.deepak.knote.R
import com.deepak.knote.service.db.Note
import kotlinx.android.synthetic.main.activity_update_note.*
import org.jetbrains.anko.*

class UpdateNoteActivity : AppCompatActivity() {
    private var id: Int = 0
    private var title: String? = null
    private var content: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_note)

        id = intent.getIntExtra(NOTE_ID,0)
        title = intent?.getStringExtra(NOTE_TITLE).toString()
        content = intent?.getStringExtra(NOTE_CONTENT).toString()

        loadNoteInfo()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.note_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.action_save_note -> updateNote()
            else -> return false
        }
        return  super.onOptionsItemSelected(item)
    }

    private fun loadNoteInfo() {
        update_note_title.setText(title)
        update_note_content.setText(content)
        update_note_content.selectionEnd
        update_note_content.requestFocus()
    }

    private fun updateNote() {
        val title = update_note_title.text.toString()
        val content = update_note_content.text.toString()
        val note = Note(id,title,content)
        if (validateInput(title,content)) {
            MainViewModel(application).updateNote(note)
            toast("Notes updated successfully...")
            finish()
            startActivity<MainActivity>()
        }else {
            toast("Field is Empty...")
        }
    }

    private fun validateInput(title: String,content: String): Boolean {
        return !(TextUtils.isEmpty(title) && TextUtils.isEmpty(content))
    }

    override fun onBackPressed() {
        val title = update_note_title.text.toString()
        val content = update_note_content.text.toString()

        if (title.isNotEmpty() || content.isNotEmpty()) {
            alert(getString(R.string.alert_message)) {
                yesButton {
                    finish()
                    super.onBackPressed()
                }
                noButton { it.dismiss() }
            }.show()
        }else {
            finish()
            super.onBackPressed()
        }
    }
}
