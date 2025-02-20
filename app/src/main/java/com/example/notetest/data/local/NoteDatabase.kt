package com.example.notetest.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.notetest.data.model.Note

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}
