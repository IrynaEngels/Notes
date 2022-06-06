package com.irene.notes.util

import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun <T> List<T>.toMutableList(): MutableList<T>{
    val mList = mutableListOf<T>()
    mList.addAll(this)
    return mList
}

@RequiresApi(Build.VERSION_CODES.O)
fun String.toDateTime(): LocalDateTime{
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    return LocalDateTime.parse(this, formatter)
}

@RequiresApi(Build.VERSION_CODES.O)
fun showDateTime(dateTime: String): String{
    val noteDateTime = dateTime.toDateTime()
    val current = LocalDateTime.now()
    val formatter = if(noteDateTime.dayOfYear == current.dayOfYear && noteDateTime.year == current.year){
        DateTimeFormatter.ofPattern("HH:mm")
    } else {
        DateTimeFormatter.ofPattern("dd/MM/yyy")
    }
    return noteDateTime.format(formatter)
}

@RequiresApi(Build.VERSION_CODES.O)
fun currentDateTimeToString(): String{
    val current = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    return current.format(formatter)
}