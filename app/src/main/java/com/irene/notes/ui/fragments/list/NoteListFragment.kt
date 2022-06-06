package com.irene.notes.ui.fragments.list

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.irene.notes.R
import com.irene.notes.data.model.Note
import com.irene.notes.ui.components.NoteItem
import com.irene.notes.ui.theme.LightGrey
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.stringResource
import androidx.work.*
import com.irene.notes.NotesApplication
import com.irene.notes.ui.MainActivity
import com.irene.notes.ui.theme.Blue
import com.irene.notes.util.LoadDataWorker
import com.irene.notes.util.Resource
import com.irene.notes.viewmodel.NotesViewModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinApiExtension

const val TAG = "WORK_TAG"
@KoinApiExtension
@ExperimentalMaterialApi
class NotesListFragment : Fragment() {

    private val viewModel by viewModel<NotesViewModel>()
    private val sharedPrefs: SharedPreferences by inject()
    private val sharedPrefsEditor: SharedPreferences.Editor by inject()

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_notes_list, container, false).apply {
            findViewById<ComposeView>(R.id.notes_list_composeView).setContent {
                var isLoadingFromRemote by rememberSaveable { mutableStateOf(true) }

                val isFirstLaunch = sharedPrefs.getBoolean(NotesApplication.sharedPrefsKey, true)

                val scaffoldState = rememberScaffoldState()
                val coroutineScope = rememberCoroutineScope()

                val workInfos = (activity as MainActivity).workManager
                    .getWorkInfosForUniqueWorkLiveData("download")
                    .observeAsState()
                    .value

                val noteSyncInfo = remember(key1 = workInfos) {
                    workInfos?.find { it.id == (activity as MainActivity).syncNoteWorkRequest.id }
                }

                when(noteSyncInfo?.state) {
                    WorkInfo.State.RUNNING -> {
                        Log.d(TAG, "WORK IS RUNNING ")
                        isLoadingFromRemote = true
                    }
                    WorkInfo.State.SUCCEEDED -> {
                        Log.d(TAG, "WORK IS SUCCEEDED ")
                        isLoadingFromRemote = false
                    }
                    WorkInfo.State.FAILED -> Log.d(TAG, "WORK IS FAILED ")
                    WorkInfo.State.CANCELLED -> Log.d(TAG, "WORK IS CANCELLED")
                    WorkInfo.State.ENQUEUED -> {
                        Log.d(TAG, "WORK IS ENQUEUED ")
                        coroutineScope.launch {
                            scaffoldState.snackbarHostState.showSnackbar(
                                message = "No internet connection"
                            )
                        }
                    }
                    WorkInfo.State.BLOCKED -> Log.d(TAG, "WORK IS BLOCKED ")
                }


                viewModel.getListOfNotes()
                val listOfNotes = viewModel.listNotes.collectAsState()

                Scaffold(
                    scaffoldState = scaffoldState,
                    topBar = {
                        TopBar()
                    },
                    floatingActionButton = {
                        FloatingActionButton(onClick = {
                            viewModel.addNewNote(Note.defaultNote())
                        }) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add new Note"
                            )
                        }
                    },
                    backgroundColor = LightGrey,
                    modifier = Modifier.fillMaxSize()
                ) {
                    MainContent(
                        isLoadingFromRemote = isLoadingFromRemote,
                        isFirstLaunch = isFirstLaunch,
                        navController = findNavController(),
                        paddingValues = it,
                        notesList = listOfNotes.value,
                        deleteNote = {
                            viewModel.deleteNote(it)
                        }
                    )
                }
            }
        }

    }

    @ExperimentalMaterialApi
    @Composable
    private fun MainContent(
        isLoadingFromRemote: Boolean,
        isFirstLaunch: Boolean,
        paddingValues: PaddingValues,
        navController: NavController,
        notesList: List<Note>,
        deleteNote: (id: Int) -> Unit
    ) {

        if (isFirstLaunch){
            if (isLoadingFromRemote){
                Loading()
            } else if (notesList.isEmpty()){
                NoDataContent("No Data")
                with (sharedPrefsEditor) {
                    putBoolean(NotesApplication.sharedPrefsKey, false)
                    apply()
                }
            } else{
                ListContent(notesList, deleteNote, navController)
            }
        } else {
            Column(Modifier.padding(top = paddingValues.calculateTopPadding())) {
                ProgressIndicator(isLoadingFromRemote)

                ListContent(notesList, deleteNote, navController)
            }
        }
    }

    @Composable
    private fun Loading() {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator()
        }
    }

    @Composable
    private fun NoDataContent(message: String) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(message)
        }
    }

    @Composable
    private fun ListContent(
        notesList: List<Note>,
        deleteNote: (id: Int) -> Unit,
        navController: NavController
    ) {
        LazyColumn(
            modifier = Modifier.padding(
                start = 8.dp,
                end = 8.dp
            )
        ) {
            items(notesList, key = { task ->
                task.id
            }) { item ->
                Log.d("LISTLENGTH", "$item id: ${item.id}")
                val state = rememberDismissState(
                    confirmStateChange = {
                        if (it == DismissValue.DismissedToStart) {
                            deleteNote(item.id)
                            Log.d("LIST_LENGTH", "After ${notesList.size}")
                        }
                        true
                    }
                )
                SwipeToDismiss(state = state, background = {
                    val color = when (state.dismissDirection) {
                        DismissDirection.StartToEnd -> Color.Transparent
                        DismissDirection.EndToStart -> Color.Black
                        null -> LightGrey
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = color)
                            .padding(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color.Gray,
                            modifier = Modifier.align(Alignment.CenterEnd)
                        )
                    }
                }, dismissContent = {

                    NoteItem(
                        item
                    ) {
                        Log.d(NotesApplication.TAG, " ITEM CLICK WITH ID $it ")
                        navController.navigate(
                            R.id.noteDetailsFragment,
                            bundleOf("note" to item)
                        )
                    }
                }, directions = setOf(DismissDirection.EndToStart))
            }
        }
    }


}

@Composable
fun ProgressIndicator(isLoadingFromRemote: Boolean) {
    if (isLoadingFromRemote)
        LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = Blue)
}

@Composable
fun TopBar() {
    TopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) })
}




