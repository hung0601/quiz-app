package com.example.quizapp.ui.screens.notification

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quizapp.model.CourseInvite
import com.example.quizapp.network.response_model.ResponseHandlerState
import com.example.quizapp.ui.components.avatar.CircleAvatar
import com.example.quizapp.ui.navigation.Screen
import com.example.quizapp.ui.screens.hooks.ErrorScreen
import com.example.quizapp.ui.screens.hooks.LoadingScreen
import kotlinx.coroutines.launch

@Composable
fun NotificationScreen(navController: NavController) {
    val notificationViewModel = hiltViewModel<NotificationViewModel>()
    val inviteList by notificationViewModel.inviteList.collectAsState()
    val acceptResponse by notificationViewModel.acceptResponse.collectAsState()

    val scope = rememberCoroutineScope()
    val context = LocalContext.current


    LaunchedEffect(key1 = acceptResponse is ResponseHandlerState.Success) {
        scope.launch {
            if (acceptResponse is ResponseHandlerState.Success) {
                notificationViewModel.getInvites()
                if ((acceptResponse as ResponseHandlerState.Success<Boolean>).data) {
                    Toast.makeText(context, "Join course successful", Toast.LENGTH_LONG).show()
                    notificationViewModel.resetState()
                    navController.navigate(Screen.Course.passId(notificationViewModel.courseId))
                } else {
                    Toast.makeText(context, "Deny successful", Toast.LENGTH_LONG).show()
                }

                //navController.navigate(HOME_GRAPH_ROUTE)
            }
        }
    }

    LaunchedEffect(key1 = acceptResponse is ResponseHandlerState.Error) {
        if (acceptResponse is ResponseHandlerState.Error) {
            scope.launch {
                Toast.makeText(
                    context,
                    (acceptResponse as ResponseHandlerState.Error).errorMsg,
                    Toast.LENGTH_LONG
                ).show()
                notificationViewModel.resetState()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Notification", style = MaterialTheme.typography.titleLarge)
            when (inviteList) {
                is ResponseHandlerState.Success -> {
                    InviteList(
                        inviteList = (inviteList as ResponseHandlerState.Success<List<CourseInvite>>).data,
                        notificationViewModel = notificationViewModel,
                        navController
                    )
                }

                is ResponseHandlerState.Loading -> {
                    LoadingScreen()
                }

                else -> {
                    ErrorScreen()
                }

            }
        }
    }
}

@Composable
fun InviteList(
    inviteList: List<CourseInvite>,
    notificationViewModel: NotificationViewModel,
    navController: NavController
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.padding(top = 10.dp)
    ) {
        items(inviteList) {
            InviteItem(
                courseInvite = it,
                onClick = { navController.navigate(Screen.Course.passId(it.courseId)) },
                onAccept = { id -> notificationViewModel.acceptInvite(id, true) },
                onDeny = { id -> notificationViewModel.acceptInvite(id, false) })
        }
    }
}

@Composable
fun InviteItem(
    courseInvite: CourseInvite,
    onClick: () -> Unit,
    onAccept: (Int) -> Unit,
    onDeny: (Int) -> Unit
) {
    Row(modifier = Modifier
        .drawBehind {
            val y = size.height - 4 / 2
            drawLine(
                Color.LightGray,
                Offset(0f, y),
                Offset(size.width, y),
                4f
            )
        }
        .clickable { onClick() }
        .padding(bottom = 10.dp)) {
        CircleAvatar(
            avatarImg = courseInvite.owner.imageUrl,
            modifier = Modifier.size(40.dp),
            name = courseInvite.owner.name
        )
        Row(modifier = Modifier.padding(start = 5.dp)) {
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Blue)) {
                        append(courseInvite.owner.name)
                    }
                    append(" has invited you to join the ")

                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Blue)) {
                        append(courseInvite.courseTitle)
                    }
                    append(" course")
                },
                modifier = Modifier.weight(1f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,

                )
            Row {
                IconButton(onClick = { onAccept(courseInvite.courseId) }) {
                    Icon(
                        imageVector = Icons.Outlined.Check,
                        contentDescription = null,
                        tint = Color.Green
                    )
                }
                IconButton(onClick = { onDeny(courseInvite.courseId) }) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = null,
                        tint = Color.Red
                    )
                }
            }
        }

    }
}