package com.example.quizapp.ui.components.basic.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.quizapp.ui.components.basic.button.CustomButton

data class TabList(
    val key: Int,
    val label: String
)

@Composable
fun CustomTab(
    items: List<TabList>,
    value: Int,
    onChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        items.map {
            CustomButton(
                onClick = { onChange(it.key) },
                type = if (value == it.key) "filled" else "outline"
            ) {
                Text(text = it.label, style = MaterialTheme.typography.titleSmall)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ButtonPreview() {
    val selectedTab = remember {
        mutableIntStateOf(1)
    }
    val listItems = listOf(
        TabList(1, "Item 1"),
        TabList(2, "Item 2"),
        TabList(3, "Item 3")
    )
    CustomTab(
        items = listItems,
        value = selectedTab.intValue,
        onChange = { selectedTab.intValue = it }
    )
}