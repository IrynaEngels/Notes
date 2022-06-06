package com.irene.notes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.irene.notes.data.model.Note
import com.irene.notes.repository.Repository
import com.irene.notes.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class NotesViewModel(private val repository: Repository) : ViewModel() {

    private val _listNotes: MutableStateFlow<List<Note>> =
        MutableStateFlow(listOf())
    val listNotes: StateFlow<List<Note>> = _listNotes.asStateFlow()


    fun getListOfNotes() {
        viewModelScope.launch {
            repository.getNotes().collect {
                _listNotes.value = it
            }
        }
    }

    fun addNewNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addNote(note = note)
        }
    }

    fun updateNote(title: String, description: String, dateAndTime: String, id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateNote(title, description, dateAndTime, id)
        }
    }

    fun deleteNote(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteNote(id)
        }
    }

}