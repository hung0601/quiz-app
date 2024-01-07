package com.example.quizapp.ui.screens.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quizapp.ui.navigation.AUTH_GRAPH_ROUTE
import com.example.quizapp.ui.screens.UserViewModel

@Composable
fun ProfileScreen(navController: NavController) {
    val userViewModel = hiltViewModel<UserViewModel>()
    Column {
        Text(text = "Profile")
        Button(onClick = {
            userViewModel.clearSession();
            navController.navigate(AUTH_GRAPH_ROUTE)
        }) {
            Text(text = "Logout")
        }
    }
}