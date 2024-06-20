package com.example.quizapp.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quizapp.R
import com.example.quizapp.model.Course
import com.example.quizapp.model.CreatorProfile
import com.example.quizapp.model.StudySet
import com.example.quizapp.network.response_model.ResponseHandlerState
import com.example.quizapp.ui.components.basic.avatar.CircleAvatar
import com.example.quizapp.ui.components.basic.card.FollowerCard
import com.example.quizapp.ui.components.basic.card.StudySetItem
import com.example.quizapp.ui.components.basic.tabs.CustomTab
import com.example.quizapp.ui.components.basic.tabs.TabList
import com.example.quizapp.ui.navigation.Screen
import com.example.quizapp.ui.screens.hooks.ErrorScreen
import com.example.quizapp.ui.screens.hooks.LoadingScreen
import com.example.quizapp.ui.screens.library.CourseList

@Composable
fun CreatorProfileScreen(navController: NavController) {
    val profileViewModel = hiltViewModel<CreatorProfileViewModel>()

    val userProfile by profileViewModel.userProfile.collectAsState()
    when (userProfile) {
        is ResponseHandlerState.Success -> {
            CreatorProfileDetail(
                profileViewModel = profileViewModel,
                navController = navController,
                userProfile = (userProfile as ResponseHandlerState.Success<CreatorProfile>).data
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
fun CreatorProfileDetail(
    profileViewModel: CreatorProfileViewModel,
    navController: NavController,
    userProfile: CreatorProfile
) {
    val studySets by profileViewModel.studySets.collectAsState()
    val courses by profileViewModel.courses.collectAsState()
    val followers by profileViewModel.followers.collectAsState()
    val subTextColor = Color(0xff586380)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Box(
            Modifier.height(240.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.profile_bg),
                contentDescription = null,
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp)),
                contentScale = ContentScale.Crop,
            )
            Box(
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircleAvatar(
                        avatarImg = userProfile.imageUrl,
                        name = userProfile.name,
                        modifier = Modifier
                            .size(60.dp)
                            .border(
                                3.dp,
                                MaterialTheme.colorScheme.background,
                                CircleShape
                            )
                    )
                    Text(
                        text = userProfile.name,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = userProfile.email,
                        style = MaterialTheme.typography.bodyLarge,
                        color = subTextColor
                    )
                    if (userProfile.isFollowing) {
                        Button(onClick = {
                            profileViewModel.unFollow(userProfile.id)
                        }) {
                            Text(text = "Unfollow", style = MaterialTheme.typography.titleSmall)
                        }
                    } else {
                        Button(onClick = {
                            profileViewModel.follow(userProfile.id)
                        }) {
                            Text(text = "Follow", style = MaterialTheme.typography.titleSmall)
                        }
                    }
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = userProfile.studySetsCount.toString(),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(text = "Study set", color = subTextColor)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = userProfile.followersCount.toString(),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(text = "Followers", color = subTextColor)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "123", style = MaterialTheme.typography.titleMedium)
                Text(text = "Following", color = subTextColor)
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
        val selectedTab = remember {
            mutableIntStateOf(1)
        }
        val listItems = listOf(
            TabList(1, "Study sets"),
            TabList(2, "Courses"),
            TabList(3, "Followers")
        )
        CustomTab(
            items = listItems,
            value = selectedTab.intValue,
            onChange = { selectedTab.intValue = it }
        )
        when (selectedTab.intValue) {
            1 -> {
                if (studySets is ResponseHandlerState.Success) {
                    StudySetList(
                        navController = navController,
                        studySets = (studySets as ResponseHandlerState.Success<List<StudySet>>).data
                    )
                }
            }

            2 -> {
                if (courses is ResponseHandlerState.Success) {
                    CourseList(
                        navController = navController,
                        courseList = (courses as ResponseHandlerState.Success<List<Course>>).data
                    )
                }
            }

            3 -> {
                if (followers is ResponseHandlerState.Success) {
                    FollowerList(
                        navController = navController,
                        userList = (followers as ResponseHandlerState.Success<List<CreatorProfile>>).data
                    )
                }
            }
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
fun FollowerList(
    navController: NavController,
    userList: List<CreatorProfile>
) {
    LazyColumn(
        contentPadding = PaddingValues(top = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(userList) { user ->
            FollowerCard(
                user = user,
                onClick = { navController.navigate(Screen.Profile.passId(it)) })
        }
    }
}