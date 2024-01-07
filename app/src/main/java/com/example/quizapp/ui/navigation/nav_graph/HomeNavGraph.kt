package com.example.quizapp.ui.navigation.nav_graph

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.quizapp.ui.navigation.HOME_GRAPH_ROUTE
import com.example.quizapp.ui.navigation.Screen
import com.example.quizapp.ui.screens.UserViewModel
import com.example.quizapp.ui.screens.flash_card.FlashCardScreen
import com.example.quizapp.ui.screens.home.HomeScreen
import com.example.quizapp.ui.screens.home.TestHomeScreen
import com.example.quizapp.ui.screens.profile.ProfileScreen
import com.example.quizapp.ui.screens.set_detail.SetDetailModel
import com.example.quizapp.ui.screens.set_detail.SetDetailScreen
import com.example.quizapp.ui.screens.set_detail.StudySetUiState

fun NavGraphBuilder.homeNavGraph(
    navController: NavHostController
) {
    navigation(
        startDestination = Screen.Test.route,
        route = HOME_GRAPH_ROUTE
    ) {
        composable(
            route = Screen.Test.route
        ) {
            val userViewModel = hiltViewModel<UserViewModel>();
            if (userViewModel.session == null) {
                navController.navigate(Screen.Login.route)
            }

            TestHomeScreen(navController = navController)
        }
        composable(
            route = Screen.Home.route
        ) {
            val userViewModel = hiltViewModel<UserViewModel>();
            if (userViewModel.session == null) {
                navController.navigate(Screen.Login.route)
            }

            HomeScreen(navController = navController)
        }
        composable(
            route = Screen.StudySet.route,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                }
            )
        ) {
            val viewModel = it.sharedViewModel<SetDetailModel>(navController)
            val id = checkNotNull(it.arguments?.getInt("id"))
            SetDetailScreen(
                viewModel,
                onClickStudy = { navController.navigate(Screen.FlashCard.passId(id)) },
                navController = navController
            )
        }
        composable(
            route = Screen.FlashCard.route,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                }
            )
        ) {
            val viewModel = it.sharedViewModel<SetDetailModel>(navController)
            val studySetUiState: StudySetUiState = viewModel.uiState
            when (studySetUiState) {
                is StudySetUiState.Success -> FlashCardScreen(studySet = studySetUiState.studySet)
                else -> Text(text = "error")
            }
        }
        composable(route = Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }
    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavHostController,
): T {
    val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
    if (!navGraphRoute.contains("study_set")) return hiltViewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return hiltViewModel(parentEntry)
}