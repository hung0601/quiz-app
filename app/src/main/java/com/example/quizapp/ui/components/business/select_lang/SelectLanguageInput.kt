package com.example.quizapp.ui.components.business.select_lang

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.quizapp.ui.components.basic.textfield.CustomTextField
import java.util.Locale

data class Language(val displayName: String, val code: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSelector(
    modifier: Modifier = Modifier,
    languages: List<Language> = getAllLanguages(),
    selectedLanguage: String,
    onLanguageSelected: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    if (expanded) {
        SelectLanguageDialog(
            languages = languages,
            onDismissRequest = { expanded = false },
            onSelect = {
                expanded = false
                onLanguageSelected(it.code)
            })
    }
    AssistChip(
        onClick = {
            expanded = !expanded
        },
        label = {
            Text(
                text = languages.find { it.code == selectedLanguage }?.displayName
                    ?: "Select language", style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
        },
        trailingIcon = {
            Icon(imageVector = icon, contentDescription = null)
        },
        border = AssistChipDefaults.assistChipBorder(borderColor = MaterialTheme.colorScheme.primary),
        modifier = modifier
    )
}

@Composable
fun SelectLanguageDialog(
    languages: List<Language>,
    onDismissRequest: () -> Unit,
    onSelect: (Language) -> Unit,
) {
    val search = remember { mutableStateOf("") }
    val filteredLanguages = languages.filter {
        it.displayName.contains(search.value, ignoreCase = true)
    }
    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                    .background(color = MaterialTheme.colorScheme.background)
                    .fillMaxHeight(0.85f)
                    .align(Alignment.BottomCenter)
                    .padding(8.dp)
            ) {

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = { onDismissRequest() }) {
                        Text(text = "Cancel")
                    }
                }
                CustomTextField(
                    value = search.value,
                    onValueChange = {
                        search.value = it
                    },
                    placeholder = { Text(text = "Search language") },
                )

                LazyColumn {
                    items(filteredLanguages) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                                .clickable {
                                    onSelect(it)
                                },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(
                                text = it.displayName,
                                style = MaterialTheme.typography.bodyLarge,
                            )
                            Text(
                                text = it.code,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                        Divider()
                    }
                }
            }
        }
    }
}

fun getAllLanguages(): List<Language> {
    val locales = Locale.getAvailableLocales()
    val languages = locales.filter { it.isO3Language.isNotEmpty() }
        .map { Language(it.displayLanguage, it.language) }.distinct()
    return languages
}


@Preview(showBackground = true)
@Composable
fun LanguageSelectorPreview() {
    var selectedLanguage by remember { mutableStateOf("") }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LanguageSelector(
                selectedLanguage = selectedLanguage,
                onLanguageSelected = { selectedLanguage = it }
            )
        }
    }
}

