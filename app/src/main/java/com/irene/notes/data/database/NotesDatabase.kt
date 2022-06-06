package com.irene.notes.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.irene.notes.data.model.Note

@Database(entities = [Note::class], version = 1)
abstract class NotesDatabase : RoomDatabase(){

    abstract fun getNotesDao(): NotesDao

    companion object{
        // @Volatile - Writes to this property are immediately visible to other threads
        @Volatile private var instance: NotesDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                NotesDatabase::class.java, "notes_database").build()
    }
}
