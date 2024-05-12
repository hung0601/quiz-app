package com.example.quizapp.ui.screens.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quizapp.ui.components.basic.avatar.CircleAvatar
import com.example.quizapp.ui.navigation.AUTH_GRAPH_ROUTE
import com.example.quizapp.ui.screens.UserViewModel

@Composable
fun ProfileScreen(navController: NavController) {
    val userViewModel = hiltViewModel<UserViewModel>()
    val profileViewModel = hiltViewModel<ProfileViewModel>()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        profileViewModel.currentUser?.let {
            CircleAvatar(
                avatarImg = profileViewModel.currentUser.imageUrl,
                name = it.name,
                modifier = Modifier.size(50.dp)
            )
            Text(
                text = profileViewModel.currentUser.name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = profileViewModel.currentUser.email,
                style = MaterialTheme.typography.bodyLarge
            )
        }
        Button(onClick = {
            userViewModel.clearSession();
            navController.navigate(AUTH_GRAPH_ROUTE)
        }) {
            Text(text = "Logout")
        }
    }
}