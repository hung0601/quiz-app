package com.example.quizapp.ui.components.basic.select

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DynamicSelectTextField(
    modifier: Modifier = Modifier,
    selectedValue: String,
    options: List<String>,
    label: @Composable() (() -> Unit)? = null,
    onValueChangedEvent: (Int) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        Column {
            AssistChip(
                onClick = {},
                label = {
                    Text(
                        text = selectedValue, style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                shape = CircleShape,
                border = AssistChipDefaults.assistChipBorder(borderColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                options.forEachIndexed(action = { index: Int, option: String ->
                    DropdownMenuItem(
                        text = { Text(text = option) },
                        onClick = {
                            expanded = false
                            onValueChangedEvent(index)
                        }
                    )
                })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DropdownPreview() {
    val listItem = listOf("Item 1", "Item 2")
    val selectedIndex = remember {
        mutableIntStateOf(0)
    }
    Surface(modifier = Modifier.fillMaxSize()) {
        DynamicSelectTextField(
            options = listItem,
            selectedValue = listItem[selectedIndex.intValue],
            onValueChangedEvent = {},
            modifier = Modifier.width(200.dp)
        )
    }
}