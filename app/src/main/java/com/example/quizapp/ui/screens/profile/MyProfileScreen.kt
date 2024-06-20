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
import com.example.quizapp.ui.components.basic.avatar.CircleAvatar
import com.example.quizapp.ui.components.basic.tabs.CustomTab
import com.example.quizapp.ui.components.basic.tabs.TabList
import com.example.quizapp.ui.navigation.AUTH_GRAPH_ROUTE
import com.example.quizapp.ui.screens.UserViewModel

@Composable
fun MyProfileScreen(navController: NavController) {
    val userViewModel = hiltViewModel<UserViewModel>()
    val profileViewModel = hiltViewModel<ProfileViewModel>()

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
            profileViewModel.currentUser?.let {
                Box(
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircleAvatar(
                            avatarImg = profileViewModel.currentUser.imageUrl,
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
                            text = profileViewModel.currentUser.name,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = profileViewModel.currentUser.email,
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
                Text(text = "34", style = MaterialTheme.typography.titleMedium)
                Text(text = "Study set", color = subTextColor)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "100", style = MaterialTheme.typography.titleMedium)
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
            TabList(2, "Collections"),
            TabList(3, "Followers")
        )
        CustomTab(
            items = listItems,
            value = selectedTab.intValue,
            onChange = { selectedTab.intValue = it }
        )
    }
}