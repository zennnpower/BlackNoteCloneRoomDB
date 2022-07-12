package com.example.blacknoteroomdb.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Note::class], version = 1)
abstract class NoteRoomDatabase: RoomDatabase() {
    abstract fun noteDAO(): NoteDAO

    companion object {
        @Volatile
        private var INSTANCE: NoteRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): NoteRoomDatabase {
            return INSTANCE?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteRoomDatabase::class.java,
                    "note_database"
                ).fallbackToDestructiveMigration().addCallback(NoteDatabaseCallback(scope)).build()
                INSTANCE = instance
                instance
            }
        }

        private class NoteDatabaseCallback(
            private val scope: CoroutineScope,
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.noteDAO())
                    }
                }
            }
        }

        suspend fun populateDatabase(noteDAO: NoteDAO) {
            Log.i(null, "Creating dummy data")
            var note = Note("Title 1", "Content 1")
            noteDAO.addNote(note)
            note = Note("Title 2", "Content 2")
            noteDAO.addNote(note)
        }
    }
}