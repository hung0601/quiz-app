package com.example.quizapp.ui.screens.set_detail

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Reviews
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.quizapp.R
import com.example.quizapp.constants.EDIT_ACCESS_LEVEL
import com.example.quizapp.constants.NONE_ACCESS_LEVEL
import com.example.quizapp.constants.OWNER_ACCESS_LEVEL
import com.example.quizapp.constants.PUBLIC_ACCESS
import com.example.quizapp.model.StudySetDetail
import com.example.quizapp.network.response_model.ResponseHandlerState
import com.example.quizapp.ui.components.basic.avatar.CircleAvatar
import com.example.quizapp.ui.components.basic.dialog.VoteDialog
import com.example.quizapp.ui.components.basic.star_review.StarReview
import com.example.quizapp.ui.components.basic.tag.CustomTag
import com.example.quizapp.ui.components.business.access_type.AccessType
import com.example.quizapp.ui.components.business.exam.ExamListDialog
import com.example.quizapp.ui.components.business.study_set.MemberListDialog
import com.example.quizapp.ui.components.business.term.TermItem
import com.example.quizapp.ui.navigation.Screen
import com.example.quizapp.ui.screens.hooks.ErrorScreen
import com.example.quizapp.ui.screens.hooks.LoadingScreen
import kotlinx.coroutines.launch

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


@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterialApi::class)
@Composable
fun DetailScreen(
    studySet: StudySetDetail,
    onClickStudy: () -> Unit,
    setDetailModel: SetDetailModel,
    navController: NavController
) {
    var expanded by remember { mutableStateOf(false) }
    var openExamListDialog by remember { mutableStateOf(false) }
    var openVoteDialog by remember { mutableStateOf(false) }
    var openMemberDialog by remember { mutableStateOf(false) }
    val voteResponse by setDetailModel.voteResponse.collectAsState()
    var isHideHeader by remember {
        mutableStateOf(false)
    }
    val termListState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val isLoading by setDetailModel.isLoading.collectAsState()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isLoading,
        onRefresh = setDetailModel::fetchData
    )

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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        PullRefreshIndicator(
            refreshing = isLoading,
            state = pullRefreshState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 50.dp)
                .zIndex(1000f)
        )

        Column(modifier = Modifier.fillMaxSize()) {
            if (openMemberDialog) {
                MemberListDialog(
                    onDismissRequest = { openMemberDialog = false },
                    studySetDetail = studySet,
                    navController = navController,
                    isOwner = studySet.permission == OWNER_ACCESS_LEVEL
                )
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
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {

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
                            if (isEditPermission(studySet.permission)) {
                                Box {
                                    IconButton(onClick = { expanded = true }) {
                                        Icon(
                                            Icons.Default.MoreVert,
                                            contentDescription = "Localized description"
                                        )
                                    }
                                    DropdownMenu(
                                        expanded = expanded,
                                        onDismissRequest = { expanded = false },
                                        modifier = Modifier.background(MaterialTheme.colorScheme.background)
                                    ) {
                                        DropdownMenuItem(
                                            text = { Text("Member list") },
                                            onClick = { expanded = false; openMemberDialog = true },
                                            leadingIcon = {
                                                Icon(
                                                    Icons.Outlined.Person,
                                                    contentDescription = null
                                                )
                                            })
                                        DropdownMenuItem(
                                            text = { Text("Edit set") },
                                            onClick = {
                                                navController.navigate(
                                                    Screen.EditStudySet.passId(
                                                        studySet.id
                                                    )
                                                )
                                            },
                                            leadingIcon = {
                                                Icon(
                                                    Icons.Outlined.Edit,
                                                    contentDescription = null
                                                )
                                            })
                                        DropdownMenuItem(
                                            text = { Text("Edit term") },
                                            onClick = {
                                                navController.navigate(
                                                    Screen.TermManagement.passId(
                                                        studySet.id
                                                    )
                                                )
                                            },
                                            leadingIcon = {
                                                Icon(
                                                    Icons.Outlined.EditNote,
                                                    contentDescription = null
                                                )
                                            })
                                    }
                                }
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
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
                                CustomTag(
                                    text = it.name,
                                    modifier = Modifier.padding(bottom = 5.dp)
                                )
                            }
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(15.dp),
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp),
                                modifier = Modifier.clickable {
                                    navController.navigate(Screen.Profile.passId(studySet.owner.id))
                                }
                            ) {
                                CircleAvatar(
                                    avatarImg = studySet.owner.imageUrl,
                                    name = studySet.owner.name,
                                    modifier = Modifier.size(20.dp, 20.dp)
                                )
                                Text(
                                    text = studySet.owner.name,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                            AccessType(accessType = studySet.accessType)
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
                        Text(text = "Terms", style = TextStyle(fontWeight = FontWeight.SemiBold))
                        Text(text = studySet.terms.size.toString())
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Favorite", style = TextStyle(fontWeight = FontWeight.SemiBold))
                        Text(text = "0")
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Mastered", style = TextStyle(fontWeight = FontWeight.SemiBold))
                        Text(text = "0")
                    }
                    if (isHideHeader) {
                        IconButton(
                            onClick = { isHideHeader = false },
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.KeyboardArrowRight,
                                contentDescription = null
                            )
                        }
                    } else {
                        IconButton(
                            onClick = { isHideHeader = true },
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.KeyboardArrowDown,
                                contentDescription = null
                            )
                        }
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
                LazyColumn(
                    modifier = Modifier.weight(1F),
                    state = termListState,
                ) {
                    item {
                        Text(
                            text = "Not studied",
                            style = TextStyle(fontWeight = FontWeight.SemiBold),
                            modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                        )
                    }
                    itemsIndexed(studySet.terms.filter { it.status == 0 }) { index, it ->
                        TermItem(term = it)
                    }
                    item {
                        Text(
                            text = "Still learning",
                            style = TextStyle(fontWeight = FontWeight.SemiBold),
                            modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                        )
                    }
                    itemsIndexed(studySet.terms.filter { it.status == 1 }) { index, it ->
                        TermItem(term = it)
                    }
                    item {
                        Text(
                            text = "Mastered", style = TextStyle(fontWeight = FontWeight.SemiBold),
                            modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
                        )
                    }
                    itemsIndexed(studySet.terms.filter { it.status == 2 }) { index, it ->
                        TermItem(term = it)
                    }
                }

                Divider(
                    modifier = Modifier.padding(bottom = 0.dp),
                    thickness = 1.dp,
                    color = Color.Gray
                )

            }

            if (!(studySet.permission == NONE_ACCESS_LEVEL && studySet.accessType != PUBLIC_ACCESS)) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = MaterialTheme.colorScheme.primary)
                            .padding(top = 5.dp)

                    ) {
                        SetMenuItem(
                            imageId = R.drawable.flash_card,
                            label = "Flash card",
                            onClick = {
                                onClickStudy()
                            },
                            enable = studySet.terms.isNotEmpty()
                        )
                        SetMenuItem(imageId = R.drawable.game_console, label = "Game", onClick = {
                            navController.navigate(Screen.MatchGame.passId(studySet.id))
                        }, enable = studySet.terms.size > 3)
                        SetMenuItem(imageId = R.drawable.study, label = "Learn", onClick = {
                            navController.navigate(Screen.Exam.passId(studySet.id))
                        }, enable = studySet.terms.size > 3)
                        SetMenuItem(imageId = R.drawable.exam, label = "Exam", onClick = {
                            openExamListDialog = true
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun SetMenuItem(
    @DrawableRes imageId: Int,
    label: String,
    onClick: () -> Unit,
    enable: Boolean = true
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable {
            if (enable) {
                onClick()
            }
        }
    ) {
        Image(
            painter = painterResource(id = imageId),
            contentDescription = null,
            modifier = Modifier.size(25.dp)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            color = if (enable) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.outlineVariant
        )
    }
}

fun isEditPermission(permission: Int): Boolean {
    return permission == EDIT_ACCESS_LEVEL || permission == OWNER_ACCESS_LEVEL
}
