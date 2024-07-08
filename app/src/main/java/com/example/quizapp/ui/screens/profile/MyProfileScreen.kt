package com.example.quizapp.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.example.quizapp.model.CreatorProfile
import com.example.quizapp.model.MyProfile
import com.example.quizapp.network.response_model.ResponseHandlerState
import com.example.quizapp.ui.components.basic.avatar.CircleAvatar
import com.example.quizapp.ui.components.basic.tabs.CustomTab
import com.example.quizapp.ui.components.basic.tabs.TabList
import com.example.quizapp.ui.navigation.AUTH_GRAPH_ROUTE
import com.example.quizapp.ui.screens.UserViewModel
import com.example.quizapp.ui.screens.hooks.LoadingScreen

@Composable
fun MyProfileScreen(navController: NavController) {
    val userViewModel = hiltViewModel<UserViewModel>()
    val profileViewModel = hiltViewModel<ProfileViewModel>()
    val myProfile by profileViewModel.userProfile.collectAsState()
    val followers by profileViewModel.followers.collectAsState()
    val followings by profileViewModel.followings.collectAsState()

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
            if (myProfile is ResponseHandlerState.Success) {
                (myProfile as ResponseHandlerState.Success<MyProfile>).data.let {
                    Box(
                        modifier = Modifier.align(Alignment.BottomCenter)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircleAvatar(
                                avatarImg = it.imageUrl,
                                name = it.name,
                                modifier = Modifier
                                    .size(60.dp)
                                    .border(
                                        3.dp,
                                        MaterialTheme.colorScheme.background,
                                        CircleShape
                                    )
                            )
                            Text(
                                text = it.name,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = it.email,
                                style = MaterialTheme.typography.bodyLarge,
                                color = subTextColor
                            )
                            Button(onClick = {
                                userViewModel.clearSession();
                                navController.navigate(AUTH_GRAPH_ROUTE)
                            }) {
                                Text(text = "Logout", style = MaterialTheme.typography.titleSmall)
                            }
                        }
                    }
                }

            } else {
                LoadingScreen()
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
        if (myProfile is ResponseHandlerState.Success) {
            (myProfile as ResponseHandlerState.Success<MyProfile>).data.let {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = it.studySetsCount.toString(),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(text = "Study set", color = subTextColor)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = it.followersCount.toString(),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(text = "Followers", color = subTextColor)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = it.followingsCount.toString(),
                            style = MaterialTheme.typography.titleMedium
                        )
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
            }
        }

        val selectedTab = remember {
            mutableIntStateOf(1)
        }
        val listItems = listOf(
            TabList(1, "Followings"),
            TabList(2, "Followers"),
        )
        CustomTab(
            items = listItems,
            value = selectedTab.intValue,
            onChange = { selectedTab.intValue = it },
        )
        when (selectedTab.intValue) {
            1 -> {
                if (followings is ResponseHandlerState.Success) {
                    FollowerList(
                        navController = navController,
                        userList = (followings as ResponseHandlerState.Success<List<CreatorProfile>>).data
                    )
                }
            }

            2 -> {
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