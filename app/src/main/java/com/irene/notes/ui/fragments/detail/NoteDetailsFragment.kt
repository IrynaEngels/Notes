package com.irene.notes.ui.fragments.detail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import com.irene.notes.NotesApplication
import com.irene.notes.R
import com.irene.notes.data.model.Note
import com.irene.notes.util.currentDateTimeToString
import com.irene.notes.viewmodel.NotesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class NoteDetailsFragment : Fragment() {

    private val viewModel by viewModel<NotesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_note_details, container, false).apply {
            findViewById<ComposeView>(R.id.note_details_composeView).setContent {

                val note = arguments?.getParcelable("note") ?: Note.defaultNote()
                var isNoteChanged by rememberSaveable { mutableStateOf(false) }
                var titleValue by rememberSaveable() { mutableStateOf(note.title) }
                var descriptionValue by rememberSaveable() { mutableStateOf(note.description) }

                Scaffold(
                    topBar = {
                        NoteTopBar(
                            isNoteChanged = isNoteChanged,
                            updateNote = {
                                viewModel.updateNote(
                                    titleValue,
                                    descriptionValue,
                                    currentDateTimeToString(),
                                    note.id
                                )
                                findNavController().navigate(
                                    R.id.notesListFragment
                                )
                            })
                    }) { paddings ->

                    NoteDetailContent(
                        paddings,
                        titleValue,
                        onTitleChanged = { changedTitle ->
                            titleValue = changedTitle
                            isNoteChanged = note.title != changedTitle

                        },
                        descriptionValue,
                        onDescriptionChanged = { changedDescription ->
                            descriptionValue = changedDescription
                            isNoteChanged = note.description != changedDescription
                        }
                    )
                }

            }
        }
    }

    @Composable
    private fun NoteDetailContent(
        it: PaddingValues,
        titleValue: String,
        onTitleChanged: (changedTitle: String) -> Unit,
        descriptionValue: String,
        onDescriptionChanged: (changedDescription: String) -> Unit
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray)
                .padding(top = it.calculateTopPadding())
        ) {
            //Title
            OutlinedTextField(
                textStyle = MaterialTheme.typography.h6,
                placeholder = { Text(text = stringResource(id = R.string.enter_note_title)) },
                modifier = Modifier
                    .fillMaxWidth(),
                value = titleValue,
                onValueChange = { onTitleChanged(it) },
                colors =
                TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.DarkGray,
                    backgroundColor = Color.White
                ),
            )

            //Description
            OutlinedTextField(
                textStyle = MaterialTheme.typography.body2,
                placeholder = { Text(text = stringResource(id = R.string.enter_description_note)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                value = descriptionValue,
                onValueChange = { onDescriptionChanged(it) },
                colors =
                TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.DarkGray, backgroundColor = Color.White
                )
            )
        }
    }
}

@Composable
fun NoteTopBar(isNoteChanged: Boolean, updateNote: () -> Unit) {
    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.note_details))
        },
        actions = {
            if (isNoteChanged) {
                IconButton(onClick = { updateNote() }) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = stringResource(id = android.R.string.ok)
                    )
                }
            }
        })
}