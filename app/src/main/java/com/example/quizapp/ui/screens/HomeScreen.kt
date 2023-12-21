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
package com.example.quizapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.quizapp.R
import com.example.quizapp.model.StudySet
import com.example.quizapp.ui.screens.quizstudy.QuizStudyScreen
import com.example.quizapp.ui.screens.termdetail.TermDetail
import com.example.quizapp.ui.screens.termdetail.TermViewModel


enum class QuizScreen(val title: String) {
    Home(title = "Quiz list"),
    QuizDetail(title = "Quiz detail"),
    QuizLearn(title = "Study"),
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    quizUiState: QuizUiState, navController: NavHostController = rememberNavController(), modifier: Modifier = Modifier
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = QuizScreen.valueOf(
        backStackEntry?.destination?.route ?: QuizScreen.Home.name
    )
    Scaffold(
        topBar = {
            QuizAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->
        when (quizUiState) {
            is QuizUiState.Loading -> LoadingScreen( modifier = modifier)
            is QuizUiState.Success -> NavigationHost(studySets = quizUiState.studySets,navController= navController, innerPadding = innerPadding,modifier= modifier)
            else -> ErrorScreen(modifier = modifier)
        }
    }
}

@Composable
fun NavigationHost(viewModel: TermViewModel = viewModel(), studySets: List<StudySet>, navController: NavHostController, innerPadding: PaddingValues, modifier: Modifier = Modifier){

    val uiState by viewModel.uiState.collectAsState()
    NavHost(
        navController = navController,
        startDestination = QuizScreen.Home.name,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable(route = QuizScreen.Home.name) {
            StudySetListScreen(studySets= studySets,
                onTermCardClicked = {
                    viewModel.setCurrentSet(it)
                    navController.navigate(QuizScreen.QuizDetail.name)
            }, modifier=modifier)
        }
        composable(route = QuizScreen.QuizDetail.name) {
            TermDetail(uiState.currentSet!!,navController)
        }
        composable(route = QuizScreen.QuizLearn.name) {
            QuizStudyScreen(studySet = uiState.currentSet!!)
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizAppBar(
    currentScreen: QuizScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {Text(text = currentScreen.title)},
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back button"
                    )
                }
            }
        }
    )
}
@Composable
fun StudySetListScreen(studySets: List<StudySet>,onTermCardClicked:(studySet: StudySet) -> Unit ,modifier: Modifier = Modifier) {
    LazyColumn() {
        items(studySets) {
            StudySetCard(
                studySet = it,
                onTermCardClicked=onTermCardClicked,
                modifier = Modifier
                    .padding(4.dp)
            )

        }
    }
   
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudySetCard(studySet: StudySet,onTermCardClicked:(studySet: StudySet) -> Unit , modifier: Modifier = Modifier) {
    OutlinedCard(
        onClick = { onTermCardClicked(studySet)},
        modifier= modifier
    )  {
        Row {
            if(studySet.imageUrl.isNotEmpty()) {
                AsyncImage(
                    model = studySet.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .width(150.dp)
                        .height(120.dp),
                    contentScale = ContentScale.Crop,
                )
            }
            Column(modifier = Modifier
                .padding(4.dp)
                .height(110.dp)
                .fillMaxWidth()) {
                Text(text = studySet.title, style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                ))
                Text(text = studySet.description)
                Row(modifier=Modifier.fillMaxSize(), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.Bottom) {
                    Text(text = studySet.terms.size.toString()+" thuật ngữ", color = Color.Blue)
                }
            }

        }

    }
}

/**
 * The home screen displaying the loading message.
 */
@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}

/**
 * The home screen displaying error message with re-attempt button.
 */
@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
    }
}

/**
 * ResultScreen displaying number of photos retrieved.
 */


//@Preview(showBackground = true)
//@Composable
//fun StudySetListScreenPreview() {
//    val studySets : List<StudySet> = listOf(StudySet(1,"test","none","http://10.0.2.2:8000/storage/study_sets/set1.jpg",null,null))
//    StudySetListScreen(studySets = studySets, modifier = Modifier)
//}
