package com.irene.notes.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.work.*
import com.irene.notes.R
import com.irene.notes.ui.theme.NotesTheme
import com.irene.notes.util.LoadDataWorker

class MainActivity : AppCompatActivity() {

    lateinit var workManager: WorkManager
    lateinit var syncNoteWorkRequest: OneTimeWorkRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        workManager = WorkManager.getInstance(this)
        syncNoteWorkRequest = OneTimeWorkRequestBuilder<LoadDataWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(
                        NetworkType.CONNECTED
                    ).build()
            ).build()

        startWork(workManager = workManager,syncNoteWorkRequest)
    }


    private fun startWork (workManager:WorkManager, noteSyncRequest: OneTimeWorkRequest){
        workManager
            .beginUniqueWork(
                "download",
                ExistingWorkPolicy.KEEP,
                noteSyncRequest
            )
            .enqueue()
    }
}

