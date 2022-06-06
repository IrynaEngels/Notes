package com.irene.notes.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.irene.notes.data.model.Note
import kotlinx.coroutines.flow.Flow
import androidx.room.Update




@Dao
interface NotesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(wallpaper: Note)

    @Query("DELETE FROM note WHERE id = :noteID")
    fun deleteNote(noteID: Int)

    @Query("SELECT * FROM note")
    fun getAllNotes(): Flow<List<Note>>

    @Query("SELECT * FROM note WHERE id = :noteID")
    fun getNoteById(noteID: Int): Flow<Note>

    @Query("UPDATE note SET title=:title,  description=:description, dateAndTime=:dateAndTime WHERE id = :id")
    fun updateNote(title: String, description: String, dateAndTime: String, id: Int)

}