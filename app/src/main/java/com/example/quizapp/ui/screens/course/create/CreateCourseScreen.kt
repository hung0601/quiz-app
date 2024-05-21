package com.example.quizapp.ui.screens.course.create

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quizapp.model.Course
import com.example.quizapp.network.response_model.ResponseHandlerState
import com.example.quizapp.ui.components.basic.textfield.CustomTextField
import com.example.quizapp.ui.navigation.Screen
import kotlinx.coroutines.launch

@Composable
fun CreateCourseScreen(navController: NavController) {
    val createCourseViewModel = hiltViewModel<CreateCourseViewModel>()
    val courseResponse by createCourseViewModel.courseResponse.collectAsState()
    val title = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(key1 = courseResponse is ResponseHandlerState.Success) {
        scope.launch {
            if (courseResponse is ResponseHandlerState.Success) {
                Toast.makeText(context, "Create collection successfully", Toast.LENGTH_LONG).show()
                navController.popBackStack()
                navController.navigate(Screen.Course.passId((courseResponse as ResponseHandlerState.Success<Course>).data.id))
            }
        }
    }

    LaunchedEffect(key1 = courseResponse is ResponseHandlerState.Error) {
        if (courseResponse is ResponseHandlerState.Error) {
            scope.launch {
                Toast.makeText(
                    context,
                    (courseResponse as ResponseHandlerState.Error).errorMsg,
                    Toast.LENGTH_LONG
                ).show()
                createCourseViewModel.resetState()
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        CustomTextField(value = title.value, onValueChange = { title.value = it },
            label = { Text(text = "Title") },
            placeholder = { Text(text = "Title") })
        CustomTextField(value = description.value, onValueChange = { description.value = it },
            label = { Text(text = "Description") },
            placeholder = { Text(text = "Description") })
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { createCourseViewModel.createCourse(title.value, description.value) },
                enabled = title.value.isNotEmpty() && description.value.isNotEmpty() || courseResponse is ResponseHandlerState.Loading
            ) {
                Text(text = "Create")
            }
        }
    }
}