package com.example.quizapp.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.quizapp.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizAppTopBar(
    navController: NavHostController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val screen = currentDestination?.hierarchy?.first()
    val appScreen = Screen.appScreens.find { it.route == screen?.route }
    val isShowBottomBar = appScreen?.isShowTopBar ?: false;
    if (isShowBottomBar) {
        TopAppBar(
            title = { Text(text = appScreen?.title ?: "") },
            colors = TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            modifier = Modifier,
            navigationIcon = {
                if (appScreen?.canNavigateBack != false) {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back button"
                        )
                    }
                }
            }
        )
    }
}