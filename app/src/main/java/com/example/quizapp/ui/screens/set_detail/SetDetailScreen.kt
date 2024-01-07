package com.example.quizapp.ui.screens.set_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.imageLoader
import coil.request.ImageRequest
import com.example.quizapp.model.StudySetDetail
import com.example.quizapp.model.Term
import com.example.quizapp.ui.screens.hooks.ErrorScreen
import com.example.quizapp.ui.screens.hooks.LoadingScreen

@Composable
fun SetDetailScreen(
    setDetailModel: SetDetailModel,
    onClickStudy: () -> Unit,
    navController: NavHostController
) {
    //val setDetailModel = navBackStackEntry.sharedViewModel<SetDetailModel>(navController)
    //setDetailModel: SetDetailModel,
    val studySetUiState: StudySetUiState = setDetailModel.uiState

    when (studySetUiState) {
        is StudySetUiState.Loading -> LoadingScreen(modifier = Modifier)
        is StudySetUiState.Success -> DetailScreen(studySetUiState.studySet, onClickStudy)
        else -> ErrorScreen(modifier = Modifier)
    }


}

@Composable
fun DetailScreen(studySet: StudySetDetail, onClickStudy: () -> Unit) {
    Column(modifier = Modifier.padding(8.dp)) {

        AsyncImage(
            model = studySet.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop,
        )
        Text(
            text = studySet.title, style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        )
        Text(text = studySet.description)

        Divider(modifier = Modifier.padding(top = 10.dp, start = 5.dp, end = 5.dp, bottom = 10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Thuật ngữ", style = TextStyle(fontWeight = FontWeight.SemiBold))
                Text(text = studySet.terms.size.toString())
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Yêu thích", style = TextStyle(fontWeight = FontWeight.SemiBold))
                Text(text = "0")
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Đã học", style = TextStyle(fontWeight = FontWeight.SemiBold))
                Text(text = "0")
            }
        }
        Divider(modifier = Modifier.padding(top = 10.dp, start = 5.dp, end = 5.dp, bottom = 10.dp))
        LazyColumn(modifier = Modifier.weight(1F)) {
            items(studySet.terms) {
                TermItem(term = it)
                Divider(
                    modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                    thickness = 1.dp,
                    color = Color.Gray
                )
            }
        }

        Divider(modifier = Modifier.padding(bottom = 0.dp), thickness = 1.dp, color = Color.Gray)
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxWidth()

            ) {
                Button(onClick = {}) {
                    Text(text = "Cancel")
                }
                Button(onClick = { onClickStudy() }) {
                    Text(text = "Study")
                }
            }
        }


    }
}

@Composable
fun TermItem(term: Term) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        val request: ImageRequest = ImageRequest.Builder(LocalContext.current.applicationContext)
            .data(term.imageUrl)
            .crossfade(true)
            .diskCacheKey(term.imageUrl)
            .build()

        LocalContext.current.applicationContext.imageLoader.enqueue(request)
        AsyncImage(
            model = request,
            contentDescription = null,
            modifier = Modifier
                .height(60.dp)
                .width(60.dp)
                .clip(RoundedCornerShape(4.dp)),
            contentScale = ContentScale.Crop,
        )
        Column(modifier = Modifier.padding(5.dp)) {
            Text(text = term.term, style = TextStyle(fontWeight = FontWeight.SemiBold))
            Text(text = term.definition)
        }
    }
}

