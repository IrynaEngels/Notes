package com.irene.notes.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.irene.notes.data.model.Note
import com.irene.notes.ui.theme.Blue
import com.irene.notes.ui.theme.DarkGrey
import com.irene.notes.util.showDateTime

@Composable
fun NoteItem(item: Note, onItemClick: (id: Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .clickable { onItemClick(item.id) },
        shape = RoundedCornerShape(12.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
        ) {
            Text(
                text = item.title,
                fontSize = 24.sp,
                fontWeight = FontWeight.W900,
                color = MaterialTheme.colors.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = item.description,
                fontSize = 12.sp,
                fontWeight = FontWeight.W900,
                color = DarkGrey,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = showDateTime(item.dateAndTime),
                fontSize = 12.sp,
                fontWeight = FontWeight.W900,
                color = Blue
            )
        }
    }

}
