package com.example.notetest.data.repository

import com.example.notetest.data.local.NoteDao
import com.example.notetest.data.model.Note
import kotlinx.coroutines.flow.Flow

class NoteRepositoryImpl(
    private val noteDao: NoteDao
) : NoteRepository {
    override fun getAllNotes(): Flow<List<Note>> = noteDao.getAllNotes()

    override suspend fun addNote(note: Note) = noteDao.insertNote(note)

    override suspend fun updateNote(note: Note) = noteDao.updateNote(note)

    override suspend fun deleteNoteById(noteId: Int) = noteDao.deleteNoteById(noteId)
}
