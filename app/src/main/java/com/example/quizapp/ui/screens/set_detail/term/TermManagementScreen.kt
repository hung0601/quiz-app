package com.example.quizapp.ui.screens.set_detail.term

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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material.icons.outlined.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.quizapp.R
import com.example.quizapp.model.StudySetDetail
import com.example.quizapp.model.Term
import com.example.quizapp.network.request_model.StoreTermRequest
import com.example.quizapp.network.response_model.ResponseHandlerState
import com.example.quizapp.ui.components.basic.textfield.CustomTextField
import com.example.quizapp.ui.navigation.Screen
import com.example.quizapp.ui.theme.md_theme_error
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun TermManagementScreen(
    navController: NavController,
    studySetDetail: StudySetDetail
) {
    val termManagementModel =
        hiltViewModel<TermManagementModel, TermManagementModel.TermManagementModelFactory> { factory ->
            factory.create(studySetDetail)
        }
    val terms by termManagementModel.terms.collectAsState()
    var openAddTermDialog by remember { mutableStateOf(false) }

    if (openAddTermDialog) {
        AddTermDialog(onDismissRequest = { openAddTermDialog = false },
            termManagementModel = termManagementModel,
            studySetDetail,
            onAddedTerm = { })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.onPrimary),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            IconButton(onClick = {
                navController.popBackStack(Screen.StudySet.route, true)
                navController.navigate(Screen.StudySet.passId(studySetDetail.id))
            }) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = null
                )
            }

            Text(text = "Edit terms", style = MaterialTheme.typography.titleLarge)
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(terms) {
                EditTermCard(term = it, onUpdate = { formData ->
                    termManagementModel.updateTerm(it, formData)
                }, onDelete = { termManagementModel.deleteTerm(it) })
            }
        }

        IconButton(onClick = { openAddTermDialog = true }) {
            Icon(imageVector = Icons.Outlined.Add, contentDescription = null)
        }
    }
}

@Composable
fun EditTermCard(term: Term, onUpdate: (StoreTermRequest) -> Unit, onDelete: () -> Unit) {
    val termText = remember {
        mutableStateOf(term.term)
    }
    val definition = remember {
        mutableStateOf(term.definition)
    }
    val editable = remember {
        mutableStateOf(false)
    }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }
    val context = LocalContext.current
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
        }
    val imageModifier = Modifier
        .size(70.dp)
        .clip(RoundedCornerShape(5.dp))
        .background(MaterialTheme.colorScheme.surface)
        .clickable {
            if (editable.value) {
                launcher.launch("image/*")
            }
        }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .border(
                2.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(5.dp)
            ),
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                CustomTextField(value = termText.value, onValueChange = {
                    termText.value = it
                }, enabled = editable.value)
                CustomTextField(value = definition.value, onValueChange = {
                    definition.value = it
                }, enabled = editable.value)
            }
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
                            modifier = imageModifier,
                        )
                    }
                }
            } else if (term.imageUrl != null) {
                AsyncImage(
                    model = term.imageUrl,
                    contentDescription = null,
                    modifier = imageModifier,
                    contentScale = ContentScale.Crop,
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.upload_image),
                    contentDescription = null,
                    modifier = imageModifier
                )
            }
        }


        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 5.dp, end = 3.dp)
        ) {
            if (editable.value) {
                IconButton(onClick = {
                    var file: File? = null
                    if (imageUri != null) {
                        val inputStream = context.contentResolver.openInputStream(imageUri!!)
                        file = File(context.cacheDir, "mage.png")
                        file.createNewFile()
                        file.outputStream().use {
                            inputStream!!.copyTo(it)
                        }
                    }
                    editable.value = false
                    onUpdate(StoreTermRequest(file, termText.value, definition.value))

                }, modifier = Modifier.size(30.dp)) {
                    Icon(
                        imageVector = Icons.Outlined.Save,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            } else {
                IconButton(onClick = { editable.value = true }, modifier = Modifier.size(30.dp)) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }
            IconButton(onClick = { onDelete() }, modifier = Modifier.size(30.dp)) {
                Icon(
                    imageVector = Icons.Outlined.DeleteOutline,
                    contentDescription = null,
                    tint = md_theme_error,
                )
            }

        }
    }
}


@Composable
fun AddTermDialog(
    onDismissRequest: () -> Unit,
    termManagementModel: TermManagementModel,
    studySet: StudySetDetail,
    onAddedTerm: () -> Unit
) {
    val term = remember { mutableStateOf("") }
    val definition = remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
        }

    val scope = rememberCoroutineScope()
    val addTermResponse by termManagementModel.addTermResponse.collectAsState()


    LaunchedEffect(key1 = addTermResponse is ResponseHandlerState.Success) {
        scope.launch {
            if (addTermResponse is ResponseHandlerState.Success) {
                Toast.makeText(context, "Add term successful", Toast.LENGTH_LONG).show()
                termManagementModel.resetState()
                onDismissRequest()
                onAddedTerm()
            }
        }
    }

    LaunchedEffect(key1 = addTermResponse is ResponseHandlerState.Error) {
        if (addTermResponse is ResponseHandlerState.Error) {
            scope.launch {
                Toast.makeText(
                    context,
                    (addTermResponse as ResponseHandlerState.Error).errorMsg,
                    Toast.LENGTH_LONG
                ).show()
                termManagementModel.resetState()
            }
        }
    }
    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text(text = "Add term", style = MaterialTheme.typography.titleLarge)
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
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


                CustomTextField(value = term.value, onValueChange = { term.value = it },
                    label = { Text(text = "Term") },
                    placeholder = { Text(text = "Term") })
                CustomTextField(value = definition.value,
                    onValueChange = { definition.value = it },
                    label = { Text(text = "Definition") },
                    placeholder = { Text(text = "Definition") })


            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(onClick = { onDismissRequest() }) {
                    Text(text = "Cancel")
                }
                Button(
                    onClick = {
                        var file: File? = null
                        if (imageUri != null) {
                            val inputStream = context.contentResolver.openInputStream(imageUri!!)
                            file = File(context.cacheDir, "mage.png")
                            file.createNewFile()
                            file.outputStream().use {
                                inputStream!!.copyTo(it)
                            }
                        }
                        termManagementModel.addTerm(file, term.value, definition.value)
                    },
                    enabled = term.value.isNotEmpty() && definition.value.isNotEmpty() || addTermResponse is ResponseHandlerState.Loading
                ) {
                    Text(text = "Add")
                }
            }
        }

    }
}