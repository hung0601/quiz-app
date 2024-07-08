package com.example.quizapp.ui.components.basic.select

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
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
import com.example.quizapp.ui.components.basic.textfield.CustomTextField

data class SelectOption<T>(
    val value: T,
    val label: String,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SelectTextField(
    modifier: Modifier = Modifier,
    options: List<SelectOption<T>>,
    value: T? = null,
    onChange: (T) -> Unit,
    placeholder: String = "",
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        var expanded by remember { mutableStateOf(false) }
        var selectedOptionLabel by remember {
            mutableStateOf(
                placeholder
            )
        }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
        ) {
            CustomTextField(
                modifier = modifier.menuAnchor(),
                readOnly = true,
                value = if (value != null) options.find { it.value == value }?.label
                    ?: placeholder else placeholder,
                onValueChange = {},
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                options.forEach {
                    DropdownMenuItem(
                        text = { Text(it.label) },
                        onClick = {
                            selectedOptionLabel = it.label
                            onChange(it.value)
                            expanded = false
                        },
                        trailingIcon = if (it.value == value) {
                            {
                                Icon(
                                    imageVector = Icons.Outlined.Check,
                                    contentDescription = null
                                )
                            }
                        } else null
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SelectTextFieldPreview() {
    val options = listOf(SelectOption(1, "Option1"), SelectOption(2, "Option2"))
    val selectedValue = remember {
        mutableIntStateOf(0)
    }
    Surface(modifier = Modifier.fillMaxSize()) {
        SelectTextField(options = options, value = selectedValue.intValue, onChange = {
            selectedValue.intValue = it
        }, placeholder = "Select topic")
    }
}