package com.example.quizapp.ui.components.basic.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Article
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.quizapp.model.Course
import com.example.quizapp.ui.components.basic.avatar.CircleAvatar
import com.example.quizapp.ui.navigation.Screen


@Composable
fun CourseItemCard(navController: NavController, course: Course) {
    CustomCard(
        onClick = { navController.navigate(Screen.Course.passId(course.id)) },
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)

    ) {
        Row(modifier = Modifier.padding(10.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier.padding(bottom = 10.dp)
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = course.title,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = course.description,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
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
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {

                    Row {
                        Icon(
                            imageVector = Icons.Outlined.Article,
                            contentDescription = null,
                            modifier = Modifier.width(15.dp)
                        )
                        Text(
                            text = course.studySetCount.toString() + " Study sets",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                    Row {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = null,
                            modifier = Modifier.width(15.dp)
                        )
                        Text(
                            text = course.enrollmentsCount.toString() + " Members",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
            Icon(imageVector = Icons.Outlined.School, contentDescription = null)
        }
    }
}
