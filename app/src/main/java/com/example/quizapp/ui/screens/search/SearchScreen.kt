package com.example.quizapp.ui.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.quizapp.model.Search
import com.example.quizapp.network.response_model.ResponseHandlerState
import com.example.quizapp.ui.screens.home.CourseList
import com.example.quizapp.ui.screens.home.StudySetList
import com.example.quizapp.ui.screens.home.TopCreatesList
import com.example.quizapp.ui.screens.hooks.ErrorScreen
import com.example.quizapp.ui.screens.hooks.LoadingScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavHostController) {
    val searchViewModel = hiltViewModel<SearchViewModel>()
    val searchResult by searchViewModel.searchResult.collectAsState()
    val listState = rememberLazyListState()
    val search = remember { mutableStateOf("") }
    val isScrolledOnTop by remember {
        derivedStateOf { listState.firstVisibleItemScrollOffset == 0 }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            OutlinedTextField(
                value = search.value,
                onValueChange = { search.value = it },
                modifier = Modifier
                    .weight(1f),
                shape = CircleShape,
                placeholder = { Text(text = "Search sets, courses, creators...") },
            )
            IconButton(
                onClick = { searchViewModel.getResults(search.value) },
                enabled = search.value.isNotEmpty()
            ) {
                Icon(imageVector = Icons.Outlined.Search, contentDescription = null)
            }
        }



        Column {
            when (searchResult) {
                is ResponseHandlerState.Success -> {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        item {
                            StudySetList(
                                navController,
                                (searchResult as ResponseHandlerState.Success<Search>).data.studySets
                            )
                        }
                        item {
                            CourseList(
                                navController,
                                (searchResult as ResponseHandlerState.Success<Search>).data.courses
                            )
                        }
                        item {
                            TopCreatesList(
                                navController,
                                (searchResult as ResponseHandlerState.Success<Search>).data.creators
                            )
                        }
                    }

                }

                is ResponseHandlerState.Loading -> {
                    LoadingScreen()
                }

                is ResponseHandlerState.Init -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "Input to search")
                    }
                }

                else -> {
                    ErrorScreen()
                }

            }

        }

    }
}
