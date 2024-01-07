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


package com.example.quizapp.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.quizapp.ui.navigation.nav_graph.SetupNavGraph
import com.example.quizapp.ui.screens.BottomBar
import com.example.quizapp.ui.screens.QuizAppTopBar


@Composable
fun QuizApp() {
    val navController = rememberNavController()
    Scaffold(
        topBar = {
            QuizAppTopBar(navController = navController)
        },
        bottomBar = { BottomBar(navController = navController) }
    ) {
        SetupNavGraph(navController = navController, modifier = Modifier.padding(it))
    }


}

