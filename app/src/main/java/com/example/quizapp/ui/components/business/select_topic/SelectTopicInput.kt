package com.example.quizapp.ui.components.business.select_topic

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quizapp.model.Topic
import com.example.quizapp.network.response_model.ResponseHandlerState
import com.example.quizapp.ui.components.basic.textfield.CustomTextField

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun TopicSelector(
    modifier: Modifier = Modifier,
    topics: List<Topic> = emptyList(),
    onSelect: (Topic) -> Unit,
    onRemove: (Topic) -> Unit,
) {
    val topicViewModel = hiltViewModel<TopicViewModel>()
    var expanded by remember { mutableStateOf(false) }
    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    if (expanded) {
        SelectTopicDialog(
            topics = topics,
            onDismissRequest = { expanded = false },
            topicViewModel = topicViewModel,
            onSelect = {
                expanded = false
                onSelect(it)
            })
    }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        FlowRow(
            modifier = Modifier
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            topics.map {
                FilterChip(selected = true, onClick = { /*TODO*/ }, label = {
                    Text(text = it.name)
                },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = null,
                            modifier = Modifier
                                .size(15.dp)
                                .clickable {
                                    onRemove(it)
                                },
                        )
                    }
                )
            }
        }
        AssistChip(
            onClick = {
                expanded = !expanded
            },
            label = {
                Text(
                    text = "Select topic", style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            },
            trailingIcon = {
                Icon(imageVector = icon, contentDescription = null)
            },
            border = AssistChipDefaults.assistChipBorder(borderColor = MaterialTheme.colorScheme.primary),
        )
    }
}

@Composable
fun SelectTopicDialog(
    topics: List<Topic>,
    onDismissRequest: () -> Unit,
    onSelect: (Topic) -> Unit,
    topicViewModel: TopicViewModel
) {
    val search = remember { mutableStateOf("") }
    val topicList by topicViewModel.topicList.collectAsState()
    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                    .background(color = MaterialTheme.colorScheme.background)
                    .fillMaxHeight(0.85f)
                    .align(Alignment.BottomCenter)
                    .padding(8.dp)
            ) {

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = { onDismissRequest() }) {
                        Text(text = "Cancel")
                    }
                }
                CustomTextField(
                    value = search.value,
                    onValueChange = {
                        search.value = it
                        topicViewModel.getTopicList(search.value)
                    },
                    placeholder = { Text(text = "Search topic") },
                )

                LazyColumn {
                    if (topicList is ResponseHandlerState.Success) {
                        items((topicList as ResponseHandlerState.Success).data) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp)
                                    .clickable {
                                        if (!topics.contains(it)) onSelect(it)
                                    },
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Text(
                                    text = it.name,
                                    style = MaterialTheme.typography.bodyLarge,
                                )
                                if (topics.contains(it)) {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = null
                                    )
                                }
                            }
                            Divider()
                        }
                    }
                }
            }
        }
    }
}