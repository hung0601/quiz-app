package com.example.quizapp.ui.screens.set_detail.create

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quizapp.model.StudySet
import com.example.quizapp.network.response_model.ResponseHandlerState
import com.example.quizapp.ui.components.textfield.CustomTextField
import com.example.quizapp.ui.navigation.Screen
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun CreateSetScreen(navController: NavController) {
    val createSetViewModel = hiltViewModel<CreateSetViewModel>()
    val setResponse by createSetViewModel.setResponse.collectAsState()
    val title = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = setResponse is ResponseHandlerState.Success) {
        scope.launch {
            if (setResponse is ResponseHandlerState.Success) {
                Toast.makeText(context, "Create set successfully", Toast.LENGTH_LONG).show()
                navController.popBackStack()
                navController.navigate(Screen.StudySet.passId((setResponse as ResponseHandlerState.Success<StudySet>).data.id))
            }
        }
    }

    LaunchedEffect(key1 = setResponse is ResponseHandlerState.Error) {
        if (setResponse is ResponseHandlerState.Error) {
            scope.launch {
                Toast.makeText(
                    context,
                    (setResponse as ResponseHandlerState.Error).errorMsg,
                    Toast.LENGTH_LONG
                ).show()
                createSetViewModel.resetState()
            }
        }
    }

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (imageUri != null) {
            imageUri?.let {
                if (Build.VERSION.SDK_INT < 28) {
                    bitmap.value = MediaStore.Images
                        .Media.getBitmap(context.contentResolver, it)
                } else {
                    val source = ImageDecoder.createSource(context.contentResolver, it)
                    bitmap.value = ImageDecoder.decodeBitmap(source)
                }

                bitmap.value?.let { btm ->
                    Image(
                        bitmap = btm.asImageBitmap(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .clip(RoundedCornerShape(15.dp))
                            .border(
                                2.dp, MaterialTheme.colorScheme.outlineVariant,
                                RoundedCornerShape(15.dp)
                            )
                            .clickable {
                                launcher.launch("image/*")
                            },
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .background(MaterialTheme.colorScheme.surface)
                    .border(
                        2.dp, MaterialTheme.colorScheme.outlineVariant,
                        RoundedCornerShape(15.dp)
                    )
                    .clickable {
                        launcher.launch("image/*")
                    },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(imageVector = Icons.Outlined.Upload, contentDescription = null)
                Text(text = "Pick Image")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))


        CustomTextField(value = title.value, onValueChange = { title.value = it },
            label = { Text(text = "Title") },
            placeholder = { Text(text = "Title") })
        CustomTextField(value = description.value, onValueChange = { description.value = it },
            label = { Text(text = "Description") },
            placeholder = { Text(text = "Description") })

        Button(
            onClick = {
                val inputStream = context.contentResolver.openInputStream(imageUri!!)
                val file = File(context.cacheDir, "mage.png")
                file.createNewFile()
                file.outputStream().use {
                    inputStream!!.copyTo(it)
                }
                createSetViewModel.createSet(file, title.value, description.value)
            },
            enabled = imageUri != null && title.value.isNotEmpty() && description.value.isNotEmpty() || setResponse is ResponseHandlerState.Loading
        ) {
            Text(text = "Create")
        }
    }
}