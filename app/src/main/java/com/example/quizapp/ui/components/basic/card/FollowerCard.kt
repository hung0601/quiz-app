package com.example.quizapp.ui.components.basic.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.quizapp.model.CreatorProfile
import com.example.quizapp.ui.components.basic.avatar.CircleAvatar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FollowerCard(modifier: Modifier = Modifier, user: CreatorProfile, onClick: (Int) -> Unit = {}) {
    CustomCard(modifier = modifier.fillMaxWidth(), onClick = { onClick(user.id) }) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                CircleAvatar(
                    avatarImg = user.imageUrl,
                    modifier = Modifier.size(30.dp, 30.dp),
                    name = user.name
                )

                Column {
                    Text(text = user.name, style = MaterialTheme.typography.bodyLarge)
                    Text(
                        text = user.email,
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
            if (user.isFollowing) {
                AssistChip(
                    onClick = { /*TODO*/ },
                    label = {
                        Text(
                            text = "Following",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                    shape = CircleShape,
                    border = AssistChipDefaults.assistChipBorder(borderColor = MaterialTheme.colorScheme.primary)
                )
            }
        }
    }
}