package com.example.blacknoteroomdb

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.blacknoteroomdb.data.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val addRequestCode = 1
    private val editRequestCode = 2

    private val noteViewModel: NoteViewModel by viewModels {
        NoteViewModelFactory((application as NoteApplication).repository)
    }

    companion object {
        val EXTRA_ID = "EXTRA_ID"
        val EXTRA_TITLE = "EXTRA_TITLE"
        val EXTRA_CONTENT = "EXTRA_CONTENT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.notes_grid)

        val onItemClick: (Int, Note) -> Unit = {
            position, note ->
            val intent = Intent(this@MainActivity,AddEditNoteActivity::class.java)
            intent.putExtra(EXTRA_ID,note.id)
            intent.putExtra(EXTRA_TITLE, note.title)
            intent.putExtra(EXTRA_CONTENT, note.content)
            startActivityForResult(intent, editRequestCode)
        }

        val adapter = NoteListAdapter(itemClickListener = onItemClick)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        noteViewModel.allWords.observe(this) { notes ->
            // Update the cached copy of the words in the adapter.
            notes.let { adapter.submitList(it) }
        }

        val bottom_nav_view = findViewById<BottomNavigationView>(R.id.bottom_nav_view)

        // BottomNavigationView logic gate
        bottom_nav_view.setOnItemSelectedListener {
                item ->
            when (item.itemId) {
                R.id.nav_category -> {
                    val categoryIntent = Intent(this, CategoryActivity::class.java)
                    startActivity(categoryIntent)
                    true
                }
                R.id.nav_search -> {
                    val searchIntent = Intent(this, SearchActivity::class.java)
                    startActivity(searchIntent)
                    true
                }
                R.id.nav_add -> {
                    val addEditIntent = Intent(this, AddEditNoteActivity::class.java)
                    startActivityForResult(addEditIntent, addRequestCode)
                    true
                }
                R.id.nav_favourites -> {
                    val favouritesIntent = Intent(this, FavouritesActivity::class.java)
                    startActivity(favouritesIntent)
                    true
                }
                R.id.nav_check -> {
                    val checkIntent = Intent(this, DailyCheckActivity::class.java)
                    startActivity(checkIntent)
                    true
                }
                else -> false
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        if (requestCode == addRequestCode && resultCode == Activity.RESULT_OK) {
            var note_title = ""
            var note_content = ""

            intentData?.getStringExtra(AddEditNoteActivity.EXTRA_REPLY_TITLE)?.let { title ->
                note_title = title
            }

            intentData?.getStringExtra(AddEditNoteActivity.EXTRA_REPLY_CONTENT)?.let { content ->
                note_content = content
            }

            val note = Note(note_title, note_content)

            lifecycleScope.launch {
                noteViewModel.addNote(note)
            }
        } else if (requestCode == editRequestCode && resultCode == Activity.RESULT_OK) {
            if (intentData?.getStringExtra(AddEditNoteActivity.EXTRA_REPLY_DELETE) == "Delete") {
                Log.i(null, "Deleting...")
                val reply_title: String? = intentData?.getStringExtra(AddEditNoteActivity.EXTRA_REPLY_TITLE)
                val reply_content: String? = intentData?.getStringExtra(AddEditNoteActivity.EXTRA_REPLY_CONTENT)
                val id: Int? = intentData?.getIntExtra(AddEditNoteActivity.EXTRA_REPLY_ID,-1)
                val note = Note(reply_title!!, reply_content!!)
                note.id = id!!

                lifecycleScope.launch {
                    noteViewModel.delete(note)
                }
            } else {
                Log.i(null, "Editing...")
                val reply_title: String? = intentData?.getStringExtra(AddEditNoteActivity.EXTRA_REPLY_TITLE)
                val reply_content: String? = intentData?.getStringExtra(AddEditNoteActivity.EXTRA_REPLY_CONTENT)
                val id: Int? = intentData?.getIntExtra(AddEditNoteActivity.EXTRA_REPLY_ID,-1)
                val note = Note(reply_title!!, reply_content!!)
                note.id = id!!

                lifecycleScope.launch {
                    noteViewModel.update(note)
                }
            }
        }
    }
}