package com.irene.notes.util

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay

class LoadDataWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = coroutineScope {
        delay(5000)
        Result.success(workDataOf())
    }
}