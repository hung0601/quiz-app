package com.example.quizapp.ui.screens.set_detail

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Reviews
import androidx.compose.material.icons.outlined.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.quizapp.model.StudySetDetail
import com.example.quizapp.network.response_model.ResponseHandlerState
import com.example.quizapp.ui.components.basic.avatar.CircleAvatar
import com.example.quizapp.ui.components.basic.dialog.VoteDialog
import com.example.quizapp.ui.components.basic.star_review.StarReview
import com.example.quizapp.ui.components.basic.tag.CustomTag
import com.example.quizapp.ui.components.basic.textfield.CustomTextField
import com.example.quizapp.ui.components.business.exam.ExamListDialog
import com.example.quizapp.ui.components.business.term.TermItem
import com.example.quizapp.ui.navigation.Screen
import com.example.quizapp.ui.screens.hooks.ErrorScreen
import com.example.quizapp.ui.screens.hooks.LoadingScreen
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun SetDetailScreen(
    setDetailModel: SetDetailModel,
    onClickStudy: () -> Unit,
    navController: NavHostController
) {
    val studySetUiState: StudySetUiState = setDetailModel.uiState

    when (studySetUiState) {
        is StudySetUiState.Loading -> LoadingScreen(modifier = Modifier)
        is StudySetUiState.Success -> DetailScreen(
            studySetUiState.studySet,
            onClickStudy,
            setDetailModel,
            navController
        )

        else -> ErrorScreen(modifier = Modifier)
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DetailScreen(
    studySet: StudySetDetail,
    onClickStudy: () -> Unit,
    setDetailModel: SetDetailModel,
    navController: NavController
) {
    var openAddTermDialog by remember { mutableStateOf(false) }
    var openExamListDialog by remember { mutableStateOf(false) }
    var openVoteDialog by remember { mutableStateOf(false) }
    val voteResponse by setDetailModel.voteResponse.collectAsState()
    var isHideHeader by remember {
        mutableStateOf(false)
    }
    val termListState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = termListState.firstVisibleItemScrollOffset) {
        if (termListState.firstVisibleItemScrollOffset > 0) isHideHeader = true
    }
    LaunchedEffect(key1 = voteResponse is ResponseHandlerState.Success) {
        scope.launch {
            if (voteResponse is ResponseHandlerState.Success) {
                setDetailModel.updateVote((voteResponse as ResponseHandlerState.Success<Float>).data)
            }
        }
    }
    Column(modifier = Modifier.padding(8.dp)) {
        if (openAddTermDialog) {
            AddTermDialog(onDismissRequest = { openAddTermDialog = false },
                studySet,
                onAddedTerm = { setDetailModel.getStudySet() })
        }
        if (openExamListDialog) {
            ExamListDialog(
                onConfirmation = { openExamListDialog = false },
                examList = studySet.exams,
                onSelect = {
                    navController.navigate(Screen.CustomExam.passId(it))
                }
            )
        }
        if (openVoteDialog) {
            VoteDialog(onVote = {
                openVoteDialog = false
                setDetailModel.sendVote(it)
            }, onCancel = { openVoteDialog = false })
        }
        AnimatedVisibility(
            visible = !isHideHeader,
        ) {
            Column {
                if (studySet.imageUrl != null) {
                    AsyncImage(
                        model = studySet.imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .height(200.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop,
                    )
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = studySet.title, style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        Text(text = studySet.description)
                    }
                    if (setDetailModel.currentUser != null && setDetailModel.currentUser.id == studySet.owner.id) {
                        IconButton(onClick = { openAddTermDialog = true }) {
                            Icon(
                                imageVector = Icons.Outlined.Add,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.tertiary
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StarReview(
                        star = studySet.votesAvgStar ?: 0F,
                        size = 15,
                        disable = true,
                        isShowText = true
                    )
                    IconButton(
                        onClick = { openVoteDialog = true },
                        modifier = Modifier.height(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Reviews,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
                FlowRow(
                    modifier = Modifier.padding(top = 5.dp),
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                ) {
                    studySet.topics.map {
                        CustomTag(text = it.name, modifier = Modifier.padding(bottom = 5.dp))
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier.padding(top = 10.dp)
                ) {
                    CircleAvatar(
                        avatarImg = studySet.owner.imageUrl,
                        name = studySet.owner.name,
                        modifier = Modifier.size(20.dp, 20.dp)
                    )
                    Box(
                        modifier = Modifier.weight(3f)
                    ) {
                        Text(
                            text = studySet.owner.name,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
                Divider(
                    modifier = Modifier.padding(
                        top = 10.dp,
                        start = 5.dp,
                        end = 5.dp,
                        bottom = 10.dp
                    )
                )
            }
        }
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
            if (isHideHeader) {
                IconButton(
                    onClick = { isHideHeader = false },
                ) {
                    Icon(imageVector = Icons.Outlined.KeyboardArrowRight, contentDescription = null)
                }
            } else {
                IconButton(
                    onClick = { isHideHeader = true },
                ) {
                    Icon(imageVector = Icons.Outlined.KeyboardArrowDown, contentDescription = null)
                }
            }
        }
        Divider(modifier = Modifier.padding(top = 10.dp, start = 5.dp, end = 5.dp, bottom = 10.dp))
        LazyColumn(
            modifier = Modifier.weight(1F),
            state = termListState
        ) {
            item {
                Text(
                    text = "Not studied",
                    style = TextStyle(fontWeight = FontWeight.SemiBold),
                    modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
                )
            }
            itemsIndexed(studySet.terms.filter { it.status == 0 }) { index, it ->
                TermItem(term = it)
                if (index != studySet.terms.filter { it.status == 0 }.lastIndex) {
                    Divider(
                        modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                        thickness = 1.dp,
                        color = Color.Gray
                    )
                }
            }
            item {
                Text(
                    text = "Still learning",
                    style = TextStyle(fontWeight = FontWeight.SemiBold),
                    modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
                )
            }
            itemsIndexed(studySet.terms.filter { it.status == 1 }) { index, it ->
                TermItem(term = it)
                if (index != studySet.terms.filter { it.status == 1 }.lastIndex) {
                    Divider(
                        modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                        thickness = 1.dp,
                        color = Color.Gray
                    )
                }
            }
            item {
                Text(
                    text = "Mastered", style = TextStyle(fontWeight = FontWeight.SemiBold),
                    modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
                )
            }
            itemsIndexed(studySet.terms.filter { it.status == 2 }) { index, it ->
                TermItem(term = it)
                if (index != studySet.terms.filter { it.status == 2 }.lastIndex) {
                    Divider(
                        modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                        thickness = 1.dp,
                        color = Color.Gray
                    )
                }
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
                Button(
                    onClick = { onClickStudy() },
                    enabled = studySet.terms.isNotEmpty()
                ) {
                    Text(text = "Flashcard")
                }
                Button(
                    onClick = {
                        navController.navigate(Screen.Exam.passId(studySet.id))
                    },
                    enabled = studySet.terms.size >= 5
                ) {
                    Text(text = "Learn")
                }
                Button(
                    onClick = {
                        openExamListDialog = true
                    },
                    enabled = studySet.terms.size >= 5
                ) {
                    Text(text = "Exam")
                }
            }
        }


    }
}


@Composable
fun AddTermDialog(
    onDismissRequest: () -> Unit,
    studySet: StudySetDetail,
    onAddedTerm: () -> Unit
) {
    val term = remember { mutableStateOf("") }
    val definition = remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
        }

    val scope = rememberCoroutineScope()
    val addTermViewModel = hiltViewModel<AddTermViewModel>()
    val addTermResponse by addTermViewModel.addTermResponse.collectAsState()


    LaunchedEffect(key1 = addTermResponse is ResponseHandlerState.Success) {
        scope.launch {
            if (addTermResponse is ResponseHandlerState.Success) {
                Toast.makeText(context, "Add term successful", Toast.LENGTH_LONG).show()
                addTermViewModel.resetState()
                onDismissRequest()
                onAddedTerm()
            }
        }
    }

    LaunchedEffect(key1 = addTermResponse is ResponseHandlerState.Error) {
        if (addTermResponse is ResponseHandlerState.Error) {
            scope.launch {
                Toast.makeText(
                    context,
                    (addTermResponse as ResponseHandlerState.Error).errorMsg,
                    Toast.LENGTH_LONG
                ).show()
                addTermViewModel.resetState()
            }
        }
    }
    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text(text = "Add term", style = MaterialTheme.typography.titleLarge)
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                if (imageUri != null) {
                    imageUri?.let {
                        if (Build.VERSION.SDK_INT < 28) {
                            bitmap.value = MediaStore.Images
                                .Media.getBitmap(context.contentResolver, it)
                        } else {
                            val source = ImageDecoder.createSource(context.contentResolver, it)
                            bitmap.value = ImageDecoder.decodeBitmap(source)
                        }

                        bitmap.value?.let { btm ->
                            Image(
                                bitmap = btm.asImageBitmap(),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp)
                                    .clip(RoundedCornerShape(15.dp))
                                    .border(
                                        2.dp, MaterialTheme.colorScheme.outlineVariant,
                                        RoundedCornerShape(15.dp)
                                    )
                                    .clickable {
                                        launcher.launch("image/*")
                                    },
                            )
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .background(MaterialTheme.colorScheme.surface)
                            .border(
                                2.dp, MaterialTheme.colorScheme.outlineVariant,
                                RoundedCornerShape(15.dp)
                            )
                            .clickable {
                                launcher.launch("image/*")
                            },
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(imageVector = Icons.Outlined.Upload, contentDescription = null)
                        Text(text = "Pick Image")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))


                CustomTextField(value = term.value, onValueChange = { term.value = it },
                    label = { Text(text = "Term") },
                    placeholder = { Text(text = "Term") })
                CustomTextField(value = definition.value,
                    onValueChange = { definition.value = it },
                    label = { Text(text = "Definition") },
                    placeholder = { Text(text = "Definition") })


            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(onClick = { onDismissRequest() }) {
                    Text(text = "Cancel")
                }
                Button(
                    onClick = {
                        val inputStream = context.contentResolver.openInputStream(imageUri!!)
                        val file = File(context.cacheDir, "mage.png")
                        file.createNewFile()
                        file.outputStream().use {
                            inputStream!!.copyTo(it)
                        }
                        addTermViewModel.createSet(file, term.value, definition.value, studySet.id)
                    },
                    enabled = imageUri != null && term.value.isNotEmpty() && definition.value.isNotEmpty() || addTermResponse is ResponseHandlerState.Loading
                ) {
                    Text(text = "Add")
                }
            }
        }

    }
}
