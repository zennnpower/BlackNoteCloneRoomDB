package com.example.blacknoteroomdb.data

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class NoteViewModel(private val repository: NoteRepository): ViewModel() {
    val allWords: LiveData<List<Note>> = repository.getNotes.asLiveData()

    fun addNote(note: Note) =
        viewModelScope.launch {
            repository.addNote(note)
        }

    fun update(note: Note) =
        viewModelScope.launch {
            repository.update(note)
        }

    fun delete(note: Note) =
        viewModelScope.launch {
            repository.delete(note)
        }
}

class NoteViewModelFactory(private val repository: NoteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NoteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}