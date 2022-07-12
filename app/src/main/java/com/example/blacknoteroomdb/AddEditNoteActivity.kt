package com.example.blacknoteroomdb

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class AddEditNoteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_note)
        val editNoteTitle = findViewById<EditText>(R.id.title_input)
        val editNoteContent = findViewById<EditText>(R.id.note_input)
        val editingToolbar = findViewById<Toolbar>(R.id.editing_toolbar)
        val editingCategory = findViewById<TextView>(R.id.editing_category)
        val editingTitle = findViewById<TextView>(R.id.editing_title)
        val backButton = findViewById<ImageView>(R.id.back_button)
        val deleteButton = findViewById<ImageView>(R.id.delete_button)

        var id: Int = -1

        // When editing:
        val intent: Intent = intent
        if(intent.hasExtra(MainActivity.EXTRA_ID)) {
            id = intent.getIntExtra(MainActivity.EXTRA_ID,-1)
            editNoteTitle.setText(intent.getStringExtra(MainActivity.EXTRA_TITLE))
            editNoteContent.setText(intent.getStringExtra(MainActivity.EXTRA_CONTENT))
            editingToolbar.setVisibility(View.VISIBLE)
            editingCategory.setVisibility(View.VISIBLE)
            editingTitle.setText(intent.getStringExtra(MainActivity.EXTRA_TITLE))
            editingTitle.setVisibility(View.VISIBLE)
        }

        // Saving:
        val button = findViewById<Button>(R.id.note_btn)
        button.setOnClickListener {
            val replyIntent = Intent()
            // Checking if fields are empty
            if (TextUtils.isEmpty(editNoteTitle.text) || TextUtils.isEmpty(editNoteContent.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            }
            // If fields are all filled, send the data back
            else {
                val note_title = editNoteTitle.text.toString()
                val note_content = editNoteContent.text.toString()

                replyIntent.putExtra(EXTRA_REPLY_TITLE, note_title)
                replyIntent.putExtra(EXTRA_REPLY_CONTENT, note_content)
                replyIntent.putExtra(EXTRA_REPLY_ID, id)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }

        // Cancelling changes using back arrow button
        backButton.setOnClickListener {
            val replyIntent = Intent()

            setResult(Activity.RESULT_CANCELED, replyIntent)

            finish()
        }

        // Deleting note using trashcan icon
        deleteButton.setOnClickListener {
            val replyIntent = Intent()
            val note_title = editNoteTitle.text.toString()
            val note_content = editNoteContent.text.toString()

            replyIntent.putExtra(EXTRA_REPLY_DELETE, "Delete")
            replyIntent.putExtra(EXTRA_REPLY_TITLE, note_title)
            replyIntent.putExtra(EXTRA_REPLY_CONTENT, note_content)
            replyIntent.putExtra(EXTRA_REPLY_ID, id)
            setResult(Activity.RESULT_OK, replyIntent)

            finish()
        }
    }

    companion object {
        const val EXTRA_REPLY_DELETE = "EXTRA_REPLY_DELETE"
        const val EXTRA_REPLY_TITLE = "EXTRA_REPLY_TITLE"
        const val EXTRA_REPLY_CONTENT = "EXTRA_REPLY_CONTENT"
        const val EXTRA_REPLY_ID = "EXTRA_REPLY_ID"
    }
}