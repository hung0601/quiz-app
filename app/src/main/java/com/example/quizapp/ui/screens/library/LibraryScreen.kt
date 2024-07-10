package com.example.quizapp.ui.screens.library

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quizapp.model.Course
import com.example.quizapp.model.StudySet
import com.example.quizapp.network.response_model.ResponseHandlerState
import com.example.quizapp.ui.components.basic.card.CourseItemCard
import com.example.quizapp.ui.components.basic.card.StudySetItem
import com.example.quizapp.ui.navigation.Screen
import com.example.quizapp.ui.screens.hooks.ErrorScreen
import com.example.quizapp.ui.screens.hooks.LoadingScreen

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LibraryScreen(navController: NavController) {
    val libraryViewModel = hiltViewModel<LibraryViewModel>()
    val studySetList by libraryViewModel.studySetList.collectAsState()
    val courseList by libraryViewModel.courseList.collectAsState()
    var tabIndex by remember { mutableStateOf(libraryViewModel.tabId) }

    val isLoading by libraryViewModel.isLoading.collectAsState()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isLoading,
        onRefresh = libraryViewModel::fetchData
    )

    val tabs = listOf("Study sets", "Courses")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface)
                .padding(10.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Library", style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.Center)
                )
                IconButton(onClick = {
                    if (tabIndex == 0) {
                        navController.navigate(Screen.CreateStudySet.route)
                    } else navController.navigate(Screen.CreateCourse.route)
                }, modifier = Modifier.align(Alignment.CenterEnd)) {
                    Icon(imageVector = Icons.Outlined.Add, contentDescription = null)
                }
            }
            TabRow(selectedTabIndex = tabIndex) {
                tabs.forEachIndexed { index, title ->
                    Tab(text = { Text(title) },
                        selected = tabIndex == index,
                        onClick = { tabIndex = index }
                    )
                }
            }
            when (tabIndex) {
                0 -> {
                    when (studySetList) {
                        is ResponseHandlerState.Success -> {
                            StudySetList(
                                navController = navController,
                                studySetList = (studySetList as ResponseHandlerState.Success<List<StudySet>>).data
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

                1 -> {
                    when (courseList) {
                        is ResponseHandlerState.Success -> {
                            CourseList(
                                navController,
                                (courseList as ResponseHandlerState.Success<List<Course>>).data
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

        PullRefreshIndicator(
            refreshing = isLoading,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@Composable
fun CourseList(navController: NavController, courseList: List<Course>) {
    LazyColumn(
        contentPadding = PaddingValues(top = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(courseList) {
            CourseItemCard(navController, it)
        }
    }

}

@Composable
fun StudySetList(navController: NavController, studySetList: List<StudySet>) {
    LazyColumn(
        contentPadding = PaddingValues(top = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(studySetList) {
            StudySetItem(onClick = { navController.navigate(Screen.StudySet.passId(it.id)) }, it)
        }
    }

}

