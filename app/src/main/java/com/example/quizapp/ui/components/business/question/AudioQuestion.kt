package com.example.quizapp.ui.components.business.question

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quizapp.ui.view_model.TextToSpeechModel

@Composable
fun AudioQuestion(speechText: String, textToSpeechModel: TextToSpeechModel = viewModel()) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Listen to the question",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 10.dp, top = 10.dp)
        )
        IconButton(
            modifier = Modifier.padding(bottom = 10.dp),
            onClick = { textToSpeechModel.textToSpeech(context, speechText) }) {
            Icon(
                modifier = Modifier.width(80.dp),
                imageVector = Icons.Outlined.PlayCircle,
                contentDescription = null
            )
        }
    }
}