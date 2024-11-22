package com.example.notetest.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notetest.data.model.Note
import com.example.notetest.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    init {
        fetchNotes()
    }

    private fun fetchNotes() {
        viewModelScope.launch {
            repository.getAllNotes().collect { noteList ->
                _notes.value = noteList
            }
        }
    }

    fun saveOrUpdateNote(
        id: Int = 0,
        title: String,
        content: String,
        color: String
    ) {
        viewModelScope.launch {
            val note = Note(
                id = id,
                title = title,
                content = content,
                color = color
            )
            if (id == 0) {
                repository.addNote(note)
            } else {
                repository.updateNote(note)
            }
        }
    }


    fun getNoteById(noteId: Int): Note? {
        return _notes.value.find { it.id == noteId }
    }

    fun deleteNoteById(noteId: Int) {
        viewModelScope.launch {
            repository.deleteNoteById(noteId)
            fetchNotes() // Refresh the notes list
        }
    }
}

