package com.deepak.knote.view.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import com.deepak.knote.viewmodel.MainViewModel
import com.deepak.knote.R
import com.deepak.knote.service.db.Note
import kotlinx.android.synthetic.main.activity_note.*
import org.jetbrains.anko.*

class NoteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.note_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.action_save_note -> insertNote()
            else -> return false
        }
        return  super.onOptionsItemSelected(item)
    }

    private fun insertNote() {
        val title = note_title.text.toString()
        val content = note_content.text.toString()
        val note = Note(noteTitle = title,noteContent = content)
        if (validateInput(title,content)) {
            MainViewModel(application).insertNote(note)
            toast("Notes inserted successfully...")
            finish()
            startActivity<MainActivity>()
        }else {
            toast("Field is Empty...")
        }
    }

    private fun validateInput(title: String, content: String): Boolean {
        return !(TextUtils.isEmpty(title) && TextUtils.isEmpty(content))
    }

    override fun onBackPressed() {
        val title = note_title.text.toString()
        val content = note_content.text.toString()

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
