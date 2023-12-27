package com.example.quizapp


import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.example.quizapp.network.QuizApiService
import com.example.quizapp.ui.theme.quizappTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var quizApiService: QuizApiService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GlobalScope.launch {
            var response = quizApiService.getStudySets()
            Log.d("RESULT", response.toString())
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            quizappTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(5.dp),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    QuizApp()
                    Text(text = "jha")
                }
            }
        }
    }
}
