package com.example.quizapp.ui.components.business.study_set

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.RemoveRedEye
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quizapp.constants.VIEW_ACCESS_LEVEL
import com.example.quizapp.model.Member
import com.example.quizapp.model.MyProfile
import com.example.quizapp.model.StudySetDetail
import com.example.quizapp.network.response_model.ResponseHandlerState
import com.example.quizapp.ui.components.basic.avatar.CircleAvatar
import com.example.quizapp.ui.components.basic.card.CustomCard
import com.example.quizapp.ui.components.basic.card.MemberItem
import com.example.quizapp.ui.components.basic.select.DynamicSelectTextField
import com.example.quizapp.ui.components.basic.textfield.CustomTextField
import com.example.quizapp.ui.screens.hooks.LoadingScreen
import com.example.quizapp.ui.theme.md_theme_error
import kotlinx.coroutines.launch

@Composable
fun MemberListDialog(
    onDismissRequest: () -> Unit,
    studySetDetail: StudySetDetail,
    navController: NavController,
    isOwner: Boolean = false,
) {
    val memberViewModel = hiltViewModel<MemberViewModel>()
    val memberList by memberViewModel.memberList.collectAsState()
    val searchUsers by memberViewModel.searchUsers.collectAsState()
    val inviteResponse by memberViewModel.inviteResponse.collectAsState()
    val search = remember { mutableStateOf("") }
    val isSearch = remember {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(key1 = inviteResponse is ResponseHandlerState.Success) {
        scope.launch {
            if (inviteResponse is ResponseHandlerState.Success) {
                Toast.makeText(context, "invite successful", Toast.LENGTH_LONG).show()
                memberViewModel.resetState()
                memberViewModel.searchUsers(studySetDetail.id, search.value)
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
                memberViewModel.resetState()
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
                Text(text = "Study set members", style = MaterialTheme.typography.titleLarge)
            }
            if (isSearch.value) {
                CustomTextField(value = search.value, onValueChange = {
                    search.value = it
                    memberViewModel.searchUsers(studySetDetail.id, search.value)
                },
                    placeholder = { Text(text = "Invite member") },
                    trailingIcon = {
                        IconButton(onClick = {
                            isSearch.value = false
                            memberViewModel.getSetMember()
                        }) {
                            Icon(imageVector = Icons.Outlined.Close, contentDescription = null)
                        }
                    }
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                if (isSearch.value) {
                    if (searchUsers is ResponseHandlerState.Success) {
                        InviteMemberList(
                            navController = navController,
                            members = (searchUsers as ResponseHandlerState.Success<List<MyProfile>>).data,
                            onInvite = { userId, accessLevel ->
                                memberViewModel.inviteMember(studySetDetail.id, userId, accessLevel)
                            }
                        )
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Members in this set",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(end = 10.dp)
                        )
                        if (isOwner) {
                            IconButton(onClick = { isSearch.value = true }) {
                                Icon(
                                    imageVector = Icons.Outlined.PersonAdd,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                    when (memberList) {
                        is ResponseHandlerState.Success -> {
                            StudySetMemberList(
                                navController = navController,
                                members = (memberList as ResponseHandlerState.Success<List<Member>>).data,
                                memberViewModel,
                                isOwner = isOwner
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
fun StudySetMemberList(
    navController: NavController,
    members: List<Member>,
    memberViewModel: MemberViewModel,
    isOwner: Boolean = false,
) {
    LazyColumn(
        contentPadding = PaddingValues(top = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(members) {
            Box {
                SetMemberItem(
                    member = it,
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    IconButton(
                        onClick = { },
                    ) {
                        if (it.accessLevel == VIEW_ACCESS_LEVEL) {
                            Icon(
                                imageVector = Icons.Outlined.RemoveRedEye,
                                contentDescription = null
                            )
                        } else {
                            Icon(imageVector = Icons.Outlined.EditNote, contentDescription = null)
                        }
                    }
                    if (isOwner) {
                        IconButton(
                            onClick = { memberViewModel.deleteMember(it.id) },
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.DeleteOutline,
                                contentDescription = null,
                                tint = md_theme_error
                            )
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun InviteMemberList(
    navController: NavController,
    members: List<MyProfile>,
    onInvite: (Int, Int) -> Unit
) {

    LazyColumn(
        contentPadding = PaddingValues(top = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(members) {
            MemberItem(
                member = it,
                suffix = {
                    InviteForm(onClick = { accessLevel ->
                        onInvite(it.id, accessLevel)
                    }, modifier = Modifier)
                }
            )
        }
    }
}

@Composable
fun InviteForm(
    modifier: Modifier = Modifier,
    onClick: (accessLevel: Int) -> Unit = {},
) {
    val listItem = listOf("Viewer", "Editor")
    val selectedIndex = remember {
        mutableIntStateOf(0)
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        DynamicSelectTextField(
            options = listItem,
            selectedValue = listItem[selectedIndex.intValue],
            onValueChangedEvent = { selectedIndex.intValue = it },
            modifier = Modifier.width(90.dp)
        )
        IconButton(
            onClick = { onClick(selectedIndex.intValue) },
            modifier = Modifier
        ) {
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = null,
            )
        }
    }
}

@Composable
fun SetMemberItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    member: Member,
) {
    CustomCard(modifier = modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
        ) {
            CircleAvatar(
                avatarImg = member.imageUrl,
                modifier = Modifier.size(30.dp, 30.dp),
                name = member.name
            )

            Column {
                Text(text = member.name, style = MaterialTheme.typography.bodyLarge)
                Text(
                    text = member.email,
                    style = MaterialTheme.typography.labelLarge,
                )
            }

        }
    }
}