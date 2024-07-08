package com.example.quizapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryAdd
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.quizapp.ui.navigation.Screen

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomBarScreen(
        route = Screen.Home.route,
        title = "Home",
        icon = Icons.Default.Home
    )

    object Search : BottomBarScreen(
        route = Screen.Search.route,
        title = "Search",
        icon = Icons.Default.Search
    )

    object Library : BottomBarScreen(
        route = Screen.Library.route,
        title = "Library",
        icon = Icons.Default.LibraryAdd
    )

    object Profile : BottomBarScreen(
        route = Screen.MyProfile.route,
        title = "Profile",
        icon = Icons.Default.Person
    )
}

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarScreen.Home,
        BottomBarScreen.Search,
        BottomBarScreen.Library,
        BottomBarScreen.Profile,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val screen = currentDestination?.hierarchy?.first()
    val appScreen = Screen.appScreens.find { it.route == screen?.route }
    val isShowBottomBar = appScreen?.isShowBottomBar ?: false;
    if (isShowBottomBar) {
        BottomNavigation(
            backgroundColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
        ) {
            screens.forEach { screen ->
                val isSelected =
                    currentDestination?.hierarchy?.any { it.route == screen.route } == true
                BottomNavigationItem(
                    icon = {
                        Icon(
                            screen.icon,
                            contentDescription = null,
                            tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary
                        )
                    },
                    label = {
                        Text(
                            text = screen.title,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary
                        )
                    },
                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id)
                            launchSingleTop = true
                        }
                    },
                    modifier = if (isSelected) Modifier
                        .background(
                            color = Color.White,
                            shape = CircleShape
                        )
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.primary,
                            CircleShape
                        ) else Modifier,
                )
            }
        }
    }
}
