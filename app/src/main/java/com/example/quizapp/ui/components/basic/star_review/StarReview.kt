package com.example.quizapp.ui.components.basic.star_review

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarHalf
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun StarReview(
    modifier: Modifier = Modifier,
    star: Float,
    size: Int = 20,
    isShowText: Boolean = false,
    disable: Boolean = false,
    onChange: (Int) -> Unit = {}
) {
    val starCount = remember {
        mutableFloatStateOf(if (star > 5) 5F else roundToNearestHalf(star))
    }
    var filledCount = (starCount.floatValue).toInt()
    val tintColor = Color(0xFFFFC888)
    LaunchedEffect(star) {
        starCount.floatValue = if (star > 5) 5F else roundToNearestHalf(star)
        filledCount = (starCount.floatValue).toInt()
    }
    val itemModifier = Modifier.size(size.dp)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        modifier = modifier
    ) {
        Row {
            for (i in 1..filledCount) {
                Icon(modifier = itemModifier.clickable(!disable) {
                    onChange(i)
                }, imageVector = Icons.Outlined.Star, contentDescription = null, tint = tintColor)
            }
            if (starCount.floatValue - filledCount > 0) {
                Icon(
                    modifier = itemModifier.clickable(!disable) {
                        onChange(filledCount + 1)
                    },
                    imageVector = Icons.Outlined.StarHalf,
                    contentDescription = null,
                    tint = tintColor
                )
                for (i in filledCount + 2..5) {
                    Icon(
                        modifier = itemModifier.clickable(!disable) {
                            onChange(i)
                        },
                        imageVector = Icons.Outlined.StarOutline,
                        contentDescription = null,
                        tint = tintColor
                    )
                }
            } else {
                for (i in filledCount + 1..5) {
                    Icon(
                        modifier = itemModifier.clickable(!disable) {
                            onChange(i)
                        },
                        imageVector = Icons.Outlined.StarOutline,
                        contentDescription = null,
                        tint = tintColor
                    )
                }
            }
        }
        if (isShowText) {
            Text(text = star.toString(), style = MaterialTheme.typography.labelMedium)
        }
    }
}

fun roundToNearestHalf(number: Float): Float {
    return (Math.round(number * 2) / 2.0).toFloat()
}

@Preview(showBackground = true)
@Composable
fun StarPreview() {
    val starCount = remember {
        mutableFloatStateOf(3.5F)
    }
    Surface(modifier = Modifier.fillMaxSize()) {
        StarReview(
            star = starCount.floatValue,
            size = 40,
            isShowText = true,
            disable = false,
            onChange = { starCount.floatValue = it.toFloat() })
    }

}