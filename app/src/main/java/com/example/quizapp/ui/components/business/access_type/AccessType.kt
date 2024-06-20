package com.example.quizapp.ui.components.business.access_type

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.quizapp.constant.PRIVATE_ACCESS
import com.example.quizapp.constant.PUBLIC_ACCESS
import com.example.quizapp.constant.SHARE_WITH_FOLLOWER_ACCESS

@Composable
fun AccessType(modifier: Modifier = Modifier, accessType: Int = PUBLIC_ACCESS) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        modifier = modifier
    ) {
        var icon = Icons.Outlined.Language
        var accessText = "Public"
        when (accessType) {
            PUBLIC_ACCESS -> {
                icon = Icons.Outlined.Language
                accessText = "Public"
            }

            SHARE_WITH_FOLLOWER_ACCESS -> {
                icon = Icons.Outlined.Groups
                accessText = "Follower"
            }

            PRIVATE_ACCESS -> {
                icon = Icons.Outlined.Lock
                accessText = "Private"
            }
        }
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(18.dp)
        )
        Text(text = accessText, style = MaterialTheme.typography.labelSmall)
    }
}