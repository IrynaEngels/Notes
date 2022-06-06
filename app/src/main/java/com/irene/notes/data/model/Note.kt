package com.irene.notes.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.irene.notes.util.currentDateTimeToString
import kotlinx.parcelize.Parcelize

@Entity(tableName = "note")
@Parcelize
data class Note(
   /* @PrimaryKey(autoGenerate = true)
    var id: Int = 0,*/
    val title: String,
    val description: String,
    val dateAndTime:String
):Parcelable{
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    companion object{
        fun defaultNote(): Note = Note("New note", "", currentDateTimeToString())
    }
}
