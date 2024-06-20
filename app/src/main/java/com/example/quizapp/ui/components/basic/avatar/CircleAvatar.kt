package com.example.quizapp.ui.components.basic.avatar

import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.example.quizapp.R

@Composable
fun CircleAvatar(avatarImg: String?, modifier: Modifier, name: String = "") {
    if (avatarImg == null) {
        Image(
            painter = painterResource(R.drawable.default_avatar),
            contentDescription = null,
            modifier = modifier
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
        )
    } else {
        AsyncImage(
            model = avatarImg,
            contentDescription = null,
            modifier = modifier
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
        )
    }
}