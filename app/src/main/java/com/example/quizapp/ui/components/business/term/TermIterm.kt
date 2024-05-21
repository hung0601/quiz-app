package com.example.quizapp.ui.components.business.term

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.imageLoader
import coil.request.ImageRequest
import com.example.quizapp.model.Term

@Composable
fun TermItem(term: Term, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        val request: ImageRequest = ImageRequest.Builder(LocalContext.current.applicationContext)
            .data(term.imageUrl)
            .crossfade(true)
            .diskCacheKey(term.imageUrl)
            .build()

        LocalContext.current.applicationContext.imageLoader.enqueue(request)
        Column(modifier = Modifier.padding(5.dp)) {
            Text(text = term.term, style = TextStyle(fontWeight = FontWeight.SemiBold))
            Text(text = term.definition)
        }
        if (term.imageUrl != null) {
            AsyncImage(
                model = request,
                contentDescription = null,
                modifier = Modifier
                    .height(60.dp)
                    .width(60.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop,
            )
        }
    }
}