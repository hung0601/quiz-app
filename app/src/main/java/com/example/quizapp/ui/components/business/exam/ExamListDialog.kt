package com.example.quizapp.ui.components.business.exam

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.quizapp.model.Exam

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamListDialog(
    examList: List<Exam>,
    onConfirmation: () -> Unit,
    onSelect: (Int) -> Unit
) {
    Dialog(
        onDismissRequest = { },
    ) {
        ElevatedCard(
            modifier = Modifier
                .width(300.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                Text(text = "Select exam", style = MaterialTheme.typography.titleMedium)
                Divider(
                    modifier = Modifier.padding(
                        top = 10.dp,
                        start = 5.dp,
                        end = 5.dp,
                        bottom = 10.dp
                    )
                )
                LazyColumn {
                    items(examList) {
                        ListItem(
                            headlineText = { Text(it.testName) },
                            trailingContent = {
                                Box(
                                    modifier = Modifier
                                        .padding(end = 5.dp, bottom = 5.dp)
                                        .background(
                                            MaterialTheme.colorScheme.tertiary,
                                            RoundedCornerShape(5.dp)
                                        )
                                ) {
                                    androidx.compose.material3.Text(
                                        text = "${it.questionCount} questions",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onTertiary,
                                        modifier = Modifier.padding(3.dp)
                                    )
                                }
                            },
                            leadingContent = {
                                Icon(
                                    Icons.Filled.Quiz,
                                    contentDescription = null,
                                )
                            },
                            modifier = Modifier
                                .padding(bottom = 10.dp)
                                .clickable {
                                    onSelect(it.id)
                                }
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(onClick = { onConfirmation() }) {
                        Text(text = "Cancel")
                    }
                }
            }
        }
    }

}