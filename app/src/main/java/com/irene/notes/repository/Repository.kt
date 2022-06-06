package com.irene.notes.repository

import com.irene.notes.data.database.NotesDao
import com.irene.notes.data.model.Note
import com.irene.notes.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class Repository(
    private val notesDao: NotesDao,
) {

    fun getNotes(): Flow<List<Note>> = notesDao.getAllNotes()

    suspend fun addNote(note: Note) {
        notesDao.insertNote(note)
    }

    suspend fun updateNote(title: String, description: String, dateAndTime: String, id: Int) {
        notesDao.updateNote(title, description, dateAndTime, id)
    }

    suspend fun deleteNote(noteId: Int) {
        notesDao.deleteNote(noteId)
    }
}