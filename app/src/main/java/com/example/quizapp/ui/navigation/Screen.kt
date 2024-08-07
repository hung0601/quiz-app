package com.example.quizapp.ui.navigation


const val ROOT_GRAPH_ROUTE = "root"
const val AUTH_GRAPH_ROUTE = "auth"
const val HOME_GRAPH_ROUTE = "home"

sealed class Screen(
    val route: String,
    val title: String,
    val isShowBottomBar: Boolean = true,
    val isShowTopBar: Boolean = true,
    val canNavigateBack: Boolean = true,
    val navigateIconType: Int = 1,
) {
    object Test : Screen(route = "test", title = "Test", isShowTopBar = false)
    object Home : Screen(
        route = "home_screen",
        title = "Home Screen",
        isShowTopBar = false,
        canNavigateBack = false
    )

    object Library : Screen(
        route = "library?tabId={tabId}",
        title = "Library",
        isShowTopBar = false,
        canNavigateBack = false
    ) {
        fun passTabId(id: Int = 0): String {
            return "library?tabId=$id"
        }
    }

    object StudySet :
        Screen(route = "study_set/{id}", title = "Study Set Detail", isShowBottomBar = false) {
        fun passId(
            id: Int
        ): String {
            return "study_set/$id"
        }
    }

    object EditStudySet :
        Screen(
            route = "edit_study_set/{id}",
            title = "Edit Study Set",
            isShowBottomBar = false,
            isShowTopBar = true,
            navigateIconType = 2
        ) {
        fun passId(
            id: Int
        ): String {
            return "edit_study_set/$id"
        }
    }

    object TermManagement :
        Screen(
            route = "term_management/{id}",
            title = "Edit Terms",
            isShowBottomBar = false,
            isShowTopBar = false
        ) {
        fun passId(
            id: Int
        ): String {
            return "term_management/$id"
        }
    }

    object Course :
        Screen(route = "course/{id}", title = "Course Detail", isShowBottomBar = false) {
        fun passId(
            id: Int
        ): String {
            return "course/$id"
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

    object Exam :
        Screen(route = "exam/{id}", title = "Exam", isShowBottomBar = false, isShowTopBar = false) {
        fun passId(
            id: Int
        ): String {
            return "exam/$id"
        }
    }

    object MatchGame :
        Screen(
            route = "match_game/{id}",
            title = "Match game",
            isShowBottomBar = false,
            isShowTopBar = false
        ) {
        fun passId(
            id: Int
        ): String {
            return "match_game/$id"
        }
    }

    object CustomExam :
        Screen(
            route = "custom_exam/{id}",
            title = "Exam",
            isShowBottomBar = false,
            isShowTopBar = false
        ) {
        fun passId(
            id: Int
        ): String {
            return "custom_exam/$id"
        }
    }

    object Login : Screen(
        route = "login_screen",
        title = "Login Screen",
        isShowBottomBar = false,
        isShowTopBar = false
    )

    object MyProfile : Screen(
        route = "my_profile_screen",
        title = "Profile",
        isShowBottomBar = true,
        isShowTopBar = true
    )

    object Profile : Screen(
        route = "profile_screen/{id}",
        title = "Profile",
        isShowBottomBar = true,
        isShowTopBar = true

    ) {
        fun passId(
            id: Int
        ): String {
            return "profile_screen/$id"
        }
    }

    object Notification : Screen(
        route = "notification",
        title = "Notification",
        isShowBottomBar = true,
        isShowTopBar = false
    )

    object Search : Screen(
        route = "search",
        title = "search",
        isShowBottomBar = true,
        isShowTopBar = false
    )

    object CreateCourse : Screen(
        route = "create_course",
        title = "Create course",
        isShowBottomBar = false,
        isShowTopBar = true,
        navigateIconType = 2
    )

    object CreateStudySet : Screen(
        route = "create_study_set?courseId={courseId}",
        title = "Create study set",
        isShowBottomBar = false,
        isShowTopBar = true,
        navigateIconType = 2
    ) {
        fun passCourseId(id: Int = 0): String {
            return "create_study_set?courseId=$id"
        }
    }


    companion object {
        val appScreens = listOf(
            Test,
            Home,
            Library,
            StudySet,
            Course,
            FlashCard,
            Exam,
            MatchGame,
            CustomExam,
            Login,
            MyProfile,
            Profile,
            CreateCourse,
            CreateStudySet,
            EditStudySet,
            TermManagement,
            Notification,
            Search
        )
    }

}
