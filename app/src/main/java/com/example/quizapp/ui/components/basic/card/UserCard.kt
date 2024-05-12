package com.example.quizapp.ui.components.basic.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.quizapp.model.Profile
import com.example.quizapp.ui.components.basic.avatar.CircleAvatar


@Composable
fun MemberItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    member: Profile,
) {
    CustomCard(modifier = modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
        ) {
            CircleAvatar(
                avatarImg = member.imageUrl,
                modifier = Modifier.size(30.dp, 30.dp),
                name = member.name
            )

            Column {
                Text(text = member.name, style = MaterialTheme.typography.bodyLarge)
                Text(
                    text = member.email,
                    style = MaterialTheme.typography.labelLarge,
                )
            }

        }


    }
}

