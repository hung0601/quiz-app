package com.example.quizapp.ui.components.basic.tag

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CustomTag(
    modifier: Modifier = Modifier,
    text: String,
    backgroundColor: Long = 0xffe6f4ea,
    color: Long = 0xff188038
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .background(
                Color(backgroundColor),
                CircleShape
            )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = Color(color),
            modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 3.dp, bottom = 3.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TagPreview() {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp, start = 20.dp)
    ) {
        CustomTag(modifier = Modifier, text = "English")
    }
}