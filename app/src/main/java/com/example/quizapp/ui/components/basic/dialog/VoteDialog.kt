package com.example.quizapp.ui.components.basic.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.quizapp.ui.components.basic.star_review.StarReview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoteDialog(
    onVote: (Int) -> Unit,
    onCancel: () -> Unit
) {
    Dialog(
        onDismissRequest = { },
    ) {
        val star = remember {
            mutableFloatStateOf(0F)
        }
        ElevatedCard(
            modifier = Modifier
                .width(300.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Vote this set",
                    style = MaterialTheme.typography.titleMedium
                )
                Divider(
                    modifier = Modifier.padding(
                        top = 10.dp,
                        start = 5.dp,
                        end = 5.dp,
                        bottom = 10.dp
                    )
                )
                Column {
                    StarReview(
                        star = star.floatValue,
                        size = 40,
                        onChange = { star.floatValue = it.toFloat() })
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { onCancel() }) {
                        Text(text = "Skip")
                    }
                    TextButton(
                        onClick = { onVote(star.floatValue.toInt()) },
                        enabled = star.floatValue > 0
                    ) {
                        Text(text = "Vote")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VoteDialogPreview() {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp, start = 20.dp)
    ) {
        VoteDialog({}, {})
    }
}