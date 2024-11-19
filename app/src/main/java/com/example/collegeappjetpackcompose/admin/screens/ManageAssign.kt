package com.example.collegeappjetpackcompose.admin.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.collegeappjetpackcompose.itemview.AssignItemView
import com.example.collegeappjetpackcompose.viewmodel.AssignViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageAssign(navController: NavController){
    val context = LocalContext.current
    val noticeViewModel: AssignViewModel = viewModel()
    val isUploaded by noticeViewModel.isPosted.observeAsState(false)
    val bannerList by noticeViewModel.noticeList.observeAsState(emptyList())

    var fileUri by remember { mutableStateOf<Uri?>(null) }
    var fileTitle by remember { mutableStateOf("") }
    var isUploading by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        fileUri = uri
    }

    LaunchedEffect(Unit) {
        noticeViewModel.getAssignments()
    }

    LaunchedEffect(isUploaded) {
        if (isUploaded) {
            Toast.makeText(context, "Assignment Uploaded", Toast.LENGTH_SHORT).show()
            fileUri = null
            fileTitle = ""
            isUploading = false
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { isUploading = true }) {
                Icon(Icons.Default.Add, contentDescription = "Upload Assignment")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            if (isUploading) {
                OutlinedTextField(
                    value = fileTitle,
                    onValueChange = { fileTitle = it },
                    placeholder = { Text("Assignment Title") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                Button(onClick = { launcher.launch("*/*") }, modifier = Modifier.padding(8.dp)) {
                    Text("Select File")
                }

                fileUri?.let {
                    Text(
                        text = "Selected: ${it.path ?: "Unknown Path"}",
                        modifier = Modifier.padding(8.dp)
                    )
                }

                Row {
                    Button(
                        onClick = {
                            if (fileUri != null && fileTitle.isNotEmpty()) {
                                val inputStream =
                                    context.contentResolver.openInputStream(fileUri!!)
                                noticeViewModel.saveAssignment(inputStream, fileTitle)
                            } else {
                                Toast.makeText(
                                    context,
                                    "Provide title and select a file",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text("Upload")
                    }

                    OutlinedButton(
                        onClick = {
                            fileUri = null
                            isUploading = false
                        },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text("Cancel")
                    }
                }
            }

            if (bannerList.isEmpty()) {
                Text("No assignments available", modifier = Modifier.padding(16.dp))
            } else {
                LazyColumn {
                    items(bannerList) { assignment ->
                        AssignItemView(assignment, delete = { noticeViewModel.deleteAssignment(it) })
                    }
                }
            }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun AssignView() {
    ManageAssign(navController = rememberNavController())
}