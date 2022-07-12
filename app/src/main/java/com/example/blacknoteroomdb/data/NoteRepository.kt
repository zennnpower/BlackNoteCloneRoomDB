package com.example.blacknoteroomdb.data

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class NoteRepository(private val noteDAO: NoteDAO) {
    val getNotes: Flow<List<Note>> = noteDAO.getNotes()

    @WorkerThread
    suspend fun addNote(note: Note) {
        noteDAO.addNote(note)
    }

    @WorkerThread
    suspend fun update(note: Note) {
        noteDAO.update(note)
    }

    @WorkerThread
    suspend fun delete(note: Note) {
        noteDAO.delete(note)
    }
}