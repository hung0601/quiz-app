package com.example.quizapp.ui.navigation


const val ROOT_GRAPH_ROUTE = "root"
const val AUTH_GRAPH_ROUTE = "auth"
const val HOME_GRAPH_ROUTE = "home"

sealed class Screen(
    val route: String,
    val title: String,
    val isShowBottomBar: Boolean = true,
    val isShowTopBar: Boolean = true,
    val canNavigateBack: Boolean = true
) {
    object Test : Screen(route = "test", title = "Test", isShowTopBar = false)
    object Home : Screen(route = "home_screen", title = "Home Screen", canNavigateBack = false)
    object StudySet :
        Screen(route = "study_set/{id}", title = "Study Set Detail", isShowBottomBar = false) {
        fun passId(
            id: Int
        ): String {
            return "study_set/$id"
        }
    }

    object FlashCard :
        Screen(route = "flash_card/{id}", title = "Flash Card", isShowBottomBar = false) {
        fun passId(
            id: Int
        ): String {
            return "flash_card/$id"
        }
    }

    object Login : Screen(
        route = "login_screen",
        title = "Login Screen",
        isShowBottomBar = false,
        isShowTopBar = false
    )

    object Profile : Screen(
        route = "profile_screen",
        title = "Profile Screen",
        isShowBottomBar = true,
        isShowTopBar = true
    )

    companion object {
        val appScreens = listOf(Test, Home, StudySet, FlashCard, Login, Profile)
    }

}
