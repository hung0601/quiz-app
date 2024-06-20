package com.example.quizapp.ui.screens.course.detail

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Article
import androidx.compose.material.icons.outlined.GroupAdd
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quizapp.model.CourseDetail
import com.example.quizapp.model.Enrollment
import com.example.quizapp.model.MyProfile
import com.example.quizapp.model.StudySet
import com.example.quizapp.network.response_model.ResponseHandlerState
import com.example.quizapp.ui.components.basic.avatar.CircleAvatar
import com.example.quizapp.ui.components.basic.card.MemberItem
import com.example.quizapp.ui.components.basic.card.StudySetItem
import com.example.quizapp.ui.components.basic.textfield.CustomTextField
import com.example.quizapp.ui.navigation.Screen
import com.example.quizapp.ui.screens.hooks.ErrorScreen
import com.example.quizapp.ui.screens.hooks.LoadingScreen
import kotlinx.coroutines.launch

@Composable
fun CourseDetailScreen(navController: NavController) {
    val courseViewModel = hiltViewModel<CourseViewModel>()
    val courseDetail by courseViewModel.courseDetail.collectAsState()
    when (courseDetail) {
        is ResponseHandlerState.Success -> {
            CourseDetail(
                navController = navController,
                course = (courseDetail as ResponseHandlerState.Success<CourseDetail>).data,
                courseViewModel = courseViewModel
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

@Composable
fun CourseDetail(
    navController: NavController,
    course: CourseDetail,
    courseViewModel: CourseViewModel
) {
    var tabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Study Sets", "Members")
    var expanded by remember { mutableStateOf(false) }
    var openAddsetDialog by remember { mutableStateOf(false) }
    var openInviteDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(10.dp)
    ) {
        if (openAddsetDialog) {
            AddSetDialog(onDismissRequest = { openAddsetDialog = false },
                course, navController,
                onAddedSet = { courseViewModel.getCourse() })
        }
        if (openInviteDialog) {
            InviteDialog(onDismissRequest = { openInviteDialog = false },
                course, navController,
                onAddedSet = {})
        }
        Row {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = course.title, style = MaterialTheme.typography.titleLarge)
                Text(
                    text = course.studySets.size.toString() + " sets",
                    style = MaterialTheme.typography.labelLarge
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier.padding(top = 10.dp)
                ) {
                    CircleAvatar(
                        avatarImg = course.owner.imageUrl,
                        name = course.owner.name,
                        modifier = Modifier.size(20.dp, 20.dp)
                    )
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

            if (courseViewModel.currentUser != null && courseViewModel.currentUser.id == course.owner.id) {
                Box {
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Localized description")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background(MaterialTheme.colorScheme.background)
                    ) {
                        DropdownMenuItem(
                            text = { Text("Add set") },
                            onClick = { expanded = false; openAddsetDialog = true },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.Article,
                                    contentDescription = null
                                )
                            })
                        DropdownMenuItem(
                            text = { Text("Invite member") },
                            onClick = { expanded = false; openInviteDialog = true },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.Person,
                                    contentDescription = null
                                )
                            })
                    }
                }
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
            0 -> StudySetList(navController = navController, studySets = course.studySets)
            1 -> MemberList(navController = navController, members = course.enrollments)
        }
    }
}

@Composable
fun StudySetList(
    navController: NavController,
    studySets: List<StudySet>
) {
    LazyColumn(
        contentPadding = PaddingValues(top = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(studySets) {
            StudySetItem(
                onClick = { navController.navigate(Screen.StudySet.passId(it.id)) },
                studySet = it
            )
        }
    }
}

@Composable
fun MemberList(
    navController: NavController,
    members: List<Enrollment>
) {
    LazyColumn(
        contentPadding = PaddingValues(top = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(members) {
            MemberItem(member = it.user)
        }
    }
}

@Composable
fun UserSearchList(
    navController: NavController,
    members: List<MyProfile>,
    searchUserViewModel: SearchUserViewModel,
    course: CourseDetail
) {
    LazyColumn(
        contentPadding = PaddingValues(top = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(members) {
            Box {
                MemberItem(
                    member = it,
                )
                IconButton(
                    onClick = { searchUserViewModel.inviteMember(course.id, it.id) },
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(imageVector = Icons.Outlined.GroupAdd, contentDescription = null)
                }
            }

        }
    }
}


@Composable
fun InviteDialog(
    onDismissRequest: () -> Unit,
    course: CourseDetail,
    navController: NavController,
    onAddedSet: () -> Unit
) {
    val searchUserViewModel = hiltViewModel<SearchUserViewModel>()
    val searchUsers by searchUserViewModel.searchUsers.collectAsState()
    val inviteResponse by searchUserViewModel.inviteResponse.collectAsState()
    val search = remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(key1 = inviteResponse is ResponseHandlerState.Success) {
        scope.launch {
            if (inviteResponse is ResponseHandlerState.Success) {
                Toast.makeText(context, "invite successful", Toast.LENGTH_LONG).show()
                searchUserViewModel.resetState()
                searchUserViewModel.searchCourseUsers(course.id, search.value)
            }
        }
    }

    LaunchedEffect(key1 = inviteResponse is ResponseHandlerState.Error) {
        if (inviteResponse is ResponseHandlerState.Error) {
            scope.launch {
                Toast.makeText(
                    context,
                    (inviteResponse as ResponseHandlerState.Error).errorMsg,
                    Toast.LENGTH_LONG
                ).show()
                searchUserViewModel.resetState()
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
                .padding(20.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text(text = "Invite member", style = MaterialTheme.typography.titleLarge)
            }
            CustomTextField(value = search.value, onValueChange = {
                search.value = it
                searchUserViewModel.searchCourseUsers(course.id, search.value);
            },
                placeholder = { Text(text = "Input to search") })
            Row(
                modifier = Modifier
                    .weight(1f)
            ) {
                when (searchUsers) {
                    is ResponseHandlerState.Success -> {
                        UserSearchList(
                            navController = navController,
                            members = (searchUsers as ResponseHandlerState.Success<List<MyProfile>>).data,
                            searchUserViewModel,
                            course
                        )
                    }

                    is ResponseHandlerState.Loading -> {
                        LoadingScreen()
                    }

                    else -> {
                        Row(horizontalArrangement = Arrangement.Center) {
                            Text(text = "Search member by email")
                        }
                    }

                }
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
                    onClick = { },
                    enabled = true
                ) {
                    Text(text = "Done")
                }
            }
        }

    }
}

@Composable
fun AddSetDialog(
    onDismissRequest: () -> Unit,
    course: CourseDetail,
    navController: NavController,
    onAddedSet: () -> Unit
) {
    val createdSetViewModel = hiltViewModel<CreatedSetViewModel>()
    val setList by createdSetViewModel.setList.collectAsState()
    val addSetResponse by createdSetViewModel.addSetResponse.collectAsState()
    val selectedSets = remember { mutableStateListOf<Int>() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(key1 = addSetResponse is ResponseHandlerState.Success) {
        scope.launch {
            if (addSetResponse is ResponseHandlerState.Success) {
                Toast.makeText(context, "Add sets successful", Toast.LENGTH_LONG).show()
                createdSetViewModel.resetState()
                createdSetViewModel.getSetList()
                onDismissRequest()
                onAddedSet()
            }
        }
    }

    LaunchedEffect(key1 = addSetResponse is ResponseHandlerState.Error) {
        if (addSetResponse is ResponseHandlerState.Error) {
            scope.launch {
                Toast.makeText(
                    context,
                    (addSetResponse as ResponseHandlerState.Error).errorMsg,
                    Toast.LENGTH_LONG
                ).show()
                createdSetViewModel.resetState()
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
                Text(text = "Add set", style = MaterialTheme.typography.titleLarge)
            }
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(10.dp)
            ) {
                when (setList) {
                    is ResponseHandlerState.Success -> {
                        CreatedSetList(
                            studySets = (setList as ResponseHandlerState.Success<List<StudySet>>).data,
                            selectedSets,
                            onItemSelect = {
                                if (selectedSets.contains(it)) selectedSets.remove(it) else selectedSets.add(
                                    it
                                )
                            },
                            onCreateSetClick = {
                                navController.navigate(
                                    Screen.CreateStudySet.passCourseId(
                                        course.id
                                    )
                                )
                            }
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
                    onClick = { createdSetViewModel.addSets(course.id, selectedSets) },
                    enabled = selectedSets.isNotEmpty() && addSetResponse !is ResponseHandlerState.Loading
                ) {
                    Text(text = "Done")
                }
            }
        }

    }
}

@Composable
fun CreatedSetList(
    studySets: List<StudySet>,
    selectedSets: List<Int>,
    onItemSelect: (Int) -> Unit,
    onCreateSetClick: () -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(top = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        item {
            TextButton(onClick = { onCreateSetClick() }) {
                Text(
                    text = "+ Create a new set",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        items(studySets) {
            StudySetItem(
                onClick = { onItemSelect(it.id) },
                studySet = it,
                modifier = if (selectedSets.contains(it.id)) Modifier.border(
                    2.dp,
                    MaterialTheme.colorScheme.primary,
                    RoundedCornerShape(15.dp)
                ) else Modifier
            )
        }
    }
}