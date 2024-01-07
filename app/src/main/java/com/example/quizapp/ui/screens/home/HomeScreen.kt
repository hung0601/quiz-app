/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.quizapp.ui.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.SnapLayoutInfoProvider
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Article
import androidx.compose.material.icons.outlined.Class
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.quizapp.R
import com.example.quizapp.model.Course
import com.example.quizapp.model.StudySet
import com.example.quizapp.network.response_model.ResponseHandlerState
import com.example.quizapp.ui.components.card.CustomCard
import com.example.quizapp.ui.navigation.Screen
import com.example.quizapp.ui.screens.hooks.ErrorScreen
import com.example.quizapp.ui.screens.hooks.LoadingScreen
import com.example.quizapp.ui.theme.quizappTheme


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TestHomeScreen(
    navController: NavHostController = rememberNavController(),
) {
    val homeViewModel = hiltViewModel<HomeViewModel>()
    val studySetList by homeViewModel.studySetList.collectAsState()
    val courseList by homeViewModel.courseList.collectAsState()
    val listState = rememberLazyListState()
    val isScrolledOnTop by remember {
        derivedStateOf { listState.firstVisibleItemScrollOffset == 0 }
    }
    Column {
        HomeTopBar(isScrolledOnTop)
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.surface)
        ) {
            item {
                when (studySetList) {
                    is ResponseHandlerState.Success -> {
                        StudySetList(
                            navController,
                            (studySetList as ResponseHandlerState.Success<List<StudySet>>).data
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
            item {
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
            item { TopCreaterList() }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TopCreaterList() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val lazyListState = rememberLazyListState()
        val snappingLayout =
            remember(lazyListState) { SnapLayoutInfoProvider(lazyListState) }
        val snapBehavior = rememberSnapFlingBehavior(snappingLayout)
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp),
        ) {
            Text(text = "Top Creators", style = MaterialTheme.typography.titleLarge)
            Text(
                text = "View all",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
        LazyRow(
            state = lazyListState,
            contentPadding = PaddingValues(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            flingBehavior = snapBehavior,
        ) {
            item {
                TopCreatorCard()
            }
            item {
                TopCreatorCard()
            }
            item {
                TopCreatorCard()
            }
        }

    }
}

@Composable
fun TopCreatorCard() {
    CustomCard(
        modifier = Modifier
            .width(200.dp)

    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            Row {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(40.dp, 40.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        )
                ) {
                    Text(
                        text = "Manh Hung".first().toString().uppercase(),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }

            }
            Row {
                Text(
                    text = "Manh Hung",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {

                Row(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.secondary,
                            shape = RoundedCornerShape(5.dp)
                        )
                        .padding(2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Article,
                        contentDescription = null,
                        modifier = Modifier.width(15.dp),
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                    Text(
                        text = "11 Study sets",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }

                Row(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.secondary,
                            shape = RoundedCornerShape(5.dp)
                        )
                        .padding(2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Class,
                        contentDescription = null,
                        modifier = Modifier.width(15.dp),
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                    Text(
                        text = "11 Courses", style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CourseList(
    navController: NavHostController,
    courseList: List<Course>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val lazyListState = rememberLazyListState()
        val snappingLayout =
            remember(lazyListState) { SnapLayoutInfoProvider(lazyListState) }
        val snapBehavior = rememberSnapFlingBehavior(snappingLayout)
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp),
        ) {
            Text(text = "Courses", style = MaterialTheme.typography.titleLarge)
            Text(
                text = "View all",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
        LazyRow(
            state = lazyListState,
            contentPadding = PaddingValues(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            flingBehavior = snapBehavior,
        ) {
            items(courseList) {
                CourseCard(navController, it)
            }
        }

    }
}

@Composable
fun CourseCard(
    navController: NavHostController,
    course: Course
) {
    CustomCard(
        modifier = Modifier
            .width(200.dp)

    ) {
        Row(modifier = Modifier.padding(10.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier.padding(bottom = 10.dp)
                ) {
                    Text(
                        modifier = Modifier.height(48.dp),
                        text = course.title,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,

                        )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(20.dp, 20.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = CircleShape
                                )
                        ) {
                            Text(
                                text = course.owner.name.first().toString().uppercase(),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                        Box(
                            modifier = Modifier.weight(3f)
                        ) {
                            Text(
                                text = course.owner.name,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
                Row {
                    Icon(
                        imageVector = Icons.Outlined.Article,
                        contentDescription = null,
                        modifier = Modifier.width(15.dp)
                    )
                    Text(
                        text = course.studySetCount.toString() + " Study sets",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                Row {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = null,
                        modifier = Modifier.width(15.dp)
                    )
                    Text(
                        text = course.enrollmentsCount.toString() + " Members",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
            Icon(imageVector = Icons.Outlined.School, contentDescription = null)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StudySetList(
    navController: NavHostController,
    studySets: List<StudySet>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val lazyListState = rememberLazyListState()
        val snappingLayout =
            remember(lazyListState) { SnapLayoutInfoProvider(lazyListState) }
        val snapBehavior = rememberSnapFlingBehavior(snappingLayout)
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp),
        ) {
            Text(text = "Sets", style = MaterialTheme.typography.titleLarge)
            Text(
                text = "View all",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
        LazyRow(
            state = lazyListState,
            contentPadding = PaddingValues(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            flingBehavior = snapBehavior,
        ) {
            items(studySets) {
                StudySetCard(navController, it)
            }
        }

    }
}

@Composable
fun StudySetCard(navController: NavHostController, studySet: StudySet) {
    CustomCard(
        onClick = { navController.navigate(Screen.StudySet.passId(studySet.id)) },
        modifier = Modifier
            .width(200.dp)

    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
        ) {
            AsyncImage(
                model = studySet.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 5.dp, bottom = 5.dp)
                    .background(
                        MaterialTheme.colorScheme.tertiary,
                        RoundedCornerShape(5.dp)
                    )
            ) {
                Text(
                    text = studySet.termNumber.toString() + " terms",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onTertiary,
                    modifier = Modifier.padding(3.dp)
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {

            Text(
                text = studySet.title,
                style = MaterialTheme.typography.titleMedium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                modifier = Modifier.height(48.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(20.dp, 20.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        )
                ) {
                    Text(
                        text = studySet.owner.name.first().toString().uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Box(
                    modifier = Modifier.weight(3f)
                ) {
                    Text(
                        text = studySet.owner.name,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(isScrollOnTop: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(10.dp)
    ) {
        if (isScrollOnTop) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = "notifications"
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth(),
                shape = CircleShape,
                placeholder = { Text(text = "Search sets, courses, creators...") },
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewTest() {
    quizappTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
        ) {
            TestHomeScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    val quizViewModel = hiltViewModel<QuizViewModel>()
    val quizUiState: QuizUiState = quizViewModel.quizUiState
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = backStackEntry?.destination?.route ?: Screen.Home.route

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (quizUiState) {
            is QuizUiState.Loading -> LoadingScreen(modifier = modifier)
            is QuizUiState.Success -> StudySetListScreen(
                studySets = quizUiState.studySets,
                onTermCardClicked = {
                    //viewModel.setCurrentSet(it)
                    navController.navigate(Screen.StudySet.passId(it.id))
                }
            )

            else -> ErrorScreen(modifier = modifier)
        }
    }


}

@Composable
fun StudySetListScreen(
    studySets: List<StudySet>,
    onTermCardClicked: (studySet: StudySet) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(studySets) {
            StudySetCard(
                studySet = it,
                onTermCardClicked = onTermCardClicked,
                modifier = Modifier
                    .padding(4.dp)
            )

        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudySetCard(
    studySet: StudySet,
    onTermCardClicked: (studySet: StudySet) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        onClick = { onTermCardClicked(studySet) },
        modifier = modifier
    ) {
        Row {
            if (studySet.imageUrl.isNotEmpty()) {
                AsyncImage(
                    model = studySet.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .width(150.dp)
                        .height(120.dp),
                    contentScale = ContentScale.Crop,
                )
            }
            Column(
                modifier = Modifier
                    .padding(4.dp)
                    .height(110.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = studySet.title, style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Text(text = studySet.description)
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(text = studySet.termNumber.toString() + " thuật ngữ", color = Color.Blue)
                }
            }

        }

    }
}
